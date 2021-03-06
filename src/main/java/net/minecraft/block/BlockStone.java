package net.minecraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;

import java.util.Random;

public class BlockStone extends Block {
	public BlockStone() {
		super(Material.rock);
		setCreativeTab(CreativeTabs.tabBlock);
	}

	@Override
	public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_) {
		return Item.getItemFromBlock(Blocks.cobblestone);
	}
}