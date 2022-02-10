package net.minecraft.realms;

import java.util.Iterator;
import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.IChatComponent;

@SideOnly(Side.CLIENT)
public class DisconnectedOnlineScreen extends RealmsScreen {
	private String title;
	private IChatComponent reason;
	private List lines;
	private final RealmsScreen parent;
	private static final String __OBFID = "CL_00001912";

	public DisconnectedOnlineScreen(RealmsScreen p_i1000_1_, String p_i1000_2_, IChatComponent p_i1000_3_) {
		parent = p_i1000_1_;
		title = getLocalizedString(p_i1000_2_);
		reason = p_i1000_3_;
	}

	@Override
	public void init() {
		buttonsClear();
		buttonsAdd(newButton(0, width() / 2 - 100, height() / 4 + 120 + 12, getLocalizedString("gui.back")));
		lines = fontSplit(reason.getFormattedText(), width() - 50);
	}

	@Override
	public void keyPressed(char p_keyPressed_1_, int p_keyPressed_2_) {
		if (p_keyPressed_2_ == 1) {
			Realms.setScreen(parent);
		}
	}

	@Override
	public void buttonClicked(RealmsButton p_buttonClicked_1_) {
		if (p_buttonClicked_1_.id() == 0) {
			Realms.setScreen(parent);
		}
	}

	@Override
	public void render(int p_render_1_, int p_render_2_, float p_render_3_) {
		this.renderBackground();
		drawCenteredString(title, width() / 2, height() / 2 - 50, 11184810);
		int k = height() / 2 - 30;

		if (lines != null) {
			for (Iterator iterator = lines.iterator(); iterator.hasNext(); k += fontLineHeight()) {
				String s = (String) iterator.next();
				drawCenteredString(s, width() / 2, k, 16777215);
			}
		}

		super.render(p_render_1_, p_render_2_, p_render_3_);
	}
}