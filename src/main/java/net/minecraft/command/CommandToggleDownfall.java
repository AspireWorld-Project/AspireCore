package net.minecraft.command;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.storage.WorldInfo;

public class CommandToggleDownfall extends CommandBase {
	private static final String __OBFID = "CL_00001184";

	@Override
	public String getCommandName() {
		return "toggledownfall";
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}

	@Override
	public String getCommandUsage(ICommandSender p_71518_1_) {
		return "commands.downfall.usage";
	}

	@Override
	public void processCommand(ICommandSender p_71515_1_, String[] p_71515_2_) {
		toggleDownfall();
		func_152373_a(p_71515_1_, this, "commands.downfall.success", new Object[0]);
	}

	protected void toggleDownfall() {
		WorldInfo worldinfo = MinecraftServer.getServer().worldServers[0].getWorldInfo();
		worldinfo.setRaining(!worldinfo.isRaining());
	}
}