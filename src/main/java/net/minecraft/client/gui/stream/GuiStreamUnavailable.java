package net.minecraft.client.gui.stream;

import com.google.common.collect.Lists;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.stream.IStream;
import net.minecraft.client.stream.NullStream;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.Session;
import net.minecraft.util.Util;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import tv.twitch.ErrorCode;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiStreamUnavailable extends GuiScreen {
	private static final Logger field_152322_a = LogManager.getLogger();
	private final IChatComponent field_152324_f;
	private final GuiScreen field_152325_g;
	private final GuiStreamUnavailable.Reason field_152326_h;
	@SuppressWarnings("rawtypes")
	private final List field_152327_i;
	@SuppressWarnings("rawtypes")
	private final List field_152323_r;
	public GuiStreamUnavailable(GuiScreen p_i1070_1_, GuiStreamUnavailable.Reason p_i1070_2_) {
		this(p_i1070_1_, p_i1070_2_, null);
	}

	@SuppressWarnings("rawtypes")
	public GuiStreamUnavailable(GuiScreen p_i1071_1_, GuiStreamUnavailable.Reason p_i1071_2_, List p_i1071_3_) {
		field_152324_f = new ChatComponentTranslation("stream.unavailable.title");
		field_152323_r = Lists.newArrayList();
		field_152325_g = p_i1071_1_;
		field_152326_h = p_i1071_2_;
		field_152327_i = p_i1071_3_;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void initGui() {
		if (field_152323_r.isEmpty()) {
			field_152323_r.addAll(fontRendererObj.listFormattedStringToWidth(
					field_152326_h.func_152561_a().getFormattedText(), (int) (width * 0.75F)));

			if (field_152327_i != null) {
				field_152323_r.add("");
				Iterator iterator = field_152327_i.iterator();

				while (iterator.hasNext()) {
					ChatComponentTranslation chatcomponenttranslation = (ChatComponentTranslation) iterator.next();
					field_152323_r.add(chatcomponenttranslation.getUnformattedTextForChat());
				}
			}
		}

		if (field_152326_h.func_152559_b() != null) {
			buttonList.add(
					new GuiButton(0, width / 2 - 155, height - 50, 150, 20, I18n.format("gui.cancel")));
			buttonList.add(new GuiButton(1, width / 2 - 155 + 160, height - 50, 150, 20,
					I18n.format(field_152326_h.func_152559_b().getFormattedText())));
		} else {
			buttonList.add(
					new GuiButton(0, width / 2 - 75, height - 50, 150, 20, I18n.format("gui.cancel")));
		}
	}

	@Override
	public void onGuiClosed() {
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
		drawDefaultBackground();
		int k = Math.max((int) (height * 0.85D / 2.0D - field_152323_r.size() * fontRendererObj.FONT_HEIGHT / 2.0F),
				50);
		drawCenteredString(fontRendererObj, field_152324_f.getFormattedText(), width / 2,
				k - fontRendererObj.FONT_HEIGHT * 2, 16777215);

		for (Iterator iterator = field_152323_r.iterator(); iterator.hasNext(); k += fontRendererObj.FONT_HEIGHT) {
			String s = (String) iterator.next();
			drawCenteredString(fontRendererObj, s, width / 2, k, 10526880);
		}

		super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
	}

	@Override
	protected void actionPerformed(GuiButton p_146284_1_) {
		if (p_146284_1_.enabled) {
			if (p_146284_1_.id == 1) {
				switch (GuiStreamUnavailable.SwitchReason.field_152577_a[field_152326_h.ordinal()]) {
				case 1:
				case 2:
					func_152320_a("https://account.mojang.com/me/settings");
					break;
				case 3:
					func_152320_a("https://account.mojang.com/migrate");
					break;
				case 4:
					func_152320_a("http://www.apple.com/osx/");
					break;
				case 5:
				case 6:
				case 7:
					func_152320_a("http://bugs.mojang.com/browse/MC");
				}
			}

			mc.displayGuiScreen(field_152325_g);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void func_152320_a(String p_152320_1_) {
		try {
			Class oclass = Class.forName("java.awt.Desktop");
			Object object = oclass.getMethod("getDesktop", new Class[0]).invoke(null);
			oclass.getMethod("browse", new Class[] { URI.class }).invoke(object, new URI(p_152320_1_));
		} catch (Throwable throwable) {
			field_152322_a.error("Couldn't open link", throwable);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void func_152321_a(GuiScreen p_152321_0_) {
		Minecraft minecraft = Minecraft.getMinecraft();
		IStream istream = minecraft.func_152346_Z();

		if (!OpenGlHelper.framebufferSupported) {
			ArrayList arraylist = Lists.newArrayList();
			arraylist.add(new ChatComponentTranslation("stream.unavailable.no_fbo.version",
					GL11.glGetString(GL11.GL_VERSION)));
			arraylist.add(new ChatComponentTranslation("stream.unavailable.no_fbo.blend",
					Boolean.valueOf(GLContext.getCapabilities().GL_EXT_blend_func_separate)));
			arraylist.add(new ChatComponentTranslation("stream.unavailable.no_fbo.arb",
					Boolean.valueOf(GLContext.getCapabilities().GL_ARB_framebuffer_object)));
			arraylist.add(new ChatComponentTranslation("stream.unavailable.no_fbo.ext",
					Boolean.valueOf(GLContext.getCapabilities().GL_EXT_framebuffer_object)));
			minecraft.displayGuiScreen(
					new GuiStreamUnavailable(p_152321_0_, GuiStreamUnavailable.Reason.NO_FBO, arraylist));
		} else if (istream instanceof NullStream) {
			if (((NullStream) istream).func_152937_a().getMessage()
					.contains("Can't load AMD 64-bit .dll on a IA 32-bit platform")) {
				minecraft.displayGuiScreen(
						new GuiStreamUnavailable(p_152321_0_, GuiStreamUnavailable.Reason.LIBRARY_ARCH_MISMATCH));
			} else {
				minecraft.displayGuiScreen(
						new GuiStreamUnavailable(p_152321_0_, GuiStreamUnavailable.Reason.LIBRARY_FAILURE));
			}
		} else if (!istream.func_152928_D() && istream.func_152912_E() == ErrorCode.TTV_EC_OS_TOO_OLD) {
			switch (GuiStreamUnavailable.SwitchReason.field_152578_b[Util.getOSType().ordinal()]) {
			case 1:
				minecraft.displayGuiScreen(
						new GuiStreamUnavailable(p_152321_0_, GuiStreamUnavailable.Reason.UNSUPPORTED_OS_WINDOWS));
				break;
			case 2:
				minecraft.displayGuiScreen(
						new GuiStreamUnavailable(p_152321_0_, GuiStreamUnavailable.Reason.UNSUPPORTED_OS_MAC));
				break;
			default:
				minecraft.displayGuiScreen(
						new GuiStreamUnavailable(p_152321_0_, GuiStreamUnavailable.Reason.UNSUPPORTED_OS_OTHER));
			}
		} else if (!minecraft.func_152341_N().containsKey("twitch_access_token")) {
			if (minecraft.getSession().func_152428_f() == Session.Type.LEGACY) {
				minecraft.displayGuiScreen(
						new GuiStreamUnavailable(p_152321_0_, GuiStreamUnavailable.Reason.ACCOUNT_NOT_MIGRATED));
			} else {
				minecraft.displayGuiScreen(
						new GuiStreamUnavailable(p_152321_0_, GuiStreamUnavailable.Reason.ACCOUNT_NOT_BOUND));
			}
		} else if (!istream.func_152913_F()) {
			switch (GuiStreamUnavailable.SwitchReason.field_152579_c[istream.func_152918_H().ordinal()]) {
			case 1:
				minecraft.displayGuiScreen(
						new GuiStreamUnavailable(p_152321_0_, GuiStreamUnavailable.Reason.FAILED_TWITCH_AUTH));
				break;
			case 2:
			default:
				minecraft.displayGuiScreen(
						new GuiStreamUnavailable(p_152321_0_, GuiStreamUnavailable.Reason.FAILED_TWITCH_AUTH_ERROR));
			}
		} else if (istream.func_152912_E() != null) {
			List list = Arrays.asList(new ChatComponentTranslation("stream.unavailable.initialization_failure.extra",
					ErrorCode.getString(istream.func_152912_E())));
			minecraft.displayGuiScreen(
					new GuiStreamUnavailable(p_152321_0_, GuiStreamUnavailable.Reason.INITIALIZATION_FAILURE, list));
		} else {
			minecraft.displayGuiScreen(new GuiStreamUnavailable(p_152321_0_, GuiStreamUnavailable.Reason.UNKNOWN));
		}
	}

	@SideOnly(Side.CLIENT)
	public enum Reason {
		NO_FBO(new ChatComponentTranslation("stream.unavailable.no_fbo")), LIBRARY_ARCH_MISMATCH(
				new ChatComponentTranslation("stream.unavailable.library_arch_mismatch"
				)), LIBRARY_FAILURE(
								new ChatComponentTranslation("stream.unavailable.library_failure"),
								new ChatComponentTranslation("stream.unavailable.report_to_mojang"
								)), UNSUPPORTED_OS_WINDOWS(
												new ChatComponentTranslation("stream.unavailable.not_supported.windows"
												)), UNSUPPORTED_OS_MAC(
																new ChatComponentTranslation(
																		"stream.unavailable.not_supported.mac"
																),
																new ChatComponentTranslation(
																		"stream.unavailable.not_supported.mac.okay"
																)), UNSUPPORTED_OS_OTHER(
																				new ChatComponentTranslation(
																						"stream.unavailable.not_supported.other"
																				)), ACCOUNT_NOT_MIGRATED(
																								new ChatComponentTranslation(
																										"stream.unavailable.account_not_migrated"
																								),
																								new ChatComponentTranslation(
																										"stream.unavailable.account_not_migrated.okay"
																								)), ACCOUNT_NOT_BOUND(
																												new ChatComponentTranslation(
																														"stream.unavailable.account_not_bound"
																												),
																												new ChatComponentTranslation(
																														"stream.unavailable.account_not_bound.okay"
																												)), FAILED_TWITCH_AUTH(
																																new ChatComponentTranslation(
																																		"stream.unavailable.failed_auth"
																																),
																																new ChatComponentTranslation(
																																		"stream.unavailable.failed_auth.okay"
																																)), FAILED_TWITCH_AUTH_ERROR(
																																				new ChatComponentTranslation(
																																						"stream.unavailable.failed_auth_error"
																																				)), INITIALIZATION_FAILURE(
																																								new ChatComponentTranslation(
																																										"stream.unavailable.initialization_failure"
																																								),
																																								new ChatComponentTranslation(
																																										"stream.unavailable.report_to_mojang"
																																								)), UNKNOWN(
																																												new ChatComponentTranslation(
																																														"stream.unavailable.unknown"
																																												),
																																												new ChatComponentTranslation(
																																														"stream.unavailable.report_to_mojang"
																																												));
		private final IChatComponent field_152574_m;
		private final IChatComponent field_152575_n;

		Reason(IChatComponent p_i1066_3_) {
			this(p_i1066_3_, null);
		}

		Reason(IChatComponent p_i1067_3_, IChatComponent p_i1067_4_) {
			field_152574_m = p_i1067_3_;
			field_152575_n = p_i1067_4_;
		}

		public IChatComponent func_152561_a() {
			return field_152574_m;
		}

		public IChatComponent func_152559_b() {
			return field_152575_n;
		}
	}

	@SideOnly(Side.CLIENT)

	static final class SwitchReason {
		static final int[] field_152577_a;

		static final int[] field_152578_b;

		static final int[] field_152579_c = new int[IStream.AuthFailureReason.values().length];
		static {
			try {
				field_152579_c[IStream.AuthFailureReason.INVALID_TOKEN.ordinal()] = 1;
			} catch (NoSuchFieldError var11) {
			}

			try {
				field_152579_c[IStream.AuthFailureReason.ERROR.ordinal()] = 2;
			} catch (NoSuchFieldError var10) {
			}

			field_152578_b = new int[Util.EnumOS.values().length];

			try {
				field_152578_b[Util.EnumOS.WINDOWS.ordinal()] = 1;
			} catch (NoSuchFieldError var9) {
			}

			try {
				field_152578_b[Util.EnumOS.OSX.ordinal()] = 2;
			} catch (NoSuchFieldError var8) {
			}

			field_152577_a = new int[GuiStreamUnavailable.Reason.values().length];

			try {
				field_152577_a[GuiStreamUnavailable.Reason.ACCOUNT_NOT_BOUND.ordinal()] = 1;
			} catch (NoSuchFieldError var7) {
			}

			try {
				field_152577_a[GuiStreamUnavailable.Reason.FAILED_TWITCH_AUTH.ordinal()] = 2;
			} catch (NoSuchFieldError var6) {
			}

			try {
				field_152577_a[GuiStreamUnavailable.Reason.ACCOUNT_NOT_MIGRATED.ordinal()] = 3;
			} catch (NoSuchFieldError var5) {
			}

			try {
				field_152577_a[GuiStreamUnavailable.Reason.UNSUPPORTED_OS_MAC.ordinal()] = 4;
			} catch (NoSuchFieldError var4) {
			}

			try {
				field_152577_a[GuiStreamUnavailable.Reason.UNKNOWN.ordinal()] = 5;
			} catch (NoSuchFieldError var3) {
			}

			try {
				field_152577_a[GuiStreamUnavailable.Reason.LIBRARY_FAILURE.ordinal()] = 6;
			} catch (NoSuchFieldError var2) {
			}

			try {
				field_152577_a[GuiStreamUnavailable.Reason.INITIALIZATION_FAILURE.ordinal()] = 7;
			} catch (NoSuchFieldError var1) {
			}
		}
	}
}