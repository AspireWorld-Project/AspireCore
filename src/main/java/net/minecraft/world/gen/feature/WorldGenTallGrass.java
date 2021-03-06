package net.minecraft.world.gen.feature;

import net.minecraft.block.Block;
import net.minecraft.world.World;

import java.util.Random;

public class WorldGenTallGrass extends WorldGenerator {
	private final Block field_150522_a;
	private final int tallGrassMetadata;
	public WorldGenTallGrass(Block p_i45466_1_, int p_i45466_2_) {
		field_150522_a = p_i45466_1_;
		tallGrassMetadata = p_i45466_2_;
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

		for (int l = 0; l < 128; ++l) {
			int i1 = p_76484_3_ + p_76484_2_.nextInt(8) - p_76484_2_.nextInt(8);
			int j1 = p_76484_4_ + p_76484_2_.nextInt(4) - p_76484_2_.nextInt(4);
			int k1 = p_76484_5_ + p_76484_2_.nextInt(8) - p_76484_2_.nextInt(8);

			if (p_76484_1_.isAirBlock(i1, j1, k1) && field_150522_a.canBlockStay(p_76484_1_, i1, j1, k1)) {
				p_76484_1_.setBlock(i1, j1, k1, field_150522_a, tallGrassMetadata, 2);
			}
		}

		return true;
	}
}