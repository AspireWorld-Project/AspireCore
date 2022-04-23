package net.minecraft.entity.boss;

import net.minecraft.block.Block;
import net.minecraft.block.BlockEndPortal;
import net.minecraft.entity.*;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.server.S23PacketBlockChange;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import org.bukkit.Bukkit;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.util.BlockStateListPopulator;
import org.bukkit.event.entity.EntityCreatePortalEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityTargetEvent;

import java.util.Iterator;
import java.util.List;

public class EntityDragon extends EntityLiving implements IBossDisplayData, IEntityMultiPart, IMob {
	public double targetX;
	public double targetY;
	public double targetZ;
	public double[][] ringBuffer = new double[64][3];
	public int ringBufferIndex = -1;
	public EntityDragonPart[] dragonPartArray;
	public EntityDragonPart dragonPartHead;
	public EntityDragonPart dragonPartBody;
	public EntityDragonPart dragonPartTail1;
	public EntityDragonPart dragonPartTail2;
	public EntityDragonPart dragonPartTail3;
	public EntityDragonPart dragonPartWing1;
	public EntityDragonPart dragonPartWing2;
	public float prevAnimTime;
	public float animTime;
	public boolean forceNewTarget;
	public boolean slowed;
	private Entity target;
	public int deathTicks;
	public EntityEnderCrystal healingEnderCrystal;
	private static final String __OBFID = "CL_00001659";

	public EntityDragon(World p_i1700_1_) {
		super(p_i1700_1_);
		dragonPartArray = new EntityDragonPart[] { dragonPartHead = new EntityDragonPart(this, "head", 6.0F, 6.0F),
				dragonPartBody = new EntityDragonPart(this, "body", 8.0F, 8.0F),
				dragonPartTail1 = new EntityDragonPart(this, "tail", 4.0F, 4.0F),
				dragonPartTail2 = new EntityDragonPart(this, "tail", 4.0F, 4.0F),
				dragonPartTail3 = new EntityDragonPart(this, "tail", 4.0F, 4.0F),
				dragonPartWing1 = new EntityDragonPart(this, "wing", 4.0F, 4.0F),
				dragonPartWing2 = new EntityDragonPart(this, "wing", 4.0F, 4.0F) };
		setHealth(getMaxHealth());
		setSize(16.0F, 8.0F);
		noClip = true;
		isImmuneToFire = true;
		targetY = 100.0D;
		ignoreFrustumCheck = true;
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(200.0D);
	}

	@Override
	protected void entityInit() {
		super.entityInit();
	}

	public double[] getMovementOffsets(int p_70974_1_, float p_70974_2_) {
		if (getHealth() <= 0.0F) {
			p_70974_2_ = 0.0F;
		}

		p_70974_2_ = 1.0F - p_70974_2_;
		int j = ringBufferIndex - p_70974_1_ * 1 & 63;
		int k = ringBufferIndex - p_70974_1_ * 1 - 1 & 63;
		double[] adouble = new double[3];
		double d0 = ringBuffer[j][0];
		double d1 = MathHelper.wrapAngleTo180_double(ringBuffer[k][0] - d0);
		adouble[0] = d0 + d1 * p_70974_2_;
		d0 = ringBuffer[j][1];
		d1 = ringBuffer[k][1] - d0;
		adouble[1] = d0 + d1 * p_70974_2_;
		adouble[2] = ringBuffer[j][2] + (ringBuffer[k][2] - ringBuffer[j][2]) * p_70974_2_;
		return adouble;
	}

