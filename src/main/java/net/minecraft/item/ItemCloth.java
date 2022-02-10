package net.minecraft.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockColored;
import net.minecraft.util.IIcon;

public class ItemCloth extends ItemBlock {
	private static final String __OBFID = "CL_00000075";

	public ItemCloth(Block p_i45358_1_) {
		super(p_i45358_1_);
		setMaxDamage(0);
		setHasSubtypes(true);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamage(int p_77617_1_) {
		return field_150939_a.func_149735_b(2, BlockColored.func_150032_b(p_77617_1_));
	}

	@Override
	public int getMetadata(int p_77647_1_) {
		return p_77647_1_;
	}

	@Override
	public String getUnlocalizedName(ItemStack p_77667_1_) {
		return super.getUnlocalizedName() + "."
				+ ItemDye.field_150923_a[BlockColored.func_150032_b(p_77667_1_.getItemDamage())];
	}
}