package net.minecraft.network.rcon;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.ultramine.server.ConfigurationHandler;
import org.ultramine.server.internal.RConCommandRequest;
import org.ultramine.server.util.GlobalExecutors;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.SERVER)
public class RConThreadClient extends RConThreadBase {
	private static final Logger field_164005_h = LogManager.getLogger();
	private boolean loggedIn;
	private Socket clientSocket;
	private byte[] buffer = new byte[4096];
	private String rconPassword;
	private static final String __OBFID = "CL_00001804";

	RConThreadClient(IServer p_i1537_1_, Socket p_i1537_2_) {
		super(p_i1537_1_, "RCON Client");
		clientSocket = p_i1537_2_;

		List<String> whitelist = ConfigurationHandler.getServerConfig().listen.rcon.whitelist;
		if (whitelist != null && !whitelist.isEmpty()
				&& !whitelist.contains(p_i1537_2_.getInetAddress().toString().replace("/", ""))) {
			logWarning("Rcon connection from not whitelisted address: " + p_i1537_2_.getInetAddress());
			closeSocket();
			return;
		}

		try {
			clientSocket.setSoTimeout(0);
		} catch (Exception exception) {
			closeSocket();
		}

		rconPassword = ConfigurationHandler.getServerConfig().listen.rcon.password;
		logDebug("Rcon connection from: " + p_i1537_2_.getInetAddress());
	}

	@Override
	public synchronized void startThread() {
		if (clientSocket == null)
			return;
		super.startThread();
	}

	@Override
	public void run() {
		try {
			DataInputStream data = new DataInputStream(new BufferedInputStream(clientSocket.getInputStream()));
			while (true) {
				if (!running) {
					break;
				}

				int size = Integer.reverseBytes(data.readInt());
				data.readFully(buffer, 0, size);
				int i = size;

				if (10 > i)
					return;

				if (true) {
					int i1 = 0;
					int k = RConUtils.getBytesAsLEInt(buffer, i1, i);
					i1 += 4;
					int l = RConUtils.getRemainingBytesAsLEInt(buffer, i1);
					i1 += 4;

					switch (l) {
					case 2:
						if (loggedIn) {
							String s1 = RConUtils.getBytesAsString(buffer, i1, i);

							try {
								sendMultipacketResponse(k,
										GlobalExecutors.nextTick().await(new RConCommandRequest(s1)));
							} catch (Exception exception) {
								sendMultipacketResponse(k,
										"Error executing: " + s1 + " (" + exception.toString() + ")");
							}

							continue;
						}

						sendLoginFailedResponse();
						continue;
					case 3:
						String s = RConUtils.getBytesAsString(buffer, i1, i);
						s.length();

						if (0 != s.length() && s.equals(rconPassword)) {
							loggedIn = true;
							sendResponse(k, 2, "");
							continue;
						}

						loggedIn = false;
						sendLoginFailedResponse();
						continue;
					default:
						sendMultipacketResponse(k,
								String.format("Unknown request %s", new Object[] { Integer.toHexString(l) }));
						continue;
					}
				}
			}
		} catch (SocketTimeoutException sockettimeoutexception) {
			return;
		} catch (IOException ioexception) {
			return;
		} catch (Exception exception1) {
			field_164005_h.error("Exception whilst parsing RCON input", exception1);
			return;
		} finally {
			this.closeSocket();
		}
	}

	private void sendResponse(int p_72654_1_, int p_72654_2_, String p_72654_3_) throws IOException {
		ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream(1248);
		DataOutputStream dataoutputstream = new DataOutputStream(bytearrayoutputstream);
		byte[] abyte = p_72654_3_.getBytes("UTF-8");
		dataoutputstream.writeInt(Integer.reverseBytes(abyte.length + 10));
		dataoutputstream.writeInt(Integer.reverseBytes(p_72654_1_));
		dataoutputstream.writeInt(Integer.reverseBytes(p_72654_2_));
		dataoutputstream.write(abyte);
		dataoutputstream.write(0);
		dataoutputstream.write(0);
		clientSocket.getOutputStream().write(bytearrayoutputstream.toByteArray());
	}

	private void sendLoginFailedResponse() throws IOException {
		sendResponse(-1, 2, "");
	}

	private void sendMultipacketResponse(int p_72655_1_, String p_72655_2_) throws IOException {
		int j = p_72655_2_.length();

		do {
			int k = 4096 <= j ? 4096 : j;
			sendResponse(p_72655_1_, 0, p_72655_2_.substring(0, k));
			p_72655_2_ = p_72655_2_.substring(k);
			j = p_72655_2_.length();
		} while (0 != j);
	}

	private void closeSocket() {
		running = false;
		if (null != clientSocket) {
			try {
				clientSocket.close();
			} catch (IOException ioexception) {
				logWarning("IO: " + ioexception.getMessage());
			}

			clientSocket = null;
		}
	}
}
