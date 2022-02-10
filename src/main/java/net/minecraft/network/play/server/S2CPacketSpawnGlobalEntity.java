package net.minecraft.network.play.server;

import java.io.IOException;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.MathHelper;

public class S2CPacketSpawnGlobalEntity extends Packet {
	private int field_149059_a;
	private int field_149057_b;
	private int field_149058_c;
	private int field_149055_d;
	private int field_149056_e;
	private static final String __OBFID = "CL_00001278";

	public S2CPacketSpawnGlobalEntity() {
	}

	public S2CPacketSpawnGlobalEntity(Entity p_i45191_1_) {
		field_149059_a = p_i45191_1_.getEntityId();
		field_149057_b = MathHelper.floor_double(p_i45191_1_.posX * 32.0D);
		field_149058_c = MathHelper.floor_double(p_i45191_1_.posY * 32.0D);
		field_149055_d = MathHelper.floor_double(p_i45191_1_.posZ * 32.0D);

		if (p_i45191_1_ instanceof EntityLightningBolt) {
			field_149056_e = 1;
		}
	}

	@Override
	public void readPacketData(PacketBuffer p_148837_1_) throws IOException {
		field_149059_a = p_148837_1_.readVarIntFromBuffer();
		field_149056_e = p_148837_1_.readByte();
		field_149057_b = p_148837_1_.readInt();
		field_149058_c = p_148837_1_.readInt();
		field_149055_d = p_148837_1_.readInt();
	}

	@Override
	public void writePacketData(PacketBuffer p_148840_1_) throws IOException {
		p_148840_1_.writeVarIntToBuffer(field_149059_a);
		p_148840_1_.writeByte(field_149056_e);
		p_148840_1_.writeInt(field_149057_b);
		p_148840_1_.writeInt(field_149058_c);
		p_148840_1_.writeInt(field_149055_d);
	}

	public void processPacket(INetHandlerPlayClient p_148833_1_) {
		p_148833_1_.handleSpawnGlobalEntity(this);
	}

	@Override
	public String serialize() {
		return String.format("id=%d, type=%d, x=%.2f, y=%.2f, z=%.2f",
				new Object[] { Integer.valueOf(field_149059_a), Integer.valueOf(field_149056_e),
						Float.valueOf(field_149057_b / 32.0F), Float.valueOf(field_149058_c / 32.0F),
						Float.valueOf(field_149055_d / 32.0F) });
	}

	@SideOnly(Side.CLIENT)
	public int func_149052_c() {
		return field_149059_a;
	}

	@SideOnly(Side.CLIENT)
	public int func_149051_d() {
		return field_149057_b;
	}

	@SideOnly(Side.CLIENT)
	public int func_149050_e() {
		return field_149058_c;
	}

	@SideOnly(Side.CLIENT)
	public int func_149049_f() {
		return field_149055_d;
	}

	@SideOnly(Side.CLIENT)
	public int func_149053_g() {
		return field_149056_e;
	}

	@Override
	public void processPacket(INetHandler p_148833_1_) {
		this.processPacket((INetHandlerPlayClient) p_148833_1_);
	}
}