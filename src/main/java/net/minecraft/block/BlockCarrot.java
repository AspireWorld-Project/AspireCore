package net.minecraft.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;

public class BlockCarrot extends BlockCrops {
	@SideOnly(Side.CLIENT)
	private IIcon[] field_149868_a;
	private static final String __OBFID = "CL_00000212";

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int p_149691_1_, int p_149691_2_) {
		if (p_149691_2_ < 7) {
			if (p_149691_2_ == 6) {
				p_149691_2_ = 5;
			}

			return field_149868_a[p_149691_2_ >> 1];
		} else
			return field_149868_a[3];
	}

	@Override
	protected Item func_149866_i() {
		return Items.carrot;
	}

	@Override
	protected Item func_149865_P() {
		return Items.carrot;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister p_149651_1_) {
		field_149868_a = new IIcon[4];

		for (int i = 0; i < field_149868_a.length; ++i) {
			field_149868_a[i] = p_149651_1_.registerIcon(getTextureName() + "_stage_" + i);
		}
	}
}