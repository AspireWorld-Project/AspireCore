package net.minecraft.client.model;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMagmaCube;

@SideOnly(Side.CLIENT)
public class ModelMagmaCube extends ModelBase {
	ModelRenderer[] field_78109_a = new ModelRenderer[8];
	ModelRenderer field_78108_b;
	public ModelMagmaCube() {
		for (int i = 0; i < field_78109_a.length; ++i) {
			byte b0 = 0;
			int j = i;

			if (i == 2) {
				b0 = 24;
				j = 10;
			} else if (i == 3) {
				b0 = 24;
				j = 19;
			}

			field_78109_a[i] = new ModelRenderer(this, b0, j);
			field_78109_a[i].addBox(-4.0F, 16 + i, -4.0F, 8, 1, 8);
		}

		field_78108_b = new ModelRenderer(this, 0, 16);
		field_78108_b.addBox(-2.0F, 18.0F, -2.0F, 4, 4, 4);
	}

	@Override
	public void setLivingAnimations(EntityLivingBase p_78086_1_, float p_78086_2_, float p_78086_3_, float p_78086_4_) {
		EntityMagmaCube entitymagmacube = (EntityMagmaCube) p_78086_1_;
		float f3 = entitymagmacube.prevSquishFactor
				+ (entitymagmacube.squishFactor - entitymagmacube.prevSquishFactor) * p_78086_4_;

		if (f3 < 0.0F) {
			f3 = 0.0F;
		}

		for (int i = 0; i < field_78109_a.length; ++i) {
			field_78109_a[i].rotationPointY = -(4 - i) * f3 * 1.7F;
		}
	}

	@Override
	public void render(Entity p_78088_1_, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_,
			float p_78088_6_, float p_78088_7_) {
		setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, p_78088_7_, p_78088_1_);
		field_78108_b.render(p_78088_7_);

		for (int i = 0; i < field_78109_a.length; ++i) {
			field_78109_a[i].render(p_78088_7_);
		}
	}
}