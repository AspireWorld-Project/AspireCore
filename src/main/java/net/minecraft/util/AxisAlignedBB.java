package net.minecraft.util;

public class AxisAlignedBB {
	public double minX;
	public double minY;
	public double minZ;
	public double maxX;
	public double maxY;
	public double maxZ;
	private static final String __OBFID = "CL_00000607";

	public static AxisAlignedBB getBoundingBox(double p_72330_0_, double p_72330_2_, double p_72330_4_,
			double p_72330_6_, double p_72330_8_, double p_72330_10_) {
		return new AxisAlignedBB(p_72330_0_, p_72330_2_, p_72330_4_, p_72330_6_, p_72330_8_, p_72330_10_);
	}

	protected AxisAlignedBB(double p_i2300_1_, double p_i2300_3_, double p_i2300_5_, double p_i2300_7_,
			double p_i2300_9_, double p_i2300_11_) {
		minX = p_i2300_1_;
		minY = p_i2300_3_;
		minZ = p_i2300_5_;
		maxX = p_i2300_7_;
		maxY = p_i2300_9_;
		maxZ = p_i2300_11_;
	}

	public AxisAlignedBB setBounds(double p_72324_1_, double p_72324_3_, double p_72324_5_, double p_72324_7_,
			double p_72324_9_, double p_72324_11_) {
		minX = p_72324_1_;
		minY = p_72324_3_;
		minZ = p_72324_5_;
		maxX = p_72324_7_;
		maxY = p_72324_9_;
		maxZ = p_72324_11_;
		return this;
	}

	public AxisAlignedBB addCoord(double p_72321_1_, double p_72321_3_, double p_72321_5_) {
		double d3 = minX;
		double d4 = minY;
		double d5 = minZ;
		double d6 = maxX;
		double d7 = maxY;
		double d8 = maxZ;

		if (p_72321_1_ < 0.0D) {
			d3 += p_72321_1_;
		}

		if (p_72321_1_ > 0.0D) {
			d6 += p_72321_1_;
		}

		if (p_72321_3_ < 0.0D) {
			d4 += p_72321_3_;
		}

		if (p_72321_3_ > 0.0D) {
			d7 += p_72321_3_;
		}

		if (p_72321_5_ < 0.0D) {
			d5 += p_72321_5_;
		}

		if (p_72321_5_ > 0.0D) {
			d8 += p_72321_5_;
		}

		return getBoundingBox(d3, d4, d5, d6, d7, d8);
	}

	public AxisAlignedBB expand(double p_72314_1_, double p_72314_3_, double p_72314_5_) {
		double d3 = minX - p_72314_1_;
		double d4 = minY - p_72314_3_;
		double d5 = minZ - p_72314_5_;
		double d6 = maxX + p_72314_1_;
		double d7 = maxY + p_72314_3_;
		double d8 = maxZ + p_72314_5_;
		return getBoundingBox(d3, d4, d5, d6, d7, d8);
	}

	public AxisAlignedBB func_111270_a(AxisAlignedBB p_111270_1_) {
		double d0 = Math.min(minX, p_111270_1_.minX);
		double d1 = Math.min(minY, p_111270_1_.minY);
		double d2 = Math.min(minZ, p_111270_1_.minZ);
		double d3 = Math.max(maxX, p_111270_1_.maxX);
		double d4 = Math.max(maxY, p_111270_1_.maxY);
		double d5 = Math.max(maxZ, p_111270_1_.maxZ);
		return getBoundingBox(d0, d1, d2, d3, d4, d5);
	}

	public AxisAlignedBB getOffsetBoundingBox(double p_72325_1_, double p_72325_3_, double p_72325_5_) {
		return getBoundingBox(minX + p_72325_1_, minY + p_72325_3_, minZ + p_72325_5_, maxX + p_72325_1_,
				maxY + p_72325_3_, maxZ + p_72325_5_);
	}

