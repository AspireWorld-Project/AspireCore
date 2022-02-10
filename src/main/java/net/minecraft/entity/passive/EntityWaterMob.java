package net.minecraft.entity.passive;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public abstract class EntityWaterMob extends EntityCreature implements IAnimals {
	private static final String __OBFID = "CL_00001653";

	public EntityWaterMob(World p_i1695_1_) {
		super(p_i1695_1_);
	}

	@Override
	public boolean canBreatheUnderwater() {
		return true;
	}

	@Override
	public boolean getCanSpawnHere() {
		return worldObj.checkNoEntityCollision(boundingBox);
	}

	@Override
	public int getTalkInterval() {
		return 120;
	}

	@Override
	protected boolean canDespawn() {
		return true;
	}

	@Override
	protected int getExperiencePoints(EntityPlayer p_70693_1_) {
		return 1 + worldObj.rand.nextInt(3);
	}

	@Override
	public void onEntityUpdate() {
		int i = getAir();
		super.onEntityUpdate();

		if (isEntityAlive() && !isInWater()) {
			--i;
			setAir(i);

			if (getAir() == -20) {
				setAir(0);
				attackEntityFrom(DamageSource.drown, 2.0F);
			}
		} else {
			setAir(300);
		}
	}
}