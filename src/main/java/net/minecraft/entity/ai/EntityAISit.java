package net.minecraft.entity.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityTameable;

public class EntityAISit extends EntityAIBase {
	private EntityTameable theEntity;
	private boolean isSitting;
	private static final String __OBFID = "CL_00001613";

	public EntityAISit(EntityTameable p_i1654_1_) {
		theEntity = p_i1654_1_;
		setMutexBits(5);
	}

	@Override
	public boolean shouldExecute() {
		if (!theEntity.isTamed())
			return false;
		else if (theEntity.isInWater())
			return false;
		else if (!theEntity.onGround)
			return false;
		else {
			EntityLivingBase entitylivingbase = theEntity.getOwner();
			return entitylivingbase == null ? true
					: theEntity.getDistanceSqToEntity(entitylivingbase) < 144.0D
							&& entitylivingbase.getAITarget() != null ? false : isSitting;
		}
	}

	@Override
	public void startExecuting() {
		theEntity.getNavigator().clearPathEntity();
		theEntity.setSitting(true);
	}

	@Override
	public void resetTask() {
		theEntity.setSitting(false);
	}

	public void setSitting(boolean p_75270_1_) {
		isSitting = p_75270_1_;
	}
}