	@Override
	public void onLivingUpdate() {
		float f;
		float f1;

		if (worldObj.isRemote) {
			f = MathHelper.cos(animTime * (float) Math.PI * 2.0F);
			f1 = MathHelper.cos(prevAnimTime * (float) Math.PI * 2.0F);

			if (f1 <= -0.3F && f >= -0.3F) {
				worldObj.playSound(posX, posY, posZ, "mob.enderdragon.wings", 5.0F, 0.8F + rand.nextFloat() * 0.3F,
						false);
			}
		}

		prevAnimTime = animTime;
		float f2;

		if (getHealth() <= 0.0F) {
			f = (rand.nextFloat() - 0.5F) * 8.0F;
			f1 = (rand.nextFloat() - 0.5F) * 4.0F;
			f2 = (rand.nextFloat() - 0.5F) * 8.0F;
			worldObj.spawnParticle("largeexplode", posX + f, posY + 2.0D + f1, posZ + f2, 0.0D, 0.0D, 0.0D);
		} else {
			updateDragonEnderCrystal();
			f = 0.2F / (MathHelper.sqrt_double(motionX * motionX + motionZ * motionZ) * 10.0F + 1.0F);
			f *= (float) Math.pow(2.0D, motionY);

			if (slowed) {
				animTime += f * 0.5F;
			} else {
				animTime += f;
			}

			rotationYaw = MathHelper.wrapAngleTo180_float(rotationYaw);

			if (ringBufferIndex < 0) {
				for (int i = 0; i < ringBuffer.length; ++i) {
					ringBuffer[i][0] = rotationYaw;
					ringBuffer[i][1] = posY;
				}
			}

			if (++ringBufferIndex == ringBuffer.length) {
				ringBufferIndex = 0;
			}

			ringBuffer[ringBufferIndex][0] = rotationYaw;
			ringBuffer[ringBufferIndex][1] = posY;
			double d0;
			double d1;
			double d2;
			double d10;
			float f12;

			if (worldObj.isRemote) {
				if (newPosRotationIncrements > 0) {
					d10 = posX + (newPosX - posX) / newPosRotationIncrements;
					d0 = posY + (newPosY - posY) / newPosRotationIncrements;
					d1 = posZ + (newPosZ - posZ) / newPosRotationIncrements;
					d2 = MathHelper.wrapAngleTo180_double(newRotationYaw - rotationYaw);
					rotationYaw = (float) (rotationYaw + d2 / newPosRotationIncrements);
					rotationPitch = (float) (rotationPitch
							+ (newRotationPitch - rotationPitch) / newPosRotationIncrements);
					--newPosRotationIncrements;
					setPosition(d10, d0, d1);
					setRotation(rotationYaw, rotationPitch);
				}
			} else {
				d10 = targetX - posX;
				d0 = targetY - posY;
				d1 = targetZ - posZ;
				d2 = d10 * d10 + d0 * d0 + d1 * d1;

				if (target != null) {
					targetX = target.posX;
					targetZ = target.posZ;
					double d3 = targetX - posX;
					double d5 = targetZ - posZ;
					double d7 = Math.sqrt(d3 * d3 + d5 * d5);
					double d8 = 0.4000000059604645D + d7 / 80.0D - 1.0D;

					if (d8 > 10.0D) {
						d8 = 10.0D;
					}

					targetY = target.boundingBox.minY + d8;
				} else {
					targetX += rand.nextGaussian() * 2.0D;
					targetZ += rand.nextGaussian() * 2.0D;
				}

				if (forceNewTarget || d2 < 100.0D || d2 > 22500.0D || isCollidedHorizontally || isCollidedVertically) {
					setNewTarget();
				}

				d0 /= MathHelper.sqrt_double(d10 * d10 + d1 * d1);
				f12 = 0.6F;

				if (d0 < -f12) {
					d0 = -f12;
				}

				if (d0 > f12) {
					d0 = f12;
				}

				motionY += d0 * 0.10000000149011612D;
				rotationYaw = MathHelper.wrapAngleTo180_float(rotationYaw);
				double d4 = 180.0D - Math.atan2(d10, d1) * 180.0D / Math.PI;
				double d6 = MathHelper.wrapAngleTo180_double(d4 - rotationYaw);

				if (d6 > 50.0D) {
					d6 = 50.0D;
				}

				if (d6 < -50.0D) {
					d6 = -50.0D;
				}

				Vec3 vec3 = Vec3.createVectorHelper(targetX - posX, targetY - posY, targetZ - posZ).normalize();
				Vec3 vec32 = Vec3.createVectorHelper(MathHelper.sin(rotationYaw * (float) Math.PI / 180.0F), motionY,
						-MathHelper.cos(rotationYaw * (float) Math.PI / 180.0F)).normalize();
				float f5 = (float) (vec32.dotProduct(vec3) + 0.5D) / 1.5F;

				if (f5 < 0.0F) {
					f5 = 0.0F;
				}

				randomYawVelocity *= 0.8F;
				float f6 = MathHelper.sqrt_double(motionX * motionX + motionZ * motionZ) * 1.0F + 1.0F;
				double d9 = Math.sqrt(motionX * motionX + motionZ * motionZ) * 1.0D + 1.0D;

				if (d9 > 40.0D) {
					d9 = 40.0D;
				}

				randomYawVelocity = (float) (randomYawVelocity + d6 * (0.699999988079071D / d9 / f6));
				rotationYaw += randomYawVelocity * 0.1F;
				float f7 = (float) (2.0D / (d9 + 1.0D));
				float f8 = 0.06F;
				moveFlying(0.0F, -1.0F, f8 * (f5 * f7 + (1.0F - f7)));

				if (slowed) {
					moveEntity(motionX * 0.800000011920929D, motionY * 0.800000011920929D,
							motionZ * 0.800000011920929D);
				} else {
					moveEntity(motionX, motionY, motionZ);
				}

				Vec3 vec31 = Vec3.createVectorHelper(motionX, motionY, motionZ).normalize();
				float f9 = (float) (vec31.dotProduct(vec32) + 1.0D) / 2.0F;
				f9 = 0.8F + 0.15F * f9;
				motionX *= f9;
				motionZ *= f9;
				motionY *= 0.9100000262260437D;
			}

			renderYawOffset = rotationYaw;
			dragonPartHead.width = dragonPartHead.height = 3.0F;
			dragonPartTail1.width = dragonPartTail1.height = 2.0F;
			dragonPartTail2.width = dragonPartTail2.height = 2.0F;
			dragonPartTail3.width = dragonPartTail3.height = 2.0F;
			dragonPartBody.height = 3.0F;
			dragonPartBody.width = 5.0F;
			dragonPartWing1.height = 2.0F;
			dragonPartWing1.width = 4.0F;
			dragonPartWing2.height = 3.0F;
			dragonPartWing2.width = 4.0F;
			f1 = (float) (getMovementOffsets(5, 1.0F)[1] - getMovementOffsets(10, 1.0F)[1]) * 10.0F / 180.0F
					* (float) Math.PI;
			f2 = MathHelper.cos(f1);
			float f10 = -MathHelper.sin(f1);
			float f3 = rotationYaw * (float) Math.PI / 180.0F;
			float f11 = MathHelper.sin(f3);
			float f4 = MathHelper.cos(f3);
			dragonPartBody.onUpdate();
			dragonPartBody.setLocationAndAngles(posX + f11 * 0.5F, posY, posZ - f4 * 0.5F, 0.0F, 0.0F);
			dragonPartWing1.onUpdate();
			dragonPartWing1.setLocationAndAngles(posX + f4 * 4.5F, posY + 2.0D, posZ + f11 * 4.5F, 0.0F, 0.0F);
			dragonPartWing2.onUpdate();
			dragonPartWing2.setLocationAndAngles(posX - f4 * 4.5F, posY + 2.0D, posZ - f11 * 4.5F, 0.0F, 0.0F);

			if (!worldObj.isRemote && hurtTime == 0) {
				collideWithEntities(worldObj.getEntitiesWithinAABBExcludingEntity(this,
						dragonPartWing1.boundingBox.expand(4.0D, 2.0D, 4.0D).offset(0.0D, -2.0D, 0.0D)));
				collideWithEntities(worldObj.getEntitiesWithinAABBExcludingEntity(this,
						dragonPartWing2.boundingBox.expand(4.0D, 2.0D, 4.0D).offset(0.0D, -2.0D, 0.0D)));
				attackEntitiesInList(worldObj.getEntitiesWithinAABBExcludingEntity(this,
						dragonPartHead.boundingBox.expand(1.0D, 1.0D, 1.0D)));
			}

			double[] adouble1 = getMovementOffsets(5, 1.0F);
			double[] adouble = getMovementOffsets(0, 1.0F);
			f12 = MathHelper.sin(rotationYaw * (float) Math.PI / 180.0F - randomYawVelocity * 0.01F);
			float f13 = MathHelper.cos(rotationYaw * (float) Math.PI / 180.0F - randomYawVelocity * 0.01F);
			dragonPartHead.onUpdate();
			dragonPartHead.setLocationAndAngles(posX + f12 * 5.5F * f2,
					posY + (adouble[1] - adouble1[1]) * 1.0D + f10 * 5.5F, posZ - f13 * 5.5F * f2, 0.0F, 0.0F);

			for (int j = 0; j < 3; ++j) {
				EntityDragonPart entitydragonpart = null;

				if (j == 0) {
					entitydragonpart = dragonPartTail1;
				}

				if (j == 1) {
					entitydragonpart = dragonPartTail2;
				}

				if (j == 2) {
					entitydragonpart = dragonPartTail3;
				}

				double[] adouble2 = getMovementOffsets(12 + j * 2, 1.0F);
				float f14 = rotationYaw * (float) Math.PI / 180.0F
						+ simplifyAngle(adouble2[0] - adouble1[0]) * (float) Math.PI / 180.0F * 1.0F;
				float f15 = MathHelper.sin(f14);
				float f16 = MathHelper.cos(f14);
				float f17 = 1.5F;
				float f18 = (j + 1) * 2.0F;
				entitydragonpart.onUpdate();
				entitydragonpart.setLocationAndAngles(posX - (f11 * f17 + f15 * f18) * f2,
						posY + (adouble2[1] - adouble1[1]) * 1.0D - (f18 + f17) * f10 + 1.5D,
						posZ + (f4 * f17 + f16 * f18) * f2, 0.0F, 0.0F);
			}

			if (!worldObj.isRemote) {
				slowed = destroyBlocksInAABB(dragonPartHead.boundingBox)
						| destroyBlocksInAABB(dragonPartBody.boundingBox);
			}
		}
	}

