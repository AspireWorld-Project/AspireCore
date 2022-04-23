package net.minecraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

import java.util.Random;

public class BlockPackedIce extends Block {
	public BlockPackedIce() {
		super(Material.packedIce);
		slipperiness = 0.98F;
		setCreativeTab(CreativeTabs.tabBlock);
	}

	@Override
	public int quantityDropped(Random p_149745_1_) {
		return 0;
	}
}