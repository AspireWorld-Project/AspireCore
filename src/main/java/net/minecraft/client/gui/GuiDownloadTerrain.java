package net.minecraft.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.resources.I18n;
import net.minecraft.network.play.client.C00PacketKeepAlive;

@SideOnly(Side.CLIENT)
public class GuiDownloadTerrain extends GuiScreen {
	private final NetHandlerPlayClient field_146594_a;
	private int field_146593_f;
	public GuiDownloadTerrain(NetHandlerPlayClient p_i45023_1_) {
		field_146594_a = p_i45023_1_;
	}

	@Override
	protected void keyTyped(char p_73869_1_, int p_73869_2_) {
	}

	@Override
	public void initGui() {
		buttonList.clear();
	}

	@Override
	public void updateScreen() {
		++field_146593_f;

		if (field_146593_f % 20 == 0) {
			field_146594_a.addToSendQueue(new C00PacketKeepAlive());
		}

		if (field_146594_a != null) {
			field_146594_a.onNetworkTick();
		}
	}

	@Override
	public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
		drawBackground(0);
		drawCenteredString(fontRendererObj, I18n.format("multiplayer.downloadingTerrain"), width / 2,
				height / 2 - 50, 16777215);
		super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
}