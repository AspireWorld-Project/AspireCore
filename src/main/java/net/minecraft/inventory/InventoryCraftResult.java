package net.minecraft.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class InventoryCraftResult implements IInventory {
	private final ItemStack[] stackResult = new ItemStack[1];
	private static final String __OBFID = "CL_00001760";

	@Override
	public int getSizeInventory() {
		return 1;
	}

	@Override
	public ItemStack getStackInSlot(int p_70301_1_) {
		return stackResult[0];
	}

	@Override
	public String getInventoryName() {
		return "Result";
	}

	@Override
	public boolean hasCustomInventoryName() {
		return false;
	}

	@Override
	public ItemStack decrStackSize(int p_70298_1_, int p_70298_2_) {
		if (stackResult[0] != null) {
			ItemStack itemstack = stackResult[0];
			stackResult[0] = null;
			return itemstack;
		} else
			return null;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int p_70304_1_) {
		if (stackResult[0] != null) {
			ItemStack itemstack = stackResult[0];
			stackResult[0] = null;
			return itemstack;
		} else
			return null;
	}

	@Override
	public void setInventorySlotContents(int p_70299_1_, ItemStack p_70299_2_) {
		stackResult[0] = p_70299_2_;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public void markDirty() {
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer p_70300_1_) {
		return true;
	}

	@Override
	public void openInventory() {
	}

	@Override
	public void closeInventory() {
	}

	@Override
	public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_) {
		return true;
	}
}