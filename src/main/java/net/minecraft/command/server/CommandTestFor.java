package net.minecraft.command.server;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;

public class CommandTestFor extends CommandBase {
	private static final String __OBFID = "CL_00001182";

	@Override
	public String getCommandName() {
		return "testfor";
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}

	@Override
	public String getCommandUsage(ICommandSender p_71518_1_) {
		return "commands.testfor.usage";
	}

	@Override
	public void processCommand(ICommandSender p_71515_1_, String[] p_71515_2_) {
		if (p_71515_2_.length != 1)
			throw new WrongUsageException("commands.testfor.usage");
		else if (!(p_71515_1_ instanceof CommandBlockLogic))
			throw new CommandException("commands.testfor.failed");
		else {
			getPlayer(p_71515_1_, p_71515_2_[0]);
		}
	}

	@Override
	public boolean isUsernameIndex(String[] p_82358_1_, int p_82358_2_) {
		return p_82358_2_ == 0;
	}
}