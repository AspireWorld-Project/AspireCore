package net.minecraft.entity.player;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ReportedException;

import java.util.concurrent.Callable;

public class InventoryPlayer implements IInventory {
	public ItemStack[] mainInventory = new ItemStack[36];
	public ItemStack[] armorInventory = new ItemStack[4];
	public int currentItem;
	@SideOnly(Side.CLIENT)
	private ItemStack currentItemStack;
	public EntityPlayer player;
	private ItemStack itemStack;
	public boolean inventoryChanged;
	private static final String __OBFID = "CL_00001709";

	public InventoryPlayer(EntityPlayer p_i1750_1_) {
		player = p_i1750_1_;
	}

	public ItemStack getCurrentItem() {
		return currentItem < 9 && currentItem >= 0 ? mainInventory[currentItem] : null;
	}

	public static int getHotbarSize() {
		return 9;
	}

	private int func_146029_c(Item p_146029_1_) {
		for (int i = 0; i < mainInventory.length; ++i) {
			if (mainInventory[i] != null && mainInventory[i].getItem() == p_146029_1_)
				return i;
		}

		return -1;
	}

	@SideOnly(Side.CLIENT)
	private int func_146024_c(Item p_146024_1_, int p_146024_2_) {
		for (int j = 0; j < mainInventory.length; ++j) {
			if (mainInventory[j] != null && mainInventory[j].getItem() == p_146024_1_
					&& mainInventory[j].getItemDamage() == p_146024_2_)
				return j;
		}

		return -1;
	}

	private int storeItemStack(ItemStack p_70432_1_) {
		for (int i = 0; i < mainInventory.length; ++i) {
			if (mainInventory[i] != null && mainInventory[i].getItem() == p_70432_1_.getItem()
					&& mainInventory[i].isStackable() && mainInventory[i].stackSize < mainInventory[i].getMaxStackSize()
					&& mainInventory[i].stackSize < getInventoryStackLimit()
					&& (!mainInventory[i].getHasSubtypes()
							|| mainInventory[i].getItemDamage() == p_70432_1_.getItemDamage())
					&& ItemStack.areItemStackTagsEqual(mainInventory[i], p_70432_1_))
				return i;
		}

		return -1;
	}

	public int getFirstEmptyStack() {
		for (int i = 0; i < mainInventory.length; ++i) {
			if (mainInventory[i] == null)
				return i;
		}

		return -1;
	}

	@SideOnly(Side.CLIENT)
	public void func_146030_a(Item p_146030_1_, int p_146030_2_, boolean p_146030_3_, boolean p_146030_4_) {
		currentItemStack = getCurrentItem();
		int k;

		if (p_146030_3_) {
			k = func_146024_c(p_146030_1_, p_146030_2_);
		} else {
			k = func_146029_c(p_146030_1_);
		}

		if (k >= 0 && k < 9) {
			currentItem = k;
		} else {
			if (p_146030_4_ && p_146030_1_ != null) {
				int j = getFirstEmptyStack();

				if (j >= 0 && j < 9) {
					currentItem = j;
				}

				func_70439_a(p_146030_1_, p_146030_2_);
			}
		}
	}

	public int clearInventory(Item p_146027_1_, int p_146027_2_) {
		int j = 0;
		int k;
		ItemStack itemstack;

		for (k = 0; k < mainInventory.length; ++k) {
			itemstack = mainInventory[k];

			if (itemstack != null && (p_146027_1_ == null || itemstack.getItem() == p_146027_1_)
					&& (p_146027_2_ <= -1 || itemstack.getItemDamage() == p_146027_2_)) {
				j += itemstack.stackSize;
				mainInventory[k] = null;
			}
		}

		for (k = 0; k < armorInventory.length; ++k) {
			itemstack = armorInventory[k];

			if (itemstack != null && (p_146027_1_ == null || itemstack.getItem() == p_146027_1_)
					&& (p_146027_2_ <= -1 || itemstack.getItemDamage() == p_146027_2_)) {
				j += itemstack.stackSize;
				armorInventory[k] = null;
			}
		}

		if (itemStack != null) {
			if (p_146027_1_ != null && itemStack.getItem() != p_146027_1_)
				return j;

			if (p_146027_2_ > -1 && itemStack.getItemDamage() != p_146027_2_)
				return j;

			j += itemStack.stackSize;
			setItemStack(null);
		}

		return j;
	}

	@SideOnly(Side.CLIENT)
	public void changeCurrentItem(int p_70453_1_) {
		if (p_70453_1_ > 0) {
			p_70453_1_ = 1;
		}

		if (p_70453_1_ < 0) {
			p_70453_1_ = -1;
		}

		for (currentItem -= p_70453_1_; currentItem < 0; currentItem += 9) {
		}

		while (currentItem >= 9) {
			currentItem -= 9;
		}
	}

