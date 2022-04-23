package net.minecraft.entity.ai;

import net.minecraft.entity.EntityCreature;

public class EntityAIRestrictSun extends EntityAIBase {
	private final EntityCreature theEntity;
	private static final String __OBFID = "CL_00001611";

	public EntityAIRestrictSun(EntityCreature p_i1652_1_) {
		theEntity = p_i1652_1_;
	}

	@Override
	public boolean shouldExecute() {
		return theEntity.worldObj.isDaytime();
	}

	@Override
	public void startExecuting() {
		theEntity.getNavigator().setAvoidSun(true);
	}

	@Override
	public void resetTask() {
		theEntity.getNavigator().setAvoidSun(false);
	}
}