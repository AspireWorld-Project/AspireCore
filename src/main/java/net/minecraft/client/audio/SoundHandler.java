package net.minecraft.client.audio;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.Map.Entry;

@SideOnly(Side.CLIENT)
public class SoundHandler implements IResourceManagerReloadListener, IUpdatePlayerListBox {
	private static final Logger logger = LogManager.getLogger();
	private static final Gson field_147699_c = new GsonBuilder()
			.registerTypeAdapter(SoundList.class, new SoundListSerializer()).create();
	private static final ParameterizedType field_147696_d = new ParameterizedType() {
		@Override
		public Type[] getActualTypeArguments() {
			return new Type[] { String.class, SoundList.class };
		}

		@Override
		public Type getRawType() {
			return Map.class;
		}

		@Override
		public Type getOwnerType() {
			return null;
		}
	};
	public static final SoundPoolEntry missing_sound = new SoundPoolEntry(new ResourceLocation("meta:missing_sound"),
			0.0D, 0.0D, false);
	private final SoundRegistry sndRegistry = new SoundRegistry();
	private final SoundManager sndManager;
	private final IResourceManager mcResourceManager;
	public SoundHandler(IResourceManager p_i45122_1_, GameSettings p_i45122_2_) {
		mcResourceManager = p_i45122_1_;
		sndManager = new SoundManager(this, p_i45122_2_);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void onResourceManagerReload(IResourceManager p_110549_1_) {
		sndManager.reloadSoundSystem();
		sndRegistry.func_148763_c();
		Iterator iterator = p_110549_1_.getResourceDomains().iterator();

		while (iterator.hasNext()) {
			String s = (String) iterator.next();

			try {
				List list = p_110549_1_.getAllResources(new ResourceLocation(s, "sounds.json"));
				Iterator iterator1 = list.iterator();

				while (iterator1.hasNext()) {
					IResource iresource = (IResource) iterator1.next();

					try {
						Map map = field_147699_c.fromJson(new InputStreamReader(iresource.getInputStream()),
								field_147696_d);
						Iterator iterator2 = map.entrySet().iterator();

						while (iterator2.hasNext()) {
							Entry entry = (Entry) iterator2.next();
							loadSoundResource(new ResourceLocation(s, (String) entry.getKey()),
									(SoundList) entry.getValue());
						}
					} catch (RuntimeException runtimeexception) {
						logger.warn("Invalid sounds.json", runtimeexception);
					}
				}
			} catch (IOException ioexception) {
			}
		}
	}

	@SuppressWarnings("rawtypes")
	private void loadSoundResource(ResourceLocation p_147693_1_, SoundList p_147693_2_) {
		SoundEventAccessorComposite soundeventaccessorcomposite;

		if (sndRegistry.containsKey(p_147693_1_) && !p_147693_2_.canReplaceExisting()) {
			soundeventaccessorcomposite = (SoundEventAccessorComposite) sndRegistry.getObject(p_147693_1_);
		} else {
			logger.debug("Registered/replaced new sound event location {}", p_147693_1_);
			soundeventaccessorcomposite = new SoundEventAccessorComposite(p_147693_1_, 1.0D, 1.0D,
					p_147693_2_.getSoundCategory());
			sndRegistry.registerSound(soundeventaccessorcomposite);
		}

		Iterator iterator = p_147693_2_.getSoundList().iterator();

		while (iterator.hasNext()) {
			final SoundList.SoundEntry soundentry = (SoundList.SoundEntry) iterator.next();
			String s = soundentry.getSoundEntryName();
			ResourceLocation resourcelocation1 = new ResourceLocation(s);
			final String s1 = s.contains(":") ? resourcelocation1.getResourceDomain() : p_147693_1_.getResourceDomain();
			Object object;

			switch (SoundHandler.SwitchType.field_148765_a[soundentry.getSoundEntryType().ordinal()]) {
			case 1:
				ResourceLocation resourcelocation2 = new ResourceLocation(s1,
						"sounds/" + resourcelocation1.getResourcePath() + ".ogg");

				try {
					mcResourceManager.getResource(resourcelocation2);
				} catch (FileNotFoundException filenotfoundexception) {
					logger.warn("File {} does not exist, cannot add it to event {}",
							resourcelocation2, p_147693_1_);
					continue;
				} catch (IOException ioexception) {
					logger.warn("Could not load sound file " + resourcelocation2 + ", cannot add it to event "
							+ p_147693_1_, ioexception);
					continue;
				}

				object = new SoundEventAccessor(
						new SoundPoolEntry(resourcelocation2, soundentry.getSoundEntryPitch(),
								soundentry.getSoundEntryVolume(), soundentry.isStreaming()),
						soundentry.getSoundEntryWeight());
				break;
			case 2:
				object = new ISoundEventAccessor() {
					final ResourceLocation field_148726_a = new ResourceLocation(s1, soundentry.getSoundEntryName());
					@Override
					public int func_148721_a() {
						SoundEventAccessorComposite soundeventaccessorcomposite1 = (SoundEventAccessorComposite) sndRegistry
								.getObject(field_148726_a);
						return soundeventaccessorcomposite1 == null ? 0 : soundeventaccessorcomposite1.func_148721_a();
					}

					@Override
					public SoundPoolEntry func_148720_g() {
						SoundEventAccessorComposite soundeventaccessorcomposite1 = (SoundEventAccessorComposite) sndRegistry
								.getObject(field_148726_a);
						return soundeventaccessorcomposite1 == null ? SoundHandler.missing_sound
								: soundeventaccessorcomposite1.func_148720_g();
					}
				};
				break;
			default:
				throw new IllegalStateException("IN YOU FACE");
			}

			soundeventaccessorcomposite.addSoundToEventPool((ISoundEventAccessor) object);
		}
	}

	public SoundEventAccessorComposite getSound(ResourceLocation p_147680_1_) {
		return (SoundEventAccessorComposite) sndRegistry.getObject(p_147680_1_);
	}

	public void playSound(ISound p_147682_1_) {
		sndManager.playSound(p_147682_1_);
	}

	public void playDelayedSound(ISound p_147681_1_, int p_147681_2_) {
		sndManager.addDelayedSound(p_147681_1_, p_147681_2_);
	}

	public void setListener(EntityPlayer p_147691_1_, float p_147691_2_) {
		sndManager.setListener(p_147691_1_, p_147691_2_);
	}

	public void pauseSounds() {
		sndManager.pauseAllSounds();
	}

	public void stopSounds() {
		sndManager.stopAllSounds();
	}

	public void unloadSounds() {
		sndManager.unloadSoundSystem();
	}

	@Override
	public void update() {
		sndManager.updateAllSounds();
	}

	public void resumeSounds() {
		sndManager.resumeAllSounds();
	}

	public void setSoundLevel(SoundCategory p_147684_1_, float p_147684_2_) {
		if (p_147684_1_ == SoundCategory.MASTER && p_147684_2_ <= 0.0F) {
			stopSounds();
		}

		sndManager.setSoundCategoryVolume(p_147684_1_, p_147684_2_);
	}

	public void stopSound(ISound p_147683_1_) {
		sndManager.stopSound(p_147683_1_);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public SoundEventAccessorComposite getRandomSoundFromCategories(SoundCategory... p_147686_1_) {
		ArrayList arraylist = Lists.newArrayList();
		Iterator iterator = sndRegistry.getKeys().iterator();

		while (iterator.hasNext()) {
			ResourceLocation resourcelocation = (ResourceLocation) iterator.next();
			SoundEventAccessorComposite soundeventaccessorcomposite = (SoundEventAccessorComposite) sndRegistry
					.getObject(resourcelocation);

			if (ArrayUtils.contains(p_147686_1_, soundeventaccessorcomposite.getSoundCategory())) {
				arraylist.add(soundeventaccessorcomposite);
			}
		}

		if (arraylist.isEmpty())
			return null;
		else
			return (SoundEventAccessorComposite) arraylist.get(new Random().nextInt(arraylist.size()));
	}

	public boolean isSoundPlaying(ISound p_147692_1_) {
		return sndManager.isSoundPlaying(p_147692_1_);
	}

	@SideOnly(Side.CLIENT)

	static final class SwitchType {
		static final int[] field_148765_a = new int[SoundList.SoundEntry.Type.values().length];
		static {
			try {
				field_148765_a[SoundList.SoundEntry.Type.FILE.ordinal()] = 1;
			} catch (NoSuchFieldError var2) {
			}

			try {
				field_148765_a[SoundList.SoundEntry.Type.SOUND_EVENT.ordinal()] = 2;
			} catch (NoSuchFieldError var1) {
			}
		}
	}
}