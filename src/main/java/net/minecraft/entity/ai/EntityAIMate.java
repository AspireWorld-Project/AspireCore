package net.minecraft.entity.ai;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.stats.AchievementList;
import net.minecraft.stats.StatList;
import net.minecraft.world.World;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class EntityAIMate extends EntityAIBase {
	private final EntityAnimal theAnimal;
	World theWorld;
	private EntityAnimal targetMate;
	int spawnBabyDelay;
	double moveSpeed;
	private static final String __OBFID = "CL_00001578";

	public EntityAIMate(EntityAnimal p_i1619_1_, double p_i1619_2_) {
		theAnimal = p_i1619_1_;
		theWorld = p_i1619_1_.worldObj;
		moveSpeed = p_i1619_2_;
		setMutexBits(3);
	}

	@Override
	public boolean shouldExecute() {
		if (!theAnimal.isInLove())
			return false;
		else {
			targetMate = getNearbyMate();
			return targetMate != null;
		}
	}

	@Override
	public boolean continueExecuting() {
		return targetMate.isEntityAlive() && targetMate.isInLove() && spawnBabyDelay < 60;
	}

	@Override
	public void resetTask() {
		targetMate = null;
		spawnBabyDelay = 0;
	}

	@Override
	public void updateTask() {
		theAnimal.getLookHelper().setLookPositionWithEntity(targetMate, 10.0F, theAnimal.getVerticalFaceSpeed());
		theAnimal.getNavigator().tryMoveToEntityLiving(targetMate, moveSpeed);
		++spawnBabyDelay;

		if (spawnBabyDelay >= 60 && theAnimal.getDistanceSqToEntity(targetMate) < 9.0D) {
			spawnBaby();
		}
	}

	private EntityAnimal getNearbyMate() {
		float f = 8.0F;
		List list = theWorld.getEntitiesWithinAABB(theAnimal.getClass(), theAnimal.boundingBox.expand(f, f, f));
		double d0 = Double.MAX_VALUE;
		EntityAnimal entityanimal = null;
		Iterator iterator = list.iterator();

		while (iterator.hasNext()) {
			EntityAnimal entityanimal1 = (EntityAnimal) iterator.next();

			if (theAnimal.canMateWith(entityanimal1) && theAnimal.getDistanceSqToEntity(entityanimal1) < d0) {
				entityanimal = entityanimal1;
				d0 = theAnimal.getDistanceSqToEntity(entityanimal1);
			}
		}

		return entityanimal;
	}

	private void spawnBaby() {
		EntityAgeable entityageable = theAnimal.createChild(targetMate);

		if (entityageable != null) {
			EntityPlayer entityplayer = theAnimal.func_146083_cb();

			if (entityplayer == null && targetMate.func_146083_cb() != null) {
				entityplayer = targetMate.func_146083_cb();
			}

			if (entityplayer != null) {
				entityplayer.triggerAchievement(StatList.field_151186_x);

				if (theAnimal instanceof EntityCow) {
					entityplayer.triggerAchievement(AchievementList.field_150962_H);
				}
			}

			theAnimal.setGrowingAge(6000);
			targetMate.setGrowingAge(6000);
			theAnimal.resetInLove();
			targetMate.resetInLove();
			entityageable.setGrowingAge(-24000);
			entityageable.setLocationAndAngles(theAnimal.posX, theAnimal.posY, theAnimal.posZ, 0.0F, 0.0F);
			theWorld.spawnEntityInWorld(entityageable);
			Random random = theAnimal.getRNG();

			for (int i = 0; i < 7; ++i) {
				double d0 = random.nextGaussian() * 0.02D;
				double d1 = random.nextGaussian() * 0.02D;
				double d2 = random.nextGaussian() * 0.02D;
				theWorld.spawnParticle("heart",
						theAnimal.posX + random.nextFloat() * theAnimal.width * 2.0F - theAnimal.width,
						theAnimal.posY + 0.5D + random.nextFloat() * theAnimal.height,
						theAnimal.posZ + random.nextFloat() * theAnimal.width * 2.0F - theAnimal.width, d0, d1, d2);
			}

			if (theWorld.getGameRules().getGameRuleBooleanValue("doMobLoot")) {
				theWorld.spawnEntityInWorld(new EntityXPOrb(theWorld, theAnimal.posX, theAnimal.posY, theAnimal.posZ,
						random.nextInt(7) + 1));
			}
		}
	}
}