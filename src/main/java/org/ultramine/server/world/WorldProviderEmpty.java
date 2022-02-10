package org.ultramine.server.world;

import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.IChunkProvider;

public class WorldProviderEmpty extends WorldProvider {

	@Override
	public IChunkProvider createChunkGenerator() {
		return new ChunkProviderEmpty(worldObj);
	}

	@Override
	public String getDimensionName() {
		return "EmptyWorld";
	}

}
