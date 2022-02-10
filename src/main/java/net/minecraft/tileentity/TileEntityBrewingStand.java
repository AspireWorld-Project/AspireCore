package net.minecraft.tileentity;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.InventoryHolder;
import org.ultramine.bukkit.util.BukkitUtil;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.PotionHelper;

public class TileEntityBrewingStand extends TileEntity implements ISidedInventory {
	private static final int[] field_145941_a = new int[] { 3 };
	private static final int[] field_145947_i = new int[] { 0, 1, 2 };
	private ItemStack[] brewingItemStacks = new ItemStack[4];
	private int brewTime;
	private int filledSlots;
	private Item ingredientID;
	private String field_145942_n;
	private static final String __OBFID = "CL_00000345";

	@Override
	public String getInventoryName() {
		return hasCustomInventoryName() ? field_145942_n : "container.brewing";
	}

	@Override
	public boolean hasCustomInventoryName() {
		return field_145942_n != null && field_145942_n.length() > 0;
	}

	public void func_145937_a(String p_145937_1_) {
		field_145942_n = p_145937_1_;
	}

	@Override
	public int getSizeInventory() {
		return brewingItemStacks.length;
	}

	@Override
	public void updateEntity() {
		if (brewTime > 0) {
			--brewTime;

			if (brewTime == 0) {
				brewPotions();
				markDirty();
			} else if (!canBrew()) {
				brewTime = 0;
				markDirty();
			} else if (ingredientID != brewingItemStacks[3].getItem()) {
				brewTime = 0;
				markDirty();
			}
		} else if (canBrew()) {
			brewTime = 400;
			ingredientID = brewingItemStacks[3].getItem();
		}

		int i = getFilledSlots();

		if (i != filledSlots) {
			filledSlots = i;
			worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, i, 2);
		}

