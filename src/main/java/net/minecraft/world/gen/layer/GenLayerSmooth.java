package net.minecraft.world.gen.layer;

public class GenLayerSmooth extends GenLayer {
	public GenLayerSmooth(long p_i2131_1_, GenLayer p_i2131_3_) {
		super(p_i2131_1_);
		super.parent = p_i2131_3_;
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
				int k2 = aint[j2 + 0 + (i2 + 1) * k1];
				int l2 = aint[j2 + 2 + (i2 + 1) * k1];
				int i3 = aint[j2 + 1 + (i2 + 0) * k1];
				int j3 = aint[j2 + 1 + (i2 + 2) * k1];
				int k3 = aint[j2 + 1 + (i2 + 1) * k1];

				if (k2 == l2 && i3 == j3) {
					initChunkSeed(j2 + p_75904_1_, i2 + p_75904_2_);

					if (nextInt(2) == 0) {
						k3 = k2;
					} else {
						k3 = i3;
					}
				} else {
					if (k2 == l2) {
						k3 = k2;
					}

					if (i3 == j3) {
						k3 = i3;
					}
				}

				aint1[j2 + i2 * p_75904_3_] = k3;
			}
		}

		return aint1;
	}
}