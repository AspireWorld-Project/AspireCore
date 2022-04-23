package net.minecraft.entity.ai;

import net.minecraft.entity.passive.EntityAnimal;

import java.util.Iterator;
import java.util.List;

public class EntityAIFollowParent extends EntityAIBase {
	EntityAnimal childAnimal;
	EntityAnimal parentAnimal;
	double field_75347_c;
	private int field_75345_d;
	private static final String __OBFID = "CL_00001586";

	public EntityAIFollowParent(EntityAnimal p_i1626_1_, double p_i1626_2_) {
		childAnimal = p_i1626_1_;
		field_75347_c = p_i1626_2_;
	}

	@Override
	public boolean shouldExecute() {
		if (childAnimal.getGrowingAge() >= 0)
			return false;
		else {
			List list = childAnimal.worldObj.getEntitiesWithinAABB(childAnimal.getClass(),
					childAnimal.boundingBox.expand(8.0D, 4.0D, 8.0D));
			EntityAnimal entityanimal = null;
			double d0 = Double.MAX_VALUE;
			Iterator iterator = list.iterator();

			while (iterator.hasNext()) {
				EntityAnimal entityanimal1 = (EntityAnimal) iterator.next();

				if (entityanimal1.getGrowingAge() >= 0) {
					double d1 = childAnimal.getDistanceSqToEntity(entityanimal1);

					if (d1 <= d0) {
						d0 = d1;
						entityanimal = entityanimal1;
					}
				}
			}

			if (entityanimal == null)
				return false;
			else if (d0 < 9.0D)
				return false;
			else {
				parentAnimal = entityanimal;
				return true;
			}
		}
	}

	@Override
	public boolean continueExecuting() {
		if (!parentAnimal.isEntityAlive())
			return false;
		else {
			double d0 = childAnimal.getDistanceSqToEntity(parentAnimal);
			return d0 >= 9.0D && d0 <= 256.0D;
		}
	}

	@Override
	public void startExecuting() {
		field_75345_d = 0;
	}

	@Override
	public void resetTask() {
		parentAnimal = null;
	}

	@Override
	public void updateTask() {
		if (--field_75345_d <= 0) {
			field_75345_d = 10;
			childAnimal.getNavigator().tryMoveToEntityLiving(parentAnimal, field_75347_c);
		}
	}
}