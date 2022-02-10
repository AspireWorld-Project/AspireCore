package net.minecraft.entity.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityTameable;

public class EntityAIOwnerHurtTarget extends EntityAITarget {
	EntityTameable theEntityTameable;
	EntityLivingBase theTarget;
	private int field_142050_e;
	private static final String __OBFID = "CL_00001625";

	public EntityAIOwnerHurtTarget(EntityTameable p_i1668_1_) {
		super(p_i1668_1_, false);
		theEntityTameable = p_i1668_1_;
		setMutexBits(1);
	}

	@Override
	public boolean shouldExecute() {
		if (!theEntityTameable.isTamed())
			return false;
		else {
			EntityLivingBase entitylivingbase = theEntityTameable.getOwner();

			if (entitylivingbase == null)
				return false;
			else {
				theTarget = entitylivingbase.getLastAttacker();
				int i = entitylivingbase.getLastAttackerTime();
				return i != field_142050_e && isSuitableTarget(theTarget, false)
						&& theEntityTameable.func_142018_a(theTarget, entitylivingbase);
			}
		}
	}

	@Override
	public void startExecuting() {
		taskOwner.setAttackTarget(theTarget);
		EntityLivingBase entitylivingbase = theEntityTameable.getOwner();

		if (entitylivingbase != null) {
			field_142050_e = entitylivingbase.getLastAttackerTime();
		}

		super.startExecuting();
	}
}