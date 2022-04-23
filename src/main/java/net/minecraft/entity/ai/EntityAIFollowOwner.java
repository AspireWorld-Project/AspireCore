package net.minecraft.entity.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityAIFollowOwner extends EntityAIBase {
	private final EntityTameable thePet;
	private EntityLivingBase theOwner;
	World theWorld;
	private final double field_75336_f;
	private final PathNavigate petPathfinder;
	private int field_75343_h;
	float maxDist;
	float minDist;
	private boolean field_75344_i;
	private static final String __OBFID = "CL_00001585";

	public EntityAIFollowOwner(EntityTameable p_i1625_1_, double p_i1625_2_, float p_i1625_4_, float p_i1625_5_) {
		thePet = p_i1625_1_;
		theWorld = p_i1625_1_.worldObj;
		field_75336_f = p_i1625_2_;
		petPathfinder = p_i1625_1_.getNavigator();
		minDist = p_i1625_4_;
		maxDist = p_i1625_5_;
		setMutexBits(3);
	}

	@Override
	public boolean shouldExecute() {
		EntityLivingBase entitylivingbase = thePet.getOwner();

		if (entitylivingbase == null)
			return false;
		else if (thePet.isSitting())
			return false;
		else if (thePet.getDistanceSqToEntity(entitylivingbase) < minDist * minDist)
			return false;
		else {
			theOwner = entitylivingbase;
			return true;
		}
	}

	@Override
	public boolean continueExecuting() {
		return !petPathfinder.noPath() && thePet.getDistanceSqToEntity(theOwner) > maxDist * maxDist
				&& !thePet.isSitting();
	}

	@Override
	public void startExecuting() {
		field_75343_h = 0;
		field_75344_i = thePet.getNavigator().getAvoidsWater();
		thePet.getNavigator().setAvoidsWater(false);
	}

	@Override
	public void resetTask() {
		theOwner = null;
		petPathfinder.clearPathEntity();
		thePet.getNavigator().setAvoidsWater(field_75344_i);
	}

	@Override
	public void updateTask() {
		thePet.getLookHelper().setLookPositionWithEntity(theOwner, 10.0F, thePet.getVerticalFaceSpeed());

		if (!thePet.isSitting()) {
			if (--field_75343_h <= 0) {
				field_75343_h = 10;

				if (!petPathfinder.tryMoveToEntityLiving(theOwner, field_75336_f)) {
					if (!thePet.getLeashed()) {
						if (thePet.getDistanceSqToEntity(theOwner) >= 144.0D) {
							int i = MathHelper.floor_double(theOwner.posX) - 2;
							int j = MathHelper.floor_double(theOwner.posZ) - 2;
							int k = MathHelper.floor_double(theOwner.boundingBox.minY);

							for (int l = 0; l <= 4; ++l) {
								for (int i1 = 0; i1 <= 4; ++i1) {
									if ((l < 1 || i1 < 1 || l > 3 || i1 > 3)
											&& World.doesBlockHaveSolidTopSurface(theWorld, i + l, k - 1, j + i1)
											&& !theWorld.getBlock(i + l, k, j + i1).isNormalCube()
											&& !theWorld.getBlock(i + l, k + 1, j + i1).isNormalCube()) {
										thePet.setLocationAndAngles(i + l + 0.5F, k, j + i1 + 0.5F, thePet.rotationYaw,
												thePet.rotationPitch);
										petPathfinder.clearPathEntity();
										return;
									}
								}
							}
						}
					}
				}
			}
		}
	}
}