package org.bukkit.craftbukkit.block;

import net.minecraft.tileentity.TileEntityCommandBlock;
import org.bukkit.block.Block;
import org.bukkit.block.CommandBlock;
import org.bukkit.craftbukkit.CraftWorld;

public class CraftCommandBlock extends CraftBlockState implements CommandBlock {
	private final TileEntityCommandBlock commandBlock;
	private String command;
	private String name;

	public CraftCommandBlock(Block block) {
		super(block);

		CraftWorld world = (CraftWorld) block.getWorld();
		commandBlock = (TileEntityCommandBlock) world.getTileEntityAt(getX(), getY(), getZ());
		command = commandBlock.func_145993_a().func_145753_i();
		name = commandBlock.func_145993_a().getCommandSenderName();
	}

	@Override
	public String getCommand() {
		return command;
	}

	@Override
	public void setCommand(String command) {
		this.command = command != null ? command : "";
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name != null ? name : "@";
	}

	@Override
	public boolean update(boolean force, boolean applyPhysics) {
		boolean result = super.update(force, applyPhysics);

		if (result) {
			commandBlock.func_145993_a().func_145752_a(command);
			commandBlock.func_145993_a().func_145754_b(name);
		}

		return result;
	}
}
