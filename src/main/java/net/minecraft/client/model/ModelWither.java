package net.minecraft.client.model;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.util.MathHelper;

@SideOnly(Side.CLIENT)
public class ModelWither extends ModelBase {
	private final ModelRenderer[] field_82905_a;
	private final ModelRenderer[] field_82904_b;
	public ModelWither() {
		textureWidth = 64;
		textureHeight = 64;
		field_82905_a = new ModelRenderer[3];
		field_82905_a[0] = new ModelRenderer(this, 0, 16);
		field_82905_a[0].addBox(-10.0F, 3.9F, -0.5F, 20, 3, 3);
		field_82905_a[1] = new ModelRenderer(this).setTextureSize(textureWidth, textureHeight);
		field_82905_a[1].setRotationPoint(-2.0F, 6.9F, -0.5F);
		field_82905_a[1].setTextureOffset(0, 22).addBox(0.0F, 0.0F, 0.0F, 3, 10, 3);
		field_82905_a[1].setTextureOffset(24, 22).addBox(-4.0F, 1.5F, 0.5F, 11, 2, 2);
		field_82905_a[1].setTextureOffset(24, 22).addBox(-4.0F, 4.0F, 0.5F, 11, 2, 2);
		field_82905_a[1].setTextureOffset(24, 22).addBox(-4.0F, 6.5F, 0.5F, 11, 2, 2);
		field_82905_a[2] = new ModelRenderer(this, 12, 22);
		field_82905_a[2].addBox(0.0F, 0.0F, 0.0F, 3, 6, 3);
		field_82904_b = new ModelRenderer[3];
		field_82904_b[0] = new ModelRenderer(this, 0, 0);
		field_82904_b[0].addBox(-4.0F, -4.0F, -4.0F, 8, 8, 8);
		field_82904_b[1] = new ModelRenderer(this, 32, 0);
		field_82904_b[1].addBox(-4.0F, -4.0F, -4.0F, 6, 6, 6);
		field_82904_b[1].rotationPointX = -8.0F;
		field_82904_b[1].rotationPointY = 4.0F;
		field_82904_b[2] = new ModelRenderer(this, 32, 0);
		field_82904_b[2].addBox(-4.0F, -4.0F, -4.0F, 6, 6, 6);
		field_82904_b[2].rotationPointX = 10.0F;
		field_82904_b[2].rotationPointY = 4.0F;
	}

	public int func_82903_a() {
		return 32;
	}

	@Override
	public void render(Entity p_78088_1_, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_,
			float p_78088_6_, float p_78088_7_) {
		setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, p_78088_7_, p_78088_1_);
		ModelRenderer[] amodelrenderer = field_82904_b;
		int i = amodelrenderer.length;
		int j;
		ModelRenderer modelrenderer;

		for (j = 0; j < i; ++j) {
			modelrenderer = amodelrenderer[j];
			modelrenderer.render(p_78088_7_);
		}

		amodelrenderer = field_82905_a;
		i = amodelrenderer.length;

		for (j = 0; j < i; ++j) {
			modelrenderer = amodelrenderer[j];
			modelrenderer.render(p_78088_7_);
		}
	}

	@Override
	public void setRotationAngles(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_,
			float p_78087_5_, float p_78087_6_, Entity p_78087_7_) {
		float f6 = MathHelper.cos(p_78087_3_ * 0.1F);
		field_82905_a[1].rotateAngleX = (0.065F + 0.05F * f6) * (float) Math.PI;
		field_82905_a[2].setRotationPoint(-2.0F, 6.9F + MathHelper.cos(field_82905_a[1].rotateAngleX) * 10.0F,
				-0.5F + MathHelper.sin(field_82905_a[1].rotateAngleX) * 10.0F);
		field_82905_a[2].rotateAngleX = (0.265F + 0.1F * f6) * (float) Math.PI;
		field_82904_b[0].rotateAngleY = p_78087_4_ / (180F / (float) Math.PI);
		field_82904_b[0].rotateAngleX = p_78087_5_ / (180F / (float) Math.PI);
	}

	@Override
	public void setLivingAnimations(EntityLivingBase p_78086_1_, float p_78086_2_, float p_78086_3_, float p_78086_4_) {
		EntityWither entitywither = (EntityWither) p_78086_1_;

		for (int i = 1; i < 3; ++i) {
			field_82904_b[i].rotateAngleY = (entitywither.func_82207_a(i - 1) - p_78086_1_.renderYawOffset)
					/ (180F / (float) Math.PI);
			field_82904_b[i].rotateAngleX = entitywither.func_82210_r(i - 1) / (180F / (float) Math.PI);
		}
	}
}