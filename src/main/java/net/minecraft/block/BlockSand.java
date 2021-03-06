package net.minecraft.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.MapColor;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import java.util.List;

public class BlockSand extends BlockFalling {
	public static final String[] field_149838_a = new String[] { "default", "red" };
	@SideOnly(Side.CLIENT)
	private static IIcon field_149837_b;
	@SideOnly(Side.CLIENT)
	private static IIcon field_149839_N;
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int p_149691_1_, int p_149691_2_) {
		return p_149691_2_ == 1 ? field_149839_N : field_149837_b;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister p_149651_1_) {
		field_149837_b = p_149651_1_.registerIcon("sand");
		field_149839_N = p_149651_1_.registerIcon("red_sand");
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
	}

	@Override
	public MapColor getMapColor(int p_149728_1_) {
		return p_149728_1_ == 1 ? MapColor.dirtColor : MapColor.sandColor;
	}
}