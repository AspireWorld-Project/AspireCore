package net.minecraft.network.play.server;

import java.io.IOException;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class S05PacketSpawnPosition extends Packet {
	private int field_149364_a;
	private int field_149362_b;
	private int field_149363_c;
	private static final String __OBFID = "CL_00001336";

	public S05PacketSpawnPosition() {
	}

	public S05PacketSpawnPosition(int p_i45229_1_, int p_i45229_2_, int p_i45229_3_) {
		field_149364_a = p_i45229_1_;
		field_149362_b = p_i45229_2_;
		field_149363_c = p_i45229_3_;
	}

	@Override
	public void readPacketData(PacketBuffer p_148837_1_) throws IOException {
		field_149364_a = p_148837_1_.readInt();
		field_149362_b = p_148837_1_.readInt();
		field_149363_c = p_148837_1_.readInt();
	}

	@Override
	public void writePacketData(PacketBuffer p_148840_1_) throws IOException {
		p_148840_1_.writeInt(field_149364_a);
		p_148840_1_.writeInt(field_149362_b);
		p_148840_1_.writeInt(field_149363_c);
	}

	public void processPacket(INetHandlerPlayClient p_148833_1_) {
		p_148833_1_.handleSpawnPosition(this);
	}

	@Override
	public boolean hasPriority() {
		return false;
	}

	@Override
	public String serialize() {
		return String.format("x=%d, y=%d, z=%d", new Object[] { Integer.valueOf(field_149364_a),
				Integer.valueOf(field_149362_b), Integer.valueOf(field_149363_c) });
	}

	@SideOnly(Side.CLIENT)
	public int func_149360_c() {
		return field_149364_a;
	}

	@SideOnly(Side.CLIENT)
	public int func_149359_d() {
		return field_149362_b;
	}

	@SideOnly(Side.CLIENT)
	public int func_149358_e() {
		return field_149363_c;
	}

	@Override
	public void processPacket(INetHandler p_148833_1_) {
		this.processPacket((INetHandlerPlayClient) p_148833_1_);
	}
}