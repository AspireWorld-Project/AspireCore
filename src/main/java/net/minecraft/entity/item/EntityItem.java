package net.minecraft.entity.item;

import java.util.Iterator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.Event.Result;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.AchievementList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.item.ItemExpireEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;

public class EntityItem extends Entity {
	private static final Logger logger = LogManager.getLogger();
	public int age;
	public int delayBeforeCanPickup;
	private int health;
	private String field_145801_f;
	private String field_145802_g;
	public float hoverStart;
	private static final String __OBFID = "CL_00001669";

	/**
	 * The maximum age of this EntityItem. The item is expired once this is reached.
	 */
	public int lifespan = 6000;

	public EntityItem(World p_i1709_1_, double p_i1709_2_, double p_i1709_4_, double p_i1709_6_) {
		super(p_i1709_1_);
		health = 5;
		hoverStart = (float) (Math.random() * Math.PI * 2.0D);
		setSize(0.25F, 0.25F);
		yOffset = height / 2.0F;
		setPosition(p_i1709_2_, p_i1709_4_, p_i1709_6_);
		rotationYaw = (float) (Math.random() * 360.0D);
		motionX = (float) (Math.random() * 0.20000000298023224D - 0.10000000149011612D);
		motionY = 0.20000000298023224D;
		motionZ = (float) (Math.random() * 0.20000000298023224D - 0.10000000149011612D);
	}

	public EntityItem(World p_i1710_1_, double p_i1710_2_, double p_i1710_4_, double p_i1710_6_, ItemStack p_i1710_8_) {
		this(p_i1710_1_, p_i1710_2_, p_i1710_4_, p_i1710_6_);
		setEntityItemStack(p_i1710_8_);
		lifespan = p_i1710_8_.getItem() == null ? 6000 : p_i1710_8_.getItem().getEntityLifespan(p_i1710_8_, p_i1710_1_);
	}

	@Override
	protected boolean canTriggerWalking() {
		return false;
	}

	public EntityItem(World p_i1711_1_) {
		super(p_i1711_1_);
		health = 5;
		hoverStart = (float) (Math.random() * Math.PI * 2.0D);
		setSize(0.25F, 0.25F);
		yOffset = height / 2.0F;
	}

	@Override
	protected void entityInit() {
		getDataWatcher().addObjectByDataType(10, 5);
	}

	@Override
	public void onUpdate() {
		ItemStack stack = getDataWatcher().getWatchableObjectItemStack(10);
		if (stack != null && stack.getItem() != null) {
			if (stack.getItem().onEntityItemUpdate(this))
				return;
		}

		if (getEntityItem() == null) {
			setDead();
		} else {
			super.onUpdate();

			if (delayBeforeCanPickup > 0) {
				--delayBeforeCanPickup;
			}

			prevPosX = posX;
			prevPosY = posY;
			prevPosZ = posZ;
			motionY -= 0.03999999910593033D;
			noClip = func_145771_j(posX, (boundingBox.minY + boundingBox.maxY) / 2.0D, posZ);
			moveEntity(motionX, motionY, motionZ);
			boolean flag = (int) prevPosX != (int) posX || (int) prevPosY != (int) posY || (int) prevPosZ != (int) posZ;

			if (flag || ticksExisted % 25 == 0) {
				if (worldObj.getBlock(MathHelper.floor_double(posX), MathHelper.floor_double(posY),
						MathHelper.floor_double(posZ)).getMaterial() == Material.lava) {
					motionY = 0.20000000298023224D;
					motionX = (rand.nextFloat() - rand.nextFloat()) * 0.2F;
					motionZ = (rand.nextFloat() - rand.nextFloat()) * 0.2F;
					playSound("random.fizz", 0.4F, 2.0F + rand.nextFloat() * 0.4F);
				}

				if (!worldObj.isRemote) {
					searchForOtherItemsNearby();
				}
			}

			float f = 0.98F;

			if (onGround) {
				f = worldObj.getBlock(MathHelper.floor_double(posX), MathHelper.floor_double(boundingBox.minY) - 1,
						MathHelper.floor_double(posZ)).slipperiness * 0.98F;
			}

			motionX *= f;
			motionY *= 0.9800000190734863D;
			motionZ *= f;

			if (onGround) {
				motionY *= -0.5D;
			}

			++age;

			ItemStack item = getDataWatcher().getWatchableObjectItemStack(10);

			if (!worldObj.isRemote && age >= lifespan) {
				if (item != null) {
					ItemExpireEvent event = new ItemExpireEvent(this,
							item.getItem() == null ? 6000 : item.getItem().getEntityLifespan(item, worldObj));
					if (MinecraftForge.EVENT_BUS.post(event)) {
						lifespan += event.extraLife;
					} else {
						setDead();
					}
				} else {
					setDead();
				}
			}

			if (item != null && item.stackSize <= 0) {
				setDead();
			}
		}
	}

