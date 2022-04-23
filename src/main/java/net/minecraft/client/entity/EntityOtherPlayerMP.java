package net.minecraft.client.entity;

import com.mojang.authlib.GameProfile;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

@SideOnly(Side.CLIENT)
public class EntityOtherPlayerMP extends AbstractClientPlayer {
	private boolean isItemInUse;
	private int otherPlayerMPPosRotationIncrements;
	private double otherPlayerMPX;
	private double otherPlayerMPY;
	private double otherPlayerMPZ;
	private double otherPlayerMPYaw;
	private double otherPlayerMPPitch;
	public EntityOtherPlayerMP(World p_i45075_1_, GameProfile p_i45075_2_) {
		super(p_i45075_1_, p_i45075_2_);
		yOffset = 0.0F;
		stepHeight = 0.0F;
		noClip = true;
		field_71082_cx = 0.25F;
		renderDistanceWeight = 10.0D;
	}

	@Override
	protected void resetHeight() {
		yOffset = 0.0F;
	}

	@Override
	public boolean attackEntityFrom(DamageSource p_70097_1_, float p_70097_2_) {
		return true;
	}

	@Override
	public void setPositionAndRotation2(double p_70056_1_, double p_70056_3_, double p_70056_5_, float p_70056_7_,
			float p_70056_8_, int p_70056_9_) {
		otherPlayerMPX = p_70056_1_;
		otherPlayerMPY = p_70056_3_;
		otherPlayerMPZ = p_70056_5_;
		otherPlayerMPYaw = p_70056_7_;
		otherPlayerMPPitch = p_70056_8_;
		otherPlayerMPPosRotationIncrements = p_70056_9_;
	}

	@Override
	public void onUpdate() {
		field_71082_cx = 0.0F;
		super.onUpdate();
		prevLimbSwingAmount = limbSwingAmount;
		double d0 = posX - prevPosX;
		double d1 = posZ - prevPosZ;
		float f = MathHelper.sqrt_double(d0 * d0 + d1 * d1) * 4.0F;

		if (f > 1.0F) {
			f = 1.0F;
		}

		limbSwingAmount += (f - limbSwingAmount) * 0.4F;
		limbSwing += limbSwingAmount;

		if (!isItemInUse && isEating() && inventory.mainInventory[inventory.currentItem] != null) {
			ItemStack itemstack = inventory.mainInventory[inventory.currentItem];
			setItemInUse(inventory.mainInventory[inventory.currentItem],
					itemstack.getItem().getMaxItemUseDuration(itemstack));
			isItemInUse = true;
		} else if (isItemInUse && !isEating()) {
			clearItemInUse();
			isItemInUse = false;
		}
	}

	@Override
	public float getShadowSize() {
		return 0.0F;
	}

	@Override
	public void onLivingUpdate() {
		super.updateEntityActionState();

		if (otherPlayerMPPosRotationIncrements > 0) {
			double d0 = posX + (otherPlayerMPX - posX) / otherPlayerMPPosRotationIncrements;
			double d1 = posY + (otherPlayerMPY - posY) / otherPlayerMPPosRotationIncrements;
			double d2 = posZ + (otherPlayerMPZ - posZ) / otherPlayerMPPosRotationIncrements;
			double d3;

			for (d3 = otherPlayerMPYaw - rotationYaw; d3 < -180.0D; d3 += 360.0D) {
			}

			while (d3 >= 180.0D) {
				d3 -= 360.0D;
			}

			rotationYaw = (float) (rotationYaw + d3 / otherPlayerMPPosRotationIncrements);
			rotationPitch = (float) (rotationPitch
					+ (otherPlayerMPPitch - rotationPitch) / otherPlayerMPPosRotationIncrements);
			--otherPlayerMPPosRotationIncrements;
			setPosition(d0, d1, d2);
			setRotation(rotationYaw, rotationPitch);
		}

		prevCameraYaw = cameraYaw;
		float f1 = MathHelper.sqrt_double(motionX * motionX + motionZ * motionZ);
		float f = (float) Math.atan(-motionY * 0.20000000298023224D) * 15.0F;

		if (f1 > 0.1F) {
			f1 = 0.1F;
		}

		if (!onGround || getHealth() <= 0.0F) {
			f1 = 0.0F;
		}

		if (onGround || getHealth() <= 0.0F) {
			f = 0.0F;
		}

		cameraYaw += (f1 - cameraYaw) * 0.4F;
		cameraPitch += (f - cameraPitch) * 0.8F;
	}

	@Override
	public void setCurrentItemOrArmor(int p_70062_1_, ItemStack p_70062_2_) {
		if (p_70062_1_ == 0) {
			inventory.mainInventory[inventory.currentItem] = p_70062_2_;
		} else {
			inventory.armorInventory[p_70062_1_ - 1] = p_70062_2_;
		}
	}

	@Override
	public float getDefaultEyeHeight() {
		return 1.82F;
	}

	@Override
	public void addChatMessage(IChatComponent p_145747_1_) {
		Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(p_145747_1_);
	}

	@Override
	public boolean canCommandSenderUseCommand(int p_70003_1_, String p_70003_2_) {
		return false;
	}

	@Override
	public ChunkCoordinates getPlayerCoordinates() {
		return new ChunkCoordinates(MathHelper.floor_double(posX + 0.5D), MathHelper.floor_double(posY + 0.5D),
				MathHelper.floor_double(posZ + 0.5D));
	}
}