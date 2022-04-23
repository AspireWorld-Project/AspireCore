package net.minecraft.village;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

public class Village {
	private World worldObj;
	private final List villageDoorInfoList = new ArrayList();
	private final ChunkCoordinates centerHelper = new ChunkCoordinates(0, 0, 0);
	private final ChunkCoordinates center = new ChunkCoordinates(0, 0, 0);
	private int villageRadius;
	private int lastAddDoorTimestamp;
	private int tickCounter;
	private int numVillagers;
	private int noBreedTicks;
	private TreeMap playerReputation = new TreeMap();
	private List villageAgressors = new ArrayList();
	private int numIronGolems;
	private static final String __OBFID = "CL_00001631";

	public Village() {
	}

	public Village(World p_i1675_1_) {
		worldObj = p_i1675_1_;
	}

	public void func_82691_a(World p_82691_1_) {
		worldObj = p_82691_1_;
	}

	public void tick(int p_75560_1_) {
		tickCounter = p_75560_1_;
		removeDeadAndOutOfRangeDoors();
		removeDeadAndOldAgressors();

		if (p_75560_1_ % 20 == 0) {
			updateNumVillagers();
		}

		if (p_75560_1_ % 30 == 0) {
			updateNumIronGolems();
		}

		int j = numVillagers / 10;

		if (numIronGolems < j && villageDoorInfoList.size() > 20 && worldObj.rand.nextInt(7000) == 0) {
			Vec3 vec3 = tryGetIronGolemSpawningLocation(MathHelper.floor_float(center.posX),
					MathHelper.floor_float(center.posY), MathHelper.floor_float(center.posZ), 2, 4, 2);

			if (vec3 != null) {
				EntityIronGolem entityirongolem = new EntityIronGolem(worldObj);
				entityirongolem.setPosition(vec3.xCoord, vec3.yCoord, vec3.zCoord);
				worldObj.spawnEntityInWorld(entityirongolem);
				++numIronGolems;
			}
		}
	}

	private Vec3 tryGetIronGolemSpawningLocation(int p_75559_1_, int p_75559_2_, int p_75559_3_, int p_75559_4_,
			int p_75559_5_, int p_75559_6_) {
		for (int k1 = 0; k1 < 10; ++k1) {
			int l1 = p_75559_1_ + worldObj.rand.nextInt(16) - 8;
			int i2 = p_75559_2_ + worldObj.rand.nextInt(6) - 3;
			int j2 = p_75559_3_ + worldObj.rand.nextInt(16) - 8;

			if (isInRange(l1, i2, j2)
					&& isValidIronGolemSpawningLocation(l1, i2, j2, p_75559_4_, p_75559_5_, p_75559_6_))
				return Vec3.createVectorHelper(l1, i2, j2);
		}

		return null;
	}

	private boolean isValidIronGolemSpawningLocation(int p_75563_1_, int p_75563_2_, int p_75563_3_, int p_75563_4_,
			int p_75563_5_, int p_75563_6_) {
		if (!World.doesBlockHaveSolidTopSurface(worldObj, p_75563_1_, p_75563_2_ - 1, p_75563_3_))
			return false;
		else {
			int k1 = p_75563_1_ - p_75563_4_ / 2;
			int l1 = p_75563_3_ - p_75563_6_ / 2;

			for (int i2 = k1; i2 < k1 + p_75563_4_; ++i2) {
				for (int j2 = p_75563_2_; j2 < p_75563_2_ + p_75563_5_; ++j2) {
					for (int k2 = l1; k2 < l1 + p_75563_6_; ++k2) {
						if (worldObj.getBlockIfExists(i2, j2, k2).isNormalCube())
							return false;
					}
				}
			}

			return true;
		}
	}

	private void updateNumIronGolems() {
		List list = worldObj.getEntitiesWithinAABB(EntityIronGolem.class,
				AxisAlignedBB.getBoundingBox(center.posX - villageRadius, center.posY - 4, center.posZ - villageRadius,
						center.posX + villageRadius, center.posY + 4, center.posZ + villageRadius));
		numIronGolems = list.size();
	}

	private void updateNumVillagers() {
		List list = worldObj.getEntitiesWithinAABB(EntityVillager.class,
				AxisAlignedBB.getBoundingBox(center.posX - villageRadius, center.posY - 4, center.posZ - villageRadius,
						center.posX + villageRadius, center.posY + 4, center.posZ + villageRadius));
		numVillagers = list.size();

		if (numVillagers == 0) {
			playerReputation.clear();
		}
	}

	public ChunkCoordinates getCenter() {
		return center;
	}

