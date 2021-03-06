package net.minecraft.server.dedicated;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.FMLLaunchHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.ServerCommand;
import net.minecraft.crash.CrashReport;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.rcon.IServer;
import net.minecraft.network.rcon.RConThreadMain;
import net.minecraft.network.rcon.RConThreadQuery;
import net.minecraft.profiler.PlayerUsageSnooper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.gui.MinecraftServerGui;
import net.minecraft.server.management.PlayerProfileCache;
import net.minecraft.server.management.PreYggdrasilConverter;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.CryptManager;
import net.minecraft.util.MathHelper;
import net.minecraft.world.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.ultramine.core.permissions.MinecraftPermissions;
import org.ultramine.core.permissions.Permissions;
import org.ultramine.core.service.InjectService;
import org.ultramine.server.BackupManager;
import org.ultramine.server.ConfigurationHandler;
import org.ultramine.server.UltramineServerConfig;
import org.ultramine.server.WorldsConfig.WorldConfig;
import org.ultramine.server.bootstrap.UMBootstrap;
import org.ultramine.server.internal.JLineSupport;
import org.ultramine.server.internal.UMHooks;
import org.ultramine.server.util.BasicTypeParser;
import org.ultramine.server.util.GlobalExecutors;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

@SideOnly(Side.SERVER)
public class DedicatedServer extends MinecraftServer implements IServer {
	private static final Logger field_155771_h = LogManager.getLogger();
	public final List pendingCommandList = Collections.synchronizedList(new ArrayList());
	private RConThreadQuery theRConThreadQuery;
	private RConThreadMain theRConThreadMain;
	private UltramineServerConfig settings;
	private boolean canSpawnStructures;
	private WorldSettings.GameType gameType;
	private boolean guiIsEnabled;
	public static boolean allowPlayerLogins = false;
	private static final String __OBFID = "CL_00001784";

	public DedicatedServer(File p_i1508_1_) {
		super(p_i1508_1_, Proxy.NO_PROXY);
		field_152367_a = getVanillaFile("usercache.json");
		field_152366_X = new PlayerProfileCache(this, field_152367_a);
		new Thread("Server Infinisleeper") {
			private static final String __OBFID = "CL_00001787";
			{
				setDaemon(true);
				start();
			}

			@Override
			public void run() {
				while (true) {
					try {
						while (true) {
							Thread.sleep(2147483647L);
						}
					} catch (InterruptedException interruptedexception) {
					}
				}
			}
		};
	}

	@Override
	protected boolean startServer() throws IOException {
		Thread thread = UMBootstrap.isJLine() ? JLineSupport.setupReadingThread(this)
				: new Thread("Server console handler") {
					private static final String __OBFID = "CL_00001786";

					@Override
					public void run() {
						BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(System.in));
						String s4;

						try {
							while (!DedicatedServer.this.isServerStopped() && DedicatedServer.this.isServerRunning()
									&& (s4 = bufferedreader.readLine()) != null) {
								DedicatedServer.this.addPendingCommand(s4, DedicatedServer.this);
							}
						} catch (IOException ioexception1) {
							DedicatedServer.field_155771_h.error("Exception handling console input", ioexception1);
						}
					}
				};
		thread.setDaemon(true);
		thread.start();
		field_155771_h.info("Starting minecraft server version 1.7.10");

		if (Runtime.getRuntime().maxMemory() / 1024L / 1024L < 512L) {
			field_155771_h.warn(
					"To start the server with more ram, launch it as \"java -Xmx1024M -Xms1024M -jar minecraft_server.jar\"");
		}

		FMLCommonHandler.instance().onServerStart(this);

		field_155771_h.info("Loading properties");
		settings = ConfigurationHandler.getServerConfig();
		WorldConfig globalWConf = ConfigurationHandler.getWorldsConfig().global;

		if (isSinglePlayer()) {
			setHostname("127.0.0.1");
		} else {
			setOnlineMode(settings.settings.authorization.onlineMode);
			setHostname(settings.listen.minecraft.serverIP);
		}

		setCanSpawnAnimals(globalWConf.mobSpawn.spawnAnimals);
		setCanSpawnNPCs(globalWConf.mobSpawn.allowNPCs);
		setAllowPvp(globalWConf.settings.pvp);
		setAllowFlight(settings.settings.security.allowFlight);
		func_155759_m(settings.settings.other.resourcePack);
		setMOTD(settings.settings.messages.motd);
		setForceGamemode(settings.settings.player.forceGamemode);
		func_143006_e(settings.settings.player.playerIdleTimeout);

