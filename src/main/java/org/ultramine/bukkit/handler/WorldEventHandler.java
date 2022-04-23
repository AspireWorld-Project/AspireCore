package org.ultramine.bukkit.handler;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.WorldEvent;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.event.world.*;

public class WorldEventHandler {
	private final CraftServer server;

	public WorldEventHandler(CraftServer server) {
		this.server = server;
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onWorldLoad(WorldEvent.Load e) {
		CraftWorld world = e.world.getWorld();
		if (e.world.provider.dimensionId == 0) {
			server.scoreboardManager = new org.bukkit.craftbukkit.scoreboard.CraftScoreboardManager(
					MinecraftServer.getServer(), e.world.getScoreboard());
		}
		server.getPluginManager().callEvent(new WorldInitEvent(world)); // TODO call only once per world
		server.getPluginManager().callEvent(new WorldLoadEvent(world));
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onWorldUnload(WorldEvent.Unload e) {
		server.getPluginManager().callEvent(new WorldUnloadEvent(e.world.getWorld()));
		server.removeWorld(e.world.getWorld());
	}

	@SubscribeEvent
	public void onWorldUnload(WorldEvent.Save e) {
		server.getPluginManager().callEvent(new WorldSaveEvent(e.world.getWorld()));
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onChunkLoad(ChunkEvent.Load e) {
		server.getPluginManager().callEvent(new ChunkLoadEvent(e.getChunk().getBukkitChunk(), false)); // TODO
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onChunkUnload(ChunkEvent.Unload e) {
		server.getPluginManager().callEvent(new ChunkUnloadEvent(e.getChunk().getBukkitChunk()));
	}
}
