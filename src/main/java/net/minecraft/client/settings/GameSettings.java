package net.minecraft.client.settings;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import com.google.common.collect.Maps;
import com.google.gson.Gson;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundCategory;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.stream.TwitchStream;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C15PacketClientSettings;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumDifficulty;

@SideOnly(Side.CLIENT)
public class GameSettings {
	private static final Logger logger = LogManager.getLogger();
	private static final Gson gson = new Gson();
	private static final ParameterizedType typeListString = new ParameterizedType() {
		private static final String __OBFID = "CL_00000651";

		@Override
		public Type[] getActualTypeArguments() {
			return new Type[] { String.class };
		}

		@Override
		public Type getRawType() {
			return List.class;
		}

		@Override
		public Type getOwnerType() {
			return null;
		}
	};
	private static final String[] GUISCALES = new String[] { "options.guiScale.auto", "options.guiScale.small",
			"options.guiScale.normal", "options.guiScale.large" };
	private static final String[] PARTICLES = new String[] { "options.particles.all", "options.particles.decreased",
			"options.particles.minimal" };
	private static final String[] AMBIENT_OCCLUSIONS = new String[] { "options.ao.off", "options.ao.min",
			"options.ao.max" };
	private static final String[] field_152391_aS = new String[] { "options.stream.compression.low",
			"options.stream.compression.medium", "options.stream.compression.high" };
	private static final String[] field_152392_aT = new String[] { "options.stream.chat.enabled.streaming",
			"options.stream.chat.enabled.always", "options.stream.chat.enabled.never" };
	private static final String[] field_152393_aU = new String[] { "options.stream.chat.userFilter.all",
			"options.stream.chat.userFilter.subs", "options.stream.chat.userFilter.mods" };
	private static final String[] field_152394_aV = new String[] { "options.stream.mic_toggle.mute",
			"options.stream.mic_toggle.talk" };
	public float mouseSensitivity = 0.5F;
	public boolean invertMouse;
	public int renderDistanceChunks = -1;
	public boolean viewBobbing = true;
	public boolean anaglyph;
	public boolean advancedOpengl;
	public boolean fboEnable = true;
	public int limitFramerate = 120;
	public boolean fancyGraphics = true;
	public int ambientOcclusion = 2;
	public boolean clouds = true;
	public List resourcePacks = new ArrayList();
	public EntityPlayer.EnumChatVisibility chatVisibility;
	public boolean chatColours;
	public boolean chatLinks;
	public boolean chatLinksPrompt;
	public float chatOpacity;
	public boolean snooperEnabled;
	public boolean fullScreen;
	public boolean enableVsync;
	public boolean hideServerAddress;
	public boolean advancedItemTooltips;
	public boolean pauseOnLostFocus;
	public boolean showCape;
	public boolean touchscreen;
	public int overrideWidth;
	public int overrideHeight;
	public boolean heldItemTooltips;
	public float chatScale;
	public float chatWidth;
	public float chatHeightUnfocused;
	public float chatHeightFocused;
	public boolean showInventoryAchievementHint;
	public int mipmapLevels;
	public int anisotropicFiltering;
	private Map mapSoundLevels;
	public float field_152400_J;
	public float field_152401_K;
	public float field_152402_L;
	public float field_152403_M;
	public float field_152404_N;
	public int field_152405_O;
	public boolean field_152406_P;
	public String field_152407_Q;
	public int field_152408_R;
	public int field_152409_S;
	public int field_152410_T;
	public KeyBinding keyBindForward;
	public KeyBinding keyBindLeft;
	public KeyBinding keyBindBack;
	public KeyBinding keyBindRight;
	public KeyBinding keyBindJump;
	public KeyBinding keyBindSneak;
	public KeyBinding keyBindInventory;
	public KeyBinding keyBindUseItem;
	public KeyBinding keyBindDrop;
	public KeyBinding keyBindAttack;
	public KeyBinding keyBindPickBlock;
	public KeyBinding keyBindSprint;
	public KeyBinding keyBindChat;
	public KeyBinding keyBindPlayerList;
	public KeyBinding keyBindCommand;
	public KeyBinding keyBindScreenshot;
	public KeyBinding keyBindTogglePerspective;
	public KeyBinding keyBindSmoothCamera;
	public KeyBinding field_152395_am;
	public KeyBinding field_152396_an;
	public KeyBinding field_152397_ao;
	public KeyBinding field_152398_ap;
	public KeyBinding field_152399_aq;
	public KeyBinding[] keyBindsHotbar;
	public KeyBinding[] keyBindings;
	protected Minecraft mc;
	private File optionsFile;
	public EnumDifficulty difficulty;
	public boolean hideGUI;
	public int thirdPersonView;
	public boolean showDebugInfo;
	public boolean showDebugProfilerChart;
	public String lastServer;
	public boolean noclip;
	public boolean smoothCamera;
	public boolean debugCamEnable;
	public float noclipRate;
	public float debugCamRate;
	public float fovSetting;
	public float gammaSetting;
	public float saturation;
	public int guiScale;
	public int particleSetting;
	public String language;
	public boolean forceUnicodeFont;
	private static final String __OBFID = "CL_00000650";

