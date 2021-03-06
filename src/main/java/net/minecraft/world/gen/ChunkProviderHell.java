package net.minecraft.world.gen;

import cpw.mods.fml.common.eventhandler.Event.Result;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.structure.MapGenNetherBridge;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.ChunkProviderEvent;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.event.terraingen.TerrainGen;

import java.util.List;
import java.util.Random;

import static net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.SHROOM;
import static net.minecraftforge.event.terraingen.InitMapGenEvent.EventType.NETHER_BRIDGE;
import static net.minecraftforge.event.terraingen.InitMapGenEvent.EventType.NETHER_CAVE;
import static net.minecraftforge.event.terraingen.OreGenEvent.GenerateMinable.EventType.QUARTZ;
import static net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.*;

public class ChunkProviderHell implements IChunkProvider {
	private final Random hellRNG;
	private NoiseGeneratorOctaves netherNoiseGen1;
	private NoiseGeneratorOctaves netherNoiseGen2;
	private NoiseGeneratorOctaves netherNoiseGen3;
	private NoiseGeneratorOctaves slowsandGravelNoiseGen;
	private NoiseGeneratorOctaves netherrackExculsivityNoiseGen;
	public NoiseGeneratorOctaves netherNoiseGen6;
	public NoiseGeneratorOctaves netherNoiseGen7;
	private final World worldObj;
	private double[] noiseField;
	public MapGenNetherBridge genNetherBridge = new MapGenNetherBridge();
	private double[] slowsandNoise = new double[256];
	private double[] gravelNoise = new double[256];
	private double[] netherrackExclusivityNoise = new double[256];
	private MapGenBase netherCaveGenerator = new MapGenCavesHell();
	double[] noiseData1;
	double[] noiseData2;
	double[] noiseData3;
	double[] noiseData4;
	double[] noiseData5;
	{
		genNetherBridge = (MapGenNetherBridge) TerrainGen.getModdedMapGen(genNetherBridge, NETHER_BRIDGE);
		netherCaveGenerator = TerrainGen.getModdedMapGen(netherCaveGenerator, NETHER_CAVE);
	}

	public ChunkProviderHell(World p_i2005_1_, long p_i2005_2_) {
		worldObj = p_i2005_1_;
		hellRNG = new Random(p_i2005_2_);
		netherNoiseGen1 = new NoiseGeneratorOctaves(hellRNG, 16);
		netherNoiseGen2 = new NoiseGeneratorOctaves(hellRNG, 16);
		netherNoiseGen3 = new NoiseGeneratorOctaves(hellRNG, 8);
		slowsandGravelNoiseGen = new NoiseGeneratorOctaves(hellRNG, 4);
		netherrackExculsivityNoiseGen = new NoiseGeneratorOctaves(hellRNG, 4);
		netherNoiseGen6 = new NoiseGeneratorOctaves(hellRNG, 10);
		netherNoiseGen7 = new NoiseGeneratorOctaves(hellRNG, 16);

		NoiseGenerator[] noiseGens = { netherNoiseGen1, netherNoiseGen2, netherNoiseGen3, slowsandGravelNoiseGen,
				netherrackExculsivityNoiseGen, netherNoiseGen6, netherNoiseGen7 };
		noiseGens = TerrainGen.getModdedNoiseGenerators(p_i2005_1_, hellRNG, noiseGens);
		netherNoiseGen1 = (NoiseGeneratorOctaves) noiseGens[0];
		netherNoiseGen2 = (NoiseGeneratorOctaves) noiseGens[1];
		netherNoiseGen3 = (NoiseGeneratorOctaves) noiseGens[2];
		slowsandGravelNoiseGen = (NoiseGeneratorOctaves) noiseGens[3];
		netherrackExculsivityNoiseGen = (NoiseGeneratorOctaves) noiseGens[4];
		netherNoiseGen6 = (NoiseGeneratorOctaves) noiseGens[5];
		netherNoiseGen7 = (NoiseGeneratorOctaves) noiseGens[6];
	}

