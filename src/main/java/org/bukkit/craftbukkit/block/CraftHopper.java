package org.bukkit.craftbukkit.block;

import org.bukkit.block.Block;
import org.bukkit.block.Hopper;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.inventory.Inventory;

import net.minecraft.tileentity.TileEntityHopper;

public class CraftHopper extends CraftBlockState implements Hopper {
	private final TileEntityHopper hopper;

	public CraftHopper(final Block block) {
		super(block);

		hopper = (TileEntityHopper) ((CraftWorld) block.getWorld()).getTileEntityAt(getX(), getY(), getZ());
	}

	@Override
	public Inventory getInventory() {
		return new CraftInventory(hopper);
	}

	@Override
	public boolean update(boolean force, boolean applyPhysics) {
		boolean result = super.update(force, applyPhysics);

		if (result) {
			hopper.markDirty();
		}

		return result;
	}
}
