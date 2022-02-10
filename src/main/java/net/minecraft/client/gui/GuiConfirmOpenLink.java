package net.minecraft.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.resources.I18n;

@SideOnly(Side.CLIENT)
public class GuiConfirmOpenLink extends GuiYesNo {
	private final String openLinkWarning;
	private final String copyLinkButtonText;
	private final String field_146361_t;
	private boolean field_146360_u = true;
	private static final String __OBFID = "CL_00000683";

	public GuiConfirmOpenLink(GuiYesNoCallback p_i1084_1_, String p_i1084_2_, int p_i1084_3_, boolean p_i1084_4_) {
		super(p_i1084_1_, I18n.format(p_i1084_4_ ? "chat.link.confirmTrusted" : "chat.link.confirm", new Object[0]),
				p_i1084_2_, p_i1084_3_);
		confirmButtonText = I18n.format(p_i1084_4_ ? "chat.link.open" : "gui.yes", new Object[0]);
		cancelButtonText = I18n.format(p_i1084_4_ ? "gui.cancel" : "gui.no", new Object[0]);
		copyLinkButtonText = I18n.format("chat.copy", new Object[0]);
		openLinkWarning = I18n.format("chat.link.warning", new Object[0]);
		field_146361_t = p_i1084_2_;
	}

	@Override
	public void initGui() {
		buttonList.add(new GuiButton(0, width / 3 - 83 + 0, height / 6 + 96, 100, 20, confirmButtonText));
		buttonList.add(new GuiButton(2, width / 3 - 83 + 105, height / 6 + 96, 100, 20, copyLinkButtonText));
		buttonList.add(new GuiButton(1, width / 3 - 83 + 210, height / 6 + 96, 100, 20, cancelButtonText));
	}

	@Override
	protected void actionPerformed(GuiButton p_146284_1_) {
		if (p_146284_1_.id == 2) {
			copyLinkToClipboard();
		}

		parentScreen.confirmClicked(p_146284_1_.id == 0, field_146357_i);
	}

	public void copyLinkToClipboard() {
		setClipboardString(field_146361_t);
	}

	@Override
	public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
		super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);

		if (field_146360_u) {
			drawCenteredString(fontRendererObj, openLinkWarning, width / 2, 110, 16764108);
		}
	}

	public void func_146358_g() {
		field_146360_u = false;
	}
}