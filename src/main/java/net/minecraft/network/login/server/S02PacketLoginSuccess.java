package net.minecraft.network.login.server;

import java.io.IOException;
import java.util.UUID;

import com.mojang.authlib.GameProfile;

import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.login.INetHandlerLoginClient;

public class S02PacketLoginSuccess extends Packet {
	private GameProfile field_149602_a;
	private static final String __OBFID = "CL_00001375";

	public S02PacketLoginSuccess() {
	}

	public S02PacketLoginSuccess(GameProfile p_i45267_1_) {
		field_149602_a = p_i45267_1_;
	}

	@Override
	public void readPacketData(PacketBuffer p_148837_1_) throws IOException {
		String s = p_148837_1_.readStringFromBuffer(36);
		String s1 = p_148837_1_.readStringFromBuffer(16);
		UUID uuid = UUID.fromString(s);
		field_149602_a = new GameProfile(uuid, s1);
	}

	@Override
	public void writePacketData(PacketBuffer p_148840_1_) throws IOException {
		UUID uuid = field_149602_a.getId();
		p_148840_1_.writeStringToBuffer(uuid == null ? "" : uuid.toString());
		p_148840_1_.writeStringToBuffer(field_149602_a.getName());
	}

	public void processPacket(INetHandlerLoginClient p_148833_1_) {
		p_148833_1_.handleLoginSuccess(this);
	}

	@Override
	public boolean hasPriority() {
		return true;
	}

	@Override
	public void processPacket(INetHandler p_148833_1_) {
		this.processPacket((INetHandlerLoginClient) p_148833_1_);
	}
}