	public int getVillageRadius() {
		return villageRadius;
	}

	public int getNumVillageDoors() {
		return villageDoorInfoList.size();
	}

	public int getTicksSinceLastDoorAdding() {
		return tickCounter - lastAddDoorTimestamp;
	}

	public int getNumVillagers() {
		return numVillagers;
	}

	public boolean isInRange(int p_75570_1_, int p_75570_2_, int p_75570_3_) {
		return center.getDistanceSquared(p_75570_1_, p_75570_2_, p_75570_3_) < villageRadius * villageRadius;
	}

	public List getVillageDoorInfoList() {
		return villageDoorInfoList;
	}

	public VillageDoorInfo findNearestDoor(int p_75564_1_, int p_75564_2_, int p_75564_3_) {
		VillageDoorInfo villagedoorinfo = null;
		int l = Integer.MAX_VALUE;
		Iterator iterator = villageDoorInfoList.iterator();

		while (iterator.hasNext()) {
			VillageDoorInfo villagedoorinfo1 = (VillageDoorInfo) iterator.next();
			int i1 = villagedoorinfo1.getDistanceSquared(p_75564_1_, p_75564_2_, p_75564_3_);

			if (i1 < l) {
				villagedoorinfo = villagedoorinfo1;
				l = i1;
			}
		}

		return villagedoorinfo;
	}

	public VillageDoorInfo findNearestDoorUnrestricted(int p_75569_1_, int p_75569_2_, int p_75569_3_) {
		VillageDoorInfo villagedoorinfo = null;
		int l = Integer.MAX_VALUE;
		Iterator iterator = villageDoorInfoList.iterator();

		while (iterator.hasNext()) {
			VillageDoorInfo villagedoorinfo1 = (VillageDoorInfo) iterator.next();
			int i1 = villagedoorinfo1.getDistanceSquared(p_75569_1_, p_75569_2_, p_75569_3_);

			if (i1 > 256) {
				i1 *= 1000;
			} else {
				i1 = villagedoorinfo1.getDoorOpeningRestrictionCounter();
			}

			if (i1 < l) {
				villagedoorinfo = villagedoorinfo1;
				l = i1;
			}
		}

		return villagedoorinfo;
	}

	public VillageDoorInfo getVillageDoorAt(int p_75578_1_, int p_75578_2_, int p_75578_3_) {
		if (center.getDistanceSquared(p_75578_1_, p_75578_2_, p_75578_3_) > villageRadius * villageRadius)
			return null;
		else {
			Iterator iterator = villageDoorInfoList.iterator();
			VillageDoorInfo villagedoorinfo;

			do {
				if (!iterator.hasNext())
					return null;

				villagedoorinfo = (VillageDoorInfo) iterator.next();
			} while (villagedoorinfo.posX != p_75578_1_ || villagedoorinfo.posZ != p_75578_3_
					|| Math.abs(villagedoorinfo.posY - p_75578_2_) > 1);

			return villagedoorinfo;
		}
	}

	public void addVillageDoorInfo(VillageDoorInfo p_75576_1_) {
		villageDoorInfoList.add(p_75576_1_);
		centerHelper.posX += p_75576_1_.posX;
		centerHelper.posY += p_75576_1_.posY;
		centerHelper.posZ += p_75576_1_.posZ;
		updateVillageRadiusAndCenter();
		lastAddDoorTimestamp = p_75576_1_.lastActivityTimestamp;
	}

	public boolean isAnnihilated() {
		return villageDoorInfoList.isEmpty();
	}

	public void addOrRenewAgressor(EntityLivingBase p_75575_1_) {
		Iterator iterator = villageAgressors.iterator();
		Village.VillageAgressor villageagressor;

		do {
			if (!iterator.hasNext()) {
				villageAgressors.add(new Village.VillageAgressor(p_75575_1_, tickCounter));
				return;
			}

			villageagressor = (Village.VillageAgressor) iterator.next();
		} while (villageagressor.agressor != p_75575_1_);

		villageagressor.agressionTime = tickCounter;
	}

	public EntityLivingBase findNearestVillageAggressor(EntityLivingBase p_75571_1_) {
		double d0 = Double.MAX_VALUE;
		Village.VillageAgressor villageagressor = null;

		for (int i = 0; i < villageAgressors.size(); ++i) {
			Village.VillageAgressor villageagressor1 = (Village.VillageAgressor) villageAgressors.get(i);
			double d1 = villageagressor1.agressor.getDistanceSqToEntity(p_75571_1_);

			if (d1 <= d0) {
				villageagressor = villageagressor1;
				d0 = d1;
			}
		}

		return villageagressor != null ? villageagressor.agressor : null;
	}

