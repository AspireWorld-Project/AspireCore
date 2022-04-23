package net.minecraft.inventory;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntityFurnace;

public class ContainerFurnace extends Container {
	private final TileEntityFurnace tileFurnace;
	private int lastCookTime;
	private int lastBurnTime;
	private int lastItemBurnTime;
	private static final String __OBFID = "CL_00001748";

	public ContainerFurnace(InventoryPlayer p_i1812_1_, TileEntityFurnace p_i1812_2_) {
		tileFurnace = p_i1812_2_;
		addSlotToContainer(new Slot(p_i1812_2_, 0, 56, 17));
		addSlotToContainer(new Slot(p_i1812_2_, 1, 56, 53));
		addSlotToContainer(new SlotFurnace(p_i1812_1_.player, p_i1812_2_, 2, 116, 35));
		int i;

		for (i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				addSlotToContainer(new Slot(p_i1812_1_, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}

		for (i = 0; i < 9; ++i) {
			addSlotToContainer(new Slot(p_i1812_1_, i, 8 + i * 18, 142));
		}
	}

	@Override
	public void addCraftingToCrafters(ICrafting p_75132_1_) {
		super.addCraftingToCrafters(p_75132_1_);
		p_75132_1_.sendProgressBarUpdate(this, 0, tileFurnace.furnaceCookTime);
		p_75132_1_.sendProgressBarUpdate(this, 1, tileFurnace.furnaceBurnTime);
		p_75132_1_.sendProgressBarUpdate(this, 2, tileFurnace.currentItemBurnTime);
	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();

		for (int i = 0; i < crafters.size(); ++i) {
			ICrafting icrafting = (ICrafting) crafters.get(i);

			if (lastCookTime != tileFurnace.furnaceCookTime) {
				icrafting.sendProgressBarUpdate(this, 0, tileFurnace.furnaceCookTime);
			}

			if (lastBurnTime != tileFurnace.furnaceBurnTime) {
				icrafting.sendProgressBarUpdate(this, 1, tileFurnace.furnaceBurnTime);
			}

			if (lastItemBurnTime != tileFurnace.currentItemBurnTime) {
				icrafting.sendProgressBarUpdate(this, 2, tileFurnace.currentItemBurnTime);
			}
		}

		lastCookTime = tileFurnace.furnaceCookTime;
		lastBurnTime = tileFurnace.furnaceBurnTime;
		lastItemBurnTime = tileFurnace.currentItemBurnTime;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int p_75137_1_, int p_75137_2_) {
		if (p_75137_1_ == 0) {
			tileFurnace.furnaceCookTime = p_75137_2_;
		}

		if (p_75137_1_ == 1) {
			tileFurnace.furnaceBurnTime = p_75137_2_;
		}

		if (p_75137_1_ == 2) {
			tileFurnace.currentItemBurnTime = p_75137_2_;
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer p_75145_1_) {
		return tileFurnace.isUseableByPlayer(p_75145_1_);
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
			} else if (p_82846_2_ != 1 && p_82846_2_ != 0) {
				if (FurnaceRecipes.smelting().getSmeltingResult(itemstack1) != null) {
					if (!mergeItemStack(itemstack1, 0, 1, false))
						return null;
				} else if (TileEntityFurnace.isItemFuel(itemstack1)) {
					if (!mergeItemStack(itemstack1, 1, 2, false))
						return null;
				} else if (p_82846_2_ >= 3 && p_82846_2_ < 30) {
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
}