package net.minecraft.entity.ai;

import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.util.Vec3;

public class EntityAIPlay extends EntityAIBase {
	private EntityVillager villagerObj;
	private EntityLivingBase targetVillager;
	private double field_75261_c;
	private int playTime;
	private static final String __OBFID = "CL_00001605";

	public EntityAIPlay(EntityVillager p_i1646_1_, double p_i1646_2_) {
		villagerObj = p_i1646_1_;
		field_75261_c = p_i1646_2_;
		setMutexBits(1);
	}

	@Override
	public boolean shouldExecute() {
		if (villagerObj.getGrowingAge() >= 0)
			return false;
		else if (villagerObj.getRNG().nextInt(400) != 0)
			return false;
		else {
			List list = villagerObj.worldObj.getEntitiesWithinAABB(EntityVillager.class,
					villagerObj.boundingBox.expand(6.0D, 3.0D, 6.0D));
			double d0 = Double.MAX_VALUE;
			Iterator iterator = list.iterator();

			while (iterator.hasNext()) {
				EntityVillager entityvillager = (EntityVillager) iterator.next();

				if (entityvillager != villagerObj && !entityvillager.isPlaying()
						&& entityvillager.getGrowingAge() < 0) {
					double d1 = entityvillager.getDistanceSqToEntity(villagerObj);

					if (d1 <= d0) {
						d0 = d1;
						targetVillager = entityvillager;
					}
				}
			}

			if (targetVillager == null) {
				Vec3 vec3 = RandomPositionGenerator.findRandomTarget(villagerObj, 16, 3);

				if (vec3 == null)
					return false;
			}

			return true;
		}
	}

	@Override
	public boolean continueExecuting() {
		return playTime > 0;
	}

	@Override
	public void startExecuting() {
		if (targetVillager != null) {
			villagerObj.setPlaying(true);
		}

		playTime = 1000;
	}

	@Override
	public void resetTask() {
		villagerObj.setPlaying(false);
		targetVillager = null;
	}

	@Override
	public void updateTask() {
		--playTime;

		if (targetVillager != null) {
			if (villagerObj.getDistanceSqToEntity(targetVillager) > 4.0D) {
				villagerObj.getNavigator().tryMoveToEntityLiving(targetVillager, field_75261_c);
			}
		} else if (villagerObj.getNavigator().noPath()) {
			Vec3 vec3 = RandomPositionGenerator.findRandomTarget(villagerObj, 16, 3);

			if (vec3 == null)
				return;

			villagerObj.getNavigator().tryMoveToXYZ(vec3.xCoord, vec3.yCoord, vec3.zCoord, field_75261_c);
		}
	}
}