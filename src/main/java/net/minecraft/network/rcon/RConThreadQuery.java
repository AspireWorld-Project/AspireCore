package net.minecraft.network.rcon;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.server.MinecraftServer;
import org.ultramine.server.ConfigurationHandler;

import java.io.IOException;
import java.net.*;
import java.util.*;
import java.util.Map.Entry;

@SideOnly(Side.SERVER)
public class RConThreadQuery extends RConThreadBase {
	private long lastAuthCheckTime;
	private int queryPort;
	private int serverPort;
	private int maxPlayers;
	private String serverMotd;
	private String worldName;
	private DatagramSocket querySocket;
	private byte[] buffer = new byte[1460];
	private DatagramPacket incomingPacket;
	private Map field_72644_p;
	private String queryHostname;
	private String serverHostname;
	private Map queryClients;
	private long time;
	private RConOutputStream output;
	private long lastQueryResponseTime;
	private static final String __OBFID = "CL_00001802";

	public RConThreadQuery(IServer p_i1536_1_) {
		super(p_i1536_1_, "Query Listener");
		queryPort = ConfigurationHandler.getServerConfig().listen.query.port;
		serverHostname = p_i1536_1_.getHostname();
		serverPort = p_i1536_1_.getPort();
		serverMotd = p_i1536_1_.getMotd();
		maxPlayers = p_i1536_1_.getMaxPlayers();
		worldName = p_i1536_1_.getFolderName();
		lastQueryResponseTime = 0L;
		queryHostname = "0.0.0.0";

		if (0 != serverHostname.length() && !queryHostname.equals(serverHostname)) {
			queryHostname = serverHostname;
		} else {
			serverHostname = "0.0.0.0";

			try {
				InetAddress inetaddress = InetAddress.getLocalHost();
				queryHostname = inetaddress.getHostAddress();
			} catch (UnknownHostException unknownhostexception) {
				logWarning("Unable to determine local host IP, please set server-ip in \'"
						+ p_i1536_1_.getSettingsFilename() + "\' : " + unknownhostexception.getMessage());
			}
		}

		if (0 == queryPort) {
			queryPort = serverPort;
			logInfo("Setting default query port to " + queryPort);
			ConfigurationHandler.getServerConfig().listen.query.port = queryPort;
			p_i1536_1_.setProperty("debug", Boolean.valueOf(false));
			p_i1536_1_.saveProperties();
		}

		field_72644_p = new HashMap();
		output = new RConOutputStream(1460);
		queryClients = new HashMap();
		time = new Date().getTime();
	}

	private void sendResponsePacket(byte[] p_72620_1_, DatagramPacket p_72620_2_) throws IOException {
		querySocket.send(new DatagramPacket(p_72620_1_, p_72620_1_.length, p_72620_2_.getSocketAddress()));
	}

	private boolean parseIncomingPacket(DatagramPacket p_72621_1_) throws IOException {
		byte[] abyte = p_72621_1_.getData();
		int i = p_72621_1_.getLength();
		SocketAddress socketaddress = p_72621_1_.getSocketAddress();
		logDebug("Packet len " + i + " [" + socketaddress + "]");

		if (3 <= i && -2 == abyte[0] && -3 == abyte[1]) {
			logDebug("Packet \'" + RConUtils.getByteAsHexString(abyte[2]) + "\' [" + socketaddress + "]");

			switch (abyte[2]) {
			case 0:
				if (!verifyClientAuth(p_72621_1_).booleanValue()) {
					logDebug("Invalid challenge [" + socketaddress + "]");
					return false;
				} else if (15 == i) {
					sendResponsePacket(createQueryResponse(p_72621_1_), p_72621_1_);
					logDebug("Rules [" + socketaddress + "]");
				} else {
					RConOutputStream rconoutputstream = new RConOutputStream(1460);
					rconoutputstream.writeInt(0);
					rconoutputstream.writeByteArray(getRequestID(p_72621_1_.getSocketAddress()));
					rconoutputstream.writeString(serverMotd);
					rconoutputstream.writeString("SMP");
					rconoutputstream.writeString(worldName);
					rconoutputstream.writeString(Integer.toString(getNumberOfPlayers()));
					rconoutputstream.writeString(Integer.toString(maxPlayers));
					rconoutputstream.writeShort((short) serverPort);
					rconoutputstream.writeString(queryHostname);
					sendResponsePacket(rconoutputstream.toByteArray(), p_72621_1_);
					logDebug("Status [" + socketaddress + "]");
				}
			case 9:
				sendAuthChallenge(p_72621_1_);
				logDebug("Challenge [" + socketaddress + "]");
				return true;
			default:
				return true;
			}
		} else {
			logDebug("Invalid packet [" + socketaddress + "]");
			return false;
		}
	}

