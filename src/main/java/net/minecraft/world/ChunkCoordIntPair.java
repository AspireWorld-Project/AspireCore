package net.minecraft.world;

import org.ultramine.server.chunk.ChunkHash;

public class ChunkCoordIntPair {
	public final int chunkXPos;
	public final int chunkZPos;
	public ChunkCoordIntPair(int p_i1947_1_, int p_i1947_2_) {
		chunkXPos = p_i1947_1_;
		chunkZPos = p_i1947_2_;
	}

	public static long chunkXZ2Int(int p_77272_0_, int p_77272_1_) {
		return p_77272_0_ & 4294967295L | (p_77272_1_ & 4294967295L) << 32;
	}

	@Override
	public int hashCode() {
		return ChunkHash.chunkToKey(chunkXPos, chunkZPos);
	}

	@Override
	public boolean equals(Object p_equals_1_) {
		if (this == p_equals_1_)
			return true;
		else if (!(p_equals_1_ instanceof ChunkCoordIntPair))
			return false;
		else {
			ChunkCoordIntPair chunkcoordintpair = (ChunkCoordIntPair) p_equals_1_;
			return chunkXPos == chunkcoordintpair.chunkXPos && chunkZPos == chunkcoordintpair.chunkZPos;
		}
	}

	public int getCenterXPos() {
		return (chunkXPos << 4) + 8;
	}

	public int getCenterZPosition() {
		return (chunkZPos << 4) + 8;
	}

	public ChunkPosition func_151349_a(int p_151349_1_) {
		return new ChunkPosition(getCenterXPos(), p_151349_1_, getCenterZPosition());
	}

	@Override
	public String toString() {
		return "[" + chunkXPos + ", " + chunkZPos + "]";
	}
}