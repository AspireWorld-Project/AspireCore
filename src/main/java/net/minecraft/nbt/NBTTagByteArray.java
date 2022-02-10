package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class NBTTagByteArray extends NBTBase {
	private byte[] byteArray;
	private static final String __OBFID = "CL_00001213";

	NBTTagByteArray() {
	}

	public NBTTagByteArray(byte[] p_i45128_1_) {
		byteArray = p_i45128_1_;
	}

	@Override
	void write(DataOutput p_74734_1_) throws IOException {
		p_74734_1_.writeInt(byteArray.length);
		p_74734_1_.write(byteArray);
	}

	@Override
	void func_152446_a(DataInput p_152446_1_, int p_152446_2_, NBTSizeTracker p_152446_3_) throws IOException {
		p_152446_3_.func_152450_a(32); // Forge: Count the length as well
		int j = p_152446_1_.readInt();
		p_152446_3_.func_152450_a(8 * j);
		byteArray = new byte[j];
		p_152446_1_.readFully(byteArray);
	}

	@Override
	public byte getId() {
		return (byte) 7;
	}

	@Override
	public String toString() {
		return "[" + byteArray.length + " bytes]";
	}

	@Override
	public NBTBase copy() {
		byte[] abyte = new byte[byteArray.length];
		System.arraycopy(byteArray, 0, abyte, 0, byteArray.length);
		return new NBTTagByteArray(abyte);
	}

	@Override
	public boolean equals(Object p_equals_1_) {
		return super.equals(p_equals_1_) ? Arrays.equals(byteArray, ((NBTTagByteArray) p_equals_1_).byteArray) : false;
	}

	@Override
	public int hashCode() {
		return super.hashCode() ^ Arrays.hashCode(byteArray);
	}

	public byte[] func_150292_c() {
		return byteArray;
	}
}