package net.minecraft.server.management;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChunkCoordinates;

import java.util.Comparator;

@SuppressWarnings("rawtypes")
public class PlayerPositionComparator implements Comparator {
	private final ChunkCoordinates theChunkCoordinates;
	public PlayerPositionComparator(ChunkCoordinates p_i1499_1_) {
		theChunkCoordinates = p_i1499_1_;
	}

	public int compare(EntityPlayerMP p_compare_1_, EntityPlayerMP p_compare_2_) {
		double d0 = p_compare_1_.getDistanceSq(theChunkCoordinates.posX, theChunkCoordinates.posY,
				theChunkCoordinates.posZ);
		double d1 = p_compare_2_.getDistanceSq(theChunkCoordinates.posX, theChunkCoordinates.posY,
				theChunkCoordinates.posZ);
		return d0 < d1 ? -1 : d0 > d1 ? 1 : 0;
	}

	@Override
	public int compare(Object p_compare_1_, Object p_compare_2_) {
		return this.compare((EntityPlayerMP) p_compare_1_, (EntityPlayerMP) p_compare_2_);
	}
}