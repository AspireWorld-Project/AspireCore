package net.minecraft.client;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Queues;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.StartupQuery;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.audio.MusicTicker;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.achievement.GuiAchievement;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.gui.stream.GuiStreamUnavailable;
import net.minecraft.client.multiplayer.ChunkProviderClient;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerLoginClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.*;
import net.minecraft.client.resources.data.*;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.stream.IStream;
import net.minecraft.client.stream.NullStream;
import net.minecraft.client.stream.TwitchStream;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.BossStatus;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Bootstrap;
import net.minecraft.item.ItemStack;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.login.client.C00PacketLoginStart;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.profiler.IPlayerUsage;
import net.minecraft.profiler.PlayerUsageSnooper;
import net.minecraft.profiler.Profiler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.stats.AchievementList;
import net.minecraft.stats.IStatStringFormat;
import net.minecraft.stats.StatFileWriter;
import net.minecraft.util.Timer;
import net.minecraft.util.Util;
import net.minecraft.util.*;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.WorldProviderEnd;
import net.minecraft.world.WorldProviderHell;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.chunk.storage.AnvilSaveConverter;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.*;
import org.lwjgl.util.glu.GLU;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.Proxy;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

@SideOnly(Side.CLIENT)
public class Minecraft implements IPlayerUsage {
	private static final Logger logger = LogManager.getLogger();
	private static final ResourceLocation locationMojangPng = new ResourceLocation("textures/gui/title/mojang.png");
	public static final boolean isRunningOnMac = Util.getOSType() == Util.EnumOS.OSX;
	public static byte[] memoryReserve = new byte[10485760];
	@SuppressWarnings("rawtypes")
	private static final List macDisplayModes = Lists
			.newArrayList(new DisplayMode(2560, 1600), new DisplayMode(2880, 1800));
	private final File fileResourcepacks;
	@SuppressWarnings("rawtypes")
	private final Multimap field_152356_J;
	private ServerData currentServerData;
	public TextureManager renderEngine;
	private static Minecraft theMinecraft;
	public PlayerControllerMP playerController;
	private boolean fullscreen;
	private boolean hasCrashed;
	private CrashReport crashReporter;
	public int displayWidth;
	public int displayHeight;
	private final Timer timer = new Timer(20.0F);
	private final PlayerUsageSnooper usageSnooper = new PlayerUsageSnooper("client", this,
			MinecraftServer.getSystemTimeMillis());
	public WorldClient theWorld;
	public RenderGlobal renderGlobal;
	public EntityClientPlayerMP thePlayer;
	public EntityLivingBase renderViewEntity;
	public Entity pointedEntity;
	public EffectRenderer effectRenderer;
	private final Session session;
	private boolean isGamePaused;
	public FontRenderer fontRenderer;
	public FontRenderer standardGalacticFontRenderer;
	public GuiScreen currentScreen;
	public LoadingScreenRenderer loadingScreen;
	public EntityRenderer entityRenderer;
	private int leftClickCounter;
	private final int tempDisplayWidth;
	private final int tempDisplayHeight;
	private IntegratedServer theIntegratedServer;
	public GuiAchievement guiAchievement;
	public GuiIngame ingameGUI;
	public boolean skipRenderWorld;
	public MovingObjectPosition objectMouseOver;
	public GameSettings gameSettings;
	public MouseHelper mouseHelper;
	public final File mcDataDir;
	private final File fileAssets;
	private final String launchedVersion;
	private final Proxy proxy;
	private ISaveFormat saveLoader;
	private static int debugFPS;
	private int rightClickDelayTimer;
	private boolean refreshTexturePacksScheduled;
	private String serverName;
	private int serverPort;
	public boolean inGameHasFocus;
	long systemTime = getSystemTime();
	private int joinPlayerCounter;
	private final boolean jvm64bit;
	private final boolean isDemo;
	private NetworkManager myNetworkManager;
	private boolean integratedServerIsRunning;
	public final Profiler mcProfiler = new Profiler();
	private long field_83002_am = -1L;
	private IReloadableResourceManager mcResourceManager;
	private final IMetadataSerializer metadataSerializer_ = new IMetadataSerializer();
	@SuppressWarnings("rawtypes")
	private final List defaultResourcePacks = Lists.newArrayList();
	public DefaultResourcePack mcDefaultResourcePack;
	private ResourcePackRepository mcResourcePackRepository;
	private LanguageManager mcLanguageManager;
	private IStream field_152353_at;
	private Framebuffer framebufferMc;
	private TextureMap textureMapBlocks;
	private SoundHandler mcSoundHandler;
	private MusicTicker mcMusicTicker;
	private ResourceLocation field_152354_ay;
	private final MinecraftSessionService field_152355_az;
	private SkinManager field_152350_aA;
	@SuppressWarnings("rawtypes")
	private final Queue field_152351_aB = Queues.newArrayDeque();
	private final Thread field_152352_aC = Thread.currentThread();
	volatile boolean running = true;
	public String debug = "";
	long debugUpdateTime = getSystemTime();
	int fpsCounter;
	long prevFrameTime = -1L;
	private String debugProfilerName = "root";
	@SuppressWarnings("rawtypes")
	public Minecraft(Session p_i1103_1_, int p_i1103_2_, int p_i1103_3_, boolean p_i1103_4_, boolean p_i1103_5_,
			File p_i1103_6_, File p_i1103_7_, File p_i1103_8_, Proxy p_i1103_9_, String p_i1103_10_,
			Multimap p_i1103_11_, String p_i1103_12_) {
		theMinecraft = this;
		mcDataDir = p_i1103_6_;
		fileAssets = p_i1103_7_;
		fileResourcepacks = p_i1103_8_;
		launchedVersion = p_i1103_10_;
		field_152356_J = p_i1103_11_;
		mcDefaultResourcePack = new DefaultResourcePack(new ResourceIndex(p_i1103_7_, p_i1103_12_).func_152782_a());
		addDefaultResourcePack();
		proxy = p_i1103_9_ == null ? Proxy.NO_PROXY : p_i1103_9_;
		field_152355_az = new YggdrasilAuthenticationService(p_i1103_9_, UUID.randomUUID().toString())
				.createMinecraftSessionService();
		startTimerHackThread();
		session = p_i1103_1_;
		logger.info("Setting user: " + p_i1103_1_.getUsername());
		isDemo = p_i1103_5_;
		displayWidth = p_i1103_2_;
		displayHeight = p_i1103_3_;
		tempDisplayWidth = p_i1103_2_;
		tempDisplayHeight = p_i1103_3_;
		fullscreen = p_i1103_4_;
		jvm64bit = isJvm64bit();
		ImageIO.setUseCache(false);
		Bootstrap.func_151354_b();
	}

	private static boolean isJvm64bit() {
		String[] astring = new String[] { "sun.arch.data.model", "com.ibm.vm.bitmode", "os.arch" };
		String[] astring1 = astring;
		int i = astring.length;

		for (int j = 0; j < i; ++j) {
			String s = astring1[j];
			String s1 = System.getProperty(s);

			if (s1 != null && s1.contains("64"))
				return true;
		}

		return false;
	}

	public Framebuffer getFramebuffer() {
		return framebufferMc;
	}

	private void startTimerHackThread() {
		Thread thread = new Thread("Timer hack thread") {
			@Override
			public void run() {
				while (running) {
					try {
						Thread.sleep(2147483647L);
					} catch (InterruptedException interruptedexception) {
					}
				}
			}
		};
		thread.setDaemon(true);
		thread.start();
	}

	public void crashed(CrashReport p_71404_1_) {
		hasCrashed = true;
		crashReporter = p_71404_1_;
	}

