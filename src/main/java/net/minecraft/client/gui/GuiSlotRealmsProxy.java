package net.minecraft.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.realms.RealmsScrolledSelectionList;

@SideOnly(Side.CLIENT)
public class GuiSlotRealmsProxy extends GuiSlot {
	private final RealmsScrolledSelectionList field_154340_k;
	private static final String __OBFID = "CL_00001846";

	public GuiSlotRealmsProxy(RealmsScrolledSelectionList p_i1085_1_, int p_i1085_2_, int p_i1085_3_, int p_i1085_4_,
			int p_i1085_5_, int p_i1085_6_) {
		super(Minecraft.getMinecraft(), p_i1085_2_, p_i1085_3_, p_i1085_4_, p_i1085_5_, p_i1085_6_);
		field_154340_k = p_i1085_1_;
	}

	@Override
	protected int getSize() {
		return field_154340_k.getItemCount();
	}

	@Override
	protected void elementClicked(int p_148144_1_, boolean p_148144_2_, int p_148144_3_, int p_148144_4_) {
		field_154340_k.selectItem(p_148144_1_, p_148144_2_, p_148144_3_, p_148144_4_);
	}

	@Override
	protected boolean isSelected(int p_148131_1_) {
		return field_154340_k.isSelectedItem(p_148131_1_);
	}

	@Override
	protected void drawBackground() {
		field_154340_k.renderBackground();
	}

	@Override
	protected void drawSlot(int p_148126_1_, int p_148126_2_, int p_148126_3_, int p_148126_4_, Tessellator p_148126_5_,
			int p_148126_6_, int p_148126_7_) {
		field_154340_k.renderItem(p_148126_1_, p_148126_2_, p_148126_3_, p_148126_4_, p_148126_6_, p_148126_7_);
	}

	public int func_154338_k() {
		return super.width;
	}

	public int func_154339_l() {
		return super.mouseY;
	}

	public int func_154337_m() {
		return super.mouseX;
	}

	@Override
	protected int getContentHeight() {
		return field_154340_k.getMaxPosition();
	}

	@Override
	protected int getScrollBarX() {
		return field_154340_k.getScrollbarPosition();
	}
}