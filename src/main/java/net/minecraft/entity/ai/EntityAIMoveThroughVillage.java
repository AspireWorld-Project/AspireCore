package net.minecraft.entity.ai;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.EntityCreature;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.village.Village;
import net.minecraft.village.VillageDoorInfo;

public class EntityAIMoveThroughVillage extends EntityAIBase {
	private EntityCreature theEntity;
	private double movementSpeed;
	private PathEntity entityPathNavigate;
	private VillageDoorInfo doorInfo;
	private boolean isNocturnal;
	private List doorList = new ArrayList();
	private static final String __OBFID = "CL_00001597";

	public EntityAIMoveThroughVillage(EntityCreature p_i1638_1_, double p_i1638_2_, boolean p_i1638_4_) {
		theEntity = p_i1638_1_;
		movementSpeed = p_i1638_2_;
		isNocturnal = p_i1638_4_;
		setMutexBits(1);
	}

	@Override
	public boolean shouldExecute() {
		func_75414_f();

		if (isNocturnal && theEntity.worldObj.isDaytime())
			return false;
		else {
			Village village = theEntity.worldObj.villageCollectionObj.findNearestVillage(
					MathHelper.floor_double(theEntity.posX), MathHelper.floor_double(theEntity.posY),
					MathHelper.floor_double(theEntity.posZ), 0);

			if (village == null)
				return false;
			else {
				doorInfo = func_75412_a(village);

				if (doorInfo == null)
					return false;
				else {
					boolean flag = theEntity.getNavigator().getCanBreakDoors();
					theEntity.getNavigator().setBreakDoors(false);
					entityPathNavigate = theEntity.getNavigator().getPathToXYZ(doorInfo.posX, doorInfo.posY,
							doorInfo.posZ);
					theEntity.getNavigator().setBreakDoors(flag);

					if (entityPathNavigate != null)
						return true;
					else {
						Vec3 vec3 = RandomPositionGenerator.findRandomTargetBlockTowards(theEntity, 10, 7,
								Vec3.createVectorHelper(doorInfo.posX, doorInfo.posY, doorInfo.posZ));

						if (vec3 == null)
							return false;
						else {
							theEntity.getNavigator().setBreakDoors(false);
							entityPathNavigate = theEntity.getNavigator().getPathToXYZ(vec3.xCoord, vec3.yCoord,
									vec3.zCoord);
							theEntity.getNavigator().setBreakDoors(flag);
							return entityPathNavigate != null;
						}
					}
				}
			}
		}
	}

	@Override
	public boolean continueExecuting() {
		if (theEntity.getNavigator().noPath())
			return false;
		else {
			float f = theEntity.width + 4.0F;
			return theEntity.getDistanceSq(doorInfo.posX, doorInfo.posY, doorInfo.posZ) > f * f;
		}
	}

	@Override
	public void startExecuting() {
		theEntity.getNavigator().setPath(entityPathNavigate, movementSpeed);
	}

	@Override
	public void resetTask() {
		if (theEntity.getNavigator().noPath()
				|| theEntity.getDistanceSq(doorInfo.posX, doorInfo.posY, doorInfo.posZ) < 16.0D) {
			doorList.add(doorInfo);
		}
	}

	private VillageDoorInfo func_75412_a(Village p_75412_1_) {
		VillageDoorInfo villagedoorinfo = null;
		int i = Integer.MAX_VALUE;
		List list = p_75412_1_.getVillageDoorInfoList();
		Iterator iterator = list.iterator();

		while (iterator.hasNext()) {
			VillageDoorInfo villagedoorinfo1 = (VillageDoorInfo) iterator.next();
			int j = villagedoorinfo1.getDistanceSquared(MathHelper.floor_double(theEntity.posX),
					MathHelper.floor_double(theEntity.posY), MathHelper.floor_double(theEntity.posZ));

			if (j < i && !func_75413_a(villagedoorinfo1)) {
				villagedoorinfo = villagedoorinfo1;
				i = j;
			}
		}

		return villagedoorinfo;
	}

	private boolean func_75413_a(VillageDoorInfo p_75413_1_) {
		Iterator iterator = doorList.iterator();
		VillageDoorInfo villagedoorinfo1;

		do {
			if (!iterator.hasNext())
				return false;

			villagedoorinfo1 = (VillageDoorInfo) iterator.next();
		} while (p_75413_1_.posX != villagedoorinfo1.posX || p_75413_1_.posY != villagedoorinfo1.posY
				|| p_75413_1_.posZ != villagedoorinfo1.posZ);

		return true;
	}

	private void func_75414_f() {
		if (doorList.size() > 15) {
			doorList.remove(0);
		}
	}
}