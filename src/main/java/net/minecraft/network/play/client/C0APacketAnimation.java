package net.minecraft.network.play.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.Entity;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

import java.io.IOException;

public class C0APacketAnimation extends Packet {
	private int field_149424_a;
	private int field_149423_b;
	private static final String __OBFID = "CL_00001345";

	public C0APacketAnimation() {
	}

	@SideOnly(Side.CLIENT)
	public C0APacketAnimation(Entity p_i45238_1_, int p_i45238_2_) {
		field_149424_a = p_i45238_1_.getEntityId();
		field_149423_b = p_i45238_2_;
	}

	@Override
	public void readPacketData(PacketBuffer p_148837_1_) throws IOException {
		field_149424_a = p_148837_1_.readInt();
		field_149423_b = p_148837_1_.readByte();
	}

	@Override
	public void writePacketData(PacketBuffer p_148840_1_) throws IOException {
		p_148840_1_.writeInt(field_149424_a);
		p_148840_1_.writeByte(field_149423_b);
	}

	public void processPacket(INetHandlerPlayServer p_148833_1_) {
		p_148833_1_.processAnimation(this);
	}

	@Override
	public String serialize() {
		return String.format("id=%d, type=%d",
				Integer.valueOf(field_149424_a), Integer.valueOf(field_149423_b));
	}

	public int func_149421_d() {
		return field_149423_b;
	}

	@Override
	public void processPacket(INetHandler p_148833_1_) {
		this.processPacket((INetHandlerPlayServer) p_148833_1_);
	}
}