	public void displayCrashReport(CrashReport p_71377_1_) {
		File file1 = new File(getMinecraft().mcDataDir, "crash-reports");
		File file2 = new File(file1,
				"crash-" + new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss").format(new Date()) + "-client.txt");
		System.out.println(p_71377_1_.getCompleteReport());

		int retVal;
		if (p_71377_1_.getFile() != null) {
			System.out.println("#@!@# Game crashed! Crash report saved to: #@!@# " + p_71377_1_.getFile());
			retVal = -1;
		} else if (p_71377_1_.saveToFile(file2)) {
			System.out.println("#@!@# Game crashed! Crash report saved to: #@!@# " + file2.getAbsolutePath());
			retVal = -1;
		} else {
			System.out.println("#@?@# Game crashed! Crash report could not be saved. #@?@#");
			retVal = -2;
		}
		FMLCommonHandler.instance().handleExit(retVal);
	}

	public void setServer(String p_71367_1_, int p_71367_2_) {
		serverName = p_71367_1_;
		serverPort = p_71367_2_;
	}

	@SuppressWarnings("unchecked")
	private void startGame() throws LWJGLException {
		gameSettings = new GameSettings(this, mcDataDir);

		if (gameSettings.overrideHeight > 0 && gameSettings.overrideWidth > 0) {
			displayWidth = gameSettings.overrideWidth;
			displayHeight = gameSettings.overrideHeight;
		}

		if (fullscreen) {
			Display.setFullscreen(true);
			displayWidth = Display.getDisplayMode().getWidth();
			displayHeight = Display.getDisplayMode().getHeight();

			if (displayWidth <= 0) {
				displayWidth = 1;
			}

			if (displayHeight <= 0) {
				displayHeight = 1;
			}
		} else {
			Display.setDisplayMode(new DisplayMode(displayWidth, displayHeight));
		}

		Display.setResizable(true);
		Display.setTitle("Minecraft 1.7.10");
		logger.info("LWJGL Version: " + Sys.getVersion());
		Util.EnumOS enumos = Util.getOSType();

		if (enumos != Util.EnumOS.OSX) {
			try {
				InputStream inputstream = mcDefaultResourcePack
						.func_152780_c(new ResourceLocation("icons/icon_16x16.png"));
				InputStream inputstream1 = mcDefaultResourcePack
						.func_152780_c(new ResourceLocation("icons/icon_32x32.png"));

				if (inputstream != null && inputstream1 != null) {
					Display.setIcon(new ByteBuffer[] { func_152340_a(inputstream), func_152340_a(inputstream1) });
				}
			} catch (IOException ioexception) {
				logger.error("Couldn't set icon", ioexception);
			}
		}

		try {
			net.minecraftforge.client.ForgeHooksClient.createDisplay();
		} catch (LWJGLException lwjglexception) {
			logger.error("Couldn't set pixel format", lwjglexception);

			try {
				Thread.sleep(1000L);
			} catch (InterruptedException interruptedexception) {
			}

			if (fullscreen) {
				updateDisplayMode();
			}

			Display.create();
		}

		OpenGlHelper.initializeTextures();

		try {
			field_152353_at = new TwitchStream(this,
					(String) Iterables.getFirst(field_152356_J.get("twitch_access_token"), null));
		} catch (Throwable throwable) {
			field_152353_at = new NullStream(throwable);
			logger.error("Couldn't initialize twitch stream");
		}

		framebufferMc = new Framebuffer(displayWidth, displayHeight, true);
		framebufferMc.setFramebufferColor(0.0F, 0.0F, 0.0F, 0.0F);
		guiAchievement = new GuiAchievement(this);
		metadataSerializer_.registerMetadataSectionType(new TextureMetadataSectionSerializer(),
				TextureMetadataSection.class);
		metadataSerializer_.registerMetadataSectionType(new FontMetadataSectionSerializer(), FontMetadataSection.class);
		metadataSerializer_.registerMetadataSectionType(new AnimationMetadataSectionSerializer(),
				AnimationMetadataSection.class);
		metadataSerializer_.registerMetadataSectionType(new PackMetadataSectionSerializer(), PackMetadataSection.class);
		metadataSerializer_.registerMetadataSectionType(new LanguageMetadataSectionSerializer(),
				LanguageMetadataSection.class);
		saveLoader = new AnvilSaveConverter(new File(mcDataDir, "saves"));
		mcResourcePackRepository = new ResourcePackRepository(fileResourcepacks,
				new File(mcDataDir, "server-resource-packs"), mcDefaultResourcePack, metadataSerializer_, gameSettings);
		mcResourceManager = new SimpleReloadableResourceManager(metadataSerializer_);
		mcLanguageManager = new LanguageManager(metadataSerializer_, gameSettings.language);
		mcResourceManager.registerReloadListener(mcLanguageManager);
		FMLClientHandler.instance().beginMinecraftLoading(this, defaultResourcePacks, mcResourceManager);
		renderEngine = new TextureManager(mcResourceManager);
		mcResourceManager.registerReloadListener(renderEngine);
		field_152350_aA = new SkinManager(renderEngine, new File(fileAssets, "skins"), field_152355_az);
		cpw.mods.fml.client.SplashProgress.drawVanillaScreen();
		mcSoundHandler = new SoundHandler(mcResourceManager, gameSettings);
		mcResourceManager.registerReloadListener(mcSoundHandler);
		mcMusicTicker = new MusicTicker(this);
		fontRenderer = new FontRenderer(gameSettings, new ResourceLocation("textures/font/ascii.png"), renderEngine,
				false);

		if (gameSettings.language != null) {
			fontRenderer.setUnicodeFlag(func_152349_b());
			fontRenderer.setBidiFlag(mcLanguageManager.isCurrentLanguageBidirectional());
		}

		standardGalacticFontRenderer = new FontRenderer(gameSettings,
				new ResourceLocation("textures/font/ascii_sga.png"), renderEngine, false);
		mcResourceManager.registerReloadListener(fontRenderer);
		mcResourceManager.registerReloadListener(standardGalacticFontRenderer);
		mcResourceManager.registerReloadListener(new GrassColorReloadListener());
		mcResourceManager.registerReloadListener(new FoliageColorReloadListener());
		cpw.mods.fml.common.ProgressManager.ProgressBar bar = cpw.mods.fml.common.ProgressManager
				.push("Rendering Setup", 9, true);
		bar.step("Loading Render Manager");
		RenderManager.instance.itemRenderer = new ItemRenderer(this);
		bar.step("Loading Entity Renderer");
		entityRenderer = new EntityRenderer(this, mcResourceManager);
		mcResourceManager.registerReloadListener(entityRenderer);
		AchievementList.openInventory.setStatStringFormatter(new IStatStringFormat() {
			@Override
			public String formatString(String p_74535_1_) {
				try {
					return String.format(p_74535_1_, GameSettings.getKeyDisplayString(gameSettings.keyBindInventory.getKeyCode()));
				} catch (Exception exception) {
					return "Error: " + exception.getLocalizedMessage();
				}
			}
		});
		bar.step("Loading GL properties");
		mouseHelper = new MouseHelper();
		checkGLError("Pre startup");
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glShadeModel(GL11.GL_SMOOTH);
		GL11.glClearDepth(1.0D);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthFunc(GL11.GL_LEQUAL);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
		GL11.glCullFace(GL11.GL_BACK);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		checkGLError("Startup");
		bar.step("Render Global instance");
		renderGlobal = new RenderGlobal(this);
		bar.step("Building Blocks Texture");
		textureMapBlocks = new TextureMap(0, "textures/blocks", true);
		bar.step("Anisotropy and Mipmaps");
		textureMapBlocks.setAnisotropicFiltering(gameSettings.anisotropicFiltering);
		textureMapBlocks.setMipmapLevels(gameSettings.mipmapLevels);
		bar.step("Loading Blocks Texture");
		renderEngine.loadTextureMap(TextureMap.locationBlocksTexture, textureMapBlocks);
		bar.step("Loading Items Texture");
		renderEngine.loadTextureMap(TextureMap.locationItemsTexture, new TextureMap(1, "textures/items", true));
		bar.step("Viewport");
		GL11.glViewport(0, 0, displayWidth, displayHeight);
		effectRenderer = new EffectRenderer(theWorld, renderEngine);
		cpw.mods.fml.common.ProgressManager.pop(bar);
		FMLClientHandler.instance().finishMinecraftLoading();
		checkGLError("Post startup");
		ingameGUI = new net.minecraftforge.client.GuiIngameForge(this);

		if (serverName != null) {
			FMLClientHandler.instance().connectToServerAtStartup(serverName, serverPort);
		} else {
			displayGuiScreen(new GuiMainMenu());
		}

		cpw.mods.fml.client.SplashProgress.clearVanillaResources(renderEngine, field_152354_ay);
		field_152354_ay = null;
		loadingScreen = new LoadingScreenRenderer(this);

		FMLClientHandler.instance().onInitializationComplete();
		if (gameSettings.fullScreen && !fullscreen) {
			toggleFullscreen();
		}

		try {
			Display.setVSyncEnabled(gameSettings.enableVsync);
		} catch (OpenGLException openglexception) {
			gameSettings.enableVsync = false;
			gameSettings.saveOptions();
		}
	}

	public boolean func_152349_b() {
		return mcLanguageManager.isCurrentLocaleUnicode() || gameSettings.forceUnicodeFont;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void refreshResources() {
		ArrayList arraylist = Lists.newArrayList(defaultResourcePacks);
		Iterator iterator = mcResourcePackRepository.getRepositoryEntries().iterator();

		while (iterator.hasNext()) {
			ResourcePackRepository.Entry entry = (ResourcePackRepository.Entry) iterator.next();
			arraylist.add(entry.getResourcePack());
		}

		if (mcResourcePackRepository.func_148530_e() != null) {
			arraylist.add(mcResourcePackRepository.func_148530_e());
		}

		try {
			mcResourceManager.reloadResources(arraylist);
		} catch (RuntimeException runtimeexception) {
			logger.info("Caught error stitching, removing all assigned resourcepacks", runtimeexception);
			arraylist.clear();
			arraylist.addAll(defaultResourcePacks);
			mcResourcePackRepository.func_148527_a(Collections.emptyList());
			mcResourceManager.reloadResources(arraylist);
			gameSettings.resourcePacks.clear();
			gameSettings.saveOptions();
		}

		mcLanguageManager.parseLanguageMetadata(arraylist);

		if (renderGlobal != null) {
			renderGlobal.loadRenderers();
		}
	}

	@SuppressWarnings("unchecked")
	private void addDefaultResourcePack() {
		defaultResourcePacks.add(mcDefaultResourcePack);
	}

	private ByteBuffer func_152340_a(InputStream p_152340_1_) throws IOException {
		BufferedImage bufferedimage = ImageIO.read(p_152340_1_);
		int[] aint = bufferedimage.getRGB(0, 0, bufferedimage.getWidth(), bufferedimage.getHeight(), null, 0,
				bufferedimage.getWidth());
		ByteBuffer bytebuffer = ByteBuffer.allocate(4 * aint.length);
		int[] aint1 = aint;
		int i = aint.length;

		for (int j = 0; j < i; ++j) {
			int k = aint1[j];
			bytebuffer.putInt(k << 8 | k >> 24 & 255);
		}

		bytebuffer.flip();
		return bytebuffer;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void updateDisplayMode() throws LWJGLException {
		HashSet hashset = new HashSet();
		Collections.addAll(hashset, Display.getAvailableDisplayModes());
		DisplayMode displaymode = Display.getDesktopDisplayMode();

		if (!hashset.contains(displaymode) && Util.getOSType() == Util.EnumOS.OSX) {
			Iterator iterator = macDisplayModes.iterator();

			while (iterator.hasNext()) {
				DisplayMode displaymode1 = (DisplayMode) iterator.next();
				boolean flag = true;
				Iterator iterator1 = hashset.iterator();
				DisplayMode displaymode2;

				while (iterator1.hasNext()) {
					displaymode2 = (DisplayMode) iterator1.next();

					if (displaymode2.getBitsPerPixel() == 32 && displaymode2.getWidth() == displaymode1.getWidth()
							&& displaymode2.getHeight() == displaymode1.getHeight()) {
						flag = false;
						break;
					}
				}

				if (!flag) {
					iterator1 = hashset.iterator();

					while (iterator1.hasNext()) {
						displaymode2 = (DisplayMode) iterator1.next();

						if (displaymode2.getBitsPerPixel() == 32
								&& displaymode2.getWidth() == displaymode1.getWidth() / 2
								&& displaymode2.getHeight() == displaymode1.getHeight() / 2) {
							displaymode = displaymode2;
							break;
						}
					}
				}
			}
		}

		Display.setDisplayMode(displaymode);
		displayWidth = displaymode.getWidth();
		displayHeight = displaymode.getHeight();
	}

	public void loadScreen() throws LWJGLException {
		ScaledResolution scaledresolution = new ScaledResolution(this, displayWidth, displayHeight);
		int i = scaledresolution.getScaleFactor();
		Framebuffer framebuffer = new Framebuffer(scaledresolution.getScaledWidth() * i,
				scaledresolution.getScaledHeight() * i, true);
		framebuffer.bindFramebuffer(false);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0.0D, scaledresolution.getScaledWidth(), scaledresolution.getScaledHeight(), 0.0D, 1000.0D,
				3000.0D);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		GL11.glTranslatef(0.0F, 0.0F, -2000.0F);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_FOG);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_TEXTURE_2D);

		try {
			field_152354_ay = renderEngine.getDynamicTextureLocation("logo",
					new DynamicTexture(ImageIO.read(mcDefaultResourcePack.getInputStream(locationMojangPng))));
			renderEngine.bindTexture(field_152354_ay);
		} catch (IOException ioexception) {
			logger.error("Unable to load logo: " + locationMojangPng, ioexception);
		}

		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		tessellator.setColorOpaque_I(16777215);
		tessellator.addVertexWithUV(0.0D, displayHeight, 0.0D, 0.0D, 0.0D);
		tessellator.addVertexWithUV(displayWidth, displayHeight, 0.0D, 0.0D, 0.0D);
		tessellator.addVertexWithUV(displayWidth, 0.0D, 0.0D, 0.0D, 0.0D);
		tessellator.addVertexWithUV(0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
		tessellator.draw();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		tessellator.setColorOpaque_I(16777215);
		short short1 = 256;
		short short2 = 256;
		scaledTessellator((scaledresolution.getScaledWidth() - short1) / 2,
				(scaledresolution.getScaledHeight() - short2) / 2, 0, 0, short1, short2);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_FOG);
		framebuffer.unbindFramebuffer();
		framebuffer.framebufferRender(scaledresolution.getScaledWidth() * i, scaledresolution.getScaledHeight() * i);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
		GL11.glFlush();
		func_147120_f();
	}

	public void scaledTessellator(int p_71392_1_, int p_71392_2_, int p_71392_3_, int p_71392_4_, int p_71392_5_,
			int p_71392_6_) {
		float f = 0.00390625F;
		float f1 = 0.00390625F;
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		tessellator.addVertexWithUV(p_71392_1_ + 0, p_71392_2_ + p_71392_6_, 0.0D, (p_71392_3_ + 0) * f,
				(p_71392_4_ + p_71392_6_) * f1);
		tessellator.addVertexWithUV(p_71392_1_ + p_71392_5_, p_71392_2_ + p_71392_6_, 0.0D,
				(p_71392_3_ + p_71392_5_) * f, (p_71392_4_ + p_71392_6_) * f1);
		tessellator.addVertexWithUV(p_71392_1_ + p_71392_5_, p_71392_2_ + 0, 0.0D, (p_71392_3_ + p_71392_5_) * f,
				(p_71392_4_ + 0) * f1);
		tessellator.addVertexWithUV(p_71392_1_ + 0, p_71392_2_ + 0, 0.0D, (p_71392_3_ + 0) * f, (p_71392_4_ + 0) * f1);
		tessellator.draw();
	}

	public ISaveFormat getSaveLoader() {
		return saveLoader;
	}

