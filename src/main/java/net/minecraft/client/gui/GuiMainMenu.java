package net.minecraft.client.gui;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import cpw.mods.fml.client.GuiModList;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.resources.I18n;
import net.minecraft.realms.RealmsBridge;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.demo.DemoWorldServer;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.client.ForgeHooksClient;
import org.apache.commons.io.Charsets;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.glu.Project;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.*;

@SideOnly(Side.CLIENT)
public class GuiMainMenu extends GuiScreen implements GuiYesNoCallback {
	private static final Logger logger = LogManager.getLogger();
	private static final Random rand = new Random();
	private float updateCounter;
	private String splashText;
	private GuiButton buttonResetDemo;
	private int panoramaTimer;
	private DynamicTexture viewportTexture;
	private final Object field_104025_t = new Object();
	private String field_92025_p;
	private String field_146972_A;
	private String field_104024_v;
	private static final ResourceLocation splashTexts = new ResourceLocation("texts/splashes.txt");
	private static final ResourceLocation minecraftTitleTextures = new ResourceLocation(
			"textures/gui/title/minecraft.png");
	private static final ResourceLocation[] titlePanoramaPaths = new ResourceLocation[] {
			new ResourceLocation("textures/gui/title/background/panorama_0.png"),
			new ResourceLocation("textures/gui/title/background/panorama_1.png"),
			new ResourceLocation("textures/gui/title/background/panorama_2.png"),
			new ResourceLocation("textures/gui/title/background/panorama_3.png"),
			new ResourceLocation("textures/gui/title/background/panorama_4.png"),
			new ResourceLocation("textures/gui/title/background/panorama_5.png") };
	public static final String field_96138_a = "Please click " + EnumChatFormatting.UNDERLINE + "here"
			+ EnumChatFormatting.RESET + " for more information.";
	private int field_92024_r;
	private int field_92023_s;
	private int field_92022_t;
	private int field_92021_u;
	private int field_92020_v;
	private int field_92019_w;
	private ResourceLocation field_110351_G;
	private static final String __OBFID = "CL_00001154";

	public GuiMainMenu() {
		field_146972_A = field_96138_a;
		splashText = "missingno";
		BufferedReader bufferedreader = null;

		try {
			ArrayList arraylist = new ArrayList();
			bufferedreader = new BufferedReader(new InputStreamReader(
					Minecraft.getMinecraft().getResourceManager().getResource(splashTexts).getInputStream(),
					Charsets.UTF_8));
			String s;

			while ((s = bufferedreader.readLine()) != null) {
				s = s.trim();

				if (!s.isEmpty()) {
					arraylist.add(s);
				}
			}

			if (!arraylist.isEmpty()) {
				do {
					splashText = (String) arraylist.get(rand.nextInt(arraylist.size()));
				} while (splashText.hashCode() == 125780783);
			}
		} catch (IOException ioexception1) {
			;
		} finally {
			if (bufferedreader != null) {
				try {
					bufferedreader.close();
				} catch (IOException ioexception) {
					;
				}
			}
		}

		updateCounter = rand.nextFloat();
		field_92025_p = "";

		if (!GLContext.getCapabilities().OpenGL20 && !OpenGlHelper.func_153193_b()) {
			field_92025_p = I18n.format("title.oldgl1", new Object[0]);
			field_146972_A = I18n.format("title.oldgl2", new Object[0]);
			field_104024_v = "https://help.mojang.com/customer/portal/articles/325948?ref=game";
		}
	}

