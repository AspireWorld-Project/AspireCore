package net.minecraft.inventory;

import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.*;
import net.minecraft.stats.AchievementList;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;

public class SlotCrafting extends Slot {
	private final IInventory craftMatrix;
	private final EntityPlayer thePlayer;
	private int amountCrafted;
	private static final String __OBFID = "CL_00001761";

	public SlotCrafting(EntityPlayer p_i1823_1_, IInventory p_i1823_2_, IInventory p_i1823_3_, int p_i1823_4_,
			int p_i1823_5_, int p_i1823_6_) {
		super(p_i1823_3_, p_i1823_4_, p_i1823_5_, p_i1823_6_);
		thePlayer = p_i1823_1_;
		craftMatrix = p_i1823_2_;
	}

	@Override
	public boolean isItemValid(ItemStack p_75214_1_) {
		return false;
	}

	@Override
	public ItemStack decrStackSize(int p_75209_1_) {
		if (getHasStack()) {
			amountCrafted += Math.min(p_75209_1_, getStack().stackSize);
		}

		return super.decrStackSize(p_75209_1_);
	}

	@Override
	protected void onCrafting(ItemStack p_75210_1_, int p_75210_2_) {
		amountCrafted += p_75210_2_;
		this.onCrafting(p_75210_1_);
	}

	@Override
	protected void onCrafting(ItemStack p_75208_1_) {
		p_75208_1_.onCrafting(thePlayer.worldObj, thePlayer, amountCrafted);
		amountCrafted = 0;

		if (p_75208_1_.getItem() == Item.getItemFromBlock(Blocks.crafting_table)) {
			thePlayer.addStat(AchievementList.buildWorkBench, 1);
		}

		if (p_75208_1_.getItem() instanceof ItemPickaxe) {
			thePlayer.addStat(AchievementList.buildPickaxe, 1);
		}

		if (p_75208_1_.getItem() == Item.getItemFromBlock(Blocks.furnace)) {
			thePlayer.addStat(AchievementList.buildFurnace, 1);
		}

		if (p_75208_1_.getItem() instanceof ItemHoe) {
			thePlayer.addStat(AchievementList.buildHoe, 1);
		}

		if (p_75208_1_.getItem() == Items.bread) {
			thePlayer.addStat(AchievementList.makeBread, 1);
		}

		if (p_75208_1_.getItem() == Items.cake) {
			thePlayer.addStat(AchievementList.bakeCake, 1);
		}

		if (p_75208_1_.getItem() instanceof ItemPickaxe
				&& ((ItemPickaxe) p_75208_1_.getItem()).func_150913_i() != Item.ToolMaterial.WOOD) {
			thePlayer.addStat(AchievementList.buildBetterPickaxe, 1);
		}

		if (p_75208_1_.getItem() instanceof ItemSword) {
			thePlayer.addStat(AchievementList.buildSword, 1);
		}

		if (p_75208_1_.getItem() == Item.getItemFromBlock(Blocks.enchanting_table)) {
			thePlayer.addStat(AchievementList.enchantments, 1);
		}

		if (p_75208_1_.getItem() == Item.getItemFromBlock(Blocks.bookshelf)) {
			thePlayer.addStat(AchievementList.bookcase, 1);
		}
	}

	@Override
	public void onPickupFromSlot(EntityPlayer p_82870_1_, ItemStack p_82870_2_) {
		FMLCommonHandler.instance().firePlayerCraftingEvent(p_82870_1_, p_82870_2_, craftMatrix);
		this.onCrafting(p_82870_2_);

		InventoryCrafting.callMatrixChanged = false;
		for (int i = 0; i < craftMatrix.getSizeInventory(); ++i) {
			ItemStack itemstack1 = craftMatrix.getStackInSlot(i);

			if (itemstack1 != null) {
				craftMatrix.decrStackSize(i, 1);

				if (itemstack1.getItem().hasContainerItem(itemstack1)) {
					ItemStack itemstack2 = itemstack1.getItem().getContainerItem(itemstack1);

					if (itemstack2 != null && itemstack2.isItemStackDamageable()
							&& itemstack2.getItemDamage() > itemstack2.getMaxDamage()) {
						MinecraftForge.EVENT_BUS.post(new PlayerDestroyItemEvent(thePlayer, itemstack2));
						continue;
					}

					if (!itemstack1.getItem().doesContainerItemLeaveCraftingGrid(itemstack1)
							|| !thePlayer.inventory.addItemStackToInventory(itemstack2)) {
						if (craftMatrix.getStackInSlot(i) == null) {
							craftMatrix.setInventorySlotContents(i, itemstack2);
						} else {
							thePlayer.dropPlayerItemWithRandomChoice(itemstack2, false);
						}
					}
				}
			}
		}
		InventoryCrafting.callMatrixChanged = true;
		craftMatrix.setInventorySlotContents(0, craftMatrix.getStackInSlot(0));
	}
}