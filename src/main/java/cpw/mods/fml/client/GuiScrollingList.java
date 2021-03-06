/*
 * Forge Mod Loader
 * Copyright (c) 2012-2013 cpw.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 *
 * Contributors:
 *     cpw - implementation
 */

package cpw.mods.fml.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.util.List;

public abstract class GuiScrollingList {
	private final Minecraft client;
	protected final int listWidth;
	protected final int listHeight;
	protected final int top;
	protected final int bottom;
	private final int right;
	protected final int left;
	protected final int slotHeight;
	private int scrollUpActionId;
	private int scrollDownActionId;
	protected int mouseX;
	protected int mouseY;
	private float initialMouseClickY = -2.0F;
	private float scrollFactor;
	private float scrollDistance;
	private int selectedIndex = -1;
	private long lastClickTime = 0L;
	private boolean field_25123_p = true;
	private boolean field_27262_q;
	private int field_27261_r;

	public GuiScrollingList(Minecraft client, int width, int height, int top, int bottom, int left, int entryHeight) {
		this.client = client;
		listWidth = width;
		listHeight = height;
		this.top = top;
		this.bottom = bottom;
		slotHeight = entryHeight;
		this.left = left;
		right = width + this.left;
	}

	public void func_27258_a(boolean p_27258_1_) {
		field_25123_p = p_27258_1_;
	}

	protected void func_27259_a(boolean p_27259_1_, int p_27259_2_) {
		field_27262_q = p_27259_1_;
		field_27261_r = p_27259_2_;

		if (!p_27259_1_) {
			field_27261_r = 0;
		}
	}

	protected abstract int getSize();

	protected abstract void elementClicked(int index, boolean doubleClick);

	protected abstract boolean isSelected(int index);

	protected int getContentHeight() {
		return getSize() * slotHeight + field_27261_r;
	}

	protected abstract void drawBackground();

	protected abstract void drawSlot(int var1, int var2, int var3, int var4, Tessellator var5);

	protected void func_27260_a(int p_27260_1_, int p_27260_2_, Tessellator p_27260_3_) {
	}

	protected void func_27255_a(int p_27255_1_, int p_27255_2_) {
	}

	protected void func_27257_b(int p_27257_1_, int p_27257_2_) {
	}

	public int func_27256_c(int p_27256_1_, int p_27256_2_)

	{
		int var3 = left + 1;
		int var4 = left + listWidth - 7;
		int var5 = p_27256_2_ - top - field_27261_r + (int) scrollDistance - 4;
		int var6 = var5 / slotHeight;
		return p_27256_1_ >= var3 && p_27256_1_ <= var4 && var6 >= 0 && var5 >= 0 && var6 < getSize() ? var6 : -1;
	}

	public void registerScrollButtons(@SuppressWarnings("rawtypes") List p_22240_1_, int p_22240_2_, int p_22240_3_) {
		scrollUpActionId = p_22240_2_;
		scrollDownActionId = p_22240_3_;
	}

	private void applyScrollLimits() {
		int var1 = getContentHeight() - (bottom - top - 4);

		if (var1 < 0) {
			var1 /= 2;
		}

		if (scrollDistance < 0.0F) {
			scrollDistance = 0.0F;
		}

		if (scrollDistance > var1) {
			scrollDistance = var1;
		}
	}

	public void actionPerformed(GuiButton button) {
		if (button.enabled) {
			if (button.id == scrollUpActionId) {
				scrollDistance -= slotHeight * 2 / 3;
				initialMouseClickY = -2.0F;
				applyScrollLimits();
			} else if (button.id == scrollDownActionId) {
				scrollDistance += slotHeight * 2 / 3;
				initialMouseClickY = -2.0F;
				applyScrollLimits();
			}
		}
	}

