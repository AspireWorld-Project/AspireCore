package org.bukkit.craftbukkit.entity;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

public class CraftItem extends CraftEntity implements Item {
	private final net.minecraft.entity.item.EntityItem item;

	public CraftItem(CraftServer server, net.minecraft.entity.Entity entity,
			net.minecraft.entity.item.EntityItem item) {
		super(server, entity);
		this.item = item;
	}

	public CraftItem(CraftServer server, net.minecraft.entity.item.EntityItem entity) {
		this(server, entity, entity);
	}

	@Override
	public ItemStack getItemStack() {
		return CraftItemStack.asCraftMirror(item.getEntityItem());
	}

	@Override
	public void setItemStack(ItemStack stack) {
		item.setEntityItemStack(CraftItemStack.asNMSCopy(stack));
	}

	@Override
	public int getPickupDelay() {
		return item.delayBeforeCanPickup;
	}

	@Override
	public void setPickupDelay(int delay) {
		item.delayBeforeCanPickup = delay;
	}

	@Override
	public String toString() {
		return "CraftItem";
	}

	@Override
	public EntityType getType() {
		return EntityType.DROPPED_ITEM;
	}
}
