package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagString extends NBTBase {
	private String data;
	private static final String __OBFID = "CL_00001228";

	public NBTTagString() {
		data = "";
	}

	public NBTTagString(String p_i1389_1_) {
		data = p_i1389_1_;

		if (p_i1389_1_ == null)
			throw new IllegalArgumentException("Empty string not allowed");
	}

	@Override
	void write(DataOutput p_74734_1_) throws IOException {
		p_74734_1_.writeUTF(data);
	}

	@Override
	void func_152446_a(DataInput p_152446_1_, int p_152446_2_, NBTSizeTracker p_152446_3_) throws IOException {
		data = p_152446_1_.readUTF();
		NBTSizeTracker.readUTF(p_152446_3_, data); // Forge: Correctly read String length including header.
	}

	@Override
	public byte getId() {
		return (byte) 8;
	}

	@Override
	public String toString() {
		return "\"" + data + "\"";
	}

	@Override
	public NBTBase copy() {
		return new NBTTagString(data);
	}

	@Override
	public boolean equals(Object p_equals_1_) {
		if (!super.equals(p_equals_1_))
			return false;
		else {
			NBTTagString nbttagstring = (NBTTagString) p_equals_1_;
			return data == null && nbttagstring.data == null || data != null && data.equals(nbttagstring.data);
		}
	}

	@Override
	public int hashCode() {
		return super.hashCode() ^ data.hashCode();
	}

	@Override
	public String func_150285_a_() {
		return data;
	}
}