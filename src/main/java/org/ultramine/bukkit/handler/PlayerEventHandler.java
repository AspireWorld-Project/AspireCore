package org.ultramine.bukkit.handler;

import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.util.CraftChatMessage;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.ultramine.bukkit.CraftPlayerCache;
import org.ultramine.core.service.InjectService;
import org.ultramine.server.event.PlayerSwingItemEvent;

import java.util.ArrayList;
import java.util.List;

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

	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent e) // TODO support for custom quit messages
	{

	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent e) {
		PlayerChangedWorldEvent event = new PlayerChangedWorldEvent((Player) e.player.getBukkitEntity(),
				MinecraftServer.getServer().worldServerForDimension(e.fromDim).getWorld());
		server.getPluginManager().callEvent(event);
	}
	
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

	@SuppressWarnings("unused")
	private String toMessage(Object obj) {
		if (obj == null)
			return "null";
		if (obj instanceof IChatComponent)
			return ((IChatComponent) obj).getFormattedText();
		return obj.toString();
	}
}
