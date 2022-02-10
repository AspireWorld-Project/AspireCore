package net.minecraft.entity.item;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.vehicle.VehicleCreateEvent;
import org.bukkit.event.vehicle.VehicleDamageEvent;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.bukkit.event.vehicle.VehicleUpdateEvent;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntityMinecartCommandBlock;
import net.minecraft.entity.ai.EntityMinecartMobSpawner;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.IMinecartCollisionHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.minecart.MinecartCollisionEvent;
import net.minecraftforge.event.entity.minecart.MinecartUpdateEvent;

public abstract class EntityMinecart extends Entity {
	private boolean isInReverse;
	private String entityName;
	private static final int[][][] matrix = new int[][][] { { { 0, 0, -1 }, { 0, 0, 1 } },
			{ { -1, 0, 0 }, { 1, 0, 0 } }, { { -1, -1, 0 }, { 1, 0, 0 } }, { { -1, 0, 0 }, { 1, -1, 0 } },
			{ { 0, 0, -1 }, { 0, -1, 1 } }, { { 0, -1, -1 }, { 0, 0, 1 } }, { { 0, 0, 1 }, { 1, 0, 0 } },
			{ { 0, 0, 1 }, { -1, 0, 0 } }, { { 0, 0, -1 }, { -1, 0, 0 } }, { { 0, 0, -1 }, { 1, 0, 0 } } };
	private int turnProgress;
	private double minecartX;
	private double minecartY;
	private double minecartZ;
	private double minecartYaw;
	private double minecartPitch;
	@SideOnly(Side.CLIENT)
	private double velocityX;
	@SideOnly(Side.CLIENT)
	private double velocityY;
	@SideOnly(Side.CLIENT)
	private double velocityZ;
	private static final String __OBFID = "CL_00001670";

	/* Forge: Minecart Compatibility Layer Integration. */
	public static float defaultMaxSpeedAirLateral = 0.4f;
	public static float defaultMaxSpeedAirVertical = -1f;
	public static double defaultDragAir = 0.94999998807907104D;
	protected boolean canUseRail = true;
	protected boolean canBePushed = true;
	private static IMinecartCollisionHandler collisionHandler = null;

	/* Instance versions of the above physics properties */
	private float currentSpeedRail = getMaxCartSpeedOnRail();
	protected float maxSpeedAirLateral = defaultMaxSpeedAirLateral;
	protected float maxSpeedAirVertical = defaultMaxSpeedAirVertical;
	protected double dragAir = defaultDragAir;

	private double prevX;
	private double prevY;
	private double prevZ;
	private float prevYaw;
	private float prevPitch;

	public EntityMinecart(World p_i1712_1_) {
		super(p_i1712_1_);
		preventEntitySpawning = true;
		setSize(0.98F, 0.7F);
		yOffset = height / 2.0F;

		CraftEntity bukkitEntity = getBukkitEntity();
		if (bukkitEntity != null && bukkitEntity instanceof Vehicle) {
			worldObj.getServer().getPluginManager().callEvent(new VehicleCreateEvent((Vehicle) bukkitEntity));
		}
	}

	public static EntityMinecart createMinecart(World p_94090_0_, double p_94090_1_, double p_94090_3_,
			double p_94090_5_, int p_94090_7_) {
		switch (p_94090_7_) {
		case 1:
			return new EntityMinecartChest(p_94090_0_, p_94090_1_, p_94090_3_, p_94090_5_);
		case 2:
			return new EntityMinecartFurnace(p_94090_0_, p_94090_1_, p_94090_3_, p_94090_5_);
		case 3:
			return new EntityMinecartTNT(p_94090_0_, p_94090_1_, p_94090_3_, p_94090_5_);
		case 4:
			return new EntityMinecartMobSpawner(p_94090_0_, p_94090_1_, p_94090_3_, p_94090_5_);
		case 5:
			return new EntityMinecartHopper(p_94090_0_, p_94090_1_, p_94090_3_, p_94090_5_);
		case 6:
			return new EntityMinecartCommandBlock(p_94090_0_, p_94090_1_, p_94090_3_, p_94090_5_);
		default:
			return new EntityMinecartEmpty(p_94090_0_, p_94090_1_, p_94090_3_, p_94090_5_);
		}
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
		dataWatcher.addObject(20, new Integer(0));
		dataWatcher.addObject(21, new Integer(6));
		dataWatcher.addObject(22, Byte.valueOf((byte) 0));
	}

	@Override
	public AxisAlignedBB getCollisionBox(Entity p_70114_1_) {
		if (getCollisionHandler() != null)
			return getCollisionHandler().getCollisionBox(this, p_70114_1_);
		return p_70114_1_.canBePushed() ? p_70114_1_.boundingBox : null;
	}

	@Override
	public AxisAlignedBB getBoundingBox() {
		if (getCollisionHandler() != null)
			return getCollisionHandler().getBoundingBox(this);
		return null;
	}

	@Override
	public boolean canBePushed() {
		return canBePushed;
	}

