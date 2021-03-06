package net.minecraft.command.server;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;

import java.util.List;

public class CommandEmote extends CommandBase {
	private static final String __OBFID = "CL_00000351";

	@Override
	public String getCommandName() {
		return "me";
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 0;
	}

	@Override
	public String getCommandUsage(ICommandSender p_71518_1_) {
		return "commands.me.usage";
	}

	@Override
	public void processCommand(ICommandSender p_71515_1_, String[] p_71515_2_) {
		if (p_71515_2_.length > 0) {
			IChatComponent ichatcomponent = func_147176_a(p_71515_1_, p_71515_2_, 0,
					p_71515_1_.canCommandSenderUseCommand(1, "me"));
			MinecraftServer.getServer().getConfigurationManager().sendChatMsg(new ChatComponentTranslation(
					"chat.type.emote", p_71515_1_.func_145748_c_(), ichatcomponent));
		} else
			throw new WrongUsageException("commands.me.usage");
	}

	@Override
	public List addTabCompletionOptions(ICommandSender p_71516_1_, String[] p_71516_2_) {
		return getListOfStringsMatchingLastWord(p_71516_2_, MinecraftServer.getServer().getAllUsernames());
	}
}