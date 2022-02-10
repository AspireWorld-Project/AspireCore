package net.minecraft.entity.ai;

import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.event.entity.EntityTargetEvent;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.util.MathHelper;

public class EntityAIArrowAttack extends EntityAIBase {
	private final EntityLiving entityHost;
	private final IRangedAttackMob rangedAttackEntityHost;
	private EntityLivingBase attackTarget;
	private int rangedAttackTime;
	private double entityMoveSpeed;
	private int field_75318_f;
	private int field_96561_g;
	private int maxRangedAttackTime;
	private float field_96562_i;
	private float field_82642_h;
	private static final String __OBFID = "CL_00001609";

	public EntityAIArrowAttack(IRangedAttackMob p_i1649_1_, double p_i1649_2_, int p_i1649_4_, float p_i1649_5_) {
		this(p_i1649_1_, p_i1649_2_, p_i1649_4_, p_i1649_4_, p_i1649_5_);
	}

	public EntityAIArrowAttack(IRangedAttackMob p_i1650_1_, double p_i1650_2_, int p_i1650_4_, int p_i1650_5_,
			float p_i1650_6_) {
		rangedAttackTime = -1;

		if (!(p_i1650_1_ instanceof EntityLivingBase))
			throw new IllegalArgumentException("ArrowAttackGoal requires Mob implements RangedAttackMob");
		else {
			rangedAttackEntityHost = p_i1650_1_;
			entityHost = (EntityLiving) p_i1650_1_;
			entityMoveSpeed = p_i1650_2_;
			field_96561_g = p_i1650_4_;
			maxRangedAttackTime = p_i1650_5_;
			field_96562_i = p_i1650_6_;
			field_82642_h = p_i1650_6_ * p_i1650_6_;
			setMutexBits(3);
		}
	}

	@Override
	public boolean shouldExecute() {
		EntityLivingBase entitylivingbase = entityHost.getAttackTarget();

		if (entitylivingbase == null)
			return false;
		else {
			attackTarget = entitylivingbase;
			return true;
		}
	}

	@Override
	public boolean continueExecuting() {
		return shouldExecute() || !entityHost.getNavigator().noPath();
	}

	@Override
	public void resetTask() {
		EntityTargetEvent.TargetReason reason = attackTarget.isEntityAlive()
				? EntityTargetEvent.TargetReason.FORGOT_TARGET
				: EntityTargetEvent.TargetReason.TARGET_DIED;
		CraftEventFactory.callEntityTargetEvent((Entity) rangedAttackEntityHost, null, reason);
		attackTarget = null;
		field_75318_f = 0;
		rangedAttackTime = -1;
	}

	@Override
	public void updateTask() {
		double d0 = entityHost.getDistanceSq(attackTarget.posX, attackTarget.boundingBox.minY, attackTarget.posZ);
		boolean flag = entityHost.getEntitySenses().canSee(attackTarget);

		if (flag) {
			++field_75318_f;
		} else {
			field_75318_f = 0;
		}

		if (d0 <= field_82642_h && field_75318_f >= 20) {
			entityHost.getNavigator().clearPathEntity();
		} else {
			entityHost.getNavigator().tryMoveToEntityLiving(attackTarget, entityMoveSpeed);
		}

		entityHost.getLookHelper().setLookPositionWithEntity(attackTarget, 30.0F, 30.0F);
		float f;

		if (--rangedAttackTime == 0) {
			if (d0 > field_82642_h || !flag)
				return;

			f = MathHelper.sqrt_double(d0) / field_96562_i;
			float f1 = f;

			if (f < 0.1F) {
				f1 = 0.1F;
			}

			if (f1 > 1.0F) {
				f1 = 1.0F;
			}

			rangedAttackEntityHost.attackEntityWithRangedAttack(attackTarget, f1);
			rangedAttackTime = MathHelper.floor_float(f * (maxRangedAttackTime - field_96561_g) + field_96561_g);
		} else if (rangedAttackTime < 0) {
			f = MathHelper.sqrt_double(d0) / field_96562_i;
			rangedAttackTime = MathHelper.floor_float(f * (maxRangedAttackTime - field_96561_g) + field_96561_g);
		}
	}
}