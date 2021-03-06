package net.minecraft.command;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.GameRules;

import java.util.List;

public class CommandGameRule extends CommandBase {
	private static final String __OBFID = "CL_00000475";

	@Override
	public String getCommandName() {
		return "gamerule";
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}

	@Override
	public String getCommandUsage(ICommandSender p_71518_1_) {
		return "commands.gamerule.usage";
	}

	@Override
	public void processCommand(ICommandSender p_71515_1_, String[] p_71515_2_) {
		String s1;

		if (p_71515_2_.length == 2) {
			s1 = p_71515_2_[0];
			String s2 = p_71515_2_[1];
			GameRules gamerules2 = getGameRules();

			if (gamerules2.hasRule(s1)) {
				gamerules2.setOrCreateGameRule(s1, s2);
				func_152373_a(p_71515_1_, this, "commands.gamerule.success");
			} else {
				func_152373_a(p_71515_1_, this, "commands.gamerule.norule", s1);
			}
		} else if (p_71515_2_.length == 1) {
			s1 = p_71515_2_[0];
			GameRules gamerules1 = getGameRules();

			if (gamerules1.hasRule(s1)) {
				String s = gamerules1.getGameRuleStringValue(s1);
				p_71515_1_.addChatMessage(new ChatComponentText(s1).appendText(" = ").appendText(s));
			} else {
				func_152373_a(p_71515_1_, this, "commands.gamerule.norule", s1);
			}
		} else if (p_71515_2_.length == 0) {
			GameRules gamerules = getGameRules();
			p_71515_1_.addChatMessage(new ChatComponentText(joinNiceString(gamerules.getRules())));
		} else
			throw new WrongUsageException("commands.gamerule.usage");
	}

	@Override
	public List addTabCompletionOptions(ICommandSender p_71516_1_, String[] p_71516_2_) {
		return p_71516_2_.length == 1 ? getListOfStringsMatchingLastWord(p_71516_2_, getGameRules().getRules())
				: p_71516_2_.length == 2
						? getListOfStringsMatchingLastWord(p_71516_2_, "true", "false")
						: null;
	}

	private GameRules getGameRules() {
		return MinecraftServer.getServer().worldServerForDimension(0).getGameRules();
	}
}