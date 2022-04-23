package net.minecraft.network.play.server;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

import java.io.IOException;

public class S10PacketSpawnPainting extends Packet {
	private int field_148973_a;
	private int field_148971_b;
	private int field_148972_c;
	private int field_148969_d;
	private int field_148970_e;
	private String field_148968_f;
	private static final String __OBFID = "CL_00001280";

	public S10PacketSpawnPainting() {
	}

	public S10PacketSpawnPainting(EntityPainting p_i45170_1_) {
		field_148973_a = p_i45170_1_.getEntityId();
		field_148971_b = p_i45170_1_.field_146063_b;
		field_148972_c = p_i45170_1_.field_146064_c;
		field_148969_d = p_i45170_1_.field_146062_d;
		field_148970_e = p_i45170_1_.hangingDirection;
		field_148968_f = p_i45170_1_.art.title;
	}

	@Override
	public void readPacketData(PacketBuffer p_148837_1_) throws IOException {
		field_148973_a = p_148837_1_.readVarIntFromBuffer();
		field_148968_f = p_148837_1_.readStringFromBuffer(EntityPainting.EnumArt.maxArtTitleLength);
		field_148971_b = p_148837_1_.readInt();
		field_148972_c = p_148837_1_.readInt();
		field_148969_d = p_148837_1_.readInt();
		field_148970_e = p_148837_1_.readInt();
	}

	@Override
	public void writePacketData(PacketBuffer p_148840_1_) throws IOException {
		p_148840_1_.writeVarIntToBuffer(field_148973_a);
		p_148840_1_.writeStringToBuffer(field_148968_f);
		p_148840_1_.writeInt(field_148971_b);
		p_148840_1_.writeInt(field_148972_c);
		p_148840_1_.writeInt(field_148969_d);
		p_148840_1_.writeInt(field_148970_e);
	}

	public void processPacket(INetHandlerPlayClient p_148833_1_) {
		p_148833_1_.handleSpawnPainting(this);
	}

	@Override
	public String serialize() {
		return String.format("id=%d, type=%s, x=%d, y=%d, z=%d",
				Integer.valueOf(field_148973_a), field_148968_f, Integer.valueOf(field_148971_b),
				Integer.valueOf(field_148972_c), Integer.valueOf(field_148969_d));
	}

	@SideOnly(Side.CLIENT)
	public int func_148965_c() {
		return field_148973_a;
	}

	@SideOnly(Side.CLIENT)
	public int func_148964_d() {
		return field_148971_b;
	}

	@SideOnly(Side.CLIENT)
	public int func_148963_e() {
		return field_148972_c;
	}

	@SideOnly(Side.CLIENT)
	public int func_148962_f() {
		return field_148969_d;
	}

	@SideOnly(Side.CLIENT)
	public int func_148966_g() {
		return field_148970_e;
	}

	@SideOnly(Side.CLIENT)
	public String func_148961_h() {
		return field_148968_f;
	}

	@Override
	public void processPacket(INetHandler p_148833_1_) {
		this.processPacket((INetHandlerPlayClient) p_148833_1_);
	}
}