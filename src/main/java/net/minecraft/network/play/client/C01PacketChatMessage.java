package net.minecraft.network.play.client;

import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

import java.io.IOException;

public class C01PacketChatMessage extends Packet {
	private String field_149440_a;

	public C01PacketChatMessage() {
	}

	public C01PacketChatMessage(String p_i45240_1_) {
		if (p_i45240_1_.length() > 100) {
			p_i45240_1_ = p_i45240_1_.substring(0, 100);
		}

		field_149440_a = p_i45240_1_;
	}

	@Override
	public void readPacketData(PacketBuffer p_148837_1_) throws IOException {
		field_149440_a = p_148837_1_.readStringFromBuffer(100);
	}

	@Override
	public void writePacketData(PacketBuffer p_148840_1_) throws IOException {
		p_148840_1_.writeStringToBuffer(field_149440_a);
	}

	public void processPacket(INetHandlerPlayServer p_148833_1_) {
		p_148833_1_.processChatMessage(this);
	}

	@Override
	public String serialize() {
		return String.format("message=\'%s\'", new Object[] { field_149440_a });
	}

	public String func_149439_c() {
		return field_149440_a;
	}

	@Override
	public void processPacket(INetHandler p_148833_1_) {
		this.processPacket((INetHandlerPlayServer) p_148833_1_);
	}

	// CraftBukkit start - make chat async
	public boolean hasPriority()
	{
		return !this.field_149440_a.startsWith("/");
	}
	// CraftBukkit end
}