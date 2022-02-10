package net.minecraft.item;

public class ItemBook extends Item {
	private static final String __OBFID = "CL_00001775";

	@Override
	public boolean isItemTool(ItemStack p_77616_1_) {
		return p_77616_1_.stackSize == 1;
	}

	@Override
	public int getItemEnchantability() {
		return 1;
	}
}