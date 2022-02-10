package net.minecraft.enchantment;

public class EnchantmentArrowDamage extends Enchantment {
	private static final String __OBFID = "CL_00000098";

	public EnchantmentArrowDamage(int p_i1919_1_, int p_i1919_2_) {
		super(p_i1919_1_, p_i1919_2_, EnumEnchantmentType.bow);
		setName("arrowDamage");
	}

	@Override
	public int getMinEnchantability(int p_77321_1_) {
		return 1 + (p_77321_1_ - 1) * 10;
	}

	@Override
	public int getMaxEnchantability(int p_77317_1_) {
		return getMinEnchantability(p_77317_1_) + 15;
	}

	@Override
	public int getMaxLevel() {
		return 5;
	}
}