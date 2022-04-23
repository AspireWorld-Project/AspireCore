package net.minecraft.client.multiplayer;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.util.concurrent.GenericFutureListener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiDisconnected;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.network.NetHandlerLoginClient;
import net.minecraft.client.resources.I18n;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.login.client.C00PacketLoginStart;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.atomic.AtomicInteger;

@SideOnly(Side.CLIENT)
public class GuiConnecting extends GuiScreen {
	private static final AtomicInteger field_146372_a = new AtomicInteger(0);
	private static final Logger logger = LogManager.getLogger();
	private NetworkManager field_146371_g;
	private boolean field_146373_h;
	private final GuiScreen field_146374_i;
	private static final String __OBFID = "CL_00000685";

	public GuiConnecting(GuiScreen p_i1181_1_, Minecraft p_i1181_2_, ServerData p_i1181_3_) {
		mc = p_i1181_2_;
		field_146374_i = p_i1181_1_;
		ServerAddress serveraddress = ServerAddress.func_78860_a(p_i1181_3_.serverIP);
		p_i1181_2_.loadWorld(null);
		p_i1181_2_.setServerData(p_i1181_3_);
		func_146367_a(serveraddress.getIP(), serveraddress.getPort());
	}

	public GuiConnecting(GuiScreen p_i1182_1_, Minecraft p_i1182_2_, String p_i1182_3_, int p_i1182_4_) {
		mc = p_i1182_2_;
		field_146374_i = p_i1182_1_;
		p_i1182_2_.loadWorld(null);
		func_146367_a(p_i1182_3_, p_i1182_4_);
	}

	private void func_146367_a(final String p_146367_1_, final int p_146367_2_) {
		logger.info("Connecting to " + p_146367_1_ + ", " + p_146367_2_);
		new Thread("Server Connector #" + field_146372_a.incrementAndGet()) {
			private static final String __OBFID = "CL_00000686";

			@Override
			public void run() {
				InetAddress inetaddress = null;

				try {
					if (field_146373_h)
						return;

					inetaddress = InetAddress.getByName(p_146367_1_);
					field_146371_g = NetworkManager.provideLanClient(inetaddress, p_146367_2_);
					field_146371_g.setNetHandler(
							new NetHandlerLoginClient(field_146371_g, GuiConnecting.this.mc, field_146374_i));
					field_146371_g.scheduleOutboundPacket(
							new C00Handshake(5, p_146367_1_, p_146367_2_, EnumConnectionState.LOGIN)
					);
					field_146371_g.scheduleOutboundPacket(
							new C00PacketLoginStart(GuiConnecting.this.mc.getSession().func_148256_e())
					);
				} catch (UnknownHostException unknownhostexception) {
					if (field_146373_h)
						return;

					GuiConnecting.logger.error("Couldn't connect to server", unknownhostexception);
					GuiConnecting.this.mc.displayGuiScreen(new GuiDisconnected(field_146374_i, "connect.failed",
							new ChatComponentTranslation("disconnect.genericReason", "Unknown host")));
				} catch (Exception exception) {
					if (field_146373_h)
						return;

					GuiConnecting.logger.error("Couldn't connect to server", exception);
					String s = exception.toString();

					if (inetaddress != null) {
						String s1 = inetaddress + ":" + p_146367_2_;
						s = s.replaceAll(s1, "");
					}

					GuiConnecting.this.mc.displayGuiScreen(new GuiDisconnected(field_146374_i, "connect.failed",
							new ChatComponentTranslation("disconnect.genericReason", s)));
				}
			}
		}.start();
	}

	@Override
	public void updateScreen() {
		if (field_146371_g != null) {
			if (field_146371_g.isChannelOpen()) {
				field_146371_g.processReceivedPackets();
			} else if (field_146371_g.getExitMessage() != null) {
				field_146371_g.getNetHandler().onDisconnect(field_146371_g.getExitMessage());
			}
		}
	}

	@Override
	protected void keyTyped(char p_73869_1_, int p_73869_2_) {
	}

	@Override
	public void initGui() {
		buttonList.clear();
		buttonList.add(new GuiButton(0, width / 2 - 100, height / 2 + 50, I18n.format("gui.cancel")));
	}

	@Override
	protected void actionPerformed(GuiButton p_146284_1_) {
		if (p_146284_1_.id == 0) {
			field_146373_h = true;

			if (field_146371_g != null) {
				field_146371_g.closeChannel(new ChatComponentText("Aborted"));
			}

			mc.displayGuiScreen(field_146374_i);
		}
	}

	@Override
	public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
		drawDefaultBackground();

		if (field_146371_g == null) {
			drawCenteredString(fontRendererObj, I18n.format("connect.connecting"), width / 2,
					height / 2 - 50, 16777215);
		} else {
			drawCenteredString(fontRendererObj, I18n.format("connect.authorizing"), width / 2,
					height / 2 - 50, 16777215);
		}

		super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
	}
}