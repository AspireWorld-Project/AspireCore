package net.minecraft.realms;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.util.concurrent.GenericFutureListener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerLoginClient;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.login.client.C00PacketLoginStart;
import net.minecraft.util.ChatComponentTranslation;

@SideOnly(Side.CLIENT)
public class RealmsConnect {
	private static final Logger LOGGER = LogManager.getLogger();
	private final RealmsScreen onlineScreen;
	private volatile boolean aborted = false;
	private NetworkManager connection;
	private static final String __OBFID = "CL_00001844";

	public RealmsConnect(RealmsScreen p_i1079_1_) {
		onlineScreen = p_i1079_1_;
	}

	public void connect(final String p_connect_1_, final int p_connect_2_) {
		new Thread("Realms-connect-task") {
			private static final String __OBFID = "CL_00001808";

			@Override
			public void run() {
				InetAddress inetaddress = null;

				try {
					cpw.mods.fml.client.FMLClientHandler.instance().connectToRealmsServer(p_connect_1_, p_connect_2_);
					inetaddress = InetAddress.getByName(p_connect_1_);

					if (aborted)
						return;

					connection = NetworkManager.provideLanClient(inetaddress, p_connect_2_);

					if (aborted)
						return;

					connection.setNetHandler(
							new NetHandlerLoginClient(connection, Minecraft.getMinecraft(), onlineScreen.getProxy()));

					if (aborted)
						return;

					connection.scheduleOutboundPacket(
							new C00Handshake(5, p_connect_1_, p_connect_2_, EnumConnectionState.LOGIN),
							new GenericFutureListener[0]);

					if (aborted)
						return;

					connection.scheduleOutboundPacket(
							new C00PacketLoginStart(Minecraft.getMinecraft().getSession().func_148256_e()),
							new GenericFutureListener[0]);
				} catch (UnknownHostException unknownhostexception) {
					if (aborted)
						return;

					RealmsConnect.LOGGER.error("Couldn\'t connect to world", unknownhostexception);
					Realms.setScreen(new DisconnectedOnlineScreen(onlineScreen, "connect.failed",
							new ChatComponentTranslation("disconnect.genericReason",
									new Object[] { "Unknown host \'" + p_connect_1_ + "\'" })));
				} catch (Exception exception) {
					if (aborted)
						return;

					RealmsConnect.LOGGER.error("Couldn\'t connect to world", exception);
					String s = exception.toString();

					if (inetaddress != null) {
						String s1 = inetaddress.toString() + ":" + p_connect_2_;
						s = s.replaceAll(s1, "");
					}

					Realms.setScreen(new DisconnectedOnlineScreen(onlineScreen, "connect.failed",
							new ChatComponentTranslation("disconnect.genericReason", new Object[] { s })));
				}
			}
		}.start();
	}

	public void abort() {
		aborted = true;
	}

	public void tick() {
		if (connection != null) {
			if (connection.isChannelOpen()) {
				connection.processReceivedPackets();
			} else if (connection.getExitMessage() != null) {
				connection.getNetHandler().onDisconnect(connection.getExitMessage());
			}
		}
	}
}