	private void updateDragonEnderCrystal() {
		if (healingEnderCrystal != null) {
			if (healingEnderCrystal.isDead) {
				if (!worldObj.isRemote) {
					attackEntityFromPart(dragonPartHead, DamageSource.setExplosionSource(null), 10.0F);
				}

				healingEnderCrystal = null;
			} else if (ticksExisted % 10 == 0 && getHealth() < getMaxHealth()) {

				EntityRegainHealthEvent event = new EntityRegainHealthEvent(this.getBukkitEntity(), 1.0D,
						EntityRegainHealthEvent.RegainReason.ENDER_CRYSTAL);
				Bukkit.getServer().getPluginManager().callEvent(event);

				if (!event.isCancelled()) {
					setHealth((float) (getHealth() + event.getAmount()));
				}
			}
		}

		if (rand.nextInt(10) == 0) {
			float f = 32.0F;
			List list = worldObj.getEntitiesWithinAABB(EntityEnderCrystal.class, boundingBox.expand(f, f, f));
			EntityEnderCrystal entityendercrystal = null;
			double d0 = Double.MAX_VALUE;
			Iterator iterator = list.iterator();

			while (iterator.hasNext()) {
				EntityEnderCrystal entityendercrystal1 = (EntityEnderCrystal) iterator.next();
				double d1 = entityendercrystal1.getDistanceSqToEntity(this);

				if (d1 < d0) {
					d0 = d1;
					entityendercrystal = entityendercrystal1;
				}
			}

			healingEnderCrystal = entityendercrystal;
		}
	}

