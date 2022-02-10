package net.minecraft.client.renderer.culling;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClippingHelper {
	public float[][] frustum = new float[16][16];
	public float[] projectionMatrix = new float[16];
	public float[] modelviewMatrix = new float[16];
	public float[] clippingMatrix = new float[16];
	private static final String __OBFID = "CL_00000977";

	public boolean isBoxInFrustum(double p_78553_1_, double p_78553_3_, double p_78553_5_, double p_78553_7_,
			double p_78553_9_, double p_78553_11_) {
		for (int i = 0; i < 6; ++i) {
			if (frustum[i][0] * p_78553_1_ + frustum[i][1] * p_78553_3_ + frustum[i][2] * p_78553_5_
					+ frustum[i][3] <= 0.0D
					&& frustum[i][0] * p_78553_7_ + frustum[i][1] * p_78553_3_ + frustum[i][2] * p_78553_5_
							+ frustum[i][3] <= 0.0D
					&& frustum[i][0] * p_78553_1_ + frustum[i][1] * p_78553_9_ + frustum[i][2] * p_78553_5_
							+ frustum[i][3] <= 0.0D
					&& frustum[i][0] * p_78553_7_ + frustum[i][1] * p_78553_9_ + frustum[i][2] * p_78553_5_
							+ frustum[i][3] <= 0.0D
					&& frustum[i][0] * p_78553_1_ + frustum[i][1] * p_78553_3_ + frustum[i][2] * p_78553_11_
							+ frustum[i][3] <= 0.0D
					&& frustum[i][0] * p_78553_7_ + frustum[i][1] * p_78553_3_ + frustum[i][2] * p_78553_11_
							+ frustum[i][3] <= 0.0D
					&& frustum[i][0] * p_78553_1_ + frustum[i][1] * p_78553_9_ + frustum[i][2] * p_78553_11_
							+ frustum[i][3] <= 0.0D
					&& frustum[i][0] * p_78553_7_ + frustum[i][1] * p_78553_9_ + frustum[i][2] * p_78553_11_
							+ frustum[i][3] <= 0.0D)
				return false;
		}

		return true;
	}
}