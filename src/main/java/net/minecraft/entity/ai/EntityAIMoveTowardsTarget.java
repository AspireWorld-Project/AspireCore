package net.minecraft.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.Vec3;

public class EntityAIMoveTowardsTarget extends EntityAIBase {
	private final EntityCreature theEntity;
	private EntityLivingBase targetEntity;
	private double movePosX;
	private double movePosY;
	private double movePosZ;
	private final double speed;
	private final float maxTargetDistance;
	private static final String __OBFID = "CL_00001599";

	public EntityAIMoveTowardsTarget(EntityCreature p_i1640_1_, double p_i1640_2_, float p_i1640_4_) {
		theEntity = p_i1640_1_;
		speed = p_i1640_2_;
		maxTargetDistance = p_i1640_4_;
		setMutexBits(1);
	}

	@Override
	public boolean shouldExecute() {
		targetEntity = theEntity.getAttackTarget();

		if (targetEntity == null)
			return false;
		else if (targetEntity.getDistanceSqToEntity(theEntity) > maxTargetDistance * maxTargetDistance)
			return false;
		else {
			Vec3 vec3 = RandomPositionGenerator.findRandomTargetBlockTowards(theEntity, 16, 7,
					Vec3.createVectorHelper(targetEntity.posX, targetEntity.posY, targetEntity.posZ));

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
		return !theEntity.getNavigator().noPath() && targetEntity.isEntityAlive()
				&& targetEntity.getDistanceSqToEntity(theEntity) < maxTargetDistance * maxTargetDistance;
	}

	@Override
	public void resetTask() {
		targetEntity = null;
	}

	@Override
	public void startExecuting() {
		theEntity.getNavigator().tryMoveToXYZ(movePosX, movePosY, movePosZ, speed);
	}
}