	@SideOnly(Side.CLIENT)
	public void func_70439_a(Item p_70439_1_, int p_70439_2_) {
		if (p_70439_1_ != null) {
			if (currentItemStack != null && currentItemStack.isItemEnchantable()
					&& func_146024_c(currentItemStack.getItem(),
							currentItemStack.getItemDamageForDisplay()) == currentItem)
				return;

			int j = func_146024_c(p_70439_1_, p_70439_2_);

			if (j >= 0) {
				int k = mainInventory[j].stackSize;
				mainInventory[j] = mainInventory[currentItem];
				mainInventory[currentItem] = new ItemStack(p_70439_1_, k, p_70439_2_);
			} else {
				mainInventory[currentItem] = new ItemStack(p_70439_1_, 1, p_70439_2_);
			}
		}
	}

	private int storePartialItemStack(ItemStack p_70452_1_) {
		Item item = p_70452_1_.getItem();
		int i = p_70452_1_.stackSize;
		int j;

		if (p_70452_1_.getMaxStackSize() == 1) {
			j = getFirstEmptyStack();

			if (j < 0)
				return i;
			else {
				if (mainInventory[j] == null) {
					mainInventory[j] = ItemStack.copyItemStack(p_70452_1_);
				}

				return 0;
			}
		} else {
			j = storeItemStack(p_70452_1_);

			if (j < 0) {
				j = getFirstEmptyStack();
			}

			if (j < 0)
				return i;
			else {
				if (mainInventory[j] == null) {
					mainInventory[j] = new ItemStack(item, 0, p_70452_1_.getItemDamage());

					if (p_70452_1_.hasTagCompound()) {
						mainInventory[j].setTagCompound((NBTTagCompound) p_70452_1_.getTagCompound().copy());
					}
				}

				int k = i;

				if (i > mainInventory[j].getMaxStackSize() - mainInventory[j].stackSize) {
					k = mainInventory[j].getMaxStackSize() - mainInventory[j].stackSize;
				}

				if (k > getInventoryStackLimit() - mainInventory[j].stackSize) {
					k = getInventoryStackLimit() - mainInventory[j].stackSize;
				}

				if (k == 0)
					return i;
				else {
					i -= k;
					mainInventory[j].stackSize += k;
					mainInventory[j].animationsToGo = 5;
					return i;
				}
			}
		}
	}

	public void decrementAnimations() {
		for (int i = 0; i < mainInventory.length; ++i) {
			if (mainInventory[i] != null) {
				mainInventory[i].updateAnimation(player.worldObj, player, i, currentItem == i);
			}
		}

		for (int i = 0; i < armorInventory.length; i++) {
			if (armorInventory[i] != null) {
				armorInventory[i].getItem().onArmorTick(player.worldObj, player, armorInventory[i]);
			}
		}
	}

	public boolean consumeInventoryItem(Item p_146026_1_) {
		int i = func_146029_c(p_146026_1_);

		if (i < 0)
			return false;
		else {
			if (--mainInventory[i].stackSize <= 0) {
				mainInventory[i] = null;
			}

			return true;
		}
	}

	public boolean hasItem(Item p_146028_1_) {
		int i = func_146029_c(p_146028_1_);
		return i >= 0;
	}

	public boolean addItemStackToInventory(final ItemStack p_70441_1_) {
		if (p_70441_1_ != null && p_70441_1_.stackSize != 0 && p_70441_1_.getItem() != null) {
			try {
				int i;

				if (p_70441_1_.isItemDamaged()) {
					i = getFirstEmptyStack();

					if (i >= 0) {
						mainInventory[i] = ItemStack.copyItemStack(p_70441_1_);
						mainInventory[i].animationsToGo = 5;
						p_70441_1_.stackSize = 0;
						return true;
					} else if (player.capabilities.isCreativeMode) {
						p_70441_1_.stackSize = 0;
						return true;
					} else
						return false;
				} else {
					do {
						i = p_70441_1_.stackSize;
						p_70441_1_.stackSize = storePartialItemStack(p_70441_1_);
					} while (p_70441_1_.stackSize > 0 && p_70441_1_.stackSize < i);

					if (p_70441_1_.stackSize == i && player.capabilities.isCreativeMode) {
						p_70441_1_.stackSize = 0;
						return true;
					} else
						return p_70441_1_.stackSize < i;
				}
			} catch (Throwable throwable) {
				CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Adding item to inventory");
				CrashReportCategory crashreportcategory = crashreport.makeCategory("Item being added");
				crashreportcategory.addCrashSection("Item ID",
						Integer.valueOf(Item.getIdFromItem(p_70441_1_.getItem())));
				crashreportcategory.addCrashSection("Item data", Integer.valueOf(p_70441_1_.getItemDamage()));
				crashreportcategory.addCrashSectionCallable("Item name", new Callable() {
					private static final String __OBFID = "CL_00001710";

					@Override
					public String call() {
						return p_70441_1_.getDisplayName();
					}
				});
				throw new ReportedException(crashreport);
			}
		} else
			return false;
	}

