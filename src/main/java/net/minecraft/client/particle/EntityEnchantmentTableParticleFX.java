package net.minecraft.client.particle;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.world.World;

@SideOnly(Side.CLIENT)
public class EntityEnchantmentTableParticleFX extends EntityFX {
	private float field_70565_a;
	private double field_70568_aq;
	private double field_70567_ar;
	private double field_70566_as;
	private static final String __OBFID = "CL_00000902";

	public EntityEnchantmentTableParticleFX(World p_i1204_1_, double p_i1204_2_, double p_i1204_4_, double p_i1204_6_,
			double p_i1204_8_, double p_i1204_10_, double p_i1204_12_) {
		super(p_i1204_1_, p_i1204_2_, p_i1204_4_, p_i1204_6_, p_i1204_8_, p_i1204_10_, p_i1204_12_);
		motionX = p_i1204_8_;
		motionY = p_i1204_10_;
		motionZ = p_i1204_12_;
		field_70568_aq = posX = p_i1204_2_;
		field_70567_ar = posY = p_i1204_4_;
		field_70566_as = posZ = p_i1204_6_;
		float f = rand.nextFloat() * 0.6F + 0.4F;
		field_70565_a = particleScale = rand.nextFloat() * 0.5F + 0.2F;
		particleRed = particleGreen = particleBlue = 1.0F * f;
		particleGreen *= 0.9F;
		particleRed *= 0.9F;
		particleMaxAge = (int) (Math.random() * 10.0D) + 30;
		noClip = true;
		setParticleTextureIndex((int) (Math.random() * 26.0D + 1.0D + 224.0D));
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
		f2 *= f2;
		f2 *= f2;
		return f1 * (1.0F - f2) + f2;
	}

	@Override
	public void onUpdate() {
		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;
		float f = (float) particleAge / (float) particleMaxAge;
		f = 1.0F - f;
		float f1 = 1.0F - f;
		f1 *= f1;
		f1 *= f1;
		posX = field_70568_aq + motionX * f;
		posY = field_70567_ar + motionY * f - f1 * 1.2F;
		posZ = field_70566_as + motionZ * f;

		if (particleAge++ >= particleMaxAge) {
			setDead();
		}
	}
}