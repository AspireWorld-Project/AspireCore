package net.minecraft.network;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import cpw.mods.fml.common.eventhandler.Event;
import io.netty.buffer.Unpooled;
import io.netty.util.concurrent.GenericFutureListener;
import net.jafama.FastMath;
import net.minecraft.block.material.Material;
import net.minecraft.command.server.CommandBlockLogic;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityMinecartCommandBlock;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemEditableBook;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemWritableBook;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.network.play.client.*;
import net.minecraft.network.play.server.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.UserListBansEntry;
import net.minecraft.stats.AchievementList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.*;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.command.CraftConsoleCommandSender;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.craftbukkit.inventory.CraftInventoryView;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.util.CraftChatMessage;
import org.bukkit.craftbukkit.util.LazyPlayerSet;
import org.bukkit.craftbukkit.util.Waitable;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;
import org.ultramine.advanced.CauldronUtils;
import org.ultramine.bukkit.UMBukkitImplMod;
import org.ultramine.core.permissions.Permissions;
import org.ultramine.core.service.InjectService;
import org.ultramine.server.event.PlayerSneakingEvent;
import org.ultramine.server.event.PlayerSwingItemEvent;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

public class NetHandlerPlayServer implements INetHandlerPlayServer {
	private static final Logger logger = LogManager.getLogger();
	public final NetworkManager netManager;
	private final MinecraftServer serverController;
	public EntityPlayerMP playerEntity;
	private int networkTickCount;
	private int floatingTickCount;
	private boolean field_147366_g;
	private int field_147378_h;
	private long field_147379_i;
	private static final Random field_147376_j = new Random();
	private long field_147377_k;
	private int field_147375_m;
	private final IntHashMap field_147372_n = new IntHashMap();
	private volatile int chatSpamThresholdCount; // Cauldron - set to volatile to fix multithreaded issues
	private static final AtomicIntegerFieldUpdater chatSpamField = AtomicIntegerFieldUpdater.newUpdater(NetHandlerPlayServer.class, CauldronUtils.deobfuscatedEnvironment() ? "chatSpamThresholdCount" : "fiel" + "d_147374_l"); // CraftBukkit - multithreaded field
	private double lastPosX;
	private double lastPosY;
	private double lastPosZ;
	private boolean hasMoved = true;
	private boolean processedDisconnect; // CraftBukkit - added
	@InjectService
	private static Permissions perms;
	private float lastPitch = Float.MAX_VALUE;
	private float lastYaw = Float.MAX_VALUE;
	private boolean justTeleported = false;
	private boolean trigger = false;

	// For the PacketPlayOutBlockPlace hack :(
	Long lastPacket;

	// Store the last block right clicked and what type it was
	private Item lastMaterial;

	public NetHandlerPlayServer(MinecraftServer p_i1530_1_, NetworkManager p_i1530_2_, EntityPlayerMP p_i1530_3_) {
		serverController = p_i1530_1_;
		netManager = p_i1530_2_;
		p_i1530_2_.setNetHandler(this);
		playerEntity = p_i1530_3_;
		p_i1530_3_.playerNetServerHandler = this;
		this.server = p_i1530_1_ == null ? null : UMBukkitImplMod.getServer();
	}

	public CraftPlayer getPlayerB() {
		return playerEntity == null ? null : playerEntity.getBukkitEntity();
	}
	private final org.bukkit.craftbukkit.CraftServer server;
	// CraftBukkit start - Add "isDisconnected" method
	public final boolean isDisconnected() {
		return !netManager.channel().config().isAutoRead();
	}

	// CraftBukkit end
	private final static HashSet<Integer> invalidItems = new HashSet<>(java.util.Arrays.asList(8, 9, 10, 11, 26, 34, 36, 43, 51, 52, 55, 59, 60, 62, 63,
			64, 68, 71, 74, 75, 83, 90, 92, 93, 94, 104, 105, 115, 117, 118, 119, 125, 127, 132, 140, 141, 142, 144)); // TODO: Check after every update.
	// CraftBukkit end
	// Cauldron start
	public CraftServer getCraftServer() {
		return server;
	}
	// Cauldron end

	public void teleport(Location dest) {
		double destX, destY, destZ;
		float destYaw, destPitch;
		destX = dest.getX();
		destY = dest.getY();
		destZ = dest.getZ();
		destYaw = dest.getYaw();
		destPitch = dest.getPitch();
		if (Float.isNaN(destYaw) || Float.isInfinite(destYaw)) {
			destYaw = 0;
		}
		if (Float.isNaN(destPitch) || Float.isInfinite(destPitch)) {
			destPitch = 0;
		}
		lastPosX = destX;
		lastPosY = destY;
		lastPosZ = destZ;
		lastYaw = destYaw;
		lastPitch = destPitch;
		justTeleported = true;
		hasMoved = false;
		playerEntity.setPositionAndRotation(destX, destY, destZ, destYaw, destPitch);
		playerEntity.playerNetServerHandler.sendPacket(
				new S08PacketPlayerPosLook(destX, destY + 1.6200000047683716D, destZ, destYaw, destPitch, false));
	}

	@Override
	public void onNetworkTick() {
		field_147366_g = false;
		++networkTickCount;
		serverController.theProfiler.startSection("keepAlive");

		if (networkTickCount - field_147377_k > 40L) {
			field_147377_k = networkTickCount;
			field_147379_i = func_147363_d();
			field_147378_h = (int) field_147379_i;
			sendPacket(new S00PacketKeepAlive(field_147378_h));
		}

		serverController.theProfiler.endSection();
		for (int spam; (spam = this.chatSpamThresholdCount) > 0 && !chatSpamField.compareAndSet(this, spam, spam - 1);) ;

		if (field_147375_m > 0) {
			--field_147375_m;
		}

		if (playerEntity.func_154331_x() > 0L && serverController.func_143007_ar() > 0
				&& MinecraftServer.getSystemTimeMillis()
				- playerEntity.func_154331_x() > serverController.func_143007_ar() * 1000 * 60) {
			kickPlayerFromServer("You have been idle for too long!");
		}
	}

	public NetworkManager func_147362_b() {
		return netManager;
	}

	public void kickPlayerFromServer(String p_147360_1_)
	{
		String leaveMessage = EnumChatFormatting.YELLOW + this.playerEntity.getCommandSenderName() + " left the game.";
		PlayerKickEvent event = new PlayerKickEvent(this.server.getPlayer(this.playerEntity), p_147360_1_, leaveMessage);
		if (this.server.getServer().isServerRunning())
		{
			this.server.getPluginManager().callEvent(event);
		}
		if (event.isCancelled())
		{
			return;
		}
		p_147360_1_ = event.getReason();
		final ChatComponentText chatcomponenttext = new ChatComponentText(p_147360_1_);
		this.netManager.scheduleOutboundPacket(new S40PacketDisconnect(chatcomponenttext),
				p_operationComplete_1_ -> NetHandlerPlayServer.this.netManager.closeChannel(chatcomponenttext));
		this.onDisconnect(chatcomponenttext);
		this.netManager.disableAutoRead();
	}


	@Override
	public void processInput(C0CPacketInput p_147358_1_) {
		playerEntity.setEntityActionState(p_147358_1_.func_149620_c(), p_147358_1_.func_149616_d(),
				p_147358_1_.func_149618_e(), p_147358_1_.func_149617_f());
	}

