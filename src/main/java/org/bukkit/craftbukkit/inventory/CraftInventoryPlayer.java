package org.bukkit.craftbukkit.inventory;

import org.apache.commons.lang.Validate;
import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

public class CraftInventoryPlayer extends CraftInventory
		implements org.bukkit.inventory.PlayerInventory, EntityEquipment {
	private final CraftHumanEntity player;

	public CraftInventoryPlayer(CraftHumanEntity player) {
		super(player.getHandle().inventory);
		this.player = player;
	}

	@Override
	public net.minecraft.entity.player.InventoryPlayer getInventory() {
		net.minecraft.entity.player.InventoryPlayer actualInv = player.getHandle().inventory;
		if (inventory != actualInv) {
			inventory = actualInv;
		}
		return actualInv;
	}

	@Override
	public int getSize() {
		return getInventory().mainInventory.length; // Cauldron - Galacticraft and Aether extend equipped item slots so
													// we need to check the main inventory array directly
	}

	@Override
	public ItemStack getItemInHand() {
		return CraftItemStack.asCraftMirror(getInventory().getCurrentItem());
	}

	@Override
	public void setItemInHand(ItemStack stack) {
		setItem(getHeldItemSlot(), stack);
	}

	@Override
	public int getHeldItemSlot() {
		return getInventory().currentItem;
	}

	@Override
	public void setHeldItemSlot(int slot) {
		Validate.isTrue(slot >= 0 && slot < net.minecraft.entity.player.InventoryPlayer.getHotbarSize(),
				"Slot is not between 0 and 8 inclusive");
		getInventory().currentItem = slot;
		((CraftPlayer) getHolder()).getHandle().playerNetServerHandler
				.sendPacket(new net.minecraft.network.play.server.S09PacketHeldItemChange(slot));
	}

	@Override
	public ItemStack getHelmet() {
		return getItem(getSize() + 3);
	}

	@Override
	public ItemStack getChestplate() {
		return getItem(getSize() + 2);
	}

	@Override
	public ItemStack getLeggings() {
		return getItem(getSize() + 1);
	}

	@Override
	public ItemStack getBoots() {
		return getItem(getSize() + 0);
	}

	@Override
	public void setHelmet(ItemStack helmet) {
		setItem(getSize() + 3, helmet);
	}

	@Override
	public void setChestplate(ItemStack chestplate) {
		setItem(getSize() + 2, chestplate);
	}

	@Override
	public void setLeggings(ItemStack leggings) {
		setItem(getSize() + 1, leggings);
	}

	@Override
	public void setBoots(ItemStack boots) {
		setItem(getSize() + 0, boots);
	}

	@Override
	public ItemStack[] getContents() {
		net.minecraft.entity.player.InventoryPlayer inv = getInventory();
		net.minecraft.item.ItemStack[] mcItems = inv.mainInventory;
		ItemStack[] items = new ItemStack[mcItems.length];
		for (int i = 0; i < mcItems.length; i++) {
			items[i] = mcItems[i] == null ? null : CraftItemStack.asCraftMirror(mcItems[i]);
		}
		return items;
	}

	@Override
	public void setContents(ItemStack[] items) {
		net.minecraft.entity.player.InventoryPlayer inv = getInventory();
		int size = inv.mainInventory.length;
		if (size < items.length)
			throw new IllegalArgumentException("Invalid inventory size; expected " + size + " or less");

		for (int i = 0; i < size; i++) {
			if (i >= items.length) {
				inv.setInventorySlotContents(i, null);
			} else {
				inv.setInventorySlotContents(i, CraftItemStack.asNMSCopy(items[i]));
			}
		}
	}

	@Override
	public ItemStack[] getArmorContents() {
		net.minecraft.item.ItemStack[] mcItems = getInventory().armorInventory;
		ItemStack[] ret = new ItemStack[mcItems.length];

		for (int i = 0; i < mcItems.length; i++) {
			ret[i] = CraftItemStack.asCraftMirror(mcItems[i]);
		}
		return ret;
	}

	@Override
	public void setArmorContents(ItemStack[] items) {
		int cnt = getSize();

		if (items == null) {
			items = new ItemStack[4];
		}
		for (ItemStack item : items) {
			if (item == null || item.getTypeId() == 0) {
				clear(cnt++);
			} else {
				setItem(cnt++, item);
			}
		}
	}

	@Override
	public int clear(int id, int data) {
		int count = 0;
		ItemStack[] items = getContents();
		ItemStack[] armor = getArmorContents();
		int armorSlot = getSize();

		for (int i = 0; i < items.length; i++) {
			ItemStack item = items[i];
			if (item == null) {
				continue;
			}
			if (id > -1 && item.getTypeId() != id) {
				continue;
			}
			if (data > -1 && item.getData().getData() != data) {
				continue;
			}

			count += item.getAmount();
			setItem(i, null);
		}

		for (ItemStack item : armor) {
			if (item == null) {
				continue;
			}
			if (id > -1 && item.getTypeId() != id) {
				continue;
			}
			if (data > -1 && item.getData().getData() != data) {
				continue;
			}

			count += item.getAmount();
			setItem(armorSlot++, null);
		}
		return count;
	}

	@Override
	public HumanEntity getHolder() {
		return player;
	}

	@Override
	public float getItemInHandDropChance() {
		return 1;
	}

	@Override
	public void setItemInHandDropChance(float chance) {
		throw new UnsupportedOperationException();
	}

	@Override
	public float getHelmetDropChance() {
		return 1;
	}

	@Override
	public void setHelmetDropChance(float chance) {
		throw new UnsupportedOperationException();
	}

	@Override
	public float getChestplateDropChance() {
		return 1;
	}

	@Override
	public void setChestplateDropChance(float chance) {
		throw new UnsupportedOperationException();
	}

	@Override
	public float getLeggingsDropChance() {
		return 1;
	}

	@Override
	public void setLeggingsDropChance(float chance) {
		throw new UnsupportedOperationException();
	}

	@Override
	public float getBootsDropChance() {
		return 1;
	}

	@Override
	public void setBootsDropChance(float chance) {
		throw new UnsupportedOperationException();
	}
}
