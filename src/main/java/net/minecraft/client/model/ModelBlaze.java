package net.minecraft.client.model;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

@SideOnly(Side.CLIENT)
public class ModelBlaze extends ModelBase {
	private ModelRenderer[] blazeSticks = new ModelRenderer[12];
	private ModelRenderer blazeHead;
	private static final String __OBFID = "CL_00000831";

	public ModelBlaze() {
		for (int i = 0; i < blazeSticks.length; ++i) {
			blazeSticks[i] = new ModelRenderer(this, 0, 16);
			blazeSticks[i].addBox(0.0F, 0.0F, 0.0F, 2, 8, 2);
		}

		blazeHead = new ModelRenderer(this, 0, 0);
		blazeHead.addBox(-4.0F, -4.0F, -4.0F, 8, 8, 8);
	}

	public int func_78104_a() {
		return 8;
	}

	@Override
	public void render(Entity p_78088_1_, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_,
			float p_78088_6_, float p_78088_7_) {
		setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, p_78088_7_, p_78088_1_);
		blazeHead.render(p_78088_7_);

		for (int i = 0; i < blazeSticks.length; ++i) {
			blazeSticks[i].render(p_78088_7_);
		}
	}

	@Override
	public void setRotationAngles(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_,
			float p_78087_5_, float p_78087_6_, Entity p_78087_7_) {
		float f6 = p_78087_3_ * (float) Math.PI * -0.1F;
		int i;

		for (i = 0; i < 4; ++i) {
			blazeSticks[i].rotationPointY = -2.0F + MathHelper.cos((i * 2 + p_78087_3_) * 0.25F);
			blazeSticks[i].rotationPointX = MathHelper.cos(f6) * 9.0F;
			blazeSticks[i].rotationPointZ = MathHelper.sin(f6) * 9.0F;
			++f6;
		}

		f6 = (float) Math.PI / 4F + p_78087_3_ * (float) Math.PI * 0.03F;

		for (i = 4; i < 8; ++i) {
			blazeSticks[i].rotationPointY = 2.0F + MathHelper.cos((i * 2 + p_78087_3_) * 0.25F);
			blazeSticks[i].rotationPointX = MathHelper.cos(f6) * 7.0F;
			blazeSticks[i].rotationPointZ = MathHelper.sin(f6) * 7.0F;
			++f6;
		}

		f6 = 0.47123894F + p_78087_3_ * (float) Math.PI * -0.05F;

		for (i = 8; i < 12; ++i) {
			blazeSticks[i].rotationPointY = 11.0F + MathHelper.cos((i * 1.5F + p_78087_3_) * 0.5F);
			blazeSticks[i].rotationPointX = MathHelper.cos(f6) * 5.0F;
			blazeSticks[i].rotationPointZ = MathHelper.sin(f6) * 5.0F;
			++f6;
		}

		blazeHead.rotateAngleY = p_78087_4_ / (180F / (float) Math.PI);
		blazeHead.rotateAngleX = p_78087_5_ / (180F / (float) Math.PI);
	}
}