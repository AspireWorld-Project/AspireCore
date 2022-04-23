package net.minecraft.client.model;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

@SideOnly(Side.CLIENT)
public class ModelSilverfish extends ModelBase {
	private final ModelRenderer[] silverfishBodyParts = new ModelRenderer[7];
	private final ModelRenderer[] silverfishWings;
	private final float[] field_78170_c = new float[7];
	private static final int[][] silverfishBoxLength = new int[][] { { 3, 2, 2 }, { 4, 3, 2 }, { 6, 4, 3 }, { 3, 3, 3 },
			{ 2, 2, 3 }, { 2, 1, 2 }, { 1, 1, 2 } };
	private static final int[][] silverfishTexturePositions = new int[][] { { 0, 0 }, { 0, 4 }, { 0, 9 }, { 0, 16 },
			{ 0, 22 }, { 11, 0 }, { 13, 4 } };
	public ModelSilverfish() {
		float f = -3.5F;

		for (int i = 0; i < silverfishBodyParts.length; ++i) {
			silverfishBodyParts[i] = new ModelRenderer(this, silverfishTexturePositions[i][0],
					silverfishTexturePositions[i][1]);
			silverfishBodyParts[i].addBox(silverfishBoxLength[i][0] * -0.5F, 0.0F, silverfishBoxLength[i][2] * -0.5F,
					silverfishBoxLength[i][0], silverfishBoxLength[i][1], silverfishBoxLength[i][2]);
			silverfishBodyParts[i].setRotationPoint(0.0F, 24 - silverfishBoxLength[i][1], f);
			field_78170_c[i] = f;

			if (i < silverfishBodyParts.length - 1) {
				f += (silverfishBoxLength[i][2] + silverfishBoxLength[i + 1][2]) * 0.5F;
			}
		}

		silverfishWings = new ModelRenderer[3];
		silverfishWings[0] = new ModelRenderer(this, 20, 0);
		silverfishWings[0].addBox(-5.0F, 0.0F, silverfishBoxLength[2][2] * -0.5F, 10, 8, silverfishBoxLength[2][2]);
		silverfishWings[0].setRotationPoint(0.0F, 16.0F, field_78170_c[2]);
		silverfishWings[1] = new ModelRenderer(this, 20, 11);
		silverfishWings[1].addBox(-3.0F, 0.0F, silverfishBoxLength[4][2] * -0.5F, 6, 4, silverfishBoxLength[4][2]);
		silverfishWings[1].setRotationPoint(0.0F, 20.0F, field_78170_c[4]);
		silverfishWings[2] = new ModelRenderer(this, 20, 18);
		silverfishWings[2].addBox(-3.0F, 0.0F, silverfishBoxLength[4][2] * -0.5F, 6, 5, silverfishBoxLength[1][2]);
		silverfishWings[2].setRotationPoint(0.0F, 19.0F, field_78170_c[1]);
	}

	@Override
	public void render(Entity p_78088_1_, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_,
			float p_78088_6_, float p_78088_7_) {
		setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, p_78088_7_, p_78088_1_);
		int i;

		for (i = 0; i < silverfishBodyParts.length; ++i) {
			silverfishBodyParts[i].render(p_78088_7_);
		}

		for (i = 0; i < silverfishWings.length; ++i) {
			silverfishWings[i].render(p_78088_7_);
		}
	}

	@Override
	public void setRotationAngles(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_,
			float p_78087_5_, float p_78087_6_, Entity p_78087_7_) {
		for (int i = 0; i < silverfishBodyParts.length; ++i) {
			silverfishBodyParts[i].rotateAngleY = MathHelper.cos(p_78087_3_ * 0.9F + i * 0.15F * (float) Math.PI)
					* (float) Math.PI * 0.05F * (1 + Math.abs(i - 2));
			silverfishBodyParts[i].rotationPointX = MathHelper.sin(p_78087_3_ * 0.9F + i * 0.15F * (float) Math.PI)
					* (float) Math.PI * 0.2F * Math.abs(i - 2);
		}

		silverfishWings[0].rotateAngleY = silverfishBodyParts[2].rotateAngleY;
		silverfishWings[1].rotateAngleY = silverfishBodyParts[4].rotateAngleY;
		silverfishWings[1].rotationPointX = silverfishBodyParts[4].rotationPointX;
		silverfishWings[2].rotateAngleY = silverfishBodyParts[1].rotateAngleY;
		silverfishWings[2].rotationPointX = silverfishBodyParts[1].rotationPointX;
	}
}