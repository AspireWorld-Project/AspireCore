package net.minecraft.world;

public enum EnumDifficulty {
	PEACEFUL(0, "options.difficulty.peaceful"), EASY(1, "options.difficulty.easy"), NORMAL(2,
			"options.difficulty.normal"), HARD(3, "options.difficulty.hard");
	private static final EnumDifficulty[] difficultyEnums = new EnumDifficulty[values().length];
	private final int difficultyId;
	private final String difficultyResourceKey;

	EnumDifficulty(int p_i45312_3_, String p_i45312_4_) {
		difficultyId = p_i45312_3_;
		difficultyResourceKey = p_i45312_4_;
	}

	public int getDifficultyId() {
		return difficultyId;
	}

	public static EnumDifficulty getDifficultyEnum(int p_151523_0_) {
		return difficultyEnums[p_151523_0_ % difficultyEnums.length];
	}

	public String getDifficultyResourceKey() {
		return difficultyResourceKey;
	}

	static {
		EnumDifficulty[] var0 = values();
		int var1 = var0.length;

		for (int var2 = 0; var2 < var1; ++var2) {
			EnumDifficulty var3 = var0[var2];
			difficultyEnums[var3.difficultyId] = var3;
		}
	}
}