package net.minecraft.client.gui;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;

@SideOnly(Side.CLIENT)
public class GuiOptionSlider extends GuiButton {
	private float field_146134_p;
	public boolean field_146135_o;
	private GameSettings.Options field_146133_q;
	private final float field_146132_r;
	private final float field_146131_s;
	private static final String __OBFID = "CL_00000680";

	public GuiOptionSlider(int p_i45016_1_, int p_i45016_2_, int p_i45016_3_, GameSettings.Options p_i45016_4_) {
		this(p_i45016_1_, p_i45016_2_, p_i45016_3_, p_i45016_4_, 0.0F, 1.0F);
	}

	public GuiOptionSlider(int p_i45017_1_, int p_i45017_2_, int p_i45017_3_, GameSettings.Options p_i45017_4_,
			float p_i45017_5_, float p_i45017_6_) {
		super(p_i45017_1_, p_i45017_2_, p_i45017_3_, 150, 20, "");
		field_146134_p = 1.0F;
		field_146133_q = p_i45017_4_;
		field_146132_r = p_i45017_5_;
		field_146131_s = p_i45017_6_;
		Minecraft minecraft = Minecraft.getMinecraft();
		field_146134_p = p_i45017_4_.normalizeValue(minecraft.gameSettings.getOptionFloatValue(p_i45017_4_));
		displayString = minecraft.gameSettings.getKeyBinding(p_i45017_4_);
	}

	@Override
	public int getHoverState(boolean p_146114_1_) {
		return 0;
	}

	@Override
	protected void mouseDragged(Minecraft p_146119_1_, int p_146119_2_, int p_146119_3_) {
		if (visible) {
			if (field_146135_o) {
				field_146134_p = (float) (p_146119_2_ - (xPosition + 4)) / (float) (width - 8);

				if (field_146134_p < 0.0F) {
					field_146134_p = 0.0F;
				}

				if (field_146134_p > 1.0F) {
					field_146134_p = 1.0F;
				}

				float f = field_146133_q.denormalizeValue(field_146134_p);
				p_146119_1_.gameSettings.setOptionFloatValue(field_146133_q, f);
				field_146134_p = field_146133_q.normalizeValue(f);
				displayString = p_146119_1_.gameSettings.getKeyBinding(field_146133_q);
			}

			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			drawTexturedModalRect(xPosition + (int) (field_146134_p * (width - 8)), yPosition, 0, 66, 4, 20);
			drawTexturedModalRect(xPosition + (int) (field_146134_p * (width - 8)) + 4, yPosition, 196, 66, 4, 20);
		}
	}

	@Override
	public boolean mousePressed(Minecraft p_146116_1_, int p_146116_2_, int p_146116_3_) {
		if (super.mousePressed(p_146116_1_, p_146116_2_, p_146116_3_)) {
			field_146134_p = (float) (p_146116_2_ - (xPosition + 4)) / (float) (width - 8);

			if (field_146134_p < 0.0F) {
				field_146134_p = 0.0F;
			}

			if (field_146134_p > 1.0F) {
				field_146134_p = 1.0F;
			}

			p_146116_1_.gameSettings.setOptionFloatValue(field_146133_q,
					field_146133_q.denormalizeValue(field_146134_p));
			displayString = p_146116_1_.gameSettings.getKeyBinding(field_146133_q);
			field_146135_o = true;
			return true;
		} else
			return false;
	}

	@Override
	public void mouseReleased(int p_146118_1_, int p_146118_2_) {
		field_146135_o = false;
	}
}