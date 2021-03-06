package net.minecraft.stats;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityList;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ChatComponentTranslation;

import java.util.*;

public class StatList {
	@SuppressWarnings("rawtypes")
	protected static Map oneShotStats = new HashMap();
	@SuppressWarnings("rawtypes")
	public static List allStats = new ArrayList();
	@SuppressWarnings("rawtypes")
	public static List generalStats = new ArrayList();
	@SuppressWarnings("rawtypes")
	public static List itemStats = new ArrayList();
	@SuppressWarnings("rawtypes")
	public static List objectMineStats = new ArrayList();
	public static StatBase leaveGameStat = new StatBasic("stat.leaveGame",
			new ChatComponentTranslation("stat.leaveGame")).initIndependentStat().registerStat();
	public static StatBase minutesPlayedStat = new StatBasic("stat.playOneMinute",
			new ChatComponentTranslation("stat.playOneMinute"), StatBase.timeStatType)
					.initIndependentStat().registerStat();
	public static StatBase distanceWalkedStat = new StatBasic("stat.walkOneCm",
			new ChatComponentTranslation("stat.walkOneCm"), StatBase.distanceStatType)
					.initIndependentStat().registerStat();
	public static StatBase distanceSwumStat = new StatBasic("stat.swimOneCm",
			new ChatComponentTranslation("stat.swimOneCm"), StatBase.distanceStatType)
					.initIndependentStat().registerStat();
	public static StatBase distanceFallenStat = new StatBasic("stat.fallOneCm",
			new ChatComponentTranslation("stat.fallOneCm"), StatBase.distanceStatType)
					.initIndependentStat().registerStat();
	public static StatBase distanceClimbedStat = new StatBasic("stat.climbOneCm",
			new ChatComponentTranslation("stat.climbOneCm"), StatBase.distanceStatType)
					.initIndependentStat().registerStat();
	public static StatBase distanceFlownStat = new StatBasic("stat.flyOneCm",
			new ChatComponentTranslation("stat.flyOneCm"), StatBase.distanceStatType)
					.initIndependentStat().registerStat();
	public static StatBase distanceDoveStat = new StatBasic("stat.diveOneCm",
			new ChatComponentTranslation("stat.diveOneCm"), StatBase.distanceStatType)
					.initIndependentStat().registerStat();
	public static StatBase distanceByMinecartStat = new StatBasic("stat.minecartOneCm",
			new ChatComponentTranslation("stat.minecartOneCm"), StatBase.distanceStatType)
					.initIndependentStat().registerStat();
	public static StatBase distanceByBoatStat = new StatBasic("stat.boatOneCm",
			new ChatComponentTranslation("stat.boatOneCm"), StatBase.distanceStatType)
					.initIndependentStat().registerStat();
	public static StatBase distanceByPigStat = new StatBasic("stat.pigOneCm",
			new ChatComponentTranslation("stat.pigOneCm"), StatBase.distanceStatType)
					.initIndependentStat().registerStat();
	public static StatBase field_151185_q = new StatBasic("stat.horseOneCm",
			new ChatComponentTranslation("stat.horseOneCm"), StatBase.distanceStatType)
					.initIndependentStat().registerStat();
	public static StatBase jumpStat = new StatBasic("stat.jump",
			new ChatComponentTranslation("stat.jump")).initIndependentStat().registerStat();
	public static StatBase dropStat = new StatBasic("stat.drop",
			new ChatComponentTranslation("stat.drop")).initIndependentStat().registerStat();
	public static StatBase damageDealtStat = new StatBasic("stat.damageDealt",
			new ChatComponentTranslation("stat.damageDealt"), StatBase.field_111202_k).registerStat();
	public static StatBase damageTakenStat = new StatBasic("stat.damageTaken",
			new ChatComponentTranslation("stat.damageTaken"), StatBase.field_111202_k).registerStat();
	public static StatBase deathsStat = new StatBasic("stat.deaths",
			new ChatComponentTranslation("stat.deaths")).registerStat();
	public static StatBase mobKillsStat = new StatBasic("stat.mobKills",
			new ChatComponentTranslation("stat.mobKills")).registerStat();
	public static StatBase field_151186_x = new StatBasic("stat.animalsBred",
			new ChatComponentTranslation("stat.animalsBred")).registerStat();
	public static StatBase playerKillsStat = new StatBasic("stat.playerKills",
			new ChatComponentTranslation("stat.playerKills")).registerStat();
	public static StatBase fishCaughtStat = new StatBasic("stat.fishCaught",
			new ChatComponentTranslation("stat.fishCaught")).registerStat();
	public static StatBase field_151183_A = new StatBasic("stat.junkFished",
			new ChatComponentTranslation("stat.junkFished")).registerStat();
	public static StatBase field_151184_B = new StatBasic("stat.treasureFished",
			new ChatComponentTranslation("stat.treasureFished")).registerStat();
	public static final StatBase[] mineBlockStatArray = new StatBase[4096];
	public static final StatBase[] objectCraftStats = new StatBase[32000];
	public static final StatBase[] objectUseStats = new StatBase[32000];
	public static final StatBase[] objectBreakStats = new StatBase[32000];
	public static void func_151178_a() {
		func_151181_c();
		initStats();
		func_151179_e();
		initCraftableStats();
		AchievementList.init();
		EntityList.func_151514_a();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static void initCraftableStats() {
		HashSet hashset = new HashSet();
		Iterator iterator = CraftingManager.getInstance().getRecipeList().iterator();

		while (iterator.hasNext()) {
			IRecipe irecipe = (IRecipe) iterator.next();

			if (irecipe.getRecipeOutput() != null) {
				hashset.add(irecipe.getRecipeOutput().getItem());
			}
		}

		iterator = FurnaceRecipes.smelting().getSmeltingList().values().iterator();

		while (iterator.hasNext()) {
			ItemStack itemstack = (ItemStack) iterator.next();
			hashset.add(itemstack.getItem());
		}

		iterator = hashset.iterator();

		while (iterator.hasNext()) {
			Item item = (Item) iterator.next();

			if (item != null) {
				int i = Item.getIdFromItem(item); // TODO: Hook FML's stat change event and re-assign these
				objectCraftStats[i] = new StatCrafting("stat.craftItem." + i, new ChatComponentTranslation(
						"stat.craftItem", new ItemStack(item).func_151000_E()), item).registerStat();
			}
		}

		replaceAllSimilarBlocks(objectCraftStats);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static void func_151181_c() {
		Iterator iterator = Block.blockRegistry.iterator();

		while (iterator.hasNext()) {
			Block block = (Block) iterator.next();

			if (Item.getItemFromBlock(block) != null) {
				int i = Block.getIdFromBlock(block); // TODO: Hook FML's stat change event and re-assign these

				if (block.getEnableStats()) {
					mineBlockStatArray[i] = new StatCrafting("stat.mineBlock." + i,
							new ChatComponentTranslation("stat.mineBlock",
									new ItemStack(block).func_151000_E()),
							Item.getItemFromBlock(block)).registerStat();
					objectMineStats.add(mineBlockStatArray[i]);
				}
			}
		}

		replaceAllSimilarBlocks(mineBlockStatArray);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static void initStats() {
		Iterator iterator = Item.itemRegistry.iterator();

		while (iterator.hasNext()) {
			Item item = (Item) iterator.next();

			if (item != null) {
				int i = Item.getIdFromItem(item); // TODO: Hook FML's stat change event and re-assign these
				objectUseStats[i] = new StatCrafting("stat.useItem." + i, new ChatComponentTranslation("stat.useItem",
						new ItemStack(item).func_151000_E()), item).registerStat();

				if (!(item instanceof ItemBlock)) {
					itemStats.add(objectUseStats[i]);
				}
			}
		}

		replaceAllSimilarBlocks(objectUseStats);
	}

	@SuppressWarnings("rawtypes")
	private static void func_151179_e() {
		Iterator iterator = Item.itemRegistry.iterator();

		while (iterator.hasNext()) {
			Item item = (Item) iterator.next();

			if (item != null) {
				int i = Item.getIdFromItem(item); // TODO: Hook FML's stat change event and re-assign these

				if (item.isDamageable()) {
					objectBreakStats[i] = new StatCrafting("stat.breakItem." + i, new ChatComponentTranslation(
							"stat.breakItem", new ItemStack(item).func_151000_E()), item)
									.registerStat();
				}
			}
		}

		replaceAllSimilarBlocks(objectBreakStats);
	}

	private static void replaceAllSimilarBlocks(StatBase[] p_75924_0_) {
		func_151180_a(p_75924_0_, Blocks.water, Blocks.flowing_water);
		func_151180_a(p_75924_0_, Blocks.lava, Blocks.flowing_lava);
		func_151180_a(p_75924_0_, Blocks.lit_pumpkin, Blocks.pumpkin);
		func_151180_a(p_75924_0_, Blocks.lit_furnace, Blocks.furnace);
		func_151180_a(p_75924_0_, Blocks.lit_redstone_ore, Blocks.redstone_ore);
		func_151180_a(p_75924_0_, Blocks.powered_repeater, Blocks.unpowered_repeater);
		func_151180_a(p_75924_0_, Blocks.powered_comparator, Blocks.unpowered_comparator);
		func_151180_a(p_75924_0_, Blocks.redstone_torch, Blocks.unlit_redstone_torch);
		func_151180_a(p_75924_0_, Blocks.lit_redstone_lamp, Blocks.redstone_lamp);
		func_151180_a(p_75924_0_, Blocks.red_mushroom, Blocks.brown_mushroom);
		func_151180_a(p_75924_0_, Blocks.double_stone_slab, Blocks.stone_slab);
		func_151180_a(p_75924_0_, Blocks.double_wooden_slab, Blocks.wooden_slab);
		func_151180_a(p_75924_0_, Blocks.grass, Blocks.dirt);
		func_151180_a(p_75924_0_, Blocks.farmland, Blocks.dirt);
	}

	private static void func_151180_a(StatBase[] p_151180_0_, Block p_151180_1_, Block p_151180_2_) {
		int i = Block.getIdFromBlock(p_151180_1_);
		int j = Block.getIdFromBlock(p_151180_2_);

		if (p_151180_0_[i] != null && p_151180_0_[j] == null) {
			p_151180_0_[j] = p_151180_0_[i];
		} else {
			allStats.remove(p_151180_0_[i]);
			objectMineStats.remove(p_151180_0_[i]);
			generalStats.remove(p_151180_0_[i]);
			p_151180_0_[i] = p_151180_0_[j];
		}
	}

	public static StatBase func_151182_a(EntityList.EntityEggInfo p_151182_0_) {
		String s = EntityList.getStringFromID(p_151182_0_.spawnedID);
		return s == null ? null
				: new StatBase("stat.killEntity." + s,
						new ChatComponentTranslation("stat.entityKill",
								new ChatComponentTranslation("entity." + s + ".name")))
										.registerStat();
	}

	public static StatBase func_151176_b(EntityList.EntityEggInfo p_151176_0_) {
		String s = EntityList.getStringFromID(p_151176_0_.spawnedID);
		return s == null ? null
				: new StatBase("stat.entityKilledBy." + s,
						new ChatComponentTranslation("stat.entityKilledBy",
								new ChatComponentTranslation("entity." + s + ".name")))
										.registerStat();
	}

	public static StatBase func_151177_a(String p_151177_0_) {
		return (StatBase) oneShotStats.get(p_151177_0_);
	}
}