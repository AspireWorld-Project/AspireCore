package net.minecraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import org.bukkit.craftbukkit.event.CraftEventFactory;

import java.util.Random;

public class BlockStaticLiquid extends BlockLiquid {
	protected BlockStaticLiquid(Material p_i45429_1_) {
		super(p_i45429_1_);
		setTickRandomly(false);

		if (p_i45429_1_ == Material.lava) {
			setTickRandomly(true);
		}
	}

	@Override
	public void onNeighborBlockChange(World p_149695_1_, int p_149695_2_, int p_149695_3_, int p_149695_4_,
			Block p_149695_5_) {
		super.onNeighborBlockChange(p_149695_1_, p_149695_2_, p_149695_3_, p_149695_4_, p_149695_5_);

		if (p_149695_1_.getBlock(p_149695_2_, p_149695_3_, p_149695_4_) == this) {
			setNotStationary(p_149695_1_, p_149695_2_, p_149695_3_, p_149695_4_);
		}
	}

	private void setNotStationary(World p_149818_1_, int p_149818_2_, int p_149818_3_, int p_149818_4_) {
		int l = p_149818_1_.getBlockMetadata(p_149818_2_, p_149818_3_, p_149818_4_);
		p_149818_1_.setBlock(p_149818_2_, p_149818_3_, p_149818_4_, Block.getBlockById(Block.getIdFromBlock(this) - 1),
				l, 2);
		p_149818_1_.scheduleBlockUpdate(p_149818_2_, p_149818_3_, p_149818_4_,
				Block.getBlockById(Block.getIdFromBlock(this) - 1), tickRate(p_149818_1_));
	}

	@Override
	public void updateTick(World world, int x, int y, int z, Random random) {
		if (this.getMaterial() == Material.lava) {
			int l = random.nextInt(3);
			int i1 = 0;
			int igniterX = x;
			int igniterY = y;
			int igniterZ = z;
			while (true) {
				if (i1 >= l) {
					if (l == 0) {
						i1 = x;
						int k1 = z;

						for (int j1 = 0; j1 < 3; ++j1) {
							x = i1 + random.nextInt(3) - 1;
							z = k1 + random.nextInt(3) - 1;
							if (world.isAirBlock(x, y + 1, z) && this.isFlammable(world, x, y, z)) {
								if (world.getBlock(x, y + 1, z) != Blocks.fire)
									if (CraftEventFactory
											.callBlockIgniteEvent(world, x, y + 1, z, igniterX, igniterY, igniterZ)
											.isCancelled()) {
										continue;
									}
								world.setBlock(x, y + 1, z, Blocks.fire);
							}
						}
					}
					break;
				}
				x += random.nextInt(3) - 1;
				++y;
				z += random.nextInt(3) - 1;
				Block block = world.getBlock(x, y, z);
				if (block.getMaterial() == Material.air) {
					if (this.isFlammable(world, x - 1, y, z) || this.isFlammable(world, x + 1, y, z)
							|| this.isFlammable(world, x, y, z - 1) || this.isFlammable(world, x, y, z + 1)
							|| this.isFlammable(world, x, y - 1, z) || this.isFlammable(world, x, y + 1, z)) {
						if (world.getBlock(x, y, z) != Blocks.fire)
							if (CraftEventFactory.callBlockIgniteEvent(world, x, y, z, igniterX, igniterY, igniterZ)
									.isCancelled()) {
								continue;
							}
						world.setBlock(x, y, z, Blocks.fire);
						return;
					}
				} else if (block.getMaterial().blocksMovement())
					return;
				++i1;
			}
		}
	}

	private boolean isFlammable(World p_149817_1_, int p_149817_2_, int p_149817_3_, int p_149817_4_) {
		return p_149817_1_.getBlock(p_149817_2_, p_149817_3_, p_149817_4_).getMaterial().getCanBurn();
	}
}