package net.minecraft.util;

@SuppressWarnings("rawtypes")
public class ChunkCoordinates implements Comparable {
	public int posX;
	public int posY;
	public int posZ;
	public ChunkCoordinates() {
	}

	public ChunkCoordinates(int p_i1354_1_, int p_i1354_2_, int p_i1354_3_) {
		posX = p_i1354_1_;
		posY = p_i1354_2_;
		posZ = p_i1354_3_;
	}

	public ChunkCoordinates(ChunkCoordinates p_i1355_1_) {
		posX = p_i1355_1_.posX;
		posY = p_i1355_1_.posY;
		posZ = p_i1355_1_.posZ;
	}

	@Override
	public boolean equals(Object p_equals_1_) {
		if (!(p_equals_1_ instanceof ChunkCoordinates))
			return false;
		else {
			ChunkCoordinates chunkcoordinates = (ChunkCoordinates) p_equals_1_;
			return posX == chunkcoordinates.posX && posY == chunkcoordinates.posY && posZ == chunkcoordinates.posZ;
		}
	}

	@Override
	public int hashCode() {
		return posX + posZ << 8 + posY << 16;
	}

	public int compareTo(ChunkCoordinates p_compareTo_1_) {
		return posY == p_compareTo_1_.posY
				? posZ == p_compareTo_1_.posZ ? posX - p_compareTo_1_.posX : posZ - p_compareTo_1_.posZ
				: posY - p_compareTo_1_.posY;
	}

	public void set(int p_71571_1_, int p_71571_2_, int p_71571_3_) {
		posX = p_71571_1_;
		posY = p_71571_2_;
		posZ = p_71571_3_;
	}

	public float getDistanceSquared(int p_71569_1_, int p_71569_2_, int p_71569_3_) {
		float f = posX - p_71569_1_;
		float f1 = posY - p_71569_2_;
		float f2 = posZ - p_71569_3_;
		return f * f + f1 * f1 + f2 * f2;
	}

	public float getDistanceSquaredToChunkCoordinates(ChunkCoordinates p_82371_1_) {
		return getDistanceSquared(p_82371_1_.posX, p_82371_1_.posY, p_82371_1_.posZ);
	}

	@Override
	public String toString() {
		return "Pos{x=" + posX + ", y=" + posY + ", z=" + posZ + '}';
	}

	@Override
	public int compareTo(Object p_compareTo_1_) {
		return this.compareTo((ChunkCoordinates) p_compareTo_1_);
	}
}