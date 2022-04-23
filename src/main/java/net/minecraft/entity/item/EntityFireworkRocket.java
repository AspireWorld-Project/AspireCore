package net.minecraft.entity.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityFireworkRocket extends Entity {
	private int fireworkAge;
	private int lifetime;
	private static final String __OBFID = "CL_00001718";

	public EntityFireworkRocket(World p_i1762_1_) {
		super(p_i1762_1_);
		setSize(0.25F, 0.25F);
	}

	@Override
	protected void entityInit() {
		dataWatcher.addObjectByDataType(8, 5);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean isInRangeToRenderDist(double p_70112_1_) {
		return p_70112_1_ < 4096.0D;
	}

	public EntityFireworkRocket(World p_i1763_1_, double p_i1763_2_, double p_i1763_4_, double p_i1763_6_,
			ItemStack p_i1763_8_) {
		super(p_i1763_1_);
		fireworkAge = 0;
		setSize(0.25F, 0.25F);
		setPosition(p_i1763_2_, p_i1763_4_, p_i1763_6_);
		yOffset = 0.0F;
		int i = 1;

		if (p_i1763_8_ != null && p_i1763_8_.hasTagCompound()) {
			dataWatcher.updateObject(8, p_i1763_8_);
			NBTTagCompound nbttagcompound = p_i1763_8_.getTagCompound();
			NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("Fireworks");

			if (nbttagcompound1 != null) {
				i += nbttagcompound1.getByte("Flight");
			}
		}

		motionX = rand.nextGaussian() * 0.001D;
		motionZ = rand.nextGaussian() * 0.001D;
		motionY = 0.05D;
		lifetime = 10 * i + rand.nextInt(6) + rand.nextInt(7);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void setVelocity(double p_70016_1_, double p_70016_3_, double p_70016_5_) {
		motionX = p_70016_1_;
		motionY = p_70016_3_;
		motionZ = p_70016_5_;

		if (prevRotationPitch == 0.0F && prevRotationYaw == 0.0F) {
			float f = MathHelper.sqrt_double(p_70016_1_ * p_70016_1_ + p_70016_5_ * p_70016_5_);
			prevRotationYaw = rotationYaw = (float) (Math.atan2(p_70016_1_, p_70016_5_) * 180.0D / Math.PI);
			prevRotationPitch = rotationPitch = (float) (Math.atan2(p_70016_3_, f) * 180.0D / Math.PI);
		}
	}

	@Override
	public void onUpdate() {
		lastTickPosX = posX;
		lastTickPosY = posY;
		lastTickPosZ = posZ;
		super.onUpdate();
		motionX *= 1.15D;
		motionZ *= 1.15D;
		motionY += 0.04D;
		moveEntity(motionX, motionY, motionZ);
		float f = MathHelper.sqrt_double(motionX * motionX + motionZ * motionZ);
		rotationYaw = (float) (Math.atan2(motionX, motionZ) * 180.0D / Math.PI);

		for (rotationPitch = (float) (Math.atan2(motionY, f) * 180.0D / Math.PI); rotationPitch
				- prevRotationPitch < -180.0F; prevRotationPitch -= 360.0F) {
		}

		while (rotationPitch - prevRotationPitch >= 180.0F) {
			prevRotationPitch += 360.0F;
		}

		while (rotationYaw - prevRotationYaw < -180.0F) {
			prevRotationYaw -= 360.0F;
		}

		while (rotationYaw - prevRotationYaw >= 180.0F) {
			prevRotationYaw += 360.0F;
		}

		rotationPitch = prevRotationPitch + (rotationPitch - prevRotationPitch) * 0.2F;
		rotationYaw = prevRotationYaw + (rotationYaw - prevRotationYaw) * 0.2F;

		if (fireworkAge == 0) {
			worldObj.playSoundAtEntity(this, "fireworks.launch", 3.0F, 1.0F);
		}

		++fireworkAge;

		if (worldObj.isRemote && fireworkAge % 2 < 2) {
			worldObj.spawnParticle("fireworksSpark", posX, posY - 0.3D, posZ, rand.nextGaussian() * 0.05D,
					-motionY * 0.5D, rand.nextGaussian() * 0.05D);
		}

		if (!worldObj.isRemote && fireworkAge > lifetime) {
			worldObj.setEntityState(this, (byte) 17);
			setDead();
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void handleHealthUpdate(byte p_70103_1_) {
		if (p_70103_1_ == 17 && worldObj.isRemote) {
			ItemStack itemstack = dataWatcher.getWatchableObjectItemStack(8);
			NBTTagCompound nbttagcompound = null;

			if (itemstack != null && itemstack.hasTagCompound()) {
				nbttagcompound = itemstack.getTagCompound().getCompoundTag("Fireworks");
			}

			worldObj.makeFireworks(posX, posY, posZ, motionX, motionY, motionZ, nbttagcompound);
		}

		super.handleHealthUpdate(p_70103_1_);
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound p_70014_1_) {
		p_70014_1_.setInteger("Life", fireworkAge);
		p_70014_1_.setInteger("LifeTime", lifetime);
		ItemStack itemstack = dataWatcher.getWatchableObjectItemStack(8);

		if (itemstack != null) {
			NBTTagCompound nbttagcompound1 = new NBTTagCompound();
			itemstack.writeToNBT(nbttagcompound1);
			p_70014_1_.setTag("FireworksItem", nbttagcompound1);
		}
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound p_70037_1_) {
		fireworkAge = p_70037_1_.getInteger("Life");
		lifetime = p_70037_1_.getInteger("LifeTime");
		NBTTagCompound nbttagcompound1 = p_70037_1_.getCompoundTag("FireworksItem");

		if (nbttagcompound1 != null) {
			ItemStack itemstack = ItemStack.loadItemStackFromNBT(nbttagcompound1);

			if (itemstack != null) {
				dataWatcher.updateObject(8, itemstack);
			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public float getShadowSize() {
		return 0.0F;
	}

	@Override
	public float getBrightness(float p_70013_1_) {
		return super.getBrightness(p_70013_1_);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getBrightnessForRender(float p_70070_1_) {
		return super.getBrightnessForRender(p_70070_1_);
	}

	@Override
	public boolean canAttackWithItem() {
		return false;
	}

	public int getLifetime() {
		return lifetime;
	}

	public void setLifetime(int lifetime) {
		this.lifetime = lifetime;
	}
}