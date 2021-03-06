/*
 * The FML Forge Mod Loader suite. Copyright (C) 2012 cpw
 *
 * This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this library; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 */
package cpw.mods.fml.common;

import com.google.common.base.Function;
import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import com.google.common.collect.*;
import com.google.common.collect.ImmutableList.Builder;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.Metadata;
import cpw.mods.fml.common.discovery.ASMDataTable;
import cpw.mods.fml.common.discovery.ASMDataTable.ASMData;
import cpw.mods.fml.common.discovery.ModCandidate;
import cpw.mods.fml.common.event.FMLConstructionEvent;
import cpw.mods.fml.common.event.FMLEvent;
import cpw.mods.fml.common.event.FMLFingerprintViolationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.versioning.ArtifactVersion;
import cpw.mods.fml.common.versioning.DefaultArtifactVersion;
import cpw.mods.fml.common.versioning.VersionParser;
import cpw.mods.fml.common.versioning.VersionRange;
import org.apache.logging.log4j.Level;

import java.io.File;
import java.io.FileInputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.security.cert.Certificate;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class FMLModContainer implements ModContainer {
	private Object modInstance;
	private final File source;
	private ModMetadata modMetadata;
	private final String className;
	private final Map<String, Object> descriptor;
	private boolean enabled = true;
	private String internalVersion;
	private boolean overridesMetadata;
	private EventBus eventBus;
	private LoadController controller;
	private DefaultArtifactVersion processedVersion;

	private String annotationDependencies;
	private VersionRange minecraftAccepted;
	private boolean fingerprintNotPresent;
	private Set<String> sourceFingerprints;
	private Certificate certificate;
	private final String modLanguage;
	private final ILanguageAdapter languageAdapter;
	private Disableable disableability;
	private final ListMultimap<Class<? extends FMLEvent>, Method> eventMethods;
	private Map<String, String> customModProperties;
	private final ModCandidate candidate;

	public FMLModContainer(String className, ModCandidate container, Map<String, Object> modDescriptor) {
		this.className = className;
		source = container.getModContainer();
		candidate = container;
		descriptor = modDescriptor;
		eventMethods = ArrayListMultimap.create();

		modLanguage = (String) modDescriptor.get("modLanguage");
		String languageAdapterType = (String) modDescriptor.get("modLanguageAdapter");
		if (Strings.isNullOrEmpty(languageAdapterType)) {
			languageAdapter = "scala".equals(modLanguage) ? new ILanguageAdapter.ScalaAdapter()
					: new ILanguageAdapter.JavaAdapter();
		} else {
			try {
				languageAdapter = (ILanguageAdapter) Class
						.forName(languageAdapterType, true, Loader.instance().getModClassLoader()).newInstance();
				FMLLog.finer("Using custom language adapter %s (type %s) for %s (modid %s)", languageAdapter,
						languageAdapterType, this.className, getModId());
			} catch (Exception ex) {
				FMLLog.log(Level.ERROR, ex,
						"Error constructing custom mod language adapter %s (referenced by %s) (modid: %s)",
						languageAdapterType, this.className, getModId());
				throw new LoaderException(ex);
			}
		}
	}

	private ILanguageAdapter getLanguageAdapter() {
		return languageAdapter;
	}

	@Override
	public String getModId() {
		return (String) descriptor.get("modid");
	}

	@Override
	public String getName() {
		return modMetadata.name;
	}

	@Override
	public String getVersion() {
		return internalVersion;
	}

	@Override
	public File getSource() {
		return source;
	}

	@Override
	public ModMetadata getMetadata() {
		return modMetadata;
	}

	@Override
	public void bindMetadata(MetadataCollection mc) {
		modMetadata = mc.getMetadataForId(getModId(), descriptor);

		if (descriptor.containsKey("useMetadata")) {
			overridesMetadata = !((Boolean) descriptor.get("useMetadata")).booleanValue();
		}

		if (overridesMetadata || !modMetadata.useDependencyInformation) {
			Set<ArtifactVersion> requirements = Sets.newHashSet();
			List<ArtifactVersion> dependencies = Lists.newArrayList();
			List<ArtifactVersion> dependants = Lists.newArrayList();
			annotationDependencies = (String) descriptor.get("dependencies");
			Loader.instance().computeDependencies(annotationDependencies, requirements, dependencies, dependants);
			dependants.addAll(Loader.instance().getInjectedBefore(getModId()));
			dependencies.addAll(Loader.instance().getInjectedAfter(getModId()));
			modMetadata.requiredMods = requirements;
			modMetadata.dependencies = dependencies;
			modMetadata.dependants = dependants;
			FMLLog.log(getModId(), Level.TRACE, "Parsed dependency info : %s %s %s", requirements, dependencies,
					dependants);
		} else {
			FMLLog.log(getModId(), Level.TRACE, "Using mcmod dependency info : %s %s %s", modMetadata.requiredMods,
					modMetadata.dependencies, modMetadata.dependants);
		}
		if (Strings.isNullOrEmpty(modMetadata.name)) {
			FMLLog.log(getModId(), Level.INFO, "Mod %s is missing the required element 'name'. Substituting %s",
					getModId(), getModId());
			modMetadata.name = getModId();
		}
		internalVersion = (String) descriptor.get("version");
		if (Strings.isNullOrEmpty(internalVersion)) {
			Properties versionProps = searchForVersionProperties();
			if (versionProps != null) {
				internalVersion = versionProps.getProperty(getModId() + ".version");
				FMLLog.log(getModId(), Level.DEBUG, "Found version %s for mod %s in version.properties, using",
						internalVersion, getModId());
			}

		}
		if (Strings.isNullOrEmpty(internalVersion) && !Strings.isNullOrEmpty(modMetadata.version)) {
			FMLLog.log(getModId(), Level.WARN,
					"Mod %s is missing the required element 'version' and a version.properties file could not be found. Falling back to metadata version %s",
					getModId(), modMetadata.version);
			internalVersion = modMetadata.version;
		}
		if (Strings.isNullOrEmpty(internalVersion)) {
			FMLLog.log(getModId(), Level.WARN,
					"Mod %s is missing the required element 'version' and no fallback can be found. Substituting '1.0'.",
					getModId());
			modMetadata.version = internalVersion = "1.0";
		}

		String mcVersionString = (String) descriptor.get("acceptedMinecraftVersions");
		if (!Strings.isNullOrEmpty(mcVersionString)) {
			minecraftAccepted = VersionParser.parseRange(mcVersionString);
		} else {
			minecraftAccepted = Loader.instance().getMinecraftModContainer().getStaticVersionRange();
		}
	}

	public Properties searchForVersionProperties() {
		try {
			FMLLog.log(getModId(), Level.DEBUG,
					"Attempting to load the file version.properties from %s to locate a version number for %s",
					getSource().getName(), getModId());
			Properties version = null;
			if (getSource().isFile()) {
				ZipFile source = new ZipFile(getSource());
				ZipEntry versionFile = source.getEntry("version.properties");
				if (versionFile != null) {
					version = new Properties();
					version.load(source.getInputStream(versionFile));
				}
				source.close();
			} else if (getSource().isDirectory()) {
				File propsFile = new File(getSource(), "version.properties");
				if (propsFile.exists() && propsFile.isFile()) {
					version = new Properties();
					FileInputStream fis = new FileInputStream(propsFile);
					version.load(fis);
					fis.close();
				}
			}
			return version;
		} catch (Exception e) {
			Throwables.propagateIfPossible(e);
			FMLLog.log(getModId(), Level.TRACE, "Failed to find a usable version.properties file");
			return null;
		}
	}

	@Override
	public void setEnabledState(boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public Set<ArtifactVersion> getRequirements() {
		return modMetadata.requiredMods;
	}

	@Override
	public List<ArtifactVersion> getDependencies() {
		return modMetadata.dependencies;
	}

	@Override
	public List<ArtifactVersion> getDependants() {
		return modMetadata.dependants;
	}

	@Override
	public String getSortingRules() {
		return overridesMetadata || !modMetadata.useDependencyInformation ? Strings.nullToEmpty(annotationDependencies)
				: modMetadata.printableSortingRules();
	}

	@Override
	public boolean matches(Object mod) {
		return mod == modInstance;
	}

	@Override
	public Object getMod() {
		return modInstance;
	}

	@Override
	public boolean registerBus(EventBus bus, LoadController controller) {
		if (enabled) {
			FMLLog.log(getModId(), Level.DEBUG, "Enabling mod %s", getModId());
			eventBus = bus;
			this.controller = controller;
			eventBus.register(this);
			return true;
		} else
			return false;
	}

	@SuppressWarnings("unchecked")
	private Method gatherAnnotations(Class<?> clazz) throws Exception {
		Method factoryMethod = null;
		for (Method m : clazz.getDeclaredMethods()) {
			for (Annotation a : m.getAnnotations()) {
				if (a.annotationType().equals(Mod.EventHandler.class)) {
					if (m.getParameterTypes().length == 1
							&& FMLEvent.class.isAssignableFrom(m.getParameterTypes()[0])) {
						m.setAccessible(true);
						eventMethods.put((Class<? extends FMLEvent>) m.getParameterTypes()[0], m);
					} else {
						FMLLog.log(getModId(), Level.ERROR,
								"The mod %s appears to have an invalid event annotation %s. This annotation can only apply to methods with recognized event arguments - it will not be called",
								getModId(), a.annotationType().getSimpleName());
					}
				} else if (a.annotationType().equals(Mod.InstanceFactory.class)) {
					if (Modifier.isStatic(m.getModifiers()) && m.getParameterTypes().length == 0
							&& factoryMethod == null) {
						m.setAccessible(true);
						factoryMethod = m;
					} else if (!(Modifier.isStatic(m.getModifiers()) && m.getParameterTypes().length == 0)) {
						FMLLog.log(getModId(), Level.ERROR,
								"The InstanceFactory annotation can only apply to a static method, taking zero arguments - it will be ignored on %s(%s)",
								m.getName(), Arrays.asList(m.getParameterTypes()));
					} else if (factoryMethod != null) {
						FMLLog.log(getModId(), Level.ERROR,
								"The InstanceFactory annotation can only be used once, the application to %s(%s) will be ignored",
								m.getName(), Arrays.asList(m.getParameterTypes()));
					}
				}
			}
		}
		return factoryMethod;
	}

	private void processFieldAnnotations(ASMDataTable asmDataTable) throws Exception {
		SetMultimap<String, ASMData> annotations = asmDataTable.getAnnotationsFor(this);

		parseSimpleFieldAnnotation(annotations, Instance.class.getName(), new Function<ModContainer, Object>() {
			@Override
			public Object apply(ModContainer mc) {
				return mc.getMod();
			}
		});
		parseSimpleFieldAnnotation(annotations, Metadata.class.getName(), new Function<ModContainer, Object>() {
			@Override
			public Object apply(ModContainer mc) {
				return mc.getMetadata();
			}
		});
	}

	private void parseSimpleFieldAnnotation(SetMultimap<String, ASMData> annotations, String annotationClassName,
			Function<ModContainer, Object> retreiver) throws IllegalAccessException {
		String[] annName = annotationClassName.split("\\.");
		String annotationName = annName[annName.length - 1];
		for (ASMData targets : annotations.get(annotationClassName)) {
			String targetMod = (String) targets.getAnnotationInfo().get("value");
			Field f = null;
			Object injectedMod = null;
			ModContainer mc = this;
			boolean isStatic = false;
			Class<?> clz = modInstance.getClass();
			if (!Strings.isNullOrEmpty(targetMod)) {
				if (Loader.isModLoaded(targetMod)) {
					mc = Loader.instance().getIndexedModList().get(targetMod);
				} else {
					mc = null;
				}
			}
			if (mc != null) {
				try {
					clz = Class.forName(targets.getClassName(), true, Loader.instance().getModClassLoader());
					f = clz.getDeclaredField(targets.getObjectName());
					f.setAccessible(true);
					isStatic = Modifier.isStatic(f.getModifiers());
					injectedMod = retreiver.apply(mc);
				} catch (Exception e) {
					Throwables.propagateIfPossible(e);
					FMLLog.log(getModId(), Level.WARN, e, "Attempting to load @%s in class %s for %s and failing",
							annotationName, targets.getClassName(), mc.getModId());
				}
			}
			if (f != null) {
				Object target = null;
				if (!isStatic) {
					target = modInstance;
					if (!modInstance.getClass().equals(clz)) {
						FMLLog.log(getModId(), Level.WARN,
								"Unable to inject @%s in non-static field %s.%s for %s as it is NOT the primary mod instance",
								annotationName, targets.getClassName(), targets.getObjectName(), mc.getModId());
						continue;
					}
				}
				f.set(target, injectedMod);
			}
		}
	}

	@Subscribe
	public void constructMod(FMLConstructionEvent event) {
		try {
			ModClassLoader modClassLoader = event.getModClassLoader();
			modClassLoader.addFile(source);
			modClassLoader.clearNegativeCacheFor(candidate.getClassList());
			Class<?> clazz = Class.forName(className, true, modClassLoader);

			Certificate[] certificates = clazz.getProtectionDomain().getCodeSource().getCertificates();
			int len = 0;
			if (certificates != null) {
				len = certificates.length;
			}
			Builder<String> certBuilder = ImmutableList.builder();
			for (int i = 0; i < len; i++) {
				certBuilder.add(CertificateHelper.getFingerprint(certificates[i]));
			}

			ImmutableList<String> certList = certBuilder.build();
			sourceFingerprints = ImmutableSet.copyOf(certList);

			String expectedFingerprint = (String) descriptor.get("certificateFingerprint");

			fingerprintNotPresent = true;

			if (expectedFingerprint != null && !expectedFingerprint.isEmpty()) {
				if (!sourceFingerprints.contains(expectedFingerprint)) {
					Level warnLevel = Level.ERROR;
					if (source.isDirectory()) {
						warnLevel = Level.TRACE;
					}
					FMLLog.log(getModId(), warnLevel,
							"The mod %s is expecting signature %s for source %s, however there is no signature matching that description",
							getModId(), expectedFingerprint, source.getName());
				} else {
					certificate = certificates[certList.indexOf(expectedFingerprint)];
					fingerprintNotPresent = false;
				}
			}

			@SuppressWarnings("unchecked")
			List<Map<String, Object>> props = (List<Map<String, Object>>) descriptor.get("customProperties");
			if (props != null) {
				com.google.common.collect.ImmutableMap.Builder<String, String> builder = ImmutableMap
						.builder();
				for (Map<String, Object> p : props) {
					builder.put((String) p.get("k"), (String) p.get("v"));
				}
				customModProperties = builder.build();
			} else {
				customModProperties = EMPTY_PROPERTIES;
			}

			Boolean hasDisableableFlag = (Boolean) descriptor.get("canBeDeactivated");
			boolean hasReverseDepends = !event.getReverseDependencies().get(getModId()).isEmpty();
			if (hasDisableableFlag != null && hasDisableableFlag) {
				disableability = hasReverseDepends ? Disableable.DEPENDENCIES : Disableable.YES;
			} else {
				disableability = hasReverseDepends ? Disableable.DEPENDENCIES : Disableable.RESTART;
			}
			Method factoryMethod = gatherAnnotations(clazz);
			modInstance = getLanguageAdapter().getNewInstance(this, clazz, modClassLoader, factoryMethod);
			NetworkRegistry.INSTANCE.register(this, clazz,
					(String) (descriptor.containsKey("acceptableRemoteVersions")
							? descriptor.get("acceptableRemoteVersions")
							: null),
					event.getASMHarvestedData());
			if (fingerprintNotPresent) {
				eventBus.post(new FMLFingerprintViolationEvent(source.isDirectory(), source,
						ImmutableSet.copyOf(sourceFingerprints), expectedFingerprint));
			}
			ProxyInjector.inject(this, event.getASMHarvestedData(), FMLCommonHandler.instance().getSide(),
					getLanguageAdapter());
			processFieldAnnotations(event.getASMHarvestedData());
		} catch (Throwable e) {
			controller.errorOccurred(this, e);
		}
	}

	@Subscribe
	public void handleModStateEvent(FMLEvent event) {
		if (!eventMethods.containsKey(event.getClass()))
			return;
		try {
			for (Method m : eventMethods.get(event.getClass())) {
				m.invoke(modInstance, event);
			}
		} catch (Throwable t) {
			controller.errorOccurred(this, t);
		}
	}

	@Override
	public ArtifactVersion getProcessedVersion() {
		if (processedVersion == null) {
			processedVersion = new DefaultArtifactVersion(getModId(), getVersion());
		}
		return processedVersion;
	}

	@Override
	public boolean isImmutable() {
		return false;
	}

	@Override
	public String getDisplayVersion() {
		return modMetadata.version;
	}

	@Override
	public VersionRange acceptableMinecraftVersionRange() {
		return minecraftAccepted;
	}

	@Override
	public Certificate getSigningCertificate() {
		return certificate;
	}

	@Override
	public String toString() {
		return "FMLMod:" + getModId() + "{" + getVersion() + "}";
	}

	@Override
	public Map<String, String> getCustomModProperties() {
		return customModProperties;
	}

	@Override
	public Class<?> getCustomResourcePackClass() {
		try {
			return getSource().isDirectory()
					? Class.forName("cpw.mods.fml.client.FMLFolderResourcePack", true, getClass().getClassLoader())
					: Class.forName("cpw.mods.fml.client.FMLFileResourcePack", true, getClass().getClassLoader());
		} catch (ClassNotFoundException e) {
			return null;
		}
	}

	@Override
	public Map<String, String> getSharedModDescriptor() {
		Map<String, String> descriptor = Maps.newHashMap();
		descriptor.put("modsystem", "FML");
		descriptor.put("id", getModId());
		descriptor.put("version", getDisplayVersion());
		descriptor.put("name", getName());
		descriptor.put("url", modMetadata.url);
		descriptor.put("authors", modMetadata.getAuthorList());
		descriptor.put("description", modMetadata.description);
		return descriptor;
	}

	@Override
	public Disableable canBeDisabled() {
		return disableability;
	}

	@Override
	public String getGuiClassName() {
		return (String) descriptor.get("guiFactory");
	}

	@Override
	public List<String> getOwnedPackages() {
		return candidate.getContainedPackages();
	}
}