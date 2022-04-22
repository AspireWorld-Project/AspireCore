package net.minecraft.world.storage;

import java.util.Map;
import java.util.concurrent.Callable;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.GameRules;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldType;

public class WorldInfo {
	private long randomSeed;
	private WorldType terrainType;
	private String generatorOptions;
	private int spawnX;
	private int spawnY;
	private int spawnZ;
	private long totalTime;
	private long worldTime;
	private long lastTimePlayed;
	private long sizeOnDisk;
	private NBTTagCompound playerTag;
	private int dimension;
	private String levelName;
	private int saveVersion;
	private boolean raining;
	private int rainTime;
	private boolean thundering;
	private int thunderTime;
	private WorldSettings.GameType theGameType;
	private boolean mapFeaturesEnabled;
	private boolean hardcore;
	private boolean allowCommands;
	private boolean initialized;
	private GameRules theGameRules;
	private Map<String, NBTBase> additionalProperties;
	private static final String __OBFID = "CL_00000587";

	protected WorldInfo() {
		terrainType = WorldType.DEFAULT;
		generatorOptions = "";
		theGameRules = new GameRules();
	}

	public WorldInfo(NBTTagCompound p_i2157_1_) {
		terrainType = WorldType.DEFAULT;
		generatorOptions = "";
		theGameRules = new GameRules();
		randomSeed = p_i2157_1_.getLong("RandomSeed");

		if (p_i2157_1_.hasKey("generatorName", 8)) {
			String s = p_i2157_1_.getString("generatorName");
			terrainType = WorldType.parseWorldType(s);

			if (terrainType == null) {
				terrainType = WorldType.DEFAULT;
			} else if (terrainType.isVersioned()) {
				int i = 0;

				if (p_i2157_1_.hasKey("generatorVersion", 99)) {
					i = p_i2157_1_.getInteger("generatorVersion");
				}

				terrainType = terrainType.getWorldTypeForGeneratorVersion(i);
			}

			if (p_i2157_1_.hasKey("generatorOptions", 8)) {
				generatorOptions = p_i2157_1_.getString("generatorOptions");
			}
		}

		theGameType = WorldSettings.GameType.getByID(p_i2157_1_.getInteger("GameType"));

		if (p_i2157_1_.hasKey("MapFeatures", 99)) {
			mapFeaturesEnabled = p_i2157_1_.getBoolean("MapFeatures");
		} else {
			mapFeaturesEnabled = true;
		}

		spawnX = p_i2157_1_.getInteger("SpawnX");
		spawnY = p_i2157_1_.getInteger("SpawnY");
		spawnZ = p_i2157_1_.getInteger("SpawnZ");
		totalTime = p_i2157_1_.getLong("Time");

		if (p_i2157_1_.hasKey("DayTime", 99)) {
			worldTime = p_i2157_1_.getLong("DayTime");
		} else {
			worldTime = totalTime;
		}

		lastTimePlayed = p_i2157_1_.getLong("LastPlayed");
		sizeOnDisk = p_i2157_1_.getLong("SizeOnDisk");
		levelName = p_i2157_1_.getString("LevelName");
		saveVersion = p_i2157_1_.getInteger("version");
		rainTime = p_i2157_1_.getInteger("rainTime");
		raining = p_i2157_1_.getBoolean("raining");
		thunderTime = p_i2157_1_.getInteger("thunderTime");
		thundering = p_i2157_1_.getBoolean("thundering");
		hardcore = p_i2157_1_.getBoolean("hardcore");

		if (p_i2157_1_.hasKey("initialized", 99)) {
			initialized = p_i2157_1_.getBoolean("initialized");
		} else {
			initialized = true;
		}

		if (p_i2157_1_.hasKey("allowCommands", 99)) {
			allowCommands = p_i2157_1_.getBoolean("allowCommands");
		} else {
			allowCommands = theGameType == WorldSettings.GameType.CREATIVE;
		}

		if (p_i2157_1_.hasKey("Player", 10)) {
			playerTag = p_i2157_1_.getCompoundTag("Player");
			dimension = playerTag.getInteger("Dimension");
		}

		if (p_i2157_1_.hasKey("GameRules", 10)) {
			theGameRules.readGameRulesFromNBT(p_i2157_1_.getCompoundTag("GameRules"));
		}
	}

