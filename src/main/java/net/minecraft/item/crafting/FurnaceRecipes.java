package net.minecraft.item.crafting;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFishFood;
import net.minecraft.item.ItemStack;
import org.ultramine.server.util.ItemStackHashMap;

import java.util.Map;

public class FurnaceRecipes {
	private static final FurnaceRecipes smeltingBase = new FurnaceRecipes();
	private final Map smeltingList = new ItemStackHashMap<>();
	private final Map experienceList = new ItemStackHashMap<>();
	private static final String __OBFID = "CL_00000085";

	public static FurnaceRecipes smelting() {
		return smeltingBase;
	}

	private FurnaceRecipes() {
		func_151393_a(Blocks.iron_ore, new ItemStack(Items.iron_ingot), 0.7F);
		func_151393_a(Blocks.gold_ore, new ItemStack(Items.gold_ingot), 1.0F);
		func_151393_a(Blocks.diamond_ore, new ItemStack(Items.diamond), 1.0F);
		func_151393_a(Blocks.sand, new ItemStack(Blocks.glass), 0.1F);
		func_151396_a(Items.porkchop, new ItemStack(Items.cooked_porkchop), 0.35F);
		func_151396_a(Items.beef, new ItemStack(Items.cooked_beef), 0.35F);
		func_151396_a(Items.chicken, new ItemStack(Items.cooked_chicken), 0.35F);
		func_151393_a(Blocks.cobblestone, new ItemStack(Blocks.stone), 0.1F);
		func_151396_a(Items.clay_ball, new ItemStack(Items.brick), 0.3F);
		func_151393_a(Blocks.clay, new ItemStack(Blocks.hardened_clay), 0.35F);
		func_151393_a(Blocks.cactus, new ItemStack(Items.dye, 1, 2), 0.2F);
		func_151393_a(Blocks.log, new ItemStack(Items.coal, 1, 1), 0.15F);
		func_151393_a(Blocks.log2, new ItemStack(Items.coal, 1, 1), 0.15F);
		func_151393_a(Blocks.emerald_ore, new ItemStack(Items.emerald), 1.0F);
		func_151396_a(Items.potato, new ItemStack(Items.baked_potato), 0.35F);
		func_151393_a(Blocks.netherrack, new ItemStack(Items.netherbrick), 0.1F);
		ItemFishFood.FishType[] afishtype = ItemFishFood.FishType.values();
		int i = afishtype.length;

		for (int j = 0; j < i; ++j) {
			ItemFishFood.FishType fishtype = afishtype[j];

			if (fishtype.func_150973_i()) {
				func_151394_a(new ItemStack(Items.fish, 1, fishtype.func_150976_a()),
						new ItemStack(Items.cooked_fished, 1, fishtype.func_150976_a()), 0.35F);
			}
		}

		func_151393_a(Blocks.coal_ore, new ItemStack(Items.coal), 0.1F);
		func_151393_a(Blocks.redstone_ore, new ItemStack(Items.redstone), 0.7F);
		func_151393_a(Blocks.lapis_ore, new ItemStack(Items.dye, 1, 4), 0.2F);
		func_151393_a(Blocks.quartz_ore, new ItemStack(Items.quartz), 0.2F);
	}

	public void func_151393_a(Block p_151393_1_, ItemStack p_151393_2_, float p_151393_3_) {
		func_151396_a(Item.getItemFromBlock(p_151393_1_), p_151393_2_, p_151393_3_);
	}

	public void func_151396_a(Item p_151396_1_, ItemStack p_151396_2_, float p_151396_3_) {
		func_151394_a(new ItemStack(p_151396_1_, 1, 32767), p_151396_2_, p_151396_3_);
	}

	public void func_151394_a(ItemStack p_151394_1_, ItemStack p_151394_2_, float p_151394_3_) {
		smeltingList.put(p_151394_1_, p_151394_2_);
		experienceList.put(p_151394_2_, Float.valueOf(p_151394_3_));
	}

	public ItemStack getSmeltingResult(ItemStack p_151395_1_) {
		return (ItemStack) smeltingList.get(p_151395_1_);// It's ItemStackHashMap
		/*
		 * Iterator iterator = this.smeltingList.entrySet().iterator(); Entry entry;
		 *
		 * do { if (!iterator.hasNext()) { return null; }
		 *
		 * entry = (Entry)iterator.next(); } while (!this.func_151397_a(p_151395_1_,
		 * (ItemStack)entry.getKey()));
		 *
		 * return (ItemStack)entry.getValue();
		 */
	}

	private boolean func_151397_a(ItemStack p_151397_1_, ItemStack p_151397_2_) {
		return p_151397_2_.getItem() == p_151397_1_.getItem()
				&& (p_151397_2_.getItemDamage() == 32767 || p_151397_2_.getItemDamage() == p_151397_1_.getItemDamage());
	}

	public Map getSmeltingList() {
		return smeltingList;
	}

	public float func_151398_b(ItemStack p_151398_1_) {
		float ret = p_151398_1_.getItem().getSmeltingExperience(p_151398_1_);
		if (ret != -1)
			return ret;
		Float f = (Float) experienceList.get(p_151398_1_);// It's ItemStackHashMap
		return f == null ? 0.0F : f;
		/*
		 * Iterator iterator = this.experienceList.entrySet().iterator(); Entry entry;
		 *
		 * do { if (!iterator.hasNext()) { return 0.0F; }
		 *
		 * entry = (Entry)iterator.next(); } while (!this.func_151397_a(p_151398_1_,
		 * (ItemStack)entry.getKey()));
		 *
		 * return ((Float)entry.getValue()).floatValue();
		 */
	}
}