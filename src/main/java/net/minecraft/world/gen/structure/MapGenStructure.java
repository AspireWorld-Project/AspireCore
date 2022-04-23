package net.minecraft.world.gen.structure;

import net.minecraft.block.Block;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ReportedException;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;
import net.minecraft.world.gen.MapGenBase;

import java.util.*;
import java.util.concurrent.Callable;

public abstract class MapGenStructure extends MapGenBase {
	private MapGenStructureData field_143029_e;
	protected Map structureMap = new HashMap();
	private static final String __OBFID = "CL_00000505";

	public abstract String func_143025_a();

	@Override
	protected final void func_151538_a(World p_151538_1_, final int p_151538_2_, final int p_151538_3_, int p_151538_4_,
			int p_151538_5_, Block[] p_151538_6_) {
		func_143027_a(p_151538_1_);

		if (!structureMap.containsKey(Long.valueOf(ChunkCoordIntPair.chunkXZ2Int(p_151538_2_, p_151538_3_)))) {
			rand.nextInt();

			try {
				if (canSpawnStructureAtCoords(p_151538_2_, p_151538_3_)) {
					StructureStart structurestart = getStructureStart(p_151538_2_, p_151538_3_);
					structureMap.put(Long.valueOf(ChunkCoordIntPair.chunkXZ2Int(p_151538_2_, p_151538_3_)),
							structurestart);
					func_143026_a(p_151538_2_, p_151538_3_, structurestart);
				}
			} catch (Throwable throwable) {
				CrashReport crashreport = CrashReport.makeCrashReport(throwable,
						"Exception preparing structure feature");
				CrashReportCategory crashreportcategory = crashreport.makeCategory("Feature being prepared");
				crashreportcategory.addCrashSectionCallable("Is feature chunk", new Callable() {
					private static final String __OBFID = "CL_00000506";

					@Override
					public String call() {
						return MapGenStructure.this.canSpawnStructureAtCoords(p_151538_2_, p_151538_3_) ? "True"
								: "False";
					}
				});
				crashreportcategory.addCrashSection("Chunk location", String.format("%d,%d",
						Integer.valueOf(p_151538_2_), Integer.valueOf(p_151538_3_)));
				crashreportcategory.addCrashSectionCallable("Chunk pos hash", new Callable() {
					private static final String __OBFID = "CL_00000507";

					@Override
					public String call() {
						return String.valueOf(ChunkCoordIntPair.chunkXZ2Int(p_151538_2_, p_151538_3_));
					}
				});
				crashreportcategory.addCrashSectionCallable("Structure type", new Callable() {
					private static final String __OBFID = "CL_00000508";

					@Override
					public String call() {
						return MapGenStructure.this.getClass().getCanonicalName();
					}
				});
				throw new ReportedException(crashreport);
			}
		}
	}

	public boolean generateStructuresInChunk(World p_75051_1_, Random p_75051_2_, int p_75051_3_, int p_75051_4_) {
		func_143027_a(p_75051_1_);
		int k = (p_75051_3_ << 4) + 8;
		int l = (p_75051_4_ << 4) + 8;
		boolean flag = false;
		Iterator iterator = new ArrayList(structureMap.values()).iterator();

		while (iterator.hasNext()) {
			StructureStart structurestart = (StructureStart) iterator.next();

			if (structurestart.isSizeableStructure()
					&& structurestart.getBoundingBox().intersectsWith(k, l, k + 15, l + 15)) {
				structurestart.generateStructure(p_75051_1_, p_75051_2_,
						new StructureBoundingBox(k, l, k + 15, l + 15));
				flag = true;
				func_143026_a(structurestart.func_143019_e(), structurestart.func_143018_f(), structurestart);
			}
		}

		return flag;
	}

	public boolean hasStructureAt(int p_75048_1_, int p_75048_2_, int p_75048_3_) {
		func_143027_a(worldObj);
		return func_143028_c(p_75048_1_, p_75048_2_, p_75048_3_) != null;
	}

	protected StructureStart func_143028_c(int p_143028_1_, int p_143028_2_, int p_143028_3_) {
		Iterator iterator = structureMap.values().iterator();

		while (iterator.hasNext()) {
			StructureStart structurestart = (StructureStart) iterator.next();

			if (structurestart.isSizeableStructure() && structurestart.getBoundingBox().intersectsWith(p_143028_1_,
					p_143028_3_, p_143028_1_, p_143028_3_)) {
				Iterator iterator1 = structurestart.getComponents().iterator();

				while (iterator1.hasNext()) {
					StructureComponent structurecomponent = (StructureComponent) iterator1.next();

					if (structurecomponent.getBoundingBox().isVecInside(p_143028_1_, p_143028_2_, p_143028_3_))
						return structurestart;
				}
			}
		}

		return null;
	}

