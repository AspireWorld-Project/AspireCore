package net.minecraft.world.biome;

import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenDesertWells;

import java.util.Random;

public class BiomeGenDesert extends BiomeGenBase {
	public BiomeGenDesert(int p_i1977_1_) {
		super(p_i1977_1_);
		spawnableCreatureList.clear();
		topBlock = Blocks.sand;
		fillerBlock = Blocks.sand;
		theBiomeDecorator.treesPerChunk = -999;
		theBiomeDecorator.deadBushPerChunk = 2;
		theBiomeDecorator.reedsPerChunk = 50;
		theBiomeDecorator.cactiPerChunk = 10;
		spawnableCreatureList.clear();
	}

	@Override
	public void decorate(World p_76728_1_, Random p_76728_2_, int p_76728_3_, int p_76728_4_) {
		super.decorate(p_76728_1_, p_76728_2_, p_76728_3_, p_76728_4_);

		if (p_76728_2_.nextInt(1000) == 0) {
			int k = p_76728_3_ + p_76728_2_.nextInt(16) + 8;
			int l = p_76728_4_ + p_76728_2_.nextInt(16) + 8;
			WorldGenDesertWells worldgendesertwells = new WorldGenDesertWells();
			worldgendesertwells.generate(p_76728_1_, p_76728_2_, k, p_76728_1_.getHeightValue(k, l) + 1, l);
		}
	}
}