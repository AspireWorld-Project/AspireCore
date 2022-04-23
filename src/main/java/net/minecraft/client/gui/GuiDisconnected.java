package net.minecraft.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.IChatComponent;

import java.util.Iterator;
import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiDisconnected extends GuiScreen {
	private final String field_146306_a;
	private final IChatComponent field_146304_f;
	private List field_146305_g;
	private final GuiScreen field_146307_h;
	private static final String __OBFID = "CL_00000693";

	public GuiDisconnected(GuiScreen p_i45020_1_, String p_i45020_2_, IChatComponent p_i45020_3_) {
		field_146307_h = p_i45020_1_;
		field_146306_a = I18n.format(p_i45020_2_);
		field_146304_f = p_i45020_3_;
	}

	@Override
	protected void keyTyped(char p_73869_1_, int p_73869_2_) {
	}

	@Override
	public void initGui() {
		buttonList.clear();
		buttonList.add(
				new GuiButton(0, width / 2 - 100, height / 4 + 120 + 12, I18n.format("gui.toMenu")));
		field_146305_g = fontRendererObj.listFormattedStringToWidth(field_146304_f.getFormattedText(), width - 50);
	}

	@Override
	protected void actionPerformed(GuiButton p_146284_1_) {
		if (p_146284_1_.id == 0) {
			mc.displayGuiScreen(field_146307_h);
		}
	}

	@Override
	public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
		drawDefaultBackground();
		drawCenteredString(fontRendererObj, field_146306_a, width / 2, height / 2 - 50, 11184810);
		int k = height / 2 - 30;

		if (field_146305_g != null) {
			for (Iterator iterator = field_146305_g.iterator(); iterator.hasNext(); k += fontRendererObj.FONT_HEIGHT) {
				String s = (String) iterator.next();
				drawCenteredString(fontRendererObj, s, width / 2, k, 16777215);
			}
		}

		super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
	}
}