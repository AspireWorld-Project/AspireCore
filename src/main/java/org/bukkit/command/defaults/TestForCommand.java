package org.bukkit.command.defaults;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class TestForCommand extends VanillaCommand {
	public TestForCommand() {
		super("testfor");
		description = "Tests whether a specifed player is online";
		usageMessage = "/testfor <player>";
		setPermission("bukkit.command.testfor");
	}

	@Override
	public boolean execute(CommandSender sender, String currentAlias, String[] args) {
		if (!testPermission(sender))
			return true;
		if (args.length < 1) {
			sender.sendMessage(ChatColor.RED + "Usage: " + usageMessage);
			return false;
		}

		sender.sendMessage(ChatColor.RED + "/testfor is only usable by commandblocks with analog output.");
		return true;
	}

	// Spigot Start
	@Override
	public java.util.List<String> tabComplete(CommandSender sender, String alias, String[] args)
			throws IllegalArgumentException {
		if (args.length == 0)
			return super.tabComplete(sender, alias, args);
		return java.util.Collections.emptyList();
	}
	// Spigot End
}