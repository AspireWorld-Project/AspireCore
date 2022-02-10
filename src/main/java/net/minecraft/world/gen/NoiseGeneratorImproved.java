package net.minecraft.world.gen;

import java.util.Random;

public class NoiseGeneratorImproved extends NoiseGenerator {
	private int[] permutations;
	public double xCoord;
	public double yCoord;
	public double zCoord;
	private static final double[] field_152381_e = new double[] { 1.0D, -1.0D, 1.0D, -1.0D, 1.0D, -1.0D, 1.0D, -1.0D,
			0.0D, 0.0D, 0.0D, 0.0D, 1.0D, 0.0D, -1.0D, 0.0D };
	private static final double[] field_152382_f = new double[] { 1.0D, 1.0D, -1.0D, -1.0D, 0.0D, 0.0D, 0.0D, 0.0D,
			1.0D, -1.0D, 1.0D, -1.0D, 1.0D, -1.0D, 1.0D, -1.0D };
	private static final double[] field_152383_g = new double[] { 0.0D, 0.0D, 0.0D, 0.0D, 1.0D, 1.0D, -1.0D, -1.0D,
			1.0D, 1.0D, -1.0D, -1.0D, 0.0D, 1.0D, 0.0D, -1.0D };
	private static final double[] field_152384_h = new double[] { 1.0D, -1.0D, 1.0D, -1.0D, 1.0D, -1.0D, 1.0D, -1.0D,
			0.0D, 0.0D, 0.0D, 0.0D, 1.0D, 0.0D, -1.0D, 0.0D };
	private static final double[] field_152385_i = new double[] { 0.0D, 0.0D, 0.0D, 0.0D, 1.0D, 1.0D, -1.0D, -1.0D,
			1.0D, 1.0D, -1.0D, -1.0D, 0.0D, 1.0D, 0.0D, -1.0D };
	private static final String __OBFID = "CL_00000534";

	public NoiseGeneratorImproved() {
		this(new Random());
	}

	public NoiseGeneratorImproved(Random p_i45469_1_) {
		permutations = new int[512];
		xCoord = p_i45469_1_.nextDouble() * 256.0D;
		yCoord = p_i45469_1_.nextDouble() * 256.0D;
		zCoord = p_i45469_1_.nextDouble() * 256.0D;
		int i;

		for (i = 0; i < 256; permutations[i] = i++) {
			;
		}

		for (i = 0; i < 256; ++i) {
			int j = p_i45469_1_.nextInt(256 - i) + i;
			int k = permutations[i];
			permutations[i] = permutations[j];
			permutations[j] = k;
			permutations[i + 256] = permutations[i];
		}
	}

	public final double lerp(double p_76311_1_, double p_76311_3_, double p_76311_5_) {
		return p_76311_3_ + p_76311_1_ * (p_76311_5_ - p_76311_3_);
	}

	public final double func_76309_a(int p_76309_1_, double p_76309_2_, double p_76309_4_) {
		int j = p_76309_1_ & 15;
		return field_152384_h[j] * p_76309_2_ + field_152385_i[j] * p_76309_4_;
	}

	public final double grad(int p_76310_1_, double p_76310_2_, double p_76310_4_, double p_76310_6_) {
		int j = p_76310_1_ & 15;
		return field_152381_e[j] * p_76310_2_ + field_152382_f[j] * p_76310_4_ + field_152383_g[j] * p_76310_6_;
	}

