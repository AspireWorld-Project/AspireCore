package net.minecraft.enchantment;

public class EnchantmentArrowInfinite extends Enchantment {
	private static final String __OBFID = "CL_00000100";

	public EnchantmentArrowInfinite(int p_i1921_1_, int p_i1921_2_) {
		super(p_i1921_1_, p_i1921_2_, EnumEnchantmentType.bow);
		setName("arrowInfinite");
	}

	@Override
	public int getMinEnchantability(int p_77321_1_) {
		return 20;
	}

	@Override
	public int getMaxEnchantability(int p_77317_1_) {
		return 50;
	}

	@Override
	public int getMaxLevel() {
		return 1;
	}
}