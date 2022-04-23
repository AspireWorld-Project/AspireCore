package net.minecraft.world;

import com.mojang.authlib.GameProfile;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEventData;
import net.minecraft.block.material.Material;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityTracker;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.INpc;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.network.play.server.*;
import net.minecraft.profiler.Profiler;
import net.minecraft.scoreboard.ScoreboardSaveData;
import net.minecraft.scoreboard.ServerScoreboard;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraft.world.chunk.storage.IChunkLoader;
import net.minecraft.world.gen.ChunkProviderServer;
import net.minecraft.world.gen.feature.WorldGeneratorBonusChest;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraftforge.common.ChestGenHooks;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.world.WorldEvent;
import net.openhft.koloboke.collect.map.IntByteCursor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Bukkit;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.CraftTravelAgent;
import org.bukkit.entity.LightningStrike;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.weather.LightningStrikeEvent;
import org.bukkit.event.weather.ThunderChangeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.ultramine.server.WorldBorder;
import org.ultramine.server.WorldsConfig.WorldConfig;
import org.ultramine.server.WorldsConfig.WorldConfig.Settings.WorldTime;
import org.ultramine.server.chunk.ChunkHash;
import org.ultramine.server.chunk.PendingBlockUpdate;
import org.ultramine.server.event.ServerWorldEventProxy;
import org.ultramine.server.event.WorldUpdateObjectType;
import org.ultramine.server.mobspawn.MobSpawnManager;
import org.ultramine.server.util.BasicTypeParser;

import java.io.File;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static net.minecraftforge.common.ChestGenHooks.BONUS_CHEST;

public class WorldServer extends World {
	private static final Logger logger = LogManager.getLogger();
	private final MinecraftServer mcServer;
	private final EntityTracker theEntityTracker;
	private final PlayerManager thePlayerManager;
	private Set pendingTickListEntriesHashSet;
	private TreeSet pendingTickListEntriesTreeSet;
	public ChunkProviderServer theChunkProviderServer;
	public boolean levelSaving;
	private boolean allPlayersSleeping;
	private int updateEntityTick;
	public Teleporter worldTeleporter;
	private final SpawnerAnimals animalSpawner = new SpawnerAnimals();
	private WorldServer.ServerBlockEventList[] field_147490_S = new WorldServer.ServerBlockEventList[] {
			new WorldServer.ServerBlockEventList(null), new WorldServer.ServerBlockEventList(null) };
	private int blockEventCacheIndex;
	public static final WeightedRandomChestContent[] bonusChestContent = new WeightedRandomChestContent[] {
			new WeightedRandomChestContent(Items.stick, 0, 1, 3, 10),
			new WeightedRandomChestContent(Item.getItemFromBlock(Blocks.planks), 0, 1, 3, 10),
			new WeightedRandomChestContent(Item.getItemFromBlock(Blocks.log), 0, 1, 3, 10),
			new WeightedRandomChestContent(Items.stone_axe, 0, 1, 1, 3),
			new WeightedRandomChestContent(Items.wooden_axe, 0, 1, 1, 5),
			new WeightedRandomChestContent(Items.stone_pickaxe, 0, 1, 1, 3),
			new WeightedRandomChestContent(Items.wooden_pickaxe, 0, 1, 1, 5),
			new WeightedRandomChestContent(Items.apple, 0, 2, 3, 5),
			new WeightedRandomChestContent(Items.bread, 0, 2, 3, 3),
			new WeightedRandomChestContent(Item.getItemFromBlock(Blocks.log2), 0, 1, 3, 10) };
	private List pendingTickListEntriesThisTick = new ArrayList();
	private IntHashMap entityIdMap;
	private static final String __OBFID = "CL_00001437";

	public List<Teleporter> customTeleporters = new ArrayList<>();

	public WorldServer(MinecraftServer p_i45284_1_, ISaveHandler p_i45284_2_, String p_i45284_3_, int p_i45284_4_,
			WorldSettings p_i45284_5_, Profiler p_i45284_6_) {
		super(p_i45284_2_, p_i45284_3_, p_i45284_5_, WorldProvider.getProviderForDimension(p_i45284_4_), p_i45284_6_);
		mcServer = p_i45284_1_;
		theEntityTracker = new EntityTracker(this);
		thePlayerManager = new PlayerManager(this);

		if (entityIdMap == null) {
			entityIdMap = new IntHashMap();
		}

		if (pendingTickListEntriesHashSet == null) {
			pendingTickListEntriesHashSet = new HashSet();
		}

		if (pendingTickListEntriesTreeSet == null) {
			pendingTickListEntriesTreeSet = new TreeSet();
		}

		worldTeleporter = new Teleporter(this);
		worldScoreboard = new ServerScoreboard(p_i45284_1_);
		ScoreboardSaveData scoreboardsavedata = (ScoreboardSaveData) mapStorage.loadData(ScoreboardSaveData.class,
				"scoreboard");

		if (scoreboardsavedata == null) {
			scoreboardsavedata = new ScoreboardSaveData();
			mapStorage.setData("scoreboard", scoreboardsavedata);
		}

		if (provider.dimensionId == 0) // Forge: We fix the global mapStorage, which causes us to share scoreboards
										// early. So don't associate the save data with the temporary scoreboard
		{
			scoreboardsavedata.func_96499_a(worldScoreboard);
		}
		((ServerScoreboard) worldScoreboard).func_96547_a(scoreboardsavedata);
		DimensionManager.setWorld(p_i45284_4_, this);
		worldTeleporter = new CraftTravelAgent(this);
	}

