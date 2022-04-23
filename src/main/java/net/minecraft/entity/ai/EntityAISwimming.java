package net.minecraft.entity.ai;

import net.minecraft.entity.EntityLiving;

public class EntityAISwimming extends EntityAIBase {
	private final EntityLiving theEntity;
	private static final String __OBFID = "CL_00001584";

	public EntityAISwimming(EntityLiving p_i1624_1_) {
		theEntity = p_i1624_1_;
		setMutexBits(4);
		p_i1624_1_.getNavigator().setCanSwim(true);
	}

	@Override
	public boolean shouldExecute() {
		return theEntity.isInWater() || theEntity.handleLavaMovement();
	}

	@Override
	public void updateTask() {
		if (theEntity.getRNG().nextFloat() < 0.8F) {
			theEntity.getJumpHelper().setJumping();
		}
	}
}