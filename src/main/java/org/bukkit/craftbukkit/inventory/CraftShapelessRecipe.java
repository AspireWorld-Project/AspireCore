package org.bukkit.craftbukkit.inventory;

import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;

import java.util.List;

public class CraftShapelessRecipe extends ShapelessRecipe implements CraftRecipe {
	// TODO: Could eventually use this to add a matches() method or some such
	@SuppressWarnings("unused")
	private net.minecraft.item.crafting.ShapelessRecipes recipe;

	public CraftShapelessRecipe(ItemStack result) {
		super(result);
	}

	public CraftShapelessRecipe(ItemStack result, net.minecraft.item.crafting.ShapelessRecipes recipe) {
		this(result);
		this.recipe = recipe;
	}

	public static CraftShapelessRecipe fromBukkitRecipe(ShapelessRecipe recipe) {
		if (recipe instanceof CraftShapelessRecipe)
			return (CraftShapelessRecipe) recipe;
		CraftShapelessRecipe ret = new CraftShapelessRecipe(recipe.getResult());
		for (ItemStack ingred : recipe.getIngredientList()) {
			ret.addIngredient(ingred.getType(), ingred.getDurability());
		}
		return ret;
	}

	@Override
	public void addToCraftingManager() {
		List<ItemStack> ingred = getIngredientList();
		Object[] data = new Object[ingred.size()];
		int i = 0;
		for (ItemStack mdata : ingred) {
			int id = mdata.getTypeId();
			short dmg = mdata.getDurability();
			data[i] = new net.minecraft.item.ItemStack(CraftMagicNumbers.getItem(id), 1, dmg);
			i++;
		}
		net.minecraft.item.crafting.CraftingManager.getInstance()
				.addShapelessRecipe(CraftItemStack.asNMSCopy(getResult()), data);
	}
}
