package net.minecraft.client.renderer;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class DestroyBlockProgress {
	private final int miningPlayerEntId;
	private final int partialBlockX;
	private final int partialBlockY;
	private final int partialBlockZ;
	private int partialBlockProgress;
	private int createdAtCloudUpdateTick;
	private static final String __OBFID = "CL_00001427";

	public DestroyBlockProgress(int p_i1511_1_, int p_i1511_2_, int p_i1511_3_, int p_i1511_4_) {
		miningPlayerEntId = p_i1511_1_;
		partialBlockX = p_i1511_2_;
		partialBlockY = p_i1511_3_;
		partialBlockZ = p_i1511_4_;
	}

	public int getPartialBlockX() {
		return partialBlockX;
	}

	public int getPartialBlockY() {
		return partialBlockY;
	}

	public int getPartialBlockZ() {
		return partialBlockZ;
	}

	public void setPartialBlockDamage(int p_73107_1_) {
		if (p_73107_1_ > 10) {
			p_73107_1_ = 10;
		}

		partialBlockProgress = p_73107_1_;
	}

	public int getPartialBlockDamage() {
		return partialBlockProgress;
	}

	public void setCloudUpdateTick(int p_82744_1_) {
		createdAtCloudUpdateTick = p_82744_1_;
	}

	public int getCreationCloudUpdateTick() {
		return createdAtCloudUpdateTick;
	}
}