	public void processPlayer(C03PacketPlayer p_147347_1_)
	{
		// CraftBukkit start - Check for NaN
		if (Double.isNaN(p_147347_1_.field_149479_a) || Double.isNaN(p_147347_1_.field_149477_b) || Double.isNaN(p_147347_1_.field_149478_c)
				|| Double.isNaN(p_147347_1_.field_149475_d))
		{
			logger.warn(playerEntity.getCommandSenderName() + " was caught trying to crash the server with an invalid position.");
			getPlayerB().kickPlayer("Nope!");
			return;
		}
		// CraftBukkit end
		WorldServer worldserver = this.serverController.worldServerForDimension(this.playerEntity.dimension);
		this.field_147366_g = true;

		if (!this.playerEntity.playerConqueredTheEnd)
		{
			double d0;

			if (!this.hasMoved)
			{
				d0 = p_147347_1_.func_149467_d() - this.lastPosY;

				if (p_147347_1_.func_149464_c() == this.lastPosX && d0 * d0 < 0.01D && p_147347_1_.func_149472_e() == this.lastPosZ)
				{
					this.hasMoved = true;
				}
			}

			// CraftBukkit start
			Player player = this.getPlayerB();
			Location from = new Location(player.getWorld(), lastPosX, lastPosY, lastPosZ, lastYaw, lastPitch); // Get the Players previous Event location.
			Location to = player.getLocation().clone(); // Start off the To location as the Players current location.

			// If the packet contains movement information then we update the To location with the correct XYZ.
			if (p_147347_1_.field_149480_h && !(p_147347_1_.field_149480_h && p_147347_1_.field_149477_b == -999.0D && p_147347_1_.field_149475_d == -999.0D))
			{
				to.setX(p_147347_1_.field_149479_a);
				to.setY(p_147347_1_.field_149477_b);
				to.setZ(p_147347_1_.field_149478_c);
			}

			// If the packet contains look information then we update the To location with the correct Yaw & Pitch.
			if (p_147347_1_.field_149481_i)
			{
				to.setYaw(p_147347_1_.field_149476_e);
				to.setPitch(p_147347_1_.field_149473_f);
			}

			// Prevent 40 event-calls for less than a single pixel of movement >.>
			double delta = Math.pow(this.lastPosX - to.getX(), 2) + Math.pow(this.lastPosY - to.getY(), 2) + Math.pow(this.lastPosZ - to.getZ(), 2);
			float deltaAngle = Math.abs(this.lastYaw - to.getYaw()) + Math.abs(this.lastPitch - to.getPitch());

			if ((delta > 2f / 256 || deltaAngle > 10f) && (this.hasMoved && !this.playerEntity.isDead))
			{
				if(this.lastPosX == to.getX() && this.lastPosY == to.getY() && this.lastPosZ == to.getZ() && this.lastYaw == to.getYaw() && this.lastPitch == to.getPitch()) return;
				this.lastPosX = to.getX();
				this.lastPosY = to.getY();
				this.lastPosZ = to.getZ();
				this.lastYaw = to.getYaw();
				this.lastPitch = to.getPitch();

				Location oldTo = to.clone();
				PlayerMoveEvent event = new PlayerMoveEvent(player, from, to);

				trigger = !trigger;
				if(trigger)
					this.server.getPluginManager().callEvent(event);

				// If the event is cancelled we move the player back to their old location.
				if (event.isCancelled())
				{
					this.playerEntity.playerNetServerHandler.sendPacket(new S08PacketPlayerPosLook(from.getX(), from.getY() + 1.6200000047683716D, from
							.getZ(), from.getYaw(), from.getPitch(), false));
					return;
				}

                /* If a Plugin has changed the To destination then we teleport the Player
                there to avoid any 'Moved wrongly' or 'Moved too quickly' errors.
                We only do this if the Event was not cancelled. */
				if (!oldTo.equals(event.getTo()) && !event.isCancelled())
				{
					this.playerEntity.getBukkitEntity().teleport(event.getTo(), PlayerTeleportEvent.TeleportCause.UNKNOWN);
					return;
				}

                /* Check to see if the Players Location has some how changed during the call of the event.
                This can happen due to a plugin teleporting the player instead of using .setTo() */
				if (!from.equals(this.getPlayerB().getLocation()) && this.justTeleported)
				{
					this.justTeleported = false;
					return;
				}
			}

			if (this.hasMoved && !this.playerEntity.isDead)
			{
				// CraftBukkit end
				double d1;
				double d2;
				double d3;

				if (this.playerEntity.ridingEntity != null)
				{
					float f4 = this.playerEntity.rotationYaw;
					float f = this.playerEntity.rotationPitch;
					this.playerEntity.ridingEntity.updateRiderPosition();
					d1 = this.playerEntity.posX;
					d2 = this.playerEntity.posY;
					d3 = this.playerEntity.posZ;

					if (p_147347_1_.func_149463_k())
					{
						f4 = p_147347_1_.func_149462_g();
						f = p_147347_1_.func_149470_h();
					}

					this.playerEntity.onGround = p_147347_1_.func_149465_i();
					this.playerEntity.onUpdateEntity();
					this.playerEntity.ySize = 0.0F;
					this.playerEntity.setPositionAndRotation(d1, d2, d3, f4, f);

					if (this.playerEntity.ridingEntity != null)
					{
						this.playerEntity.ridingEntity.updateRiderPosition();
					}

					if (!this.hasMoved) //Fixes teleportation kick while riding entities
					{
						return;
					}

					this.serverController.getConfigurationManager().updatePlayerPertinentChunks(this.playerEntity);

					if (this.hasMoved)
					{
						this.lastPosX = this.playerEntity.posX;
						this.lastPosY = this.playerEntity.posY;
						this.lastPosZ = this.playerEntity.posZ;
					}

					worldserver.updateEntity(this.playerEntity);
					return;
				}

				if (this.playerEntity.isPlayerSleeping())
				{
					this.playerEntity.onUpdateEntity();
					this.playerEntity.setPositionAndRotation(this.lastPosX, this.lastPosY, this.lastPosZ, this.playerEntity.rotationYaw, this.playerEntity.rotationPitch);
					worldserver.updateEntity(this.playerEntity);
					return;
				}

				d0 = this.playerEntity.posY;
				this.lastPosX = this.playerEntity.posX;
				this.lastPosY = this.playerEntity.posY;
				this.lastPosZ = this.playerEntity.posZ;
				d1 = this.playerEntity.posX;
				d2 = this.playerEntity.posY;
				d3 = this.playerEntity.posZ;
				float f1 = this.playerEntity.rotationYaw;
				float f2 = this.playerEntity.rotationPitch;

				if (p_147347_1_.func_149466_j() && p_147347_1_.func_149467_d() == -999.0D && p_147347_1_.func_149471_f() == -999.0D)
				{
					p_147347_1_.func_149469_a(false);
				}

				double d4;

				if (p_147347_1_.func_149466_j())
				{
					d1 = p_147347_1_.func_149464_c();
					d2 = p_147347_1_.func_149467_d();
					d3 = p_147347_1_.func_149472_e();
					d4 = p_147347_1_.func_149471_f() - p_147347_1_.func_149467_d();

					if (FastMath.abs(p_147347_1_.func_149464_c()) > 3.2E7D || FastMath.abs(p_147347_1_.func_149472_e()) > 3.2E7D)
					{
						this.kickPlayerFromServer("Illegal position");
						return;
					}
				}

				if (p_147347_1_.func_149463_k())
				{
					f1 = p_147347_1_.func_149462_g();
					f2 = p_147347_1_.func_149470_h();
				}

				this.playerEntity.onUpdateEntity();
				this.playerEntity.ySize = 0.0F;
				this.playerEntity.setPositionAndRotation(this.lastPosX, this.lastPosY, this.lastPosZ, f1, f2);

				if (!this.hasMoved)
				{
					return;
				}

				d4 = d1 - this.playerEntity.posX;
				double d5 = d2 - this.playerEntity.posY;
				double d6 = d3 - this.playerEntity.posZ;
				//BUGFIX: min -> max, grabs the highest distance
				double d7 = Math.max(FastMath.abs(d4), FastMath.abs(this.playerEntity.motionX));
				double d8 = Math.max(FastMath.abs(d5), FastMath.abs(this.playerEntity.motionY));
				boolean downMovement = d5 < 0 || this.playerEntity.motionY < 0;
				double d9 = Math.max(FastMath.abs(d6), FastMath.abs(this.playerEntity.motionZ));
				double d10 = d7 * d7 + d8 * d8 + d9 * d9;
				// 3D distance traversed, squared
				//if (!this.serverController.isFlightAllowed() && !this.playerEntity.theItemInWorldManager.isCreative() && !worldserver.checkBlockCollision(axisalignedbb) && !this.playerEntity.capabilities.allowFlying)

				// Thermos, allow bypass of moved too quickly if accelerating straight down
				if (d10 > 100.0D && this.hasMoved && (!this.serverController.isSinglePlayer()) && !(downMovement && d8 * d8 / 100.0D > .96))   // CraftBukkit - Added this.checkMovement condition to solve this check being triggered by teleports
				{
					logger.warn(this.playerEntity.getCommandSenderName() + " moved too quickly! " + d4 + "," + d5 + "," + d6 + " (" + d7 + ", " + d8 + ", " + d9 + ")");
					this.setPlayerLocation(this.lastPosX, this.lastPosY, this.lastPosZ, this.playerEntity.rotationYaw, this.playerEntity.rotationPitch);
					return;
				}

				float f3 = 0.0625F;
				boolean flag = worldserver.getCollidingBoundingBoxes(this.playerEntity, this.playerEntity.boundingBox.copy().contract((double)f3, (double)f3, (double)f3)).isEmpty();

				if (this.playerEntity.onGround && !p_147347_1_.func_149465_i() && d5 > 0.0D)
				{
					this.playerEntity.jump();
				}

				if (!this.hasMoved) //Fixes "Moved Too Fast" kick when being teleported while moving
				{
					return;
				}

				this.playerEntity.moveEntity(d4, d5, d6);
				this.playerEntity.onGround = p_147347_1_.func_149465_i();
				this.playerEntity.addMovementStat(d4, d5, d6);
				double d11 = d5;
				d4 = d1 - this.playerEntity.posX;
				d5 = d2 - this.playerEntity.posY;

				if (d5 > -0.5D || d5 < 0.5D)
				{
					d5 = 0.0D;
				}

				d6 = d3 - this.playerEntity.posZ;
				d10 = d4 * d4 + d5 * d5 + d6 * d6;
				boolean flag1 = false;

				if (d10 > 0.0625D && !this.playerEntity.isPlayerSleeping() && !this.playerEntity.theItemInWorldManager.isCreative())
				{
					flag1 = true;
					logger.warn(this.playerEntity.getCommandSenderName() + " moved wrongly!");
				}

				if (!this.hasMoved) //Fixes "Moved Too Fast" kick when being teleported while moving
				{
					return;
				}

				this.playerEntity.setPositionAndRotation(d1, d2, d3, f1, f2);
				boolean flag2 = worldserver.getCollidingBoundingBoxes(this.playerEntity, this.playerEntity.boundingBox.copy().contract((double)f3, (double)f3, (double)f3)).isEmpty();

				if (flag && (flag1 || !flag2) && !this.playerEntity.isPlayerSleeping() && !this.playerEntity.noClip)
				{
					this.setPlayerLocation(this.lastPosX, this.lastPosY, this.lastPosZ, f1, f2);
					return;
				}

				AxisAlignedBB axisalignedbb = this.playerEntity.boundingBox.copy().expand((double)f3, (double)f3, (double)f3).addCoord(0.0D, -0.55D, 0.0D);

				if (!this.serverController.isFlightAllowed() && !this.playerEntity.theItemInWorldManager.isCreative() && !worldserver.checkBlockCollision(axisalignedbb) && !this.playerEntity.capabilities.allowFlying)
				{
					if (d11 >= -0.03125D)
					{
						++this.floatingTickCount;

						if (this.floatingTickCount > 80)
						{
							logger.warn(this.playerEntity.getCommandSenderName() + " was kicked for floating too long!");
							this.kickPlayerFromServer("Flying is not enabled on this server");
							return;
						}
					}
				}
				else
				{
					this.floatingTickCount = 0;
				}

				if (!this.hasMoved) //Fixes "Moved Too Fast" kick when being teleported while moving
				{
					return;
				}

				this.playerEntity.onGround = p_147347_1_.func_149465_i();
				this.serverController.getConfigurationManager().updatePlayerPertinentChunks(this.playerEntity);
				this.playerEntity.handleFalling(this.playerEntity.posY - d0, p_147347_1_.func_149465_i());
			}
			else if (this.networkTickCount % 20 == 0)
			{
				this.setPlayerLocation(this.lastPosX, this.lastPosY, this.lastPosZ, this.playerEntity.rotationYaw, this.playerEntity.rotationPitch);
			}
		}
	}

