package net.minecraft.world.biome;

import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenIcePath;
import net.minecraft.world.gen.feature.WorldGenIceSpike;
import net.minecraft.world.gen.feature.WorldGenTaiga2;

import java.util.Random;

public class BiomeGenSnow extends BiomeGenBase {
	private boolean field_150615_aC;
	private WorldGenIceSpike field_150616_aD = new WorldGenIceSpike();
	private WorldGenIcePath field_150617_aE = new WorldGenIcePath(4);
	private static final String __OBFID = "CL_00000174";

	public BiomeGenSnow(int p_i45378_1_, boolean p_i45378_2_) {
		super(p_i45378_1_);
		field_150615_aC = p_i45378_2_;

		if (p_i45378_2_) {
			topBlock = Blocks.snow;
		}

		spawnableCreatureList.clear();
	}

	@Override
	public void decorate(World p_76728_1_, Random p_76728_2_, int p_76728_3_, int p_76728_4_) {
		if (field_150615_aC) {
			int k;
			int l;
			int i1;

			for (k = 0; k < 3; ++k) {
				l = p_76728_3_ + p_76728_2_.nextInt(16) + 8;
				i1 = p_76728_4_ + p_76728_2_.nextInt(16) + 8;
				field_150616_aD.generate(p_76728_1_, p_76728_2_, l, p_76728_1_.getHeightValue(l, i1), i1);
			}

			for (k = 0; k < 2; ++k) {
				l = p_76728_3_ + p_76728_2_.nextInt(16) + 8;
				i1 = p_76728_4_ + p_76728_2_.nextInt(16) + 8;
				field_150617_aE.generate(p_76728_1_, p_76728_2_, l, p_76728_1_.getHeightValue(l, i1), i1);
			}
		}

		super.decorate(p_76728_1_, p_76728_2_, p_76728_3_, p_76728_4_);
	}

	@Override
	public WorldGenAbstractTree func_150567_a(Random p_150567_1_) {
		return new WorldGenTaiga2(false);
	}

	@Override
	public BiomeGenBase createMutation() {
		BiomeGenBase biomegenbase = new BiomeGenSnow(biomeID + 128, true).func_150557_a(13828095, true)
				.setBiomeName(biomeName + " Spikes").setEnableSnow().setTemperatureRainfall(0.0F, 0.5F)
				.setHeight(new BiomeGenBase.Height(rootHeight + 0.1F, heightVariation + 0.1F));
		biomegenbase.rootHeight = rootHeight + 0.3F;
		biomegenbase.heightVariation = heightVariation + 0.4F;
		return biomegenbase;
	}
}