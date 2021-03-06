package net.minecraft.item.crafting;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.ultramine.server.UltramineServerModContainer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class CraftingManager {
	private static final CraftingManager instance = new CraftingManager();
	public List recipes = new ArrayList();

	public static CraftingManager getInstance() {
		return instance;
	}

	private CraftingManager() {
		new RecipesTools().addRecipes(this);
		new RecipesWeapons().addRecipes(this);
		new RecipesIngots().addRecipes(this);
		new RecipesFood().addRecipes(this);
		new RecipesCrafting().addRecipes(this);
		new RecipesArmor().addRecipes(this);
		new RecipesDyes().addRecipes(this);
		recipes.add(new RecipesArmorDyes());
		recipes.add(new RecipeBookCloning());
		recipes.add(new RecipesMapCloning());
		recipes.add(new RecipesMapExtending());
		recipes.add(new RecipeFireworks());
		addRecipe(new ItemStack(Items.paper, 3), "###", '#', Items.reeds);
		addShapelessRecipe(new ItemStack(Items.book, 1),
				Items.paper, Items.paper, Items.paper, Items.leather);
		addShapelessRecipe(new ItemStack(Items.writable_book, 1),
				Items.book, new ItemStack(Items.dye, 1, 0), Items.feather);
		addRecipe(new ItemStack(Blocks.fence, 2), "###", "###", '#', Items.stick);
		addRecipe(new ItemStack(Blocks.cobblestone_wall, 6, 0), "###", "###", '#', Blocks.cobblestone);
		addRecipe(new ItemStack(Blocks.cobblestone_wall, 6, 1),
				"###", "###", '#', Blocks.mossy_cobblestone);
		addRecipe(new ItemStack(Blocks.nether_brick_fence, 6), "###", "###", '#', Blocks.nether_brick);
		addRecipe(new ItemStack(Blocks.fence_gate, 1),
				"#W#", "#W#", '#', Items.stick, 'W', Blocks.planks);
		addRecipe(new ItemStack(Blocks.jukebox, 1),
				"###", "#X#", "###", '#', Blocks.planks, 'X', Items.diamond);
		addRecipe(new ItemStack(Items.lead, 2),
				"~~ ", "~O ", "  ~", '~', Items.string, 'O', Items.slime_ball);
		addRecipe(new ItemStack(Blocks.noteblock, 1),
				"###", "#X#", "###", '#', Blocks.planks, 'X', Items.redstone);
		addRecipe(new ItemStack(Blocks.bookshelf, 1),
				"###", "XXX", "###", '#', Blocks.planks, 'X', Items.book);
		addRecipe(new ItemStack(Blocks.snow, 1), "##", "##", '#', Items.snowball);
		addRecipe(new ItemStack(Blocks.snow_layer, 6), "###", '#', Blocks.snow);
		addRecipe(new ItemStack(Blocks.clay, 1), "##", "##", '#', Items.clay_ball);
		addRecipe(new ItemStack(Blocks.brick_block, 1), "##", "##", '#', Items.brick);
		addRecipe(new ItemStack(Blocks.glowstone, 1), "##", "##", '#', Items.glowstone_dust);
		addRecipe(new ItemStack(Blocks.quartz_block, 1), "##", "##", '#', Items.quartz);
		addRecipe(new ItemStack(Blocks.wool, 1), "##", "##", '#', Items.string);
		addRecipe(new ItemStack(Blocks.tnt, 1),
				"X#X", "#X#", "X#X", 'X', Items.gunpowder, '#', Blocks.sand);
		addRecipe(new ItemStack(Blocks.stone_slab, 6, 3), "###", '#', Blocks.cobblestone);
		addRecipe(new ItemStack(Blocks.stone_slab, 6, 0), "###", '#', Blocks.stone);
		addRecipe(new ItemStack(Blocks.stone_slab, 6, 1), "###", '#', Blocks.sandstone);
		addRecipe(new ItemStack(Blocks.stone_slab, 6, 4), "###", '#', Blocks.brick_block);
		addRecipe(new ItemStack(Blocks.stone_slab, 6, 5), "###", '#', Blocks.stonebrick);
		addRecipe(new ItemStack(Blocks.stone_slab, 6, 6), "###", '#', Blocks.nether_brick);
		addRecipe(new ItemStack(Blocks.stone_slab, 6, 7), "###", '#', Blocks.quartz_block);
		addRecipe(new ItemStack(Blocks.wooden_slab, 6, 0),
				"###", '#', new ItemStack(Blocks.planks, 1, 0));
		addRecipe(new ItemStack(Blocks.wooden_slab, 6, 2),
				"###", '#', new ItemStack(Blocks.planks, 1, 2));
		addRecipe(new ItemStack(Blocks.wooden_slab, 6, 1),
				"###", '#', new ItemStack(Blocks.planks, 1, 1));
		addRecipe(new ItemStack(Blocks.wooden_slab, 6, 3),
				"###", '#', new ItemStack(Blocks.planks, 1, 3));
		addRecipe(new ItemStack(Blocks.wooden_slab, 6, 4),
				"###", '#', new ItemStack(Blocks.planks, 1, 4));
		addRecipe(new ItemStack(Blocks.wooden_slab, 6, 5),
				"###", '#', new ItemStack(Blocks.planks, 1, 5));
		addRecipe(new ItemStack(Blocks.ladder, 3), "# #", "###", "# #", '#', Items.stick);
		addRecipe(new ItemStack(Items.wooden_door, 1), "##", "##", "##", '#', Blocks.planks);
		addRecipe(new ItemStack(Blocks.trapdoor, 2), "###", "###", '#', Blocks.planks);
		addRecipe(new ItemStack(Items.iron_door, 1), "##", "##", "##", '#', Items.iron_ingot);
		addRecipe(new ItemStack(Items.sign, 3),
				"###", "###", " X ", '#', Blocks.planks, 'X', Items.stick);
		addRecipe(new ItemStack(Items.cake, 1), "AAA", "BEB", "CCC", 'A', Items.milk_bucket, 'B',
				Items.sugar, 'C', Items.wheat, 'E', Items.egg);
		addRecipe(new ItemStack(Items.sugar, 1), "#", '#', Items.reeds);
		addRecipe(new ItemStack(Blocks.planks, 4, 0), "#", '#', new ItemStack(Blocks.log, 1, 0));
		addRecipe(new ItemStack(Blocks.planks, 4, 1), "#", '#', new ItemStack(Blocks.log, 1, 1));
		addRecipe(new ItemStack(Blocks.planks, 4, 2), "#", '#', new ItemStack(Blocks.log, 1, 2));
		addRecipe(new ItemStack(Blocks.planks, 4, 3), "#", '#', new ItemStack(Blocks.log, 1, 3));
		addRecipe(new ItemStack(Blocks.planks, 4, 4), "#", '#', new ItemStack(Blocks.log2, 1, 0));
		addRecipe(new ItemStack(Blocks.planks, 4, 5), "#", '#', new ItemStack(Blocks.log2, 1, 1));
		addRecipe(new ItemStack(Items.stick, 4), "#", "#", '#', Blocks.planks);
		addRecipe(new ItemStack(Blocks.torch, 4), "X", "#", 'X', Items.coal, '#', Items.stick);
		addRecipe(new ItemStack(Blocks.torch, 4),
				"X", "#", 'X', new ItemStack(Items.coal, 1, 1), '#', Items.stick);
		addRecipe(new ItemStack(Items.bowl, 4), "# #", " # ", '#', Blocks.planks);
		addRecipe(new ItemStack(Items.glass_bottle, 3), "# #", " # ", '#', Blocks.glass);
		addRecipe(new ItemStack(Blocks.rail, 16),
				"X X", "X#X", "X X", 'X', Items.iron_ingot, '#', Items.stick);
		addRecipe(new ItemStack(Blocks.golden_rail, 6),
				"X X", "X#X", "XRX", 'X', Items.gold_ingot, 'R', Items.redstone, '#', Items.stick);
		addRecipe(new ItemStack(Blocks.activator_rail, 6), "XSX", "X#X", "XSX", 'X', Items.iron_ingot,
				'#', Blocks.redstone_torch, 'S', Items.stick);
		addRecipe(new ItemStack(Blocks.detector_rail, 6), "X X", "X#X", "XRX", 'X', Items.iron_ingot,
				'R', Items.redstone, '#', Blocks.stone_pressure_plate);
		addRecipe(new ItemStack(Items.minecart, 1), "# #", "###", '#', Items.iron_ingot);
		addRecipe(new ItemStack(Items.cauldron, 1), "# #", "# #", "###", '#', Items.iron_ingot);
		addRecipe(new ItemStack(Items.brewing_stand, 1),
				" B ", "###", '#', Blocks.cobblestone, 'B', Items.blaze_rod);
		addRecipe(new ItemStack(Blocks.lit_pumpkin, 1),
				"A", "B", 'A', Blocks.pumpkin, 'B', Blocks.torch);
		addRecipe(new ItemStack(Items.chest_minecart, 1),
				"A", "B", 'A', Blocks.chest, 'B', Items.minecart);
		addRecipe(new ItemStack(Items.furnace_minecart, 1),
				"A", "B", 'A', Blocks.furnace, 'B', Items.minecart);
		addRecipe(new ItemStack(Items.tnt_minecart, 1),
				"A", "B", 'A', Blocks.tnt, 'B', Items.minecart);
		addRecipe(new ItemStack(Items.hopper_minecart, 1),
				"A", "B", 'A', Blocks.hopper, 'B', Items.minecart);
		addRecipe(new ItemStack(Items.boat, 1), "# #", "###", '#', Blocks.planks);
		addRecipe(new ItemStack(Items.bucket, 1), "# #", " # ", '#', Items.iron_ingot);
		addRecipe(new ItemStack(Items.flower_pot, 1), "# #", " # ", '#', Items.brick);
		addShapelessRecipe(new ItemStack(Items.flint_and_steel, 1),
				new ItemStack(Items.iron_ingot, 1), new ItemStack(Items.flint, 1));
		addRecipe(new ItemStack(Items.bread, 1), "###", '#', Items.wheat);
		addRecipe(new ItemStack(Blocks.oak_stairs, 4),
				"#  ", "## ", "###", '#', new ItemStack(Blocks.planks, 1, 0));
		addRecipe(new ItemStack(Blocks.birch_stairs, 4),
				"#  ", "## ", "###", '#', new ItemStack(Blocks.planks, 1, 2));
		addRecipe(new ItemStack(Blocks.spruce_stairs, 4),
				"#  ", "## ", "###", '#', new ItemStack(Blocks.planks, 1, 1));
		addRecipe(new ItemStack(Blocks.jungle_stairs, 4),
				"#  ", "## ", "###", '#', new ItemStack(Blocks.planks, 1, 3));
		addRecipe(new ItemStack(Blocks.acacia_stairs, 4),
				"#  ", "## ", "###", '#', new ItemStack(Blocks.planks, 1, 4));
		addRecipe(new ItemStack(Blocks.dark_oak_stairs, 4),
				"#  ", "## ", "###", '#', new ItemStack(Blocks.planks, 1, 5));
		addRecipe(new ItemStack(Items.fishing_rod, 1),
				"  #", " #X", "# X", '#', Items.stick, 'X', Items.string);
		addRecipe(new ItemStack(Items.carrot_on_a_stick, 1),
				"# ", " X", '#', Items.fishing_rod, 'X', Items.carrot).func_92100_c();
		addRecipe(new ItemStack(Blocks.stone_stairs, 4), "#  ", "## ", "###", '#', Blocks.cobblestone);
		addRecipe(new ItemStack(Blocks.brick_stairs, 4), "#  ", "## ", "###", '#', Blocks.brick_block);
		addRecipe(new ItemStack(Blocks.stone_brick_stairs, 4),
				"#  ", "## ", "###", '#', Blocks.stonebrick);
		addRecipe(new ItemStack(Blocks.nether_brick_stairs, 4),
				"#  ", "## ", "###", '#', Blocks.nether_brick);
		addRecipe(new ItemStack(Blocks.sandstone_stairs, 4),
				"#  ", "## ", "###", '#', Blocks.sandstone);
		addRecipe(new ItemStack(Blocks.quartz_stairs, 4),
				"#  ", "## ", "###", '#', Blocks.quartz_block);
		addRecipe(new ItemStack(Items.painting, 1),
				"###", "#X#", "###", '#', Items.stick, 'X', Blocks.wool);
		addRecipe(new ItemStack(Items.item_frame, 1),
				"###", "#X#", "###", '#', Items.stick, 'X', Items.leather);
		addRecipe(new ItemStack(Items.golden_apple, 1, 0),
				"###", "#X#", "###", '#', Items.gold_ingot, 'X', Items.apple);
		addRecipe(new ItemStack(Items.golden_apple, 1, 1),
				"###", "#X#", "###", '#', Blocks.gold_block, 'X', Items.apple);
		addRecipe(new ItemStack(Items.golden_carrot, 1, 0),
				"###", "#X#", "###", '#', Items.gold_nugget, 'X', Items.carrot);
		addRecipe(new ItemStack(Items.speckled_melon, 1),
				"###", "#X#", "###", '#', Items.gold_nugget, 'X', Items.melon);
		addRecipe(new ItemStack(Blocks.lever, 1), "X", "#", '#', Blocks.cobblestone, 'X', Items.stick);
		addRecipe(new ItemStack(Blocks.tripwire_hook, 2),
				"I", "S", "#", '#', Blocks.planks, 'S', Items.stick, 'I', Items.iron_ingot);
		addRecipe(new ItemStack(Blocks.redstone_torch, 1),
				"X", "#", '#', Items.stick, 'X', Items.redstone);
		addRecipe(new ItemStack(Items.repeater, 1),
				"#X#", "III", '#', Blocks.redstone_torch, 'X', Items.redstone, 'I', Blocks.stone);
		addRecipe(new ItemStack(Items.comparator, 1),
				" # ", "#X#", "III", '#', Blocks.redstone_torch, 'X', Items.quartz, 'I', Blocks.stone);
		addRecipe(new ItemStack(Items.clock, 1),
				" # ", "#X#", " # ", '#', Items.gold_ingot, 'X', Items.redstone);
		addRecipe(new ItemStack(Items.compass, 1),
				" # ", "#X#", " # ", '#', Items.iron_ingot, 'X', Items.redstone);
		addRecipe(new ItemStack(Items.map, 1),
				"###", "#X#", "###", '#', Items.paper, 'X', Items.compass);
		addRecipe(new ItemStack(Blocks.stone_button, 1), "#", '#', Blocks.stone);
		addRecipe(new ItemStack(Blocks.wooden_button, 1), "#", '#', Blocks.planks);
		addRecipe(new ItemStack(Blocks.stone_pressure_plate, 1), "##", '#', Blocks.stone);
		addRecipe(new ItemStack(Blocks.wooden_pressure_plate, 1), "##", '#', Blocks.planks);
		addRecipe(new ItemStack(Blocks.heavy_weighted_pressure_plate, 1), "##", '#', Items.iron_ingot);
		addRecipe(new ItemStack(Blocks.light_weighted_pressure_plate, 1), "##", '#', Items.gold_ingot);
		addRecipe(new ItemStack(Blocks.dispenser, 1),
				"###", "#X#", "#R#", '#', Blocks.cobblestone, 'X', Items.bow, 'R', Items.redstone);
		addRecipe(new ItemStack(Blocks.dropper, 1),
				"###", "# #", "#R#", '#', Blocks.cobblestone, 'R', Items.redstone);
		addRecipe(new ItemStack(Blocks.piston, 1), "TTT", "#X#", "#R#", '#', Blocks.cobblestone, 'X',
				Items.iron_ingot, 'R', Items.redstone, 'T', Blocks.planks);
		addRecipe(new ItemStack(Blocks.sticky_piston, 1),
				"S", "P", 'S', Items.slime_ball, 'P', Blocks.piston);
		addRecipe(new ItemStack(Items.bed, 1), "###", "XXX", '#', Blocks.wool, 'X', Blocks.planks);
		addRecipe(new ItemStack(Blocks.enchanting_table, 1),
				" B ", "D#D", "###", '#', Blocks.obsidian, 'B', Items.book, 'D', Items.diamond);
		addRecipe(new ItemStack(Blocks.anvil, 1),
				"III", " i ", "iii", 'I', Blocks.iron_block, 'i', Items.iron_ingot);
		addShapelessRecipe(new ItemStack(Items.ender_eye, 1), Items.ender_pearl, Items.blaze_powder);
		addShapelessRecipe(new ItemStack(Items.fire_charge, 3),
				Items.gunpowder, Items.blaze_powder, Items.coal);
		addShapelessRecipe(new ItemStack(Items.fire_charge, 3),
				Items.gunpowder, Items.blaze_powder, new ItemStack(Items.coal, 1, 1));
		addRecipe(new ItemStack(Blocks.daylight_detector),
				"GGG", "QQQ", "WWW", 'G', Blocks.glass, 'Q', Items.quartz, 'W', Blocks.wooden_slab);
		addRecipe(new ItemStack(Blocks.hopper),
				"I I", "ICI", " I ", 'I', Items.iron_ingot, 'C', Blocks.chest);
		recipes.sort(new Comparator() {
			private static final String __OBFID = "CL_00000091";

			public int compare(IRecipe p_compare_1_, IRecipe p_compare_2_) {
				return p_compare_1_ instanceof ShapelessRecipes && p_compare_2_ instanceof ShapedRecipes ? 1
						: p_compare_2_ instanceof ShapelessRecipes && p_compare_1_ instanceof ShapedRecipes ? -1
						: Integer.compare(p_compare_2_.getRecipeSize(), p_compare_1_.getRecipeSize());
			}

			@Override
			public int compare(Object p_compare_1_, Object p_compare_2_) {
				return this.compare((IRecipe) p_compare_1_, (IRecipe) p_compare_2_);
			}
		});
	}

	public ShapedRecipes addRecipe(ItemStack p_92103_1_, Object... p_92103_2_) {
		String s = "";
		int i = 0;
		int j = 0;
		int k = 0;

		if (p_92103_2_[i] instanceof String[]) {
			String[] astring = (String[]) p_92103_2_[i++];

			for (String s1 : astring) {
				++k;
				j = s1.length();
				s = s + s1;
			}
		} else {
			while (p_92103_2_[i] instanceof String) {
				String s2 = (String) p_92103_2_[i++];
				++k;
				j = s2.length();
				s = s + s2;
			}
		}

		HashMap hashmap;

		for (hashmap = new HashMap(); i < p_92103_2_.length; i += 2) {
			Character character = (Character) p_92103_2_[i];
			ItemStack itemstack1 = null;

			if (p_92103_2_[i + 1] instanceof Item) {
				itemstack1 = new ItemStack((Item) p_92103_2_[i + 1]);
			} else if (p_92103_2_[i + 1] instanceof Block) {
				itemstack1 = new ItemStack((Block) p_92103_2_[i + 1], 1, 32767);
			} else if (p_92103_2_[i + 1] instanceof ItemStack) {
				itemstack1 = (ItemStack) p_92103_2_[i + 1];
			}

			hashmap.put(character, itemstack1);
		}

		ItemStack[] aitemstack = new ItemStack[j * k];

		for (int i1 = 0; i1 < j * k; ++i1) {
			char c0 = s.charAt(i1);

			if (hashmap.containsKey(c0)) {
				aitemstack[i1] = ((ItemStack) hashmap.get(c0)).copy();
			} else {
				aitemstack[i1] = null;
			}
		}

		ShapedRecipes shapedrecipes = new ShapedRecipes(j, k, aitemstack, p_92103_1_);
		recipes.add(shapedrecipes);
		return shapedrecipes;
	}

	public void addShapelessRecipe(ItemStack p_77596_1_, Object... p_77596_2_) {
		ArrayList arraylist = new ArrayList();
		Object[] aobject = p_77596_2_;
		int i = p_77596_2_.length;

		for (int j = 0; j < i; ++j) {
			Object object1 = aobject[j];

			if (object1 instanceof ItemStack) {
				arraylist.add(((ItemStack) object1).copy());
			} else if (object1 instanceof Item) {
				arraylist.add(new ItemStack((Item) object1));
			} else {
				if (!(object1 instanceof Block))
					throw new RuntimeException("Invalid shapeless recipy!");

				arraylist.add(new ItemStack((Block) object1));
			}
		}

		recipes.add(new ShapelessRecipes(p_77596_1_, arraylist));
	}

	public ItemStack findMatchingRecipe(InventoryCrafting p_82787_1_, World p_82787_2_) {
		int i = 0;
		ItemStack itemstack = null;
		ItemStack itemstack1 = null;
		int j;

		for (j = 0; j < p_82787_1_.getSizeInventory(); ++j) {
			ItemStack itemstack2 = p_82787_1_.getStackInSlot(j);

			if (itemstack2 != null) {
				if (i == 0) {
					itemstack = itemstack2;
				}

				if (i == 1) {
					itemstack1 = itemstack2;
				}

				++i;
			}
		}

		if (i == 2 && itemstack.getItem() == itemstack1.getItem() && itemstack.stackSize == 1
				&& itemstack1.stackSize == 1 && itemstack.getItem().isRepairable()) {
			Item item = itemstack.getItem();
			int j1 = item.getMaxDamage() - itemstack.getItemDamageForDisplay();
			int k = item.getMaxDamage() - itemstack1.getItemDamageForDisplay();
			int l = j1 + k + item.getMaxDamage() * 5 / 100;
			int i1 = item.getMaxDamage() - l;

			if (i1 < 0) {
				i1 = 0;
			}

			ItemStack result = new ItemStack(itemstack.getItem(), 1, i1);
			List<ItemStack> ingredients = new ArrayList<>();
			ingredients.add(itemstack.copy());
			ingredients.add(itemstack1.copy());
			ShapelessRecipes currentRecipe = new ShapelessRecipes(result.copy(), ingredients);
			p_82787_1_.setCurrentRecipe(currentRecipe);
			// TODO: callPreCraftEvent
			return result;
		} else {
			/*
			 * for (j = 0; j < this.recipes.size(); ++j) { IRecipe irecipe =
			 * (IRecipe)this.recipes.get(j);
			 *
			 * if (irecipe.matches(p_82787_1_, p_82787_2_)) { return
			 * irecipe.getCraftingResult(p_82787_1_); } }
			 */

			IRecipe recipe = UltramineServerModContainer.getInstance().getRecipeCache().findRecipe(p_82787_1_,
					p_82787_2_);

			if (recipe != null) {
				if (recipe.matches(p_82787_1_, p_82787_2_)) {
					p_82787_1_.setCurrentRecipe(recipe);
				}
			}

			return recipe == null ? null : recipe.getCraftingResult(p_82787_1_);

		}
	}

	public List getRecipeList() {
		return recipes;
	}
}