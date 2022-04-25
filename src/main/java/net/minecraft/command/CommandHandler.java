package net.minecraft.command;

import net.minecraft.command.server.CommandBlockLogic;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.rcon.RConConsoleSource;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.CommandEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.command.CraftBlockCommandSender;
import org.bukkit.craftbukkit.command.CraftRemoteConsoleCommandSender;
import org.bukkit.craftbukkit.entity.CraftMinecartCommand;
import org.bukkit.craftbukkit.util.LazyPlayerSet;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.RemoteServerCommandEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.ultramine.commands.CommandRegistry;
import org.ultramine.server.event.WorldEventProxy;
import org.ultramine.server.event.WorldUpdateObject;
import org.ultramine.server.event.WorldUpdateObjectType;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class CommandHandler implements ICommandManager {
	private static final Logger logger = LogManager.getLogger();
	private final CommandRegistry registry = new CommandRegistry();
	private final Map commandMap = registry.getCommandMap();
	private final Set commandSet = registry.getCommandSet();
	private static final String __OBFID = "CL_00001765";

	@Override
	public int executeCommand(ICommandSender sender, String line) {
		line = line.trim();

		CraftServer server = (CraftServer) Bukkit.getServer();
		CommandSender bukkitSender;
		boolean tryConversation = false;
		if (sender instanceof EntityPlayerMP) {
			PlayerCommandPreprocessEvent event = new PlayerCommandPreprocessEvent(
					((EntityPlayerMP) sender).getBukkitEntity(), line, new LazyPlayerSet());
			Bukkit.getServer().getPluginManager().callEvent(event);
			if (event.isCancelled())
				return 0;
			line = event.getMessage();
			bukkitSender = ((EntityPlayerMP) sender).getBukkitEntity();
		} else if (sender instanceof CommandBlockLogic) {
			WorldUpdateObject wuo = WorldEventProxy.getCurrent().getUpdateObject();
			if (wuo.getType() == WorldUpdateObjectType.TILEE_ENTITY) {
				bukkitSender = new CraftBlockCommandSender((CommandBlockLogic) sender);
			} else if (wuo.getType() == WorldUpdateObjectType.ENTITY) {
				bukkitSender = new CraftMinecartCommand(server,
						(net.minecraft.entity.EntityMinecartCommandBlock) wuo.getEntity());
			} else {
				bukkitSender = server.getConsoleSender();
			}
		} else if (sender instanceof RConConsoleSource) {
			bukkitSender = CraftRemoteConsoleCommandSender.getInstance();
			RemoteServerCommandEvent event = new RemoteServerCommandEvent(bukkitSender, line);
			server.getPluginManager().callEvent(event);
			line = event.getCommand();
			tryConversation = true;
		} else {
			ServerCommandEvent event = new ServerCommandEvent(server.getConsoleSender(), line);
			server.getPluginManager().callEvent(event);
			line = event.getCommand();
			bukkitSender = server.getConsoleSender();
			tryConversation = true;
		}

		String trimmedLine = line;
		if (line.startsWith("/")) {
			trimmedLine = line.substring(1);
		}

		if (tryConversation && server.tryConversation(bukkitSender, trimmedLine))
			return 1;

		line = line.trim();

		if (line.startsWith("/")) {
			line = line.substring(1);
		}

		String[] astring = line.split(" ");
		String s1 = astring[0];
		astring = dropFirstString(astring);
		ICommand icommand = registry.get(s1);
		int i = getUsernameIndex(icommand, astring);
		int j = 0;
		ChatComponentTranslation chatcomponenttranslation;

		try {
			if (icommand == null)
				throw new CommandNotFoundException();

			if (icommand.canCommandSenderUseCommand(sender)) {
				CommandEvent event = new CommandEvent(icommand, sender, astring);
				if (MinecraftForge.EVENT_BUS.post(event)) {
					if (event.exception != null)
						throw event.exception;
					return 1;
				}

				if (i > -1) {
					EntityPlayerMP[] aentityplayermp = PlayerSelector.matchPlayers(sender, astring[i]);
					String s2 = astring[i];
					EntityPlayerMP[] aentityplayermp1 = aentityplayermp;
					int k = aentityplayermp.length;

					for (int l = 0; l < k; ++l) {
						EntityPlayerMP entityplayermp = aentityplayermp1[l];
						astring[i] = entityplayermp.getCommandSenderName();

						try {
							icommand.processCommand(sender, astring);
							++j;
						} catch (CommandException commandexception) {
							ChatComponentTranslation chatcomponenttranslation1 = new ChatComponentTranslation(
									commandexception.getMessage(), commandexception.getErrorOjbects());
							chatcomponenttranslation1.getChatStyle().setColor(EnumChatFormatting.RED);
							sender.addChatMessage(chatcomponenttranslation1);
						}
					}

					astring[i] = s2;
				} else {
					icommand.processCommand(sender, astring);
					++j;
				}
			} else {
				ChatComponentTranslation chatcomponenttranslation2 = new ChatComponentTranslation(
						"commands.generic.permission");
				chatcomponenttranslation2.getChatStyle().setColor(EnumChatFormatting.RED);
				sender.addChatMessage(chatcomponenttranslation2);
			}
		} catch (WrongUsageException wrongusageexception) {
			chatcomponenttranslation = new ChatComponentTranslation("commands.generic.usage",
					new ChatComponentTranslation(wrongusageexception.getMessage(),
							wrongusageexception.getErrorOjbects()));
			chatcomponenttranslation.getChatStyle().setColor(EnumChatFormatting.RED);
			sender.addChatMessage(chatcomponenttranslation);
		} catch (CommandException commandexception1) {
			chatcomponenttranslation = new ChatComponentTranslation(commandexception1.getMessage(),
					commandexception1.getErrorOjbects());
			chatcomponenttranslation.getChatStyle().setColor(EnumChatFormatting.RED);
			sender.addChatMessage(chatcomponenttranslation);
		} catch (Throwable throwable) {
			chatcomponenttranslation = new ChatComponentTranslation("commands.generic.exception");
			chatcomponenttranslation.getChatStyle().setColor(EnumChatFormatting.RED);
			sender.addChatMessage(chatcomponenttranslation);
			logger.error("Couldn't process command", throwable);
		}
		MinecraftServer.getServer().logInfo(sender.getCommandSenderName() + " tried to usage: " + line);
		return j;
	}

	private int dispatchBukkit(ICommandSender sender, CommandSender bukkitSender, String line,
			boolean tryConversation) {
		CraftServer server = (CraftServer) Bukkit.getServer();

		int ret = 0;

		try {
			// TODO support multiple
			ret += tryConversation && server.tryConversation(bukkitSender, line) ? 1
					: server.dispatchCommand(bukkitSender, line) ? 1 : 0;
		} catch (org.bukkit.command.CommandException ex) {
			if (bukkitSender instanceof Player) {
				bukkitSender.sendMessage(org.bukkit.ChatColor.RED
						+ "An internal error occurred while attempting to perform this command");
				logger.error("An internal error occurred while attempting to perform this command", ex);
			} else if (bukkitSender instanceof CraftBlockCommandSender) {
				CommandBlockLogic listener = (CommandBlockLogic) sender;
				logger.warn(String.format("CommandBlock at (%d,%d,%d) failed to handle command",
						listener.getPlayerCoordinates().posX, listener.getPlayerCoordinates().posY,
						listener.getPlayerCoordinates().posZ), ex);
			} else if (bukkitSender instanceof CraftMinecartCommand) {
				CommandBlockLogic listener = (CommandBlockLogic) sender;
				logger.warn(String.format("MinecartCommandBlock at (%d,%d,%d) failed to handle command",
						listener.getPlayerCoordinates().posX, listener.getPlayerCoordinates().posY,
						listener.getPlayerCoordinates().posZ), ex);
			} else {
				logger.warn("Unknown sender failed to handle command", ex);
			}
		}

		return ret;
	}

	public ICommand registerCommand(ICommand par1ICommand) {
		return registry.registerVanillaCommand(par1ICommand);
	}

	public CommandRegistry getRegistry() {
		return registry;
	}

	private static String[] dropFirstString(String[] par0ArrayOfStr) {
		String[] astring1 = new String[par0ArrayOfStr.length - 1];

		for (int i = 1; i < par0ArrayOfStr.length; ++i) {
			astring1[i - 1] = par0ArrayOfStr[i];
		}

		return astring1;
	}

	@Override
	public List getPossibleCommands(ICommandSender par1ICommandSender, String par2Str) {
		String[] astring = par2Str.split(" ", -1);
		String s1 = astring[0];

		if (astring.length == 1)
			return registry.filterPossibleCommandsNames(par1ICommandSender, s1);
		else {
			if (astring.length > 1) {
				ICommand icommand = registry.get(s1);

				if (icommand != null)
					return icommand.addTabCompletionOptions(par1ICommandSender, dropFirstString(astring));
			}

			return null;
		}
	}

	@Override
	public List getPossibleCommands(ICommandSender par1ICommandSender) {
		return registry.getPossibleCommands(par1ICommandSender);
	}

	@Override
	public Map getCommands() {
		return registry.getCommandMap();
	}

	private int getUsernameIndex(ICommand par1ICommand, String[] par2ArrayOfStr) {
		if (par1ICommand == null)
			return -1;
		else {
			for (int i = 0; i < par2ArrayOfStr.length; ++i) {
				if (par1ICommand.isUsernameIndex(par2ArrayOfStr, i)
						&& PlayerSelector.matchesMultiplePlayers(par2ArrayOfStr[i]))
					return i;
			}

			return -1;
		}
	}
}