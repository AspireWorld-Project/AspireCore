package net.minecraft.pathfinding;

import net.minecraft.entity.Entity;
import net.minecraft.util.Vec3;

public class PathEntity {
	private final PathPoint[] points;
	private int currentPathIndex;
	private int pathLength;
	private static final String __OBFID = "CL_00000575";

	public PathEntity(PathPoint[] p_i2136_1_) {
		points = p_i2136_1_;
		pathLength = p_i2136_1_.length;
	}

	public void incrementPathIndex() {
		++currentPathIndex;
	}

	public boolean isFinished() {
		return currentPathIndex >= pathLength;
	}

	public PathPoint getFinalPathPoint() {
		return pathLength > 0 ? points[pathLength - 1] : null;
	}

	public PathPoint getPathPointFromIndex(int p_75877_1_) {
		return points[p_75877_1_];
	}

	public int getCurrentPathLength() {
		return pathLength;
	}

	public void setCurrentPathLength(int p_75871_1_) {
		pathLength = p_75871_1_;
	}

	public int getCurrentPathIndex() {
		return currentPathIndex;
	}

	public void setCurrentPathIndex(int p_75872_1_) {
		currentPathIndex = p_75872_1_;
	}

	public Vec3 getVectorFromIndex(Entity p_75881_1_, int p_75881_2_) {
		double d0 = points[p_75881_2_].xCoord + (int) (p_75881_1_.width + 1.0F) * 0.5D;
		double d1 = points[p_75881_2_].yCoord;
		double d2 = points[p_75881_2_].zCoord + (int) (p_75881_1_.width + 1.0F) * 0.5D;
		return Vec3.createVectorHelper(d0, d1, d2);
	}

	public Vec3 getPosition(Entity p_75878_1_) {
		return getVectorFromIndex(p_75878_1_, currentPathIndex);
	}

	public boolean isSamePath(PathEntity p_75876_1_) {
		if (p_75876_1_ == null)
			return false;
		else if (p_75876_1_.points.length != points.length)
			return false;
		else {
			for (int i = 0; i < points.length; ++i) {
				if (points[i].xCoord != p_75876_1_.points[i].xCoord || points[i].yCoord != p_75876_1_.points[i].yCoord
						|| points[i].zCoord != p_75876_1_.points[i].zCoord)
					return false;
			}

			return true;
		}
	}

	public boolean isDestinationSame(Vec3 p_75880_1_) {
		PathPoint pathpoint = getFinalPathPoint();
		return pathpoint == null ? false
				: pathpoint.xCoord == (int) p_75880_1_.xCoord && pathpoint.zCoord == (int) p_75880_1_.zCoord;
	}
}