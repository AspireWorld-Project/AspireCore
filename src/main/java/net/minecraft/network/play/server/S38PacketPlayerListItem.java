package net.minecraft.network.play.server;

import java.io.IOException;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class S38PacketPlayerListItem extends Packet {
	private String field_149126_a;
	private boolean field_149124_b;
	private int field_149125_c;
	private static final String __OBFID = "CL_00001318";

	public S38PacketPlayerListItem() {
	}

	public S38PacketPlayerListItem(String p_i45209_1_, boolean p_i45209_2_, int p_i45209_3_) {
		field_149126_a = p_i45209_1_;
		field_149124_b = p_i45209_2_;
		field_149125_c = p_i45209_3_;
	}

	@Override
	public void readPacketData(PacketBuffer p_148837_1_) throws IOException {
		field_149126_a = p_148837_1_.readStringFromBuffer(16);
		field_149124_b = p_148837_1_.readBoolean();
		field_149125_c = p_148837_1_.readShort();
	}

	@Override
	public void writePacketData(PacketBuffer p_148840_1_) throws IOException {
		p_148840_1_.writeStringToBuffer(field_149126_a);
		p_148840_1_.writeBoolean(field_149124_b);
		p_148840_1_.writeShort(field_149125_c);
	}

	public void processPacket(INetHandlerPlayClient p_148833_1_) {
		p_148833_1_.handlePlayerListItem(this);
	}

	@Override
	public void processPacket(INetHandler p_148833_1_) {
		this.processPacket((INetHandlerPlayClient) p_148833_1_);
	}

	@SideOnly(Side.CLIENT)
	public String func_149122_c() {
		return field_149126_a;
	}

	@SideOnly(Side.CLIENT)
	public boolean func_149121_d() {
		return field_149124_b;
	}

	@SideOnly(Side.CLIENT)
	public int func_149120_e() {
		return field_149125_c;
	}
}