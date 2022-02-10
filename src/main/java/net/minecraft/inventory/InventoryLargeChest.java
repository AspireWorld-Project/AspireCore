package net.minecraft.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class InventoryLargeChest implements IInventory {
	private String name;
	private IInventory upperChest;
	private IInventory lowerChest;
	private static final String __OBFID = "CL_00001507";

	public InventoryLargeChest(String p_i1559_1_, IInventory p_i1559_2_, IInventory p_i1559_3_) {
		name = p_i1559_1_;

		if (p_i1559_2_ == null) {
			p_i1559_2_ = p_i1559_3_;
		}

		if (p_i1559_3_ == null) {
			p_i1559_3_ = p_i1559_2_;
		}

		upperChest = p_i1559_2_;
		lowerChest = p_i1559_3_;
	}

	@Override
	public int getSizeInventory() {
		return upperChest.getSizeInventory() + lowerChest.getSizeInventory();
	}

	public boolean isPartOfLargeChest(IInventory p_90010_1_) {
		return upperChest == p_90010_1_ || lowerChest == p_90010_1_;
	}

	@Override
	public String getInventoryName() {
		return upperChest.hasCustomInventoryName() ? upperChest.getInventoryName()
				: lowerChest.hasCustomInventoryName() ? lowerChest.getInventoryName() : name;
	}

	@Override
	public boolean hasCustomInventoryName() {
		return upperChest.hasCustomInventoryName() || lowerChest.hasCustomInventoryName();
	}

	@Override
	public ItemStack getStackInSlot(int p_70301_1_) {
		return p_70301_1_ >= upperChest.getSizeInventory()
				? lowerChest.getStackInSlot(p_70301_1_ - upperChest.getSizeInventory())
				: upperChest.getStackInSlot(p_70301_1_);
	}

	@Override
	public ItemStack decrStackSize(int p_70298_1_, int p_70298_2_) {
		return p_70298_1_ >= upperChest.getSizeInventory()
				? lowerChest.decrStackSize(p_70298_1_ - upperChest.getSizeInventory(), p_70298_2_)
				: upperChest.decrStackSize(p_70298_1_, p_70298_2_);
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int p_70304_1_) {
		return p_70304_1_ >= upperChest.getSizeInventory()
				? lowerChest.getStackInSlotOnClosing(p_70304_1_ - upperChest.getSizeInventory())
				: upperChest.getStackInSlotOnClosing(p_70304_1_);
	}

	@Override
	public void setInventorySlotContents(int p_70299_1_, ItemStack p_70299_2_) {
		if (p_70299_1_ >= upperChest.getSizeInventory()) {
			lowerChest.setInventorySlotContents(p_70299_1_ - upperChest.getSizeInventory(), p_70299_2_);
		} else {
			upperChest.setInventorySlotContents(p_70299_1_, p_70299_2_);
		}
	}

	@Override
	public int getInventoryStackLimit() {
		return upperChest.getInventoryStackLimit();
	}

	@Override
	public void markDirty() {
		upperChest.markDirty();
		lowerChest.markDirty();
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer p_70300_1_) {
		return upperChest.isUseableByPlayer(p_70300_1_) && lowerChest.isUseableByPlayer(p_70300_1_);
	}

	@Override
	public void openInventory() {
		upperChest.openInventory();
		lowerChest.openInventory();
	}

	@Override
	public void closeInventory() {
		upperChest.closeInventory();
		lowerChest.closeInventory();
	}

	@Override
	public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_) {
		return true;
	}

	public IInventory getUpperChest() {
		return upperChest;
	}

	public IInventory getLowerChest() {
		return lowerChest;
	}
}