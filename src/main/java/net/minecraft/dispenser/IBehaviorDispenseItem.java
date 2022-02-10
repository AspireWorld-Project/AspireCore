package net.minecraft.dispenser;

import net.minecraft.item.ItemStack;

public interface IBehaviorDispenseItem {
	IBehaviorDispenseItem itemDispenseBehaviorProvider = new IBehaviorDispenseItem() {
		private static final String __OBFID = "CL_00001200";

		@Override
		public ItemStack dispense(IBlockSource p_82482_1_, ItemStack p_82482_2_) {
			return p_82482_2_;
		}
	};

	ItemStack dispense(IBlockSource p_82482_1_, ItemStack p_82482_2_);
}