	public void setPlayerLocation(double x, double y, double z, float yaw, float pitch) {
		Player player = getPlayerB();
		Location from = player.getLocation();
		Location to = new Location(getPlayerB().getWorld(), x, y, z, yaw, pitch);
		PlayerTeleportEvent event = new PlayerTeleportEvent(player, from, to,
				PlayerTeleportEvent.TeleportCause.UNKNOWN);
		Bukkit.getPluginManager().callEvent(event);
		from = event.getFrom();
		to = event.isCancelled() ? from : event.getTo();
		teleport(to);
	}

	@Override
	public void processPlayerDigging(C07PacketPlayerDigging p_147345_1_) {
		WorldServer worldserver = serverController.worldServerForDimension(playerEntity.dimension);
		playerEntity.func_143004_u();

		if (p_147345_1_.func_149506_g() == 4) {
			playerEntity.dropOneItem(false);
		} else if (p_147345_1_.func_149506_g() == 3) {
			playerEntity.dropOneItem(true);
		} else if (p_147345_1_.func_149506_g() == 5) {
			playerEntity.stopUsingItem();
		} else {
			boolean flag = p_147345_1_.func_149506_g() == 0;

			if (p_147345_1_.func_149506_g() == 1) {
				flag = true;
			}

			if (p_147345_1_.func_149506_g() == 2) {
				flag = true;
			}

			int i = p_147345_1_.func_149505_c();
			int j = p_147345_1_.func_149503_d();
			int k = p_147345_1_.func_149502_e();

			if (flag) {
				double d0 = playerEntity.posX - (i + 0.5D);
				double d1 = playerEntity.posY - (j + 0.5D) + 1.5D;
				double d2 = playerEntity.posZ - (k + 0.5D);
				double d3 = d0 * d0 + d1 * d1 + d2 * d2;

				double dist = playerEntity.theItemInWorldManager.getBlockReachDistance() + 1;
				dist *= dist;

				if (d3 > dist)
					return;

				if (j >= playerEntity.getServerForPlayer().getConfig().settings.maxBuildHeight)
					return;
			}

			if (p_147345_1_.func_149506_g() == 0) {
				if (!serverController.isBlockProtected(worldserver, i, j, k, playerEntity)) {
					playerEntity.theItemInWorldManager.onBlockClicked(i, j, k, p_147345_1_.func_149501_f());
				} else {
					playerEntity.playerNetServerHandler.sendPacket(new S23PacketBlockChange(i, j, k, worldserver));
				}
			} else if (p_147345_1_.func_149506_g() == 2) {
				playerEntity.theItemInWorldManager.uncheckedTryHarvestBlock(i, j, k);

				if (worldserver.getBlock(i, j, k).getMaterial() != Material.air) {
					playerEntity.playerNetServerHandler.sendPacket(new S23PacketBlockChange(i, j, k, worldserver));
				}
			} else if (p_147345_1_.func_149506_g() == 1) {
				playerEntity.theItemInWorldManager.cancelDestroyingBlock(i, j, k);

				if (worldserver.getBlock(i, j, k).getMaterial() != Material.air) {
					playerEntity.playerNetServerHandler.sendPacket(new S23PacketBlockChange(i, j, k, worldserver));
				}
			}
		}
	}

	@Override
	public void processPlayerBlockPlacement(C08PacketPlayerBlockPlacement pct) {

		if (playerEntity.isDead)
			return;

		// This is a horrible hack needed because the client sends 2 packets on 'right
		// mouse click'
		// aimed at a block. We shouldn't need to get the second packet if the data is
		// handled
		// but we cannot know what the client will do, so we might still get it
		//
		// If the time between packets is small enough, and the 'signature' similar, we
		// discard the
		// second one. This sadly has to remain until Mojang makes their packets saner.
		// :(
		// -- Grum
		if (pct.func_149568_f() == 255) {
			if (pct.func_149574_g() != null && pct.func_149574_g().getItem() == lastMaterial && lastPacket != null
					&& pct.getTimestamp() - lastPacket < 100) {
				lastPacket = null;
				return;
			}
		} else {
			lastMaterial = pct.func_149574_g() == null ? null : pct.func_149574_g().getItem();
			lastPacket = pct.getTimestamp();
		}

		WorldServer worldserver = serverController.worldServerForDimension(playerEntity.dimension);
		ItemStack itemstack = playerEntity.inventory.getCurrentItem();
		boolean flag = false;
		boolean placeResult = true;
		int i = pct.func_149576_c();
		int j = pct.func_149571_d();
		int k = pct.func_149570_e();
		int l = pct.func_149568_f();
		playerEntity.func_143004_u();

		int buildlimit = playerEntity.getServerForPlayer().getConfig().settings.maxBuildHeight;

		if (pct.func_149568_f() == 255) {
			if (itemstack == null)
				return;

			PlayerInteractEvent event = ForgeEventFactory.onPlayerInteract(playerEntity,
					PlayerInteractEvent.Action.RIGHT_CLICK_AIR, 0, 0, 0, -1, worldserver);
			if (event.useItem != Event.Result.DENY) {
				playerEntity.theItemInWorldManager.tryUseItem(playerEntity, worldserver, itemstack);
			}
		} else if (pct.func_149571_d() >= buildlimit - 1
				&& (pct.func_149568_f() == 1 || pct.func_149571_d() >= buildlimit)) {
			ChatComponentTranslation chatcomponenttranslation = new ChatComponentTranslation("build.tooHigh",
					Integer.valueOf(buildlimit));
			chatcomponenttranslation.getChatStyle().setColor(EnumChatFormatting.RED);
			playerEntity.playerNetServerHandler.sendPacket(new S02PacketChat(chatcomponenttranslation));
			flag = true;
		} else {
			double dist = playerEntity.theItemInWorldManager.getBlockReachDistance() + 1;
			dist *= dist;
			if (hasMoved
					&& playerEntity.getDistanceSq(i + 0.5D, j + 0.5D - playerEntity.getEyeHeight(), k + 0.5D) < dist
					&& !serverController.isBlockProtected(worldserver, i, j, k, playerEntity)) {
				// record block place result so we can update client itemstack size if place
				// event was cancelled.
				if (!playerEntity.theItemInWorldManager.activateBlockOrUseItem(playerEntity, worldserver, itemstack, i,
						j, k, l, pct.func_149573_h(), pct.func_149569_i(), pct.func_149575_j())) {
					placeResult = false;
				}
			}

			flag = true;
		}

		if (flag) {
			playerEntity.playerNetServerHandler.sendPacket(new S23PacketBlockChange(i, j, k, worldserver));

			if (l == 0) {
				--j;
			}

			if (l == 1) {
				++j;
			}

			if (l == 2) {
				--k;
			}

			if (l == 3) {
				++k;
			}

			if (l == 4) {
				--i;
			}

			if (l == 5) {
				++i;
			}

			playerEntity.playerNetServerHandler.sendPacket(new S23PacketBlockChange(i, j, k, worldserver));
		}

		itemstack = playerEntity.inventory.getCurrentItem();

		if (itemstack != null && itemstack.stackSize == 0) {
			playerEntity.inventory.mainInventory[playerEntity.inventory.currentItem] = null;
			itemstack = null;
		}

		if (itemstack == null || itemstack.getMaxItemUseDuration() == 0) {
			playerEntity.isChangingQuantityOnly = true;
			playerEntity.inventory.mainInventory[playerEntity.inventory.currentItem] = ItemStack
					.copyItemStack(playerEntity.inventory.mainInventory[playerEntity.inventory.currentItem]);
			Slot slot = playerEntity.openContainer.getSlotFromInventory(playerEntity.inventory,
					playerEntity.inventory.currentItem);
			playerEntity.openContainer.detectAndSendChanges();
			playerEntity.isChangingQuantityOnly = false;

			if (!ItemStack.areItemStacksEqual(playerEntity.inventory.getCurrentItem(), pct.func_149574_g())
					|| !placeResult) // force client itemstack update if place event was cancelled
			{
				sendPacket(new S2FPacketSetSlot(playerEntity.openContainer.windowId, slot.slotNumber,
						playerEntity.inventory.getCurrentItem()));
			}
		}
	}

	public void onDisconnect(IChatComponent p_147231_1_)
	{
		if (this.processedDisconnect)
		{
			return;
		}
		else
		{
			this.processedDisconnect = true;
		}
		logger.info(this.playerEntity.getCommandSenderName() + " lost connection: " + p_147231_1_.getUnformattedText()); // CraftBukkit - Don't toString the component
		this.serverController.func_147132_au();
		this.playerEntity.mountEntityAndWakeUp();
		String quitMessage = this.serverController.getConfigurationManager().disconnect(this.playerEntity);

		if ((quitMessage != null) && (quitMessage.length() > 0))
		{
			this.serverController.getConfigurationManager().sendMessage(CraftChatMessage.fromString(quitMessage));
		}
		if (this.serverController.isSinglePlayer() && this.playerEntity.getCommandSenderName().equals(this.serverController.getServerOwner()))
		{
			logger.info("Stopping singleplayer server as player logged out");
			this.serverController.initiateShutdown();
		}
	}

	public void sendPacket(final Packet p_147359_1_) {
		if (p_147359_1_ instanceof S02PacketChat) {
			S02PacketChat s02packetchat = (S02PacketChat) p_147359_1_;
			EntityPlayer.EnumChatVisibility enumchatvisibility = playerEntity.func_147096_v();

			if (enumchatvisibility == EntityPlayer.EnumChatVisibility.HIDDEN)
				return;

			if (enumchatvisibility == EntityPlayer.EnumChatVisibility.SYSTEM && !s02packetchat.func_148916_d())
				return;
		} else if (p_147359_1_ instanceof net.minecraft.network.play.server.S07PacketRespawn) {
			playerEntity.getChunkMgr().stopSending();
		}

		try {
			netManager.scheduleOutboundPacket(p_147359_1_);
		} catch (Throwable throwable) {
			CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Sending packet");
			CrashReportCategory crashreportcategory = crashreport.makeCategory("Packet being sent");
			crashreportcategory.addCrashSectionCallable("Packet class", new Callable() {
				private static final String __OBFID = "CL_00001454";

				@Override
				public String call() {
					return p_147359_1_.getClass().getCanonicalName();
				}
			});
			throw new ReportedException(crashreport);
		}
	}

