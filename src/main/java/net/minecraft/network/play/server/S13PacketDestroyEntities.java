package net.minecraft.network.play.server;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

import java.io.IOException;

public class S13PacketDestroyEntities extends Packet {
	private int[] field_149100_a;
	private static final String __OBFID = "CL_00001320";

	public S13PacketDestroyEntities() {
	}

	public S13PacketDestroyEntities(int... p_i45211_1_) {
		field_149100_a = p_i45211_1_;
	}

	@Override
	public void readPacketData(PacketBuffer p_148837_1_) throws IOException {
		field_149100_a = new int[p_148837_1_.readByte()];

		for (int i = 0; i < field_149100_a.length; ++i) {
			field_149100_a[i] = p_148837_1_.readInt();
		}
	}

	@Override
	public void writePacketData(PacketBuffer p_148840_1_) throws IOException {
		p_148840_1_.writeByte(field_149100_a.length);

		for (int i = 0; i < field_149100_a.length; ++i) {
			p_148840_1_.writeInt(field_149100_a[i]);
		}
	}

	public void processPacket(INetHandlerPlayClient p_148833_1_) {
		p_148833_1_.handleDestroyEntities(this);
	}

	@Override
	public String serialize() {
		StringBuilder stringbuilder = new StringBuilder();

		for (int i = 0; i < field_149100_a.length; ++i) {
			if (i > 0) {
				stringbuilder.append(", ");
			}

			stringbuilder.append(field_149100_a[i]);
		}

		return String.format("entities=%d[%s]", Integer.valueOf(field_149100_a.length), stringbuilder);
	}

	@SideOnly(Side.CLIENT)
	public int[] func_149098_c() {
		return field_149100_a;
	}

	@Override
	public void processPacket(INetHandler p_148833_1_) {
		this.processPacket((INetHandlerPlayClient) p_148833_1_);
	}
}