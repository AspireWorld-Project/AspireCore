package net.minecraft.world.storage;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.world.WorldSettings;

@SideOnly(Side.CLIENT)
public class SaveFormatComparator implements Comparable {
	private final String fileName;
	private final String displayName;
	private final long lastTimePlayed;
	private final long sizeOnDisk;
	private final boolean requiresConversion;
	private final WorldSettings.GameType theEnumGameType;
	private final boolean hardcore;
	private final boolean cheatsEnabled;
	private static final String __OBFID = "CL_00000601";

	public SaveFormatComparator(String p_i2161_1_, String p_i2161_2_, long p_i2161_3_, long p_i2161_5_,
			WorldSettings.GameType p_i2161_7_, boolean p_i2161_8_, boolean p_i2161_9_, boolean p_i2161_10_) {
		fileName = p_i2161_1_;
		displayName = p_i2161_2_;
		lastTimePlayed = p_i2161_3_;
		sizeOnDisk = p_i2161_5_;
		theEnumGameType = p_i2161_7_;
		requiresConversion = p_i2161_8_;
		hardcore = p_i2161_9_;
		cheatsEnabled = p_i2161_10_;
	}

	public String getFileName() {
		return fileName;
	}

	public String getDisplayName() {
		return displayName;
	}

	public long func_154336_c() {
		return sizeOnDisk;
	}

	public boolean requiresConversion() {
		return requiresConversion;
	}

	public long getLastTimePlayed() {
		return lastTimePlayed;
	}

	public int compareTo(SaveFormatComparator p_compareTo_1_) {
		return lastTimePlayed < p_compareTo_1_.lastTimePlayed ? 1
				: lastTimePlayed > p_compareTo_1_.lastTimePlayed ? -1 : fileName.compareTo(p_compareTo_1_.fileName);
	}

	public WorldSettings.GameType getEnumGameType() {
		return theEnumGameType;
	}

	public boolean isHardcoreModeEnabled() {
		return hardcore;
	}

	public boolean getCheatsEnabled() {
		return cheatsEnabled;
	}

	@Override
	public int compareTo(Object p_compareTo_1_) {
		return this.compareTo((SaveFormatComparator) p_compareTo_1_);
	}
}