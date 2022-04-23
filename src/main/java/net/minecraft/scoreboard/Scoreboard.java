package net.minecraft.scoreboard;

import java.util.*;

public class Scoreboard {
	private final Map scoreObjectives = new HashMap();
	private final Map scoreObjectiveCriterias = new HashMap();
	private final Map field_96544_c = new HashMap();
	private final ScoreObjective[] field_96541_d = new ScoreObjective[3];
	private final Map teams = new HashMap();
	private final Map teamMemberships = new HashMap();
	private static final String __OBFID = "CL_00000619";

	public ScoreObjective getObjective(String p_96518_1_) {
		return (ScoreObjective) scoreObjectives.get(p_96518_1_);
	}

	public ScoreObjective addScoreObjective(String p_96535_1_, IScoreObjectiveCriteria p_96535_2_) {
		ScoreObjective scoreobjective = getObjective(p_96535_1_);

		if (scoreobjective != null)
			throw new IllegalArgumentException("An objective with the name '" + p_96535_1_ + "' already exists!");
		else {
			scoreobjective = new ScoreObjective(this, p_96535_1_, p_96535_2_);
			Object object = scoreObjectiveCriterias.get(p_96535_2_);

			if (object == null) {
				object = new ArrayList();
				scoreObjectiveCriterias.put(p_96535_2_, object);
			}

			((List) object).add(scoreobjective);
			scoreObjectives.put(p_96535_1_, scoreobjective);
			func_96522_a(scoreobjective);
			return scoreobjective;
		}
	}

	public Collection func_96520_a(IScoreObjectiveCriteria p_96520_1_) {
		Collection collection = (Collection) scoreObjectiveCriterias.get(p_96520_1_);
		return collection == null ? new ArrayList() : new ArrayList(collection);
	}

	public Score func_96529_a(String p_96529_1_, ScoreObjective p_96529_2_) {
		Object object = field_96544_c.get(p_96529_1_);

		if (object == null) {
			object = new HashMap();
			field_96544_c.put(p_96529_1_, object);
		}

		Score score = (Score) ((Map) object).get(p_96529_2_);

		if (score == null) {
			score = new Score(this, p_96529_2_, p_96529_1_);
			((Map) object).put(p_96529_2_, score);
		}

		return score;
	}

	public Collection func_96534_i(ScoreObjective p_96534_1_) {
		ArrayList arraylist = new ArrayList();
		Iterator iterator = field_96544_c.values().iterator();

		while (iterator.hasNext()) {
			Map map = (Map) iterator.next();
			Score score = (Score) map.get(p_96534_1_);

			if (score != null) {
				arraylist.add(score);
			}
		}

		Collections.sort(arraylist, Score.field_96658_a);
		return arraylist;
	}

	public Collection getScoreObjectives() {
		return scoreObjectives.values();
	}

	public Collection getObjectiveNames() {
		return field_96544_c.keySet();
	}

	public void func_96515_c(String p_96515_1_) {
		Map map = (Map) field_96544_c.remove(p_96515_1_);

		if (map != null) {
			func_96516_a(p_96515_1_);
		}
	}

	public Collection func_96528_e() {
		Collection collection = field_96544_c.values();
		ArrayList arraylist = new ArrayList();
		Iterator iterator = collection.iterator();

		while (iterator.hasNext()) {
			Map map = (Map) iterator.next();
			arraylist.addAll(map.values());
		}

		return arraylist;
	}

	public Map func_96510_d(String p_96510_1_) {
		Object object = field_96544_c.get(p_96510_1_);

		if (object == null) {
			object = new HashMap();
		}

		return (Map) object;
	}

	public void func_96519_k(ScoreObjective p_96519_1_) {
		scoreObjectives.remove(p_96519_1_.getName());

		for (int i = 0; i < 3; ++i) {
			if (func_96539_a(i) == p_96519_1_) {
				func_96530_a(i, null);
			}
		}

		List list = (List) scoreObjectiveCriterias.get(p_96519_1_.getCriteria());

		if (list != null) {
			list.remove(p_96519_1_);
		}

		Iterator iterator = field_96544_c.values().iterator();

		while (iterator.hasNext()) {
			Map map = (Map) iterator.next();
			map.remove(p_96519_1_);
		}

		func_96533_c(p_96519_1_);
	}

