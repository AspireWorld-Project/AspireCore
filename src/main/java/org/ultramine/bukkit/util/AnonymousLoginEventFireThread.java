package org.ultramine.bukkit.util;

import net.minecraft.server.network.NetHandlerLoginServer;
import org.bukkit.Bukkit;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

/**
 * This is an anonymous runnable
 * for @mixin.network.play.MixinNetHLoginS#processLoginStart(C00PacketLoginStart
 * packetLogin);
 */
public class AnonymousLoginEventFireThread extends Thread {
	private NetHandlerLoginServer netHandlerLoginServer;

	public AnonymousLoginEventFireThread(NetHandlerLoginServer netHandlerLoginServer) {
		this.netHandlerLoginServer = netHandlerLoginServer;
	}

	@Override
	public void run() {
		if (!netHandlerLoginServer.getNetworkManager().isChannelOpen())
			return;
		AsyncPlayerPreLoginEvent asyncPlayerPreLoginEvent = new AsyncPlayerPreLoginEvent(
				netHandlerLoginServer.getGameProfile().getName(),
				((java.net.InetSocketAddress) netHandlerLoginServer.getNetworkManager().getSocketAddress())
						.getAddress(),
				netHandlerLoginServer.getGameProfile().getId());
		Bukkit.getPluginManager().callEvent(asyncPlayerPreLoginEvent);
		if (asyncPlayerPreLoginEvent.getLoginResult() != AsyncPlayerPreLoginEvent.Result.ALLOWED) {
			netHandlerLoginServer.func_147322_a(asyncPlayerPreLoginEvent.getKickMessage());
			return;
		}
		netHandlerLoginServer.getLogger().info("UUID of player " + netHandlerLoginServer.getGameProfile().getName()
				+ " is " + netHandlerLoginServer.getGameProfile().getId());
		netHandlerLoginServer.setLoginState(NetHandlerLoginServer.LoginState.READY_TO_ACCEPT);
	}
}
