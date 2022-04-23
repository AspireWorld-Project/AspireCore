package net.minecraft.client.gui;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.AnvilConverterException;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.SaveFormatComparator;
import net.minecraft.world.storage.WorldInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;

@SideOnly(Side.CLIENT)
public class GuiSelectWorld extends GuiScreen implements GuiYesNoCallback {
	private static final Logger logger = LogManager.getLogger();
	private final DateFormat field_146633_h = new SimpleDateFormat();
	protected GuiScreen field_146632_a;
	protected String field_146628_f = "Select world";
	private boolean field_146634_i;
	private int field_146640_r;
	private java.util.List field_146639_s;
	private GuiSelectWorld.List field_146638_t;
	private String field_146637_u;
	private String field_146636_v;
	private final String[] field_146635_w = new String[3];
	private boolean field_146643_x;
	private GuiButton field_146642_y;
	private GuiButton field_146641_z;
	private GuiButton field_146630_A;
	private GuiButton field_146631_B;
	private static final String __OBFID = "CL_00000711";

	public GuiSelectWorld(GuiScreen p_i1054_1_) {
		field_146632_a = p_i1054_1_;
	}

	@Override
	public void initGui() {
		field_146628_f = I18n.format("selectWorld.title");

		try {
			func_146627_h();
		} catch (AnvilConverterException anvilconverterexception) {
			logger.error("Couldn't load level list", anvilconverterexception);
			mc.displayGuiScreen(new GuiErrorScreen("Unable to load worlds", anvilconverterexception.getMessage()));
			return;
		}

		field_146637_u = I18n.format("selectWorld.world");
		field_146636_v = I18n.format("selectWorld.conversion");
		field_146635_w[WorldSettings.GameType.SURVIVAL.getID()] = I18n.format("gameMode.survival");
		field_146635_w[WorldSettings.GameType.CREATIVE.getID()] = I18n.format("gameMode.creative");
		field_146635_w[WorldSettings.GameType.ADVENTURE.getID()] = I18n.format("gameMode.adventure");
		field_146638_t = new GuiSelectWorld.List();
		field_146638_t.registerScrollButtons(4, 5);
		func_146618_g();
	}

	private void func_146627_h() throws AnvilConverterException {
		ISaveFormat isaveformat = mc.getSaveLoader();
		field_146639_s = isaveformat.getSaveList();
		Collections.sort(field_146639_s);
		field_146640_r = -1;
	}

	protected String func_146621_a(int p_146621_1_) {
		return ((SaveFormatComparator) field_146639_s.get(p_146621_1_)).getFileName();
	}

	protected String func_146614_d(int p_146614_1_) {
		String s = ((SaveFormatComparator) field_146639_s.get(p_146614_1_)).getDisplayName();

		if (s == null || MathHelper.stringNullOrLengthZero(s)) {
			s = I18n.format("selectWorld.world") + " " + (p_146614_1_ + 1);
		}

		return s;
	}

	public void func_146618_g() {
		buttonList.add(field_146641_z = new GuiButton(1, width / 2 - 154, height - 52, 150, 20,
				I18n.format("selectWorld.select")));
		buttonList.add(new GuiButton(3, width / 2 + 4, height - 52, 150, 20,
				I18n.format("selectWorld.create")));
		buttonList.add(field_146630_A = new GuiButton(6, width / 2 - 154, height - 28, 72, 20,
				I18n.format("selectWorld.rename")));
		buttonList.add(field_146642_y = new GuiButton(2, width / 2 - 76, height - 28, 72, 20,
				I18n.format("selectWorld.delete")));
		buttonList.add(field_146631_B = new GuiButton(7, width / 2 + 4, height - 28, 72, 20,
				I18n.format("selectWorld.recreate")));
		buttonList.add(new GuiButton(0, width / 2 + 82, height - 28, 72, 20, I18n.format("gui.cancel")));
		field_146641_z.enabled = false;
		field_146642_y.enabled = false;
		field_146630_A.enabled = false;
		field_146631_B.enabled = false;
	}

	@Override
	protected void actionPerformed(GuiButton p_146284_1_) {
		if (p_146284_1_.enabled) {
			if (p_146284_1_.id == 2) {
				String s = func_146614_d(field_146640_r);

				if (s != null) {
					field_146643_x = true;
					GuiYesNo guiyesno = func_152129_a(this, s, field_146640_r);
					mc.displayGuiScreen(guiyesno);
				}
			} else if (p_146284_1_.id == 1) {
				func_146615_e(field_146640_r);
			} else if (p_146284_1_.id == 3) {
				mc.displayGuiScreen(new GuiCreateWorld(this));
			} else if (p_146284_1_.id == 6) {
				mc.displayGuiScreen(new GuiRenameWorld(this, func_146621_a(field_146640_r)));
			} else if (p_146284_1_.id == 0) {
				mc.displayGuiScreen(field_146632_a);
			} else if (p_146284_1_.id == 7) {
				GuiCreateWorld guicreateworld = new GuiCreateWorld(this);
				ISaveHandler isavehandler = mc.getSaveLoader().getSaveLoader(func_146621_a(field_146640_r), false);
				WorldInfo worldinfo = isavehandler.loadWorldInfo();
				isavehandler.flush();
				guicreateworld.func_146318_a(worldinfo);
				mc.displayGuiScreen(guicreateworld);
			} else {
				field_146638_t.actionPerformed(p_146284_1_);
			}
		}
	}

