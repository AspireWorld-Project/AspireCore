package net.minecraft.inventory;

import net.minecraft.entity.IMerchant;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;

public class InventoryMerchant implements IInventory {
	private final IMerchant theMerchant;
	private final ItemStack[] theInventory = new ItemStack[3];
	private final EntityPlayer thePlayer;
	private MerchantRecipe currentRecipe;
	private int currentRecipeIndex;
	private static final String __OBFID = "CL_00001756";

	public InventoryMerchant(EntityPlayer p_i1820_1_, IMerchant p_i1820_2_) {
		thePlayer = p_i1820_1_;
		theMerchant = p_i1820_2_;
	}

	public EntityPlayer getPlayer() {
		return thePlayer;
	}

	@Override
	public int getSizeInventory() {
		return theInventory.length;
	}

	@Override
	public ItemStack getStackInSlot(int p_70301_1_) {
		return theInventory[p_70301_1_];
	}

	@Override
	public ItemStack decrStackSize(int p_70298_1_, int p_70298_2_) {
		if (theInventory[p_70298_1_] != null) {
			ItemStack itemstack;

			if (p_70298_1_ == 2) {
				itemstack = theInventory[p_70298_1_];
				theInventory[p_70298_1_] = null;
				return itemstack;
			} else if (theInventory[p_70298_1_].stackSize <= p_70298_2_) {
				itemstack = theInventory[p_70298_1_];
				theInventory[p_70298_1_] = null;

				if (inventoryResetNeededOnSlotChange(p_70298_1_)) {
					resetRecipeAndSlots();
				}

				return itemstack;
			} else {
				itemstack = theInventory[p_70298_1_].splitStack(p_70298_2_);

				if (theInventory[p_70298_1_].stackSize == 0) {
					theInventory[p_70298_1_] = null;
				}

				if (inventoryResetNeededOnSlotChange(p_70298_1_)) {
					resetRecipeAndSlots();
				}

				return itemstack;
			}
		} else
			return null;
	}

	private boolean inventoryResetNeededOnSlotChange(int p_70469_1_) {
		return p_70469_1_ == 0 || p_70469_1_ == 1;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int p_70304_1_) {
		if (theInventory[p_70304_1_] != null) {
			ItemStack itemstack = theInventory[p_70304_1_];
			theInventory[p_70304_1_] = null;
			return itemstack;
		} else
			return null;
	}

	@Override
	public void setInventorySlotContents(int p_70299_1_, ItemStack p_70299_2_) {
		theInventory[p_70299_1_] = p_70299_2_;

		if (p_70299_2_ != null && p_70299_2_.stackSize > getInventoryStackLimit()) {
			p_70299_2_.stackSize = getInventoryStackLimit();
		}

		if (inventoryResetNeededOnSlotChange(p_70299_1_)) {
			resetRecipeAndSlots();
		}
	}

	@Override
	public String getInventoryName() {
		return "mob.villager";
	}

	@Override
	public boolean hasCustomInventoryName() {
		return false;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer p_70300_1_) {
		return theMerchant.getCustomer() == p_70300_1_;
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

	@Override
	public void markDirty() {
		resetRecipeAndSlots();
	}

	public void resetRecipeAndSlots() {
		currentRecipe = null;
		ItemStack itemstack = theInventory[0];
		ItemStack itemstack1 = theInventory[1];

		if (itemstack == null) {
			itemstack = itemstack1;
			itemstack1 = null;
		}

		if (itemstack == null) {
			setInventorySlotContents(2, null);
		} else {
			MerchantRecipeList merchantrecipelist = theMerchant.getRecipes(thePlayer);

			if (merchantrecipelist != null) {
				MerchantRecipe merchantrecipe = merchantrecipelist.canRecipeBeUsed(itemstack, itemstack1,
						currentRecipeIndex);

				if (merchantrecipe != null && !merchantrecipe.isRecipeDisabled()) {
					currentRecipe = merchantrecipe;
					setInventorySlotContents(2, merchantrecipe.getItemToSell().copy());
				} else if (itemstack1 != null) {
					merchantrecipe = merchantrecipelist.canRecipeBeUsed(itemstack1, itemstack, currentRecipeIndex);

					if (merchantrecipe != null && !merchantrecipe.isRecipeDisabled()) {
						currentRecipe = merchantrecipe;
						setInventorySlotContents(2, merchantrecipe.getItemToSell().copy());
					} else {
						setInventorySlotContents(2, null);
					}
				} else {
					setInventorySlotContents(2, null);
				}
			}
		}

		theMerchant.func_110297_a_(getStackInSlot(2));
	}

	public MerchantRecipe getCurrentRecipe() {
		return currentRecipe;
	}

	public void setCurrentRecipeIndex(int p_70471_1_) {
		currentRecipeIndex = p_70471_1_;
		resetRecipeAndSlots();
	}
}