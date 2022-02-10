package net.minecraft.client.particle;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.World;

@SideOnly(Side.CLIENT)
public class EntityReddustFX extends EntityFX {
	float reddustParticleScale;
	private static final String __OBFID = "CL_00000923";

	public EntityReddustFX(World p_i1223_1_, double p_i1223_2_, double p_i1223_4_, double p_i1223_6_, float p_i1223_8_,
			float p_i1223_9_, float p_i1223_10_) {
		this(p_i1223_1_, p_i1223_2_, p_i1223_4_, p_i1223_6_, 1.0F, p_i1223_8_, p_i1223_9_, p_i1223_10_);
	}

	public EntityReddustFX(World p_i1224_1_, double p_i1224_2_, double p_i1224_4_, double p_i1224_6_, float p_i1224_8_,
			float p_i1224_9_, float p_i1224_10_, float p_i1224_11_) {
		super(p_i1224_1_, p_i1224_2_, p_i1224_4_, p_i1224_6_, 0.0D, 0.0D, 0.0D);
		motionX *= 0.10000000149011612D;
		motionY *= 0.10000000149011612D;
		motionZ *= 0.10000000149011612D;

		if (p_i1224_9_ == 0.0F) {
			p_i1224_9_ = 1.0F;
		}

		float f4 = (float) Math.random() * 0.4F + 0.6F;
		particleRed = ((float) (Math.random() * 0.20000000298023224D) + 0.8F) * p_i1224_9_ * f4;
		particleGreen = ((float) (Math.random() * 0.20000000298023224D) + 0.8F) * p_i1224_10_ * f4;
		particleBlue = ((float) (Math.random() * 0.20000000298023224D) + 0.8F) * p_i1224_11_ * f4;
		particleScale *= 0.75F;
		particleScale *= p_i1224_8_;
		reddustParticleScale = particleScale;
		particleMaxAge = (int) (8.0D / (Math.random() * 0.8D + 0.2D));
		particleMaxAge = (int) (particleMaxAge * p_i1224_8_);
		noClip = false;
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

		particleScale = reddustParticleScale * f6;
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

		setParticleTextureIndex(7 - particleAge * 8 / particleMaxAge);
		moveEntity(motionX, motionY, motionZ);

		if (posY == prevPosY) {
			motionX *= 1.1D;
			motionZ *= 1.1D;
		}

		motionX *= 0.9599999785423279D;
		motionY *= 0.9599999785423279D;
		motionZ *= 0.9599999785423279D;

		if (onGround) {
			motionX *= 0.699999988079071D;
			motionZ *= 0.699999988079071D;
		}
	}
}