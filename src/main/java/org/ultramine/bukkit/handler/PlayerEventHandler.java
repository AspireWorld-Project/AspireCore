package org.ultramine.bukkit.handler;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.world.WorldServer;
import net.minecraftforge.event.world.WorldEvent;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.util.CraftChatMessage;
import org.bukkit.craftbukkit.util.LazyPlayerSet;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.ultramine.advanced.BetterTeleporter;
import org.ultramine.bukkit.CraftPlayerCache;
import org.ultramine.core.service.InjectService;
import org.ultramine.server.event.PlayerSwingItemEvent;

import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;

public class PlayerEventHandler {
	@InjectService
	private static CraftPlayerCache cPlayerCache;
	private final CraftServer server;

	public PlayerEventHandler(CraftServer server) {
		this.server = server;
	}

	@SubscribeEvent
	public void onPlayerClone(net.minecraftforge.event.entity.player.PlayerEvent.Clone e) {
		EntityPlayerMP from = (EntityPlayerMP) e.original;
		EntityPlayerMP to = (EntityPlayerMP) e.entityPlayer;

		if (from.isKeepLevel()) {
			to.experience = from.experience;
			to.experienceLevel = from.experienceLevel;
			to.experienceTotal = from.experienceTotal;
		} else {
			to.experience = 0;
			to.experienceLevel = from.getNewLevel();
			to.experienceTotal = from.getNewTotalExp();
			to.addExperience(from.getNewExp());
		}
		from.setKeepLevel(false);
	}

