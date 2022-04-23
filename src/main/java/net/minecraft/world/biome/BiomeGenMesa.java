package net.minecraft.world.biome;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.NoiseGeneratorPerlin;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

import java.util.Arrays;
import java.util.Random;

public class BiomeGenMesa extends BiomeGenBase {
	private byte[] field_150621_aC;
	private long field_150622_aD;
	private NoiseGeneratorPerlin field_150623_aE;
	private NoiseGeneratorPerlin field_150624_aF;
	private NoiseGeneratorPerlin field_150625_aG;
	private final boolean field_150626_aH;
	private final boolean field_150620_aI;
	private static final String __OBFID = "CL_00000176";

	public BiomeGenMesa(int p_i45380_1_, boolean p_i45380_2_, boolean p_i45380_3_) {
		super(p_i45380_1_);
		field_150626_aH = p_i45380_2_;
		field_150620_aI = p_i45380_3_;
		setDisableRain();
		setTemperatureRainfall(2.0F, 0.0F);
		spawnableCreatureList.clear();
		topBlock = Blocks.sand;
		field_150604_aj = 1;
		fillerBlock = Blocks.stained_hardened_clay;
		theBiomeDecorator.treesPerChunk = -999;
		theBiomeDecorator.deadBushPerChunk = 20;
		theBiomeDecorator.reedsPerChunk = 3;
		theBiomeDecorator.cactiPerChunk = 5;
		theBiomeDecorator.flowersPerChunk = 0;
		spawnableCreatureList.clear();

		if (p_i45380_3_) {
			theBiomeDecorator.treesPerChunk = 5;
		}
	}

	@Override
	public WorldGenAbstractTree func_150567_a(Random p_150567_1_) {
		return worldGeneratorTrees;
	}

	@Override
	public void decorate(World p_76728_1_, Random p_76728_2_, int p_76728_3_, int p_76728_4_) {
		super.decorate(p_76728_1_, p_76728_2_, p_76728_3_, p_76728_4_);
	}

	@Override
	public void genTerrainBlocks(World p_150573_1_, Random p_150573_2_, Block[] p_150573_3_, byte[] p_150573_4_,
			int p_150573_5_, int p_150573_6_, double p_150573_7_) {
		if (field_150621_aC == null || field_150622_aD != p_150573_1_.getSeed()) {
			func_150619_a(p_150573_1_.getSeed());
		}

		if (field_150623_aE == null || field_150624_aF == null || field_150622_aD != p_150573_1_.getSeed()) {
			Random random1 = new Random(field_150622_aD);
			field_150623_aE = new NoiseGeneratorPerlin(random1, 4);
			field_150624_aF = new NoiseGeneratorPerlin(random1, 1);
		}

		field_150622_aD = p_150573_1_.getSeed();
		double d5 = 0.0D;
		int k;
		int l;

		if (field_150626_aH) {
			k = (p_150573_5_ & -16) + (p_150573_6_ & 15);
			l = (p_150573_6_ & -16) + (p_150573_5_ & 15);
			double d1 = Math.min(Math.abs(p_150573_7_), field_150623_aE.func_151601_a(k * 0.25D, l * 0.25D));

			if (d1 > 0.0D) {
				double d2 = 0.001953125D;
				double d3 = Math.abs(field_150624_aF.func_151601_a(k * d2, l * d2));
				d5 = d1 * d1 * 2.5D;
				double d4 = Math.ceil(d3 * 50.0D) + 14.0D;

				if (d5 > d4) {
					d5 = d4;
				}

				d5 += 64.0D;
			}
		}

		k = p_150573_5_ & 15;
		l = p_150573_6_ & 15;
		Block block = Blocks.stained_hardened_clay;
		Block block2 = fillerBlock;
		int i1 = (int) (p_150573_7_ / 3.0D + 3.0D + p_150573_2_.nextDouble() * 0.25D);
		boolean flag1 = Math.cos(p_150573_7_ / 3.0D * Math.PI) > 0.0D;
		int j1 = -1;
		boolean flag2 = false;
		int k1 = p_150573_3_.length / 256;

		for (int l1 = 255; l1 >= 0; --l1) {
			int i2 = (l * 16 + k) * k1 + l1;

			if ((p_150573_3_[i2] == null || p_150573_3_[i2].getMaterial() == Material.air) && l1 < (int) d5) {
				p_150573_3_[i2] = Blocks.stone;
			}

			if (l1 <= 0 + p_150573_2_.nextInt(5)) {
				p_150573_3_[i2] = Blocks.bedrock;
			} else {
				Block block1 = p_150573_3_[i2];

				if (block1 != null && block1.getMaterial() != Material.air) {
					if (block1 == Blocks.stone) {
						byte b0;

						if (j1 == -1) {
							flag2 = false;

							if (i1 <= 0) {
								block = null;
								block2 = Blocks.stone;
							} else if (l1 >= 59 && l1 <= 64) {
								block = Blocks.stained_hardened_clay;
								block2 = fillerBlock;
							}

							if (l1 < 63 && (block == null || block.getMaterial() == Material.air)) {
								block = Blocks.water;
							}

							j1 = i1 + Math.max(0, l1 - 63);

							if (l1 >= 62) {
								if (field_150620_aI && l1 > 86 + i1 * 2) {
									if (flag1) {
										p_150573_3_[i2] = Blocks.dirt;
										p_150573_4_[i2] = 1;
									} else {
										p_150573_3_[i2] = Blocks.grass;
									}
								} else if (l1 > 66 + i1) {
									b0 = 16;

									if (l1 >= 64 && l1 <= 127) {
										if (!flag1) {
											b0 = func_150618_d(p_150573_5_, l1, p_150573_6_);
										}
									} else {
										b0 = 1;
									}

									if (b0 < 16) {
										p_150573_3_[i2] = Blocks.stained_hardened_clay;
										p_150573_4_[i2] = b0;
									} else {
										p_150573_3_[i2] = Blocks.hardened_clay;
									}
								} else {
									p_150573_3_[i2] = topBlock;
									p_150573_4_[i2] = (byte) field_150604_aj;
									flag2 = true;
								}
							} else {
								p_150573_3_[i2] = block2;

								if (block2 == Blocks.stained_hardened_clay) {
									p_150573_4_[i2] = 1;
								}
							}
						} else if (j1 > 0) {
							--j1;

							if (flag2) {
								p_150573_3_[i2] = Blocks.stained_hardened_clay;
								p_150573_4_[i2] = 1;
							} else {
								b0 = func_150618_d(p_150573_5_, l1, p_150573_6_);

								if (b0 < 16) {
									p_150573_3_[i2] = Blocks.stained_hardened_clay;
									p_150573_4_[i2] = b0;
								} else {
									p_150573_3_[i2] = Blocks.hardened_clay;
								}
							}
						}
					}
				} else {
					j1 = -1;
				}
			}
		}
	}

