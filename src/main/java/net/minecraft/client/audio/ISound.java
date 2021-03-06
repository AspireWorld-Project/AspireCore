package net.minecraft.client.audio;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public interface ISound {
	ResourceLocation getPositionedSoundLocation();

	boolean canRepeat();

	int getRepeatDelay();

	float getVolume();

	float getPitch();

	float getXPosF();

	float getYPosF();

	float getZPosF();

	ISound.AttenuationType getAttenuationType();

	@SideOnly(Side.CLIENT)
    enum AttenuationType {
		NONE(0), LINEAR(2);
		private final int field_148589_c;

		AttenuationType(int p_i45110_3_) {
			field_148589_c = p_i45110_3_;
		}

		public int getTypeInt() {
			return field_148589_c;
		}
	}
}