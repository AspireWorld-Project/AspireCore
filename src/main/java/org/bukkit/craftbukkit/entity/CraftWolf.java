package org.bukkit.craftbukkit.entity;

import org.bukkit.DyeColor;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Wolf;

public class CraftWolf extends CraftTameableAnimal implements Wolf {
	public CraftWolf(CraftServer server, net.minecraft.entity.passive.EntityWolf wolf) {
		super(server, wolf);
	}

	@Override
	public boolean isAngry() {
		return getHandle().isAngry();
	}

	@Override
	public void setAngry(boolean angry) {
		getHandle().setAngry(angry);
	}

	@Override
	public net.minecraft.entity.passive.EntityWolf getHandle() {
		return (net.minecraft.entity.passive.EntityWolf) entity;
	}

	@Override
	public EntityType getType() {
		return EntityType.WOLF;
	}

	@Override
	public DyeColor getCollarColor() {
		return DyeColor.getByWoolData((byte) getHandle().getCollarColor());
	}

	@Override
	public void setCollarColor(DyeColor color) {
		getHandle().setCollarColor(color.getWoolData());
	}
}
