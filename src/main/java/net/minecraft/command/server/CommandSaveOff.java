package net.minecraft.command.server;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;

public class CommandSaveOff extends CommandBase {
	private static final String __OBFID = "CL_00000847";

	@Override
	public String getCommandName() {
		return "save-off";
	}

	@Override
	public String getCommandUsage(ICommandSender p_71518_1_) {
		return "commands.save-off.usage";
	}

	@Override
	public void processCommand(ICommandSender p_71515_1_, String[] p_71515_2_) {
		MinecraftServer minecraftserver = MinecraftServer.getServer();
		boolean flag = false;

		for (int i = 0; i < minecraftserver.worldServers.length; ++i) {
			if (minecraftserver.worldServers[i] != null) {
				WorldServer worldserver = minecraftserver.worldServers[i];

				if (!worldserver.levelSaving) {
					worldserver.levelSaving = true;
					flag = true;
				}
			}
		}

		if (flag) {
			func_152373_a(p_71515_1_, this, "commands.save.disabled");
		} else
			throw new CommandException("commands.save-off.alreadyOff");
	}
}