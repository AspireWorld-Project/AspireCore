package net.minecraft.world.gen.feature;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import java.util.Random;

public class WorldGenMinable extends WorldGenerator {
	private final Block field_150519_a;
	private final int numberOfBlocks;
	private final Block field_150518_c;
	private int mineableBlockMeta;

	public WorldGenMinable(Block p_i45459_1_, int p_i45459_2_) {
		this(p_i45459_1_, p_i45459_2_, Blocks.stone);
	}

	public WorldGenMinable(Block p_i45460_1_, int p_i45460_2_, Block p_i45460_3_) {
		field_150519_a = p_i45460_1_;
		numberOfBlocks = p_i45460_2_;
		field_150518_c = p_i45460_3_;
	}

	public WorldGenMinable(Block block, int meta, int number, Block target) {
		this(block, number, target);
		mineableBlockMeta = meta;
	}

	@Override
	public boolean generate(World p_76484_1_, Random p_76484_2_, int p_76484_3_, int p_76484_4_, int p_76484_5_) {
		float f = p_76484_2_.nextFloat() * (float) Math.PI;
		double d0 = p_76484_3_ + 8 + MathHelper.sin(f) * numberOfBlocks / 8.0F;
		double d1 = p_76484_3_ + 8 - MathHelper.sin(f) * numberOfBlocks / 8.0F;
		double d2 = p_76484_5_ + 8 + MathHelper.cos(f) * numberOfBlocks / 8.0F;
		double d3 = p_76484_5_ + 8 - MathHelper.cos(f) * numberOfBlocks / 8.0F;
		double d4 = p_76484_4_ + p_76484_2_.nextInt(3) - 2;
		double d5 = p_76484_4_ + p_76484_2_.nextInt(3) - 2;

		for (int l = 0; l <= numberOfBlocks; ++l) {
			double d6 = d0 + (d1 - d0) * l / numberOfBlocks;
			double d7 = d4 + (d5 - d4) * l / numberOfBlocks;
			double d8 = d2 + (d3 - d2) * l / numberOfBlocks;
			double d9 = p_76484_2_.nextDouble() * numberOfBlocks / 16.0D;
			double d10 = (MathHelper.sin(l * (float) Math.PI / numberOfBlocks) + 1.0F) * d9 + 1.0D;
			double d11 = (MathHelper.sin(l * (float) Math.PI / numberOfBlocks) + 1.0F) * d9 + 1.0D;
			int i1 = MathHelper.floor_double(d6 - d10 / 2.0D);
			int j1 = MathHelper.floor_double(d7 - d11 / 2.0D);
			int k1 = MathHelper.floor_double(d8 - d10 / 2.0D);
			int l1 = MathHelper.floor_double(d6 + d10 / 2.0D);
			int i2 = MathHelper.floor_double(d7 + d11 / 2.0D);
			int j2 = MathHelper.floor_double(d8 + d10 / 2.0D);

			for (int k2 = i1; k2 <= l1; ++k2) {
				double d12 = (k2 + 0.5D - d6) / (d10 / 2.0D);

				if (d12 * d12 < 1.0D) {
					for (int l2 = j1; l2 <= i2; ++l2) {
						double d13 = (l2 + 0.5D - d7) / (d11 / 2.0D);

						if (d12 * d12 + d13 * d13 < 1.0D) {
							for (int i3 = k1; i3 <= j2; ++i3) {
								double d14 = (i3 + 0.5D - d8) / (d10 / 2.0D);

								if (d12 * d12 + d13 * d13 + d14 * d14 < 1.0D && p_76484_1_.getBlock(k2, l2, i3)
										.isReplaceableOreGen(p_76484_1_, k2, l2, i3, field_150518_c)) {
									p_76484_1_.setBlock(k2, l2, i3, field_150519_a, mineableBlockMeta, 2);
								}
							}
						}
					}
				}
			}
		}

		return true;
	}
}