	private void collideWithEntities(List p_70970_1_) {
		double d0 = (dragonPartBody.boundingBox.minX + dragonPartBody.boundingBox.maxX) / 2.0D;
		double d1 = (dragonPartBody.boundingBox.minZ + dragonPartBody.boundingBox.maxZ) / 2.0D;
		Iterator iterator = p_70970_1_.iterator();

		while (iterator.hasNext()) {
			Entity entity = (Entity) iterator.next();

			if (entity instanceof EntityLivingBase) {
				double d2 = entity.posX - d0;
				double d3 = entity.posZ - d1;
				double d4 = d2 * d2 + d3 * d3;
				entity.addVelocity(d2 / d4 * 4.0D, 0.20000000298023224D, d3 / d4 * 4.0D);
			}
		}
	}

	private void attackEntitiesInList(List p_70971_1_) {
		for (int i = 0; i < p_70971_1_.size(); ++i) {
			Entity entity = (Entity) p_70971_1_.get(i);

			if (entity instanceof EntityLivingBase) {
				entity.attackEntityFrom(DamageSource.causeMobDamage(this), 10.0F);
			}
		}
	}

	private void setNewTarget() {
		forceNewTarget = false;

		if (rand.nextInt(2) == 0 && !worldObj.playerEntities.isEmpty()) {
			Entity target = (Entity) worldObj.playerEntities.get(rand.nextInt(worldObj.playerEntities.size()));
			EntityTargetEvent event = new EntityTargetEvent(this.getBukkitEntity(), target.getBukkitEntity(),
					EntityTargetEvent.TargetReason.RANDOM_TARGET);
			Bukkit.getPluginManager().callEvent(event);
			if (!event.isCancelled()) {
				this.target = event.getTarget() == null ? null
						: ((org.bukkit.craftbukkit.entity.CraftEntity) event.getTarget()).getHandle();
			}
		} else {
			boolean flag = false;

			do {
				targetX = 0.0D;
				targetY = 70.0F + rand.nextFloat() * 50.0F;
				targetZ = 0.0D;
				targetX += rand.nextFloat() * 120.0F - 60.0F;
				targetZ += rand.nextFloat() * 120.0F - 60.0F;
				double d0 = posX - targetX;
				double d1 = posY - targetY;
				double d2 = posZ - targetZ;
				flag = d0 * d0 + d1 * d1 + d2 * d2 > 100.0D;
			} while (!flag);

			target = null;
		}
	}