	public void func_147419_a(int p_147419_1_, int p_147419_2_, Block[] p_147419_3_) {
		byte b0 = 4;
		byte b1 = 32;
		int k = b0 + 1;
		byte b2 = 17;
		int l = b0 + 1;
		noiseField = initializeNoiseField(noiseField, p_147419_1_ * b0, 0, p_147419_2_ * b0, k, b2, l);

		for (int i1 = 0; i1 < b0; ++i1) {
			for (int j1 = 0; j1 < b0; ++j1) {
				for (int k1 = 0; k1 < 16; ++k1) {
					double d0 = 0.125D;
					double d1 = noiseField[((i1 + 0) * l + j1 + 0) * b2 + k1 + 0];
					double d2 = noiseField[((i1 + 0) * l + j1 + 1) * b2 + k1 + 0];
					double d3 = noiseField[((i1 + 1) * l + j1 + 0) * b2 + k1 + 0];
					double d4 = noiseField[((i1 + 1) * l + j1 + 1) * b2 + k1 + 0];
					double d5 = (noiseField[((i1 + 0) * l + j1 + 0) * b2 + k1 + 1] - d1) * d0;
					double d6 = (noiseField[((i1 + 0) * l + j1 + 1) * b2 + k1 + 1] - d2) * d0;
					double d7 = (noiseField[((i1 + 1) * l + j1 + 0) * b2 + k1 + 1] - d3) * d0;
					double d8 = (noiseField[((i1 + 1) * l + j1 + 1) * b2 + k1 + 1] - d4) * d0;

					for (int l1 = 0; l1 < 8; ++l1) {
						double d9 = 0.25D;
						double d10 = d1;
						double d11 = d2;
						double d12 = (d3 - d1) * d9;
						double d13 = (d4 - d2) * d9;

						for (int i2 = 0; i2 < 4; ++i2) {
							int j2 = i2 + i1 * 4 << 11 | 0 + j1 * 4 << 7 | k1 * 8 + l1;
							short short1 = 128;
							double d14 = 0.25D;
							double d15 = d10;
							double d16 = (d11 - d10) * d14;

							for (int k2 = 0; k2 < 4; ++k2) {
								Block block = null;

								if (k1 * 8 + l1 < b1) {
									block = Blocks.lava;
								}

								if (d15 > 0.0D) {
									block = Blocks.netherrack;
								}

								p_147419_3_[j2] = block;
								j2 += short1;
								d15 += d16;
							}

							d10 += d12;
							d11 += d13;
						}

						d1 += d5;
						d2 += d6;
						d3 += d7;
						d4 += d8;
					}
				}
			}
		}
	}

	@Deprecated // You should provide meatadata and biome data in the below method
	public void func_147418_b(int p_147418_1_, int p_147418_2_, Block[] p_147418_3_) {
		replaceBiomeBlocks(p_147418_1_, p_147418_2_, p_147418_3_, new byte[p_147418_3_.length], null);
	}

