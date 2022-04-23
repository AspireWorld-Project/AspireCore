package net.minecraft.client.multiplayer;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.MovingSoundMinecart;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.particle.EntityFireworkStarterFX;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.profiler.Profiler;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IntHashMap;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.storage.SaveHandlerMP;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.openhft.koloboke.collect.IntCursor;
import net.openhft.koloboke.collect.set.hash.HashIntSet;
import net.openhft.koloboke.collect.set.hash.HashIntSets;
import org.ultramine.server.chunk.ChunkHash;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.Callable;

@SideOnly(Side.CLIENT)
public class WorldClient extends World {
	private NetHandlerPlayClient sendQueue;
	private ChunkProviderClient clientChunkProvider;
	private IntHashMap entityHashSet = new IntHashMap();
	private Set entityList = new HashSet();
	private Set entitySpawnQueue = new HashSet();
	private final Minecraft mc = Minecraft.getMinecraft();
	private final HashIntSet previousActiveChunkSet = HashIntSets.newMutableSet();
	private static final String __OBFID = "CL_00000882";

	public WorldClient(NetHandlerPlayClient p_i45063_1_, WorldSettings p_i45063_2_, int p_i45063_3_,
			EnumDifficulty p_i45063_4_, Profiler p_i45063_5_) {
		super(new SaveHandlerMP(), "MpServer", WorldProvider.getProviderForDimension(p_i45063_3_), p_i45063_2_,
				p_i45063_5_);
		sendQueue = p_i45063_1_;
		difficultySetting = p_i45063_4_;
		mapStorage = p_i45063_1_.mapStorageOrigin;
		isRemote = true;
		finishSetup();
		this.setSpawnLocation(8, 64, 8);
		MinecraftForge.EVENT_BUS.post(new WorldEvent.Load(this));
	}

	@Override
	public void tick() {
		super.tick();
		func_82738_a(getTotalWorldTime() + 1L);

		if (getGameRules().getGameRuleBooleanValue("doDaylightCycle")) {
			setWorldTime(getWorldTime() + 1L);
		}

		theProfiler.startSection("reEntryProcessing");

		for (int i = 0; i < 10 && !entitySpawnQueue.isEmpty(); ++i) {
			Entity entity = (Entity) entitySpawnQueue.iterator().next();
			entitySpawnQueue.remove(entity);

			if (!loadedEntityList.contains(entity)) {
				spawnEntityInWorld(entity);
			}
		}

		theProfiler.endStartSection("connection");
		sendQueue.onNetworkTick();
		theProfiler.endStartSection("chunkCache");
		clientChunkProvider.unloadQueuedChunks();
		theProfiler.endStartSection("blocks");
		func_147456_g();
		theProfiler.endSection();
	}

	public void invalidateBlockReceiveRegion(int p_73031_1_, int p_73031_2_, int p_73031_3_, int p_73031_4_,
			int p_73031_5_, int p_73031_6_) {
	}

	@Override
	protected IChunkProvider createChunkProvider() {
		clientChunkProvider = new ChunkProviderClient(this);
		return clientChunkProvider;
	}

	@Override
	protected void func_147456_g() {
		super.func_147456_g();
		previousActiveChunkSet.retainAll(activeChunks.keySet());

		if (previousActiveChunkSet.size() == activeChunks.size()) {
			previousActiveChunkSet.clear();
		}

		int i = 0;
		for (IntCursor iter = activeChunks.keySet().cursor(); iter.moveNext();) {
			int chunkCoord = iter.elem();

			if (!previousActiveChunkSet.contains(chunkCoord)) {
				int chunkX = ChunkHash.keyToX(chunkCoord);
				int chunkZ = ChunkHash.keyToZ(chunkCoord);

				int j = chunkX << 4;
				int k = chunkZ << 4;
				theProfiler.startSection("getChunk");
				Chunk chunk = getChunkFromChunkCoords(chunkX, chunkZ);
				func_147467_a(j, k, chunk);
				theProfiler.endSection();
				previousActiveChunkSet.add(chunkCoord);
				++i;

				if (i >= 10)
					return;
			}
		}
	}

	public void doPreChunk(int p_73025_1_, int p_73025_2_, boolean p_73025_3_) {
		if (p_73025_3_) {
			clientChunkProvider.loadChunk(p_73025_1_, p_73025_2_);
		} else {
			clientChunkProvider.unloadChunk(p_73025_1_, p_73025_2_);
		}

		if (!p_73025_3_) {
			markBlockRangeForRenderUpdate(p_73025_1_ * 16, 0, p_73025_2_ * 16, p_73025_1_ * 16 + 15, 256,
					p_73025_2_ * 16 + 15);
		}
	}

