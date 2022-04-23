package net.minecraft.entity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.ai.attributes.*;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;
import net.minecraft.network.play.server.S04PacketEntityEquipment;
import net.minecraft.network.play.server.S0BPacketAnimation;
import net.minecraft.network.play.server.S0DPacketCollectItem;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionHelper;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.ISpecialArmor;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public abstract class EntityLivingBase extends Entity {
	private static final UUID sprintingSpeedBoostModifierUUID = UUID.fromString("662A6B8D-DA3E-4C1C-8813-96EA6097278D");
	private static final AttributeModifier sprintingSpeedBoostModifier = new AttributeModifier(
			sprintingSpeedBoostModifierUUID, "Sprinting speed boost", 0.30000001192092896D, 2).setSaved(false);
	private BaseAttributeMap attributeMap;
	private final CombatTracker _combatTracker = new CombatTracker(this);
	private final HashMap activePotionsMap = new HashMap();
	private final ItemStack[] previousEquipment = new ItemStack[5];
	public boolean isSwingInProgress;
	public int swingProgressInt;
	public int arrowHitTimer;
	public float prevHealth;
	public int hurtTime;
	public int maxHurtTime;
	public float attackedAtYaw;
	public int deathTime;
	public int attackTime;
	public float prevSwingProgress;
	public float swingProgress;
	public float prevLimbSwingAmount;
	public float limbSwingAmount;
	public float limbSwing;
	public int maxHurtResistantTime = 20;
	public float prevCameraPitch;
	public float cameraPitch;
	public float field_70769_ao;
	public float field_70770_ap;
	public float renderYawOffset;
	public float prevRenderYawOffset;
	public float rotationYawHead;
	public float prevRotationYawHead;
	public float jumpMovementFactor = 0.02F;
	protected EntityPlayer attackingPlayer;
	protected int recentlyHit;
	protected boolean dead;
	protected int entityAge;
	protected float field_70768_au;
	protected float field_110154_aX;
	protected float field_70764_aw;
	protected float field_70763_ax;
	protected float field_70741_aB;
	protected int scoreValue;
	protected float lastDamage;
	protected boolean isJumping;
	public float moveStrafing;
	public float moveForward;
	protected float randomYawVelocity;
	protected int newPosRotationIncrements;
	protected double newPosX;
	protected double newPosY;
	protected double newPosZ;
	protected double newRotationYaw;
	protected double newRotationPitch;
	private boolean potionsNeedUpdate = true;
	private EntityLivingBase entityLivingToAttack;
	private int revengeTimer;
	private EntityLivingBase lastAttacker;
	private int lastAttackerTime;
	private float landMovementFactor;
	private int jumpTicks;
	private float field_110151_bq;
	private static final String __OBFID = "CL_00001549";

	public int expToDrop;
	public int maxAirTicks = 300;

	public EntityLivingBase(World p_i1594_1_) {
		super(p_i1594_1_);
		applyEntityAttributes();
		dataWatcher.updateObject(6, getMaxHealth());
		setHealth(getMaxHealth());
		preventEntitySpawning = true;
		field_70770_ap = (float) (Math.random() + 1.0D) * 0.01F;
		setPosition(posX, posY, posZ);
		field_70769_ao = (float) Math.random() * 12398.0F;
		rotationYaw = (float) (Math.random() * Math.PI * 2.0D);
		rotationYawHead = rotationYaw;
		stepHeight = 0.5F;
	}

	public void heal(float p_70691_1_, EntityRegainHealthEvent.RegainReason regainReason) {
		p_70691_1_ = net.minecraftforge.event.ForgeEventFactory.onLivingHeal(this, p_70691_1_);
		if (p_70691_1_ <= 0)
			return;
		float f1 = getHealth();

		if (f1 > 0.0F) {
			EntityRegainHealthEvent event = new EntityRegainHealthEvent(getBukkitEntity(), p_70691_1_, regainReason);
			worldObj.getServer().getPluginManager().callEvent(event);

			if (!event.isCancelled()) {
				setHealth((float) (getHealth() + event.getAmount()));
			}
		}
	}

	public int getExpToDrop() {
		return expToDrop;
	}

	public void setExpToDrop(int expToDrop) {
		this.expToDrop = expToDrop;
	}

	public int getMaxAirTicks() {
		return maxAirTicks;
	}

	public void setMaxAirTicks(int maxAirTicks) {
		this.maxAirTicks = maxAirTicks;
	}

	public float getLastDamage() {
		return lastDamage;
	}

	public void setLastDamage(float lastDamage) {
		this.lastDamage = lastDamage;
	}

	public int getRecentlyHit() {
		return recentlyHit;
	}

	public void setRecentlyHit(int recentlyHit) {
		this.recentlyHit = recentlyHit;
	}

	public EntityPlayer getAttackingPlayer() {
		return attackingPlayer;
	}

	public HashMap<Integer, PotionEffect> getActivePotionsMap() {
		return activePotionsMap;
	}

	public float applyArmorCalculationsP(DamageSource source, float damage) {
		return applyArmorCalculations(source, damage);
	}

	public float applyPotionDamageCalculationsP(DamageSource source, float damage) {
		return applyPotionDamageCalculations(source, damage);
	}

	public int getExpReward() {
		int exp = getExperiencePoints(attackingPlayer);

		if (!worldObj.isRemote && (recentlyHit > 0 || isPlayer()) && func_146066_aG())
			return exp;
		else
			return 0;
	}

	@Override
	protected void entityInit() {
		dataWatcher.addObject(7, Integer.valueOf(0));
		dataWatcher.addObject(8, Byte.valueOf((byte) 0));
		dataWatcher.addObject(9, Byte.valueOf((byte) 0));
		dataWatcher.addObject(6, Float.valueOf(1.0F));
	}

	protected void applyEntityAttributes() {
		getAttributeMap().registerAttribute(SharedMonsterAttributes.maxHealth);
		getAttributeMap().registerAttribute(SharedMonsterAttributes.knockbackResistance);
		getAttributeMap().registerAttribute(SharedMonsterAttributes.movementSpeed);

		if (!isAIEnabled()) {
			getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.10000000149011612D);
		}
	}

	@Override
	protected void updateFallState(double p_70064_1_, boolean p_70064_3_) {
		if (!isInWater()) {
			handleWaterMovement();
		}

		if (p_70064_3_ && fallDistance > 0.0F) {
			int i = MathHelper.floor_double(posX);
			int j = MathHelper.floor_double(posY - 0.20000000298023224D - yOffset);
			int k = MathHelper.floor_double(posZ);
			Block block = worldObj.getBlock(i, j, k);

			if (block.getMaterial() == Material.air) {
				int l = worldObj.getBlock(i, j - 1, k).getRenderType();

				if (l == 11 || l == 32 || l == 21) {
					block = worldObj.getBlock(i, j - 1, k);
				}
			} else if (!worldObj.isRemote && fallDistance > 3.0F
					&& (!isEntityPlayerMP() || !((EntityPlayerMP) this).isHidden())) {
				worldObj.playAuxSFX(2006, i, j, k, MathHelper.ceiling_float_int(fallDistance - 3.0F));
			}

			block.onFallenUpon(worldObj, i, j, k, this, fallDistance);
		}

		super.updateFallState(p_70064_1_, p_70064_3_);
	}

	public boolean canBreatheUnderwater() {
		return false;
	}

	@Override
	public void onEntityUpdate() {
		prevSwingProgress = swingProgress;
		super.onEntityUpdate();
		worldObj.theProfiler.startSection("livingEntityBaseTick");

		if (isEntityAlive() && isEntityInsideOpaqueBlock()) {
			attackEntityFrom(DamageSource.inWall, 1.0F);
		}

		if (isImmuneToFire() || worldObj.isRemote) {
			extinguish();
		}

		boolean flag = this instanceof EntityPlayer && ((EntityPlayer) this).capabilities.disableDamage;

		if (isEntityAlive() && isInsideOfMaterial(Material.water)) {
			if (!canBreatheUnderwater() && !this.isPotionActive(Potion.waterBreathing.id) && !flag) {
				setAir(decreaseAirSupply(getAir()));

				if (getAir() == -20) {
					setAir(0);

					for (int i = 0; i < 8; ++i) {
						float f = rand.nextFloat() - rand.nextFloat();
						float f1 = rand.nextFloat() - rand.nextFloat();
						float f2 = rand.nextFloat() - rand.nextFloat();
						worldObj.spawnParticle("bubble", posX + f, posY + f1, posZ + f2, motionX, motionY, motionZ);
					}

					attackEntityFrom(DamageSource.drown, 2.0F);
				}
			}

			if (!worldObj.isRemote && isRiding() && ridingEntity != null && ridingEntity.shouldDismountInWater(this)) {
				mountEntity((Entity) null);
			}
		} else {
			if (getAir() != 300) {
				setAir(maxAirTicks);
			}
		}

		if (isEntityAlive() && isWet()) {
			extinguish();
		}

		prevCameraPitch = cameraPitch;

		if (attackTime > 0) {
			--attackTime;
		}

		if (hurtTime > 0) {
			--hurtTime;
		}

		if (hurtResistantTime > 0 && !(this instanceof EntityPlayerMP)) {
			--hurtResistantTime;
		}

		if (getHealth() <= 0.0F) {
			onDeathUpdate();
		}

		if (recentlyHit > 0) {
			--recentlyHit;
		} else {
			attackingPlayer = null;
		}

		if (lastAttacker != null && !lastAttacker.isEntityAlive()) {
			lastAttacker = null;
		}

		if (entityLivingToAttack != null) {
			if (!entityLivingToAttack.isEntityAlive()) {
				setRevengeTarget((EntityLivingBase) null);
			} else if (ticksExisted - revengeTimer > 100) {
				setRevengeTarget((EntityLivingBase) null);
			}
		}

		updatePotionEffects();
		field_70763_ax = field_70764_aw;
		prevRenderYawOffset = renderYawOffset;
		prevRotationYawHead = rotationYawHead;
		prevRotationYaw = rotationYaw;
		prevRotationPitch = rotationPitch;
		worldObj.theProfiler.endSection();
	}

	public boolean isChild() {
		return false;
	}

	protected void onDeathUpdate() {
		++deathTime;

		if (deathTime == 20) {
			int i;

			if (!worldObj.isRemote && (recentlyHit > 0 || isPlayer()) && func_146066_aG()
					&& worldObj.getGameRules().getGameRuleBooleanValue("doMobLoot")) {
				i = getExpToDrop();
				setExpToDrop(0);

				while (i > 0) {
					int j = EntityXPOrb.getXPSplit(i);
					i -= j;
					worldObj.spawnEntityInWorld(new EntityXPOrb(worldObj, posX, posY, posZ, j));
				}
			}

			setDead();

			for (i = 0; i < 20; ++i) {
				double d2 = rand.nextGaussian() * 0.02D;
				double d0 = rand.nextGaussian() * 0.02D;
				double d1 = rand.nextGaussian() * 0.02D;
				worldObj.spawnParticle("explode", posX + rand.nextFloat() * width * 2.0F - width,
						posY + rand.nextFloat() * height, posZ + rand.nextFloat() * width * 2.0F - width, d2, d0, d1);
			}
		}
	}

	protected boolean func_146066_aG() {
		return !isChild();
	}

	protected int decreaseAirSupply(int p_70682_1_) {
		int j = EnchantmentHelper.getRespiration(this);
		return j > 0 && rand.nextInt(j + 1) > 0 ? p_70682_1_ : p_70682_1_ - 1;
	}

	protected int getExperiencePoints(EntityPlayer p_70693_1_) {
		return 0;
	}

	protected boolean isPlayer() {
		return false;
	}

	public Random getRNG() {
		return rand;
	}

	public EntityLivingBase getAITarget() {
		return entityLivingToAttack;
	}

	public int func_142015_aE() {
		return revengeTimer;
	}

	public void setRevengeTarget(EntityLivingBase p_70604_1_) {
		entityLivingToAttack = p_70604_1_;
		revengeTimer = ticksExisted;
		ForgeHooks.onLivingSetAttackTarget(this, p_70604_1_);
	}

	public EntityLivingBase getLastAttacker() {
		return lastAttacker;
	}

	public int getLastAttackerTime() {
		return lastAttackerTime;
	}

	public void setLastAttacker(Entity p_130011_1_) {
		if (p_130011_1_ instanceof EntityLivingBase) {
			lastAttacker = (EntityLivingBase) p_130011_1_;
		} else {
			lastAttacker = null;
		}

		lastAttackerTime = ticksExisted;
	}

	public int getAge() {
		return entityAge;
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound p_70014_1_) {
		p_70014_1_.setFloat("HealF", getHealth());
		p_70014_1_.setShort("Health", (short) (int) Math.ceil(getHealth()));
		p_70014_1_.setShort("HurtTime", (short) hurtTime);
		p_70014_1_.setShort("DeathTime", (short) deathTime);
		p_70014_1_.setShort("AttackTime", (short) attackTime);
		p_70014_1_.setFloat("AbsorptionAmount", getAbsorptionAmount());
		ItemStack[] aitemstack = getLastActiveItems();
		int i = aitemstack.length;
		int j;
		ItemStack itemstack;

		for (j = 0; j < i; ++j) {
			itemstack = aitemstack[j];

			if (itemstack != null) {
				attributeMap.removeAttributeModifiers(itemstack.getAttributeModifiers());
			}
		}

		p_70014_1_.setTag("Attributes", SharedMonsterAttributes.writeBaseAttributeMapToNBT(getAttributeMap()));
		aitemstack = getLastActiveItems();
		i = aitemstack.length;

		for (j = 0; j < i; ++j) {
			itemstack = aitemstack[j];

			if (itemstack != null) {
				attributeMap.applyAttributeModifiers(itemstack.getAttributeModifiers());
			}
		}

		if (!activePotionsMap.isEmpty()) {
			NBTTagList nbttaglist = new NBTTagList();
			Iterator iterator = activePotionsMap.values().iterator();

			while (iterator.hasNext()) {
				PotionEffect potioneffect = (PotionEffect) iterator.next();
				nbttaglist.appendTag(potioneffect.writeCustomPotionEffectToNBT(new NBTTagCompound()));
			}

			p_70014_1_.setTag("ActiveEffects", nbttaglist);
		}
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound p_70037_1_) {
		setAbsorptionAmount(p_70037_1_.getFloat("AbsorptionAmount"));

		if (p_70037_1_.hasKey("Attributes", 9) && worldObj != null && !worldObj.isRemote) {
			SharedMonsterAttributes.func_151475_a(getAttributeMap(), p_70037_1_.getTagList("Attributes", 10));
		}

		if (p_70037_1_.hasKey("ActiveEffects", 9)) {
			NBTTagList nbttaglist = p_70037_1_.getTagList("ActiveEffects", 10);

			for (int i = 0; i < nbttaglist.tagCount(); ++i) {
				NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
				PotionEffect potioneffect = PotionEffect.readCustomPotionEffectFromNBT(nbttagcompound1);

				if (potioneffect != null) {
					activePotionsMap.put(Integer.valueOf(potioneffect.getPotionID()), potioneffect);
				}
			}
		}

		if (p_70037_1_.hasKey("HealF", 99)) {
			setHealth(p_70037_1_.getFloat("HealF"));
		} else {
			NBTBase nbtbase = p_70037_1_.getTag("Health");

			if (nbtbase == null) {
				setHealth(getMaxHealth());
			} else if (nbtbase.getId() == 5) {
				setHealth(((NBTTagFloat) nbtbase).func_150288_h());
			} else if (nbtbase.getId() == 2) {
				setHealth(((NBTTagShort) nbtbase).func_150289_e());
			}
		}

		hurtTime = p_70037_1_.getShort("HurtTime");
		deathTime = p_70037_1_.getShort("DeathTime");
		attackTime = p_70037_1_.getShort("AttackTime");
	}

	protected void updatePotionEffects() {
		Iterator iterator = activePotionsMap.keySet().iterator();

		while (iterator.hasNext()) {
			Integer integer = (Integer) iterator.next();
			PotionEffect potioneffect = (PotionEffect) activePotionsMap.get(integer);

			if (!potioneffect.onUpdate(this)) {
				if (!worldObj.isRemote) {
					iterator.remove();
					onFinishedPotionEffect(potioneffect);
				}
			} else if (potioneffect.getDuration() % 600 == 0) {
				onChangedPotionEffect(potioneffect, false);
			}
		}

		int i;

		if (potionsNeedUpdate) {
			if (!worldObj.isRemote) {
				if (activePotionsMap.isEmpty()) {
					dataWatcher.updateObject(8, Byte.valueOf((byte) 0));
					dataWatcher.updateObject(7, Integer.valueOf(0));
					setInvisible(false);
				} else {
					i = PotionHelper.calcPotionLiquidColor(activePotionsMap.values());
					dataWatcher.updateObject(8,
							Byte.valueOf((byte) (PotionHelper.func_82817_b(activePotionsMap.values()) ? 1 : 0)));
					dataWatcher.updateObject(7, Integer.valueOf(i));
					setInvisible(this.isPotionActive(Potion.invisibility.id));
				}
			}

			potionsNeedUpdate = false;
		}

		i = dataWatcher.getWatchableObjectInt(7);
		boolean flag1 = dataWatcher.getWatchableObjectByte(8) > 0;

		if (i > 0) {
			boolean flag = false;

			if (!isInvisible()) {
				flag = rand.nextBoolean();
			} else {
				flag = rand.nextInt(15) == 0;
			}

			if (flag1) {
				flag &= rand.nextInt(5) == 0;
			}

			if (flag && i > 0) {
				double d0 = (i >> 16 & 255) / 255.0D;
				double d1 = (i >> 8 & 255) / 255.0D;
				double d2 = (i >> 0 & 255) / 255.0D;
				worldObj.spawnParticle(flag1 ? "mobSpellAmbient" : "mobSpell",
						posX + (rand.nextDouble() - 0.5D) * width, posY + rand.nextDouble() * height - yOffset,
						posZ + (rand.nextDouble() - 0.5D) * width, d0, d1, d2);
			}
		}
	}

	public void clearActivePotions() {
		Iterator iterator = activePotionsMap.keySet().iterator();

		while (iterator.hasNext()) {
			Integer integer = (Integer) iterator.next();
			PotionEffect potioneffect = (PotionEffect) activePotionsMap.get(integer);

			if (!worldObj.isRemote) {
				iterator.remove();
				onFinishedPotionEffect(potioneffect);
			}
		}
	}

	public Collection getActivePotionEffects() {
		return activePotionsMap.values();
	}

	public boolean isPotionActive(int p_82165_1_) {
		return activePotionsMap.containsKey(Integer.valueOf(p_82165_1_));
	}

	public boolean isPotionActive(Potion p_70644_1_) {
		return activePotionsMap.containsKey(Integer.valueOf(p_70644_1_.id));
	}

	public PotionEffect getActivePotionEffect(Potion p_70660_1_) {
		return (PotionEffect) activePotionsMap.get(Integer.valueOf(p_70660_1_.id));
	}

	public void addPotionEffect(PotionEffect p_70690_1_) {
		if (isPotionApplicable(p_70690_1_)) {
			if (activePotionsMap.containsKey(Integer.valueOf(p_70690_1_.getPotionID()))) {
				((PotionEffect) activePotionsMap.get(Integer.valueOf(p_70690_1_.getPotionID()))).combine(p_70690_1_);
				onChangedPotionEffect((PotionEffect) activePotionsMap.get(Integer.valueOf(p_70690_1_.getPotionID())),
						true);
			} else {
				activePotionsMap.put(Integer.valueOf(p_70690_1_.getPotionID()), p_70690_1_);
				onNewPotionEffect(p_70690_1_);
			}
		}
	}

	public boolean isPotionApplicable(PotionEffect p_70687_1_) {
		if (getCreatureAttribute() == EnumCreatureAttribute.UNDEAD) {
			int i = p_70687_1_.getPotionID();

			if (i == Potion.regeneration.id || i == Potion.poison.id)
				return false;
		}

		return true;
	}

	public boolean isEntityUndead() {
		return getCreatureAttribute() == EnumCreatureAttribute.UNDEAD;
	}

	public void removePotionEffectClient(int p_70618_1_) {
		activePotionsMap.remove(Integer.valueOf(p_70618_1_));
	}

	public void removePotionEffect(int p_82170_1_) {
		PotionEffect potioneffect = (PotionEffect) activePotionsMap.remove(Integer.valueOf(p_82170_1_));

		if (potioneffect != null) {
			onFinishedPotionEffect(potioneffect);
		}
	}

	protected void onNewPotionEffect(PotionEffect p_70670_1_) {
		potionsNeedUpdate = true;

		if (!worldObj.isRemote) {
			Potion.potionTypes[p_70670_1_.getPotionID()].applyAttributesModifiersToEntity(this, getAttributeMap(),
					p_70670_1_.getAmplifier());
		}
	}

	protected void onChangedPotionEffect(PotionEffect p_70695_1_, boolean p_70695_2_) {
		potionsNeedUpdate = true;

		if (p_70695_2_ && !worldObj.isRemote) {
			Potion.potionTypes[p_70695_1_.getPotionID()].removeAttributesModifiersFromEntity(this, getAttributeMap(),
					p_70695_1_.getAmplifier());
			Potion.potionTypes[p_70695_1_.getPotionID()].applyAttributesModifiersToEntity(this, getAttributeMap(),
					p_70695_1_.getAmplifier());
		}
	}

	protected void onFinishedPotionEffect(PotionEffect p_70688_1_) {
		potionsNeedUpdate = true;

		if (!worldObj.isRemote) {
			Potion.potionTypes[p_70688_1_.getPotionID()].removeAttributesModifiersFromEntity(this, getAttributeMap(),
					p_70688_1_.getAmplifier());
		}
	}

	public void heal(float p_70691_1_) {
		heal(p_70691_1_, EntityRegainHealthEvent.RegainReason.CUSTOM);
	}

	public float getHealth() {
		// CraftBukkit start - Use unscaled health
		if (this instanceof EntityPlayerMP)
			return (float) ((Player) getBukkitEntity()).getHealth();
		// CraftBukkit end
		return dataWatcher.getWatchableObjectFloat(6);
	}

	public void setHealth(float p_70606_1_) {
		// CraftBukkit start - Handle scaled health
		if (this instanceof EntityPlayerMP && ((EntityPlayerMP) this).getGameProfile() != null) { // dirty hack.
			CraftPlayer player = (CraftPlayer) getBukkitEntity();

			// Squeeze
			if (p_70606_1_ < 0.0F) {
				player.setRealHealth(0.0D);
			} else if (p_70606_1_ > player.getMaxHealth()) {
				player.setRealHealth(player.getMaxHealth());
			} else {
				player.setRealHealth(p_70606_1_);
			}

			dataWatcher.updateObject(6, Float.valueOf(player.getScaledHealth()));
			return;
		}
		// CraftBukkit end
		dataWatcher.updateObject(6, Float.valueOf(MathHelper.clamp_float(p_70606_1_, 0.0F, getMaxHealth())));
	}

	@Override
	public boolean attackEntityFrom(DamageSource p_70097_1_, float p_70097_2_) {
		if (ForgeHooks.onLivingAttack(this, p_70097_1_, p_70097_2_))
			return false;
		if (isEntityInvulnerable())
			return false;
		else if (worldObj.isRemote)
			return false;
		else {
			entityAge = 0;

			if (getHealth() <= 0.0F)
				return false;
			else if (p_70097_1_.isFireDamage() && this.isPotionActive(Potion.fireResistance))
				return false;
			else {
				// CraftBukkit - Moved into damageEntity_CB(DamageSource, float)
				// if ((p_70097_1_ == DamageSource.anvil || p_70097_1_ ==
				// DamageSource.fallingBlock) && this.getEquipmentInSlot(4) != null)
				// {
				// this.getEquipmentInSlot(4).damageItem((int)(p_70097_2_ * 4.0F +
				// this.rand.nextFloat() * p_70097_2_ * 2.0F), (EntityLivingBase) (Object)
				// this);
				// p_70097_2_ *= 0.75F;
				// }

				limbSwingAmount = 1.5F;
				boolean flag = true;

				if (hurtResistantTime > maxHurtResistantTime / 2.0F) {
					if (p_70097_2_ <= lastDamage)
						return false;

					// CraftBukkit start
					if (!damageEntity_CB(p_70097_1_, p_70097_2_ - lastDamage))
						return false;
					// CraftBukkit end
					lastDamage = p_70097_2_;
					flag = false;
				} else {
					// CraftBukkit start
					float previousHealth = getHealth();
					if (!damageEntity_CB(p_70097_1_, p_70097_2_))
						return false;
					lastDamage = p_70097_2_;
					prevHealth = previousHealth;
					hurtResistantTime = maxHurtResistantTime;
					// CraftBukkit end
					hurtTime = maxHurtTime = 10;
				}

				attackedAtYaw = 0.0F;
				Entity entity = p_70097_1_.getEntity();

				if (entity != null) {
					if (entity instanceof EntityLivingBase) {
						setRevengeTarget((EntityLivingBase) entity);
					}

					if (entity instanceof EntityPlayer) {
						recentlyHit = 100;
						attackingPlayer = (EntityPlayer) entity;
					} else if (entity instanceof net.minecraft.entity.passive.EntityTameable) {
						net.minecraft.entity.passive.EntityTameable entitywolf = (net.minecraft.entity.passive.EntityTameable) entity;

						if (entitywolf.isTamed()) {
							recentlyHit = 100;
							attackingPlayer = null;
						}
					}
				}

				if (flag) {
					worldObj.setEntityState(this, (byte) 2);

					if (p_70097_1_ != DamageSource.drown) {
						setBeenAttacked();
					}

					if (entity != null) {
						double d1 = entity.posX - posX;
						double d0;

						for (d0 = entity.posZ - posZ; d1 * d1 + d0 * d0 < 1.0E-4D; d0 = (Math.random() - Math.random())
								* 0.01D) {
							d1 = (Math.random() - Math.random()) * 0.01D;
						}

						attackedAtYaw = (float) (Math.atan2(d0, d1) * 180.0D / Math.PI) - rotationYaw;
						knockBack(entity, p_70097_2_, d1, d0);
					} else {
						attackedAtYaw = (int) (Math.random() * 2.0D) * 180;
					}
				}

				String s;

				if (getHealth() <= 0.0F) {
					s = getDeathSound();

					if (flag && s != null) {
						playSound(s, getSoundVolume(), getSoundPitch());
					}

					onDeath(p_70097_1_);
				} else {
					s = getHurtSound();

					if (flag && s != null) {
						playSound(s, getSoundVolume(), getSoundPitch());
					}
				}

				return true;
			}
		}
	}

	public void renderBrokenItemStack(ItemStack p_70669_1_) {
		playSound("random.break", 0.8F, 0.8F + worldObj.rand.nextFloat() * 0.4F);

		for (int i = 0; i < 5; ++i) {
			Vec3 vec3 = Vec3.createVectorHelper((rand.nextFloat() - 0.5D) * 0.1D, Math.random() * 0.1D + 0.1D, 0.0D);
			vec3.rotateAroundX(-rotationPitch * (float) Math.PI / 180.0F);
			vec3.rotateAroundY(-rotationYaw * (float) Math.PI / 180.0F);
			Vec3 vec31 = Vec3.createVectorHelper((rand.nextFloat() - 0.5D) * 0.3D, -rand.nextFloat() * 0.6D - 0.3D,
					0.6D);
			vec31.rotateAroundX(-rotationPitch * (float) Math.PI / 180.0F);
			vec31.rotateAroundY(-rotationYaw * (float) Math.PI / 180.0F);
			vec31 = vec31.addVector(posX, posY + getEyeHeight(), posZ);
			worldObj.spawnParticle("iconcrack_" + Item.getIdFromItem(p_70669_1_.getItem()), vec31.xCoord, vec31.yCoord,
					vec31.zCoord, vec3.xCoord, vec3.yCoord + 0.05D, vec3.zCoord);
		}
	}

	public void onDeath(DamageSource p_70645_1_) {
		if (ForgeHooks.onLivingDeath(this, p_70645_1_))
			return;
		Entity entity = p_70645_1_.getEntity();
		EntityLivingBase entitylivingbase = func_94060_bK();

		if (scoreValue >= 0 && entitylivingbase != null) {
			entitylivingbase.addToPlayerScore(this, scoreValue);
		}

		if (entity != null) {
			entity.onKillEntity(this);
		}

		dead = true;
		func_110142_aN().func_94549_h();

		if (!worldObj.isRemote) {
			int i = 0;

			if (entity instanceof EntityPlayer) {
				i = EnchantmentHelper.getLootingModifier((EntityLivingBase) entity);
			}

			captureDrops = true;
			capturedDrops.clear();
			int j = 0;

			if (func_146066_aG() && worldObj.getGameRules().getGameRuleBooleanValue("doMobLoot")) {
				dropFewItems(recentlyHit > 0, i);
				dropEquipment(recentlyHit > 0, i);

				if (recentlyHit > 0) {
					j = rand.nextInt(200) - i;

					if (j < 5) {
						dropRareDrop(j <= 0 ? 1 : 0);
					}
				}
			}

			captureDrops = false;

			if (!ForgeHooks.onLivingDrops(this, p_70645_1_, capturedDrops, i, recentlyHit > 0, j)) {
				for (EntityItem item : capturedDrops) {
					worldObj.spawnEntityInWorld(item);
				}
			}
		}

		worldObj.setEntityState(this, (byte) 3);
	}

	protected void dropEquipment(boolean p_82160_1_, int p_82160_2_) {
	}

	public void knockBack(Entity p_70653_1_, float p_70653_2_, double p_70653_3_, double p_70653_5_) {
		if (rand.nextDouble() >= getEntityAttribute(SharedMonsterAttributes.knockbackResistance).getAttributeValue()) {
			isAirBorne = true;
			float f1 = MathHelper.sqrt_double(p_70653_3_ * p_70653_3_ + p_70653_5_ * p_70653_5_);
			float f2 = 0.4F;
			motionX /= 2.0D;
			motionY /= 2.0D;
			motionZ /= 2.0D;
			motionX -= p_70653_3_ / f1 * f2;
			motionY += f2;
			motionZ -= p_70653_5_ / f1 * f2;

			if (motionY > 0.4000000059604645D) {
				motionY = 0.4000000059604645D;
			}
		}
	}

	protected String getHurtSound() {
		return "game.neutral.hurt";
	}

	protected String getDeathSound() {
		return "game.neutral.die";
	}

	protected void dropRareDrop(int p_70600_1_) {
	}

	protected void dropFewItems(boolean p_70628_1_, int p_70628_2_) {
	}

	public boolean isOnLadder() {
		int i = MathHelper.floor_double(posX);
		int j = MathHelper.floor_double(boundingBox.minY);
		int k = MathHelper.floor_double(posZ);
		Block block = worldObj.getBlock(i, j, k);
		return ForgeHooks.isLivingOnLadder(block, worldObj, i, j, k, this);
	}

	@Override
	public boolean isEntityAlive() {
		return !isDead && getHealth() > 0.0F;
	}

	@Override
	protected void fall(float p_70069_1_) {
		p_70069_1_ = ForgeHooks.onLivingFall(this, p_70069_1_);
		if (p_70069_1_ <= 0)
			return;
		super.fall(p_70069_1_);
		PotionEffect potioneffect = getActivePotionEffect(Potion.jump);
		float f1 = potioneffect != null ? (float) (potioneffect.getAmplifier() + 1) : 0.0F;
		int i = MathHelper.ceiling_float_int(p_70069_1_ - 3.0F - f1);

		if (i > 0) {
			playSound(func_146067_o(i), 1.0F, 1.0F);
			attackEntityFrom(DamageSource.fall, i);
			int j = MathHelper.floor_double(posX);
			int k = MathHelper.floor_double(posY - 0.20000000298023224D - yOffset);
			int l = MathHelper.floor_double(posZ);
			Block block = worldObj.getBlock(j, k, l);

			if (block.getMaterial() != Material.air) {
				Block.SoundType soundtype = block.stepSound;
				playSound(soundtype.getStepResourcePath(), soundtype.getVolume() * 0.5F, soundtype.getPitch() * 0.75F);
			}
		}
	}

	protected String func_146067_o(int p_146067_1_) {
		return p_146067_1_ > 4 ? "game.neutral.hurt.fall.big" : "game.neutral.hurt.fall.small";
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void performHurtAnimation() {
		hurtTime = maxHurtTime = 10;
		attackedAtYaw = 0.0F;
	}

	public int getTotalArmorValue() {
		int i = 0;
		ItemStack[] aitemstack = getLastActiveItems();
		int j = aitemstack.length;

		for (int k = 0; k < j; ++k) {
			ItemStack itemstack = aitemstack[k];

			if (itemstack != null && itemstack.getItem() instanceof ItemArmor) {
				int l = ((ItemArmor) itemstack.getItem()).damageReduceAmount;
				i += l;
			}
		}

		return i;
	}

	protected void damageArmor(float p_70675_1_) {
	}

	protected float applyArmorCalculations(DamageSource p_70655_1_, float p_70655_2_) {
		if (!p_70655_1_.isUnblockable()) {
			int i = 25 - getTotalArmorValue();
			float f1 = p_70655_2_ * i;
			// this.damageArmor(p_70655_2_); // CraftBukkit - Moved into
			// damageEntity_CB(DamageSource, float)
			p_70655_2_ = f1 / 25.0F;
		}

		return p_70655_2_;
	}

	protected float applyPotionDamageCalculations(DamageSource p_70672_1_, float p_70672_2_) {
		if (p_70672_1_.isDamageAbsolute())
			return p_70672_2_;
		else {

			int i;
			int j;
			float f1;

			// CraftBukkit - Moved to damageEntity_CB(DamageSource, float)
			// if (this.isPotionActive(Potion.resistance) && p_70672_1_ !=
			// DamageSource.outOfWorld)
			// {
			// i = (this.getActivePotionEffect(Potion.resistance).getAmplifier() + 1) * 5;
			// j = 25 - i;
			// f1 = p_70672_2_ * (float)j;
			// p_70672_2_ = f1 / 25.0F;
			// }

			if (p_70672_2_ <= 0.0F)
				return 0.0F;
			else {
				i = EnchantmentHelper.getEnchantmentModifierDamage(getLastActiveItems(), p_70672_1_);

				if (i > 20) {
					i = 20;
				}

				if (i > 0 && i <= 20) {
					j = 25 - i;
					f1 = p_70672_2_ * j;
					p_70672_2_ = f1 / 25.0F;
				}

				return p_70672_2_;
			}
		}
	}

	protected void damageEntity(final DamageSource damagesource, float damage) {
		damageEntity_CB(damagesource, damage);
	}

	protected boolean damageEntity_CB(final DamageSource damagesource, float damage) {
		EntityLivingBase entity = this;
		if (entity.isEntityInvulnerable())
			return false;

		damage = ForgeHooks.onLivingHurt(entity, damagesource, damage);
		if (damage <= 0)
			return true;

		EntityDamageEvent event = CraftEventFactory.handleLivingEntityDamageEvent(this, damagesource, damage);
		if (event.isCancelled())
			return false;

		damage = (float) event.getFinalDamage();
		// Apply damage to helmet
		if ((damagesource == DamageSource.anvil || damagesource == DamageSource.fallingBlock)
				&& getEquipmentInSlot(4) != null) {
			getEquipmentInSlot(4).damageItem((int) (event.getDamage() * 4.0F
					+ ThreadLocalRandom.current().nextFloat() * event.getDamage() * 2.0F), this);
		}

		final boolean human = entity instanceof EntityPlayer;

		// Apply damage to armor
		if (!damagesource.isUnblockable()) {
			float armorDamage = (float) (event.getDamage() + event.getDamage(EntityDamageEvent.DamageModifier.BLOCKING)
					+ event.getDamage(EntityDamageEvent.DamageModifier.HARD_HAT));
			if (human) {
				EntityPlayer player = (EntityPlayer) this;
				armorDamage = ISpecialArmor.ArmorProperties.ApplyArmor(player, player.inventory.armorInventory,
						damagesource, armorDamage);
			} else {
				damageArmor(armorDamage);
			}
		}

		float absorptionModifier = (float) -event.getDamage(EntityDamageEvent.DamageModifier.ABSORPTION);
		setAbsorptionAmount(Math.max(getAbsorptionAmount() - absorptionModifier, 0.0F));
		if (damage != 0.0F) {
			if (human) {
				((EntityPlayer) this).addExhaustion(damagesource.getHungerDamage());
			}
			// CraftBukkit end
			float f2 = getHealth();
			setHealth(f2 - damage);
			func_110142_aN().func_94547_a(damagesource, f2, damage);
			// CraftBukkit start
			if (human)
				return true;
			// CraftBukkit end
			setAbsorptionAmount(getAbsorptionAmount() - damage);
		}
		return true;
	}

	public CombatTracker func_110142_aN() {
		return _combatTracker;
	}

	public EntityLivingBase func_94060_bK() {
		return _combatTracker.func_94550_c() != null ? _combatTracker.func_94550_c()
				: attackingPlayer != null ? attackingPlayer
						: entityLivingToAttack != null ? entityLivingToAttack : null;
	}

	public final float getMaxHealth() {
		return (float) getEntityAttribute(SharedMonsterAttributes.maxHealth).getAttributeValue();
	}

	public final int getArrowCountInEntity() {
		return dataWatcher.getWatchableObjectByte(9);
	}

	public final void setArrowCountInEntity(int p_85034_1_) {
		dataWatcher.updateObject(9, Byte.valueOf((byte) p_85034_1_));
	}

	private int getArmSwingAnimationEnd() {
		return this.isPotionActive(Potion.digSpeed)
				? 6 - (1 + getActivePotionEffect(Potion.digSpeed).getAmplifier()) * 1
				: this.isPotionActive(Potion.digSlowdown)
						? 6 + (1 + getActivePotionEffect(Potion.digSlowdown).getAmplifier()) * 2
						: 6;
	}

	public void swingItem() {
		ItemStack stack = getHeldItem();

		if (stack != null && stack.getItem() != null) {
			Item item = stack.getItem();
			if (item.onEntitySwing(this, stack))
				return;
		}

		if (!isSwingInProgress || swingProgressInt >= getArmSwingAnimationEnd() / 2 || swingProgressInt < 0) {
			swingProgressInt = -1;
			isSwingInProgress = true;

			if (worldObj instanceof WorldServer) {
				((WorldServer) worldObj).getEntityTracker().func_151247_a(this, new S0BPacketAnimation(this, 0));
			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void handleHealthUpdate(byte p_70103_1_) {
		if (p_70103_1_ == 2) {
			limbSwingAmount = 1.5F;
			hurtResistantTime = maxHurtResistantTime;
			hurtTime = maxHurtTime = 10;
			attackedAtYaw = 0.0F;
			playSound(getHurtSound(), getSoundVolume(), (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
			attackEntityFrom(DamageSource.generic, 0.0F);
		} else if (p_70103_1_ == 3) {
			playSound(getDeathSound(), getSoundVolume(), (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
			setHealth(0.0F);
			onDeath(DamageSource.generic);
		} else {
			super.handleHealthUpdate(p_70103_1_);
		}
	}

	@Override
	protected void kill() {
		attackEntityFrom(DamageSource.outOfWorld, 4.0F);
	}

	protected void updateArmSwingProgress() {
		int i = getArmSwingAnimationEnd();

		if (isSwingInProgress) {
			++swingProgressInt;

			if (swingProgressInt >= i) {
				swingProgressInt = 0;
				isSwingInProgress = false;
			}
		} else {
			swingProgressInt = 0;
		}

		swingProgress = (float) swingProgressInt / (float) i;
	}

	public IAttributeInstance getEntityAttribute(IAttribute p_110148_1_) {
		return getAttributeMap().getAttributeInstance(p_110148_1_);
	}

	public BaseAttributeMap getAttributeMap() {
		if (attributeMap == null) {
			attributeMap = new ServersideAttributeMap();
		}

		return attributeMap;
	}

	public EnumCreatureAttribute getCreatureAttribute() {
		return EnumCreatureAttribute.UNDEFINED;
	}

	public abstract ItemStack getHeldItem();

	public abstract ItemStack getEquipmentInSlot(int p_71124_1_);

	@Override
	public abstract void setCurrentItemOrArmor(int p_70062_1_, ItemStack p_70062_2_);

	@Override
	public void setSprinting(boolean p_70031_1_) {
		super.setSprinting(p_70031_1_);
		IAttributeInstance iattributeinstance = getEntityAttribute(SharedMonsterAttributes.movementSpeed);

		if (iattributeinstance.getModifier(sprintingSpeedBoostModifierUUID) != null) {
			iattributeinstance.removeModifier(sprintingSpeedBoostModifier);
		}

		if (p_70031_1_) {
			iattributeinstance.applyModifier(sprintingSpeedBoostModifier);
		}
	}

	@Override
	public abstract ItemStack[] getLastActiveItems();

	protected float getSoundVolume() {
		return 1.0F;
	}

	protected float getSoundPitch() {
		return isChild() ? (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.5F
				: (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F;
	}

	protected boolean isMovementBlocked() {
		return getHealth() <= 0.0F;
	}

	public void setPositionAndUpdate(double p_70634_1_, double p_70634_3_, double p_70634_5_) {
		setLocationAndAngles(p_70634_1_, p_70634_3_, p_70634_5_, rotationYaw, rotationPitch);
	}

	public void dismountEntity(Entity p_110145_1_) {
		double d0 = p_110145_1_.posX;
		double d1 = p_110145_1_.boundingBox.minY + p_110145_1_.height;
		double d2 = p_110145_1_.posZ;
		byte b0 = 1;

		for (int i = -b0; i <= b0; ++i) {
			for (int j = -b0; j < b0; ++j) {
				if (i != 0 || j != 0) {
					int k = (int) (posX + i);
					int l = (int) (posZ + j);
					AxisAlignedBB axisalignedbb = boundingBox.getOffsetBoundingBox(i, 1.0D, j);

					if (worldObj.func_147461_a(axisalignedbb).isEmpty()) {
						if (World.doesBlockHaveSolidTopSurface(worldObj, k, (int) posY, l)) {
							setPositionAndUpdate(posX + i, posY + 1.0D, posZ + j);
							return;
						}

						if (World.doesBlockHaveSolidTopSurface(worldObj, k, (int) posY - 1, l)
								|| worldObj.getBlock(k, (int) posY - 1, l).getMaterial() == Material.water) {
							d0 = posX + i;
							d1 = posY + 1.0D;
							d2 = posZ + j;
						}
					}
				}
			}
		}

		setPositionAndUpdate(d0, d1, d2);
	}

	@SideOnly(Side.CLIENT)
	public boolean getAlwaysRenderNameTagForRender() {
		return false;
	}

	@SideOnly(Side.CLIENT)
	public IIcon getItemIcon(ItemStack p_70620_1_, int p_70620_2_) {
		return p_70620_1_.getItem().requiresMultipleRenderPasses()
				? p_70620_1_.getItem().getIconFromDamageForRenderPass(p_70620_1_.getItemDamage(), p_70620_2_)
				: p_70620_1_.getIconIndex();
	}

	protected void jump() {
		motionY = 0.41999998688697815D;

		if (this.isPotionActive(Potion.jump)) {
			motionY += (getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1F;
		}

		if (isSprinting()) {
			float f = rotationYaw * 0.017453292F;
			motionX -= MathHelper.sin(f) * 0.2F;
			motionZ += MathHelper.cos(f) * 0.2F;
		}

		isAirBorne = true;
		ForgeHooks.onLivingJump(this);
	}

	public void moveEntityWithHeading(float p_70612_1_, float p_70612_2_) {
		double d0;

		if (isInWater() && (!(this instanceof EntityPlayer) || !((EntityPlayer) this).capabilities.isFlying)) {
			d0 = posY;
			moveFlying(p_70612_1_, p_70612_2_, isAIEnabled() ? 0.04F : 0.02F);
			moveEntity(motionX, motionY, motionZ);
			motionX *= 0.800000011920929D;
			motionY *= 0.800000011920929D;
			motionZ *= 0.800000011920929D;
			motionY -= 0.02D;

			if (isCollidedHorizontally
					&& isOffsetPositionInLiquid(motionX, motionY + 0.6000000238418579D - posY + d0, motionZ)) {
				motionY = 0.30000001192092896D;
			}
		} else if (handleLavaMovement()
				&& (!(this instanceof EntityPlayer) || !((EntityPlayer) this).capabilities.isFlying)) {
			d0 = posY;
			moveFlying(p_70612_1_, p_70612_2_, 0.02F);
			moveEntity(motionX, motionY, motionZ);
			motionX *= 0.5D;
			motionY *= 0.5D;
			motionZ *= 0.5D;
			motionY -= 0.02D;

			if (isCollidedHorizontally
					&& isOffsetPositionInLiquid(motionX, motionY + 0.6000000238418579D - posY + d0, motionZ)) {
				motionY = 0.30000001192092896D;
			}
		} else {
			float f2 = 0.91F;

			if (onGround) {
				f2 = worldObj.getBlock(MathHelper.floor_double(posX), MathHelper.floor_double(boundingBox.minY) - 1,
						MathHelper.floor_double(posZ)).slipperiness * 0.91F;
			}

			float f3 = 0.16277136F / (f2 * f2 * f2);
			float f4;

			if (onGround) {
				f4 = getAIMoveSpeed() * f3;
			} else {
				f4 = jumpMovementFactor;
			}

			moveFlying(p_70612_1_, p_70612_2_, f4);
			f2 = 0.91F;

			if (onGround) {
				f2 = worldObj.getBlock(MathHelper.floor_double(posX), MathHelper.floor_double(boundingBox.minY) - 1,
						MathHelper.floor_double(posZ)).slipperiness * 0.91F;
			}

			if (isOnLadder()) {
				float f5 = 0.15F;

				if (motionX < -f5) {
					motionX = -f5;
				}

				if (motionX > f5) {
					motionX = f5;
				}

				if (motionZ < -f5) {
					motionZ = -f5;
				}

				if (motionZ > f5) {
					motionZ = f5;
				}

				fallDistance = 0.0F;

				if (motionY < -0.15D) {
					motionY = -0.15D;
				}

				boolean flag = isSneaking() && this instanceof EntityPlayer;

				if (flag && motionY < 0.0D) {
					motionY = 0.0D;
				}
			}

			moveEntity(motionX, motionY, motionZ);

			if (isCollidedHorizontally && isOnLadder()) {
				motionY = 0.2D;
			}

			if (worldObj.isRemote && (!worldObj.blockExists((int) posX, 0, (int) posZ)
					|| !worldObj.getChunkFromBlockCoords((int) posX, (int) posZ).isChunkLoaded)) {
				if (posY > 0.0D) {
					motionY = -0.1D;
				} else {
					motionY = 0.0D;
				}
			} else {
				motionY -= 0.08D;
			}

			motionY *= 0.9800000190734863D;
			motionX *= f2;
			motionZ *= f2;
		}

		prevLimbSwingAmount = limbSwingAmount;
		d0 = posX - prevPosX;
		double d1 = posZ - prevPosZ;
		float f6 = MathHelper.sqrt_double(d0 * d0 + d1 * d1) * 4.0F;

		if (f6 > 1.0F) {
			f6 = 1.0F;
		}

		limbSwingAmount += (f6 - limbSwingAmount) * 0.4F;
		limbSwing += limbSwingAmount;
	}

	protected boolean isAIEnabled() {
		return false;
	}

	public float getAIMoveSpeed() {
		return isAIEnabled() ? landMovementFactor : 0.1F;
	}

	public void setAIMoveSpeed(float p_70659_1_) {
		landMovementFactor = p_70659_1_;
	}

	public boolean attackEntityAsMob(Entity p_70652_1_) {
		setLastAttacker(p_70652_1_);
		return false;
	}

	public boolean isPlayerSleeping() {
		return false;
	}

	@Override
	public void onUpdate() {
		if (ForgeHooks.onLivingUpdate(this))
			return;
		super.onUpdate();

		if (!worldObj.isRemote) {
			int i = getArrowCountInEntity();

			if (i > 0) {
				if (arrowHitTimer <= 0) {
					arrowHitTimer = 20 * (30 - i);
				}

				--arrowHitTimer;

				if (arrowHitTimer <= 0) {
					setArrowCountInEntity(i - 1);
				}
			}

			for (int j = 0; j < 5; ++j) {
				ItemStack itemstack = previousEquipment[j];
				ItemStack itemstack1 = getEquipmentInSlot(j);

				if (!ItemStack.areItemStacksEqual(itemstack1, itemstack)) {
					((WorldServer) worldObj).getEntityTracker().func_151247_a(this,
							new S04PacketEntityEquipment(getEntityId(), j, itemstack1));

					if (itemstack != null) {
						attributeMap.removeAttributeModifiers(itemstack.getAttributeModifiers());
					}

					if (itemstack1 != null) {
						attributeMap.applyAttributeModifiers(itemstack1.getAttributeModifiers());
					}

					previousEquipment[j] = itemstack1 == null ? null : itemstack1.copy();
				}
			}

			if (ticksExisted % 20 == 0) {
				func_110142_aN().func_94549_h();
			}
		}

		onLivingUpdate();
		double d0 = posX - prevPosX;
		double d1 = posZ - prevPosZ;
		float f = (float) (d0 * d0 + d1 * d1);
		float f1 = renderYawOffset;
		float f2 = 0.0F;
		field_70768_au = field_110154_aX;
		float f3 = 0.0F;

		if (f > 0.0025000002F) {
			f3 = 1.0F;
			f2 = (float) Math.sqrt(f) * 3.0F;
			f1 = (float) Math.atan2(d1, d0) * 180.0F / (float) Math.PI - 90.0F;
		}

		if (swingProgress > 0.0F) {
			f1 = rotationYaw;
		}

		if (!onGround) {
			f3 = 0.0F;
		}

		field_110154_aX += (f3 - field_110154_aX) * 0.3F;
		worldObj.theProfiler.startSection("headTurn");
		f2 = func_110146_f(f1, f2);
		worldObj.theProfiler.endSection();
		worldObj.theProfiler.startSection("rangeChecks");

		while (rotationYaw - prevRotationYaw < -180.0F) {
			prevRotationYaw -= 360.0F;
		}

		while (rotationYaw - prevRotationYaw >= 180.0F) {
			prevRotationYaw += 360.0F;
		}

		while (renderYawOffset - prevRenderYawOffset < -180.0F) {
			prevRenderYawOffset -= 360.0F;
		}

		while (renderYawOffset - prevRenderYawOffset >= 180.0F) {
			prevRenderYawOffset += 360.0F;
		}

		while (rotationPitch - prevRotationPitch < -180.0F) {
			prevRotationPitch -= 360.0F;
		}

		while (rotationPitch - prevRotationPitch >= 180.0F) {
			prevRotationPitch += 360.0F;
		}

		while (rotationYawHead - prevRotationYawHead < -180.0F) {
			prevRotationYawHead -= 360.0F;
		}

		while (rotationYawHead - prevRotationYawHead >= 180.0F) {
			prevRotationYawHead += 360.0F;
		}

		worldObj.theProfiler.endSection();
		field_70764_aw += f2;
	}

	protected float func_110146_f(float p_110146_1_, float p_110146_2_) {
		float f2 = MathHelper.wrapAngleTo180_float(p_110146_1_ - renderYawOffset);
		renderYawOffset += f2 * 0.3F;
		float f3 = MathHelper.wrapAngleTo180_float(rotationYaw - renderYawOffset);
		boolean flag = f3 < -90.0F || f3 >= 90.0F;

		if (f3 < -75.0F) {
			f3 = -75.0F;
		}

		if (f3 >= 75.0F) {
			f3 = 75.0F;
		}

		renderYawOffset = rotationYaw - f3;

		if (f3 * f3 > 2500.0F) {
			renderYawOffset += f3 * 0.2F;
		}

		if (flag) {
			p_110146_2_ *= -1.0F;
		}

		return p_110146_2_;
	}

	public void onLivingUpdate() {
		if (jumpTicks > 0) {
			--jumpTicks;
		}

		if (newPosRotationIncrements > 0) {
			double d0 = posX + (newPosX - posX) / newPosRotationIncrements;
			double d1 = posY + (newPosY - posY) / newPosRotationIncrements;
			double d2 = posZ + (newPosZ - posZ) / newPosRotationIncrements;
			double d3 = MathHelper.wrapAngleTo180_double(newRotationYaw - rotationYaw);
			rotationYaw = (float) (rotationYaw + d3 / newPosRotationIncrements);
			rotationPitch = (float) (rotationPitch + (newRotationPitch - rotationPitch) / newPosRotationIncrements);
			--newPosRotationIncrements;
			setPosition(d0, d1, d2);
			setRotation(rotationYaw, rotationPitch);
		} else if (!isClientWorld()) {
			motionX *= 0.98D;
			motionY *= 0.98D;
			motionZ *= 0.98D;
		}

		if (Math.abs(motionX) < 0.005D) {
			motionX = 0.0D;
		}

		if (Math.abs(motionY) < 0.005D) {
			motionY = 0.0D;
		}

		if (Math.abs(motionZ) < 0.005D) {
			motionZ = 0.0D;
		}

		worldObj.theProfiler.startSection("ai");

		if (isMovementBlocked()) {
			isJumping = false;
			moveStrafing = 0.0F;
			moveForward = 0.0F;
			randomYawVelocity = 0.0F;
		} else if (isClientWorld()) {
			if (isAIEnabled()) {
				worldObj.theProfiler.startSection("newAi");
				updateAITasks();
				worldObj.theProfiler.endSection();
			} else {
				worldObj.theProfiler.startSection("oldAi");
				updateEntityActionState();
				worldObj.theProfiler.endSection();
				rotationYawHead = rotationYaw;
			}
		}

		worldObj.theProfiler.endSection();
		worldObj.theProfiler.startSection("jump");

		if (isJumping) {
			if (!isInWater() && !handleLavaMovement()) {
				if (onGround && jumpTicks == 0) {
					jump();
					jumpTicks = 10;
				}
			} else {
				motionY += 0.03999999910593033D;
			}
		} else {
			jumpTicks = 0;
		}

		worldObj.theProfiler.endSection();
		worldObj.theProfiler.startSection("travel");
		moveStrafing *= 0.98F;
		moveForward *= 0.98F;
		randomYawVelocity *= 0.9F;
		moveEntityWithHeading(moveStrafing, moveForward);
		worldObj.theProfiler.endSection();
		worldObj.theProfiler.startSection("push");

		if (!worldObj.isRemote) {
			collideWithNearbyEntities();
		}

		worldObj.theProfiler.endSection();
	}

	protected void updateAITasks() {
	}

	protected void collideWithNearbyEntities() {
		List list = worldObj.getEntitiesWithinAABBExcludingEntity(this,
				boundingBox.expand(0.20000000298023224D, 0.0D, 0.20000000298023224D));

		if (list != null && !list.isEmpty()) {
			for (int i = 0; i < list.size(); ++i) {
				Entity entity = (Entity) list.get(i);

				if (entity.canBePushed()) {
					collideWithEntity(entity);
				}
			}
		}
	}

	protected void collideWithEntity(Entity p_82167_1_) {
		p_82167_1_.applyEntityCollision(this);
	}

	@Override
	public void updateRidden() {
		super.updateRidden();
		field_70768_au = field_110154_aX;
		field_110154_aX = 0.0F;
		fallDistance = 0.0F;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void setPositionAndRotation2(double p_70056_1_, double p_70056_3_, double p_70056_5_, float p_70056_7_,
			float p_70056_8_, int p_70056_9_) {
		yOffset = 0.0F;
		newPosX = p_70056_1_;
		newPosY = p_70056_3_;
		newPosZ = p_70056_5_;
		newRotationYaw = p_70056_7_;
		newRotationPitch = p_70056_8_;
		newPosRotationIncrements = p_70056_9_;
	}

	protected void updateAITick() {
	}

	protected void updateEntityActionState() {
		++entityAge;
	}

	public void setJumping(boolean p_70637_1_) {
		isJumping = p_70637_1_;
	}

	public void onItemPickup(Entity p_71001_1_, int p_71001_2_) {
		if (!p_71001_1_.isDead && !worldObj.isRemote) {
			EntityTracker entitytracker = ((WorldServer) worldObj).getEntityTracker();

			if (p_71001_1_ instanceof EntityItem) {
				entitytracker.func_151247_a(p_71001_1_,
						new S0DPacketCollectItem(p_71001_1_.getEntityId(), getEntityId()));
			}

			if (p_71001_1_ instanceof EntityArrow) {
				entitytracker.func_151247_a(p_71001_1_,
						new S0DPacketCollectItem(p_71001_1_.getEntityId(), getEntityId()));
			}

			if (p_71001_1_ instanceof EntityXPOrb) {
				entitytracker.func_151247_a(p_71001_1_,
						new S0DPacketCollectItem(p_71001_1_.getEntityId(), getEntityId()));
			}
		}
	}

	public boolean canEntityBeSeen(Entity p_70685_1_) {
		return worldObj.rayTraceBlocks(Vec3.createVectorHelper(posX, posY + getEyeHeight(), posZ),
				Vec3.createVectorHelper(p_70685_1_.posX, p_70685_1_.posY + p_70685_1_.getEyeHeight(),
						p_70685_1_.posZ)) == null;
	}

	@Override
	public Vec3 getLookVec() {
		return getLook(1.0F);
	}

	public Vec3 getLook(float p_70676_1_) {
		float f1;
		float f2;
		float f3;
		float f4;

		if (p_70676_1_ == 1.0F) {
			f1 = MathHelper.cos(-rotationYaw * 0.017453292F - (float) Math.PI);
			f2 = MathHelper.sin(-rotationYaw * 0.017453292F - (float) Math.PI);
			f3 = -MathHelper.cos(-rotationPitch * 0.017453292F);
			f4 = MathHelper.sin(-rotationPitch * 0.017453292F);
			return Vec3.createVectorHelper(f2 * f3, f4, f1 * f3);
		} else {
			f1 = prevRotationPitch + (rotationPitch - prevRotationPitch) * p_70676_1_;
			f2 = prevRotationYaw + (rotationYaw - prevRotationYaw) * p_70676_1_;
			f3 = MathHelper.cos(-f2 * 0.017453292F - (float) Math.PI);
			f4 = MathHelper.sin(-f2 * 0.017453292F - (float) Math.PI);
			float f5 = -MathHelper.cos(-f1 * 0.017453292F);
			float f6 = MathHelper.sin(-f1 * 0.017453292F);
			return Vec3.createVectorHelper(f4 * f5, f6, f3 * f5);
		}
	}

	@SideOnly(Side.CLIENT)
	public float getSwingProgress(float p_70678_1_) {
		float f1 = swingProgress - prevSwingProgress;

		if (f1 < 0.0F) {
			++f1;
		}

		return prevSwingProgress + f1 * p_70678_1_;
	}

	@SideOnly(Side.CLIENT)
	public Vec3 getPosition(float p_70666_1_) {
		if (p_70666_1_ == 1.0F)
			return Vec3.createVectorHelper(posX, posY, posZ);
		else {
			double d0 = prevPosX + (posX - prevPosX) * p_70666_1_;
			double d1 = prevPosY + (posY - prevPosY) * p_70666_1_;
			double d2 = prevPosZ + (posZ - prevPosZ) * p_70666_1_;
			return Vec3.createVectorHelper(d0, d1, d2);
		}
	}

	@SideOnly(Side.CLIENT)
	public MovingObjectPosition rayTrace(double p_70614_1_, float p_70614_3_) {
		Vec3 vec3 = getPosition(p_70614_3_);
		Vec3 vec31 = getLook(p_70614_3_);
		Vec3 vec32 = vec3.addVector(vec31.xCoord * p_70614_1_, vec31.yCoord * p_70614_1_, vec31.zCoord * p_70614_1_);
		return worldObj.func_147447_a(vec3, vec32, false, false, true);
	}

	public boolean isClientWorld() {
		return !worldObj.isRemote;
	}

	@Override
	public boolean canBeCollidedWith() {
		return !isDead;
	}

	@Override
	public boolean canBePushed() {
		return !isDead;
	}

	@Override
	public float getEyeHeight() {
		return height * 0.85F;
	}

	@Override
	protected void setBeenAttacked() {
		velocityChanged = rand.nextDouble() >= getEntityAttribute(SharedMonsterAttributes.knockbackResistance)
				.getAttributeValue();
	}

	@Override
	public float getRotationYawHead() {
		return rotationYawHead;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void setRotationYawHead(float p_70034_1_) {
		rotationYawHead = p_70034_1_;
	}

	public float getAbsorptionAmount() {
		return field_110151_bq;
	}

	public void setAbsorptionAmount(float p_110149_1_) {
		if (p_110149_1_ < 0.0F) {
			p_110149_1_ = 0.0F;
		}

		field_110151_bq = p_110149_1_;
	}

	public Team getTeam() {
		return null;
	}

	public boolean isOnSameTeam(EntityLivingBase p_142014_1_) {
		return isOnTeam(p_142014_1_.getTeam());
	}

	public boolean isOnTeam(Team p_142012_1_) {
		return getTeam() != null ? getTeam().isSameTeam(p_142012_1_) : false;
	}

	/***
	 * Removes all potion effects that have curativeItem as a curative item for its
	 * effect
	 *
	 * @param curativeItem
	 *            The itemstack we are using to cure potion effects
	 */
	public void curePotionEffects(ItemStack curativeItem) {
		Iterator<Integer> potionKey = activePotionsMap.keySet().iterator();

		if (worldObj.isRemote)
			return;

		while (potionKey.hasNext()) {
			Integer key = potionKey.next();
			PotionEffect effect = (PotionEffect) activePotionsMap.get(key);

			if (effect.isCurativeItem(curativeItem)) {
				potionKey.remove();
				onFinishedPotionEffect(effect);
			}
		}
	}

	/**
	 * Returns true if the entity's rider (EntityPlayer) should face forward when
	 * mounted. currently only used in vanilla code by pigs.
	 *
	 * @param player
	 *            The player who is riding the entity.
	 * @return If the player should orient the same direction as this entity.
	 */
	public boolean shouldRiderFaceForward(EntityPlayer player) {
		return this instanceof EntityPig;
	}

	public void func_152111_bt() {
	}

	public void func_152112_bu() {
	}
}