	private byte[] createQueryResponse(DatagramPacket p_72624_1_) throws IOException {
		long i = MinecraftServer.getSystemTimeMillis();

		if (i < lastQueryResponseTime + 5000L) {
			byte[] abyte = output.toByteArray();
			byte[] abyte1 = getRequestID(p_72624_1_.getSocketAddress());
			abyte[1] = abyte1[0];
			abyte[2] = abyte1[1];
			abyte[3] = abyte1[2];
			abyte[4] = abyte1[3];
			return abyte;
		} else {
			lastQueryResponseTime = i;
			output.reset();
			output.writeInt(0);
			output.writeByteArray(getRequestID(p_72624_1_.getSocketAddress()));
			output.writeString("splitnum");
			output.writeInt(128);
			output.writeInt(0);
			output.writeString("hostname");
			output.writeString(serverMotd);
			output.writeString("gametype");
			output.writeString("SMP");
			output.writeString("game_id");
			output.writeString("MINECRAFT");
			output.writeString("version");
			output.writeString(server.getMinecraftVersion());
			output.writeString("plugins");
			output.writeString(server.getPlugins());
			output.writeString("map");
			output.writeString(worldName);
			output.writeString("numplayers");
			output.writeString("" + getNumberOfPlayers());
			output.writeString("maxplayers");
			output.writeString("" + maxPlayers);
			output.writeString("hostport");
			output.writeString("" + serverPort);
			output.writeString("hostip");
			output.writeString(queryHostname);
			output.writeInt(0);
			output.writeInt(1);
			output.writeString("player_");
			output.writeInt(0);
			String[] astring = server.getAllUsernames();
			String[] astring1 = astring;
			int j = astring.length;

			for (int k = 0; k < j; ++k) {
				String s = astring1[k];
				output.writeString(s);
			}

			output.writeInt(0);
			return output.toByteArray();
		}
	}

	private byte[] getRequestID(SocketAddress p_72625_1_) {
		return ((RConThreadQuery.Auth) queryClients.get(p_72625_1_)).getRequestId();
	}

	private Boolean verifyClientAuth(DatagramPacket p_72627_1_) {
		SocketAddress socketaddress = p_72627_1_.getSocketAddress();

		if (!queryClients.containsKey(socketaddress))
			return Boolean.valueOf(false);
		else {
			byte[] abyte = p_72627_1_.getData();
			return ((RConThreadQuery.Auth) queryClients.get(socketaddress)).getRandomChallenge() != RConUtils
					.getBytesAsBEint(abyte, 7, p_72627_1_.getLength()) ? Boolean.valueOf(false) : Boolean.valueOf(true);
		}
	}

	private void sendAuthChallenge(DatagramPacket p_72622_1_) throws IOException {
		RConThreadQuery.Auth auth = new RConThreadQuery.Auth(p_72622_1_);
		queryClients.put(p_72622_1_.getSocketAddress(), auth);
		sendResponsePacket(auth.getChallengeValue(), p_72622_1_);
	}

