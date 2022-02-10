package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagLong extends NBTBase.NBTPrimitive {
	private long data;
	private static final String __OBFID = "CL_00001225";

	NBTTagLong() {
	}

	public NBTTagLong(long p_i45134_1_) {
		data = p_i45134_1_;
	}

	@Override
	void write(DataOutput p_74734_1_) throws IOException {
		p_74734_1_.writeLong(data);
	}

	@Override
	void func_152446_a(DataInput p_152446_1_, int p_152446_2_, NBTSizeTracker p_152446_3_) throws IOException {
		p_152446_3_.func_152450_a(64L);
		data = p_152446_1_.readLong();
	}

	@Override
	public byte getId() {
		return (byte) 4;
	}

	@Override
	public String toString() {
		return "" + data + "L";
	}

	@Override
	public NBTBase copy() {
		return new NBTTagLong(data);
	}

	@Override
	public boolean equals(Object p_equals_1_) {
		if (super.equals(p_equals_1_)) {
			NBTTagLong nbttaglong = (NBTTagLong) p_equals_1_;
			return data == nbttaglong.data;
		} else
			return false;
	}

	@Override
	public int hashCode() {
		return super.hashCode() ^ (int) (data ^ data >>> 32);
	}

	@Override
	public long func_150291_c() {
		return data;
	}

	@Override
	public int func_150287_d() {
		return (int) (data & -1L);
	}

	@Override
	public short func_150289_e() {
		return (short) (int) (data & 65535L);
	}

	@Override
	public byte func_150290_f() {
		return (byte) (int) (data & 255L);
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