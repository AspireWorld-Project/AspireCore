package net.minecraft.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

import java.util.Random;

public class RandomPositionGenerator {
	private static final Vec3 staticVector = Vec3.createVectorHelper(0.0D, 0.0D, 0.0D);
	private static final String __OBFID = "CL_00001629";

	public static Vec3 findRandomTarget(EntityCreature p_75463_0_, int p_75463_1_, int p_75463_2_) {
		return findRandomTargetBlock(p_75463_0_, p_75463_1_, p_75463_2_, null);
	}

	public static Vec3 findRandomTargetBlockTowards(EntityCreature p_75464_0_, int p_75464_1_, int p_75464_2_,
			Vec3 p_75464_3_) {
		staticVector.xCoord = p_75464_3_.xCoord - p_75464_0_.posX;
		staticVector.yCoord = p_75464_3_.yCoord - p_75464_0_.posY;
		staticVector.zCoord = p_75464_3_.zCoord - p_75464_0_.posZ;
		return findRandomTargetBlock(p_75464_0_, p_75464_1_, p_75464_2_, staticVector);
	}

	public static Vec3 findRandomTargetBlockAwayFrom(EntityCreature p_75461_0_, int p_75461_1_, int p_75461_2_,
			Vec3 p_75461_3_) {
		staticVector.xCoord = p_75461_0_.posX - p_75461_3_.xCoord;
		staticVector.yCoord = p_75461_0_.posY - p_75461_3_.yCoord;
		staticVector.zCoord = p_75461_0_.posZ - p_75461_3_.zCoord;
		return findRandomTargetBlock(p_75461_0_, p_75461_1_, p_75461_2_, staticVector);
	}

	private static Vec3 findRandomTargetBlock(EntityCreature p_75462_0_, int p_75462_1_, int p_75462_2_,
			Vec3 p_75462_3_) {
		Random random = p_75462_0_.getRNG();
		boolean flag = false;
		int k = 0;
		int l = 0;
		int i1 = 0;
		float f = -99999.0F;
		boolean flag1;

		if (p_75462_0_.hasHome()) {
			double d0 = p_75462_0_.getHomePosition().getDistanceSquared(MathHelper.floor_double(p_75462_0_.posX),
					MathHelper.floor_double(p_75462_0_.posY), MathHelper.floor_double(p_75462_0_.posZ)) + 4.0F;
			double d1 = p_75462_0_.func_110174_bM() + p_75462_1_;
			flag1 = d0 < d1 * d1;
		} else {
			flag1 = false;
		}

		for (int l1 = 0; l1 < 10; ++l1) {
			int j1 = random.nextInt(2 * p_75462_1_) - p_75462_1_;
			int i2 = random.nextInt(2 * p_75462_2_) - p_75462_2_;
			int k1 = random.nextInt(2 * p_75462_1_) - p_75462_1_;

			if (p_75462_3_ == null || j1 * p_75462_3_.xCoord + k1 * p_75462_3_.zCoord >= 0.0D) {
				j1 += MathHelper.floor_double(p_75462_0_.posX);
				i2 += MathHelper.floor_double(p_75462_0_.posY);
				k1 += MathHelper.floor_double(p_75462_0_.posZ);

				if (!flag1 || p_75462_0_.isWithinHomeDistance(j1, i2, k1)) {
					float f1 = p_75462_0_.getBlockPathWeight(j1, i2, k1);

					if (f1 > f) {
						f = f1;
						k = j1;
						l = i2;
						i1 = k1;
						flag = true;
					}
				}
			}
		}

		if (flag)
			return Vec3.createVectorHelper(k, l, i1);
		else
			return null;
	}
}