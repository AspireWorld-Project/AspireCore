package net.minecraftforge.cauldron.block;

import org.bukkit.block.Block;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.block.CraftBlockState;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import net.minecraft.inventory.IInventory;

public class CraftCustomContainer extends CraftBlockState implements InventoryHolder {
	private final CraftWorld world;
	private final net.minecraft.inventory.IInventory container;

	public CraftCustomContainer(Block block) {
		super(block);
		world = (CraftWorld) block.getWorld();
		container = (IInventory) world.getTileEntityAt(getX(), getY(), getZ());
	}

	@Override
	public Inventory getInventory() {
		CraftInventory inventory = new CraftInventory(container);
		return inventory;
	}
}
