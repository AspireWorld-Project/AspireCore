package org.bukkit.craftbukkit.entity;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Slime;

public class CraftSlime extends CraftLivingEntity implements Slime {

	public CraftSlime(CraftServer server, net.minecraft.entity.monster.EntitySlime entity) {
		super(server, entity);
	}

	@Override
	public int getSize() {
		return getHandle().getSlimeSize();
	}

	@Override
	public void setSize(int size) {
		getHandle().setSlimeSizePub(size);
	}

	@Override
	public net.minecraft.entity.monster.EntitySlime getHandle() {
		return (net.minecraft.entity.monster.EntitySlime) entity;
	}

	@Override
	public String toString() {
		return "CraftSlime";
	}

	@Override
	public EntityType getType() {
		return EntityType.SLIME;
	}
}
