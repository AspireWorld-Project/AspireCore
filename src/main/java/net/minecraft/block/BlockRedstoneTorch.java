package net.minecraft.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.bukkit.Bukkit;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.plugin.PluginManager;

import java.util.*;

public class BlockRedstoneTorch extends BlockTorch {
	private final boolean field_150113_a;
	@SuppressWarnings("rawtypes")
	private static final Map field_150112_b = new HashMap();
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private boolean func_150111_a(World p_150111_1_, int p_150111_2_, int p_150111_3_, int p_150111_4_,
			boolean p_150111_5_) {
		if (!field_150112_b.containsKey(p_150111_1_)) {
			field_150112_b.put(p_150111_1_, new ArrayList());
		}

		List list = (List) field_150112_b.get(p_150111_1_);

		if (p_150111_5_) {
			list.add(new BlockRedstoneTorch.Toggle(p_150111_2_, p_150111_3_, p_150111_4_,
					p_150111_1_.getTotalWorldTime()));
		}

		int l = 0;

		for (int i1 = 0; i1 < list.size(); ++i1) {
			BlockRedstoneTorch.Toggle toggle = (BlockRedstoneTorch.Toggle) list.get(i1);

			if (toggle.field_150847_a == p_150111_2_ && toggle.field_150845_b == p_150111_3_
					&& toggle.field_150846_c == p_150111_4_) {
				++l;

				if (l >= 8)
					return true;
			}
		}

		return false;
	}

	protected BlockRedstoneTorch(boolean p_i45423_1_) {
		field_150113_a = p_i45423_1_;
		setTickRandomly(true);
		setCreativeTab(null);
	}

	@Override
	public int tickRate(World p_149738_1_) {
		return 2;
	}

	@Override
	public void onBlockAdded(World p_149726_1_, int p_149726_2_, int p_149726_3_, int p_149726_4_) {
		if (p_149726_1_.getBlockMetadata(p_149726_2_, p_149726_3_, p_149726_4_) == 0) {
			super.onBlockAdded(p_149726_1_, p_149726_2_, p_149726_3_, p_149726_4_);
		}

		if (field_150113_a) {
			p_149726_1_.notifyBlocksOfNeighborChange(p_149726_2_, p_149726_3_ - 1, p_149726_4_, this);
			p_149726_1_.notifyBlocksOfNeighborChange(p_149726_2_, p_149726_3_ + 1, p_149726_4_, this);
			p_149726_1_.notifyBlocksOfNeighborChange(p_149726_2_ - 1, p_149726_3_, p_149726_4_, this);
			p_149726_1_.notifyBlocksOfNeighborChange(p_149726_2_ + 1, p_149726_3_, p_149726_4_, this);
			p_149726_1_.notifyBlocksOfNeighborChange(p_149726_2_, p_149726_3_, p_149726_4_ - 1, this);
			p_149726_1_.notifyBlocksOfNeighborChange(p_149726_2_, p_149726_3_, p_149726_4_ + 1, this);
		}
	}

	@Override
	public void breakBlock(World p_149749_1_, int p_149749_2_, int p_149749_3_, int p_149749_4_, Block p_149749_5_,
			int p_149749_6_) {
		if (field_150113_a) {
			p_149749_1_.notifyBlocksOfNeighborChange(p_149749_2_, p_149749_3_ - 1, p_149749_4_, this);
			p_149749_1_.notifyBlocksOfNeighborChange(p_149749_2_, p_149749_3_ + 1, p_149749_4_, this);
			p_149749_1_.notifyBlocksOfNeighborChange(p_149749_2_ - 1, p_149749_3_, p_149749_4_, this);
			p_149749_1_.notifyBlocksOfNeighborChange(p_149749_2_ + 1, p_149749_3_, p_149749_4_, this);
			p_149749_1_.notifyBlocksOfNeighborChange(p_149749_2_, p_149749_3_, p_149749_4_ - 1, this);
			p_149749_1_.notifyBlocksOfNeighborChange(p_149749_2_, p_149749_3_, p_149749_4_ + 1, this);
		}
	}

	@Override
	public int isProvidingWeakPower(IBlockAccess p_149709_1_, int p_149709_2_, int p_149709_3_, int p_149709_4_,
			int p_149709_5_) {
		if (!field_150113_a)
			return 0;
		else {
			int i1 = p_149709_1_.getBlockMetadata(p_149709_2_, p_149709_3_, p_149709_4_);
			return i1 == 5 && p_149709_5_ == 1 ? 0
					: i1 == 3 && p_149709_5_ == 3 ? 0
							: i1 == 4 && p_149709_5_ == 2 ? 0
									: i1 == 1 && p_149709_5_ == 5 ? 0 : i1 == 2 && p_149709_5_ == 4 ? 0 : 15;
		}
	}

