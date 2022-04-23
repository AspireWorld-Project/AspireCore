package net.minecraft.command;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;

import java.util.List;

public class CommandTime extends CommandBase {
	private static final String __OBFID = "CL_00001183";

	@Override
	public String getCommandName() {
		return "time";
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}

	@Override
	public String getCommandUsage(ICommandSender p_71518_1_) {
		return "commands.time.usage";
	}

	@Override
	public void processCommand(ICommandSender p_71515_1_, String[] p_71515_2_) {
		if (p_71515_2_.length > 1) {
			int i;

			if (p_71515_2_[0].equals("set")) {
				if (p_71515_2_[1].equals("day")) {
					i = 1000;
				} else if (p_71515_2_[1].equals("night")) {
					i = 13000;
				} else {
					i = parseIntWithMin(p_71515_1_, p_71515_2_[1], 0);
				}

				setTime(p_71515_1_, i);
				func_152373_a(p_71515_1_, this, "commands.time.set", Integer.valueOf(i));
				return;
			}

			if (p_71515_2_[0].equals("add")) {
				i = parseIntWithMin(p_71515_1_, p_71515_2_[1], 0);
				addTime(p_71515_1_, i);
				func_152373_a(p_71515_1_, this, "commands.time.added", Integer.valueOf(i));
				return;
			}
		}

		throw new WrongUsageException("commands.time.usage");
	}

	@Override
	public List addTabCompletionOptions(ICommandSender p_71516_1_, String[] p_71516_2_) {
		return p_71516_2_.length == 1 ? getListOfStringsMatchingLastWord(p_71516_2_, "set", "add")
				: p_71516_2_.length == 2 && p_71516_2_[0].equals("set")
						? getListOfStringsMatchingLastWord(p_71516_2_, "day", "night")
						: null;
	}

	protected void setTime(ICommandSender p_71552_1_, int p_71552_2_) {
		for (int j = 0; j < MinecraftServer.getServer().worldServers.length; ++j) {
			MinecraftServer.getServer().worldServers[j].setWorldTime(p_71552_2_);
		}
	}

	protected void addTime(ICommandSender p_71553_1_, int p_71553_2_) {
		for (int j = 0; j < MinecraftServer.getServer().worldServers.length; ++j) {
			WorldServer worldserver = MinecraftServer.getServer().worldServers[j];
			worldserver.setWorldTime(worldserver.getWorldTime() + p_71553_2_);
		}
	}
}