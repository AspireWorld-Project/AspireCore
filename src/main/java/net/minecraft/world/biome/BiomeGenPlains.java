package net.minecraft.world.biome;

import net.minecraft.block.BlockFlower;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

import java.util.Random;

public class BiomeGenPlains extends BiomeGenBase {
	protected boolean field_150628_aC;
	@SuppressWarnings("unchecked")
	public BiomeGenPlains(int p_i1986_1_) {
		super(p_i1986_1_);
		setTemperatureRainfall(0.8F, 0.4F);
		setHeight(height_LowPlains);
		spawnableCreatureList.add(new BiomeGenBase.SpawnListEntry(EntityHorse.class, 5, 2, 6));
		theBiomeDecorator.treesPerChunk = -999;
		theBiomeDecorator.flowersPerChunk = 4;
		theBiomeDecorator.grassPerChunk = 10;
		flowers.clear();
		addFlower(Blocks.red_flower, 4, 3);
		addFlower(Blocks.red_flower, 5, 3);
		addFlower(Blocks.red_flower, 6, 3);
		addFlower(Blocks.red_flower, 7, 3);
		addFlower(Blocks.red_flower, 0, 20);
		addFlower(Blocks.red_flower, 3, 20);
		addFlower(Blocks.red_flower, 8, 20);
		addFlower(Blocks.yellow_flower, 0, 30);
	}

	@Override
	public String func_150572_a(Random p_150572_1_, int p_150572_2_, int p_150572_3_, int p_150572_4_) {
		double d0 = plantNoise.func_151601_a(p_150572_2_ / 200.0D, p_150572_4_ / 200.0D);
		int l;

		if (d0 < -0.8D) {
			l = p_150572_1_.nextInt(4);
			return BlockFlower.field_149859_a[4 + l];
		} else if (p_150572_1_.nextInt(3) > 0) {
			l = p_150572_1_.nextInt(3);
			return l == 0 ? BlockFlower.field_149859_a[0]
					: l == 1 ? BlockFlower.field_149859_a[3] : BlockFlower.field_149859_a[8];
		} else
			return BlockFlower.field_149858_b[0];
	}

	@Override
	public void decorate(World p_76728_1_, Random p_76728_2_, int p_76728_3_, int p_76728_4_) {
		double d0 = plantNoise.func_151601_a((p_76728_3_ + 8) / 200.0D, (p_76728_4_ + 8) / 200.0D);
		int k;
		int l;
		int i1;
		int j1;

		if (d0 < -0.8D) {
			theBiomeDecorator.flowersPerChunk = 15;
			theBiomeDecorator.grassPerChunk = 5;
		} else {
			theBiomeDecorator.flowersPerChunk = 4;
			theBiomeDecorator.grassPerChunk = 10;
			genTallFlowers.func_150548_a(2);

			for (k = 0; k < 7; ++k) {
				l = p_76728_3_ + p_76728_2_.nextInt(16) + 8;
				i1 = p_76728_4_ + p_76728_2_.nextInt(16) + 8;
				j1 = p_76728_2_.nextInt(p_76728_1_.getHeightValue(l, i1) + 32);
				genTallFlowers.generate(p_76728_1_, p_76728_2_, l, j1, i1);
			}
		}

		if (field_150628_aC) {
			genTallFlowers.func_150548_a(0);

			for (k = 0; k < 10; ++k) {
				l = p_76728_3_ + p_76728_2_.nextInt(16) + 8;
				i1 = p_76728_4_ + p_76728_2_.nextInt(16) + 8;
				j1 = p_76728_2_.nextInt(p_76728_1_.getHeightValue(l, i1) + 32);
				genTallFlowers.generate(p_76728_1_, p_76728_2_, l, j1, i1);
			}
		}

		super.decorate(p_76728_1_, p_76728_2_, p_76728_3_, p_76728_4_);
	}

	@Override
	public BiomeGenBase createMutation() {
		BiomeGenPlains biomegenplains = new BiomeGenPlains(biomeID + 128);
		biomegenplains.setBiomeName("Sunflower Plains");
		biomegenplains.field_150628_aC = true;
		biomegenplains.setColor(9286496);
		biomegenplains.field_150609_ah = 14273354;
		return biomegenplains;
	}
}