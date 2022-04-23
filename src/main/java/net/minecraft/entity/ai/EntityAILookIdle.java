package net.minecraft.entity.ai;

import net.minecraft.entity.EntityLiving;

public class EntityAILookIdle extends EntityAIBase {
	private final EntityLiving idleEntity;
	private double lookX;
	private double lookZ;
	private int idleTime;
	private static final String __OBFID = "CL_00001607";

	public EntityAILookIdle(EntityLiving p_i1647_1_) {
		idleEntity = p_i1647_1_;
		setMutexBits(3);
	}

	@Override
	public boolean shouldExecute() {
		return idleEntity.getRNG().nextFloat() < 0.02F;
	}

	@Override
	public boolean continueExecuting() {
		return idleTime >= 0;
	}

	@Override
	public void startExecuting() {
		double d0 = Math.PI * 2D * idleEntity.getRNG().nextDouble();
		lookX = Math.cos(d0);
		lookZ = Math.sin(d0);
		idleTime = 20 + idleEntity.getRNG().nextInt(20);
	}

	@Override
	public void updateTask() {
		--idleTime;
		idleEntity.getLookHelper().setLookPosition(idleEntity.posX + lookX, idleEntity.posY + idleEntity.getEyeHeight(),
				idleEntity.posZ + lookZ, 10.0F, idleEntity.getVerticalFaceSpeed());
	}
}