package net.minecraft.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class EntityAITempt extends EntityAIBase {
	private final EntityCreature temptedEntity;
	private final double field_75282_b;
	private double targetX;
	private double targetY;
	private double targetZ;
	private double field_75278_f;
	private double field_75279_g;
	private EntityPlayer temptingPlayer;
	private int delayTemptCounter;
	private boolean isRunning;
	private final Item field_151484_k;
	private final boolean scaredByPlayerMovement;
	private boolean field_75286_m;
	private static final String __OBFID = "CL_00001616";

	public EntityAITempt(EntityCreature p_i45316_1_, double p_i45316_2_, Item p_i45316_4_, boolean p_i45316_5_) {
		temptedEntity = p_i45316_1_;
		field_75282_b = p_i45316_2_;
		field_151484_k = p_i45316_4_;
		scaredByPlayerMovement = p_i45316_5_;
		setMutexBits(3);
	}

	@Override
	public boolean shouldExecute() {
		if (delayTemptCounter > 0) {
			--delayTemptCounter;
			return false;
		} else {
			temptingPlayer = temptedEntity.worldObj.getClosestPlayerToEntity(temptedEntity, 10.0D);

			if (temptingPlayer == null)
				return false;
			else {
				ItemStack itemstack = temptingPlayer.getCurrentEquippedItem();
				return itemstack != null && itemstack.getItem() == field_151484_k;
			}
		}
	}

	@Override
	public boolean continueExecuting() {
		if (scaredByPlayerMovement) {
			if (temptedEntity.getDistanceSqToEntity(temptingPlayer) < 36.0D) {
				if (temptingPlayer.getDistanceSq(targetX, targetY, targetZ) > 0.010000000000000002D)
					return false;

				if (Math.abs(temptingPlayer.rotationPitch - field_75278_f) > 5.0D
						|| Math.abs(temptingPlayer.rotationYaw - field_75279_g) > 5.0D)
					return false;
			} else {
				targetX = temptingPlayer.posX;
				targetY = temptingPlayer.posY;
				targetZ = temptingPlayer.posZ;
			}

			field_75278_f = temptingPlayer.rotationPitch;
			field_75279_g = temptingPlayer.rotationYaw;
		}

		return shouldExecute();
	}

	@Override
	public void startExecuting() {
		targetX = temptingPlayer.posX;
		targetY = temptingPlayer.posY;
		targetZ = temptingPlayer.posZ;
		isRunning = true;
		field_75286_m = temptedEntity.getNavigator().getAvoidsWater();
		temptedEntity.getNavigator().setAvoidsWater(false);
	}

	@Override
	public void resetTask() {
		temptingPlayer = null;
		temptedEntity.getNavigator().clearPathEntity();
		delayTemptCounter = 100;
		isRunning = false;
		temptedEntity.getNavigator().setAvoidsWater(field_75286_m);
	}

	@Override
	public void updateTask() {
		temptedEntity.getLookHelper().setLookPositionWithEntity(temptingPlayer, 30.0F,
				temptedEntity.getVerticalFaceSpeed());

		if (temptedEntity.getDistanceSqToEntity(temptingPlayer) < 6.25D) {
			temptedEntity.getNavigator().clearPathEntity();
		} else {
			temptedEntity.getNavigator().tryMoveToEntityLiving(temptingPlayer, field_75282_b);
		}
	}

	public boolean isRunning() {
		return isRunning;
	}
}