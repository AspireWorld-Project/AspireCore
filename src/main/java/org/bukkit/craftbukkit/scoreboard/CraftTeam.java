package org.bukkit.craftbukkit.scoreboard;

import java.util.Set;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.scoreboard.Team;

import com.google.common.collect.ImmutableSet;

final class CraftTeam extends CraftScoreboardComponent implements Team {
	private final net.minecraft.scoreboard.ScorePlayerTeam team;

	CraftTeam(CraftScoreboard scoreboard, net.minecraft.scoreboard.ScorePlayerTeam team) {
		super(scoreboard);
		this.team = team;
		scoreboard.teams.put(team.getRegisteredName(), this);
	}

	@Override
	public String getName() throws IllegalStateException {
		checkState();

		return team.getRegisteredName();
	}

	@Override
	public String getDisplayName() throws IllegalStateException {
		checkState();

		return team.func_96669_c();
	}

	@Override
	public void setDisplayName(String displayName) throws IllegalStateException {
		Validate.notNull(displayName, "Display name cannot be null");
		Validate.isTrue(displayName.length() <= 32,
				"Display name '" + displayName + "' is longer than the limit of 32 characters");
		checkState();

		team.setTeamName(displayName);
	}

	@Override
	public String getPrefix() throws IllegalStateException {
		checkState();

		return team.getColorPrefix();
	}

	@Override
	public void setPrefix(String prefix) throws IllegalStateException, IllegalArgumentException {
		Validate.notNull(prefix, "Prefix cannot be null");
		Validate.isTrue(prefix.length() <= 32, "Prefix '" + prefix + "' is longer than the limit of 32 characters");
		checkState();

		team.setNamePrefix(prefix);
	}

	@Override
	public String getSuffix() throws IllegalStateException {
		checkState();

		return team.getColorSuffix();
	}

	@Override
	public void setSuffix(String suffix) throws IllegalStateException, IllegalArgumentException {
		Validate.notNull(suffix, "Suffix cannot be null");
		Validate.isTrue(suffix.length() <= 32, "Suffix '" + suffix + "' is longer than the limit of 32 characters");
		checkState();

		team.setNameSuffix(suffix);
	}

	@Override
	public boolean allowFriendlyFire() throws IllegalStateException {
		checkState();

		return team.getAllowFriendlyFire();
	}

	@Override
	public void setAllowFriendlyFire(boolean enabled) throws IllegalStateException {
		checkState();

		team.setAllowFriendlyFire(enabled);
	}

	@Override
	public boolean canSeeFriendlyInvisibles() throws IllegalStateException {
		checkState();

		return team.func_98297_h();
	}

	@Override
	public void setCanSeeFriendlyInvisibles(boolean enabled) throws IllegalStateException {
		checkState();

		team.setSeeFriendlyInvisiblesEnabled(enabled);
	}

	@Override
	public Set<OfflinePlayer> getPlayers() throws IllegalStateException {
		checkState();

		ImmutableSet.Builder<OfflinePlayer> players = ImmutableSet.builder();
		for (Object o : team.getMembershipCollection()) {
			players.add(Bukkit.getOfflinePlayer(o.toString()));
		}
		return players.build();
	}

	@Override
	public int getSize() throws IllegalStateException {
		checkState();

		return team.getMembershipCollection().size();
	}

	@Override
	public void addPlayer(OfflinePlayer player) throws IllegalStateException, IllegalArgumentException {
		Validate.notNull(player, "OfflinePlayer cannot be null");
		CraftScoreboard scoreboard = checkState();

		scoreboard.board.func_151392_a(player.getName(), team.getRegisteredName());
	}

	@Override
	public boolean removePlayer(OfflinePlayer player) throws IllegalStateException, IllegalArgumentException {
		Validate.notNull(player, "OfflinePlayer cannot be null");
		CraftScoreboard scoreboard = checkState();

		if (!team.getMembershipCollection().contains(player.getName()))
			return false;

		scoreboard.board.removePlayerFromTeam(player.getName(), team);
		return true;
	}

	@Override
	public boolean hasPlayer(OfflinePlayer player) throws IllegalArgumentException, IllegalStateException {
		Validate.notNull(player, "OfflinePlayer cannot be null");
		checkState();

		return team.getMembershipCollection().contains(player.getName());
	}

	@Override
	public void unregister() throws IllegalStateException {
		CraftScoreboard scoreboard = checkState();

		scoreboard.board.removeTeam(team);
		scoreboard.teams.remove(team.getRegisteredName());
		setUnregistered();
	}
}
