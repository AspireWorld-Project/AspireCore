package net.minecraft.client.particle;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

@SideOnly(Side.CLIENT)
public class EntityFireworkOverlayFX extends EntityFX {
	private static final String __OBFID = "CL_00000904";

	protected EntityFireworkOverlayFX(World p_i1206_1_, double p_i1206_2_, double p_i1206_4_, double p_i1206_6_) {
		super(p_i1206_1_, p_i1206_2_, p_i1206_4_, p_i1206_6_);
		particleMaxAge = 4;
	}

	@Override
	public void renderParticle(Tessellator p_70539_1_, float p_70539_2_, float p_70539_3_, float p_70539_4_,
			float p_70539_5_, float p_70539_6_, float p_70539_7_) {
		float f6 = 0.25F;
		float f7 = f6 + 0.25F;
		float f8 = 0.125F;
		float f9 = f8 + 0.25F;
		float f10 = 7.1F * MathHelper.sin((particleAge + p_70539_2_ - 1.0F) * 0.25F * (float) Math.PI);
		particleAlpha = 0.6F - (particleAge + p_70539_2_ - 1.0F) * 0.25F * 0.5F;
		float f11 = (float) (prevPosX + (posX - prevPosX) * p_70539_2_ - interpPosX);
		float f12 = (float) (prevPosY + (posY - prevPosY) * p_70539_2_ - interpPosY);
		float f13 = (float) (prevPosZ + (posZ - prevPosZ) * p_70539_2_ - interpPosZ);
		p_70539_1_.setColorRGBA_F(particleRed, particleGreen, particleBlue, particleAlpha);
		p_70539_1_.addVertexWithUV(f11 - p_70539_3_ * f10 - p_70539_6_ * f10, f12 - p_70539_4_ * f10,
				f13 - p_70539_5_ * f10 - p_70539_7_ * f10, f7, f9);
		p_70539_1_.addVertexWithUV(f11 - p_70539_3_ * f10 + p_70539_6_ * f10, f12 + p_70539_4_ * f10,
				f13 - p_70539_5_ * f10 + p_70539_7_ * f10, f7, f8);
		p_70539_1_.addVertexWithUV(f11 + p_70539_3_ * f10 + p_70539_6_ * f10, f12 + p_70539_4_ * f10,
				f13 + p_70539_5_ * f10 + p_70539_7_ * f10, f6, f8);
		p_70539_1_.addVertexWithUV(f11 + p_70539_3_ * f10 - p_70539_6_ * f10, f12 - p_70539_4_ * f10,
				f13 + p_70539_5_ * f10 - p_70539_7_ * f10, f6, f9);
	}
}