	@Override
	public void updateScreen() {
		++panoramaTimer;
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	@Override
	protected void keyTyped(char p_73869_1_, int p_73869_2_) {
	}

	@Override
	public void initGui() {
		viewportTexture = new DynamicTexture(256, 256);
		field_110351_G = mc.getTextureManager().getDynamicTextureLocation("background", viewportTexture);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());

		if (calendar.get(2) + 1 == 11 && calendar.get(5) == 9) {
			splashText = "Happy birthday, ez!";
		} else if (calendar.get(2) + 1 == 6 && calendar.get(5) == 1) {
			splashText = "Happy birthday, Notch!";
		} else if (calendar.get(2) + 1 == 12 && calendar.get(5) == 24) {
			splashText = "Merry X-mas!";
		} else if (calendar.get(2) + 1 == 1 && calendar.get(5) == 1) {
			splashText = "Happy new year!";
		} else if (calendar.get(2) + 1 == 10 && calendar.get(5) == 31) {
			splashText = "OOoooOOOoooo! Spooky!";
		}

		int i = height / 4 + 48;

		if (mc.isDemo()) {
			addDemoButtons(i, 24);
		} else {
			addSingleplayerMultiplayerButtons(i, 24);
		}

		buttonList.add(
				new GuiButton(0, width / 2 - 100, i + 72 + 12, 98, 20, I18n.format("menu.options", new Object[0])));
		buttonList.add(new GuiButton(4, width / 2 + 2, i + 72 + 12, 98, 20, I18n.format("menu.quit", new Object[0])));
		buttonList.add(new GuiButtonLanguage(5, width / 2 - 124, i + 72 + 12));
		synchronized (field_104025_t) {
			field_92023_s = fontRendererObj.getStringWidth(field_92025_p);
			field_92024_r = fontRendererObj.getStringWidth(field_146972_A);
			int j = Math.max(field_92023_s, field_92024_r);
			field_92022_t = (width - j) / 2;
			field_92021_u = ((GuiButton) buttonList.get(0)).yPosition - 24;
			field_92020_v = field_92022_t + j;
			field_92019_w = field_92021_u + 24;
		}
	}

	private void addSingleplayerMultiplayerButtons(int p_73969_1_, int p_73969_2_) {
		buttonList.add(new GuiButton(1, width / 2 - 100, p_73969_1_, I18n.format("menu.singleplayer", new Object[0])));
		buttonList.add(new GuiButton(2, width / 2 - 100, p_73969_1_ + p_73969_2_ * 1,
				I18n.format("menu.multiplayer", new Object[0])));
		GuiButton realmsButton = new GuiButton(14, width / 2 - 100, p_73969_1_ + p_73969_2_ * 2,
				I18n.format("menu.online", new Object[0]));
		GuiButton fmlModButton = new GuiButton(6, width / 2 - 100, p_73969_1_ + p_73969_2_ * 2, "Mods");
		fmlModButton.xPosition = width / 2 + 2;
		realmsButton.width = 98;
		fmlModButton.width = 98;
		buttonList.add(realmsButton);
		buttonList.add(fmlModButton);
	}

	private void addDemoButtons(int p_73972_1_, int p_73972_2_) {
		buttonList.add(new GuiButton(11, width / 2 - 100, p_73972_1_, I18n.format("menu.playdemo", new Object[0])));
		buttonList.add(buttonResetDemo = new GuiButton(12, width / 2 - 100, p_73972_1_ + p_73972_2_ * 1,
				I18n.format("menu.resetdemo", new Object[0])));
		ISaveFormat isaveformat = mc.getSaveLoader();
		WorldInfo worldinfo = isaveformat.getWorldInfo("Demo_World");

		if (worldinfo == null) {
			buttonResetDemo.enabled = false;
		}
	}

