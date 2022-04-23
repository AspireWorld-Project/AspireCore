package net.minecraft.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.util.MathHelper;

@SideOnly(Side.CLIENT)
public class ScaledResolution {
	private int scaledWidth;
	private int scaledHeight;
	private final double scaledWidthD;
	private final double scaledHeightD;
	private int scaleFactor;
	private static final String __OBFID = "CL_00000666";

	public ScaledResolution(Minecraft p_i1094_1_, int p_i1094_2_, int p_i1094_3_) {
		scaledWidth = p_i1094_2_;
		scaledHeight = p_i1094_3_;
		scaleFactor = 1;
		boolean flag = p_i1094_1_.func_152349_b();
		int k = p_i1094_1_.gameSettings.guiScale;

		if (k == 0) {
			k = 1000;
		}

		while (scaleFactor < k && scaledWidth / (scaleFactor + 1) >= 320 && scaledHeight / (scaleFactor + 1) >= 240) {
			++scaleFactor;
		}

		if (flag && scaleFactor % 2 != 0 && scaleFactor != 1) {
			--scaleFactor;
		}

		scaledWidthD = (double) scaledWidth / (double) scaleFactor;
		scaledHeightD = (double) scaledHeight / (double) scaleFactor;
		scaledWidth = MathHelper.ceiling_double_int(scaledWidthD);
		scaledHeight = MathHelper.ceiling_double_int(scaledHeightD);
	}

	public int getScaledWidth() {
		return scaledWidth;
	}

	public int getScaledHeight() {
		return scaledHeight;
	}

	public double getScaledWidth_double() {
		return scaledWidthD;
	}

	public double getScaledHeight_double() {
		return scaledHeightD;
	}

	public int getScaleFactor() {
		return scaleFactor;
	}
}