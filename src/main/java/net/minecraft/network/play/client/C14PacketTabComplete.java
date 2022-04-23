package net.minecraft.network.play.client;

import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

public class C14PacketTabComplete extends Packet {
	private String field_149420_a;
	private static final String __OBFID = "CL_00001346";

	public C14PacketTabComplete() {
	}

	public C14PacketTabComplete(String p_i45239_1_) {
		field_149420_a = p_i45239_1_;
	}

	@Override
	public void readPacketData(PacketBuffer p_148837_1_) throws IOException {
		field_149420_a = p_148837_1_.readStringFromBuffer(32767);
	}

	@Override
	public void writePacketData(PacketBuffer p_148840_1_) throws IOException {
		p_148840_1_.writeStringToBuffer(StringUtils.substring(field_149420_a, 0, 32767));
	}

	public void processPacket(INetHandlerPlayServer p_148833_1_) {
		p_148833_1_.processTabComplete(this);
	}

	public String func_149419_c() {
		return field_149420_a;
	}

	@Override
	public String serialize() {
		return String.format("message=\'%s\'", new Object[] { field_149420_a });
	}

	@Override
	public void processPacket(INetHandler p_148833_1_) {
		this.processPacket((INetHandlerPlayServer) p_148833_1_);
	}
}