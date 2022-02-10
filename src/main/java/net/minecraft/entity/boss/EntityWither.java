package net.minecraft.entity.boss;

import java.util.Iterator;
import java.util.List;

import org.bukkit.craftbukkit.event.CraftEventFactory;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIArrowAttack;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityWitherSkull;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.stats.AchievementList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public class EntityWither extends EntityMob implements IBossDisplayData, IRangedAttackMob {
	private float[] field_82220_d = new float[2];
	private float[] field_82221_e = new float[2];
	private float[] field_82217_f = new float[2];
	private float[] field_82218_g = new float[2];
	private int[] field_82223_h = new int[2];
	private int[] field_82224_i = new int[2];
	private int field_82222_j;
	private static final IEntitySelector attackEntitySelector = new IEntitySelector() {
		private static final String __OBFID = "CL_00001662";

		@Override
		public boolean isEntityApplicable(Entity p_82704_1_) {
			return p_82704_1_ instanceof EntityLivingBase
					&& ((EntityLivingBase) p_82704_1_).getCreatureAttribute() != EnumCreatureAttribute.UNDEAD;
		}
	};
	private static final String __OBFID = "CL_00001661";

	public EntityWither(World p_i1701_1_) {
		super(p_i1701_1_);
		setHealth(getMaxHealth());
		setSize(0.9F, 4.0F);
		isImmuneToFire = true;
		getNavigator().setCanSwim(true);
		tasks.addTask(0, new EntityAISwimming(this));
		tasks.addTask(2, new EntityAIArrowAttack(this, 1.0D, 40, 20.0F));
		tasks.addTask(5, new EntityAIWander(this, 1.0D));
		tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		tasks.addTask(7, new EntityAILookIdle(this));
		targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
		targetTasks.addTask(2,
				new EntityAINearestAttackableTarget(this, EntityLiving.class, 0, false, false, attackEntitySelector));
		experienceValue = 50;
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		dataWatcher.addObject(17, new Integer(0));
		dataWatcher.addObject(18, new Integer(0));
		dataWatcher.addObject(19, new Integer(0));
		dataWatcher.addObject(20, new Integer(0));
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound p_70014_1_) {
		super.writeEntityToNBT(p_70014_1_);
		p_70014_1_.setInteger("Invul", func_82212_n());
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound p_70037_1_) {
		super.readEntityFromNBT(p_70037_1_);
		func_82215_s(p_70037_1_.getInteger("Invul"));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public float getShadowSize() {
		return height / 8.0F;
	}

	@Override
	protected String getLivingSound() {
		return "mob.wither.idle";
	}

	@Override
	protected String getHurtSound() {
		return "mob.wither.hurt";
	}

	@Override
	protected String getDeathSound() {
		return "mob.wither.death";
	}

	@Override
	public void onLivingUpdate() {
		motionY *= 0.6000000238418579D;
		double d1;
		double d3;
		double d5;

		if (!worldObj.isRemote && getWatchedTargetId(0) > 0) {
			Entity entity = worldObj.getEntityByID(getWatchedTargetId(0));

			if (entity != null) {
				if (posY < entity.posY || !isArmored() && posY < entity.posY + 5.0D) {
					if (motionY < 0.0D) {
						motionY = 0.0D;
					}

					motionY += (0.5D - motionY) * 0.6000000238418579D;
				}

				double d0 = entity.posX - posX;
				d1 = entity.posZ - posZ;
				d3 = d0 * d0 + d1 * d1;

				if (d3 > 9.0D) {
					d5 = MathHelper.sqrt_double(d3);
					motionX += (d0 / d5 * 0.5D - motionX) * 0.6000000238418579D;
					motionZ += (d1 / d5 * 0.5D - motionZ) * 0.6000000238418579D;
				}
			}
		}

		if (motionX * motionX + motionZ * motionZ > 0.05000000074505806D) {
			rotationYaw = (float) Math.atan2(motionZ, motionX) * (180F / (float) Math.PI) - 90.0F;
		}

		super.onLivingUpdate();
		int i;

		for (i = 0; i < 2; ++i) {
			field_82218_g[i] = field_82221_e[i];
			field_82217_f[i] = field_82220_d[i];
		}

		int j;

		for (i = 0; i < 2; ++i) {
			j = getWatchedTargetId(i + 1);
			Entity entity1 = null;

			if (j > 0) {
				entity1 = worldObj.getEntityByID(j);
			}

			if (entity1 != null) {
				d1 = func_82214_u(i + 1);
				d3 = func_82208_v(i + 1);
				d5 = func_82213_w(i + 1);
				double d6 = entity1.posX - d1;
				double d7 = entity1.posY + entity1.getEyeHeight() - d3;
				double d8 = entity1.posZ - d5;
				double d9 = MathHelper.sqrt_double(d6 * d6 + d8 * d8);
				float f = (float) (Math.atan2(d8, d6) * 180.0D / Math.PI) - 90.0F;
				float f1 = (float) -(Math.atan2(d7, d9) * 180.0D / Math.PI);
				field_82220_d[i] = func_82204_b(field_82220_d[i], f1, 40.0F);
				field_82221_e[i] = func_82204_b(field_82221_e[i], f, 10.0F);
			} else {
				field_82221_e[i] = func_82204_b(field_82221_e[i], renderYawOffset, 10.0F);
			}
		}

		boolean flag = isArmored();

		for (j = 0; j < 3; ++j) {
			double d10 = func_82214_u(j);
			double d2 = func_82208_v(j);
			double d4 = func_82213_w(j);
			worldObj.spawnParticle("smoke", d10 + rand.nextGaussian() * 0.30000001192092896D,
					d2 + rand.nextGaussian() * 0.30000001192092896D, d4 + rand.nextGaussian() * 0.30000001192092896D,
					0.0D, 0.0D, 0.0D);

			if (flag && worldObj.rand.nextInt(4) == 0) {
				worldObj.spawnParticle("mobSpell", d10 + rand.nextGaussian() * 0.30000001192092896D,
						d2 + rand.nextGaussian() * 0.30000001192092896D,
						d4 + rand.nextGaussian() * 0.30000001192092896D, 0.699999988079071D, 0.699999988079071D, 0.5D);
			}
		}

		if (func_82212_n() > 0) {
			for (j = 0; j < 3; ++j) {
				worldObj.spawnParticle("mobSpell", posX + rand.nextGaussian() * 1.0D, posY + rand.nextFloat() * 3.3F,
						posZ + rand.nextGaussian() * 1.0D, 0.699999988079071D, 0.699999988079071D, 0.8999999761581421D);
			}
		}
	}

	@Override
	protected void updateAITasks() {
		int i;
		if (func_82212_n() > 0) {
			i = func_82212_n() - 1;
			if (i <= 0) {
				worldObj.newExplosion(this, posX, posY + getEyeHeight(), posZ, 7.0F, false,
						worldObj.getGameRules().getGameRuleBooleanValue("mobGriefing"));
				worldObj.playBroadcastSound(1013, (int) posX, (int) posY, (int) posZ, 0);
			}
			func_82215_s(i);
			if (ticksExisted % 10 == 0) {
				this.heal(10.0F, org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason.WITHER_SPAWN);
			}
		} else {
			super.updateAITasks();
			int i1;
			int j1;
			for (i = 1; i < 3; ++i) {
				if (ticksExisted >= field_82223_h[i - 1]) {
					field_82223_h[i - 1] = ticksExisted + 10 + rand.nextInt(10);
					int k1;
					if (worldObj.difficultySetting == EnumDifficulty.NORMAL
							|| worldObj.difficultySetting == EnumDifficulty.HARD) {
						j1 = i - 1;
						k1 = field_82224_i[i - 1];
						field_82224_i[j1] = field_82224_i[i - 1] + 1;
						if (k1 > 15) {
							float f = 10.0F;
							float f1 = 5.0F;
							double d0 = MathHelper.getRandomDoubleInRange(rand, posX - f, posX + f);
							double d1 = MathHelper.getRandomDoubleInRange(rand, posY - f1, posY + f1);
							double d2 = MathHelper.getRandomDoubleInRange(rand, posZ - f, posZ + f);
							func_82209_a(i + 1, d0, d1, d2, true);
							field_82224_i[i - 1] = 0;
						}
					}
					i1 = getWatchedTargetId(i);
					if (i1 > 0) {
						Entity entity = worldObj.getEntityByID(i1);
						if (entity != null && entity.isEntityAlive() && getDistanceSqToEntity(entity) <= 900.0D
								&& canEntityBeSeen(entity)) {
							func_82216_a(i + 1, (EntityLivingBase) entity);
							field_82223_h[i - 1] = ticksExisted + 40 + rand.nextInt(20);
							field_82224_i[i - 1] = 0;
						} else {
							func_82211_c(i, 0);
						}
					} else {
						List list = worldObj.selectEntitiesWithinAABB(EntityLivingBase.class,
								boundingBox.expand(20.0D, 8.0D, 20.0D), attackEntitySelector);
						for (k1 = 0; k1 < 10 && !list.isEmpty(); ++k1) {
							EntityLivingBase entitylivingbase = (EntityLivingBase) list.get(rand.nextInt(list.size()));
							if (entitylivingbase != this && entitylivingbase.isEntityAlive()
									&& canEntityBeSeen(entitylivingbase)) {
								if (entitylivingbase instanceof EntityPlayer)
									if (!((EntityPlayer) entitylivingbase).capabilities.disableDamage) {
										func_82211_c(i, entitylivingbase.getEntityId());
									} else {
										func_82211_c(i, entitylivingbase.getEntityId());
									}
								break;
							}
							list.remove(entitylivingbase);
						}
					}
				}
			}
			if (getAttackTarget() != null) {
				func_82211_c(0, getAttackTarget().getEntityId());
			} else {
				func_82211_c(0, 0);
			}
			if (field_82222_j > 0) {
				--field_82222_j;
				if (field_82222_j == 0 && worldObj.getGameRules().getGameRuleBooleanValue("mobGriefing")) {
					i = MathHelper.floor_double(posY);
					i1 = MathHelper.floor_double(posX);
					j1 = MathHelper.floor_double(posZ);
					boolean flag = false;
					int l1 = -1;
					while (true) {
						if (l1 > 1) {
							if (flag) {
								worldObj.playAuxSFXAtEntity(null, 1012, (int) posX, (int) posY, (int) posZ, 0);
							}
							break;
						}
						for (int i2 = -1; i2 <= 1; ++i2) {
							for (int j = 0; j <= 3; ++j) {
								int j2 = i1 + l1;
								int k = i + j;
								int l = j1 + i2;
								Block block = worldObj.getBlock(j2, k, l);
								if (!block.isAir(worldObj, j2, k, l)
										&& block.canEntityDestroy(worldObj, j2, k, l, this)) {
									if (CraftEventFactory.callEntityChangeBlockEvent(this, j2, k, l, Blocks.air, 0)
											.isCancelled()) {
										continue;
									}
									flag = worldObj.func_147480_a(j2, k, l, true) || flag;
								}
							}
						}
						++l1;
					}
				}
			}
			if (ticksExisted % 20 == 0) {
				this.heal(1.0F, org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason.WITHER_SPAWN);
			}
		}
	}

	public void func_82206_m() {
		func_82215_s(220);
		setHealth(getMaxHealth() / 3.0F);
	}

	@Override
	public void setInWeb() {
	}

	@Override
	public int getTotalArmorValue() {
		return 4;
	}

	private double func_82214_u(int p_82214_1_) {
		if (p_82214_1_ <= 0)
			return posX;
		else {
			float f = (renderYawOffset + 180 * (p_82214_1_ - 1)) / 180.0F * (float) Math.PI;
			float f1 = MathHelper.cos(f);
			return posX + f1 * 1.3D;
		}
	}

	private double func_82208_v(int p_82208_1_) {
		return p_82208_1_ <= 0 ? posY + 3.0D : posY + 2.2D;
	}

	private double func_82213_w(int p_82213_1_) {
		if (p_82213_1_ <= 0)
			return posZ;
		else {
			float f = (renderYawOffset + 180 * (p_82213_1_ - 1)) / 180.0F * (float) Math.PI;
			float f1 = MathHelper.sin(f);
			return posZ + f1 * 1.3D;
		}
	}

	private float func_82204_b(float p_82204_1_, float p_82204_2_, float p_82204_3_) {
		float f3 = MathHelper.wrapAngleTo180_float(p_82204_2_ - p_82204_1_);

		if (f3 > p_82204_3_) {
			f3 = p_82204_3_;
		}

		if (f3 < -p_82204_3_) {
			f3 = -p_82204_3_;
		}

		return p_82204_1_ + f3;
	}

	private void func_82216_a(int p_82216_1_, EntityLivingBase p_82216_2_) {
		func_82209_a(p_82216_1_, p_82216_2_.posX, p_82216_2_.posY + p_82216_2_.getEyeHeight() * 0.5D, p_82216_2_.posZ,
				p_82216_1_ == 0 && rand.nextFloat() < 0.001F);
	}

	private void func_82209_a(int p_82209_1_, double p_82209_2_, double p_82209_4_, double p_82209_6_,
			boolean p_82209_8_) {
		worldObj.playAuxSFXAtEntity((EntityPlayer) null, 1014, (int) posX, (int) posY, (int) posZ, 0);
		double d3 = func_82214_u(p_82209_1_);
		double d4 = func_82208_v(p_82209_1_);
		double d5 = func_82213_w(p_82209_1_);
		double d6 = p_82209_2_ - d3;
		double d7 = p_82209_4_ - d4;
		double d8 = p_82209_6_ - d5;
		EntityWitherSkull entitywitherskull = new EntityWitherSkull(worldObj, this, d6, d7, d8);

		if (p_82209_8_) {
			entitywitherskull.setInvulnerable(true);
		}

		entitywitherskull.posY = d4;
		entitywitherskull.posX = d3;
		entitywitherskull.posZ = d5;
		worldObj.spawnEntityInWorld(entitywitherskull);
	}

	@Override
	public void attackEntityWithRangedAttack(EntityLivingBase p_82196_1_, float p_82196_2_) {
		func_82216_a(0, p_82196_1_);
	}

	@Override
	public boolean attackEntityFrom(DamageSource p_70097_1_, float p_70097_2_) {
		if (isEntityInvulnerable())
			return false;
		else if (p_70097_1_ == DamageSource.drown)
			return false;
		else if (func_82212_n() > 0)
			return false;
		else {
			Entity entity;

			if (isArmored()) {
				entity = p_70097_1_.getSourceOfDamage();

				if (entity instanceof EntityArrow)
					return false;
			}

			entity = p_70097_1_.getEntity();

			if (entity != null && !(entity instanceof EntityPlayer) && entity instanceof EntityLivingBase
					&& ((EntityLivingBase) entity).getCreatureAttribute() == getCreatureAttribute())
				return false;
			else {
				if (field_82222_j <= 0) {
					field_82222_j = 20;
				}

				for (int i = 0; i < field_82224_i.length; ++i) {
					field_82224_i[i] += 3;
				}

				return super.attackEntityFrom(p_70097_1_, p_70097_2_);
			}
		}
	}

	@Override
	protected void dropFewItems(boolean p_70628_1_, int p_70628_2_) {
		dropItem(Items.nether_star, 1);

		if (!worldObj.isRemote) {
			Iterator iterator = worldObj
					.getEntitiesWithinAABB(EntityPlayer.class, boundingBox.expand(50.0D, 100.0D, 50.0D)).iterator();

			while (iterator.hasNext()) {
				EntityPlayer entityplayer = (EntityPlayer) iterator.next();
				entityplayer.triggerAchievement(AchievementList.field_150964_J);
			}
		}
	}

	@Override
	protected void despawnEntity() {
		entityAge = 0;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getBrightnessForRender(float p_70070_1_) {
		return 15728880;
	}

	@Override
	protected void fall(float p_70069_1_) {
	}

	@Override
	public void addPotionEffect(PotionEffect p_70690_1_) {
	}

	@Override
	protected boolean isAIEnabled() {
		return true;
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(300.0D);
		getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.6000000238418579D);
		getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(40.0D);
	}

	@SideOnly(Side.CLIENT)
	public float func_82207_a(int p_82207_1_) {
		return field_82221_e[p_82207_1_];
	}

	@SideOnly(Side.CLIENT)
	public float func_82210_r(int p_82210_1_) {
		return field_82220_d[p_82210_1_];
	}

	public int func_82212_n() {
		return dataWatcher.getWatchableObjectInt(20);
	}

	public void func_82215_s(int p_82215_1_) {
		dataWatcher.updateObject(20, Integer.valueOf(p_82215_1_));
	}

	public int getWatchedTargetId(int p_82203_1_) {
		return dataWatcher.getWatchableObjectInt(17 + p_82203_1_);
	}

	public void func_82211_c(int p_82211_1_, int p_82211_2_) {
		dataWatcher.updateObject(17 + p_82211_1_, Integer.valueOf(p_82211_2_));
	}

	public boolean isArmored() {
		return getHealth() <= getMaxHealth() / 2.0F;
	}

	@Override
	public EnumCreatureAttribute getCreatureAttribute() {
		return EnumCreatureAttribute.UNDEAD;
	}

	@Override
	public void mountEntity(Entity p_70078_1_) {
		ridingEntity = null;
	}
}