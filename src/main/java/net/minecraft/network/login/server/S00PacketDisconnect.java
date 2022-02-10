package net.minecraft.network.login.server;

import java.io.IOException;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.login.INetHandlerLoginClient;
import net.minecraft.util.IChatComponent;

public class S00PacketDisconnect extends Packet {
	private IChatComponent field_149605_a;
	private static final String __OBFID = "CL_00001377";

	public S00PacketDisconnect() {
	}

	public S00PacketDisconnect(IChatComponent p_i45269_1_) {
		field_149605_a = p_i45269_1_;
	}

	@Override
	public void readPacketData(PacketBuffer p_148837_1_) throws IOException {
		field_149605_a = IChatComponent.Serializer.func_150699_a(p_148837_1_.readStringFromBuffer(32767));
	}

	@Override
	public void writePacketData(PacketBuffer p_148840_1_) throws IOException {
		p_148840_1_.writeStringToBuffer(IChatComponent.Serializer.func_150696_a(field_149605_a));
	}

	public void processPacket(INetHandlerLoginClient p_148833_1_) {
		p_148833_1_.handleDisconnect(this);
	}

	@Override
	public boolean hasPriority() {
		return true;
	}

	@Override
	public void processPacket(INetHandler p_148833_1_) {
		this.processPacket((INetHandlerLoginClient) p_148833_1_);
	}

	@SideOnly(Side.CLIENT)
	public IChatComponent func_149603_c() {
		return field_149605_a;
	}
}