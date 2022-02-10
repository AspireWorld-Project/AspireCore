package net.minecraft.entity.passive;

import org.bukkit.craftbukkit.event.CraftEventFactory;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIControlledByPlayer;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.AchievementList;
import net.minecraft.world.World;

public class EntityPig extends EntityAnimal {
	private final EntityAIControlledByPlayer aiControlledByPlayer;
	private static final String __OBFID = "CL_00001647";

	public EntityPig(World p_i1689_1_) {
		super(p_i1689_1_);
		setSize(0.9F, 0.9F);
		getNavigator().setAvoidsWater(true);
		tasks.addTask(0, new EntityAISwimming(this));
		tasks.addTask(1, new EntityAIPanic(this, 1.25D));
		tasks.addTask(2, aiControlledByPlayer = new EntityAIControlledByPlayer(this, 0.3F));
		tasks.addTask(3, new EntityAIMate(this, 1.0D));
		tasks.addTask(4, new EntityAITempt(this, 1.2D, Items.carrot_on_a_stick, false));
		tasks.addTask(4, new EntityAITempt(this, 1.2D, Items.carrot, false));
		tasks.addTask(5, new EntityAIFollowParent(this, 1.1D));
		tasks.addTask(6, new EntityAIWander(this, 1.0D));
		tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
		tasks.addTask(8, new EntityAILookIdle(this));
	}

	@Override
	public boolean isAIEnabled() {
		return true;
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(10.0D);
		getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.25D);
	}

	@Override
	protected void updateAITasks() {
		super.updateAITasks();
	}

	@Override
	public boolean canBeSteered() {
		ItemStack itemstack = ((EntityPlayer) riddenByEntity).getHeldItem();
		return itemstack != null && itemstack.getItem() == Items.carrot_on_a_stick;
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		dataWatcher.addObject(16, Byte.valueOf((byte) 0));
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound p_70014_1_) {
		super.writeEntityToNBT(p_70014_1_);
		p_70014_1_.setBoolean("Saddle", getSaddled());
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound p_70037_1_) {
		super.readEntityFromNBT(p_70037_1_);
		setSaddled(p_70037_1_.getBoolean("Saddle"));
	}

	@Override
	protected String getLivingSound() {
		return "mob.pig.say";
	}

	@Override
	protected String getHurtSound() {
		return "mob.pig.say";
	}

	@Override
	protected String getDeathSound() {
		return "mob.pig.death";
	}

	@Override
	protected void func_145780_a(int p_145780_1_, int p_145780_2_, int p_145780_3_, Block p_145780_4_) {
		playSound("mob.pig.step", 0.15F, 1.0F);
	}

	@Override
	public boolean interact(EntityPlayer p_70085_1_) {
		if (super.interact(p_70085_1_))
			return true;
		else if (getSaddled() && !worldObj.isRemote && (riddenByEntity == null || riddenByEntity == p_70085_1_)) {
			p_70085_1_.mountEntity(this);
			return true;
		} else
			return false;
	}

	@Override
	protected Item getDropItem() {
		return isBurning() ? Items.cooked_porkchop : Items.porkchop;
	}

	@Override
	protected void dropFewItems(boolean p_70628_1_, int p_70628_2_) {
		int j = rand.nextInt(3) + 1 + rand.nextInt(1 + p_70628_2_);

		for (int k = 0; k < j; ++k) {
			if (isBurning()) {
				dropItem(Items.cooked_porkchop, 1);
			} else {
				dropItem(Items.porkchop, 1);
			}
		}

		if (getSaddled()) {
			dropItem(Items.saddle, 1);
		}
	}

	public boolean getSaddled() {
		return (dataWatcher.getWatchableObjectByte(16) & 1) != 0;
	}

	public void setSaddled(boolean p_70900_1_) {
		if (p_70900_1_) {
			dataWatcher.updateObject(16, Byte.valueOf((byte) 1));
		} else {
			dataWatcher.updateObject(16, Byte.valueOf((byte) 0));
		}
	}

	@Override
	public void onStruckByLightning(EntityLightningBolt p_70077_1_) {
		if (!worldObj.isRemote) {
			EntityPigZombie entitypigzombie = new EntityPigZombie(worldObj);
			entitypigzombie.setCurrentItemOrArmor(0, new ItemStack(Items.golden_sword));
			entitypigzombie.setLocationAndAngles(posX, posY, posZ, rotationYaw, rotationPitch);
			if (CraftEventFactory.callPigZapEvent(this, p_70077_1_, entitypigzombie).isCancelled())
				return;
			worldObj.spawnEntityInWorld(entitypigzombie);
			setDead();
		}
	}

	@Override
	protected void fall(float p_70069_1_) {
		super.fall(p_70069_1_);

		if (p_70069_1_ > 5.0F && riddenByEntity instanceof EntityPlayer) {
			((EntityPlayer) riddenByEntity).triggerAchievement(AchievementList.flyPig);
		}
	}

	@Override
	public EntityPig createChild(EntityAgeable p_90011_1_) {
		return new EntityPig(worldObj);
	}

	@Override
	public boolean isBreedingItem(ItemStack p_70877_1_) {
		return p_70877_1_ != null && p_70877_1_.getItem() == Items.carrot;
	}

	public EntityAIControlledByPlayer getAIControlledByPlayer() {
		return aiControlledByPlayer;
	}
}