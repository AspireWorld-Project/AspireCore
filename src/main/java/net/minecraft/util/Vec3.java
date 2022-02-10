package net.minecraft.util;

public class Vec3 {
	public double xCoord;
	public double yCoord;
	public double zCoord;
	private static final String __OBFID = "CL_00000612";

	public static Vec3 createVectorHelper(double p_72443_0_, double p_72443_2_, double p_72443_4_) {
		return new Vec3(p_72443_0_, p_72443_2_, p_72443_4_);
	}

	protected Vec3(double p_i1108_1_, double p_i1108_3_, double p_i1108_5_) {
		if (p_i1108_1_ == -0.0D) {
			p_i1108_1_ = 0.0D;
		}

		if (p_i1108_3_ == -0.0D) {
			p_i1108_3_ = 0.0D;
		}

		if (p_i1108_5_ == -0.0D) {
			p_i1108_5_ = 0.0D;
		}

		xCoord = p_i1108_1_;
		yCoord = p_i1108_3_;
		zCoord = p_i1108_5_;
	}

	protected Vec3 setComponents(double p_72439_1_, double p_72439_3_, double p_72439_5_) {
		xCoord = p_72439_1_;
		yCoord = p_72439_3_;
		zCoord = p_72439_5_;
		return this;
	}

	public Vec3 subtract(Vec3 p_72444_1_) {
		return createVectorHelper(p_72444_1_.xCoord - xCoord, p_72444_1_.yCoord - yCoord, p_72444_1_.zCoord - zCoord);
	}

	public Vec3 normalize() {
		double d0 = MathHelper.sqrt_double(xCoord * xCoord + yCoord * yCoord + zCoord * zCoord);
		return d0 < 1.0E-4D ? createVectorHelper(0.0D, 0.0D, 0.0D)
				: createVectorHelper(xCoord / d0, yCoord / d0, zCoord / d0);
	}

	public double dotProduct(Vec3 p_72430_1_) {
		return xCoord * p_72430_1_.xCoord + yCoord * p_72430_1_.yCoord + zCoord * p_72430_1_.zCoord;
	}

	public Vec3 crossProduct(Vec3 p_72431_1_) {
		return createVectorHelper(yCoord * p_72431_1_.zCoord - zCoord * p_72431_1_.yCoord,
				zCoord * p_72431_1_.xCoord - xCoord * p_72431_1_.zCoord,
				xCoord * p_72431_1_.yCoord - yCoord * p_72431_1_.xCoord);
	}

	public Vec3 addVector(double p_72441_1_, double p_72441_3_, double p_72441_5_) {
		return createVectorHelper(xCoord + p_72441_1_, yCoord + p_72441_3_, zCoord + p_72441_5_);
	}

	public double distanceTo(Vec3 p_72438_1_) {
		double d0 = p_72438_1_.xCoord - xCoord;
		double d1 = p_72438_1_.yCoord - yCoord;
		double d2 = p_72438_1_.zCoord - zCoord;
		return MathHelper.sqrt_double(d0 * d0 + d1 * d1 + d2 * d2);
	}

	public double squareDistanceTo(Vec3 p_72436_1_) {
		double d0 = p_72436_1_.xCoord - xCoord;
		double d1 = p_72436_1_.yCoord - yCoord;
		double d2 = p_72436_1_.zCoord - zCoord;
		return d0 * d0 + d1 * d1 + d2 * d2;
	}

	public double squareDistanceTo(double p_72445_1_, double p_72445_3_, double p_72445_5_) {
		double d3 = p_72445_1_ - xCoord;
		double d4 = p_72445_3_ - yCoord;
		double d5 = p_72445_5_ - zCoord;
		return d3 * d3 + d4 * d4 + d5 * d5;
	}

	public double lengthVector() {
		return MathHelper.sqrt_double(xCoord * xCoord + yCoord * yCoord + zCoord * zCoord);
	}

	public Vec3 getIntermediateWithXValue(Vec3 p_72429_1_, double p_72429_2_) {
		double d1 = p_72429_1_.xCoord - xCoord;
		double d2 = p_72429_1_.yCoord - yCoord;
		double d3 = p_72429_1_.zCoord - zCoord;

		if (d1 * d1 < 1.0000000116860974E-7D)
			return null;
		else {
			double d4 = (p_72429_2_ - xCoord) / d1;
			return d4 >= 0.0D && d4 <= 1.0D ? createVectorHelper(xCoord + d1 * d4, yCoord + d2 * d4, zCoord + d3 * d4)
					: null;
		}
	}

	public Vec3 getIntermediateWithYValue(Vec3 p_72435_1_, double p_72435_2_) {
		double d1 = p_72435_1_.xCoord - xCoord;
		double d2 = p_72435_1_.yCoord - yCoord;
		double d3 = p_72435_1_.zCoord - zCoord;

		if (d2 * d2 < 1.0000000116860974E-7D)
			return null;
		else {
			double d4 = (p_72435_2_ - yCoord) / d2;
			return d4 >= 0.0D && d4 <= 1.0D ? createVectorHelper(xCoord + d1 * d4, yCoord + d2 * d4, zCoord + d3 * d4)
					: null;
		}
	}

	public Vec3 getIntermediateWithZValue(Vec3 p_72434_1_, double p_72434_2_) {
		double d1 = p_72434_1_.xCoord - xCoord;
		double d2 = p_72434_1_.yCoord - yCoord;
		double d3 = p_72434_1_.zCoord - zCoord;

		if (d3 * d3 < 1.0000000116860974E-7D)
			return null;
		else {
			double d4 = (p_72434_2_ - zCoord) / d3;
			return d4 >= 0.0D && d4 <= 1.0D ? createVectorHelper(xCoord + d1 * d4, yCoord + d2 * d4, zCoord + d3 * d4)
					: null;
		}
	}

	@Override
	public String toString() {
		return "(" + xCoord + ", " + yCoord + ", " + zCoord + ")";
	}

	public void rotateAroundX(float p_72440_1_) {
		float f1 = MathHelper.cos(p_72440_1_);
		float f2 = MathHelper.sin(p_72440_1_);
		double d0 = xCoord;
		double d1 = yCoord * f1 + zCoord * f2;
		double d2 = zCoord * f1 - yCoord * f2;
		setComponents(d0, d1, d2);
	}

	public void rotateAroundY(float p_72442_1_) {
		float f1 = MathHelper.cos(p_72442_1_);
		float f2 = MathHelper.sin(p_72442_1_);
		double d0 = xCoord * f1 + zCoord * f2;
		double d1 = yCoord;
		double d2 = zCoord * f1 - xCoord * f2;
		setComponents(d0, d1, d2);
	}

	public void rotateAroundZ(float p_72446_1_) {
		float f1 = MathHelper.cos(p_72446_1_);
		float f2 = MathHelper.sin(p_72446_1_);
		double d0 = xCoord * f1 + yCoord * f2;
		double d1 = yCoord * f1 - xCoord * f2;
		double d2 = zCoord;
		setComponents(d0, d1, d2);
	}
}