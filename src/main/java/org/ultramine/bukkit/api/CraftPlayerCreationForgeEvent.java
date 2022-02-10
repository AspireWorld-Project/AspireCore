package org.ultramine.bukkit.api;

import org.bukkit.craftbukkit.entity.CraftPlayer;

import cpw.mods.fml.common.eventhandler.Event;

public class CraftPlayerCreationForgeEvent extends Event {
	private final CraftPlayer player;

	public CraftPlayerCreationForgeEvent(CraftPlayer player) {
		this.player = player;
	}

	public CraftPlayer getPlayer() {
		return player;
	}
}
