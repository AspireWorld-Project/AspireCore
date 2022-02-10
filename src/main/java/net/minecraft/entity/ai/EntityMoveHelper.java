package net.minecraft.entity.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.MathHelper;

public class EntityMoveHelper {
	private EntityLiving entity;
	private double posX;
	private double posY;
	private double posZ;
	private double speed;
	private boolean update;
	private static final String __OBFID = "CL_00001573";

	public EntityMoveHelper(EntityLiving p_i1614_1_) {
		entity = p_i1614_1_;
		posX = p_i1614_1_.posX;
		posY = p_i1614_1_.posY;
		posZ = p_i1614_1_.posZ;
	}

	public boolean isUpdating() {
		return update;
	}

	public double getSpeed() {
		return speed;
	}

	public void setMoveTo(double p_75642_1_, double p_75642_3_, double p_75642_5_, double p_75642_7_) {
		posX = p_75642_1_;
		posY = p_75642_3_;
		posZ = p_75642_5_;
		speed = p_75642_7_;
		update = true;
	}

	public void onUpdateMoveHelper() {
		entity.setMoveForward(0.0F);

		if (update) {
			update = false;
			int i = MathHelper.floor_double(entity.boundingBox.minY + 0.5D);
			double d0 = posX - entity.posX;
			double d1 = posZ - entity.posZ;
			double d2 = posY - i;
			double d3 = d0 * d0 + d2 * d2 + d1 * d1;

			if (d3 >= 2.500000277905201E-7D) {
				float f = (float) (Math.atan2(d1, d0) * 180.0D / Math.PI) - 90.0F;
				entity.rotationYaw = limitAngle(entity.rotationYaw, f, 30.0F);
				entity.setAIMoveSpeed((float) (speed
						* entity.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue()));

				if (d2 > 0.0D && d0 * d0 + d1 * d1 < 1.0D) {
					entity.getJumpHelper().setJumping();
				}
			}
		}
	}

	private float limitAngle(float p_75639_1_, float p_75639_2_, float p_75639_3_) {
		float f3 = MathHelper.wrapAngleTo180_float(p_75639_2_ - p_75639_1_);

		if (f3 > p_75639_3_) {
			f3 = p_75639_3_;
		}

		if (f3 < -p_75639_3_) {
			f3 = -p_75639_3_;
		}

		return p_75639_1_ + f3;
	}
}