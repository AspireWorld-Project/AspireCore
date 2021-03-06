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

public class BlockFlower extends BlockBush {
	private static final String[][] field_149860_M = new String[][] { { "flower_dandelion" },
			{ "flower_rose", "flower_blue_orchid", "flower_allium", "flower_houstonia", "flower_tulip_red",
					"flower_tulip_orange", "flower_tulip_white", "flower_tulip_pink", "flower_oxeye_daisy" } };
	public static final String[] field_149859_a = new String[] { "poppy", "blueOrchid", "allium", "houstonia",
			"tulipRed", "tulipOrange", "tulipWhite", "tulipPink", "oxeyeDaisy" };
	public static final String[] field_149858_b = new String[] { "dandelion" };
	@SideOnly(Side.CLIENT)
	private IIcon[] field_149861_N;
	private final int field_149862_O;
	protected BlockFlower(int p_i2173_1_) {
		super(Material.plants);
		field_149862_O = p_i2173_1_;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int p_149691_1_, int p_149691_2_) {
		if (p_149691_2_ >= field_149861_N.length) {
			p_149691_2_ = 0;
		}

		return field_149861_N[p_149691_2_];
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister p_149651_1_) {
		field_149861_N = new IIcon[field_149860_M[field_149862_O].length];

		for (int i = 0; i < field_149861_N.length; ++i) {
			field_149861_N[i] = p_149651_1_.registerIcon(field_149860_M[field_149862_O][i]);
		}
	}

	@Override
	public int damageDropped(int p_149692_1_) {
		return p_149692_1_;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item p_149666_1_, CreativeTabs p_149666_2_, List p_149666_3_) {
		for (int i = 0; i < field_149861_N.length; ++i) {
			p_149666_3_.add(new ItemStack(p_149666_1_, 1, i));
		}
	}

	public static BlockFlower func_149857_e(String p_149857_0_) {
		String[] astring = field_149858_b;
		int i = astring.length;
		int j;
		String s1;

		for (j = 0; j < i; ++j) {
			s1 = astring[j];

			if (s1.equals(p_149857_0_))
				return Blocks.yellow_flower;
		}

		astring = field_149859_a;
		i = astring.length;

		for (j = 0; j < i; ++j) {
			s1 = astring[j];

			if (s1.equals(p_149857_0_))
				return Blocks.red_flower;
		}

		return null;
	}

	public static int func_149856_f(String p_149856_0_) {
		int i;

		for (i = 0; i < field_149858_b.length; ++i) {
			if (field_149858_b[i].equals(p_149856_0_))
				return i;
		}

		for (i = 0; i < field_149859_a.length; ++i) {
			if (field_149859_a[i].equals(p_149856_0_))
				return i;
		}

		return 0;
	}
}