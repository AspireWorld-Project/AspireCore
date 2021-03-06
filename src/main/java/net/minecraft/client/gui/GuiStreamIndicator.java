package net.minecraft.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiStreamIndicator {
	private static final ResourceLocation field_152441_a = new ResourceLocation("textures/gui/stream_indicator.png");
	private final Minecraft field_152442_b;
	private float field_152443_c = 1.0F;
	private int field_152444_d = 1;
	public GuiStreamIndicator(Minecraft p_i1092_1_) {
		field_152442_b = p_i1092_1_;
	}

	public void func_152437_a(int p_152437_1_, int p_152437_2_) {
		if (field_152442_b.func_152346_Z().func_152934_n()) {
			GL11.glEnable(GL11.GL_BLEND);
			int k = field_152442_b.func_152346_Z().func_152920_A();

			if (k > 0) {
				String s = "" + k;
				int l = field_152442_b.fontRenderer.getStringWidth(s);
				int i1 = p_152437_1_ - l - 1;
				int j1 = p_152437_2_ + 20 - 1;
				int k1 = p_152437_2_ + 20 + field_152442_b.fontRenderer.FONT_HEIGHT - 1;
				GL11.glDisable(GL11.GL_TEXTURE_2D);
				Tessellator tessellator = Tessellator.instance;
				GL11.glColor4f(0.0F, 0.0F, 0.0F, (0.65F + 0.35000002F * field_152443_c) / 2.0F);
				tessellator.startDrawingQuads();
				tessellator.addVertex(i1, k1, 0.0D);
				tessellator.addVertex(p_152437_1_, k1, 0.0D);
				tessellator.addVertex(p_152437_1_, j1, 0.0D);
				tessellator.addVertex(i1, j1, 0.0D);
				tessellator.draw();
				GL11.glEnable(GL11.GL_TEXTURE_2D);
				field_152442_b.fontRenderer.drawString(s, p_152437_1_ - l, p_152437_2_ + 20, 16777215);
			}

			func_152436_a(p_152437_1_, p_152437_2_, func_152440_b(), 0);
			func_152436_a(p_152437_1_, p_152437_2_, func_152438_c(), 17);
		}
	}

	private void func_152436_a(int p_152436_1_, int p_152436_2_, int p_152436_3_, int p_152436_4_) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.65F + 0.35000002F * field_152443_c);
		field_152442_b.getTextureManager().bindTexture(field_152441_a);
		float f = 150.0F;
		float f1 = 0.0F;
		float f2 = p_152436_3_ * 0.015625F;
		float f3 = 1.0F;
		float f4 = (p_152436_3_ + 16) * 0.015625F;
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		tessellator.addVertexWithUV(p_152436_1_ - 16 - p_152436_4_, p_152436_2_ + 16, f, f1, f4);
		tessellator.addVertexWithUV(p_152436_1_ - p_152436_4_, p_152436_2_ + 16, f, f3, f4);
		tessellator.addVertexWithUV(p_152436_1_ - p_152436_4_, p_152436_2_ + 0, f, f3, f2);
		tessellator.addVertexWithUV(p_152436_1_ - 16 - p_152436_4_, p_152436_2_ + 0, f, f1, f2);
		tessellator.draw();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	}

	private int func_152440_b() {
		return field_152442_b.func_152346_Z().func_152919_o() ? 16 : 0;
	}

	private int func_152438_c() {
		return field_152442_b.func_152346_Z().func_152929_G() ? 48 : 32;
	}

	public void func_152439_a() {
		if (field_152442_b.func_152346_Z().func_152934_n()) {
			field_152443_c += 0.025F * field_152444_d;

			if (field_152443_c < 0.0F) {
				field_152444_d *= -1;
				field_152443_c = 0.0F;
			} else if (field_152443_c > 1.0F) {
				field_152444_d *= -1;
				field_152443_c = 1.0F;
			}
		} else {
			field_152443_c = 1.0F;
			field_152444_d = 1;
		}
	}
}