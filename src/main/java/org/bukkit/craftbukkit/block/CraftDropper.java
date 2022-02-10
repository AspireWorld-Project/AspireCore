package org.bukkit.craftbukkit.block;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Dropper;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.inventory.Inventory;

import net.minecraft.block.BlockDropper;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntityDropper;

public class CraftDropper extends CraftBlockState implements Dropper {
	private final CraftWorld world;
	private final TileEntityDropper dropper;

	public CraftDropper(final Block block) {
		super(block);

		world = (CraftWorld) block.getWorld();
		dropper = (TileEntityDropper) world.getTileEntityAt(getX(), getY(), getZ());
	}

	@Override
	public Inventory getInventory() {
		return new CraftInventory(dropper);
	}

	@Override
	public void drop() {
		Block block = getBlock();

		if (block.getType() == Material.DROPPER) {
			BlockDropper drop = (BlockDropper) Blocks.dropper;

			// drop.func_149941_e(world.getHandle(), getX(), getY(), getZ());
			drop.updateTick(world.getHandle(), getX(), getY(), getZ(), null);
		}
	}

	@Override
	public boolean update(boolean force, boolean applyPhysics) {
		boolean result = super.update(force, applyPhysics);

		if (result) {
			dropper.markDirty();
		}

		return result;
	}
}
