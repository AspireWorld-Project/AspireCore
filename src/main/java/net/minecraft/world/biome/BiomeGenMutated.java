package net.minecraft.world.biome;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

import java.util.ArrayList;
import java.util.Random;

public class BiomeGenMutated extends BiomeGenBase {
	protected BiomeGenBase baseBiome;
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public BiomeGenMutated(int p_i45381_1_, BiomeGenBase p_i45381_2_) {
		super(p_i45381_1_);
		baseBiome = p_i45381_2_;
		func_150557_a(p_i45381_2_.color, true);
		biomeName = p_i45381_2_.biomeName + " M";
		topBlock = p_i45381_2_.topBlock;
		fillerBlock = p_i45381_2_.fillerBlock;
		field_76754_C = p_i45381_2_.field_76754_C;
		rootHeight = p_i45381_2_.rootHeight;
		heightVariation = p_i45381_2_.heightVariation;
		temperature = p_i45381_2_.temperature;
		rainfall = p_i45381_2_.rainfall;
		waterColorMultiplier = p_i45381_2_.waterColorMultiplier;
		enableSnow = p_i45381_2_.enableSnow;
		enableRain = p_i45381_2_.enableRain;
		spawnableCreatureList = new ArrayList(p_i45381_2_.spawnableCreatureList);
		spawnableMonsterList = new ArrayList(p_i45381_2_.spawnableMonsterList);
		spawnableCaveCreatureList = new ArrayList(p_i45381_2_.spawnableCaveCreatureList);
		spawnableWaterCreatureList = new ArrayList(p_i45381_2_.spawnableWaterCreatureList);
		temperature = p_i45381_2_.temperature;
		rainfall = p_i45381_2_.rainfall;
		rootHeight = p_i45381_2_.rootHeight + 0.1F;
		heightVariation = p_i45381_2_.heightVariation + 0.2F;
	}

	@Override
	public void decorate(World p_76728_1_, Random p_76728_2_, int p_76728_3_, int p_76728_4_) {
		baseBiome.theBiomeDecorator.decorateChunk(p_76728_1_, p_76728_2_, this, p_76728_3_, p_76728_4_);
	}

	@Override
	public void genTerrainBlocks(World p_150573_1_, Random p_150573_2_, Block[] p_150573_3_, byte[] p_150573_4_,
			int p_150573_5_, int p_150573_6_, double p_150573_7_) {
		baseBiome.genTerrainBlocks(p_150573_1_, p_150573_2_, p_150573_3_, p_150573_4_, p_150573_5_, p_150573_6_,
				p_150573_7_);
	}

	@Override
	public float getSpawningChance() {
		return baseBiome.getSpawningChance();
	}

	@Override
	public WorldGenAbstractTree func_150567_a(Random p_150567_1_) {
		return baseBiome.func_150567_a(p_150567_1_);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getBiomeFoliageColor(int p_150571_1_, int p_150571_2_, int p_150571_3_) {
		return baseBiome.getBiomeFoliageColor(p_150571_1_, p_150571_2_, p_150571_2_);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getBiomeGrassColor(int p_150558_1_, int p_150558_2_, int p_150558_3_) {
		return baseBiome.getBiomeGrassColor(p_150558_1_, p_150558_2_, p_150558_2_);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Class getBiomeClass() {
		return baseBiome.getBiomeClass();
	}

	@Override
	public boolean isEqualTo(BiomeGenBase p_150569_1_) {
		return baseBiome.isEqualTo(p_150569_1_);
	}

	@Override
	public BiomeGenBase.TempCategory getTempCategory() {
		return baseBiome.getTempCategory();
	}
}