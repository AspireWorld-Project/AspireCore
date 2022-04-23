package net.minecraft.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import javax.crypto.Cipher;
import javax.crypto.ShortBufferException;

public class NettyEncryptionTranslator {
	private final Cipher field_150507_a;
	private byte[] field_150505_b = new byte[0];
	private byte[] field_150506_c = new byte[0];
	private static final String __OBFID = "CL_00001237";

	protected NettyEncryptionTranslator(Cipher p_i45140_1_) {
		field_150507_a = p_i45140_1_;
	}

	private byte[] func_150502_a(ByteBuf p_150502_1_) {
		int i = p_150502_1_.readableBytes();

		if (field_150505_b.length < i) {
			field_150505_b = new byte[i];
		}

		p_150502_1_.readBytes(field_150505_b, 0, i);
		return field_150505_b;
	}

	protected ByteBuf func_150503_a(ChannelHandlerContext p_150503_1_, ByteBuf p_150503_2_)
			throws ShortBufferException {
		int i = p_150503_2_.readableBytes();
		byte[] abyte = func_150502_a(p_150503_2_);
		ByteBuf bytebuf1 = p_150503_1_.alloc().heapBuffer(field_150507_a.getOutputSize(i));
		bytebuf1.writerIndex(field_150507_a.update(abyte, 0, i, bytebuf1.array(), bytebuf1.arrayOffset()));
		return bytebuf1;
	}

	protected void func_150504_a(ByteBuf p_150504_1_, ByteBuf p_150504_2_) throws ShortBufferException {
		int i = p_150504_1_.readableBytes();
		byte[] abyte = func_150502_a(p_150504_1_);
		int j = field_150507_a.getOutputSize(i);

		if (field_150506_c.length < j) {
			field_150506_c = new byte[j];
		}

		p_150504_2_.writeBytes(field_150506_c, 0, field_150507_a.update(abyte, 0, i, field_150506_c));
	}
}