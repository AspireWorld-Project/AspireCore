package net.minecraft.network.play.server;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.Entity;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

import java.io.IOException;

public class S1BPacketEntityAttach extends Packet {
	private int field_149408_a;
	private int field_149406_b;
	private int field_149407_c;
	private static final String __OBFID = "CL_00001327";

	public S1BPacketEntityAttach() {
	}

	public S1BPacketEntityAttach(int p_i45218_1_, Entity p_i45218_2_, Entity p_i45218_3_) {
		field_149408_a = p_i45218_1_;
		field_149406_b = p_i45218_2_.getEntityId();
		field_149407_c = p_i45218_3_ != null ? p_i45218_3_.getEntityId() : -1;
	}

	@Override
	public void readPacketData(PacketBuffer p_148837_1_) throws IOException {
		field_149406_b = p_148837_1_.readInt();
		field_149407_c = p_148837_1_.readInt();
		field_149408_a = p_148837_1_.readUnsignedByte();
	}

	@Override
	public void writePacketData(PacketBuffer p_148840_1_) throws IOException {
		p_148840_1_.writeInt(field_149406_b);
		p_148840_1_.writeInt(field_149407_c);
		p_148840_1_.writeByte(field_149408_a);
	}

	public void processPacket(INetHandlerPlayClient p_148833_1_) {
		p_148833_1_.handleEntityAttach(this);
	}

	@Override
	public void processPacket(INetHandler p_148833_1_) {
		this.processPacket((INetHandlerPlayClient) p_148833_1_);
	}

	@SideOnly(Side.CLIENT)
	public int func_149404_c() {
		return field_149408_a;
	}

	@SideOnly(Side.CLIENT)
	public int func_149403_d() {
		return field_149406_b;
	}

	@SideOnly(Side.CLIENT)
	public int func_149402_e() {
		return field_149407_c;
	}
}