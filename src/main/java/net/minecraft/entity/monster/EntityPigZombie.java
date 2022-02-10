package net.minecraft.entity.monster;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.event.entity.EntityTargetEvent;

import net.minecraft.entity.Entity;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public class EntityPigZombie extends EntityZombie {
	private static final UUID field_110189_bq = UUID.fromString("49455A49-7EC5-45BA-B886-3B90B23A1718");
	private static final AttributeModifier field_110190_br = new AttributeModifier(field_110189_bq,
			"Attacking speed boost", 0.45D, 0).setSaved(false);
	private int angerLevel;
	private int randomSoundDelay;
	private Entity field_110191_bu;
	private static final String __OBFID = "CL_00001693";

	public EntityPigZombie(World p_i1739_1_) {
		super(p_i1739_1_);
		isImmuneToFire = true;
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		getEntityAttribute(field_110186_bp).setBaseValue(0.0D);
		getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.5D);
		getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(5.0D);
	}

	@Override
	protected boolean isAIEnabled() {
		return false;
	}

	@Override
	public void onUpdate() {
		if (field_110191_bu != entityToAttack && !worldObj.isRemote) {
			IAttributeInstance iattributeinstance = getEntityAttribute(SharedMonsterAttributes.movementSpeed);
			iattributeinstance.removeModifier(field_110190_br);

			if (entityToAttack != null) {
				iattributeinstance.applyModifier(field_110190_br);
			}
		}

		field_110191_bu = entityToAttack;

		if (randomSoundDelay > 0 && --randomSoundDelay == 0) {
			playSound("mob.zombiepig.zpigangry", getSoundVolume() * 2.0F,
					((rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F) * 1.8F);
		}

		super.onUpdate();
	}

	@Override
	public boolean getCanSpawnHere() {
		return worldObj.difficultySetting != EnumDifficulty.PEACEFUL && worldObj.checkNoEntityCollision(boundingBox)
				&& worldObj.getCollidingBoundingBoxes(this, boundingBox).isEmpty()
				&& !worldObj.isAnyLiquid(boundingBox);
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound p_70014_1_) {
		super.writeEntityToNBT(p_70014_1_);
		p_70014_1_.setShort("Anger", (short) angerLevel);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound p_70037_1_) {
		super.readEntityFromNBT(p_70037_1_);
		angerLevel = p_70037_1_.getShort("Anger");
	}

	@Override
	protected Entity findPlayerToAttack() {
		return angerLevel == 0 ? null : super.findPlayerToAttack();
	}

	@Override
	public boolean attackEntityFrom(DamageSource p_70097_1_, float p_70097_2_) {
		if (isEntityInvulnerable())
			return false;
		else {
			Entity entity = p_70097_1_.getEntity();

			if (entity instanceof EntityPlayer) {
				List list = worldObj.getEntitiesWithinAABBExcludingEntity(this,
						boundingBox.expand(32.0D, 32.0D, 32.0D));

				for (int i = 0; i < list.size(); ++i) {
					Entity entity1 = (Entity) list.get(i);

					if (entity1 instanceof EntityPigZombie) {
						EntityPigZombie entitypigzombie = (EntityPigZombie) entity1;
						entitypigzombie.becomeAngryAt(entity);
					}
				}

				becomeAngryAt(entity);
			}

			return super.attackEntityFrom(p_70097_1_, p_70097_2_);
		}
	}

	public void becomeAngryAt(Entity entity) {
		org.bukkit.entity.Entity bukkitTarget = entity == null ? null : entity.getBukkitEntity();
		EntityTargetEvent event = new EntityTargetEvent(this.getBukkitEntity(), bukkitTarget,
				EntityTargetEvent.TargetReason.PIG_ZOMBIE_TARGET);
		Bukkit.getPluginManager().callEvent(event);
		if (event.isCancelled())
			return;
		if (event.getTarget() == null) {
			entityToAttack = null;
			return;
		}
		entity = ((CraftEntity) event.getTarget()).getHandle();
		entityToAttack = entity;
		angerLevel = 400 + rand.nextInt(400);
		randomSoundDelay = rand.nextInt(40);
	}

	@Override
	protected String getLivingSound() {
		return "mob.zombiepig.zpig";
	}

	@Override
	protected String getHurtSound() {
		return "mob.zombiepig.zpighurt";
	}

	@Override
	protected String getDeathSound() {
		return "mob.zombiepig.zpigdeath";
	}

	@Override
	protected void dropFewItems(boolean p_70628_1_, int p_70628_2_) {
		int j = rand.nextInt(2 + p_70628_2_);
		int k;

		for (k = 0; k < j; ++k) {
			dropItem(Items.rotten_flesh, 1);
		}

		j = rand.nextInt(2 + p_70628_2_);

		for (k = 0; k < j; ++k) {
			dropItem(Items.gold_nugget, 1);
		}
	}

	@Override
	public boolean interact(EntityPlayer p_70085_1_) {
		return false;
	}

	@Override
	protected void dropRareDrop(int p_70600_1_) {
		dropItem(Items.gold_ingot, 1);
	}

	@Override
	protected void addRandomArmor() {
		setCurrentItemOrArmor(0, new ItemStack(Items.golden_sword));
	}

	@Override
	public IEntityLivingData onSpawnWithEgg(IEntityLivingData p_110161_1_) {
		super.onSpawnWithEgg(p_110161_1_);
		setVillager(false);
		return p_110161_1_;
	}

	public int getAngerLevel() {
		return angerLevel;
	}

	public void setAngerLevel(int angerLevel) {
		this.angerLevel = angerLevel;
	}
}