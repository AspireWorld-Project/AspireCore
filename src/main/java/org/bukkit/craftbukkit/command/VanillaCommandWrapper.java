package org.bukkit.craftbukkit.command;

import net.minecraft.server.MinecraftServer;
import org.apache.commons.lang.Validate;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.RemoteConsoleCommandSender;
import org.bukkit.command.defaults.VanillaCommand;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.entity.CraftMinecartCommand;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.entity.minecart.CommandMinecart;
import org.ultramine.commands.IExtendedCommand;

import java.util.List;

public final class VanillaCommandWrapper extends VanillaCommand {
	private static final Logger log = LogManager.getLogger();

	protected final net.minecraft.command.ICommand vanillaCommand;

	public VanillaCommandWrapper(net.minecraft.command.ICommand vanillaCommand) {
		super(vanillaCommand.getCommandName());
		this.vanillaCommand = vanillaCommand;
	}

	public VanillaCommandWrapper(net.minecraft.command.ICommand vanillaCommand, String usage) {
		this(vanillaCommand, usage, "A Mojang provided command.",
				"minecraft.command." + vanillaCommand.getCommandName());
	}

	public VanillaCommandWrapper(net.minecraft.command.ICommand vanillaCommand, String usage, String description,
			String permission) {
		super(vanillaCommand.getCommandName());
		this.vanillaCommand = vanillaCommand;
		this.description = description;
		usageMessage = usage;
		setPermission(permission);
	}

