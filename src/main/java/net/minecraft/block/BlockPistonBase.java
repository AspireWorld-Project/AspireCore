package net.minecraft.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityPiston;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Facing;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;

import java.util.List;

public class BlockPistonBase extends Block {
	private final boolean isSticky;
	@SideOnly(Side.CLIENT)
	private IIcon innerTopIcon;
	@SideOnly(Side.CLIENT)
	private IIcon bottomIcon;
	@SideOnly(Side.CLIENT)
	private IIcon topIcon;
	public BlockPistonBase(boolean p_i45443_1_) {
		super(Material.piston);
		isSticky = p_i45443_1_;
		setStepSound(soundTypePiston);
		setHardness(0.5F);
		setCreativeTab(CreativeTabs.tabRedstone);
	}

	@SideOnly(Side.CLIENT)
	public IIcon getPistonExtensionTexture() {
		return topIcon;
	}

	@SideOnly(Side.CLIENT)
	public void func_150070_b(float p_150070_1_, float p_150070_2_, float p_150070_3_, float p_150070_4_,
			float p_150070_5_, float p_150070_6_) {
		setBlockBounds(p_150070_1_, p_150070_2_, p_150070_3_, p_150070_4_, p_150070_5_, p_150070_6_);
	}

	@Override
	public int getRenderType() {
		return 16;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int p_149691_1_, int p_149691_2_) {
		int k = getPistonOrientation(p_149691_2_);
		return k > 5 ? topIcon
				: p_149691_1_ == k
						? !isExtended(p_149691_2_) && minX <= 0.0D && minY <= 0.0D && minZ <= 0.0D && maxX >= 1.0D
								&& maxY >= 1.0D && maxZ >= 1.0D ? topIcon : innerTopIcon
						: p_149691_1_ == Facing.oppositeSide[k] ? bottomIcon : blockIcon;
	}

