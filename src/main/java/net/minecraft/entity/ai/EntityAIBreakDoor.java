package net.minecraft.entity.ai;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLiving;
import net.minecraft.world.EnumDifficulty;

public class EntityAIBreakDoor extends EntityAIDoorInteract {
	private int breakingTime;
	private int field_75358_j = -1;
	private static final String __OBFID = "CL_00001577";

	public EntityAIBreakDoor(EntityLiving p_i1618_1_) {
		super(p_i1618_1_);
	}

	@Override
	public boolean shouldExecute() {
		return super.shouldExecute() && theEntity.worldObj.getGameRules().getGameRuleBooleanValue("mobGriefing") && !field_151504_e.func_150015_f(theEntity.worldObj, entityPosX, entityPosY, entityPosZ);
	}

	@Override
	public void startExecuting() {
		super.startExecuting();
		breakingTime = 0;
	}

	@Override
	public boolean continueExecuting() {
		double d0 = theEntity.getDistanceSq(entityPosX, entityPosY, entityPosZ);
		return breakingTime <= 240
				&& !field_151504_e.func_150015_f(theEntity.worldObj, entityPosX, entityPosY, entityPosZ) && d0 < 4.0D;
	}

	@Override
	public void resetTask() {
		super.resetTask();
		theEntity.worldObj.destroyBlockInWorldPartially(theEntity.getEntityId(), entityPosX, entityPosY, entityPosZ,
				-1);
	}

	@Override
	public void updateTask() {
		super.updateTask();

		if (theEntity.getRNG().nextInt(20) == 0) {
			theEntity.worldObj.playAuxSFX(1010, entityPosX, entityPosY, entityPosZ, 0);
		}

		++breakingTime;
		int i = (int) (breakingTime / 240.0F * 10.0F);

		if (i != field_75358_j) {
			theEntity.worldObj.destroyBlockInWorldPartially(theEntity.getEntityId(), entityPosX, entityPosY, entityPosZ,
					i);
			field_75358_j = i;
		}

		if (breakingTime == 240 && theEntity.worldObj.difficultySetting == EnumDifficulty.HARD) {
			if (org.bukkit.craftbukkit.event.CraftEventFactory
					.callEntityBreakDoorEvent(this.theEntity, this.entityPosX, this.entityPosY, this.entityPosZ)
					.isCancelled()) {
				this.resetTask();
				return;
			}
			theEntity.worldObj.setBlockToAir(entityPosX, entityPosY, entityPosZ);
			theEntity.worldObj.playAuxSFX(1012, entityPosX, entityPosY, entityPosZ, 0);
			theEntity.worldObj.playAuxSFX(2001, entityPosX, entityPosY, entityPosZ,
					Block.getIdFromBlock(field_151504_e));
		}
	}
}