package cpw.mods.fml.common.network.handshake;

import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

public class HandshakeInjector extends ChannelOutboundHandlerAdapter {

	private final NetworkDispatcher dispatcher;

	public HandshakeInjector(NetworkDispatcher networkDispatcher) {
		dispatcher = networkDispatcher;
	}

	@Override
	public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
		if (msg instanceof FMLProxyPacket) {
			dispatcher.sendProxy((FMLProxyPacket) msg);
		}
	}
}