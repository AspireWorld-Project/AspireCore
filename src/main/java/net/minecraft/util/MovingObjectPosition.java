package net.minecraft.util;

import net.minecraft.entity.Entity;

public class MovingObjectPosition {
	public MovingObjectPosition.MovingObjectType typeOfHit;
	public int blockX;
	public int blockY;
	public int blockZ;
	public int sideHit;
	public Vec3 hitVec;
	public Entity entityHit;
	/** Used to determine what sub-segment is hit */
	public int subHit = -1;

	/** Used to add extra hit info */
	public Object hitInfo = null;

	public MovingObjectPosition(int p_i2303_1_, int p_i2303_2_, int p_i2303_3_, int p_i2303_4_, Vec3 p_i2303_5_) {
		this(p_i2303_1_, p_i2303_2_, p_i2303_3_, p_i2303_4_, p_i2303_5_, true);
	}

	public MovingObjectPosition(int p_i45481_1_, int p_i45481_2_, int p_i45481_3_, int p_i45481_4_, Vec3 p_i45481_5_,
			boolean p_i45481_6_) {
		typeOfHit = p_i45481_6_ ? MovingObjectPosition.MovingObjectType.BLOCK
				: MovingObjectPosition.MovingObjectType.MISS;
		blockX = p_i45481_1_;
		blockY = p_i45481_2_;
		blockZ = p_i45481_3_;
		sideHit = p_i45481_4_;
		hitVec = Vec3.createVectorHelper(p_i45481_5_.xCoord, p_i45481_5_.yCoord, p_i45481_5_.zCoord);
	}

	public MovingObjectPosition(Entity p_i2304_1_) {
		this(p_i2304_1_, Vec3.createVectorHelper(p_i2304_1_.posX, p_i2304_1_.posY, p_i2304_1_.posZ));
	}

	public MovingObjectPosition(Entity p_i45482_1_, Vec3 p_i45482_2_) {
		typeOfHit = MovingObjectPosition.MovingObjectType.ENTITY;
		entityHit = p_i45482_1_;
		hitVec = p_i45482_2_;
	}

	@Override
	public String toString() {
		return "HitResult{type=" + typeOfHit + ", x=" + blockX + ", y=" + blockY + ", z=" + blockZ + ", f=" + sideHit
				+ ", pos=" + hitVec + ", entity=" + entityHit + '}';
	}

	public enum MovingObjectType {
		MISS, BLOCK, ENTITY
	}
}