package net.minecraft.client.multiplayer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.util.LongHashMap;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.EmptyChunk;
import net.minecraft.world.chunk.IChunkProvider;

@SideOnly(Side.CLIENT)
public class ChunkProviderClient implements IChunkProvider {
	private static final Logger logger = LogManager.getLogger();
	private Chunk blankChunk;
	private LongHashMap chunkMapping = new LongHashMap();
	private List chunkListing = new ArrayList();
	private World worldObj;
	private static final String __OBFID = "CL_00000880";

	public ChunkProviderClient(World p_i1184_1_) {
		blankChunk = new EmptyChunk(p_i1184_1_, 0, 0);
		worldObj = p_i1184_1_;
	}

	@Override
	public boolean chunkExists(int p_73149_1_, int p_73149_2_) {
		return true;
	}

	public void unloadChunk(int p_73234_1_, int p_73234_2_) {
		Chunk chunk = provideChunk(p_73234_1_, p_73234_2_);

		if (!chunk.isEmpty()) {
			chunk.onChunkUnload();
			chunk.release();
		}

		chunkMapping.remove(ChunkCoordIntPair.chunkXZ2Int(p_73234_1_, p_73234_2_));
		chunkListing.remove(chunk);
	}

	@Override
	public Chunk loadChunk(int p_73158_1_, int p_73158_2_) {
		long key = ChunkCoordIntPair.chunkXZ2Int(p_73158_1_, p_73158_2_);
		Chunk old = (Chunk) chunkMapping.getValueByKey(key);
		if (old != null) {
			old.onChunkUnload();
			old.release();
			chunkMapping.remove(key);
			chunkListing.remove(old);
		}
		Chunk chunk = new Chunk(worldObj, p_73158_1_, p_73158_2_);
		chunkMapping.add(key, chunk);
		chunkListing.add(chunk);
		net.minecraftforge.common.MinecraftForge.EVENT_BUS
				.post(new net.minecraftforge.event.world.ChunkEvent.Load(chunk));
		chunk.isChunkLoaded = true;
		return chunk;
	}

	@Override
	public Chunk provideChunk(int p_73154_1_, int p_73154_2_) {
		Chunk chunk = (Chunk) chunkMapping.getValueByKey(ChunkCoordIntPair.chunkXZ2Int(p_73154_1_, p_73154_2_));
		return chunk == null ? blankChunk : chunk;
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
		long i = System.currentTimeMillis();
		Iterator iterator = chunkListing.iterator();

		while (iterator.hasNext()) {
			Chunk chunk = (Chunk) iterator.next();
			chunk.func_150804_b(System.currentTimeMillis() - i > 5L);
		}

		if (System.currentTimeMillis() - i > 100L) {
			logger.info("Warning: Clientside chunk ticking took {} ms",
					new Object[] { Long.valueOf(System.currentTimeMillis() - i) });
		}

		return false;
	}

	@Override
	public boolean canSave() {
		return false;
	}

	@Override
	public void populate(IChunkProvider p_73153_1_, int p_73153_2_, int p_73153_3_) {
	}

	@Override
	public String makeString() {
		return "MultiplayerChunkCache: " + chunkMapping.getNumHashElements() + ", " + chunkListing.size();
	}

	@Override
	public List getPossibleCreatures(EnumCreatureType p_73155_1_, int p_73155_2_, int p_73155_3_, int p_73155_4_) {
		return null;
	}

	@Override
	public ChunkPosition func_147416_a(World p_147416_1_, String p_147416_2_, int p_147416_3_, int p_147416_4_,
			int p_147416_5_) {
		return null;
	}

	@Override
	public int getLoadedChunkCount() {
		return chunkListing.size();
	}

	@Override
	public void recreateStructures(int p_82695_1_, int p_82695_2_) {
	}

	public void free() {
		for (Object o : chunkListing) {
			((Chunk) o).release();
		}
		chunkListing.clear();
	}
}