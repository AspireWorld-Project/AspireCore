package net.minecraft.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import java.util.List;

public class BlockColored extends Block {
	@SideOnly(Side.CLIENT)
	private IIcon[] field_150033_a;
	public BlockColored(Material p_i45398_1_) {
		super(p_i45398_1_);
		setCreativeTab(CreativeTabs.tabBlock);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int p_149691_1_, int p_149691_2_) {
		return field_150033_a[p_149691_2_ % field_150033_a.length];
	}

	@Override
	public int damageDropped(int p_149692_1_) {
		return p_149692_1_;
	}

	public static int func_150032_b(int p_150032_0_) {
		return func_150031_c(p_150032_0_);
	}

	public static int func_150031_c(int p_150031_0_) {
		return ~p_150031_0_ & 15;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item p_149666_1_, CreativeTabs p_149666_2_, List p_149666_3_) {
		for (int i = 0; i < 16; ++i) {
			p_149666_3_.add(new ItemStack(p_149666_1_, 1, i));
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister p_149651_1_) {
		field_150033_a = new IIcon[16];

		for (int i = 0; i < field_150033_a.length; ++i) {
			field_150033_a[i] = p_149651_1_
					.registerIcon(getTextureName() + "_" + ItemDye.field_150921_b[func_150031_c(i)]);
		}
	}

	@Override
	public MapColor getMapColor(int p_149728_1_) {
		return MapColor.getMapColorForBlockColored(p_149728_1_);
	}
}