package net.minecraft.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.resources.I18n;

import java.util.Iterator;

@SideOnly(Side.CLIENT)
public class GuiYesNo extends GuiScreen {
	protected GuiYesNoCallback parentScreen;
	protected String field_146351_f;
	private final String field_146354_r;
	protected String confirmButtonText;
	protected String cancelButtonText;
	protected int field_146357_i;
	private int field_146353_s;
	private static final String __OBFID = "CL_00000684";

	public GuiYesNo(GuiYesNoCallback p_i1082_1_, String p_i1082_2_, String p_i1082_3_, int p_i1082_4_) {
		parentScreen = p_i1082_1_;
		field_146351_f = p_i1082_2_;
		field_146354_r = p_i1082_3_;
		field_146357_i = p_i1082_4_;
		confirmButtonText = I18n.format("gui.yes");
		cancelButtonText = I18n.format("gui.no");
	}

	public GuiYesNo(GuiYesNoCallback p_i1083_1_, String p_i1083_2_, String p_i1083_3_, String p_i1083_4_,
			String p_i1083_5_, int p_i1083_6_) {
		parentScreen = p_i1083_1_;
		field_146351_f = p_i1083_2_;
		field_146354_r = p_i1083_3_;
		confirmButtonText = p_i1083_4_;
		cancelButtonText = p_i1083_5_;
		field_146357_i = p_i1083_6_;
	}

	@Override
	public void initGui() {
		buttonList.add(new GuiOptionButton(0, width / 2 - 155, height / 6 + 96, confirmButtonText));
		buttonList.add(new GuiOptionButton(1, width / 2 - 155 + 160, height / 6 + 96, cancelButtonText));
	}

	@Override
	protected void actionPerformed(GuiButton p_146284_1_) {
		parentScreen.confirmClicked(p_146284_1_.id == 0, field_146357_i);
	}

	@Override
	public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
		drawDefaultBackground();
		drawCenteredString(fontRendererObj, field_146351_f, width / 2, 70, 16777215);
		drawCenteredString(fontRendererObj, field_146354_r, width / 2, 90, 16777215);
		super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
	}

	public void func_146350_a(int p_146350_1_) {
		field_146353_s = p_146350_1_;
		GuiButton guibutton;

		for (Iterator iterator = buttonList.iterator(); iterator.hasNext(); guibutton.enabled = false) {
			guibutton = (GuiButton) iterator.next();
		}
	}

	@Override
	public void updateScreen() {
		super.updateScreen();
		GuiButton guibutton;

		if (--field_146353_s == 0) {
			for (Iterator iterator = buttonList.iterator(); iterator.hasNext(); guibutton.enabled = true) {
				guibutton = (GuiButton) iterator.next();
			}
		}
	}
}