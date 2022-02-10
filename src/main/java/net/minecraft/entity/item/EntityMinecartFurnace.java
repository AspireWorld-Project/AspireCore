package net.minecraft.entity.item;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityMinecartFurnace extends EntityMinecart {
	private int fuel;
	public double pushX;
	public double pushZ;
	private static final String __OBFID = "CL_00001675";

	public EntityMinecartFurnace(World p_i1718_1_) {
		super(p_i1718_1_);
	}

	public EntityMinecartFurnace(World p_i1719_1_, double p_i1719_2_, double p_i1719_4_, double p_i1719_6_) {
		super(p_i1719_1_, p_i1719_2_, p_i1719_4_, p_i1719_6_);
	}

	@Override
	public int getMinecartType() {
		return 2;
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		dataWatcher.addObject(16, new Byte((byte) 0));
	}

	@Override
	public void onUpdate() {
		super.onUpdate();

		if (fuel > 0) {
			--fuel;
		}

		if (fuel <= 0) {
			pushX = pushZ = 0.0D;
		}

		setMinecartPowered(fuel > 0);

		if (isMinecartPowered() && rand.nextInt(4) == 0) {
			worldObj.spawnParticle("largesmoke", posX, posY + 0.8D, posZ, 0.0D, 0.0D, 0.0D);
		}
	}

	@Override
	public void killMinecart(DamageSource p_94095_1_) {
		super.killMinecart(p_94095_1_);

		if (!p_94095_1_.isExplosion()) {
			entityDropItem(new ItemStack(Blocks.furnace, 1), 0.0F);
		}
	}

	@Override
	protected void func_145821_a(int p_145821_1_, int p_145821_2_, int p_145821_3_, double p_145821_4_,
			double p_145821_6_, Block p_145821_8_, int p_145821_9_) {
		super.func_145821_a(p_145821_1_, p_145821_2_, p_145821_3_, p_145821_4_, p_145821_6_, p_145821_8_, p_145821_9_);
		double d2 = pushX * pushX + pushZ * pushZ;

		if (d2 > 1.0E-4D && motionX * motionX + motionZ * motionZ > 0.001D) {
			d2 = MathHelper.sqrt_double(d2);
			pushX /= d2;
			pushZ /= d2;

			if (pushX * motionX + pushZ * motionZ < 0.0D) {
				pushX = 0.0D;
				pushZ = 0.0D;
			} else {
				pushX = motionX;
				pushZ = motionZ;
			}
		}
	}

	@Override
	protected void applyDrag() {
		double d0 = pushX * pushX + pushZ * pushZ;

		if (d0 > 1.0E-4D) {
			d0 = MathHelper.sqrt_double(d0);
			pushX /= d0;
			pushZ /= d0;
			double d1 = 0.05D;
			motionX *= 0.800000011920929D;
			motionY *= 0.0D;
			motionZ *= 0.800000011920929D;
			motionX += pushX * d1;
			motionZ += pushZ * d1;
		} else {
			motionX *= 0.9800000190734863D;
			motionY *= 0.0D;
			motionZ *= 0.9800000190734863D;
		}

		super.applyDrag();
	}

	@Override
	public boolean interactFirst(EntityPlayer p_130002_1_) {
		if (net.minecraftforge.common.MinecraftForge.EVENT_BUS
				.post(new net.minecraftforge.event.entity.minecart.MinecartInteractEvent(this, p_130002_1_)))
			return true;
		ItemStack itemstack = p_130002_1_.inventory.getCurrentItem();

		if (itemstack != null && itemstack.getItem() == Items.coal) {
			if (!p_130002_1_.capabilities.isCreativeMode && --itemstack.stackSize == 0) {
				p_130002_1_.inventory.setInventorySlotContents(p_130002_1_.inventory.currentItem, (ItemStack) null);
			}

			fuel += 3600;
		}

		pushX = posX - p_130002_1_.posX;
		pushZ = posZ - p_130002_1_.posZ;
		return true;
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound p_70014_1_) {
		super.writeEntityToNBT(p_70014_1_);
		p_70014_1_.setDouble("PushX", pushX);
		p_70014_1_.setDouble("PushZ", pushZ);
		p_70014_1_.setShort("Fuel", (short) fuel);
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound p_70037_1_) {
		super.readEntityFromNBT(p_70037_1_);
		pushX = p_70037_1_.getDouble("PushX");
		pushZ = p_70037_1_.getDouble("PushZ");
		fuel = p_70037_1_.getShort("Fuel");
	}

	protected boolean isMinecartPowered() {
		return (dataWatcher.getWatchableObjectByte(16) & 1) != 0;
	}

	protected void setMinecartPowered(boolean p_94107_1_) {
		if (p_94107_1_) {
			dataWatcher.updateObject(16, Byte.valueOf((byte) (dataWatcher.getWatchableObjectByte(16) | 1)));
		} else {
			dataWatcher.updateObject(16, Byte.valueOf((byte) (dataWatcher.getWatchableObjectByte(16) & -2)));
		}
	}

	@Override
	public Block func_145817_o() {
		return Blocks.lit_furnace;
	}

	@Override
	public int getDefaultDisplayTileData() {
		return 2;
	}
}