	@Override
	public void processHeldItemChange(C09PacketHeldItemChange p_147355_1_) {

		if (playerEntity.isDead)
			return;

		PlayerItemHeldEvent event = new PlayerItemHeldEvent(getPlayerB(), playerEntity.inventory.currentItem,
				p_147355_1_.func_149614_c());
		Bukkit.getServer().getPluginManager().callEvent(event);

		if (event.isCancelled()) {
			sendPacket(new S09PacketHeldItemChange(playerEntity.inventory.currentItem));
			playerEntity.func_143004_u();
			return;
		}
		if (p_147355_1_.func_149614_c() >= 0 && p_147355_1_.func_149614_c() < InventoryPlayer.getHotbarSize()) {
			playerEntity.inventory.currentItem = p_147355_1_.func_149614_c();
			playerEntity.func_143004_u();
		} else {
			logger.warn(playerEntity.getCommandSenderName() + " tried to set an invalid carried item");
		}
	}

	public void processChatMessage(C01PacketChatMessage p_147354_1_)
	{
		if (this.playerEntity.isDead || this.playerEntity.func_147096_v() == EntityPlayer.EnumChatVisibility.HIDDEN) // CraftBukkit - dead men tell no tales
		{
			ChatComponentTranslation chatcomponenttranslation = new ChatComponentTranslation("chat.cannotSend");
			chatcomponenttranslation.getChatStyle().setColor(EnumChatFormatting.RED);
			this.sendPacket(new S02PacketChat(chatcomponenttranslation));
		}
		else
		{
			this.playerEntity.func_143004_u();
			String s = p_147354_1_.func_149439_c();
			s = StringUtils.normalizeSpace(s);

			for (int i = 0; i < s.length(); ++i)
			{
				if (!ChatAllowedCharacters.isAllowedCharacter(s.charAt(i)))
				{
					// CraftBukkit start - threadsafety
					if (p_147354_1_.hasPriority())
					{
						Waitable waitable = new Waitable() {
							@Override
							protected Object evaluate()
							{
								NetHandlerPlayServer.this.kickPlayerFromServer("Illegal characters in chat");
								return null;
							}
						};
						this.serverController.processQueue.add(waitable);

						try
						{
							waitable.get();
						}
						catch (InterruptedException e)
						{
							Thread.currentThread().interrupt();
						}
						catch (ExecutionException e)
						{
							throw new RuntimeException(e);
						}
					}
					else
					{
						this.kickPlayerFromServer("Illegal characters in chat");
					}
					// CraftBukkit end
					return;
				}
			}

			// CraftBukkit start
			if (!p_147354_1_.hasPriority())
			{
				try
				{
					this.server.playerCommandState = true;
					this.handleSlashCommand(s);
				}
				finally
				{
					this.server.playerCommandState = false;
				}
			}
			else if (s.isEmpty())
			{
				logger.warn(this.playerEntity.getCommandSenderName() + " tried to send an empty message");
			}
			else if (getPlayerB().isConversing())
			{
				getPlayerB().acceptConversationInput(s);
			}
			else if (this.playerEntity.func_147096_v() == EntityPlayer.EnumChatVisibility.SYSTEM) // Re-add "Command Only" flag check
			{
				ChatComponentTranslation chatcomponenttranslation = new ChatComponentTranslation("chat.cannotSend");
				chatcomponenttranslation.getChatStyle().setColor(EnumChatFormatting.RED);
				this.sendPacket(new S02PacketChat(chatcomponenttranslation));
			}
			else if (true)
			{
				String[] bits = s.split(" ");

				HashSet<String> possibilities = new HashSet<String>(); // No duplicates allowed
				for (String str: bits)
				{
					if (str.length() <= 17 && str.length() >= 4)
					{
						if(str.charAt(0) != '@')
							continue;
						possibilities.add(str.substring(1));

					}
				}

				for (Object o : MinecraftServer.getServer().getConfigurationManager().playerEntityList)
				{
					if (! (o instanceof EntityPlayerMP))
					{
						continue;
					}
					EntityPlayerMP ep = (EntityPlayerMP)o;
					if (possibilities.contains(ep.getCommandSenderName()))
					{
						ep.worldObj.playSoundAtEntity(ep, "random.orb", 4.0F, 4.0F);
					}

				}

				this.chat(s, true);
				// CraftBukkit end - the below is for reference. :)
			}

			// CraftBukkit start - replaced with thread safe throttle
			// this.chatSpamThresholdCount += 20;
			if (chatSpamField.addAndGet(this, 20) > 200 && !this.serverController.getConfigurationManager().func_152596_g(this.playerEntity.getGameProfile()))
			{
				if (p_147354_1_.hasPriority())
				{
					Waitable waitable = new Waitable() {
						@Override
						protected Object evaluate()
						{
							NetHandlerPlayServer.this.kickPlayerFromServer("disconnect.spam");
							return null;
						}
					};
					this.serverController.processQueue.add(waitable);

					try
					{
						waitable.get();
					}
					catch (InterruptedException e)
					{
						Thread.currentThread().interrupt();
					}
					catch (ExecutionException e)
					{
						throw new RuntimeException(e);
					}
				}
				else
				{
					this.kickPlayerFromServer("disconnect.spam");
				}

				// CraftBukkit end
			}
		}
	}



	private void handleSlashCommand(String p_147361_1_) {
		serverController.getCommandManager().executeCommand(playerEntity, p_147361_1_);
	}

	@Override
	public void processAnimation(C0APacketAnimation p_147350_1_) {
		playerEntity.func_143004_u();

		if (p_147350_1_.func_149421_d() == 1) {
			MinecraftForge.EVENT_BUS.post(new PlayerSwingItemEvent(playerEntity));

			// Arm swing animation
			PlayerAnimationEvent event = new PlayerAnimationEvent(this.getPlayerB());
			Bukkit.getPluginManager().callEvent(event);

			if (event.isCancelled()) {
				return;
			}

			playerEntity.swingItem();
		}
	}

	@Override
	public void processEntityAction(C0BPacketEntityAction p_147357_1_) {
		if (playerEntity.isDead)
			return;

		playerEntity.func_143004_u();

		if (p_147357_1_.func_149513_d() == 1 || p_147357_1_.func_149513_d() == 2) {
			PlayerToggleSneakEvent event = new PlayerToggleSneakEvent(getPlayerB(), p_147357_1_.func_149513_d() == 1);
			Bukkit.getPluginManager().callEvent(event);
			if (event.isCancelled())
				return;
		}
		if (p_147357_1_.func_149513_d() == 4 || p_147357_1_.func_149513_d() == 5) {
			PlayerToggleSprintEvent event = new PlayerToggleSprintEvent(getPlayerB(), p_147357_1_.func_149513_d() == 4);
			Bukkit.getPluginManager().callEvent(event);
			if (event.isCancelled())
				return;
		}

		if (p_147357_1_.func_149513_d() == 1) {
			MinecraftForge.EVENT_BUS.post(new PlayerSneakingEvent(playerEntity));
			playerEntity.setSneaking(true);
		} else if (p_147357_1_.func_149513_d() == 2) {
			playerEntity.setSneaking(false);
		} else if (p_147357_1_.func_149513_d() == 4) {
			playerEntity.setSprinting(true);
		} else if (p_147357_1_.func_149513_d() == 5) {
			playerEntity.setSprinting(false);
		} else if (p_147357_1_.func_149513_d() == 3) {
			playerEntity.wakeUpPlayer(false, true, true);
			hasMoved = false;
		} else if (p_147357_1_.func_149513_d() == 6) {
			if (playerEntity.ridingEntity != null && playerEntity.ridingEntity instanceof EntityHorse) {
				((EntityHorse) playerEntity.ridingEntity).setJumpPower(p_147357_1_.func_149512_e());
			}
		} else if (p_147357_1_.func_149513_d() == 7 && playerEntity.ridingEntity != null
				&& playerEntity.ridingEntity instanceof EntityHorse) {
			((EntityHorse) playerEntity.ridingEntity).openGUI(playerEntity);
		}
	}

	@Override
	public void processUseEntity(C02PacketUseEntity p_147340_1_) {
		WorldServer worldserver = serverController.worldServerForDimension(playerEntity.dimension);
		Entity entity = p_147340_1_.func_149564_a(worldserver);
		playerEntity.func_143004_u();

		if (entity != null) {
			boolean flag = playerEntity.canEntityBeSeen(entity);
			double d0 = 36.0D;

			if (!flag) {
				d0 = 9.0D;
			}

			if (playerEntity.getDistanceSqToEntity(entity) < d0) {
				if (p_147340_1_.func_149565_c() == C02PacketUseEntity.Action.INTERACT) {
					playerEntity.interactWith(entity);
				} else if (p_147340_1_.func_149565_c() == C02PacketUseEntity.Action.ATTACK) {
					if (entity instanceof EntityItem || entity instanceof EntityXPOrb || entity instanceof EntityArrow
							|| entity == playerEntity) {
						kickPlayerFromServer("Attempting to attack an invalid entity");
						serverController.logWarning(
								"Player " + playerEntity.getCommandSenderName() + " tried to attack an invalid entity");
						return;
					}

					playerEntity.attackTargetEntityWithCurrentItem(entity);
				}
			}
		}
	}

