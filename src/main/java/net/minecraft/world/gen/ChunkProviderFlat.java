package net.minecraft.world.gen;

import net.minecraft.block.Block;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraft.world.gen.feature.WorldGenDungeons;
import net.minecraft.world.gen.feature.WorldGenLakes;
import net.minecraft.world.gen.structure.*;

import java.util.*;

public class ChunkProviderFlat implements IChunkProvider {
	private final World worldObj;
	private final Random random;
	private final Block[] cachedBlockIDs = new Block[256];
	private final byte[] cachedBlockMetadata = new byte[256];
	private final FlatGeneratorInfo flatWorldGenInfo;
	@SuppressWarnings("rawtypes")
	private final List structureGenerators = new ArrayList();
	private final boolean hasDecoration;
	private final boolean hasDungeons;
	private WorldGenLakes waterLakeGenerator;
	private WorldGenLakes lavaLakeGenerator;
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ChunkProviderFlat(World p_i2004_1_, long p_i2004_2_, boolean p_i2004_4_, String p_i2004_5_) {
		worldObj = p_i2004_1_;
		random = new Random(p_i2004_2_);
		flatWorldGenInfo = FlatGeneratorInfo.createFlatGeneratorFromString(p_i2004_5_);

		if (p_i2004_4_) {
			Map map = flatWorldGenInfo.getWorldFeatures();

			if (map.containsKey("village")) {
				Map map1 = (Map) map.get("village");

				if (!map1.containsKey("size")) {
					map1.put("size", "1");
				}

				structureGenerators.add(new MapGenVillage(map1));
			}

			if (map.containsKey("biome_1")) {
				structureGenerators.add(new MapGenScatteredFeature((Map) map.get("biome_1")));
			}

			if (map.containsKey("mineshaft")) {
				structureGenerators.add(new MapGenMineshaft((Map) map.get("mineshaft")));
			}

			if (map.containsKey("stronghold")) {
				structureGenerators.add(new MapGenStronghold((Map) map.get("stronghold")));
			}
		}

		hasDecoration = flatWorldGenInfo.getWorldFeatures().containsKey("decoration");

		if (flatWorldGenInfo.getWorldFeatures().containsKey("lake")) {
			waterLakeGenerator = new WorldGenLakes(Blocks.water);
		}

		if (flatWorldGenInfo.getWorldFeatures().containsKey("lava_lake")) {
			lavaLakeGenerator = new WorldGenLakes(Blocks.lava);
		}

		hasDungeons = flatWorldGenInfo.getWorldFeatures().containsKey("dungeon");
		Iterator iterator = flatWorldGenInfo.getFlatLayers().iterator();

		while (iterator.hasNext()) {
			FlatLayerInfo flatlayerinfo = (FlatLayerInfo) iterator.next();

			for (int j = flatlayerinfo.getMinY(); j < flatlayerinfo.getMinY() + flatlayerinfo.getLayerCount(); ++j) {
				cachedBlockIDs[j] = flatlayerinfo.func_151536_b();
				cachedBlockMetadata[j] = (byte) flatlayerinfo.getFillBlockMeta();
			}
		}
	}