	// Add env and gen to constructor
	public WorldServer(MinecraftServer p_i45284_1_, ISaveHandler p_i45284_2_, String p_i45284_3_, int p_i45284_4_, WorldSettings p_i45284_5_,
					   Profiler p_i45284_6_, org.bukkit.World.Environment env, org.bukkit.generator.ChunkGenerator gen)
	{
		super(p_i45284_2_, p_i45284_3_, p_i45284_5_, WorldProvider.getProviderForDimension(p_i45284_4_), p_i45284_6_, gen, env);
		// CraftBukkit end
		this.mcServer = p_i45284_1_;
		this.theEntityTracker = new EntityTracker(this);
		thePlayerManager = new PlayerManager(this);

		if (this.entityIdMap == null)
		{
			this.entityIdMap = new IntHashMap();
		}

		if (this.pendingTickListEntriesHashSet == null)
		{
			this.pendingTickListEntriesHashSet = Collections.synchronizedSet(new LinkedHashSet());
		}

		if (this.pendingTickListEntriesTreeSet == null)
		{
			this.pendingTickListEntriesTreeSet = (new TreeSet());
		}

		this.worldTeleporter = new org.bukkit.craftbukkit.CraftTravelAgent(this); // CraftBukkit
		this.worldScoreboard = new ServerScoreboard(p_i45284_1_);
		ScoreboardSaveData scoreboardsavedata = (ScoreboardSaveData) this.mapStorage.loadData(ScoreboardSaveData.class, "scoreboard");

		if (scoreboardsavedata == null)
		{
			scoreboardsavedata = new ScoreboardSaveData();
			this.mapStorage.setData("scoreboard", scoreboardsavedata);
		}

		if (!(this instanceof WorldServerMulti)) //Forge: We fix the global mapStorage, which causes us to share scoreboards early. So don't associate the save data with the temporary scoreboard
		{
			scoreboardsavedata.func_96499_a(this.worldScoreboard);
		}
		((ServerScoreboard)this.worldScoreboard).func_96547_a(scoreboardsavedata);
		DimensionManager.setWorld(p_i45284_4_, this);
	}

    @Override
	public void tick() {
		super.tick();

		if (getWorldInfo().isHardcoreModeEnabled() && difficultySetting != EnumDifficulty.HARD) {
			difficultySetting = EnumDifficulty.HARD;
		}

		provider.worldChunkMgr.cleanupCache();

		if (areAllPlayersAsleep()) {
			if (getGameRules().getGameRuleBooleanValue("doDaylightCycle")) {
				long i = worldInfo.getWorldTime() + 24000L;
				worldInfo.setWorldTime(i - i % 24000L);
			}

			wakeAllPlayers();
		}

		theProfiler.startSection("mobSpawner");

		if (getGameRules().getGameRuleBooleanValue("doMobSpawning")) {
			if (isServer && mobSpawner != null) {
				mobSpawner.performSpawn(spawnHostileMobs, spawnPeacefulMobs, worldInfo.getWorldTotalTime());
			} else {
				animalSpawner.findChunksForSpawning(this, spawnHostileMobs, spawnPeacefulMobs,
						worldInfo.getWorldTotalTime() % 400L == 0L);
			}
		}

		theProfiler.endStartSection("chunkSource");
		chunkProvider.unloadQueuedChunks();
		int j = calculateSkylightSubtracted(1.0F);

		if (j != skylightSubtracted) {
			skylightSubtracted = j;
		}

		worldInfo.incrementTotalWorldTime(worldInfo.getWorldTotalTime() + 1L);

		WorldTime time = getConfig().settings.time;
		long curTime = worldInfo.getWorldTime() % 24000;

		if (time == WorldTime.DAY && curTime > 10000) {
			worldInfo.setWorldTime(worldInfo.getWorldTime() - curTime + 24000 + 1000);
		}
		if (time == WorldTime.NIGHT && (curTime < 14200 || curTime > 21800)) {
			worldInfo.setWorldTime(worldInfo.getWorldTime() - curTime + 24000 + 14200);
		}

		if (time != WorldTime.FIXED && getGameRules().getGameRuleBooleanValue("doDaylightCycle")) {
			worldInfo.setWorldTime(worldInfo.getWorldTime() + 1L);
		}

		theProfiler.endStartSection("tickPending");
		tickUpdates(false);
		theProfiler.endStartSection("tickBlocks");
		func_147456_g();
		theProfiler.endStartSection("chunkMap");
		thePlayerManager.updatePlayerInstances();
		theProfiler.endStartSection("village");
		villageCollectionObj.tick();
		villageSiegeObj.tick();
		theProfiler.endStartSection("portalForcer");
		worldTeleporter.removeStalePortalLocations(getTotalWorldTime());
		for (Teleporter tele : customTeleporters) {
			tele.removeStalePortalLocations(getTotalWorldTime());
		}
		theProfiler.endSection();
		func_147488_Z();
	}

	public BiomeGenBase.SpawnListEntry spawnRandomCreature(EnumCreatureType p_73057_1_, int p_73057_2_, int p_73057_3_,
			int p_73057_4_) {
		List list = getChunkProvider().getPossibleCreatures(p_73057_1_, p_73057_2_, p_73057_3_, p_73057_4_);
		list = ForgeEventFactory.getPotentialSpawns(this, p_73057_1_, p_73057_2_, p_73057_3_, p_73057_4_, list);
		return list != null && !list.isEmpty() ? (BiomeGenBase.SpawnListEntry) WeightedRandom.getRandomItem(rand, list)
				: null;
	}

