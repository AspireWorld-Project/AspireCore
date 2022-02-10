package org.ultramine.bukkit.util;

import javax.annotation.Nullable;

import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import net.minecraft.item.crafting.IRecipe;

/**
 * Bukkit API wrapper for non-vanilla IRecipe classes
 */
public class CustomModRecipe implements Recipe {
	@Nullable
	private IRecipe iRecipe;

	public CustomModRecipe(IRecipe iRecipe) {
		this.iRecipe = iRecipe;
	}

	@Override
	public ItemStack getResult() {
		if (iRecipe == null)
			return null;
		return CraftItemStack.asCraftMirror(iRecipe.getRecipeOutput());
	}

	public IRecipe getHandle() {
		return iRecipe;
	}
}
