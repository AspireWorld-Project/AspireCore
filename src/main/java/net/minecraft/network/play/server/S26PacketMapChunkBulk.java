package net.minecraft.network.play.server;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.world.chunk.Chunk;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class S26PacketMapChunkBulk extends Packet {
	private int[] field_149266_a;
	private int[] field_149264_b;
	private int[] field_149265_c;
	private int[] field_149262_d;
	private byte[] field_149263_e;
	private byte[][] field_149260_f;
	private int field_149261_g;
	private boolean field_149267_h;
	private static byte[] field_149268_i = new byte[0];
	private static final String __OBFID = "CL_00001306";
	private int maxLen = 0;
	private Semaphore deflateGate;

	public S26PacketMapChunkBulk() {
	}

	public S26PacketMapChunkBulk(List p_i45197_1_) {
		int i = p_i45197_1_.size();
		field_149266_a = new int[i];
		field_149264_b = new int[i];
		field_149265_c = new int[i];
		field_149262_d = new int[i];
		field_149260_f = new byte[i][];
		field_149267_h = !p_i45197_1_.isEmpty() && !((Chunk) p_i45197_1_.get(0)).worldObj.provider.hasNoSky;
		int j = 0;

		for (int k = 0; k < i; ++k) {
			Chunk chunk = (Chunk) p_i45197_1_.get(k);
			S21PacketChunkData.Extracted extracted = S21PacketChunkData.func_149269_a(chunk, true, 65535);
			j += extracted.field_150282_a.length;
			field_149266_a[k] = chunk.xPosition;
			field_149264_b[k] = chunk.zPosition;
			field_149265_c[k] = extracted.field_150280_b;
			field_149262_d[k] = extracted.field_150281_c;
			field_149260_f[k] = extracted.field_150282_a;
		}
		deflateGate = new Semaphore(1);
		maxLen = j;
	}

	private void deflate() {
		byte[] data = new byte[maxLen];
		int offset = 0;
		for (int x = 0; x < field_149260_f.length; x++) {
			System.arraycopy(field_149260_f[x], 0, data, offset, field_149260_f[x].length);
			offset += field_149260_f[x].length;
		}
		Deflater deflater = new Deflater(-1);

		try {
			deflater.setInput(data, 0, data.length);
			deflater.finish();
			byte[] deflated = new byte[data.length];
			field_149261_g = deflater.deflate(deflated);
			field_149263_e = deflated;
		} finally {
			deflater.end();
		}
	}

	public static int func_149258_c() {
		return 5;
	}

	@Override
	public void readPacketData(PacketBuffer p_148837_1_) throws IOException {
		short short1 = p_148837_1_.readShort();
		field_149261_g = p_148837_1_.readInt();
		field_149267_h = p_148837_1_.readBoolean();
		field_149266_a = new int[short1];
		field_149264_b = new int[short1];
		field_149265_c = new int[short1];
		field_149262_d = new int[short1];
		field_149260_f = new byte[short1][];

		if (field_149268_i.length < field_149261_g) {
			field_149268_i = new byte[field_149261_g];
		}

		p_148837_1_.readBytes(field_149268_i, 0, field_149261_g);
		byte[] abyte = new byte[S21PacketChunkData.func_149275_c() * short1];
		Inflater inflater = new Inflater();
		inflater.setInput(field_149268_i, 0, field_149261_g);

		try {
			inflater.inflate(abyte);
		} catch (DataFormatException dataformatexception) {
			throw new IOException("Bad compressed data format");
		} finally {
			inflater.end();
		}

		int i = 0;

		for (int j = 0; j < short1; ++j) {
			field_149266_a[j] = p_148837_1_.readInt();
			field_149264_b[j] = p_148837_1_.readInt();
			field_149265_c[j] = p_148837_1_.readShort();
			field_149262_d[j] = p_148837_1_.readShort();
			int k = 0;
			int l = 0;
			int i1;

			for (i1 = 0; i1 < 16; ++i1) {
				k += field_149265_c[j] >> i1 & 1;
				l += field_149262_d[j] >> i1 & 1;
			}

			i1 = 2048 * 4 * k + 256;
			i1 += 2048 * l;

			if (field_149267_h) {
				i1 += 2048 * k;
			}

			field_149260_f[j] = new byte[i1];
			System.arraycopy(abyte, i, field_149260_f[j], 0, i1);
			i += i1;
		}
	}

	@Override
	public void writePacketData(PacketBuffer p_148840_1_) throws IOException {
		if (field_149263_e == null) {
			deflateGate.acquireUninterruptibly();
			if (field_149263_e == null) {
				deflate();
			}
			deflateGate.release();
		}

		p_148840_1_.writeShort(field_149266_a.length);
		p_148840_1_.writeInt(field_149261_g);
		p_148840_1_.writeBoolean(field_149267_h);
		p_148840_1_.writeBytes(field_149263_e, 0, field_149261_g);

		for (int i = 0; i < field_149266_a.length; ++i) {
			p_148840_1_.writeInt(field_149266_a[i]);
			p_148840_1_.writeInt(field_149264_b[i]);
			p_148840_1_.writeShort((short) (field_149265_c[i] & 65535));
			p_148840_1_.writeShort((short) (field_149262_d[i] & 65535));
		}
	}

	public void processPacket(INetHandlerPlayClient p_148833_1_) {
		p_148833_1_.handleMapChunkBulk(this);
	}

	@SideOnly(Side.CLIENT)
	public int func_149255_a(int p_149255_1_) {
		return field_149266_a[p_149255_1_];
	}

	@SideOnly(Side.CLIENT)
	public int func_149253_b(int p_149253_1_) {
		return field_149264_b[p_149253_1_];
	}

	@Override
	public String serialize() {
		StringBuilder stringbuilder = new StringBuilder();

		for (int i = 0; i < field_149266_a.length; ++i) {
			if (i > 0) {
				stringbuilder.append(", ");
			}

			stringbuilder.append(String.format("{x=%d, z=%d, sections=%d, adds=%d, data=%d}",
					Integer.valueOf(field_149266_a[i]), Integer.valueOf(field_149264_b[i]),
					Integer.valueOf(field_149265_c[i]), Integer.valueOf(field_149262_d[i]),
					Integer.valueOf(field_149260_f[i].length)));
		}

		return String.format("size=%d, chunks=%d[%s]", Integer.valueOf(field_149261_g),
				Integer.valueOf(field_149266_a.length), stringbuilder);
	}

	@SideOnly(Side.CLIENT)
	public int func_149254_d() {
		return field_149266_a.length;
	}

	@SideOnly(Side.CLIENT)
	public byte[] func_149256_c(int p_149256_1_) {
		return field_149260_f[p_149256_1_];
	}

	@SideOnly(Side.CLIENT)
	public int[] func_149252_e() {
		return field_149265_c;
	}

	@SideOnly(Side.CLIENT)
	public int[] func_149257_f() {
		return field_149262_d;
	}

	@Override
	public void processPacket(INetHandler p_148833_1_) {
		this.processPacket((INetHandlerPlayClient) p_148833_1_);
	}

	public static S26PacketMapChunkBulk makeDeflated(List chunks) {
		S26PacketMapChunkBulk pkt = new S26PacketMapChunkBulk(chunks);
		pkt.deflate();
		return pkt;
	}
}
