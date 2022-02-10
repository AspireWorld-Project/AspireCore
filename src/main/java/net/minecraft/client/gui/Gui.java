package net.minecraft.client.gui;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class Gui {
	public static final ResourceLocation optionsBackground = new ResourceLocation(
			"textures/gui/options_background.png");
	public static final ResourceLocation statIcons = new ResourceLocation("textures/gui/container/stats_icons.png");
	public static final ResourceLocation icons = new ResourceLocation("textures/gui/icons.png");
	protected float zLevel;
	private static final String __OBFID = "CL_00000662";

	protected void drawHorizontalLine(int p_73730_1_, int p_73730_2_, int p_73730_3_, int p_73730_4_) {
		if (p_73730_2_ < p_73730_1_) {
			int i1 = p_73730_1_;
			p_73730_1_ = p_73730_2_;
			p_73730_2_ = i1;
		}

		drawRect(p_73730_1_, p_73730_3_, p_73730_2_ + 1, p_73730_3_ + 1, p_73730_4_);
	}

	protected void drawVerticalLine(int p_73728_1_, int p_73728_2_, int p_73728_3_, int p_73728_4_) {
		if (p_73728_3_ < p_73728_2_) {
			int i1 = p_73728_2_;
			p_73728_2_ = p_73728_3_;
			p_73728_3_ = i1;
		}

		drawRect(p_73728_1_, p_73728_2_ + 1, p_73728_1_ + 1, p_73728_3_, p_73728_4_);
	}

	public static void drawRect(int p_73734_0_, int p_73734_1_, int p_73734_2_, int p_73734_3_, int p_73734_4_) {
		int j1;

		if (p_73734_0_ < p_73734_2_) {
			j1 = p_73734_0_;
			p_73734_0_ = p_73734_2_;
			p_73734_2_ = j1;
		}

		if (p_73734_1_ < p_73734_3_) {
			j1 = p_73734_1_;
			p_73734_1_ = p_73734_3_;
			p_73734_3_ = j1;
		}

		float f3 = (p_73734_4_ >> 24 & 255) / 255.0F;
		float f = (p_73734_4_ >> 16 & 255) / 255.0F;
		float f1 = (p_73734_4_ >> 8 & 255) / 255.0F;
		float f2 = (p_73734_4_ & 255) / 255.0F;
		Tessellator tessellator = Tessellator.instance;
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		OpenGlHelper.glBlendFunc(770, 771, 1, 0);
		GL11.glColor4f(f, f1, f2, f3);
		tessellator.startDrawingQuads();
		tessellator.addVertex(p_73734_0_, p_73734_3_, 0.0D);
		tessellator.addVertex(p_73734_2_, p_73734_3_, 0.0D);
		tessellator.addVertex(p_73734_2_, p_73734_1_, 0.0D);
		tessellator.addVertex(p_73734_0_, p_73734_1_, 0.0D);
		tessellator.draw();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
	}

	protected void drawGradientRect(int p_73733_1_, int p_73733_2_, int p_73733_3_, int p_73733_4_, int p_73733_5_,
			int p_73733_6_) {
		float f = (p_73733_5_ >> 24 & 255) / 255.0F;
		float f1 = (p_73733_5_ >> 16 & 255) / 255.0F;
		float f2 = (p_73733_5_ >> 8 & 255) / 255.0F;
		float f3 = (p_73733_5_ & 255) / 255.0F;
		float f4 = (p_73733_6_ >> 24 & 255) / 255.0F;
		float f5 = (p_73733_6_ >> 16 & 255) / 255.0F;
		float f6 = (p_73733_6_ >> 8 & 255) / 255.0F;
		float f7 = (p_73733_6_ & 255) / 255.0F;
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		OpenGlHelper.glBlendFunc(770, 771, 1, 0);
		GL11.glShadeModel(GL11.GL_SMOOTH);
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		tessellator.setColorRGBA_F(f1, f2, f3, f);
		tessellator.addVertex(p_73733_3_, p_73733_2_, zLevel);
		tessellator.addVertex(p_73733_1_, p_73733_2_, zLevel);
		tessellator.setColorRGBA_F(f5, f6, f7, f4);
		tessellator.addVertex(p_73733_1_, p_73733_4_, zLevel);
		tessellator.addVertex(p_73733_3_, p_73733_4_, zLevel);
		tessellator.draw();
		GL11.glShadeModel(GL11.GL_FLAT);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}

	public void drawCenteredString(FontRenderer p_73732_1_, String p_73732_2_, int p_73732_3_, int p_73732_4_,
			int p_73732_5_) {
		p_73732_1_.drawStringWithShadow(p_73732_2_, p_73732_3_ - p_73732_1_.getStringWidth(p_73732_2_) / 2, p_73732_4_,
				p_73732_5_);
	}

	public void drawString(FontRenderer p_73731_1_, String p_73731_2_, int p_73731_3_, int p_73731_4_, int p_73731_5_) {
		p_73731_1_.drawStringWithShadow(p_73731_2_, p_73731_3_, p_73731_4_, p_73731_5_);
	}

	public void drawTexturedModalRect(int p_73729_1_, int p_73729_2_, int p_73729_3_, int p_73729_4_, int p_73729_5_,
			int p_73729_6_) {
		float f = 0.00390625F;
		float f1 = 0.00390625F;
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		tessellator.addVertexWithUV(p_73729_1_ + 0, p_73729_2_ + p_73729_6_, zLevel, (p_73729_3_ + 0) * f,
				(p_73729_4_ + p_73729_6_) * f1);
		tessellator.addVertexWithUV(p_73729_1_ + p_73729_5_, p_73729_2_ + p_73729_6_, zLevel,
				(p_73729_3_ + p_73729_5_) * f, (p_73729_4_ + p_73729_6_) * f1);
		tessellator.addVertexWithUV(p_73729_1_ + p_73729_5_, p_73729_2_ + 0, zLevel, (p_73729_3_ + p_73729_5_) * f,
				(p_73729_4_ + 0) * f1);
		tessellator.addVertexWithUV(p_73729_1_ + 0, p_73729_2_ + 0, zLevel, (p_73729_3_ + 0) * f,
				(p_73729_4_ + 0) * f1);
		tessellator.draw();
	}

	public void drawTexturedModelRectFromIcon(int p_94065_1_, int p_94065_2_, IIcon p_94065_3_, int p_94065_4_,
			int p_94065_5_) {
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		tessellator.addVertexWithUV(p_94065_1_ + 0, p_94065_2_ + p_94065_5_, zLevel, p_94065_3_.getMinU(),
				p_94065_3_.getMaxV());
		tessellator.addVertexWithUV(p_94065_1_ + p_94065_4_, p_94065_2_ + p_94065_5_, zLevel, p_94065_3_.getMaxU(),
				p_94065_3_.getMaxV());
		tessellator.addVertexWithUV(p_94065_1_ + p_94065_4_, p_94065_2_ + 0, zLevel, p_94065_3_.getMaxU(),
				p_94065_3_.getMinV());
		tessellator.addVertexWithUV(p_94065_1_ + 0, p_94065_2_ + 0, zLevel, p_94065_3_.getMinU(), p_94065_3_.getMinV());
		tessellator.draw();
	}

	public static void func_146110_a(int p_146110_0_, int p_146110_1_, float p_146110_2_, float p_146110_3_,
			int p_146110_4_, int p_146110_5_, float p_146110_6_, float p_146110_7_) {
		float f4 = 1.0F / p_146110_6_;
		float f5 = 1.0F / p_146110_7_;
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		tessellator.addVertexWithUV(p_146110_0_, p_146110_1_ + p_146110_5_, 0.0D, p_146110_2_ * f4,
				(p_146110_3_ + p_146110_5_) * f5);
		tessellator.addVertexWithUV(p_146110_0_ + p_146110_4_, p_146110_1_ + p_146110_5_, 0.0D,
				(p_146110_2_ + p_146110_4_) * f4, (p_146110_3_ + p_146110_5_) * f5);
		tessellator.addVertexWithUV(p_146110_0_ + p_146110_4_, p_146110_1_, 0.0D, (p_146110_2_ + p_146110_4_) * f4,
				p_146110_3_ * f5);
		tessellator.addVertexWithUV(p_146110_0_, p_146110_1_, 0.0D, p_146110_2_ * f4, p_146110_3_ * f5);
		tessellator.draw();
	}

	public static void func_152125_a(int p_152125_0_, int p_152125_1_, float p_152125_2_, float p_152125_3_,
			int p_152125_4_, int p_152125_5_, int p_152125_6_, int p_152125_7_, float p_152125_8_, float p_152125_9_) {
		float f4 = 1.0F / p_152125_8_;
		float f5 = 1.0F / p_152125_9_;
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		tessellator.addVertexWithUV(p_152125_0_, p_152125_1_ + p_152125_7_, 0.0D, p_152125_2_ * f4,
				(p_152125_3_ + p_152125_5_) * f5);
		tessellator.addVertexWithUV(p_152125_0_ + p_152125_6_, p_152125_1_ + p_152125_7_, 0.0D,
				(p_152125_2_ + p_152125_4_) * f4, (p_152125_3_ + p_152125_5_) * f5);
		tessellator.addVertexWithUV(p_152125_0_ + p_152125_6_, p_152125_1_, 0.0D, (p_152125_2_ + p_152125_4_) * f4,
				p_152125_3_ * f5);
		tessellator.addVertexWithUV(p_152125_0_, p_152125_1_, 0.0D, p_152125_2_ * f4, p_152125_3_ * f5);
		tessellator.draw();
	}
}