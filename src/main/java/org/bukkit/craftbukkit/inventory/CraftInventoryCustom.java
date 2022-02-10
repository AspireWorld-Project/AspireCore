package org.bukkit.craftbukkit.inventory;

import org.apache.commons.lang.Validate;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.InventoryHolder;

public class CraftInventoryCustom extends CraftInventory {
	public CraftInventoryCustom(InventoryHolder owner, InventoryType type) {
		super(new MinecraftInventory(owner, type));
	}

	public CraftInventoryCustom(InventoryHolder owner, InventoryType type, String title) {
		super(new MinecraftInventory(owner, type, title));
	}

	public CraftInventoryCustom(InventoryHolder owner, int size) {
		super(new MinecraftInventory(owner, size));
	}

	public CraftInventoryCustom(InventoryHolder owner, int size, String title) {
		super(new MinecraftInventory(owner, size, title));
	}

	static class MinecraftInventory implements net.minecraft.inventory.IInventory {
		private final net.minecraft.item.ItemStack[] items;
		private int maxStack = 64;
		private final String title;
		private InventoryType type;
		private final InventoryHolder owner;

		public MinecraftInventory(InventoryHolder owner, InventoryType type) {
			this(owner, type.getDefaultSize(), type.getDefaultTitle());
			this.type = type;
		}

		public MinecraftInventory(InventoryHolder owner, InventoryType type, String title) {
			this(owner, type.getDefaultSize(), title);
			this.type = type;
		}

		public MinecraftInventory(InventoryHolder owner, int size) {
			this(owner, size, "Chest");
		}

		public MinecraftInventory(InventoryHolder owner, int size, String title) {
			Validate.notNull(title, "Title cannot be null");
			Validate.isTrue(title.length() <= 32, "Title cannot be longer than 32 characters");
			items = new net.minecraft.item.ItemStack[size];
			this.title = title;
			this.owner = owner;
			type = InventoryType.CHEST;
		}

		@Override
		public int getSizeInventory() {
			return items.length;
		}

		@Override
		public net.minecraft.item.ItemStack getStackInSlot(int i) {
			return items[i];
		}

		@Override
		public net.minecraft.item.ItemStack decrStackSize(int i, int j) {
			net.minecraft.item.ItemStack stack = getStackInSlot(i);
			net.minecraft.item.ItemStack result;
			if (stack == null)
				return null;
			if (stack.stackSize <= j) {
				setInventorySlotContents(i, null);
				result = stack;
			} else {
				result = CraftItemStack.copyNMSStack(stack, j);
				stack.stackSize -= j;
			}
			markDirty();
			return result;
		}

		@Override
		public net.minecraft.item.ItemStack getStackInSlotOnClosing(int i) {
			net.minecraft.item.ItemStack stack = getStackInSlot(i);
			net.minecraft.item.ItemStack result;
			if (stack == null)
				return null;
			if (stack.stackSize <= 1) {
				setInventorySlotContents(i, null);
				result = stack;
			} else {
				result = CraftItemStack.copyNMSStack(stack, 1);
				stack.stackSize -= 1;
			}
			return result;
		}

		@Override
		public void setInventorySlotContents(int i, net.minecraft.item.ItemStack itemstack) {
			items[i] = itemstack;
			if (itemstack != null && getInventoryStackLimit() > 0 && itemstack.stackSize > getInventoryStackLimit()) {
				itemstack.stackSize = getInventoryStackLimit();
			}
		}

		@Override
		public String getInventoryName() {
			return title;
		}

		@Override
		public int getInventoryStackLimit() {
			return maxStack;
		}

		public void setMaxStackSize(int size) {
			maxStack = size;
		}

		@Override
		public void markDirty() {
		}

		@Override
		public boolean isUseableByPlayer(net.minecraft.entity.player.EntityPlayer entityhuman) {
			return true;
		}

		public net.minecraft.item.ItemStack[] getContents() {
			return items;
		}

		public InventoryType getType() {
			return type;
		}

		@Override
		public void closeInventory() {
		}

		public InventoryHolder getOwner() {
			return owner;
		}

		@Override
		public void openInventory() {
		}

		@Override
		public boolean hasCustomInventoryName() {
			return false;
		}

		@Override
		public boolean isItemValidForSlot(int i, net.minecraft.item.ItemStack itemstack) {
			return true;
		}
	}
}
