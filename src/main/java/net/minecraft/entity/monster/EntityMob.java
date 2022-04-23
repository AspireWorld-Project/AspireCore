package net.minecraft.entity.monster;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.event.entity.EntityTargetEvent;

public abstract class EntityMob extends EntityCreature implements IMob {
	private static final String __OBFID = "CL_00001692";

	public EntityMob(World p_i1738_1_) {
		super(p_i1738_1_);
		experienceValue = 5;
	}

	@Override
	public void onLivingUpdate() {
		updateArmSwingProgress();
		float f = getBrightness(1.0F);

		if (f > 0.5F) {
			entityAge += 2;
		}

		super.onLivingUpdate();
	}

	@Override
	public void onUpdate() {
		super.onUpdate();

		if (!worldObj.isRemote && worldObj.difficultySetting == EnumDifficulty.PEACEFUL) {
			setDead();
		}
	}

	@Override
	protected String getSwimSound() {
		return "game.hostile.swim";
	}

	@Override
	protected String getSplashSound() {
		return "game.hostile.swim.splash";
	}

	@Override
	protected Entity findPlayerToAttack() {
		EntityPlayer entityplayer = worldObj.getClosestVulnerablePlayerToEntity(this, 16.0D);
		return entityplayer != null && canEntityBeSeen(entityplayer) ? entityplayer : null;
	}

	@Override
	public boolean attackEntityFrom(DamageSource p_70097_1_, float p_70097_2_) {
		if (isEntityInvulnerable())
			return false;
		else if (super.attackEntityFrom(p_70097_1_, p_70097_2_)) {
			Entity entity = p_70097_1_.getEntity();

			if (riddenByEntity != entity && ridingEntity != entity) {
				if (entity != this) {
					if (entity != entityToAttack && (this instanceof EntityBlaze || this instanceof EntityEnderman
							|| this instanceof EntitySpider || this instanceof EntityGiantZombie
							|| this instanceof EntitySilverfish)) {
						EntityTargetEvent event = CraftEventFactory.callEntityTargetEvent(this, entity,
								EntityTargetEvent.TargetReason.TARGET_ATTACKED_ENTITY);
						if (!event.isCancelled())
							if (event.getTarget() == null) {
								entityToAttack = null;
							} else {
								entityToAttack = ((CraftEntity) event.getTarget()).getHandle();
							}
					} else {
						entityToAttack = entity;
					}
				}

				return true;
			} else
				return true;
		} else
			return false;
	}

	@Override
	protected String getHurtSound() {
		return "game.hostile.hurt";
	}

	@Override
	protected String getDeathSound() {
		return "game.hostile.die";
	}

	@Override
	protected String func_146067_o(int p_146067_1_) {
		return p_146067_1_ > 4 ? "game.hostile.hurt.fall.big" : "game.hostile.hurt.fall.small";
	}

	@Override
	public boolean attackEntityAsMob(Entity p_70652_1_) {
		float f = (float) getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue();
		int i = 0;

		if (p_70652_1_ instanceof EntityLivingBase) {
			f += EnchantmentHelper.getEnchantmentModifierLiving(this, (EntityLivingBase) p_70652_1_);
			i += EnchantmentHelper.getKnockbackModifier(this, (EntityLivingBase) p_70652_1_);
		}

		boolean flag = p_70652_1_.attackEntityFrom(DamageSource.causeMobDamage(this), f);

		if (flag) {
			if (i > 0) {
				p_70652_1_.addVelocity(-MathHelper.sin(rotationYaw * (float) Math.PI / 180.0F) * i * 0.5F, 0.1D,
						MathHelper.cos(rotationYaw * (float) Math.PI / 180.0F) * i * 0.5F);
				motionX *= 0.6D;
				motionZ *= 0.6D;
			}

			int j = EnchantmentHelper.getFireAspectModifier(this);

			if (j > 0) {
				p_70652_1_.setFire(j * 4);
			}

			if (p_70652_1_ instanceof EntityLivingBase) {
				EnchantmentHelper.func_151384_a((EntityLivingBase) p_70652_1_, this);
			}

			EnchantmentHelper.func_151385_b(this, p_70652_1_);
		}

		return flag;
	}

	@Override
	protected void attackEntity(Entity p_70785_1_, float p_70785_2_) {
		if (attackTime <= 0 && p_70785_2_ < 2.0F && p_70785_1_.boundingBox.maxY > boundingBox.minY
				&& p_70785_1_.boundingBox.minY < boundingBox.maxY) {
			attackTime = 20;
			attackEntityAsMob(p_70785_1_);
		}
	}

	@Override
	public float getBlockPathWeight(int p_70783_1_, int p_70783_2_, int p_70783_3_) {
		return 0.5F - worldObj.getLightBrightness(p_70783_1_, p_70783_2_, p_70783_3_);
	}

	protected boolean isValidLightLevel() {
		int i = MathHelper.floor_double(posX);
		int j = MathHelper.floor_double(boundingBox.minY);
		int k = MathHelper.floor_double(posZ);

		if (worldObj.getSavedLightValue(EnumSkyBlock.Sky, i, j, k) > rand.nextInt(32))
			return false;
		else {
			int l = worldObj.getBlockLightValue(i, j, k);

			if (worldObj.isThundering()) {
				int i1 = worldObj.skylightSubtracted;
				worldObj.skylightSubtracted = 10;
				l = worldObj.getBlockLightValue(i, j, k);
				worldObj.skylightSubtracted = i1;
			}

			return l <= rand.nextInt(8);
		}
	}

	@Override
	public boolean getCanSpawnHere() {
		return worldObj.difficultySetting != EnumDifficulty.PEACEFUL && isValidLightLevel() && super.getCanSpawnHere();
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		getAttributeMap().registerAttribute(SharedMonsterAttributes.attackDamage);
	}

	@Override
	protected boolean func_146066_aG() {
		return true;
	}
}