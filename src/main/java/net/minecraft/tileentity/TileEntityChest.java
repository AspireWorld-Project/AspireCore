package net.minecraft.tileentity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryLargeChest;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.AxisAlignedBB;

import java.util.Iterator;
import java.util.List;

public class TileEntityChest extends TileEntity implements IInventory {
	private ItemStack[] chestContents = new ItemStack[36];
	public boolean adjacentChestChecked;
	public TileEntityChest adjacentChestZNeg;
	public TileEntityChest adjacentChestXPos;
	public TileEntityChest adjacentChestXNeg;
	public TileEntityChest adjacentChestZPos;
	public float lidAngle;
	public float prevLidAngle;
	public int numPlayersUsing;
	private int ticksSinceSync;
	private int cachedChestType;
	private String customName;
	private static final String __OBFID = "CL_00000346";

	public TileEntityChest() {
		cachedChestType = -1;
	}

	@SideOnly(Side.CLIENT)
	public TileEntityChest(int p_i2350_1_) {
		cachedChestType = p_i2350_1_;
	}

	@Override
	public int getSizeInventory() {
		return 27;
	}

	@Override
	public ItemStack getStackInSlot(int p_70301_1_) {
		return chestContents[p_70301_1_];
	}

	@Override
	public ItemStack decrStackSize(int p_70298_1_, int p_70298_2_) {
		if (chestContents[p_70298_1_] != null) {
			ItemStack itemstack;

			if (chestContents[p_70298_1_].stackSize <= p_70298_2_) {
				itemstack = chestContents[p_70298_1_];
				chestContents[p_70298_1_] = null;
				markDirty();
				return itemstack;
			} else {
				itemstack = chestContents[p_70298_1_].splitStack(p_70298_2_);

				if (chestContents[p_70298_1_].stackSize == 0) {
					chestContents[p_70298_1_] = null;
				}

				markDirty();
				return itemstack;
			}
		} else
			return null;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int p_70304_1_) {
		if (chestContents[p_70304_1_] != null) {
			ItemStack itemstack = chestContents[p_70304_1_];
			chestContents[p_70304_1_] = null;
			return itemstack;
		} else
			return null;
	}

	@Override
	public void setInventorySlotContents(int p_70299_1_, ItemStack p_70299_2_) {
		chestContents[p_70299_1_] = p_70299_2_;

		if (p_70299_2_ != null && p_70299_2_.stackSize > getInventoryStackLimit()) {
			p_70299_2_.stackSize = getInventoryStackLimit();
		}

		markDirty();
	}

	@Override
	public String getInventoryName() {
		return hasCustomInventoryName() ? customName : "container.chest";
	}

	@Override
	public boolean hasCustomInventoryName() {
		return customName != null && customName.length() > 0;
	}

	public void func_145976_a(String p_145976_1_) {
		customName = p_145976_1_;
	}