	private float simplifyAngle(double p_70973_1_) {
		return (float) MathHelper.wrapAngleTo180_double(p_70973_1_);
	}

	private boolean destroyBlocksInAABB(AxisAlignedBB p_70972_1_) {
		int i = MathHelper.floor_double(p_70972_1_.minX);
		int j = MathHelper.floor_double(p_70972_1_.minY);
		int k = MathHelper.floor_double(p_70972_1_.minZ);
		int l = MathHelper.floor_double(p_70972_1_.maxX);
		int i1 = MathHelper.floor_double(p_70972_1_.maxY);
		int j1 = MathHelper.floor_double(p_70972_1_.maxZ);
		boolean flag = false;
		boolean flag1 = false;

		for (int k1 = i; k1 <= l; ++k1) {
			for (int l1 = j; l1 <= i1; ++l1) {
				for (int i2 = k; i2 <= j1; ++i2) {
					Block block = worldObj.getBlock(k1, l1, i2);

					if (!block.isAir(worldObj, k1, l1, i2)) {
						if (block.canEntityDestroy(worldObj, k1, l1, i2, this)
								&& worldObj.getGameRules().getGameRuleBooleanValue("mobGriefing")) {
							flag1 = worldObj.setBlockToAir(k1, l1, i2) || flag1;
						} else {
							flag = true;
						}
					}
				}
			}
		}

		if (flag1) {
			double d1 = p_70972_1_.minX + (p_70972_1_.maxX - p_70972_1_.minX) * rand.nextFloat();
			double d2 = p_70972_1_.minY + (p_70972_1_.maxY - p_70972_1_.minY) * rand.nextFloat();
			double d0 = p_70972_1_.minZ + (p_70972_1_.maxZ - p_70972_1_.minZ) * rand.nextFloat();
			worldObj.spawnParticle("largeexplode", d1, d2, d0, 0.0D, 0.0D, 0.0D);
		}

		return flag;
	}

