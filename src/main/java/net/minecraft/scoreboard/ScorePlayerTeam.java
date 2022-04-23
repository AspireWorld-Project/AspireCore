package net.minecraft.scoreboard;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class ScorePlayerTeam extends Team {
	private final Scoreboard theScoreboard;
	private final String field_96675_b;
	private final Set membershipSet = new HashSet();
	private String teamNameSPT;
	private String namePrefixSPT = "";
	private String colorSuffix = "";
	private boolean allowFriendlyFire = true;
	private boolean canSeeFriendlyInvisibles = true;
	private static final String __OBFID = "CL_00000616";

	public ScorePlayerTeam(Scoreboard p_i2308_1_, String p_i2308_2_) {
		theScoreboard = p_i2308_1_;
		field_96675_b = p_i2308_2_;
		teamNameSPT = p_i2308_2_;
	}

	@Override
	public String getRegisteredName() {
		return field_96675_b;
	}

	public String func_96669_c() {
		return teamNameSPT;
	}

	public void setTeamName(String p_96664_1_) {
		if (p_96664_1_ == null)
			throw new IllegalArgumentException("Name cannot be null");
		else {
			teamNameSPT = p_96664_1_;
			theScoreboard.broadcastTeamRemoved(this);
		}
	}

	public Collection getMembershipCollection() {
		return membershipSet;
	}

	public String getColorPrefix() {
		return namePrefixSPT;
	}

	public void setNamePrefix(String p_96666_1_) {
		if (p_96666_1_ == null)
			throw new IllegalArgumentException("Prefix cannot be null");
		else {
			namePrefixSPT = p_96666_1_;
			theScoreboard.broadcastTeamRemoved(this);
		}
	}

	public String getColorSuffix() {
		return colorSuffix;
	}

	public void setNameSuffix(String p_96662_1_) {
		if (p_96662_1_ == null)
			throw new IllegalArgumentException("Suffix cannot be null");
		else {
			colorSuffix = p_96662_1_;
			theScoreboard.broadcastTeamRemoved(this);
		}
	}

	@Override
	public String formatString(String p_142053_1_) {
		return getColorPrefix() + p_142053_1_ + getColorSuffix();
	}

	public static String formatPlayerName(Team p_96667_0_, String p_96667_1_) {
		return p_96667_0_ == null ? p_96667_1_ : p_96667_0_.formatString(p_96667_1_);
	}

	@Override
	public boolean getAllowFriendlyFire() {
		return allowFriendlyFire;
	}

	public void setAllowFriendlyFire(boolean p_96660_1_) {
		allowFriendlyFire = p_96660_1_;
		theScoreboard.broadcastTeamRemoved(this);
	}

	@Override
	public boolean func_98297_h() {
		return canSeeFriendlyInvisibles;
	}

	public void setSeeFriendlyInvisiblesEnabled(boolean p_98300_1_) {
		canSeeFriendlyInvisibles = p_98300_1_;
		theScoreboard.broadcastTeamRemoved(this);
	}

	public int func_98299_i() {
		int i = 0;

		if (getAllowFriendlyFire()) {
			i |= 1;
		}

		if (func_98297_h()) {
			i |= 2;
		}

		return i;
	}

	@SideOnly(Side.CLIENT)
	public void func_98298_a(int p_98298_1_) {
		setAllowFriendlyFire((p_98298_1_ & 1) > 0);
		setSeeFriendlyInvisiblesEnabled((p_98298_1_ & 2) > 0);
	}
}