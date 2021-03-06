package net.minecraft.world.gen.layer;

import net.minecraft.world.biome.BiomeGenBase;

public class GenLayerDeepOcean extends GenLayer {
	public GenLayerDeepOcean(long p_i45472_1_, GenLayer p_i45472_3_) {
		super(p_i45472_1_);
		parent = p_i45472_3_;
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
				int k2 = aint[j2 + 1 + (i2 + 1 - 1) * (p_75904_3_ + 2)];
				int l2 = aint[j2 + 1 + 1 + (i2 + 1) * (p_75904_3_ + 2)];
				int i3 = aint[j2 + 1 - 1 + (i2 + 1) * (p_75904_3_ + 2)];
				int j3 = aint[j2 + 1 + (i2 + 1 + 1) * (p_75904_3_ + 2)];
				int k3 = aint[j2 + 1 + (i2 + 1) * k1];
				int l3 = 0;

				if (k2 == 0) {
					++l3;
				}

				if (l2 == 0) {
					++l3;
				}

				if (i3 == 0) {
					++l3;
				}

				if (j3 == 0) {
					++l3;
				}

				if (k3 == 0 && l3 > 3) {
					aint1[j2 + i2 * p_75904_3_] = BiomeGenBase.deepOcean.biomeID;
				} else {
					aint1[j2 + i2 * p_75904_3_] = k3;
				}
			}
		}

		return aint1;
	}
}