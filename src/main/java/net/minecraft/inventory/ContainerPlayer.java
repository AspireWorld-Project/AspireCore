package net.minecraft.inventory;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.util.IIcon;

public class ContainerPlayer extends Container {
	public InventoryCrafting craftMatrix = new InventoryCrafting(this, 2, 2);
	public IInventory craftResult = new InventoryCraftResult();
	public boolean isLocalWorld;
	private final EntityPlayer thePlayer;
	private static final String __OBFID = "CL_00001754";

	public ContainerPlayer(final InventoryPlayer p_i1819_1_, boolean p_i1819_2_, EntityPlayer p_i1819_3_) {
		isLocalWorld = p_i1819_2_;
		thePlayer = p_i1819_3_;
		addSlotToContainer(new SlotCrafting(p_i1819_1_.player, craftMatrix, craftResult, 0, 144, 36));
		int i;
		int j;

		for (i = 0; i < 2; ++i) {
			for (j = 0; j < 2; ++j) {
				addSlotToContainer(new Slot(craftMatrix, j + i * 2, 88 + j * 18, 26 + i * 18));
			}
		}

		for (i = 0; i < 4; ++i) {
			final int k = i;
			addSlotToContainer(new Slot(p_i1819_1_, p_i1819_1_.getSizeInventory() - 1 - i, 8, 8 + i * 18) {
				private static final String __OBFID = "CL_00001755";

				@Override
				public int getSlotStackLimit() {
					return 1;
				}

				@Override
				public boolean isItemValid(ItemStack p_75214_1_) {
					if (p_75214_1_ == null)
						return false;
					return p_75214_1_.getItem().isValidArmor(p_75214_1_, k, thePlayer);
				}

				@Override
				@SideOnly(Side.CLIENT)
				public IIcon getBackgroundIconIndex() {
					return ItemArmor.func_94602_b(k);
				}
			});
		}

		for (i = 0; i < 3; ++i) {
			for (j = 0; j < 9; ++j) {
				addSlotToContainer(new Slot(p_i1819_1_, j + (i + 1) * 9, 8 + j * 18, 84 + i * 18));
			}
		}

		for (i = 0; i < 9; ++i) {
			addSlotToContainer(new Slot(p_i1819_1_, i, 8 + i * 18, 142));
		}

		onCraftMatrixChanged(craftMatrix);

		craftMatrix.setOwner(p_i1819_1_.player);
	}

	@Override
	public void onCraftMatrixChanged(IInventory p_75130_1_) {
		craftResult.setInventorySlotContents(0,
				CraftingManager.getInstance().findMatchingRecipe(craftMatrix, thePlayer.worldObj));
	}

	@Override
	public void onContainerClosed(EntityPlayer p_75134_1_) {
		super.onContainerClosed(p_75134_1_);

		for (int i = 0; i < 4; ++i) {
			ItemStack itemstack = craftMatrix.getStackInSlotOnClosing(i);

			if (itemstack != null) {
				p_75134_1_.dropPlayerItemWithRandomChoice(itemstack, false);
			}
		}

		craftResult.setInventorySlotContents(0, null);
	}

	@Override
	public boolean canInteractWith(EntityPlayer p_75145_1_) {
		return true;
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer p_82846_1_, int p_82846_2_) {
		ItemStack itemstack = null;
		Slot slot = (Slot) inventorySlots.get(p_82846_2_);

		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (p_82846_2_ == 0) {
				if (!mergeItemStack(itemstack1, 9, 45, true))
					return null;

				slot.onSlotChange(itemstack1, itemstack);
			} else if (p_82846_2_ >= 1 && p_82846_2_ < 5) {
				if (!mergeItemStack(itemstack1, 9, 45, false))
					return null;
			} else if (p_82846_2_ >= 5 && p_82846_2_ < 9) {
				if (!mergeItemStack(itemstack1, 9, 45, false))
					return null;
			} else if (itemstack.getItem() instanceof ItemArmor
					&& !((Slot) inventorySlots.get(5 + ((ItemArmor) itemstack.getItem()).armorType)).getHasStack()) {
				int j = 5 + ((ItemArmor) itemstack.getItem()).armorType;

				if (!mergeItemStack(itemstack1, j, j + 1, false))
					return null;
			} else if (p_82846_2_ >= 9 && p_82846_2_ < 36) {
				if (!mergeItemStack(itemstack1, 36, 45, false))
					return null;
			} else if (p_82846_2_ >= 36 && p_82846_2_ < 45) {
				if (!mergeItemStack(itemstack1, 9, 36, false))
					return null;
			} else if (!mergeItemStack(itemstack1, 9, 45, false))
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
	public boolean func_94530_a(ItemStack p_94530_1_, Slot p_94530_2_) {
		return p_94530_2_.inventory != craftResult && super.func_94530_a(p_94530_1_, p_94530_2_);
	}
}