	public WorldInfo(WorldSettings p_i2158_1_, String p_i2158_2_) {
		terrainType = WorldType.DEFAULT;
		generatorOptions = "";
		theGameRules = new GameRules();
		randomSeed = p_i2158_1_.getSeed();
		theGameType = p_i2158_1_.getGameType();
		mapFeaturesEnabled = p_i2158_1_.isMapFeaturesEnabled();
		levelName = p_i2158_2_;
		hardcore = p_i2158_1_.getHardcoreEnabled();
		terrainType = p_i2158_1_.getTerrainType();
		generatorOptions = p_i2158_1_.func_82749_j();
		allowCommands = p_i2158_1_.areCommandsAllowed();
		initialized = false;
	}

	public WorldInfo(WorldInfo p_i2159_1_) {
		terrainType = WorldType.DEFAULT;
		generatorOptions = "";
		theGameRules = new GameRules();
		randomSeed = p_i2159_1_.randomSeed;
		terrainType = p_i2159_1_.terrainType;
		generatorOptions = p_i2159_1_.generatorOptions;
		theGameType = p_i2159_1_.theGameType;
		mapFeaturesEnabled = p_i2159_1_.mapFeaturesEnabled;
		spawnX = p_i2159_1_.spawnX;
		spawnY = p_i2159_1_.spawnY;
		spawnZ = p_i2159_1_.spawnZ;
		totalTime = p_i2159_1_.totalTime;
		worldTime = p_i2159_1_.worldTime;
		lastTimePlayed = p_i2159_1_.lastTimePlayed;
		sizeOnDisk = p_i2159_1_.sizeOnDisk;
		playerTag = p_i2159_1_.playerTag;
		dimension = p_i2159_1_.dimension;
		levelName = p_i2159_1_.levelName;
		saveVersion = p_i2159_1_.saveVersion;
		rainTime = p_i2159_1_.rainTime;
		raining = p_i2159_1_.raining;
		thunderTime = p_i2159_1_.thunderTime;
		thundering = p_i2159_1_.thundering;
		hardcore = p_i2159_1_.hardcore;
		allowCommands = p_i2159_1_.allowCommands;
		initialized = p_i2159_1_.initialized;
		theGameRules = p_i2159_1_.theGameRules;
	}

	public NBTTagCompound getNBTTagCompound() {
		NBTTagCompound nbttagcompound = new NBTTagCompound();
		updateTagCompound(nbttagcompound, playerTag);
		return nbttagcompound;
	}

	public NBTTagCompound cloneNBTCompound(NBTTagCompound p_76082_1_) {
		NBTTagCompound nbttagcompound1 = new NBTTagCompound();
		updateTagCompound(nbttagcompound1, p_76082_1_);
		return nbttagcompound1;
	}

	private void updateTagCompound(NBTTagCompound p_76064_1_, NBTTagCompound p_76064_2_) {
		p_76064_1_.setLong("RandomSeed", randomSeed);
		p_76064_1_.setString("generatorName", terrainType.getWorldTypeName());
		p_76064_1_.setInteger("generatorVersion", terrainType.getGeneratorVersion());
		p_76064_1_.setString("generatorOptions", generatorOptions);
		p_76064_1_.setInteger("GameType", theGameType.getID());
		p_76064_1_.setBoolean("MapFeatures", mapFeaturesEnabled);
		p_76064_1_.setInteger("SpawnX", spawnX);
		p_76064_1_.setInteger("SpawnY", spawnY);
		p_76064_1_.setInteger("SpawnZ", spawnZ);
		p_76064_1_.setLong("Time", totalTime);
		p_76064_1_.setLong("DayTime", worldTime);
		p_76064_1_.setLong("SizeOnDisk", sizeOnDisk);
		p_76064_1_.setLong("LastPlayed", MinecraftServer.getSystemTimeMillis());
		p_76064_1_.setString("LevelName", levelName);
		p_76064_1_.setInteger("version", saveVersion);
		p_76064_1_.setInteger("rainTime", rainTime);
		p_76064_1_.setBoolean("raining", raining);
		p_76064_1_.setInteger("thunderTime", thunderTime);
		p_76064_1_.setBoolean("thundering", thundering);
		p_76064_1_.setBoolean("hardcore", hardcore);
		p_76064_1_.setBoolean("allowCommands", allowCommands);
		p_76064_1_.setBoolean("initialized", initialized);
		p_76064_1_.setTag("GameRules", theGameRules.writeGameRulesToNBT());

		if (p_76064_2_ != null) {
			p_76064_1_.setTag("Player", p_76064_2_);
		}
	}

