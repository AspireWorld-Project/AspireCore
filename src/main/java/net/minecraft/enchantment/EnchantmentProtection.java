package net.minecraft.enchantment;

import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;

public class EnchantmentProtection extends Enchantment {
	private static final String[] protectionName = new String[] { "all", "fire", "fall", "explosion", "projectile" };
	private static final int[] baseEnchantability = new int[] { 1, 10, 5, 5, 3 };
	private static final int[] levelEnchantability = new int[] { 11, 8, 6, 8, 6 };
	private static final int[] thresholdEnchantability = new int[] { 20, 12, 10, 12, 15 };
	public final int protectionType;
	private static final String __OBFID = "CL_00000121";

	public EnchantmentProtection(int p_i1936_1_, int p_i1936_2_, int p_i1936_3_) {
		super(p_i1936_1_, p_i1936_2_, EnumEnchantmentType.armor);
		protectionType = p_i1936_3_;

		if (p_i1936_3_ == 2) {
			type = EnumEnchantmentType.armor_feet;
		}
	}

	@Override
	public int getMinEnchantability(int p_77321_1_) {
		return baseEnchantability[protectionType] + (p_77321_1_ - 1) * levelEnchantability[protectionType];
	}

	@Override
	public int getMaxEnchantability(int p_77317_1_) {
		return getMinEnchantability(p_77317_1_) + thresholdEnchantability[protectionType];
	}

	@Override
	public int getMaxLevel() {
		return 4;
	}

	@Override
	public int calcModifierDamage(int p_77318_1_, DamageSource p_77318_2_) {
		if (p_77318_2_.canHarmInCreative())
			return 0;
		else {
			float f = (6 + p_77318_1_ * p_77318_1_) / 3.0F;
			return protectionType == 0 ? MathHelper.floor_float(f * 0.75F)
					: protectionType == 1 && p_77318_2_.isFireDamage() ? MathHelper.floor_float(f * 1.25F)
							: protectionType == 2 && p_77318_2_ == DamageSource.fall ? MathHelper.floor_float(f * 2.5F)
									: protectionType == 3 && p_77318_2_.isExplosion() ? MathHelper.floor_float(f * 1.5F)
											: protectionType == 4 && p_77318_2_.isProjectile()
													? MathHelper.floor_float(f * 1.5F)
													: 0;
		}
	}

	@Override
	public String getName() {
		return "enchantment.protect." + protectionName[protectionType];
	}

	@Override
	public boolean canApplyTogether(Enchantment p_77326_1_) {
		if (p_77326_1_ instanceof EnchantmentProtection) {
			EnchantmentProtection enchantmentprotection = (EnchantmentProtection) p_77326_1_;
			return enchantmentprotection.protectionType != protectionType && (protectionType == 2 || enchantmentprotection.protectionType == 2);
		} else
			return super.canApplyTogether(p_77326_1_);
	}

	public static int getFireTimeForEntity(Entity p_92093_0_, int p_92093_1_) {
		int j = EnchantmentHelper.getMaxEnchantmentLevel(Enchantment.fireProtection.effectId,
				p_92093_0_.getLastActiveItems());

		if (j > 0) {
			p_92093_1_ -= MathHelper.floor_float((float) p_92093_1_ * (float) j * 0.15F);
		}

		return p_92093_1_;
	}

	public static double func_92092_a(Entity p_92092_0_, double p_92092_1_) {
		int i = EnchantmentHelper.getMaxEnchantmentLevel(Enchantment.blastProtection.effectId,
				p_92092_0_.getLastActiveItems());

		if (i > 0) {
			p_92092_1_ -= MathHelper.floor_double(p_92092_1_ * (i * 0.15F));
		}

		return p_92092_1_;
	}
}