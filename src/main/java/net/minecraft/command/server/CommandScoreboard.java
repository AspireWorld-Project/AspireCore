package net.minecraft.command.server;

import net.minecraft.command.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.scoreboard.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;

import java.util.*;

public class CommandScoreboard extends CommandBase {
	private static final String __OBFID = "CL_00000896";

	@Override
	public String getCommandName() {
		return "scoreboard";
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}

	@Override
	public String getCommandUsage(ICommandSender p_71518_1_) {
		return "commands.scoreboard.usage";
	}

	@Override
	public void processCommand(ICommandSender p_71515_1_, String[] p_71515_2_) {
		if (p_71515_2_.length >= 1) {
			if (p_71515_2_[0].equalsIgnoreCase("objectives")) {
				if (p_71515_2_.length == 1)
					throw new WrongUsageException("commands.scoreboard.objectives.usage");

				if (p_71515_2_[1].equalsIgnoreCase("list")) {
					func_147196_d(p_71515_1_);
				} else if (p_71515_2_[1].equalsIgnoreCase("add")) {
					if (p_71515_2_.length < 4)
						throw new WrongUsageException("commands.scoreboard.objectives.add.usage");

					func_147193_c(p_71515_1_, p_71515_2_, 2);
				} else if (p_71515_2_[1].equalsIgnoreCase("remove")) {
					if (p_71515_2_.length != 3)
						throw new WrongUsageException("commands.scoreboard.objectives.remove.usage");

					func_147191_h(p_71515_1_, p_71515_2_[2]);
				} else {
					if (!p_71515_2_[1].equalsIgnoreCase("setdisplay"))
						throw new WrongUsageException("commands.scoreboard.objectives.usage");

					if (p_71515_2_.length != 3 && p_71515_2_.length != 4)
						throw new WrongUsageException("commands.scoreboard.objectives.setdisplay.usage");

					func_147198_k(p_71515_1_, p_71515_2_, 2);
				}

				return;
			}

			if (p_71515_2_[0].equalsIgnoreCase("players")) {
				if (p_71515_2_.length == 1)
					throw new WrongUsageException("commands.scoreboard.players.usage");

				if (p_71515_2_[1].equalsIgnoreCase("list")) {
					if (p_71515_2_.length > 3)
						throw new WrongUsageException("commands.scoreboard.players.list.usage");

					func_147195_l(p_71515_1_, p_71515_2_, 2);
				} else if (p_71515_2_[1].equalsIgnoreCase("add")) {
					if (p_71515_2_.length != 5)
						throw new WrongUsageException("commands.scoreboard.players.add.usage");

					func_147197_m(p_71515_1_, p_71515_2_, 2);
				} else if (p_71515_2_[1].equalsIgnoreCase("remove")) {
					if (p_71515_2_.length != 5)
						throw new WrongUsageException("commands.scoreboard.players.remove.usage");

					func_147197_m(p_71515_1_, p_71515_2_, 2);
				} else if (p_71515_2_[1].equalsIgnoreCase("set")) {
					if (p_71515_2_.length != 5)
						throw new WrongUsageException("commands.scoreboard.players.set.usage");

					func_147197_m(p_71515_1_, p_71515_2_, 2);
				} else {
					if (!p_71515_2_[1].equalsIgnoreCase("reset"))
						throw new WrongUsageException("commands.scoreboard.players.usage");

					if (p_71515_2_.length != 3)
						throw new WrongUsageException("commands.scoreboard.players.reset.usage");

					func_147187_n(p_71515_1_, p_71515_2_, 2);
				}

				return;
			}

			if (p_71515_2_[0].equalsIgnoreCase("teams")) {
				if (p_71515_2_.length == 1)
					throw new WrongUsageException("commands.scoreboard.teams.usage");

				if (p_71515_2_[1].equalsIgnoreCase("list")) {
					if (p_71515_2_.length > 3)
						throw new WrongUsageException("commands.scoreboard.teams.list.usage");

					func_147186_g(p_71515_1_, p_71515_2_, 2);
				} else if (p_71515_2_[1].equalsIgnoreCase("add")) {
					if (p_71515_2_.length < 3)
						throw new WrongUsageException("commands.scoreboard.teams.add.usage");

					func_147185_d(p_71515_1_, p_71515_2_, 2);
				} else if (p_71515_2_[1].equalsIgnoreCase("remove")) {
					if (p_71515_2_.length != 3)
						throw new WrongUsageException("commands.scoreboard.teams.remove.usage");

					func_147194_f(p_71515_1_, p_71515_2_, 2);
				} else if (p_71515_2_[1].equalsIgnoreCase("empty")) {
					if (p_71515_2_.length != 3)
						throw new WrongUsageException("commands.scoreboard.teams.empty.usage");

					func_147188_j(p_71515_1_, p_71515_2_, 2);
				} else if (p_71515_2_[1].equalsIgnoreCase("join")) {
					if (p_71515_2_.length < 4 && (p_71515_2_.length != 3 || !(p_71515_1_ instanceof EntityPlayer)))
						throw new WrongUsageException("commands.scoreboard.teams.join.usage");

					func_147190_h(p_71515_1_, p_71515_2_, 2);
				} else if (p_71515_2_[1].equalsIgnoreCase("leave")) {
					if (p_71515_2_.length < 3 && !(p_71515_1_ instanceof EntityPlayer))
						throw new WrongUsageException("commands.scoreboard.teams.leave.usage");

					func_147199_i(p_71515_1_, p_71515_2_, 2);
				} else {
					if (!p_71515_2_[1].equalsIgnoreCase("option"))
						throw new WrongUsageException("commands.scoreboard.teams.usage");

					if (p_71515_2_.length != 4 && p_71515_2_.length != 5)
						throw new WrongUsageException("commands.scoreboard.teams.option.usage");

					func_147200_e(p_71515_1_, p_71515_2_, 2);
				}

				return;
			}
		}

		throw new WrongUsageException("commands.scoreboard.usage");
	}

