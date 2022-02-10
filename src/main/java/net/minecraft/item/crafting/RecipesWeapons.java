package net.minecraft.item.crafting;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class RecipesWeapons {
	private String[][] recipePatterns = new String[][] { { "X", "X", "#" } };
	private Object[][] recipeItems;
	private static final String __OBFID = "CL_00000097";

	public RecipesWeapons() {
		recipeItems = new Object[][] {
				{ Blocks.planks, Blocks.cobblestone, Items.iron_ingot, Items.diamond, Items.gold_ingot },
				{ Items.wooden_sword, Items.stone_sword, Items.iron_sword, Items.diamond_sword, Items.golden_sword } };
	}

	public void addRecipes(CraftingManager p_77583_1_) {
		for (int i = 0; i < recipeItems[0].length; ++i) {
			Object object = recipeItems[0][i];

			for (int j = 0; j < recipeItems.length - 1; ++j) {
				Item item = (Item) recipeItems[j + 1][i];
				p_77583_1_.addRecipe(new ItemStack(item),
						new Object[] { recipePatterns[j], '#', Items.stick, 'X', object });
			}
		}

		p_77583_1_.addRecipe(new ItemStack(Items.bow, 1),
				new Object[] { " #X", "# X", " #X", 'X', Items.string, '#', Items.stick });
		p_77583_1_.addRecipe(new ItemStack(Items.arrow, 4),
				new Object[] { "X", "#", "Y", 'Y', Items.feather, 'X', Items.flint, '#', Items.stick });
	}
}