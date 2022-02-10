package net.minecraft.entity;

import net.minecraft.util.MathHelper;

public class EntityBodyHelper {
	private EntityLivingBase theLiving;
	private int field_75666_b;
	private float field_75667_c;
	private static final String __OBFID = "CL_00001570";

	public EntityBodyHelper(EntityLivingBase p_i1611_1_) {
		theLiving = p_i1611_1_;
	}

	public void func_75664_a() {
		double d0 = theLiving.posX - theLiving.prevPosX;
		double d1 = theLiving.posZ - theLiving.prevPosZ;

		if (d0 * d0 + d1 * d1 > 2.500000277905201E-7D) {
			theLiving.renderYawOffset = theLiving.rotationYaw;
			theLiving.rotationYawHead = func_75665_a(theLiving.renderYawOffset, theLiving.rotationYawHead, 75.0F);
			field_75667_c = theLiving.rotationYawHead;
			field_75666_b = 0;
		} else {
			float f = 75.0F;

			if (Math.abs(theLiving.rotationYawHead - field_75667_c) > 15.0F) {
				field_75666_b = 0;
				field_75667_c = theLiving.rotationYawHead;
			} else {
				++field_75666_b;
				if (field_75666_b > 10) {
					f = Math.max(1.0F - (field_75666_b - 10) / 10.0F, 0.0F) * 75.0F;
				}
			}

			theLiving.renderYawOffset = func_75665_a(theLiving.rotationYawHead, theLiving.renderYawOffset, f);
		}
	}

	private float func_75665_a(float p_75665_1_, float p_75665_2_, float p_75665_3_) {
		float f3 = MathHelper.wrapAngleTo180_float(p_75665_1_ - p_75665_2_);

		if (f3 < -p_75665_3_) {
			f3 = -p_75665_3_;
		}

		if (f3 >= p_75665_3_) {
			f3 = p_75665_3_;
		}

		return p_75665_1_ - f3;
	}
}