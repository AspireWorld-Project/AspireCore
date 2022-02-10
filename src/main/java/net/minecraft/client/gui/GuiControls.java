package net.minecraft.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;

@SideOnly(Side.CLIENT)
public class GuiControls extends GuiScreen {
	private static final GameSettings.Options[] field_146492_g = new GameSettings.Options[] {
			GameSettings.Options.INVERT_MOUSE, GameSettings.Options.SENSITIVITY, GameSettings.Options.TOUCHSCREEN };
	private GuiScreen parentScreen;
	protected String field_146495_a = "Controls";
	private GameSettings options;
	public KeyBinding buttonId = null;
	public long field_152177_g;
	private GuiKeyBindingList keyBindingList;
	private GuiButton field_146493_s;
	private static final String __OBFID = "CL_00000736";

	public GuiControls(GuiScreen p_i1027_1_, GameSettings p_i1027_2_) {
		parentScreen = p_i1027_1_;
		options = p_i1027_2_;
	}

	@Override
	public void initGui() {
		keyBindingList = new GuiKeyBindingList(this, mc);
		buttonList
				.add(new GuiButton(200, width / 2 - 155, height - 29, 150, 20, I18n.format("gui.done", new Object[0])));
		buttonList.add(field_146493_s = new GuiButton(201, width / 2 - 155 + 160, height - 29, 150, 20,
				I18n.format("controls.resetAll", new Object[0])));
		field_146495_a = I18n.format("controls.title", new Object[0]);
		int i = 0;
		GameSettings.Options[] aoptions = field_146492_g;
		int j = aoptions.length;

		for (int k = 0; k < j; ++k) {
			GameSettings.Options options = aoptions[k];

			if (options.getEnumFloat()) {
				buttonList.add(new GuiOptionSlider(options.returnEnumOrdinal(), width / 2 - 155 + i % 2 * 160,
						18 + 24 * (i >> 1), options));
			} else {
				buttonList.add(new GuiOptionButton(options.returnEnumOrdinal(), width / 2 - 155 + i % 2 * 160,
						18 + 24 * (i >> 1), options, this.options.getKeyBinding(options)));
			}

			++i;
		}
	}

	@Override
	protected void actionPerformed(GuiButton p_146284_1_) {
		if (p_146284_1_.id == 200) {
			mc.displayGuiScreen(parentScreen);
		} else if (p_146284_1_.id == 201) {
			KeyBinding[] akeybinding = mc.gameSettings.keyBindings;
			int i = akeybinding.length;

			for (int j = 0; j < i; ++j) {
				KeyBinding keybinding = akeybinding[j];
				keybinding.setKeyCode(keybinding.getKeyCodeDefault());
			}

			KeyBinding.resetKeyBindingArrayAndHash();
		} else if (p_146284_1_.id < 100 && p_146284_1_ instanceof GuiOptionButton) {
			options.setOptionValue(((GuiOptionButton) p_146284_1_).returnEnumOptions(), 1);
			p_146284_1_.displayString = options.getKeyBinding(GameSettings.Options.getEnumOptions(p_146284_1_.id));
		}
	}

	@Override
	protected void mouseClicked(int p_73864_1_, int p_73864_2_, int p_73864_3_) {
		if (buttonId != null) {
			options.setOptionKeyBinding(buttonId, -100 + p_73864_3_);
			buttonId = null;
			KeyBinding.resetKeyBindingArrayAndHash();
		} else if (p_73864_3_ != 0 || !keyBindingList.func_148179_a(p_73864_1_, p_73864_2_, p_73864_3_)) {
			super.mouseClicked(p_73864_1_, p_73864_2_, p_73864_3_);
		}
	}

	@Override
	protected void mouseMovedOrUp(int p_146286_1_, int p_146286_2_, int p_146286_3_) {
		if (p_146286_3_ != 0 || !keyBindingList.func_148181_b(p_146286_1_, p_146286_2_, p_146286_3_)) {
			super.mouseMovedOrUp(p_146286_1_, p_146286_2_, p_146286_3_);
		}
	}

	@Override
	protected void keyTyped(char p_73869_1_, int p_73869_2_) {
		if (buttonId != null) {
			if (p_73869_2_ == 1) {
				options.setOptionKeyBinding(buttonId, 0);
			} else {
				options.setOptionKeyBinding(buttonId, p_73869_2_);
			}

			buttonId = null;
			field_152177_g = Minecraft.getSystemTime();
			KeyBinding.resetKeyBindingArrayAndHash();
		} else {
			super.keyTyped(p_73869_1_, p_73869_2_);
		}
	}

	@Override
	public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
		drawDefaultBackground();
		keyBindingList.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
		drawCenteredString(fontRendererObj, field_146495_a, width / 2, 8, 16777215);
		boolean flag = true;
		KeyBinding[] akeybinding = options.keyBindings;
		int k = akeybinding.length;

		for (int l = 0; l < k; ++l) {
			KeyBinding keybinding = akeybinding[l];

			if (keybinding.getKeyCode() != keybinding.getKeyCodeDefault()) {
				flag = false;
				break;
			}
		}

		field_146493_s.enabled = !flag;
		super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
	}
}