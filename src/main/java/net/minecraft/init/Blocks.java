package net.minecraft.init;

import net.minecraft.block.*;

public class Blocks {
	public static final Block air = (Block) Block.blockRegistry.getObject("air");
	public static final Block stone = (Block) Block.blockRegistry.getObject("stone");
	public static final BlockGrass grass = (BlockGrass) Block.blockRegistry.getObject("grass");
	public static final Block dirt = (Block) Block.blockRegistry.getObject("dirt");
	public static final Block cobblestone = (Block) Block.blockRegistry.getObject("cobblestone");
	public static final Block planks = (Block) Block.blockRegistry.getObject("planks");
	public static final Block sapling = (Block) Block.blockRegistry.getObject("sapling");
	public static final Block bedrock = (Block) Block.blockRegistry.getObject("bedrock");
	public static final BlockLiquid flowing_water = (BlockLiquid) Block.blockRegistry.getObject("flowing_water");
	public static final Block water = (Block) Block.blockRegistry.getObject("water");
	public static final BlockLiquid flowing_lava = (BlockLiquid) Block.blockRegistry.getObject("flowing_lava");
	public static final Block lava = (Block) Block.blockRegistry.getObject("lava");
	public static final BlockSand sand = (BlockSand) Block.blockRegistry.getObject("sand");
	public static final Block gravel = (Block) Block.blockRegistry.getObject("gravel");
	public static final Block gold_ore = (Block) Block.blockRegistry.getObject("gold_ore");
	public static final Block iron_ore = (Block) Block.blockRegistry.getObject("iron_ore");
	public static final Block coal_ore = (Block) Block.blockRegistry.getObject("coal_ore");
	public static final Block log = (Block) Block.blockRegistry.getObject("log");
	public static final Block log2 = (Block) Block.blockRegistry.getObject("log2");
	public static final BlockLeaves leaves = (BlockLeaves) Block.blockRegistry.getObject("leaves");
	public static final BlockLeaves leaves2 = (BlockLeaves) Block.blockRegistry.getObject("leaves2");
	public static final Block sponge = (Block) Block.blockRegistry.getObject("sponge");
	public static final Block glass = (Block) Block.blockRegistry.getObject("glass");
	public static final Block lapis_ore = (Block) Block.blockRegistry.getObject("lapis_ore");
	public static final Block lapis_block = (Block) Block.blockRegistry.getObject("lapis_block");
	public static final Block dispenser = (Block) Block.blockRegistry.getObject("dispenser");
	public static final Block sandstone = (Block) Block.blockRegistry.getObject("sandstone");
	public static final Block noteblock = (Block) Block.blockRegistry.getObject("noteblock");
	public static final Block bed = (Block) Block.blockRegistry.getObject("bed");
	public static final Block golden_rail = (Block) Block.blockRegistry.getObject("golden_rail");
	public static final Block detector_rail = (Block) Block.blockRegistry.getObject("detector_rail");
	public static final BlockPistonBase sticky_piston = (BlockPistonBase) Block.blockRegistry
			.getObject("sticky_piston");
	public static final Block web = (Block) Block.blockRegistry.getObject("web");
	public static final BlockTallGrass tallgrass = (BlockTallGrass) Block.blockRegistry.getObject("tallgrass");
	public static final BlockDeadBush deadbush = (BlockDeadBush) Block.blockRegistry.getObject("deadbush");
	public static final BlockPistonBase piston = (BlockPistonBase) Block.blockRegistry.getObject("piston");
	public static final BlockPistonExtension piston_head = (BlockPistonExtension) Block.blockRegistry
			.getObject("piston_head");
	public static final Block wool = (Block) Block.blockRegistry.getObject("wool");
	public static final BlockPistonMoving piston_extension = (BlockPistonMoving) Block.blockRegistry
			.getObject("piston_extension");
	public static final BlockFlower yellow_flower = (BlockFlower) Block.blockRegistry.getObject("yellow_flower");
	public static final BlockFlower red_flower = (BlockFlower) Block.blockRegistry.getObject("red_flower");
	public static final BlockBush brown_mushroom = (BlockBush) Block.blockRegistry.getObject("brown_mushroom");
	public static final BlockBush red_mushroom = (BlockBush) Block.blockRegistry.getObject("red_mushroom");
	public static final Block gold_block = (Block) Block.blockRegistry.getObject("gold_block");
	public static final Block iron_block = (Block) Block.blockRegistry.getObject("iron_block");
	public static final BlockSlab double_stone_slab = (BlockSlab) Block.blockRegistry.getObject("double_stone_slab");
	public static final BlockSlab stone_slab = (BlockSlab) Block.blockRegistry.getObject("stone_slab");
	public static final Block brick_block = (Block) Block.blockRegistry.getObject("brick_block");
	public static final Block tnt = (Block) Block.blockRegistry.getObject("tnt");
	public static final Block bookshelf = (Block) Block.blockRegistry.getObject("bookshelf");
	public static final Block mossy_cobblestone = (Block) Block.blockRegistry.getObject("mossy_cobblestone");
	public static final Block obsidian = (Block) Block.blockRegistry.getObject("obsidian");
	public static final Block torch = (Block) Block.blockRegistry.getObject("torch");
	public static final BlockFire fire = (BlockFire) Block.blockRegistry.getObject("fire");
	public static final Block mob_spawner = (Block) Block.blockRegistry.getObject("mob_spawner");
	public static final Block oak_stairs = (Block) Block.blockRegistry.getObject("oak_stairs");
	public static final BlockChest chest = (BlockChest) Block.blockRegistry.getObject("chest");
	public static final BlockRedstoneWire redstone_wire = (BlockRedstoneWire) Block.blockRegistry
			.getObject("redstone_wire");
	public static final Block diamond_ore = (Block) Block.blockRegistry.getObject("diamond_ore");
	public static final Block diamond_block = (Block) Block.blockRegistry.getObject("diamond_block");
	public static final Block crafting_table = (Block) Block.blockRegistry.getObject("crafting_table");
	public static final Block wheat = (Block) Block.blockRegistry.getObject("wheat");
	public static final Block farmland = (Block) Block.blockRegistry.getObject("farmland");
	public static final Block furnace = (Block) Block.blockRegistry.getObject("furnace");
	public static final Block lit_furnace = (Block) Block.blockRegistry.getObject("lit_furnace");
	public static final Block standing_sign = (Block) Block.blockRegistry.getObject("standing_sign");
	public static final Block wooden_door = (Block) Block.blockRegistry.getObject("wooden_door");
	public static final Block ladder = (Block) Block.blockRegistry.getObject("ladder");
	public static final Block rail = (Block) Block.blockRegistry.getObject("rail");
	public static final Block stone_stairs = (Block) Block.blockRegistry.getObject("stone_stairs");
	public static final Block wall_sign = (Block) Block.blockRegistry.getObject("wall_sign");
	public static final Block lever = (Block) Block.blockRegistry.getObject("lever");
	public static final Block stone_pressure_plate = (Block) Block.blockRegistry.getObject("stone_pressure_plate");
	public static final Block iron_door = (Block) Block.blockRegistry.getObject("iron_door");
	public static final Block wooden_pressure_plate = (Block) Block.blockRegistry.getObject("wooden_pressure_plate");
	public static final Block redstone_ore = (Block) Block.blockRegistry.getObject("redstone_ore");
	public static final Block lit_redstone_ore = (Block) Block.blockRegistry.getObject("lit_redstone_ore");
	public static final Block unlit_redstone_torch = (Block) Block.blockRegistry.getObject("unlit_redstone_torch");
	public static final Block redstone_torch = (Block) Block.blockRegistry.getObject("redstone_torch");
	public static final Block stone_button = (Block) Block.blockRegistry.getObject("stone_button");
	public static final Block snow_layer = (Block) Block.blockRegistry.getObject("snow_layer");
	public static final Block ice = (Block) Block.blockRegistry.getObject("ice");
	public static final Block snow = (Block) Block.blockRegistry.getObject("snow");
	public static final Block cactus = (Block) Block.blockRegistry.getObject("cactus");
	public static final Block clay = (Block) Block.blockRegistry.getObject("clay");
	public static final Block reeds = (Block) Block.blockRegistry.getObject("reeds");
	public static final Block jukebox = (Block) Block.blockRegistry.getObject("jukebox");
	public static final Block fence = (Block) Block.blockRegistry.getObject("fence");
	public static final Block pumpkin = (Block) Block.blockRegistry.getObject("pumpkin");
	public static final Block netherrack = (Block) Block.blockRegistry.getObject("netherrack");
	public static final Block soul_sand = (Block) Block.blockRegistry.getObject("soul_sand");
	public static final Block glowstone = (Block) Block.blockRegistry.getObject("glowstone");
	public static final BlockPortal portal = (BlockPortal) Block.blockRegistry.getObject("portal");
	public static final Block lit_pumpkin = (Block) Block.blockRegistry.getObject("lit_pumpkin");
	public static final Block cake = (Block) Block.blockRegistry.getObject("cake");
	public static final BlockRedstoneRepeater unpowered_repeater = (BlockRedstoneRepeater) Block.blockRegistry
			.getObject("unpowered_repeater");
	public static final BlockRedstoneRepeater powered_repeater = (BlockRedstoneRepeater) Block.blockRegistry
			.getObject("powered_repeater");
	public static final Block trapdoor = (Block) Block.blockRegistry.getObject("trapdoor");
	public static final Block monster_egg = (Block) Block.blockRegistry.getObject("monster_egg");
	public static final Block stonebrick = (Block) Block.blockRegistry.getObject("stonebrick");
	public static final Block brown_mushroom_block = (Block) Block.blockRegistry.getObject("brown_mushroom_block");
	public static final Block red_mushroom_block = (Block) Block.blockRegistry.getObject("red_mushroom_block");
	public static final Block iron_bars = (Block) Block.blockRegistry.getObject("iron_bars");
	public static final Block glass_pane = (Block) Block.blockRegistry.getObject("glass_pane");
	public static final Block melon_block = (Block) Block.blockRegistry.getObject("melon_block");
	public static final Block pumpkin_stem = (Block) Block.blockRegistry.getObject("pumpkin_stem");
	public static final Block melon_stem = (Block) Block.blockRegistry.getObject("melon_stem");
	public static final Block vine = (Block) Block.blockRegistry.getObject("vine");
	public static final Block fence_gate = (Block) Block.blockRegistry.getObject("fence_gate");
	public static final Block brick_stairs = (Block) Block.blockRegistry.getObject("brick_stairs");
	public static final Block stone_brick_stairs = (Block) Block.blockRegistry.getObject("stone_brick_stairs");
	public static final BlockMycelium mycelium = (BlockMycelium) Block.blockRegistry.getObject("mycelium");
	public static final Block waterlily = (Block) Block.blockRegistry.getObject("waterlily");
	public static final Block nether_brick = (Block) Block.blockRegistry.getObject("nether_brick");
	public static final Block nether_brick_fence = (Block) Block.blockRegistry.getObject("nether_brick_fence");
	public static final Block nether_brick_stairs = (Block) Block.blockRegistry.getObject("nether_brick_stairs");
	public static final Block nether_wart = (Block) Block.blockRegistry.getObject("nether_wart");
	public static final Block enchanting_table = (Block) Block.blockRegistry.getObject("enchanting_table");
	public static final Block brewing_stand = (Block) Block.blockRegistry.getObject("brewing_stand");
	public static final BlockCauldron cauldron = (BlockCauldron) Block.blockRegistry.getObject("cauldron");
	public static final Block end_portal = (Block) Block.blockRegistry.getObject("end_portal");
	public static final Block end_portal_frame = (Block) Block.blockRegistry.getObject("end_portal_frame");
	public static final Block end_stone = (Block) Block.blockRegistry.getObject("end_stone");
	public static final Block dragon_egg = (Block) Block.blockRegistry.getObject("dragon_egg");
	public static final Block redstone_lamp = (Block) Block.blockRegistry.getObject("redstone_lamp");
	public static final Block lit_redstone_lamp = (Block) Block.blockRegistry.getObject("lit_redstone_lamp");
	public static final BlockSlab double_wooden_slab = (BlockSlab) Block.blockRegistry.getObject("double_wooden_slab");
	public static final BlockSlab wooden_slab = (BlockSlab) Block.blockRegistry.getObject("wooden_slab");
	public static final Block cocoa = (Block) Block.blockRegistry.getObject("cocoa");
	public static final Block sandstone_stairs = (Block) Block.blockRegistry.getObject("sandstone_stairs");
	public static final Block emerald_ore = (Block) Block.blockRegistry.getObject("emerald_ore");
	public static final Block ender_chest = (Block) Block.blockRegistry.getObject("ender_chest");
	public static final BlockTripWireHook tripwire_hook = (BlockTripWireHook) Block.blockRegistry
			.getObject("tripwire_hook");
	public static final Block tripwire = (Block) Block.blockRegistry.getObject("tripwire");
	public static final Block emerald_block = (Block) Block.blockRegistry.getObject("emerald_block");
	public static final Block spruce_stairs = (Block) Block.blockRegistry.getObject("spruce_stairs");
	public static final Block birch_stairs = (Block) Block.blockRegistry.getObject("birch_stairs");
	public static final Block jungle_stairs = (Block) Block.blockRegistry.getObject("jungle_stairs");
	public static final Block command_block = (Block) Block.blockRegistry.getObject("command_block");
	public static final BlockBeacon beacon = (BlockBeacon) Block.blockRegistry.getObject("beacon");
	public static final Block cobblestone_wall = (Block) Block.blockRegistry.getObject("cobblestone_wall");
	public static final Block flower_pot = (Block) Block.blockRegistry.getObject("flower_pot");
	public static final Block carrots = (Block) Block.blockRegistry.getObject("carrots");
	public static final Block potatoes = (Block) Block.blockRegistry.getObject("potatoes");
	public static final Block wooden_button = (Block) Block.blockRegistry.getObject("wooden_button");
	public static final Block skull = (Block) Block.blockRegistry.getObject("skull");
	public static final Block anvil = (Block) Block.blockRegistry.getObject("anvil");
	public static final Block trapped_chest = (Block) Block.blockRegistry.getObject("trapped_chest");
	public static final Block light_weighted_pressure_plate = (Block) Block.blockRegistry
			.getObject("light_weighted_pressure_plate");
	public static final Block heavy_weighted_pressure_plate = (Block) Block.blockRegistry
			.getObject("heavy_weighted_pressure_plate");
	public static final BlockRedstoneComparator unpowered_comparator = (BlockRedstoneComparator) Block.blockRegistry
			.getObject("unpowered_comparator");
	public static final BlockRedstoneComparator powered_comparator = (BlockRedstoneComparator) Block.blockRegistry
			.getObject("powered_comparator");
	public static final BlockDaylightDetector daylight_detector = (BlockDaylightDetector) Block.blockRegistry
			.getObject("daylight_detector");
	public static final Block redstone_block = (Block) Block.blockRegistry.getObject("redstone_block");
	public static final Block quartz_ore = (Block) Block.blockRegistry.getObject("quartz_ore");
	public static final BlockHopper hopper = (BlockHopper) Block.blockRegistry.getObject("hopper");
	public static final Block quartz_block = (Block) Block.blockRegistry.getObject("quartz_block");
	public static final Block quartz_stairs = (Block) Block.blockRegistry.getObject("quartz_stairs");
	public static final Block activator_rail = (Block) Block.blockRegistry.getObject("activator_rail");
	public static final Block dropper = (Block) Block.blockRegistry.getObject("dropper");
	public static final Block stained_hardened_clay = (Block) Block.blockRegistry.getObject("stained_hardened_clay");
	public static final Block hay_block = (Block) Block.blockRegistry.getObject("hay_block");
	public static final Block carpet = (Block) Block.blockRegistry.getObject("carpet");
	public static final Block hardened_clay = (Block) Block.blockRegistry.getObject("hardened_clay");
	public static final Block coal_block = (Block) Block.blockRegistry.getObject("coal_block");
	public static final Block packed_ice = (Block) Block.blockRegistry.getObject("packed_ice");
	public static final Block acacia_stairs = (Block) Block.blockRegistry.getObject("acacia_stairs");
	public static final Block dark_oak_stairs = (Block) Block.blockRegistry.getObject("dark_oak_stairs");
	public static final BlockDoublePlant double_plant = (BlockDoublePlant) Block.blockRegistry
			.getObject("double_plant");
	public static final BlockStainedGlass stained_glass = (BlockStainedGlass) Block.blockRegistry
			.getObject("stained_glass");
	public static final BlockStainedGlassPane stained_glass_pane = (BlockStainedGlassPane) Block.blockRegistry
			.getObject("stained_glass_pane");
	private static final String __OBFID = "CL_00000204";
}