package net.minecraft.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.ResourcePackListEntry;
import net.minecraft.util.EnumChatFormatting;

import java.util.List;

@SideOnly(Side.CLIENT)
public abstract class GuiResourcePackList extends GuiListExtended {
	protected final Minecraft field_148205_k;
	protected final List field_148204_l;
	private static final String __OBFID = "CL_00000825";

	public GuiResourcePackList(Minecraft p_i45055_1_, int p_i45055_2_, int p_i45055_3_, List p_i45055_4_) {
		super(p_i45055_1_, p_i45055_2_, p_i45055_3_, 32, p_i45055_3_ - 55 + 4, 36);
		field_148205_k = p_i45055_1_;
		field_148204_l = p_i45055_4_;
		field_148163_i = false;
		setHasListHeader(true, (int) (p_i45055_1_.fontRenderer.FONT_HEIGHT * 1.5F));
	}

	@Override
	protected void drawListHeader(int p_148129_1_, int p_148129_2_, Tessellator p_148129_3_) {
		String s = EnumChatFormatting.UNDERLINE + "" + EnumChatFormatting.BOLD + func_148202_k();
		field_148205_k.fontRenderer.drawString(s,
				p_148129_1_ + width / 2 - field_148205_k.fontRenderer.getStringWidth(s) / 2,
				Math.min(top + 3, p_148129_2_), 16777215);
	}

	protected abstract String func_148202_k();

	public List func_148201_l() {
		return field_148204_l;
	}

	@Override
	protected int getSize() {
		return func_148201_l().size();
	}

	@Override
	public ResourcePackListEntry getListEntry(int p_148180_1_) {
		return (ResourcePackListEntry) func_148201_l().get(p_148180_1_);
	}

	@Override
	public int getListWidth() {
		return width;
	}

	@Override
	protected int getScrollBarX() {
		return right - 6;
	}
}