	public double calculateXOffset(AxisAlignedBB p_72316_1_, double p_72316_2_) {
		if (p_72316_1_.maxY > minY && p_72316_1_.minY < maxY) {
			if (p_72316_1_.maxZ > minZ && p_72316_1_.minZ < maxZ) {
				double d1;

				if (p_72316_2_ > 0.0D && p_72316_1_.maxX <= minX) {
					d1 = minX - p_72316_1_.maxX;

					if (d1 < p_72316_2_) {
						p_72316_2_ = d1;
					}
				}

				if (p_72316_2_ < 0.0D && p_72316_1_.minX >= maxX) {
					d1 = maxX - p_72316_1_.minX;

					if (d1 > p_72316_2_) {
						p_72316_2_ = d1;
					}
				}

				return p_72316_2_;
			} else
				return p_72316_2_;
		} else
			return p_72316_2_;
	}

	public double calculateYOffset(AxisAlignedBB p_72323_1_, double p_72323_2_) {
		if (p_72323_1_.maxX > minX && p_72323_1_.minX < maxX) {
			if (p_72323_1_.maxZ > minZ && p_72323_1_.minZ < maxZ) {
				double d1;

				if (p_72323_2_ > 0.0D && p_72323_1_.maxY <= minY) {
					d1 = minY - p_72323_1_.maxY;

					if (d1 < p_72323_2_) {
						p_72323_2_ = d1;
					}
				}

				if (p_72323_2_ < 0.0D && p_72323_1_.minY >= maxY) {
					d1 = maxY - p_72323_1_.minY;

					if (d1 > p_72323_2_) {
						p_72323_2_ = d1;
					}
				}

				return p_72323_2_;
			} else
				return p_72323_2_;
		} else
			return p_72323_2_;
	}

	public double calculateZOffset(AxisAlignedBB p_72322_1_, double p_72322_2_) {
		if (p_72322_1_.maxX > minX && p_72322_1_.minX < maxX) {
			if (p_72322_1_.maxY > minY && p_72322_1_.minY < maxY) {
				double d1;

				if (p_72322_2_ > 0.0D && p_72322_1_.maxZ <= minZ) {
					d1 = minZ - p_72322_1_.maxZ;

					if (d1 < p_72322_2_) {
						p_72322_2_ = d1;
					}
				}

				if (p_72322_2_ < 0.0D && p_72322_1_.minZ >= maxZ) {
					d1 = maxZ - p_72322_1_.minZ;

					if (d1 > p_72322_2_) {
						p_72322_2_ = d1;
					}
				}

				return p_72322_2_;
			} else
				return p_72322_2_;
		} else
			return p_72322_2_;
	}

	public boolean intersectsWith(AxisAlignedBB p_72326_1_) {
		return p_72326_1_.maxX > minX && p_72326_1_.minX < maxX
				? p_72326_1_.maxY > minY && p_72326_1_.minY < maxY ? p_72326_1_.maxZ > minZ && p_72326_1_.minZ < maxZ
						: false
				: false;
	}

	public AxisAlignedBB offset(double p_72317_1_, double p_72317_3_, double p_72317_5_) {
		minX += p_72317_1_;
		minY += p_72317_3_;
		minZ += p_72317_5_;
		maxX += p_72317_1_;
		maxY += p_72317_3_;
		maxZ += p_72317_5_;
		return this;
	}

	public boolean isVecInside(Vec3 p_72318_1_) {
		return p_72318_1_.xCoord > minX && p_72318_1_.xCoord < maxX
				? p_72318_1_.yCoord > minY && p_72318_1_.yCoord < maxY
						? p_72318_1_.zCoord > minZ && p_72318_1_.zCoord < maxZ
						: false
				: false;
	}

	public double getAverageEdgeLength() {
		double d0 = maxX - minX;
		double d1 = maxY - minY;
		double d2 = maxZ - minZ;
		return (d0 + d1 + d2) / 3.0D;
	}

	public AxisAlignedBB contract(double p_72331_1_, double p_72331_3_, double p_72331_5_) {
		double d3 = minX + p_72331_1_;
		double d4 = minY + p_72331_3_;
		double d5 = minZ + p_72331_5_;
		double d6 = maxX - p_72331_1_;
		double d7 = maxY - p_72331_3_;
		double d8 = maxZ - p_72331_5_;
		return getBoundingBox(d3, d4, d5, d6, d7, d8);
	}

	public AxisAlignedBB copy() {
		return getBoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
	}

