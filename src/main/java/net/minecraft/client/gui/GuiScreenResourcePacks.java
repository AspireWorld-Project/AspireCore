package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.resources.*;
import net.minecraft.util.Util;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.Sys;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiScreenResourcePacks extends GuiScreen {
	private static final Logger logger = LogManager.getLogger();
	private final GuiScreen field_146965_f;
	@SuppressWarnings("rawtypes")
	private List field_146966_g;
	@SuppressWarnings("rawtypes")
	private List field_146969_h;
	private GuiResourcePackAvailable field_146970_i;
	private GuiResourcePackSelected field_146967_r;
	public GuiScreenResourcePacks(GuiScreen p_i45050_1_) {
		field_146965_f = p_i45050_1_;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void initGui() {
		buttonList.add(new GuiOptionButton(2, width / 2 - 154, height - 48,
				I18n.format("resourcePack.openFolder")));
		buttonList.add(new GuiOptionButton(1, width / 2 + 4, height - 48, I18n.format("gui.done")));
		field_146966_g = new ArrayList();
		field_146969_h = new ArrayList();
		ResourcePackRepository resourcepackrepository = mc.getResourcePackRepository();
		resourcepackrepository.updateRepositoryEntriesAll();
		ArrayList arraylist = Lists.newArrayList(resourcepackrepository.getRepositoryEntriesAll());
		arraylist.removeAll(resourcepackrepository.getRepositoryEntries());
		Iterator iterator = arraylist.iterator();
		ResourcePackRepository.Entry entry;

		while (iterator.hasNext()) {
			entry = (ResourcePackRepository.Entry) iterator.next();
			field_146966_g.add(new ResourcePackListEntryFound(this, entry));
		}

		iterator = Lists.reverse(resourcepackrepository.getRepositoryEntries()).iterator();

		while (iterator.hasNext()) {
			entry = (ResourcePackRepository.Entry) iterator.next();
			field_146969_h.add(new ResourcePackListEntryFound(this, entry));
		}

		field_146969_h.add(new ResourcePackListEntryDefault(this));
		field_146970_i = new GuiResourcePackAvailable(mc, 200, height, field_146966_g);
		field_146970_i.setSlotXBoundsFromLeft(width / 2 - 4 - 200);
		field_146970_i.registerScrollButtons(7, 8);
		field_146967_r = new GuiResourcePackSelected(mc, 200, height, field_146969_h);
		field_146967_r.setSlotXBoundsFromLeft(width / 2 + 4);
		field_146967_r.registerScrollButtons(7, 8);
	}

	public boolean func_146961_a(ResourcePackListEntry p_146961_1_) {
		return field_146969_h.contains(p_146961_1_);
	}

	@SuppressWarnings("rawtypes")
	public List func_146962_b(ResourcePackListEntry p_146962_1_) {
		return func_146961_a(p_146962_1_) ? field_146969_h : field_146966_g;
	}

	@SuppressWarnings("rawtypes")
	public List func_146964_g() {
		return field_146966_g;
	}

	@SuppressWarnings("rawtypes")
	public List func_146963_h() {
		return field_146969_h;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected void actionPerformed(GuiButton p_146284_1_) {
		if (p_146284_1_.enabled) {
			if (p_146284_1_.id == 2) {
				File file1 = mc.getResourcePackRepository().getDirResourcepacks();
				String s = file1.getAbsolutePath();

				if (Util.getOSType() == Util.EnumOS.OSX) {
					try {
						logger.info(s);
						Runtime.getRuntime().exec(new String[] { "/usr/bin/open", s });
						return;
					} catch (IOException ioexception1) {
						logger.error("Couldn't open file", ioexception1);
					}
				} else if (Util.getOSType() == Util.EnumOS.WINDOWS) {
					String s1 = String.format("cmd.exe /C start \"Open file\" \"%s\"", s);

					try {
						Runtime.getRuntime().exec(s1);
						return;
					} catch (IOException ioexception) {
						logger.error("Couldn't open file", ioexception);
					}
				}

				boolean flag = false;

				try {
					Class oclass = Class.forName("java.awt.Desktop");
					Object object = oclass.getMethod("getDesktop", new Class[0]).invoke(null);
					oclass.getMethod("browse", new Class[] { URI.class }).invoke(object,
							file1.toURI());
				} catch (Throwable throwable) {
					logger.error("Couldn't open link", throwable);
					flag = true;
				}

				if (flag) {
					logger.info("Opening via system class!");
					Sys.openURL("file://" + s);
				}
			} else if (p_146284_1_.id == 1) {
				ArrayList arraylist = Lists.newArrayList();
				Iterator iterator = field_146969_h.iterator();

				while (iterator.hasNext()) {
					ResourcePackListEntry resourcepacklistentry = (ResourcePackListEntry) iterator.next();

					if (resourcepacklistentry instanceof ResourcePackListEntryFound) {
						arraylist.add(((ResourcePackListEntryFound) resourcepacklistentry).func_148318_i());
					}
				}

				Collections.reverse(arraylist);
				mc.getResourcePackRepository().func_148527_a(arraylist);
				mc.gameSettings.resourcePacks.clear();
				iterator = arraylist.iterator();

				while (iterator.hasNext()) {
					ResourcePackRepository.Entry entry = (ResourcePackRepository.Entry) iterator.next();
					mc.gameSettings.resourcePacks.add(entry.getResourcePackName());
				}

				mc.gameSettings.saveOptions();
				mc.refreshResources();
				mc.displayGuiScreen(field_146965_f);
			}
		}
	}

	@Override
	protected void mouseClicked(int p_73864_1_, int p_73864_2_, int p_73864_3_) {
		super.mouseClicked(p_73864_1_, p_73864_2_, p_73864_3_);
		field_146970_i.func_148179_a(p_73864_1_, p_73864_2_, p_73864_3_);
		field_146967_r.func_148179_a(p_73864_1_, p_73864_2_, p_73864_3_);
	}

	@Override
	protected void mouseMovedOrUp(int p_146286_1_, int p_146286_2_, int p_146286_3_) {
		super.mouseMovedOrUp(p_146286_1_, p_146286_2_, p_146286_3_);
	}

	@Override
	public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
		drawBackground(0);
		field_146970_i.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
		field_146967_r.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
		drawCenteredString(fontRendererObj, I18n.format("resourcePack.title"), width / 2, 16, 16777215);
		drawCenteredString(fontRendererObj, I18n.format("resourcePack.folderInfo"), width / 2 - 77,
				height - 26, 8421504);
		super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
	}
}