	protected Scoreboard func_147192_d() {
		return MinecraftServer.getServer().worldServerForDimension(0).getScoreboard();
	}

	protected ScoreObjective func_147189_a(String p_147189_1_, boolean p_147189_2_) {
		Scoreboard scoreboard = func_147192_d();
		ScoreObjective scoreobjective = scoreboard.getObjective(p_147189_1_);

		if (scoreobjective == null)
			throw new CommandException("commands.scoreboard.objectiveNotFound", p_147189_1_);
		else if (p_147189_2_ && scoreobjective.getCriteria().isReadOnly())
			throw new CommandException("commands.scoreboard.objectiveReadOnly", p_147189_1_);
		else
			return scoreobjective;
	}

	protected ScorePlayerTeam func_147183_a(String p_147183_1_) {
		Scoreboard scoreboard = func_147192_d();
		ScorePlayerTeam scoreplayerteam = scoreboard.getTeam(p_147183_1_);

		if (scoreplayerteam == null)
			throw new CommandException("commands.scoreboard.teamNotFound", p_147183_1_);
		else
			return scoreplayerteam;
	}

	protected void func_147193_c(ICommandSender p_147193_1_, String[] p_147193_2_, int p_147193_3_) {
		String s = p_147193_2_[p_147193_3_++];
		String s1 = p_147193_2_[p_147193_3_++];
		Scoreboard scoreboard = func_147192_d();
		IScoreObjectiveCriteria iscoreobjectivecriteria = (IScoreObjectiveCriteria) IScoreObjectiveCriteria.field_96643_a
				.get(s1);

		if (iscoreobjectivecriteria == null)
			throw new WrongUsageException("commands.scoreboard.objectives.add.wrongType", s1);
		else if (scoreboard.getObjective(s) != null)
			throw new CommandException("commands.scoreboard.objectives.add.alreadyExists", s);
		else if (s.length() > 16)
			throw new SyntaxErrorException("commands.scoreboard.objectives.add.tooLong",
					s, Integer.valueOf(16));
		else if (s.length() == 0)
			throw new WrongUsageException("commands.scoreboard.objectives.add.usage");
		else {
			if (p_147193_2_.length > p_147193_3_) {
				String s2 = func_147178_a(p_147193_1_, p_147193_2_, p_147193_3_).getUnformattedText();

				if (s2.length() > 32)
					throw new SyntaxErrorException("commands.scoreboard.objectives.add.displayTooLong",
							s2, Integer.valueOf(32));

				if (s2.length() > 0) {
					scoreboard.addScoreObjective(s, iscoreobjectivecriteria).setDisplayName(s2);
				} else {
					scoreboard.addScoreObjective(s, iscoreobjectivecriteria);
				}
			} else {
				scoreboard.addScoreObjective(s, iscoreobjectivecriteria);
			}

			func_152373_a(p_147193_1_, this, "commands.scoreboard.objectives.add.success", s);
		}
	}

