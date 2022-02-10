package net.minecraft.network.login.server;

import java.io.IOException;
import java.security.PublicKey;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.login.INetHandlerLoginClient;
import net.minecraft.util.CryptManager;

public class S01PacketEncryptionRequest extends Packet {
	private String field_149612_a;
	private PublicKey field_149610_b;
	private byte[] field_149611_c;
	private static final String __OBFID = "CL_00001376";

	public S01PacketEncryptionRequest() {
	}

	public S01PacketEncryptionRequest(String p_i45268_1_, PublicKey p_i45268_2_, byte[] p_i45268_3_) {
		field_149612_a = p_i45268_1_;
		field_149610_b = p_i45268_2_;
		field_149611_c = p_i45268_3_;
	}

	@Override
	public void readPacketData(PacketBuffer p_148837_1_) throws IOException {
		field_149612_a = p_148837_1_.readStringFromBuffer(20);
		field_149610_b = CryptManager.decodePublicKey(readBlob(p_148837_1_));
		field_149611_c = readBlob(p_148837_1_);
	}

	@Override
	public void writePacketData(PacketBuffer p_148840_1_) throws IOException {
		p_148840_1_.writeStringToBuffer(field_149612_a);
		writeBlob(p_148840_1_, field_149610_b.getEncoded());
		writeBlob(p_148840_1_, field_149611_c);
	}

	public void processPacket(INetHandlerLoginClient p_148833_1_) {
		p_148833_1_.handleEncryptionRequest(this);
	}

	@Override
	public void processPacket(INetHandler p_148833_1_) {
		this.processPacket((INetHandlerLoginClient) p_148833_1_);
	}

	@SideOnly(Side.CLIENT)
	public String func_149609_c() {
		return field_149612_a;
	}

	@SideOnly(Side.CLIENT)
	public PublicKey func_149608_d() {
		return field_149610_b;
	}

	@SideOnly(Side.CLIENT)
	public byte[] func_149607_e() {
		return field_149611_c;
	}
}