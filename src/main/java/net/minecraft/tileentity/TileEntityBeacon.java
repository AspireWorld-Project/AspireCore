package net.minecraft.tileentity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.stats.AchievementList;
import net.minecraft.util.AxisAlignedBB;

import java.util.Iterator;
import java.util.List;

public class TileEntityBeacon extends TileEntity implements IInventory {
	public static final Potion[][] effectsList = new Potion[][] { { Potion.moveSpeed, Potion.digSpeed },
			{ Potion.resistance, Potion.jump }, { Potion.damageBoost }, { Potion.regeneration } };
	@SideOnly(Side.CLIENT)
	private long field_146016_i;
	@SideOnly(Side.CLIENT)
	private float field_146014_j;
	private boolean field_146015_k;
	private int levels = -1;
	private int primaryEffect;
	private int secondaryEffect;
	private ItemStack payment;
	private String field_146008_p;
	private static final String __OBFID = "CL_00000339";

	@Override
	public void updateEntity() {
		if (worldObj.getTotalWorldTime() % 80L == 0L) {
			func_146003_y();
			func_146000_x();
		}
	}

	private void func_146000_x() {
		if (field_146015_k && levels > 0 && !worldObj.isRemote && primaryEffect > 0) {
			double d0 = levels * 10 + 10;
			byte b0 = 0;

			if (levels >= 4 && primaryEffect == secondaryEffect) {
				b0 = 1;
			}

			AxisAlignedBB axisalignedbb = AxisAlignedBB
					.getBoundingBox(xCoord, yCoord, zCoord, xCoord + 1, yCoord + 1, zCoord + 1).expand(d0, d0, d0);
			axisalignedbb.maxY = worldObj.getHeight();
			List list = worldObj.getEntitiesWithinAABB(EntityPlayer.class, axisalignedbb);
			Iterator iterator = list.iterator();
			EntityPlayer entityplayer;

			while (iterator.hasNext()) {
				entityplayer = (EntityPlayer) iterator.next();
				entityplayer.addPotionEffect(new PotionEffect(primaryEffect, 180, b0, true));
			}

			if (levels >= 4 && primaryEffect != secondaryEffect && secondaryEffect > 0) {
				iterator = list.iterator();

				while (iterator.hasNext()) {
					entityplayer = (EntityPlayer) iterator.next();
					entityplayer.addPotionEffect(new PotionEffect(secondaryEffect, 180, 0, true));
				}
			}
		}
	}

	private void func_146003_y() {
		int i = levels;

		if (!worldObj.canBlockSeeTheSky(xCoord, yCoord + 1, zCoord)) {
			field_146015_k = false;
			levels = 0;
		} else {
			field_146015_k = true;
			levels = 0;

			for (int j = 1; j <= 4; levels = j++) {
				int k = yCoord - j;

				if (k < 0) {
					break;
				}

				boolean flag = true;

				for (int l = xCoord - j; l <= xCoord + j && flag; ++l) {
					for (int i1 = zCoord - j; i1 <= zCoord + j; ++i1) {
						Block block = worldObj.getBlock(l, k, i1);

						if (!block.isBeaconBase(worldObj, l, k, i1, xCoord, yCoord, zCoord)) {
							flag = false;
							break;
						}
					}
				}

				if (!flag) {
					break;
				}
			}

			if (levels == 0) {
				field_146015_k = false;
			}
		}

		if (!worldObj.isRemote && levels == 4 && i < levels) {
			Iterator iterator = worldObj.getEntitiesWithinAABB(EntityPlayer.class, AxisAlignedBB
					.getBoundingBox(xCoord, yCoord, zCoord, xCoord, yCoord - 4, zCoord).expand(10.0D, 5.0D, 10.0D))
					.iterator();

			while (iterator.hasNext()) {
				EntityPlayer entityplayer = (EntityPlayer) iterator.next();
				entityplayer.triggerAchievement(AchievementList.field_150965_K);
			}
		}
	}

