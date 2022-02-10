package net.minecraft.client.particle;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.World;

@SideOnly(Side.CLIENT)
public class EntityLavaFX extends EntityFX {
	private float lavaParticleScale;
	private static final String __OBFID = "CL_00000912";

	public EntityLavaFX(World p_i1215_1_, double p_i1215_2_, double p_i1215_4_, double p_i1215_6_) {
		super(p_i1215_1_, p_i1215_2_, p_i1215_4_, p_i1215_6_, 0.0D, 0.0D, 0.0D);
		motionX *= 0.800000011920929D;
		motionY *= 0.800000011920929D;
		motionZ *= 0.800000011920929D;
		motionY = rand.nextFloat() * 0.4F + 0.05F;
		particleRed = particleGreen = particleBlue = 1.0F;
		particleScale *= rand.nextFloat() * 2.0F + 0.2F;
		lavaParticleScale = particleScale;
		particleMaxAge = (int) (16.0D / (Math.random() * 0.8D + 0.2D));
		noClip = false;
		setParticleTextureIndex(49);
	}

	@Override
	public int getBrightnessForRender(float p_70070_1_) {
		float f1 = (particleAge + p_70070_1_) / particleMaxAge;

		if (f1 < 0.0F) {
			f1 = 0.0F;
		}

		if (f1 > 1.0F) {
			f1 = 1.0F;
		}

		int i = super.getBrightnessForRender(p_70070_1_);
		short short1 = 240;
		int j = i >> 16 & 255;
		return short1 | j << 16;
	}

	@Override
	public float getBrightness(float p_70013_1_) {
		return 1.0F;
	}

	@Override
	public void renderParticle(Tessellator p_70539_1_, float p_70539_2_, float p_70539_3_, float p_70539_4_,
			float p_70539_5_, float p_70539_6_, float p_70539_7_) {
		float f6 = (particleAge + p_70539_2_) / particleMaxAge;
		particleScale = lavaParticleScale * (1.0F - f6 * f6);
		super.renderParticle(p_70539_1_, p_70539_2_, p_70539_3_, p_70539_4_, p_70539_5_, p_70539_6_, p_70539_7_);
	}

	@Override
	public void onUpdate() {
		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;

		if (particleAge++ >= particleMaxAge) {
			setDead();
		}

		float f = (float) particleAge / (float) particleMaxAge;

		if (rand.nextFloat() > f) {
			worldObj.spawnParticle("smoke", posX, posY, posZ, motionX, motionY, motionZ);
		}

		motionY -= 0.03D;
		moveEntity(motionX, motionY, motionZ);
		motionX *= 0.9990000128746033D;
		motionY *= 0.9990000128746033D;
		motionZ *= 0.9990000128746033D;

		if (onGround) {
			motionX *= 0.699999988079071D;
			motionZ *= 0.699999988079071D;
		}
	}
}