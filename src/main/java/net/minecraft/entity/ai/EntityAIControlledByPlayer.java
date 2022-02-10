package net.minecraft.entity.ai;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathFinder;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.MathHelper;

public class EntityAIControlledByPlayer extends EntityAIBase {
	private final EntityLiving thisEntity;
	private final float maxSpeed;
	private float currentSpeed;
	private boolean speedBoosted;
	private int speedBoostTime;
	private int maxSpeedBoostTime;
	private static final String __OBFID = "CL_00001580";

	public EntityAIControlledByPlayer(EntityLiving p_i1620_1_, float p_i1620_2_) {
		thisEntity = p_i1620_1_;
		maxSpeed = p_i1620_2_;
		setMutexBits(7);
	}

	@Override
	public void startExecuting() {
		currentSpeed = 0.0F;
	}

	@Override
	public void resetTask() {
		speedBoosted = false;
		currentSpeed = 0.0F;
	}

	@Override
	public boolean shouldExecute() {
		return thisEntity.isEntityAlive() && thisEntity.riddenByEntity != null
				&& thisEntity.riddenByEntity instanceof EntityPlayer && (speedBoosted || thisEntity.canBeSteered());
	}

	@Override
	public void updateTask() {
		EntityPlayer entityplayer = (EntityPlayer) thisEntity.riddenByEntity;
		EntityCreature entitycreature = (EntityCreature) thisEntity;
		float f = MathHelper.wrapAngleTo180_float(entityplayer.rotationYaw - thisEntity.rotationYaw) * 0.5F;

		if (f > 5.0F) {
			f = 5.0F;
		}

		if (f < -5.0F) {
			f = -5.0F;
		}

		thisEntity.rotationYaw = MathHelper.wrapAngleTo180_float(thisEntity.rotationYaw + f);

		if (currentSpeed < maxSpeed) {
			currentSpeed += (maxSpeed - currentSpeed) * 0.01F;
		}

		if (currentSpeed > maxSpeed) {
			currentSpeed = maxSpeed;
		}

		int i = MathHelper.floor_double(thisEntity.posX);
		int j = MathHelper.floor_double(thisEntity.posY);
		int k = MathHelper.floor_double(thisEntity.posZ);
		float f1 = currentSpeed;

		if (speedBoosted) {
			if (speedBoostTime++ > maxSpeedBoostTime) {
				speedBoosted = false;
			}

			f1 += f1 * 1.15F * MathHelper.sin((float) speedBoostTime / (float) maxSpeedBoostTime * (float) Math.PI);
		}

		float f2 = 0.91F;

		if (thisEntity.onGround) {
			f2 = thisEntity.worldObj.getBlock(MathHelper.floor_float(i), MathHelper.floor_float(j) - 1,
					MathHelper.floor_float(k)).slipperiness * 0.91F;
		}

		float f3 = 0.16277136F / (f2 * f2 * f2);
		float f4 = MathHelper.sin(entitycreature.rotationYaw * (float) Math.PI / 180.0F);
		float f5 = MathHelper.cos(entitycreature.rotationYaw * (float) Math.PI / 180.0F);
		float f6 = entitycreature.getAIMoveSpeed() * f3;
		float f7 = Math.max(f1, 1.0F);
		f7 = f6 / f7;
		float f8 = f1 * f7;
		float f9 = -(f8 * f4);
		float f10 = f8 * f5;

		if (MathHelper.abs(f9) > MathHelper.abs(f10)) {
			if (f9 < 0.0F) {
				f9 -= thisEntity.width / 2.0F;
			}

			if (f9 > 0.0F) {
				f9 += thisEntity.width / 2.0F;
			}

			f10 = 0.0F;
		} else {
			f9 = 0.0F;

			if (f10 < 0.0F) {
				f10 -= thisEntity.width / 2.0F;
			}

			if (f10 > 0.0F) {
				f10 += thisEntity.width / 2.0F;
			}
		}

		int l = MathHelper.floor_double(thisEntity.posX + f9);
		int i1 = MathHelper.floor_double(thisEntity.posZ + f10);
		PathPoint pathpoint = new PathPoint(MathHelper.floor_float(thisEntity.width + 1.0F),
				MathHelper.floor_float(thisEntity.height + entityplayer.height + 1.0F),
				MathHelper.floor_float(thisEntity.width + 1.0F));

		if (i != l || k != i1) {
			Block block = thisEntity.worldObj.getBlock(i, j, k);
			boolean flag = !func_151498_a(block) && (block.getMaterial() != Material.air
					|| !func_151498_a(thisEntity.worldObj.getBlock(i, j - 1, k)));

			if (flag && PathFinder.func_82565_a(thisEntity, l, j, i1, pathpoint, false, false, true) == 0
					&& PathFinder.func_82565_a(thisEntity, i, j + 1, k, pathpoint, false, false, true) == 1
					&& PathFinder.func_82565_a(thisEntity, l, j + 1, i1, pathpoint, false, false, true) == 1) {
				entitycreature.getJumpHelper().setJumping();
			}
		}

		if (!entityplayer.capabilities.isCreativeMode && currentSpeed >= maxSpeed * 0.5F
				&& thisEntity.getRNG().nextFloat() < 0.006F && !speedBoosted) {
			ItemStack itemstack = entityplayer.getHeldItem();

			if (itemstack != null && itemstack.getItem() == Items.carrot_on_a_stick) {
				itemstack.damageItem(1, entityplayer);

				if (itemstack.stackSize == 0) {
					ItemStack itemstack1 = new ItemStack(Items.fishing_rod);
					itemstack1.setTagCompound(itemstack.stackTagCompound);
					entityplayer.inventory.mainInventory[entityplayer.inventory.currentItem] = itemstack1;
				}
			}
		}

		thisEntity.moveEntityWithHeading(0.0F, f1);
	}

	private boolean func_151498_a(Block p_151498_1_) {
		return p_151498_1_.getRenderType() == 10 || p_151498_1_ instanceof BlockSlab;
	}

	public boolean isSpeedBoosted() {
		return speedBoosted;
	}

	public void boostSpeed() {
		speedBoosted = true;
		speedBoostTime = 0;
		maxSpeedBoostTime = thisEntity.getRNG().nextInt(841) + 140;
	}

	public boolean isControlledByPlayer() {
		return !isSpeedBoosted() && currentSpeed > maxSpeed * 0.3F;
	}
}