	public void replaceBiomeBlocks(int p_147418_1_, int p_147418_2_, Block[] p_147418_3_, byte[] meta,
			BiomeGenBase[] biomes) {
		ChunkProviderEvent.ReplaceBiomeBlocks event = new ChunkProviderEvent.ReplaceBiomeBlocks(this, p_147418_1_,
				p_147418_2_, p_147418_3_, meta, biomes, worldObj);
		MinecraftForge.EVENT_BUS.post(event);
		if (event.getResult() == Result.DENY)
			return;

		byte b0 = 64;
		double d0 = 0.03125D;
		slowsandNoise = slowsandGravelNoiseGen.generateNoiseOctaves(slowsandNoise, p_147418_1_ * 16, p_147418_2_ * 16,
				0, 16, 16, 1, d0, d0, 1.0D);
		gravelNoise = slowsandGravelNoiseGen.generateNoiseOctaves(gravelNoise, p_147418_1_ * 16, 109, p_147418_2_ * 16,
				16, 1, 16, d0, 1.0D, d0);
		netherrackExclusivityNoise = netherrackExculsivityNoiseGen.generateNoiseOctaves(netherrackExclusivityNoise,
				p_147418_1_ * 16, p_147418_2_ * 16, 0, 16, 16, 1, d0 * 2.0D, d0 * 2.0D, d0 * 2.0D);

		for (int k = 0; k < 16; ++k) {
			for (int l = 0; l < 16; ++l) {
				boolean flag = slowsandNoise[k + l * 16] + hellRNG.nextDouble() * 0.2D > 0.0D;
				boolean flag1 = gravelNoise[k + l * 16] + hellRNG.nextDouble() * 0.2D > 0.0D;
				int i1 = (int) (netherrackExclusivityNoise[k + l * 16] / 3.0D + 3.0D + hellRNG.nextDouble() * 0.25D);
				int j1 = -1;
				Block block = Blocks.netherrack;
				Block block1 = Blocks.netherrack;

				for (int k1 = 127; k1 >= 0; --k1) {
					int l1 = (l * 16 + k) * 128 + k1;

					if (k1 < 127 - hellRNG.nextInt(5) && k1 > 0 + hellRNG.nextInt(5)) {
						Block block2 = p_147418_3_[l1];

						if (block2 != null && block2.getMaterial() != Material.air) {
							if (block2 == Blocks.netherrack) {
								if (j1 == -1) {
									if (i1 <= 0) {
										block = null;
										block1 = Blocks.netherrack;
									} else if (k1 >= b0 - 4 && k1 <= b0 + 1) {
										block = Blocks.netherrack;
										block1 = Blocks.netherrack;

										if (flag1) {
											block = Blocks.gravel;
											block1 = Blocks.netherrack;
										}

										if (flag) {
											block = Blocks.soul_sand;
											block1 = Blocks.soul_sand;
										}
									}

									if (k1 < b0 && (block == null || block.getMaterial() == Material.air)) {
										block = Blocks.lava;
									}

									j1 = i1;

									if (k1 >= b0 - 1) {
										p_147418_3_[l1] = block;
									} else {
										p_147418_3_[l1] = block1;
									}
								} else if (j1 > 0) {
									--j1;
									p_147418_3_[l1] = block1;
								}
							}
						} else {
							j1 = -1;
						}
					} else {
						p_147418_3_[l1] = Blocks.bedrock;
					}
				}
			}
		}
	}

	@Override
	public Chunk loadChunk(int p_73158_1_, int p_73158_2_) {
		return provideChunk(p_73158_1_, p_73158_2_);
	}

	@Override
	public Chunk provideChunk(int p_73154_1_, int p_73154_2_) {
		hellRNG.setSeed(p_73154_1_ * 341873128712L + p_73154_2_ * 132897987541L);
		Block[] ablock = new Block[32768];
		byte[] meta = new byte[ablock.length];
		BiomeGenBase[] abiomegenbase = worldObj.getWorldChunkManager().loadBlockGeneratorData(null,
				p_73154_1_ * 16, p_73154_2_ * 16, 16, 16); // Forge Move
															// up to
															// allow for
															// passing
															// to
															// replaceBiomeBlocks
		func_147419_a(p_73154_1_, p_73154_2_, ablock);
		replaceBiomeBlocks(p_73154_1_, p_73154_2_, ablock, meta, abiomegenbase);
		netherCaveGenerator.func_151539_a(this, worldObj, p_73154_1_, p_73154_2_, ablock);
		genNetherBridge.func_151539_a(this, worldObj, p_73154_1_, p_73154_2_, ablock);
		Chunk chunk = new Chunk(worldObj, ablock, meta, p_73154_1_, p_73154_2_);
		byte[] abyte = chunk.getBiomeArray();

		for (int k = 0; k < abyte.length; ++k) {
			abyte[k] = (byte) abiomegenbase[k].biomeID;
		}

		chunk.resetRelightChecks();
		return chunk;
	}

