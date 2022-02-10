package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagShort extends NBTBase.NBTPrimitive {
	private short data;
	private static final String __OBFID = "CL_00001227";

	public NBTTagShort() {
	}

	public NBTTagShort(short p_i45135_1_) {
		data = p_i45135_1_;
	}

	@Override
	void write(DataOutput p_74734_1_) throws IOException {
		p_74734_1_.writeShort(data);
	}

	@Override
	void func_152446_a(DataInput p_152446_1_, int p_152446_2_, NBTSizeTracker p_152446_3_) throws IOException {
		p_152446_3_.func_152450_a(16L);
		data = p_152446_1_.readShort();
	}

	@Override
	public byte getId() {
		return (byte) 2;
	}

	@Override
	public String toString() {
		return "" + data + "s";
	}

	@Override
	public NBTBase copy() {
		return new NBTTagShort(data);
	}

	@Override
	public boolean equals(Object p_equals_1_) {
		if (super.equals(p_equals_1_)) {
			NBTTagShort nbttagshort = (NBTTagShort) p_equals_1_;
			return data == nbttagshort.data;
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
		return data;
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