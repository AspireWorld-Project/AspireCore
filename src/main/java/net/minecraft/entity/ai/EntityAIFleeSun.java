package net.minecraft.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import java.util.Random;

public class EntityAIFleeSun extends EntityAIBase {
	private final EntityCreature theCreature;
	private double shelterX;
	private double shelterY;
	private double shelterZ;
	private final double movementSpeed;
	private final World theWorld;
	private static final String __OBFID = "CL_00001583";

	public EntityAIFleeSun(EntityCreature p_i1623_1_, double p_i1623_2_) {
		theCreature = p_i1623_1_;
		movementSpeed = p_i1623_2_;
		theWorld = p_i1623_1_.worldObj;
		setMutexBits(1);
	}

	@Override
	public boolean shouldExecute() {
		if (!theWorld.isDaytime())
			return false;
		else if (!theCreature.isBurning())
			return false;
		else if (!theWorld.canBlockSeeTheSky(MathHelper.floor_double(theCreature.posX),
				(int) theCreature.boundingBox.minY, MathHelper.floor_double(theCreature.posZ)))
			return false;
		else {
			Vec3 vec3 = findPossibleShelter();

			if (vec3 == null)
				return false;
			else {
				shelterX = vec3.xCoord;
				shelterY = vec3.yCoord;
				shelterZ = vec3.zCoord;
				return true;
			}
		}
	}

	@Override
	public boolean continueExecuting() {
		return !theCreature.getNavigator().noPath();
	}

	@Override
	public void startExecuting() {
		theCreature.getNavigator().tryMoveToXYZ(shelterX, shelterY, shelterZ, movementSpeed);
	}

	private Vec3 findPossibleShelter() {
		Random random = theCreature.getRNG();

		for (int i = 0; i < 10; ++i) {
			int j = MathHelper.floor_double(theCreature.posX + random.nextInt(20) - 10.0D);
			int k = MathHelper.floor_double(theCreature.boundingBox.minY + random.nextInt(6) - 3.0D);
			int l = MathHelper.floor_double(theCreature.posZ + random.nextInt(20) - 10.0D);

			if (!theWorld.canBlockSeeTheSky(j, k, l) && theCreature.getBlockPathWeight(j, k, l) < 0.0F)
				return Vec3.createVectorHelper(j, k, l);
		}

		return null;
	}
}