	public VanillaCommandWrapper(IExtendedCommand umCommand) {
		this(umCommand, umCommand.getCommandUsage(MinecraftServer.getServer()), umCommand.getDescription(), null);
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		if (!testPermission(sender))
			return true;

		net.minecraft.command.ICommandSender icommandlistener = getListener(sender);
		// Some commands use the worldserver variable but we leave it full of null
		// values,
		// so we must temporarily populate it with the world of the commandsender
		net.minecraft.world.WorldServer[] prev = net.minecraft.server.MinecraftServer.getServer().worldServers;
		net.minecraft.server.MinecraftServer.getServer().worldServers = new net.minecraft.world.WorldServer[] {
				(net.minecraft.world.WorldServer) icommandlistener.getEntityWorld() };
		try {
			vanillaCommand.processCommand(icommandlistener, args);
		} catch (net.minecraft.command.WrongUsageException exceptionusage) {
			net.minecraft.util.ChatComponentTranslation chatmessage = new net.minecraft.util.ChatComponentTranslation(
					"commands.generic.usage",
					new net.minecraft.util.ChatComponentTranslation(exceptionusage.getMessage(),
							exceptionusage.getErrorOjbects()));
			chatmessage.getChatStyle().setColor(net.minecraft.util.EnumChatFormatting.RED);
			icommandlistener.addChatMessage(chatmessage);
		} catch (net.minecraft.command.CommandException commandexception) {
			net.minecraft.util.ChatComponentTranslation chatmessage = new net.minecraft.util.ChatComponentTranslation(
					commandexception.getMessage(), commandexception.getErrorOjbects());
			chatmessage.getChatStyle().setColor(net.minecraft.util.EnumChatFormatting.RED);
			icommandlistener.addChatMessage(chatmessage);
		} finally {
			net.minecraft.server.MinecraftServer.getServer().worldServers = prev;
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
		Validate.notNull(sender, "Sender cannot be null");
		Validate.notNull(args, "Arguments cannot be null");
		Validate.notNull(alias, "Alias cannot be null");
		return vanillaCommand.addTabCompletionOptions(getListener(sender), args);
	}

	public final int dispatchVanillaCommandBlock(net.minecraft.command.server.CommandBlockLogic icommandlistener,
			String s) {
		// Copied from net.minecraft.server.CommandHandler
		s = s.trim();
		if (s.startsWith("/")) {
			s = s.substring(1);
		}
		String[] as = s.split(" ");
		as = dropFirstArgument(as);
		int i = getPlayerListSize(as);
		int j = 0;
		// Some commands use the worldserver variable but we leave it full of null
		// values,
		// so we must temporarily populate it with the world of the commandsender
		net.minecraft.world.WorldServer[] prev = net.minecraft.server.MinecraftServer.getServer().worldServers;
		net.minecraft.server.MinecraftServer.getServer().worldServers = new net.minecraft.world.WorldServer[] {
				(net.minecraft.world.WorldServer) icommandlistener.getEntityWorld() };
		try {
			if (vanillaCommand.canCommandSenderUseCommand(icommandlistener)) {
				if (i > -1) {
					net.minecraft.entity.player.EntityPlayerMP[] aentityplayer = net.minecraft.command.PlayerSelector
							.matchPlayers(icommandlistener, as[i]);
					String s2 = as[i];
					net.minecraft.entity.player.EntityPlayerMP[] aentityplayer1 = aentityplayer;
					int k = aentityplayer1.length;
					for (int l = 0; l < k;) {
						net.minecraft.entity.player.EntityPlayerMP entityplayer = aentityplayer1[l];
						as[i] = entityplayer.getCommandSenderName();
						try {
							vanillaCommand.processCommand(icommandlistener, as);
							j++;
							continue;
						} catch (net.minecraft.command.CommandException commandexception1) {
							net.minecraft.util.ChatComponentTranslation chatmessage4 = new net.minecraft.util.ChatComponentTranslation(
									commandexception1.getMessage(), commandexception1.getErrorOjbects());
							chatmessage4.getChatStyle().setColor(net.minecraft.util.EnumChatFormatting.RED);
							icommandlistener.addChatMessage(chatmessage4);
							l++;
						}
					}

					as[i] = s2;
				} else {
					vanillaCommand.processCommand(icommandlistener, as);
					j++;
				}
			} else {
				net.minecraft.util.ChatComponentTranslation chatmessage = new net.minecraft.util.ChatComponentTranslation(
						"commands.generic.permission");
				chatmessage.getChatStyle().setColor(net.minecraft.util.EnumChatFormatting.RED);
				icommandlistener.addChatMessage(chatmessage);
			}
		} catch (net.minecraft.command.WrongUsageException exceptionusage) {
			net.minecraft.util.ChatComponentTranslation chatmessage1 = new net.minecraft.util.ChatComponentTranslation(
					"commands.generic.usage",
					new net.minecraft.util.ChatComponentTranslation(exceptionusage.getMessage(),
							exceptionusage.getErrorOjbects()));
			chatmessage1.getChatStyle().setColor(net.minecraft.util.EnumChatFormatting.RED);
			icommandlistener.addChatMessage(chatmessage1);
		} catch (net.minecraft.command.CommandException commandexception) {
			net.minecraft.util.ChatComponentTranslation chatmessage2 = new net.minecraft.util.ChatComponentTranslation(
					commandexception.getMessage(), commandexception.getErrorOjbects());
			chatmessage2.getChatStyle().setColor(net.minecraft.util.EnumChatFormatting.RED);
			icommandlistener.addChatMessage(chatmessage2);
		} catch (Throwable throwable) {
			net.minecraft.util.ChatComponentTranslation chatmessage3 = new net.minecraft.util.ChatComponentTranslation(
					"commands.generic.exception");
			chatmessage3.getChatStyle().setColor(net.minecraft.util.EnumChatFormatting.RED);
			icommandlistener.addChatMessage(chatmessage3);
			log.log(Level.WARN,
					String.format("CommandBlock at (%d,%d,%d) failed to handle command",
							icommandlistener.getPlayerCoordinates().posX, icommandlistener.getPlayerCoordinates().posY,
							icommandlistener.getPlayerCoordinates().posZ),
					throwable);
		} finally {
			net.minecraft.server.MinecraftServer.getServer().worldServers = prev;
		}
		return j;
	}

	private net.minecraft.command.ICommandSender getListener(CommandSender sender) {
		if (sender instanceof Player)
			return ((CraftPlayer) sender).getHandle();
		if (sender instanceof BlockCommandSender)
			return ((CraftBlockCommandSender) sender).getTileEntity();
		if (sender instanceof CommandMinecart)
			return ((net.minecraft.entity.EntityMinecartCommandBlock) ((CraftMinecartCommand) sender).getHandle())
					.func_145822_e();
		if (sender instanceof RemoteConsoleCommandSender)
			return net.minecraft.network.rcon.RConConsoleSource.instance;
		if (sender instanceof ConsoleCommandSender)
			return ((CraftServer) sender.getServer()).getServer();
		return null;
	}

	private int getPlayerListSize(String[] as) {
		for (int i = 0; i < as.length; i++) {
			if (vanillaCommand.isUsernameIndex(as, i)
					&& net.minecraft.command.PlayerSelector.matchesMultiplePlayers(as[i]))
				return i;
		}
		return -1;
	}

	private String[] dropFirstArgument(String[] as) {
		String[] as1 = new String[as.length - 1];
		for (int i = 1; i < as.length; i++) {
			as1[i - 1] = as[i];
		}

		return as1;
	}
}
