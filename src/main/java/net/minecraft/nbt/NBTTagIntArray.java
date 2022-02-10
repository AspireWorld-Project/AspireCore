package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class NBTTagIntArray extends NBTBase {
	private int[] intArray;
	private static final String __OBFID = "CL_00001221";

	NBTTagIntArray() {
	}

	public NBTTagIntArray(int[] p_i45132_1_) {
		intArray = p_i45132_1_;
	}

	@Override
	void write(DataOutput p_74734_1_) throws IOException {
		p_74734_1_.writeInt(intArray.length);

		for (int i = 0; i < intArray.length; ++i) {
			p_74734_1_.writeInt(intArray[i]);
		}
	}

	@Override
	void func_152446_a(DataInput p_152446_1_, int p_152446_2_, NBTSizeTracker p_152446_3_) throws IOException {
		p_152446_3_.func_152450_a(32); // Forge: Count the length as well
		int j = p_152446_1_.readInt();
		p_152446_3_.func_152450_a(32 * j);
		intArray = new int[j];

		for (int k = 0; k < j; ++k) {
			intArray[k] = p_152446_1_.readInt();
		}
	}

	@Override
	public byte getId() {
		return (byte) 11;
	}

	@Override
	public String toString() {
		String s = "[";
		int[] aint = intArray;
		int i = aint.length;

		for (int j = 0; j < i; ++j) {
			int k = aint[j];
			s = s + k + ",";
		}

		return s + "]";
	}

	@Override
	public NBTBase copy() {
		int[] aint = new int[intArray.length];
		System.arraycopy(intArray, 0, aint, 0, intArray.length);
		return new NBTTagIntArray(aint);
	}

	@Override
	public boolean equals(Object p_equals_1_) {
		return super.equals(p_equals_1_) ? Arrays.equals(intArray, ((NBTTagIntArray) p_equals_1_).intArray) : false;
	}

	@Override
	public int hashCode() {
		return super.hashCode() ^ Arrays.hashCode(intArray);
	}

	public int[] func_150302_c() {
		return intArray;
	}
}