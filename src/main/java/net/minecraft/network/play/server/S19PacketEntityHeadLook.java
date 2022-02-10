package net.minecraft.network.play.server;

import java.io.IOException;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.Entity;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.world.World;

public class S19PacketEntityHeadLook extends Packet {
	private int field_149384_a;
	private byte field_149383_b;
	private static final String __OBFID = "CL_00001323";

	public S19PacketEntityHeadLook() {
	}

	public S19PacketEntityHeadLook(Entity p_i45214_1_, byte p_i45214_2_) {
		field_149384_a = p_i45214_1_.getEntityId();
		field_149383_b = p_i45214_2_;
	}

	@Override
	public void readPacketData(PacketBuffer p_148837_1_) throws IOException {
		field_149384_a = p_148837_1_.readInt();
		field_149383_b = p_148837_1_.readByte();
	}

	@Override
	public void writePacketData(PacketBuffer p_148840_1_) throws IOException {
		p_148840_1_.writeInt(field_149384_a);
		p_148840_1_.writeByte(field_149383_b);
	}

	public void processPacket(INetHandlerPlayClient p_148833_1_) {
		p_148833_1_.handleEntityHeadLook(this);
	}

	@Override
	public String serialize() {
		return String.format("id=%d, rot=%d",
				new Object[] { Integer.valueOf(field_149384_a), Byte.valueOf(field_149383_b) });
	}

	@SideOnly(Side.CLIENT)
	public Entity func_149381_a(World p_149381_1_) {
		return p_149381_1_.getEntityByID(field_149384_a);
	}

	@Override
	public void processPacket(INetHandler p_148833_1_) {
		this.processPacket((INetHandlerPlayClient) p_148833_1_);
	}

	@SideOnly(Side.CLIENT)
	public byte func_149380_c() {
		return field_149383_b;
	}
}