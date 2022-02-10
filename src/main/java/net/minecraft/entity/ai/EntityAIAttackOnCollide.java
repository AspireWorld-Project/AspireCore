package net.minecraft.entity.ai;

import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.event.entity.EntityTargetEvent;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityAIAttackOnCollide extends EntityAIBase {
	World worldObj;
	EntityCreature attacker;
	int attackTick;
	double speedTowardsTarget;
	boolean longMemory;
	PathEntity entityPathEntity;
	Class classTarget;
	private int field_75445_i;
	private double field_151497_i;
	private double field_151495_j;
	private double field_151496_k;
	private static final String __OBFID = "CL_00001595";

	private int failedPathFindingPenalty;

	public EntityAIAttackOnCollide(EntityCreature p_i1635_1_, Class p_i1635_2_, double p_i1635_3_, boolean p_i1635_5_) {
		this(p_i1635_1_, p_i1635_3_, p_i1635_5_);
		classTarget = p_i1635_2_;
	}

	public EntityAIAttackOnCollide(EntityCreature p_i1636_1_, double p_i1636_2_, boolean p_i1636_4_) {
		attacker = p_i1636_1_;
		worldObj = p_i1636_1_.worldObj;
		speedTowardsTarget = p_i1636_2_;
		longMemory = p_i1636_4_;
		setMutexBits(3);
	}

	@Override
	public boolean shouldExecute() {
		EntityLivingBase entitylivingbase = attacker.getAttackTarget();

		if (entitylivingbase == null)
			return false;
		else if (!entitylivingbase.isEntityAlive())
			return false;
		else if (classTarget != null && !classTarget.isAssignableFrom(entitylivingbase.getClass()))
			return false;
		else {
			if (--field_75445_i <= 0) {
				entityPathEntity = attacker.getNavigator().getPathToEntityLiving(entitylivingbase);
				field_75445_i = 4 + attacker.getRNG().nextInt(7);
				return entityPathEntity != null;
			} else
				return true;
		}
	}

	@Override
	public boolean continueExecuting() {
		EntityLivingBase entitylivingbase = attacker.getAttackTarget();
		EntityTargetEvent.TargetReason reason = attacker.getAttackTarget() == null
				? EntityTargetEvent.TargetReason.FORGOT_TARGET
				: EntityTargetEvent.TargetReason.TARGET_DIED;
		if (attacker.getAttackTarget() == null
				|| attacker.getAttackTarget() != null && !attacker.getAttackTarget().isEntityAlive()) {
			CraftEventFactory.callEntityTargetEvent(attacker, null, reason);
		}
		return entitylivingbase == null ? false
				: !entitylivingbase.isEntityAlive() ? false
						: !longMemory ? !attacker.getNavigator().noPath()
								: attacker.isWithinHomeDistance(MathHelper.floor_double(entitylivingbase.posX),
										MathHelper.floor_double(entitylivingbase.posY),
										MathHelper.floor_double(entitylivingbase.posZ));
	}

	@Override
	public void startExecuting() {
		attacker.getNavigator().setPath(entityPathEntity, speedTowardsTarget);
		field_75445_i = 0;
	}

	@Override
	public void resetTask() {
		attacker.getNavigator().clearPathEntity();
	}

	@Override
	public void updateTask() {
		EntityLivingBase entitylivingbase = attacker.getAttackTarget();
		attacker.getLookHelper().setLookPositionWithEntity(entitylivingbase, 30.0F, 30.0F);
		double d0 = attacker.getDistanceSq(entitylivingbase.posX, entitylivingbase.boundingBox.minY,
				entitylivingbase.posZ);
		double d1 = attacker.width * 2.0F * attacker.width * 2.0F + entitylivingbase.width;
		--field_75445_i;

		if ((longMemory || attacker.getEntitySenses().canSee(entitylivingbase)) && field_75445_i <= 0
				&& (field_151497_i == 0.0D && field_151495_j == 0.0D && field_151496_k == 0.0D
						|| entitylivingbase.getDistanceSq(field_151497_i, field_151495_j, field_151496_k) >= 1.0D
						|| attacker.getRNG().nextFloat() < 0.05F)) {
			field_151497_i = entitylivingbase.posX;
			field_151495_j = entitylivingbase.boundingBox.minY;
			field_151496_k = entitylivingbase.posZ;
			field_75445_i = failedPathFindingPenalty + 4 + attacker.getRNG().nextInt(7);

			if (attacker.getNavigator().getPath() != null) {
				PathPoint finalPathPoint = attacker.getNavigator().getPath().getFinalPathPoint();
				if (finalPathPoint != null && entitylivingbase.getDistanceSq(finalPathPoint.xCoord,
						finalPathPoint.yCoord, finalPathPoint.zCoord) < 1) {
					failedPathFindingPenalty = 0;
				} else {
					failedPathFindingPenalty += 10;
				}
			} else {
				failedPathFindingPenalty += 10;
			}

			if (d0 > 1024.0D) {
				field_75445_i += 10;
			} else if (d0 > 256.0D) {
				field_75445_i += 5;
			}

			if (!attacker.getNavigator().tryMoveToEntityLiving(entitylivingbase, speedTowardsTarget)) {
				field_75445_i += 15;
			}
		}

		attackTick = Math.max(attackTick - 1, 0);

		if (d0 <= d1 && attackTick <= 20) {
			attackTick = 20;

			if (attacker.getHeldItem() != null) {
				attacker.swingItem();
			}

			attacker.attackEntityAsMob(entitylivingbase);
		}
	}
}