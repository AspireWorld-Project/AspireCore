package net.minecraft.world.gen.structure;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;

import java.util.*;
import java.util.Map.Entry;

public class MapGenVillage extends MapGenStructure {
	@SuppressWarnings("rawtypes")
	public static List villageSpawnBiomes = Arrays
			.asList(BiomeGenBase.plains, BiomeGenBase.desert, BiomeGenBase.savanna);
	private int terrainType;
	private int field_82665_g;
	private final int field_82666_h;
	public MapGenVillage() {
		field_82665_g = 32;
		field_82666_h = 8;
	}

	@SuppressWarnings("rawtypes")
	public MapGenVillage(Map p_i2093_1_) {
		this();
		Iterator iterator = p_i2093_1_.entrySet().iterator();

		while (iterator.hasNext()) {
			Entry entry = (Entry) iterator.next();

			if (entry.getKey().equals("size")) {
				terrainType = MathHelper.parseIntWithDefaultAndMax((String) entry.getValue(), terrainType, 0);
			} else if (entry.getKey().equals("distance")) {
				field_82665_g = MathHelper.parseIntWithDefaultAndMax((String) entry.getValue(), field_82665_g,
						field_82666_h + 1);
			}
		}
	}

	@Override
	public String func_143025_a() {
		return "Village";
	}

	@Override
	protected boolean canSpawnStructureAtCoords(int p_75047_1_, int p_75047_2_) {
		int k = p_75047_1_;
		int l = p_75047_2_;

		if (p_75047_1_ < 0) {
			p_75047_1_ -= field_82665_g - 1;
		}

		if (p_75047_2_ < 0) {
			p_75047_2_ -= field_82665_g - 1;
		}

		int i1 = p_75047_1_ / field_82665_g;
		int j1 = p_75047_2_ / field_82665_g;
		Random random = worldObj.setRandomSeed(i1, j1, 10387312);
		i1 *= field_82665_g;
		j1 *= field_82665_g;
		i1 += random.nextInt(field_82665_g - field_82666_h);
		j1 += random.nextInt(field_82665_g - field_82666_h);

		if (k == i1 && l == j1) {
			boolean flag = worldObj.getWorldChunkManager().areBiomesViable(k * 16 + 8, l * 16 + 8, 0,
					villageSpawnBiomes);

			return flag;
		}

		return false;
	}

	@Override
	protected StructureStart getStructureStart(int p_75049_1_, int p_75049_2_) {
		return new MapGenVillage.Start(worldObj, rand, p_75049_1_, p_75049_2_, terrainType);
	}

	public static class Start extends StructureStart {
		private boolean hasMoreThanTwoComponents;
		public Start() {
		}

		@SuppressWarnings({ "unchecked", "rawtypes" })
		public Start(World p_i2092_1_, Random p_i2092_2_, int p_i2092_3_, int p_i2092_4_, int p_i2092_5_) {
			super(p_i2092_3_, p_i2092_4_);
			List list = StructureVillagePieces.getStructureVillageWeightedPieceList(p_i2092_2_, p_i2092_5_);
			StructureVillagePieces.Start start = new StructureVillagePieces.Start(p_i2092_1_.getWorldChunkManager(), 0,
					p_i2092_2_, (p_i2092_3_ << 4) + 2, (p_i2092_4_ << 4) + 2, list, p_i2092_5_);
			components.add(start);
			start.buildComponent(start, components, p_i2092_2_);
			List list1 = start.field_74930_j;
			List list2 = start.field_74932_i;
			int l;

			while (!list1.isEmpty() || !list2.isEmpty()) {
				StructureComponent structurecomponent;

				if (list1.isEmpty()) {
					l = p_i2092_2_.nextInt(list2.size());
					structurecomponent = (StructureComponent) list2.remove(l);
					structurecomponent.buildComponent(start, components, p_i2092_2_);
				} else {
					l = p_i2092_2_.nextInt(list1.size());
					structurecomponent = (StructureComponent) list1.remove(l);
					structurecomponent.buildComponent(start, components, p_i2092_2_);
				}
			}

			updateBoundingBox();
			l = 0;
			Iterator iterator = components.iterator();

			while (iterator.hasNext()) {
				StructureComponent structurecomponent1 = (StructureComponent) iterator.next();

				if (!(structurecomponent1 instanceof StructureVillagePieces.Road)) {
					++l;
				}
			}

			hasMoreThanTwoComponents = l > 2;
		}

		@Override
		public boolean isSizeableStructure() {
			return hasMoreThanTwoComponents;
		}

		@Override
		public void func_143022_a(NBTTagCompound p_143022_1_) {
			super.func_143022_a(p_143022_1_);
			p_143022_1_.setBoolean("Valid", hasMoreThanTwoComponents);
		}

		@Override
		public void func_143017_b(NBTTagCompound p_143017_1_) {
			super.func_143017_b(p_143017_1_);
			hasMoreThanTwoComponents = p_143017_1_.getBoolean("Valid");
		}
	}
}