	@SideOnly(Side.CLIENT)
	public static IIcon getPistonBaseIcon(String p_150074_0_) {
		return p_150074_0_ == "piston_side" ? Blocks.piston.blockIcon
				: p_150074_0_ == "piston_top_normal" ? Blocks.piston.topIcon
						: p_150074_0_ == "piston_top_sticky" ? Blocks.sticky_piston.topIcon
								: p_150074_0_ == "piston_inner" ? Blocks.piston.innerTopIcon : null;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister p_149651_1_) {
		blockIcon = p_149651_1_.registerIcon("piston_side");
		topIcon = p_149651_1_.registerIcon(isSticky ? "piston_top_sticky" : "piston_top_normal");
		innerTopIcon = p_149651_1_.registerIcon("piston_inner");
		bottomIcon = p_149651_1_.registerIcon("piston_bottom");
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean onBlockActivated(World p_149727_1_, int p_149727_2_, int p_149727_3_, int p_149727_4_,
			EntityPlayer p_149727_5_, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_) {
		return false;
	}

	@Override
	public void onBlockPlacedBy(World p_149689_1_, int p_149689_2_, int p_149689_3_, int p_149689_4_,
			EntityLivingBase p_149689_5_, ItemStack p_149689_6_) {
		int l = determineOrientation(p_149689_1_, p_149689_2_, p_149689_3_, p_149689_4_, p_149689_5_);
		p_149689_1_.setBlockMetadataWithNotify(p_149689_2_, p_149689_3_, p_149689_4_, l, 2);

		if (!p_149689_1_.isRemote) {
			updatePistonState(p_149689_1_, p_149689_2_, p_149689_3_, p_149689_4_);
		}
	}

	@Override
	public void onNeighborBlockChange(World p_149695_1_, int p_149695_2_, int p_149695_3_, int p_149695_4_,
			Block p_149695_5_) {
		if (!p_149695_1_.isRemote) {
			updatePistonState(p_149695_1_, p_149695_2_, p_149695_3_, p_149695_4_);
		}
	}

	@Override
	public void onBlockAdded(World p_149726_1_, int p_149726_2_, int p_149726_3_, int p_149726_4_) {
		if (!p_149726_1_.isRemote && p_149726_1_.getTileEntity(p_149726_2_, p_149726_3_, p_149726_4_) == null) {
			updatePistonState(p_149726_1_, p_149726_2_, p_149726_3_, p_149726_4_);
		}
	}

	private boolean isIndirectlyPowered(World p_150072_1_, int p_150072_2_, int p_150072_3_, int p_150072_4_,
			int p_150072_5_) {
		return p_150072_5_ != 0 && p_150072_1_.getIndirectPowerOutput(p_150072_2_, p_150072_3_ - 1, p_150072_4_, 0) || p_150072_5_ != 1 && p_150072_1_.getIndirectPowerOutput(p_150072_2_, p_150072_3_ + 1, p_150072_4_, 1) || p_150072_5_ != 2
				&& p_150072_1_.getIndirectPowerOutput(p_150072_2_, p_150072_3_, p_150072_4_ - 1, 2) || p_150072_5_ != 3 && p_150072_1_.getIndirectPowerOutput(p_150072_2_,
				p_150072_3_, p_150072_4_ + 1, 3) || p_150072_5_ != 5 && p_150072_1_.getIndirectPowerOutput(
				p_150072_2_ + 1, p_150072_3_, p_150072_4_, 5) || p_150072_5_ != 4 && p_150072_1_
				.getIndirectPowerOutput(p_150072_2_ - 1,
						p_150072_3_, p_150072_4_, 4) || p_150072_1_
				.getIndirectPowerOutput(
						p_150072_2_,
						p_150072_3_,
						p_150072_4_,
						0) || p_150072_1_
				.getIndirectPowerOutput(
						p_150072_2_,
						p_150072_3_
								+ 2,
						p_150072_4_,
						1) || p_150072_1_
				.getIndirectPowerOutput(
						p_150072_2_,
						p_150072_3_
								+ 1,
						p_150072_4_
								- 1,
						2) || p_150072_1_
				.getIndirectPowerOutput(
						p_150072_2_,
						p_150072_3_
								+ 1,
						p_150072_4_
								+ 1,
						3) || p_150072_1_
				.getIndirectPowerOutput(
						p_150072_2_
								- 1,
						p_150072_3_
								+ 1,
						p_150072_4_,
						4) || p_150072_1_
				.getIndirectPowerOutput(
						p_150072_2_
								+ 1,
						p_150072_3_
								+ 1,
						p_150072_4_,
						5);
	}

	@Override
	public boolean onBlockEventReceived(World p_149696_1_, int p_149696_2_, int p_149696_3_, int p_149696_4_,
			int p_149696_5_, int p_149696_6_) {
		if (!p_149696_1_.isRemote) {
			boolean flag = isIndirectlyPowered(p_149696_1_, p_149696_2_, p_149696_3_, p_149696_4_, p_149696_6_);

			if (flag && p_149696_5_ == 1) {
				p_149696_1_.setBlockMetadataWithNotify(p_149696_2_, p_149696_3_, p_149696_4_, p_149696_6_ | 8, 2);
				return false;
			}

			if (!flag && p_149696_5_ == 0)
				return false;
		}

		if (p_149696_5_ == 0) {
			if (!tryExtend(p_149696_1_, p_149696_2_, p_149696_3_, p_149696_4_, p_149696_6_))
				return false;

			p_149696_1_.setBlockMetadataWithNotify(p_149696_2_, p_149696_3_, p_149696_4_, p_149696_6_ | 8, 2);
			p_149696_1_.playSoundEffect(p_149696_2_ + 0.5D, p_149696_3_ + 0.5D, p_149696_4_ + 0.5D, "tile.piston.out",
					0.5F, p_149696_1_.rand.nextFloat() * 0.25F + 0.6F);
		} else if (p_149696_5_ == 1) {
			TileEntity tileentity1 = p_149696_1_.getTileEntity(p_149696_2_ + Facing.offsetsXForSide[p_149696_6_],
					p_149696_3_ + Facing.offsetsYForSide[p_149696_6_],
					p_149696_4_ + Facing.offsetsZForSide[p_149696_6_]);

			if (tileentity1 instanceof TileEntityPiston) {
				((TileEntityPiston) tileentity1).clearPistonTileEntity();
			}

			p_149696_1_.setBlock(p_149696_2_, p_149696_3_, p_149696_4_, Blocks.piston_extension, p_149696_6_, 3);
			p_149696_1_.setTileEntity(p_149696_2_, p_149696_3_, p_149696_4_,
					BlockPistonMoving.getTileEntity(this, p_149696_6_, p_149696_6_, false, true));

			if (isSticky) {
				int j1 = p_149696_2_ + Facing.offsetsXForSide[p_149696_6_] * 2;
				int k1 = p_149696_3_ + Facing.offsetsYForSide[p_149696_6_] * 2;
				int l1 = p_149696_4_ + Facing.offsetsZForSide[p_149696_6_] * 2;
				Block block = p_149696_1_.getBlock(j1, k1, l1);
				int i2 = p_149696_1_.getBlockMetadata(j1, k1, l1);
				boolean flag1 = false;

				if (block == Blocks.piston_extension) {
					TileEntity tileentity = p_149696_1_.getTileEntity(j1, k1, l1);

					if (tileentity instanceof TileEntityPiston) {
						TileEntityPiston tileentitypiston = (TileEntityPiston) tileentity;

						if (tileentitypiston.getPistonOrientation() == p_149696_6_ && tileentitypiston.isExtending()) {
							tileentitypiston.clearPistonTileEntity();
							block = tileentitypiston.getStoredBlockID();
							i2 = tileentitypiston.getBlockMetadata();
							flag1 = true;
						}
					}
				}

				if (!flag1 && block.getMaterial() != Material.air && canPushBlock(block, p_149696_1_, j1, k1, l1, false)
						&& (block.getMobilityFlag() == 0 || block == Blocks.piston || block == Blocks.sticky_piston)
						&& p_149696_1_.setBlockToAir(j1, k1, l1)) {
					p_149696_2_ += Facing.offsetsXForSide[p_149696_6_];
					p_149696_3_ += Facing.offsetsYForSide[p_149696_6_];
					p_149696_4_ += Facing.offsetsZForSide[p_149696_6_];
					p_149696_1_.setBlock(p_149696_2_, p_149696_3_, p_149696_4_, Blocks.piston_extension, i2, 3);
					p_149696_1_.setTileEntity(p_149696_2_, p_149696_3_, p_149696_4_,
							BlockPistonMoving.getTileEntity(block, i2, p_149696_6_, false, false));
					// p_149696_1_.setBlockToAir(j1, k1, l1); //moved up into if expression
				} else if (!flag1) {
					p_149696_1_.setBlockToAir(p_149696_2_ + Facing.offsetsXForSide[p_149696_6_],
							p_149696_3_ + Facing.offsetsYForSide[p_149696_6_],
							p_149696_4_ + Facing.offsetsZForSide[p_149696_6_]);
				}
			} else {
				p_149696_1_.setBlockToAir(p_149696_2_ + Facing.offsetsXForSide[p_149696_6_],
						p_149696_3_ + Facing.offsetsYForSide[p_149696_6_],
						p_149696_4_ + Facing.offsetsZForSide[p_149696_6_]);
			}

			p_149696_1_.playSoundEffect(p_149696_2_ + 0.5D, p_149696_3_ + 0.5D, p_149696_4_ + 0.5D, "tile.piston.in",
					0.5F, p_149696_1_.rand.nextFloat() * 0.15F + 0.6F);
		}

		return true;
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess p_149719_1_, int p_149719_2_, int p_149719_3_,
			int p_149719_4_) {
		int l = p_149719_1_.getBlockMetadata(p_149719_2_, p_149719_3_, p_149719_4_);

		if (isExtended(l)) {
			switch (getPistonOrientation(l)) {
			case 0:
				setBlockBounds(0.0F, 0.25F, 0.0F, 1.0F, 1.0F, 1.0F);
				break;
			case 1:
				setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.75F, 1.0F);
				break;
			case 2:
				setBlockBounds(0.0F, 0.0F, 0.25F, 1.0F, 1.0F, 1.0F);
				break;
			case 3:
				setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.75F);
				break;
			case 4:
				setBlockBounds(0.25F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
				break;
			case 5:
				setBlockBounds(0.0F, 0.0F, 0.0F, 0.75F, 1.0F, 1.0F);
			}
		} else {
			setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		}
	}

