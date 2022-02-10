package net.minecraft.pathfinding;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class PathNavigate {
	private EntityLiving theEntity;
	private World worldObj;
	private PathEntity currentPath;
	private double speed;
	private IAttributeInstance pathSearchRange;
	private boolean noSunPathfind;
	private int totalTicks;
	private int ticksAtLastPos;
	private Vec3 lastPosCheck = Vec3.createVectorHelper(0.0D, 0.0D, 0.0D);
	private boolean canPassOpenWoodenDoors = true;
	private boolean canPassClosedWoodenDoors;
	private boolean avoidsWater;
	private boolean canSwim;
	private static final String __OBFID = "CL_00001627";

	public PathNavigate(EntityLiving p_i1671_1_, World p_i1671_2_) {
		theEntity = p_i1671_1_;
		worldObj = p_i1671_2_;
		pathSearchRange = p_i1671_1_.getEntityAttribute(SharedMonsterAttributes.followRange);
	}

	public void setAvoidsWater(boolean p_75491_1_) {
		avoidsWater = p_75491_1_;
	}

	public boolean getAvoidsWater() {
		return avoidsWater;
	}

	public void setBreakDoors(boolean p_75498_1_) {
		canPassClosedWoodenDoors = p_75498_1_;
	}

	public void setEnterDoors(boolean p_75490_1_) {
		canPassOpenWoodenDoors = p_75490_1_;
	}

	public boolean getCanBreakDoors() {
		return canPassClosedWoodenDoors;
	}

	public void setAvoidSun(boolean p_75504_1_) {
		noSunPathfind = p_75504_1_;
	}

	public void setSpeed(double p_75489_1_) {
		speed = p_75489_1_;
	}

	public void setCanSwim(boolean p_75495_1_) {
		canSwim = p_75495_1_;
	}

	public float getPathSearchRange() {
		return (float) pathSearchRange.getAttributeValue();
	}

	public PathEntity getPathToXYZ(double p_75488_1_, double p_75488_3_, double p_75488_5_) {
		return !canNavigate() ? null
				: worldObj.getEntityPathToXYZ(theEntity, MathHelper.floor_double(p_75488_1_), (int) p_75488_3_,
						MathHelper.floor_double(p_75488_5_), getPathSearchRange(), canPassOpenWoodenDoors,
						canPassClosedWoodenDoors, avoidsWater, canSwim);
	}

	public boolean tryMoveToXYZ(double p_75492_1_, double p_75492_3_, double p_75492_5_, double p_75492_7_) {
		PathEntity pathentity = getPathToXYZ(MathHelper.floor_double(p_75492_1_), (int) p_75492_3_,
				MathHelper.floor_double(p_75492_5_));
		return setPath(pathentity, p_75492_7_);
	}

	public PathEntity getPathToEntityLiving(Entity p_75494_1_) {
		return !canNavigate() ? null
				: worldObj.getPathEntityToEntity(theEntity, p_75494_1_, getPathSearchRange(), canPassOpenWoodenDoors,
						canPassClosedWoodenDoors, avoidsWater, canSwim);
	}

	public boolean tryMoveToEntityLiving(Entity p_75497_1_, double p_75497_2_) {
		PathEntity pathentity = getPathToEntityLiving(p_75497_1_);
		return pathentity != null ? setPath(pathentity, p_75497_2_) : false;
	}

	public boolean setPath(PathEntity p_75484_1_, double p_75484_2_) {
		if (p_75484_1_ == null) {
			currentPath = null;
			return false;
		} else {
			if (!p_75484_1_.isSamePath(currentPath)) {
				currentPath = p_75484_1_;
			}

			if (noSunPathfind) {
				removeSunnyPath();
			}

			if (currentPath.getCurrentPathLength() == 0)
				return false;
			else {
				speed = p_75484_2_;
				Vec3 vec3 = getEntityPosition();
				ticksAtLastPos = totalTicks;
				lastPosCheck.xCoord = vec3.xCoord;
				lastPosCheck.yCoord = vec3.yCoord;
				lastPosCheck.zCoord = vec3.zCoord;
				return true;
			}
		}
	}

	public PathEntity getPath() {
		return currentPath;
	}

	public void onUpdateNavigation() {
		++totalTicks;

		if (!noPath()) {
			if (canNavigate()) {
				pathFollow();
			}

			if (!noPath()) {
				Vec3 vec3 = currentPath.getPosition(theEntity);

				if (vec3 != null) {
					theEntity.getMoveHelper().setMoveTo(vec3.xCoord, vec3.yCoord, vec3.zCoord, speed);
				}
			}
		}
	}

	private void pathFollow() {
		Vec3 vec3 = getEntityPosition();
		int i = currentPath.getCurrentPathLength();

		for (int j = currentPath.getCurrentPathIndex(); j < currentPath.getCurrentPathLength(); ++j) {
			if (currentPath.getPathPointFromIndex(j).yCoord != (int) vec3.yCoord) {
				i = j;
				break;
			}
		}

		float f = theEntity.width * theEntity.width;
		int k;

		for (k = currentPath.getCurrentPathIndex(); k < i; ++k) {
			if (vec3.squareDistanceTo(currentPath.getVectorFromIndex(theEntity, k)) < f) {
				currentPath.setCurrentPathIndex(k + 1);
			}
		}

		k = MathHelper.ceiling_float_int(theEntity.width);
		int l = (int) theEntity.height + 1;
		int i1 = k;

		for (int j1 = i - 1; j1 >= currentPath.getCurrentPathIndex(); --j1) {
			if (isDirectPathBetweenPoints(vec3, currentPath.getVectorFromIndex(theEntity, j1), k, l, i1)) {
				currentPath.setCurrentPathIndex(j1);
				break;
			}
		}

		if (totalTicks - ticksAtLastPos > 100) {
			if (vec3.squareDistanceTo(lastPosCheck) < 2.25D) {
				clearPathEntity();
			}

			ticksAtLastPos = totalTicks;
			lastPosCheck.xCoord = vec3.xCoord;
			lastPosCheck.yCoord = vec3.yCoord;
			lastPosCheck.zCoord = vec3.zCoord;
		}
	}

	public boolean noPath() {
		return currentPath == null || currentPath.isFinished();
	}

	public void clearPathEntity() {
		currentPath = null;
	}

	private Vec3 getEntityPosition() {
		return Vec3.createVectorHelper(theEntity.posX, getPathableYPos(), theEntity.posZ);
	}

	private int getPathableYPos() {
		if (theEntity.isInWater() && canSwim) {
			int i = (int) theEntity.boundingBox.minY;
			Block block = worldObj.getBlock(MathHelper.floor_double(theEntity.posX), i,
					MathHelper.floor_double(theEntity.posZ));
			int j = 0;

			do {
				if (block != Blocks.flowing_water && block != Blocks.water)
					return i;

				++i;
				block = worldObj.getBlock(MathHelper.floor_double(theEntity.posX), i,
						MathHelper.floor_double(theEntity.posZ));
				++j;
			} while (j <= 16);

			return (int) theEntity.boundingBox.minY;
		} else
			return (int) (theEntity.boundingBox.minY + 0.5D);
	}

	private boolean canNavigate() {
		return theEntity.onGround || canSwim && isInLiquid() || theEntity.isRiding()
				&& theEntity instanceof EntityZombie && theEntity.ridingEntity instanceof EntityChicken;
	}

	private boolean isInLiquid() {
		return theEntity.isInWater() || theEntity.handleLavaMovement();
	}

	private void removeSunnyPath() {
		if (!worldObj.canBlockSeeTheSky(MathHelper.floor_double(theEntity.posX),
				(int) (theEntity.boundingBox.minY + 0.5D), MathHelper.floor_double(theEntity.posZ))) {
			for (int i = 0; i < currentPath.getCurrentPathLength(); ++i) {
				PathPoint pathpoint = currentPath.getPathPointFromIndex(i);

				if (worldObj.canBlockSeeTheSky(pathpoint.xCoord, pathpoint.yCoord, pathpoint.zCoord)) {
					currentPath.setCurrentPathLength(i - 1);
					return;
				}
			}
		}
	}

	private boolean isDirectPathBetweenPoints(Vec3 p_75493_1_, Vec3 p_75493_2_, int p_75493_3_, int p_75493_4_,
			int p_75493_5_) {
		int l = MathHelper.floor_double(p_75493_1_.xCoord);
		int i1 = MathHelper.floor_double(p_75493_1_.zCoord);
		double d0 = p_75493_2_.xCoord - p_75493_1_.xCoord;
		double d1 = p_75493_2_.zCoord - p_75493_1_.zCoord;
		double d2 = d0 * d0 + d1 * d1;

		if (d2 < 1.0E-8D)
			return false;
		else {
			double d3 = 1.0D / Math.sqrt(d2);
			d0 *= d3;
			d1 *= d3;
			p_75493_3_ += 2;
			p_75493_5_ += 2;

			if (!isSafeToStandAt(l, (int) p_75493_1_.yCoord, i1, p_75493_3_, p_75493_4_, p_75493_5_, p_75493_1_, d0,
					d1))
				return false;
			else {
				p_75493_3_ -= 2;
				p_75493_5_ -= 2;
				double d4 = 1.0D / Math.abs(d0);
				double d5 = 1.0D / Math.abs(d1);
				double d6 = l * 1 - p_75493_1_.xCoord;
				double d7 = i1 * 1 - p_75493_1_.zCoord;

				if (d0 >= 0.0D) {
					++d6;
				}

				if (d1 >= 0.0D) {
					++d7;
				}

				d6 /= d0;
				d7 /= d1;
				int j1 = d0 < 0.0D ? -1 : 1;
				int k1 = d1 < 0.0D ? -1 : 1;
				int l1 = MathHelper.floor_double(p_75493_2_.xCoord);
				int i2 = MathHelper.floor_double(p_75493_2_.zCoord);
				int j2 = l1 - l;
				int k2 = i2 - i1;

				do {
					if (j2 * j1 <= 0 && k2 * k1 <= 0)
						return true;

					if (d6 < d7) {
						d6 += d4;
						l += j1;
						j2 = l1 - l;
					} else {
						d7 += d5;
						i1 += k1;
						k2 = i2 - i1;
					}
				} while (isSafeToStandAt(l, (int) p_75493_1_.yCoord, i1, p_75493_3_, p_75493_4_, p_75493_5_, p_75493_1_,
						d0, d1));

				return false;
			}
		}
	}

	private boolean isSafeToStandAt(int p_75483_1_, int p_75483_2_, int p_75483_3_, int p_75483_4_, int p_75483_5_,
			int p_75483_6_, Vec3 p_75483_7_, double p_75483_8_, double p_75483_10_) {
		int k1 = p_75483_1_ - p_75483_4_ / 2;
		int l1 = p_75483_3_ - p_75483_6_ / 2;

		if (!isPositionClear(k1, p_75483_2_, l1, p_75483_4_, p_75483_5_, p_75483_6_, p_75483_7_, p_75483_8_,
				p_75483_10_))
			return false;
		else {
			for (int i2 = k1; i2 < k1 + p_75483_4_; ++i2) {
				for (int j2 = l1; j2 < l1 + p_75483_6_; ++j2) {
					double d2 = i2 + 0.5D - p_75483_7_.xCoord;
					double d3 = j2 + 0.5D - p_75483_7_.zCoord;

					if (d2 * p_75483_8_ + d3 * p_75483_10_ >= 0.0D) {
						Block block = worldObj.getBlock(i2, p_75483_2_ - 1, j2);
						Material material = block.getMaterial();

						if (material == Material.air)
							return false;

						if (material == Material.water && !theEntity.isInWater())
							return false;

						if (material == Material.lava)
							return false;
					}
				}
			}

			return true;
		}
	}

	private boolean isPositionClear(int p_75496_1_, int p_75496_2_, int p_75496_3_, int p_75496_4_, int p_75496_5_,
			int p_75496_6_, Vec3 p_75496_7_, double p_75496_8_, double p_75496_10_) {
		for (int k1 = p_75496_1_; k1 < p_75496_1_ + p_75496_4_; ++k1) {
			for (int l1 = p_75496_2_; l1 < p_75496_2_ + p_75496_5_; ++l1) {
				for (int i2 = p_75496_3_; i2 < p_75496_3_ + p_75496_6_; ++i2) {
					double d2 = k1 + 0.5D - p_75496_7_.xCoord;
					double d3 = i2 + 0.5D - p_75496_7_.zCoord;

					if (d2 * p_75496_8_ + d3 * p_75496_10_ >= 0.0D) {
						Block block = worldObj.getBlock(k1, l1, i2);

						if (!block.getBlocksMovement(worldObj, k1, l1, i2))
							return false;
					}
				}
			}
		}

		return true;
	}
}