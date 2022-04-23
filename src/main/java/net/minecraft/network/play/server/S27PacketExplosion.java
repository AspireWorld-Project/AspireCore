package net.minecraft.network.play.server;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.Vec3;
import net.minecraft.world.ChunkPosition;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class S27PacketExplosion extends Packet {
	private double field_149158_a;
	private double field_149156_b;
	private double field_149157_c;
	private float field_149154_d;
	private List field_149155_e;
	private float field_149152_f;
	private float field_149153_g;
	private float field_149159_h;
	private static final String __OBFID = "CL_00001300";

	public S27PacketExplosion() {
	}

	public S27PacketExplosion(double p_i45193_1_, double p_i45193_3_, double p_i45193_5_, float p_i45193_7_,
			List p_i45193_8_, Vec3 p_i45193_9_) {
		field_149158_a = p_i45193_1_;
		field_149156_b = p_i45193_3_;
		field_149157_c = p_i45193_5_;
		field_149154_d = p_i45193_7_;
		field_149155_e = new ArrayList(p_i45193_8_);

		if (p_i45193_9_ != null) {
			field_149152_f = (float) p_i45193_9_.xCoord;
			field_149153_g = (float) p_i45193_9_.yCoord;
			field_149159_h = (float) p_i45193_9_.zCoord;
		}
	}

	@Override
	public void readPacketData(PacketBuffer p_148837_1_) throws IOException {
		field_149158_a = p_148837_1_.readFloat();
		field_149156_b = p_148837_1_.readFloat();
		field_149157_c = p_148837_1_.readFloat();
		field_149154_d = p_148837_1_.readFloat();
		int i = p_148837_1_.readInt();
		field_149155_e = new ArrayList(i);
		int j = (int) field_149158_a;
		int k = (int) field_149156_b;
		int l = (int) field_149157_c;

		for (int i1 = 0; i1 < i; ++i1) {
			int j1 = p_148837_1_.readByte() + j;
			int k1 = p_148837_1_.readByte() + k;
			int l1 = p_148837_1_.readByte() + l;
			field_149155_e.add(new ChunkPosition(j1, k1, l1));
		}

		field_149152_f = p_148837_1_.readFloat();
		field_149153_g = p_148837_1_.readFloat();
		field_149159_h = p_148837_1_.readFloat();
	}

	@Override
	public void writePacketData(PacketBuffer p_148840_1_) throws IOException {
		p_148840_1_.writeFloat((float) field_149158_a);
		p_148840_1_.writeFloat((float) field_149156_b);
		p_148840_1_.writeFloat((float) field_149157_c);
		p_148840_1_.writeFloat(field_149154_d);
		p_148840_1_.writeInt(field_149155_e.size());
		int i = (int) field_149158_a;
		int j = (int) field_149156_b;
		int k = (int) field_149157_c;
		Iterator iterator = field_149155_e.iterator();

		while (iterator.hasNext()) {
			ChunkPosition chunkposition = (ChunkPosition) iterator.next();
			int l = chunkposition.chunkPosX - i;
			int i1 = chunkposition.chunkPosY - j;
			int j1 = chunkposition.chunkPosZ - k;
			p_148840_1_.writeByte(l);
			p_148840_1_.writeByte(i1);
			p_148840_1_.writeByte(j1);
		}

		p_148840_1_.writeFloat(field_149152_f);
		p_148840_1_.writeFloat(field_149153_g);
		p_148840_1_.writeFloat(field_149159_h);
	}

	public void processPacket(INetHandlerPlayClient p_148833_1_) {
		p_148833_1_.handleExplosion(this);
	}

	@Override
	public void processPacket(INetHandler p_148833_1_) {
		this.processPacket((INetHandlerPlayClient) p_148833_1_);
	}

	@SideOnly(Side.CLIENT)
	public float func_149149_c() {
		return field_149152_f;
	}

	@SideOnly(Side.CLIENT)
	public float func_149144_d() {
		return field_149153_g;
	}

	@SideOnly(Side.CLIENT)
	public float func_149147_e() {
		return field_149159_h;
	}

	@SideOnly(Side.CLIENT)
	public double func_149148_f() {
		return field_149158_a;
	}

	@SideOnly(Side.CLIENT)
	public double func_149143_g() {
		return field_149156_b;
	}

	@SideOnly(Side.CLIENT)
	public double func_149145_h() {
		return field_149157_c;
	}

	@SideOnly(Side.CLIENT)
	public float func_149146_i() {
		return field_149154_d;
	}

	@SideOnly(Side.CLIENT)
	public List func_149150_j() {
		return field_149155_e;
	}
}