	public void drawScreen(int mouseX, int mouseY, float p_22243_3_) {
		this.mouseX = mouseX;
		this.mouseY = mouseY;
		drawBackground();
		int listLength = getSize();
		int scrollBarXStart = left + listWidth - 6;
		int scrollBarXEnd = scrollBarXStart + 6;
		int boxLeft = left;
		int boxRight = scrollBarXStart - 1;
		int var10;
		int var11;
		int var13;
		int var19;

		if (Mouse.isButtonDown(0)) {
			if (initialMouseClickY == -1.0F) {
				boolean var7 = true;

				if (mouseY >= top && mouseY <= bottom) {
					var10 = mouseY - top - field_27261_r + (int) scrollDistance - 4;
					var11 = var10 / slotHeight;

					if (mouseX >= boxLeft && mouseX <= boxRight && var11 >= 0 && var10 >= 0 && var11 < listLength) {
						boolean var12 = var11 == selectedIndex && System.currentTimeMillis() - lastClickTime < 250L;
						elementClicked(var11, var12);
						selectedIndex = var11;
						lastClickTime = System.currentTimeMillis();
					} else if (mouseX >= boxLeft && mouseX <= boxRight && var10 < 0) {
						func_27255_a(mouseX - boxLeft, mouseY - top + (int) scrollDistance - 4);
						var7 = false;
					}

					if (mouseX >= scrollBarXStart && mouseX <= scrollBarXEnd) {
						scrollFactor = -1.0F;
						var19 = getContentHeight() - (bottom - top - 4);

						if (var19 < 1) {
							var19 = 1;
						}

						var13 = (int) ((float) ((bottom - top) * (bottom - top)) / (float) getContentHeight());

						if (var13 < 32) {
							var13 = 32;
						}

						if (var13 > bottom - top - 8) {
							var13 = bottom - top - 8;
						}

						scrollFactor /= (float) (bottom - top - var13) / (float) var19;
					} else {
						scrollFactor = 1.0F;
					}

					if (var7) {
						initialMouseClickY = mouseY;
					} else {
						initialMouseClickY = -2.0F;
					}
				} else {
					initialMouseClickY = -2.0F;
				}
			} else if (initialMouseClickY >= 0.0F) {
				scrollDistance -= (mouseY - initialMouseClickY) * scrollFactor;
				initialMouseClickY = mouseY;
			}
		} else {
			while (Mouse.next()) {
				int var16 = Mouse.getEventDWheel();

				if (var16 != 0) {
					if (var16 > 0) {
						var16 = -1;
					} else if (var16 < 0) {
						var16 = 1;
					}

					scrollDistance += var16 * slotHeight / 2;
				}
			}

			initialMouseClickY = -1.0F;
		}

		applyScrollLimits();
		Tessellator var18 = Tessellator.instance;
		if (client.theWorld != null) {
			drawGradientRect(left, top, right, bottom, -1072689136, -804253680);
		} else {
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glDisable(GL11.GL_FOG);
			client.renderEngine.bindTexture(Gui.optionsBackground);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			float var17 = 32.0F;
			var18.startDrawingQuads();
			var18.setColorOpaque_I(2105376);
			var18.addVertexWithUV(left, bottom, 0.0D, left / var17, (bottom + (int) scrollDistance) / var17);
			var18.addVertexWithUV(right, bottom, 0.0D, right / var17, (bottom + (int) scrollDistance) / var17);
			var18.addVertexWithUV(right, top, 0.0D, right / var17, (top + (int) scrollDistance) / var17);
			var18.addVertexWithUV(left, top, 0.0D, left / var17, (top + (int) scrollDistance) / var17);
			var18.draw();
		}
		// boxRight = this.listWidth / 2 - 92 - 16;
		var10 = top + 4 - (int) scrollDistance;

		if (field_27262_q) {
			func_27260_a(boxRight, var10, var18);
		}

		int var14;

		for (var11 = 0; var11 < listLength; ++var11) {
			var19 = var10 + var11 * slotHeight + field_27261_r;
			var13 = slotHeight - 4;

			if (var19 <= bottom && var19 + var13 >= top) {
				if (field_25123_p && isSelected(var11)) {
					var14 = boxLeft;
					int var15 = boxRight;
					GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
					GL11.glDisable(GL11.GL_TEXTURE_2D);
					var18.startDrawingQuads();
					var18.setColorOpaque_I(8421504);
					var18.addVertexWithUV(var14, var19 + var13 + 2, 0.0D, 0.0D, 1.0D);
					var18.addVertexWithUV(var15, var19 + var13 + 2, 0.0D, 1.0D, 1.0D);
					var18.addVertexWithUV(var15, var19 - 2, 0.0D, 1.0D, 0.0D);
					var18.addVertexWithUV(var14, var19 - 2, 0.0D, 0.0D, 0.0D);
					var18.setColorOpaque_I(0);
					var18.addVertexWithUV(var14 + 1, var19 + var13 + 1, 0.0D, 0.0D, 1.0D);
					var18.addVertexWithUV(var15 - 1, var19 + var13 + 1, 0.0D, 1.0D, 1.0D);
					var18.addVertexWithUV(var15 - 1, var19 - 1, 0.0D, 1.0D, 0.0D);
					var18.addVertexWithUV(var14 + 1, var19 - 1, 0.0D, 0.0D, 0.0D);
					var18.draw();
					GL11.glEnable(GL11.GL_TEXTURE_2D);
				}

				drawSlot(var11, boxRight, var19, var13, var18);
			}
		}

		GL11.glDisable(GL11.GL_DEPTH_TEST);
		byte var20 = 4;
		if (client.theWorld == null) {
			overlayBackground(0, top, 255, 255);
			overlayBackground(bottom, listHeight, 255, 255);
		}
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glShadeModel(GL11.GL_SMOOTH);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		var18.startDrawingQuads();
		var18.setColorRGBA_I(0, 0);
		var18.addVertexWithUV(left, top + var20, 0.0D, 0.0D, 1.0D);
		var18.addVertexWithUV(right, top + var20, 0.0D, 1.0D, 1.0D);
		var18.setColorRGBA_I(0, 255);
		var18.addVertexWithUV(right, top, 0.0D, 1.0D, 0.0D);
		var18.addVertexWithUV(left, top, 0.0D, 0.0D, 0.0D);
		var18.draw();
		var18.startDrawingQuads();
		var18.setColorRGBA_I(0, 255);
		var18.addVertexWithUV(left, bottom, 0.0D, 0.0D, 1.0D);
		var18.addVertexWithUV(right, bottom, 0.0D, 1.0D, 1.0D);
		var18.setColorRGBA_I(0, 0);
		var18.addVertexWithUV(right, bottom - var20, 0.0D, 1.0D, 0.0D);
		var18.addVertexWithUV(left, bottom - var20, 0.0D, 0.0D, 0.0D);
		var18.draw();
		var19 = getContentHeight() - (bottom - top - 4);

		if (var19 > 0) {
			var13 = (bottom - top) * (bottom - top) / getContentHeight();

			if (var13 < 32) {
				var13 = 32;
			}

			if (var13 > bottom - top - 8) {
				var13 = bottom - top - 8;
			}

			var14 = (int) scrollDistance * (bottom - top - var13) / var19 + top;

			if (var14 < top) {
				var14 = top;
			}

			var18.startDrawingQuads();
			var18.setColorRGBA_I(0, 255);
			var18.addVertexWithUV(scrollBarXStart, bottom, 0.0D, 0.0D, 1.0D);
			var18.addVertexWithUV(scrollBarXEnd, bottom, 0.0D, 1.0D, 1.0D);
			var18.addVertexWithUV(scrollBarXEnd, top, 0.0D, 1.0D, 0.0D);
			var18.addVertexWithUV(scrollBarXStart, top, 0.0D, 0.0D, 0.0D);
			var18.draw();
			var18.startDrawingQuads();
			var18.setColorRGBA_I(8421504, 255);
			var18.addVertexWithUV(scrollBarXStart, var14 + var13, 0.0D, 0.0D, 1.0D);
			var18.addVertexWithUV(scrollBarXEnd, var14 + var13, 0.0D, 1.0D, 1.0D);
			var18.addVertexWithUV(scrollBarXEnd, var14, 0.0D, 1.0D, 0.0D);
			var18.addVertexWithUV(scrollBarXStart, var14, 0.0D, 0.0D, 0.0D);
			var18.draw();
			var18.startDrawingQuads();
			var18.setColorRGBA_I(12632256, 255);
			var18.addVertexWithUV(scrollBarXStart, var14 + var13 - 1, 0.0D, 0.0D, 1.0D);
			var18.addVertexWithUV(scrollBarXEnd - 1, var14 + var13 - 1, 0.0D, 1.0D, 1.0D);
			var18.addVertexWithUV(scrollBarXEnd - 1, var14, 0.0D, 1.0D, 0.0D);
			var18.addVertexWithUV(scrollBarXStart, var14, 0.0D, 0.0D, 0.0D);
			var18.draw();
		}

		func_27257_b(mouseX, mouseY);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glShadeModel(GL11.GL_FLAT);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glDisable(GL11.GL_BLEND);
	}

