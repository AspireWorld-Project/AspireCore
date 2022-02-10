package net.minecraft.client.model;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

@SideOnly(Side.CLIENT)
public class ModelZombie extends ModelBiped {
	private static final String __OBFID = "CL_00000869";

	public ModelZombie() {
		this(0.0F, false);
	}

	protected ModelZombie(float p_i1167_1_, float p_i1167_2_, int p_i1167_3_, int p_i1167_4_) {
		super(p_i1167_1_, p_i1167_2_, p_i1167_3_, p_i1167_4_);
	}

	public ModelZombie(float p_i1168_1_, boolean p_i1168_2_) {
		super(p_i1168_1_, 0.0F, 64, p_i1168_2_ ? 32 : 64);
	}

	@Override
	public void setRotationAngles(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_,
			float p_78087_5_, float p_78087_6_, Entity p_78087_7_) {
		super.setRotationAngles(p_78087_1_, p_78087_2_, p_78087_3_, p_78087_4_, p_78087_5_, p_78087_6_, p_78087_7_);
		float f6 = MathHelper.sin(onGround * (float) Math.PI);
		float f7 = MathHelper.sin((1.0F - (1.0F - onGround) * (1.0F - onGround)) * (float) Math.PI);
		bipedRightArm.rotateAngleZ = 0.0F;
		bipedLeftArm.rotateAngleZ = 0.0F;
		bipedRightArm.rotateAngleY = -(0.1F - f6 * 0.6F);
		bipedLeftArm.rotateAngleY = 0.1F - f6 * 0.6F;
		bipedRightArm.rotateAngleX = -((float) Math.PI / 2F);
		bipedLeftArm.rotateAngleX = -((float) Math.PI / 2F);
		bipedRightArm.rotateAngleX -= f6 * 1.2F - f7 * 0.4F;
		bipedLeftArm.rotateAngleX -= f6 * 1.2F - f7 * 0.4F;
		bipedRightArm.rotateAngleZ += MathHelper.cos(p_78087_3_ * 0.09F) * 0.05F + 0.05F;
		bipedLeftArm.rotateAngleZ -= MathHelper.cos(p_78087_3_ * 0.09F) * 0.05F + 0.05F;
		bipedRightArm.rotateAngleX += MathHelper.sin(p_78087_3_ * 0.067F) * 0.05F;
		bipedLeftArm.rotateAngleX -= MathHelper.sin(p_78087_3_ * 0.067F) * 0.05F;
	}
}