package net.minecraft.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.util.AxisAlignedBB;

import java.util.Iterator;
import java.util.List;

public class EntityAIHurtByTarget extends EntityAITarget {
	boolean entityCallsForHelp;
	private int field_142052_b;
	private static final String __OBFID = "CL_00001619";

	public EntityAIHurtByTarget(EntityCreature p_i1660_1_, boolean p_i1660_2_) {
		super(p_i1660_1_, false);
		entityCallsForHelp = p_i1660_2_;
		setMutexBits(1);
	}

	@Override
	public boolean shouldExecute() {
		int i = taskOwner.func_142015_aE();
		return i != field_142052_b && isSuitableTarget(taskOwner.getAITarget(), false);
	}

	@Override
	public void startExecuting() {
		taskOwner.setAttackTarget(taskOwner.getAITarget());
		field_142052_b = taskOwner.func_142015_aE();

		if (entityCallsForHelp) {
			double d0 = getTargetDistance();
			List list = taskOwner.worldObj
					.getEntitiesWithinAABB(taskOwner.getClass(),
							AxisAlignedBB
									.getBoundingBox(taskOwner.posX, taskOwner.posY, taskOwner.posZ,
											taskOwner.posX + 1.0D, taskOwner.posY + 1.0D, taskOwner.posZ + 1.0D)
									.expand(d0, 10.0D, d0));
			Iterator iterator = list.iterator();

			while (iterator.hasNext()) {
				EntityCreature entitycreature = (EntityCreature) iterator.next();

				if (taskOwner != entitycreature && entitycreature.getAttackTarget() == null
						&& !entitycreature.isOnSameTeam(taskOwner.getAITarget())) {
					entitycreature.setAttackTarget(taskOwner.getAITarget());
				}
			}
		}

		super.startExecuting();
	}
}