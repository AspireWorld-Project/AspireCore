package net.minecraft.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.village.Village;
import net.minecraft.village.VillageDoorInfo;

public class EntityAIMoveIndoors extends EntityAIBase {
	private EntityCreature entityObj;
	private VillageDoorInfo doorInfo;
	private int insidePosX = -1;
	private int insidePosZ = -1;
	private static final String __OBFID = "CL_00001596";

	public EntityAIMoveIndoors(EntityCreature p_i1637_1_) {
		entityObj = p_i1637_1_;
		setMutexBits(1);
	}

	@Override
	public boolean shouldExecute() {
		int i = MathHelper.floor_double(entityObj.posX);
		int j = MathHelper.floor_double(entityObj.posY);
		int k = MathHelper.floor_double(entityObj.posZ);

		if ((!entityObj.worldObj.isDaytime() || entityObj.worldObj.isRaining()
				|| !entityObj.worldObj.getBiomeGenForCoords(i, k).canSpawnLightningBolt())
				&& !entityObj.worldObj.provider.hasNoSky) {
			if (entityObj.getRNG().nextInt(50) != 0)
				return false;
			else if (insidePosX != -1 && entityObj.getDistanceSq(insidePosX, entityObj.posY, insidePosZ) < 4.0D)
				return false;
			else {
				Village village = entityObj.worldObj.villageCollectionObj.findNearestVillage(i, j, k, 14);

				if (village == null)
					return false;
				else {
					doorInfo = village.findNearestDoorUnrestricted(i, j, k);
					return doorInfo != null;
				}
			}
		} else
			return false;
	}

	@Override
	public boolean continueExecuting() {
		return !entityObj.getNavigator().noPath();
	}

	@Override
	public void startExecuting() {
		insidePosX = -1;

		if (entityObj.getDistanceSq(doorInfo.getInsidePosX(), doorInfo.posY, doorInfo.getInsidePosZ()) > 256.0D) {
			Vec3 vec3 = RandomPositionGenerator.findRandomTargetBlockTowards(entityObj, 14, 3, Vec3.createVectorHelper(
					doorInfo.getInsidePosX() + 0.5D, doorInfo.getInsidePosY(), doorInfo.getInsidePosZ() + 0.5D));

			if (vec3 != null) {
				entityObj.getNavigator().tryMoveToXYZ(vec3.xCoord, vec3.yCoord, vec3.zCoord, 1.0D);
			}
		} else {
			entityObj.getNavigator().tryMoveToXYZ(doorInfo.getInsidePosX() + 0.5D, doorInfo.getInsidePosY(),
					doorInfo.getInsidePosZ() + 0.5D, 1.0D);
		}
	}

	@Override
	public void resetTask() {
		insidePosX = doorInfo.getInsidePosX();
		insidePosZ = doorInfo.getInsidePosZ();
		doorInfo = null;
	}
}