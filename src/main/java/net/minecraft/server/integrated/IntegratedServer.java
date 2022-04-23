package net.minecraft.server.integrated;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.ClientBrandRetriever;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ThreadLanServerPing;
import net.minecraft.crash.CrashReport;
import net.minecraft.profiler.PlayerUsageSnooper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerProfileCache;
import net.minecraft.util.CryptManager;
import net.minecraft.util.HttpUtil;
import net.minecraft.world.*;
import net.minecraft.world.demo.DemoWorldServer;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.concurrent.Callable;

@SideOnly(Side.CLIENT)
public class IntegratedServer extends MinecraftServer {
	private static final Logger logger = LogManager.getLogger();
	private final Minecraft mc;
	private final WorldSettings theWorldSettings;
	private boolean isGamePaused;
	private boolean isPublic;
	private ThreadLanServerPing lanServerPing;
	private static final String __OBFID = "CL_00001129";

	public IntegratedServer(Minecraft p_i1317_1_, String p_i1317_2_, String p_i1317_3_, WorldSettings p_i1317_4_) {
		super(new File(p_i1317_1_.mcDataDir, "saves"), p_i1317_1_.getProxy());
		setServerOwner(p_i1317_1_.getSession().getUsername());
		setFolderName(p_i1317_2_);
		setWorldName(p_i1317_3_);
		setDemo(p_i1317_1_.isDemo());
		canCreateBonusChest(p_i1317_4_.isBonusChestEnabled());
		setBuildLimit(256);
		mc = p_i1317_1_;
		func_152361_a(new IntegratedPlayerList(this));
		theWorldSettings = p_i1317_4_;
		field_152367_a = new File(getDataDirectory(), "usercache.json");
		field_152366_X = new PlayerProfileCache(this, field_152367_a);
	}

	@Override
	protected void loadAllWorlds(String p_71247_1_, String p_71247_2_, long p_71247_3_, WorldType p_71247_5_,
			String p_71247_6_) {
		convertMapIfNeeded(p_71247_1_);
		ISaveHandler isavehandler = getActiveAnvilConverter().getSaveLoader(p_71247_1_, true);

		WorldServer overWorld = isDemo() ? new DemoWorldServer(this, isavehandler, p_71247_2_, 0, theProfiler)
				: new WorldServer(this, isavehandler, p_71247_2_, 0, theWorldSettings, theProfiler);
		for (int dim : DimensionManager.getStaticDimensionIDs()) {
			WorldServer world = dim == 0 ? overWorld
					: new WorldServerMulti(this, isavehandler, p_71247_2_, dim, theWorldSettings, overWorld,
							theProfiler);
			world.addWorldAccess(new WorldManager(this, world));

			if (!isSinglePlayer()) {
				world.getWorldInfo().setGameType(getGameType());
			}

			MinecraftForge.EVENT_BUS.post(new WorldEvent.Load(world));
		}

		getMultiWorld().handleClientWorldsInit();

		getConfigurationManager().setPlayerManager(new WorldServer[] { overWorld });
		func_147139_a(func_147135_j());
		initialWorldChunkLoad();
	}

	@Override
	protected boolean startServer() throws IOException {
		logger.info("Starting integrated minecraft server version 1.7.10");
		setOnlineMode(true);
		setCanSpawnAnimals(true);
		setCanSpawnNPCs(true);
		setAllowPvp(true);
		setAllowFlight(true);
		logger.info("Generating keypair");
		setKeyPair(CryptManager.createNewKeyPair());
		if (!FMLCommonHandler.instance().handleServerAboutToStart(this))
			return false;
		loadAllWorlds(getFolderName(), getWorldName(), theWorldSettings.getSeed(), theWorldSettings.getTerrainType(),
				theWorldSettings.func_82749_j());
		setMOTD(getServerOwner() + " - " + worldServers[0].getWorldInfo().getWorldName());
		return FMLCommonHandler.instance().handleServerStarting(this);
	}

