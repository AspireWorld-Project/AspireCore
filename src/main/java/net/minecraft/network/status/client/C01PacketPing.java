package net.minecraft.network.status.client;

import java.io.IOException;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.status.INetHandlerStatusServer;

public class C01PacketPing extends Packet {
	private long field_149290_a;
	private static final String __OBFID = "CL_00001392";

	public C01PacketPing() {
	}

	@SideOnly(Side.CLIENT)
	public C01PacketPing(long p_i45276_1_) {
		field_149290_a = p_i45276_1_;
	}

	@Override
	public void readPacketData(PacketBuffer p_148837_1_) throws IOException {
		field_149290_a = p_148837_1_.readLong();
	}

	@Override
	public void writePacketData(PacketBuffer p_148840_1_) throws IOException {
		p_148840_1_.writeLong(field_149290_a);
	}

	public void processPacket(INetHandlerStatusServer p_148833_1_) {
		p_148833_1_.processPing(this);
	}

	@Override
	public boolean hasPriority() {
		return true;
	}

	public long func_149289_c() {
		return field_149290_a;
	}

	@Override
	public void processPacket(INetHandler p_148833_1_) {
		this.processPacket((INetHandlerStatusServer) p_148833_1_);
	}
}