	public GameSettings(Minecraft p_i1016_1_, File p_i1016_2_) {
		chatVisibility = EntityPlayer.EnumChatVisibility.FULL;
		chatColours = true;
		chatLinks = true;
		chatLinksPrompt = true;
		chatOpacity = 1.0F;
		snooperEnabled = true;
		enableVsync = true;
		pauseOnLostFocus = true;
		showCape = true;
		heldItemTooltips = true;
		chatScale = 1.0F;
		chatWidth = 1.0F;
		chatHeightUnfocused = 0.44366196F;
		chatHeightFocused = 1.0F;
		showInventoryAchievementHint = true;
		mipmapLevels = 4;
		anisotropicFiltering = 1;
		mapSoundLevels = Maps.newEnumMap(SoundCategory.class);
		field_152400_J = 0.5F;
		field_152401_K = 1.0F;
		field_152402_L = 1.0F;
		field_152403_M = 0.5412844F;
		field_152404_N = 0.31690142F;
		field_152405_O = 1;
		field_152406_P = true;
		field_152407_Q = "";
		field_152408_R = 0;
		field_152409_S = 0;
		field_152410_T = 0;
		keyBindForward = new KeyBinding("key.forward", 17, "key.categories.movement");
		keyBindLeft = new KeyBinding("key.left", 30, "key.categories.movement");
		keyBindBack = new KeyBinding("key.back", 31, "key.categories.movement");
		keyBindRight = new KeyBinding("key.right", 32, "key.categories.movement");
		keyBindJump = new KeyBinding("key.jump", 57, "key.categories.movement");
		keyBindSneak = new KeyBinding("key.sneak", 42, "key.categories.movement");
		keyBindInventory = new KeyBinding("key.inventory", 18, "key.categories.inventory");
		keyBindUseItem = new KeyBinding("key.use", -99, "key.categories.gameplay");
		keyBindDrop = new KeyBinding("key.drop", 16, "key.categories.gameplay");
		keyBindAttack = new KeyBinding("key.attack", -100, "key.categories.gameplay");
		keyBindPickBlock = new KeyBinding("key.pickItem", -98, "key.categories.gameplay");
		keyBindSprint = new KeyBinding("key.sprint", 29, "key.categories.gameplay");
		keyBindChat = new KeyBinding("key.chat", 20, "key.categories.multiplayer");
		keyBindPlayerList = new KeyBinding("key.playerlist", 15, "key.categories.multiplayer");
		keyBindCommand = new KeyBinding("key.command", 53, "key.categories.multiplayer");
		keyBindScreenshot = new KeyBinding("key.screenshot", 60, "key.categories.misc");
		keyBindTogglePerspective = new KeyBinding("key.togglePerspective", 63, "key.categories.misc");
		keyBindSmoothCamera = new KeyBinding("key.smoothCamera", 0, "key.categories.misc");
		field_152395_am = new KeyBinding("key.fullscreen", 87, "key.categories.misc");
		field_152396_an = new KeyBinding("key.streamStartStop", 64, "key.categories.stream");
		field_152397_ao = new KeyBinding("key.streamPauseUnpause", 65, "key.categories.stream");
		field_152398_ap = new KeyBinding("key.streamCommercial", 0, "key.categories.stream");
		field_152399_aq = new KeyBinding("key.streamToggleMic", 0, "key.categories.stream");
		keyBindsHotbar = new KeyBinding[] { new KeyBinding("key.hotbar.1", 2, "key.categories.inventory"),
				new KeyBinding("key.hotbar.2", 3, "key.categories.inventory"),
				new KeyBinding("key.hotbar.3", 4, "key.categories.inventory"),
				new KeyBinding("key.hotbar.4", 5, "key.categories.inventory"),
				new KeyBinding("key.hotbar.5", 6, "key.categories.inventory"),
				new KeyBinding("key.hotbar.6", 7, "key.categories.inventory"),
				new KeyBinding("key.hotbar.7", 8, "key.categories.inventory"),
				new KeyBinding("key.hotbar.8", 9, "key.categories.inventory"),
				new KeyBinding("key.hotbar.9", 10, "key.categories.inventory") };
		keyBindings = ArrayUtils.addAll(new KeyBinding[] { keyBindAttack, keyBindUseItem, keyBindForward, keyBindLeft,
				keyBindBack, keyBindRight, keyBindJump, keyBindSneak, keyBindDrop, keyBindInventory, keyBindChat,
				keyBindPlayerList, keyBindPickBlock, keyBindCommand, keyBindScreenshot, keyBindTogglePerspective,
				keyBindSmoothCamera, keyBindSprint, field_152396_an, field_152397_ao, field_152398_ap, field_152399_aq,
				field_152395_am }, keyBindsHotbar);
		difficulty = EnumDifficulty.NORMAL;
		lastServer = "";
		noclipRate = 1.0F;
		debugCamRate = 1.0F;
		fovSetting = 70.0F;
		language = "en_US";
		forceUnicodeFont = false;
		mc = p_i1016_1_;
		optionsFile = new File(p_i1016_2_, "options.txt");
		GameSettings.Options.RENDER_DISTANCE.setValueMax(16.0F);
		renderDistanceChunks = p_i1016_1_.isJava64bit() ? 12 : 8;
		loadOptions();
	}

	public GameSettings() {
		chatVisibility = EntityPlayer.EnumChatVisibility.FULL;
		chatColours = true;
		chatLinks = true;
		chatLinksPrompt = true;
		chatOpacity = 1.0F;
		snooperEnabled = true;
		enableVsync = true;
		pauseOnLostFocus = true;
		showCape = true;
		heldItemTooltips = true;
		chatScale = 1.0F;
		chatWidth = 1.0F;
		chatHeightUnfocused = 0.44366196F;
		chatHeightFocused = 1.0F;
		showInventoryAchievementHint = true;
		mipmapLevels = 4;
		anisotropicFiltering = 1;
		mapSoundLevels = Maps.newEnumMap(SoundCategory.class);
		field_152400_J = 0.5F;
		field_152401_K = 1.0F;
		field_152402_L = 1.0F;
		field_152403_M = 0.5412844F;
		field_152404_N = 0.31690142F;
		field_152405_O = 1;
		field_152406_P = true;
		field_152407_Q = "";
		field_152408_R = 0;
		field_152409_S = 0;
		field_152410_T = 0;
		keyBindForward = new KeyBinding("key.forward", 17, "key.categories.movement");
		keyBindLeft = new KeyBinding("key.left", 30, "key.categories.movement");
		keyBindBack = new KeyBinding("key.back", 31, "key.categories.movement");
		keyBindRight = new KeyBinding("key.right", 32, "key.categories.movement");
		keyBindJump = new KeyBinding("key.jump", 57, "key.categories.movement");
		keyBindSneak = new KeyBinding("key.sneak", 42, "key.categories.movement");
		keyBindInventory = new KeyBinding("key.inventory", 18, "key.categories.inventory");
		keyBindUseItem = new KeyBinding("key.use", -99, "key.categories.gameplay");
		keyBindDrop = new KeyBinding("key.drop", 16, "key.categories.gameplay");
		keyBindAttack = new KeyBinding("key.attack", -100, "key.categories.gameplay");
		keyBindPickBlock = new KeyBinding("key.pickItem", -98, "key.categories.gameplay");
		keyBindSprint = new KeyBinding("key.sprint", 29, "key.categories.gameplay");
		keyBindChat = new KeyBinding("key.chat", 20, "key.categories.multiplayer");
		keyBindPlayerList = new KeyBinding("key.playerlist", 15, "key.categories.multiplayer");
		keyBindCommand = new KeyBinding("key.command", 53, "key.categories.multiplayer");
		keyBindScreenshot = new KeyBinding("key.screenshot", 60, "key.categories.misc");
		keyBindTogglePerspective = new KeyBinding("key.togglePerspective", 63, "key.categories.misc");
		keyBindSmoothCamera = new KeyBinding("key.smoothCamera", 0, "key.categories.misc");
		field_152395_am = new KeyBinding("key.fullscreen", 87, "key.categories.misc");
		field_152396_an = new KeyBinding("key.streamStartStop", 64, "key.categories.stream");
		field_152397_ao = new KeyBinding("key.streamPauseUnpause", 65, "key.categories.stream");
		field_152398_ap = new KeyBinding("key.streamCommercial", 0, "key.categories.stream");
		field_152399_aq = new KeyBinding("key.streamToggleMic", 0, "key.categories.stream");
		keyBindsHotbar = new KeyBinding[] { new KeyBinding("key.hotbar.1", 2, "key.categories.inventory"),
				new KeyBinding("key.hotbar.2", 3, "key.categories.inventory"),
				new KeyBinding("key.hotbar.3", 4, "key.categories.inventory"),
				new KeyBinding("key.hotbar.4", 5, "key.categories.inventory"),
				new KeyBinding("key.hotbar.5", 6, "key.categories.inventory"),
				new KeyBinding("key.hotbar.6", 7, "key.categories.inventory"),
				new KeyBinding("key.hotbar.7", 8, "key.categories.inventory"),
				new KeyBinding("key.hotbar.8", 9, "key.categories.inventory"),
				new KeyBinding("key.hotbar.9", 10, "key.categories.inventory") };
		keyBindings = ArrayUtils.addAll(new KeyBinding[] { keyBindAttack, keyBindUseItem, keyBindForward, keyBindLeft,
				keyBindBack, keyBindRight, keyBindJump, keyBindSneak, keyBindDrop, keyBindInventory, keyBindChat,
				keyBindPlayerList, keyBindPickBlock, keyBindCommand, keyBindScreenshot, keyBindTogglePerspective,
				keyBindSmoothCamera, keyBindSprint, field_152396_an, field_152397_ao, field_152398_ap, field_152399_aq,
				field_152395_am }, keyBindsHotbar);
		difficulty = EnumDifficulty.NORMAL;
		lastServer = "";
		noclipRate = 1.0F;
		debugCamRate = 1.0F;
		fovSetting = 70.0F;
		language = "en_US";
		forceUnicodeFont = false;
	}

