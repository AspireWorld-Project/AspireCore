package net.minecraft.network.play.server;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

import java.io.IOException;

public class S36PacketSignEditorOpen extends Packet {
	private int field_149133_a;
	private int field_149131_b;
	private int field_149132_c;
	private static final String __OBFID = "CL_00001316";

	public S36PacketSignEditorOpen() {
	}

	public S36PacketSignEditorOpen(int p_i45207_1_, int p_i45207_2_, int p_i45207_3_) {
		field_149133_a = p_i45207_1_;
		field_149131_b = p_i45207_2_;
		field_149132_c = p_i45207_3_;
	}

	public void processPacket(INetHandlerPlayClient p_148833_1_) {
		p_148833_1_.handleSignEditorOpen(this);
	}

	@Override
	public void readPacketData(PacketBuffer p_148837_1_) throws IOException {
		field_149133_a = p_148837_1_.readInt();
		field_149131_b = p_148837_1_.readInt();
		field_149132_c = p_148837_1_.readInt();
	}

	@Override
	public void writePacketData(PacketBuffer p_148840_1_) throws IOException {
		p_148840_1_.writeInt(field_149133_a);
		p_148840_1_.writeInt(field_149131_b);
		p_148840_1_.writeInt(field_149132_c);
	}

	@SideOnly(Side.CLIENT)
	public int func_149129_c() {
		return field_149133_a;
	}

	@SideOnly(Side.CLIENT)
	public int func_149128_d() {
		return field_149131_b;
	}

	@SideOnly(Side.CLIENT)
	public int func_149127_e() {
		return field_149132_c;
	}

	@Override
	public void processPacket(INetHandler p_148833_1_) {
		this.processPacket((INetHandlerPlayClient) p_148833_1_);
	}
}