	public void func_146615_e(int p_146615_1_) {
		mc.displayGuiScreen(null);

		if (!field_146634_i) {
			field_146634_i = true;
			String s = func_146621_a(p_146615_1_);

			if (s == null) {
				s = "World" + p_146615_1_;
			}

			String s1 = func_146614_d(p_146615_1_);

			if (s1 == null) {
				s1 = "World" + p_146615_1_;
			}

			if (mc.getSaveLoader().canLoadWorld(s)) {
				FMLClientHandler.instance().tryLoadExistingWorld(this, s, s1);
			}
		}
	}

	@Override
	public void confirmClicked(boolean p_73878_1_, int p_73878_2_) {
		if (field_146643_x) {
			field_146643_x = false;

			if (p_73878_1_) {
				ISaveFormat isaveformat = mc.getSaveLoader();
				isaveformat.flushCache();
				isaveformat.deleteWorldDirectory(func_146621_a(p_73878_2_));

				try {
					func_146627_h();
				} catch (AnvilConverterException anvilconverterexception) {
					logger.error("Couldn't load level list", anvilconverterexception);
				}
			}

			mc.displayGuiScreen(this);
		}
	}

	@Override
	public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
		field_146638_t.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
		drawCenteredString(fontRendererObj, field_146628_f, width / 2, 20, 16777215);
		super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
	}

	public static GuiYesNo func_152129_a(GuiYesNoCallback p_152129_0_, String p_152129_1_, int p_152129_2_) {
		String s1 = I18n.format("selectWorld.deleteQuestion");
		String s2 = "'" + p_152129_1_ + "' " + I18n.format("selectWorld.deleteWarning");
		String s3 = I18n.format("selectWorld.deleteButton");
		String s4 = I18n.format("gui.cancel");
		GuiYesNo guiyesno = new GuiYesNo(p_152129_0_, s1, s2, s3, s4, p_152129_2_);
		return guiyesno;
	}

	@SideOnly(Side.CLIENT)
	class List extends GuiSlot {
		private static final String __OBFID = "CL_00000712";

		public List() {
			super(GuiSelectWorld.this.mc, GuiSelectWorld.this.width, GuiSelectWorld.this.height, 32,
					GuiSelectWorld.this.height - 64, 36);
		}

		@Override
		protected int getSize() {
			return field_146639_s.size();
		}

		@Override
		protected void elementClicked(int p_148144_1_, boolean p_148144_2_, int p_148144_3_, int p_148144_4_) {
			field_146640_r = p_148144_1_;
			boolean flag1 = field_146640_r >= 0 && field_146640_r < getSize();
			field_146641_z.enabled = flag1;
			field_146642_y.enabled = flag1;
			field_146630_A.enabled = flag1;
			field_146631_B.enabled = flag1;

			if (p_148144_2_ && flag1) {
				func_146615_e(p_148144_1_);
			}
		}

		@Override
		protected boolean isSelected(int p_148131_1_) {
			return p_148131_1_ == field_146640_r;
		}

		@Override
		protected int getContentHeight() {
			return field_146639_s.size() * 36;
		}

		@Override
		protected void drawBackground() {
			drawDefaultBackground();
		}

		@Override
		protected void drawSlot(int p_148126_1_, int p_148126_2_, int p_148126_3_, int p_148126_4_,
				Tessellator p_148126_5_, int p_148126_6_, int p_148126_7_) {
			SaveFormatComparator saveformatcomparator = (SaveFormatComparator) field_146639_s.get(p_148126_1_);
			String s = saveformatcomparator.getDisplayName();

			if (s == null || MathHelper.stringNullOrLengthZero(s)) {
				s = field_146637_u + " " + (p_148126_1_ + 1);
			}

			String s1 = saveformatcomparator.getFileName();
			s1 = s1 + " (" + field_146633_h.format(new Date(saveformatcomparator.getLastTimePlayed()));
			s1 = s1 + ")";
			String s2 = "";

			if (saveformatcomparator.requiresConversion()) {
				s2 = field_146636_v + " " + s2;
			} else {
				s2 = field_146635_w[saveformatcomparator.getEnumGameType().getID()];

				if (saveformatcomparator.isHardcoreModeEnabled()) {
					s2 = EnumChatFormatting.DARK_RED + I18n.format("gameMode.hardcore", new Object[0])
							+ EnumChatFormatting.RESET;
				}

				if (saveformatcomparator.getCheatsEnabled()) {
					s2 = s2 + ", " + I18n.format("selectWorld.cheats");
				}
			}

			drawString(GuiSelectWorld.this.fontRendererObj, s, p_148126_2_ + 2, p_148126_3_ + 1, 16777215);
			drawString(GuiSelectWorld.this.fontRendererObj, s1, p_148126_2_ + 2, p_148126_3_ + 12, 8421504);
			drawString(GuiSelectWorld.this.fontRendererObj, s2, p_148126_2_ + 2, p_148126_3_ + 12 + 10, 8421504);
		}
	}
}