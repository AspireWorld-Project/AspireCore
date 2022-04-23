package net.minecraft.command.server;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.SyntaxErrorException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;

import java.util.List;
import java.util.regex.Matcher;

public class CommandPardonIp extends CommandBase {
	private static final String __OBFID = "CL_00000720";

	@Override
	public String getCommandName() {
		return "pardon-ip";
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 3;
	}

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender p_71519_1_) {
		return MinecraftServer.getServer().getConfigurationManager().getBannedIPs().func_152689_b()
				&& super.canCommandSenderUseCommand(p_71519_1_);
	}

	@Override
	public String getCommandUsage(ICommandSender p_71518_1_) {
		return "commands.unbanip.usage";
	}

	@Override
	public void processCommand(ICommandSender p_71515_1_, String[] p_71515_2_) {
		if (p_71515_2_.length == 1 && p_71515_2_[0].length() > 1) {
			Matcher matcher = CommandBanIp.field_147211_a.matcher(p_71515_2_[0]);

			if (matcher.matches()) {
				MinecraftServer.getServer().getConfigurationManager().getBannedIPs().func_152684_c(p_71515_2_[0]);
				func_152373_a(p_71515_1_, this, "commands.unbanip.success", p_71515_2_[0]);
			} else
				throw new SyntaxErrorException("commands.unbanip.invalid");
		} else
			throw new WrongUsageException("commands.unbanip.usage");
	}

	@Override
	public List addTabCompletionOptions(ICommandSender p_71516_1_, String[] p_71516_2_) {
		return p_71516_2_.length == 1
				? getListOfStringsMatchingLastWord(p_71516_2_,
						MinecraftServer.getServer().getConfigurationManager().getBannedIPs().func_152685_a())
				: null;
	}
}