package net.minecraft.world.biome;

import net.minecraft.block.Block;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.*;

import java.util.Random;

public class BiomeGenTaiga extends BiomeGenBase {
	private static final WorldGenTaiga1 field_150639_aC = new WorldGenTaiga1();
	private static final WorldGenTaiga2 field_150640_aD = new WorldGenTaiga2(false);
	private static final WorldGenMegaPineTree field_150641_aE = new WorldGenMegaPineTree(false, false);
	private static final WorldGenMegaPineTree field_150642_aF = new WorldGenMegaPineTree(false, true);
	private static final WorldGenBlockBlob field_150643_aG = new WorldGenBlockBlob(Blocks.mossy_cobblestone, 0);
	private final int field_150644_aH;
	@SuppressWarnings("unchecked")
	public BiomeGenTaiga(int p_i45385_1_, int p_i45385_2_) {
		super(p_i45385_1_);
		field_150644_aH = p_i45385_2_;
		spawnableCreatureList.add(new BiomeGenBase.SpawnListEntry(EntityWolf.class, 8, 4, 4));
		theBiomeDecorator.treesPerChunk = 10;

		if (p_i45385_2_ != 1 && p_i45385_2_ != 2) {
			theBiomeDecorator.grassPerChunk = 1;
			theBiomeDecorator.mushroomsPerChunk = 1;
		} else {
			theBiomeDecorator.grassPerChunk = 7;
			theBiomeDecorator.deadBushPerChunk = 1;
			theBiomeDecorator.mushroomsPerChunk = 3;
		}
	}

	@Override
	public WorldGenAbstractTree func_150567_a(Random p_150567_1_) {
		return (field_150644_aH == 1 || field_150644_aH == 2) && p_150567_1_.nextInt(3) == 0
				? field_150644_aH != 2 && p_150567_1_.nextInt(13) != 0 ? field_150641_aE : field_150642_aF
				: p_150567_1_.nextInt(3) == 0 ? field_150639_aC : field_150640_aD;
	}

	@Override
	public WorldGenerator getRandomWorldGenForGrass(Random p_76730_1_) {
		return p_76730_1_.nextInt(5) > 0 ? new WorldGenTallGrass(Blocks.tallgrass, 2)
				: new WorldGenTallGrass(Blocks.tallgrass, 1);
	}

	@Override
	public void decorate(World p_76728_1_, Random p_76728_2_, int p_76728_3_, int p_76728_4_) {
		int k;
		int l;
		int i1;
		int j1;

		if (field_150644_aH == 1 || field_150644_aH == 2) {
			k = p_76728_2_.nextInt(3);

			for (l = 0; l < k; ++l) {
				i1 = p_76728_3_ + p_76728_2_.nextInt(16) + 8;
				j1 = p_76728_4_ + p_76728_2_.nextInt(16) + 8;
				int k1 = p_76728_1_.getHeightValue(i1, j1);
				field_150643_aG.generate(p_76728_1_, p_76728_2_, i1, k1, j1);
			}
		}

		genTallFlowers.func_150548_a(3);

		for (k = 0; k < 7; ++k) {
			l = p_76728_3_ + p_76728_2_.nextInt(16) + 8;
			i1 = p_76728_4_ + p_76728_2_.nextInt(16) + 8;
			j1 = p_76728_2_.nextInt(p_76728_1_.getHeightValue(l, i1) + 32);
			genTallFlowers.generate(p_76728_1_, p_76728_2_, l, j1, i1);
		}

		super.decorate(p_76728_1_, p_76728_2_, p_76728_3_, p_76728_4_);
	}

	@Override
	public void genTerrainBlocks(World p_150573_1_, Random p_150573_2_, Block[] p_150573_3_, byte[] p_150573_4_,
			int p_150573_5_, int p_150573_6_, double p_150573_7_) {
		if (field_150644_aH == 1 || field_150644_aH == 2) {
			topBlock = Blocks.grass;
			field_150604_aj = 0;
			fillerBlock = Blocks.dirt;

			if (p_150573_7_ > 1.75D) {
				topBlock = Blocks.dirt;
				field_150604_aj = 1;
			} else if (p_150573_7_ > -0.95D) {
				topBlock = Blocks.dirt;
				field_150604_aj = 2;
			}
		}

		genBiomeTerrain(p_150573_1_, p_150573_2_, p_150573_3_, p_150573_4_, p_150573_5_, p_150573_6_, p_150573_7_);
	}

	@Override
	public BiomeGenBase createMutation() {
		return biomeID == BiomeGenBase.megaTaiga.biomeID ? new BiomeGenTaiga(biomeID + 128, 2)
				.func_150557_a(5858897, true).setBiomeName("Mega Spruce Taiga").func_76733_a(5159473)
				.setTemperatureRainfall(0.25F, 0.8F).setHeight(new BiomeGenBase.Height(rootHeight, heightVariation))
				: super.createMutation();
	}
}