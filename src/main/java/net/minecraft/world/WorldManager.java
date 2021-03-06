package net.minecraft.world;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.S25PacketBlockBreakAnim;
import net.minecraft.network.play.server.S28PacketEffect;
import net.minecraft.network.play.server.S29PacketSoundEffect;
import net.minecraft.server.MinecraftServer;

import java.util.Iterator;

public class WorldManager implements IWorldAccess {
	private final MinecraftServer mcServer;
	private final WorldServer theWorldServer;
	public WorldManager(MinecraftServer p_i1517_1_, WorldServer p_i1517_2_) {
		mcServer = p_i1517_1_;
		theWorldServer = p_i1517_2_;
	}

	@Override
	public void spawnParticle(String p_72708_1_, double p_72708_2_, double p_72708_4_, double p_72708_6_,
			double p_72708_8_, double p_72708_10_, double p_72708_12_) {
	}

	@Override
	public void onEntityCreate(Entity p_72703_1_) {
		theWorldServer.getEntityTracker().addEntityToTracker(p_72703_1_);
	}

	@Override
	public void onEntityDestroy(Entity p_72709_1_) {
		theWorldServer.getEntityTracker().removeEntityFromAllTrackingPlayers(p_72709_1_);
	}

	@Override
	public void playSound(String p_72704_1_, double p_72704_2_, double p_72704_4_, double p_72704_6_, float p_72704_8_,
			float p_72704_9_) {
		mcServer.getConfigurationManager().sendToAllNear(p_72704_2_, p_72704_4_, p_72704_6_,
				p_72704_8_ > 1.0F ? (double) (16.0F * p_72704_8_) : 16.0D, theWorldServer.provider.dimensionId,
				new S29PacketSoundEffect(p_72704_1_, p_72704_2_, p_72704_4_, p_72704_6_, p_72704_8_, p_72704_9_));
	}

	@Override
	public void playSoundToNearExcept(EntityPlayer p_85102_1_, String p_85102_2_, double p_85102_3_, double p_85102_5_,
			double p_85102_7_, float p_85102_9_, float p_85102_10_) {
		mcServer.getConfigurationManager().sendToAllNearExcept(p_85102_1_, p_85102_3_, p_85102_5_, p_85102_7_,
				p_85102_9_ > 1.0F ? (double) (16.0F * p_85102_9_) : 16.0D, theWorldServer.provider.dimensionId,
				new S29PacketSoundEffect(p_85102_2_, p_85102_3_, p_85102_5_, p_85102_7_, p_85102_9_, p_85102_10_));
	}

	@Override
	public void markBlockRangeForRenderUpdate(int p_147585_1_, int p_147585_2_, int p_147585_3_, int p_147585_4_,
			int p_147585_5_, int p_147585_6_) {
	}

	@Override
	public void markBlockForUpdate(int p_147586_1_, int p_147586_2_, int p_147586_3_) {
		theWorldServer.getPlayerManager().markBlockForUpdate(p_147586_1_, p_147586_2_, p_147586_3_);
	}

	@Override
	public void markBlockForRenderUpdate(int p_147588_1_, int p_147588_2_, int p_147588_3_) {
	}

	@Override
	public void playRecord(String p_72702_1_, int p_72702_2_, int p_72702_3_, int p_72702_4_) {
	}

	@Override
	public void playAuxSFX(EntityPlayer p_72706_1_, int p_72706_2_, int p_72706_3_, int p_72706_4_, int p_72706_5_,
			int p_72706_6_) {
		mcServer.getConfigurationManager().sendToAllNearExcept(p_72706_1_, p_72706_3_, p_72706_4_, p_72706_5_, 64.0D,
				theWorldServer.provider.dimensionId,
				new S28PacketEffect(p_72706_2_, p_72706_3_, p_72706_4_, p_72706_5_, p_72706_6_, false));
	}

	@Override
	public void broadcastSound(int p_82746_1_, int p_82746_2_, int p_82746_3_, int p_82746_4_, int p_82746_5_) {
		mcServer.getConfigurationManager().sendPacketToAllPlayers(
				new S28PacketEffect(p_82746_1_, p_82746_2_, p_82746_3_, p_82746_4_, p_82746_5_, true));
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void destroyBlockPartially(int p_147587_1_, int p_147587_2_, int p_147587_3_, int p_147587_4_,
			int p_147587_5_) {
		Iterator iterator = mcServer.getConfigurationManager().playerEntityList.iterator();

		while (iterator.hasNext()) {
			EntityPlayerMP entityplayermp = (EntityPlayerMP) iterator.next();

			if (entityplayermp != null && entityplayermp.worldObj == theWorldServer
					&& entityplayermp.getEntityId() != p_147587_1_) {
				double d0 = p_147587_2_ - entityplayermp.posX;
				double d1 = p_147587_3_ - entityplayermp.posY;
				double d2 = p_147587_4_ - entityplayermp.posZ;

				if (d0 * d0 + d1 * d1 + d2 * d2 < 1024.0D) {
					entityplayermp.playerNetServerHandler.sendPacket(new S25PacketBlockBreakAnim(p_147587_1_,
							p_147587_2_, p_147587_3_, p_147587_4_, p_147587_5_));
				}
			}
		}
	}

	@Override
	public void onStaticEntitiesChanged() {
	}
}