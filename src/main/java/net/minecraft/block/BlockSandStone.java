package net.minecraft.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import java.util.List;

public class BlockSandStone extends Block {
	public static final String[] field_150157_a = new String[] { "default", "chiseled", "smooth" };
	private static final String[] field_150156_b = new String[] { "normal", "carved", "smooth" };
	@SideOnly(Side.CLIENT)
	private IIcon[] field_150158_M;
	@SideOnly(Side.CLIENT)
	private IIcon field_150159_N;
	@SideOnly(Side.CLIENT)
	private IIcon field_150160_O;
	public BlockSandStone() {
		super(Material.rock);
		setCreativeTab(CreativeTabs.tabBlock);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int p_149691_1_, int p_149691_2_) {
		if (p_149691_1_ != 1 && (p_149691_1_ != 0 || p_149691_2_ != 1 && p_149691_2_ != 2)) {
			if (p_149691_1_ == 0)
				return field_150160_O;
			else {
				if (p_149691_2_ < 0 || p_149691_2_ >= field_150158_M.length) {
					p_149691_2_ = 0;
				}

				return field_150158_M[p_149691_2_];
			}
		} else
			return field_150159_N;
	}

	@Override
	public int damageDropped(int p_149692_1_) {
		return p_149692_1_;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item p_149666_1_, CreativeTabs p_149666_2_, List p_149666_3_) {
		p_149666_3_.add(new ItemStack(p_149666_1_, 1, 0));
		p_149666_3_.add(new ItemStack(p_149666_1_, 1, 1));
		p_149666_3_.add(new ItemStack(p_149666_1_, 1, 2));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister p_149651_1_) {
		field_150158_M = new IIcon[field_150156_b.length];

		for (int i = 0; i < field_150158_M.length; ++i) {
			field_150158_M[i] = p_149651_1_.registerIcon(getTextureName() + "_" + field_150156_b[i]);
		}

		field_150159_N = p_149651_1_.registerIcon(getTextureName() + "_top");
		field_150160_O = p_149651_1_.registerIcon(getTextureName() + "_bottom");
	}
}