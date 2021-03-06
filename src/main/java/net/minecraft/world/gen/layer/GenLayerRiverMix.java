package net.minecraft.world.gen.layer;

import net.minecraft.world.biome.BiomeGenBase;

public class GenLayerRiverMix extends GenLayer {
	private final GenLayer biomePatternGeneratorChain;
	private final GenLayer riverPatternGeneratorChain;
	public GenLayerRiverMix(long p_i2129_1_, GenLayer p_i2129_3_, GenLayer p_i2129_4_) {
		super(p_i2129_1_);
		biomePatternGeneratorChain = p_i2129_3_;
		riverPatternGeneratorChain = p_i2129_4_;
	}

	@Override
	public void initWorldGenSeed(long p_75905_1_) {
		biomePatternGeneratorChain.initWorldGenSeed(p_75905_1_);
		riverPatternGeneratorChain.initWorldGenSeed(p_75905_1_);
		super.initWorldGenSeed(p_75905_1_);
	}

	@Override
	public int[] getInts(int p_75904_1_, int p_75904_2_, int p_75904_3_, int p_75904_4_) {
		int[] aint = biomePatternGeneratorChain.getInts(p_75904_1_, p_75904_2_, p_75904_3_, p_75904_4_);
		int[] aint1 = riverPatternGeneratorChain.getInts(p_75904_1_, p_75904_2_, p_75904_3_, p_75904_4_);
		int[] aint2 = IntCache.getIntCache(p_75904_3_ * p_75904_4_);

		for (int i1 = 0; i1 < p_75904_3_ * p_75904_4_; ++i1) {
			if (aint[i1] != BiomeGenBase.ocean.biomeID && aint[i1] != BiomeGenBase.deepOcean.biomeID) {
				if (aint1[i1] == BiomeGenBase.river.biomeID) {
					if (aint[i1] == BiomeGenBase.icePlains.biomeID) {
						aint2[i1] = BiomeGenBase.frozenRiver.biomeID;
					} else if (aint[i1] != BiomeGenBase.mushroomIsland.biomeID
							&& aint[i1] != BiomeGenBase.mushroomIslandShore.biomeID) {
						aint2[i1] = aint1[i1] & 255;
					} else {
						aint2[i1] = BiomeGenBase.mushroomIslandShore.biomeID;
					}
				} else {
					aint2[i1] = aint[i1];
				}
			} else {
				aint2[i1] = aint[i1];
			}
		}

		return aint2;
	}
}