	@Override
	public Chunk loadChunk(int p_73158_1_, int p_73158_2_) {
		return provideChunk(p_73158_1_, p_73158_2_);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Chunk provideChunk(int p_73154_1_, int p_73154_2_) {
		Chunk chunk = new Chunk(worldObj, p_73154_1_, p_73154_2_);
		int l;

		for (int k = 0; k < cachedBlockIDs.length; ++k) {
			Block block = cachedBlockIDs[k];

			if (block != null) {
				l = k >> 4;
				ExtendedBlockStorage extendedblockstorage = chunk.getBlockStorageArray()[l];

				if (extendedblockstorage == null) {
					extendedblockstorage = new ExtendedBlockStorage(k, !worldObj.provider.hasNoSky);
					chunk.getBlockStorageArray()[l] = extendedblockstorage;
				}

				for (int i1 = 0; i1 < 16; ++i1) {
					for (int j1 = 0; j1 < 16; ++j1) {
						extendedblockstorage.func_150818_a(i1, k & 15, j1, block);
						extendedblockstorage.setExtBlockMetadata(i1, k & 15, j1, cachedBlockMetadata[k]);
					}
				}
			}
		}

		chunk.generateSkylightMap();
		BiomeGenBase[] abiomegenbase = worldObj.getWorldChunkManager().loadBlockGeneratorData(null,
				p_73154_1_ * 16, p_73154_2_ * 16, 16, 16);
		byte[] abyte = chunk.getBiomeArray();

		for (l = 0; l < abyte.length; ++l) {
			abyte[l] = (byte) abiomegenbase[l].biomeID;
		}

		Iterator iterator = structureGenerators.iterator();

		while (iterator.hasNext()) {
			MapGenBase mapgenbase = (MapGenBase) iterator.next();
			mapgenbase.func_151539_a(this, worldObj, p_73154_1_, p_73154_2_, null);
		}

		chunk.generateSkylightMap();
		return chunk;
	}

	@Override
	public boolean chunkExists(int p_73149_1_, int p_73149_2_) {
		return true;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void populate(IChunkProvider p_73153_1_, int p_73153_2_, int p_73153_3_) {
		int k = p_73153_2_ * 16;
		int l = p_73153_3_ * 16;
		BiomeGenBase biomegenbase = worldObj.getBiomeGenForCoords(k + 16, l + 16);
		boolean flag = false;
		random.setSeed(worldObj.getSeed());
		long i1 = random.nextLong() / 2L * 2L + 1L;
		long j1 = random.nextLong() / 2L * 2L + 1L;
		random.setSeed(p_73153_2_ * i1 + p_73153_3_ * j1 ^ worldObj.getSeed());
		Iterator iterator = structureGenerators.iterator();

		while (iterator.hasNext()) {
			MapGenStructure mapgenstructure = (MapGenStructure) iterator.next();
			boolean flag1 = mapgenstructure.generateStructuresInChunk(worldObj, random, p_73153_2_, p_73153_3_);

			if (mapgenstructure instanceof MapGenVillage) {
				flag |= flag1;
			}
		}

		int l1;
		int i2;
		int j2;

		if (waterLakeGenerator != null && !flag && random.nextInt(4) == 0) {
			l1 = k + random.nextInt(16) + 8;
			i2 = random.nextInt(256);
			j2 = l + random.nextInt(16) + 8;
			waterLakeGenerator.generate(worldObj, random, l1, i2, j2);
		}

		if (lavaLakeGenerator != null && !flag && random.nextInt(8) == 0) {
			l1 = k + random.nextInt(16) + 8;
			i2 = random.nextInt(random.nextInt(248) + 8);
			j2 = l + random.nextInt(16) + 8;

			if (i2 < 63 || random.nextInt(10) == 0) {
				lavaLakeGenerator.generate(worldObj, random, l1, i2, j2);
			}
		}

		if (hasDungeons) {
			for (l1 = 0; l1 < 8; ++l1) {
				i2 = k + random.nextInt(16) + 8;
				j2 = random.nextInt(256);
				int k1 = l + random.nextInt(16) + 8;
				new WorldGenDungeons().generate(worldObj, random, i2, j2, k1);
			}
		}

		if (hasDecoration) {
			biomegenbase.decorate(worldObj, random, k, l);
		}
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
		return "FlatLevelSource";
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List getPossibleCreatures(EnumCreatureType p_73155_1_, int p_73155_2_, int p_73155_3_, int p_73155_4_) {
		BiomeGenBase biomegenbase = worldObj.getBiomeGenForCoords(p_73155_2_, p_73155_4_);
		return biomegenbase.getSpawnableList(p_73155_1_);
	}

	@SuppressWarnings({ "rawtypes" })
	@Override
	public ChunkPosition func_147416_a(World p_147416_1_, String p_147416_2_, int p_147416_3_, int p_147416_4_,
			int p_147416_5_) {
		if ("Stronghold".equals(p_147416_2_)) {
			Iterator iterator = structureGenerators.iterator();

			while (iterator.hasNext()) {
				MapGenStructure mapgenstructure = (MapGenStructure) iterator.next();

				if (mapgenstructure instanceof MapGenStronghold)
					return mapgenstructure.func_151545_a(p_147416_1_, p_147416_3_, p_147416_4_, p_147416_5_);
			}
		}

		return null;
	}

	@Override
	public int getLoadedChunkCount() {
		return 0;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void recreateStructures(int p_82695_1_, int p_82695_2_) {
		Iterator iterator = structureGenerators.iterator();

		while (iterator.hasNext()) {
			MapGenStructure mapgenstructure = (MapGenStructure) iterator.next();
			mapgenstructure.func_151539_a(this, worldObj, p_82695_1_, p_82695_2_, null);
		}
	}
}