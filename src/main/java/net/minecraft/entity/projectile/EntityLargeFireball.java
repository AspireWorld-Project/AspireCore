package net.minecraft.entity.projectile;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntityLargeFireball extends EntityFireball {
	public int field_92057_e = 1;
	private static final String __OBFID = "CL_00001719";

	public EntityLargeFireball(World p_i1767_1_) {
		super(p_i1767_1_);
	}

	@SideOnly(Side.CLIENT)
	public EntityLargeFireball(World p_i1768_1_, double p_i1768_2_, double p_i1768_4_, double p_i1768_6_,
			double p_i1768_8_, double p_i1768_10_, double p_i1768_12_) {
		super(p_i1768_1_, p_i1768_2_, p_i1768_4_, p_i1768_6_, p_i1768_8_, p_i1768_10_, p_i1768_12_);
	}

	public EntityLargeFireball(World p_i1769_1_, EntityLivingBase p_i1769_2_, double p_i1769_3_, double p_i1769_5_,
			double p_i1769_7_) {
		super(p_i1769_1_, p_i1769_2_, p_i1769_3_, p_i1769_5_, p_i1769_7_);
	}

	@Override
	protected void onImpact(MovingObjectPosition p_70227_1_) {
		if (!worldObj.isRemote) {
			if (p_70227_1_.entityHit != null) {
				p_70227_1_.entityHit.attackEntityFrom(DamageSource.causeFireballDamage(this, shootingEntity), 6.0F);
			}

			worldObj.newExplosion(null, posX, posY, posZ, field_92057_e, true,
					worldObj.getGameRules().getGameRuleBooleanValue("mobGriefing"));
			setDead();
		}
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound p_70014_1_) {
		super.writeEntityToNBT(p_70014_1_);
		p_70014_1_.setInteger("ExplosionPower", field_92057_e);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound p_70037_1_) {
		super.readEntityFromNBT(p_70037_1_);

		if (p_70037_1_.hasKey("ExplosionPower", 99)) {
			field_92057_e = p_70037_1_.getInteger("ExplosionPower");
		}
	}
}