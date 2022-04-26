package org.bukkit.craftbukkit.entity;

import net.minecraft.entity.player.EntityPlayerMP;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.craftbukkit.inventory.*;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.*;
import org.bukkit.permissions.PermissibleBase;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;

import java.util.Set;

public class CraftHumanEntity extends CraftLivingEntity implements HumanEntity {
	public CraftInventoryPlayer inventory;
	public CraftInventory enderChest;
	protected PermissibleBase perm = new PermissibleBase(this);
	private boolean op;
	private GameMode mode;

	public CraftHumanEntity(final CraftServer server, final net.minecraft.entity.player.EntityPlayer entity) {
		super(server, entity);
		mode = server.getDefaultGameMode();
	}

	public void setPermissible(PermissibleBase perm) {
		this.perm = perm;
	}

	public PermissibleBase getPermissible() {
		return perm;
	}

	@Override
	public String getName() {
		return getHandle().getCommandSenderName();
	}

	@Override
	public PlayerInventory getInventory() {
		if (inventory == null) {
			inventory = new CraftInventoryPlayer(this);
		}
		return inventory;
	}

	@Override
	public EntityEquipment getEquipment() {
		return inventory;
	}

	@Override
	public Inventory getEnderChest() {
		if (enderChest == null) {
			enderChest = new CraftInventory(
					((net.minecraft.entity.player.EntityPlayer) entity).getInventoryEnderChest());
		}
		return enderChest;
	}

	@Override
	public ItemStack getItemInHand() {
		return getInventory().getItemInHand();
	}

	@Override
	public void setItemInHand(ItemStack item) {
		getInventory().setItemInHand(item);
	}

	@Override
	public ItemStack getItemOnCursor() {
		return CraftItemStack.asCraftMirror(getHandle().inventory.getItemStack());
	}

	@Override
	public void setItemOnCursor(ItemStack item) {
		net.minecraft.item.ItemStack stack = CraftItemStack.asNMSCopy(item);
		getHandle().inventory.setItemStack(stack);
		if (this instanceof CraftPlayer) {
			((net.minecraft.entity.player.EntityPlayerMP) getHandle()).updateHeldItem(); // Send set slot for cursor
		}
	}

	@Override
	public boolean isSleeping() {
		return getHandle().isSleeping();
	}

	@Override
	public int getSleepTicks() {
		return getHandle().getSleepTimer();
	}

	@Override
	public boolean isOp() {
		return op;
	}

	@Override
	public boolean isPermissionSet(String name) {
		return perm.isPermissionSet(name);
	}

	@Override
	public boolean isPermissionSet(Permission perm) {
		return this.perm.isPermissionSet(perm);
	}

	@Override
	public boolean hasPermission(String name) {
		return perm.hasPermission(name);
	}

	@Override
	public boolean hasPermission(Permission perm) {
		return this.perm.hasPermission(perm);
	}