	@Override
	public void updateAllPlayersSleepingFlag() {
		allPlayersSleeping = !playerEntities.isEmpty();
		Iterator iterator = playerEntities.iterator();

		while (iterator.hasNext()) {
			EntityPlayer entityplayer = (EntityPlayer) iterator.next();

			if (!entityplayer.isPlayerSleeping()) {
				allPlayersSleeping = false;
				break;
			}
		}
	}

	protected void wakeAllPlayers() {
		allPlayersSleeping = false;
		Iterator iterator = playerEntities.iterator();

		while (iterator.hasNext()) {
			EntityPlayer entityplayer = (EntityPlayer) iterator.next();

			if (entityplayer.isPlayerSleeping()) {
				entityplayer.wakeUpPlayer(false, false, true);
			}
		}

		resetRainAndThunder();
	}

	private void resetRainAndThunder() {
		WeatherChangeEvent weather = new WeatherChangeEvent(this.getWorld(), false);
		Bukkit.getPluginManager().callEvent(weather);
		ThunderChangeEvent thunder = new ThunderChangeEvent(this.getWorld(), false);
		Bukkit.getPluginManager().callEvent(thunder);
		if (!weather.isCancelled()) {
			worldInfo.setRainTime(0);
			worldInfo.setRaining(false);
		}
		if (!thunder.isCancelled()) {
			worldInfo.setThunderTime(0);
			worldInfo.setThundering(false);
		}
		if (!weather.isCancelled() && !thunder.isCancelled()) {
			provider.resetRainAndThunder(); // Maybe, WorldProvider#worldObj.worldInfo != this.worldInfo
		}
	}

