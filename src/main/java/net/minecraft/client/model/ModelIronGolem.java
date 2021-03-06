package net.minecraft.client.model;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityIronGolem;

@SideOnly(Side.CLIENT)
public class ModelIronGolem extends ModelBase {
	public ModelRenderer ironGolemHead;
	public ModelRenderer ironGolemBody;
	public ModelRenderer ironGolemRightArm;
	public ModelRenderer ironGolemLeftArm;
	public ModelRenderer ironGolemLeftLeg;
	public ModelRenderer ironGolemRightLeg;
	public ModelIronGolem() {
		this(0.0F);
	}

	public ModelIronGolem(float p_i1161_1_) {
		this(p_i1161_1_, -7.0F);
	}

	public ModelIronGolem(float p_i1162_1_, float p_i1162_2_) {
		short short1 = 128;
		short short2 = 128;
		ironGolemHead = new ModelRenderer(this).setTextureSize(short1, short2);
		ironGolemHead.setRotationPoint(0.0F, 0.0F + p_i1162_2_, -2.0F);
		ironGolemHead.setTextureOffset(0, 0).addBox(-4.0F, -12.0F, -5.5F, 8, 10, 8, p_i1162_1_);
		ironGolemHead.setTextureOffset(24, 0).addBox(-1.0F, -5.0F, -7.5F, 2, 4, 2, p_i1162_1_);
		ironGolemBody = new ModelRenderer(this).setTextureSize(short1, short2);
		ironGolemBody.setRotationPoint(0.0F, 0.0F + p_i1162_2_, 0.0F);
		ironGolemBody.setTextureOffset(0, 40).addBox(-9.0F, -2.0F, -6.0F, 18, 12, 11, p_i1162_1_);
		ironGolemBody.setTextureOffset(0, 70).addBox(-4.5F, 10.0F, -3.0F, 9, 5, 6, p_i1162_1_ + 0.5F);
		ironGolemRightArm = new ModelRenderer(this).setTextureSize(short1, short2);
		ironGolemRightArm.setRotationPoint(0.0F, -7.0F, 0.0F);
		ironGolemRightArm.setTextureOffset(60, 21).addBox(-13.0F, -2.5F, -3.0F, 4, 30, 6, p_i1162_1_);
		ironGolemLeftArm = new ModelRenderer(this).setTextureSize(short1, short2);
		ironGolemLeftArm.setRotationPoint(0.0F, -7.0F, 0.0F);
		ironGolemLeftArm.setTextureOffset(60, 58).addBox(9.0F, -2.5F, -3.0F, 4, 30, 6, p_i1162_1_);
		ironGolemLeftLeg = new ModelRenderer(this, 0, 22).setTextureSize(short1, short2);
		ironGolemLeftLeg.setRotationPoint(-4.0F, 18.0F + p_i1162_2_, 0.0F);
		ironGolemLeftLeg.setTextureOffset(37, 0).addBox(-3.5F, -3.0F, -3.0F, 6, 16, 5, p_i1162_1_);
		ironGolemRightLeg = new ModelRenderer(this, 0, 22).setTextureSize(short1, short2);
		ironGolemRightLeg.mirror = true;
		ironGolemRightLeg.setTextureOffset(60, 0).setRotationPoint(5.0F, 18.0F + p_i1162_2_, 0.0F);
		ironGolemRightLeg.addBox(-3.5F, -3.0F, -3.0F, 6, 16, 5, p_i1162_1_);
	}

	@Override
	public void render(Entity p_78088_1_, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_,
			float p_78088_6_, float p_78088_7_) {
		setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, p_78088_7_, p_78088_1_);
		ironGolemHead.render(p_78088_7_);
		ironGolemBody.render(p_78088_7_);
		ironGolemLeftLeg.render(p_78088_7_);
		ironGolemRightLeg.render(p_78088_7_);
		ironGolemRightArm.render(p_78088_7_);
		ironGolemLeftArm.render(p_78088_7_);
	}

	@Override
	public void setRotationAngles(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_,
			float p_78087_5_, float p_78087_6_, Entity p_78087_7_) {
		ironGolemHead.rotateAngleY = p_78087_4_ / (180F / (float) Math.PI);
		ironGolemHead.rotateAngleX = p_78087_5_ / (180F / (float) Math.PI);
		ironGolemLeftLeg.rotateAngleX = -1.5F * func_78172_a(p_78087_1_, 13.0F) * p_78087_2_;
		ironGolemRightLeg.rotateAngleX = 1.5F * func_78172_a(p_78087_1_, 13.0F) * p_78087_2_;
		ironGolemLeftLeg.rotateAngleY = 0.0F;
		ironGolemRightLeg.rotateAngleY = 0.0F;
	}

	@Override
	public void setLivingAnimations(EntityLivingBase p_78086_1_, float p_78086_2_, float p_78086_3_, float p_78086_4_) {
		EntityIronGolem entityirongolem = (EntityIronGolem) p_78086_1_;
		int i = entityirongolem.getAttackTimer();

		if (i > 0) {
			ironGolemRightArm.rotateAngleX = -2.0F + 1.5F * func_78172_a(i - p_78086_4_, 10.0F);
			ironGolemLeftArm.rotateAngleX = -2.0F + 1.5F * func_78172_a(i - p_78086_4_, 10.0F);
		} else {
			int j = entityirongolem.getHoldRoseTick();

			if (j > 0) {
				ironGolemRightArm.rotateAngleX = -0.8F + 0.025F * func_78172_a(j, 70.0F);
				ironGolemLeftArm.rotateAngleX = 0.0F;
			} else {
				ironGolemRightArm.rotateAngleX = (-0.2F + 1.5F * func_78172_a(p_78086_2_, 13.0F)) * p_78086_3_;
				ironGolemLeftArm.rotateAngleX = (-0.2F - 1.5F * func_78172_a(p_78086_2_, 13.0F)) * p_78086_3_;
			}
		}
	}

	private float func_78172_a(float p_78172_1_, float p_78172_2_) {
		return (Math.abs(p_78172_1_ % p_78172_2_ - p_78172_2_ * 0.5F) - p_78172_2_ * 0.25F) / (p_78172_2_ * 0.25F);
	}
}