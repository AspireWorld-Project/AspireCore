package org.bukkit.craftbukkit.inventory;

import org.bukkit.inventory.HorseInventory;
import org.bukkit.inventory.ItemStack;

public class CraftInventoryHorse extends CraftInventory implements HorseInventory {

	public CraftInventoryHorse(net.minecraft.inventory.IInventory inventory) {
		super(inventory);
	}

	@Override
	public ItemStack getSaddle() {
		return getItem(0);
	}

	@Override
	public ItemStack getArmor() {
		return getItem(1);
	}

	@Override
	public void setSaddle(ItemStack stack) {
		setItem(0, stack);
	}

	@Override
	public void setArmor(ItemStack stack) {
		setItem(1, stack);
	}
}
