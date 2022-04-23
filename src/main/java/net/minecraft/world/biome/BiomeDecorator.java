package net.minecraft.world.biome;

import net.minecraft.block.BlockFlower;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;
import net.minecraftforge.event.terraingen.OreGenEvent;
import net.minecraftforge.event.terraingen.TerrainGen;

import java.util.Random;

import static net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.*;
import static net.minecraftforge.event.terraingen.OreGenEvent.GenerateMinable.EventType.*;

public class BiomeDecorator {
	public World currentWorld;
	public Random randomGenerator;
	public int chunk_X;
	public int chunk_Z;
	public WorldGenerator clayGen = new WorldGenClay(4);
	public WorldGenerator sandGen;
	public WorldGenerator gravelAsSandGen;
	public WorldGenerator dirtGen;
	public WorldGenerator gravelGen;
	public WorldGenerator coalGen;
	public WorldGenerator ironGen;
	public WorldGenerator goldGen;
	public WorldGenerator redstoneGen;
	public WorldGenerator diamondGen;
	public WorldGenerator lapisGen;
	public WorldGenFlowers yellowFlowerGen;
	public WorldGenerator mushroomBrownGen;
	public WorldGenerator mushroomRedGen;
	public WorldGenerator bigMushroomGen;
	public WorldGenerator reedGen;
	public WorldGenerator cactusGen;
	public WorldGenerator waterlilyGen;
	public int waterlilyPerChunk;
	public int treesPerChunk;
	public int flowersPerChunk;
	public int grassPerChunk;
	public int deadBushPerChunk;
	public int mushroomsPerChunk;
	public int reedsPerChunk;
	public int cactiPerChunk;
	public int sandPerChunk;
	public int sandPerChunk2;
	public int clayPerChunk;
	public int bigMushroomsPerChunk;
	public boolean generateLakes;
	private static final String __OBFID = "CL_00000164";

	public BiomeDecorator() {
		sandGen = new WorldGenSand(Blocks.sand, 7);
		gravelAsSandGen = new WorldGenSand(Blocks.gravel, 6);
		dirtGen = new WorldGenMinable(Blocks.dirt, 32);
		gravelGen = new WorldGenMinable(Blocks.gravel, 32);
		coalGen = new WorldGenMinable(Blocks.coal_ore, 16);
		ironGen = new WorldGenMinable(Blocks.iron_ore, 8);
		goldGen = new WorldGenMinable(Blocks.gold_ore, 8);
		redstoneGen = new WorldGenMinable(Blocks.redstone_ore, 7);
		diamondGen = new WorldGenMinable(Blocks.diamond_ore, 7);
		lapisGen = new WorldGenMinable(Blocks.lapis_ore, 6);
		yellowFlowerGen = new WorldGenFlowers(Blocks.yellow_flower);
		mushroomBrownGen = new WorldGenFlowers(Blocks.brown_mushroom);
		mushroomRedGen = new WorldGenFlowers(Blocks.red_mushroom);
		bigMushroomGen = new WorldGenBigMushroom();
		reedGen = new WorldGenReed();
		cactusGen = new WorldGenCactus();
		waterlilyGen = new WorldGenWaterlily();
		flowersPerChunk = 2;
		grassPerChunk = 1;
		sandPerChunk = 1;
		sandPerChunk2 = 3;
		clayPerChunk = 1;
		generateLakes = true;
	}

	public void decorateChunk(World p_150512_1_, Random p_150512_2_, BiomeGenBase p_150512_3_, int p_150512_4_,
			int p_150512_5_) {
		if (currentWorld != null)
			throw new RuntimeException("Already decorating!!");
		else {
			currentWorld = p_150512_1_;
			randomGenerator = p_150512_2_;
			chunk_X = p_150512_4_;
			chunk_Z = p_150512_5_;
			genDecorations(p_150512_3_);
			currentWorld = null;
			randomGenerator = null;
		}
	}

