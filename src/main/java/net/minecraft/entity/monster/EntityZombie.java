package net.minecraft.entity.monster;

import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.living.ZombieEvent.SummonAidEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.entity.EntityCombustEvent;

import java.util.Calendar;
import java.util.List;
import java.util.UUID;

public class EntityZombie extends EntityMob {
	protected static final IAttribute field_110186_bp = new RangedAttribute("zombie.spawnReinforcements", 0.0D, 0.0D,
			1.0D).setDescription("Spawn Reinforcements Chance");
	private static final UUID babySpeedBoostUUID = UUID.fromString("B9766B59-9566-4402-BC1F-2EE2A276D836");
	private static final AttributeModifier babySpeedBoostModifier = new AttributeModifier(babySpeedBoostUUID,
			"Baby speed boost", 0.5D, 1);
	private final EntityAIBreakDoor field_146075_bs = new EntityAIBreakDoor(this);
	private int conversionTime;
	private boolean field_146076_bu = false;
	private float field_146074_bv = -1.0F;
	private float field_146073_bw;
	private static final String __OBFID = "CL_00001702";

	public EntityZombie(World p_i1745_1_) {
		super(p_i1745_1_);
		getNavigator().setBreakDoors(true);
		tasks.addTask(0, new EntityAISwimming(this));
		tasks.addTask(2, new EntityAIAttackOnCollide(this, EntityPlayer.class, 1.0D, false));
		tasks.addTask(4, new EntityAIAttackOnCollide(this, EntityVillager.class, 1.0D, true));
		tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 1.0D));
		tasks.addTask(6, new EntityAIMoveThroughVillage(this, 1.0D, false));
		tasks.addTask(7, new EntityAIWander(this, 1.0D));
		tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		tasks.addTask(8, new EntityAILookIdle(this));
		targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
		targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true));
		targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityVillager.class, 0, false));
		setSize(0.6F, 1.8F);
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(16.0D);
		getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.23000000417232513D);
		getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(3.0D);
		getAttributeMap().registerAttribute(field_110186_bp)
				.setBaseValue(rand.nextDouble() * ForgeModContainer.zombieSummonBaseChance);
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		getDataWatcher().addObject(12, Byte.valueOf((byte) 0));
		getDataWatcher().addObject(13, Byte.valueOf((byte) 0));
		getDataWatcher().addObject(14, Byte.valueOf((byte) 0));
	}

	@Override
	public int getTotalArmorValue() {
		int i = super.getTotalArmorValue() + 2;

		if (i > 20) {
			i = 20;
		}

		return i;
	}

	@Override
	protected boolean isAIEnabled() {
		return true;
	}

	public boolean func_146072_bX() {
		return field_146076_bu;
	}

	public void func_146070_a(boolean p_146070_1_) {
		if (field_146076_bu != p_146070_1_) {
			field_146076_bu = p_146070_1_;

			if (p_146070_1_) {
				tasks.addTask(1, field_146075_bs);
			} else {
				tasks.removeTask(field_146075_bs);
			}
		}
	}

	@Override
	public boolean isChild() {
		return getDataWatcher().getWatchableObjectByte(12) == 1;
	}

	@Override
	protected int getExperiencePoints(EntityPlayer p_70693_1_) {
		if (isChild()) {
			experienceValue = (int) (experienceValue * 2.5F);
		}

		return super.getExperiencePoints(p_70693_1_);
	}

	public void setChild(boolean p_82227_1_) {
		getDataWatcher().updateObject(12, Byte.valueOf((byte) (p_82227_1_ ? 1 : 0)));

		if (worldObj != null && !worldObj.isRemote) {
			IAttributeInstance iattributeinstance = getEntityAttribute(SharedMonsterAttributes.movementSpeed);
			iattributeinstance.removeModifier(babySpeedBoostModifier);

			if (p_82227_1_) {
				iattributeinstance.applyModifier(babySpeedBoostModifier);
			}
		}

		func_146071_k(p_82227_1_);
	}

	public boolean isVillager() {
		return getDataWatcher().getWatchableObjectByte(13) == 1;
	}

	public void setVillager(boolean p_82229_1_) {
		getDataWatcher().updateObject(13, Byte.valueOf((byte) (p_82229_1_ ? 1 : 0)));
	}

	@Override
	public void onLivingUpdate() {
		if (worldObj.isDaytime() && !worldObj.isRemote && !isChild()) {
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

					if (!(isImmuneToFire() || this instanceof EntityPigZombie)) {
						EntityCombustEvent event = new EntityCombustEvent(this.getBukkitEntity(), 8);
						Bukkit.getPluginManager().callEvent(event);
						if (!event.isCancelled()) {
							setFire(event.getDuration());
						}
					}
				}
			}
		}

		if (isRiding() && getAttackTarget() != null && ridingEntity instanceof EntityChicken) {
			((EntityLiving) ridingEntity).getNavigator().setPath(getNavigator().getPath(), 1.5D);
		}

		super.onLivingUpdate();
	}

	@Override
	public boolean attackEntityFrom(DamageSource p_70097_1_, float p_70097_2_) {
		if (!super.attackEntityFrom(p_70097_1_, p_70097_2_))
			return false;
		else {
			EntityLivingBase entitylivingbase = getAttackTarget();

			if (entitylivingbase == null && getEntityToAttack() instanceof EntityLivingBase) {
				entitylivingbase = (EntityLivingBase) getEntityToAttack();
			}

			if (entitylivingbase == null && p_70097_1_.getEntity() instanceof EntityLivingBase) {
				entitylivingbase = (EntityLivingBase) p_70097_1_.getEntity();
			}

			int i = MathHelper.floor_double(posX);
			int j = MathHelper.floor_double(posY);
			int k = MathHelper.floor_double(posZ);

			SummonAidEvent summonAid = ForgeEventFactory.fireZombieSummonAid(this, worldObj, i, j, k, entitylivingbase,
					getEntityAttribute(field_110186_bp).getAttributeValue());

			if (summonAid.getResult() == Result.DENY)
				return true;
			else if (summonAid.getResult() == Result.ALLOW
					|| entitylivingbase != null && worldObj.difficultySetting == EnumDifficulty.HARD
							&& rand.nextFloat() < getEntityAttribute(field_110186_bp).getAttributeValue()) {
				EntityZombie entityzombie;
				if (summonAid.customSummonedAid != null && summonAid.getResult() == Result.ALLOW) {
					entityzombie = summonAid.customSummonedAid;
				} else {
					entityzombie = new EntityZombie(worldObj);
				}

				for (int l = 0; l < 50; ++l) {
					int i1 = i + MathHelper.getRandomIntegerInRange(rand, 7, 40)
							* MathHelper.getRandomIntegerInRange(rand, -1, 1);
					int j1 = j + MathHelper.getRandomIntegerInRange(rand, 7, 40)
							* MathHelper.getRandomIntegerInRange(rand, -1, 1);
					int k1 = k + MathHelper.getRandomIntegerInRange(rand, 7, 40)
							* MathHelper.getRandomIntegerInRange(rand, -1, 1);

					if (worldObj.blockExists(i1, j1, k1))
						if (World.doesBlockHaveSolidTopSurface(worldObj, i1, j1 - 1, k1)
								&& worldObj.getBlockLightValue(i1, j1, k1) < 10) {
							entityzombie.setPosition(i1, j1, k1);

							if (worldObj.checkNoEntityCollision(entityzombie.boundingBox) && worldObj
									.getCollidingBoundingBoxes(entityzombie, entityzombie.boundingBox).isEmpty()
									&& !worldObj.isAnyLiquid(entityzombie.boundingBox)) {
								worldObj.spawnEntityInWorld(entityzombie);
								if (entitylivingbase != null) {
									entityzombie.setAttackTarget(entitylivingbase);
								}
								entityzombie.onSpawnWithEgg(null);
								getEntityAttribute(field_110186_bp).applyModifier(new AttributeModifier(
										"Zombie reinforcement caller charge", -0.05000000074505806D, 0));
								entityzombie.getEntityAttribute(field_110186_bp).applyModifier(new AttributeModifier(
										"Zombie reinforcement callee charge", -0.05000000074505806D, 0));
								break;
							}
						}
				}
			}

			return true;
		}
	}

	@Override
	public void onUpdate() {
		if (!worldObj.isRemote && isConverting()) {
			int i = getConversionTimeBoost();
			conversionTime -= i;

			if (conversionTime <= 0) {
				convertToVillager();
			}
		}

		super.onUpdate();
	}

	@Override
	public boolean attackEntityAsMob(Entity p_70652_1_) {
		boolean flag = super.attackEntityAsMob(p_70652_1_);

		if (flag) {
			int i = worldObj.difficultySetting.getDifficultyId();

			if (getHeldItem() == null && isBurning() && rand.nextFloat() < i * 0.3F) {
				p_70652_1_.setFire(2 * i);
			}
		}

		return flag;
	}

	@Override
	protected String getLivingSound() {
		return "mob.zombie.say";
	}

	@Override
	protected String getHurtSound() {
		return "mob.zombie.hurt";
	}

	@Override
	protected String getDeathSound() {
		return "mob.zombie.death";
	}

	@Override
	protected void func_145780_a(int p_145780_1_, int p_145780_2_, int p_145780_3_, Block p_145780_4_) {
		playSound("mob.zombie.step", 0.15F, 1.0F);
	}

	@Override
	protected Item getDropItem() {
		return Items.rotten_flesh;
	}

	@Override
	public EnumCreatureAttribute getCreatureAttribute() {
		return EnumCreatureAttribute.UNDEAD;
	}

	@Override
	protected void dropRareDrop(int p_70600_1_) {
		switch (rand.nextInt(3)) {
		case 0:
			dropItem(Items.iron_ingot, 1);
			break;
		case 1:
			dropItem(Items.carrot, 1);
			break;
		case 2:
			dropItem(Items.potato, 1);
		}
	}

	@Override
	protected void addRandomArmor() {
		super.addRandomArmor();

		if (rand.nextFloat() < (worldObj.difficultySetting == EnumDifficulty.HARD ? 0.05F : 0.01F)) {
			int i = rand.nextInt(3);

			if (i == 0) {
				setCurrentItemOrArmor(0, new ItemStack(Items.iron_sword));
			} else {
				setCurrentItemOrArmor(0, new ItemStack(Items.iron_shovel));
			}
		}
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound p_70014_1_) {
		super.writeEntityToNBT(p_70014_1_);

		if (isChild()) {
			p_70014_1_.setBoolean("IsBaby", true);
		}

		if (isVillager()) {
			p_70014_1_.setBoolean("IsVillager", true);
		}

		p_70014_1_.setInteger("ConversionTime", isConverting() ? conversionTime : -1);
		p_70014_1_.setBoolean("CanBreakDoors", func_146072_bX());
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound p_70037_1_) {
		super.readEntityFromNBT(p_70037_1_);

		if (p_70037_1_.getBoolean("IsBaby")) {
			setChild(true);
		}

		if (p_70037_1_.getBoolean("IsVillager")) {
			setVillager(true);
		}

		if (p_70037_1_.hasKey("ConversionTime", 99) && p_70037_1_.getInteger("ConversionTime") > -1) {
			startConversion(p_70037_1_.getInteger("ConversionTime"));
		}

		func_146070_a(p_70037_1_.getBoolean("CanBreakDoors"));
	}

	@Override
	public void onKillEntity(EntityLivingBase p_70074_1_) {
		super.onKillEntity(p_70074_1_);

		if ((worldObj.difficultySetting == EnumDifficulty.NORMAL || worldObj.difficultySetting == EnumDifficulty.HARD)
				&& p_70074_1_ instanceof EntityVillager) {
			if (worldObj.difficultySetting != EnumDifficulty.HARD && rand.nextBoolean())
				return;

			EntityZombie entityzombie = new EntityZombie(worldObj);
			entityzombie.copyLocationAndAnglesFrom(p_70074_1_);
			worldObj.removeEntity(p_70074_1_);
			entityzombie.onSpawnWithEgg(null);
			entityzombie.setVillager(true);

			if (p_70074_1_.isChild()) {
				entityzombie.setChild(true);
			}

			worldObj.spawnEntityInWorld(entityzombie);
			worldObj.playAuxSFXAtEntity(null, 1016, (int) posX, (int) posY, (int) posZ, 0);
		}
	}

	@Override
	public IEntityLivingData onSpawnWithEgg(IEntityLivingData p_110161_1_) {
		Object p_110161_1_1 = super.onSpawnWithEgg(p_110161_1_);
		float f = worldObj.func_147462_b(posX, posY, posZ);
		setCanPickUpLoot(rand.nextFloat() < 0.55F * f);

		if (p_110161_1_1 == null) {
			p_110161_1_1 = new EntityZombie.GroupData(worldObj.rand.nextFloat() < ForgeModContainer.zombieBabyChance,
					worldObj.rand.nextFloat() < 0.05F, null);
		}

		if (p_110161_1_1 instanceof EntityZombie.GroupData) {
			EntityZombie.GroupData groupdata = (EntityZombie.GroupData) p_110161_1_1;

			if (groupdata.field_142046_b) {
				setVillager(true);
			}

			if (groupdata.field_142048_a) {
				setChild(true);

				if (worldObj.rand.nextFloat() < 0.05D) {
					List list = worldObj.selectEntitiesWithinAABB(EntityChicken.class,
							boundingBox.expand(5.0D, 3.0D, 5.0D), IEntitySelector.field_152785_b);

					if (!list.isEmpty()) {
						EntityChicken entitychicken = (EntityChicken) list.get(0);
						entitychicken.func_152117_i(true);
						mountEntity(entitychicken);
					}
				} else if (worldObj.rand.nextFloat() < 0.05D) {
					EntityChicken entitychicken1 = new EntityChicken(worldObj);
					entitychicken1.setLocationAndAngles(posX, posY, posZ, rotationYaw, 0.0F);
					entitychicken1.onSpawnWithEgg(null);
					entitychicken1.func_152117_i(true);
					worldObj.spawnEntityInWorld(entitychicken1);
					mountEntity(entitychicken1);
				}
			}
		}

		func_146070_a(rand.nextFloat() < f * 0.1F);
		addRandomArmor();
		enchantEquipment();

		if (getEquipmentInSlot(4) == null) {
			Calendar calendar = worldObj.getCurrentDate();

			if (calendar.get(2) + 1 == 10 && calendar.get(5) == 31 && rand.nextFloat() < 0.25F) {
				setCurrentItemOrArmor(4, new ItemStack(rand.nextFloat() < 0.1F ? Blocks.lit_pumpkin : Blocks.pumpkin));
				equipmentDropChances[4] = 0.0F;
			}
		}

		getEntityAttribute(SharedMonsterAttributes.knockbackResistance).applyModifier(
				new AttributeModifier("Random spawn bonus", rand.nextDouble() * 0.05000000074505806D, 0));
		double d0 = rand.nextDouble() * 1.5D * worldObj.func_147462_b(posX, posY, posZ);

		if (d0 > 1.0D) {
			getEntityAttribute(SharedMonsterAttributes.followRange)
					.applyModifier(new AttributeModifier("Random zombie-spawn bonus", d0, 2));
		}

		if (rand.nextFloat() < f * 0.05F) {
			getEntityAttribute(field_110186_bp)
					.applyModifier(new AttributeModifier("Leader zombie bonus", rand.nextDouble() * 0.25D + 0.5D, 0));
			getEntityAttribute(SharedMonsterAttributes.maxHealth)
					.applyModifier(new AttributeModifier("Leader zombie bonus", rand.nextDouble() * 3.0D + 1.0D, 2));
			func_146070_a(true);
		}

		return (IEntityLivingData) p_110161_1_1;
	}

	@Override
	public boolean interact(EntityPlayer p_70085_1_) {
		ItemStack itemstack = p_70085_1_.getCurrentEquippedItem();

		if (itemstack != null && itemstack.getItem() == Items.golden_apple && itemstack.getItemDamage() == 0
				&& isVillager() && this.isPotionActive(Potion.weakness)) {
			if (!p_70085_1_.capabilities.isCreativeMode) {
				--itemstack.stackSize;
			}

			if (itemstack.stackSize <= 0) {
				p_70085_1_.inventory.setInventorySlotContents(p_70085_1_.inventory.currentItem, null);
			}

			if (!worldObj.isRemote) {
				startConversion(rand.nextInt(2401) + 3600);
			}

			return true;
		} else
			return false;
	}

	protected void startConversion(int p_82228_1_) {
		conversionTime = p_82228_1_;
		getDataWatcher().updateObject(14, Byte.valueOf((byte) 1));
		removePotionEffect(Potion.weakness.id);
		addPotionEffect(new PotionEffect(Potion.damageBoost.id, p_82228_1_,
				Math.min(worldObj.difficultySetting.getDifficultyId() - 1, 0)));
		worldObj.setEntityState(this, (byte) 16);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void handleHealthUpdate(byte p_70103_1_) {
		if (p_70103_1_ == 16) {
			worldObj.playSound(posX + 0.5D, posY + 0.5D, posZ + 0.5D, "mob.zombie.remedy", 1.0F + rand.nextFloat(),
					rand.nextFloat() * 0.7F + 0.3F, false);
		} else {
			super.handleHealthUpdate(p_70103_1_);
		}
	}

	@Override
	protected boolean canDespawn() {
		return !isConverting();
	}

	public boolean isConverting() {
		return getDataWatcher().getWatchableObjectByte(14) == 1;
	}

	protected void convertToVillager() {
		EntityVillager entityvillager = new EntityVillager(worldObj);
		entityvillager.copyLocationAndAnglesFrom(this);
		entityvillager.onSpawnWithEgg(null);
		entityvillager.setLookingForHome();

		if (isChild()) {
			entityvillager.setGrowingAge(-24000);
		}

		worldObj.removeEntity(this);
		worldObj.spawnEntityInWorld(entityvillager);
		entityvillager.addPotionEffect(new PotionEffect(Potion.confusion.id, 200, 0));
		worldObj.playAuxSFXAtEntity(null, 1017, (int) posX, (int) posY, (int) posZ, 0);
	}

	protected int getConversionTimeBoost() {
		int i = 1;

		if (rand.nextFloat() < 0.01F) {
			int j = 0;

			for (int k = (int) posX - 4; k < (int) posX + 4 && j < 14; ++k) {
				for (int l = (int) posY - 4; l < (int) posY + 4 && j < 14; ++l) {
					for (int i1 = (int) posZ - 4; i1 < (int) posZ + 4 && j < 14; ++i1) {
						Block block = worldObj.getBlock(k, l, i1);

						if (block == Blocks.iron_bars || block == Blocks.bed) {
							if (rand.nextFloat() < 0.3F) {
								++i;
							}

							++j;
						}
					}
				}
			}
		}

		return i;
	}

	public void func_146071_k(boolean p_146071_1_) {
		func_146069_a(p_146071_1_ ? 0.5F : 1.0F);
	}

	@Override
	protected final void setSize(float p_70105_1_, float p_70105_2_) {
		boolean flag = field_146074_bv > 0.0F && field_146073_bw > 0.0F;
		field_146074_bv = p_70105_1_;
		field_146073_bw = p_70105_2_;

		if (!flag) {
			func_146069_a(1.0F);
		}
	}

	protected final void func_146069_a(float p_146069_1_) {
		super.setSize(field_146074_bv * p_146069_1_, field_146073_bw * p_146069_1_);
	}

	class GroupData implements IEntityLivingData {
		public boolean field_142048_a;
		public boolean field_142046_b;
		private static final String __OBFID = "CL_00001704";

		private GroupData(boolean p_i2348_2_, boolean p_i2348_3_) {
			field_142048_a = false;
			field_142046_b = false;
			field_142048_a = p_i2348_2_;
			field_142046_b = p_i2348_3_;
		}

		GroupData(boolean p_i2349_2_, boolean p_i2349_3_, Object p_i2349_4_) {
			this(p_i2349_2_, p_i2349_3_);
		}
	}
}