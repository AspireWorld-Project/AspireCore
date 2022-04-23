package net.minecraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.event.block.BlockFromToEvent;

import java.util.Random;

public class BlockDynamicLiquid extends BlockLiquid {
	int field_149815_a;
	boolean[] field_149814_b = new boolean[4];
	int[] field_149816_M = new int[4];
	private static final String __OBFID = "CL_00000234";

	protected BlockDynamicLiquid(Material p_i45403_1_) {
		super(p_i45403_1_);
	}

	private void func_149811_n(World p_149811_1_, int p_149811_2_, int p_149811_3_, int p_149811_4_) {
		int l = p_149811_1_.getBlockMetadata(p_149811_2_, p_149811_3_, p_149811_4_);
		p_149811_1_.setBlock(p_149811_2_, p_149811_3_, p_149811_4_, Block.getBlockById(Block.getIdFromBlock(this) + 1),
				l, 2);
	}

	@Override
	public void updateTick(World world, int x, int y, int z, Random random) {
		org.bukkit.block.Block source = world.getWorld() == null ? null : world.getWorld().getBlockAt(x, y, z);
		int l = func_149804_e(world, x, y, z);
		byte b0 = 1;
		if (blockMaterial == Material.lava && !world.provider.isHellWorld) {
			b0 = 2;
		}
		boolean flag = true;
		int i1 = tickRate(world);
		int j1;
		if (l > 0) {
			byte b1 = -100;
			field_149815_a = 0;
			int l1 = func_149810_a(world, x - 1, y, z, b1);
			l1 = func_149810_a(world, x + 1, y, z, l1);
			l1 = func_149810_a(world, x, y, z - 1, l1);
			l1 = func_149810_a(world, x, y, z + 1, l1);
			j1 = l1 + b0;
			if (j1 >= 8 || l1 < 0) {
				j1 = -1;
			}
			if (func_149804_e(world, x, y + 1, z) >= 0) {
				int k1 = func_149804_e(world, x, y + 1, z);
				if (k1 >= 8) {
					j1 = k1;
				} else {
					j1 = k1 + 8;
				}
			}
			if (field_149815_a >= 2 && blockMaterial == Material.water) {
				if (world.getBlock(x, y - 1, z).getMaterial().isSolid()) {
					j1 = 0;
				} else if (world.getBlock(x, y - 1, z).getMaterial() == blockMaterial
						&& world.getBlockMetadata(x, y - 1, z) == 0) {
					j1 = 0;
				}
			}
			if (blockMaterial == Material.lava && l < 8 && j1 < 8 && j1 > l && random.nextInt(4) != 0) {
				i1 *= 4;
			}
			if (j1 == l) {
				if (flag) {
					func_149811_n(world, x, y, z);
				}
			} else {
				l = j1;
				if (j1 < 0) {
					world.setBlockToAir(x, y, z);
				} else {
					world.setBlockMetadataWithNotify(x, y, z, j1, 2);
					world.scheduleBlockUpdate(x, y, z, this, i1);
					world.notifyBlocksOfNeighborChange(x, y, z, this);
				}
			}
		} else {
			func_149811_n(world, x, y, z);
		}

		if (func_149809_q(world, x, y - 1, z)) {
			if (world.getType(x, y, z).getMaterial() != blockMaterial)
				return;
			BlockFromToEvent event = new BlockFromToEvent(source, BlockFace.DOWN);
			CraftServer server = world.getServer();
			if (server != null && source != null) {
				server.getPluginManager().callEvent(event);
			}
			if (!event.isCancelled()) {
				if (blockMaterial == Material.lava && world.getBlock(x, y - 1, z).getMaterial() == Material.water) {
					world.setBlock(x, y - 1, z, Blocks.stone);
					func_149799_m(world, x, y - 1, z);
					return;
				}
				if (l >= 8) {
					func_149813_h(world, x, y - 1, z, l);
				} else {
					func_149813_h(world, x, y - 1, z, l + 8);
				}
			}
		} else if (l >= 0 && (l == 0 || func_149807_p(world, x, y - 1, z))) {
			boolean[] aboolean = func_149808_o(world, x, y, z);
			j1 = l + b0;
			if (l >= 8) {
				j1 = 1;
			}
			if (j1 >= 8)
				return;
			// CraftBukkit start - All four cardinal directions. Do not change the order!
			BlockFace[] faces = new BlockFace[] { BlockFace.WEST, BlockFace.EAST, BlockFace.NORTH, BlockFace.SOUTH };
			int index = 0;
			CraftServer server = world.getServer();
			for (BlockFace currentFace : faces) {
				if (aboolean[index]) {
					BlockFromToEvent event = new BlockFromToEvent(source, currentFace);
					if (server != null && source != null) {
						server.getPluginManager().callEvent(event);
					}
					if (!event.isCancelled()) {
						func_149813_h(world, x + currentFace.getModX(), y, z + currentFace.getModZ(), j1);
					}
				}
				index++;
			}
		}
	}

	private void func_149813_h(World p_149813_1_, int p_149813_2_, int p_149813_3_, int p_149813_4_, int p_149813_5_) {
		if (func_149809_q(p_149813_1_, p_149813_2_, p_149813_3_, p_149813_4_)) {
			Block block = p_149813_1_.getBlock(p_149813_2_, p_149813_3_, p_149813_4_);

			if (blockMaterial == Material.lava) {
				func_149799_m(p_149813_1_, p_149813_2_, p_149813_3_, p_149813_4_);
			} else {
				block.dropBlockAsItem(p_149813_1_, p_149813_2_, p_149813_3_, p_149813_4_,
						p_149813_1_.getBlockMetadata(p_149813_2_, p_149813_3_, p_149813_4_), 0);
			}

			p_149813_1_.setBlock(p_149813_2_, p_149813_3_, p_149813_4_, this, p_149813_5_, 3);
		}
	}

