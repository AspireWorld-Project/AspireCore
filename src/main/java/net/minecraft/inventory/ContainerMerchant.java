package net.minecraft.inventory;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ContainerMerchant extends Container {
	private final IMerchant theMerchant;
	private final InventoryMerchant merchantInventory;
	private final World theWorld;
	private static final String __OBFID = "CL_00001757";

	public ContainerMerchant(InventoryPlayer p_i1821_1_, IMerchant p_i1821_2_, World p_i1821_3_) {
		theMerchant = p_i1821_2_;
		theWorld = p_i1821_3_;
		merchantInventory = new InventoryMerchant(p_i1821_1_.player, p_i1821_2_);
		addSlotToContainer(new Slot(merchantInventory, 0, 36, 53));
		addSlotToContainer(new Slot(merchantInventory, 1, 62, 53));
		addSlotToContainer(new SlotMerchantResult(p_i1821_1_.player, p_i1821_2_, merchantInventory, 2, 120, 53));
		int i;

		for (i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				addSlotToContainer(new Slot(p_i1821_1_, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}

		for (i = 0; i < 9; ++i) {
			addSlotToContainer(new Slot(p_i1821_1_, i, 8 + i * 18, 142));
		}
	}

	public InventoryMerchant getMerchantInventory() {
		return merchantInventory;
	}

	@Override
	public void addCraftingToCrafters(ICrafting p_75132_1_) {
		super.addCraftingToCrafters(p_75132_1_);
	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
	}

	@Override
	public void onCraftMatrixChanged(IInventory p_75130_1_) {
		merchantInventory.resetRecipeAndSlots();
		super.onCraftMatrixChanged(p_75130_1_);
	}

	public void setCurrentRecipeIndex(int p_75175_1_) {
		merchantInventory.setCurrentRecipeIndex(p_75175_1_);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int p_75137_1_, int p_75137_2_) {
	}

	@Override
	public boolean canInteractWith(EntityPlayer p_75145_1_) {
		return theMerchant.getCustomer() == p_75145_1_;
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer p_82846_1_, int p_82846_2_) {
		ItemStack itemstack = null;
		Slot slot = (Slot) inventorySlots.get(p_82846_2_);

		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (p_82846_2_ == 2) {
				if (!mergeItemStack(itemstack1, 3, 39, true))
					return null;

				slot.onSlotChange(itemstack1, itemstack);
			} else if (p_82846_2_ != 0 && p_82846_2_ != 1) {
				if (p_82846_2_ >= 3 && p_82846_2_ < 30) {
					if (!mergeItemStack(itemstack1, 30, 39, false))
						return null;
				} else if (p_82846_2_ >= 30 && p_82846_2_ < 39 && !mergeItemStack(itemstack1, 3, 30, false))
					return null;
			} else if (!mergeItemStack(itemstack1, 3, 39, false))
				return null;

			if (itemstack1.stackSize == 0) {
				slot.putStack(null);
			} else {
				slot.onSlotChanged();
			}

			if (itemstack1.stackSize == itemstack.stackSize)
				return null;

			slot.onPickupFromSlot(p_82846_1_, itemstack1);
		}

		return itemstack;
	}

	@Override
	public void onContainerClosed(EntityPlayer p_75134_1_) {
		super.onContainerClosed(p_75134_1_);
		theMerchant.setCustomer(null);
		super.onContainerClosed(p_75134_1_);

		if (!theWorld.isRemote) {
			ItemStack itemstack = merchantInventory.getStackInSlotOnClosing(0);

			if (itemstack != null) {
				p_75134_1_.dropPlayerItemWithRandomChoice(itemstack, false);
			}

			itemstack = merchantInventory.getStackInSlotOnClosing(1);

			if (itemstack != null) {
				p_75134_1_.dropPlayerItemWithRandomChoice(itemstack, false);
			}
		}
	}
}