	protected void func_147185_d(ICommandSender p_147185_1_, String[] p_147185_2_, int p_147185_3_) {
		String s = p_147185_2_[p_147185_3_++];
		Scoreboard scoreboard = func_147192_d();

		if (scoreboard.getTeam(s) != null)
			throw new CommandException("commands.scoreboard.teams.add.alreadyExists", s);
		else if (s.length() > 16)
			throw new SyntaxErrorException("commands.scoreboard.teams.add.tooLong",
					s, Integer.valueOf(16));
		else if (s.length() == 0)
			throw new WrongUsageException("commands.scoreboard.teams.add.usage");
		else {
			if (p_147185_2_.length > p_147185_3_) {
				String s1 = func_147178_a(p_147185_1_, p_147185_2_, p_147185_3_).getUnformattedText();

				if (s1.length() > 32)
					throw new SyntaxErrorException("commands.scoreboard.teams.add.displayTooLong",
							s1, Integer.valueOf(32));

				if (s1.length() > 0) {
					scoreboard.createTeam(s).setTeamName(s1);
				} else {
					scoreboard.createTeam(s);
				}
			} else {
				scoreboard.createTeam(s);
			}

			func_152373_a(p_147185_1_, this, "commands.scoreboard.teams.add.success", s);
		}
	}

	protected void func_147200_e(ICommandSender p_147200_1_, String[] p_147200_2_, int p_147200_3_) {
		ScorePlayerTeam scoreplayerteam = func_147183_a(p_147200_2_[p_147200_3_++]);

		if (scoreplayerteam != null) {
			String s = p_147200_2_[p_147200_3_++].toLowerCase();

			if (!s.equalsIgnoreCase("color") && !s.equalsIgnoreCase("friendlyfire")
					&& !s.equalsIgnoreCase("seeFriendlyInvisibles"))
				throw new WrongUsageException("commands.scoreboard.teams.option.usage");
			else if (p_147200_2_.length == 4) {
				if (s.equalsIgnoreCase("color"))
					throw new WrongUsageException("commands.scoreboard.teams.option.noValue", s,
							joinNiceStringFromCollection(EnumChatFormatting.getValidValues(true, false)));
				else if (!s.equalsIgnoreCase("friendlyfire") && !s.equalsIgnoreCase("seeFriendlyInvisibles"))
					throw new WrongUsageException("commands.scoreboard.teams.option.usage");
				else
					throw new WrongUsageException("commands.scoreboard.teams.option.noValue", s,
							joinNiceStringFromCollection(Arrays.asList("true", "false")));
			} else {
				String s1 = p_147200_2_[p_147200_3_++];

				if (s.equalsIgnoreCase("color")) {
					EnumChatFormatting enumchatformatting = EnumChatFormatting.getValueByName(s1);

					if (enumchatformatting == null || enumchatformatting.isFancyStyling())
						throw new WrongUsageException("commands.scoreboard.teams.option.noValue", s,
								joinNiceStringFromCollection(EnumChatFormatting.getValidValues(true, false)));

					scoreplayerteam.setNamePrefix(enumchatformatting.toString());
					scoreplayerteam.setNameSuffix(EnumChatFormatting.RESET.toString());
				} else if (s.equalsIgnoreCase("friendlyfire")) {
					if (!s1.equalsIgnoreCase("true") && !s1.equalsIgnoreCase("false"))
						throw new WrongUsageException("commands.scoreboard.teams.option.noValue", s,
								joinNiceStringFromCollection(Arrays.asList("true", "false")));

					scoreplayerteam.setAllowFriendlyFire(s1.equalsIgnoreCase("true"));
				} else if (s.equalsIgnoreCase("seeFriendlyInvisibles")) {
					if (!s1.equalsIgnoreCase("true") && !s1.equalsIgnoreCase("false"))
						throw new WrongUsageException("commands.scoreboard.teams.option.noValue", s,
								joinNiceStringFromCollection(Arrays.asList("true", "false")));

					scoreplayerteam.setSeeFriendlyInvisiblesEnabled(s1.equalsIgnoreCase("true"));
				}

				func_152373_a(p_147200_1_, this, "commands.scoreboard.teams.option.success",
						s, scoreplayerteam.getRegisteredName(), s1);
			}
		}
	}

