package net.minecraft.entity.ai;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;

public class EntityAINearestAttackableTarget extends EntityAITarget {
	private final Class targetClass;
	private final int targetChance;
	private final EntityAINearestAttackableTarget.Sorter theNearestAttackableTargetSorter;
	private final IEntitySelector targetEntitySelector;
	private EntityLivingBase targetEntity;
	private static final String __OBFID = "CL_00001620";

	public EntityAINearestAttackableTarget(EntityCreature p_i1663_1_, Class p_i1663_2_, int p_i1663_3_,
			boolean p_i1663_4_) {
		this(p_i1663_1_, p_i1663_2_, p_i1663_3_, p_i1663_4_, false);
	}

	public EntityAINearestAttackableTarget(EntityCreature p_i1664_1_, Class p_i1664_2_, int p_i1664_3_,
			boolean p_i1664_4_, boolean p_i1664_5_) {
		this(p_i1664_1_, p_i1664_2_, p_i1664_3_, p_i1664_4_, p_i1664_5_, (IEntitySelector) null);
	}

	public EntityAINearestAttackableTarget(EntityCreature p_i1665_1_, Class p_i1665_2_, int p_i1665_3_,
			boolean p_i1665_4_, boolean p_i1665_5_, final IEntitySelector p_i1665_6_) {
		super(p_i1665_1_, p_i1665_4_, p_i1665_5_);
		targetClass = p_i1665_2_;
		targetChance = p_i1665_3_;
		theNearestAttackableTargetSorter = new EntityAINearestAttackableTarget.Sorter(p_i1665_1_);
		setMutexBits(1);
		targetEntitySelector = new IEntitySelector() {
			private static final String __OBFID = "CL_00001621";

			@Override
			public boolean isEntityApplicable(Entity p_82704_1_) {
				return !(p_82704_1_ instanceof EntityLivingBase) ? false
						: p_i1665_6_ != null && !p_i1665_6_.isEntityApplicable(p_82704_1_) ? false
								: EntityAINearestAttackableTarget.this.isSuitableTarget((EntityLivingBase) p_82704_1_,
										false);
			}
		};
	}

	@Override
	public boolean shouldExecute() {
		if (targetChance > 0 && taskOwner.getRNG().nextInt(targetChance) != 0)
			return false;
		else {
			double d0 = getTargetDistance();
			List list = taskOwner.worldObj.selectEntitiesWithinAABB(targetClass,
					taskOwner.boundingBox.expand(d0, 4.0D, d0), targetEntitySelector);
			Collections.sort(list, theNearestAttackableTargetSorter);

			if (list.isEmpty())
				return false;
			else {
				targetEntity = (EntityLivingBase) list.get(0);
				return true;
			}
		}
	}

	@Override
	public void startExecuting() {
		taskOwner.setAttackTarget(targetEntity);
		super.startExecuting();
	}

	public static class Sorter implements Comparator {
		private final Entity theEntity;
		private static final String __OBFID = "CL_00001622";

		public Sorter(Entity p_i1662_1_) {
			theEntity = p_i1662_1_;
		}

		public int compare(Entity p_compare_1_, Entity p_compare_2_) {
			double d0 = theEntity.getDistanceSqToEntity(p_compare_1_);
			double d1 = theEntity.getDistanceSqToEntity(p_compare_2_);
			return d0 < d1 ? -1 : d0 > d1 ? 1 : 0;
		}

		@Override
		public int compare(Object p_compare_1_, Object p_compare_2_) {
			return this.compare((Entity) p_compare_1_, (Entity) p_compare_2_);
		}
	}
}