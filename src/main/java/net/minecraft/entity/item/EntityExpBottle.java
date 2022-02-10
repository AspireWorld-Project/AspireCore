package net.minecraft.entity.item;

import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.event.entity.ExpBottleEvent;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntityExpBottle extends EntityThrowable {
	private static final String __OBFID = "CL_00001726";

	public EntityExpBottle(World p_i1785_1_) {
		super(p_i1785_1_);
	}

	public EntityExpBottle(World p_i1786_1_, EntityLivingBase p_i1786_2_) {
		super(p_i1786_1_, p_i1786_2_);
	}

	public EntityExpBottle(World p_i1787_1_, double p_i1787_2_, double p_i1787_4_, double p_i1787_6_) {
		super(p_i1787_1_, p_i1787_2_, p_i1787_4_, p_i1787_6_);
	}

	@Override
	protected float getGravityVelocity() {
		return 0.07F;
	}

	@Override
	protected float func_70182_d() {
		return 0.7F;
	}

	@Override
	protected float func_70183_g() {
		return -20.0F;
	}

	@Override
	protected void onImpact(MovingObjectPosition p_70184_1_) {
		if (!worldObj.isRemote) {
			int i = 3 + worldObj.rand.nextInt(5) + worldObj.rand.nextInt(5);
			ExpBottleEvent event = CraftEventFactory.callExpBottleEvent(this, i);
			i = event.getExperience();
			if (event.getShowEffect()) {
				worldObj.playAuxSFX(2002, (int) Math.round(posX), (int) Math.round(posY), (int) Math.round(posZ), 0);
			}
			while (i > 0) {
				int j = EntityXPOrb.getXPSplit(i);
				i -= j;
				worldObj.spawnEntityInWorld(new EntityXPOrb(worldObj, posX, posY, posZ, j));
			}
			setDead();
		}
	}
}