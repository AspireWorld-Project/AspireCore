package net.minecraft.client.audio;

import com.google.common.collect.*;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.event.sound.PlaySoundSourceEvent;
import net.minecraftforge.client.event.sound.PlayStreamingSourceEvent;
import net.minecraftforge.client.event.sound.SoundLoadEvent;
import net.minecraftforge.client.event.sound.SoundSetupEvent;
import net.minecraftforge.common.MinecraftForge;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import paulscode.sound.SoundSystem;
import paulscode.sound.SoundSystemConfig;
import paulscode.sound.SoundSystemException;
import paulscode.sound.Source;
import paulscode.sound.codecs.CodecJOrbis;
import paulscode.sound.libraries.LibraryLWJGLOpenAL;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

@SideOnly(Side.CLIENT)
public class SoundManager {
	private static final Marker field_148623_a = MarkerManager.getMarker("SOUNDS");
	private static final Logger logger = LogManager.getLogger();
	public final SoundHandler sndHandler;
	private final GameSettings options;
	private SoundManager.SoundSystemStarterThread sndSystem;
	private boolean loaded;
	private int playTime = 0;
	private final Map playingSounds = HashBiMap.create();
	private final Map invPlayingSounds;
	private final Map playingSoundPoolEntries;
	private final Multimap categorySounds;
	private final List tickableSounds;
	private final Map delayedSounds;
	private final Map playingSoundsStopTime;
	private static final String __OBFID = "CL_00001141";

	public SoundManager(SoundHandler p_i45119_1_, GameSettings p_i45119_2_) {
		invPlayingSounds = ((BiMap) playingSounds).inverse();
		playingSoundPoolEntries = Maps.newHashMap();
		categorySounds = HashMultimap.create();
		tickableSounds = Lists.newArrayList();
		delayedSounds = Maps.newHashMap();
		playingSoundsStopTime = Maps.newHashMap();
		sndHandler = p_i45119_1_;
		options = p_i45119_2_;

		try {
			SoundSystemConfig.addLibrary(LibraryLWJGLOpenAL.class);
			SoundSystemConfig.setCodec("ogg", CodecJOrbis.class);
			MinecraftForge.EVENT_BUS.post(new SoundSetupEvent(this));
		} catch (SoundSystemException soundsystemexception) {
			logger.error(field_148623_a, "Error linking with the LibraryJavaSound plug-in", soundsystemexception);
		}
	}

	public void reloadSoundSystem() {
		unloadSoundSystem();
		loadSoundSystem();
		MinecraftForge.EVENT_BUS.post(new SoundLoadEvent(this));
	}

	private synchronized void loadSoundSystem() {
		if (!loaded) {
			try {
				new Thread(new Runnable() {
					private static final String __OBFID = "CL_00001142";

					@Override
					public void run() {
						sndSystem = SoundManager.this.new SoundSystemStarterThread(null);
						loaded = true;
						sndSystem.setMasterVolume(options.getSoundLevel(SoundCategory.MASTER));
						SoundManager.logger.info(SoundManager.field_148623_a, "Sound engine started");
					}
				}, "Sound Library Loader").start();
			} catch (RuntimeException runtimeexception) {
				logger.error(field_148623_a, "Error starting SoundSystem. Turning off sounds & music",
						runtimeexception);
				options.setSoundLevel(SoundCategory.MASTER, 0.0F);
				options.saveOptions();
			}
		}
	}

	private float getSoundCategoryVolume(SoundCategory p_148595_1_) {
		return p_148595_1_ != null && p_148595_1_ != SoundCategory.MASTER ? options.getSoundLevel(p_148595_1_) : 1.0F;
	}

	public void setSoundCategoryVolume(SoundCategory p_148601_1_, float p_148601_2_) {
		if (loaded) {
			if (p_148601_1_ == SoundCategory.MASTER) {
				sndSystem.setMasterVolume(p_148601_2_);
			} else {
				Iterator iterator = categorySounds.get(p_148601_1_).iterator();

				while (iterator.hasNext()) {
					String s = (String) iterator.next();
					ISound isound = (ISound) playingSounds.get(s);
					float f1 = getNormalizedVolume(isound, (SoundPoolEntry) playingSoundPoolEntries.get(isound),
							p_148601_1_);

					if (f1 <= 0.0F) {
						stopSound(isound);
					} else {
						sndSystem.setVolume(s, f1);
					}
				}
			}
		}
	}