	@SubscribeEvent(priority = EventPriority.LOW)
	public void getPlayerDisplayName(net.minecraftforge.event.entity.player.PlayerEvent.NameFormat e) {
		CraftPlayer player = (CraftPlayer) e.entity.getBukkitEntity();
		String displayName = player.getOverridenDisplayName();
		if (displayName != null) {
			e.displayname = displayName;
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent e) // TODO support for custom join messages
	{
		server.getPluginManager().callEvent(new PlayerJoinEvent((Player) e.player.getBukkitEntity(),
				"\u00A7e" + e.player.getCommandSenderName() + " joined the game."));
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent e) // TODO support for custom quit messages
	{
		EntityPlayerMP player = (EntityPlayerMP) e.player;
		PlayerQuitEvent playerQuitEvent = null;
		if (player.playerNetServerHandler != null) {
			// CraftBukkit start - Quitting must be before we do final save of data, in case
			// plugins need to modify it
			CraftEventFactory.handleInventoryCloseEvent(player);
			Player bp = server.getPlayer(player);
			playerQuitEvent = new PlayerQuitEvent(bp, "\u00A7e" + player.getCommandSenderName() + " left the game.");
			server.getPluginManager().callEvent(playerQuitEvent);
			((CraftPlayer) bp).disconnect(playerQuitEvent.getQuitMessage());
			// CraftBukkit end
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent e) {
		PlayerChangedWorldEvent event = new PlayerChangedWorldEvent((Player) e.player.getBukkitEntity(),
				MinecraftServer.getServer().worldServerForDimension(e.fromDim).getWorld());
		server.getPluginManager().callEvent(event);
	}

	// Cauldron is fucked up too with PlayerPortalEvent

	// @SubscribeEvent(priority = EventPriority.LOWEST)
	// public void onPlayerDeath(PreDimChangeEvent e)
	// {
	// if(e.isVanilla())
	// {
	// EntityPlayerMP player = e.getPlayer();
	// int dimTo = e.getDimTo();
	// Teleporter teleporter = e.getTeleporter();
	// TeleportCause cause = player.dimension == 1 || dimTo == 1 ?
	// TeleportCause.END_PORTAL :
	// player.dimension == 0 && dimTo == -1 || player.dimension == -1 && dimTo == 0
	// ? TeleportCause.NETHER_PORTAL : TeleportCause.MOD;
	// WorldServer fromWorld =
	// MinecraftServer.getServer().worldServerForDimension(player.dimension);
	// WorldServer exitWorld =
	// MinecraftServer.getServer().worldServerForDimension(dimTo);
	//
	// // CraftBukkit start - Replaced the standard handling of portals with a more
	// customised method.
	// org.bukkit.entity.Entity bentity = ((IMixinEntity) player).getBukkitEntity();
	// Location enter = bentity.getLocation();
	// Location exit = null;
	// boolean useTravelAgent = false;
	//
	// if (exitWorld != null)
	// {
	// exit = this.calculateTarget(enter, exitWorld);
	// if (cause != cause.MOD) // don't use travel agent for custom dimensions
	// {
	// useTravelAgent = true;
	// }
	// }
	//
	// // allow forge mods to be the teleporter
	// TravelAgent agent = null;
	// if (exit != null && teleporter == null)
	// {
	// teleporter = ((CraftWorld)
	// exit.getWorld()).getHandle().getDefaultTeleporter();
	// if (teleporter instanceof TravelAgent)
	// {
	// agent = (TravelAgent) teleporter;
	// }
	// }
	// else
	// {
	// if (teleporter instanceof TravelAgent)
	// {
	// agent = (TravelAgent) teleporter;
	// }
	// }
	// if (agent == null) // mod teleporter such as Twilight Forest
	// {
	// agent = org.bukkit.craftbukkit.CraftTravelAgent.DEFAULT; // return arbitrary
	// TA to compensate for implementation dependent plugins
	// }
	//
	// PlayerPortalEvent event = new PlayerPortalEvent((Player)bentity, enter, exit,
	// agent, cause);
	// event.useTravelAgent(useTravelAgent);
	// Bukkit.getServer().getPluginManager().callEvent(event);
	//
	// if (event.isCancelled() || event.getTo() == null)
	// {
	// return;
	// }
	//
	// exit = event.useTravelAgent() && cause != cause.MOD ?
	// event.getPortalTravelAgent().findOrCreate(event.getTo()) : event.getTo(); //
	// make sure plugins don't override travelagent for mods
	//
	// if (exit == null)
	// {
	// return;
	// }
	// }
	// }

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onPlayerDeath(org.ultramine.server.event.PlayerDeathEvent e) {
		EntityPlayerMP player = (EntityPlayerMP) e.entityPlayer;
		boolean keepInventory = e.isKeepInventory();
		List<org.bukkit.inventory.ItemStack> loot = new ArrayList<>();
		if (!keepInventory && e.isProcessDrops()) {
			// Cauldron start - rework CraftBukkit logic to support Forge better
			player.captureDrops = true;
			player.capturedDrops.clear();
			player.inventory.dropAllItemsWithoutClear();
			for (int i = 0; i < player.capturedDrops.size(); ++i) {
				if (player.capturedDrops.get(i) != null) {
					loot.add(CraftItemStack.asCraftMirror(player.capturedDrops.get(i).getEntityItem()));
				}
			}
			// Cauldron end
		}

		IChatComponent chatmessage = e.getDeathMessage();
		String deathmessage = chatmessage == null ? null : chatmessage.getUnformattedText();
		org.bukkit.event.entity.PlayerDeathEvent event = CraftEventFactory.callPlayerDeathEvent(player, loot,
				deathmessage, keepInventory);
		String deathMessage = event.getDeathMessage();

		if (deathMessage == null || deathMessage.isEmpty()) {
			e.setDeathMessage(null);
		} else if (!deathMessage.equals(deathmessage)) {
			e.setDeathMessage(null);
			for (IChatComponent comp : CraftChatMessage.fromString(deathMessage)) {
				MinecraftServer.getServer().getConfigurationManager().sendChatMsg(comp);
			}
		}

		if (!event.getKeepInventory() && e.isProcessDrops()) {
			// Cauldron start - rework CraftBukkit logic to support Forge better
			player.inventory.clearInventory(null, -1); // CraftBukkit - we clean the player's inventory after the
														// EntityDeathEvent is called so plugins can get the exact state
														// of the inventory.
			player.captureDrops = false;
			PlayerDropsEvent forgeEvent = new PlayerDropsEvent(player, e.damageSource, player.capturedDrops,
					player.getRecentlyHit() > 0);

			if (!MinecraftForge.EVENT_BUS.post(forgeEvent)) {
				for (EntityItem item : player.capturedDrops) {
					player.joinEntityItemWithWorld(item);
				}
			}
			// Cauldron end
		}

		if (player.captureDrops) {
			player.captureDrops = false;
		}
		player.closeScreen();
		e.setKeepInventory(event.getKeepInventory());
		e.setProcessDrops(false);
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent e) {
		cPlayerCache.updateReferences((EntityPlayerMP) e.player);
		e.player.setHealth(e.player.getMaxHealth());

		org.bukkit.entity.Entity entity = e.player.getBukkitEntity();
		PlayerRespawnEvent respawnEvent = new PlayerRespawnEvent((Player) e.player.getBukkitEntity(),
				entity.getLocation(), false);
		server.getPluginManager().callEvent(respawnEvent);
	}

	@SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
	public void onPlayerInteractEvent_RightClickAir(PlayerInteractEvent e) {
		// Cauldron calls bukkit event after forge (so lowest priority), ignores
		// cancellation status
		// and overrides deny result. It may be an issue on private regions (protected
		// with Forge) and
		// custom plugins right click behavior.
		if (e.action == Action.RIGHT_CLICK_AIR) {
			ItemStack itemstack = e.entityPlayer.inventory.getCurrentItem();
			org.bukkit.event.player.PlayerInteractEvent event = CraftEventFactory
					.callPlayerInteractEvent(e.entityPlayer, org.bukkit.event.block.Action.RIGHT_CLICK_AIR, itemstack);
			if (event.useItemInHand() == org.bukkit.event.Event.Result.DENY) {
				e.useItem = Result.DENY;
			}
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onPlayerSwingItem(PlayerSwingItemEvent e) {
		EntityPlayerMP playerEntity = (EntityPlayerMP) e.entityPlayer;
		float f = 1.0F;
		float f1 = playerEntity.prevRotationPitch + (playerEntity.rotationPitch - playerEntity.prevRotationPitch) * f;
		float f2 = playerEntity.prevRotationYaw + (playerEntity.rotationYaw - playerEntity.prevRotationYaw) * f;
		double d0 = playerEntity.prevPosX + (playerEntity.posX - playerEntity.prevPosX) * f;
		double d1 = playerEntity.prevPosY + (playerEntity.posY - playerEntity.prevPosY) * f + 1.62D
				- playerEntity.yOffset;
		double d2 = playerEntity.prevPosZ + (playerEntity.posZ - playerEntity.prevPosZ) * f;
		Vec3 vec3 = Vec3.createVectorHelper(d0, d1, d2);
		float f3 = MathHelper.cos(-f2 * 0.017453292F - (float) Math.PI);
		float f4 = MathHelper.sin(-f2 * 0.017453292F - (float) Math.PI);
		float f5 = -MathHelper.cos(-f1 * 0.017453292F);
		float f6 = MathHelper.sin(-f1 * 0.017453292F);
		float f7 = f4 * f5;
		float f8 = f3 * f5;
		double d3 = 5.0D;
		Vec3 vec31 = vec3.addVector(f7 * d3, f6 * d3, f8 * d3);
		MovingObjectPosition movingobjectposition = playerEntity.worldObj.rayTraceBlocks(vec3, vec31, true);
		boolean valid = false;

		if (movingobjectposition == null
				|| movingobjectposition.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK) {
			valid = true;
		} else {
			Block block = playerEntity.worldObj.getBlock(movingobjectposition.blockX, movingobjectposition.blockY,
					movingobjectposition.blockZ);

			if (!block.isOpaqueCube()) // Should be isBreakable?
			{
				valid = true;
			}
		}

		if (valid) {
			CraftEventFactory.callPlayerInteractEvent(playerEntity, org.bukkit.event.block.Action.LEFT_CLICK_AIR,
					playerEntity.inventory.getCurrentItem());
		}
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onEntityItemPickup(EntityItemPickupEvent e) {
		ItemStack itemstack = e.item.getEntityItem();

		int canHold = e.entityPlayer.inventory.canHold(itemstack);
		int remaining = itemstack.stackSize - canHold;

		if (e.item.delayBeforeCanPickup <= 0 && canHold > 0) {
			itemstack.stackSize = canHold;
			// Cauldron start - rename to cbEvent to fix naming collision
			PlayerPickupItemEvent cbEvent = new PlayerPickupItemEvent(
					(org.bukkit.entity.Player) e.entityPlayer.getBukkitEntity(),
					(org.bukkit.entity.Item) e.item.getBukkitEntity(), remaining);
			// cbEvent.setCancelled(!par1EntityPlayer.canPickUpLoot); TODO
			server.getPluginManager().callEvent(cbEvent);
			itemstack.stackSize = canHold + remaining;

			if (cbEvent.isCancelled()) {
				e.setCanceled(true);
				return;
			}
			// Cauldron end

			// Possibly < 0; fix here so we do not have to modify code below
			e.item.delayBeforeCanPickup = 0;
		}
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onServerChatEvent(final ServerChatEvent e) {
		ChatComponentTranslation component = e.component;
		if (component == null)
			return;
		component.getFormatArgs();
		// IChatComponent oldMsg = args.length == 0 ? component : args[args.length - 1]
		// instanceof IChatComponent ? (IChatComponent) args[args.length - 1] : null;
		// TODO
		final String toBukkitMsg = e.message;// args.length == 0 ? component.getFormattedText() :
												// toMessage(args[args.length - 1]);
		final String oldFormat = "%s\u00A77: %s";
		Player player = (Player) e.player.getBukkitEntity();
		AsyncPlayerChatEvent event = new AsyncPlayerChatEvent(false, player, toBukkitMsg, new LazyPlayerSet());
		event.setFormat(oldFormat);
		server.getPluginManager().callEvent(event);

		if (PlayerChatEvent.getHandlerList().getRegisteredListeners().length != 0) {
			// Evil plugins still listening to deprecated event (but we are sync, so no
			// problems)
			final PlayerChatEvent legacyEvent = new PlayerChatEvent(player, event.getMessage(), event.getFormat(),
					event.getRecipients());
			legacyEvent.setFormat(event.getFormat());
			legacyEvent.setCancelled(event.isCancelled());

			org.bukkit.Bukkit.getPluginManager().callEvent(legacyEvent);

			if (legacyEvent.isCancelled()) {
				e.setCanceled(true);
				return;
			}

			if (oldFormat.equals(legacyEvent.getFormat()) && toBukkitMsg.equals(event.getMessage()))
				return;

			String newMsg = String.format(event.getFormat(), event.getPlayer().getDisplayName(), event.getMessage());
			if (((LazyPlayerSet) legacyEvent.getRecipients()).isLazy()) {
				for (IChatComponent component1 : CraftChatMessage.fromString(newMsg)) {
					MinecraftServer.getServer().getConfigurationManager().sendChatMsgImpl(component1, false);
				}
			} else {
				for (IChatComponent component1 : CraftChatMessage.fromString(newMsg)) {
					MinecraftServer.getServer().addChatMessage(component1);
				}
				for (Player player1 : legacyEvent.getRecipients()) {
					player1.sendMessage(newMsg);
				}
			}
		} else {
			if (event.isCancelled()) {
				e.setCanceled(true);
				return;
			}

			if (oldFormat.equals(event.getFormat()) && toBukkitMsg.equals(event.getMessage()))
				return;

			String newMsg = String.format(event.getFormat(), event.getPlayer().getDisplayName(), event.getMessage());
			if (((LazyPlayerSet) event.getRecipients()).isLazy()) {
				for (IChatComponent component1 : CraftChatMessage.fromString(newMsg)) {
					MinecraftServer.getServer().getConfigurationManager().sendChatMsgImpl(component1, false);
				}
			} else {
				for (IChatComponent component1 : CraftChatMessage.fromString(newMsg)) {
					MinecraftServer.getServer().addChatMessage(component1);
				}
				for (Player recipient : event.getRecipients()) {
					recipient.sendMessage(newMsg);
				}
			}
		}

		e.setCanceled(true);
	}

	@SuppressWarnings("unused")
	private String toMessage(Object obj) {
		if (obj == null)
			return "null";
		if (obj instanceof IChatComponent)
			return ((IChatComponent) obj).getFormattedText();
		return obj.toString();
	}

	/* ==============================================S5A4ED1SA7================================================== */
	@SubscribeEvent
	public void onWorldLoad(WorldEvent.Load event) {
		if(event.world instanceof WorldServer) {
			if(event.world.provider.dimensionId == 0 || event.world.provider.dimensionId == -1) {
				((WorldServer) event.world).worldTeleporter = new BetterTeleporter((WorldServer) event.world);
			}
		}
	}
	// Улучшенный телепорт. Более быстрое перемещение между мирами + телепорт не в сам портал, а в +2 блока от него.

}
