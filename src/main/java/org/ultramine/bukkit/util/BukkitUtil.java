package org.ultramine.bukkit.util;

import jline.internal.Nullable;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.server.CommandBlockLogic;
import net.minecraft.entity.EntityMinecartCommandBlock;
import net.minecraft.entity.item.EntityMinecartContainer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.*;
import net.minecraft.network.rcon.RConConsoleSource;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import org.bukkit.Bukkit;
import org.bukkit.block.BlockState;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.command.CraftBlockCommandSender;
import org.bukkit.craftbukkit.command.CraftRemoteConsoleCommandSender;
import org.bukkit.craftbukkit.entity.CraftMinecartCommand;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;
import org.ultramine.server.event.WorldEventProxy;
import org.ultramine.server.event.WorldUpdateObject;
import org.ultramine.server.event.WorldUpdateObjectType;

public class BukkitUtil {
	public static net.minecraft.item.ItemStack[] getVanillaContents(IInventory inv) {
		int size = inv.getSizeInventory();
		net.minecraft.item.ItemStack[] items = new net.minecraft.item.ItemStack[size];
		for (int i = 0; i < size; i++) {
			net.minecraft.item.ItemStack mcStack = inv.getStackInSlot(i);
			items[i] = mcStack;
		}

		return items;
	}

	public static org.bukkit.inventory.ItemStack[] getBukkitContents(IInventory inv) {
		int size = inv.getSizeInventory();
		org.bukkit.inventory.ItemStack[] items = new org.bukkit.inventory.ItemStack[size];
		for (int i = 0; i < size; i++) {
			net.minecraft.item.ItemStack mcStack = inv.getStackInSlot(i);
			items[i] = mcStack == null ? null : CraftItemStack.asCraftMirror(mcStack);
		}

		return items;
	}

	public static CommandSender toBukkitCommandSender(ICommandSender sender) {
		CommandSender bukkitSender;
		if (sender instanceof EntityPlayerMP) {
			bukkitSender = (Player) ((net.minecraft.entity.Entity) sender).getBukkitEntity();
		} else if (sender instanceof CommandBlockLogic) {
			WorldUpdateObject wuo = WorldEventProxy.getCurrent().getUpdateObject();
			if (wuo.getType() == WorldUpdateObjectType.TILEE_ENTITY) {
				bukkitSender = new CraftBlockCommandSender((CommandBlockLogic) sender);
			} else if (wuo.getType() == WorldUpdateObjectType.ENTITY
					&& wuo.getEntity() instanceof net.minecraft.entity.EntityMinecartCommandBlock) {
				bukkitSender = new CraftMinecartCommand((CraftServer) Bukkit.getServer(),
						(net.minecraft.entity.EntityMinecartCommandBlock) wuo.getEntity());
			} else {
				bukkitSender = ((CraftServer) Bukkit.getServer()).getConsoleSender();
			}
		} else if (sender instanceof RConConsoleSource) {
			bukkitSender = CraftRemoteConsoleCommandSender.getInstance();
		} else {
			bukkitSender = ((CraftServer) Bukkit.getServer()).getConsoleSender();
		}

		return bukkitSender;
	}

	public static ICommandSender toVanillaCommandSender(CommandSender sender) {
		ICommandSender vanillaSender;
		if (sender instanceof CraftPlayer) {
			vanillaSender = ((CraftPlayer) sender).getHandle();
		} else if (sender instanceof CraftBlockCommandSender)
			return ((CraftBlockCommandSender) sender).getTileEntity();
		else if (sender instanceof CraftMinecartCommand) {
			vanillaSender = ((EntityMinecartCommandBlock) ((CraftMinecartCommand) sender).getHandle()).func_145822_e();
		} else if (sender instanceof CraftRemoteConsoleCommandSender)
			throw new IllegalArgumentException();
		else {
			vanillaSender = MinecraftServer.getServer();
		}

		return vanillaSender;
	}

	@Nullable
	public static InventoryHolder getInventoryOwner(IInventory inventory) {
		if (inventory instanceof TileEntity) {
			TileEntity te = (TileEntity) inventory;
			BlockState state = te.getWorldObj().getWorld().getBlockAt(te.xCoord, te.yCoord, te.zCoord).getState();
			if (state instanceof InventoryHolder)
				return (InventoryHolder) state;
		} else if (inventory instanceof InventoryBasic) {
			InventoryBasic inventoryBasic = (InventoryBasic) inventory;
			if (inventoryBasic instanceof AnimalChest)
				return (InventoryHolder) ((AnimalChest) inventoryBasic).getAnimal().getBukkitEntity();
			else if (inventoryBasic instanceof InventoryEnderChest)
				return (InventoryHolder) ((InventoryEnderChest) inventoryBasic).getOwner().getBukkitEntity();
		} else if (inventory instanceof EntityMinecartContainer) {
			Entity cart = ((net.minecraft.entity.Entity) inventory).getBukkitEntity();
			if (cart instanceof InventoryHolder)
				return (InventoryHolder) cart;
		} else if (inventory instanceof InventoryPlayer) {
			InventoryPlayer inventoryPlayer = (InventoryPlayer) inventory;
			return (InventoryHolder) inventoryPlayer.player.getBukkitEntity();
		} else if (inventory instanceof InventoryCrafting && ((InventoryCrafting) inventory).getOwner() != null)
			return (InventoryHolder) ((InventoryCrafting) inventory).getOwner().getBukkitEntity();
		else if (inventory instanceof InventoryMerchant)
			return (InventoryHolder) ((InventoryMerchant) inventory).getPlayer().getBukkitEntity();
		return null;
	}
}
