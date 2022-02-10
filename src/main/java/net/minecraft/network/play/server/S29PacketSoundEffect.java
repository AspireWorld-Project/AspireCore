package net.minecraft.network.play.server;

import java.io.IOException;

import org.apache.commons.lang3.Validate;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class S29PacketSoundEffect extends Packet {
	private String field_149219_a;
	private int field_149217_b;
	private int field_149218_c = Integer.MAX_VALUE;
	private int field_149215_d;
	private float field_149216_e;
	private int field_149214_f;
	private static final String __OBFID = "CL_00001309";

	public S29PacketSoundEffect() {
	}

	public S29PacketSoundEffect(String p_i45200_1_, double p_i45200_2_, double p_i45200_4_, double p_i45200_6_,
			float p_i45200_8_, float p_i45200_9_) {
		Validate.notNull(p_i45200_1_, "name", new Object[0]);
		field_149219_a = p_i45200_1_;
		field_149217_b = (int) (p_i45200_2_ * 8.0D);
		field_149218_c = (int) (p_i45200_4_ * 8.0D);
		field_149215_d = (int) (p_i45200_6_ * 8.0D);
		field_149216_e = p_i45200_8_;
		field_149214_f = (int) (p_i45200_9_ * 63.0F);

		if (field_149214_f < 0) {
			field_149214_f = 0;
		}

		if (field_149214_f > 255) {
			field_149214_f = 255;
		}
	}

	@Override
	public void readPacketData(PacketBuffer p_148837_1_) throws IOException {
		field_149219_a = p_148837_1_.readStringFromBuffer(256);
		field_149217_b = p_148837_1_.readInt();
		field_149218_c = p_148837_1_.readInt();
		field_149215_d = p_148837_1_.readInt();
		field_149216_e = p_148837_1_.readFloat();
		field_149214_f = p_148837_1_.readUnsignedByte();
	}

	@Override
	public void writePacketData(PacketBuffer p_148840_1_) throws IOException {
		p_148840_1_.writeStringToBuffer(field_149219_a);
		p_148840_1_.writeInt(field_149217_b);
		p_148840_1_.writeInt(field_149218_c);
		p_148840_1_.writeInt(field_149215_d);
		p_148840_1_.writeFloat(field_149216_e);
		p_148840_1_.writeByte(field_149214_f);
	}

	@SideOnly(Side.CLIENT)
	public String func_149212_c() {
		return field_149219_a;
	}

	@SideOnly(Side.CLIENT)
	public double func_149207_d() {
		return field_149217_b / 8.0F;
	}

	@SideOnly(Side.CLIENT)
	public double func_149211_e() {
		return field_149218_c / 8.0F;
	}

	@SideOnly(Side.CLIENT)
	public double func_149210_f() {
		return field_149215_d / 8.0F;
	}

	@SideOnly(Side.CLIENT)
	public float func_149208_g() {
		return field_149216_e;
	}

	@SideOnly(Side.CLIENT)
	public float func_149209_h() {
		return field_149214_f / 63.0F;
	}

	public void processPacket(INetHandlerPlayClient p_148833_1_) {
		p_148833_1_.handleSoundEffect(this);
	}

	@Override
	public void processPacket(INetHandler p_148833_1_) {
		this.processPacket((INetHandlerPlayClient) p_148833_1_);
	}
}