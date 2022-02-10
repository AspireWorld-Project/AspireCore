package net.minecraft.client.multiplayer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ThreadLanServerPing extends Thread {
	private static final AtomicInteger field_148658_a = new AtomicInteger(0);
	private static final Logger logger = LogManager.getLogger();
	private final String motd;
	private final DatagramSocket socket;
	private boolean isStopping = true;
	private final String address;
	private static final String __OBFID = "CL_00001137";

	public ThreadLanServerPing(String p_i1321_1_, String p_i1321_2_) throws IOException {
		super("LanServerPinger #" + field_148658_a.incrementAndGet());
		motd = p_i1321_1_;
		address = p_i1321_2_;
		setDaemon(true);
		socket = new DatagramSocket();
	}

	@Override
	public void run() {
		String s = getPingResponse(motd, address);
		byte[] abyte = s.getBytes();

		while (!isInterrupted() && isStopping) {
			try {
				InetAddress inetaddress = InetAddress.getByName("224.0.2.60");
				DatagramPacket datagrampacket = new DatagramPacket(abyte, abyte.length, inetaddress, 4445);
				socket.send(datagrampacket);
			} catch (IOException ioexception) {
				logger.warn("LanServerPinger: " + ioexception.getMessage());
				break;
			}

			try {
				sleep(1500L);
			} catch (InterruptedException interruptedexception) {
				;
			}
		}
	}

	@Override
	public void interrupt() {
		super.interrupt();
		isStopping = false;
	}

	public static String getPingResponse(String p_77525_0_, String p_77525_1_) {
		return "[MOTD]" + p_77525_0_ + "[/MOTD][AD]" + p_77525_1_ + "[/AD]";
	}

	public static String getMotdFromPingResponse(String p_77524_0_) {
		int i = p_77524_0_.indexOf("[MOTD]");

		if (i < 0)
			return "missing no";
		else {
			int j = p_77524_0_.indexOf("[/MOTD]", i + "[MOTD]".length());
			return j < i ? "missing no" : p_77524_0_.substring(i + "[MOTD]".length(), j);
		}
	}

	public static String getAdFromPingResponse(String p_77523_0_) {
		int i = p_77523_0_.indexOf("[/MOTD]");

		if (i < 0)
			return null;
		else {
			int j = p_77523_0_.indexOf("[/MOTD]", i + "[/MOTD]".length());

			if (j >= 0)
				return null;
			else {
				int k = p_77523_0_.indexOf("[AD]", i + "[/MOTD]".length());

				if (k < 0)
					return null;
				else {
					int l = p_77523_0_.indexOf("[/AD]", k + "[AD]".length());
					return l < k ? null : p_77523_0_.substring(k + "[AD]".length(), l);
				}
			}
		}
	}
}