	public static String getKeyDisplayString(int p_74298_0_) {
		return p_74298_0_ < 0 ? I18n.format("key.mouseButton", new Object[] { Integer.valueOf(p_74298_0_ + 101) })
				: Keyboard.getKeyName(p_74298_0_);
	}

	public static boolean isKeyDown(KeyBinding p_100015_0_) {
		return p_100015_0_.getKeyCode() == 0 ? false
				: p_100015_0_.getKeyCode() < 0 ? Mouse.isButtonDown(p_100015_0_.getKeyCode() + 100)
						: Keyboard.isKeyDown(p_100015_0_.getKeyCode());
	}

	public void setOptionKeyBinding(KeyBinding p_151440_1_, int p_151440_2_) {
		p_151440_1_.setKeyCode(p_151440_2_);
		saveOptions();
	}

	public void setOptionFloatValue(GameSettings.Options p_74304_1_, float p_74304_2_) {
		if (p_74304_1_ == GameSettings.Options.SENSITIVITY) {
			mouseSensitivity = p_74304_2_;
		}

		if (p_74304_1_ == GameSettings.Options.FOV) {
			fovSetting = p_74304_2_;
		}

		if (p_74304_1_ == GameSettings.Options.GAMMA) {
			gammaSetting = p_74304_2_;
		}

		if (p_74304_1_ == GameSettings.Options.FRAMERATE_LIMIT) {
			limitFramerate = (int) p_74304_2_;
		}

		if (p_74304_1_ == GameSettings.Options.CHAT_OPACITY) {
			chatOpacity = p_74304_2_;
			mc.ingameGUI.getChatGUI().refreshChat();
		}

		if (p_74304_1_ == GameSettings.Options.CHAT_HEIGHT_FOCUSED) {
			chatHeightFocused = p_74304_2_;
			mc.ingameGUI.getChatGUI().refreshChat();
		}

		if (p_74304_1_ == GameSettings.Options.CHAT_HEIGHT_UNFOCUSED) {
			chatHeightUnfocused = p_74304_2_;
			mc.ingameGUI.getChatGUI().refreshChat();
		}

		if (p_74304_1_ == GameSettings.Options.CHAT_WIDTH) {
			chatWidth = p_74304_2_;
			mc.ingameGUI.getChatGUI().refreshChat();
		}

		if (p_74304_1_ == GameSettings.Options.CHAT_SCALE) {
			chatScale = p_74304_2_;
			mc.ingameGUI.getChatGUI().refreshChat();
		}

		int i;

		if (p_74304_1_ == GameSettings.Options.ANISOTROPIC_FILTERING) {
			i = anisotropicFiltering;
			anisotropicFiltering = (int) p_74304_2_;

			if (i != p_74304_2_) {
				mc.getTextureMapBlocks().setAnisotropicFiltering(anisotropicFiltering);
				mc.scheduleResourcesRefresh();
			}
		}

		if (p_74304_1_ == GameSettings.Options.MIPMAP_LEVELS) {
			i = mipmapLevels;
			mipmapLevels = (int) p_74304_2_;

			if (i != p_74304_2_) {
				mc.getTextureMapBlocks().setMipmapLevels(mipmapLevels);
				mc.scheduleResourcesRefresh();
			}
		}

		if (p_74304_1_ == GameSettings.Options.RENDER_DISTANCE) {
			renderDistanceChunks = (int) p_74304_2_;
		}

		if (p_74304_1_ == GameSettings.Options.STREAM_BYTES_PER_PIXEL) {
			field_152400_J = p_74304_2_;
		}

		if (p_74304_1_ == GameSettings.Options.STREAM_VOLUME_MIC) {
			field_152401_K = p_74304_2_;
			mc.func_152346_Z().func_152915_s();
		}

		if (p_74304_1_ == GameSettings.Options.STREAM_VOLUME_SYSTEM) {
			field_152402_L = p_74304_2_;
			mc.func_152346_Z().func_152915_s();
		}

		if (p_74304_1_ == GameSettings.Options.STREAM_KBPS) {
			field_152403_M = p_74304_2_;
		}

		if (p_74304_1_ == GameSettings.Options.STREAM_FPS) {
			field_152404_N = p_74304_2_;
		}
	}