	private void overlayBackground(int p_22239_1_, int p_22239_2_, int p_22239_3_, int p_22239_4_) {
		Tessellator var5 = Tessellator.instance;
		client.renderEngine.bindTexture(Gui.optionsBackground);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		float var6 = 32.0F;
		var5.startDrawingQuads();
		var5.setColorRGBA_I(4210752, p_22239_4_);
		var5.addVertexWithUV(0.0D, p_22239_2_, 0.0D, 0.0D, p_22239_2_ / var6);
		var5.addVertexWithUV((double) listWidth + 30, p_22239_2_, 0.0D, (listWidth + 30) / var6, p_22239_2_ / var6);
		var5.setColorRGBA_I(4210752, p_22239_3_);
		var5.addVertexWithUV((double) listWidth + 30, p_22239_1_, 0.0D, (listWidth + 30) / var6, p_22239_1_ / var6);
		var5.addVertexWithUV(0.0D, p_22239_1_, 0.0D, 0.0D, p_22239_1_ / var6);
		var5.draw();
	}

	protected void drawGradientRect(int par1, int par2, int par3, int par4, int par5, int par6) {
		float f = (par5 >> 24 & 255) / 255.0F;
		float f1 = (par5 >> 16 & 255) / 255.0F;
		float f2 = (par5 >> 8 & 255) / 255.0F;
		float f3 = (par5 & 255) / 255.0F;
		float f4 = (par6 >> 24 & 255) / 255.0F;
		float f5 = (par6 >> 16 & 255) / 255.0F;
		float f6 = (par6 >> 8 & 255) / 255.0F;
		float f7 = (par6 & 255) / 255.0F;
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		OpenGlHelper.glBlendFunc(770, 771, 1, 0);
		GL11.glShadeModel(GL11.GL_SMOOTH);
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		tessellator.setColorRGBA_F(f1, f2, f3, f);
		tessellator.addVertex(par3, par2, 0.0D);
		tessellator.addVertex(par1, par2, 0.0D);
		tessellator.setColorRGBA_F(f5, f6, f7, f4);
		tessellator.addVertex(par1, par4, 0.0D);
		tessellator.addVertex(par3, par4, 0.0D);
		tessellator.draw();
		GL11.glShadeModel(GL11.GL_FLAT);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}
}