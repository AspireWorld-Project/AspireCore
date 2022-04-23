package net.minecraft.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

public class ContainerHopper extends Container {
	private final IInventory field_94538_a;
	private static final String __OBFID = "CL_00001750";

	public ContainerHopper(InventoryPlayer p_i1814_1_, IInventory p_i1814_2_) {
		field_94538_a = p_i1814_2_;
		p_i1814_2_.openInventory();
		byte b0 = 51;
		int i;

		for (i = 0; i < p_i1814_2_.getSizeInventory(); ++i) {
			addSlotToContainer(new Slot(p_i1814_2_, i, 44 + i * 18, 20));
		}

		for (i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				addSlotToContainer(new Slot(p_i1814_1_, j + i * 9 + 9, 8 + j * 18, i * 18 + b0));
			}
		}

		for (i = 0; i < 9; ++i) {
			addSlotToContainer(new Slot(p_i1814_1_, i, 8 + i * 18, 58 + b0));
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer p_75145_1_) {
		return field_94538_a.isUseableByPlayer(p_75145_1_);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer p_82846_1_, int p_82846_2_) {
		ItemStack itemstack = null;
		Slot slot = (Slot) inventorySlots.get(p_82846_2_);

		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (p_82846_2_ < field_94538_a.getSizeInventory()) {
				if (!mergeItemStack(itemstack1, field_94538_a.getSizeInventory(), inventorySlots.size(), true))
					return null;
			} else if (!mergeItemStack(itemstack1, 0, field_94538_a.getSizeInventory(), false))
				return null;

			if (itemstack1.stackSize == 0) {
				slot.putStack(null);
			} else {
				slot.onSlotChanged();
			}
		}

		return itemstack;
	}

	@Override
	public void onContainerClosed(EntityPlayer p_75134_1_) {
		super.onContainerClosed(p_75134_1_);
		field_94538_a.closeInventory();
	}
}