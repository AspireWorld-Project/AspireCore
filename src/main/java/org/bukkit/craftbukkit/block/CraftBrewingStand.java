package org.bukkit.craftbukkit.block;

import net.minecraft.tileentity.TileEntityBrewingStand;
import org.bukkit.block.Block;
import org.bukkit.block.BrewingStand;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.inventory.CraftInventoryBrewer;
import org.bukkit.inventory.BrewerInventory;

public class CraftBrewingStand extends CraftBlockState implements BrewingStand {
	private final TileEntityBrewingStand brewingStand;

	public CraftBrewingStand(Block block) {
		super(block);

		brewingStand = (TileEntityBrewingStand) ((CraftWorld) block.getWorld()).getTileEntityAt(getX(), getY(), getZ());
	}

	@Override
	public BrewerInventory getInventory() {
		return new CraftInventoryBrewer(brewingStand);
	}

	@Override
	public boolean update(boolean force, boolean applyPhysics) {
		boolean result = super.update(force, applyPhysics);

		if (result) {
			brewingStand.markDirty();
		}

		return result;
	}

	@Override
	public int getBrewingTime() {
		return brewingStand.getBrewTime();
	}

	@Override
	public void setBrewingTime(int brewTime) {
		brewingStand.setBrewTime(brewTime);
	}
}
