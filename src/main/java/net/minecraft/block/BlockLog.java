package net.minecraft.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

public abstract class BlockLog extends BlockRotatedPillar {
	@SideOnly(Side.CLIENT)
	protected IIcon[] field_150167_a;
	@SideOnly(Side.CLIENT)
	protected IIcon[] field_150166_b;
	public BlockLog() {
		super(Material.wood);
		setCreativeTab(CreativeTabs.tabBlock);
		setHardness(2.0F);
		setStepSound(soundTypeWood);
	}

	public static int func_150165_c(int p_150165_0_) {
		return p_150165_0_ & 3;
	}

	@Override
	public int quantityDropped(Random p_149745_1_) {
		return 1;
	}

	@Override
	public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_) {
		return Item.getItemFromBlock(this);
	}

	@Override
	public void breakBlock(World p_149749_1_, int p_149749_2_, int p_149749_3_, int p_149749_4_, Block p_149749_5_,
			int p_149749_6_) {
		byte b0 = 4;
		int i1 = b0 + 1;

		if (p_149749_1_.checkChunksExist(p_149749_2_ - i1, p_149749_3_ - i1, p_149749_4_ - i1, p_149749_2_ + i1,
				p_149749_3_ + i1, p_149749_4_ + i1)) {
			for (int j1 = -b0; j1 <= b0; ++j1) {
				for (int k1 = -b0; k1 <= b0; ++k1) {
					for (int l1 = -b0; l1 <= b0; ++l1) {
						Block block = p_149749_1_.getBlock(p_149749_2_ + j1, p_149749_3_ + k1, p_149749_4_ + l1);
						if (block.isLeaves(p_149749_1_, p_149749_2_ + j1, p_149749_3_ + k1, p_149749_4_ + l1)) {
							block.beginLeavesDecay(p_149749_1_, p_149749_2_ + j1, p_149749_3_ + k1, p_149749_4_ + l1);
						}
					}
				}
			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	protected IIcon getSideIcon(int p_150163_1_) {
		return field_150167_a[p_150163_1_ % field_150167_a.length];
	}

	@Override
	@SideOnly(Side.CLIENT)
	protected IIcon getTopIcon(int p_150161_1_) {
		return field_150166_b[p_150161_1_ % field_150166_b.length];
	}

	@Override
	public boolean canSustainLeaves(IBlockAccess world, int x, int y, int z) {
		return true;
	}

	@Override
	public boolean isWood(IBlockAccess world, int x, int y, int z) {
		return true;
	}
}