	protected void func_147194_f(ICommandSender p_147194_1_, String[] p_147194_2_, int p_147194_3_) {
		Scoreboard scoreboard = func_147192_d();
		ScorePlayerTeam scoreplayerteam = func_147183_a(p_147194_2_[p_147194_3_++]);

		if (scoreplayerteam != null) {
			scoreboard.removeTeam(scoreplayerteam);
			func_152373_a(p_147194_1_, this, "commands.scoreboard.teams.remove.success",
					scoreplayerteam.getRegisteredName());
		}
	}

	protected void func_147186_g(ICommandSender p_147186_1_, String[] p_147186_2_, int p_147186_3_) {
		Scoreboard scoreboard = func_147192_d();

		if (p_147186_2_.length > p_147186_3_) {
			ScorePlayerTeam scoreplayerteam = func_147183_a(p_147186_2_[p_147186_3_++]);

			if (scoreplayerteam == null)
				return;

			Collection collection = scoreplayerteam.getMembershipCollection();

			if (collection.size() <= 0)
				throw new CommandException("commands.scoreboard.teams.list.player.empty",
						scoreplayerteam.getRegisteredName());

			ChatComponentTranslation chatcomponenttranslation = new ChatComponentTranslation(
					"commands.scoreboard.teams.list.player.count",
					Integer.valueOf(collection.size()), scoreplayerteam.getRegisteredName());
			chatcomponenttranslation.getChatStyle().setColor(EnumChatFormatting.DARK_GREEN);
			p_147186_1_.addChatMessage(chatcomponenttranslation);
			p_147186_1_.addChatMessage(new ChatComponentText(joinNiceString(collection.toArray())));
		} else {
			Collection collection1 = scoreboard.getTeams();

			if (collection1.size() <= 0)
				throw new CommandException("commands.scoreboard.teams.list.empty");

			ChatComponentTranslation chatcomponenttranslation1 = new ChatComponentTranslation(
					"commands.scoreboard.teams.list.count", Integer.valueOf(collection1.size()));
			chatcomponenttranslation1.getChatStyle().setColor(EnumChatFormatting.DARK_GREEN);
			p_147186_1_.addChatMessage(chatcomponenttranslation1);
			Iterator iterator = collection1.iterator();

			while (iterator.hasNext()) {
				ScorePlayerTeam scoreplayerteam1 = (ScorePlayerTeam) iterator.next();
				p_147186_1_.addChatMessage(new ChatComponentTranslation("commands.scoreboard.teams.list.entry",
						scoreplayerteam1.getRegisteredName(), scoreplayerteam1.func_96669_c(),
						Integer.valueOf(scoreplayerteam1.getMembershipCollection().size())));
			}
		}
	}

