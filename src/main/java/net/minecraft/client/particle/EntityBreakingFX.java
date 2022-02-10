package net.minecraft.client.particle;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.world.World;

@SideOnly(Side.CLIENT)
public class EntityBreakingFX extends EntityFX {
	private static final String __OBFID = "CL_00000897";

	public EntityBreakingFX(World p_i1195_1_, double p_i1195_2_, double p_i1195_4_, double p_i1195_6_,
			Item p_i1195_8_) {
		this(p_i1195_1_, p_i1195_2_, p_i1195_4_, p_i1195_6_, p_i1195_8_, 0);
	}

	public EntityBreakingFX(World p_i1196_1_, double p_i1196_2_, double p_i1196_4_, double p_i1196_6_, Item p_i1196_8_,
			int p_i1196_9_) {
		super(p_i1196_1_, p_i1196_2_, p_i1196_4_, p_i1196_6_, 0.0D, 0.0D, 0.0D);
		setParticleIcon(p_i1196_8_.getIconFromDamage(p_i1196_9_));
		particleRed = particleGreen = particleBlue = 1.0F;
		particleGravity = Blocks.snow.blockParticleGravity;
		particleScale /= 2.0F;
	}

	public EntityBreakingFX(World p_i1197_1_, double p_i1197_2_, double p_i1197_4_, double p_i1197_6_,
			double p_i1197_8_, double p_i1197_10_, double p_i1197_12_, Item p_i1197_14_, int p_i1197_15_) {
		this(p_i1197_1_, p_i1197_2_, p_i1197_4_, p_i1197_6_, p_i1197_14_, p_i1197_15_);
		motionX *= 0.10000000149011612D;
		motionY *= 0.10000000149011612D;
		motionZ *= 0.10000000149011612D;
		motionX += p_i1197_8_;
		motionY += p_i1197_10_;
		motionZ += p_i1197_12_;
	}

	@Override
	public int getFXLayer() {
		return 2;
	}

	@Override
	public void renderParticle(Tessellator p_70539_1_, float p_70539_2_, float p_70539_3_, float p_70539_4_,
			float p_70539_5_, float p_70539_6_, float p_70539_7_) {
		float f6 = (particleTextureIndexX + particleTextureJitterX / 4.0F) / 16.0F;
		float f7 = f6 + 0.015609375F;
		float f8 = (particleTextureIndexY + particleTextureJitterY / 4.0F) / 16.0F;
		float f9 = f8 + 0.015609375F;
		float f10 = 0.1F * particleScale;

		if (particleIcon != null) {
			f6 = particleIcon.getInterpolatedU(particleTextureJitterX / 4.0F * 16.0F);
			f7 = particleIcon.getInterpolatedU((particleTextureJitterX + 1.0F) / 4.0F * 16.0F);
			f8 = particleIcon.getInterpolatedV(particleTextureJitterY / 4.0F * 16.0F);
			f9 = particleIcon.getInterpolatedV((particleTextureJitterY + 1.0F) / 4.0F * 16.0F);
		}

		float f11 = (float) (prevPosX + (posX - prevPosX) * p_70539_2_ - interpPosX);
		float f12 = (float) (prevPosY + (posY - prevPosY) * p_70539_2_ - interpPosY);
		float f13 = (float) (prevPosZ + (posZ - prevPosZ) * p_70539_2_ - interpPosZ);
		p_70539_1_.setColorOpaque_F(particleRed, particleGreen, particleBlue);
		p_70539_1_.addVertexWithUV(f11 - p_70539_3_ * f10 - p_70539_6_ * f10, f12 - p_70539_4_ * f10,
				f13 - p_70539_5_ * f10 - p_70539_7_ * f10, f6, f9);
		p_70539_1_.addVertexWithUV(f11 - p_70539_3_ * f10 + p_70539_6_ * f10, f12 + p_70539_4_ * f10,
				f13 - p_70539_5_ * f10 + p_70539_7_ * f10, f6, f8);
		p_70539_1_.addVertexWithUV(f11 + p_70539_3_ * f10 + p_70539_6_ * f10, f12 + p_70539_4_ * f10,
				f13 + p_70539_5_ * f10 + p_70539_7_ * f10, f7, f8);
		p_70539_1_.addVertexWithUV(f11 + p_70539_3_ * f10 - p_70539_6_ * f10, f12 - p_70539_4_ * f10,
				f13 + p_70539_5_ * f10 - p_70539_7_ * f10, f7, f9);
	}
}