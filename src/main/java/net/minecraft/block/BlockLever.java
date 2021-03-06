package net.minecraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import org.bukkit.Bukkit;
import org.bukkit.event.block.BlockRedstoneEvent;

import static net.minecraftforge.common.util.ForgeDirection.*;

public class BlockLever extends Block {
	protected BlockLever() {
		super(Material.circuits);
		setCreativeTab(CreativeTabs.tabRedstone);
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
		return 12;
	}

	@Override
	public boolean canPlaceBlockOnSide(World p_149707_1_, int p_149707_2_, int p_149707_3_, int p_149707_4_,
			int p_149707_5_) {
		ForgeDirection dir = ForgeDirection.getOrientation(p_149707_5_);
		return dir == DOWN && p_149707_1_.isSideSolid(p_149707_2_, p_149707_3_ + 1, p_149707_4_, DOWN)
				|| dir == UP && p_149707_1_.isSideSolid(p_149707_2_, p_149707_3_ - 1, p_149707_4_, UP)
				|| dir == NORTH && p_149707_1_.isSideSolid(p_149707_2_, p_149707_3_, p_149707_4_ + 1, NORTH)
				|| dir == SOUTH && p_149707_1_.isSideSolid(p_149707_2_, p_149707_3_, p_149707_4_ - 1, SOUTH)
				|| dir == WEST && p_149707_1_.isSideSolid(p_149707_2_ + 1, p_149707_3_, p_149707_4_, WEST)
				|| dir == EAST && p_149707_1_.isSideSolid(p_149707_2_ - 1, p_149707_3_, p_149707_4_, EAST);
	}

	@Override
	public boolean canPlaceBlockAt(World p_149742_1_, int p_149742_2_, int p_149742_3_, int p_149742_4_) {
		return p_149742_1_.isSideSolid(p_149742_2_ - 1, p_149742_3_, p_149742_4_, EAST)
				|| p_149742_1_.isSideSolid(p_149742_2_ + 1, p_149742_3_, p_149742_4_, WEST)
				|| p_149742_1_.isSideSolid(p_149742_2_, p_149742_3_, p_149742_4_ - 1, SOUTH)
				|| p_149742_1_.isSideSolid(p_149742_2_, p_149742_3_, p_149742_4_ + 1, NORTH)
				|| p_149742_1_.isSideSolid(p_149742_2_, p_149742_3_ - 1, p_149742_4_, UP)
				|| p_149742_1_.isSideSolid(p_149742_2_, p_149742_3_ + 1, p_149742_4_, DOWN);
	}

	@Override
	public int onBlockPlaced(World p_149660_1_, int p_149660_2_, int p_149660_3_, int p_149660_4_, int p_149660_5_,
			float p_149660_6_, float p_149660_7_, float p_149660_8_, int p_149660_9_) {
		int k1 = p_149660_9_ & 8;
		byte b0 = -1;

		if (p_149660_5_ == 0 && p_149660_1_.isSideSolid(p_149660_2_, p_149660_3_ + 1, p_149660_4_, DOWN)) {
			b0 = 0;
		}

		if (p_149660_5_ == 1 && p_149660_1_.isSideSolid(p_149660_2_, p_149660_3_ - 1, p_149660_4_, UP)) {
			b0 = 5;
		}

		if (p_149660_5_ == 2 && p_149660_1_.isSideSolid(p_149660_2_, p_149660_3_, p_149660_4_ + 1, NORTH)) {
			b0 = 4;
		}

		if (p_149660_5_ == 3 && p_149660_1_.isSideSolid(p_149660_2_, p_149660_3_, p_149660_4_ - 1, SOUTH)) {
			b0 = 3;
		}

		if (p_149660_5_ == 4 && p_149660_1_.isSideSolid(p_149660_2_ + 1, p_149660_3_, p_149660_4_, WEST)) {
			b0 = 2;
		}

		if (p_149660_5_ == 5 && p_149660_1_.isSideSolid(p_149660_2_ - 1, p_149660_3_, p_149660_4_, EAST)) {
			b0 = 1;
		}

		return b0 + k1;
	}

