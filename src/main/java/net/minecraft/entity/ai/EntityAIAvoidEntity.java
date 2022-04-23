package net.minecraft.entity.ai;

import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.Vec3;

import java.util.List;

public class EntityAIAvoidEntity extends EntityAIBase {
	public final IEntitySelector field_98218_a = new IEntitySelector() {
		private static final String __OBFID = "CL_00001575";

		@Override
		public boolean isEntityApplicable(Entity p_82704_1_) {
			return p_82704_1_.isEntityAlive() && theEntity.getEntitySenses().canSee(p_82704_1_);
		}
	};
	private final EntityCreature theEntity;
	private final double farSpeed;
	private final double nearSpeed;
	private Entity closestLivingEntity;
	private final float distanceFromEntity;
	private PathEntity entityPathEntity;
	private final PathNavigate entityPathNavigate;
	private final Class targetEntityClass;
	private static final String __OBFID = "CL_00001574";

	public EntityAIAvoidEntity(EntityCreature p_i1616_1_, Class p_i1616_2_, float p_i1616_3_, double p_i1616_4_,
			double p_i1616_6_) {
		theEntity = p_i1616_1_;
		targetEntityClass = p_i1616_2_;
		distanceFromEntity = p_i1616_3_;
		farSpeed = p_i1616_4_;
		nearSpeed = p_i1616_6_;
		entityPathNavigate = p_i1616_1_.getNavigator();
		setMutexBits(1);
	}

	@Override
	public boolean shouldExecute() {
		if (targetEntityClass == EntityPlayer.class) {
			if (theEntity instanceof EntityTameable && ((EntityTameable) theEntity).isTamed())
				return false;

			closestLivingEntity = theEntity.worldObj.getClosestPlayerToEntity(theEntity, distanceFromEntity);

			if (closestLivingEntity == null)
				return false;
		} else {
			List list = theEntity.worldObj.selectEntitiesWithinAABB(targetEntityClass,
					theEntity.boundingBox.expand(distanceFromEntity, 3.0D, distanceFromEntity), field_98218_a);

			if (list.isEmpty())
				return false;

			closestLivingEntity = (Entity) list.get(0);
		}

		Vec3 vec3 = RandomPositionGenerator.findRandomTargetBlockAwayFrom(theEntity, 16, 7,
				Vec3.createVectorHelper(closestLivingEntity.posX, closestLivingEntity.posY, closestLivingEntity.posZ));

		if (vec3 == null)
			return false;
		else if (closestLivingEntity.getDistanceSq(vec3.xCoord, vec3.yCoord, vec3.zCoord) < closestLivingEntity
				.getDistanceSqToEntity(theEntity))
			return false;
		else {
			entityPathEntity = entityPathNavigate.getPathToXYZ(vec3.xCoord, vec3.yCoord, vec3.zCoord);
			return entityPathEntity != null && entityPathEntity.isDestinationSame(vec3);
		}
	}

	@Override
	public boolean continueExecuting() {
		return !entityPathNavigate.noPath();
	}

	@Override
	public void startExecuting() {
		entityPathNavigate.setPath(entityPathEntity, farSpeed);
	}

	@Override
	public void resetTask() {
		closestLivingEntity = null;
	}

	@Override
	public void updateTask() {
		if (theEntity.getDistanceSqToEntity(closestLivingEntity) < 49.0D) {
			theEntity.getNavigator().setSpeed(nearSpeed);
		} else {
			theEntity.getNavigator().setSpeed(farSpeed);
		}
	}
}