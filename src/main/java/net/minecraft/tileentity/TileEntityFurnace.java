package net.minecraft.tileentity;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFurnace;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.*;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;

public class TileEntityFurnace extends TileEntity implements ISidedInventory {
	private static final int[] slotsTop = new int[] { 0 };
	private static final int[] slotsBottom = new int[] { 2, 1 };
	private static final int[] slotsSides = new int[] { 1 };
	private ItemStack[] furnaceItemStacks = new ItemStack[3];
	public int furnaceBurnTime;
	public int currentItemBurnTime;
	public int furnaceCookTime;
	private String field_145958_o;
	private static final String __OBFID = "CL_00000357";

	@Override
	public int getSizeInventory() {
		return furnaceItemStacks.length;
	}

	@Override
	public ItemStack getStackInSlot(int p_70301_1_) {
		return furnaceItemStacks[p_70301_1_];
	}

	@Override
	public ItemStack decrStackSize(int p_70298_1_, int p_70298_2_) {
		if (furnaceItemStacks[p_70298_1_] != null) {
			ItemStack itemstack;

			if (furnaceItemStacks[p_70298_1_].stackSize <= p_70298_2_) {
				itemstack = furnaceItemStacks[p_70298_1_];
				furnaceItemStacks[p_70298_1_] = null;
				return itemstack;
			} else {
				itemstack = furnaceItemStacks[p_70298_1_].splitStack(p_70298_2_);

				if (furnaceItemStacks[p_70298_1_].stackSize == 0) {
					furnaceItemStacks[p_70298_1_] = null;
				}

				return itemstack;
			}
		} else
			return null;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int p_70304_1_) {
		if (furnaceItemStacks[p_70304_1_] != null) {
			ItemStack itemstack = furnaceItemStacks[p_70304_1_];
			furnaceItemStacks[p_70304_1_] = null;
			return itemstack;
		} else
			return null;
	}

	@Override
	public void setInventorySlotContents(int p_70299_1_, ItemStack p_70299_2_) {
		furnaceItemStacks[p_70299_1_] = p_70299_2_;

		if (p_70299_2_ != null && p_70299_2_.stackSize > getInventoryStackLimit()) {
			p_70299_2_.stackSize = getInventoryStackLimit();
		}
	}

	@Override
	public String getInventoryName() {
		return hasCustomInventoryName() ? field_145958_o : "container.furnace";
	}

	@Override
	public boolean hasCustomInventoryName() {
		return field_145958_o != null && field_145958_o.length() > 0;
	}

	public void func_145951_a(String p_145951_1_) {
		field_145958_o = p_145951_1_;
	}