	public void setOptionValue(GameSettings.Options p_74306_1_, int p_74306_2_) {
		if (p_74306_1_ == GameSettings.Options.INVERT_MOUSE) {
			invertMouse = !invertMouse;
		}

		if (p_74306_1_ == GameSettings.Options.GUI_SCALE) {
			guiScale = guiScale + p_74306_2_ & 3;
		}

		if (p_74306_1_ == GameSettings.Options.PARTICLES) {
			particleSetting = (particleSetting + p_74306_2_) % 3;
		}

		if (p_74306_1_ == GameSettings.Options.VIEW_BOBBING) {
			viewBobbing = !viewBobbing;
		}

		if (p_74306_1_ == GameSettings.Options.RENDER_CLOUDS) {
			clouds = !clouds;
		}

		if (p_74306_1_ == GameSettings.Options.FORCE_UNICODE_FONT) {
			forceUnicodeFont = !forceUnicodeFont;
			mc.fontRenderer.setUnicodeFlag(mc.getLanguageManager().isCurrentLocaleUnicode() || forceUnicodeFont);
		}

		if (p_74306_1_ == GameSettings.Options.ADVANCED_OPENGL) {
			advancedOpengl = !advancedOpengl;
			mc.renderGlobal.loadRenderers();
		}

		if (p_74306_1_ == GameSettings.Options.FBO_ENABLE) {
			fboEnable = !fboEnable;
		}

		if (p_74306_1_ == GameSettings.Options.ANAGLYPH) {
			anaglyph = !anaglyph;
			mc.refreshResources();
		}

		if (p_74306_1_ == GameSettings.Options.DIFFICULTY) {
			difficulty = EnumDifficulty.getDifficultyEnum(difficulty.getDifficultyId() + p_74306_2_ & 3);
		}

		if (p_74306_1_ == GameSettings.Options.GRAPHICS) {
			fancyGraphics = !fancyGraphics;
			mc.renderGlobal.loadRenderers();
		}

		if (p_74306_1_ == GameSettings.Options.AMBIENT_OCCLUSION) {
			ambientOcclusion = (ambientOcclusion + p_74306_2_) % 3;
			mc.renderGlobal.loadRenderers();
		}

		if (p_74306_1_ == GameSettings.Options.CHAT_VISIBILITY) {
			chatVisibility = EntityPlayer.EnumChatVisibility
					.getEnumChatVisibility((chatVisibility.getChatVisibility() + p_74306_2_) % 3);
		}

		if (p_74306_1_ == GameSettings.Options.STREAM_COMPRESSION) {
			field_152405_O = (field_152405_O + p_74306_2_) % 3;
		}

		if (p_74306_1_ == GameSettings.Options.STREAM_SEND_METADATA) {
			field_152406_P = !field_152406_P;
		}

		if (p_74306_1_ == GameSettings.Options.STREAM_CHAT_ENABLED) {
			field_152408_R = (field_152408_R + p_74306_2_) % 3;
		}

		if (p_74306_1_ == GameSettings.Options.STREAM_CHAT_USER_FILTER) {
			field_152409_S = (field_152409_S + p_74306_2_) % 3;
		}

		if (p_74306_1_ == GameSettings.Options.STREAM_MIC_TOGGLE_BEHAVIOR) {
			field_152410_T = (field_152410_T + p_74306_2_) % 2;
		}

		if (p_74306_1_ == GameSettings.Options.CHAT_COLOR) {
			chatColours = !chatColours;
		}

		if (p_74306_1_ == GameSettings.Options.CHAT_LINKS) {
			chatLinks = !chatLinks;
		}

		if (p_74306_1_ == GameSettings.Options.CHAT_LINKS_PROMPT) {
			chatLinksPrompt = !chatLinksPrompt;
		}

		if (p_74306_1_ == GameSettings.Options.SNOOPER_ENABLED) {
			snooperEnabled = !snooperEnabled;
		}

		if (p_74306_1_ == GameSettings.Options.SHOW_CAPE) {
			showCape = !showCape;
		}

		if (p_74306_1_ == GameSettings.Options.TOUCHSCREEN) {
			touchscreen = !touchscreen;
		}

		if (p_74306_1_ == GameSettings.Options.USE_FULLSCREEN) {
			fullScreen = !fullScreen;

			if (mc.isFullScreen() != fullScreen) {
				mc.toggleFullscreen();
			}
		}

		if (p_74306_1_ == GameSettings.Options.ENABLE_VSYNC) {
			enableVsync = !enableVsync;
			Display.setVSyncEnabled(enableVsync);
		}

		saveOptions();
	}

	public float getOptionFloatValue(GameSettings.Options p_74296_1_) {
		return p_74296_1_ == GameSettings.Options.FOV ? fovSetting
				: p_74296_1_ == GameSettings.Options.GAMMA ? gammaSetting
						: p_74296_1_ == GameSettings.Options.SATURATION ? saturation
								: p_74296_1_ == GameSettings.Options.SENSITIVITY ? mouseSensitivity
										: p_74296_1_ == GameSettings.Options.CHAT_OPACITY ? chatOpacity
												: p_74296_1_ == GameSettings.Options.CHAT_HEIGHT_FOCUSED
														? chatHeightFocused
														: p_74296_1_ == GameSettings.Options.CHAT_HEIGHT_UNFOCUSED
																? chatHeightUnfocused
																: p_74296_1_ == GameSettings.Options.CHAT_SCALE
																		? chatScale
																		: p_74296_1_ == GameSettings.Options.CHAT_WIDTH
																				? chatWidth
																				: p_74296_1_ == GameSettings.Options.FRAMERATE_LIMIT
																						? (float) limitFramerate
																						: p_74296_1_ == GameSettings.Options.ANISOTROPIC_FILTERING
																								? (float) anisotropicFiltering
																								: p_74296_1_ == GameSettings.Options.MIPMAP_LEVELS
																										? (float) mipmapLevels
																										: p_74296_1_ == GameSettings.Options.RENDER_DISTANCE
																												? (float) renderDistanceChunks
																												: p_74296_1_ == GameSettings.Options.STREAM_BYTES_PER_PIXEL
																														? field_152400_J
																														: p_74296_1_ == GameSettings.Options.STREAM_VOLUME_MIC
																																? field_152401_K
																																: p_74296_1_ == GameSettings.Options.STREAM_VOLUME_SYSTEM
																																		? field_152402_L
																																		: p_74296_1_ == GameSettings.Options.STREAM_KBPS
																																				? field_152403_M
																																				: p_74296_1_ == GameSettings.Options.STREAM_FPS
																																						? field_152404_N
																																						: 0.0F;
	}

	public boolean getOptionOrdinalValue(GameSettings.Options p_74308_1_) {
		switch (GameSettings.SwitchOptions.optionIds[p_74308_1_.ordinal()]) {
		case 1:
			return invertMouse;
		case 2:
			return viewBobbing;
		case 3:
			return anaglyph;
		case 4:
			return advancedOpengl;
		case 5:
			return fboEnable;
		case 6:
			return clouds;
		case 7:
			return chatColours;
		case 8:
			return chatLinks;
		case 9:
			return chatLinksPrompt;
		case 10:
			return snooperEnabled;
		case 11:
			return fullScreen;
		case 12:
			return enableVsync;
		case 13:
			return showCape;
		case 14:
			return touchscreen;
		case 15:
			return field_152406_P;
		case 16:
			return forceUnicodeFont;
		default:
			return false;
		}
	}

	private static String getTranslation(String[] p_74299_0_, int p_74299_1_) {
		if (p_74299_1_ < 0 || p_74299_1_ >= p_74299_0_.length) {
			p_74299_1_ = 0;
		}

		return I18n.format(p_74299_0_[p_74299_1_], new Object[0]);
	}