	@Override
	public void tick() {
		boolean flag = isGamePaused;
		isGamePaused = Minecraft.getMinecraft().getNetHandler() != null && Minecraft.getMinecraft().isGamePaused();

		if (!flag && isGamePaused) {
			logger.info("Saving and pausing game...");
			getConfigurationManager().saveAllPlayerData();
			saveAllWorlds(false);
		}

		if (!isGamePaused) {
			super.tick();

			if (mc.gameSettings.renderDistanceChunks != getConfigurationManager().getViewDistance()) {
				logger.info("Changing view distance to {}, from {}",
						new Object[] { Integer.valueOf(mc.gameSettings.renderDistanceChunks),
								Integer.valueOf(getConfigurationManager().getViewDistance()) });
				getConfigurationManager().func_152611_a(mc.gameSettings.renderDistanceChunks);
			}
		}
	}

	@Override
	public boolean canStructuresSpawn() {
		return false;
	}

	@Override
	public WorldSettings.GameType getGameType() {
		return theWorldSettings.getGameType();
	}

	@Override
	public EnumDifficulty func_147135_j() {
		return mc.gameSettings.difficulty;
	}

	@Override
	public boolean isHardcore() {
		return theWorldSettings.getHardcoreEnabled();
	}

	@Override
	public boolean func_152363_m() {
		return false;
	}

	@Override
	protected File getDataDirectory() {
		return mc.mcDataDir;
	}

	@Override
	public boolean isDedicatedServer() {
		return false;
	}

	@Override
	protected void finalTick(CrashReport p_71228_1_) {
		mc.crashed(p_71228_1_);
	}

	@Override
	public CrashReport addServerInfoToCrashReport(CrashReport p_71230_1_) {
		p_71230_1_ = super.addServerInfoToCrashReport(p_71230_1_);
		p_71230_1_.getCategory().addCrashSectionCallable("Type", new Callable() {
			private static final String __OBFID = "CL_00001130";

			@Override
			public String call() {
				return "Integrated Server (map_client.txt)";
			}
		});
		p_71230_1_.getCategory().addCrashSectionCallable("Is Modded", new Callable() {
			private static final String __OBFID = "CL_00001131";

			@Override
			public String call() {
				String s = ClientBrandRetriever.getClientModName();

				if (!s.equals("vanilla"))
					return "Definitely; Client brand changed to \'" + s + "\'";
				else {
					s = IntegratedServer.this.getServerModName();
					return !s.equals("vanilla") ? "Definitely; Server brand changed to \'" + s + "\'"
							: Minecraft.class.getSigners() == null ? "Very likely; Jar signature invalidated"
									: "Probably not. Jar signature remains and both client + server brands are untouched.";
				}
			}
		});
		return p_71230_1_;
	}

	@Override
	public void addServerStatsToSnooper(PlayerUsageSnooper p_70000_1_) {
		super.addServerStatsToSnooper(p_70000_1_);
		p_70000_1_.func_152768_a("snooper_partner", mc.getPlayerUsageSnooper().getUniqueID());
	}

	@Override
	public boolean isSnooperEnabled() {
		return Minecraft.getMinecraft().isSnooperEnabled();
	}

	@Override
	public String shareToLAN(WorldSettings.GameType p_71206_1_, boolean p_71206_2_) {
		try {
			int i = -1;

			try {
				i = HttpUtil.func_76181_a();
			} catch (IOException ioexception) {
				;
			}

			if (i <= 0) {
				i = 25564;
			}

			func_147137_ag().addLanEndpoint((InetAddress) null, i);
			logger.info("Started on " + i);
			isPublic = true;
			lanServerPing = new ThreadLanServerPing(getMOTD(), i + "");
			lanServerPing.start();
			getConfigurationManager().func_152604_a(p_71206_1_);
			getConfigurationManager().setCommandsAllowedForAll(p_71206_2_);
			return i + "";
		} catch (IOException ioexception1) {
			return null;
		}
	}

	@Override
	public void stopServer() {
		super.stopServer();

		if (lanServerPing != null) {
			lanServerPing.interrupt();
			lanServerPing = null;
		}
	}

	@Override
	public void initiateShutdown() {
		super.initiateShutdown();

		if (lanServerPing != null) {
			lanServerPing.interrupt();
			lanServerPing = null;
		}
	}

	public boolean getPublic() {
		return isPublic;
	}

	@Override
	public void setGameType(WorldSettings.GameType p_71235_1_) {
		getConfigurationManager().func_152604_a(p_71235_1_);
	}

	@Override
	public boolean isCommandBlockEnabled() {
		return true;
	}

	@Override
	public int getOpPermissionLevel() {
		return 4;
	}
}