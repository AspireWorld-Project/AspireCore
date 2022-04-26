package net.minecraft.network.play.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

import java.io.IOException;

public class C03PacketPlayer extends Packet {
	public double field_149479_a;
	public double field_149477_b;
	public double field_149478_c;
	public double field_149475_d;
	public float field_149476_e;
	public float field_149473_f;
	protected boolean field_149474_g;
	public boolean field_149480_h;
	public boolean field_149481_i;
	private static final String __OBFID = "CL_00001360";

	public C03PacketPlayer() {
	}

	@SideOnly(Side.CLIENT)
	public C03PacketPlayer(boolean p_i45256_1_) {
		field_149474_g = p_i45256_1_;
	}

	public void processPacket(INetHandlerPlayServer p_148833_1_) {
		p_148833_1_.processPlayer(this);
	}

	@Override
	public void readPacketData(PacketBuffer p_148837_1_) throws IOException {
		field_149474_g = p_148837_1_.readUnsignedByte() != 0;
	}

	@Override
	public void writePacketData(PacketBuffer p_148840_1_) throws IOException {
		p_148840_1_.writeByte(field_149474_g ? 1 : 0);
	}

	public double func_149464_c() {
		return field_149479_a;
	}

	public double func_149467_d() {
		return field_149477_b;
	}

	public double func_149472_e() {
		return field_149478_c;
	}

	public double func_149471_f() {
		return field_149475_d;
	}

	public float func_149462_g() {
		return field_149476_e;
	}

	public float func_149470_h() {
		return field_149473_f;
	}

	public boolean func_149465_i() {
		return field_149474_g;
	}

	public boolean func_149466_j() {
		return field_149480_h;
	}

	public boolean func_149463_k() {
		return field_149481_i;
	}

	public void func_149469_a(boolean p_149469_1_) {
		field_149480_h = p_149469_1_;
	}

	@Override
	public void processPacket(INetHandler p_148833_1_) {
		this.processPacket((INetHandlerPlayServer) p_148833_1_);
	}

	public static class C04PacketPlayerPosition extends C03PacketPlayer {
		private static final String __OBFID = "CL_00001361";

		public C04PacketPlayerPosition() {
			field_149480_h = true;
		}

		@SideOnly(Side.CLIENT)
		public C04PacketPlayerPosition(double p_i45253_1_, double p_i45253_3_, double p_i45253_5_, double p_i45253_7_,
				boolean p_i45253_9_) {
			field_149479_a = p_i45253_1_;
			field_149477_b = p_i45253_3_;
			field_149475_d = p_i45253_5_;
			field_149478_c = p_i45253_7_;
			field_149474_g = p_i45253_9_;
			field_149480_h = true;
		}

		@Override
		public void readPacketData(PacketBuffer p_148837_1_) throws IOException {
			field_149479_a = p_148837_1_.readDouble();
			field_149477_b = p_148837_1_.readDouble();
			field_149475_d = p_148837_1_.readDouble();
			field_149478_c = p_148837_1_.readDouble();
			super.readPacketData(p_148837_1_);
		}

		@Override
		public void writePacketData(PacketBuffer p_148840_1_) throws IOException {
			p_148840_1_.writeDouble(field_149479_a);
			p_148840_1_.writeDouble(field_149477_b);
			p_148840_1_.writeDouble(field_149475_d);
			p_148840_1_.writeDouble(field_149478_c);
			super.writePacketData(p_148840_1_);
		}

		@Override
		public void processPacket(INetHandler p_148833_1_) {
			super.processPacket((INetHandlerPlayServer) p_148833_1_);
		}
	}

	public static class C05PacketPlayerLook extends C03PacketPlayer {
		private static final String __OBFID = "CL_00001363";

		public C05PacketPlayerLook() {
			field_149481_i = true;
		}

		@SideOnly(Side.CLIENT)
		public C05PacketPlayerLook(float p_i45255_1_, float p_i45255_2_, boolean p_i45255_3_) {
			field_149476_e = p_i45255_1_;
			field_149473_f = p_i45255_2_;
			field_149474_g = p_i45255_3_;
			field_149481_i = true;
		}

		@Override
		public void readPacketData(PacketBuffer p_148837_1_) throws IOException {
			field_149476_e = p_148837_1_.readFloat();
			field_149473_f = p_148837_1_.readFloat();
			super.readPacketData(p_148837_1_);
		}

		@Override
		public void writePacketData(PacketBuffer p_148840_1_) throws IOException {
			p_148840_1_.writeFloat(field_149476_e);
			p_148840_1_.writeFloat(field_149473_f);
			super.writePacketData(p_148840_1_);
		}

		@Override
		public void processPacket(INetHandler p_148833_1_) {
			super.processPacket((INetHandlerPlayServer) p_148833_1_);
		}
	}

	public static class C06PacketPlayerPosLook extends C03PacketPlayer {
		private static final String __OBFID = "CL_00001362";

		public C06PacketPlayerPosLook() {
			field_149480_h = true;
			field_149481_i = true;
		}

		@SideOnly(Side.CLIENT)
		public C06PacketPlayerPosLook(double p_i45254_1_, double p_i45254_3_, double p_i45254_5_, double p_i45254_7_,
				float p_i45254_9_, float p_i45254_10_, boolean p_i45254_11_) {
			field_149479_a = p_i45254_1_;
			field_149477_b = p_i45254_3_;
			field_149475_d = p_i45254_5_;
			field_149478_c = p_i45254_7_;
			field_149476_e = p_i45254_9_;
			field_149473_f = p_i45254_10_;
			field_149474_g = p_i45254_11_;
			field_149481_i = true;
			field_149480_h = true;
		}

		@Override
		public void readPacketData(PacketBuffer p_148837_1_) throws IOException {
			field_149479_a = p_148837_1_.readDouble();
			field_149477_b = p_148837_1_.readDouble();
			field_149475_d = p_148837_1_.readDouble();
			field_149478_c = p_148837_1_.readDouble();
			field_149476_e = p_148837_1_.readFloat();
			field_149473_f = p_148837_1_.readFloat();
			super.readPacketData(p_148837_1_);
		}

		@Override
		public void writePacketData(PacketBuffer p_148840_1_) throws IOException {
			p_148840_1_.writeDouble(field_149479_a);
			p_148840_1_.writeDouble(field_149477_b);
			p_148840_1_.writeDouble(field_149475_d);
			p_148840_1_.writeDouble(field_149478_c);
			p_148840_1_.writeFloat(field_149476_e);
			p_148840_1_.writeFloat(field_149473_f);
			super.writePacketData(p_148840_1_);
		}

		@Override
		public void processPacket(INetHandler p_148833_1_) {
			super.processPacket((INetHandlerPlayServer) p_148833_1_);
		}
	}
}