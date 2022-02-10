package org.bukkit.craftbukkit.scoreboard;

import org.apache.commons.lang.Validate;
import org.bukkit.OfflinePlayer;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;

final class CraftObjective extends CraftScoreboardComponent implements Objective {
	private final net.minecraft.scoreboard.ScoreObjective objective;
	private final CraftCriteria criteria;

	CraftObjective(CraftScoreboard scoreboard, net.minecraft.scoreboard.ScoreObjective objective) {
		super(scoreboard);
		this.objective = objective;
		criteria = CraftCriteria.getFromNMS(objective);

		scoreboard.objectives.put(objective.getName(), this);
	}

	net.minecraft.scoreboard.ScoreObjective getHandle() {
		return objective;
	}

	@Override
	public String getName() throws IllegalStateException {
		checkState();

		return objective.getName();
	}

	@Override
	public String getDisplayName() throws IllegalStateException {
		checkState();

		return objective.getDisplayName();
	}

	@Override
	public void setDisplayName(String displayName) throws IllegalStateException, IllegalArgumentException {
		Validate.notNull(displayName, "Display name cannot be null");
		Validate.isTrue(displayName.length() <= 32,
				"Display name '" + displayName + "' is longer than the limit of 32 characters");
		checkState();

		objective.setDisplayName(displayName);
	}

	@Override
	public String getCriteria() throws IllegalStateException {
		checkState();

		return criteria.bukkitName;
	}

	@Override
	public boolean isModifiable() throws IllegalStateException {
		checkState();

		return !criteria.criteria.isReadOnly();
	}

	@Override
	public void setDisplaySlot(DisplaySlot slot) throws IllegalStateException {
		CraftScoreboard scoreboard = checkState();
		net.minecraft.scoreboard.Scoreboard board = scoreboard.board;
		net.minecraft.scoreboard.ScoreObjective objective = this.objective;

		for (int i = 0; i < CraftScoreboardTranslations.MAX_DISPLAY_SLOT; i++) {
			if (board.func_96539_a(i) == objective) {
				board.func_96530_a(i, null);
			}
		}
		if (slot != null) {
			int slotNumber = CraftScoreboardTranslations.fromBukkitSlot(slot);
			board.func_96530_a(slotNumber, getHandle());
		}
	}

	@Override
	public DisplaySlot getDisplaySlot() throws IllegalStateException {
		CraftScoreboard scoreboard = checkState();
		net.minecraft.scoreboard.Scoreboard board = scoreboard.board;
		net.minecraft.scoreboard.ScoreObjective objective = this.objective;

		for (int i = 0; i < CraftScoreboardTranslations.MAX_DISPLAY_SLOT; i++) {
			if (board.func_96539_a(i) == objective)
				return CraftScoreboardTranslations.toBukkitSlot(i);
		}
		return null;
	}

	@Override
	public Score getScore(OfflinePlayer player) throws IllegalArgumentException, IllegalStateException {
		Validate.notNull(player, "Player cannot be null");
		checkState();

		return new CraftScore(this, player.getName());
	}

	@Override
	public Score getScore(String entry) throws IllegalArgumentException, IllegalStateException {
		Validate.notNull(entry, "Entry cannot be null");
		checkState();

		return new CraftScore(this, entry);
	}

	@Override
	public void unregister() throws IllegalStateException {
		CraftScoreboard scoreboard = checkState();

		scoreboard.objectives.remove(getName());
		scoreboard.board.func_96519_k(objective);
		setUnregistered();
	}
}