	private boolean func_150110_m(World p_150110_1_, int p_150110_2_, int p_150110_3_, int p_150110_4_) {
		int l = p_150110_1_.getBlockMetadata(p_150110_2_, p_150110_3_, p_150110_4_);
		return l == 5 && p_150110_1_.getIndirectPowerOutput(p_150110_2_, p_150110_3_ - 1, p_150110_4_, 0) || l == 3 && p_150110_1_.getIndirectPowerOutput(p_150110_2_, p_150110_3_, p_150110_4_ - 1, 2) || l == 4 && p_150110_1_.getIndirectPowerOutput(p_150110_2_, p_150110_3_, p_150110_4_ + 1, 3) || l == 1 && p_150110_1_.getIndirectPowerOutput(p_150110_2_ - 1, p_150110_3_,
				p_150110_4_, 4) || l == 2 && p_150110_1_.getIndirectPowerOutput(p_150110_2_ + 1,
				p_150110_3_, p_150110_4_, 5);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void updateTick(World world, int x, int y, int z, Random random) {
		boolean flag = func_150110_m(world, x, y, z);
		List list = (List) field_150112_b.get(world);

		while (list != null && !list.isEmpty()
				&& world.getTotalWorldTime() - ((BlockRedstoneTorch.Toggle) list.get(0)).field_150844_d > 60L) {
			list.remove(0);
		}

		PluginManager manager = Bukkit.getPluginManager();
		org.bukkit.block.Block block = world.getWorld().getBlockAt(x, y, z);
		int oldCurrent = field_150113_a ? 15 : 0;
		BlockRedstoneEvent event = new BlockRedstoneEvent(block, oldCurrent, oldCurrent);
		if (field_150113_a) {
			if (flag) {
				if (oldCurrent != 0) {
					event.setNewCurrent(0);
					manager.callEvent(event);
					if (event.getNewCurrent() != 0)
						return;
				}
				world.setBlock(x, y, z, Blocks.unlit_redstone_torch, world.getBlockMetadata(x, y, z), 3);
				if (func_150111_a(world, x, y, z, true)) {
					world.playSoundEffect(x + 0.5F, y + 0.5F, z + 0.5F, "random.fizz", 0.5F,
							2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);

					for (int l = 0; l < 5; ++l) {
						double d0 = x + random.nextDouble() * 0.6D + 0.2D;
						double d1 = y + random.nextDouble() * 0.6D + 0.2D;
						double d2 = z + random.nextDouble() * 0.6D + 0.2D;
						world.spawnParticle("smoke", d0, d1, d2, 0.0D, 0.0D, 0.0D);
					}
				}
			}
		} else if (!flag && !func_150111_a(world, x, y, z, false)) {
			if (oldCurrent != 15) {
				event.setNewCurrent(15);
				manager.callEvent(event);
				if (event.getNewCurrent() != 15)
					return;
			}
			world.setBlock(x, y, z, Blocks.redstone_torch, world.getBlockMetadata(x, y, z), 3);
		}
	}

	@Override
	public void onNeighborBlockChange(World p_149695_1_, int p_149695_2_, int p_149695_3_, int p_149695_4_,
			Block p_149695_5_) {
		if (!func_150108_b(p_149695_1_, p_149695_2_, p_149695_3_, p_149695_4_, p_149695_5_)) {
			boolean flag = func_150110_m(p_149695_1_, p_149695_2_, p_149695_3_, p_149695_4_);

			if (field_150113_a && flag || !field_150113_a && !flag) {
				p_149695_1_.scheduleBlockUpdate(p_149695_2_, p_149695_3_, p_149695_4_, this, tickRate(p_149695_1_));
			}
		}
	}

	@Override
	public int isProvidingStrongPower(IBlockAccess p_149748_1_, int p_149748_2_, int p_149748_3_, int p_149748_4_,
			int p_149748_5_) {
		return p_149748_5_ == 0 ? isProvidingWeakPower(p_149748_1_, p_149748_2_, p_149748_3_, p_149748_4_, p_149748_5_)
				: 0;
	}

	@Override
	public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_) {
		return Item.getItemFromBlock(Blocks.redstone_torch);
	}

	@Override
	public boolean canProvidePower() {
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(World p_149734_1_, int p_149734_2_, int p_149734_3_, int p_149734_4_,
			Random p_149734_5_) {
		if (field_150113_a) {
			int l = p_149734_1_.getBlockMetadata(p_149734_2_, p_149734_3_, p_149734_4_);
			double d0 = p_149734_2_ + 0.5F + (p_149734_5_.nextFloat() - 0.5F) * 0.2D;
			double d1 = p_149734_3_ + 0.7F + (p_149734_5_.nextFloat() - 0.5F) * 0.2D;
			double d2 = p_149734_4_ + 0.5F + (p_149734_5_.nextFloat() - 0.5F) * 0.2D;
			double d3 = 0.2199999988079071D;
			double d4 = 0.27000001072883606D;

			if (l == 1) {
				p_149734_1_.spawnParticle("reddust", d0 - d4, d1 + d3, d2, 0.0D, 0.0D, 0.0D);
			} else if (l == 2) {
				p_149734_1_.spawnParticle("reddust", d0 + d4, d1 + d3, d2, 0.0D, 0.0D, 0.0D);
			} else if (l == 3) {
				p_149734_1_.spawnParticle("reddust", d0, d1 + d3, d2 - d4, 0.0D, 0.0D, 0.0D);
			} else if (l == 4) {
				p_149734_1_.spawnParticle("reddust", d0, d1 + d3, d2 + d4, 0.0D, 0.0D, 0.0D);
			} else {
				p_149734_1_.spawnParticle("reddust", d0, d1, d2, 0.0D, 0.0D, 0.0D);
			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Item getItem(World p_149694_1_, int p_149694_2_, int p_149694_3_, int p_149694_4_) {
		return Item.getItemFromBlock(Blocks.redstone_torch);
	}

	@Override
	public boolean isAssociatedBlock(Block p_149667_1_) {
		return p_149667_1_ == Blocks.unlit_redstone_torch || p_149667_1_ == Blocks.redstone_torch;
	}

	public static class Toggle {
		int field_150847_a;
		int field_150845_b;
		int field_150846_c;
		public long field_150844_d;
		public Toggle(int p_i45422_1_, int p_i45422_2_, int p_i45422_3_, long p_i45422_4_) {
			field_150847_a = p_i45422_1_;
			field_150845_b = p_i45422_2_;
			field_150846_c = p_i45422_3_;
			field_150844_d = p_i45422_4_;
		}
	}
}