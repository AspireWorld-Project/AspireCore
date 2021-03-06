package net.minecraft.entity.projectile;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class EntityWitherSkull extends EntityFireball {
	private static final String __OBFID = "CL_00001728";

	public EntityWitherSkull(World p_i1793_1_) {
		super(p_i1793_1_);
		setSize(0.3125F, 0.3125F);
	}

	public EntityWitherSkull(World p_i1794_1_, EntityLivingBase p_i1794_2_, double p_i1794_3_, double p_i1794_5_,
			double p_i1794_7_) {
		super(p_i1794_1_, p_i1794_2_, p_i1794_3_, p_i1794_5_, p_i1794_7_);
		setSize(0.3125F, 0.3125F);
	}

	@Override
	protected float getMotionFactor() {
		return isInvulnerable() ? 0.73F : super.getMotionFactor();
	}

	@SideOnly(Side.CLIENT)
	public EntityWitherSkull(World p_i1795_1_, double p_i1795_2_, double p_i1795_4_, double p_i1795_6_,
			double p_i1795_8_, double p_i1795_10_, double p_i1795_12_) {
		super(p_i1795_1_, p_i1795_2_, p_i1795_4_, p_i1795_6_, p_i1795_8_, p_i1795_10_, p_i1795_12_);
		setSize(0.3125F, 0.3125F);
	}

	@Override
	public boolean isBurning() {
		return false;
	}

	@Override
	public float func_145772_a(Explosion p_145772_1_, World p_145772_2_, int p_145772_3_, int p_145772_4_,
			int p_145772_5_, Block p_145772_6_) {
		float f = super.func_145772_a(p_145772_1_, p_145772_2_, p_145772_3_, p_145772_4_, p_145772_5_, p_145772_6_);

		if (isInvulnerable() && p_145772_6_ != Blocks.bedrock && p_145772_6_ != Blocks.end_portal
				&& p_145772_6_ != Blocks.end_portal_frame && p_145772_6_ != Blocks.command_block) {
			f = Math.min(0.8F, f);
		}

		return f;
	}

	@Override
	protected void onImpact(MovingObjectPosition p_70227_1_) {
		if (!worldObj.isRemote) {
			if (p_70227_1_.entityHit != null) {
				if (shootingEntity != null) {
					if (p_70227_1_.entityHit.attackEntityFrom(DamageSource.causeMobDamage(shootingEntity), 8.0F)
							&& !p_70227_1_.entityHit.isEntityAlive()) {
						shootingEntity.heal(5.0F, org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason.WITHER);
					}
				} else {
					p_70227_1_.entityHit.attackEntityFrom(DamageSource.magic, 5.0F);
				}

				if (p_70227_1_.entityHit instanceof EntityLivingBase) {
					byte b0 = 0;

					if (worldObj.difficultySetting == EnumDifficulty.NORMAL) {
						b0 = 10;
					} else if (worldObj.difficultySetting == EnumDifficulty.HARD) {
						b0 = 40;
					}

					if (b0 > 0) {
						((EntityLivingBase) p_70227_1_.entityHit)
								.addPotionEffect(new PotionEffect(Potion.wither.id, 20 * b0, 1));
					}
				}
			}

			worldObj.newExplosion(this, posX, posY, posZ, 1.0F, false,
					worldObj.getGameRules().getGameRuleBooleanValue("mobGriefing"));
			setDead();
		}
	}

	@Override
	public boolean canBeCollidedWith() {
		return false;
	}

	@Override
	public boolean attackEntityFrom(DamageSource p_70097_1_, float p_70097_2_) {
		return false;
	}

	@Override
	protected void entityInit() {
		dataWatcher.addObject(10, Byte.valueOf((byte) 0));
	}

	public boolean isInvulnerable() {
		return dataWatcher.getWatchableObjectByte(10) == 1;
	}

	public void setInvulnerable(boolean p_82343_1_) {
		dataWatcher.updateObject(10, Byte.valueOf((byte) (p_82343_1_ ? 1 : 0)));
	}
}