	@Override
	public boolean spawnEntityInWorld(Entity p_72838_1_) {
		boolean flag = super.spawnEntityInWorld(p_72838_1_);
		entityList.add(p_72838_1_);

		if (!flag) {
			entitySpawnQueue.add(p_72838_1_);
		} else if (p_72838_1_ instanceof EntityMinecart) {
			mc.getSoundHandler().playSound(new MovingSoundMinecart((EntityMinecart) p_72838_1_));
		}

		return flag;
	}

	@Override
	public void removeEntity(Entity p_72900_1_) {
		super.removeEntity(p_72900_1_);
		entityList.remove(p_72900_1_);
	}

	@Override
	public void onEntityAdded(Entity p_72923_1_) {
		super.onEntityAdded(p_72923_1_);

		if (entitySpawnQueue.contains(p_72923_1_)) {
			entitySpawnQueue.remove(p_72923_1_);
		}
	}

	@Override
	public void onEntityRemoved(Entity p_72847_1_) {
		super.onEntityRemoved(p_72847_1_);
		boolean flag = false;

		if (entityList.contains(p_72847_1_)) {
			if (p_72847_1_.isEntityAlive()) {
				entitySpawnQueue.add(p_72847_1_);
				flag = true;
			} else {
				entityList.remove(p_72847_1_);
			}
		}

		if (RenderManager.instance.getEntityRenderObject(p_72847_1_).isStaticEntity() && !flag) {
			mc.renderGlobal.onStaticEntitiesChanged();
		}
	}

	public void addEntityToWorld(int p_73027_1_, Entity p_73027_2_) {
		Entity entity1 = getEntityByID(p_73027_1_);

		if (entity1 != null) {
			removeEntity(entity1);
		}

		entityList.add(p_73027_2_);
		p_73027_2_.setEntityId(p_73027_1_);

		if (!spawnEntityInWorld(p_73027_2_)) {
			entitySpawnQueue.add(p_73027_2_);
		}

		entityHashSet.addKey(p_73027_1_, p_73027_2_);

		if (RenderManager.instance.getEntityRenderObject(p_73027_2_).isStaticEntity()) {
			mc.renderGlobal.onStaticEntitiesChanged();
		}
	}

	@Override
	public Entity getEntityByID(int p_73045_1_) {
		return p_73045_1_ == mc.thePlayer.getEntityId() ? mc.thePlayer : (Entity) entityHashSet.lookup(p_73045_1_);
	}

	public Entity removeEntityFromWorld(int p_73028_1_) {
		Entity entity = (Entity) entityHashSet.removeObject(p_73028_1_);

		if (entity != null) {
			entityList.remove(entity);
			removeEntity(entity);
		}

		return entity;
	}

	public boolean func_147492_c(int p_147492_1_, int p_147492_2_, int p_147492_3_, Block p_147492_4_,
			int p_147492_5_) {
		invalidateBlockReceiveRegion(p_147492_1_, p_147492_2_, p_147492_3_, p_147492_1_, p_147492_2_, p_147492_3_);
		return super.setBlock(p_147492_1_, p_147492_2_, p_147492_3_, p_147492_4_, p_147492_5_, 3);
	}

	@Override
	public void sendQuittingDisconnectingPacket() {
		sendQueue.getNetworkManager().closeChannel(new ChatComponentText("Quitting"));
	}

	@Override
	protected void updateWeather() {
		super.updateWeather();
	}

	@Override
	public void updateWeatherBody() {
		if (!provider.hasNoSky) {
			;
		}
	}

	@Override
	protected int func_152379_p() {
		return mc.gameSettings.renderDistanceChunks;
	}

	public void doVoidFogParticles(int p_73029_1_, int p_73029_2_, int p_73029_3_) {
		byte b0 = 16;
		Random random = new Random();

		for (int l = 0; l < 1000; ++l) {
			int i1 = p_73029_1_ + rand.nextInt(b0) - rand.nextInt(b0);
			int j1 = p_73029_2_ + rand.nextInt(b0) - rand.nextInt(b0);
			int k1 = p_73029_3_ + rand.nextInt(b0) - rand.nextInt(b0);
			Block block = getBlock(i1, j1, k1);

			if (block.getMaterial() == Material.air) {
				if (rand.nextInt(8) > j1 && provider.getWorldHasVoidParticles()) {
					spawnParticle("depthsuspend", i1 + rand.nextFloat(), j1 + rand.nextFloat(), k1 + rand.nextFloat(),
							0.0D, 0.0D, 0.0D);
				}
			} else {
				block.randomDisplayTick(this, i1, j1, k1, random);
			}
		}
	}

