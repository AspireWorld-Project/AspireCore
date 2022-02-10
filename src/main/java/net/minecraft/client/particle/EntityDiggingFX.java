package net.minecraft.client.particle;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

@SideOnly(Side.CLIENT)
public class EntityDiggingFX extends EntityFX {
	private Block field_145784_a;
	private static final String __OBFID = "CL_00000932";
	private int side;

	public EntityDiggingFX(World p_i1234_1_, double p_i1234_2_, double p_i1234_4_, double p_i1234_6_, double p_i1234_8_,
			double p_i1234_10_, double p_i1234_12_, Block p_i1234_14_, int p_i1234_15_) {
		this(p_i1234_1_, p_i1234_2_, p_i1234_4_, p_i1234_6_, p_i1234_8_, p_i1234_10_, p_i1234_12_, p_i1234_14_,
				p_i1234_15_, p_i1234_1_.rand.nextInt(6));
	}

	public EntityDiggingFX(World p_i1234_1_, double p_i1234_2_, double p_i1234_4_, double p_i1234_6_, double p_i1234_8_,
			double p_i1234_10_, double p_i1234_12_, Block p_i1234_14_, int p_i1234_15_, int side) {
		super(p_i1234_1_, p_i1234_2_, p_i1234_4_, p_i1234_6_, p_i1234_8_, p_i1234_10_, p_i1234_12_);
		field_145784_a = p_i1234_14_;
		setParticleIcon(p_i1234_14_.getIcon(side, p_i1234_15_));
		particleGravity = p_i1234_14_.blockParticleGravity;
		particleRed = particleGreen = particleBlue = 0.6F;
		particleScale /= 2.0F;
		this.side = side;
	}

	public EntityDiggingFX applyColourMultiplier(int p_70596_1_, int p_70596_2_, int p_70596_3_) {
		if (field_145784_a == Blocks.grass && side != 1)
			return this;
		else {
			int l = field_145784_a.colorMultiplier(worldObj, p_70596_1_, p_70596_2_, p_70596_3_);
			particleRed *= (l >> 16 & 255) / 255.0F;
			particleGreen *= (l >> 8 & 255) / 255.0F;
			particleBlue *= (l & 255) / 255.0F;
			return this;
		}
	}

	public EntityDiggingFX applyRenderColor(int p_90019_1_) {
		if (field_145784_a == Blocks.grass)
			return this;
		else {
			int j = field_145784_a.getRenderColor(p_90019_1_);
			particleRed *= (j >> 16 & 255) / 255.0F;
			particleGreen *= (j >> 8 & 255) / 255.0F;
			particleBlue *= (j & 255) / 255.0F;
			return this;
		}
	}

	@Override
	public int getFXLayer() {
		return 1;
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