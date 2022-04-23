package net.minecraft.client.model;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class ModelEnderCrystal extends ModelBase {
	private ModelRenderer cube;
	private ModelRenderer glass = new ModelRenderer(this, "glass");
	private ModelRenderer base;
	private static final String __OBFID = "CL_00000871";

	public ModelEnderCrystal(float p_i1170_1_, boolean p_i1170_2_) {
		glass.setTextureOffset(0, 0).addBox(-4.0F, -4.0F, -4.0F, 8, 8, 8);
		cube = new ModelRenderer(this, "cube");
		cube.setTextureOffset(32, 0).addBox(-4.0F, -4.0F, -4.0F, 8, 8, 8);

		if (p_i1170_2_) {
			base = new ModelRenderer(this, "base");
			base.setTextureOffset(0, 16).addBox(-6.0F, 0.0F, -6.0F, 12, 4, 12);
		}
	}

	@Override
	public void render(Entity p_78088_1_, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_,
			float p_78088_6_, float p_78088_7_) {
		GL11.glPushMatrix();
		GL11.glScalef(2.0F, 2.0F, 2.0F);
		GL11.glTranslatef(0.0F, -0.5F, 0.0F);

		if (base != null) {
			base.render(p_78088_7_);
		}

		GL11.glRotatef(p_78088_3_, 0.0F, 1.0F, 0.0F);
		GL11.glTranslatef(0.0F, 0.8F + p_78088_4_, 0.0F);
		GL11.glRotatef(60.0F, 0.7071F, 0.0F, 0.7071F);
		glass.render(p_78088_7_);
		float f6 = 0.875F;
		GL11.glScalef(f6, f6, f6);
		GL11.glRotatef(60.0F, 0.7071F, 0.0F, 0.7071F);
		GL11.glRotatef(p_78088_3_, 0.0F, 1.0F, 0.0F);
		glass.render(p_78088_7_);
		GL11.glScalef(f6, f6, f6);
		GL11.glRotatef(60.0F, 0.7071F, 0.0F, 0.7071F);
		GL11.glRotatef(p_78088_3_, 0.0F, 1.0F, 0.0F);
		cube.render(p_78088_7_);
		GL11.glPopMatrix();
	}
}