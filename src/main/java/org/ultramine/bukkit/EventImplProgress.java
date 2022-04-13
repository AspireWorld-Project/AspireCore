package org.ultramine.bukkit;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;

@SuppressWarnings("deprecation")
public class EventImplProgress {
	private static final Logger log = LogManager.getLogger();
	private static final Map<Class<? extends Event>, Boolean> EVENTS_MAP = new HashMap<>();

	private EventImplProgress() {
	}

	public static void checkEventImplemented(Class<? extends Event> cls, Plugin p) {
		if (!isEventImplemented(cls)) {
			log.error(p + ": Bukkit event " + cls.getName() + " is not implemented in UltraMine bukkit impl");
		}
	}

	public static void checkEventImplemented(Class<? extends Event> cls) {
		if (!isEventImplemented(cls)) {
			log.error("Bukkit event " + cls.getName() + " is not implemented in UltraMine bukkit impl");
		}
	}

	public static boolean isEventImplemented(Class<? extends Event> cls) {
		Boolean is = EVENTS_MAP.get(cls);
		return is == null || is;
	}

	private static void reg(boolean isImplemented, Class<? extends Event> cls) {
		EVENTS_MAP.put(cls, isImplemented);
	}

	private static void printStats() {
		int implementedCount = 0;
		for (Boolean b : EVENTS_MAP.values())
			if (b) {
				implementedCount++;
			}
		log.info("================ Bukkit event implementation progress ===============");
		log.info("Total: {}, Implemented: {}, Not implemented: {}, Progress: {}%", EVENTS_MAP.size(), implementedCount,
				EVENTS_MAP.size() - implementedCount, (int) (implementedCount / (double) EVENTS_MAP.size() * 100));
		log.info("=====================================================================");
	}

