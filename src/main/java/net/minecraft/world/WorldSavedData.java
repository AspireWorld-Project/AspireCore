package net.minecraft.world;

import net.minecraft.nbt.NBTTagCompound;

public abstract class WorldSavedData {
	public final String mapName;
	private boolean dirty;
	public WorldSavedData(String p_i2141_1_) {
		mapName = p_i2141_1_;
	}

	public abstract void readFromNBT(NBTTagCompound p_76184_1_);

	public abstract void writeToNBT(NBTTagCompound p_76187_1_);

	public void markDirty() {
		setDirty(true);
	}

	public void setDirty(boolean p_76186_1_) {
		dirty = p_76186_1_;
	}

	public boolean isDirty() {
		return dirty;
	}
}