package net.minecraft.network.play.server;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.Entity;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.MathHelper;

import java.io.IOException;

public class S18PacketEntityTeleport extends Packet {
	private int field_149458_a;
	private int field_149456_b;
	private int field_149457_c;
	private int field_149454_d;
	private byte field_149455_e;
	private byte field_149453_f;
	private static final String __OBFID = "CL_00001340";

	public S18PacketEntityTeleport() {
	}

	public S18PacketEntityTeleport(Entity p_i45233_1_) {
		field_149458_a = p_i45233_1_.getEntityId();
		field_149456_b = MathHelper.floor_double(p_i45233_1_.posX * 32.0D);
		field_149457_c = MathHelper.floor_double(p_i45233_1_.posY * 32.0D);
		field_149454_d = MathHelper.floor_double(p_i45233_1_.posZ * 32.0D);
		field_149455_e = (byte) (int) (p_i45233_1_.rotationYaw * 256.0F / 360.0F);
		field_149453_f = (byte) (int) (p_i45233_1_.rotationPitch * 256.0F / 360.0F);
	}

	public S18PacketEntityTeleport(int p_i45234_1_, int p_i45234_2_, int p_i45234_3_, int p_i45234_4_, byte p_i45234_5_,
			byte p_i45234_6_) {
		field_149458_a = p_i45234_1_;
		field_149456_b = p_i45234_2_;
		field_149457_c = p_i45234_3_;
		field_149454_d = p_i45234_4_;
		field_149455_e = p_i45234_5_;
		field_149453_f = p_i45234_6_;
	}

	@Override
	public void readPacketData(PacketBuffer p_148837_1_) throws IOException {
		field_149458_a = p_148837_1_.readInt();
		field_149456_b = p_148837_1_.readInt();
		field_149457_c = p_148837_1_.readInt();
		field_149454_d = p_148837_1_.readInt();
		field_149455_e = p_148837_1_.readByte();
		field_149453_f = p_148837_1_.readByte();
	}

	@Override
	public void writePacketData(PacketBuffer p_148840_1_) throws IOException {
		p_148840_1_.writeInt(field_149458_a);
		p_148840_1_.writeInt(field_149456_b);
		p_148840_1_.writeInt(field_149457_c);
		p_148840_1_.writeInt(field_149454_d);
		p_148840_1_.writeByte(field_149455_e);
		p_148840_1_.writeByte(field_149453_f);
	}

	public void processPacket(INetHandlerPlayClient p_148833_1_) {
		p_148833_1_.handleEntityTeleport(this);
	}

	@Override
	public void processPacket(INetHandler p_148833_1_) {
		this.processPacket((INetHandlerPlayClient) p_148833_1_);
	}

	@SideOnly(Side.CLIENT)
	public int func_149451_c() {
		return field_149458_a;
	}

	@SideOnly(Side.CLIENT)
	public int func_149449_d() {
		return field_149456_b;
	}

	@SideOnly(Side.CLIENT)
	public int func_149448_e() {
		return field_149457_c;
	}

	@SideOnly(Side.CLIENT)
	public int func_149446_f() {
		return field_149454_d;
	}

	@SideOnly(Side.CLIENT)
	public byte func_149450_g() {
		return field_149455_e;
	}

	@SideOnly(Side.CLIENT)
	public byte func_149447_h() {
		return field_149453_f;
	}
}