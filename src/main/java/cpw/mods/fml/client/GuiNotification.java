package cpw.mods.fml.client;

import cpw.mods.fml.common.StartupQuery;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

public class GuiNotification extends GuiScreen {
	public GuiNotification(StartupQuery query) {
		this.query = query;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initGui() {
		buttonList.add(new GuiButton(0, width / 2 - 100, height - 38, I18n.format("gui.done")));
	}

	@Override
	protected void actionPerformed(GuiButton button) {
		if (button.enabled && button.id == 0) {
			FMLClientHandler.instance().showGuiScreen(null);
			query.finish();
		}
	}

	@Override
	public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
		drawDefaultBackground();

		String[] lines = query.getText().split("\n");

		int spaceAvailable = height - 38 - 20;
		int spaceRequired = Math.min(spaceAvailable, 10 + 10 * lines.length);

		int offset = 10 + (spaceAvailable - spaceRequired) / 2; // vertically centered

		for (String line : lines) {
			if (offset >= spaceAvailable) {
				drawCenteredString(fontRendererObj, "...", width / 2, offset, 0xFFFFFF);
				break;
			} else {
				if (!line.isEmpty()) {
					drawCenteredString(fontRendererObj, line, width / 2, offset, 0xFFFFFF);
				}
				offset += 10;
			}
		}

		super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
	}

	protected final StartupQuery query;
}