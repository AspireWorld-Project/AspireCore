package net.minecraft.network.status.client;

import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.status.INetHandlerStatusServer;

import java.io.IOException;

public class C00PacketServerQuery extends Packet {
	private static final String __OBFID = "CL_00001393";

	@Override
	public void readPacketData(PacketBuffer p_148837_1_) throws IOException {
	}

	@Override
	public void writePacketData(PacketBuffer p_148840_1_) throws IOException {
	}

	public void processPacket(INetHandlerStatusServer p_148833_1_) {
		p_148833_1_.processServerQuery(this);
	}

	@Override
	public boolean hasPriority() {
		return true;
	}

	@Override
	public void processPacket(INetHandler p_148833_1_) {
		this.processPacket((INetHandlerStatusServer) p_148833_1_);
	}
}