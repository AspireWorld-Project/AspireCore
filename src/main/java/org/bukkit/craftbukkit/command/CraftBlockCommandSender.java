package org.bukkit.craftbukkit.command;

import org.bukkit.block.Block;
import org.bukkit.command.BlockCommandSender;

import net.minecraft.command.ICommandSender;
import net.minecraft.command.server.CommandBlockLogic;

/**
 * Represents input from a command block
 */
public class CraftBlockCommandSender extends ServerCommandSender implements BlockCommandSender {
	private final CommandBlockLogic commandBlock;

	public CraftBlockCommandSender(CommandBlockLogic commandBlockListenerAbstract) {
		super();
		commandBlock = commandBlockListenerAbstract;
	}

	@Override
	public Block getBlock() {
		return commandBlock.getEntityWorld().getWorld().getBlockAt(commandBlock.getPlayerCoordinates().posX,
				commandBlock.getPlayerCoordinates().posY, commandBlock.getPlayerCoordinates().posZ);
	}

	@Override
	public void sendMessage(String message) {
	}

	@Override
	public void sendMessage(String[] messages) {
	}

	@Override
	public String getName() {
		return commandBlock.getCommandSenderName();
	}

	@Override
	public boolean isOp() {
		return true;
	}

	@Override
	public void setOp(boolean value) {
		throw new UnsupportedOperationException("Cannot change operator status of a block");
	}

	public ICommandSender getTileEntity() {
		return commandBlock;
	}
}
