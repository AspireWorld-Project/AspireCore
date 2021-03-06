package net.minecraft.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import java.util.ArrayList;

public class BlockPotato extends BlockCrops {
	@SideOnly(Side.CLIENT)
	private IIcon[] field_149869_a;
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int p_149691_1_, int p_149691_2_) {
		if (p_149691_2_ < 7) {
			if (p_149691_2_ == 6) {
				p_149691_2_ = 5;
			}

			return field_149869_a[p_149691_2_ >> 1];
		} else
			return field_149869_a[3];
	}

	@Override
	protected Item func_149866_i() {
		return Items.potato;
	}

	@Override
	protected Item func_149865_P() {
		return Items.potato;
	}

	@Override
	public void dropBlockAsItemWithChance(World p_149690_1_, int p_149690_2_, int p_149690_3_, int p_149690_4_,
			int p_149690_5_, float p_149690_6_, int p_149690_7_) {
		super.dropBlockAsItemWithChance(p_149690_1_, p_149690_2_, p_149690_3_, p_149690_4_, p_149690_5_, p_149690_6_,
				p_149690_7_);
	}

	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
		ArrayList<ItemStack> ret = super.getDrops(world, x, y, z, metadata, fortune);
		if (metadata >= 7 && world.rand.nextInt(50) == 0) {
			ret.add(new ItemStack(Items.poisonous_potato));
		}
		return ret;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister p_149651_1_) {
		field_149869_a = new IIcon[4];

		for (int i = 0; i < field_149869_a.length; ++i) {
			field_149869_a[i] = p_149651_1_.registerIcon(getTextureName() + "_stage_" + i);
		}
	}
}