	public EntityPlayer func_82685_c(EntityLivingBase p_82685_1_) {
		double d0 = Double.MAX_VALUE;
		EntityPlayer entityplayer = null;
		Iterator iterator = playerReputation.keySet().iterator();

		while (iterator.hasNext()) {
			String s = (String) iterator.next();

			if (isPlayerReputationTooLow(s)) {
				EntityPlayer entityplayer1 = worldObj.getPlayerEntityByName(s);

				if (entityplayer1 != null) {
					double d1 = entityplayer1.getDistanceSqToEntity(p_82685_1_);

					if (d1 <= d0) {
						entityplayer = entityplayer1;
						d0 = d1;
					}
				}
			}
		}

		return entityplayer;
	}

	private void removeDeadAndOldAgressors() {
		Iterator iterator = villageAgressors.iterator();

		while (iterator.hasNext()) {
			Village.VillageAgressor villageagressor = (Village.VillageAgressor) iterator.next();

			if (!villageagressor.agressor.isEntityAlive()
					|| Math.abs(tickCounter - villageagressor.agressionTime) > 300) {
				iterator.remove();
			}
		}
	}

	private void removeDeadAndOutOfRangeDoors() {
		boolean flag = false;
		boolean flag1 = worldObj.rand.nextInt(50) == 0;
		Iterator iterator = villageDoorInfoList.iterator();

		while (iterator.hasNext()) {
			VillageDoorInfo villagedoorinfo = (VillageDoorInfo) iterator.next();

			if (flag1) {
				villagedoorinfo.resetDoorOpeningRestrictionCounter();
			}

			if (!isBlockDoor(villagedoorinfo.posX, villagedoorinfo.posY, villagedoorinfo.posZ)
					|| Math.abs(tickCounter - villagedoorinfo.lastActivityTimestamp) > 1200) {
				centerHelper.posX -= villagedoorinfo.posX;
				centerHelper.posY -= villagedoorinfo.posY;
				centerHelper.posZ -= villagedoorinfo.posZ;
				flag = true;
				villagedoorinfo.isDetachedFromVillageFlag = true;
				iterator.remove();
			}
		}

		if (flag) {
			updateVillageRadiusAndCenter();
		}
	}

	private boolean isBlockDoor(int p_75574_1_, int p_75574_2_, int p_75574_3_) {
		return worldObj.getBlockIfExists(p_75574_1_, p_75574_2_, p_75574_3_) == Blocks.wooden_door;
	}

	private void updateVillageRadiusAndCenter() {
		int i = villageDoorInfoList.size();

		if (i == 0) {
			center.set(0, 0, 0);
			villageRadius = 0;
		} else {
			center.set(centerHelper.posX / i, centerHelper.posY / i, centerHelper.posZ / i);
			int j = 0;
			VillageDoorInfo villagedoorinfo;

			for (Iterator iterator = villageDoorInfoList.iterator(); iterator.hasNext(); j = Math
					.max(villagedoorinfo.getDistanceSquared(center.posX, center.posY, center.posZ), j)) {
				villagedoorinfo = (VillageDoorInfo) iterator.next();
			}

			villageRadius = Math.max(32, (int) Math.sqrt(j) + 1);
		}
	}

	public int getReputationForPlayer(String p_82684_1_) {
		Integer integer = (Integer) playerReputation.get(p_82684_1_);
		return integer != null ? integer.intValue() : 0;
	}

	public int setReputationForPlayer(String p_82688_1_, int p_82688_2_) {
		int j = getReputationForPlayer(p_82688_1_);
		int k = MathHelper.clamp_int(j + p_82688_2_, -30, 10);
		playerReputation.put(p_82688_1_, Integer.valueOf(k));
		return k;
	}

	public boolean isPlayerReputationTooLow(String p_82687_1_) {
		return getReputationForPlayer(p_82687_1_) <= -15;
	}

