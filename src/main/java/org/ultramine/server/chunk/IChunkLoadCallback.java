package org.ultramine.server.chunk;

import net.minecraft.world.chunk.Chunk;

public interface IChunkLoadCallback {
	IChunkLoadCallback EMPTY = new IChunkLoadCallback() {
		@Override
		public void onChunkLoaded(Chunk chunk) {
		}
	};

	void onChunkLoaded(Chunk chunk);
}