	public void unloadSoundSystem() {
		if (loaded) {
			stopAllSounds();
			sndSystem.cleanup();
			loaded = false;
		}
	}

	public void stopAllSounds() {
		if (loaded) {
			Iterator iterator = playingSounds.keySet().iterator();

			while (iterator.hasNext()) {
				String s = (String) iterator.next();
				sndSystem.stop(s);
			}

			playingSounds.clear();
			delayedSounds.clear();
			tickableSounds.clear();
			categorySounds.clear();
			playingSoundPoolEntries.clear();
			playingSoundsStopTime.clear();
		}
	}

	public void updateAllSounds() {
		++playTime;
		Iterator iterator = tickableSounds.iterator();
		String s;

		while (iterator.hasNext()) {
			ITickableSound itickablesound = (ITickableSound) iterator.next();
			itickablesound.update();

			if (itickablesound.isDonePlaying()) {
				stopSound(itickablesound);
			} else {
				s = (String) invPlayingSounds.get(itickablesound);
				sndSystem.setVolume(s,
						getNormalizedVolume(itickablesound,
								(SoundPoolEntry) playingSoundPoolEntries.get(itickablesound),
								sndHandler.getSound(itickablesound.getPositionedSoundLocation()).getSoundCategory()));
				sndSystem.setPitch(s, getNormalizedPitch(itickablesound,
						(SoundPoolEntry) playingSoundPoolEntries.get(itickablesound)));
				sndSystem.setPosition(s, itickablesound.getXPosF(), itickablesound.getYPosF(),
						itickablesound.getZPosF());
			}
		}

		iterator = playingSounds.entrySet().iterator();
		ISound isound;

		while (iterator.hasNext()) {
			Entry entry = (Entry) iterator.next();
			s = (String) entry.getKey();
			isound = (ISound) entry.getValue();

			if (!sndSystem.playing(s)) {
				int i = ((Integer) playingSoundsStopTime.get(s)).intValue();

				if (i <= playTime) {
					int j = isound.getRepeatDelay();

					if (isound.canRepeat() && j > 0) {
						delayedSounds.put(isound, Integer.valueOf(playTime + j));
					}

					iterator.remove();
					logger.debug(field_148623_a, "Removed channel {} because it's not playing anymore",
							s);
					sndSystem.removeSource(s);
					playingSoundsStopTime.remove(s);
					playingSoundPoolEntries.remove(isound);

					try {
						categorySounds
								.remove(sndHandler.getSound(isound.getPositionedSoundLocation()).getSoundCategory(), s);
					} catch (RuntimeException runtimeexception) {
					}

					if (isound instanceof ITickableSound) {
						tickableSounds.remove(isound);
					}
				}
			}
		}

		Iterator iterator1 = delayedSounds.entrySet().iterator();

		while (iterator1.hasNext()) {
			Entry entry1 = (Entry) iterator1.next();

			if (playTime >= ((Integer) entry1.getValue()).intValue()) {
				isound = (ISound) entry1.getKey();

				if (isound instanceof ITickableSound) {
					((ITickableSound) isound).update();
				}

				playSound(isound);
				iterator1.remove();
			}
		}
	}

	public boolean isSoundPlaying(ISound p_148597_1_) {
		if (!loaded)
			return false;
		else {
			String s = (String) invPlayingSounds.get(p_148597_1_);
			return s != null && (sndSystem.playing(s) || playingSoundsStopTime.containsKey(s)
					&& ((Integer) playingSoundsStopTime.get(s)).intValue() <= playTime);
		}
	}

	public void stopSound(ISound p_148602_1_) {
		if (loaded) {
			String s = (String) invPlayingSounds.get(p_148602_1_);

			if (s != null) {
				sndSystem.stop(s);
			}
		}
	}

