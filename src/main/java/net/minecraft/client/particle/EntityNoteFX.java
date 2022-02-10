package net.minecraft.client.particle;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

@SideOnly(Side.CLIENT)
public class EntityNoteFX extends EntityFX {
	float noteParticleScale;
	private static final String __OBFID = "CL_00000913";

	public EntityNoteFX(World p_i1216_1_, double p_i1216_2_, double p_i1216_4_, double p_i1216_6_, double p_i1216_8_,
			double p_i1216_10_, double p_i1216_12_) {
		this(p_i1216_1_, p_i1216_2_, p_i1216_4_, p_i1216_6_, p_i1216_8_, p_i1216_10_, p_i1216_12_, 2.0F);
	}

	public EntityNoteFX(World p_i1217_1_, double p_i1217_2_, double p_i1217_4_, double p_i1217_6_, double p_i1217_8_,
			double p_i1217_10_, double p_i1217_12_, float p_i1217_14_) {
		super(p_i1217_1_, p_i1217_2_, p_i1217_4_, p_i1217_6_, 0.0D, 0.0D, 0.0D);
		motionX *= 0.009999999776482582D;
		motionY *= 0.009999999776482582D;
		motionZ *= 0.009999999776482582D;
		motionY += 0.2D;
		particleRed = MathHelper.sin(((float) p_i1217_8_ + 0.0F) * (float) Math.PI * 2.0F) * 0.65F + 0.35F;
		particleGreen = MathHelper.sin(((float) p_i1217_8_ + 0.33333334F) * (float) Math.PI * 2.0F) * 0.65F + 0.35F;
		particleBlue = MathHelper.sin(((float) p_i1217_8_ + 0.6666667F) * (float) Math.PI * 2.0F) * 0.65F + 0.35F;
		particleScale *= 0.75F;
		particleScale *= p_i1217_14_;
		noteParticleScale = particleScale;
		particleMaxAge = 6;
		noClip = false;
		setParticleTextureIndex(64);
	}

	@Override
	public void renderParticle(Tessellator p_70539_1_, float p_70539_2_, float p_70539_3_, float p_70539_4_,
			float p_70539_5_, float p_70539_6_, float p_70539_7_) {
		float f6 = (particleAge + p_70539_2_) / particleMaxAge * 32.0F;

		if (f6 < 0.0F) {
			f6 = 0.0F;
		}

		if (f6 > 1.0F) {
			f6 = 1.0F;
		}

		particleScale = noteParticleScale * f6;
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

		moveEntity(motionX, motionY, motionZ);

		if (posY == prevPosY) {
			motionX *= 1.1D;
			motionZ *= 1.1D;
		}

		motionX *= 0.6600000262260437D;
		motionY *= 0.6600000262260437D;
		motionZ *= 0.6600000262260437D;

		if (onGround) {
			motionX *= 0.699999988079071D;
			motionZ *= 0.699999988079071D;
		}
	}
}