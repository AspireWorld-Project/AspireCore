package net.minecraft.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import java.util.List;
import java.util.Random;

public class BlockStoneSlab extends BlockSlab {
	public static final String[] field_150006_b = new String[] { "stone", "sand", "wood", "cobble", "brick",
			"smoothStoneBrick", "netherBrick", "quartz" };
	@SideOnly(Side.CLIENT)
	private IIcon field_150007_M;
	public BlockStoneSlab(boolean p_i45431_1_) {
		super(p_i45431_1_, Material.rock);
		setCreativeTab(CreativeTabs.tabBlock);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int p_149691_1_, int p_149691_2_) {
		int k = p_149691_2_ & 7;

		if (field_150004_a && (p_149691_2_ & 8) != 0) {
			p_149691_1_ = 1;
		}

		return k == 0 ? p_149691_1_ != 1 && p_149691_1_ != 0 ? field_150007_M : blockIcon
				: k == 1 ? Blocks.sandstone.getBlockTextureFromSide(p_149691_1_)
						: k == 2 ? Blocks.planks.getBlockTextureFromSide(p_149691_1_)
								: k == 3 ? Blocks.cobblestone.getBlockTextureFromSide(p_149691_1_)
										: k == 4 ? Blocks.brick_block.getBlockTextureFromSide(p_149691_1_)
												: k == 5 ? Blocks.stonebrick.getIcon(p_149691_1_, 0)
														: k == 6 ? Blocks.nether_brick.getBlockTextureFromSide(1)
																: k == 7 ? Blocks.quartz_block.getBlockTextureFromSide(
																		p_149691_1_) : blockIcon;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister p_149651_1_) {
		blockIcon = p_149651_1_.registerIcon("stone_slab_top");
		field_150007_M = p_149651_1_.registerIcon("stone_slab_side");
	}

	@Override
	public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_) {
		return Item.getItemFromBlock(Blocks.stone_slab);
	}

	@Override
	protected ItemStack createStackedBlock(int p_149644_1_) {
		return new ItemStack(Item.getItemFromBlock(Blocks.stone_slab), 2, p_149644_1_ & 7);
	}

	@Override
	public String func_150002_b(int p_150002_1_) {
		if (p_150002_1_ < 0 || p_150002_1_ >= field_150006_b.length) {
			p_150002_1_ = 0;
		}

		return super.getUnlocalizedName() + "." + field_150006_b[p_150002_1_];
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item p_149666_1_, CreativeTabs p_149666_2_, List p_149666_3_) {
		if (p_149666_1_ != Item.getItemFromBlock(Blocks.double_stone_slab)) {
			for (int i = 0; i <= 7; ++i) {
				if (i != 2) {
					p_149666_3_.add(new ItemStack(p_149666_1_, 1, i));
				}
			}
		}
	}
}