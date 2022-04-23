package net.minecraft.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.List;
import java.util.Random;

import static net.minecraftforge.common.util.ForgeDirection.*;

public class BlockPane extends Block {
	private final String field_150100_a;
	private final boolean field_150099_b;
	private final String field_150101_M;
	@SideOnly(Side.CLIENT)
	private IIcon field_150102_N;
	protected BlockPane(String p_i45432_1_, String p_i45432_2_, Material p_i45432_3_, boolean p_i45432_4_) {
		super(p_i45432_3_);
		field_150100_a = p_i45432_2_;
		field_150099_b = p_i45432_4_;
		field_150101_M = p_i45432_1_;
		setCreativeTab(CreativeTabs.tabDecorations);
	}

	@Override
	public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_) {
		return !field_150099_b ? null : super.getItemDropped(p_149650_1_, p_149650_2_, p_149650_3_);
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
		return blockMaterial == Material.glass ? 41 : 18;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockAccess p_149646_1_, int p_149646_2_, int p_149646_3_, int p_149646_4_,
			int p_149646_5_) {
		return p_149646_1_.getBlock(p_149646_2_, p_149646_3_, p_149646_4_) != this && super.shouldSideBeRendered(p_149646_1_, p_149646_2_, p_149646_3_, p_149646_4_, p_149646_5_);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void addCollisionBoxesToList(World p_149743_1_, int p_149743_2_, int p_149743_3_, int p_149743_4_,
			AxisAlignedBB p_149743_5_, List p_149743_6_, Entity p_149743_7_) {
		boolean flag = canPaneConnectTo(p_149743_1_, p_149743_2_, p_149743_3_, p_149743_4_ - 1, NORTH);
		boolean flag1 = canPaneConnectTo(p_149743_1_, p_149743_2_, p_149743_3_, p_149743_4_ + 1, SOUTH);
		boolean flag2 = canPaneConnectTo(p_149743_1_, p_149743_2_ - 1, p_149743_3_, p_149743_4_, WEST);
		boolean flag3 = canPaneConnectTo(p_149743_1_, p_149743_2_ + 1, p_149743_3_, p_149743_4_, EAST);

		if ((!flag2 || !flag3) && (flag2 || flag3 || flag || flag1)) {
			if (flag2 && !flag3) {
				setBlockBounds(0.0F, 0.0F, 0.4375F, 0.5F, 1.0F, 0.5625F);
				super.addCollisionBoxesToList(p_149743_1_, p_149743_2_, p_149743_3_, p_149743_4_, p_149743_5_,
						p_149743_6_, p_149743_7_);
			} else if (!flag2 && flag3) {
				setBlockBounds(0.5F, 0.0F, 0.4375F, 1.0F, 1.0F, 0.5625F);
				super.addCollisionBoxesToList(p_149743_1_, p_149743_2_, p_149743_3_, p_149743_4_, p_149743_5_,
						p_149743_6_, p_149743_7_);
			}
		} else {
			setBlockBounds(0.0F, 0.0F, 0.4375F, 1.0F, 1.0F, 0.5625F);
			super.addCollisionBoxesToList(p_149743_1_, p_149743_2_, p_149743_3_, p_149743_4_, p_149743_5_, p_149743_6_,
					p_149743_7_);
		}

		if ((!flag || !flag1) && (flag2 || flag3 || flag || flag1)) {
			if (flag && !flag1) {
				setBlockBounds(0.4375F, 0.0F, 0.0F, 0.5625F, 1.0F, 0.5F);
				super.addCollisionBoxesToList(p_149743_1_, p_149743_2_, p_149743_3_, p_149743_4_, p_149743_5_,
						p_149743_6_, p_149743_7_);
			} else if (!flag && flag1) {
				setBlockBounds(0.4375F, 0.0F, 0.5F, 0.5625F, 1.0F, 1.0F);
				super.addCollisionBoxesToList(p_149743_1_, p_149743_2_, p_149743_3_, p_149743_4_, p_149743_5_,
						p_149743_6_, p_149743_7_);
			}
		} else {
			setBlockBounds(0.4375F, 0.0F, 0.0F, 0.5625F, 1.0F, 1.0F);
			super.addCollisionBoxesToList(p_149743_1_, p_149743_2_, p_149743_3_, p_149743_4_, p_149743_5_, p_149743_6_,
					p_149743_7_);
		}
	}

	@Override
	public void setBlockBoundsForItemRender() {
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess p_149719_1_, int p_149719_2_, int p_149719_3_,
			int p_149719_4_) {
		float f = 0.4375F;
		float f1 = 0.5625F;
		float f2 = 0.4375F;
		float f3 = 0.5625F;
		boolean flag = canPaneConnectTo(p_149719_1_, p_149719_2_, p_149719_3_, p_149719_4_ - 1, NORTH);
		boolean flag1 = canPaneConnectTo(p_149719_1_, p_149719_2_, p_149719_3_, p_149719_4_ + 1, SOUTH);
		boolean flag2 = canPaneConnectTo(p_149719_1_, p_149719_2_ - 1, p_149719_3_, p_149719_4_, WEST);
		boolean flag3 = canPaneConnectTo(p_149719_1_, p_149719_2_ + 1, p_149719_3_, p_149719_4_, EAST);

		if ((!flag2 || !flag3) && (flag2 || flag3 || flag || flag1)) {
			if (flag2 && !flag3) {
				f = 0.0F;
			} else if (!flag2 && flag3) {
				f1 = 1.0F;
			}
		} else {
			f = 0.0F;
			f1 = 1.0F;
		}

		if ((!flag || !flag1) && (flag2 || flag3 || flag || flag1)) {
			if (flag && !flag1) {
				f2 = 0.0F;
			} else if (!flag && flag1) {
				f3 = 1.0F;
			}
		} else {
			f2 = 0.0F;
			f3 = 1.0F;
		}

		setBlockBounds(f, 0.0F, f2, f1, 1.0F, f3);
	}

	public final boolean canPaneConnectToBlock(Block p_150098_1_) {
		return p_150098_1_.func_149730_j() || p_150098_1_ == this || p_150098_1_ == Blocks.glass
				|| p_150098_1_ == Blocks.stained_glass || p_150098_1_ == Blocks.stained_glass_pane
				|| p_150098_1_ instanceof BlockPane;
	}

	@SideOnly(Side.CLIENT)
	public IIcon func_150097_e() {
		return field_150102_N;
	}

	@Override
	protected boolean canSilkHarvest() {
		return true;
	}

	@Override
	protected ItemStack createStackedBlock(int p_149644_1_) {
		return new ItemStack(Item.getItemFromBlock(this), 1, p_149644_1_);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister p_149651_1_) {
		blockIcon = p_149651_1_.registerIcon(field_150101_M);
		field_150102_N = p_149651_1_.registerIcon(field_150100_a);
	}

	public boolean canPaneConnectTo(IBlockAccess world, int x, int y, int z, ForgeDirection dir) {
		return canPaneConnectToBlock(world.getBlock(x, y, z)) || world.isSideSolid(x, y, z, dir.getOpposite(), false);
	}
}