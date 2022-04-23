package net.minecraft.block;

import com.mojang.authlib.GameProfile;

public class BlockEventData {
	private final int coordX;
	private final int coordY;
	private final int coordZ;
	private final Block field_151344_d;
	private final int eventID;
	private final int eventParameter;
	public GameProfile initiator;

	public BlockEventData(int p_i45362_1_, int p_i45362_2_, int p_i45362_3_, Block p_i45362_4_, int p_i45362_5_,
			int p_i45362_6_) {
		coordX = p_i45362_1_;
		coordY = p_i45362_2_;
		coordZ = p_i45362_3_;
		eventID = p_i45362_5_;
		eventParameter = p_i45362_6_;
		field_151344_d = p_i45362_4_;
	}

	public int func_151340_a() {
		return coordX;
	}

	public int func_151342_b() {
		return coordY;
	}

	public int func_151341_c() {
		return coordZ;
	}

	public int getEventID() {
		return eventID;
	}

	public int getEventParameter() {
		return eventParameter;
	}

	public Block getBlock() {
		return field_151344_d;
	}

	@Override
	public boolean equals(Object p_equals_1_) {
		if (!(p_equals_1_ instanceof BlockEventData))
			return false;
		else {
			BlockEventData blockeventdata = (BlockEventData) p_equals_1_;
			return coordX == blockeventdata.coordX && coordY == blockeventdata.coordY && coordZ == blockeventdata.coordZ
					&& eventID == blockeventdata.eventID && eventParameter == blockeventdata.eventParameter
					&& field_151344_d == blockeventdata.field_151344_d;
		}
	}

	@Override
	public String toString() {
		return "TE(" + coordX + "," + coordY + "," + coordZ + ")," + eventID + "," + eventParameter + ","
				+ field_151344_d;
	}
}