package net.minecraft.network;

import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.ShortBufferException;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

public class NettyEncryptingDecoder extends MessageToMessageDecoder {
	private final NettyEncryptionTranslator field_150509_a;
	private static final String __OBFID = "CL_00001238";

	public NettyEncryptingDecoder(Cipher p_i45141_1_) {
		field_150509_a = new NettyEncryptionTranslator(p_i45141_1_);
	}

	protected void decode(ChannelHandlerContext p_decode_1_, ByteBuf p_decode_2_, List p_decode_3_)
			throws ShortBufferException {
		p_decode_3_.add(field_150509_a.func_150503_a(p_decode_1_, p_decode_2_));
	}

	@Override
	protected void decode(ChannelHandlerContext p_decode_1_, Object p_decode_2_, List p_decode_3_)
			throws ShortBufferException {
		this.decode(p_decode_1_, (ByteBuf) p_decode_2_, p_decode_3_);
	}
}