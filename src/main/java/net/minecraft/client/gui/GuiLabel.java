package net.minecraft.client.gui;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;

@SideOnly(Side.CLIENT)
public class GuiLabel extends Gui {
	protected int field_146167_a;
	protected int field_146161_f;
	public int field_146162_g;
	public int field_146174_h;
	private ArrayList field_146173_k;
	private boolean field_146170_l;
	public boolean field_146172_j;
	private boolean field_146171_m;
	private int field_146168_n;
	private int field_146169_o;
	private int field_146166_p;
	private int field_146165_q;
	private FontRenderer field_146164_r;
	private int field_146163_s;
	private static final String __OBFID = "CL_00000671";

	public void func_146159_a(Minecraft p_146159_1_, int p_146159_2_, int p_146159_3_) {
		if (field_146172_j) {
			GL11.glEnable(GL11.GL_BLEND);
			OpenGlHelper.glBlendFunc(770, 771, 1, 0);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			func_146160_b(p_146159_1_, p_146159_2_, p_146159_3_);
			int k = field_146174_h + field_146161_f / 2 + field_146163_s / 2;
			int l = k - field_146173_k.size() * 10 / 2;

			for (int i1 = 0; i1 < field_146173_k.size(); ++i1) {
				if (field_146170_l) {
					drawCenteredString(field_146164_r, (String) field_146173_k.get(i1),
							field_146162_g + field_146167_a / 2, l + i1 * 10, field_146168_n);
				} else {
					drawString(field_146164_r, (String) field_146173_k.get(i1), field_146162_g, l + i1 * 10,
							field_146168_n);
				}
			}
		}
	}

	protected void func_146160_b(Minecraft p_146160_1_, int p_146160_2_, int p_146160_3_) {
		if (field_146171_m) {
			int k = field_146167_a + field_146163_s * 2;
			int l = field_146161_f + field_146163_s * 2;
			int i1 = field_146162_g - field_146163_s;
			int j1 = field_146174_h - field_146163_s;
			drawRect(i1, j1, i1 + k, j1 + l, field_146169_o);
			drawHorizontalLine(i1, i1 + k, j1, field_146166_p);
			drawHorizontalLine(i1, i1 + k, j1 + l, field_146165_q);
			drawVerticalLine(i1, j1, j1 + l, field_146166_p);
			drawVerticalLine(i1 + k, j1, j1 + l, field_146165_q);
		}
	}
}