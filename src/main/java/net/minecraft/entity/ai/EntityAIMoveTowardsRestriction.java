package net.minecraft.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.Vec3;

public class EntityAIMoveTowardsRestriction extends EntityAIBase {
	private final EntityCreature theEntity;
	private double movePosX;
	private double movePosY;
	private double movePosZ;
	private final double movementSpeed;
	private static final String __OBFID = "CL_00001598";

	public EntityAIMoveTowardsRestriction(EntityCreature p_i2347_1_, double p_i2347_2_) {
		theEntity = p_i2347_1_;
		movementSpeed = p_i2347_2_;
		setMutexBits(1);
	}

	@Override
	public boolean shouldExecute() {
		if (theEntity.isWithinHomeDistanceCurrentPosition())
			return false;
		else {
			ChunkCoordinates chunkcoordinates = theEntity.getHomePosition();
			Vec3 vec3 = RandomPositionGenerator.findRandomTargetBlockTowards(theEntity, 16, 7,
					Vec3.createVectorHelper(chunkcoordinates.posX, chunkcoordinates.posY, chunkcoordinates.posZ));

			if (vec3 == null)
				return false;
			else {
				movePosX = vec3.xCoord;
				movePosY = vec3.yCoord;
				movePosZ = vec3.zCoord;
				return true;
			}
		}
	}

	@Override
	public boolean continueExecuting() {
		return !theEntity.getNavigator().noPath();
	}

	@Override
	public void startExecuting() {
		theEntity.getNavigator().tryMoveToXYZ(movePosX, movePosY, movePosZ, movementSpeed);
	}
}