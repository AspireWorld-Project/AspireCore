package net.minecraft.entity;

import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public abstract class EntityFlying extends EntityLiving {
	private static final String __OBFID = "CL_00001545";

	public EntityFlying(World p_i1587_1_) {
		super(p_i1587_1_);
	}

	@Override
	protected void fall(float p_70069_1_) {
	}

	@Override
	protected void updateFallState(double p_70064_1_, boolean p_70064_3_) {
	}

	@Override
	public void moveEntityWithHeading(float p_70612_1_, float p_70612_2_) {
		if (isInWater()) {
			moveFlying(p_70612_1_, p_70612_2_, 0.02F);
			moveEntity(motionX, motionY, motionZ);
			motionX *= 0.800000011920929D;
			motionY *= 0.800000011920929D;
			motionZ *= 0.800000011920929D;
		} else if (handleLavaMovement()) {
			moveFlying(p_70612_1_, p_70612_2_, 0.02F);
			moveEntity(motionX, motionY, motionZ);
			motionX *= 0.5D;
			motionY *= 0.5D;
			motionZ *= 0.5D;
		} else {
			float f2 = 0.91F;

			if (onGround) {
				f2 = worldObj.getBlock(MathHelper.floor_double(posX), MathHelper.floor_double(boundingBox.minY) - 1,
						MathHelper.floor_double(posZ)).slipperiness * 0.91F;
			}

			float f3 = 0.16277136F / (f2 * f2 * f2);
			moveFlying(p_70612_1_, p_70612_2_, onGround ? 0.1F * f3 : 0.02F);
			f2 = 0.91F;

			if (onGround) {
				f2 = worldObj.getBlock(MathHelper.floor_double(posX), MathHelper.floor_double(boundingBox.minY) - 1,
						MathHelper.floor_double(posZ)).slipperiness * 0.91F;
			}

			moveEntity(motionX, motionY, motionZ);
			motionX *= f2;
			motionY *= f2;
			motionZ *= f2;
		}

		prevLimbSwingAmount = limbSwingAmount;
		double d1 = posX - prevPosX;
		double d0 = posZ - prevPosZ;
		float f4 = MathHelper.sqrt_double(d1 * d1 + d0 * d0) * 4.0F;

		if (f4 > 1.0F) {
			f4 = 1.0F;
		}

		limbSwingAmount += (f4 - limbSwingAmount) * 0.4F;
		limbSwing += limbSwingAmount;
	}

	@Override
	public boolean isOnLadder() {
		return false;
	}
}