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
import org.lwjgl.opengl.GL11;

/**
 * This class provides a button that shows a string glyph at the beginning. The
 * glyph can be scaled using the glyphScale parameter.
 *
 * @author bspkrs
 */
public class GuiUnicodeGlyphButton extends GuiButtonExt {
	public String glyph;
	public float glyphScale;

	public GuiUnicodeGlyphButton(int id, int xPos, int yPos, int width, int height, String displayString, String glyph,
			float glyphScale) {
		super(id, xPos, yPos, width, height, displayString);
		this.glyph = glyph;
		this.glyphScale = glyphScale;
	}

	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY) {
		if (visible) {
			field_146123_n = mouseX >= xPosition && mouseY >= yPosition && mouseX < xPosition + width
					&& mouseY < yPosition + height;
			int k = getHoverState(field_146123_n);
			GuiUtils.drawContinuousTexturedBox(buttonTextures, xPosition, yPosition, 0, 46 + k * 20, width, height, 200,
					20, 2, 3, 2, 2, zLevel);
			mouseDragged(mc, mouseX, mouseY);
			int color = 14737632;

			if (packedFGColour != 0) {
				color = packedFGColour;
			} else if (!enabled) {
				color = 10526880;
			} else if (field_146123_n) {
				color = 16777120;
			}

			String buttonText = displayString;
			int glyphWidth = (int) (mc.fontRenderer.getStringWidth(glyph) * glyphScale);
			int strWidth = mc.fontRenderer.getStringWidth(buttonText);
			int elipsisWidth = mc.fontRenderer.getStringWidth("...");
			int totalWidth = strWidth + glyphWidth;

			if (totalWidth > width - 6 && totalWidth > elipsisWidth) {
				buttonText = mc.fontRenderer.trimStringToWidth(buttonText, width - 6 - elipsisWidth).trim() + "...";
			}

			strWidth = mc.fontRenderer.getStringWidth(buttonText);
			totalWidth = glyphWidth + strWidth;

			GL11.glPushMatrix();
			GL11.glScalef(glyphScale, glyphScale, 1.0F);
			drawCenteredString(mc.fontRenderer, glyph,
					(int) ((xPosition + width / 2 - strWidth / 2) / glyphScale - glyphWidth / (2 * glyphScale) + 2),
					(int) ((yPosition + (height - 8) / glyphScale / 2 - 1) / glyphScale), color);
			GL11.glPopMatrix();

			drawCenteredString(mc.fontRenderer, buttonText, (int) (xPosition + width / 2 + glyphWidth / glyphScale),
					yPosition + (height - 8) / 2, color);
		}
	}
}