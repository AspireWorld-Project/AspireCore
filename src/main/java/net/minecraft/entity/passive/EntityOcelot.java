package net.minecraft.entity.passive;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import org.bukkit.craftbukkit.event.CraftEventFactory;

public class EntityOcelot extends EntityTameable {
	private final EntityAITempt aiTempt;
	private static final String __OBFID = "CL_00001646";

	public EntityOcelot(World p_i1688_1_) {
		super(p_i1688_1_);
		setSize(0.6F, 0.8F);
		getNavigator().setAvoidsWater(true);
		tasks.addTask(1, new EntityAISwimming(this));
		tasks.addTask(2, aiSit);
		tasks.addTask(3, aiTempt = new EntityAITempt(this, 0.6D, Items.fish, true));
		tasks.addTask(4, new EntityAIAvoidEntity(this, EntityPlayer.class, 16.0F, 0.8D, 1.33D));
		tasks.addTask(5, new EntityAIFollowOwner(this, 1.0D, 10.0F, 5.0F));
		tasks.addTask(6, new EntityAIOcelotSit(this, 1.33D));
		tasks.addTask(7, new EntityAILeapAtTarget(this, 0.3F));
		tasks.addTask(8, new EntityAIOcelotAttack(this));
		tasks.addTask(9, new EntityAIMate(this, 0.8D));
		tasks.addTask(10, new EntityAIWander(this, 0.8D));
		tasks.addTask(11, new EntityAIWatchClosest(this, EntityPlayer.class, 10.0F));
		targetTasks.addTask(1, new EntityAITargetNonTamed(this, EntityChicken.class, 750, false));
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		dataWatcher.addObject(18, Byte.valueOf((byte) 0));
	}

	@Override
	public void updateAITick() {
		if (getMoveHelper().isUpdating()) {
			double d0 = getMoveHelper().getSpeed();

			if (d0 == 0.6D) {
				setSneaking(true);
				setSprinting(false);
			} else if (d0 == 1.33D) {
				setSneaking(false);
				setSprinting(true);
			} else {
				setSneaking(false);
				setSprinting(false);
			}
		} else {
			setSneaking(false);
			setSprinting(false);
		}
	}

	@Override
	protected boolean canDespawn() {
		return !isTamed() && ticksExisted > 2400;
	}

