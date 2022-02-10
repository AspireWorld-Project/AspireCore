package org.bukkit.craftbukkit.util;

import java.util.HashSet;
import java.util.List;

import org.bukkit.entity.Player;

public class LazyPlayerSet extends LazyHashSet<Player> {

	@SuppressWarnings("unchecked")
	@Override
	HashSet<Player> makeReference() {
		if (reference != null)
			throw new IllegalStateException("Reference already created!");
		List<net.minecraft.entity.player.EntityPlayerMP> players = net.minecraft.server.MinecraftServer.getServer()
				.getConfigurationManager().playerEntityList;
		HashSet<Player> reference = new HashSet<>(players.size());
		for (net.minecraft.entity.player.EntityPlayerMP player : players) {
			reference.add((Player) player.getBukkitEntity());
		}
		return reference;
	}

}
