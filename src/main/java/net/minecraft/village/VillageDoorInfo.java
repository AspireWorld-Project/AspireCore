package net.minecraft.village;

public class VillageDoorInfo {
	public final int posX;
	public final int posY;
	public final int posZ;
	public final int insideDirectionX;
	public final int insideDirectionZ;
	public int lastActivityTimestamp;
	public boolean isDetachedFromVillageFlag;
	private int doorOpeningRestrictionCounter;
	public VillageDoorInfo(int p_i1673_1_, int p_i1673_2_, int p_i1673_3_, int p_i1673_4_, int p_i1673_5_,
			int p_i1673_6_) {
		posX = p_i1673_1_;
		posY = p_i1673_2_;
		posZ = p_i1673_3_;
		insideDirectionX = p_i1673_4_;
		insideDirectionZ = p_i1673_5_;
		lastActivityTimestamp = p_i1673_6_;
	}

	public int getDistanceSquared(int p_75474_1_, int p_75474_2_, int p_75474_3_) {
		int l = p_75474_1_ - posX;
		int i1 = p_75474_2_ - posY;
		int j1 = p_75474_3_ - posZ;
		return l * l + i1 * i1 + j1 * j1;
	}

	public int getInsideDistanceSquare(int p_75469_1_, int p_75469_2_, int p_75469_3_) {
		int l = p_75469_1_ - posX - insideDirectionX;
		int i1 = p_75469_2_ - posY;
		int j1 = p_75469_3_ - posZ - insideDirectionZ;
		return l * l + i1 * i1 + j1 * j1;
	}

	public int getInsidePosX() {
		return posX + insideDirectionX;
	}

	public int getInsidePosY() {
		return posY;
	}

	public int getInsidePosZ() {
		return posZ + insideDirectionZ;
	}

	public boolean isInside(int p_75467_1_, int p_75467_2_) {
		int k = p_75467_1_ - posX;
		int l = p_75467_2_ - posZ;
		return k * insideDirectionX + l * insideDirectionZ >= 0;
	}

	public void resetDoorOpeningRestrictionCounter() {
		doorOpeningRestrictionCounter = 0;
	}

	public void incrementDoorOpeningRestrictionCounter() {
		++doorOpeningRestrictionCounter;
	}

	public int getDoorOpeningRestrictionCounter() {
		return doorOpeningRestrictionCounter;
	}
}