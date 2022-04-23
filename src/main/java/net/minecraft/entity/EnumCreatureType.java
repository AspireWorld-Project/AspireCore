package net.minecraft.entity;

import net.minecraft.block.material.Material;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityAmbientCreature;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityWaterMob;

public enum EnumCreatureType {
	monster(IMob.class, 70, Material.air, false, false), creature(EntityAnimal.class, 10, Material.air, true,
			true), ambient(EntityAmbientCreature.class, 15, Material.air, true,
					false), waterCreature(EntityWaterMob.class, 5, Material.water, true, false);
	private final Class creatureClass;
	private final int maxNumberOfCreature;
	private final Material creatureMaterial;
	private final boolean isPeacefulCreature;
	private final boolean isAnimal;

	private static final String __OBFID = "CL_00001551";

	EnumCreatureType(Class p_i1596_3_, int p_i1596_4_, Material p_i1596_5_, boolean p_i1596_6_,
                     boolean p_i1596_7_) {
		creatureClass = p_i1596_3_;
		maxNumberOfCreature = p_i1596_4_;
		creatureMaterial = p_i1596_5_;
		isPeacefulCreature = p_i1596_6_;
		isAnimal = p_i1596_7_;
	}

	public Class getCreatureClass() {
		return creatureClass;
	}

	public int getMaxNumberOfCreature() {
		return maxNumberOfCreature;
	}

	public Material getCreatureMaterial() {
		return creatureMaterial;
	}

	public boolean getPeacefulCreature() {
		return isPeacefulCreature;
	}

	public boolean getAnimal() {
		return isAnimal;
	}
}