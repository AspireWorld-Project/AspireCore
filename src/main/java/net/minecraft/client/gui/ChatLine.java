package net.minecraft.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.IChatComponent;

@SideOnly(Side.CLIENT)
public class ChatLine {
	private final int updateCounterCreated;
	private final IChatComponent lineString;
	private final int chatLineID;
	public ChatLine(int p_i45000_1_, IChatComponent p_i45000_2_, int p_i45000_3_) {
		lineString = p_i45000_2_;
		updateCounterCreated = p_i45000_1_;
		chatLineID = p_i45000_3_;
	}

	public IChatComponent func_151461_a() {
		return lineString;
	}

	public int getUpdatedCounter() {
		return updateCounterCreated;
	}

	public int getChatLineID() {
		return chatLineID;
	}
}