	public void removeAllEntities() {
		loadedEntityList.removeAll(unloadedEntityList);
		int i;
		Entity entity;
		int j;
		int k;

		for (i = 0; i < unloadedEntityList.size(); ++i) {
			entity = (Entity) unloadedEntityList.get(i);
			j = entity.chunkCoordX;
			k = entity.chunkCoordZ;

			if (entity.addedToChunk && chunkExists(j, k)) {
				getChunkFromChunkCoords(j, k).removeEntity(entity);
			}
		}

		for (i = 0; i < unloadedEntityList.size(); ++i) {
			onEntityRemoved((Entity) unloadedEntityList.get(i));
		}

		unloadedEntityList.clear();

		for (i = 0; i < loadedEntityList.size(); ++i) {
			entity = (Entity) loadedEntityList.get(i);

			if (entity.ridingEntity != null) {
				if (!entity.ridingEntity.isDead && entity.ridingEntity.riddenByEntity == entity) {
					continue;
				}

				entity.ridingEntity.riddenByEntity = null;
				entity.ridingEntity = null;
			}

			if (entity.isDead) {
				j = entity.chunkCoordX;
				k = entity.chunkCoordZ;

				if (entity.addedToChunk && chunkExists(j, k)) {
					getChunkFromChunkCoords(j, k).removeEntity(entity);
				}

				loadedEntityList.remove(i--);
				onEntityRemoved(entity);
			}
		}
	}

	@Override
	public CrashReportCategory addWorldInfoToCrashReport(CrashReport p_72914_1_) {
		CrashReportCategory crashreportcategory = super.addWorldInfoToCrashReport(p_72914_1_);
		crashreportcategory.addCrashSectionCallable("Forced entities", new Callable() {
			private static final String __OBFID = "CL_00000883";

			@Override
			public String call() {
				return entityList.size() + " total; " + entityList.toString();
			}
		});
		crashreportcategory.addCrashSectionCallable("Retry entities", new Callable() {
			private static final String __OBFID = "CL_00000884";

			@Override
			public String call() {
				return entitySpawnQueue.size() + " total; " + entitySpawnQueue.toString();
			}
		});
		crashreportcategory.addCrashSectionCallable("Server brand", new Callable() {
			private static final String __OBFID = "CL_00000885";

			@Override
			public String call() {
				return mc.thePlayer.func_142021_k();
			}
		});
		crashreportcategory.addCrashSectionCallable("Server type", new Callable() {
			private static final String __OBFID = "CL_00000886";

			@Override
			public String call() {
				return mc.getIntegratedServer() == null ? "Non-integrated multiplayer server"
						: "Integrated singleplayer server";
			}
		});
		return crashreportcategory;
	}

	@Override
	public void playSound(double p_72980_1_, double p_72980_3_, double p_72980_5_, String p_72980_7_, float p_72980_8_,
			float p_72980_9_, boolean p_72980_10_) {
		double d3 = mc.renderViewEntity.getDistanceSq(p_72980_1_, p_72980_3_, p_72980_5_);
		PositionedSoundRecord positionedsoundrecord = new PositionedSoundRecord(new ResourceLocation(p_72980_7_),
				p_72980_8_, p_72980_9_, (float) p_72980_1_, (float) p_72980_3_, (float) p_72980_5_);

		if (p_72980_10_ && d3 > 100.0D) {
			double d4 = Math.sqrt(d3) / 40.0D;
			mc.getSoundHandler().playDelayedSound(positionedsoundrecord, (int) (d4 * 20.0D));
		} else {
			mc.getSoundHandler().playSound(positionedsoundrecord);
		}
	}

	@Override
	public void makeFireworks(double p_92088_1_, double p_92088_3_, double p_92088_5_, double p_92088_7_,
			double p_92088_9_, double p_92088_11_, NBTTagCompound p_92088_13_) {
		mc.effectRenderer.addEffect(new EntityFireworkStarterFX(this, p_92088_1_, p_92088_3_, p_92088_5_, p_92088_7_,
				p_92088_9_, p_92088_11_, mc.effectRenderer, p_92088_13_));
	}

	public void setWorldScoreboard(Scoreboard p_96443_1_) {
		worldScoreboard = p_96443_1_;
	}

	@Override
	public void setWorldTime(long p_72877_1_) {
		if (p_72877_1_ < 0L) {
			p_72877_1_ = -p_72877_1_;
			getGameRules().setOrCreateGameRule("doDaylightCycle", "false");
		} else {
			getGameRules().setOrCreateGameRule("doDaylightCycle", "true");
		}

		super.setWorldTime(p_72877_1_);
	}

	@Override
	public boolean real_spawnEntityInWorld(Entity entity) {
		return super.spawnEntityInWorld(entity);
	}
}
