package net.minecraft.client.audio;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public abstract class PositionedSound implements ISound {
	protected final ResourceLocation field_147664_a;
	protected float volume = 1.0F;
	protected float field_147663_c = 1.0F;
	protected float xPosF;
	protected float yPosF;
	protected float zPosF;
	protected boolean repeat = false;
	protected int field_147665_h = 0;
	protected ISound.AttenuationType field_147666_i;
	protected PositionedSound(ResourceLocation p_i45103_1_) {
		field_147666_i = ISound.AttenuationType.LINEAR;
		field_147664_a = p_i45103_1_;
	}

	@Override
	public ResourceLocation getPositionedSoundLocation() {
		return field_147664_a;
	}

	@Override
	public boolean canRepeat() {
		return repeat;
	}

	@Override
	public int getRepeatDelay() {
		return field_147665_h;
	}

	@Override
	public float getVolume() {
		return volume;
	}

	@Override
	public float getPitch() {
		return field_147663_c;
	}

	@Override
	public float getXPosF() {
		return xPosF;
	}

	@Override
	public float getYPosF() {
		return yPosF;
	}

	@Override
	public float getZPosF() {
		return zPosF;
	}

	@Override
	public ISound.AttenuationType getAttenuationType() {
		return field_147666_i;
	}
}