package net.minecraft.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;

public class BlockLilyPad extends BlockBush {
	protected BlockLilyPad() {
		float f = 0.5F;
		float f1 = 0.015625F;
		setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, f1, 0.5F + f);
		setCreativeTab(CreativeTabs.tabDecorations);
	}

	@Override
	public int getRenderType() {
		return 23;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void addCollisionBoxesToList(World p_149743_1_, int p_149743_2_, int p_149743_3_, int p_149743_4_,
			AxisAlignedBB p_149743_5_, List p_149743_6_, Entity p_149743_7_) {
		if (p_149743_7_ == null || !(p_149743_7_ instanceof EntityBoat)) {
			super.addCollisionBoxesToList(p_149743_1_, p_149743_2_, p_149743_3_, p_149743_4_, p_149743_5_, p_149743_6_,
					p_149743_7_);
		}
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World p_149668_1_, int p_149668_2_, int p_149668_3_,
			int p_149668_4_) {
		return AxisAlignedBB.getBoundingBox(p_149668_2_ + minX, p_149668_3_ + minY, p_149668_4_ + minZ,
				p_149668_2_ + maxX, p_149668_3_ + maxY, p_149668_4_ + maxZ);
	}

	@Override
	protected boolean canPlaceBlockOn(Block p_149854_1_) {
		return p_149854_1_ == Blocks.water;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getBlockColor() {
		return 2129968;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getRenderColor(int p_149741_1_) {
		return 2129968;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int colorMultiplier(IBlockAccess p_149720_1_, int p_149720_2_, int p_149720_3_, int p_149720_4_) {
		return 2129968;
	}

	@Override
	public boolean canBlockStay(World p_149718_1_, int p_149718_2_, int p_149718_3_, int p_149718_4_) {
		return p_149718_3_ >= 0 && p_149718_3_ < 256 && p_149718_1_.getBlock(p_149718_2_, p_149718_3_ - 1, p_149718_4_).getMaterial() == Material.water
                && p_149718_1_.getBlockMetadata(p_149718_2_, p_149718_3_ - 1, p_149718_4_) == 0;
	}
}