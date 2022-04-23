package net.minecraft.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.resources.I18n;
import net.minecraft.network.play.client.C0BPacketEntityAction;

@SideOnly(Side.CLIENT)
public class GuiSleepMP extends GuiChat {
	private static final String __OBFID = "CL_00000697";

	@Override
	public void initGui() {
		super.initGui();
		buttonList.add(
				new GuiButton(1, width / 2 - 100, height - 40, I18n.format("multiplayer.stopSleeping")));
	}

	@Override
	protected void keyTyped(char p_73869_1_, int p_73869_2_) {
		if (p_73869_2_ == 1) {
			func_146418_g();
		} else if (p_73869_2_ != 28 && p_73869_2_ != 156) {
			super.keyTyped(p_73869_1_, p_73869_2_);
		} else {
			String s = inputField.getText().trim();

			if (!s.isEmpty()) {
				func_146403_a(s); // Forge: fix vanilla not adding messages to the sent list while sleeping
			}

			inputField.setText("");
			mc.ingameGUI.getChatGUI().resetScroll();
		}
	}

	@Override
	protected void actionPerformed(GuiButton p_146284_1_) {
		if (p_146284_1_.id == 1) {
			func_146418_g();
		} else {
			super.actionPerformed(p_146284_1_);
		}
	}

	private void func_146418_g() {
		NetHandlerPlayClient nethandlerplayclient = mc.thePlayer.sendQueue;
		nethandlerplayclient.addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, 3));
	}
}