	private void cleanQueryClientsMap() {
		if (running) {
			long i = MinecraftServer.getSystemTimeMillis();

			if (i >= lastAuthCheckTime + 30000L) {
				lastAuthCheckTime = i;
				Iterator iterator = queryClients.entrySet().iterator();

				while (iterator.hasNext()) {
					Entry entry = (Entry) iterator.next();

					if (((RConThreadQuery.Auth) entry.getValue()).hasExpired(i).booleanValue()) {
						iterator.remove();
					}
				}
			}
		}
	}

	@Override
	public void run() {
		logInfo("Query running on " + serverHostname + ":" + queryPort);
		lastAuthCheckTime = MinecraftServer.getSystemTimeMillis();
		incomingPacket = new DatagramPacket(buffer, buffer.length);

		try {
			while (running) {
				try {
					querySocket.receive(incomingPacket);
					cleanQueryClientsMap();
					parseIncomingPacket(incomingPacket);
				} catch (SocketTimeoutException sockettimeoutexception) {
					cleanQueryClientsMap();
				} catch (PortUnreachableException portunreachableexception) {
					;
				} catch (IOException ioexception) {
					stopWithException(ioexception);
				}
			}
		} finally {
			closeAllSockets();
		}
	}

	@Override
	public void startThread() {
		if (!running) {
			if (0 < queryPort && 65535 >= queryPort) {
				if (initQuerySystem()) {
					super.startThread();
				}
			} else {
				logWarning("Invalid query port " + queryPort + " found in \'" + server.getSettingsFilename()
						+ "\' (queries disabled)");
			}
		}
	}

	private void stopWithException(Exception p_72623_1_) {
		if (running) {
			logWarning("Unexpected exception, buggy JRE? (" + p_72623_1_.toString() + ")");

			if (!initQuerySystem()) {
				logSevere("Failed to recover from buggy JRE, shutting down!");
				running = false;
			}
		}
	}

	private boolean initQuerySystem() {
		try {
			querySocket = new DatagramSocket(queryPort, InetAddress.getByName(serverHostname));
			registerSocket(querySocket);
			querySocket.setSoTimeout(500);
			return true;
		} catch (SocketException socketexception) {
			logWarning("Unable to initialise query system on " + serverHostname + ":" + queryPort + " (Socket): "
					+ socketexception.getMessage());
		} catch (UnknownHostException unknownhostexception) {
			logWarning("Unable to initialise query system on " + serverHostname + ":" + queryPort + " (Unknown Host): "
					+ unknownhostexception.getMessage());
		} catch (Exception exception) {
			logWarning("Unable to initialise query system on " + serverHostname + ":" + queryPort + " (E): "
					+ exception.getMessage());
		}

		return false;
	}

	@SideOnly(Side.SERVER)
	class Auth {
		private long timestamp = new Date().getTime();
		private int randomChallenge;
		private byte[] requestId;
		private byte[] challengeValue;
		private String requestIdAsString;
		private static final String __OBFID = "CL_00001803";

		public Auth(DatagramPacket p_i1535_2_) {
			byte[] abyte = p_i1535_2_.getData();
			requestId = new byte[4];
			requestId[0] = abyte[3];
			requestId[1] = abyte[4];
			requestId[2] = abyte[5];
			requestId[3] = abyte[6];
			requestIdAsString = new String(requestId);
			randomChallenge = new Random().nextInt(16777216);
			challengeValue = String
					.format("\t%s%d\u0000", new Object[] { requestIdAsString, Integer.valueOf(randomChallenge) })
					.getBytes();
		}

		public Boolean hasExpired(long p_72593_1_) {
			return Boolean.valueOf(timestamp < p_72593_1_);
		}

		public int getRandomChallenge() {
			return randomChallenge;
		}

		public byte[] getChallengeValue() {
			return challengeValue;
		}

		public byte[] getRequestId() {
			return requestId;
		}
	}
}