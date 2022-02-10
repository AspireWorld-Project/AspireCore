package net.minecraft.pathfinding;

public class Path {
	private PathPoint[] pathPoints = new PathPoint[1024];
	private int count;
	private static final String __OBFID = "CL_00000573";

	public PathPoint addPoint(PathPoint p_75849_1_) {
		if (p_75849_1_.index >= 0)
			throw new IllegalStateException("OW KNOWS!");
		else {
			if (count == pathPoints.length) {
				PathPoint[] apathpoint = new PathPoint[count << 1];
				System.arraycopy(pathPoints, 0, apathpoint, 0, count);
				pathPoints = apathpoint;
			}

			pathPoints[count] = p_75849_1_;
			p_75849_1_.index = count;
			sortBack(count++);
			return p_75849_1_;
		}
	}

	public void clearPath() {
		count = 0;
	}

	public PathPoint dequeue() {
		PathPoint pathpoint = pathPoints[0];
		pathPoints[0] = pathPoints[--count];
		pathPoints[count] = null;

		if (count > 0) {
			sortForward(0);
		}

		pathpoint.index = -1;
		return pathpoint;
	}

	public void changeDistance(PathPoint p_75850_1_, float p_75850_2_) {
		float f1 = p_75850_1_.distanceToTarget;
		p_75850_1_.distanceToTarget = p_75850_2_;

		if (p_75850_2_ < f1) {
			sortBack(p_75850_1_.index);
		} else {
			sortForward(p_75850_1_.index);
		}
	}

	private void sortBack(int p_75847_1_) {
		PathPoint pathpoint = pathPoints[p_75847_1_];
		int j;

		for (float f = pathpoint.distanceToTarget; p_75847_1_ > 0; p_75847_1_ = j) {
			j = p_75847_1_ - 1 >> 1;
			PathPoint pathpoint1 = pathPoints[j];

			if (f >= pathpoint1.distanceToTarget) {
				break;
			}

			pathPoints[p_75847_1_] = pathpoint1;
			pathpoint1.index = p_75847_1_;
		}

		pathPoints[p_75847_1_] = pathpoint;
		pathpoint.index = p_75847_1_;
	}

	private void sortForward(int p_75846_1_) {
		PathPoint pathpoint = pathPoints[p_75846_1_];
		float f = pathpoint.distanceToTarget;

		while (true) {
			int j = 1 + (p_75846_1_ << 1);
			int k = j + 1;

			if (j >= count) {
				break;
			}

			PathPoint pathpoint1 = pathPoints[j];
			float f1 = pathpoint1.distanceToTarget;
			PathPoint pathpoint2;
			float f2;

			if (k >= count) {
				pathpoint2 = null;
				f2 = Float.POSITIVE_INFINITY;
			} else {
				pathpoint2 = pathPoints[k];
				f2 = pathpoint2.distanceToTarget;
			}

			if (f1 < f2) {
				if (f1 >= f) {
					break;
				}

				pathPoints[p_75846_1_] = pathpoint1;
				pathpoint1.index = p_75846_1_;
				p_75846_1_ = j;
			} else {
				if (f2 >= f) {
					break;
				}

				pathPoints[p_75846_1_] = pathpoint2;
				pathpoint2.index = p_75846_1_;
				p_75846_1_ = k;
			}
		}

		pathPoints[p_75846_1_] = pathpoint;
		pathpoint.index = p_75846_1_;
	}

	public boolean isPathEmpty() {
		return count == 0;
	}
}