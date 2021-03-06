package net.minecraft.client.model;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.Entity;

@SideOnly(Side.CLIENT)
public class ModelSquid extends ModelBase {
	ModelRenderer squidBody;
	ModelRenderer[] squidTentacles = new ModelRenderer[8];
	public ModelSquid() {
		byte b0 = -16;
		squidBody = new ModelRenderer(this, 0, 0);
		squidBody.addBox(-6.0F, -8.0F, -6.0F, 12, 16, 12);
		squidBody.rotationPointY += 24 + b0;

		for (int i = 0; i < squidTentacles.length; ++i) {
			squidTentacles[i] = new ModelRenderer(this, 48, 0);
			double d0 = i * Math.PI * 2.0D / squidTentacles.length;
			float f = (float) Math.cos(d0) * 5.0F;
			float f1 = (float) Math.sin(d0) * 5.0F;
			squidTentacles[i].addBox(-1.0F, 0.0F, -1.0F, 2, 18, 2);
			squidTentacles[i].rotationPointX = f;
			squidTentacles[i].rotationPointZ = f1;
			squidTentacles[i].rotationPointY = 31 + b0;
			d0 = i * Math.PI * -2.0D / squidTentacles.length + Math.PI / 2D;
			squidTentacles[i].rotateAngleY = (float) d0;
		}
	}

	@Override
	public void setRotationAngles(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_,
			float p_78087_5_, float p_78087_6_, Entity p_78087_7_) {
		ModelRenderer[] amodelrenderer = squidTentacles;
		int i = amodelrenderer.length;

		for (int j = 0; j < i; ++j) {
			ModelRenderer modelrenderer = amodelrenderer[j];
			modelrenderer.rotateAngleX = p_78087_3_;
		}
	}

	@Override
	public void render(Entity p_78088_1_, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_,
			float p_78088_6_, float p_78088_7_) {
		setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, p_78088_7_, p_78088_1_);
		squidBody.render(p_78088_7_);

		for (int i = 0; i < squidTentacles.length; ++i) {
			squidTentacles[i].render(p_78088_7_);
		}
	}
}