		super.updateEntity();
	}

	public int getBrewTime() {
		return brewTime;
	}

	private boolean canBrew() {
		if (brewingItemStacks[3] != null && brewingItemStacks[3].stackSize > 0) {
			ItemStack itemstack = brewingItemStacks[3];

			if (!itemstack.getItem().isPotionIngredient(itemstack))
				return false;
			else {
				boolean flag = false;

				for (int i = 0; i < 3; ++i) {
					if (brewingItemStacks[i] != null && brewingItemStacks[i].getItem() instanceof ItemPotion) {
						int j = brewingItemStacks[i].getItemDamage();
						int k = func_145936_c(j, itemstack);

						if (!ItemPotion.isSplash(j) && ItemPotion.isSplash(k)) {
							flag = true;
							break;
						}

						List list = Items.potionitem.getEffects(j);
						List list1 = Items.potionitem.getEffects(k);

						if ((j <= 0 || list != list1) && (list == null || !list.equals(list1) && list1 != null)
								&& j != k) {
							flag = true;
							break;
						}
					}
				}
				return flag;
			}
		} else
			return false;
	}

	private void brewPotions() {
		if (net.minecraftforge.event.ForgeEventFactory.onPotionAttemptBreaw(brewingItemStacks))
			return;
		if (canBrew()) {
			InventoryHolder inventoryHolder = BukkitUtil.getInventoryOwner(this);
			if (inventoryHolder != null) {
				BrewEvent event = new BrewEvent(worldObj.getWorld().getBlockAt(xCoord, yCoord, zCoord),
						(BrewerInventory) inventoryHolder.getInventory());
				Bukkit.getPluginManager().callEvent(event);
				if (event.isCancelled())
					return;
			}
			ItemStack itemstack = brewingItemStacks[3];

			for (int i = 0; i < 3; ++i) {
				if (brewingItemStacks[i] != null && brewingItemStacks[i].getItem() instanceof ItemPotion) {
					int j = brewingItemStacks[i].getItemDamage();
					int k = func_145936_c(j, itemstack);
					List list = Items.potionitem.getEffects(j);
					List list1 = Items.potionitem.getEffects(k);

					if ((j <= 0 || list != list1) && (list == null || !list.equals(list1) && list1 != null)) {
						if (j != k) {
							brewingItemStacks[i].setItemDamage(k);
						}
					} else if (!ItemPotion.isSplash(j) && ItemPotion.isSplash(k)) {
						brewingItemStacks[i].setItemDamage(k);
					}
				}
			}

			if (itemstack.getItem().hasContainerItem(itemstack)) {
				brewingItemStacks[3] = itemstack.getItem().getContainerItem(itemstack);
			} else {
				--brewingItemStacks[3].stackSize;

				if (brewingItemStacks[3].stackSize <= 0) {
					brewingItemStacks[3] = null;
				}
			}
			net.minecraftforge.event.ForgeEventFactory.onPotionBrewed(brewingItemStacks);
		}
	}

	private int func_145936_c(int p_145936_1_, ItemStack p_145936_2_) {
		return p_145936_2_ == null ? p_145936_1_
				: p_145936_2_.getItem().isPotionIngredient(p_145936_2_)
						? PotionHelper.applyIngredient(p_145936_1_, p_145936_2_.getItem().getPotionEffect(p_145936_2_))
						: p_145936_1_;
	}

	@Override
	public void readFromNBT(NBTTagCompound p_145839_1_) {
		super.readFromNBT(p_145839_1_);
		NBTTagList nbttaglist = p_145839_1_.getTagList("Items", 10);
		brewingItemStacks = new ItemStack[getSizeInventory()];

		for (int i = 0; i < nbttaglist.tagCount(); ++i) {
			NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
			byte b0 = nbttagcompound1.getByte("Slot");

			if (b0 >= 0 && b0 < brewingItemStacks.length) {
				brewingItemStacks[b0] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
			}
		}

		brewTime = p_145839_1_.getShort("BrewTime");

		if (p_145839_1_.hasKey("CustomName", 8)) {
			field_145942_n = p_145839_1_.getString("CustomName");
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound p_145841_1_) {
		super.writeToNBT(p_145841_1_);
		p_145841_1_.setShort("BrewTime", (short) brewTime);
		NBTTagList nbttaglist = new NBTTagList();

		for (int i = 0; i < brewingItemStacks.length; ++i) {
			if (brewingItemStacks[i] != null) {
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setByte("Slot", (byte) i);
				brewingItemStacks[i].writeToNBT(nbttagcompound1);
				nbttaglist.appendTag(nbttagcompound1);
			}
		}

		p_145841_1_.setTag("Items", nbttaglist);

		if (hasCustomInventoryName()) {
			p_145841_1_.setString("CustomName", field_145942_n);
		}
	}

	@Override
	public ItemStack getStackInSlot(int p_70301_1_) {
		return p_70301_1_ >= 0 && p_70301_1_ < brewingItemStacks.length ? brewingItemStacks[p_70301_1_] : null;
	}

	@Override
	public ItemStack decrStackSize(int p_70298_1_, int p_70298_2_) {
		if (p_70298_1_ >= 0 && p_70298_1_ < brewingItemStacks.length) {
			ItemStack itemstack = brewingItemStacks[p_70298_1_];
			brewingItemStacks[p_70298_1_] = null;
			return itemstack;
		} else
			return null;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int p_70304_1_) {
		if (p_70304_1_ >= 0 && p_70304_1_ < brewingItemStacks.length) {
			ItemStack itemstack = brewingItemStacks[p_70304_1_];
			brewingItemStacks[p_70304_1_] = null;
			return itemstack;
		} else
			return null;
	}

	@Override
	public void setInventorySlotContents(int p_70299_1_, ItemStack p_70299_2_) {
		if (p_70299_1_ >= 0 && p_70299_1_ < brewingItemStacks.length) {
			brewingItemStacks[p_70299_1_] = p_70299_2_;
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
		return p_94041_1_ == 3 ? p_94041_2_.getItem().isPotionIngredient(p_94041_2_)
				: p_94041_2_.getItem() instanceof ItemPotion || p_94041_2_.getItem() == Items.glass_bottle;
	}

	@SideOnly(Side.CLIENT)
	public void func_145938_d(int p_145938_1_) {
		brewTime = p_145938_1_;
	}

	public int getFilledSlots() {
		int i = 0;

		for (int j = 0; j < 3; ++j) {
			if (brewingItemStacks[j] != null) {
				i |= 1 << j;
			}
		}

		return i;
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int p_94128_1_) {
		return p_94128_1_ == 1 ? field_145941_a : field_145947_i;
	}

	@Override
	public boolean canInsertItem(int p_102007_1_, ItemStack p_102007_2_, int p_102007_3_) {
		return isItemValidForSlot(p_102007_1_, p_102007_2_);
	}

	@Override
	public boolean canExtractItem(int p_102008_1_, ItemStack p_102008_2_, int p_102008_3_) {
		return true;
	}

	public void setBrewTime(int brewTime) {
		this.brewTime = brewTime;
	}
}