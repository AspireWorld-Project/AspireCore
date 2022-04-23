package net.minecraft.client.network;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ThreadLanServerPing;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@SideOnly(Side.CLIENT)
public class LanServerDetector {
	private static final AtomicInteger field_148551_a = new AtomicInteger(0);
	private static final Logger logger = LogManager.getLogger();
	private static final String __OBFID = "CL_00001133";

	@SideOnly(Side.CLIENT)
	public static class LanServer {
		private String lanServerMotd;
		private String lanServerIpPort;
		private long timeLastSeen;
		private static final String __OBFID = "CL_00001134";

		public LanServer(String p_i1319_1_, String p_i1319_2_) {
			lanServerMotd = p_i1319_1_;
			lanServerIpPort = p_i1319_2_;
			timeLastSeen = Minecraft.getSystemTime();
		}

		public String getServerMotd() {
			return lanServerMotd;
		}

		public String getServerIpPort() {
			return lanServerIpPort;
		}

		public void updateLastSeen() {
			timeLastSeen = Minecraft.getSystemTime();
		}
	}

	@SideOnly(Side.CLIENT)
	public static class LanServerList {
		private ArrayList listOfLanServers = new ArrayList();
		boolean wasUpdated;
		private static final String __OBFID = "CL_00001136";

		public synchronized boolean getWasUpdated() {
			return wasUpdated;
		}

		public synchronized void setWasNotUpdated() {
			wasUpdated = false;
		}

		public synchronized List getLanServers() {
			return Collections.unmodifiableList(listOfLanServers);
		}

		public synchronized void func_77551_a(String p_77551_1_, InetAddress p_77551_2_) {
			String s1 = ThreadLanServerPing.getMotdFromPingResponse(p_77551_1_);
			String s2 = ThreadLanServerPing.getAdFromPingResponse(p_77551_1_);

			if (s2 != null) {
				s2 = p_77551_2_.getHostAddress() + ":" + s2;
				boolean flag = false;
				Iterator iterator = listOfLanServers.iterator();

				while (iterator.hasNext()) {
					LanServerDetector.LanServer lanserver = (LanServerDetector.LanServer) iterator.next();

					if (lanserver.getServerIpPort().equals(s2)) {
						lanserver.updateLastSeen();
						flag = true;
						break;
					}
				}

				if (!flag) {
					listOfLanServers.add(new LanServerDetector.LanServer(s1, s2));
					wasUpdated = true;
				}
			}
		}
	}

	@SideOnly(Side.CLIENT)
	public static class ThreadLanServerFind extends Thread {
		private final LanServerDetector.LanServerList localServerList;
		private final InetAddress broadcastAddress;
		private final MulticastSocket socket;
		private static final String __OBFID = "CL_00001135";

		public ThreadLanServerFind(LanServerDetector.LanServerList p_i1320_1_) throws IOException {
			super("LanServerDetector #" + LanServerDetector.field_148551_a.incrementAndGet());
			localServerList = p_i1320_1_;
			setDaemon(true);
			socket = new MulticastSocket(4445);
			broadcastAddress = InetAddress.getByName("224.0.2.60");
			socket.setSoTimeout(5000);
			socket.joinGroup(broadcastAddress);
		}

		@Override
		public void run() {
			byte[] abyte = new byte[1024];

			while (!isInterrupted()) {
				DatagramPacket datagrampacket = new DatagramPacket(abyte, abyte.length);

				try {
					socket.receive(datagrampacket);
				} catch (SocketTimeoutException sockettimeoutexception) {
					continue;
				} catch (IOException ioexception1) {
					LanServerDetector.logger.error("Couldn\'t ping server", ioexception1);
					break;
				}

				String s = new String(datagrampacket.getData(), datagrampacket.getOffset(), datagrampacket.getLength());
				LanServerDetector.logger.debug(datagrampacket.getAddress() + ": " + s);
				localServerList.func_77551_a(s, datagrampacket.getAddress());
			}

			try {
				socket.leaveGroup(broadcastAddress);
			} catch (IOException ioexception) {
				;
			}

			socket.close();
		}
	}
}