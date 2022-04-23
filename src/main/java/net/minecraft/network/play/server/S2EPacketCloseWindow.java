package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

import java.io.IOException;

public class S2EPacketCloseWindow extends Packet {
	private int field_148896_a;
	private static final String __OBFID = "CL_00001292";

	public S2EPacketCloseWindow() {
	}

	public S2EPacketCloseWindow(int p_i45183_1_) {
		field_148896_a = p_i45183_1_;
	}

	public void processPacket(INetHandlerPlayClient p_148833_1_) {
		p_148833_1_.handleCloseWindow(this);
	}

	@Override
	public void readPacketData(PacketBuffer p_148837_1_) throws IOException {
		field_148896_a = p_148837_1_.readUnsignedByte();
	}

	@Override
	public void writePacketData(PacketBuffer p_148840_1_) throws IOException {
		p_148840_1_.writeByte(field_148896_a);
	}

	@Override
	public void processPacket(INetHandler p_148833_1_) {
		this.processPacket((INetHandlerPlayClient) p_148833_1_);
	}
}