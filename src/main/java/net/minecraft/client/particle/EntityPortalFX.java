package net.minecraft.client.particle;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.World;

@SideOnly(Side.CLIENT)
public class EntityPortalFX extends EntityFX {
	private final float portalParticleScale;
	private final double portalPosX;
	private final double portalPosY;
	private final double portalPosZ;
	private static final String __OBFID = "CL_00000921";

	public EntityPortalFX(World p_i1222_1_, double p_i1222_2_, double p_i1222_4_, double p_i1222_6_, double p_i1222_8_,
			double p_i1222_10_, double p_i1222_12_) {
		super(p_i1222_1_, p_i1222_2_, p_i1222_4_, p_i1222_6_, p_i1222_8_, p_i1222_10_, p_i1222_12_);
		motionX = p_i1222_8_;
		motionY = p_i1222_10_;
		motionZ = p_i1222_12_;
		portalPosX = posX = p_i1222_2_;
		portalPosY = posY = p_i1222_4_;
		portalPosZ = posZ = p_i1222_6_;
		float f = rand.nextFloat() * 0.6F + 0.4F;
		portalParticleScale = particleScale = rand.nextFloat() * 0.2F + 0.5F;
		particleRed = particleGreen = particleBlue = 1.0F * f;
		particleGreen *= 0.3F;
		particleRed *= 0.9F;
		particleMaxAge = (int) (Math.random() * 10.0D) + 40;
		noClip = true;
		setParticleTextureIndex((int) (Math.random() * 8.0D));
	}

	@Override
	public void renderParticle(Tessellator p_70539_1_, float p_70539_2_, float p_70539_3_, float p_70539_4_,
			float p_70539_5_, float p_70539_6_, float p_70539_7_) {
		float f6 = (particleAge + p_70539_2_) / particleMaxAge;
		f6 = 1.0F - f6;
		f6 *= f6;
		f6 = 1.0F - f6;
		particleScale = portalParticleScale * f6;
		super.renderParticle(p_70539_1_, p_70539_2_, p_70539_3_, p_70539_4_, p_70539_5_, p_70539_6_, p_70539_7_);
	}

	@Override
	public int getBrightnessForRender(float p_70070_1_) {
		int i = super.getBrightnessForRender(p_70070_1_);
		float f1 = (float) particleAge / (float) particleMaxAge;
		f1 *= f1;
		f1 *= f1;
		int j = i & 255;
		int k = i >> 16 & 255;
		k += (int) (f1 * 15.0F * 16.0F);

		if (k > 240) {
			k = 240;
		}

		return j | k << 16;
	}

	@Override
	public float getBrightness(float p_70013_1_) {
		float f1 = super.getBrightness(p_70013_1_);
		float f2 = (float) particleAge / (float) particleMaxAge;
		f2 = f2 * f2 * f2 * f2;
		return f1 * (1.0F - f2) + f2;
	}

	@Override
	public void onUpdate() {
		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;
		float f = (float) particleAge / (float) particleMaxAge;
		float f1 = f;
		f = -f + f * f * 2.0F;
		f = 1.0F - f;
		posX = portalPosX + motionX * f;
		posY = portalPosY + motionY * f + (1.0F - f1);
		posZ = portalPosZ + motionZ * f;

		if (particleAge++ >= particleMaxAge) {
			setDead();
		}
	}
}