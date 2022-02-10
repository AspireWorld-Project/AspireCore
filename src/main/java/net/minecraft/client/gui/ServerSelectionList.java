package net.minecraft.client.gui;

import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Lists;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerList;
import net.minecraft.client.network.LanServerDetector;

@SideOnly(Side.CLIENT)
public class ServerSelectionList extends GuiListExtended {
	private final GuiMultiplayer field_148200_k;
	private final List field_148198_l = Lists.newArrayList();
	private final List field_148199_m = Lists.newArrayList();
	private final GuiListExtended.IGuiListEntry field_148196_n = new ServerListEntryLanScan();
	private int field_148197_o = -1;
	private static final String __OBFID = "CL_00000819";

	public ServerSelectionList(GuiMultiplayer p_i45049_1_, Minecraft p_i45049_2_, int p_i45049_3_, int p_i45049_4_,
			int p_i45049_5_, int p_i45049_6_, int p_i45049_7_) {
		super(p_i45049_2_, p_i45049_3_, p_i45049_4_, p_i45049_5_, p_i45049_6_, p_i45049_7_);
		field_148200_k = p_i45049_1_;
	}

	@Override
	public GuiListExtended.IGuiListEntry getListEntry(int p_148180_1_) {
		if (p_148180_1_ < field_148198_l.size())
			return (GuiListExtended.IGuiListEntry) field_148198_l.get(p_148180_1_);
		else {
			p_148180_1_ -= field_148198_l.size();

			if (p_148180_1_ == 0)
				return field_148196_n;
			else {
				--p_148180_1_;
				return (GuiListExtended.IGuiListEntry) field_148199_m.get(p_148180_1_);
			}
		}
	}

	@Override
	protected int getSize() {
		return field_148198_l.size() + 1 + field_148199_m.size();
	}

	public void func_148192_c(int p_148192_1_) {
		field_148197_o = p_148192_1_;
	}

	@Override
	protected boolean isSelected(int p_148131_1_) {
		return p_148131_1_ == field_148197_o;
	}

	public int func_148193_k() {
		return field_148197_o;
	}

	public void func_148195_a(ServerList p_148195_1_) {
		field_148198_l.clear();

		for (int i = 0; i < p_148195_1_.countServers(); ++i) {
			field_148198_l.add(new ServerListEntryNormal(field_148200_k, p_148195_1_.getServerData(i)));
		}
	}

	public void func_148194_a(List p_148194_1_) {
		field_148199_m.clear();
		Iterator iterator = p_148194_1_.iterator();

		while (iterator.hasNext()) {
			LanServerDetector.LanServer lanserver = (LanServerDetector.LanServer) iterator.next();
			field_148199_m.add(new ServerListEntryLanDetected(field_148200_k, lanserver));
		}
	}

	@Override
	protected int getScrollBarX() {
		return super.getScrollBarX() + 30;
	}

	@Override
	public int getListWidth() {
		return super.getListWidth() + 85;
	}
}