	@SideOnly(Side.CLIENT)
	public float func_146002_i() {
		if (!field_146015_k)
			return 0.0F;
		else {
			int i = (int) (worldObj.getTotalWorldTime() - field_146016_i);
			field_146016_i = worldObj.getTotalWorldTime();

			if (i > 1) {
				field_146014_j -= i / 40.0F;

				if (field_146014_j < 0.0F) {
					field_146014_j = 0.0F;
				}
			}

			field_146014_j += 0.025F;

			if (field_146014_j > 1.0F) {
				field_146014_j = 1.0F;
			}

			return field_146014_j;
		}
	}

	public int getPrimaryEffect() {
		return primaryEffect;
	}

	public int getSecondaryEffect() {
		return secondaryEffect;
	}

	public int getLevels() {
		return levels;
	}

	@SideOnly(Side.CLIENT)
	public void func_146005_c(int p_146005_1_) {
		levels = p_146005_1_;
	}

	public void setPrimaryEffect(int p_146001_1_) {
		primaryEffect = 0;

		for (int j = 0; j < levels && j < 3; ++j) {
			Potion[] apotion = effectsList[j];
			int k = apotion.length;

			for (int l = 0; l < k; ++l) {
				Potion potion = apotion[l];

				if (potion.id == p_146001_1_) {
					primaryEffect = p_146001_1_;
					return;
				}
			}
		}
	}

	public void setSecondaryEffect(int p_146004_1_) {
		secondaryEffect = 0;

		if (levels >= 4) {
			for (int j = 0; j < 4; ++j) {
				Potion[] apotion = effectsList[j];
				int k = apotion.length;

				for (int l = 0; l < k; ++l) {
					Potion potion = apotion[l];

					if (potion.id == p_146004_1_) {
						secondaryEffect = p_146004_1_;
						return;
					}
				}
			}
		}
	}

	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound nbttagcompound = new NBTTagCompound();
		writeToNBT(nbttagcompound);
		return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 3, nbttagcompound);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public double getMaxRenderDistanceSquared() {
		return 65536.0D;
	}

	@Override
	public void readFromNBT(NBTTagCompound p_145839_1_) {
		super.readFromNBT(p_145839_1_);
		primaryEffect = p_145839_1_.getInteger("Primary");
		secondaryEffect = p_145839_1_.getInteger("Secondary");
		levels = p_145839_1_.getInteger("Levels");
	}

	@Override
	public void writeToNBT(NBTTagCompound p_145841_1_) {
		super.writeToNBT(p_145841_1_);
		p_145841_1_.setInteger("Primary", primaryEffect);
		p_145841_1_.setInteger("Secondary", secondaryEffect);
		p_145841_1_.setInteger("Levels", levels);
	}

	@Override
	public int getSizeInventory() {
		return 1;
	}

	@Override
	public ItemStack getStackInSlot(int p_70301_1_) {
		return p_70301_1_ == 0 ? payment : null;
	}

	@Override
	public ItemStack decrStackSize(int p_70298_1_, int p_70298_2_) {
		if (p_70298_1_ == 0 && payment != null) {
			if (p_70298_2_ >= payment.stackSize) {
				ItemStack itemstack = payment;
				payment = null;
				return itemstack;
			} else {
				payment.stackSize -= p_70298_2_;
				return new ItemStack(payment.getItem(), p_70298_2_, payment.getItemDamage());
			}
		} else
			return null;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int p_70304_1_) {
		if (p_70304_1_ == 0 && payment != null) {
			ItemStack itemstack = payment;
			payment = null;
			return itemstack;
		} else
			return null;
	}

	@Override
	public void setInventorySlotContents(int p_70299_1_, ItemStack p_70299_2_) {
		if (p_70299_1_ == 0) {
			payment = p_70299_2_;
		}
	}

	@Override
	public String getInventoryName() {
		return hasCustomInventoryName() ? field_146008_p : "container.beacon";
	}

	@Override
	public boolean hasCustomInventoryName() {
		return field_146008_p != null && field_146008_p.length() > 0;
	}

	public void func_145999_a(String p_145999_1_) {
		field_146008_p = p_145999_1_;
	}

	@Override
	public int getInventoryStackLimit() {
		return 1;
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
		return p_94041_2_.getItem() != null && p_94041_2_.getItem().isBeaconPayment(p_94041_2_);
	}
}