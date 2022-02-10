package net.minecraft.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockLeaves;
import net.minecraft.util.IIcon;

public class ItemLeaves extends ItemBlock {
	private final BlockLeaves field_150940_b;
	private static final String __OBFID = "CL_00000046";

	public ItemLeaves(BlockLeaves p_i45344_1_) {
		super(p_i45344_1_);
		field_150940_b = p_i45344_1_;
		setMaxDamage(0);
		setHasSubtypes(true);
	}

	@Override
	public int getMetadata(int p_77647_1_) {
		return p_77647_1_ | 4;
	}

	@Override
	public String getUnlocalizedName(ItemStack p_77667_1_) {
		int i = p_77667_1_.getItemDamage();

		if (i < 0 || i >= field_150940_b.func_150125_e().length) {
			i = 0;
		}

		return super.getUnlocalizedName() + "." + field_150940_b.func_150125_e()[i];
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamage(int p_77617_1_) {
		return field_150940_b.getIcon(0, p_77617_1_);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getColorFromItemStack(ItemStack p_82790_1_, int p_82790_2_) {
		return field_150940_b.getRenderColor(p_82790_1_.getItemDamage());
	}
}