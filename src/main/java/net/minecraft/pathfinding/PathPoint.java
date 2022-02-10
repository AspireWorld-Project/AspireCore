package net.minecraft.pathfinding;

import net.minecraft.util.MathHelper;

public class PathPoint {
	public final int xCoord;
	public final int yCoord;
	public final int zCoord;
	private final int hash;
	int index = -1;
	float totalPathDistance;
	float distanceToNext;
	float distanceToTarget;
	PathPoint previous;
	public boolean isFirst;
	private static final String __OBFID = "CL_00000574";

	public PathPoint(int p_i2135_1_, int p_i2135_2_, int p_i2135_3_) {
		xCoord = p_i2135_1_;
		yCoord = p_i2135_2_;
		zCoord = p_i2135_3_;
		hash = makeHash(p_i2135_1_, p_i2135_2_, p_i2135_3_);
	}

	public static int makeHash(int p_75830_0_, int p_75830_1_, int p_75830_2_) {
		return p_75830_1_ & 255 | (p_75830_0_ & 32767) << 8 | (p_75830_2_ & 32767) << 24
				| (p_75830_0_ < 0 ? Integer.MIN_VALUE : 0) | (p_75830_2_ < 0 ? 32768 : 0);
	}

	public float distanceTo(PathPoint p_75829_1_) {
		float f = p_75829_1_.xCoord - xCoord;
		float f1 = p_75829_1_.yCoord - yCoord;
		float f2 = p_75829_1_.zCoord - zCoord;
		return MathHelper.sqrt_float(f * f + f1 * f1 + f2 * f2);
	}

	public float distanceToSquared(PathPoint p_75832_1_) {
		float f = p_75832_1_.xCoord - xCoord;
		float f1 = p_75832_1_.yCoord - yCoord;
		float f2 = p_75832_1_.zCoord - zCoord;
		return f * f + f1 * f1 + f2 * f2;
	}

	@Override
	public boolean equals(Object p_equals_1_) {
		if (!(p_equals_1_ instanceof PathPoint))
			return false;
		else {
			PathPoint pathpoint = (PathPoint) p_equals_1_;
			return hash == pathpoint.hash && xCoord == pathpoint.xCoord && yCoord == pathpoint.yCoord
					&& zCoord == pathpoint.zCoord;
		}
	}

	@Override
	public int hashCode() {
		return hash;
	}

	public boolean isAssigned() {
		return index >= 0;
	}

	@Override
	public String toString() {
		return xCoord + ", " + yCoord + ", " + zCoord;
	}
}