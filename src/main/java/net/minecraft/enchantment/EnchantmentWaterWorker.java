package net.minecraft.enchantment;

public class EnchantmentWaterWorker extends Enchantment {
	private static final String __OBFID = "CL_00000124";

	public EnchantmentWaterWorker(int p_i1939_1_, int p_i1939_2_) {
		super(p_i1939_1_, p_i1939_2_, EnumEnchantmentType.armor_head);
		setName("waterWorker");
	}

	@Override
	public int getMinEnchantability(int p_77321_1_) {
		return 1;
	}

	@Override
	public int getMaxEnchantability(int p_77317_1_) {
		return getMinEnchantability(p_77317_1_) + 40;
	}

	@Override
	public int getMaxLevel() {
		return 1;
	}
}