package net.minecraft.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntityEnderChest;

public class InventoryEnderChest extends InventoryBasic {
	private TileEntityEnderChest associatedChest;
	private static final String __OBFID = "CL_00001759";

	public InventoryEnderChest() {
		super("container.enderchest", false, 27);
	}

	public void func_146031_a(TileEntityEnderChest p_146031_1_) {
		associatedChest = p_146031_1_;
	}

	public void loadInventoryFromNBT(NBTTagList p_70486_1_) {
		int i;

		for (i = 0; i < getSizeInventory(); ++i) {
			setInventorySlotContents(i, null);
		}

		for (i = 0; i < p_70486_1_.tagCount(); ++i) {
			NBTTagCompound nbttagcompound = p_70486_1_.getCompoundTagAt(i);
			int j = nbttagcompound.getByte("Slot") & 255;

			if (j >= 0 && j < getSizeInventory()) {
				setInventorySlotContents(j, ItemStack.loadItemStackFromNBT(nbttagcompound));
			}
		}
	}

	public NBTTagList saveInventoryToNBT() {
		NBTTagList nbttaglist = new NBTTagList();

		for (int i = 0; i < getSizeInventory(); ++i) {
			ItemStack itemstack = getStackInSlot(i);

			if (itemstack != null) {
				NBTTagCompound nbttagcompound = new NBTTagCompound();
				nbttagcompound.setByte("Slot", (byte) i);
				itemstack.writeToNBT(nbttagcompound);
				nbttaglist.appendTag(nbttagcompound);
			}
		}

		return nbttaglist;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer p_70300_1_) {
		return (associatedChest == null || associatedChest.func_145971_a(p_70300_1_)) && super.isUseableByPlayer(p_70300_1_);
	}

	@Override
	public void openInventory() {
		if (associatedChest != null) {
			associatedChest.func_145969_a();
		}

		super.openInventory();
	}

	@Override
	public void closeInventory() {
		if (associatedChest != null) {
			associatedChest.func_145970_b();
		}

		super.closeInventory();
		associatedChest = null;
	}

	private EntityPlayer inventoryOwner;

	public EntityPlayer getOwner() {
		return inventoryOwner;
	}

	public void setOwner(EntityPlayer player) {
		inventoryOwner = player;
	}
}