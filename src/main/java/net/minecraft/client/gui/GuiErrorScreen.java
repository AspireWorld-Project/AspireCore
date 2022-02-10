package net.minecraft.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.resources.I18n;

@SideOnly(Side.CLIENT)
public class GuiErrorScreen extends GuiScreen {
	private String field_146313_a;
	private String field_146312_f;
	private static final String __OBFID = "CL_00000696";

	public GuiErrorScreen(String p_i1034_1_, String p_i1034_2_) {
		field_146313_a = p_i1034_1_;
		field_146312_f = p_i1034_2_;
	}

	@Override
	public void initGui() {
		super.initGui();
		buttonList.add(new GuiButton(0, width / 2 - 100, 140, I18n.format("gui.cancel", new Object[0])));
	}

	@Override
	public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
		drawGradientRect(0, 0, width, height, -12574688, -11530224);
		drawCenteredString(fontRendererObj, field_146313_a, width / 2, 90, 16777215);
		drawCenteredString(fontRendererObj, field_146312_f, width / 2, 110, 16777215);
		super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
	}

	@Override
	protected void keyTyped(char p_73869_1_, int p_73869_2_) {
	}

	@Override
	protected void actionPerformed(GuiButton p_146284_1_) {
		mc.displayGuiScreen((GuiScreen) null);
	}
}