	protected void func_147190_h(ICommandSender p_147190_1_, String[] p_147190_2_, int p_147190_3_) {
		Scoreboard scoreboard = func_147192_d();
		String s = p_147190_2_[p_147190_3_++];
		HashSet hashset = new HashSet();
		HashSet hashset1 = new HashSet();
		String s1;

		if (p_147190_1_ instanceof EntityPlayer && p_147190_3_ == p_147190_2_.length) {
			s1 = getCommandSenderAsPlayer(p_147190_1_).getCommandSenderName();

			if (scoreboard.func_151392_a(s1, s)) {
				hashset.add(s1);
			} else {
				hashset1.add(s1);
			}
		} else {
			while (p_147190_3_ < p_147190_2_.length) {
				s1 = func_96332_d(p_147190_1_, p_147190_2_[p_147190_3_++]);

				if (scoreboard.func_151392_a(s1, s)) {
					hashset.add(s1);
				} else {
					hashset1.add(s1);
				}
			}
		}

		if (!hashset.isEmpty()) {
			func_152373_a(p_147190_1_, this, "commands.scoreboard.teams.join.success", Integer.valueOf(hashset.size()), s, joinNiceString(hashset.toArray(new String[0])));
		}

		if (!hashset1.isEmpty())
			throw new CommandException("commands.scoreboard.teams.join.failure", Integer.valueOf(hashset1.size()), s, joinNiceString(hashset1.toArray(new String[0])));
	}

	protected void func_147199_i(ICommandSender p_147199_1_, String[] p_147199_2_, int p_147199_3_) {
		Scoreboard scoreboard = func_147192_d();
		HashSet hashset = new HashSet();
		HashSet hashset1 = new HashSet();
		String s;

		if (p_147199_1_ instanceof EntityPlayer && p_147199_3_ == p_147199_2_.length) {
			s = getCommandSenderAsPlayer(p_147199_1_).getCommandSenderName();

			if (scoreboard.removePlayerFromTeams(s)) {
				hashset.add(s);
			} else {
				hashset1.add(s);
			}
		} else {
			while (p_147199_3_ < p_147199_2_.length) {
				s = func_96332_d(p_147199_1_, p_147199_2_[p_147199_3_++]);

				if (scoreboard.removePlayerFromTeams(s)) {
					hashset.add(s);
				} else {
					hashset1.add(s);
				}
			}
		}

		if (!hashset.isEmpty()) {
			func_152373_a(p_147199_1_, this, "commands.scoreboard.teams.leave.success",
					Integer.valueOf(hashset.size()), joinNiceString(hashset.toArray(new String[0])));
		}

		if (!hashset1.isEmpty())
			throw new CommandException("commands.scoreboard.teams.leave.failure",
					Integer.valueOf(hashset1.size()), joinNiceString(hashset1.toArray(new String[0])));
	}

	protected void func_147188_j(ICommandSender p_147188_1_, String[] p_147188_2_, int p_147188_3_) {
		Scoreboard scoreboard = func_147192_d();
		ScorePlayerTeam scoreplayerteam = func_147183_a(p_147188_2_[p_147188_3_++]);

		if (scoreplayerteam != null) {
			ArrayList arraylist = new ArrayList(scoreplayerteam.getMembershipCollection());

			if (arraylist.isEmpty())
				throw new CommandException("commands.scoreboard.teams.empty.alreadyEmpty",
						scoreplayerteam.getRegisteredName());
			else {
				Iterator iterator = arraylist.iterator();

				while (iterator.hasNext()) {
					String s = (String) iterator.next();
					scoreboard.removePlayerFromTeam(s, scoreplayerteam);
				}

				func_152373_a(p_147188_1_, this, "commands.scoreboard.teams.empty.success",
						Integer.valueOf(arraylist.size()), scoreplayerteam.getRegisteredName());
			}
		}
	}

