package net.minecraft.world.gen.feature;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

import java.util.Random;

public class WorldGenHellLava extends WorldGenerator {
	private final Block field_150553_a;
	private final boolean field_94524_b;
	public WorldGenHellLava(Block p_i45453_1_, boolean p_i45453_2_) {
		field_150553_a = p_i45453_1_;
		field_94524_b = p_i45453_2_;
	}

	@Override
	public boolean generate(World p_76484_1_, Random p_76484_2_, int p_76484_3_, int p_76484_4_, int p_76484_5_) {
		if (p_76484_1_.getBlock(p_76484_3_, p_76484_4_ + 1, p_76484_5_) != Blocks.netherrack)
			return false;
		else if (p_76484_1_.getBlock(p_76484_3_, p_76484_4_, p_76484_5_).getMaterial() != Material.air
				&& p_76484_1_.getBlock(p_76484_3_, p_76484_4_, p_76484_5_) != Blocks.netherrack)
			return false;
		else {
			int l = 0;

			if (p_76484_1_.getBlock(p_76484_3_ - 1, p_76484_4_, p_76484_5_) == Blocks.netherrack) {
				++l;
			}

			if (p_76484_1_.getBlock(p_76484_3_ + 1, p_76484_4_, p_76484_5_) == Blocks.netherrack) {
				++l;
			}

			if (p_76484_1_.getBlock(p_76484_3_, p_76484_4_, p_76484_5_ - 1) == Blocks.netherrack) {
				++l;
			}

			if (p_76484_1_.getBlock(p_76484_3_, p_76484_4_, p_76484_5_ + 1) == Blocks.netherrack) {
				++l;
			}

			if (p_76484_1_.getBlock(p_76484_3_, p_76484_4_ - 1, p_76484_5_) == Blocks.netherrack) {
				++l;
			}

			int i1 = 0;

			if (p_76484_1_.isAirBlock(p_76484_3_ - 1, p_76484_4_, p_76484_5_)) {
				++i1;
			}

			if (p_76484_1_.isAirBlock(p_76484_3_ + 1, p_76484_4_, p_76484_5_)) {
				++i1;
			}

			if (p_76484_1_.isAirBlock(p_76484_3_, p_76484_4_, p_76484_5_ - 1)) {
				++i1;
			}

			if (p_76484_1_.isAirBlock(p_76484_3_, p_76484_4_, p_76484_5_ + 1)) {
				++i1;
			}

			if (p_76484_1_.isAirBlock(p_76484_3_, p_76484_4_ - 1, p_76484_5_)) {
				++i1;
			}

			if (!field_94524_b && l == 4 && i1 == 1 || l == 5) {
				p_76484_1_.setBlock(p_76484_3_, p_76484_4_, p_76484_5_, field_150553_a, 0, 2);
				p_76484_1_.scheduledUpdatesAreImmediate = true;
				field_150553_a.updateTick(p_76484_1_, p_76484_3_, p_76484_4_, p_76484_5_, p_76484_2_);
				p_76484_1_.scheduledUpdatesAreImmediate = false;
			}

			return true;
		}
	}
}