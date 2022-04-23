package net.minecraft.inventory;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityBeacon;

public class ContainerBeacon extends Container {
	private final TileEntityBeacon tileBeacon;
	private final ContainerBeacon.BeaconSlot beaconSlot;
	private final int field_82865_g;
	private final int field_82867_h;
	private final int field_82868_i;
	private static final String __OBFID = "CL_00001735";

	public ContainerBeacon(InventoryPlayer p_i1802_1_, TileEntityBeacon p_i1802_2_) {
		tileBeacon = p_i1802_2_;
		addSlotToContainer(beaconSlot = new ContainerBeacon.BeaconSlot(p_i1802_2_, 0, 136, 110));
		byte b0 = 36;
		short short1 = 137;
		int i;

		for (i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				addSlotToContainer(new Slot(p_i1802_1_, j + i * 9 + 9, b0 + j * 18, short1 + i * 18));
			}
		}

		for (i = 0; i < 9; ++i) {
			addSlotToContainer(new Slot(p_i1802_1_, i, b0 + i * 18, 58 + short1));
		}

		field_82865_g = p_i1802_2_.getLevels();
		field_82867_h = p_i1802_2_.getPrimaryEffect();
		field_82868_i = p_i1802_2_.getSecondaryEffect();
	}

	@Override
	public void addCraftingToCrafters(ICrafting p_75132_1_) {
		super.addCraftingToCrafters(p_75132_1_);
		p_75132_1_.sendProgressBarUpdate(this, 0, field_82865_g);
		p_75132_1_.sendProgressBarUpdate(this, 1, field_82867_h);
		p_75132_1_.sendProgressBarUpdate(this, 2, field_82868_i);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int p_75137_1_, int p_75137_2_) {
		if (p_75137_1_ == 0) {
			tileBeacon.func_146005_c(p_75137_2_);
		}

		if (p_75137_1_ == 1) {
			tileBeacon.setPrimaryEffect(p_75137_2_);
		}

		if (p_75137_1_ == 2) {
			tileBeacon.setSecondaryEffect(p_75137_2_);
		}
	}

	public TileEntityBeacon func_148327_e() {
		return tileBeacon;
	}

	@Override
	public boolean canInteractWith(EntityPlayer p_75145_1_) {
		return tileBeacon.isUseableByPlayer(p_75145_1_);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer p_82846_1_, int p_82846_2_) {
		ItemStack itemstack = null;
		Slot slot = (Slot) inventorySlots.get(p_82846_2_);

		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (p_82846_2_ == 0) {
				if (!mergeItemStack(itemstack1, 1, 37, true))
					return null;

				slot.onSlotChange(itemstack1, itemstack);
			} else if (!beaconSlot.getHasStack() && beaconSlot.isItemValid(itemstack1) && itemstack1.stackSize == 1) {
				if (!mergeItemStack(itemstack1, 0, 1, false))
					return null;
			} else if (p_82846_2_ >= 1 && p_82846_2_ < 28) {
				if (!mergeItemStack(itemstack1, 28, 37, false))
					return null;
			} else if (p_82846_2_ >= 28 && p_82846_2_ < 37) {
				if (!mergeItemStack(itemstack1, 1, 28, false))
					return null;
			} else if (!mergeItemStack(itemstack1, 1, 37, false))
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

	class BeaconSlot extends Slot {
		private static final String __OBFID = "CL_00001736";

		public BeaconSlot(IInventory p_i1801_2_, int p_i1801_3_, int p_i1801_4_, int p_i1801_5_) {
			super(p_i1801_2_, p_i1801_3_, p_i1801_4_, p_i1801_5_);
		}

		@Override
		public boolean isItemValid(ItemStack p_75214_1_) {
			return p_75214_1_ != null && p_75214_1_.getItem() != null
					&& p_75214_1_.getItem().isBeaconPayment(p_75214_1_);
		}

		@Override
		public int getSlotStackLimit() {
			return 1;
		}
	}
}