	@Override
	public void processClientStatus(C16PacketClientStatus p_147342_1_) {
		playerEntity.func_143004_u();
		C16PacketClientStatus.EnumState enumstate = p_147342_1_.func_149435_c();

		switch (NetHandlerPlayServer.SwitchEnumState.field_151290_a[enumstate.ordinal()]) {
			case 1:
				if (playerEntity.playerConqueredTheEnd) {
					playerEntity = serverController.getConfigurationManager().respawnPlayer(playerEntity, 0, true);
				} else if (playerEntity.getServerForPlayer().getWorldInfo().isHardcoreModeEnabled()) {
					if (serverController.isSinglePlayer()
							&& playerEntity.getCommandSenderName().equals(serverController.getServerOwner())) {
						playerEntity.playerNetServerHandler
								.kickPlayerFromServer("You have died. Game over, man, it's game over!");
						serverController.deleteWorldAndStopServer();
					} else {
						UserListBansEntry userlistbansentry = new UserListBansEntry(playerEntity.getGameProfile(),
								null, "(You just lost the game)", null, "Death in Hardcore");
						serverController.getConfigurationManager().func_152608_h().func_152687_a(userlistbansentry);
						playerEntity.playerNetServerHandler
								.kickPlayerFromServer("You have died. Game over, man, it's game over!");
					}
				} else {
					if (playerEntity.getHealth() > 0.0F)
						return;

					playerEntity = serverController.getConfigurationManager().respawnPlayer(playerEntity,
							playerEntity.dimension, false);
				}

				break;
			case 2:
				playerEntity.func_147099_x().func_150876_a(playerEntity);
				break;
			case 3:
				playerEntity.triggerAchievement(AchievementList.openInventory);
		}
	}

	@Override
	public void processCloseWindow(C0DPacketCloseWindow p_147356_1_) {
		if (playerEntity.isDead)
			return;
		if (playerEntity.openContainer.getBukkitView() != null) {
			CraftEventFactory.handleInventoryCloseEvent(playerEntity); // CraftBukkit
		}
		playerEntity.closeContainer();
	}

	@Override
	public void processClickWindow(C0EPacketClickWindow p_147351_1_) {
		playerEntity.func_143004_u();
		if (playerEntity.openContainer.windowId == p_147351_1_.func_149548_c()
				&& playerEntity.openContainer.isPlayerNotUsingContainer(playerEntity)) {
			// CraftBukkit start - Call InventoryClickEvent
			if (p_147351_1_.func_149544_d() < -1 && p_147351_1_.func_149544_d() != -999)
				return;
			InventoryView inventory = playerEntity.openContainer.getBukkitView();
			InventoryType.SlotType type = CraftInventoryView.getSlotType(inventory, p_147351_1_.func_149544_d());
			InventoryClickEvent event;
			ClickType click = ClickType.UNKNOWN;
			InventoryAction action = InventoryAction.UNKNOWN;
			ItemStack itemstack = null;
			// Cauldron start - some containers such as NEI's Creative Container does not
			// have a view at this point so we need to create one
			if (inventory == null) {
				inventory = new CraftInventoryView(
						playerEntity.getBukkitEntity(), Bukkit.getServer()
						.createInventory(playerEntity.getBukkitEntity(), InventoryType.CHEST),
						playerEntity.openContainer);
				// this.playerEntity.openContainer.bukkitView = inventory;
			}
			// Cauldron end
			if (p_147351_1_.func_149544_d() == -1) {
				type = InventoryType.SlotType.OUTSIDE; // override
				click = p_147351_1_.func_149543_e() == 0 ? ClickType.WINDOW_BORDER_LEFT : ClickType.WINDOW_BORDER_RIGHT;
				action = InventoryAction.NOTHING;
			} else if (p_147351_1_.func_149542_h() == 0) {
				if (p_147351_1_.func_149543_e() == 0 || p_147351_1_.func_149543_e() == 1) {
					click = p_147351_1_.func_149543_e() == 0 ? ClickType.LEFT : ClickType.RIGHT;
					action = InventoryAction.NOTHING; // Don't want to repeat ourselves
					if (p_147351_1_.func_149544_d() == -999) {
						if (playerEntity.inventory.getItemStack() != null) {
							action = p_147351_1_.func_149543_e() == 0 ? InventoryAction.DROP_ALL_CURSOR
									: InventoryAction.DROP_ONE_CURSOR;
						}
					} else {
						Slot slot = playerEntity.openContainer.getSlot(p_147351_1_.func_149544_d());
						if (slot != null) {
							ItemStack clickedItem = slot.getStack();
							ItemStack cursor = playerEntity.inventory.getItemStack();
							if (clickedItem == null) {
								if (cursor != null) {
									action = p_147351_1_.func_149543_e() == 0 ? InventoryAction.PLACE_ALL
											: InventoryAction.PLACE_ONE;
								}
							} else if (slot.canTakeStack(playerEntity)) // Should be Slot.isPlayerAllowed
							{
								if (cursor == null) {
									action = p_147351_1_.func_149543_e() == 0 ? InventoryAction.PICKUP_ALL
											: InventoryAction.PICKUP_HALF;
								} else if (slot.isItemValid(cursor)) // Should be Slot.isItemAllowed
								{
									if (clickedItem.isItemEqual(cursor)
											&& ItemStack.areItemStackTagsEqual(clickedItem, cursor)) {
										int toPlace = p_147351_1_.func_149543_e() == 0 ? cursor.stackSize : 1;
										toPlace = Math.min(toPlace,
												clickedItem.getMaxStackSize() - clickedItem.stackSize);
										toPlace = Math.min(toPlace,
												slot.inventory.getInventoryStackLimit() - clickedItem.stackSize);
										if (toPlace == 1) {
											action = InventoryAction.PLACE_ONE;
										} else if (toPlace == cursor.stackSize) {
											action = InventoryAction.PLACE_ALL;
										} else if (toPlace < 0) {
											action = toPlace != -1 ? InventoryAction.PICKUP_SOME
													: InventoryAction.PICKUP_ONE; // this happens with oversized stacks
										} else if (toPlace != 0) {
											action = InventoryAction.PLACE_SOME;
										}
									} else if (cursor.stackSize <= slot.getSlotStackLimit()) // Should be
									// Slot.getMaxStackSize()
									{
										action = InventoryAction.SWAP_WITH_CURSOR;
									}
								} else if (cursor.getItem() == clickedItem.getItem()
										&& (!cursor.getHasSubtypes()
										|| cursor.getItemDamage() == clickedItem.getItemDamage())
										&& ItemStack.areItemStackTagsEqual(cursor, clickedItem)) {
									if (clickedItem.stackSize >= 0)
										if (clickedItem.stackSize + cursor.stackSize <= cursor.getMaxStackSize()) {
											// As of 1.5, this is result slots only
											action = InventoryAction.PICKUP_ALL;
										}
								}
							}
						}
					}
				}
			} else if (p_147351_1_.func_149542_h() == 1) {
				if (p_147351_1_.func_149543_e() == 0) {
					click = ClickType.SHIFT_LEFT;
				} else if (p_147351_1_.func_149543_e() == 1) {
					click = ClickType.SHIFT_RIGHT;
				}
				if (p_147351_1_.func_149543_e() == 0 || p_147351_1_.func_149543_e() == 1) {
					if (p_147351_1_.func_149544_d() < 0) {
						action = InventoryAction.NOTHING;
					} else {
						Slot slot = playerEntity.openContainer.getSlot(p_147351_1_.func_149544_d());
						if (slot != null && slot.canTakeStack(playerEntity) && slot.getHasStack()) {
							// Slot.hasItem()
							action = InventoryAction.MOVE_TO_OTHER_INVENTORY;
						} else {
							action = InventoryAction.NOTHING;
						}
					}
				}
			} else if (p_147351_1_.func_149542_h() == 2) {
				if (p_147351_1_.func_149543_e() >= 0 && p_147351_1_.func_149543_e() < 9) {
					click = ClickType.NUMBER_KEY;
					Slot clickedSlot = playerEntity.openContainer.getSlot(p_147351_1_.func_149544_d());
					if (clickedSlot.canTakeStack(playerEntity)) {
						ItemStack hotbar = playerEntity.inventory.getStackInSlot(p_147351_1_.func_149543_e());
						boolean canCleanSwap = hotbar == null
								|| clickedSlot.inventory == playerEntity.inventory && clickedSlot.isItemValid(hotbar); // the
						// slot
						// will
						// accept
						// the
						// hotbar
						// item
						if (clickedSlot.getHasStack()) {
							if (canCleanSwap) {
								action = InventoryAction.HOTBAR_SWAP;
							} else {
								int firstEmptySlot = playerEntity.inventory.getFirstEmptyStack(); // Should be
								// Inventory.firstEmpty()
								if (firstEmptySlot > -1) {
									action = InventoryAction.HOTBAR_MOVE_AND_READD;
								} else {
									action = InventoryAction.NOTHING; // This is not sane! Mojang: You should test for
									// other slots of same type
								}
							}
						} else if (!clickedSlot.getHasStack() && hotbar != null && clickedSlot.isItemValid(hotbar)) {
							action = InventoryAction.HOTBAR_SWAP;
						} else {
							action = InventoryAction.NOTHING;
						}
					} else {
						action = InventoryAction.NOTHING;
					}
					// Special constructor for number key
					event = new InventoryClickEvent(inventory, type, p_147351_1_.func_149544_d(), click, action,
							p_147351_1_.func_149543_e());
				}
			} else if (p_147351_1_.func_149542_h() == 3) {
				if (p_147351_1_.func_149543_e() == 2) {
					click = ClickType.MIDDLE;
					if (p_147351_1_.func_149544_d() == -999) {
						action = InventoryAction.NOTHING;
					} else {
						Slot slot = playerEntity.openContainer.getSlot(p_147351_1_.func_149544_d());
						if (slot != null && slot.getHasStack() && playerEntity.capabilities.isCreativeMode
								&& playerEntity.inventory.getItemStack() == null) {
							action = InventoryAction.CLONE_STACK;
						} else {
							action = InventoryAction.NOTHING;
						}
					}
				} else {
					click = ClickType.UNKNOWN;
					action = InventoryAction.UNKNOWN;
				}
			} else if (p_147351_1_.func_149542_h() == 4) {
				if (p_147351_1_.func_149544_d() >= 0) {
					if (p_147351_1_.func_149543_e() == 0) {
						click = ClickType.DROP;
						Slot slot = playerEntity.openContainer.getSlot(p_147351_1_.func_149544_d());
						if (slot != null && slot.getHasStack() && slot.canTakeStack(playerEntity)
								&& slot.getStack() != null
								&& slot.getStack().getItem() != Item.getItemFromBlock(Blocks.air)) {
							action = InventoryAction.DROP_ONE_SLOT;
						} else {
							action = InventoryAction.NOTHING;
						}
					} else if (p_147351_1_.func_149543_e() == 1) {
						click = ClickType.CONTROL_DROP;
						Slot slot = playerEntity.openContainer.getSlot(p_147351_1_.func_149544_d());
						if (slot != null && slot.getHasStack() && slot.canTakeStack(playerEntity)
								&& slot.getStack() != null
								&& slot.getStack().getItem() != Item.getItemFromBlock(Blocks.air)) {
							action = InventoryAction.DROP_ALL_SLOT;
						} else {
							action = InventoryAction.NOTHING;
						}
					}
				} else {
					// Sane default (because this happens when they are holding nothing. Don't ask
					// why.)
					click = ClickType.LEFT;
					if (p_147351_1_.func_149543_e() == 1) {
						click = ClickType.RIGHT;
					}
					action = InventoryAction.NOTHING;
				}
			} else if (p_147351_1_.func_149542_h() == 5) {
				itemstack = playerEntity.openContainer.slotClick(p_147351_1_.func_149544_d(),
						p_147351_1_.func_149543_e(), 5, playerEntity);
			} else if (p_147351_1_.func_149542_h() == 6) {
				click = ClickType.DOUBLE_CLICK;
				action = InventoryAction.NOTHING;
				if (p_147351_1_.func_149544_d() >= 0 && playerEntity.inventory.getItemStack() != null) {
					ItemStack cursor = playerEntity.inventory.getItemStack();
					action = InventoryAction.NOTHING;
					// Quick check for if we have any of the item
					// Cauldron start - can't call getContents() on modded IInventory; CB-added
					// method
					if (inventory.getTopInventory()
							.contains(org.bukkit.Material.getMaterial(Item.getIdFromItem(cursor.getItem())))
							|| inventory.getBottomInventory()
							.contains(org.bukkit.Material.getMaterial(Item.getIdFromItem(cursor.getItem())))) {
						action = InventoryAction.COLLECT_TO_CURSOR;
					}
					// Cauldron end
				}
			}
			// TODO check on updates
			if (p_147351_1_.func_149542_h() != 5) {
				if (click == ClickType.NUMBER_KEY) {
					event = new InventoryClickEvent(inventory, type, p_147351_1_.func_149544_d(), click, action,
							p_147351_1_.func_149543_e());
				} else {
					event = new InventoryClickEvent(inventory, type, p_147351_1_.func_149544_d(), click, action);
				}
				org.bukkit.inventory.Inventory top = inventory.getTopInventory();
				if (p_147351_1_.func_149544_d() == 0 && top instanceof CraftingInventory) {
					// Cauldron start - vanilla compatibility (mod recipes)
					org.bukkit.inventory.Recipe recipe = null;
					recipe = ((CraftingInventory) top).getRecipe();
					// Cauldron end

					if (recipe != null)
						if (click == ClickType.NUMBER_KEY) {
							event = new CraftItemEvent(recipe, inventory, type, p_147351_1_.func_149544_d(), click,
									action, p_147351_1_.func_149543_e());
						} else {
							event = new CraftItemEvent(recipe, inventory, type, p_147351_1_.func_149544_d(), click,
									action);
						}
				}
				Bukkit.getServer().getPluginManager().callEvent(event);
				switch (event.getResult()) {
					case ALLOW:
					case DEFAULT:
						itemstack = playerEntity.openContainer.slotClick(p_147351_1_.func_149544_d(),
								p_147351_1_.func_149543_e(), p_147351_1_.func_149542_h(), playerEntity);
						break;
					case DENY:
						switch (action) {
							// Modified other slots
							case PICKUP_ALL:
							case MOVE_TO_OTHER_INVENTORY:
							case HOTBAR_MOVE_AND_READD:
							case HOTBAR_SWAP:
							case COLLECT_TO_CURSOR:
							case UNKNOWN:
								playerEntity.sendContainerToPlayer(playerEntity.openContainer);
								break;
							// Modified cursor and clicked
							case PICKUP_SOME:
							case PICKUP_HALF:
							case PICKUP_ONE:
							case PLACE_ALL:
							case PLACE_SOME:
							case PLACE_ONE:
							case SWAP_WITH_CURSOR:
								playerEntity.playerNetServerHandler
										.sendPacket(new S2FPacketSetSlot(-1, -1, playerEntity.inventory.getItemStack()));
								playerEntity.playerNetServerHandler.sendPacket(
										new S2FPacketSetSlot(playerEntity.openContainer.windowId, p_147351_1_.func_149544_d(),
												playerEntity.openContainer.getSlot(p_147351_1_.func_149544_d()).getStack()));
								break;
							// Modified clicked only
							case DROP_ALL_SLOT:
							case DROP_ONE_SLOT:
								playerEntity.playerNetServerHandler.sendPacket(
										new S2FPacketSetSlot(playerEntity.openContainer.windowId, p_147351_1_.func_149544_d(),
												playerEntity.openContainer.getSlot(p_147351_1_.func_149544_d()).getStack()));
								break;
							// Modified cursor only
							case DROP_ALL_CURSOR:
							case DROP_ONE_CURSOR:
							case CLONE_STACK:
								playerEntity.playerNetServerHandler
										.sendPacket(new S2FPacketSetSlot(-1, -1, playerEntity.inventory.getItemStack()));
								break;
							// Nothing
							case NOTHING:
								break;
						}
						return;
				}
			}
			if (ItemStack.areItemStacksEqual(p_147351_1_.func_149546_g(), itemstack)) {
				playerEntity.playerNetServerHandler.sendPacket(new S32PacketConfirmTransaction(
						p_147351_1_.func_149548_c(), p_147351_1_.func_149547_f(), true));
				playerEntity.isChangingQuantityOnly = true;
				playerEntity.openContainer.detectAndSendChanges();
				playerEntity.updateHeldItem();
				playerEntity.isChangingQuantityOnly = false;
			} else {
				field_147372_n.addKey(playerEntity.openContainer.windowId, p_147351_1_.func_149547_f());
				playerEntity.playerNetServerHandler.sendPacket(new S32PacketConfirmTransaction(
						p_147351_1_.func_149548_c(), p_147351_1_.func_149547_f(), false));
				playerEntity.openContainer.setPlayerIsPresent(playerEntity, false);
				ArrayList<ItemStack> arraylist = new ArrayList<>();
				for (int i = 0; i < playerEntity.openContainer.inventorySlots.size(); ++i) {
					arraylist.add(((Slot) playerEntity.openContainer.inventorySlots.get(i)).getStack());
				}
				playerEntity.sendContainerAndContentsToPlayer(playerEntity.openContainer, arraylist);
			}
		}
	}

