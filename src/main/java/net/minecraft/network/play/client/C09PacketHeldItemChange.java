package net.minecraft.network.play.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

import java.io.IOException;

public class C09PacketHeldItemChange extends Packet {
	private int field_149615_a;
	private static final String __OBFID = "CL_00001368";

	public C09PacketHeldItemChange() {
	}

	@SideOnly(Side.CLIENT)
	public C09PacketHeldItemChange(int p_i45262_1_) {
		field_149615_a = p_i45262_1_;
	}

	@Override
	public void readPacketData(PacketBuffer p_148837_1_) throws IOException {
		field_149615_a = p_148837_1_.readShort();
	}

	@Override
	public void writePacketData(PacketBuffer p_148840_1_) throws IOException {
		p_148840_1_.writeShort(field_149615_a);
	}

	public void processPacket(INetHandlerPlayServer p_148833_1_) {
		p_148833_1_.processHeldItemChange(this);
	}

	public int func_149614_c() {
		return field_149615_a;
	}

	@Override
	public void processPacket(INetHandler p_148833_1_) {
		this.processPacket((INetHandlerPlayServer) p_148833_1_);
	}
}