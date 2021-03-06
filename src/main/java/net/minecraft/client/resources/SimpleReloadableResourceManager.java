package net.minecraft.client.resources;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.resources.data.IMetadataSerializer;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SideOnly(Side.CLIENT)
public class SimpleReloadableResourceManager implements IReloadableResourceManager {
	private static final Logger logger = LogManager.getLogger();
	private static final Joiner joinerResourcePacks = Joiner.on(", ");
	private final Map domainResourceManagers = Maps.newHashMap();
	private final List reloadListeners = Lists.newArrayList();
	private final Set setResourceDomains = Sets.newLinkedHashSet();
	private final IMetadataSerializer rmMetadataSerializer;
	private static final String __OBFID = "CL_00001091";

	public SimpleReloadableResourceManager(IMetadataSerializer p_i1299_1_) {
		rmMetadataSerializer = p_i1299_1_;
	}

	public void reloadResourcePack(IResourcePack p_110545_1_) {
		FallbackResourceManager fallbackresourcemanager;

		for (Iterator iterator = p_110545_1_.getResourceDomains().iterator(); iterator
				.hasNext(); fallbackresourcemanager.addResourcePack(p_110545_1_)) {
			String s = (String) iterator.next();
			setResourceDomains.add(s);
			fallbackresourcemanager = (FallbackResourceManager) domainResourceManagers.get(s);

			if (fallbackresourcemanager == null) {
				fallbackresourcemanager = new FallbackResourceManager(rmMetadataSerializer);
				domainResourceManagers.put(s, fallbackresourcemanager);
			}
		}
	}

	@Override
	public Set getResourceDomains() {
		return setResourceDomains;
	}

	@Override
	public IResource getResource(ResourceLocation p_110536_1_) throws IOException {
		IResourceManager iresourcemanager = (IResourceManager) domainResourceManagers
				.get(p_110536_1_.getResourceDomain());

		if (iresourcemanager != null)
			return iresourcemanager.getResource(p_110536_1_);
		else
			throw new FileNotFoundException(p_110536_1_.toString());
	}

	@Override
	public List getAllResources(ResourceLocation p_135056_1_) throws IOException {
		IResourceManager iresourcemanager = (IResourceManager) domainResourceManagers
				.get(p_135056_1_.getResourceDomain());

		if (iresourcemanager != null)
			return iresourcemanager.getAllResources(p_135056_1_);
		else
			throw new FileNotFoundException(p_135056_1_.toString());
	}

	private void clearResources() {
		domainResourceManagers.clear();
		setResourceDomains.clear();
	}

	@Override
	public void reloadResources(List p_110541_1_) {
		clearResources();
		cpw.mods.fml.common.ProgressManager.ProgressBar resReload = cpw.mods.fml.common.ProgressManager
				.push("Loading Resources", p_110541_1_.size() + 1, true);
		logger.info("Reloading ResourceManager: "
				+ joinerResourcePacks.join(Iterables.transform(p_110541_1_, new Function() {
					private static final String __OBFID = "CL_00001092";

					public String apply(IResourcePack p_apply_1_) {
						return p_apply_1_.getPackName();
					}

					@Override
					public Object apply(Object p_apply_1_) {
						return this.apply((IResourcePack) p_apply_1_);
					}
				})));
		Iterator iterator = p_110541_1_.iterator();

		while (iterator.hasNext()) {
			IResourcePack iresourcepack = (IResourcePack) iterator.next();
			resReload.step(iresourcepack.getPackName());
			reloadResourcePack(iresourcepack);
		}

		resReload.step("Reloading listeners");
		notifyReloadListeners();
		cpw.mods.fml.common.ProgressManager.pop(resReload);
	}

	@Override
	public void registerReloadListener(IResourceManagerReloadListener p_110542_1_) {
		reloadListeners.add(p_110542_1_);
		cpw.mods.fml.common.ProgressManager.ProgressBar resReload = cpw.mods.fml.common.ProgressManager
				.push("Loading Resource", 1);
		resReload.step(p_110542_1_.getClass());
		p_110542_1_.onResourceManagerReload(this);
		cpw.mods.fml.common.ProgressManager.pop(resReload);
	}

	private void notifyReloadListeners() {
		Iterator iterator = reloadListeners.iterator();

		cpw.mods.fml.common.ProgressManager.ProgressBar resReload = cpw.mods.fml.common.ProgressManager
				.push("Reloading", reloadListeners.size());
		while (iterator.hasNext()) {
			IResourceManagerReloadListener iresourcemanagerreloadlistener = (IResourceManagerReloadListener) iterator
					.next();
			resReload.step(iresourcemanagerreloadlistener.getClass());
			iresourcemanagerreloadlistener.onResourceManagerReload(this);
		}
		cpw.mods.fml.common.ProgressManager.pop(resReload);
	}
}