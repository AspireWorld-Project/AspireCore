package net.minecraft.enchantment;

import net.minecraft.item.*;

public enum EnumEnchantmentType {
	all, armor, armor_feet, armor_legs, armor_torso, armor_head, weapon, digger, fishing_rod, breakable, bow;

	private static final String __OBFID = "CL_00000106";

	public boolean canEnchantItem(Item p_77557_1_) {
		if (this == all)
			return true;
		else if (this == breakable && p_77557_1_.isDamageable())
			return true;
		else if (p_77557_1_ instanceof ItemArmor) {
			if (this == armor)
				return true;
			else {
				ItemArmor itemarmor = (ItemArmor) p_77557_1_;
				return itemarmor.armorType == 0 ? this == armor_head
						: itemarmor.armorType == 2 ? this == armor_legs
								: itemarmor.armorType == 1 ? this == armor_torso
										: itemarmor.armorType == 3 && this == armor_feet;
			}
		} else
			return p_77557_1_ instanceof ItemSword ? this == weapon
					: p_77557_1_ instanceof ItemTool ? this == digger
							: p_77557_1_ instanceof ItemBow ? this == bow
									: p_77557_1_ instanceof ItemFishingRod && this == fishing_rod;
	}
}