package net.minecraft.world.storage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.CraftServer;
import org.ultramine.server.util.GlobalExecutors;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.StartupQuery;
import cpw.mods.fml.common.registry.FMLControlledNamespacedRegistry;
import cpw.mods.fml.common.registry.GameData;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.MinecraftException;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.storage.IChunkLoader;

public class SaveHandler implements ISaveHandler, IPlayerFileData {
	private static final Logger logger = LogManager.getLogger();
	private final File worldDirectory;
	private final File playersDirectory;
	private final File mapDataDir;
	private final long initializationTime = MinecraftServer.getSystemTimeMillis();
	private final String saveDirectoryName;
	private static final String __OBFID = "CL_00000585";

	public SaveHandler(File p_i2146_1_, String p_i2146_2_, boolean p_i2146_3_) {
		worldDirectory = new File(p_i2146_1_, p_i2146_2_);
		worldDirectory.mkdirs();
		playersDirectory = new File(worldDirectory, "playerdata");
		mapDataDir = new File(worldDirectory, "data");
		mapDataDir.mkdirs();
		saveDirectoryName = p_i2146_2_;

		if (p_i2146_3_) {
			playersDirectory.mkdirs();
		}

		setSessionLock();
	}

	protected void setSessionLock() {
		try {
			File file1 = new File(worldDirectory, "session.lock");
			DataOutputStream dataoutputstream = new DataOutputStream(new FileOutputStream(file1));

			try {
				dataoutputstream.writeLong(initializationTime);
			} finally {
				dataoutputstream.close();
			}
		} catch (IOException ioexception) {
			ioexception.printStackTrace();
			throw new RuntimeException("Failed to check session lock, aborting");
		}
	}

	@Override
	public File getWorldDirectory() {
		return worldDirectory;
	}

	@Override
	public void checkSessionLock() throws MinecraftException {
		try {
			File file1 = new File(worldDirectory, "session.lock");
			DataInputStream datainputstream = new DataInputStream(new FileInputStream(file1));

			try {
				if (datainputstream.readLong() != initializationTime)
					throw new MinecraftException("The save is being accessed from another location, aborting");
			} finally {
				datainputstream.close();
			}
		} catch (IOException ioexception) {
			throw new MinecraftException("Failed to check session lock, aborting");
		}
	}

	@Override
	public IChunkLoader getChunkLoader(WorldProvider p_75763_1_) {
		throw new RuntimeException("Old Chunk Storage is no longer supported.");
	}

	@Override
	public WorldInfo loadWorldInfo() {
		File file1 = new File(worldDirectory, "level.dat");
		NBTTagCompound nbttagcompound;
		NBTTagCompound nbttagcompound1;

		WorldInfo worldInfo = null;

		if (file1.exists()) {
			try {
				nbttagcompound = CompressedStreamTools.readCompressed(new FileInputStream(file1));
				nbttagcompound1 = nbttagcompound.getCompoundTag("Data");
				worldInfo = new WorldInfo(nbttagcompound1);
				FMLCommonHandler.instance().handleWorldDataLoad(this, worldInfo, nbttagcompound);
				initBukkitData();
				return worldInfo;
			} catch (StartupQuery.AbortedException e) {
				throw e;
			} catch (Exception exception1) {
				exception1.printStackTrace();
			}
		}

		FMLCommonHandler.instance().confirmBackupLevelDatUse(this);
		file1 = new File(worldDirectory, "level.dat_old");

		if (file1.exists()) {
			try {
				nbttagcompound = CompressedStreamTools.readCompressed(new FileInputStream(file1));
				nbttagcompound1 = nbttagcompound.getCompoundTag("Data");
				worldInfo = new WorldInfo(nbttagcompound1);
				FMLCommonHandler.instance().handleWorldDataLoad(this, worldInfo, nbttagcompound);
				initBukkitData();
				return worldInfo;
			} catch (StartupQuery.AbortedException e) {
				throw e;
			} catch (Exception exception) {
				exception.printStackTrace();
			}
		}
		initBukkitData();
		return null;
	}