	public void playSound(ISound p_148611_1_) {
		if (loaded) {
			if (sndSystem.getMasterVolume() <= 0.0F) {
				logger.debug(field_148623_a, "Skipped playing soundEvent: {}, master volume was zero",
						p_148611_1_.getPositionedSoundLocation());
			} else {
				p_148611_1_ = ForgeHooksClient.playSound(this, p_148611_1_);
				if (p_148611_1_ == null)
					return;

				SoundEventAccessorComposite soundeventaccessorcomposite = sndHandler
						.getSound(p_148611_1_.getPositionedSoundLocation());

				if (soundeventaccessorcomposite == null) {
					logger.warn(field_148623_a, "Unable to play unknown soundEvent: {}",
							p_148611_1_.getPositionedSoundLocation());
				} else {
					SoundPoolEntry soundpoolentry = soundeventaccessorcomposite.func_148720_g();

					if (soundpoolentry == SoundHandler.missing_sound) {
						logger.warn(field_148623_a, "Unable to play empty soundEvent: {}",
								soundeventaccessorcomposite.getSoundEventLocation());
					} else {
						float f = p_148611_1_.getVolume();
						float f1 = 16.0F;

						if (f > 1.0F) {
							f1 *= f;
						}

						SoundCategory soundcategory = soundeventaccessorcomposite.getSoundCategory();
						float f2 = getNormalizedVolume(p_148611_1_, soundpoolentry, soundcategory);
						double d0 = getNormalizedPitch(p_148611_1_, soundpoolentry);
						ResourceLocation resourcelocation = soundpoolentry.getSoundPoolEntryLocation();

						if (f2 == 0.0F) {
							logger.debug(field_148623_a, "Skipped playing sound {}, volume was zero.",
									resourcelocation);
						} else {
							boolean flag = p_148611_1_.canRepeat() && p_148611_1_.getRepeatDelay() == 0;
							String s = UUID.randomUUID().toString();

							if (soundpoolentry.func_148648_d()) {
								sndSystem.newStreamingSource(false, s, getURLForSoundResource(resourcelocation),
										resourcelocation.toString(), flag, p_148611_1_.getXPosF(),
										p_148611_1_.getYPosF(), p_148611_1_.getZPosF(),
										p_148611_1_.getAttenuationType().getTypeInt(), f1);
								MinecraftForge.EVENT_BUS.post(new PlayStreamingSourceEvent(this, p_148611_1_, s));
							} else {
								sndSystem.newSource(false, s, getURLForSoundResource(resourcelocation),
										resourcelocation.toString(), flag, p_148611_1_.getXPosF(),
										p_148611_1_.getYPosF(), p_148611_1_.getZPosF(),
										p_148611_1_.getAttenuationType().getTypeInt(), f1);
								MinecraftForge.EVENT_BUS.post(new PlaySoundSourceEvent(this, p_148611_1_, s));
							}

							logger.debug(field_148623_a, "Playing sound {} for event {} as channel {}",
									soundpoolentry.getSoundPoolEntryLocation(),
									soundeventaccessorcomposite.getSoundEventLocation(), s);
							sndSystem.setPitch(s, (float) d0);
							sndSystem.setVolume(s, f2);
							sndSystem.play(s);
							playingSoundsStopTime.put(s, Integer.valueOf(playTime + 20));
							playingSounds.put(s, p_148611_1_);
							playingSoundPoolEntries.put(p_148611_1_, soundpoolentry);

							if (soundcategory != SoundCategory.MASTER) {
								categorySounds.put(soundcategory, s);
							}

							if (p_148611_1_ instanceof ITickableSound) {
								tickableSounds.add(p_148611_1_);
							}
						}
					}
				}
			}
		}
	}

	private float getNormalizedPitch(ISound p_148606_1_, SoundPoolEntry p_148606_2_) {
		return (float) MathHelper.clamp_double(p_148606_1_.getPitch() * p_148606_2_.getPitch(), 0.5D, 2.0D);
	}

	private float getNormalizedVolume(ISound p_148594_1_, SoundPoolEntry p_148594_2_, SoundCategory p_148594_3_) {
		return (float) MathHelper.clamp_double(
				p_148594_1_.getVolume() * p_148594_2_.getVolume() * getSoundCategoryVolume(p_148594_3_), 0.0D, 1.0D);
	}

