package net.minecraft.tileentity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockHopper;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.InventoryLargeChest;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Facing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.ultramine.bukkit.util.BukkitUtil;

import java.util.List;

public class TileEntityHopper extends TileEntity implements IHopper {
	private ItemStack[] field_145900_a = new ItemStack[5];
	private String field_145902_i;
	private int field_145901_j = -1;
	private static final String __OBFID = "CL_00000359";

	@Override
	public void readFromNBT(NBTTagCompound p_145839_1_) {
		super.readFromNBT(p_145839_1_);
		NBTTagList nbttaglist = p_145839_1_.getTagList("Items", 10);
		field_145900_a = new ItemStack[getSizeInventory()];

		if (p_145839_1_.hasKey("CustomName", 8)) {
			field_145902_i = p_145839_1_.getString("CustomName");
		}

		field_145901_j = p_145839_1_.getInteger("TransferCooldown");

		for (int i = 0; i < nbttaglist.tagCount(); ++i) {
			NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
			byte b0 = nbttagcompound1.getByte("Slot");

			if (b0 >= 0 && b0 < field_145900_a.length) {
				field_145900_a[b0] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
			}
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound p_145841_1_) {
		super.writeToNBT(p_145841_1_);
		NBTTagList nbttaglist = new NBTTagList();

		for (int i = 0; i < field_145900_a.length; ++i) {
			if (field_145900_a[i] != null) {
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setByte("Slot", (byte) i);
				field_145900_a[i].writeToNBT(nbttagcompound1);
				nbttaglist.appendTag(nbttagcompound1);
			}
		}

		p_145841_1_.setTag("Items", nbttaglist);
		p_145841_1_.setInteger("TransferCooldown", field_145901_j);

		if (hasCustomInventoryName()) {
			p_145841_1_.setString("CustomName", field_145902_i);
		}
	}

	@Override
	public void markDirty() {
		super.markDirty();
	}

	@Override
	public int getSizeInventory() {
		return field_145900_a.length;
	}

	@Override
	public ItemStack getStackInSlot(int p_70301_1_) {
		return field_145900_a[p_70301_1_];
	}

	@Override
	public ItemStack decrStackSize(int p_70298_1_, int p_70298_2_) {
		if (field_145900_a[p_70298_1_] != null) {
			ItemStack itemstack;

			if (field_145900_a[p_70298_1_].stackSize <= p_70298_2_) {
				itemstack = field_145900_a[p_70298_1_];
				field_145900_a[p_70298_1_] = null;
				return itemstack;
			} else {
				itemstack = field_145900_a[p_70298_1_].splitStack(p_70298_2_);

				if (field_145900_a[p_70298_1_].stackSize == 0) {
					field_145900_a[p_70298_1_] = null;
				}

				return itemstack;
			}
		} else
			return null;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int p_70304_1_) {
		if (field_145900_a[p_70304_1_] != null) {
			ItemStack itemstack = field_145900_a[p_70304_1_];
			field_145900_a[p_70304_1_] = null;
			return itemstack;
		} else
			return null;
	}

	@Override
	public void setInventorySlotContents(int p_70299_1_, ItemStack p_70299_2_) {
		field_145900_a[p_70299_1_] = p_70299_2_;

		if (p_70299_2_ != null && p_70299_2_.stackSize > getInventoryStackLimit()) {
			p_70299_2_.stackSize = getInventoryStackLimit();
		}
	}

	@Override
	public String getInventoryName() {
		return hasCustomInventoryName() ? field_145902_i : "container.hopper";
	}

	@Override
	public boolean hasCustomInventoryName() {
		return field_145902_i != null && field_145902_i.length() > 0;
	}

	public void func_145886_a(String p_145886_1_) {
		field_145902_i = p_145886_1_;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
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
		return true;
	}

	@Override
	public void updateEntity() {
		if (worldObj != null && !worldObj.isRemote) {
			--field_145901_j;

			if (!func_145888_j()) {
				func_145896_c(0);
				if (!func_145887_i()) {
					func_145896_c(16); // UM 0->16 if no work has been done, we should set DOUBLE cooldown, not
										// zero
				}
			}
		}
	}

	public boolean func_145887_i() {
		if (worldObj != null && !worldObj.isRemote) {
			if (!func_145888_j() && BlockHopper.func_149917_c(getBlockMetadata())) {
				boolean flag = false;

				if (!func_152104_k()) {
					flag = func_145883_k();
				}

				if (!func_152105_l()) {
					flag = func_145891_a(this) || flag;
				}

				if (flag) {
					func_145896_c(8);
					markDirty();
					return true;
				}
			}

			return false;
		} else
			return false;
	}

	private boolean func_152104_k() {
		ItemStack[] aitemstack = field_145900_a;
		int i = aitemstack.length;

		for (int j = 0; j < i; ++j) {
			ItemStack itemstack = aitemstack[j];

			if (itemstack != null)
				return false;
		}

		return true;
	}

	private boolean func_152105_l() {
		ItemStack[] aitemstack = field_145900_a;
		int i = aitemstack.length;

		for (int j = 0; j < i; ++j) {
			ItemStack itemstack = aitemstack[j];

			if (itemstack == null || itemstack.stackSize != itemstack.getMaxStackSize())
				return false;
		}

		return true;
	}

	private boolean func_145883_k() {
		IInventory iinventory = func_145895_l();
		TileEntityHopper thisObject = this;
		if (iinventory == null)
			return false;
		else {
			int i = Facing.oppositeSide[BlockHopper.getDirectionFromMetadata(thisObject.getBlockMetadata())];
			if (func_152102_a(iinventory, i))
				return false;
			else {
				for (int j = 0; j < thisObject.getSizeInventory(); ++j)
					if (thisObject.getStackInSlot(j) != null) {
						ItemStack itemstack = thisObject.getStackInSlot(j).copy();
						CraftItemStack oitemstack = CraftItemStack.asCraftMirror(thisObject.decrStackSize(j, 1));
						Inventory destinationInventory;

						// Have to special case large chests as they work oddly
						if (iinventory instanceof InventoryLargeChest) {
							destinationInventory = new org.bukkit.craftbukkit.inventory.CraftInventoryDoubleChest(
									(InventoryLargeChest) iinventory);
						} else {
							InventoryHolder owner = BukkitUtil.getInventoryOwner(iinventory);
							destinationInventory = owner != null ? owner.getInventory() : null;
						}
						InventoryMoveItemEvent event = new InventoryMoveItemEvent(
								BukkitUtil.getInventoryOwner(thisObject).getInventory(), oitemstack.clone(),
								destinationInventory, true);
						Bukkit.getPluginManager().callEvent(event);
						if (event.isCancelled()) {
							thisObject.setInventorySlotContents(j, itemstack);
							return false;
						}
						ItemStack itemstack1 = func_145889_a(iinventory, CraftItemStack.asNMSCopy(event.getItem()), i);
						if (itemstack1 == null || itemstack1.stackSize == 0) {
							iinventory.markDirty();
							return true;
						}
						thisObject.setInventorySlotContents(j, itemstack);
					}
				return false;
			}
		}
	}

	private boolean func_152102_a(IInventory p_152102_1_, int p_152102_2_) {
		if (p_152102_1_ instanceof ISidedInventory && p_152102_2_ > -1) {
			ISidedInventory isidedinventory = (ISidedInventory) p_152102_1_;
			int[] aint = isidedinventory.getAccessibleSlotsFromSide(p_152102_2_);

			for (int l = 0; l < aint.length; ++l) {
				ItemStack itemstack1 = isidedinventory.getStackInSlot(aint[l]);

				if (itemstack1 == null || itemstack1.stackSize != itemstack1.getMaxStackSize())
					return false;
			}
		} else {
			int j = p_152102_1_.getSizeInventory();

			for (int k = 0; k < j; ++k) {
				ItemStack itemstack = p_152102_1_.getStackInSlot(k);

				if (itemstack == null || itemstack.stackSize != itemstack.getMaxStackSize())
					return false;
			}
		}

		return true;
	}

	private static boolean func_152103_b(IInventory p_152103_0_, int p_152103_1_) {
		if (p_152103_0_ instanceof ISidedInventory && p_152103_1_ > -1) {
			ISidedInventory isidedinventory = (ISidedInventory) p_152103_0_;
			int[] aint = isidedinventory.getAccessibleSlotsFromSide(p_152103_1_);

			for (int l = 0; l < aint.length; ++l) {
				if (isidedinventory.getStackInSlot(aint[l]) != null)
					return false;
			}
		} else {
			int j = p_152103_0_.getSizeInventory();

			for (int k = 0; k < j; ++k) {
				if (p_152103_0_.getStackInSlot(k) != null)
					return false;
			}
		}

		return true;
	}

	public static boolean func_145891_a(IHopper p_145891_0_) {
		IInventory iinventory = func_145884_b(p_145891_0_);

		if (iinventory != null) {
			byte b0 = 0;

			if (func_152103_b(iinventory, b0))
				return false;

			if (iinventory instanceof ISidedInventory && b0 > -1) {
				ISidedInventory isidedinventory = (ISidedInventory) iinventory;
				int[] aint = isidedinventory.getAccessibleSlotsFromSide(b0);

				for (int k = 0; k < aint.length; ++k) {
					if (func_145892_a(p_145891_0_, iinventory, aint[k], b0))
						return true;
				}
			} else {
				int i = iinventory.getSizeInventory();

				for (int j = 0; j < i; ++j) {
					if (func_145892_a(p_145891_0_, iinventory, j, b0))
						return true;
				}
			}
		} else {
			EntityItem entityitem = func_145897_a(p_145891_0_.getWorldObj(), p_145891_0_.getXPos(),
					p_145891_0_.getYPos() + 1.0D, p_145891_0_.getZPos());

			if (entityitem != null)
				return func_145898_a(p_145891_0_, entityitem);
		}

		return false;
	}

	private static boolean func_145892_a(IHopper hopper, IInventory inventory, int slotId, int p_145892_3_) {
		ItemStack itemstack = inventory.getStackInSlot(slotId);
		if (itemstack != null && func_145890_b(inventory, itemstack, slotId, p_145892_3_)) {
			ItemStack itemstack1 = itemstack.copy();
			CraftItemStack oitemstack = CraftItemStack.asCraftMirror(inventory.decrStackSize(slotId, 1));
			Inventory sourceInventory = null;

			// Have to special case large chests as they work oddly
			if (inventory instanceof InventoryLargeChest) {
				sourceInventory = new org.bukkit.craftbukkit.inventory.CraftInventoryDoubleChest(
						(InventoryLargeChest) inventory);
			} else {
				InventoryHolder owner = BukkitUtil.getInventoryOwner(inventory);
				sourceInventory = owner != null ? owner.getInventory() : null;
			}

			InventoryMoveItemEvent event = new InventoryMoveItemEvent(sourceInventory, oitemstack.clone(),
					BukkitUtil.getInventoryOwner(hopper).getInventory(), false);
			Bukkit.getPluginManager().callEvent(event);
			if (event.isCancelled()) {
				inventory.setInventorySlotContents(slotId, itemstack1);
				return false;
			}
			ItemStack itemstack2 = func_145889_a(hopper, CraftItemStack.asNMSCopy(event.getItem()), -1);
			if (itemstack2 == null || itemstack2.stackSize == 0) {
				inventory.markDirty();
				return true;
			}
			inventory.setInventorySlotContents(slotId, itemstack1);
		}
		return false;
	}

	public static boolean func_145898_a(IInventory p_145898_0_, EntityItem p_145898_1_) {
		boolean flag = false;

		if (p_145898_1_ == null)
			return false;
		else {
			if (BukkitUtil.getInventoryOwner(p_145898_0_) != null && p_145898_1_.getBukkitEntity() != null) {
				InventoryPickupItemEvent event = new InventoryPickupItemEvent(
						BukkitUtil.getInventoryOwner(p_145898_0_).getInventory(),
						(org.bukkit.entity.Item) p_145898_1_.getBukkitEntity());
				p_145898_1_.worldObj.getServer().getPluginManager().callEvent(event);
				if (event.isCancelled())
					return false;
			}
			ItemStack itemstack = p_145898_1_.getEntityItem().copy();
			ItemStack itemstack1 = func_145889_a(p_145898_0_, itemstack, -1);
			if (itemstack1 != null && itemstack1.stackSize != 0) {
				p_145898_1_.setEntityItemStack(itemstack1);
			} else {
				flag = true;
				p_145898_1_.setDead();
			}

			return flag;
		}
	}

	public static ItemStack func_145889_a(IInventory p_145889_0_, ItemStack p_145889_1_, int p_145889_2_) {
		if (p_145889_0_ instanceof ISidedInventory && p_145889_2_ > -1) {
			ISidedInventory isidedinventory = (ISidedInventory) p_145889_0_;
			int[] aint = isidedinventory.getAccessibleSlotsFromSide(p_145889_2_);

			for (int l = 0; l < aint.length && p_145889_1_ != null && p_145889_1_.stackSize > 0; ++l) {
				p_145889_1_ = func_145899_c(p_145889_0_, p_145889_1_, aint[l], p_145889_2_);
			}
		} else {
			int j = p_145889_0_.getSizeInventory();

			for (int k = 0; k < j && p_145889_1_ != null && p_145889_1_.stackSize > 0; ++k) {
				p_145889_1_ = func_145899_c(p_145889_0_, p_145889_1_, k, p_145889_2_);
			}
		}

		if (p_145889_1_ != null && p_145889_1_.stackSize == 0) {
			p_145889_1_ = null;
		}

		return p_145889_1_;
	}

	private static boolean func_145885_a(IInventory p_145885_0_, ItemStack p_145885_1_, int p_145885_2_,
			int p_145885_3_) {
		return p_145885_0_.isItemValidForSlot(p_145885_2_, p_145885_1_) && (!(p_145885_0_ instanceof ISidedInventory)
				|| ((ISidedInventory) p_145885_0_).canInsertItem(p_145885_2_, p_145885_1_, p_145885_3_));
	}

	private static boolean func_145890_b(IInventory p_145890_0_, ItemStack p_145890_1_, int p_145890_2_,
			int p_145890_3_) {
		return !(p_145890_0_ instanceof ISidedInventory)
				|| ((ISidedInventory) p_145890_0_).canExtractItem(p_145890_2_, p_145890_1_, p_145890_3_);
	}

	private static ItemStack func_145899_c(IInventory p_145899_0_, ItemStack p_145899_1_, int p_145899_2_,
			int p_145899_3_) {
		ItemStack itemstack1 = p_145899_0_.getStackInSlot(p_145899_2_);

		boolean canMerge = itemstack1 == null || func_145894_a(itemstack1, p_145899_1_); // optimized validity check
		if (canMerge && func_145885_a(p_145899_0_, p_145899_1_, p_145899_2_, p_145899_3_)) {
			boolean flag = false;

			if (itemstack1 == null) {
				// Forge: BUGFIX: Again, make things respect max stack sizes.
				int max = Math.min(p_145899_1_.getMaxStackSize(), p_145899_0_.getInventoryStackLimit());
				if (max >= p_145899_1_.stackSize) {
					p_145899_0_.setInventorySlotContents(p_145899_2_, p_145899_1_);
					p_145899_1_ = null;
				} else {
					p_145899_0_.setInventorySlotContents(p_145899_2_, p_145899_1_.splitStack(max));
				}
				flag = true;
			} else if (canMerge) {
				// Forge: BUGFIX: Again, make things respect max stack sizes.
				int max = Math.min(p_145899_1_.getMaxStackSize(), p_145899_0_.getInventoryStackLimit());
				if (max > itemstack1.stackSize) {
					int l = Math.min(p_145899_1_.stackSize, max - itemstack1.stackSize);
					p_145899_1_.stackSize -= l;
					itemstack1.stackSize += l;
					flag = l > 0;
				}
			}

			if (flag) {
				if (p_145899_0_ instanceof TileEntityHopper) {
					((TileEntityHopper) p_145899_0_).func_145896_c(8);
					p_145899_0_.markDirty();
				}

				p_145899_0_.markDirty();
			}
		}

		return p_145899_1_;
	}

	private IInventory func_145895_l() {
		int i = BlockHopper.getDirectionFromMetadata(getBlockMetadata());
		return func_145893_b(getWorldObj(), xCoord + Facing.offsetsXForSide[i], yCoord + Facing.offsetsYForSide[i],
				zCoord + Facing.offsetsZForSide[i]);
	}

	public static IInventory func_145884_b(IHopper p_145884_0_) {
		return func_145893_b(p_145884_0_.getWorldObj(), p_145884_0_.getXPos(), p_145884_0_.getYPos() + 1.0D,
				p_145884_0_.getZPos());
	}

	public static EntityItem func_145897_a(World p_145897_0_, double p_145897_1_, double p_145897_3_,
			double p_145897_5_) {
		List list = p_145897_0_
				.selectEntitiesWithinAABB(
						EntityItem.class, AxisAlignedBB.getBoundingBox(p_145897_1_, p_145897_3_, p_145897_5_,
								p_145897_1_ + 1.0D, p_145897_3_ + 1.0D, p_145897_5_ + 1.0D),
						IEntitySelector.selectAnything);
		return list.size() > 0 ? (EntityItem) list.get(0) : null;
	}

	public static IInventory func_145893_b(World p_145893_0_, double p_145893_1_, double p_145893_3_,
			double p_145893_5_) {
		IInventory iinventory = null;
		int i = MathHelper.floor_double(p_145893_1_);
		int j = MathHelper.floor_double(p_145893_3_);
		int k = MathHelper.floor_double(p_145893_5_);
		TileEntity tileentity = p_145893_0_.getTileEntity(i, j, k);

		if (tileentity != null && tileentity instanceof IInventory) {
			iinventory = (IInventory) tileentity;

			if (iinventory instanceof TileEntityChest) {
				Block block = p_145893_0_.getBlock(i, j, k);

				if (block instanceof BlockChest) {
					iinventory = ((BlockChest) block).func_149951_m(p_145893_0_, i, j, k);
				}
			}
		}

		if (iinventory == null) {
			List list = p_145893_0_.getEntitiesWithinAABBExcludingEntity(
					null, AxisAlignedBB.getBoundingBox(p_145893_1_, p_145893_3_, p_145893_5_,
							p_145893_1_ + 1.0D, p_145893_3_ + 1.0D, p_145893_5_ + 1.0D),
					IEntitySelector.selectInventories);

			if (list != null && list.size() > 0) {
				iinventory = (IInventory) list.get(p_145893_0_.rand.nextInt(list.size()));
			}
		}

		return iinventory;
	}

	private static boolean func_145894_a(ItemStack p_145894_0_, ItemStack p_145894_1_) {
		return p_145894_0_.getItem() == p_145894_1_.getItem() && p_145894_0_.getItemDamage() == p_145894_1_.getItemDamage() && p_145894_0_.stackSize <= p_145894_0_.getMaxStackSize() && ItemStack.areItemStackTagsEqual(p_145894_0_, p_145894_1_);
	}

	@Override
	public double getXPos() {
		return xCoord;
	}

	@Override
	public double getYPos() {
		return yCoord;
	}

	@Override
	public double getZPos() {
		return zCoord;
	}

	public void func_145896_c(int p_145896_1_) {
		field_145901_j = p_145896_1_;
	}

	public boolean func_145888_j() {
		return field_145901_j > 0;
	}
}