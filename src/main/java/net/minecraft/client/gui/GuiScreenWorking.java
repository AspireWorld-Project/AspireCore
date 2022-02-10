package net.minecraft.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.IProgressUpdate;

@SideOnly(Side.CLIENT)
public class GuiScreenWorking extends GuiScreen implements IProgressUpdate {
	private String field_146591_a = "";
	private String field_146589_f = "";
	private int field_146590_g;
	private boolean field_146592_h;
	private static final String __OBFID = "CL_00000707";

	@Override
	public void displayProgressMessage(String p_73720_1_) {
		resetProgressAndMessage(p_73720_1_);
	}

	@Override
	public void resetProgressAndMessage(String p_73721_1_) {
		field_146591_a = p_73721_1_;
		resetProgresAndWorkingMessage("Working...");
	}

	@Override
	public void resetProgresAndWorkingMessage(String p_73719_1_) {
		field_146589_f = p_73719_1_;
		setLoadingProgress(0);
	}

	@Override
	public void setLoadingProgress(int p_73718_1_) {
		field_146590_g = p_73718_1_;
	}

	@Override
	public void func_146586_a() {
		field_146592_h = true;
	}

	@Override
	public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
		if (field_146592_h) {
			mc.displayGuiScreen((GuiScreen) null);
		} else {
			drawDefaultBackground();
			drawCenteredString(fontRendererObj, field_146591_a, width / 2, 70, 16777215);
			drawCenteredString(fontRendererObj, field_146589_f + " " + field_146590_g + "%", width / 2, 90, 16777215);
			super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
		}
	}
}