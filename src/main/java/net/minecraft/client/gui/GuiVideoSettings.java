package net.minecraft.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;

@SideOnly(Side.CLIENT)
public class GuiVideoSettings extends GuiScreen {
	private final GuiScreen parentGuiScreen;
	protected String screenTitle = "Video Settings";
	private final GameSettings guiGameSettings;
	private GuiListExtended optionsRowList;
	private static final GameSettings.Options[] videoOptions = new GameSettings.Options[] {
			GameSettings.Options.GRAPHICS, GameSettings.Options.RENDER_DISTANCE, GameSettings.Options.AMBIENT_OCCLUSION,
			GameSettings.Options.FRAMERATE_LIMIT, GameSettings.Options.ANAGLYPH, GameSettings.Options.VIEW_BOBBING,
			GameSettings.Options.GUI_SCALE, GameSettings.Options.ADVANCED_OPENGL, GameSettings.Options.GAMMA,
			GameSettings.Options.RENDER_CLOUDS, GameSettings.Options.PARTICLES, GameSettings.Options.USE_FULLSCREEN,
			GameSettings.Options.ENABLE_VSYNC, GameSettings.Options.MIPMAP_LEVELS,
			GameSettings.Options.ANISOTROPIC_FILTERING };
	private static final String __OBFID = "CL_00000718";

	public GuiVideoSettings(GuiScreen p_i1062_1_, GameSettings p_i1062_2_) {
		parentGuiScreen = p_i1062_1_;
		guiGameSettings = p_i1062_2_;
	}

	@Override
	public void initGui() {
		screenTitle = I18n.format("options.videoTitle");
		buttonList.clear();
		buttonList.add(new GuiButton(200, width / 2 - 100, height - 27, I18n.format("gui.done")));

		if (OpenGlHelper.field_153197_d) {
			optionsRowList = new GuiOptionsRowList(mc, width, height, 32, height - 32, 25, videoOptions);
		} else {
			GameSettings.Options[] aoptions = new GameSettings.Options[videoOptions.length - 1];
			int i = 0;
			GameSettings.Options[] aoptions1 = videoOptions;
			int j = aoptions1.length;

			for (int k = 0; k < j; ++k) {
				GameSettings.Options options = aoptions1[k];

				if (options != GameSettings.Options.ADVANCED_OPENGL) {
					aoptions[i] = options;
					++i;
				}
			}

			optionsRowList = new GuiOptionsRowList(mc, width, height, 32, height - 32, 25, aoptions);
		}
	}

	@Override
	protected void actionPerformed(GuiButton p_146284_1_) {
		if (p_146284_1_.enabled) {
			if (p_146284_1_.id == 200) {
				// this.mc.gameSettings.saveOptions();
				mc.displayGuiScreen(parentGuiScreen);
			}
		}
	}

	@Override
	public void onGuiClosed() {
		mc.gameSettings.saveOptions();
	}

	@Override
	protected void mouseClicked(int p_73864_1_, int p_73864_2_, int p_73864_3_) {
		int l = guiGameSettings.guiScale;
		super.mouseClicked(p_73864_1_, p_73864_2_, p_73864_3_);
		optionsRowList.func_148179_a(p_73864_1_, p_73864_2_, p_73864_3_);

		if (guiGameSettings.guiScale != l) {
			ScaledResolution scaledresolution = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
			int i1 = scaledresolution.getScaledWidth();
			int j1 = scaledresolution.getScaledHeight();
			setWorldAndResolution(mc, i1, j1);
		}
	}

	@Override
	protected void mouseMovedOrUp(int p_146286_1_, int p_146286_2_, int p_146286_3_) {
		int l = guiGameSettings.guiScale;
		super.mouseMovedOrUp(p_146286_1_, p_146286_2_, p_146286_3_);
		optionsRowList.func_148181_b(p_146286_1_, p_146286_2_, p_146286_3_);

		if (guiGameSettings.guiScale != l) {
			ScaledResolution scaledresolution = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
			int i1 = scaledresolution.getScaledWidth();
			int j1 = scaledresolution.getScaledHeight();
			setWorldAndResolution(mc, i1, j1);
		}
	}

	@Override
	public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
		drawDefaultBackground();
		optionsRowList.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
		drawCenteredString(fontRendererObj, screenTitle, width / 2, 5, 16777215);
		super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
	}
}