	public void populateNoiseArray(double[] p_76308_1_, double p_76308_2_, double p_76308_4_, double p_76308_6_,
			int p_76308_8_, int p_76308_9_, int p_76308_10_, double p_76308_11_, double p_76308_13_, double p_76308_15_,
			double p_76308_17_) {
		int l;
		int i1;
		double d9;
		double d11;
		int l1;
		double d12;
		int i2;
		int j2;
		double d13;
		int k5;
		int j6;

		if (p_76308_9_ == 1) {
			double d21 = 0.0D;
			double d22 = 0.0D;
			k5 = 0;
			double d23 = 1.0D / p_76308_17_;

			for (int j1 = 0; j1 < p_76308_8_; ++j1) {
				d9 = p_76308_2_ + j1 * p_76308_11_ + xCoord;
				int i6 = (int) d9;

				if (d9 < i6) {
					--i6;
				}

				int k1 = i6 & 255;
				d9 -= i6;
				d11 = d9 * d9 * d9 * (d9 * (d9 * 6.0D - 15.0D) + 10.0D);

				for (l1 = 0; l1 < p_76308_10_; ++l1) {
					d12 = p_76308_6_ + l1 * p_76308_15_ + zCoord;
					i2 = (int) d12;

					if (d12 < i2) {
						--i2;
					}

					j2 = i2 & 255;
					d12 -= i2;
					d13 = d12 * d12 * d12 * (d12 * (d12 * 6.0D - 15.0D) + 10.0D);
					l = permutations[k1] + 0;
					int i4 = permutations[l] + j2;
					int j4 = permutations[k1 + 1] + 0;
					i1 = permutations[j4] + j2;
					d21 = lerp(d11, func_76309_a(permutations[i4], d9, d12),
							grad(permutations[i1], d9 - 1.0D, 0.0D, d12));
					d22 = lerp(d11, grad(permutations[i4 + 1], d9, 0.0D, d12 - 1.0D),
							grad(permutations[i1 + 1], d9 - 1.0D, 0.0D, d12 - 1.0D));
					double d24 = lerp(d13, d21, d22);
					j6 = k5++;
					p_76308_1_[j6] += d24 * d23;
				}
			}
		} else {
			l = 0;
			double d7 = 1.0D / p_76308_17_;
			i1 = -1;
			double d8 = 0.0D;
			d9 = 0.0D;
			double d10 = 0.0D;
			d11 = 0.0D;

			for (l1 = 0; l1 < p_76308_8_; ++l1) {
				d12 = p_76308_2_ + l1 * p_76308_11_ + xCoord;
				i2 = (int) d12;

				if (d12 < i2) {
					--i2;
				}

				j2 = i2 & 255;
				d12 -= i2;
				d13 = d12 * d12 * d12 * (d12 * (d12 * 6.0D - 15.0D) + 10.0D);

				for (int k2 = 0; k2 < p_76308_10_; ++k2) {
					double d14 = p_76308_6_ + k2 * p_76308_15_ + zCoord;
					int l2 = (int) d14;

					if (d14 < l2) {
						--l2;
					}

					int i3 = l2 & 255;
					d14 -= l2;
					double d15 = d14 * d14 * d14 * (d14 * (d14 * 6.0D - 15.0D) + 10.0D);

					for (int j3 = 0; j3 < p_76308_9_; ++j3) {
						double d16 = p_76308_4_ + j3 * p_76308_13_ + yCoord;
						int k3 = (int) d16;

						if (d16 < k3) {
							--k3;
						}

						int l3 = k3 & 255;
						d16 -= k3;
						double d17 = d16 * d16 * d16 * (d16 * (d16 * 6.0D - 15.0D) + 10.0D);

						if (j3 == 0 || l3 != i1) {
							i1 = l3;
							int k4 = permutations[j2] + l3;
							int l4 = permutations[k4] + i3;
							int i5 = permutations[k4 + 1] + i3;
							int j5 = permutations[j2 + 1] + l3;
							k5 = permutations[j5] + i3;
							int l5 = permutations[j5 + 1] + i3;
							d8 = lerp(d13, grad(permutations[l4], d12, d16, d14),
									grad(permutations[k5], d12 - 1.0D, d16, d14));
							d9 = lerp(d13, grad(permutations[i5], d12, d16 - 1.0D, d14),
									grad(permutations[l5], d12 - 1.0D, d16 - 1.0D, d14));
							d10 = lerp(d13, grad(permutations[l4 + 1], d12, d16, d14 - 1.0D),
									grad(permutations[k5 + 1], d12 - 1.0D, d16, d14 - 1.0D));
							d11 = lerp(d13, grad(permutations[i5 + 1], d12, d16 - 1.0D, d14 - 1.0D),
									grad(permutations[l5 + 1], d12 - 1.0D, d16 - 1.0D, d14 - 1.0D));
						}

						double d18 = lerp(d17, d8, d9);
						double d19 = lerp(d17, d10, d11);
						double d20 = lerp(d15, d18, d19);
						j6 = l++;
						p_76308_1_[j6] += d20 * d7;
					}
				}
			}
		}
	}
}