	public void pauseAllSounds() {
		Iterator iterator = playingSounds.keySet().iterator();

		while (iterator.hasNext()) {
			String s = (String) iterator.next();
			logger.debug(field_148623_a, "Pausing channel {}", s);
			sndSystem.pause(s);
		}
	}

	public void resumeAllSounds() {
		Iterator iterator = playingSounds.keySet().iterator();

		while (iterator.hasNext()) {
			String s = (String) iterator.next();
			logger.debug(field_148623_a, "Resuming channel {}", s);
			sndSystem.play(s);
		}
	}

	public void addDelayedSound(ISound p_148599_1_, int p_148599_2_) {
		delayedSounds.put(p_148599_1_, Integer.valueOf(playTime + p_148599_2_));
	}

	private static URL getURLForSoundResource(final ResourceLocation p_148612_0_) {
		String s = String.format("%s:%s:%s",
				"mcsounddomain", p_148612_0_.getResourceDomain(), p_148612_0_.getResourcePath());
		URLStreamHandler urlstreamhandler = new URLStreamHandler() {
			private static final String __OBFID = "CL_00001143";

			@Override
			protected URLConnection openConnection(final URL p_openConnection_1_) {
				return new URLConnection(p_openConnection_1_) {
					private static final String __OBFID = "CL_00001144";

					@Override
					public void connect() {
					}

					@Override
					public InputStream getInputStream() throws IOException {
						return Minecraft.getMinecraft().getResourceManager().getResource(p_148612_0_).getInputStream();
					}
				};
			}
		};

		try {
			return new URL(null, s, urlstreamhandler);
		} catch (MalformedURLException malformedurlexception) {
			throw new Error("TODO: Sanely handle url exception! :D");
		}
	}

	public void setListener(EntityPlayer p_148615_1_, float p_148615_2_) {
		if (loaded && p_148615_1_ != null) {
			float f1 = p_148615_1_.prevRotationPitch
					+ (p_148615_1_.rotationPitch - p_148615_1_.prevRotationPitch) * p_148615_2_;
			float f2 = p_148615_1_.prevRotationYaw
					+ (p_148615_1_.rotationYaw - p_148615_1_.prevRotationYaw) * p_148615_2_;
			double d0 = p_148615_1_.prevPosX + (p_148615_1_.posX - p_148615_1_.prevPosX) * p_148615_2_;
			double d1 = p_148615_1_.prevPosY + (p_148615_1_.posY - p_148615_1_.prevPosY) * p_148615_2_;
			double d2 = p_148615_1_.prevPosZ + (p_148615_1_.posZ - p_148615_1_.prevPosZ) * p_148615_2_;
			float f3 = MathHelper.cos((f2 + 90.0F) * 0.017453292F);
			float f4 = MathHelper.sin((f2 + 90.0F) * 0.017453292F);
			float f5 = MathHelper.cos(-f1 * 0.017453292F);
			float f6 = MathHelper.sin(-f1 * 0.017453292F);
			float f7 = MathHelper.cos((-f1 + 90.0F) * 0.017453292F);
			float f8 = MathHelper.sin((-f1 + 90.0F) * 0.017453292F);
			float f9 = f3 * f5;
			float f10 = f4 * f5;
			float f11 = f3 * f7;
			float f12 = f4 * f7;
			sndSystem.setListenerPosition((float) d0, (float) d1, (float) d2);
			sndSystem.setListenerOrientation(f9, f6, f10, f11, f8, f12);
		}
	}

	@SideOnly(Side.CLIENT)
	class SoundSystemStarterThread extends SoundSystem {
		private static final String __OBFID = "CL_00001145";

		private SoundSystemStarterThread() {
		}

		@Override
		public boolean playing(String p_playing_1_) {
			synchronized (SoundSystemConfig.THREAD_SYNC) {
				if (soundLibrary == null)
					return false;
				else {
					Source source = soundLibrary.getSources().get(p_playing_1_);
					return source != null && (source.playing() || source.paused() || source.preLoad);
				}
			}
		}

		SoundSystemStarterThread(Object p_i45118_2_) {
			this();
		}
	}
}