package org.bukkit.craftbukkit.inventory;

import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import org.bukkit.inventory.Recipe;
import org.ultramine.bukkit.util.CustomModRecipe;

import java.util.Collections;
import java.util.Iterator;

public class RecipeIterator implements Iterator<Recipe> {
	private final Iterator<net.minecraft.item.crafting.IRecipe> recipes;
	private final Iterator<net.minecraft.item.ItemStack> smeltingCustom;
	private final Iterator<net.minecraft.item.ItemStack> smeltingVanilla;
	private Iterator<?> removeFrom = null;

	@SuppressWarnings("unchecked")
	public RecipeIterator() {
		recipes = net.minecraft.item.crafting.CraftingManager.getInstance().getRecipeList().iterator();
		// this.smeltingCustom =
		// net.minecraft.item.crafting.FurnaceRecipes.smelting().customRecipes.keySet().iterator();
		// this.smeltingVanilla =
		// net.minecraft.item.crafting.FurnaceRecipes.smelting().smeltingList.keySet().iterator();
		// TODO
		smeltingCustom = Collections.<net.minecraft.item.ItemStack>emptyList().iterator();
		smeltingVanilla = net.minecraft.item.crafting.FurnaceRecipes.smelting().getSmeltingList().keySet().iterator();
	}

	@Override
	public boolean hasNext() {
		return recipes.hasNext() || smeltingCustom.hasNext() || smeltingVanilla.hasNext();
	}

	@Override
	public Recipe next() {
		if (recipes.hasNext()) {
			removeFrom = recipes;
			IRecipe next = recipes.next();
			if (recipes.next() instanceof ShapelessRecipes)
				return ((ShapelessRecipes) recipes.next()).toBukkitRecipe();
			else if (recipes.next() instanceof ShapedRecipes)
				return ((ShapedRecipes) recipes.next()).toBukkitRecipe();
			return new CustomModRecipe(next);
		} else {
			net.minecraft.item.ItemStack item;
			if (smeltingCustom.hasNext()) {
				removeFrom = smeltingCustom;
				item = smeltingCustom.next();
			} else {
				removeFrom = smeltingVanilla;
				item = smeltingVanilla.next();
			}

			CraftItemStack stack = CraftItemStack
					.asCraftMirror(net.minecraft.item.crafting.FurnaceRecipes.smelting().getSmeltingResult(item));

			return new CraftFurnaceRecipe(stack, CraftItemStack.asCraftMirror(item));
		}
	}

	@Override
	public void remove() {
		if (removeFrom == null)
			throw new IllegalStateException();
		removeFrom.remove();
	}
}