	private int func_149812_c(World p_149812_1_, int p_149812_2_, int p_149812_3_, int p_149812_4_, int p_149812_5_,
			int p_149812_6_) {
		int j1 = 1000;

		for (int k1 = 0; k1 < 4; ++k1) {
			if ((k1 != 0 || p_149812_6_ != 1) && (k1 != 1 || p_149812_6_ != 0) && (k1 != 2 || p_149812_6_ != 3)
					&& (k1 != 3 || p_149812_6_ != 2)) {
				int l1 = p_149812_2_;
				int i2 = p_149812_4_;

				if (k1 == 0) {
					l1 = p_149812_2_ - 1;
				}

				if (k1 == 1) {
					++l1;
				}

				if (k1 == 2) {
					i2 = p_149812_4_ - 1;
				}

				if (k1 == 3) {
					++i2;
				}

				if (!func_149807_p(p_149812_1_, l1, p_149812_3_, i2)
						&& (p_149812_1_.getBlock(l1, p_149812_3_, i2).getMaterial() != blockMaterial
								|| p_149812_1_.getBlockMetadata(l1, p_149812_3_, i2) != 0)) {
					if (!func_149807_p(p_149812_1_, l1, p_149812_3_ - 1, i2))
						return p_149812_5_;

					if (p_149812_5_ < 4) {
						int j2 = func_149812_c(p_149812_1_, l1, p_149812_3_, i2, p_149812_5_ + 1, k1);

						if (j2 < j1) {
							j1 = j2;
						}
					}
				}
			}
		}

		return j1;
	}

	private boolean[] func_149808_o(World p_149808_1_, int p_149808_2_, int p_149808_3_, int p_149808_4_) {
		int l;
		int i1;

		for (l = 0; l < 4; ++l) {
			field_149816_M[l] = 1000;
			i1 = p_149808_2_;
			int j1 = p_149808_4_;

			if (l == 0) {
				i1 = p_149808_2_ - 1;
			}

			if (l == 1) {
				++i1;
			}

			if (l == 2) {
				j1 = p_149808_4_ - 1;
			}

			if (l == 3) {
				++j1;
			}

			if (!func_149807_p(p_149808_1_, i1, p_149808_3_, j1)
					&& (p_149808_1_.getBlock(i1, p_149808_3_, j1).getMaterial() != blockMaterial
							|| p_149808_1_.getBlockMetadata(i1, p_149808_3_, j1) != 0)) {
				if (func_149807_p(p_149808_1_, i1, p_149808_3_ - 1, j1)) {
					field_149816_M[l] = func_149812_c(p_149808_1_, i1, p_149808_3_, j1, 1, l);
				} else {
					field_149816_M[l] = 0;
				}
			}
		}

		l = field_149816_M[0];

		for (i1 = 1; i1 < 4; ++i1) {
			if (field_149816_M[i1] < l) {
				l = field_149816_M[i1];
			}
		}

		for (i1 = 0; i1 < 4; ++i1) {
			field_149814_b[i1] = field_149816_M[i1] == l;
		}

		return field_149814_b;
	}

	private boolean func_149807_p(World p_149807_1_, int p_149807_2_, int p_149807_3_, int p_149807_4_) {
		Block block = p_149807_1_.getBlock(p_149807_2_, p_149807_3_, p_149807_4_);
		return block != Blocks.wooden_door && block != Blocks.iron_door && block != Blocks.standing_sign
				&& block != Blocks.ladder && block != Blocks.reeds
						? block.blockMaterial == Material.portal ? true : block.blockMaterial.blocksMovement()
						: true;
	}

	protected int func_149810_a(World p_149810_1_, int p_149810_2_, int p_149810_3_, int p_149810_4_, int p_149810_5_) {
		int i1 = func_149804_e(p_149810_1_, p_149810_2_, p_149810_3_, p_149810_4_);

		if (i1 < 0)
			return p_149810_5_;
		else {
			if (i1 == 0) {
				++field_149815_a;
			}

			if (i1 >= 8) {
				i1 = 0;
			}

			return p_149810_5_ >= 0 && i1 >= p_149810_5_ ? p_149810_5_ : i1;
		}
	}

	private boolean func_149809_q(World p_149809_1_, int p_149809_2_, int p_149809_3_, int p_149809_4_) {
		Material material = p_149809_1_.getBlock(p_149809_2_, p_149809_3_, p_149809_4_).getMaterial();
		return material == blockMaterial ? false
				: material == Material.lava ? false
						: !func_149807_p(p_149809_1_, p_149809_2_, p_149809_3_, p_149809_4_);
	}

	@Override
	public void onBlockAdded(World p_149726_1_, int p_149726_2_, int p_149726_3_, int p_149726_4_) {
		super.onBlockAdded(p_149726_1_, p_149726_2_, p_149726_3_, p_149726_4_);

		if (p_149726_1_.getBlock(p_149726_2_, p_149726_3_, p_149726_4_) == this) {
			p_149726_1_.scheduleBlockUpdate(p_149726_2_, p_149726_3_, p_149726_4_, this, tickRate(p_149726_1_));
		}
	}

	@Override
	public boolean func_149698_L() {
		return true;
	}
}