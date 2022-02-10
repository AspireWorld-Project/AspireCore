package net.minecraft.inventory;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class InventoryBasic implements IInventory {
	private String inventoryTitle;
	private int slotsCount;
	private ItemStack[] inventoryContents;
	private List field_70480_d;
	private boolean field_94051_e;
	private static final String __OBFID = "CL_00001514";

	public InventoryBasic(String p_i1561_1_, boolean p_i1561_2_, int p_i1561_3_) {
		inventoryTitle = p_i1561_1_;
		field_94051_e = p_i1561_2_;
		slotsCount = p_i1561_3_;
		inventoryContents = new ItemStack[p_i1561_3_];
	}

	public void func_110134_a(IInvBasic p_110134_1_) {
		if (field_70480_d == null) {
			field_70480_d = new ArrayList();
		}

		field_70480_d.add(p_110134_1_);
	}

	public void func_110132_b(IInvBasic p_110132_1_) {
		field_70480_d.remove(p_110132_1_);
	}

	@Override
	public ItemStack getStackInSlot(int p_70301_1_) {
		return p_70301_1_ >= 0 && p_70301_1_ < inventoryContents.length ? inventoryContents[p_70301_1_] : null;
	}

	@Override
	public ItemStack decrStackSize(int p_70298_1_, int p_70298_2_) {
		if (inventoryContents[p_70298_1_] != null) {
			ItemStack itemstack;

			if (inventoryContents[p_70298_1_].stackSize <= p_70298_2_) {
				itemstack = inventoryContents[p_70298_1_];
				inventoryContents[p_70298_1_] = null;
				markDirty();
				return itemstack;
			} else {
				itemstack = inventoryContents[p_70298_1_].splitStack(p_70298_2_);

				if (inventoryContents[p_70298_1_].stackSize == 0) {
					inventoryContents[p_70298_1_] = null;
				}

				markDirty();
				return itemstack;
			}
		} else
			return null;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int p_70304_1_) {
		if (inventoryContents[p_70304_1_] != null) {
			ItemStack itemstack = inventoryContents[p_70304_1_];
			inventoryContents[p_70304_1_] = null;
			return itemstack;
		} else
			return null;
	}

	@Override
	public void setInventorySlotContents(int p_70299_1_, ItemStack p_70299_2_) {
		inventoryContents[p_70299_1_] = p_70299_2_;

		if (p_70299_2_ != null && p_70299_2_.stackSize > getInventoryStackLimit()) {
			p_70299_2_.stackSize = getInventoryStackLimit();
		}

		markDirty();
	}

	@Override
	public int getSizeInventory() {
		return slotsCount;
	}

	@Override
	public String getInventoryName() {
		return inventoryTitle;
	}

	@Override
	public boolean hasCustomInventoryName() {
		return field_94051_e;
	}

	public void func_110133_a(String p_110133_1_) {
		field_94051_e = true;
		inventoryTitle = p_110133_1_;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public void markDirty() {
		if (field_70480_d != null) {
			for (int i = 0; i < field_70480_d.size(); ++i) {
				((IInvBasic) field_70480_d.get(i)).onInventoryChanged(this);
			}
		}
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