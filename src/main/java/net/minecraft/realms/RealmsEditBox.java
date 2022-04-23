package net.minecraft.realms;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ChatAllowedCharacters;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RealmsEditBox {
	public static final int BACKWARDS = -1;
	public static final int FORWARDS = 1;
	private static final int CURSOR_INSERT_WIDTH = 1;
	private static final int CURSOR_INSERT_COLOR = -3092272;
	private static final String CURSOR_APPEND_CHARACTER = "_";
	private final FontRenderer font;
	private final int x;
	private final int y;
	private final int width;
	private final int height;
	private String value;
	private int maxLength;
	private int frame;
	private boolean bordered;
	private boolean canLoseFocus;
	private boolean inFocus;
	private boolean isEditable;
	private int displayPos;
	private int cursorPos;
	private int highlightPos;
	private int textColor;
	private int textColorUneditable;
	private boolean visible;
	private static final String __OBFID = "CL_00001858";

	public RealmsEditBox(int p_i1111_1_, int p_i1111_2_, int p_i1111_3_, int p_i1111_4_) {
		this(Minecraft.getMinecraft().fontRenderer, p_i1111_1_, p_i1111_2_, p_i1111_3_, p_i1111_4_);
	}

	public RealmsEditBox(FontRenderer p_i1112_1_, int p_i1112_2_, int p_i1112_3_, int p_i1112_4_, int p_i1112_5_) {
		value = "";
		maxLength = 32;
		bordered = true;
		canLoseFocus = true;
		isEditable = true;
		textColor = 14737632;
		textColorUneditable = 7368816;
		visible = true;
		font = p_i1112_1_;
		x = p_i1112_2_;
		y = p_i1112_3_;
		width = p_i1112_4_;
		height = p_i1112_5_;
	}

	public void tick() {
		++frame;
	}

	public void setValue(String p_setValue_1_) {
		if (p_setValue_1_.length() > maxLength) {
			value = p_setValue_1_.substring(0, maxLength);
		} else {
			value = p_setValue_1_;
		}

		moveCursorToEnd();
	}

	public String getValue() {
		return value;
	}

	public String getHighlighted() {
		int i = cursorPos < highlightPos ? cursorPos : highlightPos;
		int j = cursorPos < highlightPos ? highlightPos : cursorPos;
		return value.substring(i, j);
	}

	public void insertText(String p_insertText_1_) {
		String s1 = "";
		String s2 = ChatAllowedCharacters.filerAllowedCharacters(p_insertText_1_);
		int i = cursorPos < highlightPos ? cursorPos : highlightPos;
		int j = cursorPos < highlightPos ? highlightPos : cursorPos;
		int k = maxLength - value.length() - (i - highlightPos);
		if (value.length() > 0) {
			s1 = s1 + value.substring(0, i);
		}

		int l;

		if (k < s2.length()) {
			s1 = s1 + s2.substring(0, k);
			l = k;
		} else {
			s1 = s1 + s2;
			l = s2.length();
		}

		if (value.length() > 0 && j < value.length()) {
			s1 = s1 + value.substring(j);
		}

		value = s1;
		moveCursor(i - highlightPos + l);
	}

	public void deleteWords(int p_deleteWords_1_) {
		if (value.length() != 0) {
			if (highlightPos != cursorPos) {
				insertText("");
			} else {
				deleteChars(this.getWordPosition(p_deleteWords_1_) - cursorPos);
			}
		}
	}

	public void deleteChars(int p_deleteChars_1_) {
		if (value.length() != 0) {
			if (highlightPos != cursorPos) {
				insertText("");
			} else {
				boolean flag = p_deleteChars_1_ < 0;
				int j = flag ? cursorPos + p_deleteChars_1_ : cursorPos;
				int k = flag ? cursorPos : cursorPos + p_deleteChars_1_;
				String s = "";

				if (j >= 0) {
					s = value.substring(0, j);
				}

				if (k < value.length()) {
					s = s + value.substring(k);
				}

				value = s;

				if (flag) {
					moveCursor(p_deleteChars_1_);
				}
			}
		}
	}

	public int getWordPosition(int p_getWordPosition_1_) {
		return this.getWordPosition(p_getWordPosition_1_, getCursorPosition());
	}

	public int getWordPosition(int p_getWordPosition_1_, int p_getWordPosition_2_) {
		return this.getWordPosition(p_getWordPosition_1_, getCursorPosition(), true);
	}

	public int getWordPosition(int p_getWordPosition_1_, int p_getWordPosition_2_, boolean p_getWordPosition_3_) {
		int k = p_getWordPosition_2_;
		boolean flag1 = p_getWordPosition_1_ < 0;
		int l = Math.abs(p_getWordPosition_1_);

		for (int i1 = 0; i1 < l; ++i1) {
			if (flag1) {
				while (p_getWordPosition_3_ && k > 0 && value.charAt(k - 1) == 32) {
					--k;
				}

				while (k > 0 && value.charAt(k - 1) != 32) {
					--k;
				}
			} else {
				int j1 = value.length();
				k = value.indexOf(32, k);

				if (k == -1) {
					k = j1;
				} else {
					while (p_getWordPosition_3_ && k < j1 && value.charAt(k) == 32) {
						++k;
					}
				}
			}
		}

		return k;
	}

	public void moveCursor(int p_moveCursor_1_) {
		moveCursorTo(highlightPos + p_moveCursor_1_);
	}

	public void moveCursorTo(int p_moveCursorTo_1_) {
		cursorPos = p_moveCursorTo_1_;
		int j = value.length();

		if (cursorPos < 0) {
			cursorPos = 0;
		}

		if (cursorPos > j) {
			cursorPos = j;
		}

		setHighlightPos(cursorPos);
	}

	public void moveCursorToStart() {
		moveCursorTo(0);
	}

	public void moveCursorToEnd() {
		moveCursorTo(value.length());
	}

	public boolean keyPressed(char p_keyPressed_1_, int p_keyPressed_2_) {
		if (!inFocus)
			return false;
		else {
			switch (p_keyPressed_1_) {
			case 1:
				moveCursorToEnd();
				setHighlightPos(0);
				return true;
			case 3:
				GuiScreen.setClipboardString(getHighlighted());
				return true;
			case 22:
				if (isEditable) {
					insertText(GuiScreen.getClipboardString());
				}

				return true;
			case 24:
				GuiScreen.setClipboardString(getHighlighted());

				if (isEditable) {
					insertText("");
				}

				return true;
			default:
				switch (p_keyPressed_2_) {
				case 14:
					if (GuiScreen.isCtrlKeyDown()) {
						if (isEditable) {
							deleteWords(-1);
						}
					} else if (isEditable) {
						deleteChars(-1);
					}

					return true;
				case 199:
					if (GuiScreen.isShiftKeyDown()) {
						setHighlightPos(0);
					} else {
						moveCursorToStart();
					}

					return true;
				case 203:
					if (GuiScreen.isShiftKeyDown()) {
						if (GuiScreen.isCtrlKeyDown()) {
							setHighlightPos(this.getWordPosition(-1, getHighlightPos()));
						} else {
							setHighlightPos(getHighlightPos() - 1);
						}
					} else if (GuiScreen.isCtrlKeyDown()) {
						moveCursorTo(this.getWordPosition(-1));
					} else {
						moveCursor(-1);
					}

					return true;
				case 205:
					if (GuiScreen.isShiftKeyDown()) {
						if (GuiScreen.isCtrlKeyDown()) {
							setHighlightPos(this.getWordPosition(1, getHighlightPos()));
						} else {
							setHighlightPos(getHighlightPos() + 1);
						}
					} else if (GuiScreen.isCtrlKeyDown()) {
						moveCursorTo(this.getWordPosition(1));
					} else {
						moveCursor(1);
					}

					return true;
				case 207:
					if (GuiScreen.isShiftKeyDown()) {
						setHighlightPos(value.length());
					} else {
						moveCursorToEnd();
					}

					return true;
				case 211:
					if (GuiScreen.isCtrlKeyDown()) {
						if (isEditable) {
							deleteWords(1);
						}
					} else if (isEditable) {
						deleteChars(1);
					}

					return true;
				default:
					if (ChatAllowedCharacters.isAllowedCharacter(p_keyPressed_1_)) {
						if (isEditable) {
							insertText(Character.toString(p_keyPressed_1_));
						}

						return true;
					} else
						return false;
				}
			}
		}
	}

	public void mouseClicked(int p_mouseClicked_1_, int p_mouseClicked_2_, int p_mouseClicked_3_) {
		boolean flag = p_mouseClicked_1_ >= x && p_mouseClicked_1_ < x + width && p_mouseClicked_2_ >= y
				&& p_mouseClicked_2_ < y + height;

		if (canLoseFocus) {
			setFocus(flag);
		}

		if (inFocus && p_mouseClicked_3_ == 0) {
			int l = p_mouseClicked_1_ - x;

			if (bordered) {
				l -= 4;
			}

			String s = font.trimStringToWidth(value.substring(displayPos), getInnerWidth());
			moveCursorTo(font.trimStringToWidth(s, l).length() + displayPos);
		}
	}

	public void render() {
		if (isVisible()) {
			if (isBordered()) {
				Gui.drawRect(x - 1, y - 1, x + width + 1, y + height + 1, -6250336);
				Gui.drawRect(x, y, x + width, y + height, -16777216);
			}

			int i = isEditable ? textColor : textColorUneditable;
			int j = cursorPos - displayPos;
			int k = highlightPos - displayPos;
			String s = font.trimStringToWidth(value.substring(displayPos), getInnerWidth());
			boolean flag = j >= 0 && j <= s.length();
			boolean flag1 = inFocus && frame / 6 % 2 == 0 && flag;
			int l = bordered ? x + 4 : x;
			int i1 = bordered ? y + (height - 8) / 2 : y;
			int j1 = l;

			if (k > s.length()) {
				k = s.length();
			}

			if (s.length() > 0) {
				String s1 = flag ? s.substring(0, j) : s;
				j1 = font.drawStringWithShadow(s1, l, i1, i);
			}

			boolean flag2 = cursorPos < value.length() || value.length() >= getMaxLength();
			int k1 = j1;

			if (!flag) {
				k1 = j > 0 ? l + width : l;
			} else if (flag2) {
				k1 = j1 - 1;
				--j1;
			}

			if (s.length() > 0 && flag && j < s.length()) {
				font.drawStringWithShadow(s.substring(j), j1, i1, i);
			}

			if (flag1) {
				if (flag2) {
					Gui.drawRect(k1, i1 - 1, k1 + 1, i1 + 1 + font.FONT_HEIGHT, -3092272);
				} else {
					font.drawStringWithShadow("_", k1, i1, i);
				}
			}

			if (k != j) {
				int l1 = l + font.getStringWidth(s.substring(0, k));
				renderHighlight(k1, i1 - 1, l1 - 1, i1 + 1 + font.FONT_HEIGHT);
			}
		}
	}

	private void renderHighlight(int p_renderHighlight_1_, int p_renderHighlight_2_, int p_renderHighlight_3_,
			int p_renderHighlight_4_) {
		int i1;

		if (p_renderHighlight_1_ < p_renderHighlight_3_) {
			i1 = p_renderHighlight_1_;
			p_renderHighlight_1_ = p_renderHighlight_3_;
			p_renderHighlight_3_ = i1;
		}

		if (p_renderHighlight_2_ < p_renderHighlight_4_) {
			i1 = p_renderHighlight_2_;
			p_renderHighlight_2_ = p_renderHighlight_4_;
			p_renderHighlight_4_ = i1;
		}

		if (p_renderHighlight_3_ > x + width) {
			p_renderHighlight_3_ = x + width;
		}

		if (p_renderHighlight_1_ > x + width) {
			p_renderHighlight_1_ = x + width;
		}

		Tessellator tessellator = Tessellator.instance;
		GL11.glColor4f(0.0F, 0.0F, 255.0F, 255.0F);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_COLOR_LOGIC_OP);
		GL11.glLogicOp(GL11.GL_OR_REVERSE);
		tessellator.startDrawingQuads();
		tessellator.addVertex(p_renderHighlight_1_, p_renderHighlight_4_, 0.0D);
		tessellator.addVertex(p_renderHighlight_3_, p_renderHighlight_4_, 0.0D);
		tessellator.addVertex(p_renderHighlight_3_, p_renderHighlight_2_, 0.0D);
		tessellator.addVertex(p_renderHighlight_1_, p_renderHighlight_2_, 0.0D);
		tessellator.draw();
		GL11.glDisable(GL11.GL_COLOR_LOGIC_OP);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}

	public void setMaxLength(int p_setMaxLength_1_) {
		maxLength = p_setMaxLength_1_;

		if (value.length() > p_setMaxLength_1_) {
			value = value.substring(0, p_setMaxLength_1_);
		}
	}

	public int getMaxLength() {
		return maxLength;
	}

	public int getCursorPosition() {
		return cursorPos;
	}

	public boolean isBordered() {
		return bordered;
	}

	public void setBordered(boolean p_setBordered_1_) {
		bordered = p_setBordered_1_;
	}

	public int getTextColor() {
		return textColor;
	}

	public void setTextColor(int p_setTextColor_1_) {
		textColor = p_setTextColor_1_;
	}

	public int getTextColorUneditable() {
		return textColorUneditable;
	}

	public void setTextColorUneditable(int p_setTextColorUneditable_1_) {
		textColorUneditable = p_setTextColorUneditable_1_;
	}

	public void setFocus(boolean p_setFocus_1_) {
		if (p_setFocus_1_ && !inFocus) {
			frame = 0;
		}

		inFocus = p_setFocus_1_;
	}

	public boolean isFocused() {
		return inFocus;
	}

	public boolean isIsEditable() {
		return isEditable;
	}

	public void setIsEditable(boolean p_setIsEditable_1_) {
		isEditable = p_setIsEditable_1_;
	}

	public int getHighlightPos() {
		return highlightPos;
	}

	public int getInnerWidth() {
		return isBordered() ? width - 8 : width;
	}

	public void setHighlightPos(int p_setHighlightPos_1_) {
		int j = value.length();

		if (p_setHighlightPos_1_ > j) {
			p_setHighlightPos_1_ = j;
		}

		if (p_setHighlightPos_1_ < 0) {
			p_setHighlightPos_1_ = 0;
		}

		highlightPos = p_setHighlightPos_1_;

		if (font != null) {
			if (displayPos > j) {
				displayPos = j;
			}

			int k = getInnerWidth();
			String s = font.trimStringToWidth(value.substring(displayPos), k);
			int l = s.length() + displayPos;

			if (p_setHighlightPos_1_ == displayPos) {
				displayPos -= font.trimStringToWidth(value, k, true).length();
			}

			if (p_setHighlightPos_1_ > l) {
				displayPos += p_setHighlightPos_1_ - l;
			} else if (p_setHighlightPos_1_ <= displayPos) {
				displayPos -= displayPos - p_setHighlightPos_1_;
			}

			if (displayPos < 0) {
				displayPos = 0;
			}

			if (displayPos > j) {
				displayPos = j;
			}
		}
	}

	public boolean isCanLoseFocus() {
		return canLoseFocus;
	}

	public void setCanLoseFocus(boolean p_setCanLoseFocus_1_) {
		canLoseFocus = p_setCanLoseFocus_1_;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean p_setVisible_1_) {
		visible = p_setVisible_1_;
	}
}