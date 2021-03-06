package net.minecraft.inventory;

import net.minecraft.entity.IMerchant;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.village.MerchantRecipe;

public class SlotMerchantResult extends Slot {
	private final InventoryMerchant theMerchantInventory;
	private final EntityPlayer thePlayer;
	private int field_75231_g;
	private final IMerchant theMerchant;
	private static final String __OBFID = "CL_00001758";

	public SlotMerchantResult(EntityPlayer p_i1822_1_, IMerchant p_i1822_2_, InventoryMerchant p_i1822_3_,
			int p_i1822_4_, int p_i1822_5_, int p_i1822_6_) {
		super(p_i1822_3_, p_i1822_4_, p_i1822_5_, p_i1822_6_);
		thePlayer = p_i1822_1_;
		theMerchant = p_i1822_2_;
		theMerchantInventory = p_i1822_3_;
	}

	@Override
	public boolean isItemValid(ItemStack p_75214_1_) {
		return false;
	}

	@Override
	public ItemStack decrStackSize(int p_75209_1_) {
		if (getHasStack()) {
			field_75231_g += Math.min(p_75209_1_, getStack().stackSize);
		}

		return super.decrStackSize(p_75209_1_);
	}

	@Override
	protected void onCrafting(ItemStack p_75210_1_, int p_75210_2_) {
		field_75231_g += p_75210_2_;
		this.onCrafting(p_75210_1_);
	}

	@Override
	protected void onCrafting(ItemStack p_75208_1_) {
		p_75208_1_.onCrafting(thePlayer.worldObj, thePlayer, field_75231_g);
		field_75231_g = 0;
	}

	@Override
	public void onPickupFromSlot(EntityPlayer p_82870_1_, ItemStack p_82870_2_) {
		this.onCrafting(p_82870_2_);
		MerchantRecipe merchantrecipe = theMerchantInventory.getCurrentRecipe();

		if (merchantrecipe != null) {
			ItemStack itemstack1 = theMerchantInventory.getStackInSlot(0);
			ItemStack itemstack2 = theMerchantInventory.getStackInSlot(1);

			if (func_75230_a(merchantrecipe, itemstack1, itemstack2)
					|| func_75230_a(merchantrecipe, itemstack2, itemstack1)) {
				theMerchant.useRecipe(merchantrecipe);

				if (itemstack1 != null && itemstack1.stackSize <= 0) {
					itemstack1 = null;
				}

				if (itemstack2 != null && itemstack2.stackSize <= 0) {
					itemstack2 = null;
				}

				theMerchantInventory.setInventorySlotContents(0, itemstack1);
				theMerchantInventory.setInventorySlotContents(1, itemstack2);
			}
		}
	}

	private boolean func_75230_a(MerchantRecipe p_75230_1_, ItemStack p_75230_2_, ItemStack p_75230_3_) {
		ItemStack itemstack2 = p_75230_1_.getItemToBuy();
		ItemStack itemstack3 = p_75230_1_.getSecondItemToBuy();

		if (p_75230_2_ != null && p_75230_2_.getItem() == itemstack2.getItem()) {
			if (itemstack3 != null && p_75230_3_ != null && itemstack3.getItem() == p_75230_3_.getItem()) {
				p_75230_2_.stackSize -= itemstack2.stackSize;
				p_75230_3_.stackSize -= itemstack3.stackSize;
				return true;
			}

			if (itemstack3 == null && p_75230_3_ == null) {
				p_75230_2_.stackSize -= itemstack2.stackSize;
				return true;
			}
		}

		return false;
	}
}