	protected void genDecorations(BiomeGenBase p_150513_1_) {
		MinecraftForge.EVENT_BUS.post(new DecorateBiomeEvent.Pre(currentWorld, randomGenerator, chunk_X, chunk_Z));
		generateOres();
		int i;
		int j;
		int k;

		boolean doGen = TerrainGen.decorate(currentWorld, randomGenerator, chunk_X, chunk_Z, SAND);
		for (i = 0; doGen && i < sandPerChunk2; ++i) {
			j = chunk_X + randomGenerator.nextInt(16) + 8;
			k = chunk_Z + randomGenerator.nextInt(16) + 8;
			sandGen.generate(currentWorld, randomGenerator, j, currentWorld.getTopSolidOrLiquidBlock(j, k), k);
		}

		doGen = TerrainGen.decorate(currentWorld, randomGenerator, chunk_X, chunk_Z, CLAY);
		for (i = 0; doGen && i < clayPerChunk; ++i) {
			j = chunk_X + randomGenerator.nextInt(16) + 8;
			k = chunk_Z + randomGenerator.nextInt(16) + 8;
			clayGen.generate(currentWorld, randomGenerator, j, currentWorld.getTopSolidOrLiquidBlock(j, k), k);
		}

		doGen = TerrainGen.decorate(currentWorld, randomGenerator, chunk_X, chunk_Z, SAND_PASS2);
		for (i = 0; doGen && i < sandPerChunk; ++i) {
			j = chunk_X + randomGenerator.nextInt(16) + 8;
			k = chunk_Z + randomGenerator.nextInt(16) + 8;
			gravelAsSandGen.generate(currentWorld, randomGenerator, j, currentWorld.getTopSolidOrLiquidBlock(j, k), k);
		}

		i = treesPerChunk;

		if (randomGenerator.nextInt(10) == 0) {
			++i;
		}

		int l;
		int i1;

		doGen = TerrainGen.decorate(currentWorld, randomGenerator, chunk_X, chunk_Z, TREE);
		for (j = 0; doGen && j < i; ++j) {
			k = chunk_X + randomGenerator.nextInt(16) + 8;
			l = chunk_Z + randomGenerator.nextInt(16) + 8;
			i1 = currentWorld.getHeightValue(k, l);
			WorldGenAbstractTree worldgenabstracttree = p_150513_1_.func_150567_a(randomGenerator);
			worldgenabstracttree.setScale(1.0D, 1.0D, 1.0D);

			if (worldgenabstracttree.generate(currentWorld, randomGenerator, k, i1, l)) {
				worldgenabstracttree.func_150524_b(currentWorld, randomGenerator, k, i1, l);
			}
		}

		doGen = TerrainGen.decorate(currentWorld, randomGenerator, chunk_X, chunk_Z, BIG_SHROOM);
		for (j = 0; doGen && j < bigMushroomsPerChunk; ++j) {
			k = chunk_X + randomGenerator.nextInt(16) + 8;
			l = chunk_Z + randomGenerator.nextInt(16) + 8;
			bigMushroomGen.generate(currentWorld, randomGenerator, k, currentWorld.getHeightValue(k, l), l);
		}

		doGen = TerrainGen.decorate(currentWorld, randomGenerator, chunk_X, chunk_Z, FLOWERS);
		for (j = 0; doGen && j < flowersPerChunk; ++j) {
			k = chunk_X + randomGenerator.nextInt(16) + 8;
			l = chunk_Z + randomGenerator.nextInt(16) + 8;
			i1 = nextInt(currentWorld.getHeightValue(k, l) + 32);
			String s = p_150513_1_.func_150572_a(randomGenerator, k, i1, l);
			BlockFlower blockflower = BlockFlower.func_149857_e(s);

			if (blockflower.getMaterial() != Material.air) {
				yellowFlowerGen.func_150550_a(blockflower, BlockFlower.func_149856_f(s));
				yellowFlowerGen.generate(currentWorld, randomGenerator, k, i1, l);
			}
		}

		doGen = TerrainGen.decorate(currentWorld, randomGenerator, chunk_X, chunk_Z, GRASS);
		for (j = 0; doGen && j < grassPerChunk; ++j) {
			k = chunk_X + randomGenerator.nextInt(16) + 8;
			l = chunk_Z + randomGenerator.nextInt(16) + 8;
			i1 = nextInt(currentWorld.getHeightValue(k, l) * 2);
			WorldGenerator worldgenerator = p_150513_1_.getRandomWorldGenForGrass(randomGenerator);
			worldgenerator.generate(currentWorld, randomGenerator, k, i1, l);
		}

		doGen = TerrainGen.decorate(currentWorld, randomGenerator, chunk_X, chunk_Z, DEAD_BUSH);
		for (j = 0; doGen && j < deadBushPerChunk; ++j) {
			k = chunk_X + randomGenerator.nextInt(16) + 8;
			l = chunk_Z + randomGenerator.nextInt(16) + 8;
			i1 = nextInt(currentWorld.getHeightValue(k, l) * 2);
			new WorldGenDeadBush(Blocks.deadbush).generate(currentWorld, randomGenerator, k, i1, l);
		}

		doGen = TerrainGen.decorate(currentWorld, randomGenerator, chunk_X, chunk_Z, LILYPAD);
		for (j = 0; doGen && j < waterlilyPerChunk; ++j) {
			k = chunk_X + randomGenerator.nextInt(16) + 8;
			l = chunk_Z + randomGenerator.nextInt(16) + 8;

			for (i1 = nextInt(currentWorld.getHeightValue(k, l) * 2); i1 > 0
					&& currentWorld.isAirBlock(k, i1 - 1, l); --i1) {
				;
			}

			waterlilyGen.generate(currentWorld, randomGenerator, k, i1, l);
		}

		doGen = TerrainGen.decorate(currentWorld, randomGenerator, chunk_X, chunk_Z, SHROOM);
		for (j = 0; doGen && j < mushroomsPerChunk; ++j) {
			if (randomGenerator.nextInt(4) == 0) {
				k = chunk_X + randomGenerator.nextInt(16) + 8;
				l = chunk_Z + randomGenerator.nextInt(16) + 8;
				i1 = currentWorld.getHeightValue(k, l);
				mushroomBrownGen.generate(currentWorld, randomGenerator, k, i1, l);
			}

			if (randomGenerator.nextInt(8) == 0) {
				k = chunk_X + randomGenerator.nextInt(16) + 8;
				l = chunk_Z + randomGenerator.nextInt(16) + 8;
				i1 = nextInt(currentWorld.getHeightValue(k, l) * 2);
				mushroomRedGen.generate(currentWorld, randomGenerator, k, i1, l);
			}
		}

		if (doGen && randomGenerator.nextInt(4) == 0) {
			j = chunk_X + randomGenerator.nextInt(16) + 8;
			k = chunk_Z + randomGenerator.nextInt(16) + 8;
			l = nextInt(currentWorld.getHeightValue(j, k) * 2);
			mushroomBrownGen.generate(currentWorld, randomGenerator, j, l, k);
		}

		if (doGen && randomGenerator.nextInt(8) == 0) {
			j = chunk_X + randomGenerator.nextInt(16) + 8;
			k = chunk_Z + randomGenerator.nextInt(16) + 8;
			l = nextInt(currentWorld.getHeightValue(j, k) * 2);
			mushroomRedGen.generate(currentWorld, randomGenerator, j, l, k);
		}

		doGen = TerrainGen.decorate(currentWorld, randomGenerator, chunk_X, chunk_Z, REED);
		for (j = 0; doGen && j < reedsPerChunk; ++j) {
			k = chunk_X + randomGenerator.nextInt(16) + 8;
			l = chunk_Z + randomGenerator.nextInt(16) + 8;
			i1 = nextInt(currentWorld.getHeightValue(k, l) * 2);
			reedGen.generate(currentWorld, randomGenerator, k, i1, l);
		}

		for (j = 0; doGen && j < 10; ++j) {
			k = chunk_X + randomGenerator.nextInt(16) + 8;
			l = chunk_Z + randomGenerator.nextInt(16) + 8;
			i1 = nextInt(currentWorld.getHeightValue(k, l) * 2);
			reedGen.generate(currentWorld, randomGenerator, k, i1, l);
		}

		doGen = TerrainGen.decorate(currentWorld, randomGenerator, chunk_X, chunk_Z, PUMPKIN);
		if (doGen && randomGenerator.nextInt(32) == 0) {
			j = chunk_X + randomGenerator.nextInt(16) + 8;
			k = chunk_Z + randomGenerator.nextInt(16) + 8;
			l = nextInt(currentWorld.getHeightValue(j, k) * 2);
			new WorldGenPumpkin().generate(currentWorld, randomGenerator, j, l, k);
		}

		doGen = TerrainGen.decorate(currentWorld, randomGenerator, chunk_X, chunk_Z, CACTUS);
		for (j = 0; doGen && j < cactiPerChunk; ++j) {
			k = chunk_X + randomGenerator.nextInt(16) + 8;
			l = chunk_Z + randomGenerator.nextInt(16) + 8;
			i1 = nextInt(currentWorld.getHeightValue(k, l) * 2);
			cactusGen.generate(currentWorld, randomGenerator, k, i1, l);
		}

		doGen = TerrainGen.decorate(currentWorld, randomGenerator, chunk_X, chunk_Z, LAKE);
		if (doGen && generateLakes) {
			for (j = 0; j < 50; ++j) {
				k = chunk_X + randomGenerator.nextInt(16) + 8;
				l = randomGenerator.nextInt(randomGenerator.nextInt(248) + 8);
				i1 = chunk_Z + randomGenerator.nextInt(16) + 8;
				new WorldGenLiquids(Blocks.flowing_water).generate(currentWorld, randomGenerator, k, l, i1);
			}

			for (j = 0; j < 20; ++j) {
				k = chunk_X + randomGenerator.nextInt(16) + 8;
				l = randomGenerator.nextInt(randomGenerator.nextInt(randomGenerator.nextInt(240) + 8) + 8);
				i1 = chunk_Z + randomGenerator.nextInt(16) + 8;
				new WorldGenLiquids(Blocks.flowing_lava).generate(currentWorld, randomGenerator, k, l, i1);
			}
		}

		MinecraftForge.EVENT_BUS.post(new DecorateBiomeEvent.Post(currentWorld, randomGenerator, chunk_X, chunk_Z));
	}

