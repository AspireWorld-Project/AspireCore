package net.minecraft.entity;

import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.play.server.S1BPacketEntityAttach;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.stats.AchievementList;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.ForgeEventFactory;
import org.bukkit.Bukkit;
import org.bukkit.event.entity.EntityUnleashEvent;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public abstract class EntityLiving extends EntityLivingBase {
	public int livingSoundTime;
	protected int experienceValue;
	private EntityLookHelper lookHelper;
	private EntityMoveHelper moveHelper;
	private EntityJumpHelper jumpHelper;
	private EntityBodyHelper bodyHelper;
	private PathNavigate navigator;
	public final EntityAITasks tasks;
	public final EntityAITasks targetTasks;
	private EntityLivingBase attackTarget;
	private EntitySenses senses;
	private ItemStack[] equipment = new ItemStack[5];
	protected float[] equipmentDropChances = new float[5];
	private boolean canPickUpLoot;
	private boolean persistenceRequired;
	protected float defaultPitch;
	private Entity currentTarget;
	protected int numTicksToChaseTarget;
	private boolean isLeashed;
	private Entity leashedToEntity;
	private NBTTagCompound field_110170_bx;
	private static final String __OBFID = "CL_00001550";

	public EntityLiving(World p_i1595_1_) {
		super(p_i1595_1_);
		tasks = new EntityAITasks(p_i1595_1_ != null && p_i1595_1_.theProfiler != null ? p_i1595_1_.theProfiler : null);
		targetTasks = new EntityAITasks(
				p_i1595_1_ != null && p_i1595_1_.theProfiler != null ? p_i1595_1_.theProfiler : null);
		lookHelper = new EntityLookHelper(this);
		moveHelper = new EntityMoveHelper(this);
		jumpHelper = new EntityJumpHelper(this);
		bodyHelper = new EntityBodyHelper(this);
		navigator = new PathNavigate(this, p_i1595_1_);
		senses = new EntitySenses(this);

		for (int i = 0; i < equipmentDropChances.length; ++i) {
			equipmentDropChances[i] = 0.085F;
		}
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		getAttributeMap().registerAttribute(SharedMonsterAttributes.followRange).setBaseValue(16.0D);
	}

	public EntityLookHelper getLookHelper() {
		return lookHelper;
	}

	public EntityMoveHelper getMoveHelper() {
		return moveHelper;
	}

	public EntityJumpHelper getJumpHelper() {
		return jumpHelper;
	}

	public PathNavigate getNavigator() {
		return navigator;
	}

	public EntitySenses getEntitySenses() {
		return senses;
	}

	public EntityLivingBase getAttackTarget() {
		return attackTarget;
	}

	public void setAttackTarget(EntityLivingBase p_70624_1_) {
		attackTarget = p_70624_1_;
		ForgeHooks.onLivingSetAttackTarget(this, p_70624_1_);
	}

	public boolean canAttackClass(Class p_70686_1_) {
		return EntityCreeper.class != p_70686_1_ && EntityGhast.class != p_70686_1_;
	}

	public void eatGrassBonus() {
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		dataWatcher.addObject(11, Byte.valueOf((byte) 0));
		dataWatcher.addObject(10, "");
	}

	public int getTalkInterval() {
		return 80;
	}

	public void playLivingSound() {
		String s = getLivingSound();

		if (s != null) {
			playSound(s, getSoundVolume(), getSoundPitch());
		}
	}

	@Override
	public void onEntityUpdate() {
		super.onEntityUpdate();
		worldObj.theProfiler.startSection("mobBaseTick");

		if (isEntityAlive() && rand.nextInt(1000) < livingSoundTime++) {
			livingSoundTime = -getTalkInterval();
			playLivingSound();
		}

		worldObj.theProfiler.endSection();
	}

	@Override
	protected int getExperiencePoints(EntityPlayer p_70693_1_) {
		if (experienceValue > 0) {
			int i = experienceValue;
			ItemStack[] aitemstack = getLastActiveItems();

			for (int j = 0; j < aitemstack.length; ++j) {
				if (aitemstack[j] != null && equipmentDropChances[j] <= 1.0F) {
					i += 1 + rand.nextInt(3);
				}
			}

			return i;
		} else
			return experienceValue;
	}

	public void spawnExplosionParticle() {
		for (int i = 0; i < 20; ++i) {
			double d0 = rand.nextGaussian() * 0.02D;
			double d1 = rand.nextGaussian() * 0.02D;
			double d2 = rand.nextGaussian() * 0.02D;
			double d3 = 10.0D;
			worldObj.spawnParticle("explode", posX + rand.nextFloat() * width * 2.0F - width - d0 * d3,
					posY + rand.nextFloat() * height - d1 * d3,
					posZ + rand.nextFloat() * width * 2.0F - width - d2 * d3, d0, d1, d2);
		}
	}

	@Override
	public void onUpdate() {
		super.onUpdate();

		if (!worldObj.isRemote) {
			updateLeashedState();
		}
	}

	@Override
	protected float func_110146_f(float p_110146_1_, float p_110146_2_) {
		if (isAIEnabled()) {
			bodyHelper.func_75664_a();
			return p_110146_2_;
		} else
			return super.func_110146_f(p_110146_1_, p_110146_2_);
	}

	protected String getLivingSound() {
		return null;
	}

	protected Item getDropItem() {
		return Item.getItemById(0);
	}

	@Override
	protected void dropFewItems(boolean p_70628_1_, int p_70628_2_) {
		Item item = getDropItem();

		if (item != null) {
			int j = rand.nextInt(3);

			if (p_70628_2_ > 0) {
				j += rand.nextInt(p_70628_2_ + 1);
			}

			for (int k = 0; k < j; ++k) {
				dropItem(item, 1);
			}
		}
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound p_70014_1_) {
		super.writeEntityToNBT(p_70014_1_);
		p_70014_1_.setBoolean("CanPickUpLoot", canPickUpLoot());
		p_70014_1_.setBoolean("PersistenceRequired", persistenceRequired);
		NBTTagList nbttaglist = new NBTTagList();
		NBTTagCompound nbttagcompound1;

		for (int i = 0; i < equipment.length; ++i) {
			nbttagcompound1 = new NBTTagCompound();

			if (equipment[i] != null) {
				equipment[i].writeToNBT(nbttagcompound1);
			}

			nbttaglist.appendTag(nbttagcompound1);
		}

		p_70014_1_.setTag("Equipment", nbttaglist);
		NBTTagList nbttaglist1 = new NBTTagList();

		for (int j = 0; j < equipmentDropChances.length; ++j) {
			nbttaglist1.appendTag(new NBTTagFloat(equipmentDropChances[j]));
		}

		p_70014_1_.setTag("DropChances", nbttaglist1);
		p_70014_1_.setString("CustomName", getCustomNameTag());
		p_70014_1_.setBoolean("CustomNameVisible", getAlwaysRenderNameTag());
		p_70014_1_.setBoolean("Leashed", isLeashed);

		if (leashedToEntity != null) {
			nbttagcompound1 = new NBTTagCompound();

			if (leashedToEntity instanceof EntityLivingBase) {
				nbttagcompound1.setLong("UUIDMost", leashedToEntity.getUniqueID().getMostSignificantBits());
				nbttagcompound1.setLong("UUIDLeast", leashedToEntity.getUniqueID().getLeastSignificantBits());
			} else if (leashedToEntity instanceof EntityHanging) {
				EntityHanging entityhanging = (EntityHanging) leashedToEntity;
				nbttagcompound1.setInteger("X", entityhanging.field_146063_b);
				nbttagcompound1.setInteger("Y", entityhanging.field_146064_c);
				nbttagcompound1.setInteger("Z", entityhanging.field_146062_d);
			}

			p_70014_1_.setTag("Leash", nbttagcompound1);
		}
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound p_70037_1_) {
		super.readEntityFromNBT(p_70037_1_);
		setCanPickUpLoot(p_70037_1_.getBoolean("CanPickUpLoot"));
		persistenceRequired = p_70037_1_.getBoolean("PersistenceRequired");

		if (p_70037_1_.hasKey("CustomName", 8) && p_70037_1_.getString("CustomName").length() > 0) {
			setCustomNameTag(p_70037_1_.getString("CustomName"));
		}

		setAlwaysRenderNameTag(p_70037_1_.getBoolean("CustomNameVisible"));
		NBTTagList nbttaglist;
		int i;

		if (p_70037_1_.hasKey("Equipment", 9)) {
			nbttaglist = p_70037_1_.getTagList("Equipment", 10);

			for (i = 0; i < equipment.length; ++i) {
				equipment[i] = ItemStack.loadItemStackFromNBT(nbttaglist.getCompoundTagAt(i));
			}
		}

		if (p_70037_1_.hasKey("DropChances", 9)) {
			nbttaglist = p_70037_1_.getTagList("DropChances", 5);

			for (i = 0; i < nbttaglist.tagCount(); ++i) {
				equipmentDropChances[i] = nbttaglist.func_150308_e(i);
			}
		}

		isLeashed = p_70037_1_.getBoolean("Leashed");

		if (isLeashed && p_70037_1_.hasKey("Leash", 10)) {
			field_110170_bx = p_70037_1_.getCompoundTag("Leash");
		}
	}

	public void setMoveForward(float p_70657_1_) {
		moveForward = p_70657_1_;
	}

	@Override
	public void setAIMoveSpeed(float p_70659_1_) {
		super.setAIMoveSpeed(p_70659_1_);
		setMoveForward(p_70659_1_);
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		worldObj.theProfiler.startSection("looting");

		if (!worldObj.isRemote && canPickUpLoot() && !dead
				&& worldObj.getGameRules().getGameRuleBooleanValue("mobGriefing")) {
			List list = worldObj.getEntitiesWithinAABB(EntityItem.class, boundingBox.expand(1.0D, 0.0D, 1.0D));
			Iterator iterator = list.iterator();

			while (iterator.hasNext()) {
				EntityItem entityitem = (EntityItem) iterator.next();

				if (!entityitem.isDead && entityitem.getEntityItem() != null) {
					ItemStack itemstack = entityitem.getEntityItem();
					int i = getArmorPosition(itemstack);

					if (i > -1) {
						boolean flag = true;
						ItemStack itemstack1 = getEquipmentInSlot(i);

						if (itemstack1 != null) {
							if (i == 0) {
								if (itemstack.getItem() instanceof ItemSword
										&& !(itemstack1.getItem() instanceof ItemSword)) {
									flag = true;
								} else if (itemstack.getItem() instanceof ItemSword
										&& itemstack1.getItem() instanceof ItemSword) {
									ItemSword itemsword = (ItemSword) itemstack.getItem();
									ItemSword itemsword1 = (ItemSword) itemstack1.getItem();

									if (itemsword.func_150931_i() == itemsword1.func_150931_i()) {
										flag = itemstack.getItemDamage() > itemstack1.getItemDamage()
												|| itemstack.hasTagCompound() && !itemstack1.hasTagCompound();
									} else {
										flag = itemsword.func_150931_i() > itemsword1.func_150931_i();
									}
								} else {
									flag = false;
								}
							} else if (itemstack.getItem() instanceof ItemArmor
									&& !(itemstack1.getItem() instanceof ItemArmor)) {
								flag = true;
							} else if (itemstack.getItem() instanceof ItemArmor
									&& itemstack1.getItem() instanceof ItemArmor) {
								ItemArmor itemarmor = (ItemArmor) itemstack.getItem();
								ItemArmor itemarmor1 = (ItemArmor) itemstack1.getItem();

								if (itemarmor.damageReduceAmount == itemarmor1.damageReduceAmount) {
									flag = itemstack.getItemDamage() > itemstack1.getItemDamage()
											|| itemstack.hasTagCompound() && !itemstack1.hasTagCompound();
								} else {
									flag = itemarmor.damageReduceAmount > itemarmor1.damageReduceAmount;
								}
							} else {
								flag = false;
							}
						}

						if (flag) {
							if (itemstack1 != null && rand.nextFloat() - 0.1F < equipmentDropChances[i]) {
								entityDropItem(itemstack1, 0.0F);
							}

							if (itemstack.getItem() == Items.diamond && entityitem.func_145800_j() != null) {
								EntityPlayer entityplayer = worldObj.getPlayerEntityByName(entityitem.func_145800_j());

								if (entityplayer != null) {
									entityplayer.triggerAchievement(AchievementList.field_150966_x);
								}
							}

							setCurrentItemOrArmor(i, itemstack);
							equipmentDropChances[i] = 2.0F;
							persistenceRequired = true;
							onItemPickup(entityitem, 1);
							entityitem.setDead();
						}
					}
				}
			}
		}

		worldObj.theProfiler.endSection();
	}

	@Override
	protected boolean isAIEnabled() {
		return false;
	}

	protected boolean canDespawn() {
		return true;
	}

	protected void despawnEntity() {
		if (!canDespawn())
			return; // Зачем события кидать лишний раз? Если моду надо, в своих классах реализует
					// метод по-своему
		Result result = null;
		if (persistenceRequired) {
			entityAge = 0;
		} else if ((entityAge & 0x1F) == 0x1F
				&& (result = ForgeEventFactory.canEntityDespawn(this)) != Result.DEFAULT) {
			if (result == Result.DENY) {
				entityAge = 0;
			} else {
				setDead();
			}
		} else {
			EntityPlayer entityplayer = worldObj.getClosestPlayerToEntity(this, -1.0D);

			if (entityplayer != null) {
				double d0 = entityplayer.posX - posX;
				double d1 = entityplayer.posY - posY;
				double d2 = entityplayer.posZ - posZ;
				double d3 = d0 * d0 + d1 * d1 + d2 * d2;

				if (canDespawn() && d3 > getEntityDespawnDistance()) {
					setDead();
				}

				if (entityAge > 600 && rand.nextInt(800) == 0 && d3 > 1024.0D && canDespawn()) {
					setDead();
				} else if (d3 < 1024.0D) {
					entityAge = 0;
				}
			}
		}
	}

	@Override
	protected void updateAITasks() {
		++entityAge;
		worldObj.theProfiler.startSection("checkDespawn");
		despawnEntity();
		worldObj.theProfiler.endSection();
		worldObj.theProfiler.startSection("sensing");
		senses.clearSensingCache();
		worldObj.theProfiler.endSection();
		worldObj.theProfiler.startSection("targetSelector");
		targetTasks.onUpdateTasks();
		worldObj.theProfiler.endSection();
		worldObj.theProfiler.startSection("goalSelector");
		tasks.onUpdateTasks();
		worldObj.theProfiler.endSection();
		worldObj.theProfiler.startSection("navigation");
		navigator.onUpdateNavigation();
		worldObj.theProfiler.endSection();
		worldObj.theProfiler.startSection("mob tick");
		updateAITick();
		worldObj.theProfiler.endSection();
		worldObj.theProfiler.startSection("controls");
		worldObj.theProfiler.startSection("move");
		moveHelper.onUpdateMoveHelper();
		worldObj.theProfiler.endStartSection("look");
		lookHelper.onUpdateLook();
		worldObj.theProfiler.endStartSection("jump");
		jumpHelper.doJump();
		worldObj.theProfiler.endSection();
		worldObj.theProfiler.endSection();
	}

	@Override
	protected void updateEntityActionState() {
		super.updateEntityActionState();
		moveStrafing = 0.0F;
		moveForward = 0.0F;
		despawnEntity();
		float f = 8.0F;

		if (rand.nextFloat() < 0.02F) {
			EntityPlayer entityplayer = worldObj.getClosestPlayerToEntity(this, f);

			if (entityplayer != null) {
				currentTarget = entityplayer;
				numTicksToChaseTarget = 10 + rand.nextInt(20);
			} else {
				randomYawVelocity = (rand.nextFloat() - 0.5F) * 20.0F;
			}
		}

		if (currentTarget != null) {
			faceEntity(currentTarget, 10.0F, getVerticalFaceSpeed());

			if (numTicksToChaseTarget-- <= 0 || currentTarget.isDead
					|| currentTarget.getDistanceSqToEntity(this) > f * f) {
				currentTarget = null;
			}
		} else {
			if (rand.nextFloat() < 0.05F) {
				randomYawVelocity = (rand.nextFloat() - 0.5F) * 20.0F;
			}

			rotationYaw += randomYawVelocity;
			rotationPitch = defaultPitch;
		}

		boolean flag1 = isInWater();
		boolean flag = handleLavaMovement();

		if (flag1 || flag) {
			isJumping = rand.nextFloat() < 0.8F;
		}
	}

	public int getVerticalFaceSpeed() {
		return 40;
	}

	public void faceEntity(Entity p_70625_1_, float p_70625_2_, float p_70625_3_) {
		double d0 = p_70625_1_.posX - posX;
		double d2 = p_70625_1_.posZ - posZ;
		double d1;

		if (p_70625_1_ instanceof EntityLivingBase) {
			EntityLivingBase entitylivingbase = (EntityLivingBase) p_70625_1_;
			d1 = entitylivingbase.posY + entitylivingbase.getEyeHeight() - (posY + getEyeHeight());
		} else {
			d1 = (p_70625_1_.boundingBox.minY + p_70625_1_.boundingBox.maxY) / 2.0D - (posY + getEyeHeight());
		}

		double d3 = MathHelper.sqrt_double(d0 * d0 + d2 * d2);
		float f2 = (float) (Math.atan2(d2, d0) * 180.0D / Math.PI) - 90.0F;
		float f3 = (float) -(Math.atan2(d1, d3) * 180.0D / Math.PI);
		rotationPitch = updateRotation(rotationPitch, f3, p_70625_3_);
		rotationYaw = updateRotation(rotationYaw, f2, p_70625_2_);
	}

	private float updateRotation(float p_70663_1_, float p_70663_2_, float p_70663_3_) {
		float f3 = MathHelper.wrapAngleTo180_float(p_70663_2_ - p_70663_1_);

		if (f3 > p_70663_3_) {
			f3 = p_70663_3_;
		}

		if (f3 < -p_70663_3_) {
			f3 = -p_70663_3_;
		}

		return p_70663_1_ + f3;
	}

	public boolean getCanSpawnHere() {
		return worldObj.checkNoEntityCollision(boundingBox)
				&& worldObj.getCollidingBoundingBoxes(this, boundingBox).isEmpty()
				&& !worldObj.isAnyLiquid(boundingBox);
	}

	public float getRenderSizeModifier() {
		return 1.0F;
	}

	public int getMaxSpawnedInChunk() {
		return 4;
	}

	@Override
	public int getMaxSafePointTries() {
		if (getAttackTarget() == null)
			return 3;
		else {
			int i = (int) (getHealth() - getMaxHealth() * 0.33F);
			i -= (3 - worldObj.difficultySetting.getDifficultyId()) * 4;

			if (i < 0) {
				i = 0;
			}

			return i + 3;
		}
	}

	@Override
	public ItemStack getHeldItem() {
		return equipment[0];
	}

	@Override
	public ItemStack getEquipmentInSlot(int p_71124_1_) {
		return equipment[p_71124_1_];
	}

	public ItemStack func_130225_q(int p_130225_1_) {
		return equipment[p_130225_1_ + 1];
	}

	@Override
	public void setCurrentItemOrArmor(int p_70062_1_, ItemStack p_70062_2_) {
		equipment[p_70062_1_] = p_70062_2_;
	}

	@Override
	public ItemStack[] getLastActiveItems() {
		return equipment;
	}

	@Override
	protected void dropEquipment(boolean p_82160_1_, int p_82160_2_) {
		for (int j = 0; j < getLastActiveItems().length; ++j) {
			ItemStack itemstack = getEquipmentInSlot(j);
			boolean flag1 = equipmentDropChances[j] > 1.0F;

			if (itemstack != null && (p_82160_1_ || flag1)
					&& rand.nextFloat() - p_82160_2_ * 0.01F < equipmentDropChances[j]) {
				if (!flag1 && itemstack.isItemStackDamageable()) {
					int k = Math.max(itemstack.getMaxDamage() - 25, 1);
					int l = itemstack.getMaxDamage() - rand.nextInt(rand.nextInt(k) + 1);

					if (l > k) {
						l = k;
					}

					if (l < 1) {
						l = 1;
					}

					itemstack.setItemDamage(l);
				}

				entityDropItem(itemstack, 0.0F);
			}
		}
	}

	protected void addRandomArmor() {
		if (rand.nextFloat() < 0.15F * worldObj.func_147462_b(posX, posY, posZ)) {
			int i = rand.nextInt(2);
			float f = worldObj.difficultySetting == EnumDifficulty.HARD ? 0.1F : 0.25F;

			if (rand.nextFloat() < 0.095F) {
				++i;
			}

			if (rand.nextFloat() < 0.095F) {
				++i;
			}

			if (rand.nextFloat() < 0.095F) {
				++i;
			}

			for (int j = 3; j >= 0; --j) {
				ItemStack itemstack = func_130225_q(j);

				if (j < 3 && rand.nextFloat() < f) {
					break;
				}

				if (itemstack == null) {
					Item item = getArmorItemForSlot(j + 1, i);

					if (item != null) {
						setCurrentItemOrArmor(j + 1, new ItemStack(item));
					}
				}
			}
		}
	}

	public static int getArmorPosition(ItemStack p_82159_0_) {
		if (p_82159_0_.getItem() != Item.getItemFromBlock(Blocks.pumpkin) && p_82159_0_.getItem() != Items.skull) {
			if (p_82159_0_.getItem() instanceof ItemArmor) {
				switch (((ItemArmor) p_82159_0_.getItem()).armorType) {
				case 0:
					return 4;
				case 1:
					return 3;
				case 2:
					return 2;
				case 3:
					return 1;
				}
			}

			return 0;
		} else
			return 4;
	}

	public static Item getArmorItemForSlot(int p_82161_0_, int p_82161_1_) {
		switch (p_82161_0_) {
		case 4:
			if (p_82161_1_ == 0)
				return Items.leather_helmet;
			else if (p_82161_1_ == 1)
				return Items.golden_helmet;
			else if (p_82161_1_ == 2)
				return Items.chainmail_helmet;
			else if (p_82161_1_ == 3)
				return Items.iron_helmet;
			else if (p_82161_1_ == 4)
				return Items.diamond_helmet;
		case 3:
			if (p_82161_1_ == 0)
				return Items.leather_chestplate;
			else if (p_82161_1_ == 1)
				return Items.golden_chestplate;
			else if (p_82161_1_ == 2)
				return Items.chainmail_chestplate;
			else if (p_82161_1_ == 3)
				return Items.iron_chestplate;
			else if (p_82161_1_ == 4)
				return Items.diamond_chestplate;
		case 2:
			if (p_82161_1_ == 0)
				return Items.leather_leggings;
			else if (p_82161_1_ == 1)
				return Items.golden_leggings;
			else if (p_82161_1_ == 2)
				return Items.chainmail_leggings;
			else if (p_82161_1_ == 3)
				return Items.iron_leggings;
			else if (p_82161_1_ == 4)
				return Items.diamond_leggings;
		case 1:
			if (p_82161_1_ == 0)
				return Items.leather_boots;
			else if (p_82161_1_ == 1)
				return Items.golden_boots;
			else if (p_82161_1_ == 2)
				return Items.chainmail_boots;
			else if (p_82161_1_ == 3)
				return Items.iron_boots;
			else if (p_82161_1_ == 4)
				return Items.diamond_boots;
		default:
			return null;
		}
	}

	protected void enchantEquipment() {
		float f = worldObj.func_147462_b(posX, posY, posZ);

		if (getHeldItem() != null && rand.nextFloat() < 0.25F * f) {
			EnchantmentHelper.addRandomEnchantment(rand, getHeldItem(), (int) (5.0F + f * rand.nextInt(18)));
		}

		for (int i = 0; i < 4; ++i) {
			ItemStack itemstack = func_130225_q(i);

			if (itemstack != null && rand.nextFloat() < 0.5F * f) {
				EnchantmentHelper.addRandomEnchantment(rand, itemstack, (int) (5.0F + f * rand.nextInt(18)));
			}
		}
	}

	public IEntityLivingData onSpawnWithEgg(IEntityLivingData p_110161_1_) {
		getEntityAttribute(SharedMonsterAttributes.followRange)
				.applyModifier(new AttributeModifier("Random spawn bonus", rand.nextGaussian() * 0.05D, 1));
		return p_110161_1_;
	}

	public boolean canBeSteered() {
		return false;
	}

	@Override
	public String getCommandSenderName() {
		return hasCustomNameTag() ? getCustomNameTag() : super.getCommandSenderName();
	}

	public void func_110163_bv() {
		persistenceRequired = true;
	}

	public void setCustomNameTag(String p_94058_1_) {
		dataWatcher.updateObject(10, p_94058_1_);
	}

	public String getCustomNameTag() {
		return dataWatcher.getWatchableObjectString(10);
	}

	public boolean hasCustomNameTag() {
		return dataWatcher.getWatchableObjectString(10).length() > 0;
	}

	public void setAlwaysRenderNameTag(boolean p_94061_1_) {
		dataWatcher.updateObject(11, Byte.valueOf((byte) (p_94061_1_ ? 1 : 0)));
	}

	public boolean getAlwaysRenderNameTag() {
		return dataWatcher.getWatchableObjectByte(11) == 1;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean getAlwaysRenderNameTagForRender() {
		return getAlwaysRenderNameTag();
	}

	public void setEquipmentDropChance(int p_96120_1_, float p_96120_2_) {
		equipmentDropChances[p_96120_1_] = p_96120_2_;
	}

	public boolean canPickUpLoot() {
		return canPickUpLoot;
	}

	public void setCanPickUpLoot(boolean p_98053_1_) {
		canPickUpLoot = p_98053_1_;
	}

	public boolean isNoDespawnRequired() {
		return persistenceRequired;
	}

	@Override
	public final boolean interactFirst(EntityPlayer p_130002_1_) {
		if (getLeashed() && getLeashedToEntity() == p_130002_1_) {
			clearLeashed(true, !p_130002_1_.capabilities.isCreativeMode);
			return true;
		} else {
			ItemStack itemstack = p_130002_1_.inventory.getCurrentItem();

			if (itemstack != null && itemstack.getItem() == Items.lead && allowLeashing()) {
				if (!(this instanceof EntityTameable) || !((EntityTameable) this).isTamed()) {
					setLeashedToEntity(p_130002_1_, true);
					--itemstack.stackSize;
					return true;
				}

				if (((EntityTameable) this).func_152114_e(p_130002_1_)) {
					setLeashedToEntity(p_130002_1_, true);
					--itemstack.stackSize;
					return true;
				}
			}

			return interact(p_130002_1_) ? true : super.interactFirst(p_130002_1_);
		}
	}

	protected boolean interact(EntityPlayer p_70085_1_) {
		return false;
	}

	protected void updateLeashedState() {
		if (field_110170_bx != null) {
			recreateLeash();
		}

		if (isLeashed) {
			if (leashedToEntity == null || leashedToEntity.isDead) {
				Bukkit.getPluginManager().callEvent(
						new EntityUnleashEvent(this.getBukkitEntity(), EntityUnleashEvent.UnleashReason.HOLDER_GONE));
				clearLeashed(true, true);
			}
		}
	}

	public void clearLeashed(boolean p_110160_1_, boolean p_110160_2_) {
		if (isLeashed) {
			isLeashed = false;
			leashedToEntity = null;

			if (!worldObj.isRemote && p_110160_2_) {
				dropItem(Items.lead, 1);
			}

			if (!worldObj.isRemote && p_110160_1_ && worldObj instanceof WorldServer) {
				((WorldServer) worldObj).getEntityTracker().func_151247_a(this,
						new S1BPacketEntityAttach(1, this, (Entity) null));
			}
		}
	}

	public boolean allowLeashing() {
		return !getLeashed() && !(this instanceof IMob);
	}

	public boolean getLeashed() {
		return isLeashed;
	}

	public Entity getLeashedToEntity() {
		return leashedToEntity;
	}

	public void setLeashedToEntity(Entity p_110162_1_, boolean p_110162_2_) {
		isLeashed = true;
		leashedToEntity = p_110162_1_;

		if (!worldObj.isRemote && p_110162_2_ && worldObj instanceof WorldServer) {
			((WorldServer) worldObj).getEntityTracker().func_151247_a(this,
					new S1BPacketEntityAttach(1, this, leashedToEntity));
		}
	}

	private void recreateLeash() {
		if (isLeashed && field_110170_bx != null) {
			if (field_110170_bx.hasKey("UUIDMost", 4) && field_110170_bx.hasKey("UUIDLeast", 4)) {
				UUID uuid = new UUID(field_110170_bx.getLong("UUIDMost"), field_110170_bx.getLong("UUIDLeast"));
				List list = worldObj.getEntitiesWithinAABB(EntityLivingBase.class,
						boundingBox.expand(10.0D, 10.0D, 10.0D));
				Iterator iterator = list.iterator();

				while (iterator.hasNext()) {
					EntityLivingBase entitylivingbase = (EntityLivingBase) iterator.next();

					if (entitylivingbase.getUniqueID().equals(uuid)) {
						leashedToEntity = entitylivingbase;
						break;
					}
				}
			} else if (field_110170_bx.hasKey("X", 99) && field_110170_bx.hasKey("Y", 99)
					&& field_110170_bx.hasKey("Z", 99)) {
				int i = field_110170_bx.getInteger("X");
				int j = field_110170_bx.getInteger("Y");
				int k = field_110170_bx.getInteger("Z");
				EntityLeashKnot entityleashknot = EntityLeashKnot.getKnotForBlock(worldObj, i, j, k);

				if (entityleashknot == null) {
					entityleashknot = EntityLeashKnot.func_110129_a(worldObj, i, j, k);
				}

				leashedToEntity = entityleashknot;
			} else {
				Bukkit.getPluginManager().callEvent(
						new EntityUnleashEvent(this.getBukkitEntity(), EntityUnleashEvent.UnleashReason.UNKNOWN));
				clearLeashed(false, true);
			}
		}

		field_110170_bx = null;
	}

	/*
	 * ===================================== ULTRAMINE START
	 * =====================================
	 */

	@Override
	public boolean isEntityLiving() {
		return true;
	}

	public EnumCreatureType getCreatureType() {
		if (isEntityAnimal())
			return EnumCreatureType.creature;
		if (isEntityAmbient())
			return EnumCreatureType.ambient;
		if (isEntityWater())
			return EnumCreatureType.waterCreature;

		return EnumCreatureType.monster;
	}

	@Override
	public void updateInactive() {
		if (!canDespawn() || ++entityAge <= 600)
			return;

		EntityPlayer player = worldObj.getClosestPlayerToEntity(this, -1.0D);

		if (player != null) {
			double distX = player.posX - posX;
			double distY = player.posY - posY;
			double distZ = player.posZ - posZ;
			double square = distX * distX + distY * distY + distZ * distZ;

			if (square > getEntityDespawnDistance()) {
				worldObj.getEventProxy().startEntity(this);
				setDead();
			}
		}
	}

	public boolean isPersistenceRequired() {
		return persistenceRequired;
	}

	public void setPersistenceRequired(boolean persistenceRequired) {
		this.persistenceRequired = persistenceRequired;
	}

	public boolean isCanPickUpLoot() {
		return canPickUpLoot;
	}
}
