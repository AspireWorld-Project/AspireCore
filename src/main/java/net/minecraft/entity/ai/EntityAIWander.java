package net.minecraft.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.util.Vec3;

public class EntityAIWander extends EntityAIBase {
	private final EntityCreature entity;
	private double xPosition;
	private double yPosition;
	private double zPosition;
	private final double speed;
	private static final String __OBFID = "CL_00001608";

	public EntityAIWander(EntityCreature p_i1648_1_, double p_i1648_2_) {
		entity = p_i1648_1_;
		speed = p_i1648_2_;
		setMutexBits(1);
	}

	@Override
	public boolean shouldExecute() {
		if (entity.getAge() >= 100)
			return false;
		else if (entity.getRNG().nextInt(120) != 0)
			return false;
		else {
			Vec3 vec3 = RandomPositionGenerator.findRandomTarget(entity, 10, 7);

			if (vec3 == null)
				return false;
			else {
				xPosition = vec3.xCoord;
				yPosition = vec3.yCoord;
				zPosition = vec3.zCoord;
				return true;
			}
		}
	}

	@Override
	public boolean continueExecuting() {
		return !entity.getNavigator().noPath();
	}

	@Override
	public void startExecuting() {
		entity.getNavigator().tryMoveToXYZ(xPosition, yPosition, zPosition, speed);
	}
}