package net.minecraft.client.model;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class ModelWolf extends ModelBase {
	public ModelRenderer wolfHeadMain;
	public ModelRenderer wolfBody;
	public ModelRenderer wolfLeg1;
	public ModelRenderer wolfLeg2;
	public ModelRenderer wolfLeg3;
	public ModelRenderer wolfLeg4;
	ModelRenderer wolfTail;
	ModelRenderer wolfMane;
	private static final String __OBFID = "CL_00000868";

	public ModelWolf() {
		float f = 0.0F;
		float f1 = 13.5F;
		wolfHeadMain = new ModelRenderer(this, 0, 0);
		wolfHeadMain.addBox(-3.0F, -3.0F, -2.0F, 6, 6, 4, f);
		wolfHeadMain.setRotationPoint(-1.0F, f1, -7.0F);
		wolfBody = new ModelRenderer(this, 18, 14);
		wolfBody.addBox(-4.0F, -2.0F, -3.0F, 6, 9, 6, f);
		wolfBody.setRotationPoint(0.0F, 14.0F, 2.0F);
		wolfMane = new ModelRenderer(this, 21, 0);
		wolfMane.addBox(-4.0F, -3.0F, -3.0F, 8, 6, 7, f);
		wolfMane.setRotationPoint(-1.0F, 14.0F, 2.0F);
		wolfLeg1 = new ModelRenderer(this, 0, 18);
		wolfLeg1.addBox(-1.0F, 0.0F, -1.0F, 2, 8, 2, f);
		wolfLeg1.setRotationPoint(-2.5F, 16.0F, 7.0F);
		wolfLeg2 = new ModelRenderer(this, 0, 18);
		wolfLeg2.addBox(-1.0F, 0.0F, -1.0F, 2, 8, 2, f);
		wolfLeg2.setRotationPoint(0.5F, 16.0F, 7.0F);
		wolfLeg3 = new ModelRenderer(this, 0, 18);
		wolfLeg3.addBox(-1.0F, 0.0F, -1.0F, 2, 8, 2, f);
		wolfLeg3.setRotationPoint(-2.5F, 16.0F, -4.0F);
		wolfLeg4 = new ModelRenderer(this, 0, 18);
		wolfLeg4.addBox(-1.0F, 0.0F, -1.0F, 2, 8, 2, f);
		wolfLeg4.setRotationPoint(0.5F, 16.0F, -4.0F);
		wolfTail = new ModelRenderer(this, 9, 18);
		wolfTail.addBox(-1.0F, 0.0F, -1.0F, 2, 8, 2, f);
		wolfTail.setRotationPoint(-1.0F, 12.0F, 8.0F);
		wolfHeadMain.setTextureOffset(16, 14).addBox(-3.0F, -5.0F, 0.0F, 2, 2, 1, f);
		wolfHeadMain.setTextureOffset(16, 14).addBox(1.0F, -5.0F, 0.0F, 2, 2, 1, f);
		wolfHeadMain.setTextureOffset(0, 10).addBox(-1.5F, 0.0F, -5.0F, 3, 3, 4, f);
	}

	@Override
	public void render(Entity p_78088_1_, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_,
			float p_78088_6_, float p_78088_7_) {
		super.render(p_78088_1_, p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, p_78088_7_);
		setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, p_78088_7_, p_78088_1_);

		if (isChild) {
			float f6 = 2.0F;
			GL11.glPushMatrix();
			GL11.glTranslatef(0.0F, 5.0F * p_78088_7_, 2.0F * p_78088_7_);
			wolfHeadMain.renderWithRotation(p_78088_7_);
			GL11.glPopMatrix();
			GL11.glPushMatrix();
			GL11.glScalef(1.0F / f6, 1.0F / f6, 1.0F / f6);
			GL11.glTranslatef(0.0F, 24.0F * p_78088_7_, 0.0F);
			wolfBody.render(p_78088_7_);
			wolfLeg1.render(p_78088_7_);
			wolfLeg2.render(p_78088_7_);
			wolfLeg3.render(p_78088_7_);
			wolfLeg4.render(p_78088_7_);
			wolfTail.renderWithRotation(p_78088_7_);
			wolfMane.render(p_78088_7_);
			GL11.glPopMatrix();
		} else {
			wolfHeadMain.renderWithRotation(p_78088_7_);
			wolfBody.render(p_78088_7_);
			wolfLeg1.render(p_78088_7_);
			wolfLeg2.render(p_78088_7_);
			wolfLeg3.render(p_78088_7_);
			wolfLeg4.render(p_78088_7_);
			wolfTail.renderWithRotation(p_78088_7_);
			wolfMane.render(p_78088_7_);
		}
	}

	@Override
	public void setLivingAnimations(EntityLivingBase p_78086_1_, float p_78086_2_, float p_78086_3_, float p_78086_4_) {
		EntityWolf entitywolf = (EntityWolf) p_78086_1_;

		if (entitywolf.isAngry()) {
			wolfTail.rotateAngleY = 0.0F;
		} else {
			wolfTail.rotateAngleY = MathHelper.cos(p_78086_2_ * 0.6662F) * 1.4F * p_78086_3_;
		}

		if (entitywolf.isSitting()) {
			wolfMane.setRotationPoint(-1.0F, 16.0F, -3.0F);
			wolfMane.rotateAngleX = (float) Math.PI * 2F / 5F;
			wolfMane.rotateAngleY = 0.0F;
			wolfBody.setRotationPoint(0.0F, 18.0F, 0.0F);
			wolfBody.rotateAngleX = (float) Math.PI / 4F;
			wolfTail.setRotationPoint(-1.0F, 21.0F, 6.0F);
			wolfLeg1.setRotationPoint(-2.5F, 22.0F, 2.0F);
			wolfLeg1.rotateAngleX = (float) Math.PI * 3F / 2F;
			wolfLeg2.setRotationPoint(0.5F, 22.0F, 2.0F);
			wolfLeg2.rotateAngleX = (float) Math.PI * 3F / 2F;
			wolfLeg3.rotateAngleX = 5.811947F;
			wolfLeg3.setRotationPoint(-2.49F, 17.0F, -4.0F);
			wolfLeg4.rotateAngleX = 5.811947F;
			wolfLeg4.setRotationPoint(0.51F, 17.0F, -4.0F);
		} else {
			wolfBody.setRotationPoint(0.0F, 14.0F, 2.0F);
			wolfBody.rotateAngleX = (float) Math.PI / 2F;
			wolfMane.setRotationPoint(-1.0F, 14.0F, -3.0F);
			wolfMane.rotateAngleX = wolfBody.rotateAngleX;
			wolfTail.setRotationPoint(-1.0F, 12.0F, 8.0F);
			wolfLeg1.setRotationPoint(-2.5F, 16.0F, 7.0F);
			wolfLeg2.setRotationPoint(0.5F, 16.0F, 7.0F);
			wolfLeg3.setRotationPoint(-2.5F, 16.0F, -4.0F);
			wolfLeg4.setRotationPoint(0.5F, 16.0F, -4.0F);
			wolfLeg1.rotateAngleX = MathHelper.cos(p_78086_2_ * 0.6662F) * 1.4F * p_78086_3_;
			wolfLeg2.rotateAngleX = MathHelper.cos(p_78086_2_ * 0.6662F + (float) Math.PI) * 1.4F * p_78086_3_;
			wolfLeg3.rotateAngleX = MathHelper.cos(p_78086_2_ * 0.6662F + (float) Math.PI) * 1.4F * p_78086_3_;
			wolfLeg4.rotateAngleX = MathHelper.cos(p_78086_2_ * 0.6662F) * 1.4F * p_78086_3_;
		}

		wolfHeadMain.rotateAngleZ = entitywolf.getInterestedAngle(p_78086_4_)
				+ entitywolf.getShakeAngle(p_78086_4_, 0.0F);
		wolfMane.rotateAngleZ = entitywolf.getShakeAngle(p_78086_4_, -0.08F);
		wolfBody.rotateAngleZ = entitywolf.getShakeAngle(p_78086_4_, -0.16F);
		wolfTail.rotateAngleZ = entitywolf.getShakeAngle(p_78086_4_, -0.2F);
	}

	@Override
	public void setRotationAngles(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_,
			float p_78087_5_, float p_78087_6_, Entity p_78087_7_) {
		super.setRotationAngles(p_78087_1_, p_78087_2_, p_78087_3_, p_78087_4_, p_78087_5_, p_78087_6_, p_78087_7_);
		wolfHeadMain.rotateAngleX = p_78087_5_ / (180F / (float) Math.PI);
		wolfHeadMain.rotateAngleY = p_78087_4_ / (180F / (float) Math.PI);
		wolfTail.rotateAngleX = p_78087_3_;
	}
}