package net.minecraft.network.play.server;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.ItemStack;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

import java.io.IOException;

public class S04PacketEntityEquipment extends Packet {
	private int field_149394_a;
	private int field_149392_b;
	private ItemStack field_149393_c;
	private static final String __OBFID = "CL_00001330";

	public S04PacketEntityEquipment() {
	}

	public S04PacketEntityEquipment(int p_i45221_1_, int p_i45221_2_, ItemStack p_i45221_3_) {
		field_149394_a = p_i45221_1_;
		field_149392_b = p_i45221_2_;
		field_149393_c = p_i45221_3_ == null ? null : p_i45221_3_.copy();
	}

	@Override
	public void readPacketData(PacketBuffer p_148837_1_) throws IOException {
		field_149394_a = p_148837_1_.readInt();
		field_149392_b = p_148837_1_.readShort();
		field_149393_c = p_148837_1_.readItemStackFromBuffer();
	}

	@Override
	public void writePacketData(PacketBuffer p_148840_1_) throws IOException {
		p_148840_1_.writeInt(field_149394_a);
		p_148840_1_.writeShort(field_149392_b);
		p_148840_1_.writeItemStackToBuffer(field_149393_c);
	}

	public void processPacket(INetHandlerPlayClient p_148833_1_) {
		p_148833_1_.handleEntityEquipment(this);
	}

	@SideOnly(Side.CLIENT)
	public ItemStack func_149390_c() {
		return field_149393_c;
	}

	@Override
	public String serialize() {
		return String.format("entity=%d, slot=%d, item=%s",
				Integer.valueOf(field_149394_a), Integer.valueOf(field_149392_b), field_149393_c);
	}

	@SideOnly(Side.CLIENT)
	public int func_149389_d() {
		return field_149394_a;
	}

	@SideOnly(Side.CLIENT)
	public int func_149388_e() {
		return field_149392_b;
	}

	@Override
	public void processPacket(INetHandler p_148833_1_) {
		this.processPacket((INetHandlerPlayClient) p_148833_1_);
	}
}