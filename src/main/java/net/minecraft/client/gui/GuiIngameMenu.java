package net.minecraft.client.gui;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.achievement.GuiAchievements;
import net.minecraft.client.gui.achievement.GuiStats;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.resources.I18n;

@SideOnly(Side.CLIENT)
public class GuiIngameMenu extends GuiScreen {
	private int field_146445_a;
	private int field_146444_f;
	private static final String __OBFID = "CL_00000703";

	@Override
	public void initGui() {
		field_146445_a = 0;
		buttonList.clear();
		byte b0 = -16;
		buttonList.add(new GuiButton(1, width / 2 - 100, height / 4 + 120 + b0,
				I18n.format("menu.returnToMenu")));

		if (!mc.isIntegratedServerRunning()) {
			((GuiButton) buttonList.get(0)).displayString = I18n.format("menu.disconnect");
		}

		buttonList.add(new GuiButton(4, width / 2 - 100, height / 4 + 24 + b0,
				I18n.format("menu.returnToGame")));
		buttonList.add(new GuiButton(0, width / 2 - 100, height / 4 + 96 + b0, 98, 20,
				I18n.format("menu.options")));
		buttonList.add(new GuiButton(12, width / 2 + 2, height / 4 + 96 + b0, 98, 20, "Mod Options..."));
		GuiButton guibutton;
		buttonList.add(guibutton = new GuiButton(7, width / 2 - 100, height / 4 + 72 + b0, 200, 20,
				I18n.format("menu.shareToLan")));
		buttonList.add(new GuiButton(5, width / 2 - 100, height / 4 + 48 + b0, 98, 20,
				I18n.format("gui.achievements")));
		buttonList.add(
				new GuiButton(6, width / 2 + 2, height / 4 + 48 + b0, 98, 20, I18n.format("gui.stats")));
		guibutton.enabled = mc.isSingleplayer() && !mc.getIntegratedServer().getPublic();
	}

	@Override
	protected void actionPerformed(GuiButton p_146284_1_) {
		switch (p_146284_1_.id) {
		case 0:
			mc.displayGuiScreen(new GuiOptions(this, mc.gameSettings));
			break;
		case 1:
			p_146284_1_.enabled = false;
			mc.theWorld.sendQuittingDisconnectingPacket();
			mc.loadWorld(null);
			mc.displayGuiScreen(new GuiMainMenu());
		case 2:
		case 3:
		default:
			break;
		case 4:
			mc.displayGuiScreen(null);
			mc.setIngameFocus();
			break;
		case 5:
			if (mc.thePlayer != null) {
				mc.displayGuiScreen(new GuiAchievements(this, mc.thePlayer.getStatFileWriter()));
			}
			break;
		case 6:
			if (mc.thePlayer != null) {
				mc.displayGuiScreen(new GuiStats(this, mc.thePlayer.getStatFileWriter()));
			}
			break;
		case 7:
			mc.displayGuiScreen(new GuiShareToLan(this));
			break;
		case 12:
			FMLClientHandler.instance().showInGameModOptions(this);
			break;
		}
	}

	@Override
	public void updateScreen() {
		super.updateScreen();
		++field_146444_f;
	}

	@Override
	public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
		drawDefaultBackground();
		drawCenteredString(fontRendererObj, I18n.format("menu.game"), width / 2, 40, 16777215);
		super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
	}
}