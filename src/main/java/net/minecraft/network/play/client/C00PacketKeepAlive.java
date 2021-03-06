package net.minecraft.network.play.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

import java.io.IOException;

public class C00PacketKeepAlive extends Packet {
	private int field_149461_a;
	private static final String __OBFID = "CL_00001359";

	public C00PacketKeepAlive() {
	}

	@SideOnly(Side.CLIENT)
	public C00PacketKeepAlive(int p_i45252_1_) {
		field_149461_a = p_i45252_1_;
	}

	public void processPacket(INetHandlerPlayServer p_148833_1_) {
		p_148833_1_.processKeepAlive(this);
	}

	@Override
	public void readPacketData(PacketBuffer p_148837_1_) throws IOException {
		field_149461_a = p_148837_1_.readInt();
	}

	@Override
	public void writePacketData(PacketBuffer p_148840_1_) throws IOException {
		p_148840_1_.writeInt(field_149461_a);
	}

	@Override
	public boolean hasPriority() {
		return true;
	}

	public int func_149460_c() {
		return field_149461_a;
	}

	@Override
	public void processPacket(INetHandler p_148833_1_) {
		this.processPacket((INetHandlerPlayServer) p_148833_1_);
	}
}