	public long getSeed() {
		return randomSeed;
	}

	public int getSpawnX() {
		return spawnX;
	}

	public int getSpawnY() {
		return spawnY;
	}

	public int getSpawnZ() {
		return spawnZ;
	}

	public long getWorldTotalTime() {
		return totalTime;
	}

	public long getWorldTime() {
		return worldTime;
	}

	@SideOnly(Side.CLIENT)
	public long getSizeOnDisk() {
		return sizeOnDisk;
	}

	public NBTTagCompound getPlayerNBTTagCompound() {
		return playerTag;
	}

	public int getVanillaDimension() {
		return dimension;
	}

	@SideOnly(Side.CLIENT)
	public void setSpawnX(int p_76058_1_) {
		spawnX = p_76058_1_;
	}

	@SideOnly(Side.CLIENT)
	public void setSpawnY(int p_76056_1_) {
		spawnY = p_76056_1_;
	}

	public void incrementTotalWorldTime(long p_82572_1_) {
		totalTime = p_82572_1_;
	}

	@SideOnly(Side.CLIENT)
	public void setSpawnZ(int p_76087_1_) {
		spawnZ = p_76087_1_;
	}

	public void setWorldTime(long p_76068_1_) {
		worldTime = p_76068_1_;
	}

	public void setSpawnPosition(int p_76081_1_, int p_76081_2_, int p_76081_3_) {
		spawnX = p_76081_1_;
		spawnY = p_76081_2_;
		spawnZ = p_76081_3_;
	}

	public String getWorldName() {
		return levelName;
	}

	public void setWorldName(String p_76062_1_) {
		levelName = p_76062_1_;
	}

	public int getSaveVersion() {
		return saveVersion;
	}

	public void setSaveVersion(int p_76078_1_) {
		saveVersion = p_76078_1_;
	}

	@SideOnly(Side.CLIENT)
	public long getLastTimePlayed() {
		return lastTimePlayed;
	}

	public boolean isThundering() {
		return thundering;
	}

	public void setThundering(boolean p_76069_1_) {
		thundering = p_76069_1_;
	}

	public int getThunderTime() {
		return thunderTime;
	}

	public void setThunderTime(int p_76090_1_) {
		thunderTime = p_76090_1_;
	}

	public boolean isRaining() {
		return raining;
	}

	public void setRaining(boolean p_76084_1_) {
		raining = p_76084_1_;
	}

	public int getRainTime() {
		return rainTime;
	}

	public void setRainTime(int p_76080_1_) {
		rainTime = p_76080_1_;
	}

	public WorldSettings.GameType getGameType() {
		return theGameType;
	}

	public boolean isMapFeaturesEnabled() {
		return mapFeaturesEnabled;
	}

	public void setGameType(WorldSettings.GameType p_76060_1_) {
		theGameType = p_76060_1_;
	}

	public boolean isHardcoreModeEnabled() {
		return hardcore;
	}

	public WorldType getTerrainType() {
		return terrainType;
	}

	public void setTerrainType(WorldType p_76085_1_) {
		terrainType = p_76085_1_;
	}

	public String getGeneratorOptions() {
		return generatorOptions;
	}

	public boolean areCommandsAllowed() {
		return allowCommands;
	}

	public boolean isInitialized() {
		return initialized;
	}

	public void setServerInitialized(boolean p_76091_1_) {
		initialized = p_76091_1_;
	}