	@Override
	public void onBlockPlacedBy(World p_149689_1_, int p_149689_2_, int p_149689_3_, int p_149689_4_,
			EntityLivingBase p_149689_5_, ItemStack p_149689_6_) {
		int l = p_149689_1_.getBlockMetadata(p_149689_2_, p_149689_3_, p_149689_4_);
		int i1 = l & 7;
		int j1 = l & 8;

		if (i1 == invertMetadata(1)) {
			if ((MathHelper.floor_double(p_149689_5_.rotationYaw * 4.0F / 360.0F + 0.5D) & 1) == 0) {
				p_149689_1_.setBlockMetadataWithNotify(p_149689_2_, p_149689_3_, p_149689_4_, 5 | j1, 2);
			} else {
				p_149689_1_.setBlockMetadataWithNotify(p_149689_2_, p_149689_3_, p_149689_4_, 6 | j1, 2);
			}
		} else if (i1 == invertMetadata(0)) {
			if ((MathHelper.floor_double(p_149689_5_.rotationYaw * 4.0F / 360.0F + 0.5D) & 1) == 0) {
				p_149689_1_.setBlockMetadataWithNotify(p_149689_2_, p_149689_3_, p_149689_4_, 7 | j1, 2);
			} else {
				p_149689_1_.setBlockMetadataWithNotify(p_149689_2_, p_149689_3_, p_149689_4_, 0 | j1, 2);
			}
		}
	}

	public static int invertMetadata(int p_149819_0_) {
		switch (p_149819_0_) {
		case 0:
			return 0;
		case 1:
			return 5;
		case 2:
			return 4;
		case 3:
			return 3;
		case 4:
			return 2;
		case 5:
			return 1;
		default:
			return -1;
		}
	}

	@Override
	public void onNeighborBlockChange(World p_149695_1_, int p_149695_2_, int p_149695_3_, int p_149695_4_,
			Block p_149695_5_) {
		if (func_149820_e(p_149695_1_, p_149695_2_, p_149695_3_, p_149695_4_)) {
			int l = p_149695_1_.getBlockMetadata(p_149695_2_, p_149695_3_, p_149695_4_) & 7;
			boolean flag = !p_149695_1_.isSideSolid(p_149695_2_ - 1, p_149695_3_, p_149695_4_, EAST) && l == 1;

            if (!p_149695_1_.isSideSolid(p_149695_2_ + 1, p_149695_3_, p_149695_4_, WEST) && l == 2) {
				flag = true;
			}

			if (!p_149695_1_.isSideSolid(p_149695_2_, p_149695_3_, p_149695_4_ - 1, SOUTH) && l == 3) {
				flag = true;
			}

			if (!p_149695_1_.isSideSolid(p_149695_2_, p_149695_3_, p_149695_4_ + 1, NORTH) && l == 4) {
				flag = true;
			}

			if (!p_149695_1_.isSideSolid(p_149695_2_, p_149695_3_ - 1, p_149695_4_, UP) && l == 5) {
				flag = true;
			}

			if (!p_149695_1_.isSideSolid(p_149695_2_, p_149695_3_ - 1, p_149695_4_, UP) && l == 6) {
				flag = true;
			}

			if (!p_149695_1_.isSideSolid(p_149695_2_, p_149695_3_ + 1, p_149695_4_, DOWN) && l == 0) {
				flag = true;
			}

			if (!p_149695_1_.isSideSolid(p_149695_2_, p_149695_3_ + 1, p_149695_4_, DOWN) && l == 7) {
				flag = true;
			}

			if (flag) {
				this.dropBlockAsItem(p_149695_1_, p_149695_2_, p_149695_3_, p_149695_4_,
						p_149695_1_.getBlockMetadata(p_149695_2_, p_149695_3_, p_149695_4_), 0);
				p_149695_1_.setBlockToAir(p_149695_2_, p_149695_3_, p_149695_4_);
			}
		}
	}

