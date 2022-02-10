package net.minecraft.scoreboard;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ScoreObjective {
	private final Scoreboard theScoreboard;
	private final String name;
	private final IScoreObjectiveCriteria objectiveCriteria;
	private String displayName;
	private static final String __OBFID = "CL_00000614";

	public ScoreObjective(Scoreboard p_i2307_1_, String p_i2307_2_, IScoreObjectiveCriteria p_i2307_3_) {
		theScoreboard = p_i2307_1_;
		name = p_i2307_2_;
		objectiveCriteria = p_i2307_3_;
		displayName = p_i2307_2_;
	}

	@SideOnly(Side.CLIENT)
	public Scoreboard getScoreboard() {
		return theScoreboard;
	}

	public String getName() {
		return name;
	}

	public IScoreObjectiveCriteria getCriteria() {
		return objectiveCriteria;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String p_96681_1_) {
		displayName = p_96681_1_;
		theScoreboard.func_96532_b(this);
	}
}