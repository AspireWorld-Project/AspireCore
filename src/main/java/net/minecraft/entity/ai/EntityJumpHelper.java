package net.minecraft.entity.ai;

import net.minecraft.entity.EntityLiving;

public class EntityJumpHelper {
	private final EntityLiving entity;
	private boolean isJumping;
	private static final String __OBFID = "CL_00001571";

	public EntityJumpHelper(EntityLiving p_i1612_1_) {
		entity = p_i1612_1_;
	}

	public void setJumping() {
		isJumping = true;
	}

	public void doJump() {
		entity.setJumping(isJumping);
		isJumping = false;
	}
}