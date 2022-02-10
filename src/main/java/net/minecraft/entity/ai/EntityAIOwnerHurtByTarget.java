package net.minecraft.entity.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityTameable;

public class EntityAIOwnerHurtByTarget extends EntityAITarget {
	EntityTameable theDefendingTameable;
	EntityLivingBase theOwnerAttacker;
	private int field_142051_e;
	private static final String __OBFID = "CL_00001624";

	public EntityAIOwnerHurtByTarget(EntityTameable p_i1667_1_) {
		super(p_i1667_1_, false);
		theDefendingTameable = p_i1667_1_;
		setMutexBits(1);
	}

	@Override
	public boolean shouldExecute() {
		if (!theDefendingTameable.isTamed())
			return false;
		else {
			EntityLivingBase entitylivingbase = theDefendingTameable.getOwner();

			if (entitylivingbase == null)
				return false;
			else {
				theOwnerAttacker = entitylivingbase.getAITarget();
				int i = entitylivingbase.func_142015_aE();
				return i != field_142051_e && isSuitableTarget(theOwnerAttacker, false)
						&& theDefendingTameable.func_142018_a(theOwnerAttacker, entitylivingbase);
			}
		}
	}

	@Override
	public void startExecuting() {
		taskOwner.setAttackTarget(theOwnerAttacker);
		EntityLivingBase entitylivingbase = theDefendingTameable.getOwner();

		if (entitylivingbase != null) {
			field_142051_e = entitylivingbase.func_142015_aE();
		}

		super.startExecuting();
	}
}