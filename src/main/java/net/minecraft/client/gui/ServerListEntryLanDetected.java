package net.minecraft.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.LanServerDetector;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;

@SideOnly(Side.CLIENT)
public class ServerListEntryLanDetected implements GuiListExtended.IGuiListEntry {
	private final GuiMultiplayer field_148292_c;
	protected final Minecraft field_148293_a;
	protected final LanServerDetector.LanServer field_148291_b;
	private long field_148290_d = 0L;
	protected ServerListEntryLanDetected(GuiMultiplayer p_i45046_1_, LanServerDetector.LanServer p_i45046_2_) {
		field_148292_c = p_i45046_1_;
		field_148291_b = p_i45046_2_;
		field_148293_a = Minecraft.getMinecraft();
	}

	@Override
	public void drawEntry(int p_148279_1_, int p_148279_2_, int p_148279_3_, int p_148279_4_, int p_148279_5_,
			Tessellator p_148279_6_, int p_148279_7_, int p_148279_8_, boolean p_148279_9_) {
		field_148293_a.fontRenderer.drawString(I18n.format("lanServer.title"), p_148279_2_ + 32 + 3,
				p_148279_3_ + 1, 16777215);
		field_148293_a.fontRenderer.drawString(field_148291_b.getServerMotd(), p_148279_2_ + 32 + 3, p_148279_3_ + 12,
				8421504);

		if (field_148293_a.gameSettings.hideServerAddress) {
			field_148293_a.fontRenderer.drawString(I18n.format("selectServer.hiddenAddress"),
					p_148279_2_ + 32 + 3, p_148279_3_ + 12 + 11, 3158064);
		} else {
			field_148293_a.fontRenderer.drawString(field_148291_b.getServerIpPort(), p_148279_2_ + 32 + 3,
					p_148279_3_ + 12 + 11, 3158064);
		}
	}

	@Override
	public boolean mousePressed(int p_148278_1_, int p_148278_2_, int p_148278_3_, int p_148278_4_, int p_148278_5_,
			int p_148278_6_) {
		field_148292_c.func_146790_a(p_148278_1_);

		if (Minecraft.getSystemTime() - field_148290_d < 250L) {
			field_148292_c.func_146796_h();
		}

		field_148290_d = Minecraft.getSystemTime();
		return false;
	}

	@Override
	public void mouseReleased(int p_148277_1_, int p_148277_2_, int p_148277_3_, int p_148277_4_, int p_148277_5_,
			int p_148277_6_) {
	}

	public LanServerDetector.LanServer func_148289_a() {
		return field_148291_b;
	}
}