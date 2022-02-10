package net.minecraft.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.util.Vec3;

public class EntityAIPanic extends EntityAIBase {
	private EntityCreature theEntityCreature;
	private double speed;
	private double randPosX;
	private double randPosY;
	private double randPosZ;
	private static final String __OBFID = "CL_00001604";

	public EntityAIPanic(EntityCreature p_i1645_1_, double p_i1645_2_) {
		theEntityCreature = p_i1645_1_;
		speed = p_i1645_2_;
		setMutexBits(1);
	}

	@Override
	public boolean shouldExecute() {
		if (theEntityCreature.getAITarget() == null && !theEntityCreature.isBurning())
			return false;
		else {
			Vec3 vec3 = RandomPositionGenerator.findRandomTarget(theEntityCreature, 5, 4);

			if (vec3 == null)
				return false;
			else {
				randPosX = vec3.xCoord;
				randPosY = vec3.yCoord;
				randPosZ = vec3.zCoord;
				return true;
			}
		}
	}

	@Override
	public void startExecuting() {
		theEntityCreature.getNavigator().tryMoveToXYZ(randPosX, randPosY, randPosZ, speed);
	}

	@Override
	public boolean continueExecuting() {
		return !theEntityCreature.getNavigator().noPath();
	}
}