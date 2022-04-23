package net.minecraft.entity.ai;

import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class EntityAIBeg extends EntityAIBase {
	private final EntityWolf theWolf;
	private EntityPlayer thePlayer;
	private final World worldObject;
	private final float minPlayerDistance;
	private int field_75384_e;
	private static final String __OBFID = "CL_00001576";

	public EntityAIBeg(EntityWolf p_i1617_1_, float p_i1617_2_) {
		theWolf = p_i1617_1_;
		worldObject = p_i1617_1_.worldObj;
		minPlayerDistance = p_i1617_2_;
		setMutexBits(2);
	}

	@Override
	public boolean shouldExecute() {
		thePlayer = worldObject.getClosestPlayerToEntity(theWolf, minPlayerDistance);
		return thePlayer != null && hasPlayerGotBoneInHand(thePlayer);
	}

	@Override
	public boolean continueExecuting() {
		return thePlayer.isEntityAlive() && !(theWolf.getDistanceSqToEntity(thePlayer) > minPlayerDistance * minPlayerDistance) && field_75384_e > 0 && hasPlayerGotBoneInHand(thePlayer);
	}

	@Override
	public void startExecuting() {
		theWolf.func_70918_i(true);
		field_75384_e = 40 + theWolf.getRNG().nextInt(40);
	}

	@Override
	public void resetTask() {
		theWolf.func_70918_i(false);
		thePlayer = null;
	}

	@Override
	public void updateTask() {
		theWolf.getLookHelper().setLookPosition(thePlayer.posX, thePlayer.posY + thePlayer.getEyeHeight(),
				thePlayer.posZ, 10.0F, theWolf.getVerticalFaceSpeed());
		--field_75384_e;
	}

	private boolean hasPlayerGotBoneInHand(EntityPlayer p_75382_1_) {
		ItemStack itemstack = p_75382_1_.inventory.getCurrentItem();
		return itemstack != null && (!theWolf.isTamed() && itemstack.getItem() == Items.bone || theWolf.isBreedingItem(itemstack));
	}
}