	public EntityMinecart(World p_i1713_1_, double p_i1713_2_, double p_i1713_4_, double p_i1713_6_) {
		this(p_i1713_1_);
		setPosition(p_i1713_2_, p_i1713_4_, p_i1713_6_);
		motionX = 0.0D;
		motionY = 0.0D;
		motionZ = 0.0D;
		prevPosX = p_i1713_2_;
		prevPosY = p_i1713_4_;
		prevPosZ = p_i1713_6_;
	}

	@Override
	public double getMountedYOffset() {
		return height * 0.0D - 0.30000001192092896D;
	}

	@Override
	public boolean attackEntityFrom(DamageSource damageSource, float damage) {
		if (!worldObj.isRemote && !isDead) {
			if (isEntityInvulnerable())
				return false;
			else {
				Vehicle vehicle = (Vehicle) this.getBukkitEntity();
				org.bukkit.entity.Entity passenger = damageSource.getEntity() == null ? null
						: damageSource.getEntity().getBukkitEntity();
				VehicleDamageEvent event = new VehicleDamageEvent(vehicle, passenger, damage);
				Bukkit.getPluginManager().callEvent(event);
				if (event.isCancelled())
					return true;
				damage = (float) event.getDamage();
				setRollingDirection(-getRollingDirection());
				setRollingAmplitude(10);
				setBeenAttacked();
				setDamage(getDamage() + damage * 10.0F);
				boolean flag = damageSource.getEntity() instanceof EntityPlayer
						&& ((EntityPlayer) damageSource.getEntity()).capabilities.isCreativeMode;
				if (flag || getDamage() > 40.0F) {
					if (riddenByEntity != null) {
						riddenByEntity.mountEntity(this);
					}
					VehicleDestroyEvent destroyEvent = new VehicleDestroyEvent(vehicle, passenger);
					Bukkit.getPluginManager().callEvent(destroyEvent);
					if (destroyEvent.isCancelled()) {
						setDamage(40); // Maximize damage so this doesn't get triggered again right away
						return true;
					}
					if (flag && !hasCustomInventoryName()) {
						setDead();
					} else {
						killMinecart(damageSource);
					}
				}
				return true;
			}
		} else
			return true;
	}

