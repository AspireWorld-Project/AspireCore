package org.bukkit.craftbukkit.inventory;

import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;

public class CraftFurnaceRecipe extends FurnaceRecipe implements CraftRecipe {
	public CraftFurnaceRecipe(ItemStack result, ItemStack source) {
		super(result, source.getType(), source.getDurability());
	}

	public static CraftFurnaceRecipe fromBukkitRecipe(FurnaceRecipe recipe) {
		if (recipe instanceof CraftFurnaceRecipe)
			return (CraftFurnaceRecipe) recipe;
		return new CraftFurnaceRecipe(recipe.getResult(), recipe.getInput());
	}

	@Override
	public void addToCraftingManager() {
		ItemStack result = getResult();
		ItemStack input = getInput();
		net.minecraft.item.crafting.FurnaceRecipes.smelting().func_151394_a(CraftItemStack.asNMSCopy(input),
				CraftItemStack.asNMSCopy(result), 0);
	}
}
