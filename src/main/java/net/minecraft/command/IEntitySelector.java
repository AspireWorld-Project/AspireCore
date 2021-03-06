package net.minecraft.command;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public interface IEntitySelector {
	IEntitySelector selectAnything = new IEntitySelector() {
		private static final String __OBFID = "CL_00001541";

		@Override
		public boolean isEntityApplicable(Entity p_82704_1_) {
			return p_82704_1_.isEntityAlive();
		}
	};
	IEntitySelector field_152785_b = new IEntitySelector() {
		private static final String __OBFID = "CL_00001542";

		@Override
		public boolean isEntityApplicable(Entity p_82704_1_) {
			return p_82704_1_.isEntityAlive() && p_82704_1_.riddenByEntity == null && p_82704_1_.ridingEntity == null;
		}
	};
	IEntitySelector selectInventories = new IEntitySelector() {
		private static final String __OBFID = "CL_00001867";

		@Override
		public boolean isEntityApplicable(Entity p_82704_1_) {
			return p_82704_1_ instanceof IInventory && p_82704_1_.isEntityAlive();
		}
	};

	boolean isEntityApplicable(Entity p_82704_1_);

	class ArmoredMob implements IEntitySelector {
		private final ItemStack field_96567_c;
		private static final String __OBFID = "CL_00001543";

		public ArmoredMob(ItemStack p_i1584_1_) {
			field_96567_c = p_i1584_1_;
		}

		@Override
		public boolean isEntityApplicable(Entity p_82704_1_) {
			if (!p_82704_1_.isEntityAlive())
				return false;
			else if (!(p_82704_1_ instanceof EntityLivingBase))
				return false;
			else {
				EntityLivingBase entitylivingbase = (EntityLivingBase) p_82704_1_;
				return entitylivingbase.getEquipmentInSlot(EntityLiving.getArmorPosition(field_96567_c)) == null && (entitylivingbase instanceof EntityLiving ? ((EntityLiving) entitylivingbase).canPickUpLoot()
						: entitylivingbase instanceof EntityPlayer);
			}
		}
	}
}