package net.minecraft.world.biome;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.material.Material;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

import java.util.Random;

public class BiomeGenSwamp extends BiomeGenBase {
	@SuppressWarnings("unchecked")
	protected BiomeGenSwamp(int p_i1988_1_) {
		super(p_i1988_1_);
		theBiomeDecorator.treesPerChunk = 2;
		theBiomeDecorator.flowersPerChunk = 1;
		theBiomeDecorator.deadBushPerChunk = 1;
		theBiomeDecorator.mushroomsPerChunk = 8;
		theBiomeDecorator.reedsPerChunk = 10;
		theBiomeDecorator.clayPerChunk = 1;
		theBiomeDecorator.waterlilyPerChunk = 4;
		theBiomeDecorator.sandPerChunk2 = 0;
		theBiomeDecorator.sandPerChunk = 0;
		theBiomeDecorator.grassPerChunk = 5;
		waterColorMultiplier = 14745518;
		spawnableMonsterList.add(new BiomeGenBase.SpawnListEntry(EntitySlime.class, 1, 1, 1));
		flowers.clear();
		addFlower(Blocks.red_flower, 1, 10);
	}

	@Override
	public WorldGenAbstractTree func_150567_a(Random p_150567_1_) {
		return worldGeneratorSwamp;
	}

	@Override
	public String func_150572_a(Random p_150572_1_, int p_150572_2_, int p_150572_3_, int p_150572_4_) {
		return BlockFlower.field_149859_a[1];
	}

	@Override
	public void genTerrainBlocks(World p_150573_1_, Random p_150573_2_, Block[] p_150573_3_, byte[] p_150573_4_,
			int p_150573_5_, int p_150573_6_, double p_150573_7_) {
		double d1 = plantNoise.func_151601_a(p_150573_5_ * 0.25D, p_150573_6_ * 0.25D);

		if (d1 > 0.0D) {
			int k = p_150573_5_ & 15;
			int l = p_150573_6_ & 15;
			int i1 = p_150573_3_.length / 256;

			for (int j1 = 255; j1 >= 0; --j1) {
				int k1 = (l * 16 + k) * i1 + j1;

				if (p_150573_3_[k1] == null || p_150573_3_[k1].getMaterial() != Material.air) {
					if (j1 == 62 && p_150573_3_[k1] != Blocks.water) {
						p_150573_3_[k1] = Blocks.water;

						if (d1 < 0.12D) {
							p_150573_3_[k1 + 1] = Blocks.waterlily;
						}
					}

					break;
				}
			}
		}

		genBiomeTerrain(p_150573_1_, p_150573_2_, p_150573_3_, p_150573_4_, p_150573_5_, p_150573_6_, p_150573_7_);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getBiomeGrassColor(int p_150558_1_, int p_150558_2_, int p_150558_3_) {
		double d0 = plantNoise.func_151601_a(p_150558_1_ * 0.0225D, p_150558_3_ * 0.0225D);
		return d0 < -0.1D ? 5011004 : 6975545;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getBiomeFoliageColor(int p_150571_1_, int p_150571_2_, int p_150571_3_) {
		return 6975545;
	}
}