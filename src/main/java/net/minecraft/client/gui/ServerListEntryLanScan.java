package net.minecraft.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;

@SideOnly(Side.CLIENT)
public class ServerListEntryLanScan implements GuiListExtended.IGuiListEntry {
	private final Minecraft field_148288_a = Minecraft.getMinecraft();
	@Override
	public void drawEntry(int p_148279_1_, int p_148279_2_, int p_148279_3_, int p_148279_4_, int p_148279_5_,
			Tessellator p_148279_6_, int p_148279_7_, int p_148279_8_, boolean p_148279_9_) {
		int l1 = p_148279_3_ + p_148279_5_ / 2 - field_148288_a.fontRenderer.FONT_HEIGHT / 2;
		field_148288_a.fontRenderer.drawString(I18n.format("lanServer.scanning"),
				field_148288_a.currentScreen.width / 2
						- field_148288_a.fontRenderer.getStringWidth(I18n.format("lanServer.scanning"))
								/ 2,
				l1, 16777215);
		String s;

		switch ((int) (Minecraft.getSystemTime() / 300L % 4L)) {
		case 0:
		default:
			s = "O o o";
			break;
		case 1:
		case 3:
			s = "o O o";
			break;
		case 2:
			s = "o o O";
		}

		field_148288_a.fontRenderer.drawString(s,
				field_148288_a.currentScreen.width / 2 - field_148288_a.fontRenderer.getStringWidth(s) / 2,
				l1 + field_148288_a.fontRenderer.FONT_HEIGHT, 8421504);
	}

	@Override
	public boolean mousePressed(int p_148278_1_, int p_148278_2_, int p_148278_3_, int p_148278_4_, int p_148278_5_,
			int p_148278_6_) {
		return false;
	}

	@Override
	public void mouseReleased(int p_148277_1_, int p_148277_2_, int p_148277_3_, int p_148277_4_, int p_148277_5_,
			int p_148277_6_) {
	}
}