package net.minecraft.client.renderer.culling;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.AxisAlignedBB;

@SideOnly(Side.CLIENT)
public class Frustrum implements ICamera {
	private ClippingHelper clippingHelper = ClippingHelperImpl.getInstance();
	private double xPosition;
	private double yPosition;
	private double zPosition;
	private static final String __OBFID = "CL_00000976";

	@Override
	public void setPosition(double p_78547_1_, double p_78547_3_, double p_78547_5_) {
		xPosition = p_78547_1_;
		yPosition = p_78547_3_;
		zPosition = p_78547_5_;
	}

	public boolean isBoxInFrustum(double p_78548_1_, double p_78548_3_, double p_78548_5_, double p_78548_7_,
			double p_78548_9_, double p_78548_11_) {
		return clippingHelper.isBoxInFrustum(p_78548_1_ - xPosition, p_78548_3_ - yPosition, p_78548_5_ - zPosition,
				p_78548_7_ - xPosition, p_78548_9_ - yPosition, p_78548_11_ - zPosition);
	}

	@Override
	public boolean isBoundingBoxInFrustum(AxisAlignedBB p_78546_1_) {
		return isBoxInFrustum(p_78546_1_.minX, p_78546_1_.minY, p_78546_1_.minZ, p_78546_1_.maxX, p_78546_1_.maxY,
				p_78546_1_.maxZ);
	}
}