	private boolean func_149820_e(World p_149820_1_, int p_149820_2_, int p_149820_3_, int p_149820_4_) {
		if (!canPlaceBlockAt(p_149820_1_, p_149820_2_, p_149820_3_, p_149820_4_)) {
			this.dropBlockAsItem(p_149820_1_, p_149820_2_, p_149820_3_, p_149820_4_,
					p_149820_1_.getBlockMetadata(p_149820_2_, p_149820_3_, p_149820_4_), 0);
			p_149820_1_.setBlockToAir(p_149820_2_, p_149820_3_, p_149820_4_);
			return false;
		} else
			return true;
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess p_149719_1_, int p_149719_2_, int p_149719_3_,
			int p_149719_4_) {
		int l = p_149719_1_.getBlockMetadata(p_149719_2_, p_149719_3_, p_149719_4_) & 7;
		float f = 0.1875F;

		if (l == 1) {
			setBlockBounds(0.0F, 0.2F, 0.5F - f, f * 2.0F, 0.8F, 0.5F + f);
		} else if (l == 2) {
			setBlockBounds(1.0F - f * 2.0F, 0.2F, 0.5F - f, 1.0F, 0.8F, 0.5F + f);
		} else if (l == 3) {
			setBlockBounds(0.5F - f, 0.2F, 0.0F, 0.5F + f, 0.8F, f * 2.0F);
		} else if (l == 4) {
			setBlockBounds(0.5F - f, 0.2F, 1.0F - f * 2.0F, 0.5F + f, 0.8F, 1.0F);
		} else if (l != 5 && l != 6) {
			if (l == 0 || l == 7) {
				f = 0.25F;
				setBlockBounds(0.5F - f, 0.4F, 0.5F - f, 0.5F + f, 1.0F, 0.5F + f);
			}
		} else {
			f = 0.25F;
			setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, 0.6F, 0.5F + f);
		}
	}

	@Override
	public boolean onBlockActivated(World p_149727_1_, int p_149727_2_, int p_149727_3_, int p_149727_4_,
			EntityPlayer p_149727_5_, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_) {
		if (p_149727_1_.isRemote)
			return true;
		else {
			int i1 = p_149727_1_.getBlockMetadata(p_149727_2_, p_149727_3_, p_149727_4_);
			int j1 = i1 & 7;
			int k1 = 8 - (i1 & 8);

			org.bukkit.block.Block block = p_149727_1_.getWorld().getBlockAt(p_149727_2_, p_149727_3_, p_149727_4_);
			int old = k1 != 8 ? 15 : 0;
			int current = k1 == 8 ? 15 : 0;
			BlockRedstoneEvent eventRedstone = new BlockRedstoneEvent(block, old, current);
			Bukkit.getPluginManager().callEvent(eventRedstone);
			if (eventRedstone.getNewCurrent() > 0 != (k1 == 8))
				return true;

			p_149727_1_.setBlockMetadataWithNotify(p_149727_2_, p_149727_3_, p_149727_4_, j1 + k1, 3);
			p_149727_1_.playSoundEffect(p_149727_2_ + 0.5D, p_149727_3_ + 0.5D, p_149727_4_ + 0.5D, "random.click",
					0.3F, k1 > 0 ? 0.6F : 0.5F);
			p_149727_1_.notifyBlocksOfNeighborChange(p_149727_2_, p_149727_3_, p_149727_4_, this);

			if (j1 == 1) {
				p_149727_1_.notifyBlocksOfNeighborChange(p_149727_2_ - 1, p_149727_3_, p_149727_4_, this);
			} else if (j1 == 2) {
				p_149727_1_.notifyBlocksOfNeighborChange(p_149727_2_ + 1, p_149727_3_, p_149727_4_, this);
			} else if (j1 == 3) {
				p_149727_1_.notifyBlocksOfNeighborChange(p_149727_2_, p_149727_3_, p_149727_4_ - 1, this);
			} else if (j1 == 4) {
				p_149727_1_.notifyBlocksOfNeighborChange(p_149727_2_, p_149727_3_, p_149727_4_ + 1, this);
			} else if (j1 != 5 && j1 != 6) {
				if (j1 == 0 || j1 == 7) {
					p_149727_1_.notifyBlocksOfNeighborChange(p_149727_2_, p_149727_3_ + 1, p_149727_4_, this);
				}
			} else {
				p_149727_1_.notifyBlocksOfNeighborChange(p_149727_2_, p_149727_3_ - 1, p_149727_4_, this);
			}

			return true;
		}
	}

	@Override
	public void breakBlock(World p_149749_1_, int p_149749_2_, int p_149749_3_, int p_149749_4_, Block p_149749_5_,
			int p_149749_6_) {
		if ((p_149749_6_ & 8) > 0) {
			p_149749_1_.notifyBlocksOfNeighborChange(p_149749_2_, p_149749_3_, p_149749_4_, this);
			int i1 = p_149749_6_ & 7;

			if (i1 == 1) {
				p_149749_1_.notifyBlocksOfNeighborChange(p_149749_2_ - 1, p_149749_3_, p_149749_4_, this);
			} else if (i1 == 2) {
				p_149749_1_.notifyBlocksOfNeighborChange(p_149749_2_ + 1, p_149749_3_, p_149749_4_, this);
			} else if (i1 == 3) {
				p_149749_1_.notifyBlocksOfNeighborChange(p_149749_2_, p_149749_3_, p_149749_4_ - 1, this);
			} else if (i1 == 4) {
				p_149749_1_.notifyBlocksOfNeighborChange(p_149749_2_, p_149749_3_, p_149749_4_ + 1, this);
			} else if (i1 != 5 && i1 != 6) {
				if (i1 == 0 || i1 == 7) {
					p_149749_1_.notifyBlocksOfNeighborChange(p_149749_2_, p_149749_3_ + 1, p_149749_4_, this);
				}
			} else {
				p_149749_1_.notifyBlocksOfNeighborChange(p_149749_2_, p_149749_3_ - 1, p_149749_4_, this);
			}
		}

		super.breakBlock(p_149749_1_, p_149749_2_, p_149749_3_, p_149749_4_, p_149749_5_, p_149749_6_);
	}

	@Override
	public int isProvidingWeakPower(IBlockAccess p_149709_1_, int p_149709_2_, int p_149709_3_, int p_149709_4_,
			int p_149709_5_) {
		return (p_149709_1_.getBlockMetadata(p_149709_2_, p_149709_3_, p_149709_4_) & 8) > 0 ? 15 : 0;
	}

	@Override
	public int isProvidingStrongPower(IBlockAccess p_149748_1_, int p_149748_2_, int p_149748_3_, int p_149748_4_,
			int p_149748_5_) {
		int i1 = p_149748_1_.getBlockMetadata(p_149748_2_, p_149748_3_, p_149748_4_);

		if ((i1 & 8) == 0)
			return 0;
		else {
			int j1 = i1 & 7;
			return j1 == 0 && p_149748_5_ == 0 ? 15
					: j1 == 7 && p_149748_5_ == 0 ? 15
							: j1 == 6 && p_149748_5_ == 1 ? 15
									: j1 == 5 && p_149748_5_ == 1 ? 15
											: j1 == 4 && p_149748_5_ == 2 ? 15
													: j1 == 3 && p_149748_5_ == 3 ? 15
															: j1 == 2 && p_149748_5_ == 4 ? 15
																	: j1 == 1 && p_149748_5_ == 5 ? 15 : 0;
		}
	}

	@Override
	public boolean canProvidePower() {
		return true;
	}
}