		canSpawnStructures = globalWConf.generation.generateStructures;
		int i = settings.settings.player.gamemode;
		gameType = WorldSettings.getGameTypeById(i);
		field_155771_h.info("Default game type: " + gameType);
		InetAddress inetaddress = null;

		if (getServerHostname().length() > 0) {
			inetaddress = InetAddress.getByName(getServerHostname());
		}

		if (getServerPort() < 0) {
			setServerPort(settings.listen.minecraft.port);
		}

		field_155771_h.info("Generating keypair");
		setKeyPair(CryptManager.createNewKeyPair());
		field_155771_h.info("Starting Minecraft server on "
				+ (getServerHostname().length() == 0 ? "*" : getServerHostname()) + ":" + getServerPort());

		try {
			func_147137_ag().addLanEndpoint(inetaddress, getServerPort());
		} catch (IOException ioexception) {
			field_155771_h.warn("**** FAILED TO BIND TO PORT!");
			field_155771_h.warn("The exception was: {}", ioexception.toString());
			field_155771_h.warn("Perhaps a server is already running on that port?");
			return false;
		}

		if (!isServerInOnlineMode()) {
			field_155771_h.warn("**** SERVER IS RUNNING IN OFFLINE/INSECURE MODE!");
			field_155771_h.warn("The server will make no attempt to authenticate usernames. Beware.");
			field_155771_h.warn(
					"While this makes the game possible to play without internet access, it also opens up the ability for hackers to connect with any username they choose.");
			field_155771_h.warn("To change this, set \"online-mode\" to \"true\" in the server.properties file.");
		}

		if (func_152368_aE()) {
			func_152358_ax().func_152658_c();
		}

		if (!PreYggdrasilConverter.func_152714_a(getWorldsDir()))
			return false;

		FMLCommonHandler.instance().onServerStarted();
		func_152361_a(new DedicatedPlayerList(this));
		long j = System.nanoTime();

		if (getFolderName() == null) {
			setFolderName("world");
		}

		String s = globalWConf.generation.seed;
		String s1 = globalWConf.generation.levelType;
		String s2 = globalWConf.generation.generatorSettings;
		long k = new Random().nextLong();

		if (s.length() > 0) {
			try {
				long l = Long.parseLong(s);

				if (l != 0L) {
					k = l;
				}
			} catch (NumberFormatException numberformatexception) {
				k = s.hashCode();
			}
		}

		WorldType worldtype = WorldType.parseWorldType(s1);

		if (worldtype == null) {
			worldtype = WorldType.DEFAULT;
		}

		func_147136_ar();
		isCommandBlockEnabled();
		getOpPermissionLevel();
		isSnooperEnabled();
		setBuildLimit(globalWConf.settings.maxBuildHeight);
		setBuildLimit((getBuildLimit() + 8) / 16 * 16);
		setBuildLimit(MathHelper.clamp_int(getBuildLimit(), 64, 256));
		globalWConf.settings.maxBuildHeight = getBuildLimit();
		if (!FMLCommonHandler.instance().handleServerAboutToStart(this))
			return false;
		field_155771_h.info("Preparing level \"" + getFolderName() + "\"");
		loadAllWorlds(getFolderName(), getFolderName(), k, worldtype, s2);
		long i1 = System.nanoTime() - j;
		String s3 = String.format("%.3fs", Double.valueOf(i1 / 1.0E9D));
		field_155771_h.info("Done (" + s3 + ")! For help, type \"help\" or \"?\"");

		if (settings.listen.query.enabled) {
			field_155771_h.info("Starting GS4 status listener");
			theRConThreadQuery = new RConThreadQuery(this);
			theRConThreadQuery.startThread();
		}

		if (settings.listen.rcon.enabled) {
			field_155771_h.info("Starting remote control listener");
			theRConThreadMain = new RConThreadMain(this);
			theRConThreadMain.startThread();
		}

