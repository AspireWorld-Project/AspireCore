package org.bukkit.craftbukkit.entity;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.PigZombie;

public class CraftPigZombie extends CraftZombie implements PigZombie {

	public CraftPigZombie(CraftServer server, net.minecraft.entity.monster.EntityPigZombie entity) {
		super(server, entity);
	}

	@Override
	public int getAnger() {
		return getHandle().getAngerLevel();
	}

	@Override
	public void setAnger(int level) {
		getHandle().setAngerLevel(level);
	}

	@Override
	public void setAngry(boolean angry) {
		setAnger(angry ? 400 : 0);
	}

	@Override
	public boolean isAngry() {
		return getAnger() > 0;
	}

	@Override
	public net.minecraft.entity.monster.EntityPigZombie getHandle() {
		return (net.minecraft.entity.monster.EntityPigZombie) entity;
	}

	@Override
	public String toString() {
		return "CraftPigZombie";
	}

	@Override
	public EntityType getType() {
		return EntityType.PIG_ZOMBIE;
	}
}
