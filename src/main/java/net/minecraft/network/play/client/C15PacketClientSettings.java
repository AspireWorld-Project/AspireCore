package net.minecraft.network.play.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.world.EnumDifficulty;

import java.io.IOException;

public class C15PacketClientSettings extends Packet {
	private String field_149530_a;
	private int field_149528_b;
	private EntityPlayer.EnumChatVisibility field_149529_c;
	private boolean field_149526_d;
	private EnumDifficulty field_149527_e;
	private boolean field_149525_f;
	private static final String __OBFID = "CL_00001350";

	public C15PacketClientSettings() {
	}

	@SideOnly(Side.CLIENT)
	public C15PacketClientSettings(String p_i45243_1_, int p_i45243_2_, EntityPlayer.EnumChatVisibility p_i45243_3_,
			boolean p_i45243_4_, EnumDifficulty p_i45243_5_, boolean p_i45243_6_) {
		field_149530_a = p_i45243_1_;
		field_149528_b = p_i45243_2_;
		field_149529_c = p_i45243_3_;
		field_149526_d = p_i45243_4_;
		field_149527_e = p_i45243_5_;
		field_149525_f = p_i45243_6_;
	}

	@Override
	public void readPacketData(PacketBuffer p_148837_1_) throws IOException {
		field_149530_a = p_148837_1_.readStringFromBuffer(7);
		field_149528_b = p_148837_1_.readByte();
		field_149529_c = EntityPlayer.EnumChatVisibility.getEnumChatVisibility(p_148837_1_.readByte());
		field_149526_d = p_148837_1_.readBoolean();
		field_149527_e = EnumDifficulty.getDifficultyEnum(p_148837_1_.readByte());
		field_149525_f = p_148837_1_.readBoolean();
	}

	@Override
	public void writePacketData(PacketBuffer p_148840_1_) throws IOException {
		p_148840_1_.writeStringToBuffer(field_149530_a);
		p_148840_1_.writeByte(field_149528_b);
		p_148840_1_.writeByte(field_149529_c.getChatVisibility());
		p_148840_1_.writeBoolean(field_149526_d);
		p_148840_1_.writeByte(field_149527_e.getDifficultyId());
		p_148840_1_.writeBoolean(field_149525_f);
	}

	public void processPacket(INetHandlerPlayServer p_148833_1_) {
		p_148833_1_.processClientSettings(this);
	}

	public String func_149524_c() {
		return field_149530_a;
	}

	public int func_149521_d() {
		return field_149528_b;
	}

	public EntityPlayer.EnumChatVisibility func_149523_e() {
		return field_149529_c;
	}

	public boolean func_149520_f() {
		return field_149526_d;
	}

	public EnumDifficulty func_149518_g() {
		return field_149527_e;
	}

	public boolean func_149519_h() {
		return field_149525_f;
	}

	@Override
	public String serialize() {
		return String.format("lang='%s', view=%d, chat=%s, col=%b, difficulty=%s, cape=%b",
				field_149530_a, Integer.valueOf(field_149528_b), field_149529_c,
				Boolean.valueOf(field_149526_d), field_149527_e, Boolean.valueOf(field_149525_f));
	}

	@Override
	public void processPacket(INetHandler p_148833_1_) {
		this.processPacket((INetHandlerPlayServer) p_148833_1_);
	}
}