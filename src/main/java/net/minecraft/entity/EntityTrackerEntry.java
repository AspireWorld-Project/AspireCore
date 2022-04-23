package net.minecraft.entity;

import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import net.minecraft.block.Block;
import net.minecraft.entity.ai.attributes.ServersideAttributeMap;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.item.*;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.*;
import net.minecraft.init.Items;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.*;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.MathHelper;
import net.minecraft.world.storage.MapData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerVelocityEvent;
import org.bukkit.util.Vector;
import org.ultramine.core.permissions.MinecraftPermissions;

import java.util.*;

public class EntityTrackerEntry {
	private static final Logger logger = LogManager.getLogger();
	public Entity myEntity;
	public int blocksDistanceThreshold;
	public int updateFrequency;
	public int lastScaledXPosition;
	public int lastScaledYPosition;
	public int lastScaledZPosition;
	public int lastYaw;
	public int lastPitch;
	public int lastHeadMotion;
	public double motionX;
	public double motionY;
	public double motionZ;
	public int ticks;
	private double posX;
	private double posY;
	private double posZ;
	private boolean isDataInitialized;
	private boolean sendVelocityUpdates;
	private int ticksSinceLastForcedTeleport;
	private Entity field_85178_v;
	private boolean ridingEntity;
	public boolean playerEntitiesUpdated;
	public Set trackingPlayers = new HashSet();
	private static final String __OBFID = "CL_00001443";

	public EntityTrackerEntry(Entity p_i1525_1_, int p_i1525_2_, int p_i1525_3_, boolean p_i1525_4_) {
		myEntity = p_i1525_1_;
		blocksDistanceThreshold = p_i1525_2_;
		updateFrequency = p_i1525_3_;
		sendVelocityUpdates = p_i1525_4_;
		lastScaledXPosition = MathHelper.floor_double(p_i1525_1_.posX * 32.0D);
		lastScaledYPosition = MathHelper.floor_double(p_i1525_1_.posY * 32.0D);
		lastScaledZPosition = MathHelper.floor_double(p_i1525_1_.posZ * 32.0D);
		lastYaw = MathHelper.floor_float(p_i1525_1_.rotationYaw * 256.0F / 360.0F);
		lastPitch = MathHelper.floor_float(p_i1525_1_.rotationPitch * 256.0F / 360.0F);
		lastHeadMotion = MathHelper.floor_float(p_i1525_1_.getRotationYawHead() * 256.0F / 360.0F);
	}

	@Override
	public boolean equals(Object p_equals_1_) {
		return p_equals_1_ instanceof EntityTrackerEntry
				? ((EntityTrackerEntry) p_equals_1_).myEntity.getEntityId() == myEntity.getEntityId()
				: false;
	}

	@Override
	public int hashCode() {
		return myEntity.getEntityId();
	}

