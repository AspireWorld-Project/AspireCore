package net.minecraft.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityOwnable;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.MathHelper;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.event.entity.EntityTargetEvent;

public abstract class EntityAITarget extends EntityAIBase {
	protected EntityCreature taskOwner;
	protected boolean shouldCheckSight;
	private final boolean nearbyOnly;
	private int targetSearchStatus;
	private int targetSearchDelay;
	private int field_75298_g;
	private static final String __OBFID = "CL_00001626";

	public EntityAITarget(EntityCreature p_i1669_1_, boolean p_i1669_2_) {
		this(p_i1669_1_, p_i1669_2_, false);
	}

	public EntityAITarget(EntityCreature p_i1670_1_, boolean p_i1670_2_, boolean p_i1670_3_) {
		taskOwner = p_i1670_1_;
		shouldCheckSight = p_i1670_2_;
		nearbyOnly = p_i1670_3_;
	}

	@Override
	public boolean continueExecuting() {
		EntityLivingBase entitylivingbase = taskOwner.getAttackTarget();

		if (entitylivingbase == null)
			return false;
		else if (!entitylivingbase.isEntityAlive())
			return false;
		else {
			double d0 = getTargetDistance();

			if (taskOwner.getDistanceSqToEntity(entitylivingbase) > d0 * d0)
				return false;
			else {
				if (shouldCheckSight) {
					if (taskOwner.getEntitySenses().canSee(entitylivingbase)) {
						field_75298_g = 0;
					} else if (++field_75298_g > 60)
						return false;
				}

				return !(entitylivingbase instanceof EntityPlayerMP)
						|| !((EntityPlayerMP) entitylivingbase).theItemInWorldManager.isCreative();
			}
		}
	}

	protected double getTargetDistance() {
		IAttributeInstance iattributeinstance = taskOwner.getEntityAttribute(SharedMonsterAttributes.followRange);
		return iattributeinstance == null ? 16.0D : iattributeinstance.getAttributeValue();
	}

	@Override
	public void startExecuting() {
		targetSearchStatus = 0;
		targetSearchDelay = 0;
		field_75298_g = 0;
	}

	@Override
	public void resetTask() {
		taskOwner.setAttackTarget(null);
	}

	protected boolean isSuitableTarget(EntityLivingBase p_75296_1_, boolean p_75296_2_) {
		if (p_75296_1_ == null)
			return false;
		else if (p_75296_1_ == taskOwner)
			return false;
		else if (!p_75296_1_.isEntityAlive())
			return false;
		else if (!taskOwner.canAttackClass(p_75296_1_.getClass()))
			return false;
		else {
			if (taskOwner instanceof IEntityOwnable
					&& StringUtils.isNotEmpty(((IEntityOwnable) taskOwner).func_152113_b())) {
				if (p_75296_1_ instanceof IEntityOwnable && ((IEntityOwnable) taskOwner).func_152113_b()
						.equals(((IEntityOwnable) p_75296_1_).func_152113_b()))
					return false;

				if (p_75296_1_ == ((IEntityOwnable) taskOwner).getOwner())
					return false;
			} else if (p_75296_1_ instanceof EntityPlayer && !p_75296_2_
					&& ((EntityPlayer) p_75296_1_).capabilities.disableDamage)
				return false;

			if (!taskOwner.isWithinHomeDistance(MathHelper.floor_double(p_75296_1_.posX),
					MathHelper.floor_double(p_75296_1_.posY), MathHelper.floor_double(p_75296_1_.posZ)))
				return false;
			else if (shouldCheckSight && !taskOwner.getEntitySenses().canSee(p_75296_1_))
				return false;
			else {
				if (nearbyOnly) {
					if (--targetSearchDelay <= 0) {
						targetSearchStatus = 0;
					}

					if (targetSearchStatus == 0) {
						targetSearchStatus = canEasilyReach(p_75296_1_) ? 1 : 2;
					}

					if (targetSearchStatus == 2)
						return false;
				}

				return isSuitableTargetInject(p_75296_1_, p_75296_2_);
			}
		}
	}

	private boolean canEasilyReach(EntityLivingBase p_75295_1_) {
		targetSearchDelay = 10 + taskOwner.getRNG().nextInt(5);
		PathEntity pathentity = taskOwner.getNavigator().getPathToEntityLiving(p_75295_1_);

		if (pathentity == null)
			return false;
		else {
			PathPoint pathpoint = pathentity.getFinalPathPoint();

			if (pathpoint == null)
				return false;
			else {
				int i = pathpoint.xCoord - MathHelper.floor_double(p_75295_1_.posX);
				int j = pathpoint.zCoord - MathHelper.floor_double(p_75295_1_.posZ);
				return i * i + j * j <= 2.25D;
			}
		}
	}

	private boolean isSuitableTargetInject(EntityLivingBase p_75296_1_, boolean p_75296_2_) {
		EntityTargetEvent.TargetReason reason = EntityTargetEvent.TargetReason.RANDOM_TARGET;
		EntityAITarget thisObj = this;
		if (thisObj instanceof EntityAIDefendVillage) {
			reason = EntityTargetEvent.TargetReason.DEFEND_VILLAGE;
		} else if (thisObj instanceof EntityAIHurtByTarget) {
			reason = EntityTargetEvent.TargetReason.TARGET_ATTACKED_ENTITY;
		} else if (thisObj instanceof EntityAINearestAttackableTarget) {
			if (p_75296_1_ instanceof EntityPlayer) {
				reason = EntityTargetEvent.TargetReason.CLOSEST_PLAYER;
			}
		} else if (thisObj instanceof EntityAIOwnerHurtByTarget) {
			reason = EntityTargetEvent.TargetReason.TARGET_ATTACKED_OWNER;
		} else if (thisObj instanceof EntityAIOwnerHurtTarget) {
			reason = EntityTargetEvent.TargetReason.OWNER_ATTACKED_TARGET;
		}
		org.bukkit.event.entity.EntityTargetLivingEntityEvent event = org.bukkit.craftbukkit.event.CraftEventFactory
				.callEntityTargetLivingEvent(taskOwner, p_75296_1_, reason);
		if (event.isCancelled() || event.getTarget() == null)
			return false;
		else if (p_75296_1_.getBukkitEntity() != event.getTarget()) {
			taskOwner.setAttackTarget((EntityLivingBase) ((CraftEntity) event.getTarget()).getHandle());
		}
		if (taskOwner instanceof EntityCreature) {
			taskOwner.setEntityToAttack(((CraftEntity) event.getTarget()).getHandle());
		}
		return true;
	}
}