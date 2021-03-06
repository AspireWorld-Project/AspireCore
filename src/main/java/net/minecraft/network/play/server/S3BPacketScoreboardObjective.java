package net.minecraft.network.play.server;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.scoreboard.ScoreObjective;

import java.io.IOException;

public class S3BPacketScoreboardObjective extends Packet {
	private String field_149343_a;
	private String field_149341_b;
	private int field_149342_c;
	private static final String __OBFID = "CL_00001333";

	public S3BPacketScoreboardObjective() {
	}

	public S3BPacketScoreboardObjective(ScoreObjective p_i45224_1_, int p_i45224_2_) {
		field_149343_a = p_i45224_1_.getName();
		field_149341_b = p_i45224_1_.getDisplayName();
		field_149342_c = p_i45224_2_;
	}

	@Override
	public void readPacketData(PacketBuffer p_148837_1_) throws IOException {
		field_149343_a = p_148837_1_.readStringFromBuffer(16);
		field_149341_b = p_148837_1_.readStringFromBuffer(32);
		field_149342_c = p_148837_1_.readByte();
	}

	@Override
	public void writePacketData(PacketBuffer p_148840_1_) throws IOException {
		p_148840_1_.writeStringToBuffer(field_149343_a);
		p_148840_1_.writeStringToBuffer(field_149341_b);
		p_148840_1_.writeByte(field_149342_c);
	}

	public void processPacket(INetHandlerPlayClient p_148833_1_) {
		p_148833_1_.handleScoreboardObjective(this);
	}

	@Override
	public void processPacket(INetHandler p_148833_1_) {
		this.processPacket((INetHandlerPlayClient) p_148833_1_);
	}

	@SideOnly(Side.CLIENT)
	public String func_149339_c() {
		return field_149343_a;
	}

	@SideOnly(Side.CLIENT)
	public String func_149337_d() {
		return field_149341_b;
	}

	@SideOnly(Side.CLIENT)
	public int func_149338_e() {
		return field_149342_c;
	}
}