package net.minecraft.world.biome;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenTaiga2;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.Random;

public class BiomeGenHills extends BiomeGenBase {
	private final WorldGenerator theWorldGenerator;
	private final WorldGenTaiga2 field_150634_aD;
	private final int field_150635_aE;
	private final int field_150636_aF;
	private final int field_150637_aG;
	private int field_150638_aH;
	public BiomeGenHills(int p_i45373_1_, boolean p_i45373_2_) {
		super(p_i45373_1_);
		theWorldGenerator = new WorldGenMinable(Blocks.monster_egg, 8);
		field_150634_aD = new WorldGenTaiga2(false);
		field_150635_aE = 0;
		field_150636_aF = 1;
		field_150637_aG = 2;
		field_150638_aH = field_150635_aE;

		if (p_i45373_2_) {
			theBiomeDecorator.treesPerChunk = 3;
			field_150638_aH = field_150636_aF;
		}
	}

	@Override
	public WorldGenAbstractTree func_150567_a(Random p_150567_1_) {
		return p_150567_1_.nextInt(3) > 0 ? field_150634_aD : super.func_150567_a(p_150567_1_);
	}

	@Override
	public void decorate(World p_76728_1_, Random p_76728_2_, int p_76728_3_, int p_76728_4_) {
		super.decorate(p_76728_1_, p_76728_2_, p_76728_3_, p_76728_4_);
		int k = 3 + p_76728_2_.nextInt(6);
		int l;
		int i1;
		int j1;

		for (l = 0; l < k; ++l) {
			i1 = p_76728_3_ + p_76728_2_.nextInt(16);
			j1 = p_76728_2_.nextInt(28) + 4;
			int k1 = p_76728_4_ + p_76728_2_.nextInt(16);

			if (p_76728_1_.getBlock(i1, j1, k1).isReplaceableOreGen(p_76728_1_, i1, j1, k1, Blocks.stone)) {
				p_76728_1_.setBlock(i1, j1, k1, Blocks.emerald_ore, 0, 2);
			}
		}

		for (k = 0; k < 7; ++k) {
			l = p_76728_3_ + p_76728_2_.nextInt(16);
			i1 = p_76728_2_.nextInt(64);
			j1 = p_76728_4_ + p_76728_2_.nextInt(16);
			theWorldGenerator.generate(p_76728_1_, p_76728_2_, l, i1, j1);
		}
	}

	@Override
	public void genTerrainBlocks(World p_150573_1_, Random p_150573_2_, Block[] p_150573_3_, byte[] p_150573_4_,
			int p_150573_5_, int p_150573_6_, double p_150573_7_) {
		topBlock = Blocks.grass;
		field_150604_aj = 0;
		fillerBlock = Blocks.dirt;

		if ((p_150573_7_ < -1.0D || p_150573_7_ > 2.0D) && field_150638_aH == field_150637_aG) {
			topBlock = Blocks.gravel;
			fillerBlock = Blocks.gravel;
		} else if (p_150573_7_ > 1.0D && field_150638_aH != field_150636_aF) {
			topBlock = Blocks.stone;
			fillerBlock = Blocks.stone;
		}

		genBiomeTerrain(p_150573_1_, p_150573_2_, p_150573_3_, p_150573_4_, p_150573_5_, p_150573_6_, p_150573_7_);
	}

	public BiomeGenHills mutateHills(BiomeGenBase p_150633_1_) {
		field_150638_aH = field_150637_aG;
		func_150557_a(p_150633_1_.color, true);
		setBiomeName(p_150633_1_.biomeName + " M");
		setHeight(new BiomeGenBase.Height(p_150633_1_.rootHeight, p_150633_1_.heightVariation));
		setTemperatureRainfall(p_150633_1_.temperature, p_150633_1_.rainfall);
		return this;
	}

	@Override
	public BiomeGenBase createMutation() {
		return new BiomeGenHills(biomeID + 128, false).mutateHills(this);
	}
}