	@Override
	protected void actionPerformed(GuiButton p_146284_1_) {
		if (p_146284_1_.id == 0) {
			mc.displayGuiScreen(new GuiOptions(this, mc.gameSettings));
		}

		if (p_146284_1_.id == 5) {
			mc.displayGuiScreen(new GuiLanguage(this, mc.gameSettings, mc.getLanguageManager()));
		}

		if (p_146284_1_.id == 1) {
			mc.displayGuiScreen(new GuiSelectWorld(this));
		}

		if (p_146284_1_.id == 2) {
			mc.displayGuiScreen(new GuiMultiplayer(this));
		}

		if (p_146284_1_.id == 14) {
			func_140005_i();
		}

		if (p_146284_1_.id == 4) {
			mc.shutdown();
		}

		if (p_146284_1_.id == 6) {
			mc.displayGuiScreen(new GuiModList(this));
		}

		if (p_146284_1_.id == 11) {
			mc.launchIntegratedServer("Demo_World", "Demo_World", DemoWorldServer.demoWorldSettings);
		}

		if (p_146284_1_.id == 12) {
			ISaveFormat isaveformat = mc.getSaveLoader();
			WorldInfo worldinfo = isaveformat.getWorldInfo("Demo_World");

			if (worldinfo != null) {
				GuiYesNo guiyesno = GuiSelectWorld.func_152129_a(this, worldinfo.getWorldName(), 12);
				mc.displayGuiScreen(guiyesno);
			}
		}
	}

	private void func_140005_i() {
		RealmsBridge realmsbridge = new RealmsBridge();
		realmsbridge.switchToRealms(this);
	}

	@Override
	public void confirmClicked(boolean p_73878_1_, int p_73878_2_) {
		if (p_73878_1_ && p_73878_2_ == 12) {
			ISaveFormat isaveformat = mc.getSaveLoader();
			isaveformat.flushCache();
			isaveformat.deleteWorldDirectory("Demo_World");
			mc.displayGuiScreen(this);
		} else if (p_73878_2_ == 13) {
			if (p_73878_1_) {
				try {
					Class oclass = Class.forName("java.awt.Desktop");
					Object object = oclass.getMethod("getDesktop", new Class[0]).invoke((Object) null, new Object[0]);
					oclass.getMethod("browse", new Class[] { URI.class }).invoke(object,
							new Object[] { new URI(field_104024_v) });
				} catch (Throwable throwable) {
					logger.error("Couldn\'t open link", throwable);
				}
			}

			mc.displayGuiScreen(this);
		}
	}

