package net.minecraft.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiButtonLanguage extends GuiButton {
	public GuiButtonLanguage(int p_i1041_1_, int p_i1041_2_, int p_i1041_3_) {
		super(p_i1041_1_, p_i1041_2_, p_i1041_3_, 20, 20, "");
	}

	@Override
	public void drawButton(Minecraft p_146112_1_, int p_146112_2_, int p_146112_3_) {
		if (visible) {
			p_146112_1_.getTextureManager().bindTexture(GuiButton.buttonTextures);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			boolean flag = p_146112_2_ >= xPosition && p_146112_3_ >= yPosition && p_146112_2_ < xPosition + width
					&& p_146112_3_ < yPosition + height;
			int k = 106;

			if (flag) {
				k += height;
			}

			drawTexturedModalRect(xPosition, yPosition, 0, k, width, height);
		}
	}
}