	// CraftBukkit start
	public void chat(String s, boolean async)
	{
		if (s.isEmpty() || this.playerEntity.func_147096_v() == EntityPlayer.EnumChatVisibility.HIDDEN)
		{
			return;
		}

		if (!async && s.startsWith("/"))
		{
			this.handleSlashCommand(s);
		}
		else if (this.playerEntity.func_147096_v() == EntityPlayer.EnumChatVisibility.SYSTEM)
		{
			// Do nothing, this is coming from a plugin
		}
		else
		{
			// Cauldron start - handle Forge event
			ChatComponentTranslation chatcomponenttranslation1 = new ChatComponentTranslation("chat.type.text", this.playerEntity.func_145748_c_(), s);
			chatcomponenttranslation1 = ForgeHooks.onServerChatEvent(this, s, chatcomponenttranslation1);

			if (chatcomponenttranslation1 != null
					&& chatcomponenttranslation1.getFormatArgs()[chatcomponenttranslation1.getFormatArgs().length - 1] instanceof String)
			{
				// use event message from Forge
				s = (String) chatcomponenttranslation1.getFormatArgs()[chatcomponenttranslation1.getFormatArgs().length - 1];
			}
			// Cauldron end
			Player player = this.getPlayerB();
			AsyncPlayerChatEvent event = new AsyncPlayerChatEvent(async, player, s, new LazyPlayerSet()); // Cauldron - pass changed message if any from Forge
			event.setCancelled(chatcomponenttranslation1 == null); // Cauldron - pre-cancel event if forge event was cancelled
			this.server.getPluginManager().callEvent(event);
			if (PlayerChatEvent.getHandlerList().getRegisteredListeners().length != 0)
			{
				// Evil plugins still listening to deprecated event
				final PlayerChatEvent queueEvent = new PlayerChatEvent(player, event.getMessage(), event.getFormat(), event.getRecipients());
				queueEvent.setCancelled(event.isCancelled());
				Waitable waitable = new Waitable() {
					@Override
					protected Object evaluate()
					{
						org.bukkit.Bukkit.getPluginManager().callEvent(queueEvent);

						if (queueEvent.isCancelled())
						{
							return null;
						}
						System.out.println("AAAA");
						String message = String.format(queueEvent.getFormat(), queueEvent.getPlayer().getDisplayName(), queueEvent.getMessage());
						NetHandlerPlayServer.this.serverController.console.sendMessage(message);
						if (((LazyPlayerSet) queueEvent.getRecipients()).isLazy())
						{
							for (Object recipient : serverController.getConfigurationManager().playerEntityList)
							{
								((EntityPlayerMP) recipient).sendMessage(CraftChatMessage.fromString(message));
							}
						}
						else
						{
							for (Player player : queueEvent.getRecipients())
							{
								player.sendMessage(message);
							}
						}

						return null;
					}
				};

				if (async)
				{
					serverController.processQueue.add(waitable);
				}
				else
				{
					System.out.println("serverController.processQueue");
					waitable.run();
				}

				try
				{
					System.out.println("serverController.processQueue");
					waitable.get();
				}
				catch (InterruptedException e)
				{
					Thread.currentThread().interrupt(); // This is proper habit for java. If we aren't handling it, pass it on!
				}
				catch (ExecutionException e)
				{
					throw new RuntimeException("Exception processing chat event", e.getCause());
				}
			}
			else
			{
				if (event.isCancelled())
				{
					return;
				}
				s = String.format(event.getFormat(), event.getPlayer().getDisplayName(), event.getMessage());
				if(serverController.console == null){
					serverController.console = new CraftConsoleCommandSender();
				}
				serverController.console.sendMessage(s);
				if (((LazyPlayerSet) event.getRecipients()).isLazy())
				{
					for (Object recipient : serverController.getConfigurationManager().playerEntityList)
					{
						for (IChatComponent component : CraftChatMessage.fromString(s))
						{
							((EntityPlayerMP) recipient).sendMessage(CraftChatMessage.fromString(s));
						}
					}
				}
				else
				{
					for (Player recipient : event.getRecipients())
					{
						recipient.sendMessage(s);
					}
				}
			}
		}
	}
	// CraftBukkit end

