package net.minecraft.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.opengl.GL11;

import java.util.Iterator;

@SideOnly(Side.CLIENT)
public class GuiGameOver extends GuiScreen implements GuiYesNoCallback {
	private int field_146347_a;
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void initGui() {
		buttonList.clear();

		if (mc.theWorld.getWorldInfo().isHardcoreModeEnabled()) {
			if (mc.isIntegratedServerRunning()) {
				buttonList.add(new GuiButton(1, width / 2 - 100, height / 4 + 96,
						I18n.format("deathScreen.deleteWorld")));
			} else {
				buttonList.add(new GuiButton(1, width / 2 - 100, height / 4 + 96,
						I18n.format("deathScreen.leaveServer")));
			}
		} else {
			buttonList.add(new GuiButton(0, width / 2 - 100, height / 4 + 72,
					I18n.format("deathScreen.respawn")));
			buttonList.add(new GuiButton(1, width / 2 - 100, height / 4 + 96,
					I18n.format("deathScreen.titleScreen")));

			if (mc.getSession() == null) {
				((GuiButton) buttonList.get(1)).enabled = false;
			}
		}

		GuiButton guibutton;

		for (Iterator iterator = buttonList.iterator(); iterator.hasNext(); guibutton.enabled = false) {
			guibutton = (GuiButton) iterator.next();
		}
	}

	@Override
	protected void keyTyped(char p_73869_1_, int p_73869_2_) {
	}

	@Override
	protected void actionPerformed(GuiButton p_146284_1_) {
		switch (p_146284_1_.id) {
		case 0:
			mc.thePlayer.respawnPlayer();
			mc.displayGuiScreen(null);
			break;
		case 1:
			GuiYesNo guiyesno = new GuiYesNo(this, I18n.format("deathScreen.quit.confirm"), "",
					I18n.format("deathScreen.titleScreen"),
					I18n.format("deathScreen.respawn"), 0);
			mc.displayGuiScreen(guiyesno);
			guiyesno.func_146350_a(20);
		}
	}

	@Override
	public void confirmClicked(boolean p_73878_1_, int p_73878_2_) {
		if (p_73878_1_) {
			mc.theWorld.sendQuittingDisconnectingPacket();
			mc.loadWorld(null);
			mc.displayGuiScreen(new GuiMainMenu());
		} else {
			mc.thePlayer.respawnPlayer();
			mc.displayGuiScreen(null);
		}
	}

	@Override
	public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
		drawGradientRect(0, 0, width, height, 1615855616, -1602211792);
		GL11.glPushMatrix();
		GL11.glScalef(2.0F, 2.0F, 2.0F);
		boolean flag = mc.theWorld.getWorldInfo().isHardcoreModeEnabled();
		String s = flag ? I18n.format("deathScreen.title.hardcore")
				: I18n.format("deathScreen.title");
		drawCenteredString(fontRendererObj, s, width / 2 / 2, 30, 16777215);
		GL11.glPopMatrix();

		if (flag) {
			drawCenteredString(fontRendererObj, I18n.format("deathScreen.hardcoreInfo"), width / 2, 144,
					16777215);
		}

		drawCenteredString(fontRendererObj, I18n.format("deathScreen.score") + ": "
				+ EnumChatFormatting.YELLOW + mc.thePlayer.getScore(), width / 2, 100, 16777215);
		super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void updateScreen() {
		super.updateScreen();
		++field_146347_a;
		GuiButton guibutton;

		if (field_146347_a == 20) {
			for (Iterator iterator = buttonList.iterator(); iterator.hasNext(); guibutton.enabled = true) {
				guibutton = (GuiButton) iterator.next();
			}
		}
	}
}