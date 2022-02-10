package net.minecraft.network.play.server;

import java.io.IOException;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class S32PacketConfirmTransaction extends Packet {
	private int field_148894_a;
	private short field_148892_b;
	private boolean field_148893_c;
	private static final String __OBFID = "CL_00001291";

	public S32PacketConfirmTransaction() {
	}

	public S32PacketConfirmTransaction(int p_i45182_1_, short p_i45182_2_, boolean p_i45182_3_) {
		field_148894_a = p_i45182_1_;
		field_148892_b = p_i45182_2_;
		field_148893_c = p_i45182_3_;
	}

	public void processPacket(INetHandlerPlayClient p_148833_1_) {
		p_148833_1_.handleConfirmTransaction(this);
	}

	@Override
	public void readPacketData(PacketBuffer p_148837_1_) throws IOException {
		field_148894_a = p_148837_1_.readUnsignedByte();
		field_148892_b = p_148837_1_.readShort();
		field_148893_c = p_148837_1_.readBoolean();
	}

	@Override
	public void writePacketData(PacketBuffer p_148840_1_) throws IOException {
		p_148840_1_.writeByte(field_148894_a);
		p_148840_1_.writeShort(field_148892_b);
		p_148840_1_.writeBoolean(field_148893_c);
	}

	@Override
	public String serialize() {
		return String.format("id=%d, uid=%d, accepted=%b", new Object[] { Integer.valueOf(field_148894_a),
				Short.valueOf(field_148892_b), Boolean.valueOf(field_148893_c) });
	}

	@SideOnly(Side.CLIENT)
	public int func_148889_c() {
		return field_148894_a;
	}

	@SideOnly(Side.CLIENT)
	public short func_148890_d() {
		return field_148892_b;
	}

	@SideOnly(Side.CLIENT)
	public boolean func_148888_e() {
		return field_148893_c;
	}

	@Override
	public void processPacket(INetHandler p_148833_1_) {
		this.processPacket((INetHandlerPlayClient) p_148833_1_);
	}
}