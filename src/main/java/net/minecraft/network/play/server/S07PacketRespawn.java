package net.minecraft.network.play.server;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldType;

import java.io.IOException;

public class S07PacketRespawn extends Packet {
	private int field_149088_a;
	private EnumDifficulty field_149086_b;
	private WorldSettings.GameType field_149087_c;
	private WorldType field_149085_d;
	private static final String __OBFID = "CL_00001322";

	public S07PacketRespawn() {
	}

	public S07PacketRespawn(int p_i45213_1_, EnumDifficulty p_i45213_2_, WorldType p_i45213_3_,
			WorldSettings.GameType p_i45213_4_) {
		field_149088_a = p_i45213_1_;
		field_149086_b = p_i45213_2_;
		field_149087_c = p_i45213_4_;
		field_149085_d = p_i45213_3_;
	}

	public void processPacket(INetHandlerPlayClient p_148833_1_) {
		p_148833_1_.handleRespawn(this);
	}

	@Override
	public void readPacketData(PacketBuffer p_148837_1_) throws IOException {
		field_149088_a = p_148837_1_.readInt();
		field_149086_b = EnumDifficulty.getDifficultyEnum(p_148837_1_.readUnsignedByte());
		field_149087_c = WorldSettings.GameType.getByID(p_148837_1_.readUnsignedByte());
		field_149085_d = WorldType.parseWorldType(p_148837_1_.readStringFromBuffer(16));

		if (field_149085_d == null) {
			field_149085_d = WorldType.DEFAULT;
		}
	}

	@Override
	public void writePacketData(PacketBuffer p_148840_1_) throws IOException {
		p_148840_1_.writeInt(field_149088_a);
		p_148840_1_.writeByte(field_149086_b.getDifficultyId());
		p_148840_1_.writeByte(field_149087_c.getID());
		p_148840_1_.writeStringToBuffer(field_149085_d.getWorldTypeName());
	}

	@SideOnly(Side.CLIENT)
	public int func_149082_c() {
		return field_149088_a;
	}

	@SideOnly(Side.CLIENT)
	public EnumDifficulty func_149081_d() {
		return field_149086_b;
	}

	@SideOnly(Side.CLIENT)
	public WorldSettings.GameType func_149083_e() {
		return field_149087_c;
	}

	@SideOnly(Side.CLIENT)
	public WorldType func_149080_f() {
		return field_149085_d;
	}

	@Override
	public void processPacket(INetHandler p_148833_1_) {
		this.processPacket((INetHandlerPlayClient) p_148833_1_);
	}
}