package org.ultramine.bukkit.util;

import net.minecraft.item.crafting.IRecipe;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import javax.annotation.Nullable;

/**
 * Bukkit API wrapper for non-vanilla IRecipe classes
 */
public class CustomModRecipe implements Recipe {
	@Nullable
	private final IRecipe iRecipe;

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
