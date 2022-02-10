package net.minecraft.command;

import java.util.Iterator;

import org.ultramine.core.permissions.MinecraftPermissions;
import org.ultramine.core.permissions.Permissions;
import org.ultramine.core.service.InjectService;

import net.minecraft.command.server.CommandAchievement;
import net.minecraft.command.server.CommandBanIp;
import net.minecraft.command.server.CommandBanPlayer;
import net.minecraft.command.server.CommandBlockLogic;
import net.minecraft.command.server.CommandBroadcast;
import net.minecraft.command.server.CommandDeOp;
import net.minecraft.command.server.CommandEmote;
import net.minecraft.command.server.CommandListBans;
import net.minecraft.command.server.CommandListPlayers;
import net.minecraft.command.server.CommandMessage;
import net.minecraft.command.server.CommandMessageRaw;
import net.minecraft.command.server.CommandNetstat;
import net.minecraft.command.server.CommandOp;
import net.minecraft.command.server.CommandPardonIp;
import net.minecraft.command.server.CommandPardonPlayer;
import net.minecraft.command.server.CommandPublishLocalServer;
import net.minecraft.command.server.CommandSaveAll;
import net.minecraft.command.server.CommandSaveOff;
import net.minecraft.command.server.CommandSaveOn;
import net.minecraft.command.server.CommandScoreboard;
import net.minecraft.command.server.CommandSetBlock;
import net.minecraft.command.server.CommandSetDefaultSpawnpoint;
import net.minecraft.command.server.CommandStop;
import net.minecraft.command.server.CommandSummon;
import net.minecraft.command.server.CommandTeleport;
import net.minecraft.command.server.CommandTestFor;
import net.minecraft.command.server.CommandTestForBlock;
import net.minecraft.command.server.CommandWhitelist;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.rcon.RConConsoleSource;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;

public class ServerCommandManager extends CommandHandler implements IAdminCommand {
	private static final String __OBFID = "CL_00000922";
	@InjectService
	private static Permissions perms;

	public ServerCommandManager() {
		registerCommand(new CommandTime());
		registerCommand(new CommandGameMode());
		registerCommand(new CommandDifficulty());
		registerCommand(new CommandDefaultGameMode());
		registerCommand(new CommandKill());
		registerCommand(new CommandToggleDownfall());
		registerCommand(new CommandWeather());
		registerCommand(new CommandXP());
		registerCommand(new CommandTeleport());
		registerCommand(new CommandGive());
		registerCommand(new CommandEffect());
		registerCommand(new CommandEnchant());
		registerCommand(new CommandEmote());
		registerCommand(new CommandShowSeed());
		registerCommand(new CommandHelp());
		registerCommand(new CommandDebug());
		registerCommand(new CommandMessage());
		registerCommand(new CommandBroadcast());
		registerCommand(new CommandSetSpawnpoint());
		registerCommand(new CommandSetDefaultSpawnpoint());
		registerCommand(new CommandGameRule());
		registerCommand(new CommandClearInventory());
		registerCommand(new CommandTestFor());
		registerCommand(new CommandSpreadPlayers());
		registerCommand(new CommandPlaySound());
		registerCommand(new CommandScoreboard());
		registerCommand(new CommandAchievement());
		registerCommand(new CommandSummon());
		registerCommand(new CommandSetBlock());
		registerCommand(new CommandTestForBlock());
		registerCommand(new CommandMessageRaw());

		if (MinecraftServer.getServer().isDedicatedServer()) {
			registerCommand(new CommandOp());
			registerCommand(new CommandDeOp());
			registerCommand(new CommandStop());
			registerCommand(new CommandSaveAll());
			registerCommand(new CommandSaveOff());
			registerCommand(new CommandSaveOn());
			registerCommand(new CommandBanIp());
			registerCommand(new CommandPardonIp());
			registerCommand(new CommandBanPlayer());
			registerCommand(new CommandListBans());
			registerCommand(new CommandPardonPlayer());
			registerCommand(new CommandServerKick());
			registerCommand(new CommandListPlayers());
			registerCommand(new CommandWhitelist());
			registerCommand(new CommandSetPlayerTimeout());
			registerCommand(new CommandNetstat());
		} else {
			registerCommand(new CommandPublishLocalServer());
		}

		CommandBase.setAdminCommander(this);
	}

	@Override
	public void func_152372_a(ICommandSender p_152372_1_, ICommand p_152372_2_, int p_152372_3_, String p_152372_4_,
			Object... p_152372_5_) {
		notifyAdmins(p_152372_1_, p_152372_3_, p_152372_4_, p_152372_5_);
	}

	public void notifyAdmins(ICommandSender par1ICommandSender, int par2, String par3Str, Object... par4ArrayOfObj) {
		boolean flag = true;

		if (par1ICommandSender instanceof CommandBlockLogic && !MinecraftServer.getServer().worldServers[0]
				.getGameRules().getGameRuleBooleanValue("commandBlockOutput")) {
			flag = false;
		}

		ChatComponentTranslation chatcomponenttranslation = new ChatComponentTranslation("chat.type.admin",
				new Object[] { par1ICommandSender.getCommandSenderName(),
						new ChatComponentTranslation(par3Str, par4ArrayOfObj) });
		chatcomponenttranslation.getChatStyle().setColor(EnumChatFormatting.GRAY);
		chatcomponenttranslation.getChatStyle().setItalic(Boolean.valueOf(true));

		if (flag) {
			Iterator iterator = MinecraftServer.getServer().getConfigurationManager().playerEntityList.iterator();

			while (iterator.hasNext()) {
				EntityPlayerMP entityplayermp = (EntityPlayerMP) iterator.next();

				if (entityplayermp != par1ICommandSender
						&& perms.has(entityplayermp, MinecraftPermissions.COMMAND_NOTIFICATION)
						&& (!(par1ICommandSender instanceof RConConsoleSource)
								|| MinecraftServer.getServer().func_152363_m())) {
					entityplayermp.addChatMessage(chatcomponenttranslation);
				}
			}
		}

		if (par1ICommandSender != MinecraftServer.getServer()) {
			MinecraftServer.getServer().addChatMessage(chatcomponenttranslation);
		}

		if ((par2 & 1) != 1) {
			par1ICommandSender.addChatMessage(new ChatComponentTranslation(par3Str, par4ArrayOfObj));
		}
	}
}