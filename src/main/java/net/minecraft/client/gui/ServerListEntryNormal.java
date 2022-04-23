package net.minecraft.client.gui;

import com.google.common.base.Charsets;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.base64.Base64;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;

@SideOnly(Side.CLIENT)
public class ServerListEntryNormal implements GuiListExtended.IGuiListEntry {
	private static final Logger logger = LogManager.getLogger();
	private static final ThreadPoolExecutor field_148302_b = new ScheduledThreadPoolExecutor(5,
			new ThreadFactoryBuilder().setNameFormat("Server Pinger #%d").setDaemon(true).build());
	private final GuiMultiplayer field_148303_c;
	private final Minecraft field_148300_d;
	private final ServerData field_148301_e;
	private long field_148298_f;
	private String field_148299_g;
	private DynamicTexture field_148305_h;
	private final ResourceLocation field_148306_i;
	protected ServerListEntryNormal(GuiMultiplayer p_i45048_1_, ServerData p_i45048_2_) {
		field_148303_c = p_i45048_1_;
		field_148301_e = p_i45048_2_;
		field_148300_d = Minecraft.getMinecraft();
		field_148306_i = new ResourceLocation("servers/" + p_i45048_2_.serverIP + "/icon");
		field_148305_h = (DynamicTexture) field_148300_d.getTextureManager().getTexture(field_148306_i);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void drawEntry(int p_148279_1_, int p_148279_2_, int p_148279_3_, int p_148279_4_, int p_148279_5_,
			Tessellator p_148279_6_, int p_148279_7_, int p_148279_8_, boolean p_148279_9_) {
		if (!field_148301_e.field_78841_f) {
			field_148301_e.field_78841_f = true;
			field_148301_e.pingToServer = -2L;
			field_148301_e.serverMOTD = "";
			field_148301_e.populationInfo = "";
			field_148302_b.submit(new Runnable() {
				@Override
				public void run() {
					try {
						field_148303_c.func_146789_i().func_147224_a(field_148301_e);
					} catch (UnknownHostException unknownhostexception) {
						field_148301_e.pingToServer = -1L;
						field_148301_e.serverMOTD = EnumChatFormatting.DARK_RED + "Can't resolve hostname";
					} catch (Exception exception) {
						field_148301_e.pingToServer = -1L;
						field_148301_e.serverMOTD = EnumChatFormatting.DARK_RED + "Can't connect to server.";
					}
				}
			});
		}

		boolean flag1 = field_148301_e.field_82821_f > 5;
		boolean flag2 = field_148301_e.field_82821_f < 5;
		boolean flag3 = flag1 || flag2;
		field_148300_d.fontRenderer.drawString(field_148301_e.serverName, p_148279_2_ + 32 + 3, p_148279_3_ + 1,
				16777215);
		List list = field_148300_d.fontRenderer.listFormattedStringToWidth(
				FMLClientHandler.instance().fixDescription(field_148301_e.serverMOTD), p_148279_4_ - 48 - 2);

		for (int l1 = 0; l1 < Math.min(list.size(), 2); ++l1) {
			field_148300_d.fontRenderer.drawString((String) list.get(l1), p_148279_2_ + 32 + 3,
					p_148279_3_ + 12 + field_148300_d.fontRenderer.FONT_HEIGHT * l1, 8421504);
		}

		String s2 = flag3 ? EnumChatFormatting.DARK_RED + field_148301_e.gameVersion : field_148301_e.populationInfo;
		int i2 = field_148300_d.fontRenderer.getStringWidth(s2);
		field_148300_d.fontRenderer.drawString(s2, p_148279_2_ + p_148279_4_ - i2 - 15 - 2, p_148279_3_ + 1, 8421504);
		byte b0 = 0;
		String s = null;
		int j2;
		String s1;

		if (flag3) {
			j2 = 5;
			s1 = flag1 ? "Client out of date!" : "Server out of date!";
			s = field_148301_e.field_147412_i;
		} else if (field_148301_e.field_78841_f && field_148301_e.pingToServer != -2L) {
			if (field_148301_e.pingToServer < 0L) {
				j2 = 5;
			} else if (field_148301_e.pingToServer < 150L) {
				j2 = 0;
			} else if (field_148301_e.pingToServer < 300L) {
				j2 = 1;
			} else if (field_148301_e.pingToServer < 600L) {
				j2 = 2;
			} else if (field_148301_e.pingToServer < 1000L) {
				j2 = 3;
			} else {
				j2 = 4;
			}

			if (field_148301_e.pingToServer < 0L) {
				s1 = "(no connection)";
			} else {
				s1 = field_148301_e.pingToServer + "ms";
				s = field_148301_e.field_147412_i;
			}
		} else {
			b0 = 1;
			j2 = (int) (Minecraft.getSystemTime() / 100L + p_148279_1_ * 2 & 7L);

			if (j2 > 4) {
				j2 = 8 - j2;
			}

			s1 = "Pinging...";
		}

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		field_148300_d.getTextureManager().bindTexture(Gui.icons);
		Gui.func_146110_a(p_148279_2_ + p_148279_4_ - 15, p_148279_3_, b0 * 10, 176 + j2 * 8, 10, 8, 256.0F, 256.0F);

		if (field_148301_e.getBase64EncodedIconData() != null
				&& !field_148301_e.getBase64EncodedIconData().equals(field_148299_g)) {
			field_148299_g = field_148301_e.getBase64EncodedIconData();
			func_148297_b();
			field_148303_c.func_146795_p().saveServerList();
		}

		if (field_148305_h != null) {
			field_148300_d.getTextureManager().bindTexture(field_148306_i);
			Gui.func_146110_a(p_148279_2_, p_148279_3_, 0.0F, 0.0F, 32, 32, 32.0F, 32.0F);
		}

		int k2 = p_148279_7_ - p_148279_2_;
		int l2 = p_148279_8_ - p_148279_3_;

		String tooltip = FMLClientHandler.instance().enhanceServerListEntry(this, field_148301_e, p_148279_2_,
				p_148279_4_, p_148279_3_, k2, l2);
		if (tooltip != null) {
			field_148303_c.func_146793_a(tooltip);
		} else if (k2 >= p_148279_4_ - 15 && k2 <= p_148279_4_ - 5 && l2 >= 0 && l2 <= 8) {
			field_148303_c.func_146793_a(s1);
		} else if (k2 >= p_148279_4_ - i2 - 15 - 2 && k2 <= p_148279_4_ - 15 - 2 && l2 >= 0 && l2 <= 8) {
			field_148303_c.func_146793_a(s);
		}
	}

	private void func_148297_b() {
		if (field_148301_e.getBase64EncodedIconData() == null) {
			field_148300_d.getTextureManager().deleteTexture(field_148306_i);
			field_148305_h = null;
		} else {
			ByteBuf bytebuf = Unpooled.copiedBuffer(field_148301_e.getBase64EncodedIconData(), Charsets.UTF_8);
			ByteBuf bytebuf1 = Base64.decode(bytebuf);
			BufferedImage bufferedimage;
			label74: {
				try {
					bufferedimage = ImageIO.read(new ByteBufInputStream(bytebuf1));
					Validate.validState(bufferedimage.getWidth() == 64, "Must be 64 pixels wide");
					Validate.validState(bufferedimage.getHeight() == 64, "Must be 64 pixels high");
					break label74;
				} catch (Exception exception) {
					logger.error("Invalid icon for server " + field_148301_e.serverName + " (" + field_148301_e.serverIP
							+ ")", exception);
					field_148301_e.func_147407_a(null);
				} finally {
					bytebuf.release();
					bytebuf1.release();
				}

				return;
			}

			if (field_148305_h == null) {
				field_148305_h = new DynamicTexture(bufferedimage.getWidth(), bufferedimage.getHeight());
				field_148300_d.getTextureManager().loadTexture(field_148306_i, field_148305_h);
			}

			bufferedimage.getRGB(0, 0, bufferedimage.getWidth(), bufferedimage.getHeight(),
					field_148305_h.getTextureData(), 0, bufferedimage.getWidth());
			field_148305_h.updateDynamicTexture();
		}
	}

	@Override
	public boolean mousePressed(int p_148278_1_, int p_148278_2_, int p_148278_3_, int p_148278_4_, int p_148278_5_,
			int p_148278_6_) {
		field_148303_c.func_146790_a(p_148278_1_);

		if (Minecraft.getSystemTime() - field_148298_f < 250L) {
			field_148303_c.func_146796_h();
		}

		field_148298_f = Minecraft.getSystemTime();
		return false;
	}

	@Override
	public void mouseReleased(int p_148277_1_, int p_148277_2_, int p_148277_3_, int p_148277_4_, int p_148277_5_,
			int p_148277_6_) {
	}

	public ServerData func_148296_a() {
		return field_148301_e;
	}
}