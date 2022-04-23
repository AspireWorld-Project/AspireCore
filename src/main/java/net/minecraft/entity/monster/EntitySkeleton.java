package net.minecraft.entity.monster;

import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.stats.AchievementList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderHell;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityShootBowEvent;

import java.util.Calendar;

public class EntitySkeleton extends EntityMob implements IRangedAttackMob {
	private final EntityAIArrowAttack aiArrowAttack = new EntityAIArrowAttack(this, 1.0D, 20, 60, 15.0F);
	private final EntityAIAttackOnCollide aiAttackOnCollide = new EntityAIAttackOnCollide(this, EntityPlayer.class, 1.2D,
			false);
	private static final String __OBFID = "CL_00001697";

	public EntitySkeleton(World p_i1741_1_) {
		super(p_i1741_1_);
		tasks.addTask(1, new EntityAISwimming(this));
		tasks.addTask(2, new EntityAIRestrictSun(this));
		tasks.addTask(3, new EntityAIFleeSun(this, 1.0D));
		tasks.addTask(5, new EntityAIWander(this, 1.0D));
		tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		tasks.addTask(6, new EntityAILookIdle(this));
		targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
		targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true));

		if (p_i1741_1_ != null && !p_i1741_1_.isRemote) {
			setCombatTask();
		}
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.25D);
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		dataWatcher.addObject(13, new Byte((byte) 0));
	}

	@Override
	public boolean isAIEnabled() {
		return true;
	}

	@Override
	protected String getLivingSound() {
		return "mob.skeleton.say";
	}

	@Override
	protected String getHurtSound() {
		return "mob.skeleton.hurt";
	}

	@Override
	protected String getDeathSound() {
		return "mob.skeleton.death";
	}

	@Override
	protected void func_145780_a(int p_145780_1_, int p_145780_2_, int p_145780_3_, Block p_145780_4_) {
		playSound("mob.skeleton.step", 0.15F, 1.0F);
	}

	@Override
	public boolean attackEntityAsMob(Entity p_70652_1_) {
		if (super.attackEntityAsMob(p_70652_1_)) {
			if (getSkeletonType() == 1 && p_70652_1_ instanceof EntityLivingBase) {
				((EntityLivingBase) p_70652_1_).addPotionEffect(new PotionEffect(Potion.wither.id, 200));
			}

			return true;
		} else
			return false;
	}

	@Override
	public EnumCreatureAttribute getCreatureAttribute() {
		return EnumCreatureAttribute.UNDEAD;
	}

	@Override
	public void onLivingUpdate() {
		if (worldObj.isDaytime() && !worldObj.isRemote) {
			float f = getBrightness(1.0F);

			if (f > 0.5F && rand.nextFloat() * 30.0F < (f - 0.4F) * 2.0F && worldObj.canBlockSeeTheSky(
					MathHelper.floor_double(posX), MathHelper.floor_double(posY), MathHelper.floor_double(posZ))) {
				boolean flag = true;
				ItemStack itemstack = getEquipmentInSlot(4);

				if (itemstack != null) {
					if (itemstack.isItemStackDamageable()) {
						itemstack.setItemDamage(itemstack.getItemDamageForDisplay() + rand.nextInt(2));

						if (itemstack.getItemDamageForDisplay() >= itemstack.getMaxDamage()) {
							renderBrokenItemStack(itemstack);
							setCurrentItemOrArmor(4, null);
						}
					}

					flag = false;
				}

				if (flag) {
					EntityCombustEvent event = new EntityCombustEvent(this.getBukkitEntity(), 8);
					Bukkit.getPluginManager().callEvent(event);
					if (!event.isCancelled()) {
						setFire(event.getDuration());
					}
				}
			}
		}

		if (worldObj.isRemote && getSkeletonType() == 1) {
			setSize(0.72F, 2.34F);
		}

		super.onLivingUpdate();
	}

	@Override
	public void updateRidden() {
		super.updateRidden();

		if (ridingEntity instanceof EntityCreature) {
			EntityCreature entitycreature = (EntityCreature) ridingEntity;
			renderYawOffset = entitycreature.renderYawOffset;
		}
	}

	@Override
	public void onDeath(DamageSource p_70645_1_) {
		super.onDeath(p_70645_1_);

		if (p_70645_1_.getSourceOfDamage() instanceof EntityArrow && p_70645_1_.getEntity() instanceof EntityPlayer) {
			EntityPlayer entityplayer = (EntityPlayer) p_70645_1_.getEntity();
			double d0 = entityplayer.posX - posX;
			double d1 = entityplayer.posZ - posZ;

			if (d0 * d0 + d1 * d1 >= 2500.0D) {
				entityplayer.triggerAchievement(AchievementList.snipeSkeleton);
			}
		}
	}

	@Override
	protected Item getDropItem() {
		return Items.arrow;
	}

	@Override
	protected void dropFewItems(boolean p_70628_1_, int p_70628_2_) {
		int j;
		int k;

		if (getSkeletonType() == 1) {
			j = rand.nextInt(3 + p_70628_2_) - 1;

			for (k = 0; k < j; ++k) {
				dropItem(Items.coal, 1);
			}
		} else {
			j = rand.nextInt(3 + p_70628_2_);

			for (k = 0; k < j; ++k) {
				dropItem(Items.arrow, 1);
			}
		}

		j = rand.nextInt(3 + p_70628_2_);

		for (k = 0; k < j; ++k) {
			dropItem(Items.bone, 1);
		}
	}

	@Override
	protected void dropRareDrop(int p_70600_1_) {
		if (getSkeletonType() == 1) {
			entityDropItem(new ItemStack(Items.skull, 1, 1), 0.0F);
		}
	}

	@Override
	protected void addRandomArmor() {
		super.addRandomArmor();
		setCurrentItemOrArmor(0, new ItemStack(Items.bow));
	}

	@Override
	public IEntityLivingData onSpawnWithEgg(IEntityLivingData p_110161_1_) {
		p_110161_1_ = super.onSpawnWithEgg(p_110161_1_);

		if (worldObj.provider instanceof WorldProviderHell && getRNG().nextInt(5) > 0) {
			tasks.addTask(4, aiAttackOnCollide);
			setSkeletonType(1);
			setCurrentItemOrArmor(0, new ItemStack(Items.stone_sword));
			getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(4.0D);
		} else {
			tasks.addTask(4, aiArrowAttack);
			addRandomArmor();
			enchantEquipment();
		}

		setCanPickUpLoot(rand.nextFloat() < 0.55F * worldObj.func_147462_b(posX, posY, posZ));

		if (getEquipmentInSlot(4) == null) {
			Calendar calendar = worldObj.getCurrentDate();

			if (calendar.get(2) + 1 == 10 && calendar.get(5) == 31 && rand.nextFloat() < 0.25F) {
				setCurrentItemOrArmor(4, new ItemStack(rand.nextFloat() < 0.1F ? Blocks.lit_pumpkin : Blocks.pumpkin));
				equipmentDropChances[4] = 0.0F;
			}
		}

		return p_110161_1_;
	}

	public void setCombatTask() {
		tasks.removeTask(aiAttackOnCollide);
		tasks.removeTask(aiArrowAttack);
		ItemStack itemstack = getHeldItem();

		if (itemstack != null && itemstack.getItem() == Items.bow) {
			tasks.addTask(4, aiArrowAttack);
		} else {
			tasks.addTask(4, aiAttackOnCollide);
		}
	}

	@Override
	public void attackEntityWithRangedAttack(EntityLivingBase p_82196_1_, float p_82196_2_) {
		EntityArrow entityarrow = new EntityArrow(worldObj, this, p_82196_1_, 1.6F,
				14 - worldObj.difficultySetting.getDifficultyId() * 4);
		int i = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, getHeldItem());
		int j = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, getHeldItem());
		entityarrow.setDamage(
				p_82196_2_ * 2.0F + rand.nextGaussian() * 0.25D + worldObj.difficultySetting.getDifficultyId() * 0.11F);

		if (i > 0) {
			entityarrow.setDamage(entityarrow.getDamage() + i * 0.5D + 0.5D);
		}

		if (j > 0) {
			entityarrow.setKnockbackStrength(j);
		}

		if (EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, getHeldItem()) > 0
				|| getSkeletonType() == 1) {
			entityarrow.setFire(100);
		}

		EntityShootBowEvent event = CraftEventFactory.callEntityShootBowEvent(this, getHeldItem(), entityarrow, 0.8F);
		if (event.isCancelled()) {
			event.getProjectile().remove();
			return;
		}
		if (event.getProjectile() == entityarrow.getBukkitEntity()) {
			worldObj.spawnEntityInWorld(entityarrow);
		}

		playSound("random.bow", 1.0F, 1.0F / (getRNG().nextFloat() * 0.4F + 0.8F));
	}

	public int getSkeletonType() {
		return dataWatcher.getWatchableObjectByte(13);
	}

	public void setSkeletonType(int p_82201_1_) {
		dataWatcher.updateObject(13, Byte.valueOf((byte) p_82201_1_));
		isImmuneToFire = p_82201_1_ == 1;

		if (p_82201_1_ == 1) {
			setSize(0.72F, 2.34F);
		} else {
			setSize(0.6F, 1.8F);
		}
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound p_70037_1_) {
		super.readEntityFromNBT(p_70037_1_);

		if (p_70037_1_.hasKey("SkeletonType", 99)) {
			byte b0 = p_70037_1_.getByte("SkeletonType");
			setSkeletonType(b0);
		}

		setCombatTask();
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound p_70014_1_) {
		super.writeEntityToNBT(p_70014_1_);
		p_70014_1_.setByte("SkeletonType", (byte) getSkeletonType());
	}

	@Override
	public void setCurrentItemOrArmor(int p_70062_1_, ItemStack p_70062_2_) {
		super.setCurrentItemOrArmor(p_70062_1_, p_70062_2_);

		if (!worldObj.isRemote && p_70062_1_ == 0) {
			setCombatTask();
		}
	}

	@Override
	public double getYOffset() {
		return super.getYOffset() - 0.5D;
	}
}