	@Override
	public void processEnchantItem(C11PacketEnchantItem p_147338_1_) {
		playerEntity.func_143004_u();

		if (playerEntity.openContainer.windowId == p_147338_1_.func_149539_c()
				&& playerEntity.openContainer.isPlayerNotUsingContainer(playerEntity)) {
			playerEntity.openContainer.enchantItem(playerEntity, p_147338_1_.func_149537_d());
			playerEntity.openContainer.detectAndSendChanges();
		}
	}

	@Override
	public void processCreativeInventoryAction(C10PacketCreativeInventoryAction creativeActionPacket) {
		if (playerEntity.theItemInWorldManager.isCreative()) {
			boolean flag = creativeActionPacket.func_149627_c() < 0;
			ItemStack itemstack = creativeActionPacket.func_149625_d();
			boolean flag1 = creativeActionPacket.func_149627_c() >= 1
					&& creativeActionPacket.func_149627_c() < 36 + InventoryPlayer.getHotbarSize();
			boolean flag2 = itemstack == null
					|| itemstack.getItem() != null && !invalidItems.contains(Item.getIdFromItem(itemstack.getItem()));
			boolean flag3 = itemstack == null
					|| itemstack.getItemDamage() >= 0 && itemstack.stackSize <= 64 && itemstack.stackSize > 0;
			if (flag || flag1 && !ItemStack.areItemStacksEqual(
					playerEntity.inventoryContainer.getSlot(creativeActionPacket.func_149627_c()).getStack(),
					creativeActionPacket.func_149625_d())) // Insist on valid slot
			{
				org.bukkit.entity.HumanEntity player = playerEntity.getBukkitEntity();
				InventoryView inventory = new CraftInventoryView(player, player.getInventory(),
						playerEntity.inventoryContainer);
				org.bukkit.inventory.ItemStack item = CraftItemStack.asBukkitCopy(creativeActionPacket.func_149625_d()); // Should
				// be
				// packet107setcreativeslot.newitem
				InventoryType.SlotType type = InventoryType.SlotType.QUICKBAR;
				if (flag) {
					type = InventoryType.SlotType.OUTSIDE;
				} else if (creativeActionPacket.func_149627_c() < 36)
					if (creativeActionPacket.func_149627_c() >= 5 && creativeActionPacket.func_149627_c() < 9) {
						type = InventoryType.SlotType.ARMOR;
					} else {
						type = InventoryType.SlotType.CONTAINER;
					}
				InventoryCreativeEvent event = new InventoryCreativeEvent(inventory, type,
						flag ? -999 : creativeActionPacket.func_149627_c(), item);
				Bukkit.getServer().getPluginManager().callEvent(event);
				itemstack = CraftItemStack.asNMSCopy(event.getCursor());
				switch (event.getResult()) {
					case ALLOW:
						// Plugin cleared the id / stacksize checks
						flag2 = flag3 = true;
						break;
					case DEFAULT:
						break;
					case DENY:
						// Reset the slot
						if (creativeActionPacket.func_149627_c() >= 0) {
							playerEntity.playerNetServerHandler
									.sendPacket(new S2FPacketSetSlot(playerEntity.inventoryContainer.windowId,
											creativeActionPacket.func_149627_c(), playerEntity.inventoryContainer
											.getSlot(creativeActionPacket.func_149627_c()).getStack()));
							playerEntity.playerNetServerHandler.sendPacket(new S2FPacketSetSlot(-1, -1, null));
						}
						return;
				}
			}
			if (flag1 && flag2 && flag3) {
				playerEntity.inventoryContainer.putStackInSlot(creativeActionPacket.func_149627_c(), itemstack);
				playerEntity.inventoryContainer.setPlayerIsPresent(playerEntity, true);
			} else if (flag && flag2 && flag3 && field_147375_m < 200) {
				field_147375_m += 20;
				EntityItem entityitem = playerEntity.dropPlayerItemWithRandomChoice(itemstack, true);
				if (entityitem != null) {
					entityitem.setAgeToCreativeDespawnTime();
				}
			}
		}

	}

	@Override
	public void processConfirmTransaction(C0FPacketConfirmTransaction p_147339_1_) {
		Short oshort = (Short) field_147372_n.lookup(playerEntity.openContainer.windowId);

		if (oshort != null && p_147339_1_.func_149533_d() == oshort.shortValue()
				&& playerEntity.openContainer.windowId == p_147339_1_.func_149532_c()
				&& !playerEntity.openContainer.isPlayerNotUsingContainer(playerEntity)) {
			playerEntity.openContainer.setPlayerIsPresent(playerEntity, true);
		}
	}

	@Override
	public void processUpdateSign(C12PacketUpdateSign p_147343_1_) {
		playerEntity.func_143004_u();
		WorldServer worldserver = serverController.worldServerForDimension(playerEntity.dimension);

		if (worldserver.blockExists(p_147343_1_.func_149588_c(), p_147343_1_.func_149586_d(),
				p_147343_1_.func_149585_e())) {
			TileEntity tileentity = worldserver.getTileEntity(p_147343_1_.func_149588_c(), p_147343_1_.func_149586_d(),
					p_147343_1_.func_149585_e());

			if (tileentity instanceof TileEntitySign) {
				TileEntitySign tileentitysign = (TileEntitySign) tileentity;

				if (!tileentitysign.func_145914_a() || tileentitysign.func_145911_b() != playerEntity) {
					serverController.logWarning("Player " + playerEntity.getCommandSenderName()
							+ " just tried to change non-editable sign");
					return;
				}
			}

			int i;
			int j;

			for (j = 0; j < 4; ++j) {
				boolean flag = true;

				if (p_147343_1_.func_149589_f()[j].length() > 15) {
					flag = false;
				} else {
					for (i = 0; i < p_147343_1_.func_149589_f()[j].length(); ++i) {
						if (!ChatAllowedCharacters.isAllowedCharacter(p_147343_1_.func_149589_f()[j].charAt(i))) {
							flag = false;
						}
					}
				}

				if (!flag) {
					p_147343_1_.func_149589_f()[j] = "!?";
				}
			}

			if (tileentity instanceof TileEntitySign) {
				j = p_147343_1_.func_149588_c();
				p_147343_1_.func_149586_d();
				i = p_147343_1_.func_149585_e();
				TileEntitySign tileentitysign1 = (TileEntitySign) tileentity;

				int x = p_147343_1_.func_149588_c();
				int y = p_147343_1_.func_149586_d();
				int z = p_147343_1_.func_149585_e();
				// CraftBukkit start
				Player player = getPlayerB();
				SignChangeEvent event = new SignChangeEvent(player.getWorld().getBlockAt(x, y, z), player,
						p_147343_1_.func_149589_f());
				Bukkit.getServer().getPluginManager().callEvent(event);

				if (!event.isCancelled()) {
					for (int l = 0; l < 4; ++l) {
						tileentitysign1.signText[l] = event.getLine(l);

						if (tileentitysign1.signText[l] == null) {
							tileentitysign1.signText[l] = "";
						}
					}

					// tileentitysign1.field_145916_j = false;
				}

				// System.arraycopy(p_147343_1_.func_149589_f(), 0, tileentitysign1.signText, 0,
				// 4);
				// CraftBukkit end
				tileentitysign1.markDirty();
				playerEntity.worldObj.markBlockForUpdate(x, y, z);

				return;
			}
		}
	}

	@Override
	public void processKeepAlive(C00PacketKeepAlive p_147353_1_) {
		if (p_147353_1_.func_149460_c() == field_147378_h) {
			int i = (int) (func_147363_d() - field_147379_i);
			playerEntity.ping = (playerEntity.ping * 3 + i) / 4;
		}
	}

	private long func_147363_d() {
		return System.nanoTime() / 1000000L;
	}

	@Override
	public void processPlayerAbilities(C13PacketPlayerAbilities p_147348_1_) {
		if(this.playerEntity.capabilities.allowFlying&&this.playerEntity.capabilities.isFlying!=p_147348_1_.func_149488_d()) {

			//TODO Странный запрос Bukkit.getPlayer не принял EntityPlayerMP
			PlayerToggleFlightEvent event = new PlayerToggleFlightEvent(Bukkit.getPlayer(this.playerEntity.getUniqueID()), p_147348_1_.func_149488_d());
			Bukkit.getPluginManager().callEvent(event);

			if (!event.isCancelled()) {
				this.playerEntity.capabilities.isFlying = p_147348_1_.func_149488_d(); // Actually set the player's flying status
			} else {
				this.playerEntity.sendPlayerAbilities(); // Tell the player their ability was reverted
			}
		}
	}

	@Override
	public void processTabComplete(C14PacketTabComplete p_147341_1_) {
		ArrayList arraylist = Lists.newArrayList();
		Iterator iterator = serverController.getPossibleCompletions(playerEntity, p_147341_1_.func_149419_c())
				.iterator();

		while (iterator.hasNext()) {
			String s = (String) iterator.next();
			arraylist.add(s);
		}

		playerEntity.playerNetServerHandler
				.sendPacket(new S3APacketTabComplete((String[]) arraylist.toArray(new String[arraylist.size()])));
	}

