package net.minecraft.world.chunk.storage;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import cpw.mods.fml.common.FMLLog;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.EbsSaveFakeNbt;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.MinecraftException;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.storage.IThreadedFileIO;
import net.minecraft.world.storage.ThreadedFileIOBase;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.openhft.koloboke.collect.map.IntObjCursor;
import net.openhft.koloboke.collect.map.IntObjMap;
import net.openhft.koloboke.collect.map.hash.HashIntObjMaps;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.ultramine.server.chunk.ChunkHash;
import org.ultramine.server.chunk.PendingBlockUpdate;
import org.ultramine.server.util.VanillaChunkCoordIntPairSet;

import javax.annotation.Nullable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class AnvilChunkLoader implements IChunkLoader, IThreadedFileIO {
	private static final Logger logger = LogManager.getLogger();
	protected final IntObjMap<PendingChunk> pendingSaves = HashIntObjMaps.newMutableMap();
	private final List<PendingChunk> chunksToRemoveUm = new ArrayList<>();
	protected final Object syncLockObject = new Object();
	public File chunkSaveLocation;
	private final ConcurrentMap<ChunkCoordIntPair, PendingChunk> savingChunks = new ConcurrentHashMap<>();

	public AnvilChunkLoader(File par1File) {
		chunkSaveLocation = par1File;
	}

	public boolean chunkExists(World world, int i, int j) {
		synchronized (syncLockObject) {
			if (pendingSaves.containsKey(ChunkHash.chunkToKey(i, j)))
				return true;
		}
		if (this.getSavingChunk(new ChunkCoordIntPair(i, j)) != null) {
			return true;
		}
		return isChunkExistsInFile(i, j);
	}

	protected boolean isChunkExistsInFile(int cx, int cz) {
		return RegionFileCache.createOrLoadRegionFile(chunkSaveLocation, cx, cz).chunkExists(cx & 31, cz & 31);
	}

	@Override
	public Chunk loadChunk(World par1World, int par2, int par3) throws IOException {
		Object[] data = loadChunk__Async(par1World, par2, par3);

		if (data != null) {
			Chunk chunk = (Chunk) data[0];
			NBTTagCompound nbttagcompound = (NBTTagCompound) data[1];
			loadEntities(par1World, nbttagcompound.getCompoundTag("Level"), chunk);
			MinecraftForge.EVENT_BUS.post(new ChunkDataEvent.Load(chunk, nbttagcompound));
			releaseNbt(nbttagcompound);
			return chunk;
		}

		return null;
	}

	public Object[] loadChunk__Async(World par1World, int par2, int par3) {
		NBTTagCompound nbttagcompound = null;

		synchronized (syncLockObject) {

			PendingChunk anvilchunkloaderpending = pendingSaves.get(ChunkHash.chunkToKey(par2, par3));

			if (anvilchunkloaderpending != null) {
				nbttagcompound = anvilchunkloaderpending.nbtTags;

				retainNbt(nbttagcompound);
			}
		}

		if (nbttagcompound == null) {
			DataInputStream datainputstream = getChunkInputStream(par2, par3);

			if (datainputstream == null)
				return null;

			try {
				nbttagcompound = CompressedStreamTools.read(datainputstream);
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}

		if (nbttagcompound == null) {
			final AnvilChunkLoader.PendingChunk savingChunk = this.getSavingChunk(new ChunkCoordIntPair(par2, par3));
			if (savingChunk != null) {
				nbttagcompound = savingChunk.nbtTags;
			}
		}

		if (nbttagcompound == null)
			return null;
		Chunk chunk = checkedReadChunkFromNBT(par1World, par2, par3, nbttagcompound);
		if (chunk == null)
			return null;

		Object[] data = new Object[2];
		data[0] = chunk;
		data[1] = nbttagcompound;

		return data;
	}

	protected DataInputStream getChunkInputStream(int cz, int cx) {
		return RegionFileCache.getChunkInputStream(chunkSaveLocation, cz, cx);
	}

	protected Chunk checkedReadChunkFromNBT(World par1World, int par2, int par3, NBTTagCompound par4NBTTagCompound) {
		if (!par4NBTTagCompound.hasKey("Level", 10)) {
			logger.error("Chunk file at " + par2 + "," + par3 + " is missing level data, skipping");
			return null;
		} else if (!par4NBTTagCompound.getCompoundTag("Level").hasKey("Sections", 9)) {
			logger.error("Chunk file at " + par2 + "," + par3 + " is missing block data, skipping");
			return null;
		} else {
			Chunk chunk = readChunkFromNBT(par1World, par4NBTTagCompound.getCompoundTag("Level"));

			if (!chunk.isAtLocation(par2, par3)) {
				logger.error("Chunk file at " + par2 + "," + par3 + " is in the wrong location; relocating. (Expected "
						+ par2 + ", " + par3 + ", got " + chunk.xPosition + ", " + chunk.zPosition + ")");
				par4NBTTagCompound.setInteger("xPos", par2);
				par4NBTTagCompound.setInteger("zPos", par3);
				// Have to move tile entities since we don't load them at this stage
				NBTTagList tileEntities = par4NBTTagCompound.getCompoundTag("Level").getTagList("TileEntities", 10);

				if (tileEntities != null) {
					for (int te = 0; te < tileEntities.tagCount(); te++) {
						NBTTagCompound tileEntity = tileEntities.getCompoundTagAt(te);
						int x = tileEntity.getInteger("x") - chunk.xPosition * 16;
						int z = tileEntity.getInteger("z") - chunk.zPosition * 16;
						tileEntity.setInteger("x", par2 * 16 + x);
						tileEntity.setInteger("z", par3 * 16 + z);
					}
				}

				chunk = readChunkFromNBT(par1World, par4NBTTagCompound.getCompoundTag("Level"));
			}
			return chunk;
		}
	}

	@Override
	public void saveChunk(World par1World, Chunk par2Chunk) throws MinecraftException, IOException {
		par1World.checkSessionLock();

		boolean prevValue = NBTTagCompound.setUseKolobokeMap(true);
		try {
			NBTTagCompound nbttagcompound = new NBTTagCompound();
			NBTTagCompound nbttagcompound1 = new NBTTagCompound(13);
			nbttagcompound.setTag("Level", nbttagcompound1);
			writeChunkToNBT(par2Chunk, par1World, nbttagcompound1);
			MinecraftForge.EVENT_BUS.post(new ChunkDataEvent.Save(par2Chunk, nbttagcompound));
			addChunkToPending(par2Chunk.getChunkCoordIntPair(), nbttagcompound);
		} catch (Exception exception) {
			exception.printStackTrace();
		} finally {
			NBTTagCompound.setUseKolobokeMap(prevValue);
		}
	}

	protected void addChunkToPending(ChunkCoordIntPair par1ChunkCoordIntPair, NBTTagCompound par2NBTTagCompound) {
		synchronized (syncLockObject) {
			int hash = ChunkHash.chunkToKey(par1ChunkCoordIntPair.chunkXPos, par1ChunkCoordIntPair.chunkZPos);

			PendingChunk pendingChunk = new PendingChunk(par1ChunkCoordIntPair, par2NBTTagCompound);
			if (pendingSaves.put(hash, pendingChunk) != null) {
				for (int i = 0, s = chunksToRemoveUm.size(); i < s; i++)
					if (chunksToRemoveUm.get(i).chunkCoordinate.equals(par1ChunkCoordIntPair)) {
						chunksToRemoveUm.set(i, pendingChunk);
						return;
					}
			}
			chunksToRemoveUm.add(pendingChunk);
			// this.pendingAnvilChunksCoordinates.add(par1ChunkCoordIntPair);
			ThreadedFileIOBase.threadedIOInstance.queueIO(this);
		}
	}

	@Override
	public boolean writeNextIO() {
		AnvilChunkLoader.PendingChunk pendingchunk = null;
		int key;

		synchronized (syncLockObject) {
			if (pendingSaves.isEmpty())
				return false;

			IntObjCursor<PendingChunk> it = pendingSaves.cursor();
			it.moveNext();
			pendingchunk = it.value();
			this.savingChunks.put(pendingchunk.chunkCoordinate, pendingchunk);
			key = it.key();
		}

		if (pendingchunk != null) {
			try {
				writeChunkNBTTags(pendingchunk);
			} catch (Exception exception) {
				exception.printStackTrace();
			}

			synchronized (syncLockObject) {
				pendingSaves.remove(key);
				chunksToRemoveUm.remove(pendingchunk);
			}

			// release only after remove from lists (may be reread by other threads)
			this.savingChunks.remove(pendingchunk.chunkCoordinate);
			releaseNbt(pendingchunk.nbtTags);
		}

		return true;
	}

	protected void writeChunkNBTTags(AnvilChunkLoader.PendingChunk par1AnvilChunkLoaderPending) throws IOException {
		DataOutputStream dataoutputstream = getChunkOutputStream(par1AnvilChunkLoaderPending);
		CompressedStreamTools.write(par1AnvilChunkLoaderPending.nbtTags, dataoutputstream);
		dataoutputstream.close();
	}

	protected DataOutputStream getChunkOutputStream(AnvilChunkLoader.PendingChunk pending) {
		return RegionFileCache.getChunkOutputStream(chunkSaveLocation, pending.chunkCoordinate.chunkXPos,
				pending.chunkCoordinate.chunkZPos);
	}

	@Override
	public void saveExtraChunkData(World par1World, Chunk par2Chunk) {
	}

	@Override
	public void chunkTick() {
	}

	@Override
	public void saveExtraData() {
		// Async only
		// while (this.writeNextIO())
		// {
		// ;
		// }
	}

	@SuppressWarnings("rawtypes")
	protected void writeChunkToNBT(Chunk par1Chunk, World par2World, NBTTagCompound par3NBTTagCompound) {
		par3NBTTagCompound.setByte("V", (byte) 1);
		par3NBTTagCompound.setInteger("xPos", par1Chunk.xPosition);
		par3NBTTagCompound.setInteger("zPos", par1Chunk.zPosition);
		par3NBTTagCompound.setLong("LastUpdate", par2World.getTotalWorldTime());
		par3NBTTagCompound.setIntArray("HeightMap", par1Chunk.heightMap);
		par3NBTTagCompound.setBoolean("TerrainPopulated", par1Chunk.isTerrainPopulated);
		par3NBTTagCompound.setBoolean("LightPopulated", par1Chunk.isLightPopulated);
		par3NBTTagCompound.setLong("InhabitedTime", par1Chunk.inhabitedTime);
		ExtendedBlockStorage[] aextendedblockstorage = par1Chunk.getBlockStorageArray();
		NBTTagList nbttaglist = new NBTTagList();
		boolean flag = !par2World.provider.hasNoSky;
		ExtendedBlockStorage[] aextendedblockstorage1 = aextendedblockstorage;
		int i = aextendedblockstorage.length;
		NBTTagCompound nbttagcompound1;

		for (int j = 0; j < i; ++j) {
			ExtendedBlockStorage extendedblockstorage = aextendedblockstorage1[j];

			if (extendedblockstorage != null) {
				nbttaglist.appendTag(new EbsSaveFakeNbt(extendedblockstorage.copy(), !flag));
			}
		}

		par3NBTTagCompound.setTag("Sections", nbttaglist);
		par3NBTTagCompound.setByteArray("Biomes", par1Chunk.getBiomeArray());
		par1Chunk.hasEntities = false;
		NBTTagList nbttaglist2 = new NBTTagList();
		Iterator iterator1;

		for (i = 0; i < par1Chunk.entityLists.length; ++i) {
			iterator1 = par1Chunk.entityLists[i].iterator();

			while (iterator1.hasNext()) {
				Entity entity = (Entity) iterator1.next();
				nbttagcompound1 = new NBTTagCompound(34);

				try {
					if (entity.writeToNBTOptional(nbttagcompound1)) {
						par1Chunk.hasEntities = true;
						nbttaglist2.appendTag(nbttagcompound1);
					}
				} catch (Exception e) {
					FMLLog.log(Level.ERROR, e,
							"An Entity type %s has thrown an exception trying to write state. It will not persist. Report this to the mod author",
							entity.getClass().getName());
				}
			}
		}

		par3NBTTagCompound.setTag("Entities", nbttaglist2);
		NBTTagList nbttaglist3 = new NBTTagList(par1Chunk.chunkTileEntityMap.size());
		iterator1 = par1Chunk.chunkTileEntityMap.values().iterator();

		while (iterator1.hasNext()) {
			TileEntity tileentity = (TileEntity) iterator1.next();
			nbttagcompound1 = new NBTTagCompound(7);
			try {
				tileentity.writeToNBT(nbttagcompound1);
				nbttaglist3.appendTag(nbttagcompound1);
			} catch (Exception e) {
				FMLLog.log(Level.ERROR, e,
						"A TileEntity type %s has throw an exception trying to write state. It will not persist. Report this to the mod author",
						tileentity.getClass().getName());
			}
		}

		par3NBTTagCompound.setTag("TileEntities", nbttaglist3);

		Set<PendingBlockUpdate> set = par1Chunk.getPendingUpdatesForSave();
		if (set != null) {
			int x = par1Chunk.xPosition << 4;
			int z = par1Chunk.zPosition << 4;

			long k = par2World.getTotalWorldTime();
			NBTTagList nbttaglist1 = new NBTTagList(set.size());

			for (PendingBlockUpdate p : set) {
				NBTTagCompound nbttagcompound2 = new NBTTagCompound(6);
				nbttagcompound2.setInteger("i", Block.getIdFromBlock(p.getBlock()));
				nbttagcompound2.setInteger("x", x + p.x);
				nbttagcompound2.setInteger("y", p.y);
				nbttagcompound2.setInteger("z", z + p.z);
				nbttagcompound2.setInteger("t", (int) (p.scheduledTime - k));
				nbttagcompound2.setInteger("p", p.priority);
				nbttaglist1.appendTag(nbttagcompound2);
			}

			par3NBTTagCompound.setTag("TileTicks", nbttaglist1);
		}
	}

	protected Chunk readChunkFromNBT(World par1World, NBTTagCompound par2NBTTagCompound) {
		int i = par2NBTTagCompound.getInteger("xPos");
		int j = par2NBTTagCompound.getInteger("zPos");
		Chunk chunk = new Chunk(par1World, i, j);
		chunk.heightMap = par2NBTTagCompound.getIntArray("HeightMap");
		chunk.isTerrainPopulated = par2NBTTagCompound.getBoolean("TerrainPopulated");
		chunk.isLightPopulated = par2NBTTagCompound.getBoolean("LightPopulated");
		chunk.inhabitedTime = par2NBTTagCompound.getLong("InhabitedTime");
		NBTTagList nbttaglist = par2NBTTagCompound.getTagList("Sections", 10);
		byte b0 = 16;
		ExtendedBlockStorage[] aextendedblockstorage = new ExtendedBlockStorage[b0];
		boolean flag = !par1World.provider.hasNoSky;

		for (int k = 0; k < nbttaglist.tagCount(); ++k) {
			NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(k);
			if (nbttagcompound1 instanceof EbsSaveFakeNbt) {
				ExtendedBlockStorage ebs = ((EbsSaveFakeNbt) nbttagcompound1).getEbs().copy();
				aextendedblockstorage[ebs.getYLocation() >> 4 & 255] = ebs;
				continue;
			}
			byte b1 = nbttagcompound1.getByte("Y");
			ExtendedBlockStorage extendedblockstorage = new ExtendedBlockStorage(b1 << 4, flag, false);
			byte[] lsb = nbttagcompound1.getByteArray("Blocks");
			byte[] msb;

			if (nbttagcompound1.hasKey("Add", 7)) {
				msb = nbttagcompound1.getByteArray("Add");
			} else {
				msb = null;
			}

			byte[] meta = nbttagcompound1.getByteArray("Data");
			byte[] blockLight = nbttagcompound1.getByteArray("BlockLight");
			byte[] skyLight;

			if (flag) {
				skyLight = nbttagcompound1.getByteArray("SkyLight");
			} else {
				skyLight = null;
			}

			extendedblockstorage.getSlot().setData(lsb, msb, meta, blockLight, skyLight);

			extendedblockstorage.removeInvalidBlocks();
			aextendedblockstorage[b1] = extendedblockstorage;
		}

		chunk.setStorageArrays(aextendedblockstorage);

		if (par2NBTTagCompound.hasKey("Biomes", 7)) {
			chunk.setBiomeArray(par2NBTTagCompound.getByteArray("Biomes"));
		}

		// End this method here and split off entity loading to another method
		return chunk;
	}

	public void loadEntities(World par1World, NBTTagCompound par2NBTTagCompound, Chunk chunk) {
		NBTTagList nbttaglist1 = par2NBTTagCompound.getTagList("Entities", 10);

		if (nbttaglist1 != null) {
			for (int l = 0; l < nbttaglist1.tagCount(); ++l) {
				NBTTagCompound nbttagcompound3 = nbttaglist1.getCompoundTagAt(l);
				Entity entity2 = EntityList.createEntityFromNBT(nbttagcompound3, par1World);
				chunk.hasEntities = true;

				if (entity2 != null) {
					chunk.addEntity(entity2);
					Entity entity = entity2;

					for (NBTTagCompound nbttagcompound2 = nbttagcompound3; nbttagcompound2.hasKey("Riding",
							10); nbttagcompound2 = nbttagcompound2.getCompoundTag("Riding")) {
						Entity entity1 = EntityList.createEntityFromNBT(nbttagcompound2.getCompoundTag("Riding"),
								par1World);

						if (entity1 != null) {
							chunk.addEntity(entity1);
							entity.mountEntity(entity1);
						}

						entity = entity1;
					}
				}
			}
		}

		NBTTagList nbttaglist2 = par2NBTTagCompound.getTagList("TileEntities", 10);

		if (nbttaglist2 != null) {
			for (int i1 = 0; i1 < nbttaglist2.tagCount(); ++i1) {
				NBTTagCompound nbttagcompound4 = nbttaglist2.getCompoundTagAt(i1);
				TileEntity tileentity = TileEntity.createAndLoadEntity(nbttagcompound4);

				if (tileentity != null) {
					chunk.addTileEntity(tileentity);
				}
			}
		}

		if (par2NBTTagCompound.hasKey("TileTicks", 9)) {
			NBTTagList nbttaglist3 = par2NBTTagCompound.getTagList("TileTicks", 10);

			if (nbttaglist3 != null) {
				long time = par1World.getTotalWorldTime();
				for (int j1 = 0; j1 < nbttaglist3.tagCount(); ++j1) {
					NBTTagCompound nbttagcompound5 = nbttaglist3.getCompoundTagAt(j1);
					chunk.scheduleBlockUpdate(new PendingBlockUpdate(nbttagcompound5.getInteger("x") & 15,
							nbttagcompound5.getInteger("y"), nbttagcompound5.getInteger("z") & 15,
							Block.getBlockById(nbttagcompound5.getInteger("i")), nbttagcompound5.getInteger("t") + time,
							nbttagcompound5.getInteger("p")), false);
				}
			}
		}

		chunk.isModified = false;
	}

	public int getSaveQueueSize() {
		synchronized (syncLockObject) {
			return pendingSaves.size();
		}
	}

	public void unsafeRemoveAll() {
		synchronized (syncLockObject) {
			pendingSaves.clear();
			chunksToRemoveUm.clear();
		}
	}

	public static void releaseNbt(NBTTagCompound root) {
		NBTTagList sections = root.getCompoundTag("Level").getTagList("Sections", 10);
		for (int i = 0; i < sections.tagCount(); i++) {
			NBTTagCompound nbt = sections.getCompoundTagAt(i);
			if (nbt instanceof EbsSaveFakeNbt) {
				((EbsSaveFakeNbt) nbt).release();
			}
		}
	}

	private static void retainNbt(NBTTagCompound root) {
		NBTTagList sections = root.getCompoundTag("Level").getTagList("Sections", 10);
		for (int i = 0; i < sections.tagCount(); i++) {
			NBTTagCompound nbt = sections.getCompoundTagAt(i);
			if (nbt instanceof EbsSaveFakeNbt) {
				((EbsSaveFakeNbt) nbt).retain();
			}
		}
	}

	protected static class PendingChunk {
		public final ChunkCoordIntPair chunkCoordinate;
		public final NBTTagCompound nbtTags;
		public PendingChunk(ChunkCoordIntPair par1ChunkCoordIntPair, NBTTagCompound par2NBTTagCompound) {
			chunkCoordinate = par1ChunkCoordIntPair;
			nbtTags = par2NBTTagCompound;
		}
	}

	private AnvilChunkLoader.PendingChunk getSavingChunk(final ChunkCoordIntPair chunkCoordIntPair) {
		final AnvilChunkLoader.PendingChunk pendingChunk = this.savingChunks.get(chunkCoordIntPair);
		return (pendingChunk != null && pendingChunk.chunkCoordinate.equals(chunkCoordIntPair)) ? pendingChunk : null;
	}
}