	public void readVillageDataFromNBT(NBTTagCompound p_82690_1_) {
		numVillagers = p_82690_1_.getInteger("PopSize");
		villageRadius = p_82690_1_.getInteger("Radius");
		numIronGolems = p_82690_1_.getInteger("Golems");
		lastAddDoorTimestamp = p_82690_1_.getInteger("Stable");
		tickCounter = p_82690_1_.getInteger("Tick");
		noBreedTicks = p_82690_1_.getInteger("MTick");
		center.posX = p_82690_1_.getInteger("CX");
		center.posY = p_82690_1_.getInteger("CY");
		center.posZ = p_82690_1_.getInteger("CZ");
		centerHelper.posX = p_82690_1_.getInteger("ACX");
		centerHelper.posY = p_82690_1_.getInteger("ACY");
		centerHelper.posZ = p_82690_1_.getInteger("ACZ");
		NBTTagList nbttaglist = p_82690_1_.getTagList("Doors", 10);

		for (int i = 0; i < nbttaglist.tagCount(); ++i) {
			NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
			VillageDoorInfo villagedoorinfo = new VillageDoorInfo(nbttagcompound1.getInteger("X"),
					nbttagcompound1.getInteger("Y"), nbttagcompound1.getInteger("Z"), nbttagcompound1.getInteger("IDX"),
					nbttagcompound1.getInteger("IDZ"), nbttagcompound1.getInteger("TS"));
			villageDoorInfoList.add(villagedoorinfo);
		}

		NBTTagList nbttaglist1 = p_82690_1_.getTagList("Players", 10);

		for (int j = 0; j < nbttaglist1.tagCount(); ++j) {
			NBTTagCompound nbttagcompound2 = nbttaglist1.getCompoundTagAt(j);
			playerReputation.put(nbttagcompound2.getString("Name"), Integer.valueOf(nbttagcompound2.getInteger("S")));
		}
	}

	public void writeVillageDataToNBT(NBTTagCompound p_82689_1_) {
		p_82689_1_.setInteger("PopSize", numVillagers);
		p_82689_1_.setInteger("Radius", villageRadius);
		p_82689_1_.setInteger("Golems", numIronGolems);
		p_82689_1_.setInteger("Stable", lastAddDoorTimestamp);
		p_82689_1_.setInteger("Tick", tickCounter);
		p_82689_1_.setInteger("MTick", noBreedTicks);
		p_82689_1_.setInteger("CX", center.posX);
		p_82689_1_.setInteger("CY", center.posY);
		p_82689_1_.setInteger("CZ", center.posZ);
		p_82689_1_.setInteger("ACX", centerHelper.posX);
		p_82689_1_.setInteger("ACY", centerHelper.posY);
		p_82689_1_.setInteger("ACZ", centerHelper.posZ);
		NBTTagList nbttaglist = new NBTTagList();
		Iterator iterator = villageDoorInfoList.iterator();

		while (iterator.hasNext()) {
			VillageDoorInfo villagedoorinfo = (VillageDoorInfo) iterator.next();
			NBTTagCompound nbttagcompound1 = new NBTTagCompound();
			nbttagcompound1.setInteger("X", villagedoorinfo.posX);
			nbttagcompound1.setInteger("Y", villagedoorinfo.posY);
			nbttagcompound1.setInteger("Z", villagedoorinfo.posZ);
			nbttagcompound1.setInteger("IDX", villagedoorinfo.insideDirectionX);
			nbttagcompound1.setInteger("IDZ", villagedoorinfo.insideDirectionZ);
			nbttagcompound1.setInteger("TS", villagedoorinfo.lastActivityTimestamp);
			nbttaglist.appendTag(nbttagcompound1);
		}

		p_82689_1_.setTag("Doors", nbttaglist);
		NBTTagList nbttaglist1 = new NBTTagList();
		Iterator iterator1 = playerReputation.keySet().iterator();

		while (iterator1.hasNext()) {
			String s = (String) iterator1.next();
			NBTTagCompound nbttagcompound2 = new NBTTagCompound();
			nbttagcompound2.setString("Name", s);
			nbttagcompound2.setInteger("S", ((Integer) playerReputation.get(s)).intValue());
			nbttaglist1.appendTag(nbttagcompound2);
		}

		p_82689_1_.setTag("Players", nbttaglist1);
	}

	public void endMatingSeason() {
		noBreedTicks = tickCounter;
	}

	public boolean isMatingSeason() {
		return noBreedTicks == 0 || tickCounter - noBreedTicks >= 3600;
	}

	public void setDefaultPlayerReputation(int p_82683_1_) {
		Iterator iterator = playerReputation.keySet().iterator();

		while (iterator.hasNext()) {
			String s = (String) iterator.next();
			setReputationForPlayer(s, p_82683_1_);
		}
	}

	class VillageAgressor {
		public EntityLivingBase agressor;
		public int agressionTime;
		private static final String __OBFID = "CL_00001632";

		VillageAgressor(EntityLivingBase p_i1674_2_, int p_i1674_3_) {
			agressor = p_i1674_2_;
			agressionTime = p_i1674_3_;
		}
	}
}