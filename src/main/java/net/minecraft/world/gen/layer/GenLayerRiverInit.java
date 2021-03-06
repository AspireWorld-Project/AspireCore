package net.minecraft.world.gen.layer;

public class GenLayerRiverInit extends GenLayer {
	public GenLayerRiverInit(long p_i2127_1_, GenLayer p_i2127_3_) {
		super(p_i2127_1_);
		parent = p_i2127_3_;
	}

	@Override
	public int[] getInts(int p_75904_1_, int p_75904_2_, int p_75904_3_, int p_75904_4_) {
		int[] aint = parent.getInts(p_75904_1_, p_75904_2_, p_75904_3_, p_75904_4_);
		int[] aint1 = IntCache.getIntCache(p_75904_3_ * p_75904_4_);

		for (int i1 = 0; i1 < p_75904_4_; ++i1) {
			for (int j1 = 0; j1 < p_75904_3_; ++j1) {
				initChunkSeed(j1 + p_75904_1_, i1 + p_75904_2_);
				aint1[j1 + i1 * p_75904_3_] = aint[j1 + i1 * p_75904_3_] > 0 ? nextInt(299999) + 2 : 0;
			}
		}

		return aint1;
	}
}