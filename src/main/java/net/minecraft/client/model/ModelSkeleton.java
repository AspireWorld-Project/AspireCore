package net.minecraft.client.model;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntitySkeleton;

@SideOnly(Side.CLIENT)
public class ModelSkeleton extends ModelZombie {
	public ModelSkeleton() {
		this(0.0F);
	}

	public ModelSkeleton(float p_i1156_1_) {
		super(p_i1156_1_, 0.0F, 64, 32);
		bipedRightArm = new ModelRenderer(this, 40, 16);
		bipedRightArm.addBox(-1.0F, -2.0F, -1.0F, 2, 12, 2, p_i1156_1_);
		bipedRightArm.setRotationPoint(-5.0F, 2.0F, 0.0F);
		bipedLeftArm = new ModelRenderer(this, 40, 16);
		bipedLeftArm.mirror = true;
		bipedLeftArm.addBox(-1.0F, -2.0F, -1.0F, 2, 12, 2, p_i1156_1_);
		bipedLeftArm.setRotationPoint(5.0F, 2.0F, 0.0F);
		bipedRightLeg = new ModelRenderer(this, 0, 16);
		bipedRightLeg.addBox(-1.0F, 0.0F, -1.0F, 2, 12, 2, p_i1156_1_);
		bipedRightLeg.setRotationPoint(-2.0F, 12.0F, 0.0F);
		bipedLeftLeg = new ModelRenderer(this, 0, 16);
		bipedLeftLeg.mirror = true;
		bipedLeftLeg.addBox(-1.0F, 0.0F, -1.0F, 2, 12, 2, p_i1156_1_);
		bipedLeftLeg.setRotationPoint(2.0F, 12.0F, 0.0F);
	}

	@Override
	public void setLivingAnimations(EntityLivingBase p_78086_1_, float p_78086_2_, float p_78086_3_, float p_78086_4_) {
		aimedBow = ((EntitySkeleton) p_78086_1_).getSkeletonType() == 1;
		super.setLivingAnimations(p_78086_1_, p_78086_2_, p_78086_3_, p_78086_4_);
	}

	@Override
	public void setRotationAngles(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_,
			float p_78087_5_, float p_78087_6_, Entity p_78087_7_) {
		super.setRotationAngles(p_78087_1_, p_78087_2_, p_78087_3_, p_78087_4_, p_78087_5_, p_78087_6_, p_78087_7_);
	}
}