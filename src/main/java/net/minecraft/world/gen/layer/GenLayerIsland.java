package net.minecraft.world.gen.layer;

public class GenLayerIsland extends GenLayer {
	public GenLayerIsland(long p_i2124_1_) {
		super(p_i2124_1_);
	}

	@Override
	public int[] getInts(int p_75904_1_, int p_75904_2_, int p_75904_3_, int p_75904_4_) {
		int[] aint = IntCache.getIntCache(p_75904_3_ * p_75904_4_);

		for (int i1 = 0; i1 < p_75904_4_; ++i1) {
			for (int j1 = 0; j1 < p_75904_3_; ++j1) {
				initChunkSeed(p_75904_1_ + j1, p_75904_2_ + i1);
				aint[j1 + i1 * p_75904_3_] = nextInt(10) == 0 ? 1 : 0;
			}
		}

		if (p_75904_1_ > -p_75904_3_ && p_75904_1_ <= 0 && p_75904_2_ > -p_75904_4_ && p_75904_2_ <= 0) {
			aint[-p_75904_1_ + -p_75904_2_ * p_75904_3_] = 1;
		}

		return aint;
	}
}