	@Override
	public void readFromNBT(NBTTagCompound p_145839_1_) {
		super.readFromNBT(p_145839_1_);
		NBTTagList nbttaglist = p_145839_1_.getTagList("Items", 10);
		chestContents = new ItemStack[getSizeInventory()];

		if (p_145839_1_.hasKey("CustomName", 8)) {
			customName = p_145839_1_.getString("CustomName");
		}

		for (int i = 0; i < nbttaglist.tagCount(); ++i) {
			NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
			int j = nbttagcompound1.getByte("Slot") & 255;

			if (j >= 0 && j < chestContents.length) {
				chestContents[j] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
			}
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound p_145841_1_) {
		super.writeToNBT(p_145841_1_);
		NBTTagList nbttaglist = new NBTTagList();

		for (int i = 0; i < chestContents.length; ++i) {
			if (chestContents[i] != null) {
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setByte("Slot", (byte) i);
				chestContents[i].writeToNBT(nbttagcompound1);
				nbttaglist.appendTag(nbttagcompound1);
			}
		}

		p_145841_1_.setTag("Items", nbttaglist);

		if (hasCustomInventoryName()) {
			p_145841_1_.setString("CustomName", customName);
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
	public void updateContainingBlockInfo() {
		super.updateContainingBlockInfo();
		adjacentChestChecked = false;
	}

	private void func_145978_a(TileEntityChest p_145978_1_, int p_145978_2_) {
		if (p_145978_1_.isInvalid()) {
			adjacentChestChecked = false;
		} else if (adjacentChestChecked) {
			switch (p_145978_2_) {
			case 0:
				if (adjacentChestZPos != p_145978_1_) {
					adjacentChestChecked = false;
				}

				break;
			case 1:
				if (adjacentChestXNeg != p_145978_1_) {
					adjacentChestChecked = false;
				}

				break;
			case 2:
				if (adjacentChestZNeg != p_145978_1_) {
					adjacentChestChecked = false;
				}

				break;
			case 3:
				if (adjacentChestXPos != p_145978_1_) {
					adjacentChestChecked = false;
				}
			}
		}
	}

	public void checkForAdjacentChests() {
		if (!adjacentChestChecked) {
			adjacentChestChecked = true;
			adjacentChestZNeg = null;
			adjacentChestXPos = null;
			adjacentChestXNeg = null;
			adjacentChestZPos = null;

			if (func_145977_a(xCoord - 1, yCoord, zCoord)) {
				adjacentChestXNeg = (TileEntityChest) worldObj.getTileEntity(xCoord - 1, yCoord, zCoord);
			}

			if (func_145977_a(xCoord + 1, yCoord, zCoord)) {
				adjacentChestXPos = (TileEntityChest) worldObj.getTileEntity(xCoord + 1, yCoord, zCoord);
			}

			if (func_145977_a(xCoord, yCoord, zCoord - 1)) {
				adjacentChestZNeg = (TileEntityChest) worldObj.getTileEntity(xCoord, yCoord, zCoord - 1);
			}

			if (func_145977_a(xCoord, yCoord, zCoord + 1)) {
				adjacentChestZPos = (TileEntityChest) worldObj.getTileEntity(xCoord, yCoord, zCoord + 1);
			}

			if (adjacentChestZNeg != null) {
				adjacentChestZNeg.func_145978_a(this, 0);
			}

			if (adjacentChestZPos != null) {
				adjacentChestZPos.func_145978_a(this, 2);
			}

			if (adjacentChestXPos != null) {
				adjacentChestXPos.func_145978_a(this, 1);
			}

			if (adjacentChestXNeg != null) {
				adjacentChestXNeg.func_145978_a(this, 3);
			}
		}
	}

	private boolean func_145977_a(int p_145977_1_, int p_145977_2_, int p_145977_3_) {
		if (worldObj == null)
			return false;
		else {
			Block block = worldObj.getBlock(p_145977_1_, p_145977_2_, p_145977_3_);
			return block instanceof BlockChest && ((BlockChest) block).field_149956_a == func_145980_j();
		}
	}

	@Override
	public void updateEntity() {
		super.updateEntity();
		checkForAdjacentChests();
		++ticksSinceSync;
		float f;

		if (!worldObj.isRemote && numPlayersUsing != 0 && (ticksSinceSync + xCoord + yCoord + zCoord) % 200 == 0) {
			numPlayersUsing = 0;
			f = 5.0F;
			List list = worldObj.getEntitiesWithinAABB(EntityPlayer.class, AxisAlignedBB.getBoundingBox(xCoord - f,
					yCoord - f, zCoord - f, xCoord + 1 + f, yCoord + 1 + f, zCoord + 1 + f));
			Iterator iterator = list.iterator();

			while (iterator.hasNext()) {
				EntityPlayer entityplayer = (EntityPlayer) iterator.next();

				if (entityplayer.openContainer instanceof ContainerChest) {
					IInventory iinventory = ((ContainerChest) entityplayer.openContainer).getLowerChestInventory();

					if (iinventory == this || iinventory instanceof InventoryLargeChest
							&& ((InventoryLargeChest) iinventory).isPartOfLargeChest(this)) {
						++numPlayersUsing;
					}
				}
			}
		}

		prevLidAngle = lidAngle;
		f = 0.1F;
		double d2;

		if (numPlayersUsing > 0 && lidAngle == 0.0F && adjacentChestZNeg == null && adjacentChestXNeg == null) {
			double d1 = xCoord + 0.5D;
			d2 = zCoord + 0.5D;

			if (adjacentChestZPos != null) {
				d2 += 0.5D;
			}

			if (adjacentChestXPos != null) {
				d1 += 0.5D;
			}

			worldObj.playSoundEffect(d1, yCoord + 0.5D, d2, "random.chestopen", 0.5F,
					worldObj.rand.nextFloat() * 0.1F + 0.9F);
		}

		if (numPlayersUsing == 0 && lidAngle > 0.0F || numPlayersUsing > 0 && lidAngle < 1.0F) {
			float f1 = lidAngle;

			if (numPlayersUsing > 0) {
				lidAngle += f;
			} else {
				lidAngle -= f;
			}

			if (lidAngle > 1.0F) {
				lidAngle = 1.0F;
			}

			float f2 = 0.5F;

			if (lidAngle < f2 && f1 >= f2 && adjacentChestZNeg == null && adjacentChestXNeg == null) {
				d2 = xCoord + 0.5D;
				double d0 = zCoord + 0.5D;

				if (adjacentChestZPos != null) {
					d0 += 0.5D;
				}

				if (adjacentChestXPos != null) {
					d2 += 0.5D;
				}

				worldObj.playSoundEffect(d2, yCoord + 0.5D, d0, "random.chestclosed", 0.5F,
						worldObj.rand.nextFloat() * 0.1F + 0.9F);
			}

			if (lidAngle < 0.0F) {
				lidAngle = 0.0F;
			}
		}
	}

	@Override
	public boolean receiveClientEvent(int p_145842_1_, int p_145842_2_) {
		if (p_145842_1_ == 1) {
			numPlayersUsing = p_145842_2_;
			return true;
		} else
			return super.receiveClientEvent(p_145842_1_, p_145842_2_);
	}

	@Override
	public void openInventory() {
		if (numPlayersUsing < 0) {
			numPlayersUsing = 0;
		}

		++numPlayersUsing;
		worldObj.addBlockEvent(xCoord, yCoord, zCoord, getBlockType(), 1, numPlayersUsing);
		worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, getBlockType());
		worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord - 1, zCoord, getBlockType());
	}

	@Override
	public void closeInventory() {
		if (getBlockType() instanceof BlockChest) {
			--numPlayersUsing;
			worldObj.addBlockEvent(xCoord, yCoord, zCoord, getBlockType(), 1, numPlayersUsing);
			worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, getBlockType());
			worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord - 1, zCoord, getBlockType());
		}
	}

	@Override
	public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_) {
		return true;
	}

	@Override
	public void invalidate() {
		super.invalidate();
		updateContainingBlockInfo();
		checkForAdjacentChests();
	}

	public int func_145980_j() {
		if (cachedChestType == -1) {
			if (worldObj == null || !(getBlockType() instanceof BlockChest))
				return 0;

			cachedChestType = ((BlockChest) getBlockType()).field_149956_a;
		}

		return cachedChestType;
	}
}