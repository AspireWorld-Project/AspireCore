package net.minecraft.network.play.server;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.potion.PotionEffect;

import java.io.IOException;

public class S1EPacketRemoveEntityEffect extends Packet {
	private int field_149079_a;
	private int field_149078_b;
	private static final String __OBFID = "CL_00001321";

	public S1EPacketRemoveEntityEffect() {
	}

	public S1EPacketRemoveEntityEffect(int p_i45212_1_, PotionEffect p_i45212_2_) {
		field_149079_a = p_i45212_1_;
		field_149078_b = p_i45212_2_.getPotionID();
	}

	@Override
	public void readPacketData(PacketBuffer p_148837_1_) throws IOException {
		field_149079_a = p_148837_1_.readInt();
		field_149078_b = p_148837_1_.readUnsignedByte();
	}

	@Override
	public void writePacketData(PacketBuffer p_148840_1_) throws IOException {
		p_148840_1_.writeInt(field_149079_a);
		p_148840_1_.writeByte(field_149078_b);
	}

	public void processPacket(INetHandlerPlayClient p_148833_1_) {
		p_148833_1_.handleRemoveEntityEffect(this);
	}

	@Override
	public void processPacket(INetHandler p_148833_1_) {
		this.processPacket((INetHandlerPlayClient) p_148833_1_);
	}

	@SideOnly(Side.CLIENT)
	public int func_149076_c() {
		return field_149079_a;
	}

	@SideOnly(Side.CLIENT)
	public int func_149075_d() {
		return field_149078_b;
	}
}