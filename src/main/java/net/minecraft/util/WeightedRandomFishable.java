package net.minecraft.util;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;

import java.util.Random;

public class WeightedRandomFishable extends WeightedRandom.Item {
	private final ItemStack field_150711_b;
	private float field_150712_c;
	private boolean field_150710_d;
	public WeightedRandomFishable(ItemStack p_i45317_1_, int p_i45317_2_) {
		super(p_i45317_2_);
		field_150711_b = p_i45317_1_;
	}

	public ItemStack func_150708_a(Random p_150708_1_) {
		ItemStack itemstack = field_150711_b.copy();

		if (field_150712_c > 0.0F) {
			int i = (int) (field_150712_c * field_150711_b.getMaxDamage());
			int j = itemstack.getMaxDamage() - p_150708_1_.nextInt(p_150708_1_.nextInt(i) + 1);

			if (j > i) {
				j = i;
			}

			if (j < 1) {
				j = 1;
			}

			itemstack.setItemDamage(j);
		}

		if (field_150710_d) {
			EnchantmentHelper.addRandomEnchantment(p_150708_1_, itemstack, 30);
		}

		return itemstack;
	}

	public WeightedRandomFishable func_150709_a(float p_150709_1_) {
		field_150712_c = p_150709_1_;
		return this;
	}

	public WeightedRandomFishable func_150707_a() {
		field_150710_d = true;
		return this;
	}

	public ItemStack getField_150711_b() {
		return field_150711_b;
	}

	public float getField_150712_c() {
		return field_150712_c;
	}

	public boolean getField_150710_d() {
		return field_150710_d;
	}
}