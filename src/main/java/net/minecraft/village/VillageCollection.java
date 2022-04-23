package net.minecraft.village;

import net.minecraft.block.BlockDoor;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class VillageCollection extends WorldSavedData {
	private World worldObj;
	@SuppressWarnings("rawtypes")
	private final List villagerPositionsList = new ArrayList();
	@SuppressWarnings("rawtypes")
	private final List newDoors = new ArrayList();
	@SuppressWarnings("rawtypes")
	private final List villageList = new ArrayList();
	private int tickCounter;
	public VillageCollection(String p_i1677_1_) {
		super(p_i1677_1_);
	}

	public VillageCollection(World p_i1678_1_) {
		super("villages");
		worldObj = p_i1678_1_;
		markDirty();
	}

	@SuppressWarnings("rawtypes")
	public void func_82566_a(World p_82566_1_) {
		worldObj = p_82566_1_;
		Iterator iterator = villageList.iterator();

		while (iterator.hasNext()) {
			Village village = (Village) iterator.next();
			village.func_82691_a(p_82566_1_);
		}
	}

	@SuppressWarnings("unchecked")
	public void addVillagerPosition(int p_75551_1_, int p_75551_2_, int p_75551_3_) {
		if (villagerPositionsList.size() <= 64) {
			if (!isVillagerPositionPresent(p_75551_1_, p_75551_2_, p_75551_3_)) {
				villagerPositionsList.add(new ChunkCoordinates(p_75551_1_, p_75551_2_, p_75551_3_));
			}
		}
	}

	@SuppressWarnings("rawtypes")
	public void tick() {
		++tickCounter;
		Iterator iterator = villageList.iterator();

		while (iterator.hasNext()) {
			Village village = (Village) iterator.next();
			village.tick(tickCounter);
		}

		removeAnnihilatedVillages();
		dropOldestVillagerPosition();
		addNewDoorsToVillageOrCreateVillage();

		if (tickCounter % 400 == 0) {
			markDirty();
		}
	}

	@SuppressWarnings("rawtypes")
	private void removeAnnihilatedVillages() {
		Iterator iterator = villageList.iterator();

		while (iterator.hasNext()) {
			Village village = (Village) iterator.next();

			if (village.isAnnihilated()) {
				iterator.remove();
				markDirty();
			}
		}
	}

	@SuppressWarnings("rawtypes")
	public List getVillageList() {
		return villageList;
	}

	@SuppressWarnings("rawtypes")
	public Village findNearestVillage(int p_75550_1_, int p_75550_2_, int p_75550_3_, int p_75550_4_) {
		Village village = null;
		float f = Float.MAX_VALUE;
		Iterator iterator = villageList.iterator();

		while (iterator.hasNext()) {
			Village village1 = (Village) iterator.next();
			float f1 = village1.getCenter().getDistanceSquared(p_75550_1_, p_75550_2_, p_75550_3_);

			if (f1 < f) {
				float f2 = p_75550_4_ + village1.getVillageRadius();

				if (f1 <= f2 * f2) {
					village = village1;
					f = f1;
				}
			}
		}

		return village;
	}

	private void dropOldestVillagerPosition() {
		if (!villagerPositionsList.isEmpty()) {
			addUnassignedWoodenDoorsAroundToNewDoorsList((ChunkCoordinates) villagerPositionsList.remove(0));
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void addNewDoorsToVillageOrCreateVillage() {
		int i = 0;

		while (i < newDoors.size()) {
			VillageDoorInfo villagedoorinfo = (VillageDoorInfo) newDoors.get(i);
			boolean flag = false;
			Iterator iterator = villageList.iterator();

			while (true) {
				if (iterator.hasNext()) {
					Village village = (Village) iterator.next();
					int j = (int) village.getCenter().getDistanceSquared(villagedoorinfo.posX, villagedoorinfo.posY,
							villagedoorinfo.posZ);
					float k = 32f + village.getVillageRadius(); // BugFix: Avoid int wrapping

					if (j > k * k) {
						continue;
					}

					village.addVillageDoorInfo(villagedoorinfo);
					flag = true;
				}

				if (!flag) {
					Village village1 = new Village(worldObj);
					village1.addVillageDoorInfo(villagedoorinfo);
					villageList.add(village1);
					markDirty();
				}

				++i;
				break;
			}
		}

		newDoors.clear();
	}

	private void addUnassignedWoodenDoorsAroundToNewDoorsList(ChunkCoordinates p_75546_1_) {
		byte b0 = 16;
		byte b1 = 4;
		byte b2 = 16;

		for (int i = p_75546_1_.posX - b0; i < p_75546_1_.posX + b0; ++i) {
			for (int j = p_75546_1_.posY - b1; j < p_75546_1_.posY + b1; ++j) {
				for (int k = p_75546_1_.posZ - b2; k < p_75546_1_.posZ + b2; ++k) {
					if (isWoodenDoorAt(i, j, k)) {
						VillageDoorInfo villagedoorinfo = getVillageDoorAt(i, j, k);

						if (villagedoorinfo == null) {
							addDoorToNewListIfAppropriate(i, j, k);
						} else {
							villagedoorinfo.lastActivityTimestamp = tickCounter;
						}
					}
				}
			}
		}
	}

	@SuppressWarnings("rawtypes")
	private VillageDoorInfo getVillageDoorAt(int p_75547_1_, int p_75547_2_, int p_75547_3_) {
		Iterator iterator = newDoors.iterator();
		VillageDoorInfo villagedoorinfo;

		do {
			if (!iterator.hasNext()) {
				iterator = villageList.iterator();
				VillageDoorInfo villagedoorinfo1;

				do {
					if (!iterator.hasNext())
						return null;

					Village village = (Village) iterator.next();
					villagedoorinfo1 = village.getVillageDoorAt(p_75547_1_, p_75547_2_, p_75547_3_);
				} while (villagedoorinfo1 == null);

				return villagedoorinfo1;
			}

			villagedoorinfo = (VillageDoorInfo) iterator.next();
		} while (villagedoorinfo.posX != p_75547_1_ || villagedoorinfo.posZ != p_75547_3_
				|| Math.abs(villagedoorinfo.posY - p_75547_2_) > 1);

		return villagedoorinfo;
	}

	@SuppressWarnings("unchecked")
	private void addDoorToNewListIfAppropriate(int p_75542_1_, int p_75542_2_, int p_75542_3_) {
		int l = ((BlockDoor) Blocks.wooden_door).func_150013_e(worldObj, p_75542_1_, p_75542_2_, p_75542_3_);
		int i1;
		int j1;

		if (l != 0 && l != 2) {
			i1 = 0;

			for (j1 = -5; j1 < 0; ++j1) {
				if (worldObj.canBlockSeeTheSky(p_75542_1_, p_75542_2_, p_75542_3_ + j1)) {
					--i1;
				}
			}

			for (j1 = 1; j1 <= 5; ++j1) {
				if (worldObj.canBlockSeeTheSky(p_75542_1_, p_75542_2_, p_75542_3_ + j1)) {
					++i1;
				}
			}

			if (i1 != 0) {
				newDoors.add(new VillageDoorInfo(p_75542_1_, p_75542_2_, p_75542_3_, 0, i1 > 0 ? -2 : 2, tickCounter));
			}
		} else {
			i1 = 0;

			for (j1 = -5; j1 < 0; ++j1) {
				if (worldObj.canBlockSeeTheSky(p_75542_1_ + j1, p_75542_2_, p_75542_3_)) {
					--i1;
				}
			}

			for (j1 = 1; j1 <= 5; ++j1) {
				if (worldObj.canBlockSeeTheSky(p_75542_1_ + j1, p_75542_2_, p_75542_3_)) {
					++i1;
				}
			}

			if (i1 != 0) {
				newDoors.add(new VillageDoorInfo(p_75542_1_, p_75542_2_, p_75542_3_, i1 > 0 ? -2 : 2, 0, tickCounter));
			}
		}
	}

	@SuppressWarnings("rawtypes")
	private boolean isVillagerPositionPresent(int p_75548_1_, int p_75548_2_, int p_75548_3_) {
		Iterator iterator = villagerPositionsList.iterator();
		ChunkCoordinates chunkcoordinates;

		do {
			if (!iterator.hasNext())
				return false;

			chunkcoordinates = (ChunkCoordinates) iterator.next();
		} while (chunkcoordinates.posX != p_75548_1_ || chunkcoordinates.posY != p_75548_2_
				|| chunkcoordinates.posZ != p_75548_3_);

		return true;
	}

	private boolean isWoodenDoorAt(int p_75541_1_, int p_75541_2_, int p_75541_3_) {
		return worldObj.getBlock(p_75541_1_, p_75541_2_, p_75541_3_) == Blocks.wooden_door;
	}

	@SuppressWarnings({ "unchecked" })
	@Override
	public void readFromNBT(NBTTagCompound p_76184_1_) {
		tickCounter = p_76184_1_.getInteger("Tick");
		NBTTagList nbttaglist = p_76184_1_.getTagList("Villages", 10);

		for (int i = 0; i < nbttaglist.tagCount(); ++i) {
			NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
			Village village = new Village();
			village.readVillageDataFromNBT(nbttagcompound1);
			villageList.add(village);
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void writeToNBT(NBTTagCompound p_76187_1_) {
		p_76187_1_.setInteger("Tick", tickCounter);
		NBTTagList nbttaglist = new NBTTagList();
		Iterator iterator = villageList.iterator();

		while (iterator.hasNext()) {
			Village village = (Village) iterator.next();
			NBTTagCompound nbttagcompound1 = new NBTTagCompound();
			village.writeVillageDataToNBT(nbttagcompound1);
			nbttaglist.appendTag(nbttagcompound1);
		}

		p_76187_1_.setTag("Villages", nbttaglist);
	}
}