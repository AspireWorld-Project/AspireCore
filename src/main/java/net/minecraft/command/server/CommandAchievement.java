package net.minecraft.command.server;

import com.google.common.collect.Lists;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.AchievementList;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatList;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CommandAchievement extends CommandBase {
	private static final String __OBFID = "CL_00000113";

	@Override
	public String getCommandName() {
		return "achievement";
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}

	@Override
	public String getCommandUsage(ICommandSender p_71518_1_) {
		return "commands.achievement.usage";
	}

	@Override
	public void processCommand(ICommandSender p_71515_1_, String[] p_71515_2_) {
		if (p_71515_2_.length >= 2) {
			StatBase statbase = StatList.func_151177_a(p_71515_2_[1]);

			if (statbase == null && !p_71515_2_[1].equals("*"))
				throw new CommandException("commands.achievement.unknownAchievement", p_71515_2_[1]);

			EntityPlayerMP entityplayermp;

			if (p_71515_2_.length >= 3) {
				entityplayermp = getPlayer(p_71515_1_, p_71515_2_[2]);
			} else {
				entityplayermp = getCommandSenderAsPlayer(p_71515_1_);
			}

			if (p_71515_2_[0].equalsIgnoreCase("give")) {
				if (statbase == null) {
					Iterator iterator = AchievementList.achievementList.iterator();

					while (iterator.hasNext()) {
						Achievement achievement = (Achievement) iterator.next();
						entityplayermp.triggerAchievement(achievement);
					}

					func_152373_a(p_71515_1_, this, "commands.achievement.give.success.all",
							entityplayermp.getCommandSenderName());
				} else {
					if (statbase instanceof Achievement) {
						Achievement achievement2 = (Achievement) statbase;
						ArrayList arraylist;

						for (arraylist = Lists.newArrayList(); achievement2.parentAchievement != null
								&& !entityplayermp.func_147099_x().hasAchievementUnlocked(
										achievement2.parentAchievement); achievement2 = achievement2.parentAchievement) {
							arraylist.add(achievement2.parentAchievement);
						}

						Iterator iterator1 = Lists.reverse(arraylist).iterator();

						while (iterator1.hasNext()) {
							Achievement achievement1 = (Achievement) iterator1.next();
							entityplayermp.triggerAchievement(achievement1);
						}
					}

					entityplayermp.triggerAchievement(statbase);
					func_152373_a(p_71515_1_, this, "commands.achievement.give.success.one",
							entityplayermp.getCommandSenderName(), statbase.func_150955_j());
				}

				return;
			}
		}

		throw new WrongUsageException("commands.achievement.usage");
	}

	@Override
	public List addTabCompletionOptions(ICommandSender p_71516_1_, String[] p_71516_2_) {
		if (p_71516_2_.length == 1)
			return getListOfStringsMatchingLastWord(p_71516_2_, "give");
		else if (p_71516_2_.length != 2)
			return p_71516_2_.length == 3
					? getListOfStringsMatchingLastWord(p_71516_2_, MinecraftServer.getServer().getAllUsernames())
					: null;
		else {
			ArrayList arraylist = Lists.newArrayList();
			Iterator iterator = StatList.allStats.iterator();

			while (iterator.hasNext()) {
				StatBase statbase = (StatBase) iterator.next();
				arraylist.add(statbase.statId);
			}

			return getListOfStringsFromIterableMatchingLastWord(p_71516_2_, arraylist);
		}
	}

	@Override
	public boolean isUsernameIndex(String[] p_82358_1_, int p_82358_2_) {
		return p_82358_2_ == 2;
	}
}