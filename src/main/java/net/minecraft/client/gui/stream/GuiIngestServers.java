package net.minecraft.client.gui.stream;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.stream.IngestServerTester;
import net.minecraft.util.EnumChatFormatting;
import tv.twitch.broadcast.IngestServer;

@SideOnly(Side.CLIENT)
public class GuiIngestServers extends GuiScreen {
	private final GuiScreen field_152309_a;
	private String field_152310_f;
	private GuiIngestServers.ServerList field_152311_g;
	private static final String __OBFID = "CL_00001843";

	public GuiIngestServers(GuiScreen p_i1077_1_) {
		field_152309_a = p_i1077_1_;
	}

	@Override
	public void initGui() {
		field_152310_f = I18n.format("options.stream.ingest.title");
		field_152311_g = new GuiIngestServers.ServerList();

		if (!mc.func_152346_Z().func_152908_z()) {
			mc.func_152346_Z().func_152909_x();
		}

		buttonList.add(
				new GuiButton(1, width / 2 - 155, height - 24 - 6, 150, 20, I18n.format("gui.done")));
		buttonList.add(new GuiButton(2, width / 2 + 5, height - 24 - 6, 150, 20,
				I18n.format("options.stream.ingest.reset")));
	}

	@Override
	public void onGuiClosed() {
		if (mc.func_152346_Z().func_152908_z()) {
			mc.func_152346_Z().func_152932_y().func_153039_l();
		}
	}

	@Override
	protected void actionPerformed(GuiButton p_146284_1_) {
		if (p_146284_1_.enabled) {
			if (p_146284_1_.id == 1) {
				mc.displayGuiScreen(field_152309_a);
			} else {
				mc.gameSettings.field_152407_Q = "";
				mc.gameSettings.saveOptions();
			}
		}
	}

	@Override
	public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
		drawDefaultBackground();
		field_152311_g.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
		drawCenteredString(fontRendererObj, field_152310_f, width / 2, 20, 16777215);
		super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
	}

	@SideOnly(Side.CLIENT)
	class ServerList extends GuiSlot {
		private static final String __OBFID = "CL_00001842";

		public ServerList() {
			super(GuiIngestServers.this.mc, GuiIngestServers.this.width, GuiIngestServers.this.height, 32,
					GuiIngestServers.this.height - 35,
					(int) (GuiIngestServers.this.mc.fontRenderer.FONT_HEIGHT * 3.5D));
			setShowSelectionBox(false);
		}

		@Override
		protected int getSize() {
			return GuiIngestServers.this.mc.func_152346_Z().func_152925_v().length;
		}

		@Override
		protected void elementClicked(int p_148144_1_, boolean p_148144_2_, int p_148144_3_, int p_148144_4_) {
			GuiIngestServers.this.mc.gameSettings.field_152407_Q = GuiIngestServers.this.mc.func_152346_Z()
					.func_152925_v()[p_148144_1_].serverUrl;
			GuiIngestServers.this.mc.gameSettings.saveOptions();
		}

		@Override
		protected boolean isSelected(int p_148131_1_) {
			return GuiIngestServers.this.mc.func_152346_Z().func_152925_v()[p_148131_1_].serverUrl
					.equals(GuiIngestServers.this.mc.gameSettings.field_152407_Q);
		}

		@Override
		protected void drawBackground() {
		}

		@Override
		protected void drawSlot(int p_148126_1_, int p_148126_2_, int p_148126_3_, int p_148126_4_,
				Tessellator p_148126_5_, int p_148126_6_, int p_148126_7_) {
			IngestServer ingestserver = GuiIngestServers.this.mc.func_152346_Z().func_152925_v()[p_148126_1_];
			String s = ingestserver.serverUrl.replaceAll("\\{stream_key\\}", "");
			String s1 = (int) ingestserver.bitrateKbps + " kbps";
			String s2 = null;
			IngestServerTester ingestservertester = GuiIngestServers.this.mc.func_152346_Z().func_152932_y();

			if (ingestservertester != null) {
				if (ingestserver == ingestservertester.func_153040_c()) {
					s = EnumChatFormatting.GREEN + s;
					s1 = (int) (ingestservertester.func_153030_h() * 100.0F) + "%";
				} else if (p_148126_1_ < ingestservertester.func_153028_p()) {
					if (ingestserver.bitrateKbps == 0.0F) {
						s1 = EnumChatFormatting.RED + "Down!";
					}
				} else {
					s1 = EnumChatFormatting.OBFUSCATED + "1234" + EnumChatFormatting.RESET + " kbps";
				}
			} else if (ingestserver.bitrateKbps == 0.0F) {
				s1 = EnumChatFormatting.RED + "Down!";
			}

			p_148126_2_ -= 15;

			if (isSelected(p_148126_1_)) {
				s2 = EnumChatFormatting.BLUE + "(Preferred)";
			} else if (ingestserver.defaultServer) {
				s2 = EnumChatFormatting.GREEN + "(Default)";
			}

			drawString(GuiIngestServers.this.fontRendererObj, ingestserver.serverName, p_148126_2_ + 2, p_148126_3_ + 5,
					16777215);
			drawString(GuiIngestServers.this.fontRendererObj, s, p_148126_2_ + 2,
					p_148126_3_ + GuiIngestServers.this.fontRendererObj.FONT_HEIGHT + 5 + 3, 3158064);
			drawString(GuiIngestServers.this.fontRendererObj, s1,
					getScrollBarX() - 5 - GuiIngestServers.this.fontRendererObj.getStringWidth(s1), p_148126_3_ + 5,
					8421504);

			if (s2 != null) {
				drawString(GuiIngestServers.this.fontRendererObj, s2,
						getScrollBarX() - 5 - GuiIngestServers.this.fontRendererObj.getStringWidth(s2),
						p_148126_3_ + 5 + 3 + GuiIngestServers.this.fontRendererObj.FONT_HEIGHT, 8421504);
			}
		}

		@Override
		protected int getScrollBarX() {
			return super.getScrollBarX() + 15;
		}
	}
}