	public void sendLocationToAllClients(List p_73122_1_) {
		boolean playerEntitiesUpdated = false;

		if (!isDataInitialized || posX != myEntity.chunkCoordX || posZ != myEntity.chunkCoordZ) {
			posX = myEntity.chunkCoordX;
			posY = myEntity.posY;
			posZ = myEntity.chunkCoordZ;
			isDataInitialized = true;
			playerEntitiesUpdated = true;
			// this.sendEventsToPlayers(p_73122_1_);
			// Moved below to lastScaled(*)Position be calculated first (fixes bug with
			// invisible players)
		}
		this.playerEntitiesUpdated = playerEntitiesUpdated;

		if (field_85178_v != myEntity.ridingEntity || myEntity.ridingEntity != null && ticks % 60 == 0) {
			field_85178_v = myEntity.ridingEntity;
			func_151259_a(new S1BPacketEntityAttach(0, myEntity, myEntity.ridingEntity));
		}

		if (myEntity instanceof EntityItemFrame && ticks % 10 == 0) {
			EntityItemFrame entityitemframe = (EntityItemFrame) myEntity;
			ItemStack itemstack = entityitemframe.getDisplayedItem();

			if (itemstack != null && itemstack.getItem() instanceof ItemMap) {
				MapData mapdata = Items.filled_map.getMapData(itemstack, myEntity.worldObj);
				Iterator iterator = p_73122_1_.iterator();

				while (iterator.hasNext()) {
					EntityPlayer entityplayer = (EntityPlayer) iterator.next();
					EntityPlayerMP entityplayermp = (EntityPlayerMP) entityplayer;
					mapdata.updateVisiblePlayers(entityplayermp, itemstack);
					Packet packet = Items.filled_map.func_150911_c(itemstack, myEntity.worldObj, entityplayermp);

					if (packet != null) {
						entityplayermp.playerNetServerHandler.sendPacket(packet);
					}
				}
			}

			sendMetadataToAllAssociatedPlayers();
		} else if (trackingPlayers.size() == 0 && !playerEntitiesUpdated) {
			// No players - no tracking
		} else if (ticks % updateFrequency == 0 || myEntity.isAirBorne || myEntity.getDataWatcher().hasChanges()
				|| playerEntitiesUpdated) {
			int i;
			int j;

			if (myEntity.ridingEntity == null) {
				++ticksSinceLastForcedTeleport;
				i = myEntity.myEntitySize.multiplyBy32AndRound(myEntity.posX);
				j = MathHelper.floor_double(myEntity.posY * 32.0D);
				int k = myEntity.myEntitySize.multiplyBy32AndRound(myEntity.posZ);
				int l = MathHelper.floor_float(myEntity.rotationYaw * 256.0F / 360.0F);
				int i1 = MathHelper.floor_float(myEntity.rotationPitch * 256.0F / 360.0F);
				int j1 = i - lastScaledXPosition;
				int k1 = j - lastScaledYPosition;
				int l1 = k - lastScaledZPosition;
				Object object = null;
				boolean flag = Math.abs(j1) >= 4 || Math.abs(k1) >= 4 || Math.abs(l1) >= 4 || ticks % 60 == 0;
				boolean flag1 = Math.abs(l - lastYaw) >= 4 || Math.abs(i1 - lastPitch) >= 4;

				if (ticks > 0 || myEntity instanceof EntityArrow) {
					if (j1 >= -128 && j1 < 128 && k1 >= -128 && k1 < 128 && l1 >= -128 && l1 < 128
							&& ticksSinceLastForcedTeleport <= 400 && !ridingEntity) {
						if (flag && flag1) {
							object = new S14PacketEntity.S17PacketEntityLookMove(myEntity.getEntityId(), (byte) j1,
									(byte) k1, (byte) l1, (byte) l, (byte) i1);
						} else if (flag) {
							object = new S14PacketEntity.S15PacketEntityRelMove(myEntity.getEntityId(), (byte) j1,
									(byte) k1, (byte) l1);
						} else if (flag1) {
							object = new S14PacketEntity.S16PacketEntityLook(myEntity.getEntityId(), (byte) l,
									(byte) i1);
						}
					} else {
						ticksSinceLastForcedTeleport = 0;
						object = new S18PacketEntityTeleport(myEntity.getEntityId(), i, j, k, (byte) l, (byte) i1);
					}
				}

				if (sendVelocityUpdates) {
					double d0 = myEntity.motionX - motionX;
					double d1 = myEntity.motionY - motionY;
					double d2 = myEntity.motionZ - motionZ;
					double d3 = 0.02D;
					double d4 = d0 * d0 + d1 * d1 + d2 * d2;

					if (d4 > d3 * d3 || d4 > 0.0D && myEntity.motionX == 0.0D && myEntity.motionY == 0.0D
							&& myEntity.motionZ == 0.0D) {
						motionX = myEntity.motionX;
						motionY = myEntity.motionY;
						motionZ = myEntity.motionZ;
						func_151259_a(new S12PacketEntityVelocity(myEntity.getEntityId(), motionX, motionY, motionZ));
					}
				}

				if (object != null) {
					func_151259_a((Packet) object);
				}

				sendMetadataToAllAssociatedPlayers();

				if (flag) {
					lastScaledXPosition = i;
					lastScaledYPosition = j;
					lastScaledZPosition = k;
				}

				if (flag1) {
					lastYaw = l;
					lastPitch = i1;
				}

				ridingEntity = false;
			} else {
				i = MathHelper.floor_float(myEntity.rotationYaw * 256.0F / 360.0F);
				j = MathHelper.floor_float(myEntity.rotationPitch * 256.0F / 360.0F);
				boolean flag2 = Math.abs(i - lastYaw) >= 4 || Math.abs(j - lastPitch) >= 4;

				if (flag2) {
					func_151259_a(new S14PacketEntity.S16PacketEntityLook(myEntity.getEntityId(), (byte) i, (byte) j));
					lastYaw = i;
					lastPitch = j;
				}

				lastScaledXPosition = myEntity.myEntitySize.multiplyBy32AndRound(myEntity.posX);
				lastScaledYPosition = MathHelper.floor_double(myEntity.posY * 32.0D);
				lastScaledZPosition = myEntity.myEntitySize.multiplyBy32AndRound(myEntity.posZ);
				sendMetadataToAllAssociatedPlayers();
				ridingEntity = true;
			}

			i = MathHelper.floor_float(myEntity.getRotationYawHead() * 256.0F / 360.0F);

			if (Math.abs(i - lastHeadMotion) >= 4) {
				func_151259_a(new S19PacketEntityHeadLook(myEntity, (byte) i));
				lastHeadMotion = i;
			}

			myEntity.isAirBorne = false;
		}

		++ticks;

		if (myEntity.velocityChanged) {
			boolean cancelled = false;
			if (myEntity instanceof EntityPlayerMP) {
				Player player = (Player) myEntity.getBukkitEntity();
				Vector velocity = player.getVelocity();
				PlayerVelocityEvent event = new PlayerVelocityEvent(player, velocity);
				Bukkit.getPluginManager().callEvent(event);
				if (event.isCancelled()) {
					cancelled = true;
				} else if (!velocity.equals(event.getVelocity())) {
					player.setVelocity(velocity);
				}
			}
			if (!cancelled) {
				func_151261_b(new S12PacketEntityVelocity(myEntity));
			}
			myEntity.velocityChanged = false;
		}

		if (playerEntitiesUpdated) {
			sendEventsToPlayers(p_73122_1_);
		}
	}

