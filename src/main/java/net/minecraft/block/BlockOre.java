package net.minecraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

public class BlockOre extends Block {
	public BlockOre() {
		super(Material.rock);
		setCreativeTab(CreativeTabs.tabBlock);
	}

	@Override
	public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_) {
		return this == Blocks.coal_ore ? Items.coal
				: this == Blocks.diamond_ore ? Items.diamond
						: this == Blocks.lapis_ore ? Items.dye
								: this == Blocks.emerald_ore ? Items.emerald
										: this == Blocks.quartz_ore ? Items.quartz : Item.getItemFromBlock(this);
	}

	@Override
	public int quantityDropped(Random p_149745_1_) {
		return this == Blocks.lapis_ore ? 4 + p_149745_1_.nextInt(5) : 1;
	}

	@Override
	public int quantityDroppedWithBonus(int p_149679_1_, Random p_149679_2_) {
		if (p_149679_1_ > 0 && Item.getItemFromBlock(this) != getItemDropped(0, p_149679_2_, p_149679_1_)) {
			int j = p_149679_2_.nextInt(p_149679_1_ + 2) - 1;

			if (j < 0) {
				j = 0;
			}

			return this.quantityDropped(p_149679_2_) * (j + 1);
		} else
			return this.quantityDropped(p_149679_2_);
	}

	@Override
	public void dropBlockAsItemWithChance(World p_149690_1_, int p_149690_2_, int p_149690_3_, int p_149690_4_,
			int p_149690_5_, float p_149690_6_, int p_149690_7_) {
		super.dropBlockAsItemWithChance(p_149690_1_, p_149690_2_, p_149690_3_, p_149690_4_, p_149690_5_, p_149690_6_,
				p_149690_7_);
	}

	private final Random rand = new Random();

	@Override
	public int getExpDrop(IBlockAccess p_149690_1_, int p_149690_5_, int p_149690_7_) {
		if (getItemDropped(p_149690_5_, rand, p_149690_7_) != Item.getItemFromBlock(this)) {
			int j1 = 0;

			if (this == Blocks.coal_ore) {
				j1 = MathHelper.getRandomIntegerInRange(rand, 0, 2);
			} else if (this == Blocks.diamond_ore) {
				j1 = MathHelper.getRandomIntegerInRange(rand, 3, 7);
			} else if (this == Blocks.emerald_ore) {
				j1 = MathHelper.getRandomIntegerInRange(rand, 3, 7);
			} else if (this == Blocks.lapis_ore) {
				j1 = MathHelper.getRandomIntegerInRange(rand, 2, 5);
			} else if (this == Blocks.quartz_ore) {
				j1 = MathHelper.getRandomIntegerInRange(rand, 2, 5);
			}

			return j1;
		}
		return 0;
	}

	@Override
	public int damageDropped(int p_149692_1_) {
		return this == Blocks.lapis_ore ? 4 : 0;
	}
}