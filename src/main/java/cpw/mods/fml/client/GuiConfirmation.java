package cpw.mods.fml.client;

import cpw.mods.fml.common.StartupQuery;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiOptionButton;
import net.minecraft.client.resources.I18n;

public class GuiConfirmation extends GuiNotification {
	public GuiConfirmation(StartupQuery query) {
		super(query);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initGui() {
		buttonList.add(new GuiOptionButton(0, width / 2 - 155, height - 38, I18n.format("gui.yes")));
		buttonList.add(new GuiOptionButton(1, width / 2 - 155 + 160, height - 38, I18n.format("gui.no")));
	}

	@Override
	protected void actionPerformed(GuiButton button) {
		if (button.enabled && (button.id == 0 || button.id == 1)) {
			FMLClientHandler.instance().showGuiScreen(null);
			query.setResult(button.id == 0);
			query.finish();
		}
	}
}