	private void sendMetadataToAllAssociatedPlayers() {
		DataWatcher datawatcher = myEntity.getDataWatcher();

		if (datawatcher.hasChanges()) {
			func_151261_b(new S1CPacketEntityMetadata(myEntity.getEntityId(), datawatcher, false));
		}

		if (myEntity instanceof EntityLivingBase) {
			ServersideAttributeMap serversideattributemap = (ServersideAttributeMap) ((EntityLivingBase) myEntity)
					.getAttributeMap();
			Set set = serversideattributemap.getAttributeInstanceSet();

			if (!set.isEmpty()) {
				func_151261_b(new S20PacketEntityProperties(myEntity.getEntityId(), set));
			}

			set.clear();
		}
	}

	public void func_151259_a(Packet p_151259_1_) {
		Iterator iterator = trackingPlayers.iterator();

		while (iterator.hasNext()) {
			EntityPlayerMP entityplayermp = (EntityPlayerMP) iterator.next();
			entityplayermp.playerNetServerHandler.sendPacket(p_151259_1_);
		}
	}

	public void func_151261_b(Packet p_151261_1_) {
		func_151259_a(p_151261_1_);

		if (myEntity instanceof EntityPlayerMP) {
			((EntityPlayerMP) myEntity).playerNetServerHandler.sendPacket(p_151261_1_);
		}
	}

	public void informAllAssociatedPlayersOfItemDestruction() {
		Iterator iterator = trackingPlayers.iterator();

		while (iterator.hasNext()) {
			EntityPlayerMP entityplayermp = (EntityPlayerMP) iterator.next();
			entityplayermp.func_152339_d(myEntity);
		}
	}

