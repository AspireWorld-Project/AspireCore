package net.minecraft.network.rcon;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import org.ultramine.server.ConfigurationHandler;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

@SideOnly(Side.SERVER)
public class RConThreadMain extends RConThreadBase {
	private int rconPort;
	private final int serverPort;
	private String hostname;
	private ServerSocket serverSocket;
	private final String rconPassword;
	private Map clientThreads;
	private static final String __OBFID = "CL_00001805";

	public RConThreadMain(IServer p_i1538_1_) {
		super(p_i1538_1_, "RCON Listener");
		rconPort = ConfigurationHandler.getServerConfig().listen.rcon.port;
		rconPassword = ConfigurationHandler.getServerConfig().listen.rcon.password;
		hostname = p_i1538_1_.getHostname();
		serverPort = p_i1538_1_.getPort();

		if (0 == rconPort) {
			rconPort = serverPort + 10;
			logInfo("Setting default rcon port to " + rconPort);
			ConfigurationHandler.getServerConfig().listen.rcon.port = rconPort;
			p_i1538_1_.saveProperties();
		}

		if (0 == hostname.length()) {
			hostname = "0.0.0.0";
		}

		initClientThreadList();
		serverSocket = null;
	}

	private void initClientThreadList() {
		clientThreads = new HashMap();
	}

	private void cleanClientThreadsMap() {
		Iterator iterator = clientThreads.entrySet().iterator();

		while (iterator.hasNext()) {
			Entry entry = (Entry) iterator.next();

			if (!((RConThreadClient) entry.getValue()).isRunning()) {
				iterator.remove();
			}
		}
	}

	@Override
	public void run() {
		logInfo("RCON running on " + hostname + ":" + rconPort);

		try {
			while (running) {
				try {
					Socket socket = serverSocket.accept();
					socket.setSoTimeout(500);
					RConThreadClient rconthreadclient = new RConThreadClient(server, socket);
					rconthreadclient.startThread();
					clientThreads.put(socket.getRemoteSocketAddress(), rconthreadclient);
					cleanClientThreadsMap();
				} catch (SocketTimeoutException sockettimeoutexception) {
					cleanClientThreadsMap();
				} catch (IOException ioexception) {
					if (running) {
						logInfo("IO: " + ioexception.getMessage());
					}
				}
			}
		} finally {
			closeServerSocket(serverSocket);
		}
	}

	@Override
	public void startThread() {
		if (0 == rconPassword.length()) {
			logWarning("No rcon password set in '" + server.getSettingsFilename() + "', rcon disabled!");
		} else if (0 < rconPort && 65535 >= rconPort) {
			if (!running) {
				try {
					serverSocket = new ServerSocket(rconPort, 0, InetAddress.getByName(hostname));
					serverSocket.setSoTimeout(500);
					super.startThread();
				} catch (IOException ioexception) {
					logWarning("Unable to initialise rcon on " + hostname + ":" + rconPort + " : "
							+ ioexception.getMessage());
				}
			}
		} else {
			logWarning("Invalid rcon port " + rconPort + " found in '" + server.getSettingsFilename()
					+ "', rcon disabled!");
		}
	}
}