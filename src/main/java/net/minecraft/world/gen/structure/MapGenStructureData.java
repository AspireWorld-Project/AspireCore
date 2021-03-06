package net.minecraft.world.gen.structure;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldSavedData;

import java.util.Map;

public class MapGenStructureData extends WorldSavedData {
	private NBTTagCompound field_143044_a = new NBTTagCompound();
	public MapGenStructureData(String p_i43001_1_) {
		super(p_i43001_1_);
	}

	@Override
	public void readFromNBT(NBTTagCompound p_76184_1_) {
		field_143044_a = p_76184_1_.getCompoundTag("Features");
	}

	@Override
	public void writeToNBT(NBTTagCompound p_76187_1_) {
		if (field_143044_a == null)
			throw new IllegalStateException();
		p_76187_1_.setTag("Features", field_143044_a);
	}

	public void func_143043_a(NBTTagCompound p_143043_1_, int p_143043_2_, int p_143043_3_) {
		if (field_143044_a != null) {
			field_143044_a.setTag(func_143042_b(p_143043_2_, p_143043_3_), p_143043_1_);
		}
	}

	public static String func_143042_b(int p_143042_0_, int p_143042_1_) {
		return "[" + p_143042_0_ + "," + p_143042_1_ + "]";
	}

	public NBTTagCompound func_143041_a() {
		return field_143044_a;
	}

	/*
	 * ======================================== ULTRAMINE START
	 * =====================================
	 */

	private Map<Long, StructureStart> structureMap;

	public void replaceNbtWithStrictureMap(Map<Long, StructureStart> structureMap) {
		this.structureMap = structureMap;
		field_143044_a = null;
	}

	public Map<Long, StructureStart> getStructureMap() {
		return structureMap;
	}
}