	protected void func_147191_h(ICommandSender p_147191_1_, String p_147191_2_) {
		Scoreboard scoreboard = func_147192_d();
		ScoreObjective scoreobjective = func_147189_a(p_147191_2_, false);
		scoreboard.func_96519_k(scoreobjective);
		func_152373_a(p_147191_1_, this, "commands.scoreboard.objectives.remove.success", p_147191_2_);
	}

	protected void func_147196_d(ICommandSender p_147196_1_) {
		Scoreboard scoreboard = func_147192_d();
		Collection collection = scoreboard.getScoreObjectives();

		if (collection.size() <= 0)
			throw new CommandException("commands.scoreboard.objectives.list.empty");
		else {
			ChatComponentTranslation chatcomponenttranslation = new ChatComponentTranslation(
					"commands.scoreboard.objectives.list.count", Integer.valueOf(collection.size()));
			chatcomponenttranslation.getChatStyle().setColor(EnumChatFormatting.DARK_GREEN);
			p_147196_1_.addChatMessage(chatcomponenttranslation);
			Iterator iterator = collection.iterator();

			while (iterator.hasNext()) {
				ScoreObjective scoreobjective = (ScoreObjective) iterator.next();
				p_147196_1_.addChatMessage(new ChatComponentTranslation("commands.scoreboard.objectives.list.entry",
						scoreobjective.getName(), scoreobjective.getDisplayName(),
						scoreobjective.getCriteria().func_96636_a()));
			}
		}
	}

	protected void func_147198_k(ICommandSender p_147198_1_, String[] p_147198_2_, int p_147198_3_) {
		Scoreboard scoreboard = func_147192_d();
		String s = p_147198_2_[p_147198_3_++];
		int j = Scoreboard.getObjectiveDisplaySlotNumber(s);
		ScoreObjective scoreobjective = null;

		if (p_147198_2_.length == 4) {
			scoreobjective = func_147189_a(p_147198_2_[p_147198_3_++], false);
		}

		if (j < 0)
			throw new CommandException("commands.scoreboard.objectives.setdisplay.invalidSlot", s);
		else {
			scoreboard.func_96530_a(j, scoreobjective);

			if (scoreobjective != null) {
				func_152373_a(p_147198_1_, this, "commands.scoreboard.objectives.setdisplay.successSet",
						Scoreboard.getObjectiveDisplaySlot(j), scoreobjective.getName());
			} else {
				func_152373_a(p_147198_1_, this, "commands.scoreboard.objectives.setdisplay.successCleared",
						Scoreboard.getObjectiveDisplaySlot(j));
			}
		}
	}

	protected void func_147195_l(ICommandSender p_147195_1_, String[] p_147195_2_, int p_147195_3_) {
		Scoreboard scoreboard = func_147192_d();

		if (p_147195_2_.length > p_147195_3_) {
			String s = func_96332_d(p_147195_1_, p_147195_2_[p_147195_3_++]);
			Map map = scoreboard.func_96510_d(s);

			if (map.size() <= 0)
				throw new CommandException("commands.scoreboard.players.list.player.empty", s);

			ChatComponentTranslation chatcomponenttranslation = new ChatComponentTranslation(
					"commands.scoreboard.players.list.player.count", Integer.valueOf(map.size()), s);
			chatcomponenttranslation.getChatStyle().setColor(EnumChatFormatting.DARK_GREEN);
			p_147195_1_.addChatMessage(chatcomponenttranslation);
			Iterator iterator = map.values().iterator();

			while (iterator.hasNext()) {
				Score score = (Score) iterator.next();
				p_147195_1_.addChatMessage(new ChatComponentTranslation("commands.scoreboard.players.list.player.entry",
						Integer.valueOf(score.getScorePoints()), score.func_96645_d().getDisplayName(),
						score.func_96645_d().getName()));
			}
		} else {
			Collection collection = scoreboard.getObjectiveNames();

			if (collection.size() <= 0)
				throw new CommandException("commands.scoreboard.players.list.empty");

			ChatComponentTranslation chatcomponenttranslation1 = new ChatComponentTranslation(
					"commands.scoreboard.players.list.count", Integer.valueOf(collection.size()));
			chatcomponenttranslation1.getChatStyle().setColor(EnumChatFormatting.DARK_GREEN);
			p_147195_1_.addChatMessage(chatcomponenttranslation1);
			p_147195_1_.addChatMessage(new ChatComponentText(joinNiceString(collection.toArray())));
		}
	}