	public String getKeyBinding(GameSettings.Options p_74297_1_) {
		String s = I18n.format(p_74297_1_.getEnumString(), new Object[0]) + ": ";

		if (p_74297_1_.getEnumFloat()) {
			float f1 = getOptionFloatValue(p_74297_1_);
			float f = p_74297_1_.normalizeValue(f1);
			return p_74297_1_ == GameSettings.Options.SENSITIVITY
					? f == 0.0F ? s + I18n.format("options.sensitivity.min", new Object[0])
							: f == 1.0F ? s + I18n.format("options.sensitivity.max", new Object[0])
									: s + (int) (f * 200.0F) + "%"
					: p_74297_1_ == GameSettings.Options.FOV
							? f1 == 70.0F ? s + I18n.format("options.fov.min", new Object[0])
									: f1 == 110.0F ? s + I18n.format("options.fov.max", new Object[0]) : s + (int) f1
							: p_74297_1_ == GameSettings.Options.FRAMERATE_LIMIT
									? f1 == p_74297_1_.valueMax
											? s + I18n.format("options.framerateLimit.max", new Object[0])
											: s + (int) f1 + " fps"
									: p_74297_1_ == GameSettings.Options.GAMMA
											? f == 0.0F ? s + I18n.format("options.gamma.min", new Object[0])
													: f == 1.0F ? s + I18n.format("options.gamma.max", new Object[0])
															: s + "+" + (int) (f * 100.0F) + "%"
											: p_74297_1_ == GameSettings.Options.SATURATION
													? s + (int) (f * 400.0F) + "%"
													: p_74297_1_ == GameSettings.Options.CHAT_OPACITY
															? s + (int) (f * 90.0F + 10.0F) + "%"
															: p_74297_1_ == GameSettings.Options.CHAT_HEIGHT_UNFOCUSED
																	? s + GuiNewChat.func_146243_b(f) + "px"
																	: p_74297_1_ == GameSettings.Options.CHAT_HEIGHT_FOCUSED
																			? s + GuiNewChat.func_146243_b(f) + "px"
																			: p_74297_1_ == GameSettings.Options.CHAT_WIDTH
																					? s + GuiNewChat.func_146233_a(f)
																							+ "px"
																					: p_74297_1_ == GameSettings.Options.RENDER_DISTANCE
																							? s + (int) f1 + " chunks"
																							: p_74297_1_ == GameSettings.Options.ANISOTROPIC_FILTERING
																									? f1 == 1.0F ? s
																											+ I18n.format(
																													"options.off",
																													new Object[0])
																											: s + (int) f1
																									: p_74297_1_ == GameSettings.Options.MIPMAP_LEVELS
																											? f1 == 0.0F
																													? s + I18n
																															.format("options.off",
																																	new Object[0])
																													: s + (int) f1
																											: p_74297_1_ == GameSettings.Options.STREAM_FPS
																													? s + TwitchStream
																															.func_152948_a(
																																	f)
																															+ " fps"
																													: p_74297_1_ == GameSettings.Options.STREAM_KBPS
																															? s + TwitchStream
																																	.func_152946_b(
																																			f)
																																	+ " Kbps"
																															: p_74297_1_ == GameSettings.Options.STREAM_BYTES_PER_PIXEL
																																	? s + String
																																			.format("%.3f bpp",
																																					new Object[] {
																																							Float.valueOf(
																																									TwitchStream
																																											.func_152947_c(
																																													f)) })
																																	: f == 0.0F
																																			? s + I18n
																																					.format("options.off",
																																							new Object[0])
																																			: s + (int) (f
																																					* 100.0F)
																																					+ "%";
		} else if (p_74297_1_.getEnumBoolean()) {
			boolean flag = getOptionOrdinalValue(p_74297_1_);
			return flag ? s + I18n.format("options.on", new Object[0]) : s + I18n.format("options.off", new Object[0]);
		} else if (p_74297_1_ == GameSettings.Options.DIFFICULTY)
			return s + I18n.format(difficulty.getDifficultyResourceKey(), new Object[0]);
		else if (p_74297_1_ == GameSettings.Options.GUI_SCALE)
			return s + getTranslation(GUISCALES, guiScale);
		else if (p_74297_1_ == GameSettings.Options.CHAT_VISIBILITY)
			return s + I18n.format(chatVisibility.getResourceKey(), new Object[0]);
		else if (p_74297_1_ == GameSettings.Options.PARTICLES)
			return s + getTranslation(PARTICLES, particleSetting);
		else if (p_74297_1_ == GameSettings.Options.AMBIENT_OCCLUSION)
			return s + getTranslation(AMBIENT_OCCLUSIONS, ambientOcclusion);
		else if (p_74297_1_ == GameSettings.Options.STREAM_COMPRESSION)
			return s + getTranslation(field_152391_aS, field_152405_O);
		else if (p_74297_1_ == GameSettings.Options.STREAM_CHAT_ENABLED)
			return s + getTranslation(field_152392_aT, field_152408_R);
		else if (p_74297_1_ == GameSettings.Options.STREAM_CHAT_USER_FILTER)
			return s + getTranslation(field_152393_aU, field_152409_S);
		else if (p_74297_1_ == GameSettings.Options.STREAM_MIC_TOGGLE_BEHAVIOR)
			return s + getTranslation(field_152394_aV, field_152410_T);
		else if (p_74297_1_ == GameSettings.Options.GRAPHICS) {
			if (fancyGraphics)
				return s + I18n.format("options.graphics.fancy", new Object[0]);
			else
				return s + I18n.format("options.graphics.fast", new Object[0]);
		} else
			return s;
	}

