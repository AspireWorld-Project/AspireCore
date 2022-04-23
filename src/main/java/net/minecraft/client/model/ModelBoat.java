package net.minecraft.client.model;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.Entity;

@SideOnly(Side.CLIENT)
public class ModelBoat extends ModelBase {
	public ModelRenderer[] boatSides = new ModelRenderer[5];
	public ModelBoat() {
		boatSides[0] = new ModelRenderer(this, 0, 8);
		boatSides[1] = new ModelRenderer(this, 0, 0);
		boatSides[2] = new ModelRenderer(this, 0, 0);
		boatSides[3] = new ModelRenderer(this, 0, 0);
		boatSides[4] = new ModelRenderer(this, 0, 0);
		byte b0 = 24;
		byte b1 = 6;
		byte b2 = 20;
		byte b3 = 4;
		boatSides[0].addBox(-b0 / 2, -b2 / 2 + 2, -3.0F, b0, b2 - 4, 4, 0.0F);
		boatSides[0].setRotationPoint(0.0F, b3, 0.0F);
		boatSides[1].addBox(-b0 / 2 + 2, -b1 - 1, -1.0F, b0 - 4, b1, 2, 0.0F);
		boatSides[1].setRotationPoint(-b0 / 2 + 1, b3, 0.0F);
		boatSides[2].addBox(-b0 / 2 + 2, -b1 - 1, -1.0F, b0 - 4, b1, 2, 0.0F);
		boatSides[2].setRotationPoint(b0 / 2 - 1, b3, 0.0F);
		boatSides[3].addBox(-b0 / 2 + 2, -b1 - 1, -1.0F, b0 - 4, b1, 2, 0.0F);
		boatSides[3].setRotationPoint(0.0F, b3, -b2 / 2 + 1);
		boatSides[4].addBox(-b0 / 2 + 2, -b1 - 1, -1.0F, b0 - 4, b1, 2, 0.0F);
		boatSides[4].setRotationPoint(0.0F, b3, b2 / 2 - 1);
		boatSides[0].rotateAngleX = (float) Math.PI / 2F;
		boatSides[1].rotateAngleY = (float) Math.PI * 3F / 2F;
		boatSides[2].rotateAngleY = (float) Math.PI / 2F;
		boatSides[3].rotateAngleY = (float) Math.PI;
	}

	@Override
	public void render(Entity p_78088_1_, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_,
			float p_78088_6_, float p_78088_7_) {
		for (int i = 0; i < 5; ++i) {
			boatSides[i].render(p_78088_7_);
		}
	}
}