	@Override
	public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value) {
		return perm.addAttachment(plugin, name, value);
	}

	@Override
	public PermissionAttachment addAttachment(Plugin plugin) {
		return perm.addAttachment(plugin);
	}

	@Override
	public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value, int ticks) {
		return perm.addAttachment(plugin, name, value, ticks);
	}

	@Override
	public PermissionAttachment addAttachment(Plugin plugin, int ticks) {
		return perm.addAttachment(plugin, ticks);
	}

	@Override
	public void removeAttachment(PermissionAttachment attachment) {
		perm.removeAttachment(attachment);
	}

	@Override
	public void recalculatePermissions() {
		perm.recalculatePermissions();
	}

	@Override
	public void setOp(boolean value) {
		op = value;
		perm.recalculatePermissions();
	}

	@Override
	public Set<PermissionAttachmentInfo> getEffectivePermissions() {
		return perm.getEffectivePermissions();
	}

	@Override
	public GameMode getGameMode() {
		return mode;
	}

	@Override
	public void setGameMode(GameMode mode) {
		if (mode == null)
			throw new IllegalArgumentException("Mode cannot be null");

		this.mode = mode;
	}

	@Override
	public net.minecraft.entity.player.EntityPlayer getHandle() {
		return (net.minecraft.entity.player.EntityPlayer) entity;
	}

	public void setHandle(final net.minecraft.entity.player.EntityPlayer entity) {
		super.setHandle(entity);
		if (inventory != null) {
			inventory = new CraftInventoryPlayer(this);
		}
	}

	@Override
	public String toString() {
		return "CraftHumanEntity{" + "id=" + getEntityId() + "name=" + getName() + '}';
	}

	@Override
	public InventoryView getOpenInventory() {
		return getHandle().openContainer.getBukkitView();
	}

	@SuppressWarnings("incomplete-switch")
	@Override
	public InventoryView openInventory(Inventory inventory) {
		if (!(getHandle() instanceof net.minecraft.entity.player.EntityPlayerMP))
			return null;
		net.minecraft.entity.player.EntityPlayerMP player = (net.minecraft.entity.player.EntityPlayerMP) getHandle();
		InventoryType type = inventory.getType();
		net.minecraft.inventory.Container formerContainer = getHandle().openContainer;
		// TODO: Should we check that it really IS a CraftInventory first?
		CraftInventory craftinv = (CraftInventory) inventory;
		switch (type) {
		case PLAYER:
		case CHEST:
		case ENDER_CHEST:
			getHandle().displayGUIChest(craftinv.getInventory());
			break;
		case DISPENSER:
			if (craftinv.getInventory() instanceof net.minecraft.tileentity.TileEntityDispenser) {
				getHandle().func_146102_a((net.minecraft.tileentity.TileEntityDispenser) craftinv.getInventory());
			} else {
				openCustomInventory(inventory, player, 3);
			}
			break;
		case FURNACE:
			if (craftinv.getInventory() instanceof net.minecraft.tileentity.TileEntityFurnace) {
				getHandle().func_146101_a((net.minecraft.tileentity.TileEntityFurnace) craftinv.getInventory());
			} else {
				openCustomInventory(inventory, player, 2);
			}
			break;
		case WORKBENCH:
			openCustomInventory(inventory, player, 1);
			break;
		case BREWING:
			if (craftinv.getInventory() instanceof net.minecraft.tileentity.TileEntityBrewingStand) {
				getHandle().func_146098_a((net.minecraft.tileentity.TileEntityBrewingStand) craftinv.getInventory());
			} else {
				openCustomInventory(inventory, player, 5);
			}
			break;
		case ENCHANTING:
			openCustomInventory(inventory, player, 4);
			break;
		case HOPPER:
			if (craftinv.getInventory() instanceof net.minecraft.tileentity.TileEntityHopper) {
				getHandle().func_146093_a((net.minecraft.tileentity.TileEntityHopper) craftinv.getInventory());
			} else if (craftinv.getInventory() instanceof net.minecraft.entity.item.EntityMinecartHopper) {
				getHandle().displayGUIHopperMinecart(
						(net.minecraft.entity.item.EntityMinecartHopper) craftinv.getInventory());
			} else {
				openCustomInventory(inventory, player, 9);
			}
			break;
		case CREATIVE:
		case CRAFTING:
			throw new IllegalArgumentException("Can't open a " + type + " inventory!");
		}
		if (getHandle().openContainer == formerContainer)
			return null;
		// TODO
		// getHandle().openContainer.checkReachable = false;
		return getHandle().openContainer.getBukkitView();
	}

	private void openCustomInventory(Inventory inventory, net.minecraft.entity.player.EntityPlayerMP player,
			int windowType) {
		if (player.playerNetServerHandler == null)
			return;
		net.minecraft.inventory.Container container = new CraftContainer(inventory, this,
				player.nextContainerCounter());

		container = CraftEventFactory.callInventoryOpenEvent(player, container);
		if (container == null)
			return;

		InventoryView view = container.getBukkitView();
		if (view == null)
			return;

		String title = view.getTitle();
		int size = view.getTopInventory().getSize();

		player.playerNetServerHandler.sendPacket(new net.minecraft.network.play.server.S2DPacketOpenWindow(
				container.windowId, windowType, title, size, true));
		getHandle().openContainer = container;
		getHandle().openContainer.addCraftingToCrafters(player);
	}

	@Override
	public InventoryView openWorkbench(Location location, boolean force) {
		if (!force) {
			Block block = location.getBlock();
			if (block.getType() != Material.WORKBENCH)
				return null;
		}
		if (location == null) {
			location = getLocation();
		}
		getHandle().displayGUIWorkbench(location.getBlockX(), location.getBlockY(), location.getBlockZ());
		if (force) {
			// TODO
			//getHandle().openContainer.checkReachable = false;
		}
		return getHandle().openContainer.getBukkitView();
	}

	@Override
	public InventoryView openEnchanting(Location location, boolean force) {
		if (!force) {
			Block block = location.getBlock();
			if (block.getType() != Material.ENCHANTMENT_TABLE)
				return null;
		}
		if (location == null) {
			location = getLocation();
		}
		getHandle().displayGUIEnchantment(location.getBlockX(), location.getBlockY(), location.getBlockZ(), null);
		if (force) {
			// TODO
			// getHandle().openContainer.checkReachable = false;
		}
		return getHandle().openContainer.getBukkitView();
	}

	@Override
	public void openInventory(InventoryView inventory) {
		if (!(getHandle() instanceof net.minecraft.entity.player.EntityPlayerMP))
			return; // TODO: NPC support?
		if (((net.minecraft.entity.player.EntityPlayerMP) getHandle()).playerNetServerHandler == null)
			return;
		if (getHandle().openContainer != getHandle().inventoryContainer) {
			// fire INVENTORY_CLOSE if one already open
			((net.minecraft.entity.player.EntityPlayerMP) getHandle()).playerNetServerHandler.processCloseWindow(
					new net.minecraft.network.play.client.C0DPacketCloseWindow(getHandle().openContainer.windowId));
		}
		net.minecraft.entity.player.EntityPlayerMP player = (net.minecraft.entity.player.EntityPlayerMP) getHandle();
		net.minecraft.inventory.Container container;
		if (inventory instanceof CraftInventoryView) {
			container = ((CraftInventoryView) inventory).getHandle();
		} else {
			player.getNextWindowId();
			container = new CraftContainer(inventory, player.currentWindowId);
		}

		// Trigger an INVENTORY_OPEN event
		container = CraftEventFactory.callInventoryOpenEvent(player, container);
		if (container == null)
			return;

		// Now open the window
		InventoryType type = inventory.getType();
		int windowType = CraftContainer.getNotchInventoryType(type);
		String title = inventory.getTitle();
		int size = inventory.getTopInventory().getSize();
		player.playerNetServerHandler.sendPacket(new net.minecraft.network.play.server.S2DPacketOpenWindow(
				container.windowId, windowType, title, size, false));
		player.openContainer = container;
		player.openContainer.addCraftingToCrafters(player);
	}

	@Override
	public void closeInventory() {
		net.minecraft.entity.player.EntityPlayer player = getHandle();
		if (player instanceof EntityPlayerMP && ((EntityPlayerMP) player).playerNetServerHandler == null)
			return;
		player.closeScreen();
	}

	@Override
	public boolean isBlocking() {
		return getHandle().isBlocking();
	}

	@Override
	public boolean setWindowProperty(InventoryView.Property prop, int value) {
		return false;
	}

	@Override
	public int getExpToLevel() {
		return getHandle().xpBarCap();
	}
}