	@Override
	public boolean attackEntityFromPart(EntityDragonPart p_70965_1_, DamageSource p_70965_2_, float p_70965_3_) {
		if (p_70965_1_ != dragonPartHead) {
			p_70965_3_ = p_70965_3_ / 4.0F + 1.0F;
		}

		float f1 = rotationYaw * (float) Math.PI / 180.0F;
		float f2 = MathHelper.sin(f1);
		float f3 = MathHelper.cos(f1);
		targetX = posX + f2 * 5.0F + (rand.nextFloat() - 0.5F) * 2.0F;
		targetY = posY + rand.nextFloat() * 3.0F + 1.0D;
		targetZ = posZ - f3 * 5.0F + (rand.nextFloat() - 0.5F) * 2.0F;
		target = null;

		if (p_70965_2_.getEntity() instanceof EntityPlayer || p_70965_2_.isExplosion()) {
			func_82195_e(p_70965_2_, p_70965_3_);
		}

		return true;
	}

	@Override
	public boolean attackEntityFrom(DamageSource p_70097_1_, float p_70097_2_) {
		return false;
	}

	protected boolean func_82195_e(DamageSource p_82195_1_, float p_82195_2_) {
		return super.attackEntityFrom(p_82195_1_, p_82195_2_);
	}

	@Override
	protected void onDeathUpdate() {
		++deathTicks;

		if (deathTicks >= 180 && deathTicks <= 200) {
			float f = (rand.nextFloat() - 0.5F) * 8.0F;
			float f1 = (rand.nextFloat() - 0.5F) * 4.0F;
			float f2 = (rand.nextFloat() - 0.5F) * 8.0F;
			worldObj.spawnParticle("hugeexplosion", posX + f, posY + 2.0D + f1, posZ + f2, 0.0D, 0.0D, 0.0D);
		}

		int i;
		int j;

		if (!worldObj.isRemote) {
			if (deathTicks > 150 && deathTicks % 5 == 0) {
				i = 1000;

				while (i > 0) {
					j = EntityXPOrb.getXPSplit(i);
					i -= j;
					worldObj.spawnEntityInWorld(new EntityXPOrb(worldObj, posX, posY, posZ, j));
				}
			}

			if (deathTicks == 1) {
				worldObj.playBroadcastSound(1018, (int) posX, (int) posY, (int) posZ, 0);
			}
		}

		moveEntity(0.0D, 0.10000000149011612D, 0.0D);
		renderYawOffset = rotationYaw += 20.0F;

		if (deathTicks == 200 && !worldObj.isRemote) {
			i = 2000;

			while (i > 0) {
				j = EntityXPOrb.getXPSplit(i);
				i -= j;
				worldObj.spawnEntityInWorld(new EntityXPOrb(worldObj, posX, posY, posZ, j));
			}

			createEnderPortal(MathHelper.floor_double(posX), MathHelper.floor_double(posZ));
			setDead();
		}
	}

