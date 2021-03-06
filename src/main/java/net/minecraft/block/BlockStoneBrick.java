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

public class BlockStoneBrick extends Block {
	public static final String[] field_150142_a = new String[] { "default", "mossy", "cracked", "chiseled" };
	public static final String[] field_150141_b = new String[] { null, "mossy", "cracked", "carved" };
	@SideOnly(Side.CLIENT)
	private IIcon[] field_150143_M;
	public BlockStoneBrick() {
		super(Material.rock);
		setCreativeTab(CreativeTabs.tabBlock);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int p_149691_1_, int p_149691_2_) {
		if (p_149691_2_ < 0 || p_149691_2_ >= field_150141_b.length) {
			p_149691_2_ = 0;
		}

		return field_150143_M[p_149691_2_];
	}

	@Override
	public int damageDropped(int p_149692_1_) {
		return p_149692_1_;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item p_149666_1_, CreativeTabs p_149666_2_, List p_149666_3_) {
		for (int i = 0; i < 4; ++i) {
			p_149666_3_.add(new ItemStack(p_149666_1_, 1, i));
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister p_149651_1_) {
		field_150143_M = new IIcon[field_150141_b.length];

		for (int i = 0; i < field_150143_M.length; ++i) {
			String s = getTextureName();

			if (field_150141_b[i] != null) {
				s = s + "_" + field_150141_b[i];
			}

			field_150143_M[i] = p_149651_1_.registerIcon(s);
		}
	}
}