	public boolean areAllPlayersAsleep() {
		if (allPlayersSleeping && !isRemote) {
			Iterator iterator = playerEntities.iterator();
			EntityPlayer entityplayer;

			do {
				if (!iterator.hasNext())
					return true;

				entityplayer = (EntityPlayer) iterator.next();
			} while (entityplayer.isPlayerFullyAsleep());

			return false;
		} else
			return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void setSpawnLocation() {
		if (worldInfo.getSpawnY() <= 0) {
			worldInfo.setSpawnY(64);
		}

		int i = worldInfo.getSpawnX();
		int j = worldInfo.getSpawnZ();
		int k = 0;

		while (getTopBlock(i, j).getMaterial() == Material.air) {
			i += rand.nextInt(8) - rand.nextInt(8);
			j += rand.nextInt(8) - rand.nextInt(8);
			++k;

			if (k == 10000) {
				break;
			}
		}

		worldInfo.setSpawnX(i);
		worldInfo.setSpawnZ(j);
	}

	@Override
	protected void func_147456_g() {

		super.func_147456_g();
		// int i = 0;
		// int j = 0;

		Random random = ThreadLocalRandom.current();
		for (IntByteCursor iter = activeChunks.cursor(); iter.moveNext();) {
			int chunkCoord = iter.key();
			int chunkX = ChunkHash.keyToX(chunkCoord);
			int chunkZ = ChunkHash.keyToZ(chunkCoord);
			int priority = iter.value();
			int blocksPerChunk = priority < 3 ? 3 : priority < 5 ? 2 : 1;
			int k = chunkX << 4;
			int l = chunkZ << 4;

			theProfiler.startSection("getChunk");
			Chunk chunk = getChunkFromChunkCoords(chunkX, chunkZ);
			chunk.setActive();
			chunkProfiler.startChunk(chunkCoord);
			theProfiler.endStartSection("updatePending");
			updatePendingOf(chunk);
			func_147467_a(k, l, chunk);
			theProfiler.endStartSection("tickChunk");
			chunk.func_150804_b(false);
			theProfiler.endStartSection("thunder");
			int i1;
			int j1;
			int k1;
			int l1;

			if (provider.canDoLightning(chunk) && random.nextInt(100000) == 0 && isRaining() && isThundering()) {
				updateLCG = updateLCG * 3 + 1013904223;
				i1 = updateLCG >> 2;
				j1 = k + (i1 & 15);
				k1 = l + (i1 >> 8 & 15);
				l1 = getPrecipitationHeight(j1, k1);

				if (canLightningStrikeAt(j1, l1, k1)) {
					addWeatherEffect(new EntityLightningBolt(this, j1, l1, k1));
				}
			}

			theProfiler.endStartSection("iceandsnow");

			if (provider.canDoRainSnowIce(chunk) && random.nextInt(16) == 0) {
				getEventProxy().pushState(WorldUpdateObjectType.WEATHER);
				updateLCG = updateLCG * 3 + 1013904223;
				i1 = updateLCG >> 2;
				j1 = i1 & 15;
				k1 = i1 >> 8 & 15;
				l1 = getPrecipitationHeight(j1 + k, k1 + l);

				if (isBlockFreezableNaturally(j1 + k, l1 - 1, k1 + l)) {
					BlockState blockState = this.getWorld().getBlockAt(j1 + k, l1 - 1, k1 + l).getState();
					blockState.setTypeId(Block.getIdFromBlock(Blocks.ice));
					BlockFormEvent iceBlockForm = new BlockFormEvent(blockState.getBlock(), blockState);
					this.getServer().getPluginManager().callEvent(iceBlockForm);
					if (!iceBlockForm.isCancelled()) {
						blockState.update(true);
					}
				}

				if (isRaining() && func_147478_e(j1 + k, l1, k1 + l, true)) {
					BlockState blockState = this.getWorld().getBlockAt(j1 + k, l1, k1 + l).getState();
					blockState.setTypeId(Block.getIdFromBlock(Blocks.snow_layer));
					BlockFormEvent snow = new BlockFormEvent(blockState.getBlock(), blockState);
					this.getServer().getPluginManager().callEvent(snow);
					if (!snow.isCancelled()) {
						blockState.update(true);
					}
				}

				if (isRaining()) {
					BiomeGenBase biomegenbase = getBiomeGenForCoords(j1 + k, k1 + l);

					if (biomegenbase.canSpawnLightningBolt()) {
						getBlock(j1 + k, l1 - 1, k1 + l).fillWithRain(this, j1 + k, l1 - 1, k1 + l);
					}
				}
				getEventProxy().popState();
			}

			theProfiler.endStartSection("tickBlocks");
			ExtendedBlockStorage[] aextendedblockstorage = chunk.getBlockStorageArray();
			j1 = aextendedblockstorage.length;

			getEventProxy().pushState(WorldUpdateObjectType.BLOCK_RANDOM);
			for (k1 = 0; k1 < j1; ++k1) {
				ExtendedBlockStorage extendedblockstorage = aextendedblockstorage[k1];

				if (extendedblockstorage != null && extendedblockstorage.getNeedsRandomTick()) {
					for (int i3 = 0; i3 < blocksPerChunk; ++i3) {
						updateLCG = updateLCG * 3 + 1013904223;
						int i2 = updateLCG >> 2;
						int j2 = i2 & 15;
						int k2 = i2 >> 8 & 15;
						int l2 = i2 >> 16 & 15;
						// ++j;
						Block block = extendedblockstorage.getBlockByExtId(j2, l2, k2);

						if (block.getTickRandomly()) {
							// ++i;
							getEventProxy().startBlock(block, j2 + k, l2 + extendedblockstorage.getYLocation(), k2 + l);
							block.updateTick(this, j2 + k, l2 + extendedblockstorage.getYLocation(), k2 + l, rand);
						}
					}
				}
			}
			getEventProxy().popState();

			chunkProfiler.endChunk();
			theProfiler.endSection();
		}
	}

	@Override
	public boolean isBlockTickScheduledThisTick(int p_147477_1_, int p_147477_2_, int p_147477_3_, Block p_147477_4_) {
		// Это не заглушка. Этот метод может использоваться модами по назначению, все
		// работает правильно.
		return false;
	}

	@Override
	public void scheduleBlockUpdate(int p_147464_1_, int p_147464_2_, int p_147464_3_, Block p_147464_4_,
			int p_147464_5_) {
		scheduleBlockUpdateWithPriority(p_147464_1_, p_147464_2_, p_147464_3_, p_147464_4_, p_147464_5_, 0);
	}

	@Override
	public void scheduleBlockUpdateWithPriority(int x, int y, int z, Block block, int time, int priority) {
		// NextTickListEntry nextticklistentry = new NextTickListEntry(x, y, z, block);
		// Keeping here as a note for future when it may be restored.
		// boolean isForced = getPersistentChunks().containsKey(new
		// ChunkCoordIntPair(nextticklistentry.xCoord >> 4, nextticklistentry.zCoord >>
		// 4));
		// byte b0 = isForced ? 0 : 8;
		byte b0 = 0;

		if (scheduledUpdatesAreImmediate && block.getMaterial() != Material.air) {
			if (block.func_149698_L()) {
				b0 = 8;

				if (checkChunksExist(x - b0, y - b0, z - b0, x + b0, y + b0, z + b0)) {
					Block block1 = getBlock(x, y, z);

					if (block1.getMaterial() != Material.air && block1 == block) {
						block1.updateTick(this, x, y, z, rand);
					}
				}

				return;
			}

			time = 1;
		}

		Chunk chunk = getChunkIfExists(x >> 4, z >> 4);

		if (chunk != null) {
			PendingBlockUpdate p = new PendingBlockUpdate(x & 15, y, z & 15, block,
					worldInfo.getWorldTotalTime() + time, priority);
			p.initiator = getEventProxy().getObjectOwner();
			chunk.scheduleBlockUpdate(p, true);
		}
	}

	@Override
	public void func_147446_b(int p_147446_1_, int p_147446_2_, int p_147446_3_, Block p_147446_4_, int p_147446_5_,
			int p_147446_6_) {
		// Данный метод вызывался только при загрузке чанка. Для совместимости с модами,
		// которые неправильно используют этот метод, пытаемся запланировать обновление.
		Chunk chunk = getChunkIfExists(p_147446_1_ >> 4, p_147446_3_ >> 4);

		if (chunk != null) {
			PendingBlockUpdate p = new PendingBlockUpdate(p_147446_1_ & 15, p_147446_2_, p_147446_3_ & 15, p_147446_4_,
					worldInfo.getWorldTotalTime() + p_147446_5_, p_147446_6_);
			chunk.scheduleBlockUpdate(p, true);
		}
	}

	@Override
	public void updateEntities() {
		if (playerEntities.isEmpty()
				&& (getPersistentChunks().isEmpty() || !getConfig().chunkLoading.enableChunkLoaders)) {
			if (updateEntityTick++ >= 1200) {
				processEntityUnload();
				processTileEntityUnload();
				return;
			}
		} else {
			resetUpdateEntityTick();
		}

		super.updateEntities();
	}

	public void resetUpdateEntityTick() {
		updateEntityTick = 0;
	}

	@Override
	public boolean tickUpdates(boolean p_72955_1_) {
		// Выполнение отложенных обновлений перенесено в updatePendingOf(Chunk)
		return false;
	}

	@Override
	@SuppressWarnings("rawtypes")
	public List getPendingBlockUpdates(Chunk p_72920_1_, boolean p_72920_2_) {
		// Данный метод вызывался только при сохранении чанка. Выполняем преобразование
		// для совместимости с модами
		Set<PendingBlockUpdate> set = p_72920_1_.getPendingUpdatesForSave();
		if (set == null)
			return null;
		int xadd = p_72920_1_.xPosition << 4;
		int zadd = p_72920_1_.zPosition << 4;
		List<NextTickListEntry> list = new ArrayList<>(set.size());
		for (PendingBlockUpdate pbu : set) {
			NextTickListEntry ent = new NextTickListEntry(pbu.x + xadd, pbu.y, pbu.z + zadd, pbu.getBlock());
			ent.setScheduledTime(pbu.scheduledTime).setPriority(pbu.priority);
			list.add(ent);
		}
		return list;
	}

	@Override
	public void updateEntityWithOptionalForce(Entity p_72866_1_, boolean p_72866_2_) {
		if (!getConfig().mobSpawn.allowAnimals
				&& (p_72866_1_ instanceof EntityAnimal || p_72866_1_ instanceof EntityWaterMob)) {
			p_72866_1_.setDead();
		}

		if (!getConfig().mobSpawn.allowNPCs && p_72866_1_ instanceof INpc) {
			p_72866_1_.setDead();
		}

		super.updateEntityWithOptionalForce(p_72866_1_, p_72866_2_);
	}

	@Override
	protected IChunkProvider createChunkProvider() {
		IChunkLoader ichunkloader = saveHandler.getChunkLoader(provider);
		theChunkProviderServer = new ChunkProviderServer(this, ichunkloader, provider.createChunkGenerator());
		return theChunkProviderServer;
	}

	public List func_147486_a(int p_147486_1_, int p_147486_2_, int p_147486_3_, int p_147486_4_, int p_147486_5_,
			int p_147486_6_) {
		ArrayList arraylist = new ArrayList();

		for (int x = p_147486_1_ >> 4; x <= p_147486_4_ >> 4; x++) {
			for (int z = p_147486_3_ >> 4; z <= p_147486_6_ >> 4; z++) {
				Chunk chunk = getChunkFromChunkCoords(x, z);
				if (chunk != null) {
					for (Object obj : chunk.chunkTileEntityMap.values()) {
						TileEntity entity = (TileEntity) obj;
						if (!entity.isInvalid()) {
							if (entity.xCoord >= p_147486_1_ && entity.yCoord >= p_147486_2_
									&& entity.zCoord >= p_147486_3_ && entity.xCoord <= p_147486_4_
									&& entity.yCoord <= p_147486_5_ && entity.zCoord <= p_147486_6_) {
								arraylist.add(entity);
							}
						}
					}
				}
			}
		}

		return arraylist;
	}

	@Override
	public boolean canMineBlock(EntityPlayer p_72962_1_, int p_72962_2_, int p_72962_3_, int p_72962_4_) {
		return super.canMineBlock(p_72962_1_, p_72962_2_, p_72962_3_, p_72962_4_);
	}

	@Override
	public boolean canMineBlockBody(EntityPlayer par1EntityPlayer, int par2, int par3, int par4) {
		return !mcServer.isBlockProtected(this, par2, par3, par4, par1EntityPlayer);
	}

	@Override
	protected void initialize(WorldSettings p_72963_1_) {
		if (entityIdMap == null) {
			entityIdMap = new IntHashMap();
		}

		if (pendingTickListEntriesHashSet == null) {
			pendingTickListEntriesHashSet = new HashSet();
		}

		if (pendingTickListEntriesTreeSet == null) {
			pendingTickListEntriesTreeSet = new TreeSet();
		}

		setConfig(MinecraftServer.getServer().getMultiWorld().getConfigByID(provider.dimensionId));
		createSpawnPosition(p_72963_1_);
		super.initialize(p_72963_1_);
	}

	protected void createSpawnPosition(WorldSettings p_73052_1_) {
		if (!provider.canRespawnHere()) {
			worldInfo.setSpawnPosition(0, provider.getAverageGroundLevel(), 0);
		} else {
			if (net.minecraftforge.event.ForgeEventFactory.onCreateWorldSpawn(this, p_73052_1_))
				return;
			findingSpawnPoint = true;
			WorldChunkManager worldchunkmanager = provider.worldChunkMgr;
			List list = worldchunkmanager.getBiomesToSpawnIn();
			Random random = new Random(getSeed());
			ChunkPosition chunkposition = worldchunkmanager.findBiomePosition(0, 0, 256, list, random);
			int i = 0;
			provider.getAverageGroundLevel();
			int k = 0;

			if (chunkposition != null) {
				i = chunkposition.chunkPosX;
				k = chunkposition.chunkPosZ;
			} else {
				logger.warn("Unable to find spawn biome");
			}

			int l = 0;

			while (!provider.canCoordinateBeSpawn(i, k)) {
				i += random.nextInt(64) - random.nextInt(64);
				k += random.nextInt(64) - random.nextInt(64);
				++l;

				if (l == 1000) {
					break;
				}
			}

			worldInfo.setSpawnPosition(i, getHeightValue(i, k), k);
			findingSpawnPoint = false;

			if (p_73052_1_.isBonusChestEnabled()) {
				createBonusChest();
			}
		}
	}

	protected void createBonusChest() {
		WorldGeneratorBonusChest worldgeneratorbonuschest = new WorldGeneratorBonusChest(
				ChestGenHooks.getItems(BONUS_CHEST, rand), ChestGenHooks.getCount(BONUS_CHEST, rand));

		for (int i = 0; i < 10; ++i) {
			int j = worldInfo.getSpawnX() + rand.nextInt(6) - rand.nextInt(6);
			int k = worldInfo.getSpawnZ() + rand.nextInt(6) - rand.nextInt(6);
			int l = getTopSolidOrLiquidBlock(j, k) + 1;

			if (worldgeneratorbonuschest.generate(this, rand, j, l, k)) {
				break;
			}
		}
	}

	public ChunkCoordinates getEntrancePortalLocation() {
		return provider.getEntrancePortalLocation();
	}

	public void saveAllChunks(boolean p_73044_1_, IProgressUpdate p_73044_2_) throws MinecraftException {
		if (chunkProvider.canSave()) {
			if (p_73044_2_ != null) {
				p_73044_2_.displayProgressMessage("Saving level");
			}

			saveLevel();

			if (p_73044_2_ != null) {
				p_73044_2_.resetProgresAndWorkingMessage("Saving chunks");
			}

			chunkProvider.saveChunks(p_73044_1_, p_73044_2_);
			MinecraftForge.EVENT_BUS.postWithProfile(theProfiler, new WorldEvent.Save(this));
		}
	}

	public void saveChunkData() {
		if (chunkProvider.canSave()) {
			chunkProvider.saveExtraData();
		}
	}

	protected void saveLevel() throws MinecraftException {
		checkSessionLock();
		saveHandler.saveWorldInfoWithPlayer(worldInfo, mcServer.getConfigurationManager().getHostPlayerData());
		mapStorage.saveAllData();
		perWorldStorage.saveAllData();
	}

	@Override
	public void onEntityAdded(Entity p_72923_1_) {
		super.onEntityAdded(p_72923_1_);
		entityIdMap.addKey(p_72923_1_.getEntityId(), p_72923_1_);
		Entity[] aentity = p_72923_1_.getParts();

		if (aentity != null) {
			for (int i = 0; i < aentity.length; ++i) {
				entityIdMap.addKey(aentity[i].getEntityId(), aentity[i]);
			}
		}
	}

	@Override
	public void onEntityRemoved(Entity p_72847_1_) {
		super.onEntityRemoved(p_72847_1_);
		entityIdMap.removeObject(p_72847_1_.getEntityId());
		Entity[] aentity = p_72847_1_.getParts();

		if (aentity != null) {
			for (int i = 0; i < aentity.length; ++i) {
				entityIdMap.removeObject(aentity[i].getEntityId());
			}
		}
	}

	@Override
	public Entity getEntityByID(int p_73045_1_) {
		return (Entity) entityIdMap.lookup(p_73045_1_);
	}

	@Override
	public boolean addWeatherEffect(Entity p_72942_1_) {
		if (p_72942_1_ instanceof EntityLightningBolt) {
			LightningStrikeEvent lightning = new LightningStrikeEvent(this.getWorld(),
					(LightningStrike) p_72942_1_.getBukkitEntity());
			Bukkit.getPluginManager().callEvent(lightning);
			if (lightning.isCancelled())
				return false;
		}

		if (super.addWeatherEffect(p_72942_1_)) {
			mcServer.getConfigurationManager().sendToAllNear(p_72942_1_.posX, p_72942_1_.posY, p_72942_1_.posZ, 512.0D,
					provider.dimensionId, new S2CPacketSpawnGlobalEntity(p_72942_1_));
			return true;
		} else
			return false;
	}

	@Override
	public void setEntityState(Entity p_72960_1_, byte p_72960_2_) {
		getEntityTracker().func_151248_b(p_72960_1_, new S19PacketEntityStatus(p_72960_1_, p_72960_2_));
	}

	@Override
	public Explosion newExplosion(Entity p_72885_1_, double p_72885_2_, double p_72885_4_, double p_72885_6_,
			float p_72885_8_, boolean p_72885_9_, boolean p_72885_10_) {
		Explosion explosion = new Explosion(this, p_72885_1_, p_72885_2_, p_72885_4_, p_72885_6_, p_72885_8_);
		explosion.isFlaming = p_72885_9_;
		explosion.isSmoking = p_72885_10_;
		if (net.minecraftforge.event.ForgeEventFactory.onExplosionStart(this, explosion))
			return explosion;
		explosion.doExplosionA();
		explosion.doExplosionB(false);

		if (!p_72885_10_) {
			explosion.affectedBlockPositions.clear();
		}

		Iterator iterator = playerEntities.iterator();

		while (iterator.hasNext()) {
			EntityPlayer entityplayer = (EntityPlayer) iterator.next();

			if (entityplayer.getDistanceSq(p_72885_2_, p_72885_4_, p_72885_6_) < 4096.0D) {
				((EntityPlayerMP) entityplayer).playerNetServerHandler
						.sendPacket(new S27PacketExplosion(p_72885_2_, p_72885_4_, p_72885_6_, p_72885_8_,
								explosion.affectedBlockPositions, (Vec3) explosion.func_77277_b().get(entityplayer)));
			}
		}

		return explosion;
	}

	@Override
	public void addBlockEvent(int p_147452_1_, int p_147452_2_, int p_147452_3_, Block p_147452_4_, int p_147452_5_,
			int p_147452_6_) {
		BlockEventData blockeventdata = new BlockEventData(p_147452_1_, p_147452_2_, p_147452_3_, p_147452_4_,
				p_147452_5_, p_147452_6_);
		blockeventdata.initiator = getEventProxy().getObjectOwner();
		Iterator iterator = field_147490_S[blockEventCacheIndex].iterator();
		BlockEventData blockeventdata1;

		do {
			if (!iterator.hasNext()) {
				field_147490_S[blockEventCacheIndex].add(blockeventdata);
				return;
			}

			blockeventdata1 = (BlockEventData) iterator.next();
		} while (!blockeventdata1.equals(blockeventdata));
	}

	private void func_147488_Z() {
		getEventProxy().pushState(WorldUpdateObjectType.BLOCK_EVENT);
		while (!field_147490_S[blockEventCacheIndex].isEmpty()) {
			int i = blockEventCacheIndex;
			blockEventCacheIndex ^= 1;
			Iterator iterator = field_147490_S[i].iterator();

			while (iterator.hasNext()) {
				BlockEventData blockeventdata = (BlockEventData) iterator.next();
				Block block = getBlockIfExists(blockeventdata.func_151340_a(), blockeventdata.func_151342_b(),
						blockeventdata.func_151341_c());
				if (block == blockeventdata.getBlock()) {
					getEventProxy().startBlock(block, blockeventdata.func_151340_a(), blockeventdata.func_151342_b(),
							blockeventdata.func_151341_c(), blockeventdata.initiator);
				}

				if (func_147485_a(blockeventdata)) {
					mcServer.getConfigurationManager().sendToAllNear(blockeventdata.func_151340_a(),
							blockeventdata.func_151342_b(), blockeventdata.func_151341_c(), 64.0D, provider.dimensionId,
							new S24PacketBlockAction(blockeventdata.func_151340_a(), blockeventdata.func_151342_b(),
									blockeventdata.func_151341_c(), blockeventdata.getBlock(),
									blockeventdata.getEventID(), blockeventdata.getEventParameter()));
				}
			}

			field_147490_S[i].clear();
		}
		getEventProxy().popState();
	}

	private boolean func_147485_a(BlockEventData p_147485_1_) {
		Block block = getBlockIfExists(p_147485_1_.func_151340_a(), p_147485_1_.func_151342_b(),
				p_147485_1_.func_151341_c());
		return block == p_147485_1_.getBlock()
				? block.onBlockEventReceived(this, p_147485_1_.func_151340_a(), p_147485_1_.func_151342_b(),
						p_147485_1_.func_151341_c(), p_147485_1_.getEventID(), p_147485_1_.getEventParameter())
				: false;
	}

	public void flush() {
		saveHandler.flush();
	}

	@Override
	protected void updateWeather() {
		boolean flag = isRaining();

		switch (getConfig().settings.weather) {
		case NONE:
			if (flag) {
				worldInfo.setRainTime(12300);
				worldInfo.setThunderTime(12300);
				worldInfo.setRaining(false);
				worldInfo.setThundering(false);
				prevRainingStrength = rainingStrength = 0F;
				prevThunderingStrength = thunderingStrength = 0F;
			}
			break;
		case THUNDER:
			worldInfo.setThunderTime(12300);
			worldInfo.setThundering(true);
		case RAIN:
			worldInfo.setRainTime(12300);
			worldInfo.setRaining(true);
		case NORMAL:
			super.updateWeather();
		}

		if (prevRainingStrength != rainingStrength) {
			mcServer.getConfigurationManager().sendPacketToAllPlayersInDimension(
					new S2BPacketChangeGameState(7, rainingStrength), provider.dimensionId);
		}

		if (prevThunderingStrength != thunderingStrength) {
			mcServer.getConfigurationManager().sendPacketToAllPlayersInDimension(
					new S2BPacketChangeGameState(8, thunderingStrength), provider.dimensionId);
		}

		/*
		 * The function in use here has been replaced in order to only send the weather
		 * info to players in the correct dimension, rather than to all players on the
		 * server. This is what causes the client-side rain, as the client believes that
		 * it has started raining locally, rather than in another dimension.
		 */
		if (flag != isRaining()) {
			if (flag) {
				mcServer.getConfigurationManager()
						.sendPacketToAllPlayersInDimension(new S2BPacketChangeGameState(2, 0.0F), provider.dimensionId);
			} else {
				mcServer.getConfigurationManager()
						.sendPacketToAllPlayersInDimension(new S2BPacketChangeGameState(1, 0.0F), provider.dimensionId);
			}

			mcServer.getConfigurationManager().sendPacketToAllPlayersInDimension(
					new S2BPacketChangeGameState(7, rainingStrength), provider.dimensionId);
			mcServer.getConfigurationManager().sendPacketToAllPlayersInDimension(
					new S2BPacketChangeGameState(8, thunderingStrength), provider.dimensionId);
		}
	}

	@Override
	protected int func_152379_p() {
		return config.chunkLoading.chunkActivateRadius;
	}

	public MinecraftServer func_73046_m() {
		return mcServer;
	}

	public EntityTracker getEntityTracker() {
		return theEntityTracker;
	}

	public PlayerManager getPlayerManager() {
		return thePlayerManager;
	}

	public Teleporter getDefaultTeleporter() {
		return worldTeleporter;
	}

	public void func_147487_a(String p_147487_1_, double p_147487_2_, double p_147487_4_, double p_147487_6_,
			int p_147487_8_, double p_147487_9_, double p_147487_11_, double p_147487_13_, double p_147487_15_) {
		S2APacketParticles s2apacketparticles = new S2APacketParticles(p_147487_1_, (float) p_147487_2_,
				(float) p_147487_4_, (float) p_147487_6_, (float) p_147487_9_, (float) p_147487_11_,
				(float) p_147487_13_, (float) p_147487_15_, p_147487_8_);

		for (int j = 0; j < playerEntities.size(); ++j) {
			EntityPlayerMP entityplayermp = (EntityPlayerMP) playerEntities.get(j);
			ChunkCoordinates chunkcoordinates = entityplayermp.getPlayerCoordinates();
			double d7 = p_147487_2_ - chunkcoordinates.posX;
			double d8 = p_147487_4_ - chunkcoordinates.posY;
			double d9 = p_147487_6_ - chunkcoordinates.posZ;
			double d10 = d7 * d7 + d8 * d8 + d9 * d9;

			if (d10 <= 256.0D) {
				entityplayermp.playerNetServerHandler.sendPacket(s2apacketparticles);
			}
		}
	}

	public File getChunkSaveLocation() {
		return ((AnvilChunkLoader) theChunkProviderServer.currentChunkLoader).chunkSaveLocation;
	}

	static class ServerBlockEventList extends ArrayList {
		private static final String __OBFID = "CL_00001439";

		private ServerBlockEventList() {
		}

		ServerBlockEventList(Object p_i1521_1_) {
			this();
		}
	}

	/*
	 * ======================================== ULTRAMINE START
	 * =====================================
	 */

	private static final boolean isServer = FMLCommonHandler.instance().getSide().isServer();
	private WorldConfig config;
	@SideOnly(Side.SERVER)
	private WorldBorder border;
	@SideOnly(Side.SERVER)
	private MobSpawnManager mobSpawner;

	@Override
	public void checkSessionLock() throws MinecraftException {
		// Removes world lock checking on server
		if (!isServer) {
			super.checkSessionLock();
		}
	}

	@Override
	public Chunk getChunkIfExists(int cx, int cz) {
		return theChunkProviderServer.getChunkIfExists(cx, cz);
	}

	public void updatePendingOf(Chunk chunk) {
		long time = worldInfo.getWorldTotalTime();
		int x = chunk.xPosition << 4;
		int z = chunk.zPosition << 4;

		getEventProxy().pushState(WorldUpdateObjectType.BLOCK_PENDING);
		PendingBlockUpdate p;
		while ((p = chunk.pollPending(time)) != null) {
			updateBlock(x + p.x, p.y, z + p.z, p.getBlock(), p.initiator);
		}
		getEventProxy().popState();
	}

	private void updateBlock(int x, int y, int z, Block block1, GameProfile initiator) {
		Block block = getBlock(x, y, z);
		getEventProxy().startBlock(block, x, y, z, initiator);

		if (block.getMaterial() != Material.air && Block.isEqualTo(block, block1)) {
			try {
				block.updateTick(this, x, y, z, rand);
			} catch (Throwable throwable1) {
				CrashReport crashreport = CrashReport.makeCrashReport(throwable1, "Exception while ticking a block");
				CrashReportCategory crashreportcategory = crashreport.makeCategory("Block being ticked");
				int k;

				try {
					k = getBlockMetadata(x, y, z);
				} catch (Throwable throwable) {
					k = -1;
				}

				CrashReportCategory.func_147153_a(crashreportcategory, x, y, z, block, k);
				throw new ReportedException(crashreport);
			}
		}
	}

	public void setConfig(WorldConfig config) {
		this.config = config;
		if (isServer) {
			applyConfig();
		}
	}

	@SideOnly(Side.SERVER)
	public void applyConfig() {
		difficultySetting = BasicTypeParser.parseDifficulty(config.settings.difficulty);
		setAllowedSpawnTypes(config.mobSpawn.spawnMonsters, config.mobSpawn.spawnAnimals);
		getGameRules().setOrCreateGameRule("doDaylightCycle",
				Boolean.toString(config.settings.time != WorldTime.FIXED));
		getGameRules().setOrCreateGameRule("doMobSpawning",
				Boolean.toString(config.mobSpawn.spawnEngine != WorldConfig.MobSpawn.MobSpawnEngine.NONE));

		if (config.mobSpawn.spawnEngine == WorldConfig.MobSpawn.MobSpawnEngine.NEW) {
			if (mobSpawner == null) {
				mobSpawner = new MobSpawnManager(this);
			}
			mobSpawner.configure(config);
		}

		border = new WorldBorder(config.borders);
		eventProxy = new ServerWorldEventProxy(this);
	}

	public WorldConfig getConfig() {
		return config;
	}

	public int getViewDistance() {
		return isServer ? config.chunkLoading.viewDistance : mcServer.getConfigurationManager().getViewDistance();
	}

	@Override
	protected boolean isChunkLoaderEnabled() {
		return config.chunkLoading.enableChunkLoaders;
	}

	@SideOnly(Side.SERVER)
	public WorldBorder getBorder() {
		return border;
	}

	public void saveOtherData() {
		try {
			saveLevel();
		} catch (MinecraftException ignored) {
		}
		MinecraftForge.EVENT_BUS.postWithProfile(theProfiler, new WorldEvent.Save(this));
	}

	@Override
	public boolean spawnEntityInWorld(Entity entity) {
		return this.addEntity(entity, CreatureSpawnEvent.SpawnReason.DEFAULT);
	}

	@Override
	public boolean real_spawnEntityInWorld(Entity entity) {
		return super.spawnEntityInWorld(entity);
	}
}