	@Override
	public boolean isAIEnabled() {
		return true;
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(10.0D);
		getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.30000001192092896D);
	}

	@Override
	protected void fall(float p_70069_1_) {
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound p_70014_1_) {
		super.writeEntityToNBT(p_70014_1_);
		p_70014_1_.setInteger("CatType", getTameSkin());
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound p_70037_1_) {
		super.readEntityFromNBT(p_70037_1_);
		setTameSkin(p_70037_1_.getInteger("CatType"));
	}

	@Override
	protected String getLivingSound() {
		return isTamed() ? isInLove() ? "mob.cat.purr" : rand.nextInt(4) == 0 ? "mob.cat.purreow" : "mob.cat.meow" : "";
	}

	@Override
	protected String getHurtSound() {
		return "mob.cat.hitt";
	}

	@Override
	protected String getDeathSound() {
		return "mob.cat.hitt";
	}

	@Override
	protected float getSoundVolume() {
		return 0.4F;
	}

	@Override
	protected Item getDropItem() {
		return Items.leather;
	}

	@Override
	public boolean attackEntityAsMob(Entity p_70652_1_) {
		return p_70652_1_.attackEntityFrom(DamageSource.causeMobDamage(this), 3.0F);
	}

	@Override
	public boolean attackEntityFrom(DamageSource p_70097_1_, float p_70097_2_) {
		if (isEntityInvulnerable())
			return false;
		else {
			aiSit.setSitting(false);
			return super.attackEntityFrom(p_70097_1_, p_70097_2_);
		}
	}

	@Override
	protected void dropFewItems(boolean p_70628_1_, int p_70628_2_) {
	}

	@Override
	public boolean interact(EntityPlayer player) {
		ItemStack itemstack = player.inventory.getCurrentItem();
		if (isTamed()) {
			if (func_152114_e(player) && !worldObj.isRemote && !isBreedingItem(itemstack)) {
				aiSit.setSitting(!isSitting());
			}
		} else if (aiTempt.isRunning() && itemstack != null && itemstack.getItem() == Items.fish
				&& player.getDistanceSqToEntity(this) < 9.0D) {
			if (!player.capabilities.isCreativeMode) {
				--itemstack.stackSize;
			}
			if (itemstack.stackSize <= 0) {
				player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
			}
			if (!worldObj.isRemote) {
				if (rand.nextInt(3) == 0 && !CraftEventFactory.callEntityTameEvent(this, player).isCancelled()) {
					setTamed(true);
					setTameSkin(1 + worldObj.rand.nextInt(3));
					func_152115_b(player.getUniqueID().toString());
					playTameEffect(true);
					aiSit.setSitting(true);
					worldObj.setEntityState(this, (byte) 7);
				} else {
					playTameEffect(false);
					worldObj.setEntityState(this, (byte) 6);
				}
			}
			return true;
		}
		return super.interact(player);
	}

	@Override
	public EntityOcelot createChild(EntityAgeable p_90011_1_) {
		EntityOcelot entityocelot = new EntityOcelot(worldObj);

		if (isTamed()) {
			entityocelot.func_152115_b(func_152113_b());
			entityocelot.setTamed(true);
			entityocelot.setTameSkin(getTameSkin());
		}

		return entityocelot;
	}

	@Override
	public boolean isBreedingItem(ItemStack p_70877_1_) {
		return p_70877_1_ != null && p_70877_1_.getItem() == Items.fish;
	}

	@Override
	public boolean canMateWith(EntityAnimal p_70878_1_) {
		if (p_70878_1_ == this)
			return false;
		else if (!isTamed())
			return false;
		else if (!(p_70878_1_ instanceof EntityOcelot))
			return false;
		else {
			EntityOcelot entityocelot = (EntityOcelot) p_70878_1_;
			return entityocelot.isTamed() && isInLove() && entityocelot.isInLove();
		}
	}

	public int getTameSkin() {
		return dataWatcher.getWatchableObjectByte(18);
	}

	public void setTameSkin(int p_70912_1_) {
		dataWatcher.updateObject(18, Byte.valueOf((byte) p_70912_1_));
	}

	@Override
	public boolean getCanSpawnHere() {
		if (worldObj.rand.nextInt(3) == 0)
			return false;
		else {
			if (worldObj.checkNoEntityCollision(boundingBox)
					&& worldObj.getCollidingBoundingBoxes(this, boundingBox).isEmpty()
					&& !worldObj.isAnyLiquid(boundingBox)) {
				int i = MathHelper.floor_double(posX);
				int j = MathHelper.floor_double(boundingBox.minY);
				int k = MathHelper.floor_double(posZ);

				if (j < 63)
					return false;

				Block block = worldObj.getBlock(i, j - 1, k);

				return block == Blocks.grass || block.isLeaves(worldObj, i, j - 1, k);
			}

			return false;
		}
	}

	@Override
	public String getCommandSenderName() {
		return hasCustomNameTag() ? getCustomNameTag()
				: isTamed() ? StatCollector.translateToLocal("entity.Cat.name") : super.getCommandSenderName();
	}

	@Override
	public IEntityLivingData onSpawnWithEgg(IEntityLivingData p_110161_1_) {
		p_110161_1_ = super.onSpawnWithEgg(p_110161_1_);

		if (worldObj.rand.nextInt(7) == 0) {
			for (int i = 0; i < 2; ++i) {
				EntityOcelot entityocelot = new EntityOcelot(worldObj);
				entityocelot.setLocationAndAngles(posX, posY, posZ, rotationYaw, 0.0F);
				entityocelot.setGrowingAge(-24000);
				worldObj.spawnEntityInWorld(entityocelot);
			}
		}

		return p_110161_1_;
	}
}