	public MovingObjectPosition calculateIntercept(Vec3 p_72327_1_, Vec3 p_72327_2_) {
		Vec3 vec32 = p_72327_1_.getIntermediateWithXValue(p_72327_2_, minX);
		Vec3 vec33 = p_72327_1_.getIntermediateWithXValue(p_72327_2_, maxX);
		Vec3 vec34 = p_72327_1_.getIntermediateWithYValue(p_72327_2_, minY);
		Vec3 vec35 = p_72327_1_.getIntermediateWithYValue(p_72327_2_, maxY);
		Vec3 vec36 = p_72327_1_.getIntermediateWithZValue(p_72327_2_, minZ);
		Vec3 vec37 = p_72327_1_.getIntermediateWithZValue(p_72327_2_, maxZ);

		if (!isVecInYZ(vec32)) {
			vec32 = null;
		}

		if (!isVecInYZ(vec33)) {
			vec33 = null;
		}

		if (!isVecInXZ(vec34)) {
			vec34 = null;
		}

		if (!isVecInXZ(vec35)) {
			vec35 = null;
		}

		if (!isVecInXY(vec36)) {
			vec36 = null;
		}

		if (!isVecInXY(vec37)) {
			vec37 = null;
		}

		Vec3 vec38 = null;

		if (vec32 != null
				&& (vec38 == null || p_72327_1_.squareDistanceTo(vec32) < p_72327_1_.squareDistanceTo(vec38))) {
			vec38 = vec32;
		}

		if (vec33 != null
				&& (vec38 == null || p_72327_1_.squareDistanceTo(vec33) < p_72327_1_.squareDistanceTo(vec38))) {
			vec38 = vec33;
		}

		if (vec34 != null
				&& (vec38 == null || p_72327_1_.squareDistanceTo(vec34) < p_72327_1_.squareDistanceTo(vec38))) {
			vec38 = vec34;
		}

		if (vec35 != null
				&& (vec38 == null || p_72327_1_.squareDistanceTo(vec35) < p_72327_1_.squareDistanceTo(vec38))) {
			vec38 = vec35;
		}

		if (vec36 != null
				&& (vec38 == null || p_72327_1_.squareDistanceTo(vec36) < p_72327_1_.squareDistanceTo(vec38))) {
			vec38 = vec36;
		}

		if (vec37 != null
				&& (vec38 == null || p_72327_1_.squareDistanceTo(vec37) < p_72327_1_.squareDistanceTo(vec38))) {
			vec38 = vec37;
		}

		if (vec38 == null)
			return null;
		else {
			byte b0 = -1;

			if (vec38 == vec32) {
				b0 = 4;
			}

			if (vec38 == vec33) {
				b0 = 5;
			}

			if (vec38 == vec34) {
				b0 = 0;
			}

			if (vec38 == vec35) {
				b0 = 1;
			}

			if (vec38 == vec36) {
				b0 = 2;
			}

			if (vec38 == vec37) {
				b0 = 3;
			}

			return new MovingObjectPosition(0, 0, 0, b0, vec38);
		}
	}

	private boolean isVecInYZ(Vec3 p_72333_1_) {
		return p_72333_1_ == null ? false
				: p_72333_1_.yCoord >= minY && p_72333_1_.yCoord <= maxY && p_72333_1_.zCoord >= minZ
						&& p_72333_1_.zCoord <= maxZ;
	}

	private boolean isVecInXZ(Vec3 p_72315_1_) {
		return p_72315_1_ == null ? false
				: p_72315_1_.xCoord >= minX && p_72315_1_.xCoord <= maxX && p_72315_1_.zCoord >= minZ
						&& p_72315_1_.zCoord <= maxZ;
	}

	private boolean isVecInXY(Vec3 p_72319_1_) {
		return p_72319_1_ == null ? false
				: p_72319_1_.xCoord >= minX && p_72319_1_.xCoord <= maxX && p_72319_1_.yCoord >= minY
						&& p_72319_1_.yCoord <= maxY;
	}

	public void setBB(AxisAlignedBB p_72328_1_) {
		minX = p_72328_1_.minX;
		minY = p_72328_1_.minY;
		minZ = p_72328_1_.minZ;
		maxX = p_72328_1_.maxX;
		maxY = p_72328_1_.maxY;
		maxZ = p_72328_1_.maxZ;
	}

	@Override
	public String toString() {
		return "box[" + minX + ", " + minY + ", " + minZ + " -> " + maxX + ", " + maxY + ", " + maxZ + "]";
	}
}