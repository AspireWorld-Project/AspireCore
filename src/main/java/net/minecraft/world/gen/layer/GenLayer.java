package net.minecraft.world.gen.layer;

import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.ReportedException;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.WorldTypeEvent;

import java.util.concurrent.Callable;

public abstract class GenLayer {
	private long worldGenSeed;
	protected GenLayer parent;
	private long chunkSeed;
	protected long baseSeed;
	public static GenLayer[] initializeAllBiomeGenerators(long p_75901_0_, WorldType p_75901_2_) {
		boolean flag = false;
		GenLayerIsland genlayerisland = new GenLayerIsland(1L);
		GenLayerFuzzyZoom genlayerfuzzyzoom = new GenLayerFuzzyZoom(2000L, genlayerisland);
		GenLayerAddIsland genlayeraddisland = new GenLayerAddIsland(1L, genlayerfuzzyzoom);
		GenLayerZoom genlayerzoom = new GenLayerZoom(2001L, genlayeraddisland);
		genlayeraddisland = new GenLayerAddIsland(2L, genlayerzoom);
		genlayeraddisland = new GenLayerAddIsland(50L, genlayeraddisland);
		genlayeraddisland = new GenLayerAddIsland(70L, genlayeraddisland);
		GenLayerRemoveTooMuchOcean genlayerremovetoomuchocean = new GenLayerRemoveTooMuchOcean(2L, genlayeraddisland);
		GenLayerAddSnow genlayeraddsnow = new GenLayerAddSnow(2L, genlayerremovetoomuchocean);
		genlayeraddisland = new GenLayerAddIsland(3L, genlayeraddsnow);
		GenLayerEdge genlayeredge = new GenLayerEdge(2L, genlayeraddisland, GenLayerEdge.Mode.COOL_WARM);
		genlayeredge = new GenLayerEdge(2L, genlayeredge, GenLayerEdge.Mode.HEAT_ICE);
		genlayeredge = new GenLayerEdge(3L, genlayeredge, GenLayerEdge.Mode.SPECIAL);
		genlayerzoom = new GenLayerZoom(2002L, genlayeredge);
		genlayerzoom = new GenLayerZoom(2003L, genlayerzoom);
		genlayeraddisland = new GenLayerAddIsland(4L, genlayerzoom);
		GenLayerAddMushroomIsland genlayeraddmushroomisland = new GenLayerAddMushroomIsland(5L, genlayeraddisland);
		GenLayerDeepOcean genlayerdeepocean = new GenLayerDeepOcean(4L, genlayeraddmushroomisland);
		GenLayer genlayer2 = GenLayerZoom.magnify(1000L, genlayerdeepocean, 0);
		byte b0 = 4;

		if (p_75901_2_ == WorldType.LARGE_BIOMES) {
			b0 = 6;
		}

		if (flag) {
			b0 = 4;
		}
		b0 = getModdedBiomeSize(p_75901_2_, b0);

		GenLayer genlayer = GenLayerZoom.magnify(1000L, genlayer2, 0);
		GenLayerRiverInit genlayerriverinit = new GenLayerRiverInit(100L, genlayer);
		Object object = p_75901_2_.getBiomeLayer(p_75901_0_, genlayer2);

		GenLayer genlayer1 = GenLayerZoom.magnify(1000L, genlayerriverinit, 2);
		GenLayerHills genlayerhills = new GenLayerHills(1000L, (GenLayer) object, genlayer1);
		genlayer = GenLayerZoom.magnify(1000L, genlayerriverinit, 2);
		genlayer = GenLayerZoom.magnify(1000L, genlayer, b0);
		GenLayerRiver genlayerriver = new GenLayerRiver(1L, genlayer);
		GenLayerSmooth genlayersmooth = new GenLayerSmooth(1000L, genlayerriver);
		object = new GenLayerRareBiome(1001L, genlayerhills);

		for (int j = 0; j < b0; ++j) {
			object = new GenLayerZoom(1000 + j, (GenLayer) object);

			if (j == 0) {
				object = new GenLayerAddIsland(3L, (GenLayer) object);
			}

			if (j == 1) {
				object = new GenLayerShore(1000L, (GenLayer) object);
			}
		}

		GenLayerSmooth genlayersmooth1 = new GenLayerSmooth(1000L, (GenLayer) object);
		GenLayerRiverMix genlayerrivermix = new GenLayerRiverMix(100L, genlayersmooth1, genlayersmooth);
		GenLayerVoronoiZoom genlayervoronoizoom = new GenLayerVoronoiZoom(10L, genlayerrivermix);
		genlayerrivermix.initWorldGenSeed(p_75901_0_);
		genlayervoronoizoom.initWorldGenSeed(p_75901_0_);
		return new GenLayer[] { genlayerrivermix, genlayervoronoizoom, genlayerrivermix };
	}

	public GenLayer(long p_i2125_1_) {
		baseSeed = p_i2125_1_;
		baseSeed *= baseSeed * 6364136223846793005L + 1442695040888963407L;
		baseSeed += p_i2125_1_;
		baseSeed *= baseSeed * 6364136223846793005L + 1442695040888963407L;
		baseSeed += p_i2125_1_;
		baseSeed *= baseSeed * 6364136223846793005L + 1442695040888963407L;
		baseSeed += p_i2125_1_;
	}

	public void initWorldGenSeed(long p_75905_1_) {
		worldGenSeed = p_75905_1_;

		if (parent != null) {
			parent.initWorldGenSeed(p_75905_1_);
		}

		worldGenSeed *= worldGenSeed * 6364136223846793005L + 1442695040888963407L;
		worldGenSeed += baseSeed;
		worldGenSeed *= worldGenSeed * 6364136223846793005L + 1442695040888963407L;
		worldGenSeed += baseSeed;
		worldGenSeed *= worldGenSeed * 6364136223846793005L + 1442695040888963407L;
		worldGenSeed += baseSeed;
	}

