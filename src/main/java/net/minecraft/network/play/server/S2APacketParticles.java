package net.minecraft.network.play.server;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

import java.io.IOException;

public class S2APacketParticles extends Packet {
	private String field_149236_a;
	private float field_149234_b;
	private float field_149235_c;
	private float field_149232_d;
	private float field_149233_e;
	private float field_149230_f;
	private float field_149231_g;
	private float field_149237_h;
	private int field_149238_i;
	private static final String __OBFID = "CL_00001308";

	public S2APacketParticles() {
	}

	public S2APacketParticles(String p_i45199_1_, float p_i45199_2_, float p_i45199_3_, float p_i45199_4_,
			float p_i45199_5_, float p_i45199_6_, float p_i45199_7_, float p_i45199_8_, int p_i45199_9_) {
		field_149236_a = p_i45199_1_;
		field_149234_b = p_i45199_2_;
		field_149235_c = p_i45199_3_;
		field_149232_d = p_i45199_4_;
		field_149233_e = p_i45199_5_;
		field_149230_f = p_i45199_6_;
		field_149231_g = p_i45199_7_;
		field_149237_h = p_i45199_8_;
		field_149238_i = p_i45199_9_;
	}

	@Override
	public void readPacketData(PacketBuffer p_148837_1_) throws IOException {
		field_149236_a = p_148837_1_.readStringFromBuffer(64);
		field_149234_b = p_148837_1_.readFloat();
		field_149235_c = p_148837_1_.readFloat();
		field_149232_d = p_148837_1_.readFloat();
		field_149233_e = p_148837_1_.readFloat();
		field_149230_f = p_148837_1_.readFloat();
		field_149231_g = p_148837_1_.readFloat();
		field_149237_h = p_148837_1_.readFloat();
		field_149238_i = p_148837_1_.readInt();
	}

	@Override
	public void writePacketData(PacketBuffer p_148840_1_) throws IOException {
		p_148840_1_.writeStringToBuffer(field_149236_a);
		p_148840_1_.writeFloat(field_149234_b);
		p_148840_1_.writeFloat(field_149235_c);
		p_148840_1_.writeFloat(field_149232_d);
		p_148840_1_.writeFloat(field_149233_e);
		p_148840_1_.writeFloat(field_149230_f);
		p_148840_1_.writeFloat(field_149231_g);
		p_148840_1_.writeFloat(field_149237_h);
		p_148840_1_.writeInt(field_149238_i);
	}

	@SideOnly(Side.CLIENT)
	public String func_149228_c() {
		return field_149236_a;
	}

	@SideOnly(Side.CLIENT)
	public double func_149220_d() {
		return field_149234_b;
	}

	@SideOnly(Side.CLIENT)
	public double func_149226_e() {
		return field_149235_c;
	}

	@SideOnly(Side.CLIENT)
	public double func_149225_f() {
		return field_149232_d;
	}

	@SideOnly(Side.CLIENT)
	public float func_149221_g() {
		return field_149233_e;
	}

	@SideOnly(Side.CLIENT)
	public float func_149224_h() {
		return field_149230_f;
	}

	@SideOnly(Side.CLIENT)
	public float func_149223_i() {
		return field_149231_g;
	}

	@SideOnly(Side.CLIENT)
	public float func_149227_j() {
		return field_149237_h;
	}

	@SideOnly(Side.CLIENT)
	public int func_149222_k() {
		return field_149238_i;
	}

	public void processPacket(INetHandlerPlayClient p_148833_1_) {
		p_148833_1_.handleParticles(this);
	}

	@Override
	public void processPacket(INetHandler p_148833_1_) {
		this.processPacket((INetHandlerPlayClient) p_148833_1_);
	}
}