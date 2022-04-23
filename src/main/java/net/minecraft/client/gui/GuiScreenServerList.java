package net.minecraft.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.resources.I18n;
import org.lwjgl.input.Keyboard;

@SideOnly(Side.CLIENT)
public class GuiScreenServerList extends GuiScreen {
	private final GuiScreen field_146303_a;
	private final ServerData field_146301_f;
	private GuiTextField field_146302_g;
	private static final String __OBFID = "CL_00000692";

	public GuiScreenServerList(GuiScreen p_i1031_1_, ServerData p_i1031_2_) {
		field_146303_a = p_i1031_1_;
		field_146301_f = p_i1031_2_;
	}

	@Override
	public void updateScreen() {
		field_146302_g.updateCursorCounter();
	}

	@Override
	public void initGui() {
		Keyboard.enableRepeatEvents(true);
		buttonList.clear();
		buttonList.add(new GuiButton(0, width / 2 - 100, height / 4 + 96 + 12,
				I18n.format("selectServer.select")));
		buttonList.add(
				new GuiButton(1, width / 2 - 100, height / 4 + 120 + 12, I18n.format("gui.cancel")));
		field_146302_g = new GuiTextField(fontRendererObj, width / 2 - 100, 116, 200, 20);
		field_146302_g.setMaxStringLength(128);
		field_146302_g.setFocused(true);
		field_146302_g.setText(mc.gameSettings.lastServer);
		((GuiButton) buttonList.get(0)).enabled = field_146302_g.getText().length() > 0
				&& field_146302_g.getText().split(":").length > 0;
	}

	@Override
	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
		mc.gameSettings.lastServer = field_146302_g.getText();
		mc.gameSettings.saveOptions();
	}

	@Override
	protected void actionPerformed(GuiButton p_146284_1_) {
		if (p_146284_1_.enabled) {
			if (p_146284_1_.id == 1) {
				field_146303_a.confirmClicked(false, 0);
			} else if (p_146284_1_.id == 0) {
				field_146301_f.serverIP = field_146302_g.getText();
				field_146303_a.confirmClicked(true, 0);
			}
		}
	}

	@Override
	protected void keyTyped(char p_73869_1_, int p_73869_2_) {
		if (field_146302_g.textboxKeyTyped(p_73869_1_, p_73869_2_)) {
			((GuiButton) buttonList.get(0)).enabled = field_146302_g.getText().length() > 0
					&& field_146302_g.getText().split(":").length > 0;
		} else if (p_73869_2_ == 28 || p_73869_2_ == 156) {
			actionPerformed((GuiButton) buttonList.get(0));
		}
	}

	@Override
	protected void mouseClicked(int p_73864_1_, int p_73864_2_, int p_73864_3_) {
		super.mouseClicked(p_73864_1_, p_73864_2_, p_73864_3_);
		field_146302_g.mouseClicked(p_73864_1_, p_73864_2_, p_73864_3_);
	}

	@Override
	public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
		drawDefaultBackground();
		drawCenteredString(fontRendererObj, I18n.format("selectServer.direct"), width / 2, 20, 16777215);
		drawString(fontRendererObj, I18n.format("addServer.enterIp"), width / 2 - 100, 100, 10526880);
		field_146302_g.drawTextBox();
		super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
	}
}