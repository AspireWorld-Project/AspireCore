package net.minecraft.client.model;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

@SideOnly(Side.CLIENT)
public class ModelSpider extends ModelBase {
	public ModelRenderer spiderHead;
	public ModelRenderer spiderNeck;
	public ModelRenderer spiderBody;
	public ModelRenderer spiderLeg1;
	public ModelRenderer spiderLeg2;
	public ModelRenderer spiderLeg3;
	public ModelRenderer spiderLeg4;
	public ModelRenderer spiderLeg5;
	public ModelRenderer spiderLeg6;
	public ModelRenderer spiderLeg7;
	public ModelRenderer spiderLeg8;
	public ModelSpider() {
		float f = 0.0F;
		byte b0 = 15;
		spiderHead = new ModelRenderer(this, 32, 4);
		spiderHead.addBox(-4.0F, -4.0F, -8.0F, 8, 8, 8, f);
		spiderHead.setRotationPoint(0.0F, b0, -3.0F);
		spiderNeck = new ModelRenderer(this, 0, 0);
		spiderNeck.addBox(-3.0F, -3.0F, -3.0F, 6, 6, 6, f);
		spiderNeck.setRotationPoint(0.0F, b0, 0.0F);
		spiderBody = new ModelRenderer(this, 0, 12);
		spiderBody.addBox(-5.0F, -4.0F, -6.0F, 10, 8, 12, f);
		spiderBody.setRotationPoint(0.0F, b0, 9.0F);
		spiderLeg1 = new ModelRenderer(this, 18, 0);
		spiderLeg1.addBox(-15.0F, -1.0F, -1.0F, 16, 2, 2, f);
		spiderLeg1.setRotationPoint(-4.0F, b0, 2.0F);
		spiderLeg2 = new ModelRenderer(this, 18, 0);
		spiderLeg2.addBox(-1.0F, -1.0F, -1.0F, 16, 2, 2, f);
		spiderLeg2.setRotationPoint(4.0F, b0, 2.0F);
		spiderLeg3 = new ModelRenderer(this, 18, 0);
		spiderLeg3.addBox(-15.0F, -1.0F, -1.0F, 16, 2, 2, f);
		spiderLeg3.setRotationPoint(-4.0F, b0, 1.0F);
		spiderLeg4 = new ModelRenderer(this, 18, 0);
		spiderLeg4.addBox(-1.0F, -1.0F, -1.0F, 16, 2, 2, f);
		spiderLeg4.setRotationPoint(4.0F, b0, 1.0F);
		spiderLeg5 = new ModelRenderer(this, 18, 0);
		spiderLeg5.addBox(-15.0F, -1.0F, -1.0F, 16, 2, 2, f);
		spiderLeg5.setRotationPoint(-4.0F, b0, 0.0F);
		spiderLeg6 = new ModelRenderer(this, 18, 0);
		spiderLeg6.addBox(-1.0F, -1.0F, -1.0F, 16, 2, 2, f);
		spiderLeg6.setRotationPoint(4.0F, b0, 0.0F);
		spiderLeg7 = new ModelRenderer(this, 18, 0);
		spiderLeg7.addBox(-15.0F, -1.0F, -1.0F, 16, 2, 2, f);
		spiderLeg7.setRotationPoint(-4.0F, b0, -1.0F);
		spiderLeg8 = new ModelRenderer(this, 18, 0);
		spiderLeg8.addBox(-1.0F, -1.0F, -1.0F, 16, 2, 2, f);
		spiderLeg8.setRotationPoint(4.0F, b0, -1.0F);
	}

