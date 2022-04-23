package net.minecraft.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiPlayerInfo {
	public final String name;
	public int responseTime;
	public GuiPlayerInfo(String p_i1190_1_) {
		name = p_i1190_1_;
		p_i1190_1_.toLowerCase();
	}
}