		return FMLCommonHandler.instance().handleServerStarting(this);
	}

	@Override
	public boolean canStructuresSpawn() {
		return canSpawnStructures;
	}

	@Override
	public WorldSettings.GameType getGameType() {
		return gameType;
	}

	@Override
	public EnumDifficulty func_147135_j() {
		return BasicTypeParser.parseDifficulty(ConfigurationHandler.getWorldsConfig().global.settings.difficulty);
	}

	@Override
	public boolean isHardcore() {
		return settings.settings.other.hardcore;
	}

	@Override
	protected void finalTick(CrashReport par1CrashReport) {
	}

	@Override
	public CrashReport addServerInfoToCrashReport(CrashReport p_71230_1_) {
		p_71230_1_ = super.addServerInfoToCrashReport(p_71230_1_);
		p_71230_1_.getCategory().addCrashSectionCallable("Is Modded", new Callable() {
			private static final String __OBFID = "CL_00001785";

			@Override
			public String call() {
				String s = DedicatedServer.this.getServerModName();
				return !s.equals("vanilla") ? "Definitely; Server brand changed to '" + s + "'"
						: "Unknown (can't tell)";
			}
		});
		p_71230_1_.getCategory().addCrashSectionCallable("Type", new Callable() {
			private static final String __OBFID = "CL_00001788";

			@Override
			public String call() {
				return "Dedicated Server (map_server.txt)";
			}
		});
		return p_71230_1_;
	}

	@Override
	protected void systemExitNow() {
		System.exit(0);
	}

	@Override
	public void updateTimeLightAndEntities() {
		super.updateTimeLightAndEntities();
		executePendingCommands();
	}

	@Override
	public boolean getAllowNether() {
		return true;
	}

	@Override
	public boolean allowSpawnMonsters() {
		return ConfigurationHandler.getWorldsConfig().global.mobSpawn.spawnMonsters;
	}

	@Override
	public void addServerStatsToSnooper(PlayerUsageSnooper p_70000_1_) {
		p_70000_1_.func_152768_a("whitelist_enabled", Boolean.valueOf(getConfigurationManager().isWhiteListEnabled()));
		p_70000_1_.func_152768_a("whitelist_count", Integer.valueOf(getConfigurationManager().func_152598_l().length));
		super.addServerStatsToSnooper(p_70000_1_);
	}

	@Override
	public boolean isSnooperEnabled() {
		return settings.settings.other.snooperEnabled;
	}

	public void addPendingCommand(String p_71331_1_, ICommandSender p_71331_2_) {
		pendingCommandList.add(new ServerCommand(p_71331_1_, p_71331_2_));
	}

	public void executePendingCommands() {
		while (!pendingCommandList.isEmpty()) {
			ServerCommand servercommand = (ServerCommand) pendingCommandList.remove(0);
			getCommandManager().executeCommand(servercommand.sender, servercommand.command);
		}
	}

	@Override
	public boolean isDedicatedServer() {
		return true;
	}

	@Override
	public DedicatedPlayerList getConfigurationManager() {
		return (DedicatedPlayerList) super.getConfigurationManager();
	}

	@Override
	public int getIntProperty(String par1Str, int par2) {
		logInfo("Attempted to get server config unresolved integer parameter " + par1Str);
		return settings.vanilla.unresolved.containsKey(par1Str) ? (Integer) settings.vanilla.unresolved.get(par1Str)
				: par2;
	}

	@Override
	public String getStringProperty(String par1Str, String par2Str) {
		logInfo("Attempted to get server config unresolved string parameter " + par1Str);
		return settings.vanilla.unresolved.containsKey(par1Str) ? (String) settings.vanilla.unresolved.get(par1Str)
				: par2Str;
	}

	public boolean getBooleanProperty(String par1Str, boolean par2) {
		logInfo("Attempted to get server config unresolved boolean parameter " + par1Str);
		return settings.vanilla.unresolved.containsKey(par1Str) ? (Boolean) settings.vanilla.unresolved.get(par1Str)
				: par2;
	}

	@Override
	public void setProperty(String par1Str, Object par2Obj) {
		logInfo("Attempted to set server config unresolved parameter " + par1Str);
		settings.vanilla.unresolved.put(par1Str, par2Obj);
	}

	@Override
	public void saveProperties() {
		// ConfigurationHandler.saveServerConfig();
	}

	@Override
	public String getSettingsFilename() {
		return "server.yml";
	}

	public void setGuiEnabled() {
		MinecraftServerGui.createServerGui(this);
		guiIsEnabled = true;
	}

	@Override
	public boolean getGuiEnabled() {
		return guiIsEnabled;
	}

	@Override
	public String shareToLAN(WorldSettings.GameType p_71206_1_, boolean p_71206_2_) {
		return "";
	}

	@Override
	public boolean isCommandBlockEnabled() {
		return settings.settings.other.enableCommandBlock;
	}

	@Override
	public int getSpawnProtectionSize() {
		return 0;
	}

	@Override
	public boolean isBlockProtected(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer) {
		if (par1World.provider.dimensionId != 0)
			return false;
		else if (getConfigurationManager().func_152603_m().func_152690_d())
			return false;
		else if (perms.has(par5EntityPlayer, MinecraftPermissions.IGNORE_SPAWN_PROTECTION))
			return false;
		else if (getSpawnProtectionSize() <= 0)
			return false;
		else {
			ChunkCoordinates chunkcoordinates = par1World.getSpawnPoint();
			int l = MathHelper.abs_int(par2 - chunkcoordinates.posX);
			int i1 = MathHelper.abs_int(par4 - chunkcoordinates.posZ);
			int j1 = Math.max(l, i1);
			return j1 <= getSpawnProtectionSize();
		}
	}

	@Override
	public int getOpPermissionLevel() {
		return 4;
	}

	@Override
	public void func_143006_e(int par1) {
		super.func_143006_e(par1);
		settings.settings.player.playerIdleTimeout = par1;
		saveProperties();
	}

	@Override
	public boolean func_152363_m() {
		return false;// this.settings.getBooleanProperty("broadcast-rcon-to-ops", true);
	}

	@Override
	public boolean func_147136_ar() {
		return settings.settings.messages.announcePlayerAchievements;
	}

	protected boolean func_152368_aE() throws IOException {
		boolean flag = false;
		int i;

		for (i = 0; !flag && i <= 2; ++i) {
			if (i > 0) {
				field_155771_h
						.warn("Encountered a problem while converting the user banlist, retrying in a few seconds");
				func_152369_aG();
			}

			flag = PreYggdrasilConverter.func_152724_a(this);
		}

		boolean flag1 = false;

		for (i = 0; !flag1 && i <= 2; ++i) {
			if (i > 0) {
				field_155771_h.warn("Encountered a problem while converting the ip banlist, retrying in a few seconds");
				func_152369_aG();
			}

			flag1 = PreYggdrasilConverter.func_152722_b(this);
		}

		boolean flag2 = false;

		for (i = 0; !flag2 && i <= 2; ++i) {
			if (i > 0) {
				field_155771_h.warn("Encountered a problem while converting the op list, retrying in a few seconds");
				func_152369_aG();
			}

			flag2 = PreYggdrasilConverter.func_152718_c(this);
		}

		boolean flag3 = false;

		for (i = 0; !flag3 && i <= 2; ++i) {
			if (i > 0) {
				field_155771_h.warn("Encountered a problem while converting the whitelist, retrying in a few seconds");
				func_152369_aG();
			}

			flag3 = PreYggdrasilConverter.func_152710_d(this);
		}

		boolean flag4 = false;

		for (i = 0; !flag4 && i <= 2; ++i) {
			if (i > 0) {
				field_155771_h.warn(
						"Encountered a problem while converting the player save files, retrying in a few seconds");
				func_152369_aG();
			}

			flag4 = PreYggdrasilConverter.func_152723_a(this, getWorldsDir());
		}

		return flag || flag1 || flag2 || flag3 || flag4;
	}

	private void func_152369_aG() {
		try {
			Thread.sleep(5000L);
		} catch (InterruptedException ignored) {
		}
	}

	/*
	 * ======================================== ULTRAMINE START
	 * =====================================
	 */

	@InjectService
	private static Permissions perms;
	private final BackupManager backupMgr = new BackupManager(this);

	@Override
	protected void loadAllWorlds(String par1Str, String par2Str, long par3, WorldType par5WorldType, String par6Str) {
		convertMapIfNeeded(par1Str);
		setUserMessage("menu.loadingLevel");

		getMultiWorld().handleServerWorldsInit();

		getConfigurationManager().setPlayerManager(new WorldServer[] { getMultiWorld().getWorldByID(0) });
		initialWorldChunkLoad();
	}

	@Override
	public void stopServer() {
		super.stopServer();

		field_155771_h.info("Saving other data");
		try {
			GlobalExecutors.cachedIO().shutdown();
			if (!GlobalExecutors.cachedIO().awaitTermination(10000, TimeUnit.MILLISECONDS)) {
				field_155771_h.info("Global cached executor did not terminated cleanly");
			}
			GlobalExecutors.writingIO().shutdown();
			if (!GlobalExecutors.writingIO().awaitTermination(60000, TimeUnit.MILLISECONDS)) {
				field_155771_h.info("Global writing IO executor did not terminated cleanly");
			}
		} catch (InterruptedException ignored) {
		}
	}

	@Override
	public File getHomeDirectory() {
		return FMLLaunchHandler.getMinecraftHome();
	}

	@Override
	public File getVanillaFile(String name) {
		return new File(ConfigurationHandler.getVanillaConfigsDir(), name);
	}

	@Override
	public File getStorageFile(String name) {
		return new File(ConfigurationHandler.getStorageDir(), name);
	}

	@Override
	public BackupManager getBackupManager() {
		return backupMgr;
	}

	@Override
	protected void utilizeCPU(long nanos) throws InterruptedException
	{
		UMHooks.utilizeCPU(nanos);
	}

}