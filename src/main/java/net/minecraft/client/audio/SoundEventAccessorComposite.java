package net.minecraft.client.audio;

import com.google.common.collect.Lists;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.ResourceLocation;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

@SideOnly(Side.CLIENT)
public class SoundEventAccessorComposite implements ISoundEventAccessor {
	@SuppressWarnings("rawtypes")
	private final List soundPool = Lists.newArrayList();
	private final Random rnd = new Random();
	private final ResourceLocation field_148735_c;
	private final SoundCategory field_148732_d;
	private final double eventPitch;
	private final double eventVolume;
	public SoundEventAccessorComposite(ResourceLocation p_i45120_1_, double p_i45120_2_, double p_i45120_4_,
			SoundCategory p_i45120_6_) {
		field_148735_c = p_i45120_1_;
		eventVolume = p_i45120_4_;
		eventPitch = p_i45120_2_;
		field_148732_d = p_i45120_6_;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public int func_148721_a() {
		int i = 0;
		ISoundEventAccessor isoundeventaccessor;

		for (Iterator iterator = soundPool.iterator(); iterator.hasNext(); i += isoundeventaccessor.func_148721_a()) {
			isoundeventaccessor = (ISoundEventAccessor) iterator.next();
		}

		return i;
	}

	@Override
	public SoundPoolEntry func_148720_g() {
		int i = func_148721_a();

		if (!soundPool.isEmpty() && i != 0) {
			int j = rnd.nextInt(i);
			@SuppressWarnings("rawtypes")
			Iterator iterator = soundPool.iterator();
			ISoundEventAccessor isoundeventaccessor;

			do {
				if (!iterator.hasNext())
					return SoundHandler.missing_sound;

				isoundeventaccessor = (ISoundEventAccessor) iterator.next();
				j -= isoundeventaccessor.func_148721_a();
			} while (j >= 0);

			SoundPoolEntry soundpoolentry = (SoundPoolEntry) isoundeventaccessor.func_148720_g();
			soundpoolentry.setPitch(soundpoolentry.getPitch() * eventPitch);
			soundpoolentry.setVolume(soundpoolentry.getVolume() * eventVolume);
			return soundpoolentry;
		} else
			return SoundHandler.missing_sound;
	}

	@SuppressWarnings("unchecked")
	public void addSoundToEventPool(ISoundEventAccessor p_148727_1_) {
		soundPool.add(p_148727_1_);
	}

	public ResourceLocation getSoundEventLocation() {
		return field_148735_c;
	}

	public SoundCategory getSoundCategory() {
		return field_148732_d;
	}
}