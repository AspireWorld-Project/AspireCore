package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagEnd extends NBTBase {
	private static final String __OBFID = "CL_00001219";

	@Override
	void func_152446_a(DataInput p_152446_1_, int p_152446_2_, NBTSizeTracker p_152446_3_) throws IOException {
	}

	@Override
	void write(DataOutput p_74734_1_) throws IOException {
	}

	@Override
	public byte getId() {
		return (byte) 0;
	}

	@Override
	public String toString() {
		return "END";
	}

	@Override
	public NBTBase copy() {
		return new NBTTagEnd();
	}
}