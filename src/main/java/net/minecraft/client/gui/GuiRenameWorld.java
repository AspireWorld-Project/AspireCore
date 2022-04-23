package net.minecraft.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.resources.I18n;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.WorldInfo;
import org.lwjgl.input.Keyboard;

@SideOnly(Side.CLIENT)
public class GuiRenameWorld extends GuiScreen {
	private GuiScreen field_146585_a;
	private GuiTextField field_146583_f;
	private final String field_146584_g;
	private static final String __OBFID = "CL_00000709";

	public GuiRenameWorld(GuiScreen p_i1050_1_, String p_i1050_2_) {
		field_146585_a = p_i1050_1_;
		field_146584_g = p_i1050_2_;
	}

	@Override
	public void updateScreen() {
		field_146583_f.updateCursorCounter();
	}

	@Override
	public void initGui() {
		Keyboard.enableRepeatEvents(true);
		buttonList.clear();
		buttonList.add(new GuiButton(0, width / 2 - 100, height / 4 + 96 + 12,
				I18n.format("selectWorld.renameButton", new Object[0])));
		buttonList.add(
				new GuiButton(1, width / 2 - 100, height / 4 + 120 + 12, I18n.format("gui.cancel", new Object[0])));
		ISaveFormat isaveformat = mc.getSaveLoader();
		WorldInfo worldinfo = isaveformat.getWorldInfo(field_146584_g);
		String s = worldinfo.getWorldName();
		field_146583_f = new GuiTextField(fontRendererObj, width / 2 - 100, 60, 200, 20);
		field_146583_f.setFocused(true);
		field_146583_f.setText(s);
	}

	@Override
	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
	}

	@Override
	protected void actionPerformed(GuiButton p_146284_1_) {
		if (p_146284_1_.enabled) {
			if (p_146284_1_.id == 1) {
				mc.displayGuiScreen(field_146585_a);
			} else if (p_146284_1_.id == 0) {
				ISaveFormat isaveformat = mc.getSaveLoader();
				isaveformat.renameWorld(field_146584_g, field_146583_f.getText().trim());
				mc.displayGuiScreen(field_146585_a);
			}
		}
	}

	@Override
	protected void keyTyped(char p_73869_1_, int p_73869_2_) {
		field_146583_f.textboxKeyTyped(p_73869_1_, p_73869_2_);
		((GuiButton) buttonList.get(0)).enabled = field_146583_f.getText().trim().length() > 0;

		if (p_73869_2_ == 28 || p_73869_2_ == 156) {
			actionPerformed((GuiButton) buttonList.get(0));
		}
	}

	@Override
	protected void mouseClicked(int p_73864_1_, int p_73864_2_, int p_73864_3_) {
		super.mouseClicked(p_73864_1_, p_73864_2_, p_73864_3_);
		field_146583_f.mouseClicked(p_73864_1_, p_73864_2_, p_73864_3_);
	}

	@Override
	public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
		drawDefaultBackground();
		drawCenteredString(fontRendererObj, I18n.format("selectWorld.renameTitle", new Object[0]), width / 2, 20,
				16777215);
		drawString(fontRendererObj, I18n.format("selectWorld.enterName", new Object[0]), width / 2 - 100, 47, 10526880);
		field_146583_f.drawTextBox();
		super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
	}
}