package net.minecraft.entity.item;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.vehicle.VehicleCreateEvent;
import org.bukkit.event.vehicle.VehicleDamageEvent;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityBoat extends Entity {
	private boolean isBoatEmpty;
	private double speedMultiplier;
	private int boatPosRotationIncrements;
	private double boatX;
	private double boatY;
	private double boatZ;
	private double boatYaw;
	private double boatPitch;
	@SideOnly(Side.CLIENT)
	private double velocityX;
	@SideOnly(Side.CLIENT)
	private double velocityY;
	@SideOnly(Side.CLIENT)
	private double velocityZ;
	private static final String __OBFID = "CL_00001667";

	public EntityBoat(World p_i1704_1_) {
		super(p_i1704_1_);
		isBoatEmpty = true;
		speedMultiplier = 0.07D;
		preventEntitySpawning = true;
		setSize(1.5F, 0.6F);
		yOffset = height / 2.0F;

		CraftEntity bukkitEntity = getBukkitEntity();
		if (bukkitEntity != null && bukkitEntity instanceof Vehicle) {
			worldObj.getServer().getPluginManager().callEvent(new VehicleCreateEvent((Vehicle) bukkitEntity));
		}
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		if (getTimeSinceHit() > 0) {
			setTimeSinceHit(getTimeSinceHit() - 1);
		}
		if (getDamageTaken() > 0.0F) {
			setDamageTaken(getDamageTaken() - 1.0F);
		}
		double prevX = posX;
		double prevY = posY;
		double prevZ = posZ;
		float prevYaw = rotationYaw;
		float prevPitch = rotationPitch;
		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;
		byte b0 = 5;
		double d0 = 0.0D;
		for (int i = 0; i < b0; ++i) {
			double d1 = boundingBox.minY + (boundingBox.maxY - boundingBox.minY) * i / b0 - 0.125D;
			double d3 = boundingBox.minY + (boundingBox.maxY - boundingBox.minY) * (i + 1) / b0 - 0.125D;
			AxisAlignedBB axisalignedbb = AxisAlignedBB.getBoundingBox(boundingBox.minX, d1, boundingBox.minZ,
					boundingBox.maxX, d3, boundingBox.maxZ);
			if (worldObj.isAABBInMaterial(axisalignedbb, Material.water)) {
				d0 += 1.0D / b0;
			}
		}
		double d10 = Math.sqrt(motionX * motionX + motionZ * motionZ);
		double d2;
		double d4;
		int j;
		double d11;
		double d12;
		if (d10 > 0.26249999999999996D) {
			d2 = Math.cos(rotationYaw * 3.141592653589793D / 180.0D);
			d4 = Math.sin(rotationYaw * 3.141592653589793D / 180.0D);

			for (j = 0; j < 1.0D + d10 * 60.0D; ++j) {
				d11 = rand.nextFloat() * 2.0F - 1.0F;
				d12 = (rand.nextInt(2) * 2 - 1) * 0.7D;
				double d8;
				double d9;
				if (rand.nextBoolean()) {
					d8 = posX - d2 * d11 * 0.8D + d4 * d12;
					d9 = posZ - d4 * d11 * 0.8D - d2 * d12;
					worldObj.spawnParticle("splash", d8, posY - 0.125D, d9, motionX, motionY, motionZ);
				} else {
					d8 = posX + d2 + d4 * d11 * 0.7D;
					d9 = posZ + d4 - d2 * d11 * 0.7D;
					worldObj.spawnParticle("splash", d8, posY - 0.125D, d9, motionX, motionY, motionZ);
				}
			}
		}

		if (worldObj.isRemote && isBoatEmpty) {
			if (boatPosRotationIncrements > 0) {
				d2 = posX + (boatX - posX) / boatPosRotationIncrements;
				d4 = posY + (boatY - posY) / boatPosRotationIncrements;
				d11 = posZ + (boatZ - posZ) / boatPosRotationIncrements;
				d12 = MathHelper.wrapAngleTo180_double(boatYaw - rotationYaw);
				rotationYaw = (float) (rotationYaw + d12 / boatPosRotationIncrements);
				rotationPitch = (float) (rotationPitch + (boatPitch - rotationPitch) / boatPosRotationIncrements);
				--boatPosRotationIncrements;
				setPosition(d2, d4, d11);
				setRotation(rotationYaw, rotationPitch);
			} else {
				d2 = posX + motionX;
				d4 = posY + motionY;
				d11 = posZ + motionZ;
				setPosition(d2, d4, d11);
				if (onGround) {
					motionX *= 0.5D;
					motionY *= 0.5D;
					motionZ *= 0.5D;
				}

				motionX *= 0.9900000095367432D;
				motionY *= 0.949999988079071D;
				motionZ *= 0.9900000095367432D;
			}
		} else {
			if (d0 < 1.0D) {
				d2 = d0 * 2.0D - 1.0D;
				motionY += 0.03999999910593033D * d2;
			} else {
				if (motionY < 0.0D) {
					motionY /= 2.0D;
				}
				motionY += 0.007000000216066837D;
			}
			if (riddenByEntity != null && riddenByEntity instanceof EntityLivingBase) {
				EntityLivingBase entitylivingbase = (EntityLivingBase) riddenByEntity;
				float f = riddenByEntity.rotationYaw + -entitylivingbase.moveStrafing * 90.0F;
				motionX += -Math.sin(f * 3.1415927F / 180.0F) * speedMultiplier * entitylivingbase.moveForward
						* 0.05000000074505806D;
				motionZ += Math.cos(f * 3.1415927F / 180.0F) * speedMultiplier * entitylivingbase.moveForward
						* 0.05000000074505806D;
			}
			d2 = Math.sqrt(motionX * motionX + motionZ * motionZ);
			if (d2 > 0.35D) {
				d4 = 0.35D / d2;
				motionX *= d4;
				motionZ *= d4;
				d2 = 0.35D;
			}
			if (d2 > d10 && speedMultiplier < 0.35D) {
				speedMultiplier += (0.35D - speedMultiplier) / 35.0D;
				if (speedMultiplier > 0.35D) {
					speedMultiplier = 0.35D;
				}
			} else {
				speedMultiplier -= (speedMultiplier - 0.07D) / 35.0D;
				if (speedMultiplier < 0.07D) {
					speedMultiplier = 0.07D;
				}
			}
			int l;
			for (l = 0; l < 4; ++l) {
				int i1 = MathHelper.floor_double(posX + (l % 2 - 0.5D) * 0.8D);
				j = MathHelper.floor_double(posZ + (l / 2 - 0.5D) * 0.8D);
				for (int j1 = 0; j1 < 2; ++j1) {
					int k = MathHelper.floor_double(posY) + j1;
					Block block = worldObj.getBlock(i1, k, j);
					if (block == Blocks.snow_layer) {
						if (CraftEventFactory.callEntityChangeBlockEvent(this, i1, k, j, Blocks.air, 0).isCancelled()) {
							continue;
						}
						worldObj.setBlockToAir(i1, k, j);
						isCollidedHorizontally = false;
					} else if (block == Blocks.waterlily) {
						if (CraftEventFactory.callEntityChangeBlockEvent(this, i1, k, j, Blocks.air, 0).isCancelled()) {
							continue;
						}
						worldObj.func_147480_a(i1, k, j, true);
						isCollidedHorizontally = false;
					}
				}
			}
			if (onGround) {
				motionX *= 0.5D;
				motionY *= 0.5D;
				motionZ *= 0.5D;
			}
			moveEntity(motionX, motionY, motionZ);
			if (isCollidedHorizontally && d10 > 0.2D) {
				if (!worldObj.isRemote && !isDead) {
					Vehicle vehicle = (Vehicle) this.getBukkitEntity();
					VehicleDestroyEvent destroyEvent = new VehicleDestroyEvent(vehicle, null);
					Bukkit.getPluginManager().callEvent(destroyEvent);
					if (!destroyEvent.isCancelled()) {
						setDead();
						for (l = 0; l < 3; ++l) {
							func_145778_a(Item.getItemFromBlock(Blocks.planks), 1, 0.0F);
						}
						for (l = 0; l < 2; ++l) {
							func_145778_a(Items.stick, 1, 0.0F);
						}
					}
				}
			} else {
				motionX *= 0.9900000095367432D;
				motionY *= 0.949999988079071D;
				motionZ *= 0.9900000095367432D;
			}
			rotationPitch = 0.0F;
			d4 = rotationYaw;
			d11 = prevPosX - posX;
			d12 = prevPosZ - posZ;
			if (d11 * d11 + d12 * d12 > 0.001D) {
				d4 = Math.atan2(d12, d11) * 180.0D / 3.141592653589793D;
			}
			double d7 = MathHelper.wrapAngleTo180_double(d4 - rotationYaw);
			if (d7 > 20.0D) {
				d7 = 20.0D;
			}
			if (d7 < -20.0D) {
				d7 = -20.0D;
			}
			rotationYaw = (float) (rotationYaw + d7);
			setRotation(rotationYaw, rotationPitch);
			org.bukkit.World bworld = worldObj.getWorld();
			// Math.floor(prevY * 10) / 10 - Preventing VehicleMoveEvent spam.Only for Y.
			Location from = new Location(bworld, prevX, Math.floor(prevY * 10) / 10, prevZ, prevYaw, prevPitch);
			Location to = new Location(bworld, posX, Math.floor(posY * 10) / 10, posZ, rotationYaw, rotationPitch);
			Vehicle vehicle = (Vehicle) this.getBukkitEntity();
			Bukkit.getPluginManager().callEvent(new org.bukkit.event.vehicle.VehicleUpdateEvent(vehicle));
			if (!from.equals(to)) {
				VehicleMoveEvent event = new VehicleMoveEvent(vehicle, from, to);
				Bukkit.getPluginManager().callEvent(event);
			}
			if (!worldObj.isRemote) {
				List list = worldObj.getEntitiesWithinAABBExcludingEntity(this,
						boundingBox.expand(0.20000000298023224D, 0.0D, 0.20000000298023224D));
				if (list != null && !list.isEmpty()) {
					for (Object aList : list) {
						Entity entity = (Entity) aList;
						if (entity != riddenByEntity && entity.canBePushed() && entity instanceof EntityBoat) {
							entity.applyEntityCollision(this);
						}
					}
				}
				if (riddenByEntity != null && riddenByEntity.isDead) {
					riddenByEntity = null;
				}
			}
		}
	}

	@Override
	public boolean attackEntityFrom(DamageSource damageSource, float damage) {
		if (!worldObj.isRemote && !isDead) {
			if (isEntityInvulnerable())
				return false;
			else {
				Vehicle vehicle = (Vehicle) this.getBukkitEntity();
				org.bukkit.entity.Entity attacker = damageSource.getEntity() == null ? null
						: damageSource.getEntity().getBukkitEntity();
				VehicleDamageEvent event = new VehicleDamageEvent(vehicle, attacker, damage);
				Bukkit.getPluginManager().callEvent(event);
				if (event.isCancelled())
					return true;
				damage = (float) event.getDamage();
				setForwardDirection(-getForwardDirection());
				setTimeSinceHit(10);
				setDamageTaken(getDamageTaken() + damage * 10.0F);
				setBeenAttacked();
				boolean flag = damageSource.getEntity() instanceof EntityPlayer
						&& ((EntityPlayer) damageSource.getEntity()).capabilities.isCreativeMode;
				if (flag || getDamageTaken() > 40.0F) {
					VehicleDestroyEvent destroyEvent = new VehicleDestroyEvent(vehicle, attacker);
					Bukkit.getPluginManager().callEvent(destroyEvent);
					if (destroyEvent.isCancelled()) {
						setDamageTaken(40F); // Maximize damage so this doesn't get triggered again right away
						return true;
					}
					if (riddenByEntity != null) {
						riddenByEntity.mountEntity(this);
					}
					if (!flag) {
						func_145778_a(Items.boat, 1, 0.0F);
					}
					setDead();
				}
				return true;
			}
		} else
			return true;
	}

	@Override
	protected boolean canTriggerWalking() {
		return false;
	}

	@Override
	protected void entityInit() {
		dataWatcher.addObject(17, new Integer(0));
		dataWatcher.addObject(18, new Integer(1));
		dataWatcher.addObject(19, new Float(0.0F));
	}

	@Override
	public AxisAlignedBB getCollisionBox(Entity p_70114_1_) {
		return p_70114_1_.boundingBox;
	}

	@Override
	public AxisAlignedBB getBoundingBox() {
		return boundingBox;
	}

	@Override
	public boolean canBePushed() {
		return true;
	}

	public EntityBoat(World p_i1705_1_, double p_i1705_2_, double p_i1705_4_, double p_i1705_6_) {
		this(p_i1705_1_);
		setPosition(p_i1705_2_, p_i1705_4_ + yOffset, p_i1705_6_);
		motionX = 0.0D;
		motionY = 0.0D;
		motionZ = 0.0D;
		prevPosX = p_i1705_2_;
		prevPosY = p_i1705_4_;
		prevPosZ = p_i1705_6_;
	}

	@Override
	public double getMountedYOffset() {
		return height * 0.0D - 0.30000001192092896D;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void performHurtAnimation() {
		setForwardDirection(-getForwardDirection());
		setTimeSinceHit(10);
		setDamageTaken(getDamageTaken() * 11.0F);
	}

	@Override
	public boolean canBeCollidedWith() {
		return !isDead;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void setPositionAndRotation2(double p_70056_1_, double p_70056_3_, double p_70056_5_, float p_70056_7_,
			float p_70056_8_, int p_70056_9_) {
		if (isBoatEmpty) {
			boatPosRotationIncrements = p_70056_9_ + 5;
		} else {
			double d3 = p_70056_1_ - posX;
			double d4 = p_70056_3_ - posY;
			double d5 = p_70056_5_ - posZ;
			double d6 = d3 * d3 + d4 * d4 + d5 * d5;

			if (d6 <= 1.0D)
				return;

			boatPosRotationIncrements = 3;
		}

		boatX = p_70056_1_;
		boatY = p_70056_3_;
		boatZ = p_70056_5_;
		boatYaw = p_70056_7_;
		boatPitch = p_70056_8_;
		motionX = velocityX;
		motionY = velocityY;
		motionZ = velocityZ;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void setVelocity(double p_70016_1_, double p_70016_3_, double p_70016_5_) {
		velocityX = motionX = p_70016_1_;
		velocityY = motionY = p_70016_3_;
		velocityZ = motionZ = p_70016_5_;
	}

	@Override
	public void updateRiderPosition() {
		if (riddenByEntity != null) {
			double d0 = Math.cos(rotationYaw * Math.PI / 180.0D) * 0.4D;
			double d1 = Math.sin(rotationYaw * Math.PI / 180.0D) * 0.4D;
			riddenByEntity.setPosition(posX + d0, posY + getMountedYOffset() + riddenByEntity.getYOffset(), posZ + d1);
		}
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound p_70014_1_) {
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound p_70037_1_) {
	}

	@Override
	@SideOnly(Side.CLIENT)
	public float getShadowSize() {
		return 0.0F;
	}

	@Override
	public boolean interactFirst(EntityPlayer p_130002_1_) {
		if (riddenByEntity != null && riddenByEntity instanceof EntityPlayer && riddenByEntity != p_130002_1_)
			return true;
		else {
			if (!worldObj.isRemote) {
				p_130002_1_.mountEntity(this);
			}

			return true;
		}
	}

	@Override
	protected void updateFallState(double p_70064_1_, boolean p_70064_3_) {
		int i = MathHelper.floor_double(posX);
		int j = MathHelper.floor_double(posY);
		int k = MathHelper.floor_double(posZ);

		if (p_70064_3_) {
			if (fallDistance > 3.0F) {
				fall(fallDistance);

				if (!worldObj.isRemote && !isDead) {
					setDead();
					int l;

					for (l = 0; l < 3; ++l) {
						func_145778_a(Item.getItemFromBlock(Blocks.planks), 1, 0.0F);
					}

					for (l = 0; l < 2; ++l) {
						func_145778_a(Items.stick, 1, 0.0F);
					}
				}

				fallDistance = 0.0F;
			}
		} else if (worldObj.getBlock(i, j - 1, k).getMaterial() != Material.water && p_70064_1_ < 0.0D) {
			fallDistance = (float) (fallDistance - p_70064_1_);
		}
	}

	public void setDamageTaken(float p_70266_1_) {
		dataWatcher.updateObject(19, Float.valueOf(p_70266_1_));
	}

	public float getDamageTaken() {
		return dataWatcher.getWatchableObjectFloat(19);
	}

	public void setTimeSinceHit(int p_70265_1_) {
		dataWatcher.updateObject(17, Integer.valueOf(p_70265_1_));
	}

	public int getTimeSinceHit() {
		return dataWatcher.getWatchableObjectInt(17);
	}

	public void setForwardDirection(int p_70269_1_) {
		dataWatcher.updateObject(18, Integer.valueOf(p_70269_1_));
	}

	public int getForwardDirection() {
		return dataWatcher.getWatchableObjectInt(18);
	}

	@SideOnly(Side.CLIENT)
	public void setIsBoatEmpty(boolean p_70270_1_) {
		isBoatEmpty = p_70270_1_;
	}
}