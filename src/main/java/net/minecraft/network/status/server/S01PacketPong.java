package net.minecraft.network.status.server;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.status.INetHandlerStatusClient;

import java.io.IOException;

public class S01PacketPong extends Packet {
	private long field_149293_a;
	private static final String __OBFID = "CL_00001383";

	public S01PacketPong() {
	}

	public S01PacketPong(long p_i45272_1_) {
		field_149293_a = p_i45272_1_;
	}

	@Override
	public void readPacketData(PacketBuffer p_148837_1_) throws IOException {
		field_149293_a = p_148837_1_.readLong();
	}

	@Override
	public void writePacketData(PacketBuffer p_148840_1_) throws IOException {
		p_148840_1_.writeLong(field_149293_a);
	}

	public void processPacket(INetHandlerStatusClient p_148833_1_) {
		p_148833_1_.handlePong(this);
	}

	@Override
	public boolean hasPriority() {
		return true;
	}

	@Override
	public void processPacket(INetHandler p_148833_1_) {
		this.processPacket((INetHandlerStatusClient) p_148833_1_);
	}

	@SideOnly(Side.CLIENT)
	public long func_149292_c() {
		return field_149293_a;
	}
}