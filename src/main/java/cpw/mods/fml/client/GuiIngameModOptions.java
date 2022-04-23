package cpw.mods.fml.client;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

public class GuiIngameModOptions extends GuiScreen {
	private final GuiScreen parentScreen;
	protected String title = "Mod Options";
	private GuiModOptionList optionList;

	public GuiIngameModOptions(GuiScreen parentScreen) {
		this.parentScreen = parentScreen;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initGui() {
		optionList = new GuiModOptionList(this);
		optionList.registerScrollButtons(buttonList, 7, 8);
		buttonList.add(new GuiButton(200, width / 2 - 100, height / 6 + 168, I18n.format("gui.done")));
	}

	@Override
	protected void actionPerformed(GuiButton p_146284_1_) {
		if (p_146284_1_.enabled) {
			if (p_146284_1_.id == 200) {
				mc.gameSettings.saveOptions();
				mc.displayGuiScreen(parentScreen);
			}
		}
	}

	@Override
	public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
		// force a non-transparent background
		drawDefaultBackground();
		optionList.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
		drawCenteredString(fontRendererObj, title, width / 2, 15, 0xFFFFFF);
		super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
	}

	FontRenderer getFontRenderer() {
		return fontRendererObj;
	}

}