package net.minecraft.entity.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityEnderEye extends Entity {
	private double targetX;
	private double targetY;
	private double targetZ;
	private int despawnTimer;
	private boolean shatterOrDrop;
	private static final String __OBFID = "CL_00001716";

	public EntityEnderEye(World p_i1757_1_) {
		super(p_i1757_1_);
		setSize(0.25F, 0.25F);
	}

	@Override
	protected void entityInit() {
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean isInRangeToRenderDist(double p_70112_1_) {
		double d1 = boundingBox.getAverageEdgeLength() * 4.0D;
		d1 *= 64.0D;
		return p_70112_1_ < d1 * d1;
	}

	public EntityEnderEye(World p_i1758_1_, double p_i1758_2_, double p_i1758_4_, double p_i1758_6_) {
		super(p_i1758_1_);
		despawnTimer = 0;
		setSize(0.25F, 0.25F);
		setPosition(p_i1758_2_, p_i1758_4_, p_i1758_6_);
		yOffset = 0.0F;
	}

	public void moveTowards(double p_70220_1_, int p_70220_3_, double p_70220_4_) {
		double d2 = p_70220_1_ - posX;
		double d3 = p_70220_4_ - posZ;
		float f = MathHelper.sqrt_double(d2 * d2 + d3 * d3);

		if (f > 12.0F) {
			targetX = posX + d2 / f * 12.0D;
			targetZ = posZ + d3 / f * 12.0D;
			targetY = posY + 8.0D;
		} else {
			targetX = p_70220_1_;
			targetY = p_70220_3_;
			targetZ = p_70220_4_;
		}

		despawnTimer = 0;
		shatterOrDrop = rand.nextInt(5) > 0;
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
		posX += motionX;
		posY += motionY;
		posZ += motionZ;
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

		if (!worldObj.isRemote) {
			double d0 = targetX - posX;
			double d1 = targetZ - posZ;
			float f1 = (float) Math.sqrt(d0 * d0 + d1 * d1);
			float f2 = (float) Math.atan2(d1, d0);
			double d2 = f + (f1 - f) * 0.0025D;

			if (f1 < 1.0F) {
				d2 *= 0.8D;
				motionY *= 0.8D;
			}

			motionX = Math.cos(f2) * d2;
			motionZ = Math.sin(f2) * d2;

			if (posY < targetY) {
				motionY += (1.0D - motionY) * 0.014999999664723873D;
			} else {
				motionY += (-1.0D - motionY) * 0.014999999664723873D;
			}
		}

		float f3 = 0.25F;

		if (isInWater()) {
			for (int i = 0; i < 4; ++i) {
				worldObj.spawnParticle("bubble", posX - motionX * f3, posY - motionY * f3, posZ - motionZ * f3, motionX,
						motionY, motionZ);
			}
		} else {
			worldObj.spawnParticle("portal", posX - motionX * f3 + rand.nextDouble() * 0.6D - 0.3D,
					posY - motionY * f3 - 0.5D, posZ - motionZ * f3 + rand.nextDouble() * 0.6D - 0.3D, motionX, motionY,
					motionZ);
		}

		if (!worldObj.isRemote) {
			setPosition(posX, posY, posZ);
			++despawnTimer;

			if (despawnTimer > 80 && !worldObj.isRemote) {
				setDead();

				if (shatterOrDrop) {
					worldObj.spawnEntityInWorld(
							new EntityItem(worldObj, posX, posY, posZ, new ItemStack(Items.ender_eye)));
				} else {
					worldObj.playAuxSFX(2003, (int) Math.round(posX), (int) Math.round(posY), (int) Math.round(posZ),
							0);
				}
			}
		}
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound p_70014_1_) {
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound p_70037_1_) {
	}

	@Override
	@SideOnly(Side.CLIENT)
	public float getShadowSize() {
		return 0.0F;
	}

	@Override
	public float getBrightness(float p_70013_1_) {
		return 1.0F;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getBrightnessForRender(float p_70070_1_) {
		return 15728880;
	}

	@Override
	public boolean canAttackWithItem() {
		return false;
	}
}