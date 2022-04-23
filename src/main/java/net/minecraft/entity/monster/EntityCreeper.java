package net.minecraft.entity.monster;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import org.bukkit.craftbukkit.event.CraftEventFactory;

public class EntityCreeper extends EntityMob {
	private int lastActiveTime;
	private int timeSinceIgnited;
	private int fuseTime = 30;
	private int explosionRadius = 3;
	private static final String __OBFID = "CL_00001684";

	public void setPowered(boolean powered) {
		if (!powered) {
			dataWatcher.updateObject(17, Byte.valueOf((byte) 0));
		} else {
			dataWatcher.updateObject(17, Byte.valueOf((byte) 1));
		}
	}

	public EntityCreeper(World p_i1733_1_) {
		super(p_i1733_1_);
		tasks.addTask(1, new EntityAISwimming(this));
		tasks.addTask(2, new EntityAICreeperSwell(this));
		tasks.addTask(3, new EntityAIAvoidEntity(this, EntityOcelot.class, 6.0F, 1.0D, 1.2D));
		tasks.addTask(4, new EntityAIAttackOnCollide(this, 1.0D, false));
		tasks.addTask(5, new EntityAIWander(this, 0.8D));
		tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		tasks.addTask(6, new EntityAILookIdle(this));
		targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true));
		targetTasks.addTask(2, new EntityAIHurtByTarget(this, false));
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.25D);
	}

	@Override
	public boolean isAIEnabled() {
		return true;
	}

	@Override
	public int getMaxSafePointTries() {
		return getAttackTarget() == null ? 3 : 3 + (int) (getHealth() - 1.0F);
	}

	@Override
	protected void fall(float p_70069_1_) {
		super.fall(p_70069_1_);
		timeSinceIgnited = (int) (timeSinceIgnited + p_70069_1_ * 1.5F);

		if (timeSinceIgnited > fuseTime - 5) {
			timeSinceIgnited = fuseTime - 5;
		}
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		dataWatcher.addObject(16, Byte.valueOf((byte) -1));
		dataWatcher.addObject(17, Byte.valueOf((byte) 0));
		dataWatcher.addObject(18, Byte.valueOf((byte) 0));
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound p_70014_1_) {
		super.writeEntityToNBT(p_70014_1_);

		if (dataWatcher.getWatchableObjectByte(17) == 1) {
			p_70014_1_.setBoolean("powered", true);
		}

		p_70014_1_.setShort("Fuse", (short) fuseTime);
		p_70014_1_.setByte("ExplosionRadius", (byte) explosionRadius);
		p_70014_1_.setBoolean("ignited", func_146078_ca());
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound p_70037_1_) {
		super.readEntityFromNBT(p_70037_1_);
		dataWatcher.updateObject(17, Byte.valueOf((byte) (p_70037_1_.getBoolean("powered") ? 1 : 0)));

		if (p_70037_1_.hasKey("Fuse", 99)) {
			fuseTime = p_70037_1_.getShort("Fuse");
		}

		if (p_70037_1_.hasKey("ExplosionRadius", 99)) {
			explosionRadius = p_70037_1_.getByte("ExplosionRadius");
		}

		if (p_70037_1_.getBoolean("ignited")) {
			func_146079_cb();
		}
	}

	@Override
	public void onUpdate() {
		if (isEntityAlive()) {
			lastActiveTime = timeSinceIgnited;

			if (func_146078_ca()) {
				setCreeperState(1);
			}

			int i = getCreeperState();

			if (i > 0 && timeSinceIgnited == 0) {
				playSound("creeper.primed", 1.0F, 0.5F);
			}

			timeSinceIgnited += i;

			if (timeSinceIgnited < 0) {
				timeSinceIgnited = 0;
			}

			if (timeSinceIgnited >= fuseTime) {
				timeSinceIgnited = fuseTime;
				func_146077_cc();
			}
		}

		super.onUpdate();
	}

	@Override
	protected String getHurtSound() {
		return "mob.creeper.say";
	}

	@Override
	protected String getDeathSound() {
		return "mob.creeper.death";
	}

	@Override
	public void onDeath(DamageSource p_70645_1_) {
		super.onDeath(p_70645_1_);

		if (p_70645_1_.getEntity() instanceof EntitySkeleton) {
			int i = Item.getIdFromItem(Items.record_13);
			int j = Item.getIdFromItem(Items.record_wait);
			int k = i + rand.nextInt(j - i + 1);
			dropItem(Item.getItemById(k), 1);
		}
	}

	@Override
	public boolean attackEntityAsMob(Entity p_70652_1_) {
		return true;
	}

	public boolean getPowered() {
		return dataWatcher.getWatchableObjectByte(17) == 1;
	}

	@SideOnly(Side.CLIENT)
	public float getCreeperFlashIntensity(float p_70831_1_) {
		return (lastActiveTime + (timeSinceIgnited - lastActiveTime) * p_70831_1_) / (fuseTime - 2);
	}

	@Override
	protected Item getDropItem() {
		return Items.gunpowder;
	}

	public int getCreeperState() {
		return dataWatcher.getWatchableObjectByte(16);
	}

	public void setCreeperState(int p_70829_1_) {
		dataWatcher.updateObject(16, Byte.valueOf((byte) p_70829_1_));
	}

	@Override
	public void onStruckByLightning(EntityLightningBolt ligtning) {
		boolean lastPowered = getPowered();
		super.onStruckByLightning(ligtning);

		if (ligtning != null) {
			setPowered(lastPowered);

			// CraftBukkit start
			if (CraftEventFactory.callCreeperPowerEvent(this, ligtning,
					org.bukkit.event.entity.CreeperPowerEvent.PowerCause.LIGHTNING).isCancelled())
				return;
		}

		setPowered(true);
	}

	@Override
	protected boolean interact(EntityPlayer p_70085_1_) {
		ItemStack itemstack = p_70085_1_.inventory.getCurrentItem();

		if (itemstack != null && itemstack.getItem() == Items.flint_and_steel) {
			worldObj.playSoundEffect(posX + 0.5D, posY + 0.5D, posZ + 0.5D, "fire.ignite", 1.0F,
					rand.nextFloat() * 0.4F + 0.8F);
			p_70085_1_.swingItem();

			if (!worldObj.isRemote) {
				func_146079_cb();
				itemstack.damageItem(1, p_70085_1_);
				return true;
			}
		}

		return super.interact(p_70085_1_);
	}

	private void func_146077_cc() {
		if (!worldObj.isRemote) {
			boolean flag = worldObj.getGameRules().getGameRuleBooleanValue("mobGriefing");

			if (getPowered()) {
				worldObj.createExplosion(this, posX, posY, posZ, explosionRadius * 2, flag);
			} else {
				worldObj.createExplosion(this, posX, posY, posZ, explosionRadius, flag);
			}

			setDead();
		}
	}

	public boolean func_146078_ca() {
		return dataWatcher.getWatchableObjectByte(18) != 0;
	}

	public void func_146079_cb() {
		dataWatcher.updateObject(18, Byte.valueOf((byte) 1));
	}
}