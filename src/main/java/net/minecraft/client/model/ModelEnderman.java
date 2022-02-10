package net.minecraft.client.model;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.Entity;

@SideOnly(Side.CLIENT)
public class ModelEnderman extends ModelBiped {
	public boolean isCarrying;
	public boolean isAttacking;
	private static final String __OBFID = "CL_00000838";

	public ModelEnderman() {
		super(0.0F, -14.0F, 64, 32);
		float f = -14.0F;
		float f1 = 0.0F;
		bipedHeadwear = new ModelRenderer(this, 0, 16);
		bipedHeadwear.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, f1 - 0.5F);
		bipedHeadwear.setRotationPoint(0.0F, 0.0F + f, 0.0F);
		bipedBody = new ModelRenderer(this, 32, 16);
		bipedBody.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, f1);
		bipedBody.setRotationPoint(0.0F, 0.0F + f, 0.0F);
		bipedRightArm = new ModelRenderer(this, 56, 0);
		bipedRightArm.addBox(-1.0F, -2.0F, -1.0F, 2, 30, 2, f1);
		bipedRightArm.setRotationPoint(-3.0F, 2.0F + f, 0.0F);
		bipedLeftArm = new ModelRenderer(this, 56, 0);
		bipedLeftArm.mirror = true;
		bipedLeftArm.addBox(-1.0F, -2.0F, -1.0F, 2, 30, 2, f1);
		bipedLeftArm.setRotationPoint(5.0F, 2.0F + f, 0.0F);
		bipedRightLeg = new ModelRenderer(this, 56, 0);
		bipedRightLeg.addBox(-1.0F, 0.0F, -1.0F, 2, 30, 2, f1);
		bipedRightLeg.setRotationPoint(-2.0F, 12.0F + f, 0.0F);
		bipedLeftLeg = new ModelRenderer(this, 56, 0);
		bipedLeftLeg.mirror = true;
		bipedLeftLeg.addBox(-1.0F, 0.0F, -1.0F, 2, 30, 2, f1);
		bipedLeftLeg.setRotationPoint(2.0F, 12.0F + f, 0.0F);
	}

	@Override
	public void setRotationAngles(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_,
			float p_78087_5_, float p_78087_6_, Entity p_78087_7_) {
		super.setRotationAngles(p_78087_1_, p_78087_2_, p_78087_3_, p_78087_4_, p_78087_5_, p_78087_6_, p_78087_7_);
		bipedHead.showModel = true;
		float f6 = -14.0F;
		bipedBody.rotateAngleX = 0.0F;
		bipedBody.rotationPointY = f6;
		bipedBody.rotationPointZ = -0.0F;
		bipedRightLeg.rotateAngleX -= 0.0F;
		bipedLeftLeg.rotateAngleX -= 0.0F;
		bipedRightArm.rotateAngleX = (float) (bipedRightArm.rotateAngleX * 0.5D);
		bipedLeftArm.rotateAngleX = (float) (bipedLeftArm.rotateAngleX * 0.5D);
		bipedRightLeg.rotateAngleX = (float) (bipedRightLeg.rotateAngleX * 0.5D);
		bipedLeftLeg.rotateAngleX = (float) (bipedLeftLeg.rotateAngleX * 0.5D);
		float f7 = 0.4F;

		if (bipedRightArm.rotateAngleX > f7) {
			bipedRightArm.rotateAngleX = f7;
		}

		if (bipedLeftArm.rotateAngleX > f7) {
			bipedLeftArm.rotateAngleX = f7;
		}

		if (bipedRightArm.rotateAngleX < -f7) {
			bipedRightArm.rotateAngleX = -f7;
		}

		if (bipedLeftArm.rotateAngleX < -f7) {
			bipedLeftArm.rotateAngleX = -f7;
		}

		if (bipedRightLeg.rotateAngleX > f7) {
			bipedRightLeg.rotateAngleX = f7;
		}

		if (bipedLeftLeg.rotateAngleX > f7) {
			bipedLeftLeg.rotateAngleX = f7;
		}

		if (bipedRightLeg.rotateAngleX < -f7) {
			bipedRightLeg.rotateAngleX = -f7;
		}

		if (bipedLeftLeg.rotateAngleX < -f7) {
			bipedLeftLeg.rotateAngleX = -f7;
		}

		if (isCarrying) {
			bipedRightArm.rotateAngleX = -0.5F;
			bipedLeftArm.rotateAngleX = -0.5F;
			bipedRightArm.rotateAngleZ = 0.05F;
			bipedLeftArm.rotateAngleZ = -0.05F;
		}

		bipedRightArm.rotationPointZ = 0.0F;
		bipedLeftArm.rotationPointZ = 0.0F;
		bipedRightLeg.rotationPointZ = 0.0F;
		bipedLeftLeg.rotationPointZ = 0.0F;
		bipedRightLeg.rotationPointY = 9.0F + f6;
		bipedLeftLeg.rotationPointY = 9.0F + f6;
		bipedHead.rotationPointZ = -0.0F;
		bipedHead.rotationPointY = f6 + 1.0F;
		bipedHeadwear.rotationPointX = bipedHead.rotationPointX;
		bipedHeadwear.rotationPointY = bipedHead.rotationPointY;
		bipedHeadwear.rotationPointZ = bipedHead.rotationPointZ;
		bipedHeadwear.rotateAngleX = bipedHead.rotateAngleX;
		bipedHeadwear.rotateAngleY = bipedHead.rotateAngleY;
		bipedHeadwear.rotateAngleZ = bipedHead.rotateAngleZ;

		if (isAttacking) {
			float f8 = 1.0F;
			bipedHead.rotationPointY -= f8 * 5.0F;
		}
	}
}