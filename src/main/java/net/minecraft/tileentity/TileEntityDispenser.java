package net.minecraft.tileentity;

import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class TileEntityDispenser extends TileEntity implements IInventory {
	private ItemStack[] field_146022_i = new ItemStack[9];
	private Random field_146021_j = new Random();
	protected String field_146020_a;
	private static final String __OBFID = "CL_00000352";

	@Override
	public int getSizeInventory() {
		return 9;
	}

	@Override
	public ItemStack getStackInSlot(int p_70301_1_) {
		return field_146022_i[p_70301_1_];
	}

	@Override
	public ItemStack decrStackSize(int p_70298_1_, int p_70298_2_) {
		if (field_146022_i[p_70298_1_] != null) {
			ItemStack itemstack;

			if (field_146022_i[p_70298_1_].stackSize <= p_70298_2_) {
				itemstack = field_146022_i[p_70298_1_];
				field_146022_i[p_70298_1_] = null;
				markDirty();
				return itemstack;
			} else {
				itemstack = field_146022_i[p_70298_1_].splitStack(p_70298_2_);

				if (field_146022_i[p_70298_1_].stackSize == 0) {
					field_146022_i[p_70298_1_] = null;
				}

				markDirty();
				return itemstack;
			}
		} else
			return null;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int p_70304_1_) {
		if (field_146022_i[p_70304_1_] != null) {
			ItemStack itemstack = field_146022_i[p_70304_1_];
			field_146022_i[p_70304_1_] = null;
			return itemstack;
		} else
			return null;
	}

	public int func_146017_i() {
		int i = -1;
		int j = 1;

		for (int k = 0; k < field_146022_i.length; ++k) {
			if (field_146022_i[k] != null && field_146021_j.nextInt(j++) == 0) {
				i = k;
			}
		}

		return i;
	}

	@Override
	public void setInventorySlotContents(int p_70299_1_, ItemStack p_70299_2_) {
		field_146022_i[p_70299_1_] = p_70299_2_;

		if (p_70299_2_ != null && p_70299_2_.stackSize > getInventoryStackLimit()) {
			p_70299_2_.stackSize = getInventoryStackLimit();
		}

		markDirty();
	}

	public int func_146019_a(ItemStack p_146019_1_) {
		for (int i = 0; i < field_146022_i.length; ++i) {
			if (field_146022_i[i] == null || field_146022_i[i].getItem() == null) {
				setInventorySlotContents(i, p_146019_1_);
				return i;
			}
		}

		return -1;
	}

	@Override
	public String getInventoryName() {
		return hasCustomInventoryName() ? field_146020_a : "container.dispenser";
	}

	public void func_146018_a(String p_146018_1_) {
		field_146020_a = p_146018_1_;
	}

	@Override
	public boolean hasCustomInventoryName() {
		return field_146020_a != null;
	}

	@Override
	public void readFromNBT(NBTTagCompound p_145839_1_) {
		super.readFromNBT(p_145839_1_);
		NBTTagList nbttaglist = p_145839_1_.getTagList("Items", 10);
		field_146022_i = new ItemStack[getSizeInventory()];

		for (int i = 0; i < nbttaglist.tagCount(); ++i) {
			NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
			int j = nbttagcompound1.getByte("Slot") & 255;

			if (j >= 0 && j < field_146022_i.length) {
				field_146022_i[j] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
			}
		}

		if (p_145839_1_.hasKey("CustomName", 8)) {
			field_146020_a = p_145839_1_.getString("CustomName");
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound p_145841_1_) {
		super.writeToNBT(p_145841_1_);
		NBTTagList nbttaglist = new NBTTagList();

		for (int i = 0; i < field_146022_i.length; ++i) {
			if (field_146022_i[i] != null) {
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setByte("Slot", (byte) i);
				field_146022_i[i].writeToNBT(nbttagcompound1);
				nbttaglist.appendTag(nbttagcompound1);
			}
		}

		p_145841_1_.setTag("Items", nbttaglist);

		if (hasCustomInventoryName()) {
			p_145841_1_.setString("CustomName", field_146020_a);
		}
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer p_70300_1_) {
		return worldObj.getTileEntity(xCoord, yCoord, zCoord) != this ? false
				: p_70300_1_.getDistanceSq(xCoord + 0.5D, yCoord + 0.5D, zCoord + 0.5D) <= 64.0D;
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
}