	public void loadOptions() {
		try {
			if (!optionsFile.exists())
				return;

			BufferedReader bufferedreader = new BufferedReader(new FileReader(optionsFile));
			String s = "";
			mapSoundLevels.clear();

			while ((s = bufferedreader.readLine()) != null) {
				try {
					String[] astring = s.split(":");

					if (astring[0].equals("mouseSensitivity")) {
						mouseSensitivity = parseFloat(astring[1]);
					}

					if (astring[0].equals("invertYMouse")) {
						invertMouse = astring[1].equals("true");
					}

					if (astring[0].equals("fov")) {
						fovSetting = parseFloat(astring[1]);
					}

					if (astring[0].equals("gamma")) {
						gammaSetting = parseFloat(astring[1]);
					}

					if (astring[0].equals("saturation")) {
						saturation = parseFloat(astring[1]);
					}

					if (astring[0].equals("fov")) {
						fovSetting = parseFloat(astring[1]) * 40.0F + 70.0F;
					}

					if (astring[0].equals("renderDistance")) {
						renderDistanceChunks = Integer.parseInt(astring[1]);
					}

					if (astring[0].equals("guiScale")) {
						guiScale = Integer.parseInt(astring[1]);
					}

					if (astring[0].equals("particles")) {
						particleSetting = Integer.parseInt(astring[1]);
					}

					if (astring[0].equals("bobView")) {
						viewBobbing = astring[1].equals("true");
					}

					if (astring[0].equals("anaglyph3d")) {
						anaglyph = astring[1].equals("true");
					}

					if (astring[0].equals("advancedOpengl")) {
						advancedOpengl = astring[1].equals("true");
					}

					if (astring[0].equals("maxFps")) {
						limitFramerate = Integer.parseInt(astring[1]);
					}

					if (astring[0].equals("fboEnable")) {
						fboEnable = astring[1].equals("true");
					}

					if (astring[0].equals("difficulty")) {
						difficulty = EnumDifficulty.getDifficultyEnum(Integer.parseInt(astring[1]));
					}

					if (astring[0].equals("fancyGraphics")) {
						fancyGraphics = astring[1].equals("true");
					}

					if (astring[0].equals("ao")) {
						if (astring[1].equals("true")) {
							ambientOcclusion = 2;
						} else if (astring[1].equals("false")) {
							ambientOcclusion = 0;
						} else {
							ambientOcclusion = Integer.parseInt(astring[1]);
						}
					}

					if (astring[0].equals("clouds")) {
						clouds = astring[1].equals("true");
					}

					if (astring[0].equals("resourcePacks")) {
						resourcePacks = (List) gson.fromJson(s.substring(s.indexOf(58) + 1), typeListString);

						if (resourcePacks == null) {
							resourcePacks = new ArrayList();
						}
					}

					if (astring[0].equals("lastServer") && astring.length >= 2) {
						lastServer = s.substring(s.indexOf(58) + 1);
					}

					if (astring[0].equals("lang") && astring.length >= 2) {
						language = astring[1];
					}

					if (astring[0].equals("chatVisibility")) {
						chatVisibility = EntityPlayer.EnumChatVisibility
								.getEnumChatVisibility(Integer.parseInt(astring[1]));
					}

					if (astring[0].equals("chatColors")) {
						chatColours = astring[1].equals("true");
					}

					if (astring[0].equals("chatLinks")) {
						chatLinks = astring[1].equals("true");
					}

					if (astring[0].equals("chatLinksPrompt")) {
						chatLinksPrompt = astring[1].equals("true");
					}

					if (astring[0].equals("chatOpacity")) {
						chatOpacity = parseFloat(astring[1]);
					}

					if (astring[0].equals("snooperEnabled")) {
						snooperEnabled = astring[1].equals("true");
					}

					if (astring[0].equals("fullscreen")) {
						fullScreen = astring[1].equals("true");
					}

					if (astring[0].equals("enableVsync")) {
						enableVsync = astring[1].equals("true");
					}

					if (astring[0].equals("hideServerAddress")) {
						hideServerAddress = astring[1].equals("true");
					}

					if (astring[0].equals("advancedItemTooltips")) {
						advancedItemTooltips = astring[1].equals("true");
					}

					if (astring[0].equals("pauseOnLostFocus")) {
						pauseOnLostFocus = astring[1].equals("true");
					}

					if (astring[0].equals("showCape")) {
						showCape = astring[1].equals("true");
					}

					if (astring[0].equals("touchscreen")) {
						touchscreen = astring[1].equals("true");
					}

					if (astring[0].equals("overrideHeight")) {
						overrideHeight = Integer.parseInt(astring[1]);
					}

					if (astring[0].equals("overrideWidth")) {
						overrideWidth = Integer.parseInt(astring[1]);
					}

					if (astring[0].equals("heldItemTooltips")) {
						heldItemTooltips = astring[1].equals("true");
					}

					if (astring[0].equals("chatHeightFocused")) {
						chatHeightFocused = parseFloat(astring[1]);
					}

					if (astring[0].equals("chatHeightUnfocused")) {
						chatHeightUnfocused = parseFloat(astring[1]);
					}

					if (astring[0].equals("chatScale")) {
						chatScale = parseFloat(astring[1]);
					}

					if (astring[0].equals("chatWidth")) {
						chatWidth = parseFloat(astring[1]);
					}

					if (astring[0].equals("showInventoryAchievementHint")) {
						showInventoryAchievementHint = astring[1].equals("true");
					}

					if (astring[0].equals("mipmapLevels")) {
						mipmapLevels = Integer.parseInt(astring[1]);
					}

					if (astring[0].equals("anisotropicFiltering")) {
						anisotropicFiltering = Integer.parseInt(astring[1]);
					}

					if (astring[0].equals("streamBytesPerPixel")) {
						field_152400_J = parseFloat(astring[1]);
					}

					if (astring[0].equals("streamMicVolume")) {
						field_152401_K = parseFloat(astring[1]);
					}

					if (astring[0].equals("streamSystemVolume")) {
						field_152402_L = parseFloat(astring[1]);
					}

					if (astring[0].equals("streamKbps")) {
						field_152403_M = parseFloat(astring[1]);
					}

					if (astring[0].equals("streamFps")) {
						field_152404_N = parseFloat(astring[1]);
					}

					if (astring[0].equals("streamCompression")) {
						field_152405_O = Integer.parseInt(astring[1]);
					}

					if (astring[0].equals("streamSendMetadata")) {
						field_152406_P = astring[1].equals("true");
					}

					if (astring[0].equals("streamPreferredServer") && astring.length >= 2) {
						field_152407_Q = s.substring(s.indexOf(58) + 1);
					}

					if (astring[0].equals("streamChatEnabled")) {
						field_152408_R = Integer.parseInt(astring[1]);
					}

					if (astring[0].equals("streamChatUserFilter")) {
						field_152409_S = Integer.parseInt(astring[1]);
					}

					if (astring[0].equals("streamMicToggleBehavior")) {
						field_152410_T = Integer.parseInt(astring[1]);
					}

					if (astring[0].equals("forceUnicodeFont")) {
						forceUnicodeFont = astring[1].equals("true");
					}

					KeyBinding[] akeybinding = keyBindings;
					int i = akeybinding.length;
					int j;

					for (j = 0; j < i; ++j) {
						KeyBinding keybinding = akeybinding[j];

						if (astring[0].equals("key_" + keybinding.getKeyDescription())) {
							keybinding.setKeyCode(Integer.parseInt(astring[1]));
						}
					}

					SoundCategory[] asoundcategory = SoundCategory.values();
					i = asoundcategory.length;

					for (j = 0; j < i; ++j) {
						SoundCategory soundcategory = asoundcategory[j];

						if (astring[0].equals("soundCategory_" + soundcategory.getCategoryName())) {
							mapSoundLevels.put(soundcategory, Float.valueOf(parseFloat(astring[1])));
						}
					}
				} catch (Exception exception) {
					logger.warn("Skipping bad option: " + s);
				}
			}

			KeyBinding.resetKeyBindingArrayAndHash();
			bufferedreader.close();
		} catch (Exception exception1) {
			logger.error("Failed to load options", exception1);
		}
	}

	private float parseFloat(String p_74305_1_) {
		return p_74305_1_.equals("true") ? 1.0F : p_74305_1_.equals("false") ? 0.0F : Float.parseFloat(p_74305_1_);
	}

