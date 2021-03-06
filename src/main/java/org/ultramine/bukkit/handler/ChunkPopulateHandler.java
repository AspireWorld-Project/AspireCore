package org.ultramine.bukkit.handler;

import cpw.mods.fml.common.IWorldGenerator;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.event.world.ChunkPopulateEvent;

import java.util.Random;

public class ChunkPopulateHandler implements IWorldGenerator {
	private final CraftServer server;

	public ChunkPopulateHandler(CraftServer server) {
		this.server = server;
	}

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator,
			IChunkProvider chunkProvider) {
		server.getPluginManager()
				.callEvent(new ChunkPopulateEvent(chunkGenerator.provideChunk(chunkX, chunkZ).getBukkitChunk()));
	}
}