	public void func_96530_a(int p_96530_1_, ScoreObjective p_96530_2_) {
		field_96541_d[p_96530_1_] = p_96530_2_;
	}

	public ScoreObjective func_96539_a(int p_96539_1_) {
		return field_96541_d[p_96539_1_];
	}

	public ScorePlayerTeam getTeam(String p_96508_1_) {
		return (ScorePlayerTeam) teams.get(p_96508_1_);
	}

	public ScorePlayerTeam createTeam(String p_96527_1_) {
		ScorePlayerTeam scoreplayerteam = getTeam(p_96527_1_);

		if (scoreplayerteam != null)
			throw new IllegalArgumentException("A team with the name '" + p_96527_1_ + "' already exists!");
		else {
			scoreplayerteam = new ScorePlayerTeam(this, p_96527_1_);
			teams.put(p_96527_1_, scoreplayerteam);
			broadcastTeamCreated(scoreplayerteam);
			return scoreplayerteam;
		}
	}

	public void removeTeam(ScorePlayerTeam p_96511_1_) {
		teams.remove(p_96511_1_.getRegisteredName());
		Iterator iterator = p_96511_1_.getMembershipCollection().iterator();

		while (iterator.hasNext()) {
			String s = (String) iterator.next();
			teamMemberships.remove(s);
		}

		func_96513_c(p_96511_1_);
	}

	public boolean func_151392_a(String p_151392_1_, String p_151392_2_) {
		if (!teams.containsKey(p_151392_2_))
			return false;
		else {
			ScorePlayerTeam scoreplayerteam = getTeam(p_151392_2_);

			if (getPlayersTeam(p_151392_1_) != null) {
				removePlayerFromTeams(p_151392_1_);
			}

			teamMemberships.put(p_151392_1_, scoreplayerteam);
			scoreplayerteam.getMembershipCollection().add(p_151392_1_);
			return true;
		}
	}

	public boolean removePlayerFromTeams(String p_96524_1_) {
		ScorePlayerTeam scoreplayerteam = getPlayersTeam(p_96524_1_);

		if (scoreplayerteam != null) {
			removePlayerFromTeam(p_96524_1_, scoreplayerteam);
			return true;
		} else
			return false;
	}

	public void removePlayerFromTeam(String p_96512_1_, ScorePlayerTeam p_96512_2_) {
		if (getPlayersTeam(p_96512_1_) != p_96512_2_)
			throw new IllegalStateException(
					"Player is either on another team or not on any team. Cannot remove from team '"
							+ p_96512_2_.getRegisteredName() + "'.");
		else {
			teamMemberships.remove(p_96512_1_);
			p_96512_2_.getMembershipCollection().remove(p_96512_1_);
		}
	}

	public Collection getTeamNames() {
		return teams.keySet();
	}

	public Collection getTeams() {
		return teams.values();
	}

	public ScorePlayerTeam getPlayersTeam(String p_96509_1_) {
		return (ScorePlayerTeam) teamMemberships.get(p_96509_1_);
	}

	public void func_96522_a(ScoreObjective p_96522_1_) {
	}

	public void func_96532_b(ScoreObjective p_96532_1_) {
	}

	public void func_96533_c(ScoreObjective p_96533_1_) {
	}

	public void func_96536_a(Score p_96536_1_) {
	}

	public void func_96516_a(String p_96516_1_) {
	}

	public void broadcastTeamCreated(ScorePlayerTeam p_96523_1_) {
	}

	public void broadcastTeamRemoved(ScorePlayerTeam p_96538_1_) {
	}

	public void func_96513_c(ScorePlayerTeam p_96513_1_) {
	}

	public static String getObjectiveDisplaySlot(int p_96517_0_) {
		switch (p_96517_0_) {
		case 0:
			return "list";
		case 1:
			return "sidebar";
		case 2:
			return "belowName";
		default:
			return null;
		}
	}

	public static int getObjectiveDisplaySlotNumber(String p_96537_0_) {
		return p_96537_0_.equalsIgnoreCase("list") ? 0
				: p_96537_0_.equalsIgnoreCase("sidebar") ? 1 : p_96537_0_.equalsIgnoreCase("belowName") ? 2 : -1;
	}
}