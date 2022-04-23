package net.minecraft.client.model;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.Entity;

@SideOnly(Side.CLIENT)
public class ModelMinecart extends ModelBase {
	public ModelRenderer[] sideModels = new ModelRenderer[7];
	public ModelMinecart() {
		sideModels[0] = new ModelRenderer(this, 0, 10);
		sideModels[1] = new ModelRenderer(this, 0, 0);
		sideModels[2] = new ModelRenderer(this, 0, 0);
		sideModels[3] = new ModelRenderer(this, 0, 0);
		sideModels[4] = new ModelRenderer(this, 0, 0);
		sideModels[5] = new ModelRenderer(this, 44, 10);
		byte b0 = 20;
		byte b1 = 8;
		byte b2 = 16;
		byte b3 = 4;
		sideModels[0].addBox(-b0 / 2, -b2 / 2, -1.0F, b0, b2, 2, 0.0F);
		sideModels[0].setRotationPoint(0.0F, b3, 0.0F);
		sideModels[5].addBox(-b0 / 2 + 1, -b2 / 2 + 1, -1.0F, b0 - 2, b2 - 2, 1, 0.0F);
		sideModels[5].setRotationPoint(0.0F, b3, 0.0F);
		sideModels[1].addBox(-b0 / 2 + 2, -b1 - 1, -1.0F, b0 - 4, b1, 2, 0.0F);
		sideModels[1].setRotationPoint(-b0 / 2 + 1, b3, 0.0F);
		sideModels[2].addBox(-b0 / 2 + 2, -b1 - 1, -1.0F, b0 - 4, b1, 2, 0.0F);
		sideModels[2].setRotationPoint(b0 / 2 - 1, b3, 0.0F);
		sideModels[3].addBox(-b0 / 2 + 2, -b1 - 1, -1.0F, b0 - 4, b1, 2, 0.0F);
		sideModels[3].setRotationPoint(0.0F, b3, -b2 / 2 + 1);
		sideModels[4].addBox(-b0 / 2 + 2, -b1 - 1, -1.0F, b0 - 4, b1, 2, 0.0F);
		sideModels[4].setRotationPoint(0.0F, b3, b2 / 2 - 1);
		sideModels[0].rotateAngleX = (float) Math.PI / 2F;
		sideModels[1].rotateAngleY = (float) Math.PI * 3F / 2F;
		sideModels[2].rotateAngleY = (float) Math.PI / 2F;
		sideModels[3].rotateAngleY = (float) Math.PI;
		sideModels[5].rotateAngleX = -((float) Math.PI / 2F);
	}

	@Override
	public void render(Entity p_78088_1_, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_,
			float p_78088_6_, float p_78088_7_) {
		sideModels[5].rotationPointY = 4.0F - p_78088_4_;

		for (int i = 0; i < 6; ++i) {
			sideModels[i].render(p_78088_7_);
		}
	}
}