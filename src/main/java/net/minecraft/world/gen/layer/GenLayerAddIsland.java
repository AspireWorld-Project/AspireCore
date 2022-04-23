package net.minecraft.world.gen.layer;

public class GenLayerAddIsland extends GenLayer {
	public GenLayerAddIsland(long p_i2119_1_, GenLayer p_i2119_3_) {
		super(p_i2119_1_);
		parent = p_i2119_3_;
	}

	@Override
	public int[] getInts(int p_75904_1_, int p_75904_2_, int p_75904_3_, int p_75904_4_) {
		int i1 = p_75904_1_ - 1;
		int j1 = p_75904_2_ - 1;
		int k1 = p_75904_3_ + 2;
		int l1 = p_75904_4_ + 2;
		int[] aint = parent.getInts(i1, j1, k1, l1);
		int[] aint1 = IntCache.getIntCache(p_75904_3_ * p_75904_4_);

		for (int i2 = 0; i2 < p_75904_4_; ++i2) {
			for (int j2 = 0; j2 < p_75904_3_; ++j2) {
				int k2 = aint[j2 + 0 + (i2 + 0) * k1];
				int l2 = aint[j2 + 2 + (i2 + 0) * k1];
				int i3 = aint[j2 + 0 + (i2 + 2) * k1];
				int j3 = aint[j2 + 2 + (i2 + 2) * k1];
				int k3 = aint[j2 + 1 + (i2 + 1) * k1];
				initChunkSeed(j2 + p_75904_1_, i2 + p_75904_2_);

				if (k3 == 0 && (k2 != 0 || l2 != 0 || i3 != 0 || j3 != 0)) {
					int l3 = 1;
					int i4 = 1;

					if (k2 != 0 && nextInt(l3++) == 0) {
						i4 = k2;
					}

					if (l2 != 0 && nextInt(l3++) == 0) {
						i4 = l2;
					}

					if (i3 != 0 && nextInt(l3++) == 0) {
						i4 = i3;
					}

					if (j3 != 0 && nextInt(l3++) == 0) {
						i4 = j3;
					}

					if (nextInt(3) == 0) {
						aint1[j2 + i2 * p_75904_3_] = i4;
					} else if (i4 == 4) {
						aint1[j2 + i2 * p_75904_3_] = 4;
					} else {
						aint1[j2 + i2 * p_75904_3_] = 0;
					}
				} else if (k3 > 0 && (k2 == 0 || l2 == 0 || i3 == 0 || j3 == 0)) {
					if (nextInt(5) == 0) {
						if (k3 == 4) {
							aint1[j2 + i2 * p_75904_3_] = 4;
						} else {
							aint1[j2 + i2 * p_75904_3_] = 0;
						}
					} else {
						aint1[j2 + i2 * p_75904_3_] = k3;
					}
				} else {
					aint1[j2 + i2 * p_75904_3_] = k3;
				}
			}
		}

		return aint1;
	}
}