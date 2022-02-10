package net.minecraft.client.gui;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class GuiButton extends Gui {
	protected static final ResourceLocation buttonTextures = new ResourceLocation("textures/gui/widgets.png");
	public int width;
	public int height;
	public int xPosition;
	public int yPosition;
	public String displayString;
	public int id;
	public boolean enabled;
	public boolean visible;
	protected boolean field_146123_n;
	private static final String __OBFID = "CL_00000668";
	public int packedFGColour;

	public GuiButton(int p_i1020_1_, int p_i1020_2_, int p_i1020_3_, String p_i1020_4_) {
		this(p_i1020_1_, p_i1020_2_, p_i1020_3_, 200, 20, p_i1020_4_);
	}

	public GuiButton(int p_i1021_1_, int p_i1021_2_, int p_i1021_3_, int p_i1021_4_, int p_i1021_5_,
			String p_i1021_6_) {
		width = 200;
		height = 20;
		enabled = true;
		visible = true;
		id = p_i1021_1_;
		xPosition = p_i1021_2_;
		yPosition = p_i1021_3_;
		width = p_i1021_4_;
		height = p_i1021_5_;
		displayString = p_i1021_6_;
	}

	public int getHoverState(boolean p_146114_1_) {
		byte b0 = 1;

		if (!enabled) {
			b0 = 0;
		} else if (p_146114_1_) {
			b0 = 2;
		}

		return b0;
	}

	public void drawButton(Minecraft p_146112_1_, int p_146112_2_, int p_146112_3_) {
		if (visible) {
			FontRenderer fontrenderer = p_146112_1_.fontRenderer;
			p_146112_1_.getTextureManager().bindTexture(buttonTextures);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			field_146123_n = p_146112_2_ >= xPosition && p_146112_3_ >= yPosition && p_146112_2_ < xPosition + width
					&& p_146112_3_ < yPosition + height;
			int k = getHoverState(field_146123_n);
			GL11.glEnable(GL11.GL_BLEND);
			OpenGlHelper.glBlendFunc(770, 771, 1, 0);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			drawTexturedModalRect(xPosition, yPosition, 0, 46 + k * 20, width / 2, height);
			drawTexturedModalRect(xPosition + width / 2, yPosition, 200 - width / 2, 46 + k * 20, width / 2, height);
			mouseDragged(p_146112_1_, p_146112_2_, p_146112_3_);
			int l = 14737632;

			if (packedFGColour != 0) {
				l = packedFGColour;
			} else if (!enabled) {
				l = 10526880;
			} else if (field_146123_n) {
				l = 16777120;
			}

			drawCenteredString(fontrenderer, displayString, xPosition + width / 2, yPosition + (height - 8) / 2, l);
		}
	}

	protected void mouseDragged(Minecraft p_146119_1_, int p_146119_2_, int p_146119_3_) {
	}

	public void mouseReleased(int p_146118_1_, int p_146118_2_) {
	}

	public boolean mousePressed(Minecraft p_146116_1_, int p_146116_2_, int p_146116_3_) {
		return enabled && visible && p_146116_2_ >= xPosition && p_146116_3_ >= yPosition
				&& p_146116_2_ < xPosition + width && p_146116_3_ < yPosition + height;
	}

	public boolean func_146115_a() {
		return field_146123_n;
	}

	public void func_146111_b(int p_146111_1_, int p_146111_2_) {
	}

	public void func_146113_a(SoundHandler p_146113_1_) {
		p_146113_1_.playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0F));
	}

	public int getButtonWidth() {
		return width;
	}

	public int func_154310_c() {
		return height;
	}
}