	static {
		reg(true, org.bukkit.event.block.BlockBreakEvent.class);
		reg(true, org.bukkit.event.block.BlockBurnEvent.class);
		reg(true, org.bukkit.event.block.BlockCanBuildEvent.class);
		reg(true, org.bukkit.event.block.BlockDamageEvent.class);
		reg(true, org.bukkit.event.block.BlockDispenseEvent.class);
		reg(true, org.bukkit.event.block.BlockExpEvent.class);
		reg(true, org.bukkit.event.block.BlockFadeEvent.class);
		reg(true, org.bukkit.event.block.BlockFormEvent.class);
		reg(true, org.bukkit.event.block.BlockFromToEvent.class);
		reg(true, org.bukkit.event.block.BlockGrowEvent.class);
		reg(true, org.bukkit.event.block.BlockIgniteEvent.class);
		reg(true, org.bukkit.event.block.BlockMultiPlaceEvent.class);
		reg(true, org.bukkit.event.block.BlockPhysicsEvent.class);
		reg(true, org.bukkit.event.block.BlockPistonExtendEvent.class);
		reg(true, org.bukkit.event.block.BlockPistonRetractEvent.class);
		reg(true, org.bukkit.event.block.BlockPlaceEvent.class);
		reg(true, org.bukkit.event.block.BlockRedstoneEvent.class);
		reg(true, org.bukkit.event.block.BlockSpreadEvent.class);
		reg(true, org.bukkit.event.block.EntityBlockFormEvent.class);
		reg(true, org.bukkit.event.block.LeavesDecayEvent.class);
		reg(true, org.bukkit.event.block.NotePlayEvent.class);
		reg(true, org.bukkit.event.block.SignChangeEvent.class);
		reg(true, org.bukkit.event.enchantment.EnchantItemEvent.class);
		reg(true, org.bukkit.event.enchantment.PrepareItemEnchantEvent.class);
		reg(true, org.bukkit.event.entity.CreatureSpawnEvent.class); // TODO impl SpawnReason
		reg(true, org.bukkit.event.entity.CreeperPowerEvent.class);
		reg(true, org.bukkit.event.entity.EntityBreakDoorEvent.class);
		reg(true, org.bukkit.event.entity.EntityChangeBlockEvent.class);
		reg(true, org.bukkit.event.entity.EntityCombustByBlockEvent.class);
		reg(true, org.bukkit.event.entity.EntityCombustByEntityEvent.class);
		reg(true, org.bukkit.event.entity.EntityCombustEvent.class);
		reg(true, org.bukkit.event.entity.EntityCreatePortalEvent.class);
		reg(true, org.bukkit.event.entity.EntityDamageByBlockEvent.class);
		reg(true, org.bukkit.event.entity.EntityDamageByEntityEvent.class);
		reg(true, org.bukkit.event.entity.EntityDamageEvent.class);
		reg(true, org.bukkit.event.entity.EntityDeathEvent.class);
		reg(true, org.bukkit.event.entity.EntityExplodeEvent.class);
		reg(true, org.bukkit.event.entity.EntityInteractEvent.class);
		reg(true, org.bukkit.event.entity.EntityPortalEnterEvent.class);
		reg(true, org.bukkit.event.entity.EntityPortalEvent.class);
		reg(true, org.bukkit.event.entity.EntityPortalExitEvent.class);
		reg(true, org.bukkit.event.entity.EntityRegainHealthEvent.class);
		reg(true, org.bukkit.event.entity.EntityShootBowEvent.class);
		reg(true, org.bukkit.event.entity.EntityTameEvent.class);
		reg(true, org.bukkit.event.entity.EntityTargetEvent.class);
		reg(true, org.bukkit.event.entity.EntityTargetLivingEntityEvent.class);
		reg(true, org.bukkit.event.entity.EntityTeleportEvent.class); // NOTE: this event is only for endermen, and not
																		// for other entities!
		reg(true, org.bukkit.event.entity.EntityUnleashEvent.class);
		reg(true, org.bukkit.event.entity.ExpBottleEvent.class);
		reg(true, org.bukkit.event.entity.ExplosionPrimeEvent.class);
		reg(true, org.bukkit.event.entity.FoodLevelChangeEvent.class);
		reg(true, org.bukkit.event.entity.HorseJumpEvent.class);
		reg(true, org.bukkit.event.entity.ItemDespawnEvent.class);
		reg(true, org.bukkit.event.entity.ItemSpawnEvent.class);
		reg(true, org.bukkit.event.entity.PigZapEvent.class);
		reg(true, org.bukkit.event.entity.PlayerDeathEvent.class);
		reg(true, org.bukkit.event.entity.PlayerLeashEntityEvent.class);
		reg(true, org.bukkit.event.entity.PotionSplashEvent.class);
		reg(true, org.bukkit.event.entity.ProjectileHitEvent.class);
		reg(true, org.bukkit.event.entity.ProjectileLaunchEvent.class);
		reg(true, org.bukkit.event.entity.SheepDyeWoolEvent.class);
		reg(true, org.bukkit.event.entity.SheepRegrowWoolEvent.class);
		reg(true, org.bukkit.event.entity.SlimeSplitEvent.class);
		reg(true, org.bukkit.event.hanging.HangingBreakByEntityEvent.class);
		reg(true, org.bukkit.event.hanging.HangingBreakEvent.class);
		reg(true, org.bukkit.event.hanging.HangingPlaceEvent.class);
		reg(true, org.bukkit.event.inventory.BrewEvent.class);
		reg(true, org.bukkit.event.inventory.CraftItemEvent.class);
		reg(true, org.bukkit.event.inventory.FurnaceBurnEvent.class);
		reg(true, org.bukkit.event.inventory.FurnaceExtractEvent.class);
		reg(true, org.bukkit.event.inventory.FurnaceSmeltEvent.class);
		reg(true, org.bukkit.event.inventory.InventoryClickEvent.class);
		reg(true, org.bukkit.event.inventory.InventoryCloseEvent.class);
		reg(true, org.bukkit.event.inventory.InventoryCreativeEvent.class);
		reg(true, org.bukkit.event.inventory.InventoryDragEvent.class);
		reg(true, org.bukkit.event.inventory.InventoryEvent.class);
		reg(true, org.bukkit.event.inventory.InventoryInteractEvent.class);
		reg(true, org.bukkit.event.inventory.InventoryMoveItemEvent.class);
		reg(true, org.bukkit.event.inventory.InventoryOpenEvent.class);
		reg(true, org.bukkit.event.inventory.InventoryPickupItemEvent.class);
		reg(true, org.bukkit.event.inventory.PrepareItemCraftEvent.class);
		reg(true, org.bukkit.event.painting.PaintingBreakByEntityEvent.class);
		reg(true, org.bukkit.event.painting.PaintingBreakEvent.class);
		reg(true, org.bukkit.event.painting.PaintingPlaceEvent.class);
		reg(true, org.bukkit.event.player.AsyncPlayerChatEvent.class);
		reg(true, org.bukkit.event.player.AsyncPlayerPreLoginEvent.class);
		reg(true, org.bukkit.event.player.PlayerAchievementAwardedEvent.class);
		reg(true, org.bukkit.event.player.PlayerAnimationEvent.class); // TODO Testing
		reg(true, org.bukkit.event.player.PlayerBedEnterEvent.class);
		reg(true, org.bukkit.event.player.PlayerBedLeaveEvent.class);
		reg(true, org.bukkit.event.player.PlayerBucketEmptyEvent.class);
		reg(true, org.bukkit.event.player.PlayerBucketFillEvent.class);
		reg(true, org.bukkit.event.player.PlayerChangedWorldEvent.class);
		reg(true, org.bukkit.event.player.PlayerChannelEvent.class); // FIXME: implement
		reg(true, org.bukkit.event.player.PlayerChatEvent.class); // FIXME: implement
		reg(true, org.bukkit.event.player.PlayerChatTabCompleteEvent.class); // FIXME: implement
		reg(true, org.bukkit.event.player.PlayerCommandPreprocessEvent.class);
		reg(true, org.bukkit.event.player.PlayerDropItemEvent.class);
		reg(true, org.bukkit.event.player.PlayerEditBookEvent.class);
		reg(true, org.bukkit.event.player.PlayerEggThrowEvent.class);
		reg(true, org.bukkit.event.player.PlayerExpChangeEvent.class);
		reg(true, org.bukkit.event.player.PlayerFishEvent.class);
		reg(true, org.bukkit.event.player.PlayerGameModeChangeEvent.class); // TODO currently only in CraftPlayer
		reg(true, org.bukkit.event.player.PlayerInteractEntityEvent.class);
		reg(true, org.bukkit.event.player.PlayerInteractEvent.class); // TODO currently only click actions
		reg(true, org.bukkit.event.player.PlayerInventoryEvent.class);
		reg(true, org.bukkit.event.player.PlayerItemBreakEvent.class);
		reg(true, org.bukkit.event.player.PlayerItemConsumeEvent.class);
		reg(true, org.bukkit.event.player.PlayerItemHeldEvent.class);
		reg(true, org.bukkit.event.player.PlayerJoinEvent.class); // TODO support for custom join messages
		reg(true, org.bukkit.event.player.PlayerKickEvent.class);
		reg(true, org.bukkit.event.player.PlayerLevelChangeEvent.class);
		reg(true, org.bukkit.event.player.PlayerLoginEvent.class); // TODO add server hostname
		reg(true, org.bukkit.event.player.PlayerMoveEvent.class);
		reg(true, org.bukkit.event.player.PlayerPickupItemEvent.class);
		reg(true, org.bukkit.event.player.PlayerPortalEvent.class);
		reg(true, org.bukkit.event.player.PlayerPreLoginEvent.class);
		reg(true, org.bukkit.event.player.PlayerQuitEvent.class); // TODO support for custom quit messages
		reg(true, org.bukkit.event.player.PlayerRegisterChannelEvent.class);
		reg(true, org.bukkit.event.player.PlayerRespawnEvent.class);
		reg(true, org.bukkit.event.player.PlayerShearEntityEvent.class);
		reg(true, org.bukkit.event.player.PlayerStatisticIncrementEvent.class);
		reg(true, org.bukkit.event.player.PlayerTeleportEvent.class);
		reg(true, org.bukkit.event.player.PlayerToggleFlightEvent.class); // TODO Testing
		reg(true, org.bukkit.event.player.PlayerToggleSneakEvent.class);
		reg(true, org.bukkit.event.player.PlayerToggleSprintEvent.class); // TODO Testing
		reg(true, org.bukkit.event.player.PlayerUnleashEntityEvent.class);
		reg(true, org.bukkit.event.player.PlayerUnregisterChannelEvent.class); // FIXME: implement
		reg(true, org.bukkit.event.player.PlayerVelocityEvent.class);
		reg(true, org.bukkit.event.server.MapInitializeEvent.class); // FIXME: implement
		reg(true, org.bukkit.event.server.PluginDisableEvent.class);
		reg(true, org.bukkit.event.server.PluginEnableEvent.class);
		reg(true, org.bukkit.event.server.PluginEvent.class);
		reg(true, org.bukkit.event.server.RemoteServerCommandEvent.class);
		reg(true, org.bukkit.event.server.ServerCommandEvent.class);
		reg(true, org.bukkit.event.server.ServerListPingEvent.class); // FIXME: implement
		reg(true, org.bukkit.event.server.ServiceRegisterEvent.class);
		reg(true, org.bukkit.event.server.ServiceUnregisterEvent.class);
		reg(true, org.bukkit.event.vehicle.VehicleBlockCollisionEvent.class); // FIXME: implement
		reg(true, org.bukkit.event.vehicle.VehicleCollisionEvent.class); // FIXME: implement
		reg(true, org.bukkit.event.vehicle.VehicleCreateEvent.class);
		reg(true, org.bukkit.event.vehicle.VehicleDamageEvent.class);
		reg(true, org.bukkit.event.vehicle.VehicleDestroyEvent.class);
		reg(true, org.bukkit.event.vehicle.VehicleEnterEvent.class);
		reg(true, org.bukkit.event.vehicle.VehicleEntityCollisionEvent.class); // FIXME: implement
		reg(true, org.bukkit.event.vehicle.VehicleExitEvent.class);
		reg(true, org.bukkit.event.vehicle.VehicleMoveEvent.class);
		reg(true, org.bukkit.event.vehicle.VehicleUpdateEvent.class);
		reg(true, org.bukkit.event.weather.LightningStrikeEvent.class);
		reg(true, org.bukkit.event.weather.ThunderChangeEvent.class);
		reg(true, org.bukkit.event.weather.WeatherChangeEvent.class);
		reg(true, org.bukkit.event.world.ChunkLoadEvent.class);
		reg(true, org.bukkit.event.world.ChunkPopulateEvent.class);
		reg(true, org.bukkit.event.world.ChunkUnloadEvent.class);
		reg(true, org.bukkit.event.world.PortalCreateEvent.class);
		reg(true, org.bukkit.event.world.SpawnChangeEvent.class);
		reg(true, org.bukkit.event.world.StructureGrowEvent.class);
		reg(true, org.bukkit.event.world.WorldInitEvent.class);
		reg(true, org.bukkit.event.world.WorldLoadEvent.class);
		reg(true, org.bukkit.event.world.WorldSaveEvent.class);
		reg(true, org.bukkit.event.world.WorldUnloadEvent.class);

		reg(true, org.spigotmc.event.entity.EntityDismountEvent.class);
		reg(true, org.spigotmc.event.entity.EntityMountEvent.class);

		printStats();
	}
}
