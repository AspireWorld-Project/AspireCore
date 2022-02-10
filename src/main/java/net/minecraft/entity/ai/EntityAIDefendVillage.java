package net.minecraft.entity.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.village.Village;

public class EntityAIDefendVillage extends EntityAITarget {
	EntityIronGolem irongolem;
	EntityLivingBase villageAgressorTarget;
	private static final String __OBFID = "CL_00001618";

	public EntityAIDefendVillage(EntityIronGolem p_i1659_1_) {
		super(p_i1659_1_, false, true);
		irongolem = p_i1659_1_;
		setMutexBits(1);
	}

	@Override
	public boolean shouldExecute() {
		Village village = irongolem.getVillage();

		if (village == null)
			return false;
		else {
			villageAgressorTarget = village.findNearestVillageAggressor(irongolem);

			if (!isSuitableTarget(villageAgressorTarget, false)) {
				if (taskOwner.getRNG().nextInt(20) == 0) {
					villageAgressorTarget = village.func_82685_c(irongolem);
					return isSuitableTarget(villageAgressorTarget, false);
				} else
					return false;
			} else
				return true;
		}
	}

	@Override
	public void startExecuting() {
		irongolem.setAttackTarget(villageAgressorTarget);
		super.startExecuting();
	}
}