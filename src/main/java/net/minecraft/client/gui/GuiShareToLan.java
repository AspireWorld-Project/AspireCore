package net.minecraft.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.WorldSettings;

@SideOnly(Side.CLIENT)
public class GuiShareToLan extends GuiScreen {
	private final GuiScreen field_146598_a;
	private GuiButton field_146596_f;
	private GuiButton field_146597_g;
	private String field_146599_h = "survival";
	private boolean field_146600_i;
	public GuiShareToLan(GuiScreen p_i1055_1_) {
		field_146598_a = p_i1055_1_;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initGui() {
		buttonList.clear();
		buttonList.add(new GuiButton(101, width / 2 - 155, height - 28, 150, 20,
				I18n.format("lanServer.start")));
		buttonList
				.add(new GuiButton(102, width / 2 + 5, height - 28, 150, 20, I18n.format("gui.cancel")));
		buttonList.add(field_146597_g = new GuiButton(104, width / 2 - 155, 100, 150, 20,
				I18n.format("selectWorld.gameMode")));
		buttonList.add(field_146596_f = new GuiButton(103, width / 2 + 5, 100, 150, 20,
				I18n.format("selectWorld.allowCommands")));
		func_146595_g();
	}

	private void func_146595_g() {
		field_146597_g.displayString = I18n.format("selectWorld.gameMode") + " "
				+ I18n.format("selectWorld.gameMode." + field_146599_h);
		field_146596_f.displayString = I18n.format("selectWorld.allowCommands") + " ";

		if (field_146600_i) {
			field_146596_f.displayString = field_146596_f.displayString + I18n.format("options.on");
		} else {
			field_146596_f.displayString = field_146596_f.displayString + I18n.format("options.off");
		}
	}

	@Override
	protected void actionPerformed(GuiButton p_146284_1_) {
		if (p_146284_1_.id == 102) {
			mc.displayGuiScreen(field_146598_a);
		} else if (p_146284_1_.id == 104) {
			if (field_146599_h.equals("survival")) {
				field_146599_h = "creative";
			} else if (field_146599_h.equals("creative")) {
				field_146599_h = "adventure";
			} else {
				field_146599_h = "survival";
			}

			func_146595_g();
		} else if (p_146284_1_.id == 103) {
			field_146600_i = !field_146600_i;
			func_146595_g();
		} else if (p_146284_1_.id == 101) {
			mc.displayGuiScreen(null);
			String s = mc.getIntegratedServer().shareToLAN(WorldSettings.GameType.getByName(field_146599_h),
					field_146600_i);
			Object object;

			if (s != null) {
				object = new ChatComponentTranslation("commands.publish.started", s);
			} else {
				object = new ChatComponentText("commands.publish.failed");
			}

			mc.ingameGUI.getChatGUI().printChatMessage((IChatComponent) object);
		}
	}

	@Override
	public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
		drawDefaultBackground();
		drawCenteredString(fontRendererObj, I18n.format("lanServer.title"), width / 2, 50, 16777215);
		drawCenteredString(fontRendererObj, I18n.format("lanServer.otherPlayers"), width / 2, 82,
				16777215);
		super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
	}
}