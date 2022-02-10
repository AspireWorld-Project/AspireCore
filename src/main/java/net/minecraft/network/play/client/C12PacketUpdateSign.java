package net.minecraft.network.play.client;

import java.io.IOException;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class C12PacketUpdateSign extends Packet {
	private int field_149593_a;
	private int field_149591_b;
	private int field_149592_c;
	private String[] field_149590_d;
	private static final String __OBFID = "CL_00001370";

	public C12PacketUpdateSign() {
	}

	@SideOnly(Side.CLIENT)
	public C12PacketUpdateSign(int p_i45264_1_, int p_i45264_2_, int p_i45264_3_, String[] p_i45264_4_) {
		field_149593_a = p_i45264_1_;
		field_149591_b = p_i45264_2_;
		field_149592_c = p_i45264_3_;
		field_149590_d = new String[] { p_i45264_4_[0], p_i45264_4_[1], p_i45264_4_[2], p_i45264_4_[3] };
	}

	@Override
	public void readPacketData(PacketBuffer p_148837_1_) throws IOException {
		field_149593_a = p_148837_1_.readInt();
		field_149591_b = p_148837_1_.readShort();
		field_149592_c = p_148837_1_.readInt();
		field_149590_d = new String[4];

		for (int i = 0; i < 4; ++i) {
			field_149590_d[i] = p_148837_1_.readStringFromBuffer(15);
		}
	}

	@Override
	public void writePacketData(PacketBuffer p_148840_1_) throws IOException {
		p_148840_1_.writeInt(field_149593_a);
		p_148840_1_.writeShort(field_149591_b);
		p_148840_1_.writeInt(field_149592_c);

		for (int i = 0; i < 4; ++i) {
			p_148840_1_.writeStringToBuffer(field_149590_d[i]);
		}
	}

	public void processPacket(INetHandlerPlayServer p_148833_1_) {
		p_148833_1_.processUpdateSign(this);
	}

	public int func_149588_c() {
		return field_149593_a;
	}

	public int func_149586_d() {
		return field_149591_b;
	}

	public int func_149585_e() {
		return field_149592_c;
	}

	public String[] func_149589_f() {
		return field_149590_d;
	}

	@Override
	public void processPacket(INetHandler p_148833_1_) {
		this.processPacket((INetHandlerPlayServer) p_148833_1_);
	}
}