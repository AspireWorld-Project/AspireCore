package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagInt extends NBTBase.NBTPrimitive {
	private int data;
	private static final String __OBFID = "CL_00001223";

	NBTTagInt() {
	}

	public NBTTagInt(int p_i45133_1_) {
		data = p_i45133_1_;
	}

	@Override
	void write(DataOutput p_74734_1_) throws IOException {
		p_74734_1_.writeInt(data);
	}

	@Override
	void func_152446_a(DataInput p_152446_1_, int p_152446_2_, NBTSizeTracker p_152446_3_) throws IOException {
		p_152446_3_.func_152450_a(32L);
		data = p_152446_1_.readInt();
	}

	@Override
	public byte getId() {
		return (byte) 3;
	}

	@Override
	public String toString() {
		return "" + data;
	}

	@Override
	public NBTBase copy() {
		return new NBTTagInt(data);
	}

	@Override
	public boolean equals(Object p_equals_1_) {
		if (super.equals(p_equals_1_)) {
			NBTTagInt nbttagint = (NBTTagInt) p_equals_1_;
			return data == nbttagint.data;
		} else
			return false;
	}

	@Override
	public int hashCode() {
		return super.hashCode() ^ data;
	}

	@Override
	public long func_150291_c() {
		return data;
	}

	@Override
	public int func_150287_d() {
		return data;
	}

	@Override
	public short func_150289_e() {
		return (short) (data & 65535);
	}

	@Override
	public byte func_150290_f() {
		return (byte) (data & 255);
	}

	@Override
	public double func_150286_g() {
		return data;
	}

	@Override
	public float func_150288_h() {
		return data;
	}
}