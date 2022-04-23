package net.minecraft.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.util.MathHelper;
import net.minecraft.village.Village;
import net.minecraft.village.VillageDoorInfo;

public class EntityAIRestrictOpenDoor extends EntityAIBase {
	private final EntityCreature entityObj;
	private VillageDoorInfo frontDoor;
	private static final String __OBFID = "CL_00001610";

	public EntityAIRestrictOpenDoor(EntityCreature p_i1651_1_) {
		entityObj = p_i1651_1_;
	}

	@Override
	public boolean shouldExecute() {
		if (entityObj.worldObj.isDaytime())
			return false;
		else {
			Village village = entityObj.worldObj.villageCollectionObj.findNearestVillage(
					MathHelper.floor_double(entityObj.posX), MathHelper.floor_double(entityObj.posY),
					MathHelper.floor_double(entityObj.posZ), 16);

			if (village == null)
				return false;
			else {
				frontDoor = village.findNearestDoor(MathHelper.floor_double(entityObj.posX),
						MathHelper.floor_double(entityObj.posY), MathHelper.floor_double(entityObj.posZ));
				return frontDoor != null && frontDoor.getInsideDistanceSquare(MathHelper.floor_double(entityObj.posX),
						MathHelper.floor_double(entityObj.posY),
						MathHelper.floor_double(entityObj.posZ)) < 2.25D;
			}
		}
	}

	@Override
	public boolean continueExecuting() {
		return !entityObj.worldObj.isDaytime() && !frontDoor.isDetachedFromVillageFlag && frontDoor.isInside(MathHelper.floor_double(entityObj.posX),
				MathHelper.floor_double(entityObj.posZ));
	}

	@Override
	public void startExecuting() {
		entityObj.getNavigator().setBreakDoors(false);
		entityObj.getNavigator().setEnterDoors(false);
	}

	@Override
	public void resetTask() {
		entityObj.getNavigator().setBreakDoors(true);
		entityObj.getNavigator().setEnterDoors(true);
		frontDoor = null;
	}

	@Override
	public void updateTask() {
		frontDoor.incrementDoorOpeningRestrictionCounter();
	}
}