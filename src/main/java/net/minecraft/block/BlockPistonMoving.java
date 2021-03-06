package net.minecraft.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityPiston;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Facing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Random;

public class BlockPistonMoving extends BlockContainer {
	public BlockPistonMoving() {
		super(Material.piston);
		setHardness(-1.0F);
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return null;
	}

	@Override
	public void onBlockAdded(World p_149726_1_, int p_149726_2_, int p_149726_3_, int p_149726_4_) {
	}

	@Override
	public void breakBlock(World p_149749_1_, int p_149749_2_, int p_149749_3_, int p_149749_4_, Block p_149749_5_,
			int p_149749_6_) {
		TileEntity tileentity = p_149749_1_.getTileEntity(p_149749_2_, p_149749_3_, p_149749_4_);

		if (tileentity instanceof TileEntityPiston) {
			((TileEntityPiston) tileentity).clearPistonTileEntity();
		} else {
			super.breakBlock(p_149749_1_, p_149749_2_, p_149749_3_, p_149749_4_, p_149749_5_, p_149749_6_);
		}
	}

	@Override
	public boolean canPlaceBlockAt(World p_149742_1_, int p_149742_2_, int p_149742_3_, int p_149742_4_) {
		return false;
	}

	@Override
	public boolean canPlaceBlockOnSide(World p_149707_1_, int p_149707_2_, int p_149707_3_, int p_149707_4_,
			int p_149707_5_) {
		return false;
	}

	@Override
	public int getRenderType() {
		return -1;
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
	public boolean onBlockActivated(World p_149727_1_, int p_149727_2_, int p_149727_3_, int p_149727_4_,
			EntityPlayer p_149727_5_, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_) {
		if (!p_149727_1_.isRemote && p_149727_1_.getTileEntity(p_149727_2_, p_149727_3_, p_149727_4_) == null) {
			p_149727_1_.setBlockToAir(p_149727_2_, p_149727_3_, p_149727_4_);
			return true;
		} else
			return false;
	}

	@Override
	public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_) {
		return null;
	}

	@Override
	public void dropBlockAsItemWithChance(World p_149690_1_, int p_149690_2_, int p_149690_3_, int p_149690_4_,
			int p_149690_5_, float p_149690_6_, int p_149690_7_) {
		super.dropBlockAsItemWithChance(p_149690_1_, p_149690_2_, p_149690_3_, p_149690_4_, p_149690_5_, p_149690_6_,
				p_149690_7_);
	}

	@Override
	public void onNeighborBlockChange(World p_149695_1_, int p_149695_2_, int p_149695_3_, int p_149695_4_,
			Block p_149695_5_) {
		if (!p_149695_1_.isRemote) {
			p_149695_1_.getTileEntity(p_149695_2_, p_149695_3_, p_149695_4_);
		}
	}

	public static TileEntity getTileEntity(Block p_149962_0_, int p_149962_1_, int p_149962_2_, boolean p_149962_3_,
			boolean p_149962_4_) {
		return new TileEntityPiston(p_149962_0_, p_149962_1_, p_149962_2_, p_149962_3_, p_149962_4_);
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World p_149668_1_, int p_149668_2_, int p_149668_3_,
			int p_149668_4_) {
		TileEntityPiston tileentitypiston = func_149963_e(p_149668_1_, p_149668_2_, p_149668_3_, p_149668_4_);

		if (tileentitypiston == null)
			return null;
		else {
			float f = tileentitypiston.func_145860_a(0.0F);

			if (tileentitypiston.isExtending()) {
				f = 1.0F - f;
			}

			return func_149964_a(p_149668_1_, p_149668_2_, p_149668_3_, p_149668_4_,
					tileentitypiston.getStoredBlockID(), f, tileentitypiston.getPistonOrientation());
		}
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess p_149719_1_, int p_149719_2_, int p_149719_3_,
			int p_149719_4_) {
		TileEntityPiston tileentitypiston = func_149963_e(p_149719_1_, p_149719_2_, p_149719_3_, p_149719_4_);

		if (tileentitypiston != null) {
			Block block = tileentitypiston.getStoredBlockID();

			if (block == this || block.getMaterial() == Material.air)
				return;

			block.setBlockBoundsBasedOnState(p_149719_1_, p_149719_2_, p_149719_3_, p_149719_4_);
			float f = tileentitypiston.func_145860_a(0.0F);

			if (tileentitypiston.isExtending()) {
				f = 1.0F - f;
			}

			int l = tileentitypiston.getPistonOrientation();
			minX = block.getBlockBoundsMinX() - Facing.offsetsXForSide[l] * f;
			minY = block.getBlockBoundsMinY() - Facing.offsetsYForSide[l] * f;
			minZ = block.getBlockBoundsMinZ() - Facing.offsetsZForSide[l] * f;
			maxX = block.getBlockBoundsMaxX() - Facing.offsetsXForSide[l] * f;
			maxY = block.getBlockBoundsMaxY() - Facing.offsetsYForSide[l] * f;
			maxZ = block.getBlockBoundsMaxZ() - Facing.offsetsZForSide[l] * f;
		}
	}

	public AxisAlignedBB func_149964_a(World p_149964_1_, int p_149964_2_, int p_149964_3_, int p_149964_4_,
			Block p_149964_5_, float p_149964_6_, int p_149964_7_) {
		if (p_149964_5_ != this && p_149964_5_.getMaterial() != Material.air) {
			AxisAlignedBB axisalignedbb = p_149964_5_.getCollisionBoundingBoxFromPool(p_149964_1_, p_149964_2_,
					p_149964_3_, p_149964_4_);

			if (axisalignedbb == null)
				return null;
			else {
				if (Facing.offsetsXForSide[p_149964_7_] < 0) {
					axisalignedbb.minX -= Facing.offsetsXForSide[p_149964_7_] * p_149964_6_;
				} else {
					axisalignedbb.maxX -= Facing.offsetsXForSide[p_149964_7_] * p_149964_6_;
				}

				if (Facing.offsetsYForSide[p_149964_7_] < 0) {
					axisalignedbb.minY -= Facing.offsetsYForSide[p_149964_7_] * p_149964_6_;
				} else {
					axisalignedbb.maxY -= Facing.offsetsYForSide[p_149964_7_] * p_149964_6_;
				}

				if (Facing.offsetsZForSide[p_149964_7_] < 0) {
					axisalignedbb.minZ -= Facing.offsetsZForSide[p_149964_7_] * p_149964_6_;
				} else {
					axisalignedbb.maxZ -= Facing.offsetsZForSide[p_149964_7_] * p_149964_6_;
				}

				return axisalignedbb;
			}
		} else
			return null;
	}

	private TileEntityPiston func_149963_e(IBlockAccess p_149963_1_, int p_149963_2_, int p_149963_3_,
			int p_149963_4_) {
		TileEntity tileentity = p_149963_1_.getTileEntity(p_149963_2_, p_149963_3_, p_149963_4_);
		return tileentity instanceof TileEntityPiston ? (TileEntityPiston) tileentity : null;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Item getItem(World p_149694_1_, int p_149694_2_, int p_149694_3_, int p_149694_4_) {
		return Item.getItemById(0);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister p_149651_1_) {
		blockIcon = p_149651_1_.registerIcon("piston_top_normal");
	}

	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
		TileEntityPiston te = func_149963_e(world, x, y, z);
		if (te != null)
			return te.getStoredBlockID().getDrops(world, x, y, z, te.getBlockMetadata(), 0);
		return new ArrayList<>();
	}
}