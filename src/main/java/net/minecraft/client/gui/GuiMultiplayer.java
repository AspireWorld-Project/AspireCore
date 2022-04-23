package net.minecraft.client.gui;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ServerList;
import net.minecraft.client.network.LanServerDetector;
import net.minecraft.client.network.OldServerPinger;
import net.minecraft.client.resources.I18n;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;

import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiMultiplayer extends GuiScreen implements GuiYesNoCallback {
	private static final Logger logger = LogManager.getLogger();
	private final OldServerPinger field_146797_f = new OldServerPinger();
	private GuiScreen field_146798_g;
	private ServerSelectionList field_146803_h;
	private ServerList field_146804_i;
	private GuiButton field_146810_r;
	private GuiButton field_146809_s;
	private GuiButton field_146808_t;
	private boolean field_146807_u;
	private boolean field_146806_v;
	private boolean field_146805_w;
	private boolean field_146813_x;
	private String field_146812_y;
	private ServerData field_146811_z;
	private LanServerDetector.LanServerList field_146799_A;
	private LanServerDetector.ThreadLanServerFind field_146800_B;
	private boolean field_146801_C;
	private static final String __OBFID = "CL_00000814";

	public GuiMultiplayer(GuiScreen p_i1040_1_) {
		field_146798_g = p_i1040_1_;
		FMLClientHandler.instance().setupServerList();
	}

	@Override
	public void initGui() {
		Keyboard.enableRepeatEvents(true);
		buttonList.clear();

		if (!field_146801_C) {
			field_146801_C = true;
			field_146804_i = new ServerList(mc);
			field_146804_i.loadServerList();
			field_146799_A = new LanServerDetector.LanServerList();

			try {
				field_146800_B = new LanServerDetector.ThreadLanServerFind(field_146799_A);
				field_146800_B.start();
			} catch (Exception exception) {
				logger.warn("Unable to start LAN server detection: " + exception.getMessage());
			}

			field_146803_h = new ServerSelectionList(this, mc, width, height, 32, height - 64, 36);
			field_146803_h.func_148195_a(field_146804_i);
		} else {
			field_146803_h.func_148122_a(width, height, 32, height - 64);
		}

		func_146794_g();
	}

	public void func_146794_g() {
		buttonList.add(field_146810_r = new GuiButton(7, width / 2 - 154, height - 28, 70, 20,
				I18n.format("selectServer.edit", new Object[0])));
		buttonList.add(field_146808_t = new GuiButton(2, width / 2 - 74, height - 28, 70, 20,
				I18n.format("selectServer.delete", new Object[0])));
		buttonList.add(field_146809_s = new GuiButton(1, width / 2 - 154, height - 52, 100, 20,
				I18n.format("selectServer.select", new Object[0])));
		buttonList.add(new GuiButton(4, width / 2 - 50, height - 52, 100, 20,
				I18n.format("selectServer.direct", new Object[0])));
		buttonList.add(new GuiButton(3, width / 2 + 4 + 50, height - 52, 100, 20,
				I18n.format("selectServer.add", new Object[0])));
		buttonList.add(new GuiButton(8, width / 2 + 4, height - 28, 70, 20,
				I18n.format("selectServer.refresh", new Object[0])));
		buttonList.add(
				new GuiButton(0, width / 2 + 4 + 76, height - 28, 75, 20, I18n.format("gui.cancel", new Object[0])));
		func_146790_a(field_146803_h.func_148193_k());
	}

	@Override
	public void updateScreen() {
		super.updateScreen();

		if (field_146799_A.getWasUpdated()) {
			List list = field_146799_A.getLanServers();
			field_146799_A.setWasNotUpdated();
			field_146803_h.func_148194_a(list);
		}

		field_146797_f.func_147223_a();
	}

	@Override
	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);

		if (field_146800_B != null) {
			field_146800_B.interrupt();
			field_146800_B = null;
		}

		field_146797_f.func_147226_b();
	}

	@Override
	protected void actionPerformed(GuiButton p_146284_1_) {
		if (p_146284_1_.enabled) {
			GuiListExtended.IGuiListEntry iguilistentry = field_146803_h.func_148193_k() < 0 ? null
					: field_146803_h.getListEntry(field_146803_h.func_148193_k());

			if (p_146284_1_.id == 2 && iguilistentry instanceof ServerListEntryNormal) {
				String s4 = ((ServerListEntryNormal) iguilistentry).func_148296_a().serverName;

				if (s4 != null) {
					field_146807_u = true;
					String s = I18n.format("selectServer.deleteQuestion", new Object[0]);
					String s1 = "\'" + s4 + "\' " + I18n.format("selectServer.deleteWarning", new Object[0]);
					String s2 = I18n.format("selectServer.deleteButton", new Object[0]);
					String s3 = I18n.format("gui.cancel", new Object[0]);
					GuiYesNo guiyesno = new GuiYesNo(this, s, s1, s2, s3, field_146803_h.func_148193_k());
					mc.displayGuiScreen(guiyesno);
				}
			} else if (p_146284_1_.id == 1) {
				func_146796_h();
			} else if (p_146284_1_.id == 4) {
				field_146813_x = true;
				mc.displayGuiScreen(new GuiScreenServerList(this,
						field_146811_z = new ServerData(I18n.format("selectServer.defaultName", new Object[0]), "")));
			} else if (p_146284_1_.id == 3) {
				field_146806_v = true;
				mc.displayGuiScreen(new GuiScreenAddServer(this,
						field_146811_z = new ServerData(I18n.format("selectServer.defaultName", new Object[0]), "")));
			} else if (p_146284_1_.id == 7 && iguilistentry instanceof ServerListEntryNormal) {
				field_146805_w = true;
				ServerData serverdata = ((ServerListEntryNormal) iguilistentry).func_148296_a();
				field_146811_z = new ServerData(serverdata.serverName, serverdata.serverIP);
				field_146811_z.func_152583_a(serverdata);
				mc.displayGuiScreen(new GuiScreenAddServer(this, field_146811_z));
			} else if (p_146284_1_.id == 0) {
				mc.displayGuiScreen(field_146798_g);
			} else if (p_146284_1_.id == 8) {
				func_146792_q();
			}
		}
	}

	private void func_146792_q() {
		mc.displayGuiScreen(new GuiMultiplayer(field_146798_g));
	}

	@Override
	public void confirmClicked(boolean p_73878_1_, int p_73878_2_) {
		GuiListExtended.IGuiListEntry iguilistentry = field_146803_h.func_148193_k() < 0 ? null
				: field_146803_h.getListEntry(field_146803_h.func_148193_k());

		if (field_146807_u) {
			field_146807_u = false;

			if (p_73878_1_ && iguilistentry instanceof ServerListEntryNormal) {
				field_146804_i.removeServerData(field_146803_h.func_148193_k());
				field_146804_i.saveServerList();
				field_146803_h.func_148192_c(-1);
				field_146803_h.func_148195_a(field_146804_i);
			}

			mc.displayGuiScreen(this);
		} else if (field_146813_x) {
			field_146813_x = false;

			if (p_73878_1_) {
				func_146791_a(field_146811_z);
			} else {
				mc.displayGuiScreen(this);
			}
		} else if (field_146806_v) {
			field_146806_v = false;

			if (p_73878_1_) {
				field_146804_i.addServerData(field_146811_z);
				field_146804_i.saveServerList();
				field_146803_h.func_148192_c(-1);
				field_146803_h.func_148195_a(field_146804_i);
			}

			mc.displayGuiScreen(this);
		} else if (field_146805_w) {
			field_146805_w = false;

			if (p_73878_1_ && iguilistentry instanceof ServerListEntryNormal) {
				ServerData serverdata = ((ServerListEntryNormal) iguilistentry).func_148296_a();
				serverdata.serverName = field_146811_z.serverName;
				serverdata.serverIP = field_146811_z.serverIP;
				serverdata.func_152583_a(field_146811_z);
				field_146804_i.saveServerList();
				field_146803_h.func_148195_a(field_146804_i);
			}

			mc.displayGuiScreen(this);
		}
	}

	@Override
	protected void keyTyped(char p_73869_1_, int p_73869_2_) {
		int j = field_146803_h.func_148193_k();
		GuiListExtended.IGuiListEntry iguilistentry = j < 0 ? null : field_146803_h.getListEntry(j);

		if (p_73869_2_ == 63) {
			func_146792_q();
		} else {
			if (j >= 0) {
				if (p_73869_2_ == 200) {
					if (isShiftKeyDown()) {
						if (j > 0 && iguilistentry instanceof ServerListEntryNormal) {
							field_146804_i.swapServers(j, j - 1);
							func_146790_a(field_146803_h.func_148193_k() - 1);
							field_146803_h.scrollBy(-field_146803_h.getSlotHeight());
							field_146803_h.func_148195_a(field_146804_i);
						}
					} else if (j > 0) {
						func_146790_a(field_146803_h.func_148193_k() - 1);
						field_146803_h.scrollBy(-field_146803_h.getSlotHeight());

						if (field_146803_h
								.getListEntry(field_146803_h.func_148193_k()) instanceof ServerListEntryLanScan) {
							if (field_146803_h.func_148193_k() > 0) {
								func_146790_a(field_146803_h.getSize() - 1);
								field_146803_h.scrollBy(-field_146803_h.getSlotHeight());
							} else {
								func_146790_a(-1);
							}
						}
					} else {
						func_146790_a(-1);
					}
				} else if (p_73869_2_ == 208) {
					if (isShiftKeyDown()) {
						if (j < field_146804_i.countServers() - 1) {
							field_146804_i.swapServers(j, j + 1);
							func_146790_a(j + 1);
							field_146803_h.scrollBy(field_146803_h.getSlotHeight());
							field_146803_h.func_148195_a(field_146804_i);
						}
					} else if (j < field_146803_h.getSize()) {
						func_146790_a(field_146803_h.func_148193_k() + 1);
						field_146803_h.scrollBy(field_146803_h.getSlotHeight());

						if (field_146803_h
								.getListEntry(field_146803_h.func_148193_k()) instanceof ServerListEntryLanScan) {
							if (field_146803_h.func_148193_k() < field_146803_h.getSize() - 1) {
								func_146790_a(field_146803_h.getSize() + 1);
								field_146803_h.scrollBy(field_146803_h.getSlotHeight());
							} else {
								func_146790_a(-1);
							}
						}
					} else {
						func_146790_a(-1);
					}
				} else if (p_73869_2_ != 28 && p_73869_2_ != 156) {
					super.keyTyped(p_73869_1_, p_73869_2_);
				} else {
					actionPerformed((GuiButton) buttonList.get(2));
				}
			} else {
				super.keyTyped(p_73869_1_, p_73869_2_);
			}
		}
	}

	@Override
	public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
		field_146812_y = null;
		drawDefaultBackground();
		field_146803_h.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
		drawCenteredString(fontRendererObj, I18n.format("multiplayer.title", new Object[0]), width / 2, 20, 16777215);
		super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);

		if (field_146812_y != null) {
			func_146283_a(Lists.newArrayList(Splitter.on("\n").split(field_146812_y)), p_73863_1_, p_73863_2_);
		}
	}

	public void func_146796_h() {
		GuiListExtended.IGuiListEntry iguilistentry = field_146803_h.func_148193_k() < 0 ? null
				: field_146803_h.getListEntry(field_146803_h.func_148193_k());

		if (iguilistentry instanceof ServerListEntryNormal) {
			func_146791_a(((ServerListEntryNormal) iguilistentry).func_148296_a());
		} else if (iguilistentry instanceof ServerListEntryLanDetected) {
			LanServerDetector.LanServer lanserver = ((ServerListEntryLanDetected) iguilistentry).func_148289_a();
			func_146791_a(new ServerData(lanserver.getServerMotd(), lanserver.getServerIpPort(), true));
		}
	}

	private void func_146791_a(ServerData p_146791_1_) {
		FMLClientHandler.instance().connectToServer(this, p_146791_1_);
	}

	public void func_146790_a(int p_146790_1_) {
		field_146803_h.func_148192_c(p_146790_1_);
		GuiListExtended.IGuiListEntry iguilistentry = p_146790_1_ < 0 ? null : field_146803_h.getListEntry(p_146790_1_);
		field_146809_s.enabled = false;
		field_146810_r.enabled = false;
		field_146808_t.enabled = false;

		if (iguilistentry != null && !(iguilistentry instanceof ServerListEntryLanScan)) {
			field_146809_s.enabled = true;

			if (iguilistentry instanceof ServerListEntryNormal) {
				field_146810_r.enabled = true;
				field_146808_t.enabled = true;
			}
		}
	}

	public OldServerPinger func_146789_i() {
		return field_146797_f;
	}

	public void func_146793_a(String p_146793_1_) {
		field_146812_y = p_146793_1_;
	}

	@Override
	protected void mouseClicked(int p_73864_1_, int p_73864_2_, int p_73864_3_) {
		super.mouseClicked(p_73864_1_, p_73864_2_, p_73864_3_);
		field_146803_h.func_148179_a(p_73864_1_, p_73864_2_, p_73864_3_);
	}

	@Override
	protected void mouseMovedOrUp(int p_146286_1_, int p_146286_2_, int p_146286_3_) {
		super.mouseMovedOrUp(p_146286_1_, p_146286_2_, p_146286_3_);
		field_146803_h.func_148181_b(p_146286_1_, p_146286_2_, p_146286_3_);
	}

	public ServerList func_146795_p() {
		return field_146804_i;
	}
}