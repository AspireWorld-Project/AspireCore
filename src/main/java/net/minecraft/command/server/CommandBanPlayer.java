package net.minecraft.command.server;

import com.mojang.authlib.GameProfile;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.UserListBansEntry;

import java.util.Date;
import java.util.List;

public class CommandBanPlayer extends CommandBase {
	private static final String __OBFID = "CL_00000165";

	@Override
	public String getCommandName() {
		return "ban";
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 3;
	}

	@Override
	public String getCommandUsage(ICommandSender p_71518_1_) {
		return "commands.ban.usage";
	}

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender p_71519_1_) {
		return MinecraftServer.getServer().getConfigurationManager().func_152608_h().func_152689_b()
				&& super.canCommandSenderUseCommand(p_71519_1_);
	}

	@Override
	public void processCommand(ICommandSender p_71515_1_, String[] p_71515_2_) {
		if (p_71515_2_.length >= 1 && p_71515_2_[0].length() > 0) {
			MinecraftServer minecraftserver = MinecraftServer.getServer();
			GameProfile gameprofile = minecraftserver.func_152358_ax().func_152655_a(p_71515_2_[0]);

			if (gameprofile == null)
				throw new CommandException("commands.ban.failed", p_71515_2_[0]);
			else {
				String s = null;

				if (p_71515_2_.length >= 2) {
					s = func_147178_a(p_71515_1_, p_71515_2_, 1).getUnformattedText();
				}

				UserListBansEntry userlistbansentry = new UserListBansEntry(gameprofile, null,
						p_71515_1_.getCommandSenderName(), null, s);
				minecraftserver.getConfigurationManager().func_152608_h().func_152687_a(userlistbansentry);
				EntityPlayerMP entityplayermp = minecraftserver.getConfigurationManager().func_152612_a(p_71515_2_[0]);

				if (entityplayermp != null) {
					entityplayermp.playerNetServerHandler.kickPlayerFromServer("You are banned from this server.");
				}

				func_152373_a(p_71515_1_, this, "commands.ban.success", p_71515_2_[0]);
			}
		} else
			throw new WrongUsageException("commands.ban.usage");
	}

	@Override
	public List addTabCompletionOptions(ICommandSender p_71516_1_, String[] p_71516_2_) {
		return p_71516_2_.length >= 1
				? getListOfStringsMatchingLastWord(p_71516_2_, MinecraftServer.getServer().getAllUsernames())
				: null;
	}
}