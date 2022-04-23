package net.minecraft.network.play.server;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.ItemStack;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

import java.io.IOException;

public class S2FPacketSetSlot extends Packet {
	private int field_149179_a;
	private int field_149177_b;
	private ItemStack field_149178_c;
	private static final String __OBFID = "CL_00001296";

	public S2FPacketSetSlot() {
	}

	public S2FPacketSetSlot(int p_i45188_1_, int p_i45188_2_, ItemStack p_i45188_3_) {
		field_149179_a = p_i45188_1_;
		field_149177_b = p_i45188_2_;
		field_149178_c = p_i45188_3_ == null ? null : p_i45188_3_.copy();
	}

	public void processPacket(INetHandlerPlayClient p_148833_1_) {
		p_148833_1_.handleSetSlot(this);
	}

	@Override
	public void readPacketData(PacketBuffer p_148837_1_) throws IOException {
		field_149179_a = p_148837_1_.readByte();
		field_149177_b = p_148837_1_.readShort();
		field_149178_c = p_148837_1_.readItemStackFromBuffer();
	}

	@Override
	public void writePacketData(PacketBuffer p_148840_1_) throws IOException {
		p_148840_1_.writeByte(field_149179_a);
		p_148840_1_.writeShort(field_149177_b);
		p_148840_1_.writeItemStackToBuffer(field_149178_c);
	}

	@SideOnly(Side.CLIENT)
	public int func_149175_c() {
		return field_149179_a;
	}

	@SideOnly(Side.CLIENT)
	public int func_149173_d() {
		return field_149177_b;
	}

	@SideOnly(Side.CLIENT)
	public ItemStack func_149174_e() {
		return field_149178_c;
	}

	@Override
	public void processPacket(INetHandler p_148833_1_) {
		this.processPacket((INetHandlerPlayClient) p_148833_1_);
	}
}