	public void saveOptions() {
		if (FMLClientHandler.instance().isLoading())
			return;
		try {
			PrintWriter printwriter = new PrintWriter(new FileWriter(optionsFile));
			printwriter.println("invertYMouse:" + invertMouse);
			printwriter.println("mouseSensitivity:" + mouseSensitivity);
			printwriter.println("fov:" + (fovSetting - 70.0F) / 40.0F);
			printwriter.println("gamma:" + gammaSetting);
			printwriter.println("saturation:" + saturation);
			printwriter.println("renderDistance:" + renderDistanceChunks);
			printwriter.println("guiScale:" + guiScale);
			printwriter.println("particles:" + particleSetting);
			printwriter.println("bobView:" + viewBobbing);
			printwriter.println("anaglyph3d:" + anaglyph);
			printwriter.println("advancedOpengl:" + advancedOpengl);
			printwriter.println("maxFps:" + limitFramerate);
			printwriter.println("fboEnable:" + fboEnable);
			printwriter.println("difficulty:" + difficulty.getDifficultyId());
			printwriter.println("fancyGraphics:" + fancyGraphics);
			printwriter.println("ao:" + ambientOcclusion);
			printwriter.println("clouds:" + clouds);
			printwriter.println("resourcePacks:" + gson.toJson(resourcePacks));
			printwriter.println("lastServer:" + lastServer);
			printwriter.println("lang:" + language);
			printwriter.println("chatVisibility:" + chatVisibility.getChatVisibility());
			printwriter.println("chatColors:" + chatColours);
			printwriter.println("chatLinks:" + chatLinks);
			printwriter.println("chatLinksPrompt:" + chatLinksPrompt);
			printwriter.println("chatOpacity:" + chatOpacity);
			printwriter.println("snooperEnabled:" + snooperEnabled);
			printwriter.println("fullscreen:" + fullScreen);
			printwriter.println("enableVsync:" + enableVsync);
			printwriter.println("hideServerAddress:" + hideServerAddress);
			printwriter.println("advancedItemTooltips:" + advancedItemTooltips);
			printwriter.println("pauseOnLostFocus:" + pauseOnLostFocus);
			printwriter.println("showCape:" + showCape);
			printwriter.println("touchscreen:" + touchscreen);
			printwriter.println("overrideWidth:" + overrideWidth);
			printwriter.println("overrideHeight:" + overrideHeight);
			printwriter.println("heldItemTooltips:" + heldItemTooltips);
			printwriter.println("chatHeightFocused:" + chatHeightFocused);
			printwriter.println("chatHeightUnfocused:" + chatHeightUnfocused);
			printwriter.println("chatScale:" + chatScale);
			printwriter.println("chatWidth:" + chatWidth);
			printwriter.println("showInventoryAchievementHint:" + showInventoryAchievementHint);
			printwriter.println("mipmapLevels:" + mipmapLevels);
			printwriter.println("anisotropicFiltering:" + anisotropicFiltering);
			printwriter.println("streamBytesPerPixel:" + field_152400_J);
			printwriter.println("streamMicVolume:" + field_152401_K);
			printwriter.println("streamSystemVolume:" + field_152402_L);
			printwriter.println("streamKbps:" + field_152403_M);
			printwriter.println("streamFps:" + field_152404_N);
			printwriter.println("streamCompression:" + field_152405_O);
			printwriter.println("streamSendMetadata:" + field_152406_P);
			printwriter.println("streamPreferredServer:" + field_152407_Q);
			printwriter.println("streamChatEnabled:" + field_152408_R);
			printwriter.println("streamChatUserFilter:" + field_152409_S);
			printwriter.println("streamMicToggleBehavior:" + field_152410_T);
			printwriter.println("forceUnicodeFont:" + forceUnicodeFont);
			KeyBinding[] akeybinding = keyBindings;
			int i = akeybinding.length;
			int j;

			for (j = 0; j < i; ++j) {
				KeyBinding keybinding = akeybinding[j];
				printwriter.println("key_" + keybinding.getKeyDescription() + ":" + keybinding.getKeyCode());
			}

			SoundCategory[] asoundcategory = SoundCategory.values();
			i = asoundcategory.length;

			for (j = 0; j < i; ++j) {
				SoundCategory soundcategory = asoundcategory[j];
				printwriter.println(
						"soundCategory_" + soundcategory.getCategoryName() + ":" + getSoundLevel(soundcategory));
			}

			printwriter.close();
		} catch (Exception exception) {
			logger.error("Failed to save options", exception);
		}

		sendSettingsToServer();
	}

	public float getSoundLevel(SoundCategory p_151438_1_) {
		return mapSoundLevels.containsKey(p_151438_1_) ? ((Float) mapSoundLevels.get(p_151438_1_)).floatValue() : 1.0F;
	}

	public void setSoundLevel(SoundCategory p_151439_1_, float p_151439_2_) {
		mc.getSoundHandler().setSoundLevel(p_151439_1_, p_151439_2_);
		mapSoundLevels.put(p_151439_1_, Float.valueOf(p_151439_2_));
	}

	public void sendSettingsToServer() {
		if (mc.thePlayer != null) {
			mc.thePlayer.sendQueue.addToSendQueue(new C15PacketClientSettings(language, renderDistanceChunks,
					chatVisibility, chatColours, difficulty, showCape));
		}
	}

	public boolean shouldRenderClouds() {
		return renderDistanceChunks >= 4 && clouds;
	}

	@SideOnly(Side.CLIENT)
	public static enum Options {
		INVERT_MOUSE("options.invertMouse", false, true), SENSITIVITY("options.sensitivity", true, false), FOV(
				"options.fov", true, false, 30.0F, 110.0F,
				1.0F), GAMMA("options.gamma", true, false), SATURATION("options.saturation", true,
						false), RENDER_DISTANCE("options.renderDistance", true, false, 2.0F, 16.0F, 1.0F), VIEW_BOBBING(
								"options.viewBobbing", false, true), ANAGLYPH("options.anaglyph", false,
										true), ADVANCED_OPENGL("options.advancedOpengl", false, true), FRAMERATE_LIMIT(
												"options.framerateLimit", true, false, 10.0F, 260.0F,
												10.0F), FBO_ENABLE("options.fboEnable", false, true), DIFFICULTY(
														"options.difficulty", false,
														false), GRAPHICS("options.graphics", false,
																false), AMBIENT_OCCLUSION("options.ao", false,
																		false), GUI_SCALE("options.guiScale", false,
																				false), RENDER_CLOUDS(
																						"options.renderClouds", false,
																						true), PARTICLES(
																								"options.particles",
																								false,
																								false), CHAT_VISIBILITY(
																										"options.chat.visibility",
																										false,
																										false), CHAT_COLOR(
																												"options.chat.color",
																												false,
																												true), CHAT_LINKS(
																														"options.chat.links",
																														false,
																														true), CHAT_OPACITY(
																																"options.chat.opacity",
																																true,
																																false), CHAT_LINKS_PROMPT(
																																		"options.chat.links.prompt",
																																		false,
																																		true), SNOOPER_ENABLED(
																																				"options.snooper",
																																				false,
																																				true), USE_FULLSCREEN(
																																						"options.fullscreen",
																																						false,
																																						true), ENABLE_VSYNC(
																																								"options.vsync",
																																								false,
																																								true), SHOW_CAPE(
																																										"options.showCape",
																																										false,
																																										true), TOUCHSCREEN(
																																												"options.touchscreen",
																																												false,
																																												true), CHAT_SCALE(
																																														"options.chat.scale",
																																														true,
																																														false), CHAT_WIDTH(
																																																"options.chat.width",
																																																true,
																																																false), CHAT_HEIGHT_FOCUSED(
																																																		"options.chat.height.focused",
																																																		true,
																																																		false), CHAT_HEIGHT_UNFOCUSED(
																																																				"options.chat.height.unfocused",
																																																				true,
																																																				false), MIPMAP_LEVELS(
																																																						"options.mipmapLevels",
																																																						true,
																																																						false,
																																																						0.0F,
																																																						4.0F,
																																																						1.0F), ANISOTROPIC_FILTERING(
																																																								"options.anisotropicFiltering",
																																																								true,
																																																								false,
																																																								1.0F,
																																																								16.0F,
																																																								0.0F) {
																																																							private static final String __OBFID = "CL_00000654";

																																																							@Override
																																																							protected float snapToStep(
																																																									float p_148264_1_) {
																																																								return MathHelper
																																																										.roundUpToPowerOfTwo(
																																																												(int) p_148264_1_);
																																																							}
																																																						},
		FORCE_UNICODE_FONT("options.forceUnicodeFont", false, true), STREAM_BYTES_PER_PIXEL(
				"options.stream.bytesPerPixel", true, false), STREAM_VOLUME_MIC("options.stream.micVolumne", true,
						false), STREAM_VOLUME_SYSTEM("options.stream.systemVolume", true, false), STREAM_KBPS(
								"options.stream.kbps", true, false), STREAM_FPS("options.stream.fps", true,
										false), STREAM_COMPRESSION("options.stream.compression", false,
												false), STREAM_SEND_METADATA("options.stream.sendMetadata", false,
														true), STREAM_CHAT_ENABLED("options.stream.chat.enabled", false,
																false), STREAM_CHAT_USER_FILTER(
																		"options.stream.chat.userFilter", false,
																		false), STREAM_MIC_TOGGLE_BEHAVIOR(
																				"options.stream.micToggleBehavior",
																				false, false);
		private final boolean enumFloat;
		private final boolean enumBoolean;
		private final String enumString;
		private final float valueStep;
		private float valueMin;
		private float valueMax;

