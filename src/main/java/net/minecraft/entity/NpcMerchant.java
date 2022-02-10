package net.minecraft.entity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryMerchant;
import net.minecraft.item.ItemStack;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;

@SideOnly(Side.CLIENT)
public class NpcMerchant implements IMerchant {
	private InventoryMerchant theMerchantInventory;
	private EntityPlayer customer;
	private MerchantRecipeList recipeList;
	private static final String __OBFID = "CL_00001705";

	public NpcMerchant(EntityPlayer p_i1746_1_) {
		customer = p_i1746_1_;
		theMerchantInventory = new InventoryMerchant(p_i1746_1_, this);
	}

	@Override
	public EntityPlayer getCustomer() {
		return customer;
	}

	@Override
	public void setCustomer(EntityPlayer p_70932_1_) {
	}

	@Override
	public MerchantRecipeList getRecipes(EntityPlayer p_70934_1_) {
		return recipeList;
	}

	@Override
	public void setRecipes(MerchantRecipeList p_70930_1_) {
		recipeList = p_70930_1_;
	}

	@Override
	public void useRecipe(MerchantRecipe p_70933_1_) {
	}

	@Override
	public void func_110297_a_(ItemStack p_110297_1_) {
	}
}