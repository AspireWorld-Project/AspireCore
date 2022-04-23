package net.minecraft.network.play.server;

import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

import java.io.IOException;

public class S39PacketPlayerAbilities extends Packet {
	private boolean field_149119_a;
	private boolean field_149117_b;
	private boolean field_149118_c;
	private boolean field_149115_d;
	private float field_149116_e;
	private float field_149114_f;
	private static final String __OBFID = "CL_00001317";

	public S39PacketPlayerAbilities() {
	}

	public S39PacketPlayerAbilities(PlayerCapabilities p_i45208_1_) {
		func_149108_a(p_i45208_1_.disableDamage);
		func_149102_b(p_i45208_1_.isFlying);
		func_149109_c(p_i45208_1_.allowFlying);
		func_149111_d(p_i45208_1_.isCreativeMode);
		func_149104_a(p_i45208_1_.getFlySpeed());
		func_149110_b(p_i45208_1_.getWalkSpeed());
	}

	@Override
	public void readPacketData(PacketBuffer p_148837_1_) throws IOException {
		byte b0 = p_148837_1_.readByte();
		func_149108_a((b0 & 1) > 0);
		func_149102_b((b0 & 2) > 0);
		func_149109_c((b0 & 4) > 0);
		func_149111_d((b0 & 8) > 0);
		func_149104_a(p_148837_1_.readFloat());
		func_149110_b(p_148837_1_.readFloat());
	}

	@Override
	public void writePacketData(PacketBuffer p_148840_1_) throws IOException {
		byte b0 = 0;

		if (func_149112_c()) {
			b0 = (byte) (b0 | 1);
		}

		if (func_149106_d()) {
			b0 = (byte) (b0 | 2);
		}

		if (func_149105_e()) {
			b0 = (byte) (b0 | 4);
		}

		if (func_149103_f()) {
			b0 = (byte) (b0 | 8);
		}

		p_148840_1_.writeByte(b0);
		p_148840_1_.writeFloat(field_149116_e);
		p_148840_1_.writeFloat(field_149114_f);
	}

	public void processPacket(INetHandlerPlayClient p_148833_1_) {
		p_148833_1_.handlePlayerAbilities(this);
	}

	@Override
	public String serialize() {
		return String.format("invuln=%b, flying=%b, canfly=%b, instabuild=%b, flyspeed=%.4f, walkspped=%.4f",
				Boolean.valueOf(func_149112_c()), Boolean.valueOf(func_149106_d()),
				Boolean.valueOf(func_149105_e()), Boolean.valueOf(func_149103_f()),
				Float.valueOf(func_149101_g()), Float.valueOf(func_149107_h()));
	}

	public boolean func_149112_c() {
		return field_149119_a;
	}

	public void func_149108_a(boolean p_149108_1_) {
		field_149119_a = p_149108_1_;
	}

	public boolean func_149106_d() {
		return field_149117_b;
	}

	public void func_149102_b(boolean p_149102_1_) {
		field_149117_b = p_149102_1_;
	}

	public boolean func_149105_e() {
		return field_149118_c;
	}

	public void func_149109_c(boolean p_149109_1_) {
		field_149118_c = p_149109_1_;
	}

	public boolean func_149103_f() {
		return field_149115_d;
	}

	public void func_149111_d(boolean p_149111_1_) {
		field_149115_d = p_149111_1_;
	}

	public float func_149101_g() {
		return field_149116_e;
	}

	public void func_149104_a(float p_149104_1_) {
		field_149116_e = p_149104_1_;
	}

	public float func_149107_h() {
		return field_149114_f;
	}

	public void func_149110_b(float p_149110_1_) {
		field_149114_f = p_149110_1_;
	}

	@Override
	public void processPacket(INetHandler p_148833_1_) {
		this.processPacket((INetHandlerPlayClient) p_148833_1_);
	}
}