package org.bukkit.craftbukkit.entity;

import net.minecraft.entity.monster.EntityGhast;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Ghast;

public class CraftGhast extends CraftFlying implements Ghast {

	public CraftGhast(CraftServer server, net.minecraft.entity.monster.EntityGhast entity) {
		super(server, entity);
	}

	@Override
	public EntityGhast getHandle() {
		return (EntityGhast) entity;
	}

	@Override
	public String toString() {
		return "CraftGhast";
	}

	@Override
	public EntityType getType() {
		return EntityType.GHAST;
	}
}
