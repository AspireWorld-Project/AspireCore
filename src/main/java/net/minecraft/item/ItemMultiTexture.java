package net.minecraft.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.util.IIcon;

public class ItemMultiTexture extends ItemBlock {
	protected final Block field_150941_b;
	protected final String[] field_150942_c;
	private static final String __OBFID = "CL_00000051";

	public ItemMultiTexture(Block p_i45346_1_, Block p_i45346_2_, String[] p_i45346_3_) {
		super(p_i45346_1_);
		field_150941_b = p_i45346_2_;
		field_150942_c = p_i45346_3_;
		setMaxDamage(0);
		setHasSubtypes(true);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamage(int p_77617_1_) {
		return field_150941_b.getIcon(2, p_77617_1_);
	}

	@Override
	public int getMetadata(int p_77647_1_) {
		return p_77647_1_;
	}

	@Override
	public String getUnlocalizedName(ItemStack p_77667_1_) {
		int i = p_77667_1_.getItemDamage();

		if (i < 0 || i >= field_150942_c.length) {
			i = 0;
		}

		return super.getUnlocalizedName() + "." + field_150942_c[i];
	}
}