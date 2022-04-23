package net.minecraft.world.chunk.storage;

public class NibbleArrayReader {
	public final byte[] data;
	private final int depthBits;
	private final int depthBitsPlusFour;
	public NibbleArrayReader(byte[] p_i1998_1_, int p_i1998_2_) {
		data = p_i1998_1_;
		depthBits = p_i1998_2_;
		depthBitsPlusFour = p_i1998_2_ + 4;
	}

	public int get(int p_76686_1_, int p_76686_2_, int p_76686_3_) {
		int l = p_76686_1_ << depthBitsPlusFour | p_76686_3_ << depthBits | p_76686_2_;
		int i1 = l >> 1;
		int j1 = l & 1;
		return j1 == 0 ? data[i1] & 15 : data[i1] >> 4 & 15;
	}
}