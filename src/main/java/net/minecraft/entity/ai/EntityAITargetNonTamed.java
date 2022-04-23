package net.minecraft.entity.ai;

import net.minecraft.entity.passive.EntityTameable;

public class EntityAITargetNonTamed extends EntityAINearestAttackableTarget {
	private final EntityTameable theTameable;
	private static final String __OBFID = "CL_00001623";

	public EntityAITargetNonTamed(EntityTameable p_i1666_1_, Class p_i1666_2_, int p_i1666_3_, boolean p_i1666_4_) {
		super(p_i1666_1_, p_i1666_2_, p_i1666_3_, p_i1666_4_);
		theTameable = p_i1666_1_;
	}

	@Override
	public boolean shouldExecute() {
		return !theTameable.isTamed() && super.shouldExecute();
	}
}