package net.minecraft.entity.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapData;

public class EntityItemFrame extends EntityHanging {
	private float itemDropChance = 1.0F;
	private static final String __OBFID = "CL_00001547";

	public EntityItemFrame(World p_i1590_1_) {
		super(p_i1590_1_);
	}

	public EntityItemFrame(World p_i1591_1_, int p_i1591_2_, int p_i1591_3_, int p_i1591_4_, int p_i1591_5_) {
		super(p_i1591_1_, p_i1591_2_, p_i1591_3_, p_i1591_4_, p_i1591_5_);
		setDirection(p_i1591_5_);
	}

	@Override
	protected void entityInit() {
		getDataWatcher().addObjectByDataType(2, 5);
		getDataWatcher().addObject(3, Byte.valueOf((byte) 0));
	}

	@Override
	public boolean attackEntityFrom(DamageSource p_70097_1_, float p_70097_2_) {
		if (isEntityInvulnerable())
			return false;
		else if (getDisplayedItem() != null) {
			if (!worldObj.isRemote) {
				if (org.bukkit.craftbukkit.event.CraftEventFactory.handleNonLivingEntityDamageEvent(this, p_70097_1_,
						p_70097_2_) || isDead)
					return true;
				func_146065_b(p_70097_1_.getEntity(), false);
				setDisplayedItem(null);
			}

			return true;
		} else
			return super.attackEntityFrom(p_70097_1_, p_70097_2_);
	}

	@Override
	public int getWidthPixels() {
		return 9;
	}

	@Override
	public int getHeightPixels() {
		return 9;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean isInRangeToRenderDist(double p_70112_1_) {
		double d1 = 16.0D;
		d1 *= 64.0D * renderDistanceWeight;
		return p_70112_1_ < d1 * d1;
	}

	@Override
	public void onBroken(Entity p_110128_1_) {
		func_146065_b(p_110128_1_, true);
	}

	public void func_146065_b(Entity p_146065_1_, boolean p_146065_2_) {
		ItemStack itemstack = getDisplayedItem();

		if (p_146065_1_ instanceof EntityPlayer) {
			EntityPlayer entityplayer = (EntityPlayer) p_146065_1_;

			if (entityplayer.capabilities.isCreativeMode) {
				removeFrameFromMap(itemstack);
				return;
			}
		}

		if (p_146065_2_) {
			entityDropItem(new ItemStack(Items.item_frame), 0.0F);
		}

		if (itemstack != null && rand.nextFloat() < itemDropChance) {
			itemstack = itemstack.copy();
			removeFrameFromMap(itemstack);
			entityDropItem(itemstack, 0.0F);
		}
	}

	private void removeFrameFromMap(ItemStack p_110131_1_) {
		if (p_110131_1_ != null) {
			if (p_110131_1_.getItem() == Items.filled_map) {
				MapData mapdata = ((ItemMap) p_110131_1_.getItem()).getMapData(p_110131_1_, worldObj);
				mapdata.playersVisibleOnMap.remove("frame-" + getEntityId());
			}

			p_110131_1_.setItemFrame(null);
		}
	}

	public ItemStack getDisplayedItem() {
		return getDataWatcher().getWatchableObjectItemStack(2);
	}

	public void setDisplayedItem(ItemStack p_82334_1_) {
		if (p_82334_1_ != null) {
			p_82334_1_ = p_82334_1_.copy();
			p_82334_1_.stackSize = 1;
			p_82334_1_.setItemFrame(this);
		}

		getDataWatcher().updateObject(2, p_82334_1_);
		getDataWatcher().setObjectWatched(2);
	}

	public int getRotation() {
		return getDataWatcher().getWatchableObjectByte(3);
	}

	public void setItemRotation(int p_82336_1_) {
		getDataWatcher().updateObject(3, Byte.valueOf((byte) (p_82336_1_ % 4)));
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound p_70014_1_) {
		if (getDisplayedItem() != null) {
			p_70014_1_.setTag("Item", getDisplayedItem().writeToNBT(new NBTTagCompound()));
			p_70014_1_.setByte("ItemRotation", (byte) getRotation());
			p_70014_1_.setFloat("ItemDropChance", itemDropChance);
		}

		super.writeEntityToNBT(p_70014_1_);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound p_70037_1_) {
		NBTTagCompound nbttagcompound1 = p_70037_1_.getCompoundTag("Item");

		if (nbttagcompound1 != null && !nbttagcompound1.hasNoTags()) {
			setDisplayedItem(ItemStack.loadItemStackFromNBT(nbttagcompound1));
			setItemRotation(p_70037_1_.getByte("ItemRotation"));

			if (p_70037_1_.hasKey("ItemDropChance", 99)) {
				itemDropChance = p_70037_1_.getFloat("ItemDropChance");
			}
		}

		super.readEntityFromNBT(p_70037_1_);
	}

	@Override
	public boolean interactFirst(EntityPlayer p_130002_1_) {
		if (getDisplayedItem() == null) {
			ItemStack itemstack = p_130002_1_.getHeldItem();

			if (itemstack != null && !worldObj.isRemote) {
				setDisplayedItem(itemstack);

				if (!p_130002_1_.capabilities.isCreativeMode && --itemstack.stackSize <= 0) {
					p_130002_1_.inventory.setInventorySlotContents(p_130002_1_.inventory.currentItem, null);
				}
			}
		} else if (!worldObj.isRemote) {
			setItemRotation(getRotation() + 1);
		}

		return true;
	}
}