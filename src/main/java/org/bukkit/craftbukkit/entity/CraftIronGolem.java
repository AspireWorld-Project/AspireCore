package org.bukkit.craftbukkit.entity;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.IronGolem;

public class CraftIronGolem extends CraftGolem implements IronGolem {
	public CraftIronGolem(CraftServer server, net.minecraft.entity.monster.EntityIronGolem entity) {
		super(server, entity);
	}

	@Override
	public net.minecraft.entity.monster.EntityIronGolem getHandle() {
		return (net.minecraft.entity.monster.EntityIronGolem) entity;
	}

	@Override
	public String toString() {
		return "CraftIronGolem";
	}

	@Override
	public boolean isPlayerCreated() {
		return getHandle().isPlayerCreated();
	}

	@Override
	public void setPlayerCreated(boolean playerCreated) {
		getHandle().setPlayerCreated(playerCreated);
	}

	@Override
	public EntityType getType() {
		return EntityType.IRON_GOLEM;
	}
}