	private void searchForOtherItemsNearby() {
		Iterator iterator = worldObj.getEntitiesWithinAABB(EntityItem.class, boundingBox.expand(0.5D, 0.0D, 0.5D))
				.iterator();

		while (iterator.hasNext()) {
			EntityItem entityitem = (EntityItem) iterator.next();
			combineItems(entityitem);
		}
	}

	public boolean combineItems(EntityItem p_70289_1_) {
		if (p_70289_1_ == this)
			return false;
		else if (p_70289_1_.isEntityAlive() && isEntityAlive()) {
			ItemStack itemstack = getEntityItem();
			ItemStack itemstack1 = p_70289_1_.getEntityItem();

			if (itemstack1.getItem() != itemstack.getItem())
				return false;
			else if (itemstack1.hasTagCompound() ^ itemstack.hasTagCompound())
				return false;
			else if (itemstack1.hasTagCompound() && !itemstack1.getTagCompound().equals(itemstack.getTagCompound()))
				return false;
			else if (itemstack1.getItem() == null)
				return false;
			else if (itemstack1.getItem().getHasSubtypes() && itemstack1.getItemDamage() != itemstack.getItemDamage())
				return false;
			else if (itemstack1.stackSize < itemstack.stackSize)
				return p_70289_1_.combineItems(this);
			else if (itemstack1.stackSize + itemstack.stackSize > itemstack1.getMaxStackSize())
				return false;
			else {
				itemstack1.stackSize += itemstack.stackSize;
				p_70289_1_.delayBeforeCanPickup = Math.max(p_70289_1_.delayBeforeCanPickup, delayBeforeCanPickup);
				p_70289_1_.age = Math.min(p_70289_1_.age, age);
				p_70289_1_.setEntityItemStack(itemstack1);
				setDead();
				return true;
			}
		} else
			return false;
	}

	public void setAgeToCreativeDespawnTime() {
		age = 4800;
	}

	@Override
	public boolean handleWaterMovement() {
		return worldObj.handleMaterialAcceleration(boundingBox, Material.water, this);
	}

	@Override
	protected void dealFireDamage(int p_70081_1_) {
		attackEntityFrom(DamageSource.inFire, p_70081_1_);
	}

	@Override
	public boolean attackEntityFrom(DamageSource p_70097_1_, float p_70097_2_) {
		if (isEntityInvulnerable())
			return false;
		else if (getEntityItem() != null && getEntityItem().getItem() == Items.nether_star && p_70097_1_.isExplosion())
			return false;
		else {
			setBeenAttacked();
			health = (int) (health - p_70097_2_);

			if (health <= 0) {
				setDead();
			}

			return false;
		}
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound p_70014_1_) {
		p_70014_1_.setShort("Health", (byte) health);
		p_70014_1_.setShort("Age", (short) age);
		p_70014_1_.setInteger("Lifespan", lifespan);

		if (func_145800_j() != null) {
			p_70014_1_.setString("Thrower", field_145801_f);
		}

		if (func_145798_i() != null) {
			p_70014_1_.setString("Owner", field_145802_g);
		}

		if (getEntityItem() != null) {
			p_70014_1_.setTag("Item", getEntityItem().writeToNBT(new NBTTagCompound()));
		}
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound p_70037_1_) {
		health = p_70037_1_.getShort("Health") & 255;
		age = p_70037_1_.getShort("Age");

		if (p_70037_1_.hasKey("Owner")) {
			field_145802_g = p_70037_1_.getString("Owner");
		}

		if (p_70037_1_.hasKey("Thrower")) {
			field_145801_f = p_70037_1_.getString("Thrower");
		}

		NBTTagCompound nbttagcompound1 = p_70037_1_.getCompoundTag("Item");
		setEntityItemStack(ItemStack.loadItemStackFromNBT(nbttagcompound1));

		ItemStack item = getDataWatcher().getWatchableObjectItemStack(10);

		if (item == null || item.stackSize <= 0) {
			setDead();
		}

		if (p_70037_1_.hasKey("Lifespan")) {
			lifespan = p_70037_1_.getInteger("Lifespan");
		}
	}