	public boolean func_142038_b(int p_142038_1_, int p_142038_2_, int p_142038_3_) {
		func_143027_a(worldObj);
		Iterator iterator = structureMap.values().iterator();
		StructureStart structurestart;

		do {
			if (!iterator.hasNext())
				return false;

			structurestart = (StructureStart) iterator.next();
		} while (!structurestart.isSizeableStructure());

		return structurestart.getBoundingBox().intersectsWith(p_142038_1_, p_142038_3_, p_142038_1_, p_142038_3_);
	}

	public ChunkPosition func_151545_a(World p_151545_1_, int p_151545_2_, int p_151545_3_, int p_151545_4_) {
		worldObj = p_151545_1_;
		func_143027_a(p_151545_1_);
		rand.setSeed(p_151545_1_.getSeed());
		long l = rand.nextLong();
		long i1 = rand.nextLong();
		long j1 = (p_151545_2_ >> 4) * l;
		long k1 = (p_151545_4_ >> 4) * i1;
		rand.setSeed(j1 ^ k1 ^ p_151545_1_.getSeed());
		func_151538_a(p_151545_1_, p_151545_2_ >> 4, p_151545_4_ >> 4, 0, 0, null);
		double d0 = Double.MAX_VALUE;
		ChunkPosition chunkposition = null;
		Iterator iterator = structureMap.values().iterator();
		ChunkPosition chunkposition1;
		int l1;
		int i2;
		int j2;
		double d1;

		while (iterator.hasNext()) {
			StructureStart structurestart = (StructureStart) iterator.next();

			if (structurestart.isSizeableStructure()) {
				StructureComponent structurecomponent = (StructureComponent) structurestart.getComponents().get(0);
				chunkposition1 = structurecomponent.func_151553_a();
				l1 = chunkposition1.chunkPosX - p_151545_2_;
				i2 = chunkposition1.chunkPosY - p_151545_3_;
				j2 = chunkposition1.chunkPosZ - p_151545_4_;
				d1 = l1 * l1 + i2 * i2 + j2 * j2;

				if (d1 < d0) {
					d0 = d1;
					chunkposition = chunkposition1;
				}
			}
		}

		if (chunkposition != null)
			return chunkposition;
		else {
			List list = getCoordList();

			if (list != null) {
				ChunkPosition chunkposition2 = null;
				Iterator iterator1 = list.iterator();

				while (iterator1.hasNext()) {
					chunkposition1 = (ChunkPosition) iterator1.next();
					l1 = chunkposition1.chunkPosX - p_151545_2_;
					i2 = chunkposition1.chunkPosY - p_151545_3_;
					j2 = chunkposition1.chunkPosZ - p_151545_4_;
					d1 = l1 * l1 + i2 * i2 + j2 * j2;

					if (d1 < d0) {
						d0 = d1;
						chunkposition2 = chunkposition1;
					}
				}

				return chunkposition2;
			} else
				return null;
		}
	}

	protected List getCoordList() {
		return null;
	}

	private void func_143027_a(World p_143027_1_) {
		if (field_143029_e == null) {
			field_143029_e = (MapGenStructureData) p_143027_1_.perWorldStorage.loadData(MapGenStructureData.class,
					func_143025_a());

			if (field_143029_e == null) {
				field_143029_e = new MapGenStructureData(func_143025_a());
				p_143027_1_.perWorldStorage.setData(func_143025_a(), field_143029_e);
			} else {
				NBTTagCompound nbttagcompound = field_143029_e.func_143041_a();
				if (nbttagcompound == null) {
					structureMap = field_143029_e.getStructureMap();
					return;
				}
				Iterator iterator = nbttagcompound.func_150296_c().iterator();

				while (iterator.hasNext()) {
					String s = (String) iterator.next();
					NBTBase nbtbase = nbttagcompound.getTag(s);

					if (nbtbase.getId() == 10) {
						NBTTagCompound nbttagcompound1 = (NBTTagCompound) nbtbase;

						if (nbttagcompound1.hasKey("ChunkX") && nbttagcompound1.hasKey("ChunkZ")) {
							int i = nbttagcompound1.getInteger("ChunkX");
							int j = nbttagcompound1.getInteger("ChunkZ");
							StructureStart structurestart = MapGenStructureIO.func_143035_a(nbttagcompound1,
									p_143027_1_);

							if (structurestart != null) {
								structureMap.put(Long.valueOf(ChunkCoordIntPair.chunkXZ2Int(i, j)), structurestart);
							}
						}
					}
				}
			}

			field_143029_e.replaceNbtWithStrictureMap(structureMap);
		}
	}

	private void func_143026_a(int p_143026_1_, int p_143026_2_, StructureStart p_143026_3_) {
		field_143029_e.func_143043_a(p_143026_3_.func_143021_a(p_143026_1_, p_143026_2_), p_143026_1_, p_143026_2_);
		field_143029_e.markDirty();
	}

	protected abstract boolean canSpawnStructureAtCoords(int p_75047_1_, int p_75047_2_);

	protected abstract StructureStart getStructureStart(int p_75049_1_, int p_75049_2_);
}