	@Override
	public ItemStack decrStackSize(int p_70298_1_, int p_70298_2_) {
		ItemStack[] aitemstack = mainInventory;

		if (p_70298_1_ >= mainInventory.length) {
			aitemstack = armorInventory;
			p_70298_1_ -= mainInventory.length;
		}

		if (aitemstack[p_70298_1_] != null) {
			ItemStack itemstack;

			if (aitemstack[p_70298_1_].stackSize <= p_70298_2_) {
				itemstack = aitemstack[p_70298_1_];
				aitemstack[p_70298_1_] = null;
				return itemstack;
			} else {
				itemstack = aitemstack[p_70298_1_].splitStack(p_70298_2_);

				if (aitemstack[p_70298_1_].stackSize == 0) {
					aitemstack[p_70298_1_] = null;
				}

				return itemstack;
			}
		} else
			return null;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int p_70304_1_) {
		ItemStack[] aitemstack = mainInventory;

		if (p_70304_1_ >= mainInventory.length) {
			aitemstack = armorInventory;
			p_70304_1_ -= mainInventory.length;
		}

		if (aitemstack[p_70304_1_] != null) {
			ItemStack itemstack = aitemstack[p_70304_1_];
			aitemstack[p_70304_1_] = null;
			return itemstack;
		} else
			return null;
	}

	@Override
	public void setInventorySlotContents(int p_70299_1_, ItemStack p_70299_2_) {
		ItemStack[] aitemstack = mainInventory;

		if (p_70299_1_ >= aitemstack.length) {
			p_70299_1_ -= aitemstack.length;
			aitemstack = armorInventory;
		}

		aitemstack[p_70299_1_] = p_70299_2_;
	}

	public float func_146023_a(Block p_146023_1_) {
		float f = 1.0F;

		if (mainInventory[currentItem] != null) {
			f *= mainInventory[currentItem].func_150997_a(p_146023_1_);
		}

		return f;
	}

	public NBTTagList writeToNBT(NBTTagList p_70442_1_) {
		int i;
		NBTTagCompound nbttagcompound;

		for (i = 0; i < mainInventory.length; ++i) {
			if (mainInventory[i] != null) {
				nbttagcompound = new NBTTagCompound();
				nbttagcompound.setByte("Slot", (byte) i);
				mainInventory[i].writeToNBT(nbttagcompound);
				p_70442_1_.appendTag(nbttagcompound);
			}
		}

		for (i = 0; i < armorInventory.length; ++i) {
			if (armorInventory[i] != null) {
				nbttagcompound = new NBTTagCompound();
				nbttagcompound.setByte("Slot", (byte) (i + 100));
				armorInventory[i].writeToNBT(nbttagcompound);
				p_70442_1_.appendTag(nbttagcompound);
			}
		}

		return p_70442_1_;
	}

	public void readFromNBT(NBTTagList p_70443_1_) {
		mainInventory = new ItemStack[36];
		armorInventory = new ItemStack[4];

		for (int i = 0; i < p_70443_1_.tagCount(); ++i) {
			NBTTagCompound nbttagcompound = p_70443_1_.getCompoundTagAt(i);
			int j = nbttagcompound.getByte("Slot") & 255;
			ItemStack itemstack = ItemStack.loadItemStackFromNBT(nbttagcompound);

			if (itemstack != null) {
				if (j >= 0 && j < mainInventory.length) {
					mainInventory[j] = itemstack;
				}

				if (j >= 100 && j < armorInventory.length + 100) {
					armorInventory[j - 100] = itemstack;
				}
			}
		}
	}

	@Override
	public int getSizeInventory() {
		return mainInventory.length + 4;
	}

	@Override
	public ItemStack getStackInSlot(int p_70301_1_) {
		ItemStack[] aitemstack = mainInventory;

		if (p_70301_1_ >= aitemstack.length) {
			p_70301_1_ -= aitemstack.length;
			aitemstack = armorInventory;
		}

		return aitemstack[p_70301_1_];
	}

	@Override
	public String getInventoryName() {
		return "container.inventory";
	}