	@Override
	public void onCollideWithPlayer(EntityPlayer p_70100_1_) {
		if (!worldObj.isRemote) {
			if (delayBeforeCanPickup > 0)
				return;

			EntityItemPickupEvent event = new EntityItemPickupEvent(p_70100_1_, this);

			if (MinecraftForge.EVENT_BUS.post(event))
				return;

			ItemStack itemstack = getEntityItem();
			int i = itemstack.stackSize;

			if (delayBeforeCanPickup <= 0
					&& (field_145802_g == null || lifespan - age <= 200
							|| field_145802_g.equals(p_70100_1_.getCommandSenderName()))
					&& (event.getResult() == Result.ALLOW || i <= 0
							|| p_70100_1_.inventory.addItemStackToInventory(itemstack))) {
				if (itemstack.getItem() == Item.getItemFromBlock(Blocks.log)) {
					p_70100_1_.triggerAchievement(AchievementList.mineWood);
				}

				if (itemstack.getItem() == Item.getItemFromBlock(Blocks.log2)) {
					p_70100_1_.triggerAchievement(AchievementList.mineWood);
				}

				if (itemstack.getItem() == Items.leather) {
					p_70100_1_.triggerAchievement(AchievementList.killCow);
				}

				if (itemstack.getItem() == Items.diamond) {
					p_70100_1_.triggerAchievement(AchievementList.diamonds);
				}

				if (itemstack.getItem() == Items.blaze_rod) {
					p_70100_1_.triggerAchievement(AchievementList.blazeRod);
				}

				if (itemstack.getItem() == Items.diamond && func_145800_j() != null) {
					EntityPlayer entityplayer1 = worldObj.getPlayerEntityByName(func_145800_j());

					if (entityplayer1 != null && entityplayer1 != p_70100_1_) {
						entityplayer1.triggerAchievement(AchievementList.field_150966_x);
					}
				}

				FMLCommonHandler.instance().firePlayerItemPickupEvent(p_70100_1_, this);

				worldObj.playSoundAtEntity(p_70100_1_, "random.pop", 0.2F,
						((rand.nextFloat() - rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
				p_70100_1_.onItemPickup(this, i);

				if (itemstack.stackSize <= 0) {
					setDead();
				}
			}
		}
	}

	@Override
	public String getCommandSenderName() {
		return StatCollector.translateToLocal("item." + getEntityItem().getUnlocalizedName());
	}

	@Override
	public boolean canAttackWithItem() {
		return false;
	}

	@Override
	public void travelToDimension(int p_71027_1_) {
		super.travelToDimension(p_71027_1_);

		if (!worldObj.isRemote) {
			searchForOtherItemsNearby();
		}
	}

	public ItemStack getEntityItem() {
		ItemStack itemstack = getDataWatcher().getWatchableObjectItemStack(10);
		return itemstack == null ? new ItemStack(Blocks.stone) : itemstack;
	}

	public void setEntityItemStack(ItemStack p_92058_1_) {
		getDataWatcher().updateObject(10, p_92058_1_);
		getDataWatcher().setObjectWatched(10);
	}

	public String func_145798_i() {
		return field_145802_g;
	}

	public void func_145797_a(String p_145797_1_) {
		field_145802_g = p_145797_1_;
	}

	public String func_145800_j() {
		return field_145801_f;
	}

	public void func_145799_b(String p_145799_1_) {
		field_145801_f = p_145799_1_;
	}

	/*
	 * ===================================== ULTRAMINE START
	 * =====================================
	 */

	@Override
	public org.ultramine.server.EntityType computeEntityType() {
		return org.ultramine.server.EntityType.ITEM;
	}

	@Override
	public void updateInactive() {
		if (++age >= lifespan) {
			ItemStack item = getDataWatcher().getWatchableObjectItemStack(10);
			if (item != null) {
				ItemExpireEvent event = new ItemExpireEvent(this,
						item.getItem() == null ? 6000 : item.getItem().getEntityLifespan(item, worldObj));
				if (MinecraftForge.EVENT_BUS.post(event)) {
					lifespan += event.extraLife;
				} else {
					setDead();
				}
			} else {
				setDead();
			}
		}
	}
}