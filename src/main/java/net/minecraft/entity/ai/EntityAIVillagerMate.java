package net.minecraft.entity.ai;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.util.MathHelper;
import net.minecraft.village.Village;
import net.minecraft.world.World;

public class EntityAIVillagerMate extends EntityAIBase {
	private EntityVillager villagerObj;
	private EntityVillager mate;
	private World worldObj;
	private int matingTimeout;
	Village villageObj;
	private static final String __OBFID = "CL_00001594";

	public EntityAIVillagerMate(EntityVillager p_i1634_1_) {
		villagerObj = p_i1634_1_;
		worldObj = p_i1634_1_.worldObj;
		setMutexBits(3);
	}

	@Override
	public boolean shouldExecute() {
		if (villagerObj.getGrowingAge() != 0)
			return false;
		else if (villagerObj.getRNG().nextInt(500) != 0)
			return false;
		else {
			villageObj = worldObj.villageCollectionObj.findNearestVillage(MathHelper.floor_double(villagerObj.posX),
					MathHelper.floor_double(villagerObj.posY), MathHelper.floor_double(villagerObj.posZ), 0);

			if (villageObj == null)
				return false;
			else if (!checkSufficientDoorsPresentForNewVillager())
				return false;
			else {
				Entity entity = worldObj.findNearestEntityWithinAABB(EntityVillager.class,
						villagerObj.boundingBox.expand(8.0D, 3.0D, 8.0D), villagerObj);

				if (entity == null)
					return false;
				else {
					mate = (EntityVillager) entity;
					return mate.getGrowingAge() == 0;
				}
			}
		}
	}

	@Override
	public void startExecuting() {
		matingTimeout = 300;
		villagerObj.setMating(true);
	}

	@Override
	public void resetTask() {
		villageObj = null;
		mate = null;
		villagerObj.setMating(false);
	}

	@Override
	public boolean continueExecuting() {
		return matingTimeout >= 0 && checkSufficientDoorsPresentForNewVillager() && villagerObj.getGrowingAge() == 0;
	}

	@Override
	public void updateTask() {
		--matingTimeout;
		villagerObj.getLookHelper().setLookPositionWithEntity(mate, 10.0F, 30.0F);

		if (villagerObj.getDistanceSqToEntity(mate) > 2.25D) {
			villagerObj.getNavigator().tryMoveToEntityLiving(mate, 0.25D);
		} else if (matingTimeout == 0 && mate.isMating()) {
			giveBirth();
		}

		if (villagerObj.getRNG().nextInt(35) == 0) {
			worldObj.setEntityState(villagerObj, (byte) 12);
		}
	}

	private boolean checkSufficientDoorsPresentForNewVillager() {
		if (!villageObj.isMatingSeason())
			return false;
		else {
			int i = (int) (villageObj.getNumVillageDoors() * 0.35D);
			return villageObj.getNumVillagers() < i;
		}
	}

	private void giveBirth() {
		EntityVillager entityvillager = villagerObj.createChild(mate);
		mate.setGrowingAge(6000);
		villagerObj.setGrowingAge(6000);
		entityvillager.setGrowingAge(-24000);
		entityvillager.setLocationAndAngles(villagerObj.posX, villagerObj.posY, villagerObj.posZ, 0.0F, 0.0F);
		worldObj.spawnEntityInWorld(entityvillager);
		worldObj.setEntityState(entityvillager, (byte) 12);
	}
}