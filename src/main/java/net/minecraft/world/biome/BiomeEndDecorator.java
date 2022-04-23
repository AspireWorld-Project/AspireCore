package net.minecraft.world.biome;

import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.init.Blocks;
import net.minecraft.world.gen.feature.WorldGenSpikes;
import net.minecraft.world.gen.feature.WorldGenerator;

public class BiomeEndDecorator extends BiomeDecorator {
	protected WorldGenerator spikeGen;
	public BiomeEndDecorator() {
		spikeGen = new WorldGenSpikes(Blocks.end_stone);
	}

	@Override
	protected void genDecorations(BiomeGenBase p_150513_1_) {
		generateOres();

		if (randomGenerator.nextInt(5) == 0) {
			int i = chunk_X + randomGenerator.nextInt(16) + 8;
			int j = chunk_Z + randomGenerator.nextInt(16) + 8;
			int k = currentWorld.getTopSolidOrLiquidBlock(i, j);
			spikeGen.generate(currentWorld, randomGenerator, i, k, j);
		}

		if (chunk_X == 0 && chunk_Z == 0) {
			EntityDragon entitydragon = new EntityDragon(currentWorld);
			entitydragon.setLocationAndAngles(0.0D, 128.0D, 0.0D, randomGenerator.nextFloat() * 360.0F, 0.0F);
			currentWorld.spawnEntityInWorld(entitydragon);
		}
	}
}