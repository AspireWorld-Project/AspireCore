package net.minecraft.world.biome;

import net.minecraft.block.Block;
import net.minecraft.world.World;

import java.util.Random;

public class BiomeGenOcean extends BiomeGenBase {
	private static final String __OBFID = "CL_00000179";

	public BiomeGenOcean(int p_i1985_1_) {
		super(p_i1985_1_);
		spawnableCreatureList.clear();
	}

	@Override
	public BiomeGenBase.TempCategory getTempCategory() {
		return BiomeGenBase.TempCategory.OCEAN;
	}

	@Override
	public void genTerrainBlocks(World p_150573_1_, Random p_150573_2_, Block[] p_150573_3_, byte[] p_150573_4_,
			int p_150573_5_, int p_150573_6_, double p_150573_7_) {
		super.genTerrainBlocks(p_150573_1_, p_150573_2_, p_150573_3_, p_150573_4_, p_150573_5_, p_150573_6_,
				p_150573_7_);
	}
}