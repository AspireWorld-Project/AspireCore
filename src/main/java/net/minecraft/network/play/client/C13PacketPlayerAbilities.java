package net.minecraft.network.play.client;

import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

import java.io.IOException;

public class C13PacketPlayerAbilities extends Packet {
	private boolean field_149500_a;
	private boolean field_149498_b;
	private boolean field_149499_c;
	private boolean field_149496_d;
	private float field_149497_e;
	private float field_149495_f;
	private static final String __OBFID = "CL_00001364";

	public C13PacketPlayerAbilities() {
	}

	public C13PacketPlayerAbilities(PlayerCapabilities p_i45257_1_) {
		func_149490_a(p_i45257_1_.disableDamage);
		func_149483_b(p_i45257_1_.isFlying);
		func_149491_c(p_i45257_1_.allowFlying);
		func_149493_d(p_i45257_1_.isCreativeMode);
		func_149485_a(p_i45257_1_.getFlySpeed());
		func_149492_b(p_i45257_1_.getWalkSpeed());
	}

	@Override
	public void readPacketData(PacketBuffer p_148837_1_) throws IOException {
		byte b0 = p_148837_1_.readByte();
		func_149490_a((b0 & 1) > 0);
		func_149483_b((b0 & 2) > 0);
		func_149491_c((b0 & 4) > 0);
		func_149493_d((b0 & 8) > 0);
		func_149485_a(p_148837_1_.readFloat());
		func_149492_b(p_148837_1_.readFloat());
	}

	@Override
	public void writePacketData(PacketBuffer p_148840_1_) throws IOException {
		byte b0 = 0;

		if (func_149494_c()) {
			b0 = (byte) (b0 | 1);
		}

		if (func_149488_d()) {
			b0 = (byte) (b0 | 2);
		}

		if (func_149486_e()) {
			b0 = (byte) (b0 | 4);
		}

		if (func_149484_f()) {
			b0 = (byte) (b0 | 8);
		}

		p_148840_1_.writeByte(b0);
		p_148840_1_.writeFloat(field_149497_e);
		p_148840_1_.writeFloat(field_149495_f);
	}

	public void processPacket(INetHandlerPlayServer p_148833_1_) {
		p_148833_1_.processPlayerAbilities(this);
	}

	@Override
	public String serialize() {
		return String.format("invuln=%b, flying=%b, canfly=%b, instabuild=%b, flyspeed=%.4f, walkspped=%.4f",
				Boolean.valueOf(func_149494_c()), Boolean.valueOf(func_149488_d()),
				Boolean.valueOf(func_149486_e()), Boolean.valueOf(func_149484_f()),
				Float.valueOf(func_149482_g()), Float.valueOf(func_149489_h()));
	}

	public boolean func_149494_c() {
		return field_149500_a;
	}

	public void func_149490_a(boolean p_149490_1_) {
		field_149500_a = p_149490_1_;
	}

	public boolean func_149488_d() {
		return field_149498_b;
	}

	public void func_149483_b(boolean p_149483_1_) {
		field_149498_b = p_149483_1_;
	}

	public boolean func_149486_e() {
		return field_149499_c;
	}

	public void func_149491_c(boolean p_149491_1_) {
		field_149499_c = p_149491_1_;
	}

	public boolean func_149484_f() {
		return field_149496_d;
	}

	public void func_149493_d(boolean p_149493_1_) {
		field_149496_d = p_149493_1_;
	}

	public float func_149482_g() {
		return field_149497_e;
	}

	public void func_149485_a(float p_149485_1_) {
		field_149497_e = p_149485_1_;
	}

	public float func_149489_h() {
		return field_149495_f;
	}

	public void func_149492_b(float p_149492_1_) {
		field_149495_f = p_149492_1_;
	}

	@Override
	public void processPacket(INetHandler p_148833_1_) {
		this.processPacket((INetHandlerPlayServer) p_148833_1_);
	}
}