package net.minecraft.network.play.server;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.Entity;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

import java.io.IOException;

public class S0BPacketAnimation extends Packet {
	private int field_148981_a;
	private int field_148980_b;
	private static final String __OBFID = "CL_00001282";

	public S0BPacketAnimation() {
	}

	public S0BPacketAnimation(Entity p_i45172_1_, int p_i45172_2_) {
		field_148981_a = p_i45172_1_.getEntityId();
		field_148980_b = p_i45172_2_;
	}

	@Override
	public void readPacketData(PacketBuffer p_148837_1_) throws IOException {
		field_148981_a = p_148837_1_.readVarIntFromBuffer();
		field_148980_b = p_148837_1_.readUnsignedByte();
	}

	@Override
	public void writePacketData(PacketBuffer p_148840_1_) throws IOException {
		p_148840_1_.writeVarIntToBuffer(field_148981_a);
		p_148840_1_.writeByte(field_148980_b);
	}

	public void processPacket(INetHandlerPlayClient p_148833_1_) {
		p_148833_1_.handleAnimation(this);
	}

	@Override
	public String serialize() {
		return String.format("id=%d, type=%d",
				Integer.valueOf(field_148981_a), Integer.valueOf(field_148980_b));
	}

	@SideOnly(Side.CLIENT)
	public int func_148978_c() {
		return field_148981_a;
	}

	@SideOnly(Side.CLIENT)
	public int func_148977_d() {
		return field_148980_b;
	}

	@Override
	public void processPacket(INetHandler p_148833_1_) {
		this.processPacket((INetHandlerPlayClient) p_148833_1_);
	}
}