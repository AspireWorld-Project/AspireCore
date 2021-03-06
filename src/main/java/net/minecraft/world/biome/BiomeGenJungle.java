package net.minecraft.world.biome;

import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.*;

import java.util.Random;

public class BiomeGenJungle extends BiomeGenBase {
	private final boolean field_150614_aC;
	@SuppressWarnings("unchecked")
	public BiomeGenJungle(int p_i45379_1_, boolean p_i45379_2_) {
		super(p_i45379_1_);
		field_150614_aC = p_i45379_2_;

		if (p_i45379_2_) {
			theBiomeDecorator.treesPerChunk = 2;
		} else {
			theBiomeDecorator.treesPerChunk = 50;
		}

		theBiomeDecorator.grassPerChunk = 25;
		theBiomeDecorator.flowersPerChunk = 4;

		if (!p_i45379_2_) {
			spawnableMonsterList.add(new BiomeGenBase.SpawnListEntry(EntityOcelot.class, 2, 1, 1));
		}

		spawnableCreatureList.add(new BiomeGenBase.SpawnListEntry(EntityChicken.class, 10, 4, 4));
	}

	@Override
	public WorldGenAbstractTree func_150567_a(Random p_150567_1_) {
		return p_150567_1_.nextInt(10) == 0 ? worldGeneratorBigTree
				: p_150567_1_.nextInt(2) == 0 ? new WorldGenShrub(3, 0)
						: !field_150614_aC && p_150567_1_.nextInt(3) == 0 ? new WorldGenMegaJungle(false, 10, 20, 3, 3)
								: new WorldGenTrees(false, 4 + p_150567_1_.nextInt(7), 3, 3, true);
	}

	@Override
	public WorldGenerator getRandomWorldGenForGrass(Random p_76730_1_) {
		return p_76730_1_.nextInt(4) == 0 ? new WorldGenTallGrass(Blocks.tallgrass, 2)
				: new WorldGenTallGrass(Blocks.tallgrass, 1);
	}

	@Override
	public void decorate(World p_76728_1_, Random p_76728_2_, int p_76728_3_, int p_76728_4_) {
		super.decorate(p_76728_1_, p_76728_2_, p_76728_3_, p_76728_4_);
		int k = p_76728_3_ + p_76728_2_.nextInt(16) + 8;
		int l = p_76728_4_ + p_76728_2_.nextInt(16) + 8;
		int height = p_76728_1_.getHeightValue(k, l) * 2; // This was the original input for the nextInt below. But it
															// could == 0, which crashes nextInt
		if (height < 1) {
			height = 1;
		}
		int i1 = p_76728_2_.nextInt(height);
		new WorldGenMelon().generate(p_76728_1_, p_76728_2_, k, i1, l);
		WorldGenVines worldgenvines = new WorldGenVines();

		for (l = 0; l < 50; ++l) {
			i1 = p_76728_3_ + p_76728_2_.nextInt(16) + 8;
			short short1 = 128;
			int j1 = p_76728_4_ + p_76728_2_.nextInt(16) + 8;
			worldgenvines.generate(p_76728_1_, p_76728_2_, i1, short1, j1);
		}
	}
}