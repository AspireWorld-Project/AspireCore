package net.minecraft.server.management;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S21PacketChunkData;
import net.minecraft.network.play.server.S22PacketMultiBlockChange;
import net.minecraft.network.play.server.S23PacketBlockChange;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.LongHashMap;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class PlayerManager {
	@SuppressWarnings("unused")
	private static final Logger field_152627_a = LogManager.getLogger();
	private final WorldServer theWorldServer;
	@SuppressWarnings("rawtypes")
	private final List players = new ArrayList();
	private final LongHashMap playerInstances = new LongHashMap();
	@SuppressWarnings("rawtypes")
	private final List chunkWatcherWithPlayers = new ArrayList();
	@SuppressWarnings("rawtypes")
	private final List playerInstanceList = new ArrayList();
	private int playerViewRadius;
	private long previousTotalWorldTime;
	private final int[][] xzDirectionsConst = new int[][] { { 1, 0 }, { 0, 1 }, { -1, 0 }, { 0, -1 } };
	public PlayerManager(WorldServer p_i1176_1_) {
		theWorldServer = p_i1176_1_;
		func_152622_a(p_i1176_1_.func_73046_m().getConfigurationManager().getViewDistance());
	}

	public WorldServer getWorldServer() {
		return theWorldServer;
	}

	public void updatePlayerInstances() {
		long i = theWorldServer.getTotalWorldTime();
		int j;
		PlayerManager.PlayerInstance playerinstance;

		if (i - previousTotalWorldTime > 8000L) {
			previousTotalWorldTime = i;

			for (j = 0; j < playerInstanceList.size(); ++j) {
				playerinstance = (PlayerManager.PlayerInstance) playerInstanceList.get(j);
				playerinstance.sendChunkUpdate();
				playerinstance.processChunk();
			}
		} else {
			for (j = 0; j < chunkWatcherWithPlayers.size(); ++j) {
				playerinstance = (PlayerManager.PlayerInstance) chunkWatcherWithPlayers.get(j);
				playerinstance.sendChunkUpdate();
			}
		}

		chunkWatcherWithPlayers.clear();

		if (players.isEmpty()) {
			WorldProvider worldprovider = theWorldServer.provider;

			if (!worldprovider.canRespawnHere()) {
				theWorldServer.theChunkProviderServer.unloadAllChunks();
			}
		}
	}

	public boolean func_152621_a(int p_152621_1_, int p_152621_2_) {
		long k = p_152621_1_ + 2147483647L | p_152621_2_ + 2147483647L << 32;
		return playerInstances.getValueByKey(k) != null;
	}

	@SuppressWarnings("unchecked")
	public PlayerManager.PlayerInstance getOrCreateChunkWatcher(int p_72690_1_, int p_72690_2_, boolean p_72690_3_) {
		long k = p_72690_1_ + 2147483647L | p_72690_2_ + 2147483647L << 32;
		PlayerManager.PlayerInstance playerinstance = (PlayerManager.PlayerInstance) playerInstances.getValueByKey(k);

		if (playerinstance == null && p_72690_3_) {
			playerinstance = new PlayerManager.PlayerInstance(p_72690_1_, p_72690_2_);
			playerInstances.add(k, playerinstance);
			playerInstanceList.add(playerinstance);
		}

		return playerinstance;
	}

	public void markBlockForUpdate(int p_151250_1_, int p_151250_2_, int p_151250_3_) {
		int l = p_151250_1_ >> 4;
		int i1 = p_151250_3_ >> 4;
		PlayerManager.PlayerInstance playerinstance = getOrCreateChunkWatcher(l, i1, false);

		if (playerinstance != null) {
			playerinstance.flagChunkForUpdate(p_151250_1_ & 15, p_151250_2_, p_151250_3_ & 15);
		}
	}

	@SuppressWarnings("unchecked")
	public void addPlayer(EntityPlayerMP par1EntityPlayerMP) {
		par1EntityPlayerMP.getChunkMgr().addTo(this);
		players.add(par1EntityPlayerMP);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void filterChunkLoadQueue(EntityPlayerMP p_72691_1_) {
		ArrayList arraylist = new ArrayList(p_72691_1_.loadedChunks);
		int i = 0;
		int j = playerViewRadius;
		int k = (int) p_72691_1_.posX >> 4;
		int l = (int) p_72691_1_.posZ >> 4;
		int i1 = 0;
		int j1 = 0;
		ChunkCoordIntPair chunkcoordintpair = getOrCreateChunkWatcher(k, l, true).chunkLocation;
		p_72691_1_.loadedChunks.clear();

		if (arraylist.contains(chunkcoordintpair)) {
			p_72691_1_.loadedChunks.add(chunkcoordintpair);
		}

		int k1;

		for (k1 = 1; k1 <= j * 2; ++k1) {
			for (int l1 = 0; l1 < 2; ++l1) {
				int[] aint = xzDirectionsConst[i++ % 4];

				for (int i2 = 0; i2 < k1; ++i2) {
					i1 += aint[0];
					j1 += aint[1];
					chunkcoordintpair = getOrCreateChunkWatcher(k + i1, l + j1, true).chunkLocation;

					if (arraylist.contains(chunkcoordintpair)) {
						p_72691_1_.loadedChunks.add(chunkcoordintpair);
					}
				}
			}
		}

		i %= 4;

		for (k1 = 0; k1 < j * 2; ++k1) {
			i1 += xzDirectionsConst[i][0];
			j1 += xzDirectionsConst[i][1];
			chunkcoordintpair = getOrCreateChunkWatcher(k + i1, l + j1, true).chunkLocation;

			if (arraylist.contains(chunkcoordintpair)) {
				p_72691_1_.loadedChunks.add(chunkcoordintpair);
			}
		}
	}

	public void removePlayer(EntityPlayerMP par1EntityPlayerMP) {
		par1EntityPlayerMP.getChunkMgr().removeFrom(this);
		players.remove(par1EntityPlayerMP);
	}

	@SuppressWarnings("unused")
	private boolean overlaps(int p_72684_1_, int p_72684_2_, int p_72684_3_, int p_72684_4_, int p_72684_5_) {
		int j1 = p_72684_1_ - p_72684_3_;
		int k1 = p_72684_2_ - p_72684_4_;
		return j1 >= -p_72684_5_ && j1 <= p_72684_5_ && k1 >= -p_72684_5_ && k1 <= p_72684_5_;
	}

	public void updatePlayerPertinentChunks(EntityPlayerMP par1EntityPlayerMP) {
		// par1EntityPlayerMP.getChunkMgr().updatePlayerPertinentChunks();
	}

	public boolean isPlayerWatchingChunk(EntityPlayerMP p_72694_1_, int p_72694_2_, int p_72694_3_) {
		PlayerManager.PlayerInstance playerinstance = getOrCreateChunkWatcher(p_72694_2_, p_72694_3_, false);
		return playerinstance != null && playerinstance.playersWatchingChunk.contains(p_72694_1_)
				&& !p_72694_1_.loadedChunks.contains(playerinstance.chunkLocation);
	}

	public void func_152622_a(int p_152622_1_) {
		p_152622_1_ = MathHelper.clamp_int(p_152622_1_, 3, 20);

		playerViewRadius = p_152622_1_;
		/*
		 * if (p_152622_1_ != this.playerViewRadius) { int j = p_152622_1_ -
		 * this.playerViewRadius; Iterator iterator = this.players.iterator();
		 *
		 * while (iterator.hasNext()) { EntityPlayerMP entityplayermp =
		 * (EntityPlayerMP)iterator.next(); int k = (int)entityplayermp.posX >> 4; int l
		 * = (int)entityplayermp.posZ >> 4; int i1; int j1;
		 *
		 * if (j > 0) { for (i1 = k - p_152622_1_; i1 <= k + p_152622_1_; ++i1) { for
		 * (j1 = l - p_152622_1_; j1 <= l + p_152622_1_; ++j1) {
		 * PlayerManager.PlayerInstance playerinstance =
		 * this.getOrCreateChunkWatcher(i1, j1, true);
		 *
		 * if (!playerinstance.playersWatchingChunk.contains(entityplayermp)) {
		 * playerinstance.addPlayer(entityplayermp); } } } } else { for (i1 = k -
		 * this.playerViewRadius; i1 <= k + this.playerViewRadius; ++i1) { for (j1 = l -
		 * this.playerViewRadius; j1 <= l + this.playerViewRadius; ++j1) { if
		 * (!this.overlaps(i1, j1, k, l, p_152622_1_)) {
		 * this.getOrCreateChunkWatcher(i1, j1, true).removePlayer(entityplayermp); } }
		 * } } }
		 *
		 * this.playerViewRadius = p_152622_1_; }
		 */
	}

	public static int getFurthestViewableBlock(int p_72686_0_) {
		return p_72686_0_ * 16 - 16;
	}

	public class PlayerInstance {
		@SuppressWarnings("rawtypes")
		private final List playersWatchingChunk = new ArrayList();
		private final ChunkCoordIntPair chunkLocation;
		private short[] locationOfBlockChange = new short[64];
		private int numberOfTilesToUpdate;
		private int flagsYAreasToUpdate;
		private long previousWorldTime;
		public PlayerInstance(int par2, int par3) {
			chunkLocation = new ChunkCoordIntPair(par2, par3);
			// getWorldServer().theChunkProviderServer.loadAsync(par2, par3,
			// this.loadedRunnable);
		}

		@SuppressWarnings("unchecked")
		public void addPlayer(EntityPlayerMP par1EntityPlayerMP) {
			if (playersWatchingChunk.contains(par1EntityPlayerMP))
				throw new IllegalStateException("Failed to add player. " + par1EntityPlayerMP + " already is in chunk "
						+ chunkLocation.chunkXPos + ", " + chunkLocation.chunkZPos);
			else {
				if (playersWatchingChunk.isEmpty()) {
					previousWorldTime = theWorldServer.getTotalWorldTime();
				}

				playersWatchingChunk.add(par1EntityPlayerMP);
			}
		}

		public void removePlayer(EntityPlayerMP par1EntityPlayerMP) {
			if (playersWatchingChunk.contains(par1EntityPlayerMP)) {
				Chunk chunk = theWorldServer.getChunkFromChunkCoords(chunkLocation.chunkXPos, chunkLocation.chunkZPos);

				// if (chunk.func_150802_k())
				{
					par1EntityPlayerMP.playerNetServerHandler.sendPacket(S21PacketChunkData.makeForUnload(chunk));
				}

				playersWatchingChunk.remove(par1EntityPlayerMP);
				// par1EntityPlayerMP.loadedChunks.remove(this.chunkLocation);

				net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(
						new net.minecraftforge.event.world.ChunkWatchEvent.UnWatch(chunkLocation, par1EntityPlayerMP));

				if (playersWatchingChunk.isEmpty()) {
					long i = chunkLocation.chunkXPos + 2147483647L | chunkLocation.chunkZPos + 2147483647L << 32;
					increaseInhabitedTime(chunk);
					playerInstances.remove(i);
					playerInstanceList.remove(this);

					if (numberOfTilesToUpdate > 0) {
						chunkWatcherWithPlayers.remove(this);
					}

					getWorldServer().theChunkProviderServer.unbindChunk(chunkLocation.chunkXPos,
							chunkLocation.chunkZPos);
				}
			}
		}

		public void processChunk() {
			increaseInhabitedTime(
					theWorldServer.getChunkFromChunkCoords(chunkLocation.chunkXPos, chunkLocation.chunkZPos));
		}

		private void increaseInhabitedTime(Chunk p_111196_1_) {
			p_111196_1_.inhabitedTime += theWorldServer.getTotalWorldTime() - previousWorldTime;
			previousWorldTime = theWorldServer.getTotalWorldTime();
		}

		@SuppressWarnings("unchecked")
		public void flagChunkForUpdate(int p_151253_1_, int p_151253_2_, int p_151253_3_) {
			if (numberOfTilesToUpdate == 0) {
				chunkWatcherWithPlayers.add(this);
			}

			flagsYAreasToUpdate |= 1 << (p_151253_2_ >> 4);

			// if (this.numberOfTilesToUpdate < 64) //Forge; Cache everything, so always run
			{
				short short1 = (short) (p_151253_1_ << 12 | p_151253_3_ << 8 | p_151253_2_);

				for (int l = 0; l < numberOfTilesToUpdate; ++l) {
					if (locationOfBlockChange[l] == short1)
						return;
				}

				if (numberOfTilesToUpdate == locationOfBlockChange.length) {
					locationOfBlockChange = java.util.Arrays.copyOf(locationOfBlockChange,
							locationOfBlockChange.length << 1);
				}
				locationOfBlockChange[numberOfTilesToUpdate++] = short1;
			}
		}

		public void sendToAllPlayersWatchingChunk(Packet p_151251_1_) {
			for (int i = 0; i < playersWatchingChunk.size(); ++i) {
				EntityPlayerMP entityplayermp = (EntityPlayerMP) playersWatchingChunk.get(i);

				if (!entityplayermp.loadedChunks.contains(chunkLocation)) {
					entityplayermp.playerNetServerHandler.sendPacket(p_151251_1_);
				}
			}
		}

		@SuppressWarnings({ "unused", "rawtypes" })
		public void sendChunkUpdate() {
			if (numberOfTilesToUpdate != 0) {
				int i;
				int j;
				int k;

				if (numberOfTilesToUpdate == 1) {
					i = chunkLocation.chunkXPos * 16 + (locationOfBlockChange[0] >> 12 & 15);
					j = locationOfBlockChange[0] & 255;
					k = chunkLocation.chunkZPos * 16 + (locationOfBlockChange[0] >> 8 & 15);
					sendToAllPlayersWatchingChunk(new S23PacketBlockChange(i, j, k, theWorldServer));

					if (theWorldServer.getBlock(i, j, k).hasTileEntity(theWorldServer.getBlockMetadata(i, j, k))) {
						sendTileToAllPlayersWatchingChunk(theWorldServer.getTileEntity(i, j, k));
					}
				} else {
					int l;

					if (numberOfTilesToUpdate >= net.minecraftforge.common.ForgeModContainer.clumpingThreshold) {
						i = chunkLocation.chunkXPos * 16;
						j = chunkLocation.chunkZPos * 16;
						sendToAllPlayersWatchingChunk(new S21PacketChunkData(theWorldServer.getChunkFromChunkCoords(
								chunkLocation.chunkXPos, chunkLocation.chunkZPos), false, flagsYAreasToUpdate));

						// Forge: Grabs ALL tile entities is costly on a modded server, only send needed
						// ones
						for (k = 0; false && k < 16; ++k) {
							if ((flagsYAreasToUpdate & 1 << k) != 0) {
								l = k << 4;
								List list = theWorldServer.func_147486_a(i, l, j, i + 16, l + 16, j + 16);

								for (int i1 = 0; i1 < list.size(); ++i1) {
									sendTileToAllPlayersWatchingChunk((TileEntity) list.get(i1));
								}
							}
						}
					} else {
						sendToAllPlayersWatchingChunk(new S22PacketMultiBlockChange(numberOfTilesToUpdate,
								locationOfBlockChange, theWorldServer.getChunkFromChunkCoords(chunkLocation.chunkXPos,
										chunkLocation.chunkZPos)));
					}

					{ // Forge: Send only the tile entities that are updated, Adding this brace lets
						// us keep the indent and the patch small
						WorldServer world = theWorldServer;
						for (i = 0; i < numberOfTilesToUpdate; ++i) {
							j = chunkLocation.chunkXPos * 16 + (locationOfBlockChange[i] >> 12 & 15);
							k = locationOfBlockChange[i] & 255;
							l = chunkLocation.chunkZPos * 16 + (locationOfBlockChange[i] >> 8 & 15);

							if (world.getBlock(j, k, l).hasTileEntity(world.getBlockMetadata(j, k, l))) {
								sendTileToAllPlayersWatchingChunk(theWorldServer.getTileEntity(j, k, l));
							}
						}
					}
				}

				numberOfTilesToUpdate = 0;
				flagsYAreasToUpdate = 0;
			}
		}

		private void sendTileToAllPlayersWatchingChunk(TileEntity p_151252_1_) {
			if (p_151252_1_ != null) {
				Packet packet = p_151252_1_.getDescriptionPacket();

				if (packet != null) {
					sendToAllPlayersWatchingChunk(packet);
				}
			}
		}
	}
}