	protected void func_147197_m(ICommandSender p_147197_1_, String[] p_147197_2_, int p_147197_3_) {
		String s = p_147197_2_[p_147197_3_ - 1];
		String s1 = func_96332_d(p_147197_1_, p_147197_2_[p_147197_3_++]);
		ScoreObjective scoreobjective = func_147189_a(p_147197_2_[p_147197_3_++], true);
		int j = s.equalsIgnoreCase("set") ? parseInt(p_147197_1_, p_147197_2_[p_147197_3_++])
				: parseIntWithMin(p_147197_1_, p_147197_2_[p_147197_3_++], 1);
		Scoreboard scoreboard = func_147192_d();
		Score score = scoreboard.func_96529_a(s1, scoreobjective);

		if (s.equalsIgnoreCase("set")) {
			score.setScorePoints(j);
		} else if (s.equalsIgnoreCase("add")) {
			score.increseScore(j);
		} else {
			score.decreaseScore(j);
		}

		func_152373_a(p_147197_1_, this, "commands.scoreboard.players.set.success",
				scoreobjective.getName(), s1, Integer.valueOf(score.getScorePoints()));
	}

	protected void func_147187_n(ICommandSender p_147187_1_, String[] p_147187_2_, int p_147187_3_) {
		Scoreboard scoreboard = func_147192_d();
		String s = func_96332_d(p_147187_1_, p_147187_2_[p_147187_3_++]);
		scoreboard.func_96515_c(s);
		func_152373_a(p_147187_1_, this, "commands.scoreboard.players.reset.success", s);
	}

