package net.minecraft.world;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.world.storage.WorldInfo;

public final class WorldSettings {
	private final long seed;
	private final WorldSettings.GameType theGameType;
	private final boolean mapFeaturesEnabled;
	private final boolean hardcoreEnabled;
	private final WorldType terrainType;
	private boolean commandsAllowed;
	private boolean bonusChestEnabled;
	private String field_82751_h;
	private static final String __OBFID = "CL_00000147";

	public WorldSettings(long p_i1957_1_, WorldSettings.GameType p_i1957_3_, boolean p_i1957_4_, boolean p_i1957_5_,
			WorldType p_i1957_6_) {
		field_82751_h = "";
		seed = p_i1957_1_;
		theGameType = p_i1957_3_;
		mapFeaturesEnabled = p_i1957_4_;
		hardcoreEnabled = p_i1957_5_;
		terrainType = p_i1957_6_;
	}

	public WorldSettings(WorldInfo p_i1958_1_) {
		this(p_i1958_1_.getSeed(), p_i1958_1_.getGameType(), p_i1958_1_.isMapFeaturesEnabled(),
				p_i1958_1_.isHardcoreModeEnabled(), p_i1958_1_.getTerrainType());
	}

	public WorldSettings enableBonusChest() {
		bonusChestEnabled = true;
		return this;
	}

	public WorldSettings func_82750_a(String p_82750_1_) {
		field_82751_h = p_82750_1_;
		return this;
	}

	@SideOnly(Side.CLIENT)
	public WorldSettings enableCommands() {
		commandsAllowed = true;
		return this;
	}

	public boolean isBonusChestEnabled() {
		return bonusChestEnabled;
	}

	public long getSeed() {
		return seed;
	}

	public WorldSettings.GameType getGameType() {
		return theGameType;
	}

	public boolean getHardcoreEnabled() {
		return hardcoreEnabled;
	}

	public boolean isMapFeaturesEnabled() {
		return mapFeaturesEnabled;
	}

	public WorldType getTerrainType() {
		return terrainType;
	}

	public boolean areCommandsAllowed() {
		return commandsAllowed;
	}

	public static WorldSettings.GameType getGameTypeById(int p_77161_0_) {
		return WorldSettings.GameType.getByID(p_77161_0_);
	}

	public String func_82749_j() {
		return field_82751_h;
	}

	public enum GameType {
		NOT_SET(-1, ""), SURVIVAL(0, "survival"), CREATIVE(1, "creative"), ADVENTURE(2, "adventure");
		int id;
		String name;

		private static final String __OBFID = "CL_00000148";

		GameType(int p_i1956_3_, String p_i1956_4_) {
			id = p_i1956_3_;
			name = p_i1956_4_;
		}

		public int getID() {
			return id;
		}

		public String getName() {
			return name;
		}

		public void configurePlayerCapabilities(PlayerCapabilities p_77147_1_) {
			if (this == CREATIVE) {
				p_77147_1_.allowFlying = true;
				p_77147_1_.isCreativeMode = true;
				p_77147_1_.disableDamage = true;
			} else {
				p_77147_1_.allowFlying = false;
				p_77147_1_.isCreativeMode = false;
				p_77147_1_.disableDamage = false;
				p_77147_1_.isFlying = false;
			}

			p_77147_1_.allowEdit = !isAdventure();
		}

		public boolean isAdventure() {
			return this == ADVENTURE;
		}

		public boolean isCreative() {
			return this == CREATIVE;
		}

		@SideOnly(Side.CLIENT)
		public boolean isSurvivalOrAdventure() {
			return this == SURVIVAL || this == ADVENTURE;
		}

		public static WorldSettings.GameType getByID(int p_77146_0_) {
			WorldSettings.GameType[] agametype = values();
			int j = agametype.length;

			for (int k = 0; k < j; ++k) {
				WorldSettings.GameType gametype = agametype[k];

				if (gametype.id == p_77146_0_)
					return gametype;
			}

			return SURVIVAL;
		}

		@SideOnly(Side.CLIENT)
		public static WorldSettings.GameType getByName(String p_77142_0_) {
			WorldSettings.GameType[] agametype = values();
			int i = agametype.length;

			for (int j = 0; j < i; ++j) {
				WorldSettings.GameType gametype = agametype[j];

				if (gametype.name.equals(p_77142_0_))
					return gametype;
			}

			return SURVIVAL;
		}
	}
}