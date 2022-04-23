package net.minecraft.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import java.util.List;

public class BlockNewLog extends BlockLog {
	public static final String[] field_150169_M = new String[] { "acacia", "big_oak" };
	private static final String __OBFID = "CL_00000277";

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item p_149666_1_, CreativeTabs p_149666_2_, List p_149666_3_) {
		p_149666_3_.add(new ItemStack(p_149666_1_, 1, 0));
		p_149666_3_.add(new ItemStack(p_149666_1_, 1, 1));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister p_149651_1_) {
		field_150167_a = new IIcon[field_150169_M.length];
		field_150166_b = new IIcon[field_150169_M.length];

		for (int i = 0; i < field_150167_a.length; ++i) {
			field_150167_a[i] = p_149651_1_.registerIcon(getTextureName() + "_" + field_150169_M[i]);
			field_150166_b[i] = p_149651_1_.registerIcon(getTextureName() + "_" + field_150169_M[i] + "_top");
		}
	}
}