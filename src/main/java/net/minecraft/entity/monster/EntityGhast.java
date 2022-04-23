package net.minecraft.entity.monster;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityFlying;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.AchievementList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.event.entity.EntityTargetEvent;

public class EntityGhast extends EntityFlying implements IMob {
	public int courseChangeCooldown;
	public double waypointX;
	public double waypointY;
	public double waypointZ;
	private Entity targetedEntity;
	private int aggroCooldown;
	public int prevAttackCounter;
	public int attackCounter;
	private int explosionStrength = 1;
	private static final String __OBFID = "CL_00001689";

	public EntityGhast(World p_i1735_1_) {
		super(p_i1735_1_);
		setSize(4.0F, 4.0F);
		isImmuneToFire = true;
		experienceValue = 5;
	}

	@SideOnly(Side.CLIENT)
	public boolean func_110182_bF() {
		return dataWatcher.getWatchableObjectByte(16) != 0;
	}

	@Override
	public boolean attackEntityFrom(DamageSource p_70097_1_, float p_70097_2_) {
		if (isEntityInvulnerable())
			return false;
		else if ("fireball".equals(p_70097_1_.getDamageType()) && p_70097_1_.getEntity() instanceof EntityPlayer) {
			super.attackEntityFrom(p_70097_1_, 1000.0F);
			((EntityPlayer) p_70097_1_.getEntity()).triggerAchievement(AchievementList.ghast);
			return true;
		} else
			return super.attackEntityFrom(p_70097_1_, p_70097_2_);
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		dataWatcher.addObject(16, Byte.valueOf((byte) 0));
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(10.0D);
	}

	@Override
	protected void updateEntityActionState() {
		if (!worldObj.isRemote && worldObj.difficultySetting == EnumDifficulty.PEACEFUL) {
			setDead();
		}

		despawnEntity();
		prevAttackCounter = attackCounter;
		double d0 = waypointX - posX;
		double d1 = waypointY - posY;
		double d2 = waypointZ - posZ;
		double d3 = d0 * d0 + d1 * d1 + d2 * d2;

		if (d3 < 1.0D || d3 > 3600.0D) {
			waypointX = posX + (rand.nextFloat() * 2.0F - 1.0F) * 16.0F;
			waypointY = posY + (rand.nextFloat() * 2.0F - 1.0F) * 16.0F;
			waypointZ = posZ + (rand.nextFloat() * 2.0F - 1.0F) * 16.0F;
		}

		if (courseChangeCooldown-- <= 0) {
			courseChangeCooldown += rand.nextInt(5) + 2;
			d3 = MathHelper.sqrt_double(d3);

			if (isCourseTraversable(waypointX, waypointY, waypointZ, d3)) {
				motionX += d0 / d3 * 0.1D;
				motionY += d1 / d3 * 0.1D;
				motionZ += d2 / d3 * 0.1D;
			} else {
				waypointX = posX;
				waypointY = posY;
				waypointZ = posZ;
			}
		}

		if (targetedEntity != null && targetedEntity.isDead) {
			EntityTargetEvent event = new EntityTargetEvent(this.getBukkitEntity(), null,
					EntityTargetEvent.TargetReason.TARGET_DIED);
			Bukkit.getPluginManager().callEvent(event);
			if (!event.isCancelled()) {
				targetedEntity = event.getTarget() == null ? null : ((CraftEntity) event.getTarget()).getHandle();
			}
		}

		if (targetedEntity == null || aggroCooldown-- <= 0) {
			Entity target = worldObj.getClosestVulnerablePlayerToEntity(this, 100.0D);
			if (target != null) {
				EntityTargetEvent event = new EntityTargetEvent(this.getBukkitEntity(), target.getBukkitEntity(),
						EntityTargetEvent.TargetReason.CLOSEST_PLAYER);
				Bukkit.getPluginManager().callEvent(event);
				if (!event.isCancelled()) {
					targetedEntity = event.getTarget() == null ? null : ((CraftEntity) event.getTarget()).getHandle();
				}
			}

			if (targetedEntity != null) {
				aggroCooldown = 20;
			}
		}

		double d4 = 64.0D;

		if (targetedEntity != null && targetedEntity.getDistanceSqToEntity(this) < d4 * d4) {
			double d5 = targetedEntity.posX - posX;
			double d6 = targetedEntity.boundingBox.minY + targetedEntity.height / 2.0F - (posY + height / 2.0F);
			double d7 = targetedEntity.posZ - posZ;
			renderYawOffset = rotationYaw = -((float) Math.atan2(d5, d7)) * 180.0F / (float) Math.PI;

			if (canEntityBeSeen(targetedEntity)) {
				if (attackCounter == 10) {
					worldObj.playAuxSFXAtEntity((EntityPlayer) null, 1007, (int) posX, (int) posY, (int) posZ, 0);
				}

				++attackCounter;

				if (attackCounter == 20) {
					worldObj.playAuxSFXAtEntity((EntityPlayer) null, 1008, (int) posX, (int) posY, (int) posZ, 0);
					EntityLargeFireball entitylargefireball = new EntityLargeFireball(worldObj, this, d5, d6, d7);
					entitylargefireball.field_92057_e = explosionStrength;
					double d8 = 4.0D;
					Vec3 vec3 = getLook(1.0F);
					entitylargefireball.posX = posX + vec3.xCoord * d8;
					entitylargefireball.posY = posY + height / 2.0F + 0.5D;
					entitylargefireball.posZ = posZ + vec3.zCoord * d8;
					worldObj.spawnEntityInWorld(entitylargefireball);
					attackCounter = -40;
				}
			} else if (attackCounter > 0) {
				--attackCounter;
			}
		} else {
			renderYawOffset = rotationYaw = -((float) Math.atan2(motionX, motionZ)) * 180.0F / (float) Math.PI;

			if (attackCounter > 0) {
				--attackCounter;
			}
		}

		if (!worldObj.isRemote) {
			byte b1 = dataWatcher.getWatchableObjectByte(16);
			byte b0 = (byte) (attackCounter > 10 ? 1 : 0);

			if (b1 != b0) {
				dataWatcher.updateObject(16, Byte.valueOf(b0));
			}
		}
	}

