package net.minecraft.world.storage;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.GameRules;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldType;

public class DerivedWorldInfo extends WorldInfo {
	private final WorldInfo theWorldInfo;
	private static final String __OBFID = "CL_00000584";

	public DerivedWorldInfo(WorldInfo p_i2145_1_) {
		theWorldInfo = p_i2145_1_;
	}

	@Override
	public NBTTagCompound getNBTTagCompound() {
		return theWorldInfo.getNBTTagCompound();
	}

	@Override
	public NBTTagCompound cloneNBTCompound(NBTTagCompound p_76082_1_) {
		return theWorldInfo.cloneNBTCompound(p_76082_1_);
	}

	@Override
	public long getSeed() {
		return theWorldInfo.getSeed();
	}

	@Override
	public int getSpawnX() {
		return theWorldInfo.getSpawnX();
	}

	@Override
	public int getSpawnY() {
		return theWorldInfo.getSpawnY();
	}

	@Override
	public int getSpawnZ() {
		return theWorldInfo.getSpawnZ();
	}

	@Override
	public long getWorldTotalTime() {
		return theWorldInfo.getWorldTotalTime();
	}

	@Override
	public long getWorldTime() {
		return theWorldInfo.getWorldTime();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public long getSizeOnDisk() {
		return theWorldInfo.getSizeOnDisk();
	}

	@Override
	public NBTTagCompound getPlayerNBTTagCompound() {
		return theWorldInfo.getPlayerNBTTagCompound();
	}

	@Override
	public int getVanillaDimension() {
		return theWorldInfo.getVanillaDimension();
	}

	@Override
	public String getWorldName() {
		return theWorldInfo.getWorldName();
	}

	@Override
	public int getSaveVersion() {
		return theWorldInfo.getSaveVersion();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public long getLastTimePlayed() {
		return theWorldInfo.getLastTimePlayed();
	}

	@Override
	public boolean isThundering() {
		return theWorldInfo.isThundering();
	}

	@Override
	public int getThunderTime() {
		return theWorldInfo.getThunderTime();
	}

	@Override
	public boolean isRaining() {
		return theWorldInfo.isRaining();
	}

	@Override
	public int getRainTime() {
		return theWorldInfo.getRainTime();
	}

	@Override
	public WorldSettings.GameType getGameType() {
		return theWorldInfo.getGameType();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void setSpawnX(int p_76058_1_) {
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void setSpawnY(int p_76056_1_) {
	}

	@Override
	public void incrementTotalWorldTime(long p_82572_1_) {
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void setSpawnZ(int p_76087_1_) {
	}

	@Override
	public void setWorldTime(long p_76068_1_) {
	}

	@Override
	public void setSpawnPosition(int p_76081_1_, int p_76081_2_, int p_76081_3_) {
	}

	@Override
	public void setWorldName(String p_76062_1_) {
	}

	@Override
	public void setSaveVersion(int p_76078_1_) {
	}

	@Override
	public void setThundering(boolean p_76069_1_) {
	}

	@Override
	public void setThunderTime(int p_76090_1_) {
	}

	@Override
	public void setRaining(boolean p_76084_1_) {
	}

	@Override
	public void setRainTime(int p_76080_1_) {
	}

	@Override
	public boolean isMapFeaturesEnabled() {
		return theWorldInfo.isMapFeaturesEnabled();
	}

	@Override
	public boolean isHardcoreModeEnabled() {
		return theWorldInfo.isHardcoreModeEnabled();
	}

	@Override
	public WorldType getTerrainType() {
		return theWorldInfo.getTerrainType();
	}

	@Override
	public void setTerrainType(WorldType p_76085_1_) {
	}

	@Override
	public boolean areCommandsAllowed() {
		return theWorldInfo.areCommandsAllowed();
	}

	@Override
	public boolean isInitialized() {
		return theWorldInfo.isInitialized();
	}

	@Override
	public void setServerInitialized(boolean p_76091_1_) {
	}

	@Override
	public GameRules getGameRulesInstance() {
		return theWorldInfo.getGameRulesInstance();
	}
}