	private double[] initializeNoiseField(double[] p_73164_1_, int p_73164_2_, int p_73164_3_, int p_73164_4_,
			int p_73164_5_, int p_73164_6_, int p_73164_7_) {
		ChunkProviderEvent.InitNoiseField event = new ChunkProviderEvent.InitNoiseField(this, p_73164_1_, p_73164_2_,
				p_73164_3_, p_73164_4_, p_73164_5_, p_73164_6_, p_73164_7_);
		MinecraftForge.EVENT_BUS.post(event);
		if (event.getResult() == Result.DENY)
			return event.noisefield;

		if (p_73164_1_ == null) {
			p_73164_1_ = new double[p_73164_5_ * p_73164_6_ * p_73164_7_];
		}

		double d0 = 684.412D;
		double d1 = 2053.236D;
		noiseData4 = netherNoiseGen6.generateNoiseOctaves(noiseData4, p_73164_2_, p_73164_3_, p_73164_4_, p_73164_5_, 1,
				p_73164_7_, 1.0D, 0.0D, 1.0D);
		noiseData5 = netherNoiseGen7.generateNoiseOctaves(noiseData5, p_73164_2_, p_73164_3_, p_73164_4_, p_73164_5_, 1,
				p_73164_7_, 100.0D, 0.0D, 100.0D);
		noiseData1 = netherNoiseGen3.generateNoiseOctaves(noiseData1, p_73164_2_, p_73164_3_, p_73164_4_, p_73164_5_,
				p_73164_6_, p_73164_7_, d0 / 80.0D, d1 / 60.0D, d0 / 80.0D);
		noiseData2 = netherNoiseGen1.generateNoiseOctaves(noiseData2, p_73164_2_, p_73164_3_, p_73164_4_, p_73164_5_,
				p_73164_6_, p_73164_7_, d0, d1, d0);
		noiseData3 = netherNoiseGen2.generateNoiseOctaves(noiseData3, p_73164_2_, p_73164_3_, p_73164_4_, p_73164_5_,
				p_73164_6_, p_73164_7_, d0, d1, d0);
		int k1 = 0;
		int l1 = 0;
		double[] adouble1 = new double[p_73164_6_];
		int i2;

		for (i2 = 0; i2 < p_73164_6_; ++i2) {
			adouble1[i2] = Math.cos(i2 * Math.PI * 6.0D / p_73164_6_) * 2.0D;
			double d2 = i2;

			if (i2 > p_73164_6_ / 2) {
				d2 = p_73164_6_ - 1 - i2;
			}

			if (d2 < 4.0D) {
				d2 = 4.0D - d2;
				adouble1[i2] -= d2 * d2 * d2 * 10.0D;
			}
		}

		for (i2 = 0; i2 < p_73164_5_; ++i2) {
			for (int k2 = 0; k2 < p_73164_7_; ++k2) {
				double d3 = (noiseData4[l1] + 256.0D) / 512.0D;

				if (d3 > 1.0D) {
					d3 = 1.0D;
				}

				double d4 = 0.0D;
				double d5 = noiseData5[l1] / 8000.0D;

				if (d5 < 0.0D) {
					d5 = -d5;
				}

				d5 = d5 * 3.0D - 3.0D;

				if (d5 < 0.0D) {
					d5 /= 2.0D;

					if (d5 < -1.0D) {
						d5 = -1.0D;
					}

					d5 /= 1.4D;
					d5 /= 2.0D;
					d3 = 0.0D;
				} else {
					if (d5 > 1.0D) {
						d5 = 1.0D;
					}

					d5 /= 6.0D;
				}

				d3 += 0.5D;
				d5 = d5 * p_73164_6_ / 16.0D;
				++l1;

				for (int j2 = 0; j2 < p_73164_6_; ++j2) {
					double d6 = 0.0D;
					double d7 = adouble1[j2];
					double d8 = noiseData2[k1] / 512.0D;
					double d9 = noiseData3[k1] / 512.0D;
					double d10 = (noiseData1[k1] / 10.0D + 1.0D) / 2.0D;

					if (d10 < 0.0D) {
						d6 = d8;
					} else if (d10 > 1.0D) {
						d6 = d9;
					} else {
						d6 = d8 + (d9 - d8) * d10;
					}

					d6 -= d7;
					double d11;

					if (j2 > p_73164_6_ - 4) {
						d11 = (j2 - (p_73164_6_ - 4)) / 3.0F;
						d6 = d6 * (1.0D - d11) + -10.0D * d11;
					}

					if (j2 < d4) {
						d11 = (d4 - j2) / 4.0D;

						if (d11 < 0.0D) {
							d11 = 0.0D;
						}

						if (d11 > 1.0D) {
							d11 = 1.0D;
						}

						d6 = d6 * (1.0D - d11) + -10.0D * d11;
					}

					p_73164_1_[k1] = d6;
					++k1;
				}
			}
		}

		return p_73164_1_;
	}

