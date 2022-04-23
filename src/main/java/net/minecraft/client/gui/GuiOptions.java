package net.minecraft.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundCategory;
import net.minecraft.client.audio.SoundEventAccessorComposite;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.stream.GuiStreamOptions;
import net.minecraft.client.gui.stream.GuiStreamUnavailable;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.stream.IStream;

@SideOnly(Side.CLIENT)
public class GuiOptions extends GuiScreen implements GuiYesNoCallback {
	private static final GameSettings.Options[] field_146440_f = new GameSettings.Options[] { GameSettings.Options.FOV,
			GameSettings.Options.DIFFICULTY };
	private final GuiScreen field_146441_g;
	private final GameSettings field_146443_h;
	protected String field_146442_a = "Options";
	private static final String __OBFID = "CL_00000700";

	public GuiOptions(GuiScreen p_i1046_1_, GameSettings p_i1046_2_) {
		field_146441_g = p_i1046_1_;
		field_146443_h = p_i1046_2_;
	}

	@Override
	public void initGui() {
		int i = 0;
		field_146442_a = I18n.format("options.title");
		GameSettings.Options[] aoptions = field_146440_f;
		int j = aoptions.length;

		for (int k = 0; k < j; ++k) {
			GameSettings.Options options = aoptions[k];

			if (options.getEnumFloat()) {
				buttonList.add(new GuiOptionSlider(options.returnEnumOrdinal(), width / 2 - 155 + i % 2 * 160,
						height / 6 - 12 + 24 * (i >> 1), options));
			} else {
				GuiOptionButton guioptionbutton = new GuiOptionButton(options.returnEnumOrdinal(),
						width / 2 - 155 + i % 2 * 160, height / 6 - 12 + 24 * (i >> 1), options,
						field_146443_h.getKeyBinding(options));

				if (options == GameSettings.Options.DIFFICULTY && mc.theWorld != null
						&& mc.theWorld.getWorldInfo().isHardcoreModeEnabled()) {
					guioptionbutton.enabled = false;
					guioptionbutton.displayString = I18n.format("options.difficulty") + ": "
							+ I18n.format("options.difficulty.hardcore");
				}

				buttonList.add(guioptionbutton);
			}

			++i;
		}

		buttonList.add(new GuiButton(8675309, width / 2 + 5, height / 6 + 48 - 6, 150, 20, "Super Secret Settings...") {
			private static final String __OBFID = "CL_00000701";

			@Override
			public void func_146113_a(SoundHandler p_146113_1_) {
				SoundEventAccessorComposite soundeventaccessorcomposite = p_146113_1_
						.getRandomSoundFromCategories(SoundCategory.ANIMALS, SoundCategory.BLOCKS,
								SoundCategory.MOBS, SoundCategory.PLAYERS, SoundCategory.WEATHER);

				if (soundeventaccessorcomposite != null) {
					p_146113_1_.playSound(PositionedSoundRecord
							.func_147674_a(soundeventaccessorcomposite.getSoundEventLocation(), 0.5F));
				}
			}
		});
		buttonList.add(new GuiButton(106, width / 2 - 155, height / 6 + 72 - 6, 150, 20,
				I18n.format("options.sounds")));
		buttonList.add(new GuiButton(107, width / 2 + 5, height / 6 + 72 - 6, 150, 20,
				I18n.format("options.stream")));
		buttonList.add(new GuiButton(101, width / 2 - 155, height / 6 + 96 - 6, 150, 20,
				I18n.format("options.video")));
		buttonList.add(new GuiButton(100, width / 2 + 5, height / 6 + 96 - 6, 150, 20,
				I18n.format("options.controls")));
		buttonList.add(new GuiButton(102, width / 2 - 155, height / 6 + 120 - 6, 150, 20,
				I18n.format("options.language")));
		buttonList.add(new GuiButton(103, width / 2 + 5, height / 6 + 120 - 6, 150, 20,
				I18n.format("options.multiplayer.title")));
		buttonList.add(new GuiButton(105, width / 2 - 155, height / 6 + 144 - 6, 150, 20,
				I18n.format("options.resourcepack")));
		buttonList.add(new GuiButton(104, width / 2 + 5, height / 6 + 144 - 6, 150, 20,
				I18n.format("options.snooper.view")));
		buttonList.add(new GuiButton(200, width / 2 - 100, height / 6 + 168, I18n.format("gui.done")));
	}

	@Override
	protected void actionPerformed(GuiButton p_146284_1_) {
		if (p_146284_1_.enabled) {
			if (p_146284_1_.id < 100 && p_146284_1_ instanceof GuiOptionButton) {
				field_146443_h.setOptionValue(((GuiOptionButton) p_146284_1_).returnEnumOptions(), 1);
				p_146284_1_.displayString = field_146443_h
						.getKeyBinding(GameSettings.Options.getEnumOptions(p_146284_1_.id));
			}

			if (p_146284_1_.id == 8675309) {
				mc.entityRenderer.activateNextShader();
			}

			if (p_146284_1_.id == 101) {
				mc.gameSettings.saveOptions();
				mc.displayGuiScreen(new GuiVideoSettings(this, field_146443_h));
			}

			if (p_146284_1_.id == 100) {
				mc.gameSettings.saveOptions();
				mc.displayGuiScreen(new GuiControls(this, field_146443_h));
			}

			if (p_146284_1_.id == 102) {
				mc.gameSettings.saveOptions();
				mc.displayGuiScreen(new GuiLanguage(this, field_146443_h, mc.getLanguageManager()));
			}

			if (p_146284_1_.id == 103) {
				mc.gameSettings.saveOptions();
				mc.displayGuiScreen(new ScreenChatOptions(this, field_146443_h));
			}

			if (p_146284_1_.id == 104) {
				mc.gameSettings.saveOptions();
				mc.displayGuiScreen(new GuiSnooper(this, field_146443_h));
			}

			if (p_146284_1_.id == 200) {
				mc.gameSettings.saveOptions();
				mc.displayGuiScreen(field_146441_g);
			}

			if (p_146284_1_.id == 105) {
				mc.gameSettings.saveOptions();
				mc.displayGuiScreen(new GuiScreenResourcePacks(this));
			}

			if (p_146284_1_.id == 106) {
				mc.gameSettings.saveOptions();
				mc.displayGuiScreen(new GuiScreenOptionsSounds(this, field_146443_h));
			}

			if (p_146284_1_.id == 107) {
				mc.gameSettings.saveOptions();
				IStream istream = mc.func_152346_Z();

				if (istream.func_152936_l() && istream.func_152928_D()) {
					mc.displayGuiScreen(new GuiStreamOptions(this, field_146443_h));
				} else {
					GuiStreamUnavailable.func_152321_a(this);
				}
			}
		}
	}

	@Override
	public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
		drawDefaultBackground();
		drawCenteredString(fontRendererObj, field_146442_a, width / 2, 15, 16777215);
		super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
	}
}