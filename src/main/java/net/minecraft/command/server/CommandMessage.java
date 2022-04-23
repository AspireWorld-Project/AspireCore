package net.minecraft.command.server;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

import java.util.Arrays;
import java.util.List;

public class CommandMessage extends CommandBase {
	private static final String __OBFID = "CL_00000641";

	@Override
	public List getCommandAliases() {
		return Arrays.asList("w", "msg");
	}

	@Override
	public String getCommandName() {
		return "tell";
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 0;
	}

	@Override
	public String getCommandUsage(ICommandSender p_71518_1_) {
		return "commands.message.usage";
	}

	@Override
	public void processCommand(ICommandSender p_71515_1_, String[] p_71515_2_) {
		if (p_71515_2_.length < 2)
			throw new WrongUsageException("commands.message.usage");
		else {
			EntityPlayerMP entityplayermp = getPlayer(p_71515_1_, p_71515_2_[0]);

			if (entityplayermp == null)
				throw new PlayerNotFoundException();
			else if (entityplayermp == p_71515_1_)
				throw new PlayerNotFoundException("commands.message.sameTarget");
			else {
				IChatComponent ichatcomponent = func_147176_a(p_71515_1_, p_71515_2_, 1,
						!(p_71515_1_ instanceof EntityPlayer));
				ChatComponentTranslation chatcomponenttranslation = new ChatComponentTranslation(
						"commands.message.display.incoming",
						p_71515_1_.func_145748_c_(), ichatcomponent.createCopy());
				ChatComponentTranslation chatcomponenttranslation1 = new ChatComponentTranslation(
						"commands.message.display.outgoing",
						entityplayermp.func_145748_c_(), ichatcomponent.createCopy());
				chatcomponenttranslation.getChatStyle().setColor(EnumChatFormatting.GRAY)
						.setItalic(Boolean.valueOf(true));
				chatcomponenttranslation1.getChatStyle().setColor(EnumChatFormatting.GRAY)
						.setItalic(Boolean.valueOf(true));
				entityplayermp.addChatMessage(chatcomponenttranslation);
				p_71515_1_.addChatMessage(chatcomponenttranslation1);
			}
		}
	}

	@Override
	public List addTabCompletionOptions(ICommandSender p_71516_1_, String[] p_71516_2_) {
		return getListOfStringsMatchingLastWord(p_71516_2_, MinecraftServer.getServer().getAllUsernames());
	}

	@Override
	public boolean isUsernameIndex(String[] p_82358_1_, int p_82358_2_) {
		return p_82358_2_ == 0;
	}
}