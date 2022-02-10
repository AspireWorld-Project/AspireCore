package net.minecraft.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;

public class InventoryCrafting implements IInventory {
	private EntityPlayer inventoryOwner;
	private IRecipe currentRecipe;

	public void setOwner(EntityPlayer inventoryOwner) {
		this.inventoryOwner = inventoryOwner;
	}

	public void setCurrentRecipe(IRecipe recipe) {
		currentRecipe = recipe;
	}

	public IRecipe getCurrentRecipe() {
		return currentRecipe;
	}

	public EntityPlayer getOwner() {
		return inventoryOwner;
	}

	private ItemStack[] stackList;
	private int inventoryWidth;
	private Container eventHandler;
	private static final String __OBFID = "CL_00001743";

	public InventoryCrafting(Container p_i1807_1_, int p_i1807_2_, int p_i1807_3_) {
		int k = p_i1807_2_ * p_i1807_3_;
		stackList = new ItemStack[k];
		eventHandler = p_i1807_1_;
		inventoryWidth = p_i1807_2_;
	}

	@Override
	public int getSizeInventory() {
		return stackList.length;
	}

	@Override
	public ItemStack getStackInSlot(int p_70301_1_) {
		return p_70301_1_ >= getSizeInventory() ? null : stackList[p_70301_1_];
	}

	public ItemStack getStackInRowAndColumn(int p_70463_1_, int p_70463_2_) {
		if (p_70463_1_ >= 0 && p_70463_1_ < inventoryWidth) {
			int k = p_70463_1_ + p_70463_2_ * inventoryWidth;
			return getStackInSlot(k);
		} else
			return null;
	}

	@Override
	public String getInventoryName() {
		return "container.crafting";
	}

	@Override
	public boolean hasCustomInventoryName() {
		return false;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int p_70304_1_) {
		if (stackList[p_70304_1_] != null) {
			ItemStack itemstack = stackList[p_70304_1_];
			stackList[p_70304_1_] = null;
			return itemstack;
		} else
			return null;
	}

	@Override
	public ItemStack decrStackSize(int p_70298_1_, int p_70298_2_) {
		if (stackList[p_70298_1_] != null) {
			ItemStack itemstack;

			if (stackList[p_70298_1_].stackSize <= p_70298_2_) {
				itemstack = stackList[p_70298_1_];
				stackList[p_70298_1_] = null;
				if (callMatrixChanged) {
					eventHandler.onCraftMatrixChanged(this);
				}
				return itemstack;
			} else {
				itemstack = stackList[p_70298_1_].splitStack(p_70298_2_);

				if (stackList[p_70298_1_].stackSize == 0) {
					stackList[p_70298_1_] = null;
				}

				if (callMatrixChanged) {
					eventHandler.onCraftMatrixChanged(this);
				}
				return itemstack;
			}
		} else
			return null;
	}

	@Override
	public void setInventorySlotContents(int p_70299_1_, ItemStack p_70299_2_) {
		stackList[p_70299_1_] = p_70299_2_;
		if (callMatrixChanged) {
			eventHandler.onCraftMatrixChanged(this);
		}
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

	public int getWidth() {
		return inventoryWidth;
	}

	public static boolean callMatrixChanged = true;
}