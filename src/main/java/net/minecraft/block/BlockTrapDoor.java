package net.minecraft.block;

import org.bukkit.Bukkit;
import org.bukkit.event.block.BlockRedstoneEvent;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockTrapDoor extends Block {
	/** Set this to allow trapdoors to remain free-floating */
	public static boolean disableValidation = false;
	private static final String __OBFID = "CL_00000327";

	protected BlockTrapDoor(Material p_i45434_1_) {
		super(p_i45434_1_);
		float f = 0.5F;
		float f1 = 1.0F;
		setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, f1, 0.5F + f);
		setCreativeTab(CreativeTabs.tabRedstone);
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
	public boolean getBlocksMovement(IBlockAccess p_149655_1_, int p_149655_2_, int p_149655_3_, int p_149655_4_) {
		return !func_150118_d(p_149655_1_.getBlockMetadata(p_149655_2_, p_149655_3_, p_149655_4_));
	}

	@Override
	public int getRenderType() {
		return 0;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World p_149633_1_, int p_149633_2_, int p_149633_3_,
			int p_149633_4_) {
		setBlockBoundsBasedOnState(p_149633_1_, p_149633_2_, p_149633_3_, p_149633_4_);
		return super.getSelectedBoundingBoxFromPool(p_149633_1_, p_149633_2_, p_149633_3_, p_149633_4_);
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World p_149668_1_, int p_149668_2_, int p_149668_3_,
			int p_149668_4_) {
		setBlockBoundsBasedOnState(p_149668_1_, p_149668_2_, p_149668_3_, p_149668_4_);
		return super.getCollisionBoundingBoxFromPool(p_149668_1_, p_149668_2_, p_149668_3_, p_149668_4_);
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess p_149719_1_, int p_149719_2_, int p_149719_3_,
			int p_149719_4_) {
		func_150117_b(p_149719_1_.getBlockMetadata(p_149719_2_, p_149719_3_, p_149719_4_));
	}

	@Override
	public void setBlockBoundsForItemRender() {
		float f = 0.1875F;
		setBlockBounds(0.0F, 0.5F - f / 2.0F, 0.0F, 1.0F, 0.5F + f / 2.0F, 1.0F);
	}

	public void func_150117_b(int p_150117_1_) {
		float f = 0.1875F;

		if ((p_150117_1_ & 8) != 0) {
			setBlockBounds(0.0F, 1.0F - f, 0.0F, 1.0F, 1.0F, 1.0F);
		} else {
			setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, f, 1.0F);
		}

		if (func_150118_d(p_150117_1_)) {
			if ((p_150117_1_ & 3) == 0) {
				setBlockBounds(0.0F, 0.0F, 1.0F - f, 1.0F, 1.0F, 1.0F);
			}

			if ((p_150117_1_ & 3) == 1) {
				setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, f);
			}

			if ((p_150117_1_ & 3) == 2) {
				setBlockBounds(1.0F - f, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
			}

			if ((p_150117_1_ & 3) == 3) {
				setBlockBounds(0.0F, 0.0F, 0.0F, f, 1.0F, 1.0F);
			}
		}
	}

	@Override
	public void onBlockClicked(World p_149699_1_, int p_149699_2_, int p_149699_3_, int p_149699_4_,
			EntityPlayer p_149699_5_) {
	}

	@Override
	public boolean onBlockActivated(World p_149727_1_, int p_149727_2_, int p_149727_3_, int p_149727_4_,
			EntityPlayer p_149727_5_, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_) {
		if (blockMaterial == Material.iron)
			return true;
		else {
			int i1 = p_149727_1_.getBlockMetadata(p_149727_2_, p_149727_3_, p_149727_4_);
			p_149727_1_.setBlockMetadataWithNotify(p_149727_2_, p_149727_3_, p_149727_4_, i1 ^ 4, 2);
			p_149727_1_.playAuxSFXAtEntity(p_149727_5_, 1003, p_149727_2_, p_149727_3_, p_149727_4_, 0);
			return true;
		}
	}

	public void func_150120_a(World p_150120_1_, int p_150120_2_, int p_150120_3_, int p_150120_4_,
			boolean p_150120_5_) {
		int l = p_150120_1_.getBlockMetadata(p_150120_2_, p_150120_3_, p_150120_4_);
		boolean flag1 = (l & 4) > 0;

		if (flag1 != p_150120_5_) {
			p_150120_1_.setBlockMetadataWithNotify(p_150120_2_, p_150120_3_, p_150120_4_, l ^ 4, 2);
			p_150120_1_.playAuxSFXAtEntity((EntityPlayer) null, 1003, p_150120_2_, p_150120_3_, p_150120_4_, 0);
		}
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
		if (!world.isRemote) {
			int l = world.getBlockMetadata(x, y, z);
			int i1 = x;
			int j1 = z;
			if ((l & 3) == 0) {
				j1 = z + 1;
			}

			if ((l & 3) == 1) {
				--j1;
			}

			if ((l & 3) == 2) {
				i1 = x + 1;
			}

			if ((l & 3) == 3) {
				--i1;
			}

			if (!func_150119_a(world.getBlock(i1, y, j1))
					&& !world.isSideSolid(i1, y, j1, ForgeDirection.getOrientation((l & 3) + 2))) {
				world.setBlockToAir(x, y, z);
				this.dropBlockAsItem(world, x, y, z, l, 0);
			}

			boolean flag = world.isBlockIndirectlyGettingPowered(x, y, z);
			if (flag || block.canProvidePower()) {
				org.bukkit.World bworld = world.getWorld();
				org.bukkit.block.Block bblock = bworld.getBlockAt(x, y, z);
				int power = bblock.getBlockPower();
				int oldPower = (world.getBlockMetadata(x, y, z) & 4) > 0 ? 15 : 0;
				if (oldPower == 0 ^ power == 0 || block.canProvidePower()) {
					BlockRedstoneEvent eventRedstone = new BlockRedstoneEvent(bblock, oldPower, power);
					Bukkit.getPluginManager().callEvent(eventRedstone);
					flag = eventRedstone.getNewCurrent() > 0;
				}
				func_150120_a(world, x, y, z, flag);
			}
		}
	}

	@Override
	public MovingObjectPosition collisionRayTrace(World p_149731_1_, int p_149731_2_, int p_149731_3_, int p_149731_4_,
			Vec3 p_149731_5_, Vec3 p_149731_6_) {
		setBlockBoundsBasedOnState(p_149731_1_, p_149731_2_, p_149731_3_, p_149731_4_);
		return super.collisionRayTrace(p_149731_1_, p_149731_2_, p_149731_3_, p_149731_4_, p_149731_5_, p_149731_6_);
	}

	@Override
	public int onBlockPlaced(World p_149660_1_, int p_149660_2_, int p_149660_3_, int p_149660_4_, int p_149660_5_,
			float p_149660_6_, float p_149660_7_, float p_149660_8_, int p_149660_9_) {
		int j1 = 0;

		if (p_149660_5_ == 2) {
			j1 = 0;
		}

		if (p_149660_5_ == 3) {
			j1 = 1;
		}

		if (p_149660_5_ == 4) {
			j1 = 2;
		}

		if (p_149660_5_ == 5) {
			j1 = 3;
		}

		if (p_149660_5_ != 1 && p_149660_5_ != 0 && p_149660_7_ > 0.5F) {
			j1 |= 8;
		}

		return j1;
	}

	@Override
	public boolean canPlaceBlockOnSide(World p_149707_1_, int p_149707_2_, int p_149707_3_, int p_149707_4_,
			int p_149707_5_) {
		if (disableValidation)
			return true;
		if (p_149707_5_ == 0)
			return false;
		else if (p_149707_5_ == 1)
			return false;
		else {
			if (p_149707_5_ == 2) {
				++p_149707_4_;
			}

			if (p_149707_5_ == 3) {
				--p_149707_4_;
			}

			if (p_149707_5_ == 4) {
				++p_149707_2_;
			}

			if (p_149707_5_ == 5) {
				--p_149707_2_;
			}

			return func_150119_a(p_149707_1_.getBlock(p_149707_2_, p_149707_3_, p_149707_4_))
					|| p_149707_1_.isSideSolid(p_149707_2_, p_149707_3_, p_149707_4_, ForgeDirection.UP);
		}
	}

	public static boolean func_150118_d(int p_150118_0_) {
		return (p_150118_0_ & 4) != 0;
	}

	private static boolean func_150119_a(Block p_150119_0_) {
		if (disableValidation)
			return true;
		return p_150119_0_.blockMaterial.isOpaque() && p_150119_0_.renderAsNormalBlock()
				|| p_150119_0_ == Blocks.glowstone || p_150119_0_ instanceof BlockSlab
				|| p_150119_0_ instanceof BlockStairs;
	}
}