package net.minecraft.world.gen.structure;

import net.minecraft.util.MathHelper;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.BiomeManager;

import java.util.*;
import java.util.Map.Entry;

public class MapGenStronghold extends MapGenStructure {
	public final List field_151546_e;
	private boolean ranBiomeCheck;
	private ChunkCoordIntPair[] structureCoords;
	private double field_82671_h;
	private int field_82672_i;
	private static final String __OBFID = "CL_00000481";

	public MapGenStronghold() {
		structureCoords = new ChunkCoordIntPair[3];
		field_82671_h = 32.0D;
		field_82672_i = 3;
		field_151546_e = new ArrayList();
		BiomeGenBase[] abiomegenbase = BiomeGenBase.getBiomeGenArray();
		int i = abiomegenbase.length;

		for (int j = 0; j < i; ++j) {
			BiomeGenBase biomegenbase = abiomegenbase[j];

			if (biomegenbase != null && biomegenbase.rootHeight > 0.0F
					&& !BiomeManager.strongHoldBiomesBlackList.contains(biomegenbase)) {
				field_151546_e.add(biomegenbase);
			}
		}
		for (BiomeGenBase biome : BiomeManager.strongHoldBiomes) {
			if (!field_151546_e.contains(biome)) {
				field_151546_e.add(biome);
			}
		}
	}

	public MapGenStronghold(Map p_i2068_1_) {
		this();
		Iterator iterator = p_i2068_1_.entrySet().iterator();

		while (iterator.hasNext()) {
			Entry entry = (Entry) iterator.next();

			if (entry.getKey().equals("distance")) {
				field_82671_h = MathHelper.parseDoubleWithDefaultAndMax((String) entry.getValue(), field_82671_h, 1.0D);
			} else if (entry.getKey().equals("count")) {
				structureCoords = new ChunkCoordIntPair[MathHelper.parseIntWithDefaultAndMax((String) entry.getValue(),
						structureCoords.length, 1)];
			} else if (entry.getKey().equals("spread")) {
				field_82672_i = MathHelper.parseIntWithDefaultAndMax((String) entry.getValue(), field_82672_i, 1);
			}
		}
	}

	@Override
	public String func_143025_a() {
		return "Stronghold";
	}

	@Override
	protected boolean canSpawnStructureAtCoords(int p_75047_1_, int p_75047_2_) {
		if (!ranBiomeCheck) {
			Random random = new Random();
			random.setSeed(worldObj.getSeed());
			double d0 = random.nextDouble() * Math.PI * 2.0D;
			int l = 1;

			for (int i1 = 0; i1 < structureCoords.length; ++i1) {
				double d1 = (1.25D * l + random.nextDouble()) * field_82671_h * l;
				int j1 = (int) Math.round(Math.cos(d0) * d1);
				int k1 = (int) Math.round(Math.sin(d0) * d1);
				ChunkPosition chunkposition = worldObj.getWorldChunkManager().findBiomePosition((j1 << 4) + 8,
						(k1 << 4) + 8, 112, field_151546_e, random);

				if (chunkposition != null) {
					j1 = chunkposition.chunkPosX >> 4;
					k1 = chunkposition.chunkPosZ >> 4;
				}

				structureCoords[i1] = new ChunkCoordIntPair(j1, k1);
				d0 += Math.PI * 2D * l / field_82672_i;

				if (i1 == field_82672_i) {
					l += 2 + random.nextInt(5);
					field_82672_i += 1 + random.nextInt(2);
				}
			}

			ranBiomeCheck = true;
		}

		ChunkCoordIntPair[] achunkcoordintpair = structureCoords;
		int l1 = achunkcoordintpair.length;

		for (int k = 0; k < l1; ++k) {
			ChunkCoordIntPair chunkcoordintpair = achunkcoordintpair[k];

			if (p_75047_1_ == chunkcoordintpair.chunkXPos && p_75047_2_ == chunkcoordintpair.chunkZPos)
				return true;
		}

		return false;
	}

	@Override
	protected List getCoordList() {
		ArrayList arraylist = new ArrayList();
		ChunkCoordIntPair[] achunkcoordintpair = structureCoords;
		int i = achunkcoordintpair.length;

		for (int j = 0; j < i; ++j) {
			ChunkCoordIntPair chunkcoordintpair = achunkcoordintpair[j];

			if (chunkcoordintpair != null) {
				arraylist.add(chunkcoordintpair.func_151349_a(64));
			}
		}

		return arraylist;
	}

	@Override
	protected StructureStart getStructureStart(int p_75049_1_, int p_75049_2_) {
		MapGenStronghold.Start start;

		for (start = new MapGenStronghold.Start(worldObj, rand, p_75049_1_, p_75049_2_); start.getComponents().isEmpty()
				|| ((StructureStrongholdPieces.Stairs2) start.getComponents()
						.get(0)).strongholdPortalRoom == null; start = new MapGenStronghold.Start(worldObj, rand,
								p_75049_1_, p_75049_2_)) {
		}

		return start;
	}

	public static class Start extends StructureStart {
		private static final String __OBFID = "CL_00000482";

		public Start() {
		}

		public Start(World p_i2067_1_, Random p_i2067_2_, int p_i2067_3_, int p_i2067_4_) {
			super(p_i2067_3_, p_i2067_4_);
			StructureStrongholdPieces.prepareStructurePieces();
			StructureStrongholdPieces.Stairs2 stairs2 = new StructureStrongholdPieces.Stairs2(0, p_i2067_2_,
					(p_i2067_3_ << 4) + 2, (p_i2067_4_ << 4) + 2);
			components.add(stairs2);
			stairs2.buildComponent(stairs2, components, p_i2067_2_);
			List list = stairs2.field_75026_c;

			while (!list.isEmpty()) {
				int k = p_i2067_2_.nextInt(list.size());
				StructureComponent structurecomponent = (StructureComponent) list.remove(k);
				structurecomponent.buildComponent(stairs2, components, p_i2067_2_);
			}

			updateBoundingBox();
			markAvailableHeight(p_i2067_1_, p_i2067_2_, 10);
		}
	}
}