	@Override
	public void readFromNBT(NBTTagCompound p_145839_1_) {
		super.readFromNBT(p_145839_1_);
		NBTTagList nbttaglist = p_145839_1_.getTagList("Items", 10);
		furnaceItemStacks = new ItemStack[getSizeInventory()];

		for (int i = 0; i < nbttaglist.tagCount(); ++i) {
			NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
			byte b0 = nbttagcompound1.getByte("Slot");

			if (b0 >= 0 && b0 < furnaceItemStacks.length) {
				furnaceItemStacks[b0] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
			}
		}

		furnaceBurnTime = p_145839_1_.getShort("BurnTime");
		furnaceCookTime = p_145839_1_.getShort("CookTime");
		currentItemBurnTime = getItemBurnTime(furnaceItemStacks[1]);

		if (p_145839_1_.hasKey("CustomName", 8)) {
			field_145958_o = p_145839_1_.getString("CustomName");
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound p_145841_1_) {
		super.writeToNBT(p_145841_1_);
		p_145841_1_.setShort("BurnTime", (short) furnaceBurnTime);
		p_145841_1_.setShort("CookTime", (short) furnaceCookTime);
		NBTTagList nbttaglist = new NBTTagList();

		for (int i = 0; i < furnaceItemStacks.length; ++i) {
			if (furnaceItemStacks[i] != null) {
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setByte("Slot", (byte) i);
				furnaceItemStacks[i].writeToNBT(nbttagcompound1);
				nbttaglist.appendTag(nbttagcompound1);
			}
		}

		p_145841_1_.setTag("Items", nbttaglist);

		if (hasCustomInventoryName()) {
			p_145841_1_.setString("CustomName", field_145958_o);
		}
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@SideOnly(Side.CLIENT)
	public int getCookProgressScaled(int p_145953_1_) {
		return furnaceCookTime * p_145953_1_ / 200;
	}

	@SideOnly(Side.CLIENT)
	public int getBurnTimeRemainingScaled(int p_145955_1_) {
		if (currentItemBurnTime == 0) {
			currentItemBurnTime = 200;
		}

		return furnaceBurnTime * p_145955_1_ / currentItemBurnTime;
	}

	public boolean isBurning() {
		return furnaceBurnTime > 0;
	}

	@Override
	public void updateEntity() {
		boolean flag = furnaceBurnTime > 0;
		boolean flag1 = false;

		// CraftBukkit - moved from below
		if (isBurning() && canSmelt()) {
			++furnaceCookTime;

			if (furnaceCookTime == 200) {
				furnaceCookTime = 0;
				smeltItem();
				flag1 = true;
			}
		} else {
			furnaceCookTime = 0;
		}

		if (furnaceBurnTime > 0) {
			--furnaceBurnTime;
		}

		if (!worldObj.isRemote) {
			if (furnaceBurnTime != 0 || furnaceItemStacks[1] != null && furnaceItemStacks[0] != null) {
				// CraftBukkit start
				if (furnaceBurnTime <= 0 && canSmelt()) // CraftBukkit - == to <=
				{
					ItemStack itemstack = furnaceItemStacks[1];
					CraftItemStack fuel = CraftItemStack.asCraftMirror(itemstack);
					FurnaceBurnEvent furnaceBurnEvent = new FurnaceBurnEvent(
							worldObj.getWorld().getBlockAt(xCoord, yCoord, zCoord), fuel,
							TileEntityFurnace.getItemBurnTime(itemstack));
					worldObj.getServer().getPluginManager().callEvent(furnaceBurnEvent);

					if (furnaceBurnEvent.isCancelled())
						return;

					currentItemBurnTime = furnaceBurnEvent.getBurnTime();
					furnaceBurnTime += currentItemBurnTime;

					if (furnaceBurnTime > 0 && furnaceBurnEvent.isBurning())
					// CraftBukkit end
					{
						flag1 = true;

						if (furnaceItemStacks[1] != null) {
							--furnaceItemStacks[1].stackSize;

							if (furnaceItemStacks[1].stackSize == 0) {
								furnaceItemStacks[1] = furnaceItemStacks[1].getItem()
										.getContainerItem(furnaceItemStacks[1]);
							}
						}
					}
				}
			}

			if (flag != furnaceBurnTime > 0) {
				flag1 = true;
				BlockFurnace.updateFurnaceBlockState(furnaceBurnTime > 0, worldObj, xCoord, yCoord, zCoord);
			}
		}

		if (flag1) {
			markDirty();
		}
	}

	private boolean canSmelt() {
		if (furnaceItemStacks[0] == null)
			return false;
		else {
			ItemStack itemstack = FurnaceRecipes.smelting().getSmeltingResult(furnaceItemStacks[0]);
			if (itemstack == null)
				return false;
			if (furnaceItemStacks[2] == null)
				return true;
			if (!furnaceItemStacks[2].isItemEqual(itemstack))
				return false;
			int result = furnaceItemStacks[2].stackSize + itemstack.stackSize;
			return result <= getInventoryStackLimit() && result <= furnaceItemStacks[2].getMaxStackSize(); // Forge
																											// BugFix:
																											// Make
																											// it
																											// respect
																											// stack
																											// sizes
																											// properly.
		}
	}

	public void smeltItem() {
		if (canSmelt()) {
			ItemStack itemstack = FurnaceRecipes.smelting().getSmeltingResult(furnaceItemStacks[0]);

			// CraftBukkit start - fire FurnaceSmeltEvent
			CraftItemStack source = CraftItemStack.asCraftMirror(furnaceItemStacks[0]);
			org.bukkit.inventory.ItemStack result = CraftItemStack.asBukkitCopy(itemstack);

			FurnaceSmeltEvent furnaceSmeltEvent = new FurnaceSmeltEvent(
					worldObj.getWorld().getBlockAt(xCoord, yCoord, zCoord), source, result);
			worldObj.getServer().getPluginManager().callEvent(furnaceSmeltEvent);

			if (furnaceSmeltEvent.isCancelled())
				return;

			result = furnaceSmeltEvent.getResult();
			itemstack = CraftItemStack.asNMSCopy(result);

			if (itemstack != null) {
				if (furnaceItemStacks[2] == null) {
					furnaceItemStacks[2] = itemstack.copy();
				} else if (CraftItemStack.asCraftMirror(furnaceItemStacks[2]).isSimilar(result)) {
					furnaceItemStacks[2].stackSize += itemstack.stackSize; // Forge BugFix: Results may have
																			// multiple items
				}
			}
			// CraftBukkit end

			--furnaceItemStacks[0].stackSize;

			if (furnaceItemStacks[0].stackSize <= 0) {
				furnaceItemStacks[0] = null;
			}
		}
	}

	public static int getItemBurnTime(ItemStack p_145952_0_) {
		if (p_145952_0_ == null)
			return 0;
		else {
			int moddedBurnTime = net.minecraftforge.event.ForgeEventFactory.getFuelBurnTime(p_145952_0_);
			if (moddedBurnTime >= 0)
				return moddedBurnTime;

			Item item = p_145952_0_.getItem();

			if (item instanceof ItemBlock && Block.getBlockFromItem(item) != Blocks.air) {
				Block block = Block.getBlockFromItem(item);

				if (block == Blocks.wooden_slab)
					return 150;

				if (block.getMaterial() == Material.wood)
					return 300;

				if (block == Blocks.coal_block)
					return 16000;
			}

			if (item instanceof ItemTool && ((ItemTool) item).getToolMaterialName().equals("WOOD"))
				return 200;
			if (item instanceof ItemSword && ((ItemSword) item).getToolMaterialName().equals("WOOD"))
				return 200;
			if (item instanceof ItemHoe && ((ItemHoe) item).getToolMaterialName().equals("WOOD"))
				return 200;
			if (item == Items.stick)
				return 100;
			if (item == Items.coal)
				return 1600;
			if (item == Items.lava_bucket)
				return 20000;
			if (item == Item.getItemFromBlock(Blocks.sapling))
				return 100;
			if (item == Items.blaze_rod)
				return 2400;
			return GameRegistry.getFuelValue(p_145952_0_);
		}
	}

	public static boolean isItemFuel(ItemStack p_145954_0_) {
		return getItemBurnTime(p_145954_0_) > 0;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer p_70300_1_) {
		return worldObj.getTileEntity(xCoord, yCoord, zCoord) == this && p_70300_1_.getDistanceSq(xCoord + 0.5D, yCoord + 0.5D, zCoord + 0.5D) <= 64.0D;
	}

	@Override
	public void openInventory() {
	}

	@Override
	public void closeInventory() {
	}

	@Override
	public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_) {
		return p_94041_1_ != 2 && (p_94041_1_ != 1 || isItemFuel(p_94041_2_));
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int p_94128_1_) {
		return p_94128_1_ == 0 ? slotsBottom : p_94128_1_ == 1 ? slotsTop : slotsSides;
	}

	@Override
	public boolean canInsertItem(int p_102007_1_, ItemStack p_102007_2_, int p_102007_3_) {
		return isItemValidForSlot(p_102007_1_, p_102007_2_);
	}

	@Override
	public boolean canExtractItem(int p_102008_1_, ItemStack p_102008_2_, int p_102008_3_) {
		return p_102008_3_ != 0 || p_102008_1_ != 1 || p_102008_2_.getItem() == Items.bucket;
	}
}