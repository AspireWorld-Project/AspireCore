package net.minecraft.world.biome;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockFlower;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenBigMushroom;
import net.minecraft.world.gen.feature.WorldGenCanopyTree;
import net.minecraft.world.gen.feature.WorldGenForest;

import java.util.Random;

public class BiomeGenForest extends BiomeGenBase {
	private final int field_150632_aF;
	protected static final WorldGenForest field_150629_aC = new WorldGenForest(false, true);
	protected static final WorldGenForest field_150630_aD = new WorldGenForest(false, false);
	protected static final WorldGenCanopyTree field_150631_aE = new WorldGenCanopyTree(false);
	@SuppressWarnings("unchecked")
	public BiomeGenForest(int p_i45377_1_, int p_i45377_2_) {
		super(p_i45377_1_);
		field_150632_aF = p_i45377_2_;
		theBiomeDecorator.treesPerChunk = 10;
		theBiomeDecorator.grassPerChunk = 2;

		if (field_150632_aF == 1) {
			theBiomeDecorator.treesPerChunk = 6;
			theBiomeDecorator.flowersPerChunk = 100;
			theBiomeDecorator.grassPerChunk = 1;
		}

		func_76733_a(5159473);
		setTemperatureRainfall(0.7F, 0.8F);

		if (field_150632_aF == 2) {
			field_150609_ah = 353825;
			color = 3175492;
			setTemperatureRainfall(0.6F, 0.6F);
		}

		if (field_150632_aF == 0) {
			spawnableCreatureList.add(new BiomeGenBase.SpawnListEntry(EntityWolf.class, 5, 4, 4));
		}

		if (field_150632_aF == 3) {
			theBiomeDecorator.treesPerChunk = -999;
		}

		if (field_150632_aF == 1) {
			flowers.clear();
			for (int x = 0; x < BlockFlower.field_149859_a.length; x++) {
				addFlower(Blocks.red_flower, x == 1 ? 0 : x, 10);
			}
		}
	}

	@Override
	public BiomeGenBase func_150557_a(int p_150557_1_, boolean p_150557_2_) {
		if (field_150632_aF == 2) {
			field_150609_ah = 353825;
			color = p_150557_1_;

			if (p_150557_2_) {
				field_150609_ah = (field_150609_ah & 16711422) >> 1;
			}

			return this;
		} else
			return super.func_150557_a(p_150557_1_, p_150557_2_);
	}

	@Override
	public WorldGenAbstractTree func_150567_a(Random p_150567_1_) {
		return field_150632_aF == 3 && p_150567_1_.nextInt(3) > 0 ? field_150631_aE
				: field_150632_aF != 2 && p_150567_1_.nextInt(5) != 0 ? worldGeneratorTrees : field_150630_aD;
	}

	@Override
	public String func_150572_a(Random p_150572_1_, int p_150572_2_, int p_150572_3_, int p_150572_4_) {
		if (field_150632_aF == 1) {
			double d0 = MathHelper.clamp_double(
					(1.0D + plantNoise.func_151601_a(p_150572_2_ / 48.0D, p_150572_4_ / 48.0D)) / 2.0D, 0.0D, 0.9999D);
			int l = (int) (d0 * BlockFlower.field_149859_a.length);

			if (l == 1) {
				l = 0;
			}

			return BlockFlower.field_149859_a[l];
		} else
			return super.func_150572_a(p_150572_1_, p_150572_2_, p_150572_3_, p_150572_4_);
	}

	@Override
	public void decorate(World p_76728_1_, Random p_76728_2_, int p_76728_3_, int p_76728_4_) {
		int k;
		int l;
		int i1;
		int j1;
		int k1;

		if (field_150632_aF == 3) {
			for (k = 0; k < 4; ++k) {
				for (l = 0; l < 4; ++l) {
					i1 = p_76728_3_ + k * 4 + 1 + 8 + p_76728_2_.nextInt(3);
					j1 = p_76728_4_ + l * 4 + 1 + 8 + p_76728_2_.nextInt(3);
					k1 = p_76728_1_.getHeightValue(i1, j1);

					if (p_76728_2_.nextInt(20) == 0) {
						WorldGenBigMushroom worldgenbigmushroom = new WorldGenBigMushroom();
						worldgenbigmushroom.generate(p_76728_1_, p_76728_2_, i1, k1, j1);
					} else {
						WorldGenAbstractTree worldgenabstracttree = func_150567_a(p_76728_2_);
						worldgenabstracttree.setScale(1.0D, 1.0D, 1.0D);

						if (worldgenabstracttree.generate(p_76728_1_, p_76728_2_, i1, k1, j1)) {
							worldgenabstracttree.func_150524_b(p_76728_1_, p_76728_2_, i1, k1, j1);
						}
					}
				}
			}
		}

		k = p_76728_2_.nextInt(5) - 3;

		if (field_150632_aF == 1) {
			k += 2;
		}

		l = 0;

		while (l < k) {
			i1 = p_76728_2_.nextInt(3);

			if (i1 == 0) {
				genTallFlowers.func_150548_a(1);
			} else if (i1 == 1) {
				genTallFlowers.func_150548_a(4);
			} else if (i1 == 2) {
				genTallFlowers.func_150548_a(5);
			}

			j1 = 0;

			while (true) {
				if (j1 < 5) {
					k1 = p_76728_3_ + p_76728_2_.nextInt(16) + 8;
					int i2 = p_76728_4_ + p_76728_2_.nextInt(16) + 8;
					int l1 = p_76728_2_.nextInt(p_76728_1_.getHeightValue(k1, i2) + 32);

					if (!genTallFlowers.generate(p_76728_1_, p_76728_2_, k1, l1, i2)) {
						++j1;
						continue;
					}
				}

				++l;
				break;
			}
		}

		super.decorate(p_76728_1_, p_76728_2_, p_76728_3_, p_76728_4_);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getBiomeGrassColor(int p_150558_1_, int p_150558_2_, int p_150558_3_) {
		int l = super.getBiomeGrassColor(p_150558_1_, p_150558_2_, p_150558_3_);
		return field_150632_aF == 3 ? (l & 16711422) + 2634762 >> 1 : l;
	}

	@Override
	public BiomeGenBase createMutation() {
		if (biomeID == BiomeGenBase.forest.biomeID) {
			BiomeGenForest biomegenforest = new BiomeGenForest(biomeID + 128, 1);
			biomegenforest.setHeight(new BiomeGenBase.Height(rootHeight, heightVariation + 0.2F));
			biomegenforest.setBiomeName("Flower Forest");
			biomegenforest.func_150557_a(6976549, true);
			biomegenforest.func_76733_a(8233509);
			return biomegenforest;
		} else
			return biomeID != BiomeGenBase.birchForest.biomeID && biomeID != BiomeGenBase.birchForestHills.biomeID
					? new BiomeGenMutated(biomeID + 128, this) {
						@Override
						public void decorate(World p_76728_1_, Random p_76728_2_, int p_76728_3_, int p_76728_4_) {
							baseBiome.decorate(p_76728_1_, p_76728_2_, p_76728_3_, p_76728_4_);
						}
					}
					: new BiomeGenMutated(biomeID + 128, this) {
						@Override
						public WorldGenAbstractTree func_150567_a(Random p_150567_1_) {
							return p_150567_1_.nextBoolean() ? BiomeGenForest.field_150629_aC
									: BiomeGenForest.field_150630_aD;
						}
					};
	}
}