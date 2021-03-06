package net.minecraft.enchantment;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class EnchantmentDamage extends Enchantment {
	private static final String[] protectionName = new String[] { "all", "undead", "arthropods" };
	private static final int[] baseEnchantability = new int[] { 1, 5, 5 };
	private static final int[] levelEnchantability = new int[] { 11, 8, 8 };
	private static final int[] thresholdEnchantability = new int[] { 20, 20, 20 };
	public final int damageType;
	private static final String __OBFID = "CL_00000102";

	public EnchantmentDamage(int p_i1923_1_, int p_i1923_2_, int p_i1923_3_) {
		super(p_i1923_1_, p_i1923_2_, EnumEnchantmentType.weapon);
		damageType = p_i1923_3_;
	}

	@Override
	public int getMinEnchantability(int p_77321_1_) {
		return baseEnchantability[damageType] + (p_77321_1_ - 1) * levelEnchantability[damageType];
	}

	@Override
	public int getMaxEnchantability(int p_77317_1_) {
		return getMinEnchantability(p_77317_1_) + thresholdEnchantability[damageType];
	}

	@Override
	public int getMaxLevel() {
		return 5;
	}

	@Override
	public float func_152376_a(int p_152376_1_, EnumCreatureAttribute p_152376_2_) {
		return damageType == 0 ? p_152376_1_ * 1.25F
				: damageType == 1 && p_152376_2_ == EnumCreatureAttribute.UNDEAD ? p_152376_1_ * 2.5F
						: damageType == 2 && p_152376_2_ == EnumCreatureAttribute.ARTHROPOD ? p_152376_1_ * 2.5F : 0.0F;
	}

	@Override
	public String getName() {
		return "enchantment.damage." + protectionName[damageType];
	}

	@Override
	public boolean canApplyTogether(Enchantment p_77326_1_) {
		return !(p_77326_1_ instanceof EnchantmentDamage);
	}

	@Override
	public boolean canApply(ItemStack p_92089_1_) {
		return p_92089_1_.getItem() instanceof ItemAxe || super.canApply(p_92089_1_);
	}

	@Override
	public void func_151368_a(EntityLivingBase p_151368_1_, Entity p_151368_2_, int p_151368_3_) {
		if (p_151368_2_ instanceof EntityLivingBase) {
			EntityLivingBase entitylivingbase1 = (EntityLivingBase) p_151368_2_;

			if (damageType == 2 && entitylivingbase1.getCreatureAttribute() == EnumCreatureAttribute.ARTHROPOD) {
				int j = 20 + p_151368_1_.getRNG().nextInt(10 * p_151368_3_);
				entitylivingbase1.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, j, 3));
			}
		}
	}
}