	@Override
	public List addTabCompletionOptions(ICommandSender p_71516_1_, String[] p_71516_2_) {
		if (p_71516_2_.length == 1)
			return getListOfStringsMatchingLastWord(p_71516_2_, "objectives", "players", "teams");
		else {
			if (p_71516_2_[0].equalsIgnoreCase("objectives")) {
				if (p_71516_2_.length == 2)
					return getListOfStringsMatchingLastWord(p_71516_2_,
							"list", "add", "remove", "setdisplay");

				if (p_71516_2_[1].equalsIgnoreCase("add")) {
					if (p_71516_2_.length == 4) {
						Set set = IScoreObjectiveCriteria.field_96643_a.keySet();
						return getListOfStringsFromIterableMatchingLastWord(p_71516_2_, set);
					}
				} else if (p_71516_2_[1].equalsIgnoreCase("remove")) {
					if (p_71516_2_.length == 3)
						return getListOfStringsFromIterableMatchingLastWord(p_71516_2_, func_147184_a(false));
				} else if (p_71516_2_[1].equalsIgnoreCase("setdisplay")) {
					if (p_71516_2_.length == 3)
						return getListOfStringsMatchingLastWord(p_71516_2_,
								"list", "sidebar", "belowName");

					if (p_71516_2_.length == 4)
						return getListOfStringsFromIterableMatchingLastWord(p_71516_2_, func_147184_a(false));
				}
			} else if (p_71516_2_[0].equalsIgnoreCase("players")) {
				if (p_71516_2_.length == 2)
					return getListOfStringsMatchingLastWord(p_71516_2_,
							"set", "add", "remove", "reset", "list");

				if (!p_71516_2_[1].equalsIgnoreCase("set") && !p_71516_2_[1].equalsIgnoreCase("add")
						&& !p_71516_2_[1].equalsIgnoreCase("remove")) {
					if ((p_71516_2_[1].equalsIgnoreCase("reset") || p_71516_2_[1].equalsIgnoreCase("list"))
							&& p_71516_2_.length == 3)
						return getListOfStringsFromIterableMatchingLastWord(p_71516_2_,
								func_147192_d().getObjectiveNames());
				} else {
					if (p_71516_2_.length == 3)
						return getListOfStringsMatchingLastWord(p_71516_2_,
								MinecraftServer.getServer().getAllUsernames());

					if (p_71516_2_.length == 4)
						return getListOfStringsFromIterableMatchingLastWord(p_71516_2_, func_147184_a(true));
				}
			} else if (p_71516_2_[0].equalsIgnoreCase("teams")) {
				if (p_71516_2_.length == 2)
					return getListOfStringsMatchingLastWord(p_71516_2_,
							"add", "remove", "join", "leave", "empty", "list", "option");

				if (p_71516_2_[1].equalsIgnoreCase("join")) {
					if (p_71516_2_.length == 3)
						return getListOfStringsFromIterableMatchingLastWord(p_71516_2_, func_147192_d().getTeamNames());

					if (p_71516_2_.length >= 4)
						return getListOfStringsMatchingLastWord(p_71516_2_,
								MinecraftServer.getServer().getAllUsernames());
				} else {
					if (p_71516_2_[1].equalsIgnoreCase("leave"))
						return getListOfStringsMatchingLastWord(p_71516_2_,
								MinecraftServer.getServer().getAllUsernames());

					if (!p_71516_2_[1].equalsIgnoreCase("empty") && !p_71516_2_[1].equalsIgnoreCase("list")
							&& !p_71516_2_[1].equalsIgnoreCase("remove")) {
						if (p_71516_2_[1].equalsIgnoreCase("option")) {
							if (p_71516_2_.length == 3)
								return getListOfStringsFromIterableMatchingLastWord(p_71516_2_,
										func_147192_d().getTeamNames());

							if (p_71516_2_.length == 4)
								return getListOfStringsMatchingLastWord(p_71516_2_,
										"color", "friendlyfire", "seeFriendlyInvisibles");

							if (p_71516_2_.length == 5) {
								if (p_71516_2_[3].equalsIgnoreCase("color"))
									return getListOfStringsFromIterableMatchingLastWord(p_71516_2_,
											EnumChatFormatting.getValidValues(true, false));

								if (p_71516_2_[3].equalsIgnoreCase("friendlyfire")
										|| p_71516_2_[3].equalsIgnoreCase("seeFriendlyInvisibles"))
									return getListOfStringsMatchingLastWord(p_71516_2_,
											"true", "false");
							}
						}
					} else if (p_71516_2_.length == 3)
						return getListOfStringsFromIterableMatchingLastWord(p_71516_2_, func_147192_d().getTeamNames());
				}
			}

			return null;
		}
	}

	protected List func_147184_a(boolean p_147184_1_) {
		Collection collection = func_147192_d().getScoreObjectives();
		ArrayList arraylist = new ArrayList();
		Iterator iterator = collection.iterator();

		while (iterator.hasNext()) {
			ScoreObjective scoreobjective = (ScoreObjective) iterator.next();

			if (!p_147184_1_ || !scoreobjective.getCriteria().isReadOnly()) {
				arraylist.add(scoreobjective.getName());
			}
		}

		return arraylist;
	}

	@Override
	public boolean isUsernameIndex(String[] p_82358_1_, int p_82358_2_) {
		return p_82358_1_[0].equalsIgnoreCase("players") ? p_82358_2_ == 2
				: p_82358_1_[0].equalsIgnoreCase("teams") && (p_82358_2_ == 2 || p_82358_2_ == 3);
	}
}