package net.minecraft.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.util.IIcon;

public class ItemBlockWithMetadata extends ItemBlock {
	private final Block field_150950_b;
	private static final String __OBFID = "CL_00001769";

	public ItemBlockWithMetadata(Block p_i45326_1_, Block p_i45326_2_) {
		super(p_i45326_1_);
		field_150950_b = p_i45326_2_;
		setMaxDamage(0);
		setHasSubtypes(true);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamage(int p_77617_1_) {
		return field_150950_b.getIcon(2, p_77617_1_);
	}

	@Override
	public int getMetadata(int p_77647_1_) {
		return p_77647_1_;
	}
}