	private void createEnderPortal(int p_70975_1_, int p_70975_2_) {
		byte b0 = 64;
		BlockEndPortal.field_149948_a = true;
		byte b1 = 4;

		for (int k = b0 - 1; k <= b0 + 32; ++k) {
			for (int l = p_70975_1_ - b1; l <= p_70975_1_ + b1; ++l) {
				for (int i1 = p_70975_2_ - b1; i1 <= p_70975_2_ + b1; ++i1) {
					double d0 = l - p_70975_1_;
					double d1 = i1 - p_70975_2_;
					double d2 = d0 * d0 + d1 * d1;

					if (d2 <= (b1 - 0.5D) * (b1 - 0.5D)) {
						if (k < b0) {
							if (d2 <= (b1 - 1 - 0.5D) * (b1 - 1 - 0.5D)) {
								worldObj.setBlock(l, k, i1, Blocks.bedrock);
							}
						} else if (k > b0) {
							worldObj.setBlock(l, k, i1, Blocks.air);
						} else if (d2 > (b1 - 1 - 0.5D) * (b1 - 1 - 0.5D)) {
							worldObj.setBlock(l, k, i1, Blocks.bedrock);
						} else {
							worldObj.setBlock(l, k, i1, Blocks.end_portal);
						}
					}
				}
			}
		}

		worldObj.setBlock(p_70975_1_, b0 + 0, p_70975_2_, Blocks.bedrock);
		worldObj.setBlock(p_70975_1_, b0 + 1, p_70975_2_, Blocks.bedrock);
		worldObj.setBlock(p_70975_1_, b0 + 2, p_70975_2_, Blocks.bedrock);
		worldObj.setBlock(p_70975_1_ - 1, b0 + 2, p_70975_2_, Blocks.torch);
		worldObj.setBlock(p_70975_1_ + 1, b0 + 2, p_70975_2_, Blocks.torch);
		worldObj.setBlock(p_70975_1_, b0 + 2, p_70975_2_ - 1, Blocks.torch);
		worldObj.setBlock(p_70975_1_, b0 + 2, p_70975_2_ + 1, Blocks.torch);
		worldObj.setBlock(p_70975_1_, b0 + 3, p_70975_2_, Blocks.bedrock);
		worldObj.setBlock(p_70975_1_, b0 + 4, p_70975_2_, Blocks.dragon_egg);

		BlockStateListPopulator world = new BlockStateListPopulator(worldObj.getWorld());
		EntityCreatePortalEvent event = new EntityCreatePortalEvent(
				(org.bukkit.entity.LivingEntity) this.getBukkitEntity(),
				java.util.Collections.unmodifiableList(world.getList()), org.bukkit.PortalType.ENDER);
		worldObj.getServer().getPluginManager().callEvent(event);
		if (!event.isCancelled()) {
			for (BlockState state : event.getBlocks()) {
				state.update(true);
			}
		} else {
			for (BlockState state : event.getBlocks()) {
				S23PacketBlockChange packet = new S23PacketBlockChange(state.getX(), state.getY(), state.getZ(),
						worldObj);
				for (Object playerEntity : worldObj.playerEntities) {
					EntityPlayer entity = (EntityPlayer) playerEntity;
					if (entity instanceof EntityPlayerMP) {
						((EntityPlayerMP) entity).playerNetServerHandler.sendPacket(packet);
					}
				}
			}
		}
		BlockEndPortal.field_149948_a = false;
	}

	@Override
	protected void despawnEntity() {
	}

	@Override
	public Entity[] getParts() {
		return dragonPartArray;
	}

	@Override
	public boolean canBeCollidedWith() {
		return false;
	}

	@Override
	public World func_82194_d() {
		return worldObj;
	}

	@Override
	protected String getLivingSound() {
		return "mob.enderdragon.growl";
	}

	@Override
	protected String getHurtSound() {
		return "mob.enderdragon.hit";
	}

	@Override
	protected float getSoundVolume() {
		return 5.0F;
	}

	public boolean realAttackEntityFrom(DamageSource source, float amount) {
		return func_82195_e(source, amount);
	}
}