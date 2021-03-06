package net.minecraft.world.gen.feature;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

import java.util.Random;

public class WorldGenIcePath extends WorldGenerator {
	private final Block field_150555_a;
	private final int field_150554_b;
	public WorldGenIcePath(int p_i45454_1_) {
		field_150555_a = Blocks.packed_ice;
		field_150554_b = p_i45454_1_;
	}

	@Override
	public boolean generate(World p_76484_1_, Random p_76484_2_, int p_76484_3_, int p_76484_4_, int p_76484_5_) {
		while (p_76484_1_.isAirBlock(p_76484_3_, p_76484_4_, p_76484_5_) && p_76484_4_ > 2) {
			--p_76484_4_;
		}

		if (p_76484_1_.getBlock(p_76484_3_, p_76484_4_, p_76484_5_) != Blocks.snow)
			return false;
		else {
			int l = p_76484_2_.nextInt(field_150554_b - 2) + 2;
			byte b0 = 1;

			for (int i1 = p_76484_3_ - l; i1 <= p_76484_3_ + l; ++i1) {
				for (int j1 = p_76484_5_ - l; j1 <= p_76484_5_ + l; ++j1) {
					int k1 = i1 - p_76484_3_;
					int l1 = j1 - p_76484_5_;

					if (k1 * k1 + l1 * l1 <= l * l) {
						for (int i2 = p_76484_4_ - b0; i2 <= p_76484_4_ + b0; ++i2) {
							Block block = p_76484_1_.getBlock(i1, i2, j1);

							if (block == Blocks.dirt || block == Blocks.snow || block == Blocks.ice) {
								p_76484_1_.setBlock(i1, i2, j1, field_150555_a, 0, 2);
							}
						}
					}
				}
			}

			return true;
		}
	}
}