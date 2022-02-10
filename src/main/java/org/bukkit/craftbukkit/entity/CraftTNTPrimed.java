package org.bukkit.craftbukkit.entity;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.TNTPrimed;

public class CraftTNTPrimed extends CraftEntity implements TNTPrimed {
	private float yield = 4;
	private boolean isIncendiary = false;

	public CraftTNTPrimed(CraftServer server, net.minecraft.entity.item.EntityTNTPrimed entity) {
		super(server, entity);
	}

	@Override
	public float getYield() {
		return yield;
	}

	@Override
	public boolean isIncendiary() {
		return isIncendiary;
	}

	@Override
	public void setIsIncendiary(boolean isIncendiary) {
		this.isIncendiary = isIncendiary;
	}

	@Override
	public void setYield(float yield) {
		this.yield = yield;
	}

	@Override
	public int getFuseTicks() {
		return getHandle().fuse;
	}

	@Override
	public void setFuseTicks(int fuseTicks) {
		getHandle().fuse = fuseTicks;
	}

	@Override
	public net.minecraft.entity.item.EntityTNTPrimed getHandle() {
		return (net.minecraft.entity.item.EntityTNTPrimed) entity;
	}

	@Override
	public String toString() {
		return "CraftTNTPrimed";
	}

	@Override
	public EntityType getType() {
		return EntityType.PRIMED_TNT;
	}

	@Override
	public Entity getSource() {
		net.minecraft.entity.EntityLivingBase source = getHandle().getTntPlacedBy();

		if (source != null) {
			Entity bukkitEntity = source.getBukkitEntity();

			if (bukkitEntity.isValid())
				return bukkitEntity;
		}

		return null;
	}
}
