package net.minecraft.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;

import java.net.URI;

@SideOnly(Side.CLIENT)
public class GuiScreenDemo extends GuiScreen {
	private static final Logger logger = LogManager.getLogger();
	private static final ResourceLocation field_146348_f = new ResourceLocation("textures/gui/demo_background.png");
	@SuppressWarnings("unchecked")
	@Override
	public void initGui() {
		buttonList.clear();
		byte b0 = -16;
		buttonList.add(new GuiButton(1, width / 2 - 116, height / 2 + 62 + b0, 114, 20,
				I18n.format("demo.help.buy")));
		buttonList.add(new GuiButton(2, width / 2 + 2, height / 2 + 62 + b0, 114, 20,
				I18n.format("demo.help.later")));
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected void actionPerformed(GuiButton p_146284_1_) {
		switch (p_146284_1_.id) {
		case 1:
			p_146284_1_.enabled = false;

			try {
				Class oclass = Class.forName("java.awt.Desktop");
				Object object = oclass.getMethod("getDesktop", new Class[0]).invoke(null);
				oclass.getMethod("browse", new Class[] { URI.class }).invoke(object,
						new URI("http://www.minecraft.net/store?source=demo"));
			} catch (Throwable throwable) {
				logger.error("Couldn't open link", throwable);
			}

			break;
		case 2:
			mc.displayGuiScreen(null);
			mc.setIngameFocus();
		}
	}

	@Override
	public void updateScreen() {
		super.updateScreen();
	}

	@Override
	public void drawDefaultBackground() {
		super.drawDefaultBackground();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(field_146348_f);
		int i = (width - 248) / 2;
		int j = (height - 166) / 2;
		drawTexturedModalRect(i, j, 0, 0, 248, 166);
	}

	@Override
	public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
		drawDefaultBackground();
		int k = (width - 248) / 2 + 10;
		int l = (height - 166) / 2 + 8;
		fontRendererObj.drawString(I18n.format("demo.help.title"), k, l, 2039583);
		l += 12;
		GameSettings gamesettings = mc.gameSettings;
		fontRendererObj.drawString(
				I18n.format("demo.help.movementShort",
						GameSettings.getKeyDisplayString(gamesettings.keyBindForward.getKeyCode()),
						GameSettings.getKeyDisplayString(gamesettings.keyBindLeft.getKeyCode()),
						GameSettings.getKeyDisplayString(gamesettings.keyBindBack.getKeyCode()),
						GameSettings.getKeyDisplayString(gamesettings.keyBindRight.getKeyCode())),
				k, l, 5197647);
		fontRendererObj.drawString(I18n.format("demo.help.movementMouse"), k, l + 12, 5197647);
		fontRendererObj
				.drawString(
						I18n.format("demo.help.jump",
								GameSettings.getKeyDisplayString(gamesettings.keyBindJump.getKeyCode())),
						k, l + 24, 5197647);
		fontRendererObj
				.drawString(
						I18n.format("demo.help.inventory",
								GameSettings.getKeyDisplayString(gamesettings.keyBindInventory.getKeyCode())),
						k, l + 36, 5197647);
		fontRendererObj.drawSplitString(I18n.format("demo.help.fullWrapped"), k, l + 68, 218, 2039583);
		super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
	}
}