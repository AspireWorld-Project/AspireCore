package net.minecraft.world.gen.layer;

public class GenLayerVoronoiZoom extends GenLayer {
	public GenLayerVoronoiZoom(long p_i2133_1_, GenLayer p_i2133_3_) {
		super(p_i2133_1_);
		super.parent = p_i2133_3_;
	}

	@Override
	public int[] getInts(int p_75904_1_, int p_75904_2_, int p_75904_3_, int p_75904_4_) {
		p_75904_1_ -= 2;
		p_75904_2_ -= 2;
		int i1 = p_75904_1_ >> 2;
		int j1 = p_75904_2_ >> 2;
		int k1 = (p_75904_3_ >> 2) + 2;
		int l1 = (p_75904_4_ >> 2) + 2;
		int[] aint = parent.getInts(i1, j1, k1, l1);
		int i2 = k1 - 1 << 2;
		int j2 = l1 - 1 << 2;
		int[] aint1 = IntCache.getIntCache(i2 * j2);
		int l2;

		for (int k2 = 0; k2 < l1 - 1; ++k2) {
			l2 = 0;
			int i3 = aint[l2 + 0 + (k2 + 0) * k1];

			for (int j3 = aint[l2 + 0 + (k2 + 1) * k1]; l2 < k1 - 1; ++l2) {
				initChunkSeed(l2 + i1 << 2, k2 + j1 << 2);
				double d1 = (nextInt(1024) / 1024.0D - 0.5D) * 3.6D;
				double d2 = (nextInt(1024) / 1024.0D - 0.5D) * 3.6D;
				initChunkSeed(l2 + i1 + 1 << 2, k2 + j1 << 2);
				double d3 = (nextInt(1024) / 1024.0D - 0.5D) * 3.6D + 4.0D;
				double d4 = (nextInt(1024) / 1024.0D - 0.5D) * 3.6D;
				initChunkSeed(l2 + i1 << 2, k2 + j1 + 1 << 2);
				double d5 = (nextInt(1024) / 1024.0D - 0.5D) * 3.6D;
				double d6 = (nextInt(1024) / 1024.0D - 0.5D) * 3.6D + 4.0D;
				initChunkSeed(l2 + i1 + 1 << 2, k2 + j1 + 1 << 2);
				double d7 = (nextInt(1024) / 1024.0D - 0.5D) * 3.6D + 4.0D;
				double d8 = (nextInt(1024) / 1024.0D - 0.5D) * 3.6D + 4.0D;
				int k3 = aint[l2 + 1 + (k2 + 0) * k1] & 255;
				int l3 = aint[l2 + 1 + (k2 + 1) * k1] & 255;

				for (int i4 = 0; i4 < 4; ++i4) {
					int j4 = ((k2 << 2) + i4) * i2 + (l2 << 2);

					for (int k4 = 0; k4 < 4; ++k4) {
						double d9 = (i4 - d2) * (i4 - d2) + (k4 - d1) * (k4 - d1);
						double d10 = (i4 - d4) * (i4 - d4) + (k4 - d3) * (k4 - d3);
						double d11 = (i4 - d6) * (i4 - d6) + (k4 - d5) * (k4 - d5);
						double d12 = (i4 - d8) * (i4 - d8) + (k4 - d7) * (k4 - d7);

						if (d9 < d10 && d9 < d11 && d9 < d12) {
							aint1[j4++] = i3;
						} else if (d10 < d9 && d10 < d11 && d10 < d12) {
							aint1[j4++] = k3;
						} else if (d11 < d9 && d11 < d10 && d11 < d12) {
							aint1[j4++] = j3;
						} else {
							aint1[j4++] = l3;
						}
					}
				}

				i3 = k3;
				j3 = l3;
			}
		}

		int[] aint2 = IntCache.getIntCache(p_75904_3_ * p_75904_4_);

		for (l2 = 0; l2 < p_75904_4_; ++l2) {
			System.arraycopy(aint1, (l2 + (p_75904_2_ & 3)) * i2 + (p_75904_1_ & 3), aint2, l2 * p_75904_3_,
					p_75904_3_);
		}

		return aint2;
	}
}