	public void initChunkSeed(long p_75903_1_, long p_75903_3_) {
		chunkSeed = worldGenSeed;
		chunkSeed *= chunkSeed * 6364136223846793005L + 1442695040888963407L;
		chunkSeed += p_75903_1_;
		chunkSeed *= chunkSeed * 6364136223846793005L + 1442695040888963407L;
		chunkSeed += p_75903_3_;
		chunkSeed *= chunkSeed * 6364136223846793005L + 1442695040888963407L;
		chunkSeed += p_75903_1_;
		chunkSeed *= chunkSeed * 6364136223846793005L + 1442695040888963407L;
		chunkSeed += p_75903_3_;
	}

	protected int nextInt(int p_75902_1_) {
		int j = (int) ((chunkSeed >> 24) % p_75902_1_);

		if (j < 0) {
			j += p_75902_1_;
		}

		chunkSeed *= chunkSeed * 6364136223846793005L + 1442695040888963407L;
		chunkSeed += worldGenSeed;
		return j;
	}

	public abstract int[] getInts(int p_75904_1_, int p_75904_2_, int p_75904_3_, int p_75904_4_);

	@SuppressWarnings("rawtypes")
	protected static boolean compareBiomesById(final int p_151616_0_, final int p_151616_1_) {
		if (p_151616_0_ == p_151616_1_)
			return true;
		else if (p_151616_0_ != BiomeGenBase.mesaPlateau_F.biomeID && p_151616_0_ != BiomeGenBase.mesaPlateau.biomeID) {
			try {
				return BiomeGenBase.getBiome(p_151616_0_) != null && BiomeGenBase.getBiome(p_151616_1_) != null && BiomeGenBase.getBiome(p_151616_0_).isEqualTo(BiomeGenBase.getBiome(p_151616_1_));
			} catch (Throwable throwable) {
				CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Comparing biomes");
				CrashReportCategory crashreportcategory = crashreport.makeCategory("Biomes being compared");
				crashreportcategory.addCrashSection("Biome A ID", Integer.valueOf(p_151616_0_));
				crashreportcategory.addCrashSection("Biome B ID", Integer.valueOf(p_151616_1_));
				crashreportcategory.addCrashSectionCallable("Biome A", new Callable() {
					@Override
					public String call() {
						return String.valueOf(BiomeGenBase.getBiome(p_151616_0_));
					}
				});
				crashreportcategory.addCrashSectionCallable("Biome B", new Callable() {
					@Override
					public String call() {
						return String.valueOf(BiomeGenBase.getBiome(p_151616_1_));
					}
				});
				throw new ReportedException(crashreport);
			}
		} else
			return p_151616_1_ == BiomeGenBase.mesaPlateau_F.biomeID || p_151616_1_ == BiomeGenBase.mesaPlateau.biomeID;
	}

	protected static boolean isBiomeOceanic(int p_151618_0_) {
		return BiomeManager.oceanBiomes.contains(BiomeGenBase.getBiome(p_151618_0_));
	}

	protected int selectRandom(int... p_151619_1_) {
		return p_151619_1_[nextInt(p_151619_1_.length)];
	}

	protected int selectModeOrRandom(int p_151617_1_, int p_151617_2_, int p_151617_3_, int p_151617_4_) {
		return p_151617_2_ == p_151617_3_ && p_151617_3_ == p_151617_4_ ? p_151617_2_
				: p_151617_1_ == p_151617_2_ && p_151617_1_ == p_151617_3_ ? p_151617_1_
						: p_151617_1_ == p_151617_2_ && p_151617_1_ == p_151617_4_ ? p_151617_1_
								: p_151617_1_ == p_151617_3_ && p_151617_1_ == p_151617_4_ ? p_151617_1_
										: p_151617_1_ == p_151617_2_ && p_151617_3_ != p_151617_4_ ? p_151617_1_
												: p_151617_1_ == p_151617_3_ && p_151617_2_ != p_151617_4_ ? p_151617_1_
														: p_151617_1_ == p_151617_4_ && p_151617_2_ != p_151617_3_
																? p_151617_1_
																: p_151617_2_ == p_151617_3_
																		&& p_151617_1_ != p_151617_4_
																				? p_151617_2_
																				: p_151617_2_ == p_151617_4_
																						&& p_151617_1_ != p_151617_3_
																								? p_151617_2_
																								: p_151617_3_ == p_151617_4_
																										&& p_151617_1_ != p_151617_2_
																												? p_151617_3_
																												: selectRandom(
				p_151617_1_,
				p_151617_2_,
				p_151617_3_,
				p_151617_4_);
	}

	/*
	 * ======================================== FORGE START
	 * =====================================
	 */
	protected long nextLong(long par1) {
		long j = (chunkSeed >> 24) % par1;

		if (j < 0) {
			j += par1;
		}

		chunkSeed *= chunkSeed * 6364136223846793005L + 1442695040888963407L;
		chunkSeed += worldGenSeed;
		return j;
	}

	public static byte getModdedBiomeSize(WorldType worldType, byte original) {
		WorldTypeEvent.BiomeSize event = new WorldTypeEvent.BiomeSize(worldType, original);
		MinecraftForge.TERRAIN_GEN_BUS.post(event);
		return event.newSize;
	}
	/*
	 * ========================================= FORGE END
	 * ======================================
	 */
}