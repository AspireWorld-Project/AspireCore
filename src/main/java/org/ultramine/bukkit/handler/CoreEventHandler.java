package org.ultramine.bukkit.handler;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.play.server.S1BPacketEntityAttach;
import net.minecraft.network.play.server.S1CPacketEntityMetadata;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.Explosion;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.event.entity.item.ItemExpireEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import org.bukkit.Bukkit;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.block.CraftBlockState;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.craftbukkit.scheduler.CraftScheduler;
import org.bukkit.entity.Explosive;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockMultiPlaceEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import java.util.ArrayList;
import java.util.List;

public class CoreEventHandler {
	private final CraftServer server;

	public CoreEventHandler(CraftServer server) {
		this.server = server;
	}

	@SubscribeEvent
	public void onTick(TickEvent.ServerTickEvent e) {
		if (e.phase == TickEvent.Phase.START) {
			((CraftScheduler) Bukkit.getScheduler()).mainThreadHeartbeat(MinecraftServer.getServer().getTickCounter());
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onBlockBreak(BlockEvent.BreakEvent e) {
		BlockBreakEvent bukkitEvent = CraftEventFactory.callBlockBreakEvent(e.world, e.x, e.y, e.z, e.block,
				e.blockMetadata, (EntityPlayerMP) e.getPlayer());
		if (bukkitEvent.isCancelled()) {
			e.setCanceled(true);
		} else {
			e.setExpToDrop(bukkitEvent.getExpToDrop());
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onBlockMultiPlace(BlockEvent.MultiPlaceEvent e) {
		List<BlockState> craftBlockStates = new ArrayList<>(e.getReplacedBlockSnapshots().size());
		for (BlockSnapshot blockSnapshot : e.getReplacedBlockSnapshots()) {
			craftBlockStates.add(CraftBlockState.getBlockState(blockSnapshot.world, blockSnapshot.x, blockSnapshot.y,
					blockSnapshot.z));
		}
		BlockMultiPlaceEvent multiPlaceEvent = CraftEventFactory.callBlockMultiPlaceEvent(e.world, e.player,
				craftBlockStates, e.x, e.y, e.z);
		if (multiPlaceEvent.isCancelled() || !multiPlaceEvent.canBuild()) {
			e.setCanceled(true);
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onBlockPlace(BlockEvent.PlaceEvent e) {
		CraftBlockState blockstate = CraftBlockState.getBlockState(e.world, e.x, e.y, e.z);
		BlockPlaceEvent bukkitEvent = CraftEventFactory.callBlockPlaceEvent(e.world, e.player, blockstate, e.x, e.y,
				e.z);
		if (bukkitEvent.isCancelled() || !bukkitEvent.canBuild()) {
			e.setCanceled(true);
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onItemExpire(ItemExpireEvent e) {
		if (CraftEventFactory.callItemDespawnEvent(e.entityItem).isCancelled()) {
			e.entityItem.age = 0;
			e.setCanceled(true);
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onExplosionStart(ExplosionEvent.Start e) {
		Explosion exp = e.explosion;
		if (exp.exploder != null) {
			CraftEntity centity = exp.exploder.getBukkitEntity();
			ExplosionPrimeEvent event = centity instanceof Explosive ? new ExplosionPrimeEvent((Explosive) centity)
					: new ExplosionPrimeEvent(centity, exp.explosionSize, exp.isFlaming);
			server.getPluginManager().callEvent(event);
			if (event.isCancelled()) {
				e.setCanceled(true);
			} else {
				exp.explosionSize = event.getRadius();
				exp.isFlaming = event.getFire();
			}
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onCheckSpawn(LivingSpawnEvent.CheckSpawn e) {
		e.entity.setSpawnReason("natural");
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onSpecialSpawn(LivingSpawnEvent.SpecialSpawn e) {
		e.entity.setSpawnReason("spawner");
	}
}
