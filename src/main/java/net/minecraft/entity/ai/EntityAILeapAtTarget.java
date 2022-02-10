package net.minecraft.entity.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;

public class EntityAILeapAtTarget extends EntityAIBase {
	EntityLiving leaper;
	EntityLivingBase leapTarget;
	float leapMotionY;
	private static final String __OBFID = "CL_00001591";

	public EntityAILeapAtTarget(EntityLiving p_i1630_1_, float p_i1630_2_) {
		leaper = p_i1630_1_;
		leapMotionY = p_i1630_2_;
		setMutexBits(5);
	}

	@Override
	public boolean shouldExecute() {
		leapTarget = leaper.getAttackTarget();

		if (leapTarget == null)
			return false;
		else {
			double d0 = leaper.getDistanceSqToEntity(leapTarget);
			return d0 >= 4.0D && d0 <= 16.0D ? !leaper.onGround ? false : leaper.getRNG().nextInt(5) == 0 : false;
		}
	}

	@Override
	public boolean continueExecuting() {
		return !leaper.onGround;
	}

	@Override
	public void startExecuting() {
		double d0 = leapTarget.posX - leaper.posX;
		double d1 = leapTarget.posZ - leaper.posZ;
		float f = MathHelper.sqrt_double(d0 * d0 + d1 * d1);
		leaper.motionX += d0 / f * 0.5D * 0.800000011920929D + leaper.motionX * 0.20000000298023224D;
		leaper.motionZ += d1 / f * 0.5D * 0.800000011920929D + leaper.motionZ * 0.20000000298023224D;
		leaper.motionY = leapMotionY;
	}
}