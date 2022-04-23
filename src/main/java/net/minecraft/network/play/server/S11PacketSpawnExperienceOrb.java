package net.minecraft.network.play.server;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.MathHelper;

import java.io.IOException;

public class S11PacketSpawnExperienceOrb extends Packet {
	private int field_148992_a;
	private int field_148990_b;
	private int field_148991_c;
	private int field_148988_d;
	private int field_148989_e;
	private static final String __OBFID = "CL_00001277";

	public S11PacketSpawnExperienceOrb() {
	}

	public S11PacketSpawnExperienceOrb(EntityXPOrb p_i45167_1_) {
		field_148992_a = p_i45167_1_.getEntityId();
		field_148990_b = MathHelper.floor_double(p_i45167_1_.posX * 32.0D);
		field_148991_c = MathHelper.floor_double(p_i45167_1_.posY * 32.0D);
		field_148988_d = MathHelper.floor_double(p_i45167_1_.posZ * 32.0D);
		field_148989_e = p_i45167_1_.getXpValue();
	}

	@Override
	public void readPacketData(PacketBuffer p_148837_1_) throws IOException {
		field_148992_a = p_148837_1_.readVarIntFromBuffer();
		field_148990_b = p_148837_1_.readInt();
		field_148991_c = p_148837_1_.readInt();
		field_148988_d = p_148837_1_.readInt();
		field_148989_e = p_148837_1_.readShort();
	}

	@Override
	public void writePacketData(PacketBuffer p_148840_1_) throws IOException {
		p_148840_1_.writeVarIntToBuffer(field_148992_a);
		p_148840_1_.writeInt(field_148990_b);
		p_148840_1_.writeInt(field_148991_c);
		p_148840_1_.writeInt(field_148988_d);
		p_148840_1_.writeShort(field_148989_e);
	}

	public void processPacket(INetHandlerPlayClient p_148833_1_) {
		p_148833_1_.handleSpawnExperienceOrb(this);
	}

	@Override
	public String serialize() {
		return String.format("id=%d, value=%d, x=%.2f, y=%.2f, z=%.2f",
				Integer.valueOf(field_148992_a), Integer.valueOf(field_148989_e),
				Float.valueOf(field_148990_b / 32.0F), Float.valueOf(field_148991_c / 32.0F),
				Float.valueOf(field_148988_d / 32.0F));
	}

	@SideOnly(Side.CLIENT)
	public int func_148985_c() {
		return field_148992_a;
	}

	@SideOnly(Side.CLIENT)
	public int func_148984_d() {
		return field_148990_b;
	}

	@SideOnly(Side.CLIENT)
	public int func_148983_e() {
		return field_148991_c;
	}

	@SideOnly(Side.CLIENT)
	public int func_148982_f() {
		return field_148988_d;
	}

	@SideOnly(Side.CLIENT)
	public int func_148986_g() {
		return field_148989_e;
	}

	@Override
	public void processPacket(INetHandler p_148833_1_) {
		this.processPacket((INetHandlerPlayClient) p_148833_1_);
	}
}