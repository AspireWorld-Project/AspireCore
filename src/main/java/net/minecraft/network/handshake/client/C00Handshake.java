package net.minecraft.network.handshake.client;

import java.io.IOException;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.handshake.INetHandlerHandshakeServer;

public class C00Handshake extends Packet {
	private int field_149600_a;
	private String field_149598_b;
	private int field_149599_c;
	private EnumConnectionState field_149597_d;
	private static final String __OBFID = "CL_00001372";

	public C00Handshake() {
	}

	@SideOnly(Side.CLIENT)
	public C00Handshake(int p_i45266_1_, String p_i45266_2_, int p_i45266_3_, EnumConnectionState p_i45266_4_) {
		field_149600_a = p_i45266_1_;
		field_149598_b = p_i45266_2_;
		field_149599_c = p_i45266_3_;
		field_149597_d = p_i45266_4_;
	}

	@Override
	public void readPacketData(PacketBuffer p_148837_1_) throws IOException {
		field_149600_a = p_148837_1_.readVarIntFromBuffer();
		field_149598_b = p_148837_1_.readStringFromBuffer(255);
		field_149599_c = p_148837_1_.readUnsignedShort();
		field_149597_d = EnumConnectionState.func_150760_a(p_148837_1_.readVarIntFromBuffer());
	}

	@Override
	public void writePacketData(PacketBuffer p_148840_1_) throws IOException {
		p_148840_1_.writeVarIntToBuffer(field_149600_a);
		p_148840_1_.writeStringToBuffer(field_149598_b);
		p_148840_1_.writeShort(field_149599_c);
		p_148840_1_.writeVarIntToBuffer(field_149597_d.func_150759_c());
	}

	public void processPacket(INetHandlerHandshakeServer p_148833_1_) {
		p_148833_1_.processHandshake(this);
	}

	@Override
	public boolean hasPriority() {
		return true;
	}

	public EnumConnectionState func_149594_c() {
		return field_149597_d;
	}

	public int func_149595_d() {
		return field_149600_a;
	}

	@Override
	public void processPacket(INetHandler p_148833_1_) {
		this.processPacket((INetHandlerHandshakeServer) p_148833_1_);
	}
}