	@Override
	public boolean chunkExists(int p_73149_1_, int p_73149_2_) {
		return true;
	}

	@Override
	public void populate(IChunkProvider p_73153_1_, int p_73153_2_, int p_73153_3_) {
		BlockFalling.fallInstantly = true;

		MinecraftForge.EVENT_BUS
				.post(new PopulateChunkEvent.Pre(p_73153_1_, worldObj, hellRNG, p_73153_2_, p_73153_3_, false));

		int k = p_73153_2_ * 16;
		int l = p_73153_3_ * 16;
		genNetherBridge.generateStructuresInChunk(worldObj, hellRNG, p_73153_2_, p_73153_3_);
		int i1;
		int j1;
		int k1;
		int l1;

		boolean doGen = TerrainGen.populate(p_73153_1_, worldObj, hellRNG, p_73153_2_, p_73153_3_, false, NETHER_LAVA);
		for (i1 = 0; doGen && i1 < 8; ++i1) {
			j1 = k + hellRNG.nextInt(16) + 8;
			k1 = hellRNG.nextInt(120) + 4;
			l1 = l + hellRNG.nextInt(16) + 8;
			new WorldGenHellLava(Blocks.flowing_lava, false).generate(worldObj, hellRNG, j1, k1, l1);
		}

		i1 = hellRNG.nextInt(hellRNG.nextInt(10) + 1) + 1;
		int i2;

		doGen = TerrainGen.populate(p_73153_1_, worldObj, hellRNG, p_73153_2_, p_73153_3_, false, FIRE);
		for (j1 = 0; doGen && j1 < i1; ++j1) {
			k1 = k + hellRNG.nextInt(16) + 8;
			l1 = hellRNG.nextInt(120) + 4;
			i2 = l + hellRNG.nextInt(16) + 8;
			new WorldGenFire().generate(worldObj, hellRNG, k1, l1, i2);
		}

		i1 = hellRNG.nextInt(hellRNG.nextInt(10) + 1);

		doGen = TerrainGen.populate(p_73153_1_, worldObj, hellRNG, p_73153_2_, p_73153_3_, false, GLOWSTONE);
		for (j1 = 0; doGen && j1 < i1; ++j1) {
			k1 = k + hellRNG.nextInt(16) + 8;
			l1 = hellRNG.nextInt(120) + 4;
			i2 = l + hellRNG.nextInt(16) + 8;
			new WorldGenGlowStone1().generate(worldObj, hellRNG, k1, l1, i2);
		}

		for (j1 = 0; doGen && j1 < 10; ++j1) {
			k1 = k + hellRNG.nextInt(16) + 8;
			l1 = hellRNG.nextInt(128);
			i2 = l + hellRNG.nextInt(16) + 8;
			new WorldGenGlowStone2().generate(worldObj, hellRNG, k1, l1, i2);
		}

		MinecraftForge.EVENT_BUS.post(new DecorateBiomeEvent.Pre(worldObj, hellRNG, k, l));

		doGen = TerrainGen.decorate(worldObj, hellRNG, k, l, SHROOM);
		if (doGen && hellRNG.nextInt(1) == 0) {
			j1 = k + hellRNG.nextInt(16) + 8;
			k1 = hellRNG.nextInt(128);
			l1 = l + hellRNG.nextInt(16) + 8;
			new WorldGenFlowers(Blocks.brown_mushroom).generate(worldObj, hellRNG, j1, k1, l1);
		}

		if (doGen && hellRNG.nextInt(1) == 0) {
			j1 = k + hellRNG.nextInt(16) + 8;
			k1 = hellRNG.nextInt(128);
			l1 = l + hellRNG.nextInt(16) + 8;
			new WorldGenFlowers(Blocks.red_mushroom).generate(worldObj, hellRNG, j1, k1, l1);
		}

		WorldGenMinable worldgenminable = new WorldGenMinable(Blocks.quartz_ore, 13, Blocks.netherrack);
		int j2;

		doGen = TerrainGen.generateOre(worldObj, hellRNG, worldgenminable, k, l, QUARTZ);
		for (k1 = 0; doGen && k1 < 16; ++k1) {
			l1 = k + hellRNG.nextInt(16);
			i2 = hellRNG.nextInt(108) + 10;
			j2 = l + hellRNG.nextInt(16);
			worldgenminable.generate(worldObj, hellRNG, l1, i2, j2);
		}

		for (k1 = 0; k1 < 16; ++k1) {
			l1 = k + hellRNG.nextInt(16);
			i2 = hellRNG.nextInt(108) + 10;
			j2 = l + hellRNG.nextInt(16);
			new WorldGenHellLava(Blocks.flowing_lava, true).generate(worldObj, hellRNG, l1, i2, j2);
		}

		MinecraftForge.EVENT_BUS.post(new DecorateBiomeEvent.Post(worldObj, hellRNG, k, l));
		MinecraftForge.EVENT_BUS
				.post(new PopulateChunkEvent.Post(p_73153_1_, worldObj, hellRNG, p_73153_2_, p_73153_3_, false));

		BlockFalling.fallInstantly = false;
	}

