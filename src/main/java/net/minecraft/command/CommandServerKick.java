package net.minecraft.command;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

import java.util.List;

public class CommandServerKick extends CommandBase {
	private static final String __OBFID = "CL_00000550";

	@Override
	public String getCommandName() {
		return "kick";
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}

	@Override
	public String getCommandUsage(ICommandSender p_71518_1_) {
		return "commands.kick.usage";
	}

	@Override
	public void processCommand(ICommandSender p_71515_1_, String[] p_71515_2_) {
		if (p_71515_2_.length > 0 && p_71515_2_[0].length() > 1) {
			EntityPlayerMP entityplayermp = MinecraftServer.getServer().getConfigurationManager()
					.func_152612_a(p_71515_2_[0]);
			String s = "Kicked by an operator.";
			boolean flag = false;

			if (entityplayermp == null)
				throw new PlayerNotFoundException();
			else {
				if (p_71515_2_.length >= 2) {
					s = func_147178_a(p_71515_1_, p_71515_2_, 1).getUnformattedText();
					flag = true;
				}

				entityplayermp.playerNetServerHandler.kickPlayerFromServer(s);

				if (flag) {
					func_152373_a(p_71515_1_, this, "commands.kick.success.reason",
							entityplayermp.getCommandSenderName(), s);
				} else {
					func_152373_a(p_71515_1_, this, "commands.kick.success",
							entityplayermp.getCommandSenderName());
				}
			}
		} else
			throw new WrongUsageException("commands.kick.usage");
	}

	@Override
	public List addTabCompletionOptions(ICommandSender p_71516_1_, String[] p_71516_2_) {
		return p_71516_2_.length >= 1
				? getListOfStringsMatchingLastWord(p_71516_2_, MinecraftServer.getServer().getAllUsernames())
				: null;
	}
}