	protected void genStandardOre1(int p_76795_1_, WorldGenerator p_76795_2_, int p_76795_3_, int p_76795_4_) {
		for (int l = 0; l < p_76795_1_; ++l) {
			int i1 = chunk_X + randomGenerator.nextInt(16);
			int j1 = randomGenerator.nextInt(p_76795_4_ - p_76795_3_) + p_76795_3_;
			int k1 = chunk_Z + randomGenerator.nextInt(16);
			p_76795_2_.generate(currentWorld, randomGenerator, i1, j1, k1);
		}
	}

	protected void genStandardOre2(int p_76793_1_, WorldGenerator p_76793_2_, int p_76793_3_, int p_76793_4_) {
		for (int l = 0; l < p_76793_1_; ++l) {
			int i1 = chunk_X + randomGenerator.nextInt(16);
			int j1 = randomGenerator.nextInt(p_76793_4_) + randomGenerator.nextInt(p_76793_4_) + p_76793_3_
					- p_76793_4_;
			int k1 = chunk_Z + randomGenerator.nextInt(16);
			p_76793_2_.generate(currentWorld, randomGenerator, i1, j1, k1);
		}
	}

	protected void generateOres() {
		MinecraftForge.ORE_GEN_BUS.post(new OreGenEvent.Pre(currentWorld, randomGenerator, chunk_X, chunk_Z));
		if (TerrainGen.generateOre(currentWorld, randomGenerator, dirtGen, chunk_X, chunk_Z, DIRT)) {
			genStandardOre1(20, dirtGen, 0, 256);
		}
		if (TerrainGen.generateOre(currentWorld, randomGenerator, gravelGen, chunk_X, chunk_Z, GRAVEL)) {
			genStandardOre1(10, gravelGen, 0, 256);
		}
		if (TerrainGen.generateOre(currentWorld, randomGenerator, coalGen, chunk_X, chunk_Z, COAL)) {
			genStandardOre1(20, coalGen, 0, 128);
		}
		if (TerrainGen.generateOre(currentWorld, randomGenerator, ironGen, chunk_X, chunk_Z, IRON)) {
			genStandardOre1(20, ironGen, 0, 64);
		}
		if (TerrainGen.generateOre(currentWorld, randomGenerator, goldGen, chunk_X, chunk_Z, GOLD)) {
			genStandardOre1(2, goldGen, 0, 32);
		}
		if (TerrainGen.generateOre(currentWorld, randomGenerator, redstoneGen, chunk_X, chunk_Z, REDSTONE)) {
			genStandardOre1(8, redstoneGen, 0, 16);
		}
		if (TerrainGen.generateOre(currentWorld, randomGenerator, diamondGen, chunk_X, chunk_Z, DIAMOND)) {
			genStandardOre1(1, diamondGen, 0, 16);
		}
		if (TerrainGen.generateOre(currentWorld, randomGenerator, lapisGen, chunk_X, chunk_Z, LAPIS)) {
			genStandardOre2(1, lapisGen, 16, 16);
		}
		MinecraftForge.ORE_GEN_BUS.post(new OreGenEvent.Post(currentWorld, randomGenerator, chunk_X, chunk_Z));
	}

	private int nextInt(int i) {
		if (i <= 1)
			return 0;
		return randomGenerator.nextInt(i);
	}
}