	public void displayGuiScreen(GuiScreen p_147108_1_) {
		if (p_147108_1_ == null && theWorld == null) {
			p_147108_1_ = new GuiMainMenu();
		} else if (p_147108_1_ == null && thePlayer.getHealth() <= 0.0F) {
			p_147108_1_ = new GuiGameOver();
		}

		GuiScreen old = currentScreen;
		net.minecraftforge.client.event.GuiOpenEvent event = new net.minecraftforge.client.event.GuiOpenEvent(
				p_147108_1_);

		if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event))
			return;

		p_147108_1_ = event.gui;
		if (old != null && p_147108_1_ != old) {
			old.onGuiClosed();
		}

		if (p_147108_1_ instanceof GuiMainMenu) {
			gameSettings.showDebugInfo = false;
			ingameGUI.getChatGUI().clearChatMessages();
		}

		currentScreen = p_147108_1_;

		if (p_147108_1_ != null) {
			setIngameNotInFocus();
			ScaledResolution scaledresolution = new ScaledResolution(this, displayWidth, displayHeight);
			int i = scaledresolution.getScaledWidth();
			int j = scaledresolution.getScaledHeight();
			p_147108_1_.setWorldAndResolution(this, i, j);
			skipRenderWorld = false;
		} else {
			mcSoundHandler.resumeSounds();
			setIngameFocus();
		}
	}

	private void checkGLError(String p_71361_1_) {
		int i = GL11.glGetError();

		if (i != 0) {
			String s1 = GLU.gluErrorString(i);
			logger.error("########## GL ERROR ##########");
			logger.error("@ " + p_71361_1_);
			logger.error(i + ": " + s1);
		}
	}

	public void shutdownMinecraftApplet() {
		try {
			field_152353_at.func_152923_i();
			logger.info("Stopping!");

			try {
				this.loadWorld(null);
			} catch (Throwable throwable1) {
			}

			try {
				GLAllocation.deleteTexturesAndDisplayLists();
			} catch (Throwable throwable) {
			}

			mcSoundHandler.unloadSounds();
		} finally {
			Display.destroy();

			if (!hasCrashed) {
				System.exit(0);
			}
		}

		System.gc();
	}

	public void run() {
		running = true;
		CrashReport crashreport;

		try {
			startGame();
		} catch (Throwable throwable) {
			crashreport = CrashReport.makeCrashReport(throwable, "Initializing game");
			crashreport.makeCategory("Initialization");
			displayCrashReport(addGraphicsAndWorldToCrashReport(crashreport));
			return;
		}

		while (true) {
			try {
				while (running) {
					if (!hasCrashed || crashReporter == null) {
						try {
							runGameLoop();
						} catch (OutOfMemoryError outofmemoryerror) {
							freeMemory();
							displayGuiScreen(new GuiMemoryErrorScreen());
							System.gc();
						}

						continue;
					}

					displayCrashReport(crashReporter);
					return;
				}
			} catch (MinecraftError minecrafterror) {
			} catch (ReportedException reportedexception) {
				addGraphicsAndWorldToCrashReport(reportedexception.getCrashReport());
				freeMemory();
				logger.fatal("Reported exception thrown!", reportedexception);
				displayCrashReport(reportedexception.getCrashReport());
			} catch (Throwable throwable1) {
				crashreport = addGraphicsAndWorldToCrashReport(new CrashReport("Unexpected error", throwable1));
				freeMemory();
				logger.fatal("Unreported exception thrown!", throwable1);
				displayCrashReport(crashreport);
			} finally {
				shutdownMinecraftApplet();
			}

			return;
		}
	}

	private void runGameLoop() {
		mcProfiler.startSection("root");

		if (Display.isCreated() && Display.isCloseRequested()) {
			shutdown();
		}

		if (isGamePaused && theWorld != null) {
			float f = timer.renderPartialTicks;
			timer.updateTimer();
			timer.renderPartialTicks = f;
		} else {
			timer.updateTimer();
		}

		if ((theWorld == null || currentScreen == null) && refreshTexturePacksScheduled) {
			refreshTexturePacksScheduled = false;
			refreshResources();
		}

		long j = System.nanoTime();
		mcProfiler.startSection("tick");

		for (int i = 0; i < timer.elapsedTicks; ++i) {
			runTick();
		}

		mcProfiler.endStartSection("preRenderErrors");
		long k = System.nanoTime() - j;
		checkGLError("Pre render");
		RenderBlocks.fancyGrass = gameSettings.fancyGraphics;
		mcProfiler.endStartSection("sound");
		mcSoundHandler.setListener(thePlayer, timer.renderPartialTicks);
		mcProfiler.endSection();
		mcProfiler.startSection("render");
		GL11.glPushMatrix();
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		framebufferMc.bindFramebuffer(true);
		mcProfiler.startSection("display");
		GL11.glEnable(GL11.GL_TEXTURE_2D);

		if (thePlayer != null && thePlayer.isEntityInsideOpaqueBlock()) {
			gameSettings.thirdPersonView = 0;
		}

		mcProfiler.endSection();

		if (!skipRenderWorld) {
			FMLCommonHandler.instance().onRenderTickStart(timer.renderPartialTicks);
			mcProfiler.endStartSection("gameRenderer");
			entityRenderer.updateCameraAndRender(timer.renderPartialTicks);
			mcProfiler.endSection();
			FMLCommonHandler.instance().onRenderTickEnd(timer.renderPartialTicks);
		}

		GL11.glFlush();
		mcProfiler.endSection();

		if (!Display.isActive() && fullscreen) {
			toggleFullscreen();
		}

		if (gameSettings.showDebugInfo && gameSettings.showDebugProfilerChart) {
			if (!mcProfiler.profilingEnabled) {
				mcProfiler.clearProfiling();
			}

			mcProfiler.profilingEnabled = true;
			displayDebugInfo(k);
		} else {
			mcProfiler.profilingEnabled = false;
			prevFrameTime = System.nanoTime();
		}

		guiAchievement.func_146254_a();
		framebufferMc.unbindFramebuffer();
		GL11.glPopMatrix();
		GL11.glPushMatrix();
		framebufferMc.framebufferRender(displayWidth, displayHeight);
		GL11.glPopMatrix();
		GL11.glPushMatrix();
		entityRenderer.func_152430_c(timer.renderPartialTicks);
		GL11.glPopMatrix();
		mcProfiler.startSection("root");
		func_147120_f();
		Thread.yield();
		mcProfiler.startSection("stream");
		mcProfiler.startSection("update");
		field_152353_at.func_152935_j();
		mcProfiler.endStartSection("submit");
		field_152353_at.func_152922_k();
		mcProfiler.endSection();
		mcProfiler.endSection();
		checkGLError("Post render");
		++fpsCounter;
		isGamePaused = isSingleplayer() && currentScreen != null && currentScreen.doesGuiPauseGame()
				&& !theIntegratedServer.getPublic();

		while (getSystemTime() >= debugUpdateTime + 1000L) {
			debugFPS = fpsCounter;
			debug = debugFPS + " fps, " + WorldRenderer.chunksUpdated + " chunk updates";
			WorldRenderer.chunksUpdated = 0;
			debugUpdateTime += 1000L;
			fpsCounter = 0;
			usageSnooper.addMemoryStatsToSnooper();

			if (!usageSnooper.isSnooperRunning()) {
				usageSnooper.startSnooper();
			}
		}

		mcProfiler.endSection();

		if (isFramerateLimitBelowMax()) {
			Display.sync(getLimitFramerate());
		}
	}

	public void func_147120_f() {
		Display.update();

		if (!fullscreen && Display.wasResized()) {
			int i = displayWidth;
			int j = displayHeight;
			displayWidth = Display.getWidth();
			displayHeight = Display.getHeight();

			if (displayWidth != i || displayHeight != j) {
				if (displayWidth <= 0) {
					displayWidth = 1;
				}

				if (displayHeight <= 0) {
					displayHeight = 1;
				}

				resize(displayWidth, displayHeight);
			}
		}
	}

	public int getLimitFramerate() {
		return theWorld == null && currentScreen != null ? 30 : gameSettings.limitFramerate;
	}

	public boolean isFramerateLimitBelowMax() {
		return getLimitFramerate() < GameSettings.Options.FRAMERATE_LIMIT.getValueMax();
	}

	public void freeMemory() {
		try {
			memoryReserve = new byte[0];
			renderGlobal.deleteAllDisplayLists();
		} catch (Throwable throwable2) {
		}

		try {
			System.gc();
		} catch (Throwable throwable1) {
		}

		try {
			System.gc();
			this.loadWorld(null);
		} catch (Throwable throwable) {
		}

		System.gc();
	}

	@SuppressWarnings("rawtypes")
	private void updateDebugProfilerName(int p_71383_1_) {
		List list = mcProfiler.getProfilingData(debugProfilerName);

		if (list != null && !list.isEmpty()) {
			Profiler.Result result = (Profiler.Result) list.remove(0);

			if (p_71383_1_ == 0) {
				if (result.field_76331_c.length() > 0) {
					int j = debugProfilerName.lastIndexOf(".");

					if (j >= 0) {
						debugProfilerName = debugProfilerName.substring(0, j);
					}
				}
			} else {
				--p_71383_1_;

				if (p_71383_1_ < list.size()
						&& !((Profiler.Result) list.get(p_71383_1_)).field_76331_c.equals("unspecified")) {
					if (debugProfilerName.length() > 0) {
						debugProfilerName = debugProfilerName + ".";
					}

					debugProfilerName = debugProfilerName + ((Profiler.Result) list.get(p_71383_1_)).field_76331_c;
				}
			}
		}
	}

	@SuppressWarnings("rawtypes")
	private void displayDebugInfo(long p_71366_1_) {
		if (mcProfiler.profilingEnabled) {
			List list = mcProfiler.getProfilingData(debugProfilerName);
			Profiler.Result result = (Profiler.Result) list.remove(0);
			GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
			GL11.glMatrixMode(GL11.GL_PROJECTION);
			GL11.glEnable(GL11.GL_COLOR_MATERIAL);
			GL11.glLoadIdentity();
			GL11.glOrtho(0.0D, displayWidth, displayHeight, 0.0D, 1000.0D, 3000.0D);
			GL11.glMatrixMode(GL11.GL_MODELVIEW);
			GL11.glLoadIdentity();
			GL11.glTranslatef(0.0F, 0.0F, -2000.0F);
			GL11.glLineWidth(1.0F);
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			Tessellator tessellator = Tessellator.instance;
			short short1 = 160;
			int j = displayWidth - short1 - 10;
			int k = displayHeight - short1 * 2;
			GL11.glEnable(GL11.GL_BLEND);
			tessellator.startDrawingQuads();
			tessellator.setColorRGBA_I(0, 200);
			tessellator.addVertex(j - short1 * 1.1F, k - short1 * 0.6F - 16.0F, 0.0D);
			tessellator.addVertex(j - short1 * 1.1F, k + short1 * 2, 0.0D);
			tessellator.addVertex(j + short1 * 1.1F, k + short1 * 2, 0.0D);
			tessellator.addVertex(j + short1 * 1.1F, k - short1 * 0.6F - 16.0F, 0.0D);
			tessellator.draw();
			GL11.glDisable(GL11.GL_BLEND);
			double d0 = 0.0D;
			int i1;

			for (int l = 0; l < list.size(); ++l) {
				Profiler.Result result1 = (Profiler.Result) list.get(l);
				i1 = MathHelper.floor_double(result1.field_76332_a / 4.0D) + 1;
				tessellator.startDrawing(6);
				tessellator.setColorOpaque_I(result1.func_76329_a());
				tessellator.addVertex(j, k, 0.0D);
				int j1;
				float f;
				float f1;
				float f2;

				for (j1 = i1; j1 >= 0; --j1) {
					f = (float) ((d0 + result1.field_76332_a * j1 / i1) * Math.PI * 2.0D / 100.0D);
					f1 = MathHelper.sin(f) * short1;
					f2 = MathHelper.cos(f) * short1 * 0.5F;
					tessellator.addVertex(j + f1, k - f2, 0.0D);
				}

				tessellator.draw();
				tessellator.startDrawing(5);
				tessellator.setColorOpaque_I((result1.func_76329_a() & 16711422) >> 1);

				for (j1 = i1; j1 >= 0; --j1) {
					f = (float) ((d0 + result1.field_76332_a * j1 / i1) * Math.PI * 2.0D / 100.0D);
					f1 = MathHelper.sin(f) * short1;
					f2 = MathHelper.cos(f) * short1 * 0.5F;
					tessellator.addVertex(j + f1, k - f2, 0.0D);
					tessellator.addVertex(j + f1, k - f2 + 10.0F, 0.0D);
				}

				tessellator.draw();
				d0 += result1.field_76332_a;
			}

			DecimalFormat decimalformat = new DecimalFormat("##0.00");
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			String s = "";

			if (!result.field_76331_c.equals("unspecified")) {
				s = s + "[0] ";
			}

			if (result.field_76331_c.length() == 0) {
				s = s + "ROOT ";
			} else {
				s = s + result.field_76331_c + " ";
			}

			i1 = 16777215;
			fontRenderer.drawStringWithShadow(s, j - short1, k - short1 / 2 - 16, i1);
			fontRenderer.drawStringWithShadow(s = decimalformat.format(result.field_76330_b) + "%",
					j + short1 - fontRenderer.getStringWidth(s), k - short1 / 2 - 16, i1);

			for (int k1 = 0; k1 < list.size(); ++k1) {
				Profiler.Result result2 = (Profiler.Result) list.get(k1);
				String s1 = "";

				if (result2.field_76331_c.equals("unspecified")) {
					s1 = s1 + "[?] ";
				} else {
					s1 = s1 + "[" + (k1 + 1) + "] ";
				}

				s1 = s1 + result2.field_76331_c;
				fontRenderer.drawStringWithShadow(s1, j - short1, k + short1 / 2 + k1 * 8 + 20, result2.func_76329_a());
				fontRenderer.drawStringWithShadow(s1 = decimalformat.format(result2.field_76332_a) + "%",
						j + short1 - 50 - fontRenderer.getStringWidth(s1), k + short1 / 2 + k1 * 8 + 20,
						result2.func_76329_a());
				fontRenderer.drawStringWithShadow(s1 = decimalformat.format(result2.field_76330_b) + "%",
						j + short1 - fontRenderer.getStringWidth(s1), k + short1 / 2 + k1 * 8 + 20,
						result2.func_76329_a());
			}
		}
	}

	public void shutdown() {
		running = false;
	}

	public void setIngameFocus() {
		if (Display.isActive()) {
			if (!inGameHasFocus) {
				inGameHasFocus = true;
				mouseHelper.grabMouseCursor();
				displayGuiScreen(null);
				leftClickCounter = 10000;
			}
		}
	}

	public void setIngameNotInFocus() {
		if (inGameHasFocus) {
			KeyBinding.unPressAllKeys();
			inGameHasFocus = false;
			mouseHelper.ungrabMouseCursor();
		}
	}

	public void displayInGameMenu() {
		if (currentScreen == null) {
			displayGuiScreen(new GuiIngameMenu());

			if (isSingleplayer() && !theIntegratedServer.getPublic()) {
				mcSoundHandler.pauseSounds();
			}
		}
	}

	private void func_147115_a(boolean p_147115_1_) {
		if (!p_147115_1_) {
			leftClickCounter = 0;
		}

		if (leftClickCounter <= 0) {
			if (p_147115_1_ && objectMouseOver != null
					&& objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
				int i = objectMouseOver.blockX;
				int j = objectMouseOver.blockY;
				int k = objectMouseOver.blockZ;

				if (theWorld.getBlock(i, j, k).getMaterial() != Material.air) {
					playerController.onPlayerDamageBlock(i, j, k, objectMouseOver.sideHit);

					if (thePlayer.isCurrentToolAdventureModeExempt(i, j, k)) {
						effectRenderer.addBlockHitEffects(i, j, k, objectMouseOver);
						thePlayer.swingItem();
					}
				}
			} else {
				playerController.resetBlockRemoving();
			}
		}
	}

	private void func_147116_af() {
		if (leftClickCounter <= 0) {
			thePlayer.swingItem();

			if (objectMouseOver == null) {
				logger.error("Null returned as 'hitResult', this shouldn't happen!");

				if (playerController.isNotCreative()) {
					leftClickCounter = 10;
				}
			} else {
				switch (Minecraft.SwitchMovingObjectType.field_152390_a[objectMouseOver.typeOfHit.ordinal()]) {
				case 1:
					playerController.attackEntity(thePlayer, objectMouseOver.entityHit);
					break;
				case 2:
					int i = objectMouseOver.blockX;
					int j = objectMouseOver.blockY;
					int k = objectMouseOver.blockZ;

					if (theWorld.getBlock(i, j, k).getMaterial() == Material.air) {
						if (playerController.isNotCreative()) {
							leftClickCounter = 10;
						}
					} else {
						playerController.clickBlock(i, j, k, objectMouseOver.sideHit);
					}
				}
			}
		}
	}

	private void func_147121_ag() {
		rightClickDelayTimer = 4;
		boolean flag = true;
		ItemStack itemstack = thePlayer.inventory.getCurrentItem();

		if (objectMouseOver == null) {
			logger.warn("Null returned as 'hitResult', this shouldn't happen!");
		} else {
			switch (Minecraft.SwitchMovingObjectType.field_152390_a[objectMouseOver.typeOfHit.ordinal()]) {
			case 1:
				if (playerController.interactWithEntitySendPacket(thePlayer, objectMouseOver.entityHit)) {
					flag = false;
				}

				break;
			case 2:
				int i = objectMouseOver.blockX;
				int j = objectMouseOver.blockY;
				int k = objectMouseOver.blockZ;

				if (!theWorld.getBlock(i, j, k).isAir(theWorld, i, j, k)) {
					int l = itemstack != null ? itemstack.stackSize : 0;

					boolean result = !net.minecraftforge.event.ForgeEventFactory.onPlayerInteract(thePlayer,
							net.minecraftforge.event.entity.player.PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK, i, j,
							k, objectMouseOver.sideHit, theWorld).isCanceled();
					if (result && playerController.onPlayerRightClick(thePlayer, theWorld, itemstack, i, j, k,
							objectMouseOver.sideHit, objectMouseOver.hitVec)) {
						flag = false;
						thePlayer.swingItem();
					}

					if (itemstack == null)
						return;

					if (itemstack.stackSize == 0) {
						thePlayer.inventory.mainInventory[thePlayer.inventory.currentItem] = null;
					} else if (itemstack.stackSize != l || playerController.isInCreativeMode()) {
						entityRenderer.itemRenderer.resetEquippedProgress();
					}
				}
			}
		}

		if (flag) {
			ItemStack itemstack1 = thePlayer.inventory.getCurrentItem();

			boolean result = !net.minecraftforge.event.ForgeEventFactory.onPlayerInteract(thePlayer,
					net.minecraftforge.event.entity.player.PlayerInteractEvent.Action.RIGHT_CLICK_AIR, 0, 0, 0, -1,
					theWorld).isCanceled();
			if (result && itemstack1 != null && playerController.sendUseItem(thePlayer, theWorld, itemstack1)) {
				entityRenderer.itemRenderer.resetEquippedProgress2();
			}
		}
	}

	public void toggleFullscreen() {
		try {
			fullscreen = !fullscreen;

			if (fullscreen) {
				updateDisplayMode();
				displayWidth = Display.getDisplayMode().getWidth();
				displayHeight = Display.getDisplayMode().getHeight();

				if (displayWidth <= 0) {
					displayWidth = 1;
				}

				if (displayHeight <= 0) {
					displayHeight = 1;
				}
			} else {
				Display.setDisplayMode(new DisplayMode(tempDisplayWidth, tempDisplayHeight));
				displayWidth = tempDisplayWidth;
				displayHeight = tempDisplayHeight;

				if (displayWidth <= 0) {
					displayWidth = 1;
				}

				if (displayHeight <= 0) {
					displayHeight = 1;
				}
			}

			if (currentScreen != null) {
				resize(displayWidth, displayHeight);
			} else {
				updateFramebufferSize();
			}

			Display.setFullscreen(fullscreen);
			Display.setVSyncEnabled(gameSettings.enableVsync);
			func_147120_f();
		} catch (Exception exception) {
			logger.error("Couldn't toggle fullscreen", exception);
		}
	}

	public void resize(int p_71370_1_, int p_71370_2_) {
		displayWidth = p_71370_1_ <= 0 ? 1 : p_71370_1_;
		displayHeight = p_71370_2_ <= 0 ? 1 : p_71370_2_;

		if (currentScreen != null) {
			ScaledResolution scaledresolution = new ScaledResolution(this, p_71370_1_, p_71370_2_);
			int k = scaledresolution.getScaledWidth();
			int l = scaledresolution.getScaledHeight();
			currentScreen.setWorldAndResolution(this, k, l);
		}

		loadingScreen = new LoadingScreenRenderer(this);
		updateFramebufferSize();
	}

	private void updateFramebufferSize() {
		framebufferMc.createBindFramebuffer(displayWidth, displayHeight);

		if (entityRenderer != null) {
			entityRenderer.updateShaderGroupSize(displayWidth, displayHeight);
		}
	}

	@SuppressWarnings("rawtypes")
	public void runTick() {
		mcProfiler.startSection("scheduledExecutables");
		synchronized (field_152351_aB) {
			while (!field_152351_aB.isEmpty()) {
				((FutureTask) field_152351_aB.poll()).run();
			}
		}

		mcProfiler.endSection();

		if (rightClickDelayTimer > 0) {
			--rightClickDelayTimer;
		}

		FMLCommonHandler.instance().onPreClientTick();

		mcProfiler.startSection("gui");

		if (!isGamePaused) {
			ingameGUI.updateTick();
		}

		mcProfiler.endStartSection("pick");
		entityRenderer.getMouseOver(1.0F);
		mcProfiler.endStartSection("gameMode");

		if (!isGamePaused && theWorld != null) {
			playerController.updateController();
		}

		mcProfiler.endStartSection("textures");

		if (!isGamePaused) {
			renderEngine.tick();
		}

		if (currentScreen == null && thePlayer != null) {
			if (thePlayer.getHealth() <= 0.0F) {
				displayGuiScreen(null);
			} else if (thePlayer.isPlayerSleeping() && theWorld != null) {
				displayGuiScreen(new GuiSleepMP());
			}
		} else if (currentScreen != null && currentScreen instanceof GuiSleepMP && !thePlayer.isPlayerSleeping()) {
			displayGuiScreen(null);
		}

		if (currentScreen != null) {
			leftClickCounter = 10000;
		}

		CrashReport crashreport;
		CrashReportCategory crashreportcategory;

		if (currentScreen != null) {
			try {
				currentScreen.handleInput();
			} catch (Throwable throwable1) {
				crashreport = CrashReport.makeCrashReport(throwable1, "Updating screen events");
				crashreportcategory = crashreport.makeCategory("Affected screen");
				crashreportcategory.addCrashSectionCallable("Screen name", new Callable() {
					@Override
					public String call() {
						return currentScreen.getClass().getCanonicalName();
					}
				});
				throw new ReportedException(crashreport);
			}

			if (currentScreen != null) {
				try {
					currentScreen.updateScreen();
				} catch (Throwable throwable) {
					crashreport = CrashReport.makeCrashReport(throwable, "Ticking screen");
					crashreportcategory = crashreport.makeCategory("Affected screen");
					crashreportcategory.addCrashSectionCallable("Screen name", new Callable() {
						@Override
						public String call() {
							return currentScreen.getClass().getCanonicalName();
						}
					});
					throw new ReportedException(crashreport);
				}
			}
		}

		if (currentScreen == null || currentScreen.allowUserInput) {
			mcProfiler.endStartSection("mouse");
			int j;

			while (Mouse.next()) {
				if (net.minecraftforge.client.ForgeHooksClient.postMouseEvent()) {
					continue;
				}

				j = Mouse.getEventButton();
				KeyBinding.setKeyBindState(j - 100, Mouse.getEventButtonState());

				if (Mouse.getEventButtonState()) {
					KeyBinding.onTick(j - 100);
				}

				long k = getSystemTime() - systemTime;

				if (k <= 200L) {
					int i = Mouse.getEventDWheel();

					if (i != 0) {
						thePlayer.inventory.changeCurrentItem(i);

						if (gameSettings.noclip) {
							if (i > 0) {
								i = 1;
							}

							if (i < 0) {
								i = -1;
							}

							gameSettings.noclipRate += i * 0.25F;
						}
					}

					if (currentScreen == null) {
						if (!inGameHasFocus && Mouse.getEventButtonState()) {
							setIngameFocus();
						}
					} else if (currentScreen != null) {
						currentScreen.handleMouseInput();
					}
				}
				FMLCommonHandler.instance().fireMouseInput();
			}

			if (leftClickCounter > 0) {
				--leftClickCounter;
			}

			mcProfiler.endStartSection("keyboard");
			boolean flag;

			while (Keyboard.next()) {
				KeyBinding.setKeyBindState(Keyboard.getEventKey(), Keyboard.getEventKeyState());

				if (Keyboard.getEventKeyState()) {
					KeyBinding.onTick(Keyboard.getEventKey());
				}

				if (field_83002_am > 0L) {
					if (getSystemTime() - field_83002_am >= 6000L)
						throw new ReportedException(new CrashReport("Manually triggered debug crash", new Throwable()));

					if (!Keyboard.isKeyDown(46) || !Keyboard.isKeyDown(61)) {
						field_83002_am = -1L;
					}
				} else if (Keyboard.isKeyDown(46) && Keyboard.isKeyDown(61)) {
					field_83002_am = getSystemTime();
				}

				func_152348_aa();

				if (Keyboard.getEventKeyState()) {
					if (Keyboard.getEventKey() == 62 && entityRenderer != null) {
						entityRenderer.deactivateShader();
					}

					if (currentScreen != null) {
						currentScreen.handleKeyboardInput();
					} else {
						if (Keyboard.getEventKey() == 1) {
							displayInGameMenu();
						}

						if (Keyboard.getEventKey() == 31 && Keyboard.isKeyDown(61)) {
							refreshResources();
						}

						if (Keyboard.getEventKey() == 20 && Keyboard.isKeyDown(61)) {
							refreshResources();
						}

						if (Keyboard.getEventKey() == 33 && Keyboard.isKeyDown(61)) {
							flag = Keyboard.isKeyDown(42) | Keyboard.isKeyDown(54);
							gameSettings.setOptionValue(GameSettings.Options.RENDER_DISTANCE, flag ? -1 : 1);
						}

						if (Keyboard.getEventKey() == 30 && Keyboard.isKeyDown(61)) {
							renderGlobal.loadRenderers();
						}

						if (Keyboard.getEventKey() == 35 && Keyboard.isKeyDown(61)) {
							gameSettings.advancedItemTooltips = !gameSettings.advancedItemTooltips;
							gameSettings.saveOptions();
						}

						if (Keyboard.getEventKey() == 48 && Keyboard.isKeyDown(61)) {
							RenderManager.debugBoundingBox = !RenderManager.debugBoundingBox;
						}

						if (Keyboard.getEventKey() == 25 && Keyboard.isKeyDown(61)) {
							gameSettings.pauseOnLostFocus = !gameSettings.pauseOnLostFocus;
							gameSettings.saveOptions();
						}

						if (Keyboard.getEventKey() == 59) {
							gameSettings.hideGUI = !gameSettings.hideGUI;
						}

						if (Keyboard.getEventKey() == 61) {
							gameSettings.showDebugInfo = !gameSettings.showDebugInfo;
							gameSettings.showDebugProfilerChart = GuiScreen.isShiftKeyDown();
						}

						if (gameSettings.keyBindTogglePerspective.isPressed()) {
							++gameSettings.thirdPersonView;

							if (gameSettings.thirdPersonView > 2) {
								gameSettings.thirdPersonView = 0;
							}
						}

						if (gameSettings.keyBindSmoothCamera.isPressed()) {
							gameSettings.smoothCamera = !gameSettings.smoothCamera;
						}
					}

					if (gameSettings.showDebugInfo && gameSettings.showDebugProfilerChart) {
						if (Keyboard.getEventKey() == 11) {
							updateDebugProfilerName(0);
						}

						for (j = 0; j < 9; ++j) {
							if (Keyboard.getEventKey() == 2 + j) {
								updateDebugProfilerName(j + 1);
							}
						}
					}
				}
				FMLCommonHandler.instance().fireKeyInput();
			}

			for (j = 0; j < 9; ++j) {
				if (gameSettings.keyBindsHotbar[j].isPressed()) {
					thePlayer.inventory.currentItem = j;
				}
			}

			flag = gameSettings.chatVisibility != EntityPlayer.EnumChatVisibility.HIDDEN;

			while (gameSettings.keyBindInventory.isPressed()) {
				if (playerController.func_110738_j()) {
					thePlayer.func_110322_i();
				} else {
					getNetHandler().addToSendQueue(
							new C16PacketClientStatus(C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT));
					displayGuiScreen(new GuiInventory(thePlayer));
				}
			}

			while (gameSettings.keyBindDrop.isPressed()) {
				thePlayer.dropOneItem(GuiScreen.isCtrlKeyDown());
			}

			while (gameSettings.keyBindChat.isPressed() && flag) {
				displayGuiScreen(new GuiChat());
			}

			if (currentScreen == null && gameSettings.keyBindCommand.isPressed() && flag) {
				displayGuiScreen(new GuiChat("/"));
			}

			if (thePlayer.isUsingItem()) {
				if (!gameSettings.keyBindUseItem.getIsKeyPressed()) {
					playerController.onStoppedUsingItem(thePlayer);
				}

				label391:

				while (true) {
					if (!gameSettings.keyBindAttack.isPressed()) {
						while (gameSettings.keyBindUseItem.isPressed()) {
						}

						while (true) {
							if (gameSettings.keyBindPickBlock.isPressed()) {
								continue;
							}

							break label391;
						}
					}
				}
			} else {
				while (gameSettings.keyBindAttack.isPressed()) {
					func_147116_af();
				}

				while (gameSettings.keyBindUseItem.isPressed()) {
					func_147121_ag();
				}

				while (gameSettings.keyBindPickBlock.isPressed()) {
					func_147112_ai();
				}
			}

			if (gameSettings.keyBindUseItem.getIsKeyPressed() && rightClickDelayTimer == 0
					&& !thePlayer.isUsingItem()) {
				func_147121_ag();
			}

			func_147115_a(currentScreen == null && gameSettings.keyBindAttack.getIsKeyPressed() && inGameHasFocus);
		}

		if (theWorld != null) {
			if (thePlayer != null) {
				++joinPlayerCounter;

				if (joinPlayerCounter == 30) {
					joinPlayerCounter = 0;
					theWorld.joinEntityInSurroundings(thePlayer);
				}
			}

			mcProfiler.endStartSection("gameRenderer");

			if (!isGamePaused) {
				entityRenderer.updateRenderer();
			}

			mcProfiler.endStartSection("levelRenderer");

			if (!isGamePaused) {
				renderGlobal.updateClouds();
			}

			mcProfiler.endStartSection("level");

			if (!isGamePaused) {
				if (theWorld.lastLightningBolt > 0) {
					--theWorld.lastLightningBolt;
				}

				theWorld.updateEntities();
			}
		}

		if (!isGamePaused) {
			mcMusicTicker.update();
			mcSoundHandler.update();
		}

		if (theWorld != null) {
			if (!isGamePaused) {
				theWorld.setAllowedSpawnTypes(theWorld.difficultySetting != EnumDifficulty.PEACEFUL, true);

				try {
					theWorld.tick();
				} catch (Throwable throwable2) {
					crashreport = CrashReport.makeCrashReport(throwable2, "Exception in world tick");

					if (theWorld == null) {
						crashreportcategory = crashreport.makeCategory("Affected level");
						crashreportcategory.addCrashSection("Problem", "Level is null!");
					} else {
						theWorld.addWorldInfoToCrashReport(crashreport);
					}

					throw new ReportedException(crashreport);
				}
			}

			mcProfiler.endStartSection("animateTick");

			if (!isGamePaused && theWorld != null) {
				theWorld.doVoidFogParticles(MathHelper.floor_double(thePlayer.posX),
						MathHelper.floor_double(thePlayer.posY), MathHelper.floor_double(thePlayer.posZ));
			}

			mcProfiler.endStartSection("particles");

			if (!isGamePaused) {
				effectRenderer.updateEffects();
			}
		} else if (myNetworkManager != null) {
			mcProfiler.endStartSection("pendingConnection");
			myNetworkManager.processReceivedPackets();
		}

		FMLCommonHandler.instance().onPostClientTick();

		mcProfiler.endSection();
		systemTime = getSystemTime();
	}

	public void launchIntegratedServer(String p_71371_1_, String p_71371_2_, WorldSettings p_71371_3_) {
		FMLClientHandler.instance().startIntegratedServer(p_71371_1_, p_71371_2_, p_71371_3_);
		this.loadWorld(null);
		System.gc();
		ISaveHandler isavehandler = saveLoader.getSaveLoader(p_71371_1_, false);
		WorldInfo worldinfo = isavehandler.loadWorldInfo();

		if (worldinfo == null && p_71371_3_ != null) {
			worldinfo = new WorldInfo(p_71371_3_, p_71371_1_);
			isavehandler.saveWorldInfo(worldinfo);
		}

		if (p_71371_3_ == null) {
			p_71371_3_ = new WorldSettings(worldinfo);
		}

		try {
			theIntegratedServer = new IntegratedServer(this, p_71371_1_, p_71371_2_, p_71371_3_);
			theIntegratedServer.startServerThread();
			integratedServerIsRunning = true;
		} catch (Throwable throwable) {
			CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Starting integrated server");
			CrashReportCategory crashreportcategory = crashreport.makeCategory("Starting integrated server");
			crashreportcategory.addCrashSection("Level ID", p_71371_1_);
			crashreportcategory.addCrashSection("Level Name", p_71371_2_);
			throw new ReportedException(crashreport);
		}

		loadingScreen.displayProgressMessage(I18n.format("menu.loadingLevel"));

		while (!theIntegratedServer.serverIsInRunLoop()) {
			if (!StartupQuery.check()) {
				loadWorld(null);
				displayGuiScreen(null);
				return;
			}
			String s2 = theIntegratedServer.getUserMessage();

			if (s2 != null) {
				loadingScreen.resetProgresAndWorkingMessage(I18n.format(s2));
			} else {
				loadingScreen.resetProgresAndWorkingMessage("");
			}

			try {
				Thread.sleep(200L);
			} catch (InterruptedException interruptedexception) {
			}
		}

		displayGuiScreen(null);
		SocketAddress socketaddress = theIntegratedServer.func_147137_ag().addLocalEndpoint();
		NetworkManager networkmanager = NetworkManager.provideLocalClient(socketaddress);
		networkmanager.setNetHandler(new NetHandlerLoginClient(networkmanager, this, null));
		networkmanager.scheduleOutboundPacket(
				new C00Handshake(5, socketaddress.toString(), 0, EnumConnectionState.LOGIN)
		);
		networkmanager.scheduleOutboundPacket(new C00PacketLoginStart(getSession().func_148256_e())
		);
		myNetworkManager = networkmanager;
	}

	public void loadWorld(WorldClient p_71403_1_) {
		this.loadWorld(p_71403_1_, "");
	}

	public void loadWorld(WorldClient p_71353_1_, String p_71353_2_) {
		if (theWorld != null) {
			net.minecraftforge.common.MinecraftForge.EVENT_BUS
					.post(new net.minecraftforge.event.world.WorldEvent.Unload(theWorld));
		}

		if (p_71353_1_ == null) {
			NetHandlerPlayClient nethandlerplayclient = getNetHandler();

			if (nethandlerplayclient != null) {
				nethandlerplayclient.cleanup();
			}

			if (theIntegratedServer != null) {
				theIntegratedServer.initiateShutdown();
				if (loadingScreen != null) {
					loadingScreen.resetProgresAndWorkingMessage(I18n.format("forge.client.shutdown.internal"));
				}
				while (!theIntegratedServer.isServerStopped()) {
					try {
						Thread.sleep(10);
					} catch (InterruptedException ie) {
					}
				}
			}

			theIntegratedServer = null;
			guiAchievement.func_146257_b();
			entityRenderer.getMapItemRenderer().func_148249_a();
		}

		renderViewEntity = null;
		myNetworkManager = null;

		if (loadingScreen != null) {
			loadingScreen.resetProgressAndMessage(p_71353_2_);
			loadingScreen.resetProgresAndWorkingMessage("");
		}

		if (p_71353_1_ == null && theWorld != null) {
			if (mcResourcePackRepository.func_148530_e() != null) {
				scheduleResourcesRefresh();
			}

			mcResourcePackRepository.func_148529_f();
			setServerData(null);
			integratedServerIsRunning = false;
			FMLClientHandler.instance().handleClientWorldClosing(theWorld);
			((ChunkProviderClient) theWorld.getChunkProvider()).free();
		}

		mcSoundHandler.stopSounds();
		theWorld = p_71353_1_;

		if (p_71353_1_ != null) {
			if (renderGlobal != null) {
				renderGlobal.setWorldAndLoadRenderers(p_71353_1_);
			}

			if (effectRenderer != null) {
				effectRenderer.clearEffects(p_71353_1_);
			}

			if (thePlayer == null) {
				thePlayer = playerController.func_147493_a(p_71353_1_, new StatFileWriter());
				playerController.flipPlayer(thePlayer);
			}

			thePlayer.preparePlayerToSpawn();
			p_71353_1_.spawnEntityInWorld(thePlayer);
			thePlayer.movementInput = new MovementInputFromOptions(gameSettings);
			playerController.setPlayerCapabilities(thePlayer);
			renderViewEntity = thePlayer;
		} else {
			saveLoader.flushCache();
			thePlayer = null;
		}

		System.gc();
		systemTime = 0L;
	}

	public String debugInfoRenders() {
		return renderGlobal.getDebugInfoRenders();
	}

	public String getEntityDebug() {
		return renderGlobal.getDebugInfoEntities();
	}

	public String getWorldProviderName() {
		return theWorld.getProviderName();
	}

	public String debugInfoEntities() {
		return "P: " + effectRenderer.getStatistics() + ". T: " + theWorld.getDebugLoadedEntities();
	}

	public void setDimensionAndSpawnPlayer(int p_71354_1_) {
		theWorld.setSpawnLocation();
		theWorld.removeAllEntities();
		int j = 0;
		String s = null;

		if (thePlayer != null) {
			j = thePlayer.getEntityId();
			theWorld.removeEntity(thePlayer);
			s = thePlayer.func_142021_k();
		}

		renderViewEntity = null;
		thePlayer = playerController.func_147493_a(theWorld,
				thePlayer == null ? new StatFileWriter() : thePlayer.getStatFileWriter());
		thePlayer.dimension = p_71354_1_;
		renderViewEntity = thePlayer;
		thePlayer.preparePlayerToSpawn();
		thePlayer.func_142020_c(s);
		theWorld.spawnEntityInWorld(thePlayer);
		playerController.flipPlayer(thePlayer);
		thePlayer.movementInput = new MovementInputFromOptions(gameSettings);
		thePlayer.setEntityId(j);
		playerController.setPlayerCapabilities(thePlayer);

		if (currentScreen instanceof GuiGameOver) {
			displayGuiScreen(null);
		}
	}

	public final boolean isDemo() {
		return isDemo;
	}

	public NetHandlerPlayClient getNetHandler() {
		return thePlayer != null ? thePlayer.sendQueue : null;
	}

	public static boolean isGuiEnabled() {
		return theMinecraft == null || !theMinecraft.gameSettings.hideGUI;
	}

	public static boolean isFancyGraphicsEnabled() {
		return theMinecraft != null && theMinecraft.gameSettings.fancyGraphics;
	}

	public static boolean isAmbientOcclusionEnabled() {
		return theMinecraft != null && theMinecraft.gameSettings.ambientOcclusion != 0;
	}

	private void func_147112_ai() {
		if (objectMouseOver != null) {
			boolean flag = thePlayer.capabilities.isCreativeMode;
			int j;

			if (!net.minecraftforge.common.ForgeHooks.onPickBlock(objectMouseOver, thePlayer, theWorld))
				return;
			// We delete this code wholly instead of commenting it out, to make sure we
			// detect changes in it between MC versions
			if (flag) {
				j = thePlayer.inventoryContainer.inventorySlots.size() - 9 + thePlayer.inventory.currentItem;
				playerController.sendSlotPacket(thePlayer.inventory.getStackInSlot(thePlayer.inventory.currentItem), j);
			}
		}
	}

	@SuppressWarnings("rawtypes")
	public CrashReport addGraphicsAndWorldToCrashReport(CrashReport p_71396_1_) {
		p_71396_1_.getCategory().addCrashSectionCallable("Launched Version", new Callable() {
			@Override
			public String call() {
				return launchedVersion;
			}
		});
		p_71396_1_.getCategory().addCrashSectionCallable("LWJGL", new Callable() {
			@Override
			public String call() {
				return Sys.getVersion();
			}
		});
		p_71396_1_.getCategory().addCrashSectionCallable("OpenGL", new Callable() {
			@Override
			public String call() {
				return GL11.glGetString(GL11.GL_RENDERER) + " GL version " + GL11.glGetString(GL11.GL_VERSION) + ", "
						+ GL11.glGetString(GL11.GL_VENDOR);
			}
		});
		p_71396_1_.getCategory().addCrashSectionCallable("GL Caps", new Callable() {
			@Override
			public String call() {
				return OpenGlHelper.func_153172_c();
			}
		});
		p_71396_1_.getCategory().addCrashSectionCallable("Is Modded", new Callable() {
			@Override
			public String call() {
				String s = ClientBrandRetriever.getClientModName();
				return !s.equals("vanilla") ? "Definitely; Client brand changed to '" + s + "'"
						: Minecraft.class.getSigners() == null ? "Very likely; Jar signature invalidated"
								: "Probably not. Jar signature remains and client brand is untouched.";
			}
		});
		p_71396_1_.getCategory().addCrashSectionCallable("Type", new Callable() {
			@Override
			public String call() {
				return "Client (map_client.txt)";
			}
		});
		p_71396_1_.getCategory().addCrashSectionCallable("Resource Packs", new Callable() {
			@Override
			public String call() {
				return gameSettings.resourcePacks.toString();
			}
		});
		p_71396_1_.getCategory().addCrashSectionCallable("Current Language", new Callable() {
			@Override
			public String call() {
				return mcLanguageManager.getCurrentLanguage().toString();
			}
		});
		p_71396_1_.getCategory().addCrashSectionCallable("Profiler Position", new Callable() {
			@Override
			public String call() {
				return mcProfiler.profilingEnabled ? mcProfiler.getNameOfLastSection() : "N/A (disabled)";
			}
		});
		p_71396_1_.getCategory().addCrashSectionCallable("Vec3 Pool Size", new Callable() {
			@Override
			public String call() {
				byte b0 = 0;
				int i = 56 * b0;
				int j = i / 1024 / 1024;
				byte b1 = 0;
				int k = 56 * b1;
				int l = k / 1024 / 1024;
				return b0 + " (" + i + " bytes; " + j + " MB) allocated, " + b1 + " (" + k + " bytes; " + l
						+ " MB) used";
			}
		});
		p_71396_1_.getCategory().addCrashSectionCallable("Anisotropic Filtering", new Callable() {
			public String func_152388_a() {
				return gameSettings.anisotropicFiltering == 1 ? "Off (1)"
						: "On (" + gameSettings.anisotropicFiltering + ")";
			}

			@Override
			public Object call() {
				return func_152388_a();
			}
		});

		if (theWorld != null) {
			theWorld.addWorldInfoToCrashReport(p_71396_1_);
		}

		return p_71396_1_;
	}

	public static Minecraft getMinecraft() {
		return theMinecraft;
	}

	public void scheduleResourcesRefresh() {
		refreshTexturePacksScheduled = true;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void addServerStatsToSnooper(PlayerUsageSnooper p_70000_1_) {
		p_70000_1_.func_152768_a("fps", Integer.valueOf(debugFPS));
		p_70000_1_.func_152768_a("vsync_enabled", Boolean.valueOf(gameSettings.enableVsync));
		p_70000_1_.func_152768_a("display_frequency", Integer.valueOf(Display.getDisplayMode().getFrequency()));
		p_70000_1_.func_152768_a("display_type", fullscreen ? "fullscreen" : "windowed");
		p_70000_1_.func_152768_a("run_time", Long.valueOf(
				(MinecraftServer.getSystemTimeMillis() - p_70000_1_.getMinecraftStartTimeMillis()) / 60L * 1000L));
		p_70000_1_.func_152768_a("resource_packs",
				Integer.valueOf(mcResourcePackRepository.getRepositoryEntries().size()));
		int i = 0;
		Iterator iterator = mcResourcePackRepository.getRepositoryEntries().iterator();

		while (iterator.hasNext()) {
			ResourcePackRepository.Entry entry = (ResourcePackRepository.Entry) iterator.next();
			p_70000_1_.func_152768_a("resource_pack[" + i++ + "]", entry.getResourcePackName());
		}

		if (theIntegratedServer != null && theIntegratedServer.getPlayerUsageSnooper() != null) {
			p_70000_1_.func_152768_a("snooper_partner", theIntegratedServer.getPlayerUsageSnooper().getUniqueID());
		}
	}

	@Override
	public void addServerTypeToSnooper(PlayerUsageSnooper p_70001_1_) {
		p_70001_1_.func_152767_b("opengl_version", GL11.glGetString(GL11.GL_VERSION));
		p_70001_1_.func_152767_b("opengl_vendor", GL11.glGetString(GL11.GL_VENDOR));
		p_70001_1_.func_152767_b("client_brand", ClientBrandRetriever.getClientModName());
		p_70001_1_.func_152767_b("launched_version", launchedVersion);
		ContextCapabilities contextcapabilities = GLContext.getCapabilities();
		p_70001_1_.func_152767_b("gl_caps[ARB_arrays_of_arrays]",
				Boolean.valueOf(contextcapabilities.GL_ARB_arrays_of_arrays));
		p_70001_1_.func_152767_b("gl_caps[ARB_base_instance]",
				Boolean.valueOf(contextcapabilities.GL_ARB_base_instance));
		p_70001_1_.func_152767_b("gl_caps[ARB_blend_func_extended]",
				Boolean.valueOf(contextcapabilities.GL_ARB_blend_func_extended));
		p_70001_1_.func_152767_b("gl_caps[ARB_clear_buffer_object]",
				Boolean.valueOf(contextcapabilities.GL_ARB_clear_buffer_object));
		p_70001_1_.func_152767_b("gl_caps[ARB_color_buffer_float]",
				Boolean.valueOf(contextcapabilities.GL_ARB_color_buffer_float));
		p_70001_1_.func_152767_b("gl_caps[ARB_compatibility]",
				Boolean.valueOf(contextcapabilities.GL_ARB_compatibility));
		p_70001_1_.func_152767_b("gl_caps[ARB_compressed_texture_pixel_storage]",
				Boolean.valueOf(contextcapabilities.GL_ARB_compressed_texture_pixel_storage));
		p_70001_1_.func_152767_b("gl_caps[ARB_compute_shader]",
				Boolean.valueOf(contextcapabilities.GL_ARB_compute_shader));
		p_70001_1_.func_152767_b("gl_caps[ARB_copy_buffer]", Boolean.valueOf(contextcapabilities.GL_ARB_copy_buffer));
		p_70001_1_.func_152767_b("gl_caps[ARB_copy_image]", Boolean.valueOf(contextcapabilities.GL_ARB_copy_image));
		p_70001_1_.func_152767_b("gl_caps[ARB_depth_buffer_float]",
				Boolean.valueOf(contextcapabilities.GL_ARB_depth_buffer_float));
		p_70001_1_.func_152767_b("gl_caps[ARB_compute_shader]",
				Boolean.valueOf(contextcapabilities.GL_ARB_compute_shader));
		p_70001_1_.func_152767_b("gl_caps[ARB_copy_buffer]", Boolean.valueOf(contextcapabilities.GL_ARB_copy_buffer));
		p_70001_1_.func_152767_b("gl_caps[ARB_copy_image]", Boolean.valueOf(contextcapabilities.GL_ARB_copy_image));
		p_70001_1_.func_152767_b("gl_caps[ARB_depth_buffer_float]",
				Boolean.valueOf(contextcapabilities.GL_ARB_depth_buffer_float));
		p_70001_1_.func_152767_b("gl_caps[ARB_depth_clamp]", Boolean.valueOf(contextcapabilities.GL_ARB_depth_clamp));
		p_70001_1_.func_152767_b("gl_caps[ARB_depth_texture]",
				Boolean.valueOf(contextcapabilities.GL_ARB_depth_texture));
		p_70001_1_.func_152767_b("gl_caps[ARB_draw_buffers]", Boolean.valueOf(contextcapabilities.GL_ARB_draw_buffers));
		p_70001_1_.func_152767_b("gl_caps[ARB_draw_buffers_blend]",
				Boolean.valueOf(contextcapabilities.GL_ARB_draw_buffers_blend));
		p_70001_1_.func_152767_b("gl_caps[ARB_draw_elements_base_vertex]",
				Boolean.valueOf(contextcapabilities.GL_ARB_draw_elements_base_vertex));
		p_70001_1_.func_152767_b("gl_caps[ARB_draw_indirect]",
				Boolean.valueOf(contextcapabilities.GL_ARB_draw_indirect));
		p_70001_1_.func_152767_b("gl_caps[ARB_draw_instanced]",
				Boolean.valueOf(contextcapabilities.GL_ARB_draw_instanced));
		p_70001_1_.func_152767_b("gl_caps[ARB_explicit_attrib_location]",
				Boolean.valueOf(contextcapabilities.GL_ARB_explicit_attrib_location));
		p_70001_1_.func_152767_b("gl_caps[ARB_explicit_uniform_location]",
				Boolean.valueOf(contextcapabilities.GL_ARB_explicit_uniform_location));
		p_70001_1_.func_152767_b("gl_caps[ARB_fragment_layer_viewport]",
				Boolean.valueOf(contextcapabilities.GL_ARB_fragment_layer_viewport));
		p_70001_1_.func_152767_b("gl_caps[ARB_fragment_program]",
				Boolean.valueOf(contextcapabilities.GL_ARB_fragment_program));
		p_70001_1_.func_152767_b("gl_caps[ARB_fragment_shader]",
				Boolean.valueOf(contextcapabilities.GL_ARB_fragment_shader));
		p_70001_1_.func_152767_b("gl_caps[ARB_fragment_program_shadow]",
				Boolean.valueOf(contextcapabilities.GL_ARB_fragment_program_shadow));
		p_70001_1_.func_152767_b("gl_caps[ARB_framebuffer_object]",
				Boolean.valueOf(contextcapabilities.GL_ARB_framebuffer_object));
		p_70001_1_.func_152767_b("gl_caps[ARB_framebuffer_sRGB]",
				Boolean.valueOf(contextcapabilities.GL_ARB_framebuffer_sRGB));
		p_70001_1_.func_152767_b("gl_caps[ARB_geometry_shader4]",
				Boolean.valueOf(contextcapabilities.GL_ARB_geometry_shader4));
		p_70001_1_.func_152767_b("gl_caps[ARB_gpu_shader5]", Boolean.valueOf(contextcapabilities.GL_ARB_gpu_shader5));
		p_70001_1_.func_152767_b("gl_caps[ARB_half_float_pixel]",
				Boolean.valueOf(contextcapabilities.GL_ARB_half_float_pixel));
		p_70001_1_.func_152767_b("gl_caps[ARB_half_float_vertex]",
				Boolean.valueOf(contextcapabilities.GL_ARB_half_float_vertex));
		p_70001_1_.func_152767_b("gl_caps[ARB_instanced_arrays]",
				Boolean.valueOf(contextcapabilities.GL_ARB_instanced_arrays));
		p_70001_1_.func_152767_b("gl_caps[ARB_map_buffer_alignment]",
				Boolean.valueOf(contextcapabilities.GL_ARB_map_buffer_alignment));
		p_70001_1_.func_152767_b("gl_caps[ARB_map_buffer_range]",
				Boolean.valueOf(contextcapabilities.GL_ARB_map_buffer_range));
		p_70001_1_.func_152767_b("gl_caps[ARB_multisample]", Boolean.valueOf(contextcapabilities.GL_ARB_multisample));
		p_70001_1_.func_152767_b("gl_caps[ARB_multitexture]", Boolean.valueOf(contextcapabilities.GL_ARB_multitexture));
		p_70001_1_.func_152767_b("gl_caps[ARB_occlusion_query2]",
				Boolean.valueOf(contextcapabilities.GL_ARB_occlusion_query2));
		p_70001_1_.func_152767_b("gl_caps[ARB_pixel_buffer_object]",
				Boolean.valueOf(contextcapabilities.GL_ARB_pixel_buffer_object));
		p_70001_1_.func_152767_b("gl_caps[ARB_seamless_cube_map]",
				Boolean.valueOf(contextcapabilities.GL_ARB_seamless_cube_map));
		p_70001_1_.func_152767_b("gl_caps[ARB_shader_objects]",
				Boolean.valueOf(contextcapabilities.GL_ARB_shader_objects));
		p_70001_1_.func_152767_b("gl_caps[ARB_shader_stencil_export]",
				Boolean.valueOf(contextcapabilities.GL_ARB_shader_stencil_export));
		p_70001_1_.func_152767_b("gl_caps[ARB_shader_texture_lod]",
				Boolean.valueOf(contextcapabilities.GL_ARB_shader_texture_lod));
		p_70001_1_.func_152767_b("gl_caps[ARB_shadow]", Boolean.valueOf(contextcapabilities.GL_ARB_shadow));
		p_70001_1_.func_152767_b("gl_caps[ARB_shadow_ambient]",
				Boolean.valueOf(contextcapabilities.GL_ARB_shadow_ambient));
		p_70001_1_.func_152767_b("gl_caps[ARB_stencil_texturing]",
				Boolean.valueOf(contextcapabilities.GL_ARB_stencil_texturing));
		p_70001_1_.func_152767_b("gl_caps[ARB_sync]", Boolean.valueOf(contextcapabilities.GL_ARB_sync));
		p_70001_1_.func_152767_b("gl_caps[ARB_tessellation_shader]",
				Boolean.valueOf(contextcapabilities.GL_ARB_tessellation_shader));
		p_70001_1_.func_152767_b("gl_caps[ARB_texture_border_clamp]",
				Boolean.valueOf(contextcapabilities.GL_ARB_texture_border_clamp));
		p_70001_1_.func_152767_b("gl_caps[ARB_texture_buffer_object]",
				Boolean.valueOf(contextcapabilities.GL_ARB_texture_buffer_object));
		p_70001_1_.func_152767_b("gl_caps[ARB_texture_cube_map]",
				Boolean.valueOf(contextcapabilities.GL_ARB_texture_cube_map));
		p_70001_1_.func_152767_b("gl_caps[ARB_texture_cube_map_array]",
				Boolean.valueOf(contextcapabilities.GL_ARB_texture_cube_map_array));
		p_70001_1_.func_152767_b("gl_caps[ARB_texture_non_power_of_two]",
				Boolean.valueOf(contextcapabilities.GL_ARB_texture_non_power_of_two));
		p_70001_1_.func_152767_b("gl_caps[ARB_uniform_buffer_object]",
				Boolean.valueOf(contextcapabilities.GL_ARB_uniform_buffer_object));
		p_70001_1_.func_152767_b("gl_caps[ARB_vertex_blend]", Boolean.valueOf(contextcapabilities.GL_ARB_vertex_blend));
		p_70001_1_.func_152767_b("gl_caps[ARB_vertex_buffer_object]",
				Boolean.valueOf(contextcapabilities.GL_ARB_vertex_buffer_object));
		p_70001_1_.func_152767_b("gl_caps[ARB_vertex_program]",
				Boolean.valueOf(contextcapabilities.GL_ARB_vertex_program));
		p_70001_1_.func_152767_b("gl_caps[ARB_vertex_shader]",
				Boolean.valueOf(contextcapabilities.GL_ARB_vertex_shader));
		p_70001_1_.func_152767_b("gl_caps[EXT_bindable_uniform]",
				Boolean.valueOf(contextcapabilities.GL_EXT_bindable_uniform));
		p_70001_1_.func_152767_b("gl_caps[EXT_blend_equation_separate]",
				Boolean.valueOf(contextcapabilities.GL_EXT_blend_equation_separate));
		p_70001_1_.func_152767_b("gl_caps[EXT_blend_func_separate]",
				Boolean.valueOf(contextcapabilities.GL_EXT_blend_func_separate));
		p_70001_1_.func_152767_b("gl_caps[EXT_blend_minmax]", Boolean.valueOf(contextcapabilities.GL_EXT_blend_minmax));
		p_70001_1_.func_152767_b("gl_caps[EXT_blend_subtract]",
				Boolean.valueOf(contextcapabilities.GL_EXT_blend_subtract));
		p_70001_1_.func_152767_b("gl_caps[EXT_draw_instanced]",
				Boolean.valueOf(contextcapabilities.GL_EXT_draw_instanced));
		p_70001_1_.func_152767_b("gl_caps[EXT_framebuffer_multisample]",
				Boolean.valueOf(contextcapabilities.GL_EXT_framebuffer_multisample));
		p_70001_1_.func_152767_b("gl_caps[EXT_framebuffer_object]",
				Boolean.valueOf(contextcapabilities.GL_EXT_framebuffer_object));
		p_70001_1_.func_152767_b("gl_caps[EXT_framebuffer_sRGB]",
				Boolean.valueOf(contextcapabilities.GL_EXT_framebuffer_sRGB));
		p_70001_1_.func_152767_b("gl_caps[EXT_geometry_shader4]",
				Boolean.valueOf(contextcapabilities.GL_EXT_geometry_shader4));
		p_70001_1_.func_152767_b("gl_caps[EXT_gpu_program_parameters]",
				Boolean.valueOf(contextcapabilities.GL_EXT_gpu_program_parameters));
		p_70001_1_.func_152767_b("gl_caps[EXT_gpu_shader4]", Boolean.valueOf(contextcapabilities.GL_EXT_gpu_shader4));
		p_70001_1_.func_152767_b("gl_caps[EXT_multi_draw_arrays]",
				Boolean.valueOf(contextcapabilities.GL_EXT_multi_draw_arrays));
		p_70001_1_.func_152767_b("gl_caps[EXT_packed_depth_stencil]",
				Boolean.valueOf(contextcapabilities.GL_EXT_packed_depth_stencil));
		p_70001_1_.func_152767_b("gl_caps[EXT_paletted_texture]",
				Boolean.valueOf(contextcapabilities.GL_EXT_paletted_texture));
		p_70001_1_.func_152767_b("gl_caps[EXT_rescale_normal]",
				Boolean.valueOf(contextcapabilities.GL_EXT_rescale_normal));
		p_70001_1_.func_152767_b("gl_caps[EXT_separate_shader_objects]",
				Boolean.valueOf(contextcapabilities.GL_EXT_separate_shader_objects));
		p_70001_1_.func_152767_b("gl_caps[EXT_shader_image_load_store]",
				Boolean.valueOf(contextcapabilities.GL_EXT_shader_image_load_store));
		p_70001_1_.func_152767_b("gl_caps[EXT_shadow_funcs]", Boolean.valueOf(contextcapabilities.GL_EXT_shadow_funcs));
		p_70001_1_.func_152767_b("gl_caps[EXT_shared_texture_palette]",
				Boolean.valueOf(contextcapabilities.GL_EXT_shared_texture_palette));
		p_70001_1_.func_152767_b("gl_caps[EXT_stencil_clear_tag]",
				Boolean.valueOf(contextcapabilities.GL_EXT_stencil_clear_tag));
		p_70001_1_.func_152767_b("gl_caps[EXT_stencil_two_side]",
				Boolean.valueOf(contextcapabilities.GL_EXT_stencil_two_side));
		p_70001_1_.func_152767_b("gl_caps[EXT_stencil_wrap]", Boolean.valueOf(contextcapabilities.GL_EXT_stencil_wrap));
		p_70001_1_.func_152767_b("gl_caps[EXT_texture_3d]", Boolean.valueOf(contextcapabilities.GL_EXT_texture_3d));
		p_70001_1_.func_152767_b("gl_caps[EXT_texture_array]",
				Boolean.valueOf(contextcapabilities.GL_EXT_texture_array));
		p_70001_1_.func_152767_b("gl_caps[EXT_texture_buffer_object]",
				Boolean.valueOf(contextcapabilities.GL_EXT_texture_buffer_object));
		p_70001_1_.func_152767_b("gl_caps[EXT_texture_filter_anisotropic]",
				Boolean.valueOf(contextcapabilities.GL_EXT_texture_filter_anisotropic));
		p_70001_1_.func_152767_b("gl_caps[EXT_texture_integer]",
				Boolean.valueOf(contextcapabilities.GL_EXT_texture_integer));
		p_70001_1_.func_152767_b("gl_caps[EXT_texture_lod_bias]",
				Boolean.valueOf(contextcapabilities.GL_EXT_texture_lod_bias));
		p_70001_1_.func_152767_b("gl_caps[EXT_texture_sRGB]", Boolean.valueOf(contextcapabilities.GL_EXT_texture_sRGB));
		p_70001_1_.func_152767_b("gl_caps[EXT_vertex_shader]",
				Boolean.valueOf(contextcapabilities.GL_EXT_vertex_shader));
		p_70001_1_.func_152767_b("gl_caps[EXT_vertex_weighting]",
				Boolean.valueOf(contextcapabilities.GL_EXT_vertex_weighting));
		p_70001_1_.func_152767_b("gl_caps[gl_max_vertex_uniforms]",
				Integer.valueOf(GL11.glGetInteger(GL20.GL_MAX_VERTEX_UNIFORM_COMPONENTS)));
		GL11.glGetError();
		p_70001_1_.func_152767_b("gl_caps[gl_max_fragment_uniforms]",
				Integer.valueOf(GL11.glGetInteger(GL20.GL_MAX_FRAGMENT_UNIFORM_COMPONENTS)));
		GL11.glGetError();
		p_70001_1_.func_152767_b("gl_caps[gl_max_vertex_attribs]",
				Integer.valueOf(GL11.glGetInteger(GL20.GL_MAX_VERTEX_ATTRIBS)));
		GL11.glGetError();
		p_70001_1_.func_152767_b("gl_caps[gl_max_vertex_texture_image_units]",
				Integer.valueOf(GL11.glGetInteger(GL20.GL_MAX_VERTEX_TEXTURE_IMAGE_UNITS)));
		GL11.glGetError();
		p_70001_1_.func_152767_b("gl_caps[gl_max_texture_image_units]",
				Integer.valueOf(GL11.glGetInteger(GL20.GL_MAX_TEXTURE_IMAGE_UNITS)));
		GL11.glGetError();
		p_70001_1_.func_152767_b("gl_caps[gl_max_texture_image_units]", Integer.valueOf(GL11.glGetInteger(35071)));
		GL11.glGetError();
		p_70001_1_.func_152767_b("gl_max_texture_size", Integer.valueOf(getGLMaximumTextureSize()));
	}

	// Forge: Adds a optimization to the getGLMaximumTextureSize, only calculate it
	// once.
	private static int max_texture_size = -1;

	public static int getGLMaximumTextureSize() {
		if (max_texture_size != -1)
			return max_texture_size;

		for (int i = 16384; i > 0; i >>= 1) {
			GL11.glTexImage2D(GL11.GL_PROXY_TEXTURE_2D, 0, GL11.GL_RGBA, i, i, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE,
					(ByteBuffer) null);
			int j = GL11.glGetTexLevelParameteri(GL11.GL_PROXY_TEXTURE_2D, 0, GL11.GL_TEXTURE_WIDTH);

			if (j != 0) {
				max_texture_size = i;
				return i;
			}
		}

		return -1;
	}

	@Override
	public boolean isSnooperEnabled() {
		return gameSettings.snooperEnabled;
	}

	public void setServerData(ServerData p_71351_1_) {
		currentServerData = p_71351_1_;
	}

	public ServerData func_147104_D() {
		return currentServerData;
	}

	public boolean isIntegratedServerRunning() {
		return integratedServerIsRunning;
	}

	public boolean isSingleplayer() {
		return integratedServerIsRunning && theIntegratedServer != null;
	}

	public IntegratedServer getIntegratedServer() {
		return theIntegratedServer;
	}

	public static void stopIntegratedServer() {
		if (theMinecraft != null) {
			IntegratedServer integratedserver = theMinecraft.getIntegratedServer();

			if (integratedserver != null) {
				integratedserver.stopServer();
			}
		}
	}

	public PlayerUsageSnooper getPlayerUsageSnooper() {
		return usageSnooper;
	}

	public static long getSystemTime() {
		return Sys.getTime() * 1000L / Sys.getTimerResolution();
	}

	public boolean isFullScreen() {
		return fullscreen;
	}

	public Session getSession() {
		return session;
	}

	@SuppressWarnings("rawtypes")
	public Multimap func_152341_N() {
		return field_152356_J;
	}

	public Proxy getProxy() {
		return proxy;
	}

	public TextureManager getTextureManager() {
		return renderEngine;
	}

	public IResourceManager getResourceManager() {
		return mcResourceManager;
	}

	public ResourcePackRepository getResourcePackRepository() {
		return mcResourcePackRepository;
	}

	public LanguageManager getLanguageManager() {
		return mcLanguageManager;
	}

	public TextureMap getTextureMapBlocks() {
		return textureMapBlocks;
	}

	public boolean isJava64bit() {
		return jvm64bit;
	}

	public boolean isGamePaused() {
		return isGamePaused;
	}

	public SoundHandler getSoundHandler() {
		return mcSoundHandler;
	}

	public MusicTicker.MusicType func_147109_W() {
		return currentScreen instanceof GuiWinGame ? MusicTicker.MusicType.CREDITS
				: thePlayer != null
						? thePlayer.worldObj.provider instanceof WorldProviderHell ? MusicTicker.MusicType.NETHER
								: thePlayer.worldObj.provider instanceof WorldProviderEnd
										? BossStatus.bossName != null && BossStatus.statusBarTime > 0
												? MusicTicker.MusicType.END_BOSS
												: MusicTicker.MusicType.END
										: thePlayer.capabilities.isCreativeMode && thePlayer.capabilities.allowFlying
												? MusicTicker.MusicType.CREATIVE
												: MusicTicker.MusicType.GAME
						: MusicTicker.MusicType.MENU;
	}

	public IStream func_152346_Z() {
		return field_152353_at;
	}

	public void func_152348_aa() {
		int i = Keyboard.getEventKey();

		if (i != 0 && !Keyboard.isRepeatEvent()) {
			if (!(currentScreen instanceof GuiControls)
					|| ((GuiControls) currentScreen).field_152177_g <= getSystemTime() - 20L) {
				if (Keyboard.getEventKeyState()) {
					if (i == gameSettings.field_152396_an.getKeyCode()) {
						if (func_152346_Z().func_152934_n()) {
							func_152346_Z().func_152914_u();
						} else if (func_152346_Z().func_152924_m()) {
							displayGuiScreen(new GuiYesNo(new GuiYesNoCallback() {
								@Override
								public void confirmClicked(boolean p_73878_1_, int p_73878_2_) {
									if (p_73878_1_) {
										Minecraft.this.func_152346_Z().func_152930_t();
									}

									Minecraft.this.displayGuiScreen(null);
								}
							}, I18n.format("stream.confirm_start"), "", 0));
						} else if (func_152346_Z().func_152928_D() && func_152346_Z().func_152936_l()) {
							if (theWorld != null) {
								ingameGUI.getChatGUI()
										.printChatMessage(new ChatComponentText("Not ready to start streaming yet!"));
							}
						} else {
							GuiStreamUnavailable.func_152321_a(currentScreen);
						}
					} else if (i == gameSettings.field_152397_ao.getKeyCode()) {
						if (func_152346_Z().func_152934_n()) {
							if (func_152346_Z().func_152919_o()) {
								func_152346_Z().func_152933_r();
							} else {
								func_152346_Z().func_152916_q();
							}
						}
					} else if (i == gameSettings.field_152398_ap.getKeyCode()) {
						if (func_152346_Z().func_152934_n()) {
							func_152346_Z().func_152931_p();
						}
					} else if (i == gameSettings.field_152399_aq.getKeyCode()) {
						field_152353_at.func_152910_a(true);
					} else if (i == gameSettings.field_152395_am.getKeyCode()) {
						toggleFullscreen();
					} else if (i == gameSettings.keyBindScreenshot.getKeyCode()) {
						ingameGUI.getChatGUI().printChatMessage(
								ScreenShotHelper.saveScreenshot(mcDataDir, displayWidth, displayHeight, framebufferMc));
					}
				} else if (i == gameSettings.field_152399_aq.getKeyCode()) {
					field_152353_at.func_152910_a(false);
				}
			}
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ListenableFuture func_152343_a(Callable p_152343_1_) {
		Validate.notNull(p_152343_1_);

		if (!func_152345_ab()) {
			ListenableFutureTask listenablefuturetask = ListenableFutureTask.create(p_152343_1_);
			synchronized (field_152351_aB) {
				field_152351_aB.add(listenablefuturetask);
				return listenablefuturetask;
			}
		} else {
			try {
				return Futures.immediateFuture(p_152343_1_.call());
			} catch (Exception exception) {
				return Futures.immediateFailedCheckedFuture(exception);
			}
		}
	}

	@SuppressWarnings("rawtypes")
	public ListenableFuture func_152344_a(Runnable p_152344_1_) {
		Validate.notNull(p_152344_1_);
		return func_152343_a(Executors.callable(p_152344_1_));
	}

	public boolean func_152345_ab() {
		return Thread.currentThread() == field_152352_aC;
	}

	public MinecraftSessionService func_152347_ac() {
		return field_152355_az;
	}

	public SkinManager func_152342_ad() {
		return field_152350_aA;
	}

	@SideOnly(Side.CLIENT)

	static final class SwitchMovingObjectType {
		static final int[] field_152390_a = new int[MovingObjectPosition.MovingObjectType.values().length];
		static {
			try {
				field_152390_a[MovingObjectPosition.MovingObjectType.ENTITY.ordinal()] = 1;
			} catch (NoSuchFieldError var2) {
			}

			try {
				field_152390_a[MovingObjectPosition.MovingObjectType.BLOCK.ordinal()] = 2;
			} catch (NoSuchFieldError var1) {
			}
		}
	}
}