	@Override
	public void render(Entity p_78088_1_, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_,
			float p_78088_6_, float p_78088_7_) {
		setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, p_78088_7_, p_78088_1_);
		spiderHead.render(p_78088_7_);
		spiderNeck.render(p_78088_7_);
		spiderBody.render(p_78088_7_);
		spiderLeg1.render(p_78088_7_);
		spiderLeg2.render(p_78088_7_);
		spiderLeg3.render(p_78088_7_);
		spiderLeg4.render(p_78088_7_);
		spiderLeg5.render(p_78088_7_);
		spiderLeg6.render(p_78088_7_);
		spiderLeg7.render(p_78088_7_);
		spiderLeg8.render(p_78088_7_);
	}

	@Override
	public void setRotationAngles(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_,
			float p_78087_5_, float p_78087_6_, Entity p_78087_7_) {
		spiderHead.rotateAngleY = p_78087_4_ / (180F / (float) Math.PI);
		spiderHead.rotateAngleX = p_78087_5_ / (180F / (float) Math.PI);
		float f6 = (float) Math.PI / 4F;
		spiderLeg1.rotateAngleZ = -f6;
		spiderLeg2.rotateAngleZ = f6;
		spiderLeg3.rotateAngleZ = -f6 * 0.74F;
		spiderLeg4.rotateAngleZ = f6 * 0.74F;
		spiderLeg5.rotateAngleZ = -f6 * 0.74F;
		spiderLeg6.rotateAngleZ = f6 * 0.74F;
		spiderLeg7.rotateAngleZ = -f6;
		spiderLeg8.rotateAngleZ = f6;
		float f7 = -0.0F;
		float f8 = 0.3926991F;
		spiderLeg1.rotateAngleY = f8 * 2.0F + f7;
		spiderLeg2.rotateAngleY = -f8 * 2.0F - f7;
		spiderLeg3.rotateAngleY = f8 * 1.0F + f7;
		spiderLeg4.rotateAngleY = -f8 * 1.0F - f7;
		spiderLeg5.rotateAngleY = -f8 * 1.0F + f7;
		spiderLeg6.rotateAngleY = f8 * 1.0F - f7;
		spiderLeg7.rotateAngleY = -f8 * 2.0F + f7;
		spiderLeg8.rotateAngleY = f8 * 2.0F - f7;
		float f9 = -(MathHelper.cos(p_78087_1_ * 0.6662F * 2.0F + 0.0F) * 0.4F) * p_78087_2_;
		float f10 = -(MathHelper.cos(p_78087_1_ * 0.6662F * 2.0F + (float) Math.PI) * 0.4F) * p_78087_2_;
		float f11 = -(MathHelper.cos(p_78087_1_ * 0.6662F * 2.0F + (float) Math.PI / 2F) * 0.4F) * p_78087_2_;
		float f12 = -(MathHelper.cos(p_78087_1_ * 0.6662F * 2.0F + (float) Math.PI * 3F / 2F) * 0.4F) * p_78087_2_;
		float f13 = Math.abs(MathHelper.sin(p_78087_1_ * 0.6662F + 0.0F) * 0.4F) * p_78087_2_;
		float f14 = Math.abs(MathHelper.sin(p_78087_1_ * 0.6662F + (float) Math.PI) * 0.4F) * p_78087_2_;
		float f15 = Math.abs(MathHelper.sin(p_78087_1_ * 0.6662F + (float) Math.PI / 2F) * 0.4F) * p_78087_2_;
		float f16 = Math.abs(MathHelper.sin(p_78087_1_ * 0.6662F + (float) Math.PI * 3F / 2F) * 0.4F) * p_78087_2_;
		spiderLeg1.rotateAngleY += f9;
		spiderLeg2.rotateAngleY += -f9;
		spiderLeg3.rotateAngleY += f10;
		spiderLeg4.rotateAngleY += -f10;
		spiderLeg5.rotateAngleY += f11;
		spiderLeg6.rotateAngleY += -f11;
		spiderLeg7.rotateAngleY += f12;
		spiderLeg8.rotateAngleY += -f12;
		spiderLeg1.rotateAngleZ += f13;
		spiderLeg2.rotateAngleZ += -f13;
		spiderLeg3.rotateAngleZ += f14;
		spiderLeg4.rotateAngleZ += -f14;
		spiderLeg5.rotateAngleZ += f15;
		spiderLeg6.rotateAngleZ += -f15;
		spiderLeg7.rotateAngleZ += f16;
		spiderLeg8.rotateAngleZ += -f16;
	}
}