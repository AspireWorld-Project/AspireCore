package net.minecraft.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Direction;
import net.minecraft.util.IIcon;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.bukkit.Bukkit;
import org.bukkit.event.block.BlockRedstoneEvent;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class BlockRedstoneWire extends Block {
	private boolean field_150181_a = true;
	private final Set field_150179_b = new HashSet();
	@SideOnly(Side.CLIENT)
	private IIcon field_150182_M;
	@SideOnly(Side.CLIENT)
	private IIcon field_150183_N;
	@SideOnly(Side.CLIENT)
	private IIcon field_150184_O;
	@SideOnly(Side.CLIENT)
	private IIcon field_150180_P;
	private static final String __OBFID = "CL_00000295";

	public BlockRedstoneWire() {
		super(Material.circuits);
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.0625F, 1.0F);
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World p_149668_1_, int p_149668_2_, int p_149668_3_,
			int p_149668_4_) {
		return null;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public int getRenderType() {
		return 5;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int colorMultiplier(IBlockAccess p_149720_1_, int p_149720_2_, int p_149720_3_, int p_149720_4_) {
		return 8388608;
	}

	@Override
	public boolean canPlaceBlockAt(World p_149742_1_, int p_149742_2_, int p_149742_3_, int p_149742_4_) {
		return World.doesBlockHaveSolidTopSurface(p_149742_1_, p_149742_2_, p_149742_3_ - 1, p_149742_4_)
				|| p_149742_1_.getBlock(p_149742_2_, p_149742_3_ - 1, p_149742_4_) == Blocks.glowstone;
	}

	private void func_150177_e(World p_150177_1_, int p_150177_2_, int p_150177_3_, int p_150177_4_) {
		func_150175_a(p_150177_1_, p_150177_2_, p_150177_3_, p_150177_4_, p_150177_2_, p_150177_3_, p_150177_4_);
		ArrayList arraylist = new ArrayList(field_150179_b);
		field_150179_b.clear();

		for (int l = 0; l < arraylist.size(); ++l) {
			ChunkPosition chunkposition = (ChunkPosition) arraylist.get(l);
			p_150177_1_.notifyBlocksOfNeighborChange(chunkposition.chunkPosX, chunkposition.chunkPosY,
					chunkposition.chunkPosZ, this);
		}
	}

	private void func_150175_a(World world, int x, int y, int z, int p_150175_5_, int p_150175_6_, int p_150175_7_) {
		int k1 = world.getBlockMetadata(x, y, z);
		byte b0 = 0;
		int i3 = func_150178_a(world, p_150175_5_, p_150175_6_, p_150175_7_, b0);
		field_150181_a = false;
		int l1 = world.getStrongestIndirectPower(x, y, z);
		field_150181_a = true;
		if (l1 > 0 && l1 > i3 - 1) {
			i3 = l1;
		}

		int i2 = 0;

		for (int j2 = 0; j2 < 4; ++j2) {
			int k2 = x;
			int l2 = z;
			if (j2 == 0) {
				k2 = x - 1;
			}

			if (j2 == 1) {
				++k2;
			}

			if (j2 == 2) {
				l2 = z - 1;
			}

			if (j2 == 3) {
				++l2;
			}

			if (k2 != p_150175_5_ || l2 != p_150175_7_) {
				i2 = func_150178_a(world, k2, y, l2, i2);
			}

			if (world.getBlock(k2, y, l2).isNormalCube() && !world.getBlock(x, y + 1, z).isNormalCube()) {
				if ((k2 != p_150175_5_ || l2 != p_150175_7_) && y >= p_150175_6_) {
					i2 = func_150178_a(world, k2, y + 1, l2, i2);
				}
			} else if (!world.getBlock(k2, y, l2).isNormalCube() && (k2 != p_150175_5_ || l2 != p_150175_7_)
					&& y <= p_150175_6_) {
				i2 = func_150178_a(world, k2, y - 1, l2, i2);
			}
		}

		if (i2 > i3) {
			i3 = i2 - 1;
		} else if (i3 > 0) {
			--i3;
		} else {
			i3 = 0;
		}

		if (l1 > i3 - 1) {
			i3 = l1;
		}

		if (k1 != i3) {
			BlockRedstoneEvent event = new BlockRedstoneEvent(world.getWorld().getBlockAt(x, y, z), k1, i3);
			Bukkit.getPluginManager().callEvent(event);
			i3 = event.getNewCurrent();
		}
		// CraftBukkit end
		if (k1 != i3) {
			world.setBlockMetadataWithNotify(x, y, z, i3, 2);
			field_150179_b.add(new ChunkPosition(x, y, z));
			field_150179_b.add(new ChunkPosition(x - 1, y, z));
			field_150179_b.add(new ChunkPosition(x + 1, y, z));
			field_150179_b.add(new ChunkPosition(x, y - 1, z));
			field_150179_b.add(new ChunkPosition(x, y + 1, z));
			field_150179_b.add(new ChunkPosition(x, y, z - 1));
			field_150179_b.add(new ChunkPosition(x, y, z + 1));
		}

	}

	private void func_150172_m(World p_150172_1_, int p_150172_2_, int p_150172_3_, int p_150172_4_) {
		if (p_150172_1_.getBlock(p_150172_2_, p_150172_3_, p_150172_4_) == this) {
			p_150172_1_.notifyBlocksOfNeighborChange(p_150172_2_, p_150172_3_, p_150172_4_, this);
			p_150172_1_.notifyBlocksOfNeighborChange(p_150172_2_ - 1, p_150172_3_, p_150172_4_, this);
			p_150172_1_.notifyBlocksOfNeighborChange(p_150172_2_ + 1, p_150172_3_, p_150172_4_, this);
			p_150172_1_.notifyBlocksOfNeighborChange(p_150172_2_, p_150172_3_, p_150172_4_ - 1, this);
			p_150172_1_.notifyBlocksOfNeighborChange(p_150172_2_, p_150172_3_, p_150172_4_ + 1, this);
			p_150172_1_.notifyBlocksOfNeighborChange(p_150172_2_, p_150172_3_ - 1, p_150172_4_, this);
			p_150172_1_.notifyBlocksOfNeighborChange(p_150172_2_, p_150172_3_ + 1, p_150172_4_, this);
		}
	}

	@Override
	public void onBlockAdded(World p_149726_1_, int p_149726_2_, int p_149726_3_, int p_149726_4_) {
		super.onBlockAdded(p_149726_1_, p_149726_2_, p_149726_3_, p_149726_4_);

		if (!p_149726_1_.isRemote) {
			func_150177_e(p_149726_1_, p_149726_2_, p_149726_3_, p_149726_4_);
			p_149726_1_.notifyBlocksOfNeighborChange(p_149726_2_, p_149726_3_ + 1, p_149726_4_, this);
			p_149726_1_.notifyBlocksOfNeighborChange(p_149726_2_, p_149726_3_ - 1, p_149726_4_, this);
			func_150172_m(p_149726_1_, p_149726_2_ - 1, p_149726_3_, p_149726_4_);
			func_150172_m(p_149726_1_, p_149726_2_ + 1, p_149726_3_, p_149726_4_);
			func_150172_m(p_149726_1_, p_149726_2_, p_149726_3_, p_149726_4_ - 1);
			func_150172_m(p_149726_1_, p_149726_2_, p_149726_3_, p_149726_4_ + 1);

			if (p_149726_1_.getBlock(p_149726_2_ - 1, p_149726_3_, p_149726_4_).isNormalCube()) {
				func_150172_m(p_149726_1_, p_149726_2_ - 1, p_149726_3_ + 1, p_149726_4_);
			} else {
				func_150172_m(p_149726_1_, p_149726_2_ - 1, p_149726_3_ - 1, p_149726_4_);
			}

			if (p_149726_1_.getBlock(p_149726_2_ + 1, p_149726_3_, p_149726_4_).isNormalCube()) {
				func_150172_m(p_149726_1_, p_149726_2_ + 1, p_149726_3_ + 1, p_149726_4_);
			} else {
				func_150172_m(p_149726_1_, p_149726_2_ + 1, p_149726_3_ - 1, p_149726_4_);
			}

			if (p_149726_1_.getBlock(p_149726_2_, p_149726_3_, p_149726_4_ - 1).isNormalCube()) {
				func_150172_m(p_149726_1_, p_149726_2_, p_149726_3_ + 1, p_149726_4_ - 1);
			} else {
				func_150172_m(p_149726_1_, p_149726_2_, p_149726_3_ - 1, p_149726_4_ - 1);
			}

			if (p_149726_1_.getBlock(p_149726_2_, p_149726_3_, p_149726_4_ + 1).isNormalCube()) {
				func_150172_m(p_149726_1_, p_149726_2_, p_149726_3_ + 1, p_149726_4_ + 1);
			} else {
				func_150172_m(p_149726_1_, p_149726_2_, p_149726_3_ - 1, p_149726_4_ + 1);
			}
		}
	}

	@Override
	public void breakBlock(World p_149749_1_, int p_149749_2_, int p_149749_3_, int p_149749_4_, Block p_149749_5_,
			int p_149749_6_) {
		super.breakBlock(p_149749_1_, p_149749_2_, p_149749_3_, p_149749_4_, p_149749_5_, p_149749_6_);

		if (!p_149749_1_.isRemote) {
			p_149749_1_.notifyBlocksOfNeighborChange(p_149749_2_, p_149749_3_ + 1, p_149749_4_, this);
			p_149749_1_.notifyBlocksOfNeighborChange(p_149749_2_, p_149749_3_ - 1, p_149749_4_, this);
			p_149749_1_.notifyBlocksOfNeighborChange(p_149749_2_ + 1, p_149749_3_, p_149749_4_, this);
			p_149749_1_.notifyBlocksOfNeighborChange(p_149749_2_ - 1, p_149749_3_, p_149749_4_, this);
			p_149749_1_.notifyBlocksOfNeighborChange(p_149749_2_, p_149749_3_, p_149749_4_ + 1, this);
			p_149749_1_.notifyBlocksOfNeighborChange(p_149749_2_, p_149749_3_, p_149749_4_ - 1, this);
			func_150177_e(p_149749_1_, p_149749_2_, p_149749_3_, p_149749_4_);
			func_150172_m(p_149749_1_, p_149749_2_ - 1, p_149749_3_, p_149749_4_);
			func_150172_m(p_149749_1_, p_149749_2_ + 1, p_149749_3_, p_149749_4_);
			func_150172_m(p_149749_1_, p_149749_2_, p_149749_3_, p_149749_4_ - 1);
			func_150172_m(p_149749_1_, p_149749_2_, p_149749_3_, p_149749_4_ + 1);

			if (p_149749_1_.getBlock(p_149749_2_ - 1, p_149749_3_, p_149749_4_).isNormalCube()) {
				func_150172_m(p_149749_1_, p_149749_2_ - 1, p_149749_3_ + 1, p_149749_4_);
			} else {
				func_150172_m(p_149749_1_, p_149749_2_ - 1, p_149749_3_ - 1, p_149749_4_);
			}

			if (p_149749_1_.getBlock(p_149749_2_ + 1, p_149749_3_, p_149749_4_).isNormalCube()) {
				func_150172_m(p_149749_1_, p_149749_2_ + 1, p_149749_3_ + 1, p_149749_4_);
			} else {
				func_150172_m(p_149749_1_, p_149749_2_ + 1, p_149749_3_ - 1, p_149749_4_);
			}

			if (p_149749_1_.getBlock(p_149749_2_, p_149749_3_, p_149749_4_ - 1).isNormalCube()) {
				func_150172_m(p_149749_1_, p_149749_2_, p_149749_3_ + 1, p_149749_4_ - 1);
			} else {
				func_150172_m(p_149749_1_, p_149749_2_, p_149749_3_ - 1, p_149749_4_ - 1);
			}

			if (p_149749_1_.getBlock(p_149749_2_, p_149749_3_, p_149749_4_ + 1).isNormalCube()) {
				func_150172_m(p_149749_1_, p_149749_2_, p_149749_3_ + 1, p_149749_4_ + 1);
			} else {
				func_150172_m(p_149749_1_, p_149749_2_, p_149749_3_ - 1, p_149749_4_ + 1);
			}
		}
	}

	private int func_150178_a(World p_150178_1_, int p_150178_2_, int p_150178_3_, int p_150178_4_, int p_150178_5_) {
		if (p_150178_1_.getBlock(p_150178_2_, p_150178_3_, p_150178_4_) != this)
			return p_150178_5_;
		else {
			int i1 = p_150178_1_.getBlockMetadata(p_150178_2_, p_150178_3_, p_150178_4_);
			return i1 > p_150178_5_ ? i1 : p_150178_5_;
		}
	}

	@Override
	public void onNeighborBlockChange(World p_149695_1_, int p_149695_2_, int p_149695_3_, int p_149695_4_,
			Block p_149695_5_) {
		if (!p_149695_1_.isRemote) {
			boolean flag = canPlaceBlockAt(p_149695_1_, p_149695_2_, p_149695_3_, p_149695_4_);

			if (flag) {
				func_150177_e(p_149695_1_, p_149695_2_, p_149695_3_, p_149695_4_);
			} else {
				this.dropBlockAsItem(p_149695_1_, p_149695_2_, p_149695_3_, p_149695_4_, 0, 0);
				p_149695_1_.setBlockToAir(p_149695_2_, p_149695_3_, p_149695_4_);
			}

			super.onNeighborBlockChange(p_149695_1_, p_149695_2_, p_149695_3_, p_149695_4_, p_149695_5_);
		}
	}

	@Override
	public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_) {
		return Items.redstone;
	}

	@Override
	public int isProvidingStrongPower(IBlockAccess p_149748_1_, int p_149748_2_, int p_149748_3_, int p_149748_4_,
			int p_149748_5_) {
		return !field_150181_a ? 0
				: isProvidingWeakPower(p_149748_1_, p_149748_2_, p_149748_3_, p_149748_4_, p_149748_5_);
	}

	@Override
	public int isProvidingWeakPower(IBlockAccess p_149709_1_, int p_149709_2_, int p_149709_3_, int p_149709_4_,
			int p_149709_5_) {
		if (!field_150181_a)
			return 0;
		else {
			int i1 = p_149709_1_.getBlockMetadata(p_149709_2_, p_149709_3_, p_149709_4_);

			if (i1 == 0)
				return 0;
			else if (p_149709_5_ == 1)
				return i1;
			else {
				boolean flag = func_150176_g(p_149709_1_, p_149709_2_ - 1, p_149709_3_, p_149709_4_, 1)
						|| !p_149709_1_.getBlock(p_149709_2_ - 1, p_149709_3_, p_149709_4_).isNormalCube()
								&& func_150176_g(p_149709_1_, p_149709_2_ - 1, p_149709_3_ - 1, p_149709_4_, -1);
				boolean flag1 = func_150176_g(p_149709_1_, p_149709_2_ + 1, p_149709_3_, p_149709_4_, 3)
						|| !p_149709_1_.getBlock(p_149709_2_ + 1, p_149709_3_, p_149709_4_).isNormalCube()
								&& func_150176_g(p_149709_1_, p_149709_2_ + 1, p_149709_3_ - 1, p_149709_4_, -1);
				boolean flag2 = func_150176_g(p_149709_1_, p_149709_2_, p_149709_3_, p_149709_4_ - 1, 2)
						|| !p_149709_1_.getBlock(p_149709_2_, p_149709_3_, p_149709_4_ - 1).isNormalCube()
								&& func_150176_g(p_149709_1_, p_149709_2_, p_149709_3_ - 1, p_149709_4_ - 1, -1);
				boolean flag3 = func_150176_g(p_149709_1_, p_149709_2_, p_149709_3_, p_149709_4_ + 1, 0)
						|| !p_149709_1_.getBlock(p_149709_2_, p_149709_3_, p_149709_4_ + 1).isNormalCube()
								&& func_150176_g(p_149709_1_, p_149709_2_, p_149709_3_ - 1, p_149709_4_ + 1, -1);

				if (!p_149709_1_.getBlock(p_149709_2_, p_149709_3_ + 1, p_149709_4_).isNormalCube()) {
					if (p_149709_1_.getBlock(p_149709_2_ - 1, p_149709_3_, p_149709_4_).isNormalCube()
							&& func_150176_g(p_149709_1_, p_149709_2_ - 1, p_149709_3_ + 1, p_149709_4_, -1)) {
						flag = true;
					}

					if (p_149709_1_.getBlock(p_149709_2_ + 1, p_149709_3_, p_149709_4_).isNormalCube()
							&& func_150176_g(p_149709_1_, p_149709_2_ + 1, p_149709_3_ + 1, p_149709_4_, -1)) {
						flag1 = true;
					}

					if (p_149709_1_.getBlock(p_149709_2_, p_149709_3_, p_149709_4_ - 1).isNormalCube()
							&& func_150176_g(p_149709_1_, p_149709_2_, p_149709_3_ + 1, p_149709_4_ - 1, -1)) {
						flag2 = true;
					}

					if (p_149709_1_.getBlock(p_149709_2_, p_149709_3_, p_149709_4_ + 1).isNormalCube()
							&& func_150176_g(p_149709_1_, p_149709_2_, p_149709_3_ + 1, p_149709_4_ + 1, -1)) {
						flag3 = true;
					}
				}

				return !flag2 && !flag1 && !flag && !flag3 && p_149709_5_ >= 2 && p_149709_5_ <= 5 ? i1
						: p_149709_5_ == 2 && flag2 && !flag && !flag1 ? i1
								: p_149709_5_ == 3 && flag3 && !flag && !flag1 ? i1
										: p_149709_5_ == 4 && flag && !flag2 && !flag3 ? i1
												: p_149709_5_ == 5 && flag1 && !flag2 && !flag3 ? i1 : 0;
			}
		}
	}

	@Override
	public boolean canProvidePower() {
		return field_150181_a;
	}

	public static boolean isPowerProviderOrWire(IBlockAccess p_150174_0_, int p_150174_1_, int p_150174_2_,
			int p_150174_3_, int p_150174_4_) {
		Block block = p_150174_0_.getBlock(p_150174_1_, p_150174_2_, p_150174_3_);

		if (block == Blocks.redstone_wire)
			return true;
		else if (!Blocks.unpowered_repeater.func_149907_e(block))
			return block.canConnectRedstone(p_150174_0_, p_150174_1_, p_150174_2_, p_150174_3_, p_150174_4_);
		else {
			int i1 = p_150174_0_.getBlockMetadata(p_150174_1_, p_150174_2_, p_150174_3_);
			return p_150174_4_ == (i1 & 3) || p_150174_4_ == Direction.rotateOpposite[i1 & 3];
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(World p_149734_1_, int p_149734_2_, int p_149734_3_, int p_149734_4_,
			Random p_149734_5_) {
		int l = p_149734_1_.getBlockMetadata(p_149734_2_, p_149734_3_, p_149734_4_);

		if (l > 0) {
			double d0 = p_149734_2_ + 0.5D + (p_149734_5_.nextFloat() - 0.5D) * 0.2D;
			double d1 = p_149734_3_ + 0.0625F;
			double d2 = p_149734_4_ + 0.5D + (p_149734_5_.nextFloat() - 0.5D) * 0.2D;
			float f = l / 15.0F;
			float f1 = f * 0.6F + 0.4F;

			if (l == 0) {
				f1 = 0.0F;
			}

			float f2 = f * f * 0.7F - 0.5F;
			float f3 = f * f * 0.6F - 0.7F;

			if (f2 < 0.0F) {
				f2 = 0.0F;
			}

			if (f3 < 0.0F) {
				f3 = 0.0F;
			}

			p_149734_1_.spawnParticle("reddust", d0, d1, d2, f1, f2, f3);
		}
	}

	public static boolean func_150176_g(IBlockAccess p_150176_0_, int p_150176_1_, int p_150176_2_, int p_150176_3_,
			int p_150176_4_) {
		if (isPowerProviderOrWire(p_150176_0_, p_150176_1_, p_150176_2_, p_150176_3_, p_150176_4_))
			return true;
		else if (p_150176_0_.getBlock(p_150176_1_, p_150176_2_, p_150176_3_) == Blocks.powered_repeater) {
			int i1 = p_150176_0_.getBlockMetadata(p_150176_1_, p_150176_2_, p_150176_3_);
			return p_150176_4_ == (i1 & 3);
		} else
			return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Item getItem(World p_149694_1_, int p_149694_2_, int p_149694_3_, int p_149694_4_) {
		return Items.redstone;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister p_149651_1_) {
		field_150182_M = p_149651_1_.registerIcon(getTextureName() + "_" + "cross");
		field_150183_N = p_149651_1_.registerIcon(getTextureName() + "_" + "line");
		field_150184_O = p_149651_1_.registerIcon(getTextureName() + "_" + "cross_overlay");
		field_150180_P = p_149651_1_.registerIcon(getTextureName() + "_" + "line_overlay");
		blockIcon = field_150182_M;
	}

	@SideOnly(Side.CLIENT)
	public static IIcon getRedstoneWireIcon(String p_150173_0_) {
		return p_150173_0_.equals("cross") ? Blocks.redstone_wire.field_150182_M
				: p_150173_0_.equals("line") ? Blocks.redstone_wire.field_150183_N
						: p_150173_0_.equals("cross_overlay") ? Blocks.redstone_wire.field_150184_O
								: p_150173_0_.equals("line_overlay") ? Blocks.redstone_wire.field_150180_P : null;
	}
}