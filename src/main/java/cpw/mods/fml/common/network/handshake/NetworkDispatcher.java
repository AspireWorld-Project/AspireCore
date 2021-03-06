package cpw.mods.fml.common.network.handshake;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.network.FMLNetworkEvent;
import cpw.mods.fml.common.network.FMLNetworkException;
import cpw.mods.fml.common.network.FMLOutboundHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.internal.FMLMessage;
import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import cpw.mods.fml.relauncher.Side;
import io.netty.channel.*;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.ScheduledFuture;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.*;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.network.play.server.S01PacketJoinGame;
import net.minecraft.network.play.server.S3FPacketCustomPayload;
import net.minecraft.network.play.server.S40PacketDisconnect;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.util.ChatComponentText;
import org.apache.logging.log4j.Level;
import java.net.SocketAddress;
import java.nio.channels.ClosedChannelException;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class NetworkDispatcher extends SimpleChannelInboundHandler<Packet> implements ChannelOutboundHandler {
	private enum ConnectionState {
		OPENING, AWAITING_HANDSHAKE, HANDSHAKING, HANDSHAKECOMPLETE, CONNECTED
    }

	private enum ConnectionType {
		MODDED, BUKKIT, VANILLA
    }

	public static NetworkDispatcher get(NetworkManager manager) {
		return manager.channel().attr(FML_DISPATCHER).get();
	}

	public static NetworkDispatcher allocAndSet(NetworkManager manager) {
		NetworkDispatcher net = new NetworkDispatcher(manager);
		manager.channel().attr(FML_DISPATCHER).getAndSet(net);
		return net;
	}

	public static NetworkDispatcher allocAndSet(NetworkManager manager, ServerConfigurationManager scm) {
		NetworkDispatcher net = new NetworkDispatcher(manager, scm);
		manager.channel().attr(FML_DISPATCHER).getAndSet(net);
		return net;
	}

	public static final AttributeKey<NetworkDispatcher> FML_DISPATCHER = new AttributeKey<>("fml:dispatcher");
	public static final AttributeKey<Boolean> IS_LOCAL = new AttributeKey<>("fml:isLocal");
	public final NetworkManager manager;
	private final ServerConfigurationManager scm;
	private EntityPlayerMP player;
	private ConnectionState state;
	private ConnectionType connectionType;
	private final Side side;
	private final EmbeddedChannel handshakeChannel;
	private NetHandlerPlayServer serverHandler;
	private INetHandler netHandler;
	private int overrideLoginDim;

	public NetworkDispatcher(NetworkManager manager) {
		super(Packet.class, false);
		this.manager = manager;
		scm = null;
		side = Side.CLIENT;
		handshakeChannel = new EmbeddedChannel(new HandshakeInjector(this), new ChannelRegistrationHandler(),
				new FMLHandshakeCodec(), new HandshakeMessageHandler<>(FMLHandshakeClientState.class));
		handshakeChannel.attr(FML_DISPATCHER).set(this);
		handshakeChannel.attr(NetworkRegistry.CHANNEL_SOURCE).set(Side.SERVER);
		handshakeChannel.attr(NetworkRegistry.FML_CHANNEL).set("FML|HS");
		handshakeChannel.attr(IS_LOCAL).set(manager.isLocalChannel());
	}

	public NetworkDispatcher(NetworkManager manager, ServerConfigurationManager scm) {
		super(Packet.class, false);
		this.manager = manager;
		this.scm = scm;
		side = Side.SERVER;
		handshakeChannel = new EmbeddedChannel(new HandshakeInjector(this), new ChannelRegistrationHandler(),
				new FMLHandshakeCodec(), new HandshakeMessageHandler<>(FMLHandshakeServerState.class));
		handshakeChannel.attr(FML_DISPATCHER).set(this);
		handshakeChannel.attr(NetworkRegistry.CHANNEL_SOURCE).set(Side.CLIENT);
		handshakeChannel.attr(NetworkRegistry.FML_CHANNEL).set("FML|HS");
		handshakeChannel.attr(IS_LOCAL).set(manager.isLocalChannel());
	}

	public void serverToClientHandshake(EntityPlayerMP player) {
		this.player = player;
		insertIntoChannel();
	}

	private void insertIntoChannel() {
		manager.channel().config().setAutoRead(false);
		// Insert ourselves into the pipeline
		manager.channel().pipeline().addBefore("packet_handler", "fml:packet_handler", this);
	}

	public void clientToServerHandshake() {
		insertIntoChannel();
	}

	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		state = ConnectionState.OPENING;
		// send ourselves as a user event, to kick the pipeline active
		handshakeChannel.pipeline().fireUserEventTriggered(this);
		manager.channel().config().setAutoRead(true);
	}

	int serverInitiateHandshake() {
		// Send mod salutation to the client
		// This will be ignored by vanilla clients
		state = ConnectionState.AWAITING_HANDSHAKE;
		manager.channel().pipeline().addFirst("fml:vanilla_detector", new VanillaTimeoutWaiter());
		// Need to start the handler here, so we can send custompayload packets
		serverHandler = new NetHandlerPlayServer(scm.getServerInstance(), manager, player);
		netHandler = serverHandler;
		// NULL the play server here - we restore it further on. If not, there are
		// packets sent before the login
		player.playerNetServerHandler = null;
		// manually for the manager into the PLAY state, so we can send packets later
		manager.setConnectionState(EnumConnectionState.PLAY);

		// Return the dimension the player is in, so it can be pre-sent to the client in
		// the ServerHello v2 packet
		// Requires some hackery to the serverconfigmanager and stuff for this to work

		// Disabled by UltraMine - no IO operations in main thread
		// Support for more than 256 dimension ids implemented in
		// ServerConfigurationManager.initializeConnectionToPlayer_body
		// by sending additional S07PacketRespawn packet.
		return 0;
		/*
		 * NBTTagCompound playerNBT = scm.getPlayerNBT(player); if (playerNBT!=null) {
		 * return playerNBT.getInteger("Dimension"); } else { return 0; }
		 */
	}

	void clientListenForServerHandshake() {
		manager.setConnectionState(EnumConnectionState.PLAY);
		FMLCommonHandler.instance().waitForPlayClient();
		netHandler = FMLCommonHandler.instance().getClientPlayHandler();
		state = ConnectionState.AWAITING_HANDSHAKE;
	}

	private void completeClientSideConnection(ConnectionType type) {
		connectionType = type;
		FMLLog.info("[%s] Client side %s connection established", Thread.currentThread().getName(),
				connectionType.name().toLowerCase(Locale.ENGLISH));
		state = ConnectionState.CONNECTED;
		FMLCommonHandler.instance().bus()
				.post(new FMLNetworkEvent.ClientConnectedToServerEvent(manager, connectionType.name()));
	}

	private void completeServerSideConnection(ConnectionType type) {
		connectionType = type;
		FMLLog.fine("[%s] Server side %s connection established", Thread.currentThread().getName(),
				connectionType.name().toLowerCase(Locale.ENGLISH));
		state = ConnectionState.CONNECTED;
		// FMLCommonHandler.instance().bus().post(new
		// FMLNetworkEvent.ServerConnectionFromClientEvent(manager));
		scm.initializeConnectionToPlayer(manager, player, serverHandler);
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Packet msg) throws Exception {
		boolean handled = false;
		if (msg instanceof C17PacketCustomPayload) {
			handled = handleServerSideCustomPacket((C17PacketCustomPayload) msg, ctx);
		} else if (msg instanceof S3FPacketCustomPayload) {
			handled = handleClientSideCustomPacket((S3FPacketCustomPayload) msg, ctx);
		} else if (state != ConnectionState.CONNECTED && state != ConnectionState.HANDSHAKECOMPLETE) {
			handled = handleVanilla(msg);
		}
		if (!handled) {
			ctx.fireChannelRead(msg);
		}
	}

	private boolean handleVanilla(Packet msg) {
		if (state == ConnectionState.AWAITING_HANDSHAKE && msg instanceof S01PacketJoinGame) {
			handshakeChannel.pipeline().fireUserEventTriggered(msg);
		} else {
			FMLLog.info("Unexpected packet during modded negotiation - assuming vanilla or keepalives : %s",
					msg.getClass().getName());
		}
		return false;
	}

	public INetHandler getNetHandler() {
		return netHandler;
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if (evt instanceof ConnectionType && side == Side.SERVER) {
			FMLLog.info("Timeout occurred, assuming a vanilla client");
			kickVanilla();
		}
	}

	private void kickVanilla() {
		kickWithMessage("This is modded. No modded response received. Bye!");
	}

	private void kickWithMessage(String message) {
		final ChatComponentText chatcomponenttext = new ChatComponentText(message);
		if (side == Side.CLIENT) {
			manager.closeChannel(chatcomponenttext);
		} else {
			manager.scheduleOutboundPacket(new S40PacketDisconnect(chatcomponenttext),
					new GenericFutureListener<Future<?>>() {
						@Override
						public void operationComplete(Future<?> result) {
							manager.closeChannel(chatcomponenttext);
						}
					});
		}
		manager.channel().config().setAutoRead(false);
	}

	private boolean handleClientSideCustomPacket(S3FPacketCustomPayload msg, ChannelHandlerContext context) {
		String channelName = msg.func_149169_c();
		if ("FML|HS".equals(channelName) || "REGISTER".equals(channelName) || "UNREGISTER".equals(channelName)) {
			FMLProxyPacket proxy = new FMLProxyPacket(msg);
			proxy.setDispatcher(this);
			handshakeChannel.writeInbound(proxy);
			// forward any messages into the regular channel
			for (Object push : handshakeChannel.inboundMessages()) {
				List<FMLProxyPacket> messageResult = FMLNetworkHandler
						.forwardHandshake((FMLMessage.CompleteHandshake) push, this, Side.CLIENT);
				for (FMLProxyPacket result : messageResult) {
					result.setTarget(Side.CLIENT);
					result.payload().resetReaderIndex();
					context.fireChannelRead(result);
				}
			}
			handshakeChannel.inboundMessages().clear();
			return true;
		} else if (NetworkRegistry.INSTANCE.hasChannel(channelName, Side.CLIENT)) {
			FMLProxyPacket proxy = new FMLProxyPacket(msg);
			proxy.setDispatcher(this);
			context.fireChannelRead(proxy);
			return true;
		}
		return false;
	}

	private boolean handleServerSideCustomPacket(C17PacketCustomPayload msg, ChannelHandlerContext context) {
		player.getBukkitEntity().addChannel(msg.func_149559_c()); // Cauldron -
																					// register channel
																					// for bukkit player
		if (state == ConnectionState.AWAITING_HANDSHAKE) {
			manager.channel().pipeline().remove("fml:vanilla_detector");
			state = ConnectionState.HANDSHAKING;
		}
		String channelName = msg.func_149559_c();
		if ("FML|HS".equals(channelName) || "REGISTER".equals(channelName) || "UNREGISTER".equals(channelName)) {
			FMLProxyPacket proxy = new FMLProxyPacket(msg);
			proxy.setDispatcher(this);
			handshakeChannel.writeInbound(proxy);
			for (Object push : handshakeChannel.inboundMessages()) {
				List<FMLProxyPacket> messageResult = FMLNetworkHandler
						.forwardHandshake((FMLMessage.CompleteHandshake) push, this, Side.SERVER);
				for (FMLProxyPacket result : messageResult) {
					result.setTarget(Side.SERVER);
					result.payload().resetReaderIndex();
					context.fireChannelRead(result);
				}
			}
			handshakeChannel.inboundMessages().clear();
			return true;
		} else if (NetworkRegistry.INSTANCE.hasChannel(channelName, Side.SERVER)) {
			FMLProxyPacket proxy = new FMLProxyPacket(msg);
			proxy.setDispatcher(this);
			context.fireChannelRead(proxy);
			return true;
		}
		return false;
	}

	private class VanillaTimeoutWaiter extends ChannelInboundHandlerAdapter {
		private ScheduledFuture<Void> future;

		@Override
		public void handlerAdded(final ChannelHandlerContext ctx) throws Exception {
			future = ctx.executor().schedule(new Callable<Void>() {
				@Override
				public Void call() throws Exception {
					if (state != ConnectionState.CONNECTED) {
						FMLLog.info("Timeout occurred waiting for response, assuming vanilla connection");
						ctx.fireUserEventTriggered(ConnectionType.VANILLA);
					}
					return null;
				}
			}, 10, TimeUnit.HOURS);
		}

		@Override
		public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
			future.cancel(true);
		}
	}

	public void sendProxy(FMLProxyPacket msg) {
		manager.scheduleOutboundPacket(msg);
	}

	public void rejectHandshake(String result) {
		kickWithMessage(result);
	}

	@Override
	public void bind(ChannelHandlerContext ctx, SocketAddress localAddress, ChannelPromise promise) throws Exception {
		ctx.bind(localAddress, promise);
	}

	@Override
	public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress,
			ChannelPromise promise) throws Exception {
		ctx.connect(remoteAddress, localAddress, promise);
	}

	@Override
	public void disconnect(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
		if (side == Side.CLIENT) {
			FMLCommonHandler.instance().bus().post(new FMLNetworkEvent.ClientDisconnectionFromServerEvent(manager));
		} else {
			FMLCommonHandler.instance().bus().post(new FMLNetworkEvent.ServerDisconnectionFromClientEvent(manager));
		}
		cleanAttributes(ctx);
		ctx.disconnect(promise);
	}

	@Override
	public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
		if (side == Side.CLIENT) {
			FMLCommonHandler.instance().bus().post(new FMLNetworkEvent.ClientDisconnectionFromServerEvent(manager));
		} else {
			FMLCommonHandler.instance().bus().post(new FMLNetworkEvent.ServerDisconnectionFromClientEvent(manager));
		}
		cleanAttributes(ctx);
		ctx.close(promise);
	}

	@Override
	@Deprecated
	public void deregister(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
		ctx.deregister(promise);
	}

	@Override
	public void read(ChannelHandlerContext ctx) throws Exception {
		ctx.read();
	}

	@Override
	public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
		if (msg instanceof FMLProxyPacket) {
			if (side == Side.CLIENT) {
				ctx.write(((FMLProxyPacket) msg).toC17Packet(), promise);
			} else {
				ctx.write(((FMLProxyPacket) msg).toS3FPacket(), promise);
			}
		} else {
			ctx.write(msg, promise);
		}
	}

	@Override
	public void flush(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}

	public void completeHandshake(Side target) {
		if (state == ConnectionState.CONNECTED) {
			FMLLog.severe("Attempt to double complete the network connection!");
			throw new FMLNetworkException("Attempt to double complete!");
		}
		if (side == Side.CLIENT) {
			completeClientSideConnection(ConnectionType.MODDED);
		} else {
			completeServerSideConnection(ConnectionType.MODDED);
		}
	}

	public void completeClientHandshake() {
		state = ConnectionState.HANDSHAKECOMPLETE;
	}

	public void abortClientHandshake(String type) {
		FMLLog.log(Level.INFO, "Aborting client handshake \"%s\"", type);
		FMLCommonHandler.instance().waitForPlayClient();
		completeClientSideConnection(ConnectionType.valueOf(type));
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		// Stop the epic channel closed spam at close
		if (!(cause instanceof ClosedChannelException)) {
			FMLLog.log(Level.DEBUG, cause, "NetworkDispatcher exception");
		}
		super.exceptionCaught(ctx, cause);
	}

	// if we add any attributes, we should force removal of them here so that
	// they do not hold references to the world and causes it to leak.
	private void cleanAttributes(ChannelHandlerContext ctx) {
		ctx.channel().attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).remove();
		ctx.channel().attr(NetworkRegistry.NET_HANDLER).remove();
		ctx.channel().attr(NetworkDispatcher.FML_DISPATCHER).remove();
		handshakeChannel.attr(FML_DISPATCHER).remove();
		manager.channel().attr(FML_DISPATCHER).remove();
	}

	public void setOverrideDimension(int overrideDim) {
		overrideLoginDim = overrideDim;
		FMLLog.fine("Received override dimension %d", overrideDim);
	}

	public int getOverrideDimension(S01PacketJoinGame p_147282_1_) {
		FMLLog.fine("Overriding dimension: using %d", overrideLoginDim);
		return overrideLoginDim != 0 ? overrideLoginDim : p_147282_1_.func_149194_f();
	}

	public EntityPlayerMP getPlayer() {
		return player;
	}
}
