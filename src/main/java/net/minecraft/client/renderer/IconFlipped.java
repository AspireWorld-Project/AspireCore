package net.minecraft.client.renderer;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.IIcon;

@SideOnly(Side.CLIENT)
public class IconFlipped implements IIcon {
	private final IIcon baseIcon;
	private final boolean flipU;
	private final boolean flipV;
	private static final String __OBFID = "CL_00001511";

	public IconFlipped(IIcon p_i1560_1_, boolean p_i1560_2_, boolean p_i1560_3_) {
		baseIcon = p_i1560_1_;
		flipU = p_i1560_2_;
		flipV = p_i1560_3_;
	}

	@Override
	public int getIconWidth() {
		return baseIcon.getIconWidth();
	}

	@Override
	public int getIconHeight() {
		return baseIcon.getIconHeight();
	}

	@Override
	public float getMinU() {
		return flipU ? baseIcon.getMaxU() : baseIcon.getMinU();
	}

	@Override
	public float getMaxU() {
		return flipU ? baseIcon.getMinU() : baseIcon.getMaxU();
	}

	@Override
	public float getInterpolatedU(double p_94214_1_) {
		float f = getMaxU() - getMinU();
		return getMinU() + f * ((float) p_94214_1_ / 16.0F);
	}

	@Override
	public float getMinV() {
		return flipV ? baseIcon.getMinV() : baseIcon.getMinV();
	}

	@Override
	public float getMaxV() {
		return flipV ? baseIcon.getMinV() : baseIcon.getMaxV();
	}

	@Override
	public float getInterpolatedV(double p_94207_1_) {
		float f = getMaxV() - getMinV();
		return getMinV() + f * ((float) p_94207_1_ / 16.0F);
	}

	@Override
	public String getIconName() {
		return baseIcon.getIconName();
	}
}