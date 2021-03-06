package net.minecraft.command.server;

import com.mojang.authlib.GameProfile;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;

import java.util.List;

public class CommandPardonPlayer extends CommandBase {
	private static final String __OBFID = "CL_00000747";

	@Override
	public String getCommandName() {
		return "pardon";
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 3;
	}

	@Override
	public String getCommandUsage(ICommandSender p_71518_1_) {
		return "commands.unban.usage";
	}

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender p_71519_1_) {
		return MinecraftServer.getServer().getConfigurationManager().func_152608_h().func_152689_b()
				&& super.canCommandSenderUseCommand(p_71519_1_);
	}

	@Override
	public void processCommand(ICommandSender p_71515_1_, String[] p_71515_2_) {
		if (p_71515_2_.length == 1 && p_71515_2_[0].length() > 0) {
			MinecraftServer minecraftserver = MinecraftServer.getServer();
			GameProfile gameprofile = minecraftserver.getConfigurationManager().func_152608_h()
					.func_152703_a(p_71515_2_[0]);

			if (gameprofile == null)
				throw new CommandException("commands.unban.failed", p_71515_2_[0]);
			else {
				minecraftserver.getConfigurationManager().func_152608_h().func_152684_c(gameprofile);
				func_152373_a(p_71515_1_, this, "commands.unban.success", p_71515_2_[0]);
			}
		} else
			throw new WrongUsageException("commands.unban.usage");
	}

	@Override
	public List addTabCompletionOptions(ICommandSender p_71516_1_, String[] p_71516_2_) {
		return p_71516_2_.length == 1
				? getListOfStringsMatchingLastWord(p_71516_2_,
						MinecraftServer.getServer().getConfigurationManager().func_152608_h().func_152685_a())
				: null;
	}
}