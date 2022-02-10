package net.minecraft.entity.ai;

import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.passive.EntityVillager;

public class EntityAIFollowGolem extends EntityAIBase {
	private EntityVillager theVillager;
	private EntityIronGolem theGolem;
	private int takeGolemRoseTick;
	private boolean tookGolemRose;
	private static final String __OBFID = "CL_00001615";

	public EntityAIFollowGolem(EntityVillager p_i1656_1_) {
		theVillager = p_i1656_1_;
		setMutexBits(3);
	}

	@Override
	public boolean shouldExecute() {
		if (theVillager.getGrowingAge() >= 0)
			return false;
		else if (!theVillager.worldObj.isDaytime())
			return false;
		else {
			List list = theVillager.worldObj.getEntitiesWithinAABB(EntityIronGolem.class,
					theVillager.boundingBox.expand(6.0D, 2.0D, 6.0D));

			if (list.isEmpty())
				return false;
			else {
				Iterator iterator = list.iterator();

				while (iterator.hasNext()) {
					EntityIronGolem entityirongolem = (EntityIronGolem) iterator.next();

					if (entityirongolem.getHoldRoseTick() > 0) {
						theGolem = entityirongolem;
						break;
					}
				}

				return theGolem != null;
			}
		}
	}

	@Override
	public boolean continueExecuting() {
		return theGolem.getHoldRoseTick() > 0;
	}

	@Override
	public void startExecuting() {
		takeGolemRoseTick = theVillager.getRNG().nextInt(320);
		tookGolemRose = false;
		theGolem.getNavigator().clearPathEntity();
	}

	@Override
	public void resetTask() {
		theGolem = null;
		theVillager.getNavigator().clearPathEntity();
	}

	@Override
	public void updateTask() {
		theVillager.getLookHelper().setLookPositionWithEntity(theGolem, 30.0F, 30.0F);

		if (theGolem.getHoldRoseTick() == takeGolemRoseTick) {
			theVillager.getNavigator().tryMoveToEntityLiving(theGolem, 0.5D);
			tookGolemRose = true;
		}

		if (tookGolemRose && theVillager.getDistanceSqToEntity(theGolem) < 4.0D) {
			theGolem.setHoldingRose(false);
			theVillager.getNavigator().clearPathEntity();
		}
	}
}