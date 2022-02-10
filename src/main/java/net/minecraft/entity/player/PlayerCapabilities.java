package net.minecraft.entity.player;

import net.minecraft.nbt.NBTTagCompound;

public class PlayerCapabilities {
	public boolean disableDamage;
	public boolean isFlying;
	public boolean allowFlying;
	public boolean isCreativeMode;
	public boolean allowEdit = true;
	private float flySpeed = 0.05F;
	private float walkSpeed = 0.1F;
	private static final String __OBFID = "CL_00001708";

	public void writeCapabilitiesToNBT(NBTTagCompound p_75091_1_) {
		NBTTagCompound nbttagcompound1 = new NBTTagCompound();
		nbttagcompound1.setBoolean("invulnerable", disableDamage);
		nbttagcompound1.setBoolean("flying", isFlying);
		nbttagcompound1.setBoolean("mayfly", allowFlying);
		nbttagcompound1.setBoolean("instabuild", isCreativeMode);
		nbttagcompound1.setBoolean("mayBuild", allowEdit);
		nbttagcompound1.setFloat("flySpeed", flySpeed);
		nbttagcompound1.setFloat("walkSpeed", walkSpeed);
		p_75091_1_.setTag("abilities", nbttagcompound1);
	}

	public void readCapabilitiesFromNBT(NBTTagCompound p_75095_1_) {
		if (p_75095_1_.hasKey("abilities", 10)) {
			NBTTagCompound nbttagcompound1 = p_75095_1_.getCompoundTag("abilities");
			disableDamage = nbttagcompound1.getBoolean("invulnerable");
			isFlying = nbttagcompound1.getBoolean("flying");
			allowFlying = nbttagcompound1.getBoolean("mayfly");
			isCreativeMode = nbttagcompound1.getBoolean("instabuild");

			if (nbttagcompound1.hasKey("flySpeed", 99)) {
				flySpeed = nbttagcompound1.getFloat("flySpeed");
				walkSpeed = nbttagcompound1.getFloat("walkSpeed");
			}

			if (nbttagcompound1.hasKey("mayBuild", 1)) {
				allowEdit = nbttagcompound1.getBoolean("mayBuild");
			}
		}
	}

	public float getFlySpeed() {
		return flySpeed;
	}

	public void setFlySpeed(float p_75092_1_) {
		flySpeed = p_75092_1_;
	}

	public float getWalkSpeed() {
		return walkSpeed;
	}

	public void setPlayerWalkSpeed(float p_82877_1_) {
		walkSpeed = p_82877_1_;
	}

	public void setWalkSpeed(float walkSpeed) {
		this.walkSpeed = walkSpeed;
	}
}