package net.minecraftforge.common.util;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.ChunkCoordIntPair;

// Sorter to load nearby chunks first
public class ChunkCoordComparator implements java.util.Comparator<ChunkCoordIntPair> {
	private final int x;
	private final int z;

	public ChunkCoordComparator(EntityPlayerMP entityplayer) {
		x = (int) entityplayer.posX >> 4;
		z = (int) entityplayer.posZ >> 4;
	}

	@Override
	public int compare(ChunkCoordIntPair a, ChunkCoordIntPair b) {
		if (a.equals(b))
			return 0;

		// Subtract current position to set center point
		int ax = a.chunkXPos - x;
		int az = a.chunkZPos - z;
		int bx = b.chunkXPos - x;
		int bz = b.chunkZPos - z;
		int result = (ax - bx) * (ax + bx) + (az - bz) * (az + bz);

		if (result != 0)
			return result;

		if (ax < 0) {
			if (bx < 0)
				return bz - az;
			else
				return -1;
		} else {
			if (bx < 0)
				return 1;
			else
				return az - bz;
		}
	}
}