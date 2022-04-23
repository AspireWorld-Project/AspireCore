package net.minecraft.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import java.util.List;

public class BlockQuartz extends Block {
	public static final String[] field_150191_a = new String[] { "default", "chiseled", "lines" };
	private static final String[] field_150189_b = new String[] { "side", "chiseled", "lines", null, null };
	@SideOnly(Side.CLIENT)
	private IIcon[] field_150192_M;
	@SideOnly(Side.CLIENT)
	private IIcon field_150193_N;
	@SideOnly(Side.CLIENT)
	private IIcon field_150194_O;
	@SideOnly(Side.CLIENT)
	private IIcon field_150190_P;
	@SideOnly(Side.CLIENT)
	private IIcon field_150188_Q;
	public BlockQuartz() {
		super(Material.rock);
		setCreativeTab(CreativeTabs.tabBlock);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int p_149691_1_, int p_149691_2_) {
		if (p_149691_2_ != 2 && p_149691_2_ != 3 && p_149691_2_ != 4) {
			if (p_149691_1_ != 1 && (p_149691_1_ != 0 || p_149691_2_ != 1)) {
				if (p_149691_1_ == 0)
					return field_150188_Q;
				else {
					if (p_149691_2_ < 0 || p_149691_2_ >= field_150192_M.length) {
						p_149691_2_ = 0;
					}

					return field_150192_M[p_149691_2_];
				}
			} else
				return p_149691_2_ == 1 ? field_150193_N : field_150190_P;
		} else
			return p_149691_2_ == 2 && (p_149691_1_ == 1 || p_149691_1_ == 0) ? field_150194_O
					: p_149691_2_ == 3 && (p_149691_1_ == 5 || p_149691_1_ == 4) ? field_150194_O
							: p_149691_2_ == 4 && (p_149691_1_ == 2 || p_149691_1_ == 3) ? field_150194_O
									: field_150192_M[p_149691_2_];
	}

	@Override
	public int onBlockPlaced(World p_149660_1_, int p_149660_2_, int p_149660_3_, int p_149660_4_, int p_149660_5_,
			float p_149660_6_, float p_149660_7_, float p_149660_8_, int p_149660_9_) {
		if (p_149660_9_ == 2) {
			switch (p_149660_5_) {
			case 0:
			case 1:
				p_149660_9_ = 2;
				break;
			case 2:
			case 3:
				p_149660_9_ = 4;
				break;
			case 4:
			case 5:
				p_149660_9_ = 3;
			}
		}

		return p_149660_9_;
	}

	@Override
	public int damageDropped(int p_149692_1_) {
		return p_149692_1_ != 3 && p_149692_1_ != 4 ? p_149692_1_ : 2;
	}

	@Override
	protected ItemStack createStackedBlock(int p_149644_1_) {
		return p_149644_1_ != 3 && p_149644_1_ != 4 ? super.createStackedBlock(p_149644_1_)
				: new ItemStack(Item.getItemFromBlock(this), 1, 2);
	}

	@Override
	public int getRenderType() {
		return 39;
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
		field_150192_M = new IIcon[field_150189_b.length];

		for (int i = 0; i < field_150192_M.length; ++i) {
			if (field_150189_b[i] == null) {
				field_150192_M[i] = field_150192_M[i - 1];
			} else {
				field_150192_M[i] = p_149651_1_.registerIcon(getTextureName() + "_" + field_150189_b[i]);
			}
		}

		field_150190_P = p_149651_1_.registerIcon(getTextureName() + "_" + "top");
		field_150193_N = p_149651_1_.registerIcon(getTextureName() + "_" + "chiseled_top");
		field_150194_O = p_149651_1_.registerIcon(getTextureName() + "_" + "lines_top");
		field_150188_Q = p_149651_1_.registerIcon(getTextureName() + "_" + "bottom");
	}

	@Override
	public MapColor getMapColor(int p_149728_1_) {
		return MapColor.quartzColor;
	}
}