package net.minecraft.item.crafting;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.inventory.CraftShapedRecipe;

public class ShapedRecipes implements IRecipe {
	public final int recipeWidth;
	public final int recipeHeight;
	public final ItemStack[] recipeItems;
	private final ItemStack recipeOutput;
	private boolean field_92101_f;
	private static final String __OBFID = "CL_00000093";

	public ShapedRecipes(int p_i1917_1_, int p_i1917_2_, ItemStack[] p_i1917_3_, ItemStack p_i1917_4_) {
		recipeWidth = p_i1917_1_;
		recipeHeight = p_i1917_2_;
		recipeItems = p_i1917_3_;
		recipeOutput = p_i1917_4_;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return recipeOutput;
	}

	@Override
	public boolean matches(InventoryCrafting p_77569_1_, World p_77569_2_) {
		for (int i = 0; i <= 3 - recipeWidth; ++i) {
			for (int j = 0; j <= 3 - recipeHeight; ++j) {
				if (checkMatch(p_77569_1_, i, j, true))
					return true;

				if (checkMatch(p_77569_1_, i, j, false))
					return true;
			}
		}

		return false;
	}

	private boolean checkMatch(InventoryCrafting p_77573_1_, int p_77573_2_, int p_77573_3_, boolean p_77573_4_) {
		for (int k = 0; k < 3; ++k) {
			for (int l = 0; l < 3; ++l) {
				int i1 = k - p_77573_2_;
				int j1 = l - p_77573_3_;
				ItemStack itemstack = null;

				if (i1 >= 0 && j1 >= 0 && i1 < recipeWidth && j1 < recipeHeight) {
					if (p_77573_4_) {
						itemstack = recipeItems[recipeWidth - i1 - 1 + j1 * recipeWidth];
					} else {
						itemstack = recipeItems[i1 + j1 * recipeWidth];
					}
				}

				ItemStack itemstack1 = p_77573_1_.getStackInRowAndColumn(k, l);

				if (itemstack1 != null || itemstack != null) {
					if (itemstack1 == null && itemstack != null || itemstack1 != null && itemstack == null)
						return false;

					if (itemstack.getItem() != itemstack1.getItem())
						return false;

					if (itemstack.getItemDamage() != 32767 && itemstack.getItemDamage() != itemstack1.getItemDamage())
						return false;
				}
			}
		}

		return true;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting p_77572_1_) {
		ItemStack itemstack = getRecipeOutput().copy();

		if (field_92101_f) {
			for (int i = 0; i < p_77572_1_.getSizeInventory(); ++i) {
				ItemStack itemstack1 = p_77572_1_.getStackInSlot(i);

				if (itemstack1 != null && itemstack1.hasTagCompound()) {
					itemstack.setTagCompound((NBTTagCompound) itemstack1.stackTagCompound.copy());
				}
			}
		}

		return itemstack;
	}

	@Override
	public int getRecipeSize() {
		return recipeWidth * recipeHeight;
	}

	public ShapedRecipes func_92100_c() {
		field_92101_f = true;
		return this;
	}

	public CraftShapedRecipe toBukkitRecipe() {
		ShapedRecipes thisShapedRecipe = this;
		CraftItemStack result = CraftItemStack.asCraftMirror(recipeOutput);
		CraftShapedRecipe craftShapedRecipe = new CraftShapedRecipe(result, thisShapedRecipe);
		switch (thisShapedRecipe.recipeHeight) {
		case 1:
			switch (thisShapedRecipe.recipeWidth) {
			case 1:
				craftShapedRecipe.shape("a");
				break;
			case 2:
				craftShapedRecipe.shape("ab");
				break;
			case 3:
				craftShapedRecipe.shape("abc");
				break;
			}
			break;
		case 2:
			switch (thisShapedRecipe.recipeWidth) {
			case 1:
				craftShapedRecipe.shape("a", "b");
				break;
			case 2:
				craftShapedRecipe.shape("ab", "cd");
				break;
			case 3:
				craftShapedRecipe.shape("abc", "def");
				break;
			}
			break;
		case 3:
			switch (thisShapedRecipe.recipeWidth) {
			case 1:
				craftShapedRecipe.shape("a", "b", "c");
				break;
			case 2:
				craftShapedRecipe.shape("ab", "cd", "ef");
				break;
			case 3:
				craftShapedRecipe.shape("abc", "def", "ghi");
				break;
			}
			break;
		}
		char c = 'a';
		for (ItemStack stack : thisShapedRecipe.recipeItems) {
			if (stack != null) {
				craftShapedRecipe.setIngredient(c,
						org.bukkit.craftbukkit.util.CraftMagicNumbers.getMaterial(stack.getItem()),
						stack.getItemDamage());
			}
			c++;
		}
		return craftShapedRecipe;
	}
}