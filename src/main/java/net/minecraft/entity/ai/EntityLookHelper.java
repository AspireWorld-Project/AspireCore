package net.minecraft.entity.ai;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;

public class EntityLookHelper {
	private EntityLiving entity;
	private float deltaLookYaw;
	private float deltaLookPitch;
	private boolean isLooking;
	private double posX;
	private double posY;
	private double posZ;
	private static final String __OBFID = "CL_00001572";

	public EntityLookHelper(EntityLiving p_i1613_1_) {
		entity = p_i1613_1_;
	}

	public void setLookPositionWithEntity(Entity p_75651_1_, float p_75651_2_, float p_75651_3_) {
		posX = p_75651_1_.posX;

		if (p_75651_1_ instanceof EntityLivingBase) {
			posY = p_75651_1_.posY + p_75651_1_.getEyeHeight();
		} else {
			posY = (p_75651_1_.boundingBox.minY + p_75651_1_.boundingBox.maxY) / 2.0D;
		}

		posZ = p_75651_1_.posZ;
		deltaLookYaw = p_75651_2_;
		deltaLookPitch = p_75651_3_;
		isLooking = true;
	}

	public void setLookPosition(double p_75650_1_, double p_75650_3_, double p_75650_5_, float p_75650_7_,
			float p_75650_8_) {
		posX = p_75650_1_;
		posY = p_75650_3_;
		posZ = p_75650_5_;
		deltaLookYaw = p_75650_7_;
		deltaLookPitch = p_75650_8_;
		isLooking = true;
	}

	public void onUpdateLook() {
		entity.rotationPitch = 0.0F;

		if (isLooking) {
			isLooking = false;
			double d0 = posX - entity.posX;
			double d1 = posY - (entity.posY + entity.getEyeHeight());
			double d2 = posZ - entity.posZ;
			double d3 = MathHelper.sqrt_double(d0 * d0 + d2 * d2);
			float f = (float) (Math.atan2(d2, d0) * 180.0D / Math.PI) - 90.0F;
			float f1 = (float) -(Math.atan2(d1, d3) * 180.0D / Math.PI);
			entity.rotationPitch = updateRotation(entity.rotationPitch, f1, deltaLookPitch);
			entity.rotationYawHead = updateRotation(entity.rotationYawHead, f, deltaLookYaw);
		} else {
			entity.rotationYawHead = updateRotation(entity.rotationYawHead, entity.renderYawOffset, 10.0F);
		}

		float f2 = MathHelper.wrapAngleTo180_float(entity.rotationYawHead - entity.renderYawOffset);

		if (!entity.getNavigator().noPath()) {
			if (f2 < -75.0F) {
				entity.rotationYawHead = entity.renderYawOffset - 75.0F;
			}

			if (f2 > 75.0F) {
				entity.rotationYawHead = entity.renderYawOffset + 75.0F;
			}
		}
	}

	private float updateRotation(float p_75652_1_, float p_75652_2_, float p_75652_3_) {
		float f3 = MathHelper.wrapAngleTo180_float(p_75652_2_ - p_75652_1_);

		if (f3 > p_75652_3_) {
			f3 = p_75652_3_;
		}

		if (f3 < -p_75652_3_) {
			f3 = -p_75652_3_;
		}

		return p_75652_1_ + f3;
	}
}