	@Override
	public void saveWorldInfoWithPlayer(WorldInfo p_75755_1_, NBTTagCompound p_75755_2_) {
		NBTTagCompound nbttagcompound1 = p_75755_1_.cloneNBTCompound(p_75755_2_);
		final NBTTagCompound nbttagcompound2 = new NBTTagCompound();
		nbttagcompound2.setTag("Data", nbttagcompound1);

		FMLCommonHandler.instance().handleWorldDataSave(this, p_75755_1_, nbttagcompound2);

		GlobalExecutors.writingIO().execute(new Runnable() {
			@Override
			public void run() {
				try {
					File file1 = new File(worldDirectory, "level.dat_new");
					File file2 = new File(worldDirectory, "level.dat_old");
					File file3 = new File(worldDirectory, "level.dat");
					CompressedStreamTools.writeCompressed(nbttagcompound2, new FileOutputStream(file1));

					if (file2.exists()) {
						file2.delete();
					}

					file3.renameTo(file2);

					if (file3.exists()) {
						file3.delete();
					}

					file1.renameTo(file3);

					if (file1.exists()) {
						file1.delete();
					}
				} catch (Exception exception) {
					exception.printStackTrace();
				}
			}
		});
	}

	@Override
	public void saveWorldInfo(WorldInfo p_75761_1_) {
		NBTTagCompound nbttagcompound = p_75761_1_.getNBTTagCompound();
		NBTTagCompound nbttagcompound1 = new NBTTagCompound();
		nbttagcompound1.setTag("Data", nbttagcompound);

		FMLCommonHandler.instance().handleWorldDataSave(this, p_75761_1_, nbttagcompound1);

		try {
			File file1 = new File(worldDirectory, "level.dat_new");
			File file2 = new File(worldDirectory, "level.dat_old");
			File file3 = new File(worldDirectory, "level.dat");
			CompressedStreamTools.writeCompressed(nbttagcompound1, new FileOutputStream(file1));

			if (file2.exists()) {
				file2.delete();
			}

			file3.renameTo(file2);

			if (file3.exists()) {
				file3.delete();
			}

			file1.renameTo(file3);

			if (file1.exists()) {
				file1.delete();
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	@Override
	public void writePlayerData(EntityPlayer p_75753_1_) {
		try {
			NBTTagCompound nbttagcompound = new NBTTagCompound();
			p_75753_1_.writeToNBT(nbttagcompound);
			File file1 = new File(playersDirectory, p_75753_1_.getUniqueID().toString() + ".dat.tmp");
			File file2 = new File(playersDirectory, p_75753_1_.getUniqueID().toString() + ".dat");
			CompressedStreamTools.writeCompressed(nbttagcompound, new FileOutputStream(file1));

			if (file2.exists()) {
				file2.delete();
			}

			file1.renameTo(file2);
			net.minecraftforge.event.ForgeEventFactory.firePlayerSavingEvent(p_75753_1_, playersDirectory,
					p_75753_1_.getUniqueID().toString());
		} catch (Exception exception) {
			logger.warn("Failed to save player data for " + p_75753_1_.getCommandSenderName());
		}
	}

	@Override
	public NBTTagCompound readPlayerData(EntityPlayer p_75752_1_) {
		NBTTagCompound nbttagcompound = null;

		try {
			File file1 = new File(playersDirectory, p_75752_1_.getUniqueID().toString() + ".dat");

			if (file1.exists() && file1.isFile()) {
				nbttagcompound = CompressedStreamTools.readCompressed(new FileInputStream(file1));
			}
		} catch (Exception exception) {
			logger.warn("Failed to load player data for " + p_75752_1_.getCommandSenderName());
		}

		if (nbttagcompound != null) {
			p_75752_1_.readFromNBT(nbttagcompound);
		}

		net.minecraftforge.event.ForgeEventFactory.firePlayerLoadingEvent(p_75752_1_, playersDirectory,
				p_75752_1_.getUniqueID().toString());
		return nbttagcompound;
	}

	@Override
	public IPlayerFileData getSaveHandler() {
		return this;
	}

	@Override
	public String[] getAvailablePlayerDat() {
		String[] astring = playersDirectory.list();

		for (int i = 0; i < astring.length; ++i) {
			if (astring[i].endsWith(".dat")) {
				astring[i] = astring[i].substring(0, astring[i].length() - 4);
			}
		}

		return astring;
	}

	@Override
	public void flush() {
	}

	@Override
	public File getMapFileFromName(String p_75758_1_) {
		return new File(mapDataDir, p_75758_1_ + ".dat");
	}

	@Override
	public String getWorldDirectoryName() {
		return saveDirectoryName;
	}

	public NBTTagCompound getPlayerNBT(EntityPlayerMP player) {
		try {
			File file1 = new File(playersDirectory, player.getUniqueID().toString() + ".dat");

			if (file1.exists() && file1.isFile())
				return CompressedStreamTools.readCompressed(new FileInputStream(file1));
		} catch (Exception exception) {
			logger.warn("Failed to load player data for " + player.getCommandSenderName());
		}
		return null;
	}

	public File getPlayerSaveDir() {
		return playersDirectory;
	}

	public UUID getUUID() {
		if (uuid != null)
			return uuid;

		File file1 = new File(worldDirectory, "uid.dat");

		if (file1.exists()) {
			DataInputStream dis = null;

			try {
				dis = new DataInputStream(new FileInputStream(file1));
				return uuid = new UUID(dis.readLong(), dis.readLong());
			} catch (IOException ex) {
				logger.warn("Failed to read " + file1 + ", generating new random UUID", ex);
			} finally {
				if (dis != null) {
					try {
						dis.close();
					} catch (IOException ex) {
						// NOOP
					}
				}
			}
		}

		uuid = UUID.randomUUID();

		DataOutputStream dos = null;
		try {
			dos = new DataOutputStream(new FileOutputStream(file1));
			dos.writeLong(uuid.getMostSignificantBits());
			dos.writeLong(uuid.getLeastSignificantBits());
		} catch (IOException ex) {
			logger.warn("Failed to write " + file1, ex);
		} finally {
			if (dos != null) {
				try {
					dos.close();
				} catch (IOException ex) {
					// NOOP
				}
			}
		}

		return uuid;
	}

	private void initBukkitData() {
		// inject bukkit materials before plugins load
		if (!initializedBukkit) {
			injectBlockBukkitMaterials();
			injectItemBukkitMaterials();
			// since we modify bukkit enums, we need to guarantee that plugins are
			// loaded after all mods have been loaded by FML to avoid race conditions.
			((CraftServer) Bukkit.getServer()).loadPlugins();
			((CraftServer) Bukkit.getServer()).enablePlugins(org.bukkit.plugin.PluginLoadOrder.STARTUP);
			initializedBukkit = true;
		}
	}

	@SuppressWarnings("deprecation")
	private static void injectItemBukkitMaterials() {
		FMLControlledNamespacedRegistry<Item> itemRegistry = GameData.getItemRegistry();
		List<Integer> ids = new ArrayList<>();

		for (Item thing : itemRegistry.typeSafeIterable()) {
			ids.add(itemRegistry.getId(thing));
		}

		// sort by id
		Collections.sort(ids);

		for (int id : ids) {
			Item item = itemRegistry.getRaw(id);
			// inject item materials into Bukkit for FML
			org.bukkit.Material material = org.bukkit.Material.addMaterial(id, itemRegistry.getNameForObject(item),
					false);
			if (material != null) {
				FMLLog.fine("Injected new Forge item material %s with ID %d.", material.name(), material.getId());
			}
		}
	}

	@SuppressWarnings("deprecation")
	private static void injectBlockBukkitMaterials() {
		FMLControlledNamespacedRegistry<Block> blockRegistry = GameData.getBlockRegistry();
		List<Integer> ids = new ArrayList<>();

		for (Block block : blockRegistry.typeSafeIterable()) {
			ids.add(blockRegistry.getId(block));
		}

		// sort by id
		Collections.sort(ids);

		for (int id : ids) {
			Block block = blockRegistry.getRaw(id);
			// inject block materials into Bukkit for FML
			org.bukkit.Material material = org.bukkit.Material.addMaterial(id, blockRegistry.getNameForObject(block),
					true);
			if (material != null) {
				FMLLog.fine("Injected new Forge block material %s with ID %d.", material.name(), material.getId());
			}
		}
	}

	private UUID uuid = null; // CraftBukkit
	private static boolean initializedBukkit = false; // Cauldron
}