	public void killMinecart(DamageSource p_94095_1_) {
		setDead();
		ItemStack itemstack = new ItemStack(Items.minecart, 1);

		if (entityName != null) {
			itemstack.setStackDisplayName(entityName);
		}

		entityDropItem(itemstack, 0.0F);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void performHurtAnimation() {
		setRollingDirection(-getRollingDirection());
		setRollingAmplitude(10);
		setDamage(getDamage() + getDamage() * 10.0F);
	}

	@Override
	public boolean canBeCollidedWith() {
		return !isDead;
	}

	@Override
	public void setDead() {
		super.setDead();
	}

	@Override
	public void onUpdate() {
		prevX = posX;
		prevY = posY;
		prevZ = posZ;
		prevYaw = rotationYaw;
		prevPitch = rotationPitch;

		if (getRollingAmplitude() > 0) {
			setRollingAmplitude(getRollingAmplitude() - 1);
		}

		if (getDamage() > 0.0F) {
			setDamage(getDamage() - 1.0F);
		}

		if (posY < -64.0D) {
			kill();
		}

		int i;

		if (!worldObj.isRemote && worldObj instanceof WorldServer) {
			worldObj.theProfiler.startSection("portal");
			MinecraftServer minecraftserver = ((WorldServer) worldObj).func_73046_m();
			i = getMaxInPortalTime();

			if (inPortal) {
				if (minecraftserver.getAllowNether()) {
					if (ridingEntity == null && portalCounter++ >= i) {
						portalCounter = i;
						timeUntilPortal = getPortalCooldown();
						int dim = ((WorldServer) worldObj).getConfig().portals.netherLink;

						if (dim != Integer.MIN_VALUE) {
							travelToDimension(dim);
						}
					}

					inPortal = false;
				}
			} else {
				if (portalCounter > 0) {
					portalCounter -= 4;
				}

				if (portalCounter < 0) {
					portalCounter = 0;
				}
			}

			if (timeUntilPortal > 0) {
				--timeUntilPortal;
			}

			worldObj.theProfiler.endSection();
		}

		if (worldObj.isRemote) {
			if (turnProgress > 0) {
				double d6 = posX + (minecartX - posX) / turnProgress;
				double d7 = posY + (minecartY - posY) / turnProgress;
				double d1 = posZ + (minecartZ - posZ) / turnProgress;
				double d3 = MathHelper.wrapAngleTo180_double(minecartYaw - rotationYaw);
				rotationYaw = (float) (rotationYaw + d3 / turnProgress);
				rotationPitch = (float) (rotationPitch + (minecartPitch - rotationPitch) / turnProgress);
				--turnProgress;
				setPosition(d6, d7, d1);
				setRotation(rotationYaw, rotationPitch);
			} else {
				setPosition(posX, posY, posZ);
				setRotation(rotationYaw, rotationPitch);
			}
		} else {
			prevPosX = posX;
			prevPosY = posY;
			prevPosZ = posZ;
			motionY -= 0.03999999910593033D;
			int l = MathHelper.floor_double(posX);
			i = MathHelper.floor_double(posY);
			int i1 = MathHelper.floor_double(posZ);

			if (BlockRailBase.func_150049_b_(worldObj, l, i - 1, i1)) {
				--i;
			}

			double d0 = 0.4D;
			Block block = worldObj.getBlock(l, i, i1);

			if (canUseRail() && BlockRailBase.func_150051_a(block)) {
				float railMaxSpeed = ((BlockRailBase) block).getRailMaxSpeed(worldObj, this, l, i, i1);
				double maxSpeed = Math.min(railMaxSpeed, getCurrentCartSpeedCapOnRail());
				func_145821_a(l, i, i1, maxSpeed, getSlopeAdjustment(), block,
						((BlockRailBase) block).getBasicRailMetadata(worldObj, this, l, i, i1));

				if (block == Blocks.activator_rail) {
					onActivatorRailPass(l, i, i1, (worldObj.getBlockMetadata(l, i, i1) & 8) != 0);
				}
			} else {
				func_94088_b(onGround ? d0 : getMaxSpeedAirLateral());
			}

			func_145775_I();
			rotationPitch = 0.0F;
			double d8 = prevPosX - posX;
			double d4 = prevPosZ - posZ;

			if (d8 * d8 + d4 * d4 > 0.001D) {
				rotationYaw = (float) (Math.atan2(d4, d8) * 180.0D / Math.PI);

				if (isInReverse) {
					rotationYaw += 180.0F;
				}
			}

			double d5 = MathHelper.wrapAngleTo180_float(rotationYaw - prevRotationYaw);

			if (d5 < -170.0D || d5 >= 170.0D) {
				rotationYaw += 180.0F;
				isInReverse = !isInReverse;
			}

			setRotation(rotationYaw, rotationPitch);

			org.bukkit.World bworld = worldObj.getWorld();
			Location from = new Location(bworld, prevX, prevY, prevZ, prevYaw, prevPitch);
			Location to = new Location(bworld, posX, posY, posZ, rotationYaw, rotationPitch);
			Vehicle vehicle = (Vehicle) this.getBukkitEntity();
			Bukkit.getPluginManager().callEvent(new VehicleUpdateEvent(vehicle));
			if (!from.equals(to)) {
				Bukkit.getPluginManager().callEvent(new VehicleMoveEvent(vehicle, from, to));
			}

			AxisAlignedBB box;
			if (getCollisionHandler() != null) {
				box = getCollisionHandler().getMinecartCollisionBox(this);
			} else {
				box = boundingBox.expand(0.2D, 0.0D, 0.2D);
			}

			List list = worldObj.getEntitiesWithinAABBExcludingEntity(this, box);

			if (list != null && !list.isEmpty()) {
				for (int k = 0; k < list.size(); ++k) {
					Entity entity = (Entity) list.get(k);

					if (entity != riddenByEntity && entity.canBePushed() && entity instanceof EntityMinecart) {
						entity.applyEntityCollision(this);
					}
				}
			}

			if (riddenByEntity != null && riddenByEntity.isDead) {
				if (riddenByEntity.ridingEntity == this) {
					riddenByEntity.ridingEntity = null;
				}

				riddenByEntity = null;
			}

			MinecraftForge.EVENT_BUS.post(new MinecartUpdateEvent(this, l, i, i1));
		}
	}

	public void onActivatorRailPass(int p_96095_1_, int p_96095_2_, int p_96095_3_, boolean p_96095_4_) {
	}

	protected void func_94088_b(double p_94088_1_) {
		if (motionX < -p_94088_1_) {
			motionX = -p_94088_1_;
		}

		if (motionX > p_94088_1_) {
			motionX = p_94088_1_;
		}

		if (motionZ < -p_94088_1_) {
			motionZ = -p_94088_1_;
		}

		if (motionZ > p_94088_1_) {
			motionZ = p_94088_1_;
		}

		double moveY = motionY;
		if (getMaxSpeedAirVertical() > 0 && motionY > getMaxSpeedAirVertical()) {
			moveY = getMaxSpeedAirVertical();
			if (Math.abs(motionX) < 0.3f && Math.abs(motionZ) < 0.3f) {
				moveY = 0.15f;
				motionY = moveY;
			}
		}

		if (onGround) {
			motionX *= 0.5D;
			motionY *= 0.5D;
			motionZ *= 0.5D;
		}

		moveEntity(motionX, moveY, motionZ);

		if (!onGround) {
			motionX *= getDragAir();
			motionY *= getDragAir();
			motionZ *= getDragAir();
		}
	}

	protected void func_145821_a(int p_145821_1_, int p_145821_2_, int p_145821_3_, double p_145821_4_,
			double p_145821_6_, Block p_145821_8_, int p_145821_9_) {
		fallDistance = 0.0F;
		Vec3 vec3 = func_70489_a(posX, posY, posZ);
		posY = p_145821_2_;
		boolean flag = false;
		boolean flag1 = false;

		if (p_145821_8_ == Blocks.golden_rail) {
			flag = (worldObj.getBlockMetadata(p_145821_1_, p_145821_2_, p_145821_3_) & 8) != 0;
			flag1 = !flag;
		}

		if (((BlockRailBase) p_145821_8_).isPowered()) {
			p_145821_9_ &= 7;
		}

		if (p_145821_9_ >= 2 && p_145821_9_ <= 5) {
			posY = p_145821_2_ + 1;
		}

		if (p_145821_9_ == 2) {
			motionX -= p_145821_6_;
		}

		if (p_145821_9_ == 3) {
			motionX += p_145821_6_;
		}

		if (p_145821_9_ == 4) {
			motionZ += p_145821_6_;
		}

		if (p_145821_9_ == 5) {
			motionZ -= p_145821_6_;
		}

		int[][] aint = matrix[p_145821_9_];
		double d2 = aint[1][0] - aint[0][0];
		double d3 = aint[1][2] - aint[0][2];
		double d4 = Math.sqrt(d2 * d2 + d3 * d3);
		double d5 = motionX * d2 + motionZ * d3;

		if (d5 < 0.0D) {
			d2 = -d2;
			d3 = -d3;
		}

		double d6 = Math.sqrt(motionX * motionX + motionZ * motionZ);

		if (d6 > 2.0D) {
			d6 = 2.0D;
		}

		motionX = d6 * d2 / d4;
		motionZ = d6 * d3 / d4;
		double d7;
		double d8;
		double d9;
		double d10;

		if (riddenByEntity != null && riddenByEntity instanceof EntityLivingBase) {
			d7 = ((EntityLivingBase) riddenByEntity).moveForward;

			if (d7 > 0.0D) {
				d8 = -Math.sin(riddenByEntity.rotationYaw * (float) Math.PI / 180.0F);
				d9 = Math.cos(riddenByEntity.rotationYaw * (float) Math.PI / 180.0F);
				d10 = motionX * motionX + motionZ * motionZ;

				if (d10 < 0.01D) {
					motionX += d8 * 0.1D;
					motionZ += d9 * 0.1D;
					flag1 = false;
				}
			}
		}

		if (flag1 && shouldDoRailFunctions()) {
			d7 = Math.sqrt(motionX * motionX + motionZ * motionZ);

			if (d7 < 0.03D) {
				motionX *= 0.0D;
				motionY *= 0.0D;
				motionZ *= 0.0D;
			} else {
				motionX *= 0.5D;
				motionY *= 0.0D;
				motionZ *= 0.5D;
			}
		}

		d7 = 0.0D;
		d8 = p_145821_1_ + 0.5D + aint[0][0] * 0.5D;
		d9 = p_145821_3_ + 0.5D + aint[0][2] * 0.5D;
		d10 = p_145821_1_ + 0.5D + aint[1][0] * 0.5D;
		double d11 = p_145821_3_ + 0.5D + aint[1][2] * 0.5D;
		d2 = d10 - d8;
		d3 = d11 - d9;
		double d12;
		double d13;

		if (d2 == 0.0D) {
			posX = p_145821_1_ + 0.5D;
			d7 = posZ - p_145821_3_;
		} else if (d3 == 0.0D) {
			posZ = p_145821_3_ + 0.5D;
			d7 = posX - p_145821_1_;
		} else {
			d12 = posX - d8;
			d13 = posZ - d9;
			d7 = (d12 * d2 + d13 * d3) * 2.0D;
		}

		posX = d8 + d2 * d7;
		posZ = d9 + d3 * d7;
		setPosition(posX, posY + yOffset, posZ);

		moveMinecartOnRail(p_145821_1_, p_145821_2_, p_145821_3_, p_145821_4_);

		if (aint[0][1] != 0 && MathHelper.floor_double(posX) - p_145821_1_ == aint[0][0]
				&& MathHelper.floor_double(posZ) - p_145821_3_ == aint[0][2]) {
			setPosition(posX, posY + aint[0][1], posZ);
		} else if (aint[1][1] != 0 && MathHelper.floor_double(posX) - p_145821_1_ == aint[1][0]
				&& MathHelper.floor_double(posZ) - p_145821_3_ == aint[1][2]) {
			setPosition(posX, posY + aint[1][1], posZ);
		}

		applyDrag();
		Vec3 vec31 = func_70489_a(posX, posY, posZ);

		if (vec31 != null && vec3 != null) {
			double d14 = (vec3.yCoord - vec31.yCoord) * 0.05D;
			d6 = Math.sqrt(motionX * motionX + motionZ * motionZ);

			if (d6 > 0.0D) {
				motionX = motionX / d6 * (d6 + d14);
				motionZ = motionZ / d6 * (d6 + d14);
			}

			setPosition(posX, vec31.yCoord, posZ);
		}

		int j1 = MathHelper.floor_double(posX);
		int i1 = MathHelper.floor_double(posZ);

		if (j1 != p_145821_1_ || i1 != p_145821_3_) {
			d6 = Math.sqrt(motionX * motionX + motionZ * motionZ);
			motionX = d6 * (j1 - p_145821_1_);
			motionZ = d6 * (i1 - p_145821_3_);
		}

		if (shouldDoRailFunctions()) {
			((BlockRailBase) p_145821_8_).onMinecartPass(worldObj, this, p_145821_1_, p_145821_2_, p_145821_3_);
		}

		if (flag && shouldDoRailFunctions()) {
			double d15 = Math.sqrt(motionX * motionX + motionZ * motionZ);

			if (d15 > 0.01D) {
				double d16 = 0.06D;
				motionX += motionX / d15 * d16;
				motionZ += motionZ / d15 * d16;
			} else if (p_145821_9_ == 1) {
				if (worldObj.getBlock(p_145821_1_ - 1, p_145821_2_, p_145821_3_).isNormalCube()) {
					motionX = 0.02D;
				} else if (worldObj.getBlock(p_145821_1_ + 1, p_145821_2_, p_145821_3_).isNormalCube()) {
					motionX = -0.02D;
				}
			} else if (p_145821_9_ == 0) {
				if (worldObj.getBlock(p_145821_1_, p_145821_2_, p_145821_3_ - 1).isNormalCube()) {
					motionZ = 0.02D;
				} else if (worldObj.getBlock(p_145821_1_, p_145821_2_, p_145821_3_ + 1).isNormalCube()) {
					motionZ = -0.02D;
				}
			}
		}
	}

	protected void applyDrag() {
		if (riddenByEntity != null) {
			motionX *= 0.996999979019165D;
			motionY *= 0.0D;
			motionZ *= 0.996999979019165D;
		} else {
			motionX *= 0.9599999785423279D;
			motionY *= 0.0D;
			motionZ *= 0.9599999785423279D;
		}
	}

	@SideOnly(Side.CLIENT)
	public Vec3 func_70495_a(double p_70495_1_, double p_70495_3_, double p_70495_5_, double p_70495_7_) {
		int i = MathHelper.floor_double(p_70495_1_);
		int j = MathHelper.floor_double(p_70495_3_);
		int k = MathHelper.floor_double(p_70495_5_);

		if (BlockRailBase.func_150049_b_(worldObj, i, j - 1, k)) {
			--j;
		}

		Block block = worldObj.getBlock(i, j, k);

		if (!BlockRailBase.func_150051_a(block))
			return null;
		else {
			int l = ((BlockRailBase) block).getBasicRailMetadata(worldObj, this, i, j, k);

			p_70495_3_ = j;

			if (l >= 2 && l <= 5) {
				p_70495_3_ = j + 1;
			}

			int[][] aint = matrix[l];
			double d4 = aint[1][0] - aint[0][0];
			double d5 = aint[1][2] - aint[0][2];
			double d6 = Math.sqrt(d4 * d4 + d5 * d5);
			d4 /= d6;
			d5 /= d6;
			p_70495_1_ += d4 * p_70495_7_;
			p_70495_5_ += d5 * p_70495_7_;

			if (aint[0][1] != 0 && MathHelper.floor_double(p_70495_1_) - i == aint[0][0]
					&& MathHelper.floor_double(p_70495_5_) - k == aint[0][2]) {
				p_70495_3_ += aint[0][1];
			} else if (aint[1][1] != 0 && MathHelper.floor_double(p_70495_1_) - i == aint[1][0]
					&& MathHelper.floor_double(p_70495_5_) - k == aint[1][2]) {
				p_70495_3_ += aint[1][1];
			}

			return func_70489_a(p_70495_1_, p_70495_3_, p_70495_5_);
		}
	}

	public Vec3 func_70489_a(double p_70489_1_, double p_70489_3_, double p_70489_5_) {
		int i = MathHelper.floor_double(p_70489_1_);
		int j = MathHelper.floor_double(p_70489_3_);
		int k = MathHelper.floor_double(p_70489_5_);

		if (BlockRailBase.func_150049_b_(worldObj, i, j - 1, k)) {
			--j;
		}

		Block block = worldObj.getBlock(i, j, k);

		if (BlockRailBase.func_150051_a(block)) {
			int l = ((BlockRailBase) block).getBasicRailMetadata(worldObj, this, i, j, k);
			p_70489_3_ = j;

			if (l >= 2 && l <= 5) {
				p_70489_3_ = j + 1;
			}

			int[][] aint = matrix[l];
			double d3 = 0.0D;
			double d4 = i + 0.5D + aint[0][0] * 0.5D;
			double d5 = j + 0.5D + aint[0][1] * 0.5D;
			double d6 = k + 0.5D + aint[0][2] * 0.5D;
			double d7 = i + 0.5D + aint[1][0] * 0.5D;
			double d8 = j + 0.5D + aint[1][1] * 0.5D;
			double d9 = k + 0.5D + aint[1][2] * 0.5D;
			double d10 = d7 - d4;
			double d11 = (d8 - d5) * 2.0D;
			double d12 = d9 - d6;

			if (d10 == 0.0D) {
				p_70489_1_ = i + 0.5D;
				d3 = p_70489_5_ - k;
			} else if (d12 == 0.0D) {
				p_70489_5_ = k + 0.5D;
				d3 = p_70489_1_ - i;
			} else {
				double d13 = p_70489_1_ - d4;
				double d14 = p_70489_5_ - d6;
				d3 = (d13 * d10 + d14 * d12) * 2.0D;
			}

			p_70489_1_ = d4 + d10 * d3;
			p_70489_3_ = d5 + d11 * d3;
			p_70489_5_ = d6 + d12 * d3;

			if (d11 < 0.0D) {
				++p_70489_3_;
			}

			if (d11 > 0.0D) {
				p_70489_3_ += 0.5D;
			}

			return Vec3.createVectorHelper(p_70489_1_, p_70489_3_, p_70489_5_);
		} else
			return null;
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound p_70037_1_) {
		if (p_70037_1_.getBoolean("CustomDisplayTile")) {
			func_145819_k(p_70037_1_.getInteger("DisplayTile"));
			setDisplayTileData(p_70037_1_.getInteger("DisplayData"));
			setDisplayTileOffset(p_70037_1_.getInteger("DisplayOffset"));
		}

		if (p_70037_1_.hasKey("CustomName", 8) && p_70037_1_.getString("CustomName").length() > 0) {
			entityName = p_70037_1_.getString("CustomName");
		}
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound p_70014_1_) {
		if (hasDisplayTile()) {
			p_70014_1_.setBoolean("CustomDisplayTile", true);
			p_70014_1_.setInteger("DisplayTile",
					func_145820_n().getMaterial() == Material.air ? 0 : Block.getIdFromBlock(func_145820_n()));
			p_70014_1_.setInteger("DisplayData", getDisplayTileData());
			p_70014_1_.setInteger("DisplayOffset", getDisplayTileOffset());
		}

		if (entityName != null && entityName.length() > 0) {
			p_70014_1_.setString("CustomName", entityName);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public float getShadowSize() {
		return 0.0F;
	}

	@Override
	public void applyEntityCollision(Entity p_70108_1_) {
		MinecraftForge.EVENT_BUS.post(new MinecartCollisionEvent(this, p_70108_1_));
		if (getCollisionHandler() != null) {
			getCollisionHandler().onEntityCollision(this, p_70108_1_);
			return;
		}
		if (!worldObj.isRemote) {
			if (p_70108_1_ != riddenByEntity) {
				if (p_70108_1_ instanceof EntityLivingBase && !(p_70108_1_ instanceof EntityPlayer)
						&& !(p_70108_1_ instanceof EntityIronGolem) && canBeRidden()
						&& motionX * motionX + motionZ * motionZ > 0.01D && riddenByEntity == null
						&& p_70108_1_.ridingEntity == null) {
					p_70108_1_.mountEntity(this);
				}

				double d0 = p_70108_1_.posX - posX;
				double d1 = p_70108_1_.posZ - posZ;
				double d2 = d0 * d0 + d1 * d1;

				if (d2 >= 9.999999747378752E-5D) {
					d2 = MathHelper.sqrt_double(d2);
					d0 /= d2;
					d1 /= d2;
					double d3 = 1.0D / d2;

					if (d3 > 1.0D) {
						d3 = 1.0D;
					}

					d0 *= d3;
					d1 *= d3;
					d0 *= 0.10000000149011612D;
					d1 *= 0.10000000149011612D;
					d0 *= 1.0F - entityCollisionReduction;
					d1 *= 1.0F - entityCollisionReduction;
					d0 *= 0.5D;
					d1 *= 0.5D;

					if (p_70108_1_ instanceof EntityMinecart) {
						double d4 = p_70108_1_.posX - posX;
						double d5 = p_70108_1_.posZ - posZ;
						Vec3 vec3 = Vec3.createVectorHelper(d4, 0.0D, d5).normalize();
						Vec3 vec31 = Vec3.createVectorHelper(MathHelper.cos(rotationYaw * (float) Math.PI / 180.0F),
								0.0D, MathHelper.sin(rotationYaw * (float) Math.PI / 180.0F)).normalize();
						double d6 = Math.abs(vec3.dotProduct(vec31));

						if (d6 < 0.800000011920929D)
							return;

						double d7 = p_70108_1_.motionX + motionX;
						double d8 = p_70108_1_.motionZ + motionZ;

						if (((EntityMinecart) p_70108_1_).isPoweredCart() && !isPoweredCart()) {
							motionX *= 0.20000000298023224D;
							motionZ *= 0.20000000298023224D;
							addVelocity(p_70108_1_.motionX - d0, 0.0D, p_70108_1_.motionZ - d1);
							p_70108_1_.motionX *= 0.949999988079071D;
							p_70108_1_.motionZ *= 0.949999988079071D;
						} else if (((EntityMinecart) p_70108_1_).isPoweredCart() && !isPoweredCart()) {
							p_70108_1_.motionX *= 0.20000000298023224D;
							p_70108_1_.motionZ *= 0.20000000298023224D;
							p_70108_1_.addVelocity(motionX + d0, 0.0D, motionZ + d1);
							motionX *= 0.949999988079071D;
							motionZ *= 0.949999988079071D;
						} else {
							d7 /= 2.0D;
							d8 /= 2.0D;
							motionX *= 0.20000000298023224D;
							motionZ *= 0.20000000298023224D;
							addVelocity(d7 - d0, 0.0D, d8 - d1);
							p_70108_1_.motionX *= 0.20000000298023224D;
							p_70108_1_.motionZ *= 0.20000000298023224D;
							p_70108_1_.addVelocity(d7 + d0, 0.0D, d8 + d1);
						}
					} else {
						addVelocity(-d0, 0.0D, -d1);
						p_70108_1_.addVelocity(d0 / 4.0D, 0.0D, d1 / 4.0D);
					}
				}
			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void setPositionAndRotation2(double p_70056_1_, double p_70056_3_, double p_70056_5_, float p_70056_7_,
			float p_70056_8_, int p_70056_9_) {
		minecartX = p_70056_1_;
		minecartY = p_70056_3_;
		minecartZ = p_70056_5_;
		minecartYaw = p_70056_7_;
		minecartPitch = p_70056_8_;
		turnProgress = p_70056_9_ + 2;
		motionX = velocityX;
		motionY = velocityY;
		motionZ = velocityZ;
	}

	public void setDamage(float p_70492_1_) {
		dataWatcher.updateObject(19, Float.valueOf(p_70492_1_));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void setVelocity(double p_70016_1_, double p_70016_3_, double p_70016_5_) {
		velocityX = motionX = p_70016_1_;
		velocityY = motionY = p_70016_3_;
		velocityZ = motionZ = p_70016_5_;
	}

	public float getDamage() {
		return dataWatcher.getWatchableObjectFloat(19);
	}

	public void setRollingAmplitude(int p_70497_1_) {
		dataWatcher.updateObject(17, Integer.valueOf(p_70497_1_));
	}

	public int getRollingAmplitude() {
		return dataWatcher.getWatchableObjectInt(17);
	}

	public void setRollingDirection(int p_70494_1_) {
		dataWatcher.updateObject(18, Integer.valueOf(p_70494_1_));
	}

	public int getRollingDirection() {
		return dataWatcher.getWatchableObjectInt(18);
	}

	public abstract int getMinecartType();

	public Block func_145820_n() {
		if (!hasDisplayTile())
			return func_145817_o();
		else {
			int i = getDataWatcher().getWatchableObjectInt(20) & 65535;
			return Block.getBlockById(i);
		}
	}

	public Block func_145817_o() {
		return Blocks.air;
	}

	public int getDisplayTileData() {
		return !hasDisplayTile() ? getDefaultDisplayTileData() : getDataWatcher().getWatchableObjectInt(20) >> 16;
	}

	public int getDefaultDisplayTileData() {
		return 0;
	}

	public int getDisplayTileOffset() {
		return !hasDisplayTile() ? getDefaultDisplayTileOffset() : getDataWatcher().getWatchableObjectInt(21);
	}

	public int getDefaultDisplayTileOffset() {
		return 6;
	}

	public void func_145819_k(int p_145819_1_) {
		getDataWatcher().updateObject(20, Integer.valueOf(p_145819_1_ & 65535 | getDisplayTileData() << 16));
		setHasDisplayTile(true);
	}

	public void setDisplayTileData(int p_94092_1_) {
		getDataWatcher().updateObject(20,
				Integer.valueOf(Block.getIdFromBlock(func_145820_n()) & 65535 | p_94092_1_ << 16));
		setHasDisplayTile(true);
	}

	public void setDisplayTileOffset(int p_94086_1_) {
		getDataWatcher().updateObject(21, Integer.valueOf(p_94086_1_));
		setHasDisplayTile(true);
	}

	public boolean hasDisplayTile() {
		return getDataWatcher().getWatchableObjectByte(22) == 1;
	}

	public void setHasDisplayTile(boolean p_94096_1_) {
		getDataWatcher().updateObject(22, Byte.valueOf((byte) (p_94096_1_ ? 1 : 0)));
	}

	public void setMinecartName(String p_96094_1_) {
		entityName = p_96094_1_;
	}

	@Override
	public String getCommandSenderName() {
		return entityName != null ? entityName : super.getCommandSenderName();
	}

	public boolean hasCustomInventoryName() {
		return entityName != null;
	}

	public String func_95999_t() {
		return entityName;
	}

	/*
	 * =================================== FORGE START
	 * ===========================================
	 */
	/**
	 * Moved to allow overrides. This code handles minecart movement and speed
	 * capping when on a rail.
	 */
	public void moveMinecartOnRail(int x, int y, int z, double par4) {
		double d12 = motionX;
		double d13 = motionZ;

		if (riddenByEntity != null) {
			d12 *= 0.75D;
			d13 *= 0.75D;
		}

		if (d12 < -par4) {
			d12 = -par4;
		}

		if (d12 > par4) {
			d12 = par4;
		}

		if (d13 < -par4) {
			d13 = -par4;
		}

		if (d13 > par4) {
			d13 = par4;
		}

		moveEntity(d12, 0.0D, d13);
	}

	/**
	 * Gets the current global Minecart Collision handler if none is registered,
	 * returns null
	 *
	 * @return The collision handler or null
	 */
	public static IMinecartCollisionHandler getCollisionHandler() {
		return collisionHandler;
	}

	/**
	 * Sets the global Minecart Collision handler, overwrites any that is currently
	 * set.
	 *
	 * @param handler
	 *            The new handler
	 */
	public static void setCollisionHandler(IMinecartCollisionHandler handler) {
		collisionHandler = handler;
	}

	/**
	 * This function returns an ItemStack that represents this cart. This should be
	 * an ItemStack that can be used by the player to place the cart, but is not
	 * necessary the item the cart drops when destroyed.
	 *
	 * @return An ItemStack that can be used to place the cart.
	 */
	public ItemStack getCartItem() {
		if (this instanceof EntityMinecartFurnace)
			return new ItemStack(Items.furnace_minecart);
		else if (this instanceof EntityMinecartChest)
			return new ItemStack(Items.chest_minecart);
		else if (this instanceof EntityMinecartTNT)
			return new ItemStack(Items.tnt_minecart);
		else if (this instanceof EntityMinecartHopper)
			return new ItemStack(Items.hopper_minecart);
		else if (this instanceof EntityMinecartCommandBlock)
			return new ItemStack(Items.command_block_minecart);
		return new ItemStack(Items.minecart);
	}

	/**
	 * Returns true if this cart can currently use rails. This function is mainly
	 * used to gracefully detach a minecart from a rail.
	 *
	 * @return True if the minecart can use rails.
	 */
	public boolean canUseRail() {
		return canUseRail;
	}

	/**
	 * Set whether the minecart can use rails. This function is mainly used to
	 * gracefully detach a minecart from a rail.
	 *
	 * @param use
	 *            Whether the minecart can currently use rails.
	 */
	public void setCanUseRail(boolean use) {
		canUseRail = use;
	}

	/**
	 * Return false if this cart should not call onMinecartPass() and should ignore
	 * Powered Rails.
	 *
	 * @return True if this cart should call onMinecartPass().
	 */
	public boolean shouldDoRailFunctions() {
		return true;
	}

	/**
	 * Returns true if this cart is self propelled.
	 *
	 * @return True if powered.
	 */
	public boolean isPoweredCart() {
		return getMinecartType() == 2;
	}

	/**
	 * Returns true if this cart can be ridden by an Entity.
	 *
	 * @return True if this cart can be ridden.
	 */
	public boolean canBeRidden() {
		if (this instanceof EntityMinecartEmpty)
			return true;
		return false;
	}

	/**
	 * Getters/setters for physics variables
	 */

	/**
	 * Returns the carts max speed when traveling on rails. Carts going faster than
	 * 1.1 cause issues with chunk loading. Carts cant traverse slopes or corners at
	 * greater than 0.5 - 0.6. This value is compared with the rails max speed and
	 * the carts current speed cap to determine the carts current max speed. A
	 * normal rail's max speed is 0.4.
	 *
	 * @return Carts max speed.
	 */
	public float getMaxCartSpeedOnRail() {
		return 1.2f;
	}

	/**
	 * Returns the current speed cap for the cart when traveling on rails. This
	 * functions differs from getMaxCartSpeedOnRail() in that it controls current
	 * movement and cannot be overridden. The value however can never be higher than
	 * getMaxCartSpeedOnRail().
	 *
	 * @return
	 */
	public final float getCurrentCartSpeedCapOnRail() {
		return currentSpeedRail;
	}

	public final void setCurrentCartSpeedCapOnRail(float value) {
		value = Math.min(value, getMaxCartSpeedOnRail());
		currentSpeedRail = value;
	}

	public float getMaxSpeedAirLateral() {
		return maxSpeedAirLateral;
	}

	public void setMaxSpeedAirLateral(float value) {
		maxSpeedAirLateral = value;
	}

	public float getMaxSpeedAirVertical() {
		return maxSpeedAirVertical;
	}

	public void setMaxSpeedAirVertical(float value) {
		maxSpeedAirVertical = value;
	}

	public double getDragAir() {
		return dragAir;
	}

	public void setDragAir(double value) {
		dragAir = value;
	}

	public double getSlopeAdjustment() {
		return 0.0078125D;
	}
	/*
	 * =================================== FORGE END
	 * ===========================================
	 */
}