		private static final String __OBFID = "CL_00000653";

		public static GameSettings.Options getEnumOptions(int p_74379_0_) {
			GameSettings.Options[] aoptions = values();
			int j = aoptions.length;

			for (int k = 0; k < j; ++k) {
				GameSettings.Options options = aoptions[k];

				if (options.returnEnumOrdinal() == p_74379_0_)
					return options;
			}

			return null;
		}

		private Options(String p_i1015_3_, boolean p_i1015_4_, boolean p_i1015_5_) {
			this(p_i1015_3_, p_i1015_4_, p_i1015_5_, 0.0F, 1.0F, 0.0F);
		}

		private Options(String p_i45004_3_, boolean p_i45004_4_, boolean p_i45004_5_, float p_i45004_6_,
				float p_i45004_7_, float p_i45004_8_) {
			enumString = p_i45004_3_;
			enumFloat = p_i45004_4_;
			enumBoolean = p_i45004_5_;
			valueMin = p_i45004_6_;
			valueMax = p_i45004_7_;
			valueStep = p_i45004_8_;
		}

		public boolean getEnumFloat() {
			return enumFloat;
		}

		public boolean getEnumBoolean() {
			return enumBoolean;
		}

		public int returnEnumOrdinal() {
			return ordinal();
		}

		public String getEnumString() {
			return enumString;
		}

		public float getValueMax() {
			return valueMax;
		}

		public void setValueMax(float p_148263_1_) {
			valueMax = p_148263_1_;
		}

		public float normalizeValue(float p_148266_1_) {
			return MathHelper.clamp_float((snapToStepClamp(p_148266_1_) - valueMin) / (valueMax - valueMin), 0.0F,
					1.0F);
		}

		public float denormalizeValue(float p_148262_1_) {
			return snapToStepClamp(valueMin + (valueMax - valueMin) * MathHelper.clamp_float(p_148262_1_, 0.0F, 1.0F));
		}

		public float snapToStepClamp(float p_148268_1_) {
			p_148268_1_ = snapToStep(p_148268_1_);
			return MathHelper.clamp_float(p_148268_1_, valueMin, valueMax);
		}

		protected float snapToStep(float p_148264_1_) {
			if (valueStep > 0.0F) {
				p_148264_1_ = valueStep * Math.round(p_148264_1_ / valueStep);
			}

			return p_148264_1_;
		}

		Options(String p_i45005_3_, boolean p_i45005_4_, boolean p_i45005_5_, float p_i45005_6_, float p_i45005_7_,
				float p_i45005_8_, Object p_i45005_9_) {
			this(p_i45005_3_, p_i45005_4_, p_i45005_5_, p_i45005_6_, p_i45005_7_, p_i45005_8_);
		}
	}

	@SideOnly(Side.CLIENT)

	static final class SwitchOptions {
		static final int[] optionIds = new int[GameSettings.Options.values().length];
		private static final String __OBFID = "CL_00000652";

		static {
			try {
				optionIds[GameSettings.Options.INVERT_MOUSE.ordinal()] = 1;
			} catch (NoSuchFieldError var16) {
				;
			}

			try {
				optionIds[GameSettings.Options.VIEW_BOBBING.ordinal()] = 2;
			} catch (NoSuchFieldError var15) {
				;
			}

			try {
				optionIds[GameSettings.Options.ANAGLYPH.ordinal()] = 3;
			} catch (NoSuchFieldError var14) {
				;
			}

			try {
				optionIds[GameSettings.Options.ADVANCED_OPENGL.ordinal()] = 4;
			} catch (NoSuchFieldError var13) {
				;
			}

			try {
				optionIds[GameSettings.Options.FBO_ENABLE.ordinal()] = 5;
			} catch (NoSuchFieldError var12) {
				;
			}

			try {
				optionIds[GameSettings.Options.RENDER_CLOUDS.ordinal()] = 6;
			} catch (NoSuchFieldError var11) {
				;
			}

			try {
				optionIds[GameSettings.Options.CHAT_COLOR.ordinal()] = 7;
			} catch (NoSuchFieldError var10) {
				;
			}

			try {
				optionIds[GameSettings.Options.CHAT_LINKS.ordinal()] = 8;
			} catch (NoSuchFieldError var9) {
				;
			}

			try {
				optionIds[GameSettings.Options.CHAT_LINKS_PROMPT.ordinal()] = 9;
			} catch (NoSuchFieldError var8) {
				;
			}

			try {
				optionIds[GameSettings.Options.SNOOPER_ENABLED.ordinal()] = 10;
			} catch (NoSuchFieldError var7) {
				;
			}

			try {
				optionIds[GameSettings.Options.USE_FULLSCREEN.ordinal()] = 11;
			} catch (NoSuchFieldError var6) {
				;
			}

			try {
				optionIds[GameSettings.Options.ENABLE_VSYNC.ordinal()] = 12;
			} catch (NoSuchFieldError var5) {
				;
			}

			try {
				optionIds[GameSettings.Options.SHOW_CAPE.ordinal()] = 13;
			} catch (NoSuchFieldError var4) {
				;
			}

			try {
				optionIds[GameSettings.Options.TOUCHSCREEN.ordinal()] = 14;
			} catch (NoSuchFieldError var3) {
				;
			}

			try {
				optionIds[GameSettings.Options.STREAM_SEND_METADATA.ordinal()] = 15;
			} catch (NoSuchFieldError var2) {
				;
			}

			try {
				optionIds[GameSettings.Options.FORCE_UNICODE_FONT.ordinal()] = 16;
			} catch (NoSuchFieldError var1) {
				;
			}
		}
	}
}