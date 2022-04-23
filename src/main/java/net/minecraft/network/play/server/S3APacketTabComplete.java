package net.minecraft.network.play.server;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import org.apache.commons.lang3.ArrayUtils;

import java.io.IOException;

public class S3APacketTabComplete extends Packet {
	private String[] field_149632_a;
	private static final String __OBFID = "CL_00001288";

	public S3APacketTabComplete() {
	}

	public S3APacketTabComplete(String[] p_i45178_1_) {
		field_149632_a = p_i45178_1_;
	}

	@Override
	public void readPacketData(PacketBuffer p_148837_1_) throws IOException {
		field_149632_a = new String[p_148837_1_.readVarIntFromBuffer()];

		for (int i = 0; i < field_149632_a.length; ++i) {
			field_149632_a[i] = p_148837_1_.readStringFromBuffer(32767);
		}
	}

	@Override
	public void writePacketData(PacketBuffer p_148840_1_) throws IOException {
		p_148840_1_.writeVarIntToBuffer(field_149632_a.length);
		String[] astring = field_149632_a;
		int i = astring.length;

		for (int j = 0; j < i; ++j) {
			String s = astring[j];
			p_148840_1_.writeStringToBuffer(s);
		}
	}

	public void processPacket(INetHandlerPlayClient p_148833_1_) {
		p_148833_1_.handleTabComplete(this);
	}

	@SideOnly(Side.CLIENT)
	public String[] func_149630_c() {
		return field_149632_a;
	}

	@Override
	public String serialize() {
		return String.format("candidates='%s'", ArrayUtils.toString(field_149632_a));
	}

	@Override
	public void processPacket(INetHandler p_148833_1_) {
		this.processPacket((INetHandlerPlayClient) p_148833_1_);
	}
}