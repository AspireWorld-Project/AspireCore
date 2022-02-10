package net.minecraft.world.gen.structure;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;

public class MapGenNetherBridge extends MapGenStructure {
	private List spawnList = new ArrayList();
	private static final String __OBFID = "CL_00000451";

	public MapGenNetherBridge() {
		spawnList.add(new BiomeGenBase.SpawnListEntry(EntityBlaze.class, 10, 2, 3));
		spawnList.add(new BiomeGenBase.SpawnListEntry(EntityPigZombie.class, 5, 4, 4));
		spawnList.add(new BiomeGenBase.SpawnListEntry(EntitySkeleton.class, 10, 4, 4));
		spawnList.add(new BiomeGenBase.SpawnListEntry(EntityMagmaCube.class, 3, 4, 4));
	}

	@Override
	public String func_143025_a() {
		return "Fortress";
	}

	public List getSpawnList() {
		return spawnList;
	}

	@Override
	protected boolean canSpawnStructureAtCoords(int p_75047_1_, int p_75047_2_) {
		int k = p_75047_1_ >> 4;
		int l = p_75047_2_ >> 4;
		rand.setSeed(k ^ l << 4 ^ worldObj.getSeed());
		rand.nextInt();
		return rand.nextInt(3) != 0 ? false
				: p_75047_1_ != (k << 4) + 4 + rand.nextInt(8) ? false : p_75047_2_ == (l << 4) + 4 + rand.nextInt(8);
	}

	@Override
	protected StructureStart getStructureStart(int p_75049_1_, int p_75049_2_) {
		return new MapGenNetherBridge.Start(worldObj, rand, p_75049_1_, p_75049_2_);
	}

	public static class Start extends StructureStart {
		private static final String __OBFID = "CL_00000452";

		public Start() {
		}

		public Start(World p_i2040_1_, Random p_i2040_2_, int p_i2040_3_, int p_i2040_4_) {
			super(p_i2040_3_, p_i2040_4_);
			StructureNetherBridgePieces.Start start = new StructureNetherBridgePieces.Start(p_i2040_2_,
					(p_i2040_3_ << 4) + 2, (p_i2040_4_ << 4) + 2);
			components.add(start);
			start.buildComponent(start, components, p_i2040_2_);
			ArrayList arraylist = start.field_74967_d;

			while (!arraylist.isEmpty()) {
				int k = p_i2040_2_.nextInt(arraylist.size());
				StructureComponent structurecomponent = (StructureComponent) arraylist.remove(k);
				structurecomponent.buildComponent(start, components, p_i2040_2_);
			}

			updateBoundingBox();
			setRandomHeight(p_i2040_1_, p_i2040_2_, 48, 70);
		}
	}
}