	private boolean isCourseTraversable(double p_70790_1_, double p_70790_3_, double p_70790_5_, double p_70790_7_) {
		double d4 = (waypointX - posX) / p_70790_7_;
		double d5 = (waypointY - posY) / p_70790_7_;
		double d6 = (waypointZ - posZ) / p_70790_7_;
		AxisAlignedBB axisalignedbb = boundingBox.copy();

		for (int i = 1; i < p_70790_7_; ++i) {
			axisalignedbb.offset(d4, d5, d6);

			if (!worldObj.getCollidingBoundingBoxes(this, axisalignedbb).isEmpty())
				return false;
		}

		return true;
	}

	@Override
	protected String getLivingSound() {
		return "mob.ghast.moan";
	}

	@Override
	protected String getHurtSound() {
		return "mob.ghast.scream";
	}

	@Override
	protected String getDeathSound() {
		return "mob.ghast.death";
	}

	@Override
	protected Item getDropItem() {
		return Items.gunpowder;
	}

	@Override
	protected void dropFewItems(boolean p_70628_1_, int p_70628_2_) {
		int j = rand.nextInt(2) + rand.nextInt(1 + p_70628_2_);
		int k;

		for (k = 0; k < j; ++k) {
			dropItem(Items.ghast_tear, 1);
		}

		j = rand.nextInt(3) + rand.nextInt(1 + p_70628_2_);

		for (k = 0; k < j; ++k) {
			dropItem(Items.gunpowder, 1);
		}
	}

	@Override
	protected float getSoundVolume() {
		return 10.0F;
	}

	@Override
	public boolean getCanSpawnHere() {
		return rand.nextInt(20) == 0 && super.getCanSpawnHere()
				&& worldObj.difficultySetting != EnumDifficulty.PEACEFUL;
	}

	@Override
	public int getMaxSpawnedInChunk() {
		return 1;
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound p_70014_1_) {
		super.writeEntityToNBT(p_70014_1_);
		p_70014_1_.setInteger("ExplosionPower", explosionStrength);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound p_70037_1_) {
		super.readEntityFromNBT(p_70037_1_);

		if (p_70037_1_.hasKey("ExplosionPower", 99)) {
			explosionStrength = p_70037_1_.getInteger("ExplosionPower");
		}
	}
}