	@Override
	public boolean hasCustomInventoryName() {
		return false;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	public boolean func_146025_b(Block p_146025_1_) {
		if (p_146025_1_.getMaterial().isToolNotRequired())
			return true;
		else {
			ItemStack itemstack = getStackInSlot(currentItem);
			return itemstack != null && itemstack.func_150998_b(p_146025_1_);
		}
	}

	public ItemStack armorItemInSlot(int p_70440_1_) {
		return armorInventory[p_70440_1_];
	}

	public int getTotalArmorValue() {
		int i = 0;

		for (int j = 0; j < armorInventory.length; ++j) {
			if (armorInventory[j] != null && armorInventory[j].getItem() instanceof ItemArmor) {
				int k = ((ItemArmor) armorInventory[j].getItem()).damageReduceAmount;
				i += k;
			}
		}

		return i;
	}

	public void damageArmor(float p_70449_1_) {
		p_70449_1_ /= 4.0F;

		if (p_70449_1_ < 1.0F) {
			p_70449_1_ = 1.0F;
		}

		for (int i = 0; i < armorInventory.length; ++i) {
			if (armorInventory[i] != null && armorInventory[i].getItem() instanceof ItemArmor) {
				armorInventory[i].damageItem((int) p_70449_1_, player);

				if (armorInventory[i].stackSize == 0) {
					armorInventory[i] = null;
				}
			}
		}
	}

	public void dropAllItems() {
		int i;

		for (i = 0; i < mainInventory.length; ++i) {
			if (mainInventory[i] != null) {
				player.func_146097_a(mainInventory[i], true, false);
				mainInventory[i] = null;
			}
		}

		for (i = 0; i < armorInventory.length; ++i) {
			if (armorInventory[i] != null) {
				player.func_146097_a(armorInventory[i], true, false);
				armorInventory[i] = null;
			}
		}
	}

	@Override
	public void markDirty() {
		inventoryChanged = true;
	}

	public void setItemStack(ItemStack p_70437_1_) {
		itemStack = p_70437_1_;
	}

	public ItemStack getItemStack() {
		return itemStack;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer p_70300_1_) {
		return !player.isDead && p_70300_1_.getDistanceSqToEntity(player) <= 64.0D;
	}

	public boolean hasItemStack(ItemStack p_70431_1_) {
		int i;

		for (i = 0; i < armorInventory.length; ++i) {
			if (armorInventory[i] != null && armorInventory[i].isItemEqual(p_70431_1_))
				return true;
		}

		for (i = 0; i < mainInventory.length; ++i) {
			if (mainInventory[i] != null && mainInventory[i].isItemEqual(p_70431_1_))
				return true;
		}

		return false;
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

	public void copyInventory(InventoryPlayer p_70455_1_) {
		int i;

		for (i = 0; i < mainInventory.length; ++i) {
			mainInventory[i] = ItemStack.copyItemStack(p_70455_1_.mainInventory[i]);
		}

		for (i = 0; i < armorInventory.length; ++i) {
			armorInventory[i] = ItemStack.copyItemStack(p_70455_1_.armorInventory[i]);
		}

		currentItem = p_70455_1_.currentItem;
	}

	public int canHold(ItemStack itemstack) {
		int remains = itemstack.stackSize;

		for (int i = 0; i < mainInventory.length; ++i) {
			if (mainInventory[i] == null)
				return itemstack.stackSize;

			// Taken from firstPartial(ItemStack)
			if (mainInventory[i] != null && mainInventory[i].getItem() == itemstack.getItem()
					&& mainInventory[i].isStackable() && mainInventory[i].stackSize < mainInventory[i].getMaxStackSize()
					&& mainInventory[i].stackSize < getInventoryStackLimit()
					&& (!mainInventory[i].getHasSubtypes()
							|| mainInventory[i].getItemDamage() == itemstack.getItemDamage())
					&& ItemStack.areItemStackTagsEqual(mainInventory[i], itemstack)) {
				remains -= (mainInventory[i].getMaxStackSize() < getInventoryStackLimit()
						? mainInventory[i].getMaxStackSize()
						: getInventoryStackLimit()) - mainInventory[i].stackSize;
			}

			if (remains <= 0)
				return itemstack.stackSize;
		}

		return itemstack.stackSize - remains;
	}

	public void dropAllItemsWithoutClear() {
		int i;

		for (i = 0; i < mainInventory.length; ++i) {
			if (mainInventory[i] != null) {
				player.func_146097_a(mainInventory[i], true, false);
				// this.mainInventory[i] = null; // Cauldron - we clear this in
				// EntityPlayerMP.onDeath after PlayerDeathEvent
			}
		}

		for (i = 0; i < armorInventory.length; ++i) {
			if (armorInventory[i] != null) {
				player.func_146097_a(armorInventory[i], true, false);
				// this.armorInventory[i] = null; // Cauldron - we clear this in
				// EntityPlayerMP.onDeath after PlayerDeathEvent
			}
		}
	}
}