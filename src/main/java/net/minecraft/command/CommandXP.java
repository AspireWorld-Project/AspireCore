package net.minecraft.command;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

import java.util.List;

public class CommandXP extends CommandBase {
	private static final String __OBFID = "CL_00000398";

	@Override
	public String getCommandName() {
		return "xp";
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}

	@Override
	public String getCommandUsage(ICommandSender p_71518_1_) {
		return "commands.xp.usage";
	}

	@Override
	public void processCommand(ICommandSender p_71515_1_, String[] p_71515_2_) {
		if (p_71515_2_.length <= 0)
			throw new WrongUsageException("commands.xp.usage");
		else {
			String s = p_71515_2_[0];
			boolean flag = s.endsWith("l") || s.endsWith("L");

			if (flag && s.length() > 1) {
				s = s.substring(0, s.length() - 1);
			}

			int i = parseInt(p_71515_1_, s);
			boolean flag1 = i < 0;

			if (flag1) {
				i *= -1;
			}

			EntityPlayerMP entityplayermp;

			if (p_71515_2_.length > 1) {
				entityplayermp = getPlayer(p_71515_1_, p_71515_2_[1]);
			} else {
				entityplayermp = getCommandSenderAsPlayer(p_71515_1_);
			}

			if (flag) {
				if (flag1) {
					entityplayermp.addExperienceLevel(-i);
					func_152373_a(p_71515_1_, this, "commands.xp.success.negative.levels",
							Integer.valueOf(i), entityplayermp.getCommandSenderName());
				} else {
					entityplayermp.addExperienceLevel(i);
					func_152373_a(p_71515_1_, this, "commands.xp.success.levels",
							Integer.valueOf(i), entityplayermp.getCommandSenderName());
				}
			} else {
				if (flag1)
					throw new WrongUsageException("commands.xp.failure.widthdrawXp");

				entityplayermp.addExperience(i);
				func_152373_a(p_71515_1_, this, "commands.xp.success",
						Integer.valueOf(i), entityplayermp.getCommandSenderName());
			}
		}
	}

	@Override
	public List addTabCompletionOptions(ICommandSender p_71516_1_, String[] p_71516_2_) {
		return p_71516_2_.length == 2 ? getListOfStringsMatchingLastWord(p_71516_2_, getAllUsernames()) : null;
	}

	protected String[] getAllUsernames() {
		return MinecraftServer.getServer().getAllUsernames();
	}

	@Override
	public boolean isUsernameIndex(String[] p_82358_1_, int p_82358_2_) {
		return p_82358_2_ == 1;
	}
}