	public void removeFromWatchingList(EntityPlayerMP p_73118_1_) {
		if (trackingPlayers.contains(p_73118_1_)) {
			p_73118_1_.func_152339_d(myEntity);
			trackingPlayers.remove(p_73118_1_);
		}
	}

	public void tryStartWachingThis(EntityPlayerMP p_73117_1_) {
		if (p_73117_1_ != myEntity) {
			double d0 = p_73117_1_.posX - lastScaledXPosition / 32;
			double d1 = p_73117_1_.posZ - lastScaledZPosition / 32;

			if (d0 >= -blocksDistanceThreshold && d0 <= blocksDistanceThreshold && d1 >= -blocksDistanceThreshold
					&& d1 <= blocksDistanceThreshold) {
				if (!trackingPlayers.contains(p_73117_1_)
						&& (isPlayerWatchingThisChunk(p_73117_1_) || myEntity.forceSpawn)) {
					if (myEntity.isEntityPlayerMP() && ((EntityPlayerMP) myEntity).isHidden()
							&& !p_73117_1_.hasPermission(MinecraftPermissions.SEE_INVISIBLE_PLAYERS))
						return;
					trackingPlayers.add(p_73117_1_);
					Packet packet = func_151260_c();
					p_73117_1_.playerNetServerHandler.sendPacket(packet);

					if (!myEntity.getDataWatcher().getIsBlank()) {
						p_73117_1_.playerNetServerHandler.sendPacket(
								new S1CPacketEntityMetadata(myEntity.getEntityId(), myEntity.getDataWatcher(), true));
					}

					if (myEntity instanceof EntityLivingBase) {
						ServersideAttributeMap serversideattributemap = (ServersideAttributeMap) ((EntityLivingBase) myEntity)
								.getAttributeMap();
						Collection collection = serversideattributemap.getWatchedAttributes();

						if (!collection.isEmpty()) {
							p_73117_1_.playerNetServerHandler
									.sendPacket(new S20PacketEntityProperties(myEntity.getEntityId(), collection));
						}
					}

					motionX = myEntity.motionX;
					motionY = myEntity.motionY;
					motionZ = myEntity.motionZ;

					int posX = MathHelper.floor_double(myEntity.posX * 32.0D);
					int posY = MathHelper.floor_double(myEntity.posY * 32.0D);
					int posZ = MathHelper.floor_double(myEntity.posZ * 32.0D);
					if (posX != lastScaledXPosition || posY != lastScaledYPosition || posZ != lastScaledZPosition) {
						FMLNetworkHandler.makeEntitySpawnAdjustment(myEntity, p_73117_1_, lastScaledXPosition,
								lastScaledYPosition, lastScaledZPosition);
					}

					if (sendVelocityUpdates && !(packet instanceof S0FPacketSpawnMob)) {
						p_73117_1_.playerNetServerHandler.sendPacket(new S12PacketEntityVelocity(myEntity.getEntityId(),
								myEntity.motionX, myEntity.motionY, myEntity.motionZ));
					}

					if (myEntity.ridingEntity != null) {
						p_73117_1_.playerNetServerHandler
								.sendPacket(new S1BPacketEntityAttach(0, myEntity, myEntity.ridingEntity));
					}

					if (myEntity instanceof EntityLiving && ((EntityLiving) myEntity).getLeashedToEntity() != null) {
						p_73117_1_.playerNetServerHandler.sendPacket(
								new S1BPacketEntityAttach(1, myEntity, ((EntityLiving) myEntity).getLeashedToEntity()));
					}

					if (myEntity instanceof EntityLivingBase) {
						for (int i = 0; i < 5; ++i) {
							ItemStack itemstack = ((EntityLivingBase) myEntity).getEquipmentInSlot(i);

							if (itemstack != null) {
								p_73117_1_.playerNetServerHandler
										.sendPacket(new S04PacketEntityEquipment(myEntity.getEntityId(), i, itemstack));
							}
						}
					}

					if (myEntity instanceof EntityPlayer) {
						EntityPlayer entityplayer = (EntityPlayer) myEntity;

						if (entityplayer.isPlayerSleeping()) {
							p_73117_1_.playerNetServerHandler.sendPacket(new S0APacketUseBed(entityplayer,
									MathHelper.floor_double(myEntity.posX), MathHelper.floor_double(myEntity.posY),
									MathHelper.floor_double(myEntity.posZ)));
						}
					}

					if (myEntity instanceof EntityLivingBase) {
						EntityLivingBase entitylivingbase = (EntityLivingBase) myEntity;
						Iterator iterator = entitylivingbase.getActivePotionEffects().iterator();

						while (iterator.hasNext()) {
							PotionEffect potioneffect = (PotionEffect) iterator.next();
							p_73117_1_.playerNetServerHandler
									.sendPacket(new S1DPacketEntityEffect(myEntity.getEntityId(), potioneffect));
						}
					}
					net.minecraftforge.event.ForgeEventFactory.onStartEntityTracking(myEntity, p_73117_1_);
				}
			} else if (trackingPlayers.contains(p_73117_1_)) {
				trackingPlayers.remove(p_73117_1_);
				p_73117_1_.func_152339_d(myEntity);
				net.minecraftforge.event.ForgeEventFactory.onStopEntityTracking(myEntity, p_73117_1_);
			}
		}
	}