	public void func_150619_a(long p_150619_1_) {
		field_150621_aC = new byte[64];
		Arrays.fill(field_150621_aC, (byte) 16);
		Random random = new Random(p_150619_1_);
		field_150625_aG = new NoiseGeneratorPerlin(random, 1);
		int j;

		for (j = 0; j < 64; ++j) {
			j += random.nextInt(5) + 1;

			if (j < 64) {
				field_150621_aC[j] = 1;
			}
		}

		j = random.nextInt(4) + 2;
		int k;
		int l;
		int i1;
		int j1;

		for (k = 0; k < j; ++k) {
			l = random.nextInt(3) + 1;
			i1 = random.nextInt(64);

			for (j1 = 0; i1 + j1 < 64 && j1 < l; ++j1) {
				field_150621_aC[i1 + j1] = 4;
			}
		}

		k = random.nextInt(4) + 2;
		int k1;

		for (l = 0; l < k; ++l) {
			i1 = random.nextInt(3) + 2;
			j1 = random.nextInt(64);

			for (k1 = 0; j1 + k1 < 64 && k1 < i1; ++k1) {
				field_150621_aC[j1 + k1] = 12;
			}
		}

		l = random.nextInt(4) + 2;

		for (i1 = 0; i1 < l; ++i1) {
			j1 = random.nextInt(3) + 1;
			k1 = random.nextInt(64);

			for (int l1 = 0; k1 + l1 < 64 && l1 < j1; ++l1) {
				field_150621_aC[k1 + l1] = 14;
			}
		}

		i1 = random.nextInt(3) + 3;
		j1 = 0;

		for (k1 = 0; k1 < i1; ++k1) {
			byte b0 = 1;
			j1 += random.nextInt(16) + 4;

			for (int i2 = 0; j1 + i2 < 64 && i2 < b0; ++i2) {
				field_150621_aC[j1 + i2] = 0;

				if (j1 + i2 > 1 && random.nextBoolean()) {
					field_150621_aC[j1 + i2 - 1] = 8;
				}

				if (j1 + i2 < 63 && random.nextBoolean()) {
					field_150621_aC[j1 + i2 + 1] = 8;
				}
			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getBiomeFoliageColor(int p_150571_1_, int p_150571_2_, int p_150571_3_) {
		return 10387789;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getBiomeGrassColor(int p_150558_1_, int p_150558_2_, int p_150558_3_) {
		return 9470285;
	}

	public byte func_150618_d(int p_150618_1_, int p_150618_2_, int p_150618_3_) {
		int l = (int) Math
				.round(field_150625_aG.func_151601_a(p_150618_1_ * 1.0D / 512.0D, p_150618_1_ * 1.0D / 512.0D) * 2.0D);
		return field_150621_aC[(p_150618_2_ + l + 64) % 64];
	}

	@Override
	public BiomeGenBase createMutation() {
		boolean flag = biomeID == BiomeGenBase.mesa.biomeID;
		BiomeGenMesa biomegenmesa = new BiomeGenMesa(biomeID + 128, flag, field_150620_aI);

		if (!flag) {
			biomegenmesa.setHeight(height_LowHills);
			biomegenmesa.setBiomeName(biomeName + " M");
		} else {
			biomegenmesa.setBiomeName(biomeName + " (Bryce)");
		}

		biomegenmesa.func_150557_a(color, true);
		return biomegenmesa;
	}
}