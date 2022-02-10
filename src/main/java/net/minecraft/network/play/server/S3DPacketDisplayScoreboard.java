package net.minecraft.network.play.server;

import java.io.IOException;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.scoreboard.ScoreObjective;

public class S3DPacketDisplayScoreboard extends Packet {
	private int field_149374_a;
	private String field_149373_b;
	private static final String __OBFID = "CL_00001325";

	public S3DPacketDisplayScoreboard() {
	}

	public S3DPacketDisplayScoreboard(int p_i45216_1_, ScoreObjective p_i45216_2_) {
		field_149374_a = p_i45216_1_;

		if (p_i45216_2_ == null) {
			field_149373_b = "";
		} else {
			field_149373_b = p_i45216_2_.getName();
		}
	}

	@Override
	public void readPacketData(PacketBuffer p_148837_1_) throws IOException {
		field_149374_a = p_148837_1_.readByte();
		field_149373_b = p_148837_1_.readStringFromBuffer(16);
	}

	@Override
	public void writePacketData(PacketBuffer p_148840_1_) throws IOException {
		p_148840_1_.writeByte(field_149374_a);
		p_148840_1_.writeStringToBuffer(field_149373_b);
	}

	public void processPacket(INetHandlerPlayClient p_148833_1_) {
		p_148833_1_.handleDisplayScoreboard(this);
	}

	@Override
	public void processPacket(INetHandler p_148833_1_) {
		this.processPacket((INetHandlerPlayClient) p_148833_1_);
	}

	@SideOnly(Side.CLIENT)
	public int func_149371_c() {
		return field_149374_a;
	}

	@SideOnly(Side.CLIENT)
	public String func_149370_d() {
		return field_149373_b;
	}
}