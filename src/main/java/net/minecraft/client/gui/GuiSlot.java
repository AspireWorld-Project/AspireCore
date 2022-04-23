package net.minecraft.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public abstract class GuiSlot {
	private final Minecraft mc;
	public int width;
	public int height;
	public int top;
	public int bottom;
	public int right;
	public int left;
	public final int slotHeight;
	private int scrollUpButtonID;
	private int scrollDownButtonID;
	protected int mouseX;
	protected int mouseY;
	protected boolean field_148163_i = true;
	private float initialClickY = -2.0F;
	private float scrollMultiplier;
	private float amountScrolled;
	private int selectedElement = -1;
	private long lastClicked;
	private boolean showSelectionBox = true;
	private boolean hasListHeader;
	public int headerPadding;
	private boolean field_148164_v = true;
	public GuiSlot(Minecraft p_i1052_1_, int p_i1052_2_, int p_i1052_3_, int p_i1052_4_, int p_i1052_5_,
			int p_i1052_6_) {
		mc = p_i1052_1_;
		width = p_i1052_2_;
		height = p_i1052_3_;
		top = p_i1052_4_;
		bottom = p_i1052_5_;
		slotHeight = p_i1052_6_;
		left = 0;
		right = p_i1052_2_;
	}

	public void func_148122_a(int p_148122_1_, int p_148122_2_, int p_148122_3_, int p_148122_4_) {
		width = p_148122_1_;
		height = p_148122_2_;
		top = p_148122_3_;
		bottom = p_148122_4_;
		left = 0;
		right = p_148122_1_;
	}

	public void setShowSelectionBox(boolean p_148130_1_) {
		showSelectionBox = p_148130_1_;
	}

	protected void setHasListHeader(boolean p_148133_1_, int p_148133_2_) {
		hasListHeader = p_148133_1_;
		headerPadding = p_148133_2_;

		if (!p_148133_1_) {
			headerPadding = 0;
		}
	}

	protected abstract int getSize();

	protected abstract void elementClicked(int p_148144_1_, boolean p_148144_2_, int p_148144_3_, int p_148144_4_);

	protected abstract boolean isSelected(int p_148131_1_);

	protected int getContentHeight() {
		return getSize() * slotHeight + headerPadding;
	}

	protected abstract void drawBackground();

	protected abstract void drawSlot(int p_148126_1_, int p_148126_2_, int p_148126_3_, int p_148126_4_,
			Tessellator p_148126_5_, int p_148126_6_, int p_148126_7_);

	protected void drawListHeader(int p_148129_1_, int p_148129_2_, Tessellator p_148129_3_) {
	}

	protected void func_148132_a(int p_148132_1_, int p_148132_2_) {
	}

	protected void func_148142_b(int p_148142_1_, int p_148142_2_) {
	}

	public int func_148124_c(int p_148124_1_, int p_148124_2_) {
		int k = left + width / 2 - getListWidth() / 2;
		int l = left + width / 2 + getListWidth() / 2;
		int i1 = p_148124_2_ - top - headerPadding + (int) amountScrolled - 4;
		int j1 = i1 / slotHeight;
		return p_148124_1_ < getScrollBarX() && p_148124_1_ >= k && p_148124_1_ <= l && j1 >= 0 && i1 >= 0
				&& j1 < getSize() ? j1 : -1;
	}

	public void registerScrollButtons(int p_148134_1_, int p_148134_2_) {
		scrollUpButtonID = p_148134_1_;
		scrollDownButtonID = p_148134_2_;
	}

	private void bindAmountScrolled() {
		int i = func_148135_f();

		if (i < 0) {
			i /= 2;
		}

		if (!field_148163_i && i < 0) {
			i = 0;
		}

		if (amountScrolled < 0.0F) {
			amountScrolled = 0.0F;
		}

		if (amountScrolled > i) {
			amountScrolled = i;
		}
	}

	public int func_148135_f() {
		return getContentHeight() - (bottom - top - 4);
	}

	public int getAmountScrolled() {
		return (int) amountScrolled;
	}

	public boolean func_148141_e(int p_148141_1_) {
		return p_148141_1_ >= top && p_148141_1_ <= bottom;
	}

	public void scrollBy(int p_148145_1_) {
		amountScrolled += p_148145_1_;
		bindAmountScrolled();
		initialClickY = -2.0F;
	}

	public void actionPerformed(GuiButton p_148147_1_) {
		if (p_148147_1_.enabled) {
			if (p_148147_1_.id == scrollUpButtonID) {
				amountScrolled -= slotHeight * 2 / 3;
				initialClickY = -2.0F;
				bindAmountScrolled();
			} else if (p_148147_1_.id == scrollDownButtonID) {
				amountScrolled += slotHeight * 2 / 3;
				initialClickY = -2.0F;
				bindAmountScrolled();
			}
		}
	}

	public void drawScreen(int p_148128_1_, int p_148128_2_, float p_148128_3_) {
		mouseX = p_148128_1_;
		mouseY = p_148128_2_;
		drawBackground();
		int k = getSize();
		int l = getScrollBarX();
		int i1 = l + 6;
		int l1;
		int i2;
		int k2;
		int i3;

		if (p_148128_1_ > left && p_148128_1_ < right && p_148128_2_ > top && p_148128_2_ < bottom) {
			if (Mouse.isButtonDown(0) && func_148125_i()) {
				if (initialClickY == -1.0F) {
					boolean flag1 = true;

					if (p_148128_2_ >= top && p_148128_2_ <= bottom) {
						int k1 = width / 2 - getListWidth() / 2;
						l1 = width / 2 + getListWidth() / 2;
						i2 = p_148128_2_ - top - headerPadding + (int) amountScrolled - 4;
						int j2 = i2 / slotHeight;

						if (p_148128_1_ >= k1 && p_148128_1_ <= l1 && j2 >= 0 && i2 >= 0 && j2 < k) {
							boolean flag = j2 == selectedElement && Minecraft.getSystemTime() - lastClicked < 250L;
							elementClicked(j2, flag, p_148128_1_, p_148128_2_);
							selectedElement = j2;
							lastClicked = Minecraft.getSystemTime();
						} else if (p_148128_1_ >= k1 && p_148128_1_ <= l1 && i2 < 0) {
							func_148132_a(p_148128_1_ - k1, p_148128_2_ - top + (int) amountScrolled - 4);
							flag1 = false;
						}

						if (p_148128_1_ >= l && p_148128_1_ <= i1) {
							scrollMultiplier = -1.0F;
							i3 = func_148135_f();

							if (i3 < 1) {
								i3 = 1;
							}

							k2 = (int) ((float) ((bottom - top) * (bottom - top)) / (float) getContentHeight());

							if (k2 < 32) {
								k2 = 32;
							}

							if (k2 > bottom - top - 8) {
								k2 = bottom - top - 8;
							}

							scrollMultiplier /= (float) (bottom - top - k2) / (float) i3;
						} else {
							scrollMultiplier = 1.0F;
						}

						if (flag1) {
							initialClickY = p_148128_2_;
						} else {
							initialClickY = -2.0F;
						}
					} else {
						initialClickY = -2.0F;
					}
				} else if (initialClickY >= 0.0F) {
					amountScrolled -= (p_148128_2_ - initialClickY) * scrollMultiplier;
					initialClickY = p_148128_2_;
				}
			} else {
				for (; !mc.gameSettings.touchscreen && Mouse.next(); mc.currentScreen.handleMouseInput()) {
					int j1 = Mouse.getEventDWheel();

					if (j1 != 0) {
						if (j1 > 0) {
							j1 = -1;
						} else if (j1 < 0) {
							j1 = 1;
						}

						amountScrolled += j1 * slotHeight / 2;
					}
				}

				initialClickY = -1.0F;
			}
		}

		bindAmountScrolled();
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_FOG);
		Tessellator tessellator = Tessellator.instance;
		drawContainerBackground(tessellator);
		l1 = left + width / 2 - getListWidth() / 2 + 2;
		i2 = top + 4 - (int) amountScrolled;

		if (hasListHeader) {
			drawListHeader(l1, i2, tessellator);
		}

		drawSelectionBox(l1, i2, p_148128_1_, p_148128_2_);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		byte b0 = 4;
		overlayBackground(0, top, 255, 255);
		overlayBackground(bottom, height, 255, 255);
		GL11.glEnable(GL11.GL_BLEND);
		OpenGlHelper.glBlendFunc(770, 771, 0, 1);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glShadeModel(GL11.GL_SMOOTH);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		tessellator.startDrawingQuads();
		tessellator.setColorRGBA_I(0, 0);
		tessellator.addVertexWithUV(left, top + b0, 0.0D, 0.0D, 1.0D);
		tessellator.addVertexWithUV(right, top + b0, 0.0D, 1.0D, 1.0D);
		tessellator.setColorRGBA_I(0, 255);
		tessellator.addVertexWithUV(right, top, 0.0D, 1.0D, 0.0D);
		tessellator.addVertexWithUV(left, top, 0.0D, 0.0D, 0.0D);
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setColorRGBA_I(0, 255);
		tessellator.addVertexWithUV(left, bottom, 0.0D, 0.0D, 1.0D);
		tessellator.addVertexWithUV(right, bottom, 0.0D, 1.0D, 1.0D);
		tessellator.setColorRGBA_I(0, 0);
		tessellator.addVertexWithUV(right, bottom - b0, 0.0D, 1.0D, 0.0D);
		tessellator.addVertexWithUV(left, bottom - b0, 0.0D, 0.0D, 0.0D);
		tessellator.draw();
		i3 = func_148135_f();

		if (i3 > 0) {
			k2 = (bottom - top) * (bottom - top) / getContentHeight();

			if (k2 < 32) {
				k2 = 32;
			}

			if (k2 > bottom - top - 8) {
				k2 = bottom - top - 8;
			}

			int l2 = (int) amountScrolled * (bottom - top - k2) / i3 + top;

			if (l2 < top) {
				l2 = top;
			}

			tessellator.startDrawingQuads();
			tessellator.setColorRGBA_I(0, 255);
			tessellator.addVertexWithUV(l, bottom, 0.0D, 0.0D, 1.0D);
			tessellator.addVertexWithUV(i1, bottom, 0.0D, 1.0D, 1.0D);
			tessellator.addVertexWithUV(i1, top, 0.0D, 1.0D, 0.0D);
			tessellator.addVertexWithUV(l, top, 0.0D, 0.0D, 0.0D);
			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setColorRGBA_I(8421504, 255);
			tessellator.addVertexWithUV(l, l2 + k2, 0.0D, 0.0D, 1.0D);
			tessellator.addVertexWithUV(i1, l2 + k2, 0.0D, 1.0D, 1.0D);
			tessellator.addVertexWithUV(i1, l2, 0.0D, 1.0D, 0.0D);
			tessellator.addVertexWithUV(l, l2, 0.0D, 0.0D, 0.0D);
			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setColorRGBA_I(12632256, 255);
			tessellator.addVertexWithUV(l, l2 + k2 - 1, 0.0D, 0.0D, 1.0D);
			tessellator.addVertexWithUV(i1 - 1, l2 + k2 - 1, 0.0D, 1.0D, 1.0D);
			tessellator.addVertexWithUV(i1 - 1, l2, 0.0D, 1.0D, 0.0D);
			tessellator.addVertexWithUV(l, l2, 0.0D, 0.0D, 0.0D);
			tessellator.draw();
		}

		func_148142_b(p_148128_1_, p_148128_2_);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glShadeModel(GL11.GL_FLAT);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glDisable(GL11.GL_BLEND);
	}

	public void func_148143_b(boolean p_148143_1_) {
		field_148164_v = p_148143_1_;
	}

	public boolean func_148125_i() {
		return field_148164_v;
	}

	public int getListWidth() {
		return 220;
	}

	protected void drawSelectionBox(int p_148120_1_, int p_148120_2_, int p_148120_3_, int p_148120_4_) {
		int i1 = getSize();
		Tessellator tessellator = Tessellator.instance;

		for (int j1 = 0; j1 < i1; ++j1) {
			int k1 = p_148120_2_ + j1 * slotHeight + headerPadding;
			int l1 = slotHeight - 4;

			if (k1 <= bottom && k1 + l1 >= top) {
				if (showSelectionBox && isSelected(j1)) {
					int i2 = left + width / 2 - getListWidth() / 2;
					int j2 = left + width / 2 + getListWidth() / 2;
					GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
					GL11.glDisable(GL11.GL_TEXTURE_2D);
					tessellator.startDrawingQuads();
					tessellator.setColorOpaque_I(8421504);
					tessellator.addVertexWithUV(i2, k1 + l1 + 2, 0.0D, 0.0D, 1.0D);
					tessellator.addVertexWithUV(j2, k1 + l1 + 2, 0.0D, 1.0D, 1.0D);
					tessellator.addVertexWithUV(j2, k1 - 2, 0.0D, 1.0D, 0.0D);
					tessellator.addVertexWithUV(i2, k1 - 2, 0.0D, 0.0D, 0.0D);
					tessellator.setColorOpaque_I(0);
					tessellator.addVertexWithUV(i2 + 1, k1 + l1 + 1, 0.0D, 0.0D, 1.0D);
					tessellator.addVertexWithUV(j2 - 1, k1 + l1 + 1, 0.0D, 1.0D, 1.0D);
					tessellator.addVertexWithUV(j2 - 1, k1 - 1, 0.0D, 1.0D, 0.0D);
					tessellator.addVertexWithUV(i2 + 1, k1 - 1, 0.0D, 0.0D, 0.0D);
					tessellator.draw();
					GL11.glEnable(GL11.GL_TEXTURE_2D);
				}

				drawSlot(j1, p_148120_1_, k1, l1, tessellator, p_148120_3_, p_148120_4_);
			}
		}
	}

	protected int getScrollBarX() {
		return width / 2 + 124;
	}

	private void overlayBackground(int p_148136_1_, int p_148136_2_, int p_148136_3_, int p_148136_4_) {
		Tessellator tessellator = Tessellator.instance;
		mc.getTextureManager().bindTexture(Gui.optionsBackground);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		float f = 32.0F;
		tessellator.startDrawingQuads();
		tessellator.setColorRGBA_I(4210752, p_148136_4_);
		tessellator.addVertexWithUV(left, p_148136_2_, 0.0D, 0.0D, p_148136_2_ / f);
		tessellator.addVertexWithUV(left + width, p_148136_2_, 0.0D, width / f, p_148136_2_ / f);
		tessellator.setColorRGBA_I(4210752, p_148136_3_);
		tessellator.addVertexWithUV(left + width, p_148136_1_, 0.0D, width / f, p_148136_1_ / f);
		tessellator.addVertexWithUV(left, p_148136_1_, 0.0D, 0.0D, p_148136_1_ / f);
		tessellator.draw();
	}

	public void setSlotXBoundsFromLeft(int p_148140_1_) {
		left = p_148140_1_;
		right = p_148140_1_ + width;
	}

	public int getSlotHeight() {
		return slotHeight;
	}

	protected void drawContainerBackground(Tessellator tessellator) {
		mc.getTextureManager().bindTexture(Gui.optionsBackground);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		float f1 = 32.0F;
		tessellator.startDrawingQuads();
		tessellator.setColorOpaque_I(2105376);
		tessellator.addVertexWithUV(left, bottom, 0.0D, left / f1, (bottom + (int) amountScrolled) / f1);
		tessellator.addVertexWithUV(right, bottom, 0.0D, right / f1, (bottom + (int) amountScrolled) / f1);
		tessellator.addVertexWithUV(right, top, 0.0D, right / f1, (top + (int) amountScrolled) / f1);
		tessellator.addVertexWithUV(left, top, 0.0D, left / f1, (top + (int) amountScrolled) / f1);
		tessellator.draw();
	}
}