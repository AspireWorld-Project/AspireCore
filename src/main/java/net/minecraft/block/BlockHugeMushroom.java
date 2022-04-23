package net.minecraft.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import java.util.Random;

public class BlockHugeMushroom extends Block {
	private static final String[] field_149793_a = new String[] { "skin_brown", "skin_red" };
	private final int field_149792_b;
	@SideOnly(Side.CLIENT)
	private IIcon[] field_149794_M;
	@SideOnly(Side.CLIENT)
	private IIcon field_149795_N;
	@SideOnly(Side.CLIENT)
	private IIcon field_149796_O;
	public BlockHugeMushroom(Material p_i45412_1_, int p_i45412_2_) {
		super(p_i45412_1_);
		field_149792_b = p_i45412_2_;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int p_149691_1_, int p_149691_2_) {
		return p_149691_2_ == 10 && p_149691_1_ > 1 ? field_149795_N
				: p_149691_2_ >= 1 && p_149691_2_ <= 9 && p_149691_1_ == 1 ? field_149794_M[field_149792_b]
						: p_149691_2_ >= 1 && p_149691_2_ <= 3 && p_149691_1_ == 2 ? field_149794_M[field_149792_b]
								: p_149691_2_ >= 7 && p_149691_2_ <= 9 && p_149691_1_ == 3
										? field_149794_M[field_149792_b]
										: (p_149691_2_ == 1 || p_149691_2_ == 4 || p_149691_2_ == 7) && p_149691_1_ == 4
												? field_149794_M[field_149792_b]
												: (p_149691_2_ == 3 || p_149691_2_ == 6 || p_149691_2_ == 9)
														&& p_149691_1_ == 5
																? field_149794_M[field_149792_b]
																: p_149691_2_ == 14 ? field_149794_M[field_149792_b]
																		: p_149691_2_ == 15 ? field_149795_N
																				: field_149796_O;
	}

	@Override
	public int quantityDropped(Random p_149745_1_) {
		int i = p_149745_1_.nextInt(10) - 7;

		if (i < 0) {
			i = 0;
		}

		return i;
	}

	@Override
	public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_) {
		return Item.getItemById(Block.getIdFromBlock(Blocks.brown_mushroom) + field_149792_b);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Item getItem(World p_149694_1_, int p_149694_2_, int p_149694_3_, int p_149694_4_) {
		return Item.getItemById(Block.getIdFromBlock(Blocks.brown_mushroom) + field_149792_b);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister p_149651_1_) {
		field_149794_M = new IIcon[field_149793_a.length];

		for (int i = 0; i < field_149794_M.length; ++i) {
			field_149794_M[i] = p_149651_1_.registerIcon(getTextureName() + "_" + field_149793_a[i]);
		}

		field_149796_O = p_149651_1_.registerIcon(getTextureName() + "_" + "inside");
		field_149795_N = p_149651_1_.registerIcon(getTextureName() + "_" + "skin_stem");
	}
}