package net.minecraft.inventory;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class ContainerHorseInventory extends Container {
	private final IInventory field_111243_a;
	private final EntityHorse theHorse;
	private static final String __OBFID = "CL_00001751";

	public ContainerHorseInventory(IInventory p_i1817_1_, final IInventory p_i1817_2_, final EntityHorse p_i1817_3_) {
		field_111243_a = p_i1817_2_;
		theHorse = p_i1817_3_;
		byte b0 = 3;
		p_i1817_2_.openInventory();
		int i = (b0 - 4) * 18;
		addSlotToContainer(new Slot(p_i1817_2_, 0, 8, 18) {
			private static final String __OBFID = "CL_00001752";

			@Override
			public boolean isItemValid(ItemStack p_75214_1_) {
				return super.isItemValid(p_75214_1_) && p_75214_1_.getItem() == Items.saddle && !getHasStack();
			}
		});
		addSlotToContainer(new Slot(p_i1817_2_, 1, 8, 36) {
			private static final String __OBFID = "CL_00001753";

			@Override
			public boolean isItemValid(ItemStack p_75214_1_) {
				return super.isItemValid(p_75214_1_) && p_i1817_3_.func_110259_cr()
						&& EntityHorse.func_146085_a(p_75214_1_.getItem());
			}

			@Override
			@SideOnly(Side.CLIENT)
			public boolean func_111238_b() {
				return p_i1817_3_.func_110259_cr();
			}
		});
		int j;
		int k;

		if (p_i1817_3_.isChested()) {
			for (j = 0; j < b0; ++j) {
				for (k = 0; k < 5; ++k) {
					addSlotToContainer(new Slot(p_i1817_2_, 2 + k + j * 5, 80 + k * 18, 18 + j * 18));
				}
			}
		}

		for (j = 0; j < 3; ++j) {
			for (k = 0; k < 9; ++k) {
				addSlotToContainer(new Slot(p_i1817_1_, k + j * 9 + 9, 8 + k * 18, 102 + j * 18 + i));
			}
		}

		for (j = 0; j < 9; ++j) {
			addSlotToContainer(new Slot(p_i1817_1_, j, 8 + j * 18, 160 + i));
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer p_75145_1_) {
		return field_111243_a.isUseableByPlayer(p_75145_1_) && theHorse.isEntityAlive()
				&& theHorse.getDistanceToEntity(p_75145_1_) < 8.0F;
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer p_82846_1_, int p_82846_2_) {
		ItemStack itemstack = null;
		Slot slot = (Slot) inventorySlots.get(p_82846_2_);

		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (p_82846_2_ < field_111243_a.getSizeInventory()) {
				if (!mergeItemStack(itemstack1, field_111243_a.getSizeInventory(), inventorySlots.size(), true))
					return null;
			} else if (getSlot(1).isItemValid(itemstack1) && !getSlot(1).getHasStack()) {
				if (!mergeItemStack(itemstack1, 1, 2, false))
					return null;
			} else if (getSlot(0).isItemValid(itemstack1)) {
				if (!mergeItemStack(itemstack1, 0, 1, false))
					return null;
			} else if (field_111243_a.getSizeInventory() <= 2
					|| !mergeItemStack(itemstack1, 2, field_111243_a.getSizeInventory(), false))
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
		field_111243_a.closeInventory();
	}
}