	public GameRules getGameRulesInstance() {
		return theGameRules;
	}

	public void addToCrashReport(CrashReportCategory p_85118_1_) {
		p_85118_1_.addCrashSectionCallable("Level seed", new Callable() {
			private static final String __OBFID = "CL_00000588";

			@Override
			public String call() {
				return String.valueOf(WorldInfo.this.getSeed());
			}
		});
		p_85118_1_.addCrashSectionCallable("Level generator", new Callable() {
			private static final String __OBFID = "CL_00000589";

			@Override
			public String call() {
				return String.format("ID %02d - %s, ver %d. Features enabled: %b",
						new Object[] { Integer.valueOf(terrainType.getWorldTypeID()), terrainType.getWorldTypeName(),
								Integer.valueOf(terrainType.getGeneratorVersion()),
								Boolean.valueOf(mapFeaturesEnabled) });
			}
		});
		p_85118_1_.addCrashSectionCallable("Level generator options", new Callable() {
			private static final String __OBFID = "CL_00000590";

			@Override
			public String call() {
				return generatorOptions;
			}
		});
		p_85118_1_.addCrashSectionCallable("Level spawn location", new Callable() {
			private static final String __OBFID = "CL_00000591";

			@Override
			public String call() {
				return CrashReportCategory.getLocationInfo(spawnX, spawnY, spawnZ);
			}
		});
		p_85118_1_.addCrashSectionCallable("Level time", new Callable() {
			private static final String __OBFID = "CL_00000592";

			@Override
			public String call() {
				return String.format("%d game time, %d day time",
						new Object[] { Long.valueOf(totalTime), Long.valueOf(worldTime) });
			}
		});
		p_85118_1_.addCrashSectionCallable("Level dimension", new Callable() {
			private static final String __OBFID = "CL_00000593";

			@Override
			public String call() {
				return String.valueOf(dimension);
			}
		});
		p_85118_1_.addCrashSectionCallable("Level storage version", new Callable() {
			private static final String __OBFID = "CL_00000594";

			@Override
			public String call() {
				String s = "Unknown?";

				try {
					switch (saveVersion) {
					case 19132:
						s = "McRegion";
						break;
					case 19133:
						s = "Anvil";
					}
				} catch (Throwable throwable) {
					;
				}

				return String.format("0x%05X - %s", new Object[] { Integer.valueOf(saveVersion), s });
			}
		});
		p_85118_1_.addCrashSectionCallable("Level weather", new Callable() {
			private static final String __OBFID = "CL_00000595";

			@Override
			public String call() {
				return String.format("Rain time: %d (now: %b), thunder time: %d (now: %b)",
						new Object[] { Integer.valueOf(rainTime), Boolean.valueOf(raining),
								Integer.valueOf(thunderTime), Boolean.valueOf(thundering) });
			}
		});
		p_85118_1_.addCrashSectionCallable("Level game mode", new Callable() {
			private static final String __OBFID = "CL_00000597";

			@Override
			public String call() {
				return String.format("Game mode: %s (ID %d). Hardcore: %b. Cheats: %b",
						new Object[] { theGameType.getName(), Integer.valueOf(theGameType.getID()),
								Boolean.valueOf(hardcore), Boolean.valueOf(allowCommands) });
			}
		});
	}

	/**
	 * Allow access to additional mod specific world based properties Used by FML to
	 * store mod list associated with a world, and maybe an id map Used by Forge to
	 * store the dimensions available to a world
	 *
	 * @param additionalProperties
	 */
	public void setAdditionalProperties(Map<String, NBTBase> additionalProperties) {
		// one time set for this
		if (this.additionalProperties == null) {
			this.additionalProperties = additionalProperties;
		}
	}

	public NBTBase getAdditionalProperty(String additionalProperty) {
		return additionalProperties != null ? additionalProperties.get(additionalProperty) : null;
	}

	// Cauldron start
	/**
	 * Sets the Dimension.
	 */
	public void setDimension(int dim)
	{
		this.dimension = dim;
	}

	public int getDimension()
	{
		return this.dimension;
	}
	// Cauldron end
}