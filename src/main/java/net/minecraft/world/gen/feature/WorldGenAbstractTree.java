package net.minecraft.world.gen.feature;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

import java.util.Random;

public abstract class WorldGenAbstractTree extends WorldGenerator {
	public WorldGenAbstractTree(boolean p_i45448_1_) {
		super(p_i45448_1_);
	}

	protected boolean func_150523_a(Block p_150523_1_) {
		return p_150523_1_.getMaterial() == Material.air || p_150523_1_.getMaterial() == Material.leaves
				|| p_150523_1_ == Blocks.grass || p_150523_1_ == Blocks.dirt || p_150523_1_ == Blocks.log
				|| p_150523_1_ == Blocks.log2 || p_150523_1_ == Blocks.sapling || p_150523_1_ == Blocks.vine;
	}

	public void func_150524_b(World p_150524_1_, Random p_150524_2_, int p_150524_3_, int p_150524_4_,
			int p_150524_5_) {
	}

	protected boolean isReplaceable(World world, int x, int y, int z) {
		Block block = world.getBlock(x, y, z);
		return block.isAir(world, x, y, z) || block.isLeaves(world, x, y, z) || block.isWood(world, x, y, z)
				|| func_150523_a(block);
	}
}