	@Override
	public boolean saveChunks(boolean p_73151_1_, IProgressUpdate p_73151_2_) {
		return true;
	}

	@Override
	public void saveExtraData() {
	}

	@Override
	public boolean unloadQueuedChunks() {
		return false;
	}

	@Override
	public boolean canSave() {
		return true;
	}

	@Override
	public String makeString() {
		return "HellRandomLevelSource";
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List getPossibleCreatures(EnumCreatureType p_73155_1_, int p_73155_2_, int p_73155_3_, int p_73155_4_) {
		if (p_73155_1_ == EnumCreatureType.monster) {
			if (genNetherBridge.hasStructureAt(p_73155_2_, p_73155_3_, p_73155_4_))
				return genNetherBridge.getSpawnList();

			if (genNetherBridge.func_142038_b(p_73155_2_, p_73155_3_, p_73155_4_)
					&& worldObj.getBlock(p_73155_2_, p_73155_3_ - 1, p_73155_4_) == Blocks.nether_brick)
				return genNetherBridge.getSpawnList();
		}

		BiomeGenBase biomegenbase = worldObj.getBiomeGenForCoords(p_73155_2_, p_73155_4_);
		return biomegenbase.getSpawnableList(p_73155_1_);
	}

	@Override
	public ChunkPosition func_147416_a(World p_147416_1_, String p_147416_2_, int p_147416_3_, int p_147416_4_,
			int p_147416_5_) {
		return null;
	}

	@Override
	public int getLoadedChunkCount() {
		return 0;
	}

	@Override
	public void recreateStructures(int p_82695_1_, int p_82695_2_) {
		genNetherBridge.func_151539_a(this, worldObj, p_82695_1_, p_82695_2_, null);
	}
}