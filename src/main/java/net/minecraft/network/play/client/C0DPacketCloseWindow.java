package net.minecraft.network.play.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

import java.io.IOException;

public class C0DPacketCloseWindow extends Packet {
	private int field_149556_a;
	private static final String __OBFID = "CL_00001354";

	public C0DPacketCloseWindow() {
	}

	public void setWindowId(int windowId) {
		field_149556_a = windowId;
	}

	@SideOnly(Side.CLIENT)
	public C0DPacketCloseWindow(int p_i45247_1_) {
		field_149556_a = p_i45247_1_;
	}

	public void processPacket(INetHandlerPlayServer p_148833_1_) {
		p_148833_1_.processCloseWindow(this);
	}

	@Override
	public void readPacketData(PacketBuffer p_148837_1_) throws IOException {
		field_149556_a = p_148837_1_.readByte();
	}

	@Override
	public void writePacketData(PacketBuffer p_148840_1_) throws IOException {
		p_148840_1_.writeByte(field_149556_a);
	}

	@Override
	public void processPacket(INetHandler p_148833_1_) {
		this.processPacket((INetHandlerPlayServer) p_148833_1_);
	}
}