	@Override
	public void setBlockBoundsForItemRender() {
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void addCollisionBoxesToList(World p_149743_1_, int p_149743_2_, int p_149743_3_, int p_149743_4_,
			AxisAlignedBB p_149743_5_, List p_149743_6_, Entity p_149743_7_) {
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		super.addCollisionBoxesToList(p_149743_1_, p_149743_2_, p_149743_3_, p_149743_4_, p_149743_5_, p_149743_6_,
				p_149743_7_);
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World p_149668_1_, int p_149668_2_, int p_149668_3_,
			int p_149668_4_) {
		setBlockBoundsBasedOnState(p_149668_1_, p_149668_2_, p_149668_3_, p_149668_4_);
		return super.getCollisionBoundingBoxFromPool(p_149668_1_, p_149668_2_, p_149668_3_, p_149668_4_);
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	public static int getPistonOrientation(int p_150076_0_) {
		return p_150076_0_ & 7;
	}

	public static boolean isExtended(int p_150075_0_) {
		return (p_150075_0_ & 8) != 0;
	}

	public static int determineOrientation(World p_150071_0_, int p_150071_1_, int p_150071_2_, int p_150071_3_,
			EntityLivingBase p_150071_4_) {
		if (MathHelper.abs((float) p_150071_4_.posX - p_150071_1_) < 2.0F
				&& MathHelper.abs((float) p_150071_4_.posZ - p_150071_3_) < 2.0F) {
			double d0 = p_150071_4_.posY + 1.82D - p_150071_4_.yOffset;

			if (d0 - p_150071_2_ > 2.0D)
				return 1;

			if (p_150071_2_ - d0 > 0.0D)
				return 0;
		}

		int l = MathHelper.floor_double(p_150071_4_.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
		return l == 0 ? 2 : l == 1 ? 5 : l == 2 ? 3 : l == 3 ? 4 : 0;
	}

	private static boolean canPushBlock(Block p_150080_0_, World p_150080_1_, int p_150080_2_, int p_150080_3_,
			int p_150080_4_, boolean p_150080_5_) {
		if (p_150080_0_ == Blocks.obsidian)
			return false;
		else {
			if (p_150080_0_ != Blocks.piston && p_150080_0_ != Blocks.sticky_piston) {
				if (p_150080_0_.getBlockHardness(p_150080_1_, p_150080_2_, p_150080_3_, p_150080_4_) == -1.0F)
					return false;

				if (p_150080_0_.getMobilityFlag() == 2)
					return false;

				if (p_150080_0_.getMobilityFlag() == 1) {
					return p_150080_5_;
				}
			} else if (isExtended(p_150080_1_.getBlockMetadata(p_150080_2_, p_150080_3_, p_150080_4_)))
				return false;

			return !p_150080_1_.getBlock(p_150080_2_, p_150080_3_, p_150080_4_)
					.hasTileEntity(p_150080_1_.getBlockMetadata(p_150080_2_, p_150080_3_, p_150080_4_));

		}
	}

	@SuppressWarnings("unused")
	private static boolean canExtend(World p_150077_0_, int p_150077_1_, int p_150077_2_, int p_150077_3_,
			int p_150077_4_) {
		int i1 = p_150077_1_ + Facing.offsetsXForSide[p_150077_4_];
		int j1 = p_150077_2_ + Facing.offsetsYForSide[p_150077_4_];
		int k1 = p_150077_3_ + Facing.offsetsZForSide[p_150077_4_];
		int l1 = 0;

		while (true) {
			if (l1 < 13) {
				if (j1 <= 0 || j1 >= p_150077_0_.getHeight())
					return false;

				Block block = p_150077_0_.getBlock(i1, j1, k1);

				if (!block.isAir(p_150077_0_, i1, j1, k1)) {
					if (!canPushBlock(block, p_150077_0_, i1, j1, k1, true))
						return false;

					if (block.getMobilityFlag() != 1) {
						if (l1 == 12)
							return false;

						i1 += Facing.offsetsXForSide[p_150077_4_];
						j1 += Facing.offsetsYForSide[p_150077_4_];
						k1 += Facing.offsetsZForSide[p_150077_4_];
						++l1;
						continue;
					}
				}
			}

			return true;
		}
	}

	private boolean tryExtend(World p_150079_1_, int p_150079_2_, int p_150079_3_, int p_150079_4_, int p_150079_5_) {
		int i1 = p_150079_2_ + Facing.offsetsXForSide[p_150079_5_];
		int j1 = p_150079_3_ + Facing.offsetsYForSide[p_150079_5_];
		int k1 = p_150079_4_ + Facing.offsetsZForSide[p_150079_5_];
		int l1 = 0;

		while (true) {
			if (l1 < 13) {
				if (j1 <= 0 || j1 >= p_150079_1_.getHeight())
					return false;

				Block block = p_150079_1_.getBlock(i1, j1, k1);

				if (!block.isAir(p_150079_1_, i1, j1, k1)) {
					if (!canPushBlock(block, p_150079_1_, i1, j1, k1, true))
						return false;

					if (block.getMobilityFlag() != 1) {
						if (l1 == 12)
							return false;

						i1 += Facing.offsetsXForSide[p_150079_5_];
						j1 += Facing.offsetsYForSide[p_150079_5_];
						k1 += Facing.offsetsZForSide[p_150079_5_];
						++l1;
						continue;
					}

					// With our change to how snowballs are dropped this needs to disallow to mimic
					// vanilla behavior.
					float chance = block instanceof BlockSnow ? -1.0f : 1.0f;
					block.dropBlockAsItemWithChance(p_150079_1_, i1, j1, k1, p_150079_1_.getBlockMetadata(i1, j1, k1),
							chance, 0);
					p_150079_1_.setBlockToAir(i1, j1, k1);
				}
			}

			l1 = i1;
			int k3 = j1;
			int i2 = k1;
			int j2 = 0;
			Block[] ablock;
			int k2;
			int l2;
			int i3;

			for (ablock = new Block[13]; i1 != p_150079_2_ || j1 != p_150079_3_ || k1 != p_150079_4_; k1 = i3) {
				k2 = i1 - Facing.offsetsXForSide[p_150079_5_];
				l2 = j1 - Facing.offsetsYForSide[p_150079_5_];
				i3 = k1 - Facing.offsetsZForSide[p_150079_5_];
				Block block1 = p_150079_1_.getBlock(k2, l2, i3);
				int j3 = p_150079_1_.getBlockMetadata(k2, l2, i3);

				if (block1 == this && k2 == p_150079_2_ && l2 == p_150079_3_ && i3 == p_150079_4_) {
					p_150079_1_.setBlock(i1, j1, k1, Blocks.piston_extension, p_150079_5_ | (isSticky ? 8 : 0), 4);
					p_150079_1_.setTileEntity(i1, j1, k1, BlockPistonMoving.getTileEntity(Blocks.piston_head,
							p_150079_5_ | (isSticky ? 8 : 0), p_150079_5_, true, false));
				} else {
					p_150079_1_.setBlock(i1, j1, k1, Blocks.piston_extension, j3, 4);
					p_150079_1_.setTileEntity(i1, j1, k1,
							BlockPistonMoving.getTileEntity(block1, j3, p_150079_5_, true, false));
				}

				ablock[j2++] = block1;
				i1 = k2;
				j1 = l2;
			}

			i1 = l1;
			j1 = k3;
			k1 = i2;

			for (j2 = 0; i1 != p_150079_2_ || j1 != p_150079_3_ || k1 != p_150079_4_; k1 = i3) {
				k2 = i1 - Facing.offsetsXForSide[p_150079_5_];
				l2 = j1 - Facing.offsetsYForSide[p_150079_5_];
				i3 = k1 - Facing.offsetsZForSide[p_150079_5_];
				p_150079_1_.notifyBlocksOfNeighborChange(k2, l2, i3, ablock[j2++]);
				i1 = k2;
				j1 = l2;
			}

			return true;
		}
	}

	private void updatePistonState(World world, int x, int y, int z) {
		int l = world.getBlockMetadata(x, y, z);
		int i1 = getPistonOrientation(l);
		if (i1 != 7) {
			boolean flag = isIndirectlyPowered(world, x, y, z, i1);
			if (flag && !isExtended(l)) {
				int length = canExtend_IntCB(world, x, y, z, i1);
				if (length >= 0) {
					org.bukkit.block.Block block = world.getWorld().getBlockAt(x, y, z);
					BlockPistonExtendEvent event = new BlockPistonExtendEvent(block, length,
							CraftBlock.notchToBlockFace(i1));
					world.getServer().getPluginManager().callEvent(event);
					if (event.isCancelled())
						return;
					world.addBlockEvent(x, y, z, this, 0, i1);
				}
			} else if (!flag && isExtended(l)) {
				org.bukkit.block.Block block = world.getWorld().getBlockAt(x, y, z);
				BlockPistonRetractEvent event = new BlockPistonRetractEvent(block, CraftBlock.notchToBlockFace(i1));
				world.getServer().getPluginManager().callEvent(event);
				if (event.isCancelled())
					return;
				world.setBlockMetadataWithNotify(x, y, z, i1, 2);
				world.addBlockEvent(x, y, z, this, 1, i1);
			}
		}
	}

	private int canExtend_IntCB(World world, int x, int y, int z, int p_150077_4_) {
		int i1 = x + Facing.offsetsXForSide[p_150077_4_];
		int j1 = y + Facing.offsetsYForSide[p_150077_4_];
		int k1 = z + Facing.offsetsZForSide[p_150077_4_];
		int l1 = 0;
		while (true) {
			if (l1 < 13) {
				if (j1 <= 0 || j1 >= world.getHeight())
					return -1;
				Block block = world.getBlock(i1, j1, k1);
				if (!block.isAir(world, i1, j1, k1)) {
					if (!canPushBlock(block, world, i1, j1, k1, true))
						return -1;
					if (block.getMobilityFlag() != 1) {
						if (l1 == 12)
							return -1;
						i1 += Facing.offsetsXForSide[p_150077_4_];
						j1 += Facing.offsetsYForSide[p_150077_4_];
						k1 += Facing.offsetsZForSide[p_150077_4_];
						++l1;
						continue;
					}
				}
			}
			return l1;
		}
	}
}