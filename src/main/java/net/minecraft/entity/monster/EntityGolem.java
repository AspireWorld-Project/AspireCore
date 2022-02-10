package net.minecraft.entity.monster;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.world.World;

public abstract class EntityGolem extends EntityCreature implements IAnimals {
	private static final String __OBFID = "CL_00001644";

	public EntityGolem(World p_i1686_1_) {
		super(p_i1686_1_);
	}

	@Override
	protected void fall(float p_70069_1_) {
	}

	@Override
	protected String getLivingSound() {
		return "none";
	}

	@Override
	protected String getHurtSound() {
		return "none";
	}

	@Override
	protected String getDeathSound() {
		return "none";
	}

	@Override
	public int getTalkInterval() {
		return 120;
	}

	@Override
	protected boolean canDespawn() {
		return false;
	}

	/*
	 * ===================================== ULTRAMINE START
	 * =====================================
	 */

	@Override
	public org.ultramine.server.EntityType computeEntityType() {
		return org.ultramine.server.EntityType.ANIMAL;
	}
}