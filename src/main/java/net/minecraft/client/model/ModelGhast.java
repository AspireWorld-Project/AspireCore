package net.minecraft.client.model;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;

import java.util.Random;

@SideOnly(Side.CLIENT)
public class ModelGhast extends ModelBase {
	ModelRenderer body;
	ModelRenderer[] tentacles = new ModelRenderer[9];
	public ModelGhast() {
		byte b0 = -16;
		body = new ModelRenderer(this, 0, 0);
		body.addBox(-8.0F, -8.0F, -8.0F, 16, 16, 16);
		body.rotationPointY += 24 + b0;
		Random random = new Random(1660L);

		for (int i = 0; i < tentacles.length; ++i) {
			tentacles[i] = new ModelRenderer(this, 0, 0);
			float f = ((i % 3 - i / 3 % 2 * 0.5F + 0.25F) / 2.0F * 2.0F - 1.0F) * 5.0F;
			float f1 = (i / 3 / 2.0F * 2.0F - 1.0F) * 5.0F;
			int j = random.nextInt(7) + 8;
			tentacles[i].addBox(-1.0F, 0.0F, -1.0F, 2, j, 2);
			tentacles[i].rotationPointX = f;
			tentacles[i].rotationPointZ = f1;
			tentacles[i].rotationPointY = 31 + b0;
		}
	}

	@Override
	public void setRotationAngles(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_,
			float p_78087_5_, float p_78087_6_, Entity p_78087_7_) {
		for (int i = 0; i < tentacles.length; ++i) {
			tentacles[i].rotateAngleX = 0.2F * MathHelper.sin(p_78087_3_ * 0.3F + i) + 0.4F;
		}
	}

	@Override
	public void render(Entity p_78088_1_, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_,
			float p_78088_6_, float p_78088_7_) {
		setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, p_78088_7_, p_78088_1_);
		GL11.glPushMatrix();
		GL11.glTranslatef(0.0F, 0.6F, 0.0F);
		body.render(p_78088_7_);
		ModelRenderer[] amodelrenderer = tentacles;
		int i = amodelrenderer.length;

		for (int j = 0; j < i; ++j) {
			ModelRenderer modelrenderer = amodelrenderer[j];
			modelrenderer.render(p_78088_7_);
		}

		GL11.glPopMatrix();
	}
}