	@Override
	public void processClientSettings(C15PacketClientSettings p_147352_1_) {
		playerEntity.func_147100_a(p_147352_1_);
	}

	@Override
	public void processVanilla250Packet(C17PacketCustomPayload p_147349_1_) {

		if (p_147349_1_.func_149558_e() != null) {
			Bukkit.getServer().getMessenger().dispatchIncomingMessage(getPlayerB(), p_147349_1_.func_149559_c(),
					p_147349_1_.func_149558_e());
		}
		if ("MC|BEdit".equals(p_147349_1_.func_149559_c())) {
			PacketBuffer packetbuffer = new PacketBuffer(Unpooled.wrappedBuffer(p_147349_1_.func_149558_e()));
			try {
				ItemStack itemStackFromPacket = packetbuffer.readItemStackFromBuffer();
				if (itemStackFromPacket == null)
					return;
				if (!ItemWritableBook.func_150930_a(itemStackFromPacket.getTagCompound()))
					throw new IOException("Invalid book tag!");
				ItemStack itemStackInHand = playerEntity.inventory.getCurrentItem();
				if (itemStackInHand == null)
					return;
				if (itemStackFromPacket.getItem() == Items.writable_book
						&& itemStackFromPacket.getItem() == itemStackInHand.getItem()) {
					ItemStack clearItemStack = itemStackInHand.copy();
					ItemStack copyItemStack = itemStackInHand.copy();
					copyItemStack.setTagInfo("pages", itemStackFromPacket.getTagCompound().getTagList("pages", 8));
					PlayerEditBookEvent editBookEvent = CraftEventFactory.handleEditBookEvent(playerEntity,
							copyItemStack, clearItemStack);
					if (!editBookEvent.isCancelled()) {
						itemStackInHand.setTagInfo("pages",
								itemStackFromPacket.getTagCompound().getTagList("pages", 8));
						CraftItemStack.setItemMeta(itemStackInHand, editBookEvent.getNewBookMeta());
						if (editBookEvent.isSigning()) {
							itemStackInHand.func_150996_a(net.minecraft.init.Items.written_book);
						}
					}
					Slot slot = playerEntity.openContainer.getSlotFromInventory(playerEntity.inventory,
							playerEntity.inventory.currentItem);
					playerEntity.playerNetServerHandler.sendPacket(new S2FPacketSetSlot(
							playerEntity.openContainer.windowId, slot.slotNumber, itemStackInHand));
				}
			} catch (Exception var39) {
				logger.error("Couldn't handle book info", var39);
			} finally {
				packetbuffer.release();
			}

		} else if ("MC|BSign".equals(p_147349_1_.func_149559_c())) {
			PacketBuffer packetbuffer = new PacketBuffer(Unpooled.wrappedBuffer(p_147349_1_.func_149558_e()));
			try {
				ItemStack itemStackFromPacket = packetbuffer.readItemStackFromBuffer();
				if (itemStackFromPacket != null) {
					if (!ItemEditableBook.validBookTagContents(itemStackFromPacket.getTagCompound()))
						throw new IOException("Invalid book tag!");
					ItemStack itemStackInHand = playerEntity.inventory.getCurrentItem();
					if (itemStackInHand == null)
						return;
					if (itemStackFromPacket.getItem() == Items.written_book
							&& itemStackInHand.getItem() == Items.writable_book) {
						ItemStack clearItemStack = itemStackInHand.copy();
						ItemStack copyItemStack = itemStackInHand.copy();
						copyItemStack.setTagInfo("author", new NBTTagString(playerEntity.getCommandSenderName()));
						copyItemStack.setTagInfo("title",
								new NBTTagString(itemStackFromPacket.getTagCompound().getString("title")));
						copyItemStack.setTagInfo("pages", itemStackFromPacket.getTagCompound().getTagList("pages", 8));
						copyItemStack.func_150996_a(Items.written_book);
						PlayerEditBookEvent editBookEvent = CraftEventFactory.handleEditBookEvent(playerEntity,
								copyItemStack, clearItemStack);
						if (!editBookEvent.isCancelled()) {
							if (editBookEvent.isSigning()) {
								itemStackInHand.setTagInfo("author",
										new NBTTagString(playerEntity.getCommandSenderName()));
								itemStackInHand.setTagInfo("title",
										new NBTTagString(itemStackFromPacket.getTagCompound().getString("title")));
								itemStackInHand.setTagInfo("pages",
										itemStackFromPacket.getTagCompound().getTagList("pages", 8));
								itemStackInHand.func_150996_a(net.minecraft.init.Items.written_book);
							}
							if (!CraftItemStack.getItemMeta(copyItemStack).equals(editBookEvent.getNewBookMeta())) {
								CraftItemStack.setItemMeta(itemStackInHand, editBookEvent.getNewBookMeta());
							}
						}
						Slot slot = playerEntity.openContainer.getSlotFromInventory(playerEntity.inventory,
								playerEntity.inventory.currentItem);
						playerEntity.playerNetServerHandler.sendPacket(new S2FPacketSetSlot(
								playerEntity.openContainer.windowId, slot.slotNumber, itemStackInHand));
					}
				}
			} catch (Exception var41) {
				logger.error("Couldn't sign book", var41);
			} finally {
				packetbuffer.release();
			}

		} else {
			DataInputStream datainputstream;
			int i;
			if ("MC|TrSel".equals(p_147349_1_.func_149559_c())) {
				try {
					datainputstream = new DataInputStream(new ByteArrayInputStream(p_147349_1_.func_149558_e()));
					i = datainputstream.readInt();
					Container container = playerEntity.openContainer;
					if (container instanceof ContainerMerchant) {
						((ContainerMerchant) container).setCurrentRecipeIndex(i);
					}
				} catch (Exception var38) {
					logger.error("Couldn't select trade", var38);
				}
			} else if ("MC|AdvCdm".equals(p_147349_1_.func_149559_c())) {
				if (!serverController.isCommandBlockEnabled()) {
					playerEntity.addChatMessage(new ChatComponentTranslation("advMode.notEnabled"));
				} else if (playerEntity.canCommandSenderUseCommand(2, "") && playerEntity.capabilities.isCreativeMode) {
					PacketBuffer packetbuffer = new PacketBuffer(Unpooled.wrappedBuffer(p_147349_1_.func_149558_e()));

					try {
						byte b0 = packetbuffer.readByte();
						CommandBlockLogic commandblocklogic = null;
						if (b0 == 0) {
							TileEntity tileentity = playerEntity.worldObj.getTileEntity(packetbuffer.readInt(),
									packetbuffer.readInt(), packetbuffer.readInt());
							if (tileentity instanceof TileEntityCommandBlock) {
								commandblocklogic = ((TileEntityCommandBlock) tileentity).func_145993_a();
							}
						} else if (b0 == 1) {
							Entity entity = playerEntity.worldObj.getEntityByID(packetbuffer.readInt());
							if (entity instanceof EntityMinecartCommandBlock) {
								commandblocklogic = ((EntityMinecartCommandBlock) entity).func_145822_e();
							}
						}

						String s1 = packetbuffer.readStringFromBuffer(packetbuffer.readableBytes());
						if (commandblocklogic != null) {
							commandblocklogic.func_145752_a(s1);
							commandblocklogic.func_145756_e();
							playerEntity.addChatMessage(new ChatComponentTranslation("advMode.setCommand.success", s1));
						}
					} catch (Exception var36) {
						logger.error("Couldn't set command block", var36);
					} finally {
						packetbuffer.release();
					}
				} else {
					playerEntity.addChatMessage(new ChatComponentTranslation("advMode.notAllowed"));
				}
			} else if ("MC|Beacon".equals(p_147349_1_.func_149559_c())) {
				if (playerEntity.openContainer instanceof ContainerBeacon) {
					try {
						datainputstream = new DataInputStream(new ByteArrayInputStream(p_147349_1_.func_149558_e()));
						i = datainputstream.readInt();
						int j = datainputstream.readInt();
						ContainerBeacon containerbeacon = (ContainerBeacon) playerEntity.openContainer;
						Slot slot = containerbeacon.getSlot(0);
						if (slot.getHasStack()) {
							slot.decrStackSize(1);
							TileEntityBeacon tileentitybeacon = containerbeacon.func_148327_e();
							tileentitybeacon.setPrimaryEffect(i);
							tileentitybeacon.setSecondaryEffect(j);
							tileentitybeacon.markDirty();
						}
					} catch (Exception var35) {
						logger.error("Couldn't set beacon", var35);
					}
				}
			} else if ("MC|ItemName".equals(p_147349_1_.func_149559_c())
					&& playerEntity.openContainer instanceof ContainerRepair) {
				ContainerRepair containerrepair = (ContainerRepair) playerEntity.openContainer;
				if (p_147349_1_.func_149558_e() != null && p_147349_1_.func_149558_e().length >= 1) {
					String s = ChatAllowedCharacters
							.filerAllowedCharacters(new String(p_147349_1_.func_149558_e(), Charsets.UTF_8));
					if (s.length() <= 30) {
						containerrepair.updateItemName(s);
					}
				} else {
					containerrepair.updateItemName("");
				}
			}
		}
	}

	@Override
	public void onConnectionStateTransition(EnumConnectionState p_147232_1_, EnumConnectionState p_147232_2_) {
		if (p_147232_2_ != EnumConnectionState.PLAY)
			throw new IllegalStateException("Unexpected change in protocol!");
	}

	static final class SwitchEnumState {
		static final int[] field_151290_a = new int[C16PacketClientStatus.EnumState.values().length];
		private static final String __OBFID = "CL_00001455";

		static {
			try {
				field_151290_a[C16PacketClientStatus.EnumState.PERFORM_RESPAWN.ordinal()] = 1;
			} catch (NoSuchFieldError var3) {
			}

			try {
				field_151290_a[C16PacketClientStatus.EnumState.REQUEST_STATS.ordinal()] = 2;
			} catch (NoSuchFieldError var2) {
			}

			try {
				field_151290_a[C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT.ordinal()] = 3;
			} catch (NoSuchFieldError var1) {
			}
		}
	}
}