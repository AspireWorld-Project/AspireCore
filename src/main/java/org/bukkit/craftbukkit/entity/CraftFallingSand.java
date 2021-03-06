package org.bukkit.craftbukkit.entity;

import net.minecraft.entity.item.EntityFallingBlock;
import org.bukkit.Material;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingSand;

public class CraftFallingSand extends CraftEntity implements FallingSand {

	public CraftFallingSand(CraftServer server, net.minecraft.entity.item.EntityFallingBlock entity) {
		super(server, entity);
	}

	@Override
	public EntityFallingBlock getHandle() {
		return (EntityFallingBlock) entity;
	}

	@Override
	public String toString() {
		return "CraftFallingSand";
	}

	@Override
	public EntityType getType() {
		return EntityType.FALLING_BLOCK;
	}

	@Override
	public Material getMaterial() {
		return Material.getMaterial(getBlockId());
	}

	@Override
	public int getBlockId() {
		return CraftMagicNumbers.getId(getHandle().func_145805_f());
	}

	@Override
	public byte getBlockData() {
		return (byte) getHandle().field_145814_a;
	}

	@Override
	public boolean getDropItem() {
		return getHandle().field_145813_c;
	}

	@Override
	public void setDropItem(boolean drop) {
		getHandle().field_145813_c = drop;
	}
}