	private void drawPanorama(int p_73970_1_, int p_73970_2_, float p_73970_3_) {
		Tessellator tessellator = Tessellator.instance;
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glPushMatrix();
		GL11.glLoadIdentity();
		Project.gluPerspective(120.0F, 1.0F, 0.05F, 10.0F);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glPushMatrix();
		GL11.glLoadIdentity();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
		GL11.glRotatef(90.0F, 0.0F, 0.0F, 1.0F);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glDepthMask(false);
		OpenGlHelper.glBlendFunc(770, 771, 1, 0);
		byte b0 = 8;

		for (int k = 0; k < b0 * b0; ++k) {
			GL11.glPushMatrix();
			float f1 = ((float) (k % b0) / (float) b0 - 0.5F) / 64.0F;
			float f2 = ((float) (k / b0) / (float) b0 - 0.5F) / 64.0F;
			float f3 = 0.0F;
			GL11.glTranslatef(f1, f2, f3);
			GL11.glRotatef(MathHelper.sin((panoramaTimer + p_73970_3_) / 400.0F) * 25.0F + 20.0F, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef(-(panoramaTimer + p_73970_3_) * 0.1F, 0.0F, 1.0F, 0.0F);

			for (int l = 0; l < 6; ++l) {
				GL11.glPushMatrix();

				if (l == 1) {
					GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
				}

				if (l == 2) {
					GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
				}

				if (l == 3) {
					GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
				}

				if (l == 4) {
					GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
				}

				if (l == 5) {
					GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
				}

				mc.getTextureManager().bindTexture(titlePanoramaPaths[l]);
				tessellator.startDrawingQuads();
				tessellator.setColorRGBA_I(16777215, 255 / (k + 1));
				float f4 = 0.0F;
				tessellator.addVertexWithUV(-1.0D, -1.0D, 1.0D, 0.0F + f4, 0.0F + f4);
				tessellator.addVertexWithUV(1.0D, -1.0D, 1.0D, 1.0F - f4, 0.0F + f4);
				tessellator.addVertexWithUV(1.0D, 1.0D, 1.0D, 1.0F - f4, 1.0F - f4);
				tessellator.addVertexWithUV(-1.0D, 1.0D, 1.0D, 0.0F + f4, 1.0F - f4);
				tessellator.draw();
				GL11.glPopMatrix();
			}

			GL11.glPopMatrix();
			GL11.glColorMask(true, true, true, false);
		}

		tessellator.setTranslation(0.0D, 0.0D, 0.0D);
		GL11.glColorMask(true, true, true, true);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glPopMatrix();
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glPopMatrix();
		GL11.glDepthMask(true);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}

	private void rotateAndBlurSkybox(float p_73968_1_) {
		mc.getTextureManager().bindTexture(field_110351_G);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glCopyTexSubImage2D(GL11.GL_TEXTURE_2D, 0, 0, 0, 0, 0, 256, 256);
		GL11.glEnable(GL11.GL_BLEND);
		OpenGlHelper.glBlendFunc(770, 771, 1, 0);
		GL11.glColorMask(true, true, true, false);
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		byte b0 = 3;

		for (int i = 0; i < b0; ++i) {
			tessellator.setColorRGBA_F(1.0F, 1.0F, 1.0F, 1.0F / (i + 1));
			int j = width;
			int k = height;
			float f1 = (i - b0 / 2) / 256.0F;
			tessellator.addVertexWithUV(j, k, zLevel, 0.0F + f1, 1.0D);
			tessellator.addVertexWithUV(j, 0.0D, zLevel, 1.0F + f1, 1.0D);
			tessellator.addVertexWithUV(0.0D, 0.0D, zLevel, 1.0F + f1, 0.0D);
			tessellator.addVertexWithUV(0.0D, k, zLevel, 0.0F + f1, 0.0D);
		}

		tessellator.draw();
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glColorMask(true, true, true, true);
	}

	private void renderSkybox(int p_73971_1_, int p_73971_2_, float p_73971_3_) {
		mc.getFramebuffer().unbindFramebuffer();
		GL11.glViewport(0, 0, 256, 256);
		drawPanorama(p_73971_1_, p_73971_2_, p_73971_3_);
		rotateAndBlurSkybox(p_73971_3_);
		rotateAndBlurSkybox(p_73971_3_);
		rotateAndBlurSkybox(p_73971_3_);
		rotateAndBlurSkybox(p_73971_3_);
		rotateAndBlurSkybox(p_73971_3_);
		rotateAndBlurSkybox(p_73971_3_);
		rotateAndBlurSkybox(p_73971_3_);
		mc.getFramebuffer().bindFramebuffer(true);
		GL11.glViewport(0, 0, mc.displayWidth, mc.displayHeight);
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		float f1 = width > height ? 120.0F / width : 120.0F / height;
		float f2 = height * f1 / 256.0F;
		float f3 = width * f1 / 256.0F;
		tessellator.setColorRGBA_F(1.0F, 1.0F, 1.0F, 1.0F);
		int k = width;
		int l = height;
		tessellator.addVertexWithUV(0.0D, l, zLevel, 0.5F - f2, 0.5F + f3);
		tessellator.addVertexWithUV(k, l, zLevel, 0.5F - f2, 0.5F - f3);
		tessellator.addVertexWithUV(k, 0.0D, zLevel, 0.5F + f2, 0.5F - f3);
		tessellator.addVertexWithUV(0.0D, 0.0D, zLevel, 0.5F + f2, 0.5F + f3);
		tessellator.draw();
	}

	@Override
	public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		renderSkybox(p_73863_1_, p_73863_2_, p_73863_3_);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		Tessellator tessellator = Tessellator.instance;
		short short1 = 274;
		int k = width / 2 - short1 / 2;
		byte b0 = 30;
		drawGradientRect(0, 0, width, height, -2130706433, 16777215);
		drawGradientRect(0, 0, width, height, 0, Integer.MIN_VALUE);
		mc.getTextureManager().bindTexture(minecraftTitleTextures);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		if (updateCounter < 1.0E-4D) {
			drawTexturedModalRect(k + 0, b0 + 0, 0, 0, 99, 44);
			drawTexturedModalRect(k + 99, b0 + 0, 129, 0, 27, 44);
			drawTexturedModalRect(k + 99 + 26, b0 + 0, 126, 0, 3, 44);
			drawTexturedModalRect(k + 99 + 26 + 3, b0 + 0, 99, 0, 26, 44);
			drawTexturedModalRect(k + 155, b0 + 0, 0, 45, 155, 44);
		} else {
			drawTexturedModalRect(k + 0, b0 + 0, 0, 0, 155, 44);
			drawTexturedModalRect(k + 155, b0 + 0, 0, 45, 155, 44);
		}

		tessellator.setColorOpaque_I(-1);
		GL11.glPushMatrix();
		GL11.glTranslatef(width / 2 + 90, 70.0F, 0.0F);
		GL11.glRotatef(-20.0F, 0.0F, 0.0F, 1.0F);
		float f1 = 1.8F - MathHelper
				.abs(MathHelper.sin(Minecraft.getSystemTime() % 1000L / 1000.0F * (float) Math.PI * 2.0F) * 0.1F);
		f1 = f1 * 100.0F / (fontRendererObj.getStringWidth(splashText) + 32);
		GL11.glScalef(f1, f1, f1);
		drawCenteredString(fontRendererObj, splashText, 0, -8, -256);
		GL11.glPopMatrix();
		String s = "Minecraft 1.7.10";

		if (mc.isDemo()) {
			s = s + " Demo";
		}

		List<String> brandings = Lists.reverse(FMLCommonHandler.instance().getBrandings(true));
		for (int i = 0; i < brandings.size(); i++) {
			String brd = brandings.get(i);
			if (!Strings.isNullOrEmpty(brd)) {
				drawString(fontRendererObj, brd, 2, height - (10 + i * (fontRendererObj.FONT_HEIGHT + 1)), 16777215);
			}
		}
		ForgeHooksClient.renderMainMenu(this, fontRendererObj, width, height);
		String s1 = "Copyright Mojang AB. Do not distribute!";
		drawString(fontRendererObj, s1, width - fontRendererObj.getStringWidth(s1) - 2, height - 10, -1);

		if (field_92025_p != null && field_92025_p.length() > 0) {
			drawRect(field_92022_t - 2, field_92021_u - 2, field_92020_v + 2, field_92019_w - 1, 1428160512);
			drawString(fontRendererObj, field_92025_p, field_92022_t, field_92021_u, -1);
			drawString(fontRendererObj, field_146972_A, (width - field_92024_r) / 2,
					((GuiButton) buttonList.get(0)).yPosition - 12, -1);
		}

		super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
	}

	@Override
	protected void mouseClicked(int p_73864_1_, int p_73864_2_, int p_73864_3_) {
		super.mouseClicked(p_73864_1_, p_73864_2_, p_73864_3_);
		synchronized (field_104025_t) {
			if (field_92025_p.length() > 0 && p_73864_1_ >= field_92022_t && p_73864_1_ <= field_92020_v
					&& p_73864_2_ >= field_92021_u && p_73864_2_ <= field_92019_w) {
				GuiConfirmOpenLink guiconfirmopenlink = new GuiConfirmOpenLink(this, field_104024_v, 13, true);
				guiconfirmopenlink.func_146358_g();
				mc.displayGuiScreen(guiconfirmopenlink);
			}
		}
	}
}