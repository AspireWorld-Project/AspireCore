package net.minecraft.network.play.server;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

import java.io.IOException;

public class S34PacketMaps extends Packet {
	private int field_149191_a;
	private byte[] field_149190_b;
	private static final String __OBFID = "CL_00001311";

	public S34PacketMaps() {
	}

	public S34PacketMaps(int p_i45202_1_, byte[] p_i45202_2_) {
		field_149191_a = p_i45202_1_;
		field_149190_b = p_i45202_2_;
	}

	@Override
	public void readPacketData(PacketBuffer p_148837_1_) throws IOException {
		field_149191_a = p_148837_1_.readVarIntFromBuffer();
		field_149190_b = new byte[p_148837_1_.readUnsignedShort()];
		p_148837_1_.readBytes(field_149190_b);
	}

	@Override
	public void writePacketData(PacketBuffer p_148840_1_) throws IOException {
		p_148840_1_.writeVarIntToBuffer(field_149191_a);
		p_148840_1_.writeShort(field_149190_b.length);
		p_148840_1_.writeBytes(field_149190_b);
	}

	public void processPacket(INetHandlerPlayClient p_148833_1_) {
		p_148833_1_.handleMaps(this);
	}

	@Override
	public String serialize() {
		return String.format("id=%d, length=%d",
				Integer.valueOf(field_149191_a), Integer.valueOf(field_149190_b.length));
	}

	@SideOnly(Side.CLIENT)
	public int func_149188_c() {
		return field_149191_a;
	}

	@SideOnly(Side.CLIENT)
	public byte[] func_149187_d() {
		return field_149190_b;
	}

	@Override
	public void processPacket(INetHandler p_148833_1_) {
		this.processPacket((INetHandlerPlayClient) p_148833_1_);
	}
}