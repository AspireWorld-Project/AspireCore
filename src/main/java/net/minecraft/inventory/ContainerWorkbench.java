package net.minecraft.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.world.World;

public class ContainerWorkbench extends Container {
	public InventoryCrafting craftMatrix = new InventoryCrafting(this, 3, 3);
	public IInventory craftResult = new InventoryCraftResult();
	private World worldObj;
	private int posX;
	private int posY;
	private int posZ;
	private static final String __OBFID = "CL_00001744";

	public ContainerWorkbench(InventoryPlayer p_i1808_1_, World p_i1808_2_, int p_i1808_3_, int p_i1808_4_,
			int p_i1808_5_) {
		worldObj = p_i1808_2_;
		posX = p_i1808_3_;
		posY = p_i1808_4_;
		posZ = p_i1808_5_;
		addSlotToContainer(new SlotCrafting(p_i1808_1_.player, craftMatrix, craftResult, 0, 124, 35));
		int l;
		int i1;

		for (l = 0; l < 3; ++l) {
			for (i1 = 0; i1 < 3; ++i1) {
				addSlotToContainer(new Slot(craftMatrix, i1 + l * 3, 30 + i1 * 18, 17 + l * 18));
			}
		}

		for (l = 0; l < 3; ++l) {
			for (i1 = 0; i1 < 9; ++i1) {
				addSlotToContainer(new Slot(p_i1808_1_, i1 + l * 9 + 9, 8 + i1 * 18, 84 + l * 18));
			}
		}

		for (l = 0; l < 9; ++l) {
			addSlotToContainer(new Slot(p_i1808_1_, l, 8 + l * 18, 142));
		}

		onCraftMatrixChanged(craftMatrix);
		craftMatrix.setOwner(p_i1808_1_.player);
	}

	@Override
	public void onCraftMatrixChanged(IInventory p_75130_1_) {
		craftResult.setInventorySlotContents(0,
				CraftingManager.getInstance().findMatchingRecipe(craftMatrix, worldObj));
	}

	@Override
	public void onContainerClosed(EntityPlayer p_75134_1_) {
		super.onContainerClosed(p_75134_1_);

		if (!worldObj.isRemote) {
			for (int i = 0; i < 9; ++i) {
				ItemStack itemstack = craftMatrix.getStackInSlotOnClosing(i);

				if (itemstack != null) {
					p_75134_1_.dropPlayerItemWithRandomChoice(itemstack, false);
				}
			}
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer p_75145_1_) {
		return worldObj.getBlock(posX, posY, posZ) != Blocks.crafting_table ? false
				: p_75145_1_.getDistanceSq(posX + 0.5D, posY + 0.5D, posZ + 0.5D) <= 64.0D;
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer p_82846_1_, int p_82846_2_) {
		ItemStack itemstack = null;
		Slot slot = (Slot) inventorySlots.get(p_82846_2_);

		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (p_82846_2_ == 0) {
				if (!mergeItemStack(itemstack1, 10, 46, true))
					return null;

				slot.onSlotChange(itemstack1, itemstack);
			} else if (p_82846_2_ >= 10 && p_82846_2_ < 37) {
				if (!mergeItemStack(itemstack1, 37, 46, false))
					return null;
			} else if (p_82846_2_ >= 37 && p_82846_2_ < 46) {
				if (!mergeItemStack(itemstack1, 10, 37, false))
					return null;
			} else if (!mergeItemStack(itemstack1, 10, 46, false))
				return null;

			if (itemstack1.stackSize == 0) {
				slot.putStack((ItemStack) null);
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