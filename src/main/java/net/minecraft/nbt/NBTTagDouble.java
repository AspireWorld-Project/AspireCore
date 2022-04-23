package net.minecraft.nbt;

import net.minecraft.util.MathHelper;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagDouble extends NBTBase.NBTPrimitive {
	private double data;
	private static final String __OBFID = "CL_00001218";

	NBTTagDouble() {
	}

	public NBTTagDouble(double p_i45130_1_) {
		data = p_i45130_1_;
	}

	@Override
	void write(DataOutput p_74734_1_) throws IOException {
		p_74734_1_.writeDouble(data);
	}

	@Override
	void func_152446_a(DataInput p_152446_1_, int p_152446_2_, NBTSizeTracker p_152446_3_) throws IOException {
		p_152446_3_.func_152450_a(64L);
		data = p_152446_1_.readDouble();
	}

	@Override
	public byte getId() {
		return (byte) 6;
	}

	@Override
	public String toString() {
		return "" + data + "d";
	}

	@Override
	public NBTBase copy() {
		return new NBTTagDouble(data);
	}

	@Override
	public boolean equals(Object p_equals_1_) {
		if (super.equals(p_equals_1_)) {
			NBTTagDouble nbttagdouble = (NBTTagDouble) p_equals_1_;
			return data == nbttagdouble.data;
		} else
			return false;
	}

	@Override
	public int hashCode() {
		long i = Double.doubleToLongBits(data);
		return super.hashCode() ^ (int) (i ^ i >>> 32);
	}

	@Override
	public long func_150291_c() {
		return (long) Math.floor(data);
	}

	@Override
	public int func_150287_d() {
		return MathHelper.floor_double(data);
	}

	@Override
	public short func_150289_e() {
		return (short) (MathHelper.floor_double(data) & 65535);
	}

	@Override
	public byte func_150290_f() {
		return (byte) (MathHelper.floor_double(data) & 255);
	}

	@Override
	public double func_150286_g() {
		return data;
	}

	@Override
	public float func_150288_h() {
		return (float) data;
	}
}