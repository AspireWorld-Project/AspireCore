package net.minecraft.world.gen.feature;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.Random;

public class WorldGenShrub extends WorldGenTrees {
	private final int field_150528_a;
	private final int field_150527_b;
	public WorldGenShrub(int p_i2015_1_, int p_i2015_2_) {
		super(false);
		field_150527_b = p_i2015_1_;
		field_150528_a = p_i2015_2_;
	}

	@Override
	public boolean generate(World p_76484_1_, Random p_76484_2_, int p_76484_3_, int p_76484_4_, int p_76484_5_) {
		Block block;

		do {
			block = p_76484_1_.getBlock(p_76484_3_, p_76484_4_, p_76484_5_);
			if (!(block.isLeaves(p_76484_1_, p_76484_3_, p_76484_4_, p_76484_5_)
					|| block.isAir(p_76484_1_, p_76484_3_, p_76484_4_, p_76484_5_))) {
				break;
			}
			--p_76484_4_;
		} while (p_76484_4_ > 0);

		Block block1 = p_76484_1_.getBlock(p_76484_3_, p_76484_4_, p_76484_5_);

		if (block1.canSustainPlant(p_76484_1_, p_76484_3_, p_76484_4_, p_76484_5_, ForgeDirection.UP,
				(IPlantable) Blocks.sapling)) {
			++p_76484_4_;
			setBlockAndNotifyAdequately(p_76484_1_, p_76484_3_, p_76484_4_, p_76484_5_, Blocks.log, field_150527_b);

			for (int l = p_76484_4_; l <= p_76484_4_ + 2; ++l) {
				int i1 = l - p_76484_4_;
				int j1 = 2 - i1;

				for (int k1 = p_76484_3_ - j1; k1 <= p_76484_3_ + j1; ++k1) {
					int l1 = k1 - p_76484_3_;

					for (int i2 = p_76484_5_ - j1; i2 <= p_76484_5_ + j1; ++i2) {
						int j2 = i2 - p_76484_5_;

						if ((Math.abs(l1) != j1 || Math.abs(j2) != j1 || p_76484_2_.nextInt(2) != 0)
								&& p_76484_1_.getBlock(k1, l, i2).canBeReplacedByLeaves(p_76484_1_, k1, l, i2)) {
							setBlockAndNotifyAdequately(p_76484_1_, k1, l, i2, Blocks.leaves, field_150528_a);
						}
					}
				}
			}
		}

		return true;
	}
}