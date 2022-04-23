package net.minecraft.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ChatAllowedCharacters;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiTextField extends Gui {
	private final FontRenderer field_146211_a;
	public int xPosition;
	public int yPosition;
	public int width;
	public int height;
	private String text = "";
	private int maxStringLength = 32;
	private int cursorCounter;
	private boolean enableBackgroundDrawing = true;
	private boolean canLoseFocus = true;
	private boolean isFocused;
	private boolean isEnabled = true;
	private int lineScrollOffset;
	private int cursorPosition;
	private int selectionEnd;
	private int enabledColor = 14737632;
	private int disabledColor = 7368816;
	private boolean visible = true;
	public GuiTextField(FontRenderer p_i1032_1_, int p_i1032_2_, int p_i1032_3_, int p_i1032_4_, int p_i1032_5_) {
		field_146211_a = p_i1032_1_;
		xPosition = p_i1032_2_;
		yPosition = p_i1032_3_;
		width = p_i1032_4_;
		height = p_i1032_5_;
	}

	public void updateCursorCounter() {
		++cursorCounter;
	}

	public void setText(String p_146180_1_) {
		if (p_146180_1_.length() > maxStringLength) {
			text = p_146180_1_.substring(0, maxStringLength);
		} else {
			text = p_146180_1_;
		}

		setCursorPositionEnd();
	}

	public String getText() {
		return text;
	}

	public String getSelectedText() {
		int i = cursorPosition < selectionEnd ? cursorPosition : selectionEnd;
		int j = cursorPosition < selectionEnd ? selectionEnd : cursorPosition;
		return text.substring(i, j);
	}

	public void writeText(String p_146191_1_) {
		String s1 = "";
		String s2 = ChatAllowedCharacters.filerAllowedCharacters(p_146191_1_);
		int i = cursorPosition < selectionEnd ? cursorPosition : selectionEnd;
		int j = cursorPosition < selectionEnd ? selectionEnd : cursorPosition;
		int k = maxStringLength - text.length() - (i - selectionEnd);
		if (text.length() > 0) {
			s1 = s1 + text.substring(0, i);
		}

		int l;

		if (k < s2.length()) {
			s1 = s1 + s2.substring(0, k);
			l = k;
		} else {
			s1 = s1 + s2;
			l = s2.length();
		}

		if (text.length() > 0 && j < text.length()) {
			s1 = s1 + text.substring(j);
		}

		text = s1;
		moveCursorBy(i - selectionEnd + l);
	}

	public void deleteWords(int p_146177_1_) {
		if (text.length() != 0) {
			if (selectionEnd != cursorPosition) {
				writeText("");
			} else {
				deleteFromCursor(getNthWordFromCursor(p_146177_1_) - cursorPosition);
			}
		}
	}

	public void deleteFromCursor(int p_146175_1_) {
		if (text.length() != 0) {
			if (selectionEnd != cursorPosition) {
				writeText("");
			} else {
				boolean flag = p_146175_1_ < 0;
				int j = flag ? cursorPosition + p_146175_1_ : cursorPosition;
				int k = flag ? cursorPosition : cursorPosition + p_146175_1_;
				String s = "";

				if (j >= 0) {
					s = text.substring(0, j);
				}

				if (k < text.length()) {
					s = s + text.substring(k);
				}

				text = s;

				if (flag) {
					moveCursorBy(p_146175_1_);
				}
			}
		}
	}

	public int getNthWordFromCursor(int p_146187_1_) {
		return getNthWordFromPos(p_146187_1_, getCursorPosition());
	}

	public int getNthWordFromPos(int p_146183_1_, int p_146183_2_) {
		return func_146197_a(p_146183_1_, getCursorPosition(), true);
	}

	public int func_146197_a(int p_146197_1_, int p_146197_2_, boolean p_146197_3_) {
		int k = p_146197_2_;
		boolean flag1 = p_146197_1_ < 0;
		int l = Math.abs(p_146197_1_);

		for (int i1 = 0; i1 < l; ++i1) {
			if (flag1) {
				while (p_146197_3_ && k > 0 && text.charAt(k - 1) == 32) {
					--k;
				}

				while (k > 0 && text.charAt(k - 1) != 32) {
					--k;
				}
			} else {
				int j1 = text.length();
				k = text.indexOf(32, k);

				if (k == -1) {
					k = j1;
				} else {
					while (p_146197_3_ && k < j1 && text.charAt(k) == 32) {
						++k;
					}
				}
			}
		}

		return k;
	}

	public void moveCursorBy(int p_146182_1_) {
		setCursorPosition(selectionEnd + p_146182_1_);
	}

	public void setCursorPosition(int p_146190_1_) {
		cursorPosition = p_146190_1_;
		int j = text.length();

		if (cursorPosition < 0) {
			cursorPosition = 0;
		}

		if (cursorPosition > j) {
			cursorPosition = j;
		}

		setSelectionPos(cursorPosition);
	}

	public void setCursorPositionZero() {
		setCursorPosition(0);
	}

	public void setCursorPositionEnd() {
		setCursorPosition(text.length());
	}

	public boolean textboxKeyTyped(char p_146201_1_, int p_146201_2_) {
		if (!isFocused)
			return false;
		else {
			switch (p_146201_1_) {
			case 1:
				setCursorPositionEnd();
				setSelectionPos(0);
				return true;
			case 3:
				GuiScreen.setClipboardString(getSelectedText());
				return true;
			case 22:
				if (isEnabled) {
					writeText(GuiScreen.getClipboardString());
				}

				return true;
			case 24:
				GuiScreen.setClipboardString(getSelectedText());

				if (isEnabled) {
					writeText("");
				}

				return true;
			default:
				switch (p_146201_2_) {
				case 14:
					if (GuiScreen.isCtrlKeyDown()) {
						if (isEnabled) {
							deleteWords(-1);
						}
					} else if (isEnabled) {
						deleteFromCursor(-1);
					}

					return true;
				case 199:
					if (GuiScreen.isShiftKeyDown()) {
						setSelectionPos(0);
					} else {
						setCursorPositionZero();
					}

					return true;
				case 203:
					if (GuiScreen.isShiftKeyDown()) {
						if (GuiScreen.isCtrlKeyDown()) {
							setSelectionPos(getNthWordFromPos(-1, getSelectionEnd()));
						} else {
							setSelectionPos(getSelectionEnd() - 1);
						}
					} else if (GuiScreen.isCtrlKeyDown()) {
						setCursorPosition(getNthWordFromCursor(-1));
					} else {
						moveCursorBy(-1);
					}

					return true;
				case 205:
					if (GuiScreen.isShiftKeyDown()) {
						if (GuiScreen.isCtrlKeyDown()) {
							setSelectionPos(getNthWordFromPos(1, getSelectionEnd()));
						} else {
							setSelectionPos(getSelectionEnd() + 1);
						}
					} else if (GuiScreen.isCtrlKeyDown()) {
						setCursorPosition(getNthWordFromCursor(1));
					} else {
						moveCursorBy(1);
					}

					return true;
				case 207:
					if (GuiScreen.isShiftKeyDown()) {
						setSelectionPos(text.length());
					} else {
						setCursorPositionEnd();
					}

					return true;
				case 211:
					if (GuiScreen.isCtrlKeyDown()) {
						if (isEnabled) {
							deleteWords(1);
						}
					} else if (isEnabled) {
						deleteFromCursor(1);
					}

					return true;
				default:
					if (ChatAllowedCharacters.isAllowedCharacter(p_146201_1_)) {
						if (isEnabled) {
							writeText(Character.toString(p_146201_1_));
						}

						return true;
					} else
						return false;
				}
			}
		}
	}

	public void mouseClicked(int p_146192_1_, int p_146192_2_, int p_146192_3_) {
		boolean flag = p_146192_1_ >= xPosition && p_146192_1_ < xPosition + width && p_146192_2_ >= yPosition
				&& p_146192_2_ < yPosition + height;

		if (canLoseFocus) {
			setFocused(flag);
		}

		if (isFocused && p_146192_3_ == 0) {
			int l = p_146192_1_ - xPosition;

			if (enableBackgroundDrawing) {
				l -= 4;
			}

			String s = field_146211_a.trimStringToWidth(text.substring(lineScrollOffset), getWidth());
			setCursorPosition(field_146211_a.trimStringToWidth(s, l).length() + lineScrollOffset);
		}
	}

	public void drawTextBox() {
		if (getVisible()) {
			if (getEnableBackgroundDrawing()) {
				drawRect(xPosition - 1, yPosition - 1, xPosition + width + 1, yPosition + height + 1, -6250336);
				drawRect(xPosition, yPosition, xPosition + width, yPosition + height, -16777216);
			}

			int i = isEnabled ? enabledColor : disabledColor;
			int j = cursorPosition - lineScrollOffset;
			int k = selectionEnd - lineScrollOffset;
			String s = field_146211_a.trimStringToWidth(text.substring(lineScrollOffset), getWidth());
			boolean flag = j >= 0 && j <= s.length();
			boolean flag1 = isFocused && cursorCounter / 6 % 2 == 0 && flag;
			int l = enableBackgroundDrawing ? xPosition + 4 : xPosition;
			int i1 = enableBackgroundDrawing ? yPosition + (height - 8) / 2 : yPosition;
			int j1 = l;

			if (k > s.length()) {
				k = s.length();
			}

			if (s.length() > 0) {
				String s1 = flag ? s.substring(0, j) : s;
				j1 = field_146211_a.drawStringWithShadow(s1, l, i1, i);
			}

			boolean flag2 = cursorPosition < text.length() || text.length() >= getMaxStringLength();
			int k1 = j1;

			if (!flag) {
				k1 = j > 0 ? l + width : l;
			} else if (flag2) {
				k1 = j1 - 1;
				--j1;
			}

			if (s.length() > 0 && flag && j < s.length()) {
				field_146211_a.drawStringWithShadow(s.substring(j), j1, i1, i);
			}

			if (flag1) {
				if (flag2) {
					Gui.drawRect(k1, i1 - 1, k1 + 1, i1 + 1 + field_146211_a.FONT_HEIGHT, -3092272);
				} else {
					field_146211_a.drawStringWithShadow("_", k1, i1, i);
				}
			}

			if (k != j) {
				int l1 = l + field_146211_a.getStringWidth(s.substring(0, k));
				drawCursorVertical(k1, i1 - 1, l1 - 1, i1 + 1 + field_146211_a.FONT_HEIGHT);
			}
		}
	}

	private void drawCursorVertical(int p_146188_1_, int p_146188_2_, int p_146188_3_, int p_146188_4_) {
		int i1;

		if (p_146188_1_ < p_146188_3_) {
			i1 = p_146188_1_;
			p_146188_1_ = p_146188_3_;
			p_146188_3_ = i1;
		}

		if (p_146188_2_ < p_146188_4_) {
			i1 = p_146188_2_;
			p_146188_2_ = p_146188_4_;
			p_146188_4_ = i1;
		}

		if (p_146188_3_ > xPosition + width) {
			p_146188_3_ = xPosition + width;
		}

		if (p_146188_1_ > xPosition + width) {
			p_146188_1_ = xPosition + width;
		}

		Tessellator tessellator = Tessellator.instance;
		GL11.glColor4f(0.0F, 0.0F, 255.0F, 255.0F);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_COLOR_LOGIC_OP);
		GL11.glLogicOp(GL11.GL_OR_REVERSE);
		tessellator.startDrawingQuads();
		tessellator.addVertex(p_146188_1_, p_146188_4_, 0.0D);
		tessellator.addVertex(p_146188_3_, p_146188_4_, 0.0D);
		tessellator.addVertex(p_146188_3_, p_146188_2_, 0.0D);
		tessellator.addVertex(p_146188_1_, p_146188_2_, 0.0D);
		tessellator.draw();
		GL11.glDisable(GL11.GL_COLOR_LOGIC_OP);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}

	public void setMaxStringLength(int p_146203_1_) {
		maxStringLength = p_146203_1_;

		if (text.length() > p_146203_1_) {
			text = text.substring(0, p_146203_1_);
		}
	}

	public int getMaxStringLength() {
		return maxStringLength;
	}

	public int getCursorPosition() {
		return cursorPosition;
	}

	public boolean getEnableBackgroundDrawing() {
		return enableBackgroundDrawing;
	}

	public void setEnableBackgroundDrawing(boolean p_146185_1_) {
		enableBackgroundDrawing = p_146185_1_;
	}

	public void setTextColor(int p_146193_1_) {
		enabledColor = p_146193_1_;
	}

	public void setDisabledTextColour(int p_146204_1_) {
		disabledColor = p_146204_1_;
	}

	public void setFocused(boolean p_146195_1_) {
		if (p_146195_1_ && !isFocused) {
			cursorCounter = 0;
		}

		isFocused = p_146195_1_;
	}

	public boolean isFocused() {
		return isFocused;
	}

	public void setEnabled(boolean p_146184_1_) {
		isEnabled = p_146184_1_;
	}

	public int getSelectionEnd() {
		return selectionEnd;
	}

	public int getWidth() {
		return getEnableBackgroundDrawing() ? width - 8 : width;
	}

	public void setSelectionPos(int p_146199_1_) {
		int j = text.length();

		if (p_146199_1_ > j) {
			p_146199_1_ = j;
		}

		if (p_146199_1_ < 0) {
			p_146199_1_ = 0;
		}

		selectionEnd = p_146199_1_;

		if (field_146211_a != null) {
			if (lineScrollOffset > j) {
				lineScrollOffset = j;
			}

			int k = getWidth();
			String s = field_146211_a.trimStringToWidth(text.substring(lineScrollOffset), k);
			int l = s.length() + lineScrollOffset;

			if (p_146199_1_ == lineScrollOffset) {
				lineScrollOffset -= field_146211_a.trimStringToWidth(text, k, true).length();
			}

			if (p_146199_1_ > l) {
				lineScrollOffset += p_146199_1_ - l;
			} else if (p_146199_1_ <= lineScrollOffset) {
				lineScrollOffset -= lineScrollOffset - p_146199_1_;
			}

			if (lineScrollOffset < 0) {
				lineScrollOffset = 0;
			}

			if (lineScrollOffset > j) {
				lineScrollOffset = j;
			}
		}
	}

	public void setCanLoseFocus(boolean p_146205_1_) {
		canLoseFocus = p_146205_1_;
	}

	public boolean getVisible() {
		return visible;
	}

	public void setVisible(boolean p_146189_1_) {
		visible = p_146189_1_;
	}
}