package net.minecraft.network.play.server;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

import java.io.IOException;

public class S06PacketUpdateHealth extends Packet {
	private float field_149336_a;
	private int field_149334_b;
	private float field_149335_c;
	private static final String __OBFID = "CL_00001332";

	public S06PacketUpdateHealth() {
	}

	public S06PacketUpdateHealth(float p_i45223_1_, int p_i45223_2_, float p_i45223_3_) {
		field_149336_a = p_i45223_1_;
		field_149334_b = p_i45223_2_;
		field_149335_c = p_i45223_3_;
	}

	@Override
	public void readPacketData(PacketBuffer p_148837_1_) throws IOException {
		field_149336_a = p_148837_1_.readFloat();
		field_149334_b = p_148837_1_.readShort();
		field_149335_c = p_148837_1_.readFloat();
	}

	@Override
	public void writePacketData(PacketBuffer p_148840_1_) throws IOException {
		p_148840_1_.writeFloat(field_149336_a);
		p_148840_1_.writeShort(field_149334_b);
		p_148840_1_.writeFloat(field_149335_c);
	}

	public void processPacket(INetHandlerPlayClient p_148833_1_) {
		p_148833_1_.handleUpdateHealth(this);
	}

	@Override
	public void processPacket(INetHandler p_148833_1_) {
		this.processPacket((INetHandlerPlayClient) p_148833_1_);
	}

	@SideOnly(Side.CLIENT)
	public float func_149332_c() {
		return field_149336_a;
	}

	@SideOnly(Side.CLIENT)
	public int func_149330_d() {
		return field_149334_b;
	}

	@SideOnly(Side.CLIENT)
	public float func_149331_e() {
		return field_149335_c;
	}
}