package org.bukkit.craftbukkit.block;

import net.minecraft.block.BlockDispenser;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntityDispenser;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Dispenser;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.craftbukkit.projectiles.CraftBlockProjectileSource;
import org.bukkit.inventory.Inventory;
import org.bukkit.projectiles.BlockProjectileSource;

public class CraftDispenser extends CraftBlockState implements Dispenser {
	private final CraftWorld world;
	private final TileEntityDispenser dispenser;

	public CraftDispenser(final Block block) {
		super(block);

		world = (CraftWorld) block.getWorld();
		dispenser = (TileEntityDispenser) world.getTileEntityAt(getX(), getY(), getZ());
	}

	@Override
	public Inventory getInventory() {
		return new CraftInventory(dispenser);
	}

	@Override
	public BlockProjectileSource getBlockProjectileSource() {
		Block block = getBlock();

		if (block.getType() != Material.DISPENSER)
			return null;

		return new CraftBlockProjectileSource(dispenser);
	}

	@Override
	public boolean dispense() {
		Block block = getBlock();

		if (block.getType() == Material.DISPENSER) {
			BlockDispenser dispense = (BlockDispenser) Blocks.dispenser;

			// dispense.func_149941_e(world.getHandle(), getX(), getY(), getZ());
			dispense.updateTick(world.getHandle(), getX(), getY(), getZ(), null);
			return true;
		} else
			return false;
	}

	@Override
	public boolean update(boolean force, boolean applyPhysics) {
		boolean result = super.update(force, applyPhysics);

		if (result) {
			dispenser.markDirty();
		}

		return result;
	}
}
