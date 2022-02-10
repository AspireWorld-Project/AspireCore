package org.ultramine.server.chunk;

import org.ultramine.core.service.Service;

import net.minecraft.world.chunk.Chunk;

@Service
public interface AntiXRayService<T> {
	T prepareChunkSync(ChunkSnapshot chunkSnapshot, Chunk chunk);

	void prepareChunkAsync(ChunkSnapshot chunkSnapshot, T param);

	class EmptyImpl implements AntiXRayService<Void> {
		@Override
		public Void prepareChunkSync(ChunkSnapshot chunkSnapshot, Chunk chunk) {
			return null;
		}

		@Override
		public void prepareChunkAsync(ChunkSnapshot chunkSnapshot, Void v) {
		}
	}
}
