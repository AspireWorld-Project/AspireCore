package net.minecraft.network.play.server;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

import java.io.IOException;

public class S33PacketUpdateSign extends Packet {
	private int field_149352_a;
	private int field_149350_b;
	private int field_149351_c;
	private String[] field_149349_d;
	private static final String __OBFID = "CL_00001338";

	public S33PacketUpdateSign() {
	}

	public S33PacketUpdateSign(int p_i45231_1_, int p_i45231_2_, int p_i45231_3_, String[] p_i45231_4_) {
		field_149352_a = p_i45231_1_;
		field_149350_b = p_i45231_2_;
		field_149351_c = p_i45231_3_;
		field_149349_d = new String[] { p_i45231_4_[0], p_i45231_4_[1], p_i45231_4_[2], p_i45231_4_[3] };
	}

	@Override
	public void readPacketData(PacketBuffer p_148837_1_) throws IOException {
		field_149352_a = p_148837_1_.readInt();
		field_149350_b = p_148837_1_.readShort();
		field_149351_c = p_148837_1_.readInt();
		field_149349_d = new String[4];

		for (int i = 0; i < 4; ++i) {
			field_149349_d[i] = p_148837_1_.readStringFromBuffer(15);
		}
	}

	@Override
	public void writePacketData(PacketBuffer p_148840_1_) throws IOException {
		p_148840_1_.writeInt(field_149352_a);
		p_148840_1_.writeShort(field_149350_b);
		p_148840_1_.writeInt(field_149351_c);

		for (int i = 0; i < 4; ++i) {
			p_148840_1_.writeStringToBuffer(field_149349_d[i]);
		}
	}

	public void processPacket(INetHandlerPlayClient p_148833_1_) {
		p_148833_1_.handleUpdateSign(this);
	}

	@Override
	public void processPacket(INetHandler p_148833_1_) {
		this.processPacket((INetHandlerPlayClient) p_148833_1_);
	}

	@SideOnly(Side.CLIENT)
	public int func_149346_c() {
		return field_149352_a;
	}

	@SideOnly(Side.CLIENT)
	public int func_149345_d() {
		return field_149350_b;
	}

	@SideOnly(Side.CLIENT)
	public int func_149344_e() {
		return field_149351_c;
	}

	@SideOnly(Side.CLIENT)
	public String[] func_149347_f() {
		return field_149349_d;
	}
}