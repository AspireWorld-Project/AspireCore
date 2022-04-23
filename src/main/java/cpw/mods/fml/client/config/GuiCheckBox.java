/*
 * Forge Mod Loader
 * Copyright (c) 2012-2014 cpw.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 *
 * Contributors (this class):
 *     bspkrs - implementation
 */

package cpw.mods.fml.client.config;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

/**
 * This class provides a checkbox style control.
 *
 * @author bspkrs
 */
public class GuiCheckBox extends GuiButton {
	private boolean isChecked;
	private final int boxWidth;

	public GuiCheckBox(int id, int xPos, int yPos, String displayString, boolean isChecked) {
		super(id, xPos, yPos, displayString);
		this.isChecked = isChecked;
		boxWidth = 11;
		height = 11;
		width = boxWidth + 2 + Minecraft.getMinecraft().fontRenderer.getStringWidth(displayString);
	}

	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY) {
		if (visible) {
			field_146123_n = mouseX >= xPosition && mouseY >= yPosition && mouseX < xPosition + boxWidth
					&& mouseY < yPosition + height;
			GuiUtils.drawContinuousTexturedBox(buttonTextures, xPosition, yPosition, 0, 46, boxWidth, height, 200, 20,
					2, 3, 2, 2, zLevel);
			mouseDragged(mc, mouseX, mouseY);
			int color = 14737632;

			if (packedFGColour != 0) {
				color = packedFGColour;
			} else if (!enabled) {
				color = 10526880;
			}

			if (isChecked) {
				drawCenteredString(mc.fontRenderer, "x", xPosition + boxWidth / 2 + 1, yPosition + 1, 14737632);
			}

			drawString(mc.fontRenderer, displayString, xPosition + boxWidth + 2, yPosition + 2, color);
		}
	}

	@Override
	public boolean mousePressed(Minecraft p_146116_1_, int p_146116_2_, int p_146116_3_) {
		if (enabled && visible && p_146116_2_ >= xPosition && p_146116_3_ >= yPosition
				&& p_146116_2_ < xPosition + width && p_146116_3_ < yPosition + height) {
			isChecked = !isChecked;
			return true;
		}

		return false;
	}

	public boolean isChecked() {
		return isChecked;
	}

	public void setIsChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}
}