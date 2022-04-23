package net.minecraft.world.gen.structure;

import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;

import java.util.*;
import java.util.Map.Entry;

public class MapGenScatteredFeature extends MapGenStructure {
	private static List biomelist = Arrays.asList(new BiomeGenBase[] { BiomeGenBase.desert, BiomeGenBase.desertHills,
			BiomeGenBase.jungle, BiomeGenBase.jungleHills, BiomeGenBase.swampland });
	private List scatteredFeatureSpawnList;
	private int maxDistanceBetweenScatteredFeatures;
	private int minDistanceBetweenScatteredFeatures;
	private static final String __OBFID = "CL_00000471";

	public MapGenScatteredFeature() {
		scatteredFeatureSpawnList = new ArrayList();
		maxDistanceBetweenScatteredFeatures = 32;
		minDistanceBetweenScatteredFeatures = 8;
		scatteredFeatureSpawnList.add(new BiomeGenBase.SpawnListEntry(EntityWitch.class, 1, 1, 1));
	}

	public MapGenScatteredFeature(Map p_i2061_1_) {
		this();
		Iterator iterator = p_i2061_1_.entrySet().iterator();

		while (iterator.hasNext()) {
			Entry entry = (Entry) iterator.next();

			if (((String) entry.getKey()).equals("distance")) {
				maxDistanceBetweenScatteredFeatures = MathHelper.parseIntWithDefaultAndMax((String) entry.getValue(),
						maxDistanceBetweenScatteredFeatures, minDistanceBetweenScatteredFeatures + 1);
			}
		}
	}

	@Override
	public String func_143025_a() {
		return "Temple";
	}

	@Override
	protected boolean canSpawnStructureAtCoords(int p_75047_1_, int p_75047_2_) {
		int k = p_75047_1_;
		int l = p_75047_2_;

		if (p_75047_1_ < 0) {
			p_75047_1_ -= maxDistanceBetweenScatteredFeatures - 1;
		}

		if (p_75047_2_ < 0) {
			p_75047_2_ -= maxDistanceBetweenScatteredFeatures - 1;
		}

		int i1 = p_75047_1_ / maxDistanceBetweenScatteredFeatures;
		int j1 = p_75047_2_ / maxDistanceBetweenScatteredFeatures;
		Random random = worldObj.setRandomSeed(i1, j1, 14357617);
		i1 *= maxDistanceBetweenScatteredFeatures;
		j1 *= maxDistanceBetweenScatteredFeatures;
		i1 += random.nextInt(maxDistanceBetweenScatteredFeatures - minDistanceBetweenScatteredFeatures);
		j1 += random.nextInt(maxDistanceBetweenScatteredFeatures - minDistanceBetweenScatteredFeatures);

		if (k == i1 && l == j1) {
			BiomeGenBase biomegenbase = worldObj.getWorldChunkManager().getBiomeGenAt(k * 16 + 8, l * 16 + 8);
			Iterator iterator = biomelist.iterator();

			while (iterator.hasNext()) {
				BiomeGenBase biomegenbase1 = (BiomeGenBase) iterator.next();

				if (biomegenbase == biomegenbase1)
					return true;
			}
		}

		return false;
	}

	@Override
	protected StructureStart getStructureStart(int p_75049_1_, int p_75049_2_) {
		return new MapGenScatteredFeature.Start(worldObj, rand, p_75049_1_, p_75049_2_);
	}

	public boolean func_143030_a(int p_143030_1_, int p_143030_2_, int p_143030_3_) {
		StructureStart structurestart = func_143028_c(p_143030_1_, p_143030_2_, p_143030_3_);

		if (structurestart != null && structurestart instanceof MapGenScatteredFeature.Start
				&& !structurestart.components.isEmpty()) {
			StructureComponent structurecomponent = (StructureComponent) structurestart.components.getFirst();
			return structurecomponent instanceof ComponentScatteredFeaturePieces.SwampHut;
		} else
			return false;
	}

	public List getScatteredFeatureSpawnList() {
		return scatteredFeatureSpawnList;
	}

	public static class Start extends StructureStart {
		private static final String __OBFID = "CL_00000472";

		public Start() {
		}

		public Start(World p_i2060_1_, Random p_i2060_2_, int p_i2060_3_, int p_i2060_4_) {
			super(p_i2060_3_, p_i2060_4_);
			BiomeGenBase biomegenbase = p_i2060_1_.getBiomeGenForCoords(p_i2060_3_ * 16 + 8, p_i2060_4_ * 16 + 8);

			if (biomegenbase != BiomeGenBase.jungle && biomegenbase != BiomeGenBase.jungleHills) {
				if (biomegenbase == BiomeGenBase.swampland) {
					ComponentScatteredFeaturePieces.SwampHut swamphut = new ComponentScatteredFeaturePieces.SwampHut(
							p_i2060_2_, p_i2060_3_ * 16, p_i2060_4_ * 16);
					components.add(swamphut);
				} else {
					ComponentScatteredFeaturePieces.DesertPyramid desertpyramid = new ComponentScatteredFeaturePieces.DesertPyramid(
							p_i2060_2_, p_i2060_3_ * 16, p_i2060_4_ * 16);
					components.add(desertpyramid);
				}
			} else {
				ComponentScatteredFeaturePieces.JunglePyramid junglepyramid = new ComponentScatteredFeaturePieces.JunglePyramid(
						p_i2060_2_, p_i2060_3_ * 16, p_i2060_4_ * 16);
				components.add(junglepyramid);
			}

			updateBoundingBox();
		}
	}
}