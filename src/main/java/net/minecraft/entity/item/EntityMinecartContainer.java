package net.minecraft.entity.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public abstract class EntityMinecartContainer extends EntityMinecart implements IInventory {
	private ItemStack[] minecartContainerItems = new ItemStack[36];
	private boolean dropContentsWhenDead = true;
	private static final String __OBFID = "CL_00001674";

	public EntityMinecartContainer(World p_i1716_1_) {
		super(p_i1716_1_);
	}

	public EntityMinecartContainer(World p_i1717_1_, double p_i1717_2_, double p_i1717_4_, double p_i1717_6_) {
		super(p_i1717_1_, p_i1717_2_, p_i1717_4_, p_i1717_6_);
	}

	@Override
	public void killMinecart(DamageSource p_94095_1_) {
		super.killMinecart(p_94095_1_);

		for (int i = 0; i < getSizeInventory(); ++i) {
			ItemStack itemstack = getStackInSlot(i);

			if (itemstack != null) {
				float f = rand.nextFloat() * 0.8F + 0.1F;
				float f1 = rand.nextFloat() * 0.8F + 0.1F;
				float f2 = rand.nextFloat() * 0.8F + 0.1F;

				while (itemstack.stackSize > 0) {
					int j = rand.nextInt(21) + 10;

					if (j > itemstack.stackSize) {
						j = itemstack.stackSize;
					}

					itemstack.stackSize -= j;
					EntityItem entityitem = new EntityItem(worldObj, posX + f, posY + f1, posZ + f2,
							new ItemStack(itemstack.getItem(), j, itemstack.getItemDamage()));
					float f3 = 0.05F;
					entityitem.motionX = (float) rand.nextGaussian() * f3;
					entityitem.motionY = (float) rand.nextGaussian() * f3 + 0.2F;
					entityitem.motionZ = (float) rand.nextGaussian() * f3;
					worldObj.spawnEntityInWorld(entityitem);
				}
			}
		}
	}

	@Override
	public ItemStack getStackInSlot(int p_70301_1_) {
		return minecartContainerItems[p_70301_1_];
	}

	@Override
	public ItemStack decrStackSize(int p_70298_1_, int p_70298_2_) {
		if (minecartContainerItems[p_70298_1_] != null) {
			ItemStack itemstack;

			if (minecartContainerItems[p_70298_1_].stackSize <= p_70298_2_) {
				itemstack = minecartContainerItems[p_70298_1_];
				minecartContainerItems[p_70298_1_] = null;
				return itemstack;
			} else {
				itemstack = minecartContainerItems[p_70298_1_].splitStack(p_70298_2_);

				if (minecartContainerItems[p_70298_1_].stackSize == 0) {
					minecartContainerItems[p_70298_1_] = null;
				}

				return itemstack;
			}
		} else
			return null;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int p_70304_1_) {
		if (minecartContainerItems[p_70304_1_] != null) {
			ItemStack itemstack = minecartContainerItems[p_70304_1_];
			minecartContainerItems[p_70304_1_] = null;
			return itemstack;
		} else
			return null;
	}

	@Override
	public void setInventorySlotContents(int p_70299_1_, ItemStack p_70299_2_) {
		minecartContainerItems[p_70299_1_] = p_70299_2_;

		if (p_70299_2_ != null && p_70299_2_.stackSize > getInventoryStackLimit()) {
			p_70299_2_.stackSize = getInventoryStackLimit();
		}
	}

	@Override
	public void markDirty() {
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer p_70300_1_) {
		return isDead ? false : p_70300_1_.getDistanceSqToEntity(this) <= 64.0D;
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
	public String getInventoryName() {
		return hasCustomInventoryName() ? func_95999_t() : "container.minecart";
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public void travelToDimension(int p_71027_1_) {
		dropContentsWhenDead = false;
		super.travelToDimension(p_71027_1_);
	}

	@Override
	public void setDead() {
		if (dropContentsWhenDead) {
			for (int i = 0; i < getSizeInventory(); ++i) {
				ItemStack itemstack = getStackInSlot(i);

				if (itemstack != null) {
					float f = rand.nextFloat() * 0.8F + 0.1F;
					float f1 = rand.nextFloat() * 0.8F + 0.1F;
					float f2 = rand.nextFloat() * 0.8F + 0.1F;

					while (itemstack.stackSize > 0) {
						int j = rand.nextInt(21) + 10;

						if (j > itemstack.stackSize) {
							j = itemstack.stackSize;
						}

						itemstack.stackSize -= j;
						EntityItem entityitem = new EntityItem(worldObj, posX + f, posY + f1, posZ + f2,
								new ItemStack(itemstack.getItem(), j, itemstack.getItemDamage()));

						if (itemstack.hasTagCompound()) {
							entityitem.getEntityItem()
									.setTagCompound((NBTTagCompound) itemstack.getTagCompound().copy());
						}

						float f3 = 0.05F;
						entityitem.motionX = (float) rand.nextGaussian() * f3;
						entityitem.motionY = (float) rand.nextGaussian() * f3 + 0.2F;
						entityitem.motionZ = (float) rand.nextGaussian() * f3;
						worldObj.spawnEntityInWorld(entityitem);
					}
				}
			}
		}

		super.setDead();
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound p_70014_1_) {
		super.writeEntityToNBT(p_70014_1_);
		NBTTagList nbttaglist = new NBTTagList();

		for (int i = 0; i < minecartContainerItems.length; ++i) {
			if (minecartContainerItems[i] != null) {
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setByte("Slot", (byte) i);
				minecartContainerItems[i].writeToNBT(nbttagcompound1);
				nbttaglist.appendTag(nbttagcompound1);
			}
		}

		p_70014_1_.setTag("Items", nbttaglist);
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound p_70037_1_) {
		super.readEntityFromNBT(p_70037_1_);
		NBTTagList nbttaglist = p_70037_1_.getTagList("Items", 10);
		minecartContainerItems = new ItemStack[getSizeInventory()];

		for (int i = 0; i < nbttaglist.tagCount(); ++i) {
			NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
			int j = nbttagcompound1.getByte("Slot") & 255;

			if (j >= 0 && j < minecartContainerItems.length) {
				minecartContainerItems[j] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
			}
		}
	}

	@Override
	public boolean interactFirst(EntityPlayer p_130002_1_) {
		if (net.minecraftforge.common.MinecraftForge.EVENT_BUS
				.post(new net.minecraftforge.event.entity.minecart.MinecartInteractEvent(this, p_130002_1_)))
			return true;
		if (!worldObj.isRemote) {
			p_130002_1_.displayGUIChest(this);
		}

		return true;
	}

	@Override
	protected void applyDrag() {
		int i = 15 - Container.calcRedstoneFromInventory(this);
		float f = 0.98F + i * 0.001F;
		motionX *= f;
		motionY *= 0.0D;
		motionZ *= f;
	}
}