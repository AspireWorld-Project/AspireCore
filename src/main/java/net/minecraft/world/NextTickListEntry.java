package net.minecraft.world;

import net.minecraft.block.Block;

public class NextTickListEntry implements Comparable {
	private static long nextTickEntryID;
	private final Block field_151352_g;
	public int xCoord;
	public int yCoord;
	public int zCoord;
	public long scheduledTime;
	public int priority;
	private long tickEntryID;
	private static final String __OBFID = "CL_00000156";

	public NextTickListEntry(int p_i45370_1_, int p_i45370_2_, int p_i45370_3_, Block p_i45370_4_) {
		tickEntryID = nextTickEntryID++;
		xCoord = p_i45370_1_;
		yCoord = p_i45370_2_;
		zCoord = p_i45370_3_;
		field_151352_g = p_i45370_4_;
	}

	@Override
	public boolean equals(Object p_equals_1_) {
		if (!(p_equals_1_ instanceof NextTickListEntry))
			return false;
		else {
			NextTickListEntry nextticklistentry = (NextTickListEntry) p_equals_1_;
			return xCoord == nextticklistentry.xCoord && yCoord == nextticklistentry.yCoord
					&& zCoord == nextticklistentry.zCoord
					&& Block.isEqualTo(field_151352_g, nextticklistentry.field_151352_g);
		}
	}

	@Override
	public int hashCode() {
		return (xCoord * 1024 * 1024 + zCoord * 1024 + yCoord) * 256;
	}

	public NextTickListEntry setScheduledTime(long p_77176_1_) {
		scheduledTime = p_77176_1_;
		return this;
	}

	public void setPriority(int p_82753_1_) {
		priority = p_82753_1_;
	}

	public int compareTo(NextTickListEntry p_compareTo_1_) {
		return scheduledTime < p_compareTo_1_.scheduledTime ? -1
				: scheduledTime > p_compareTo_1_.scheduledTime ? 1
						: priority != p_compareTo_1_.priority ? priority - p_compareTo_1_.priority
								: tickEntryID < p_compareTo_1_.tickEntryID ? -1
										: tickEntryID > p_compareTo_1_.tickEntryID ? 1 : 0;
	}

	@Override
	public String toString() {
		return Block.getIdFromBlock(field_151352_g) + ": (" + xCoord + ", " + yCoord + ", " + zCoord + "), "
				+ scheduledTime + ", " + priority + ", " + tickEntryID;
	}

	public Block func_151351_a() {
		return field_151352_g;
	}

	@Override
	public int compareTo(Object p_compareTo_1_) {
		return this.compareTo((NextTickListEntry) p_compareTo_1_);
	}
}