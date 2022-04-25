package net.minecraft.network;

import com.google.common.collect.BiMap;
import com.google.common.collect.Queues;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.mojang.authlib.properties.Property;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.local.LocalChannel;
import io.netty.channel.local.LocalServerChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.TimeoutException;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.client.C0EPacketClickWindow;
import net.minecraft.util.*;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.ultramine.server.UltramineServerConfig;
import org.ultramine.server.event.WorldUpdateObjectType;

import javax.crypto.SecretKey;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class NetworkManager extends SimpleChannelInboundHandler {
	private static final Logger logger = LogManager.getLogger();
	public static final Marker logMarkerNetwork = MarkerManager.getMarker("NETWORK");
	public static final Marker logMarkerPackets = MarkerManager.getMarker("NETWORK_PACKETS", logMarkerNetwork);
	public static final Marker field_152461_c = MarkerManager.getMarker("NETWORK_STAT", logMarkerNetwork);
	public static final AttributeKey<EnumConnectionState> attrKeyConnectionState = new AttributeKey<>("protocol");
	public static final AttributeKey<BiMap<Integer, Class<Packet>>> attrKeyReceivable = new AttributeKey<>("receivable_packets");
	public static final AttributeKey<BiMap<Integer, Class<Packet>>> attrKeySendable = new AttributeKey<>("sendable_packets");
	public static final NioEventLoopGroup eventLoops = new NioEventLoopGroup(4,
			new ThreadFactoryBuilder().setNameFormat("Netty Client IO #%d").setDaemon(true).build());
	public static final NetworkStatistics field_152462_h = new NetworkStatistics();
	private final boolean isClientSide;
	private final Queue<Packet> receivedPacketsQueue = Queues.newConcurrentLinkedQueue();
	private final Queue<InboundHandlerTuplePacketListener> outboundPacketsQueue = Queues.newConcurrentLinkedQueue();
	private ChannelFuture channelFuture;
	private SocketAddress socketAddress;
	private INetHandler netHandler;
	private EnumConnectionState connectionState;
	private IChatComponent terminationReason;
	// Spigot start
	public Property[] spoofedProfile;
	public UUID spoofedUUID;
	// Spigot end
	private boolean field_152463_r;

	public NetworkManager(boolean p_i45147_1_) {
		isClientSide = p_i45147_1_;
	}

	@Override
	public void channelActive(ChannelHandlerContext p_channelActive_1_) throws Exception {
		super.channelActive(p_channelActive_1_);
		channelFuture = p_channelActive_1_.newSucceededFuture();
		socketAddress = channelFuture.channel().remoteAddress();
		setConnectionState(EnumConnectionState.HANDSHAKING);
	}

	public void setConnectionState(EnumConnectionState p_150723_1_) {
		connectionState = channelFuture.channel().attr(attrKeyConnectionState).getAndSet(p_150723_1_);
		channelFuture.channel().attr(attrKeyReceivable).set(p_150723_1_.func_150757_a(isClientSide));
		channelFuture.channel().attr(attrKeySendable).set(p_150723_1_.func_150754_b(isClientSide));
		channelFuture.channel().config().setAutoRead(true);
		logger.debug("Enabled auto read");
	}

	@Override
	public void channelInactive(ChannelHandlerContext p_channelInactive_1_) {
		closeChannel(new ChatComponentTranslation("disconnect.endOfStream"));
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext p_exceptionCaught_1_, Throwable p_exceptionCaught_2_) {
		ChatComponentTranslation chatcomponenttranslation;

		if (p_exceptionCaught_2_ instanceof TimeoutException) {
			chatcomponenttranslation = new ChatComponentTranslation("disconnect.timeout");
		} else {
			chatcomponenttranslation = new ChatComponentTranslation("disconnect.genericReason",
					"Internal Exception: " + p_exceptionCaught_2_);
		}

		closeChannel(chatcomponenttranslation);
	}

	protected void channelRead0(ChannelHandlerContext p_channelRead0_1_, Packet p_channelRead0_2_) {
		if (channelFuture.channel().isOpen()) {
			if (p_channelRead0_2_.hasPriority()) {
				p_channelRead0_2_.processPacket(netHandler);
				p_channelRead0_1_.fireChannelRead(p_channelRead0_2_);
			} else {
				receivedPacketsQueue.add(p_channelRead0_2_);
			}
		}
	}

	public void setNetHandler(INetHandler p_150719_1_) {
		Validate.notNull(p_150719_1_, "packetListener");
		logger.debug("Set listener of {} to {}", this, p_150719_1_);
		netHandler = p_150719_1_;
	}

	public void scheduleOutboundPacket(Packet p_150725_1_, GenericFutureListener... p_150725_2_) {
		if (channelFuture.channel() != null && channelFuture.channel().isOpen()) {
			flushOutboundQueue();
			dispatchPacket(p_150725_1_, p_150725_2_);
		} else {
			outboundPacketsQueue.add(new NetworkManager.InboundHandlerTuplePacketListener(p_150725_1_, p_150725_2_));
		}
	}

	private void dispatchPacket(final Packet p_150732_1_, final GenericFutureListener<Future<Void>>[] p_150732_2_) {
		final EnumConnectionState enumconnectionstate = EnumConnectionState.func_150752_a(p_150732_1_);
		final EnumConnectionState enumconnectionstate1 = channelFuture.channel()
				.attr(attrKeyConnectionState)
				.get();

		if (enumconnectionstate1 != enumconnectionstate && !(p_150732_1_ instanceof FMLProxyPacket)) {
			logger.debug("Disabled auto read");
			channelFuture.channel().config().setAutoRead(false);
		}

		if (channelFuture.channel().eventLoop().inEventLoop()) {
			if (enumconnectionstate != enumconnectionstate1 && !(p_150732_1_ instanceof FMLProxyPacket)) {
				setConnectionState(enumconnectionstate);
			}

			channelFuture.channel().writeAndFlush(p_150732_1_).addListeners(p_150732_2_)
					.addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
		} else {
			channelFuture.channel().eventLoop().execute(() -> {
				if (enumconnectionstate != enumconnectionstate1 && !(p_150732_1_ instanceof FMLProxyPacket)) {
					NetworkManager.this.setConnectionState(enumconnectionstate);
				}

				channelFuture.channel().writeAndFlush(p_150732_1_).addListeners(p_150732_2_)
						.addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
			});

		}
	}

	private void flushOutboundQueue() {
		if (channelFuture.channel() != null && channelFuture.channel().isOpen()) {
			while (!outboundPacketsQueue.isEmpty()) {
				NetworkManager.InboundHandlerTuplePacketListener inboundhandlertuplepacketlistener =
						outboundPacketsQueue.poll();
				dispatchPacket(inboundhandlertuplepacketlistener.field_150774_a,
						inboundhandlertuplepacketlistener.field_150773_b);
			}
		}
	}

	public void processReceivedPackets() {
		flushOutboundQueue();
		EnumConnectionState enumconnectionstate = channelFuture.channel().attr(attrKeyConnectionState).get();

		if (connectionState != enumconnectionstate) {
			if (connectionState != null) {
				netHandler.onConnectionStateTransition(connectionState, enumconnectionstate);
			}

			connectionState = enumconnectionstate;
		}

		if (netHandler != null) {
			EntityPlayerMP player = netHandler instanceof NetHandlerPlayServer
					? ((NetHandlerPlayServer) netHandler).playerEntity
					: null;
			net.minecraft.profiler.Profiler profiler = null;
			if (player != null) {
				player.worldObj.getEventProxy().pushState(WorldUpdateObjectType.PLAYER);
				player.worldObj.getEventProxy().startEntity(player);
				profiler = player.worldObj.theProfiler;
			}

			for (int i = 1000; !receivedPacketsQueue.isEmpty() && i >= 0; --i) {
				Packet packet = receivedPacketsQueue.poll();
				if (profiler != null) {
					profiler.startSection(packet.getClass().getSimpleName());
				}
				long startT = System.nanoTime();
				packet.processPacket(netHandler);
				long elapsed = System.nanoTime() - startT;
				UltramineServerConfig config = new UltramineServerConfig();
				if (elapsed > 20000000 && config.vanilla.EnableLoggingPackets) {
					logger.warn("Possible lag source on processiong packet {} from {} {}ms",
							packet.getClass().getSimpleName(), player, elapsed / 1000000);
					if (packet instanceof C0EPacketClickWindow && player != null) {
						logger.warn("    Container: " + player.openContainer.getClass().getName());
					} else if (packet instanceof FMLProxyPacket) {
						logger.warn("    Channel: " + ((FMLProxyPacket) packet).channel());
					}
				}
				if (profiler != null) {
					profiler.endSection();
				}
			}

			if (player != null) {
				player.worldObj.getEventProxy().popState();
			}

			netHandler.onNetworkTick();
		}

		channelFuture.channel().flush();
	}

	public SocketAddress getSocketAddress() {
		return socketAddress;
	}

	public void closeChannel(IChatComponent p_150718_1_) {
		if (channelFuture.channel().isOpen()) {
			channelFuture.channel().close();
			terminationReason = p_150718_1_;
		}
	}

	// Spigot Start
	public SocketAddress getRawAddress()
	{
		return this.channelFuture.channel().remoteAddress();
	}
	// Spigot End

	public boolean isLocalChannel() {
		return channelFuture instanceof LocalChannel || channelFuture instanceof LocalServerChannel;
	}

	@SideOnly(Side.CLIENT)
	public static NetworkManager provideLanClient(InetAddress p_150726_0_, int p_150726_1_) {
		final NetworkManager networkmanager = new NetworkManager(true);
		new Bootstrap().group(eventLoops).handler(new ChannelInitializer<NioSocketChannel>() {

			@Override
			protected void initChannel(NioSocketChannel p_initChannel_1_) {
				try {
					p_initChannel_1_.config().setOption(ChannelOption.IP_TOS, 24);
				} catch (ChannelException ignored) {
				}

				try {
					p_initChannel_1_.config().setOption(ChannelOption.TCP_NODELAY, Boolean.FALSE);
				} catch (ChannelException ignored) {
				}

				p_initChannel_1_.pipeline().addLast("timeout", new ReadTimeoutHandler(20))
						.addLast("splitter", new MessageDeserializer2())
						.addLast("decoder", new MessageDeserializer(NetworkManager.field_152462_h))
						.addLast("prepender", new MessageSerializer2())
						.addLast("encoder", new MessageSerializer(NetworkManager.field_152462_h))
						.addLast("packet_handler", networkmanager);
			}
		}).channel(NioSocketChannel.class).connect(p_150726_0_, p_150726_1_).syncUninterruptibly();
		return networkmanager;
	}

	@SideOnly(Side.CLIENT)
	public static NetworkManager provideLocalClient(SocketAddress p_150722_0_) {
		final NetworkManager networkmanager = new NetworkManager(true);
		new Bootstrap().group(eventLoops).handler(new ChannelInitializer<NioSocketChannel>() {

			@Override
			protected void initChannel(NioSocketChannel p_initChannel_1_) {
				p_initChannel_1_.pipeline().addLast("packet_handler", networkmanager);
			}
		}).channel(LocalChannel.class).connect(p_150722_0_).syncUninterruptibly();
		return networkmanager;
	}

	public void enableEncryption(SecretKey p_150727_1_) {
		channelFuture.channel().pipeline().addBefore("splitter", "decrypt",
				new NettyEncryptingDecoder(CryptManager.func_151229_a(2, p_150727_1_)));
		channelFuture.channel().pipeline().addBefore("prepender", "encrypt",
				new NettyEncryptingEncoder(CryptManager.func_151229_a(1, p_150727_1_)));
		field_152463_r = true;
	}

	public boolean isChannelOpen() {
		return channelFuture.channel() != null && channelFuture.channel().isOpen();
	}

	public INetHandler getNetHandler() {
		return netHandler;
	}

	public IChatComponent getExitMessage() {
		return terminationReason;
	}

	public void disableAutoRead() {
		channelFuture.channel().config().setAutoRead(false);
	}

	@Override
	protected void channelRead0(ChannelHandlerContext p_channelRead0_1_, Object p_channelRead0_2_) {
		this.channelRead0(p_channelRead0_1_, (Packet) p_channelRead0_2_);
	}

	public Channel channel() {
		return channelFuture.channel();
	}

	static class InboundHandlerTuplePacketListener {
		private final Packet field_150774_a;
		private final GenericFutureListener[] field_150773_b;

		public InboundHandlerTuplePacketListener(Packet p_i45146_1_, GenericFutureListener... p_i45146_2_) {
			field_150774_a = p_i45146_1_;
			field_150773_b = p_i45146_2_;
		}
	}
}