	private boolean isPlayerWatchingThisChunk(EntityPlayerMP p_73121_1_) {
		return p_73121_1_.getServerForPlayer().getPlayerManager().isPlayerWatchingChunk(p_73121_1_,
				myEntity.chunkCoordX, myEntity.chunkCoordZ);
	}

	public void sendEventsToPlayers(List p_73125_1_) {
		for (int i = 0; i < p_73125_1_.size(); ++i) {
			tryStartWachingThis((EntityPlayerMP) p_73125_1_.get(i));
		}
	}

	private Packet func_151260_c() {
		if (myEntity.isDead) {
			logger.warn("Fetching addPacket for removed entity");
		}

		Packet pkt = FMLNetworkHandler.getEntitySpawningPacket(myEntity);

		if (pkt != null)
			return pkt;
		if (myEntity instanceof EntityItem)
			return new S0EPacketSpawnObject(myEntity, 2, 1);
		else if (myEntity instanceof EntityPlayerMP)
			return new S0CPacketSpawnPlayer((EntityPlayer) myEntity);
		else if (myEntity instanceof EntityMinecart) {
			EntityMinecart entityminecart = (EntityMinecart) myEntity;
			return new S0EPacketSpawnObject(myEntity, 10, entityminecart.getMinecartType());
		} else if (myEntity instanceof EntityBoat)
			return new S0EPacketSpawnObject(myEntity, 1);
		else if (!(myEntity instanceof IAnimals) && !(myEntity instanceof EntityDragon)) {
			if (myEntity instanceof EntityFishHook) {
				EntityPlayer entityplayer = ((EntityFishHook) myEntity).field_146042_b;
				return new S0EPacketSpawnObject(myEntity, 90,
						entityplayer != null ? entityplayer.getEntityId() : myEntity.getEntityId());
			} else if (myEntity instanceof EntityArrow) {
				Entity entity = ((EntityArrow) myEntity).shootingEntity;
				return new S0EPacketSpawnObject(myEntity, 60,
						entity != null ? entity.getEntityId() : myEntity.getEntityId());
			} else if (myEntity instanceof EntitySnowball)
				return new S0EPacketSpawnObject(myEntity, 61);
			else if (myEntity instanceof EntityPotion)
				return new S0EPacketSpawnObject(myEntity, 73, ((EntityPotion) myEntity).getPotionDamage());
			else if (myEntity instanceof EntityExpBottle)
				return new S0EPacketSpawnObject(myEntity, 75);
			else if (myEntity instanceof EntityEnderPearl)
				return new S0EPacketSpawnObject(myEntity, 65);
			else if (myEntity instanceof EntityEnderEye)
				return new S0EPacketSpawnObject(myEntity, 72);
			else if (myEntity instanceof EntityFireworkRocket)
				return new S0EPacketSpawnObject(myEntity, 76);
			else {
				S0EPacketSpawnObject s0epacketspawnobject;

				if (myEntity instanceof EntityFireball) {
					EntityFireball entityfireball = (EntityFireball) myEntity;
					s0epacketspawnobject = null;
					byte b0 = 63;

					if (myEntity instanceof EntitySmallFireball) {
						b0 = 64;
					} else if (myEntity instanceof EntityWitherSkull) {
						b0 = 66;
					}

					if (entityfireball.shootingEntity != null) {
						s0epacketspawnobject = new S0EPacketSpawnObject(myEntity, b0,
								((EntityFireball) myEntity).shootingEntity.getEntityId());
					} else {
						s0epacketspawnobject = new S0EPacketSpawnObject(myEntity, b0, 0);
					}

					s0epacketspawnobject.func_149003_d((int) (entityfireball.accelerationX * 8000.0D));
					s0epacketspawnobject.func_149000_e((int) (entityfireball.accelerationY * 8000.0D));
					s0epacketspawnobject.func_149007_f((int) (entityfireball.accelerationZ * 8000.0D));
					return s0epacketspawnobject;
				} else if (myEntity instanceof EntityEgg)
					return new S0EPacketSpawnObject(myEntity, 62);
				else if (myEntity instanceof EntityTNTPrimed)
					return new S0EPacketSpawnObject(myEntity, 50);
				else if (myEntity instanceof EntityEnderCrystal)
					return new S0EPacketSpawnObject(myEntity, 51);
				else if (myEntity instanceof EntityFallingBlock) {
					EntityFallingBlock entityfallingblock = (EntityFallingBlock) myEntity;
					return new S0EPacketSpawnObject(myEntity, 70,
							Block.getIdFromBlock(entityfallingblock.func_145805_f())
									| entityfallingblock.field_145814_a << 16);
				} else if (myEntity instanceof EntityPainting)
					return new S10PacketSpawnPainting((EntityPainting) myEntity);
				else if (myEntity instanceof EntityItemFrame) {
					EntityItemFrame entityitemframe = (EntityItemFrame) myEntity;
					s0epacketspawnobject = new S0EPacketSpawnObject(myEntity, 71, entityitemframe.hangingDirection);
					s0epacketspawnobject.func_148996_a(MathHelper.floor_float(entityitemframe.field_146063_b * 32));
					s0epacketspawnobject.func_148995_b(MathHelper.floor_float(entityitemframe.field_146064_c * 32));
					s0epacketspawnobject.func_149005_c(MathHelper.floor_float(entityitemframe.field_146062_d * 32));
					return s0epacketspawnobject;
				} else if (myEntity instanceof EntityLeashKnot) {
					EntityLeashKnot entityleashknot = (EntityLeashKnot) myEntity;
					s0epacketspawnobject = new S0EPacketSpawnObject(myEntity, 77);
					s0epacketspawnobject.func_148996_a(MathHelper.floor_float(entityleashknot.field_146063_b * 32));
					s0epacketspawnobject.func_148995_b(MathHelper.floor_float(entityleashknot.field_146064_c * 32));
					s0epacketspawnobject.func_149005_c(MathHelper.floor_float(entityleashknot.field_146062_d * 32));
					return s0epacketspawnobject;
				} else if (myEntity instanceof EntityXPOrb)
					return new S11PacketSpawnExperienceOrb((EntityXPOrb) myEntity);
				else
					throw new IllegalArgumentException("Don\'t know how to add " + myEntity.getClass() + "!");
			}
		} else {
			lastHeadMotion = MathHelper.floor_float(myEntity.getRotationYawHead() * 256.0F / 360.0F);
			return new S0FPacketSpawnMob((EntityLivingBase) myEntity);
		}
	}

	public void removePlayerFromTracker(EntityPlayerMP p_73123_1_) {
		if (trackingPlayers.contains(p_73123_1_)) {
			trackingPlayers.remove(p_73123_1_);
			p_73123_1_.func_152339_d(myEntity);
		}
	}
}