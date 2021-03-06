package net.minecraft.network.play.server;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.DataWatcher;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.MathHelper;

import java.io.IOException;
import java.util.List;

public class S0FPacketSpawnMob extends Packet {
	private int field_149042_a;
	private int field_149040_b;
	private int field_149041_c;
	private int field_149038_d;
	private int field_149039_e;
	private int field_149036_f;
	private int field_149037_g;
	private int field_149047_h;
	private byte field_149048_i;
	private byte field_149045_j;
	private byte field_149046_k;
	private DataWatcher field_149043_l;
	private List field_149044_m;
	private static final String __OBFID = "CL_00001279";

	public S0FPacketSpawnMob() {
	}

	public S0FPacketSpawnMob(EntityLivingBase p_i45192_1_) {
		field_149042_a = p_i45192_1_.getEntityId();
		field_149040_b = (byte) EntityList.getEntityID(p_i45192_1_);
		field_149041_c = p_i45192_1_.myEntitySize.multiplyBy32AndRound(p_i45192_1_.posX);
		field_149038_d = MathHelper.floor_double(p_i45192_1_.posY * 32.0D);
		field_149039_e = p_i45192_1_.myEntitySize.multiplyBy32AndRound(p_i45192_1_.posZ);
		field_149048_i = (byte) (int) (p_i45192_1_.rotationYaw * 256.0F / 360.0F);
		field_149045_j = (byte) (int) (p_i45192_1_.rotationPitch * 256.0F / 360.0F);
		field_149046_k = (byte) (int) (p_i45192_1_.rotationYawHead * 256.0F / 360.0F);
		double d0 = 3.9D;
		double d1 = p_i45192_1_.motionX;
		double d2 = p_i45192_1_.motionY;
		double d3 = p_i45192_1_.motionZ;

		if (d1 < -d0) {
			d1 = -d0;
		}

		if (d2 < -d0) {
			d2 = -d0;
		}

		if (d3 < -d0) {
			d3 = -d0;
		}

		if (d1 > d0) {
			d1 = d0;
		}

		if (d2 > d0) {
			d2 = d0;
		}

		if (d3 > d0) {
			d3 = d0;
		}

		field_149036_f = (int) (d1 * 8000.0D);
		field_149037_g = (int) (d2 * 8000.0D);
		field_149047_h = (int) (d3 * 8000.0D);
		field_149043_l = p_i45192_1_.getDataWatcher();
	}

	@Override
	public void readPacketData(PacketBuffer p_148837_1_) throws IOException {
		field_149042_a = p_148837_1_.readVarIntFromBuffer();
		field_149040_b = p_148837_1_.readByte() & 255;
		field_149041_c = p_148837_1_.readInt();
		field_149038_d = p_148837_1_.readInt();
		field_149039_e = p_148837_1_.readInt();
		field_149048_i = p_148837_1_.readByte();
		field_149045_j = p_148837_1_.readByte();
		field_149046_k = p_148837_1_.readByte();
		field_149036_f = p_148837_1_.readShort();
		field_149037_g = p_148837_1_.readShort();
		field_149047_h = p_148837_1_.readShort();
		field_149044_m = DataWatcher.readWatchedListFromPacketBuffer(p_148837_1_);
	}

	@Override
	public void writePacketData(PacketBuffer p_148840_1_) throws IOException {
		p_148840_1_.writeVarIntToBuffer(field_149042_a);
		p_148840_1_.writeByte(field_149040_b & 255);
		p_148840_1_.writeInt(field_149041_c);
		p_148840_1_.writeInt(field_149038_d);
		p_148840_1_.writeInt(field_149039_e);
		p_148840_1_.writeByte(field_149048_i);
		p_148840_1_.writeByte(field_149045_j);
		p_148840_1_.writeByte(field_149046_k);
		p_148840_1_.writeShort(field_149036_f);
		p_148840_1_.writeShort(field_149037_g);
		p_148840_1_.writeShort(field_149047_h);
		field_149043_l.func_151509_a(p_148840_1_);
	}

	public void processPacket(INetHandlerPlayClient p_148833_1_) {
		p_148833_1_.handleSpawnMob(this);
	}

	@SideOnly(Side.CLIENT)
	public List func_149027_c() {
		if (field_149044_m == null) {
			field_149044_m = field_149043_l.getAllWatched();
		}

		return field_149044_m;
	}

	@Override
	public String serialize() {
		return String.format("id=%d, type=%d, x=%.2f, y=%.2f, z=%.2f, xd=%.2f, yd=%.2f, zd=%.2f",
				Integer.valueOf(field_149042_a), Integer.valueOf(field_149040_b),
				Float.valueOf(field_149041_c / 32.0F), Float.valueOf(field_149038_d / 32.0F),
				Float.valueOf(field_149039_e / 32.0F), Float.valueOf(field_149036_f / 8000.0F),
				Float.valueOf(field_149037_g / 8000.0F), Float.valueOf(field_149047_h / 8000.0F));
	}

	@SideOnly(Side.CLIENT)
	public int func_149024_d() {
		return field_149042_a;
	}

	@SideOnly(Side.CLIENT)
	public int func_149025_e() {
		return field_149040_b;
	}

	@SideOnly(Side.CLIENT)
	public int func_149023_f() {
		return field_149041_c;
	}

	@SideOnly(Side.CLIENT)
	public int func_149034_g() {
		return field_149038_d;
	}

	@SideOnly(Side.CLIENT)
	public int func_149029_h() {
		return field_149039_e;
	}

	@SideOnly(Side.CLIENT)
	public int func_149026_i() {
		return field_149036_f;
	}

	@SideOnly(Side.CLIENT)
	public int func_149033_j() {
		return field_149037_g;
	}

	@SideOnly(Side.CLIENT)
	public int func_149031_k() {
		return field_149047_h;
	}

	@SideOnly(Side.CLIENT)
	public byte func_149028_l() {
		return field_149048_i;
	}

	@SideOnly(Side.CLIENT)
	public byte func_149030_m() {
		return field_149045_j;
	}

	@SideOnly(Side.CLIENT)
	public byte func_149032_n() {
		return field_149046_k;
	}

	@Override
	public void processPacket(INetHandler p_148833_1_) {
		this.processPacket((INetHandlerPlayClient) p_148833_1_);
	}
}