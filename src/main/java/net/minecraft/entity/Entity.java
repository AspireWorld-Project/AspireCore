package net.minecraft.entity;

import com.mojang.authlib.GameProfile;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jline.internal.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.enchantment.EnchantmentProtection;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.*;
import net.minecraft.world.Explosion;
import net.minecraft.world.Teleporter;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.IExtendedEntityProperties;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.fluids.IFluidBlock;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.TravelAgent;
import org.bukkit.craftbukkit.CraftTravelAgent;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.projectiles.ProjectileSource;
import org.ultramine.server.EntityType;
import org.ultramine.server.event.EntitySetFireEvent;
import org.ultramine.server.internal.UMHooks;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadLocalRandom;

public abstract class Entity {
	private static int nextEntityID;
	private int entityId;
	public double renderDistanceWeight;
	public boolean preventEntitySpawning;
	public Entity riddenByEntity;
	public Entity ridingEntity;
	public boolean forceSpawn;
	public World worldObj;
	public double prevPosX;
	public double prevPosY;
	public double prevPosZ;
	public double posX;
	public double posY;
	public double posZ;
	public double motionX;
	public double motionY;
	public double motionZ;
	public float rotationYaw;
	public float rotationPitch;
	public float prevRotationYaw;
	public float prevRotationPitch;
	public final AxisAlignedBB boundingBox;
	public boolean onGround;
	public boolean isCollidedHorizontally;
	public boolean isCollidedVertically;
	public boolean isCollided;
	public boolean velocityChanged;
	protected boolean isInWeb;
	public boolean field_70135_K;
	public boolean isDead;
	public float yOffset;
	public float width;
	public float height;
	public float prevDistanceWalkedModified;
	public float distanceWalkedModified;
	public float distanceWalkedOnStepModified;
	public float fallDistance;
	private int nextStepDistance;
	public double lastTickPosX;
	public double lastTickPosY;
	public double lastTickPosZ;
	public float ySize;
	public float stepHeight;
	public boolean noClip;
	public float entityCollisionReduction;
	protected Random rand;
	public int ticksExisted;
	public int fireResistance;
	private int fire;
	protected boolean inWater;
	public int hurtResistantTime;
	private boolean firstUpdate;
	protected boolean isImmuneToFire;
	public DataWatcher dataWatcher;
	private double entityRiderPitchDelta;
	private double entityRiderYawDelta;
	public boolean addedToChunk;
	public int chunkCoordX;
	public int chunkCoordY;
	public int chunkCoordZ;
	@SideOnly(Side.CLIENT)
	public int serverPosX;
	@SideOnly(Side.CLIENT)
	public int serverPosY;
	@SideOnly(Side.CLIENT)
	public int serverPosZ;
	public boolean ignoreFrustumCheck;
	public boolean isAirBorne;
	public int timeUntilPortal;
	protected boolean inPortal;
	protected int portalCounter;
	public int dimension;
	protected int teleportDirection;
	private boolean invulnerable;
	protected UUID entityUniqueID;
	public Entity.EnumEntitySize myEntitySize;
	private static final String __OBFID = "CL_00001533";
	/** Forge: Used to store custom data for each entity. */
	private NBTTagCompound customEntityData;
	public boolean captureDrops = false;
	public ArrayList<EntityItem> capturedDrops = new ArrayList<>();
	private UUID persistentID;

	public CraftEntity bukkitEntity;
	public ProjectileSource projectileSource;
	public String spawnReason;

	protected HashMap<String, IExtendedEntityProperties> extendedProperties;

	public int getEntityId() {
		return entityId;
	}

	public void setEntityId(int p_145769_1_) {
		entityId = p_145769_1_;
	}

	public Entity(World p_i1582_1_) {
		entityId = nextEntityID++;
		renderDistanceWeight = 1.0D;
		boundingBox = AxisAlignedBB.getBoundingBox(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
		field_70135_K = true;
		width = 0.6F;
		height = 1.8F;
		nextStepDistance = 1;
		rand = new Random();
		fireResistance = 1;
		firstUpdate = true;
		entityUniqueID = new UUID(ThreadLocalRandom.current().nextLong(), ThreadLocalRandom.current().nextLong());
		myEntitySize = Entity.EnumEntitySize.SIZE_2;
		worldObj = p_i1582_1_;
		setPosition(0.0D, 0.0D, 0.0D);

		if (p_i1582_1_ != null) {
			dimension = p_i1582_1_.provider.dimensionId;
		}

		dataWatcher = new DataWatcher(this);
		dataWatcher.addObject(0, Byte.valueOf((byte) 0));
		dataWatcher.addObject(1, Short.valueOf((short) 300));
		entityInit();

		extendedProperties = new HashMap<>();

		MinecraftForge.EVENT_BUS.post(new EntityEvent.EntityConstructing(this));

		for (IExtendedEntityProperties props : extendedProperties.values()) {
			props.init(this, p_i1582_1_);
		}
	}

	protected abstract void entityInit();

	public DataWatcher getDataWatcher() {
		return dataWatcher;
	}

	@Override
	public boolean equals(Object p_equals_1_) {
		return p_equals_1_ instanceof Entity && ((Entity) p_equals_1_).entityId == entityId;
	}

	@Override
	public int hashCode() {
		return entityId;
	}

	@SideOnly(Side.CLIENT)
	protected void preparePlayerToSpawn() {
		if (worldObj != null) {
			while (posY > 0.0D) {
				setPosition(posX, posY, posZ);

				if (worldObj.getCollidingBoundingBoxes(this, boundingBox).isEmpty()) {
					break;
				}

				++posY;
			}

			motionX = motionY = motionZ = 0.0D;
			rotationPitch = 0.0F;
		}
	}

	public void setDead() {
		isDead = true;
	}

	protected void setSize(float p_70105_1_, float p_70105_2_) {
		float f2;

		if (p_70105_1_ != width || p_70105_2_ != height) {
			f2 = width;
			width = p_70105_1_;
			height = p_70105_2_;
			boundingBox.maxX = boundingBox.minX + width;
			boundingBox.maxZ = boundingBox.minZ + width;
			boundingBox.maxY = boundingBox.minY + height;

			if (width > f2 && !firstUpdate && !worldObj.isRemote) {
				moveEntity(f2 - width, 0.0D, f2 - width);
			}
		}

		f2 = p_70105_1_ % 2.0F;

		if (f2 < 0.375D) {
			myEntitySize = Entity.EnumEntitySize.SIZE_1;
		} else if (f2 < 0.75D) {
			myEntitySize = Entity.EnumEntitySize.SIZE_2;
		} else if (f2 < 1.0D) {
			myEntitySize = Entity.EnumEntitySize.SIZE_3;
		} else if (f2 < 1.375D) {
			myEntitySize = Entity.EnumEntitySize.SIZE_4;
		} else if (f2 < 1.75D) {
			myEntitySize = Entity.EnumEntitySize.SIZE_5;
		} else {
			myEntitySize = Entity.EnumEntitySize.SIZE_6;
		}
	}

	protected void setRotation(float p_70101_1_, float p_70101_2_) {
		rotationYaw = p_70101_1_ % 360.0F;
		rotationPitch = p_70101_2_ % 360.0F;
	}

	public void setPosition(double p_70107_1_, double p_70107_3_, double p_70107_5_) {
		posX = p_70107_1_;
		posY = p_70107_3_;
		posZ = p_70107_5_;
		float f = width / 2.0F;
		float f1 = height;
		boundingBox.setBounds(p_70107_1_ - f, p_70107_3_ - yOffset + ySize, p_70107_5_ - f, p_70107_1_ + f,
				p_70107_3_ - yOffset + ySize + f1, p_70107_5_ + f);
	}

	@SideOnly(Side.CLIENT)
	public void setAngles(float p_70082_1_, float p_70082_2_) {
		float f2 = rotationPitch;
		float f3 = rotationYaw;
		rotationYaw = (float) (rotationYaw + p_70082_1_ * 0.15D);
		rotationPitch = (float) (rotationPitch - p_70082_2_ * 0.15D);

		if (rotationPitch < -90.0F) {
			rotationPitch = -90.0F;
		}

		if (rotationPitch > 90.0F) {
			rotationPitch = 90.0F;
		}

		prevRotationPitch += rotationPitch - f2;
		prevRotationYaw += rotationYaw - f3;
	}

	public void onUpdate() {
		onEntityUpdate();
	}

	public void onEntityUpdate() {
		worldObj.theProfiler.startSection("entityBaseTick");

		if (ridingEntity != null && ridingEntity.isDead) {
			ridingEntity = null;
		}

		prevDistanceWalkedModified = distanceWalkedModified;
		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;
		prevRotationPitch = rotationPitch;
		prevRotationYaw = rotationYaw;
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

		if (isSprinting() && !isInWater()) {
			int j = MathHelper.floor_double(posX);
			i = MathHelper.floor_double(posY - 0.20000000298023224D - yOffset);
			int k = MathHelper.floor_double(posZ);
			Block block = worldObj.getBlock(j, i, k);

			if (block.getMaterial() != Material.air) {
				worldObj.spawnParticle(
						"blockcrack_" + Block.getIdFromBlock(block) + "_" + worldObj.getBlockMetadata(j, i, k),
						posX + (rand.nextFloat() - 0.5D) * width, boundingBox.minY + 0.1D,
						posZ + (rand.nextFloat() - 0.5D) * width, -motionX * 4.0D, 1.5D, -motionZ * 4.0D);
			}
		}

		handleWaterMovement();

		if (worldObj.isRemote) {
			fire = 0;
		} else if (fire > 0) {
			if (isImmuneToFire) {
				fire -= 4;

				if (fire < 0) {
					fire = 0;
				}
			} else {
				if (fire % 20 == 0) {
					attackEntityFrom(DamageSource.onFire, 1.0F);
				}

				--fire;
			}
		}

		if (handleLavaMovement()) {
			setOnFireFromLava();
			fallDistance *= 0.5F;
		}

		if (posY < -64.0D) {
			kill();
		}

		if (!worldObj.isRemote) {
			setFlag(0, fire > 0);
		}

		firstUpdate = false;
		worldObj.theProfiler.endSection();
	}

	public int getMaxInPortalTime() {
		return 0;
	}

	protected void setOnFireFromLava() {
		if (!isImmuneToFire) {
			if (this instanceof EntityLivingBase) {
				// TODO: Fix event spam, when player is in creative mode.Also, try to make event
				// fire, when player is in survival mode.
				if (fire == 0) {
					org.bukkit.entity.Entity damagee = getBukkitEntity();
					org.bukkit.block.Block combusterBlock = getMaterialInBB(boundingBox, Material.lava);
					EntityCombustEvent combustEvent = new org.bukkit.event.entity.EntityCombustByBlockEvent(
							combusterBlock, damagee, 15);
					Bukkit.getPluginManager().callEvent(combustEvent);
					if (!combustEvent.isCancelled()) {
						setFire(combustEvent.getDuration());
					}
				} else {
					setFire(15);
				}
			}
		}
	}

	public void setFire(int p_70015_1_) {
		int j = p_70015_1_ * 20;
		EntitySetFireEvent event = new EntitySetFireEvent(this, j);
		if (MinecraftForge.EVENT_BUS.post(event))
			return;
		j = event.fireTicks;
		j = EnchantmentProtection.getFireTimeForEntity(this, j);

		if (fire < j) {
			fire = j;
		}
	}

	public void extinguish() {
		fire = 0;
	}

	protected void kill() {
		setDead();
	}

	public boolean isOffsetPositionInLiquid(double p_70038_1_, double p_70038_3_, double p_70038_5_) {
		AxisAlignedBB axisalignedbb = boundingBox.getOffsetBoundingBox(p_70038_1_, p_70038_3_, p_70038_5_);
		List list = worldObj.getCollidingBoundingBoxes(this, axisalignedbb);
		return list.isEmpty() && !worldObj.isAnyLiquid(axisalignedbb);
	}

	public void moveEntity(double p_70091_1_, double p_70091_3_, double p_70091_5_) {
		if (noClip) {
			boundingBox.offset(p_70091_1_, p_70091_3_, p_70091_5_);
			posX = (boundingBox.minX + boundingBox.maxX) / 2.0D;
			posY = boundingBox.minY + yOffset - ySize;
			posZ = (boundingBox.minZ + boundingBox.maxZ) / 2.0D;
		} else {
			worldObj.theProfiler.startSection("move");
			ySize *= 0.4F;
			double d3 = posX;
			double d4 = posY;
			double d5 = posZ;

			if (isInWeb) {
				isInWeb = false;
				p_70091_1_ *= 0.25D;
				p_70091_3_ *= 0.05000000074505806D;
				p_70091_5_ *= 0.25D;
				motionX = 0.0D;
				motionY = 0.0D;
				motionZ = 0.0D;
			}

			double d6 = p_70091_1_;
			double d7 = p_70091_3_;
			double d8 = p_70091_5_;
			AxisAlignedBB axisalignedbb = boundingBox.copy();
			boolean flag = onGround && isSneaking() && this instanceof EntityPlayer;

			if (flag) {
				double d9;

				for (d9 = 0.05D; p_70091_1_ != 0.0D && worldObj
						.getCollidingBoundingBoxes(this, boundingBox.getOffsetBoundingBox(p_70091_1_, -1.0D, 0.0D))
						.isEmpty(); d6 = p_70091_1_) {
					if (p_70091_1_ < d9 && p_70091_1_ >= -d9) {
						p_70091_1_ = 0.0D;
					} else if (p_70091_1_ > 0.0D) {
						p_70091_1_ -= d9;
					} else {
						p_70091_1_ += d9;
					}
				}

				for (; p_70091_5_ != 0.0D && worldObj
						.getCollidingBoundingBoxes(this, boundingBox.getOffsetBoundingBox(0.0D, -1.0D, p_70091_5_))
						.isEmpty(); d8 = p_70091_5_) {
					if (p_70091_5_ < d9 && p_70091_5_ >= -d9) {
						p_70091_5_ = 0.0D;
					} else if (p_70091_5_ > 0.0D) {
						p_70091_5_ -= d9;
					} else {
						p_70091_5_ += d9;
					}
				}

				while (p_70091_1_ != 0.0D && p_70091_5_ != 0.0D && worldObj.getCollidingBoundingBoxes(this,
						boundingBox.getOffsetBoundingBox(p_70091_1_, -1.0D, p_70091_5_)).isEmpty()) {
					if (p_70091_1_ < d9 && p_70091_1_ >= -d9) {
						p_70091_1_ = 0.0D;
					} else if (p_70091_1_ > 0.0D) {
						p_70091_1_ -= d9;
					} else {
						p_70091_1_ += d9;
					}

					if (p_70091_5_ < d9 && p_70091_5_ >= -d9) {
						p_70091_5_ = 0.0D;
					} else if (p_70091_5_ > 0.0D) {
						p_70091_5_ -= d9;
					} else {
						p_70091_5_ += d9;
					}

					d6 = p_70091_1_;
					d8 = p_70091_5_;
				}
			}

			List list = worldObj.getCollidingBoundingBoxes(this,
					boundingBox.addCoord(p_70091_1_, p_70091_3_, p_70091_5_));

			for (int i = 0; i < list.size(); ++i) {
				p_70091_3_ = ((AxisAlignedBB) list.get(i)).calculateYOffset(boundingBox, p_70091_3_);
			}

			boundingBox.offset(0.0D, p_70091_3_, 0.0D);

			if (!field_70135_K && d7 != p_70091_3_) {
				p_70091_5_ = 0.0D;
				p_70091_3_ = 0.0D;
				p_70091_1_ = 0.0D;
			}

			boolean flag1 = onGround || d7 != p_70091_3_ && d7 < 0.0D;
			int j;

			for (j = 0; j < list.size(); ++j) {
				p_70091_1_ = ((AxisAlignedBB) list.get(j)).calculateXOffset(boundingBox, p_70091_1_);
			}

			boundingBox.offset(p_70091_1_, 0.0D, 0.0D);

			if (!field_70135_K && d6 != p_70091_1_) {
				p_70091_5_ = 0.0D;
				p_70091_3_ = 0.0D;
				p_70091_1_ = 0.0D;
			}

			for (j = 0; j < list.size(); ++j) {
				p_70091_5_ = ((AxisAlignedBB) list.get(j)).calculateZOffset(boundingBox, p_70091_5_);
			}

			boundingBox.offset(0.0D, 0.0D, p_70091_5_);

			if (!field_70135_K && d8 != p_70091_5_) {
				p_70091_5_ = 0.0D;
				p_70091_3_ = 0.0D;
				p_70091_1_ = 0.0D;
			}

			double d10;
			double d11;
			int k;
			double d12;

			if (stepHeight > 0.0F && flag1 && (flag || ySize < 0.05F) && (d6 != p_70091_1_ || d8 != p_70091_5_)) {
				d12 = p_70091_1_;
				d10 = p_70091_3_;
				d11 = p_70091_5_;
				p_70091_1_ = d6;
				p_70091_3_ = stepHeight;
				p_70091_5_ = d8;
				AxisAlignedBB axisalignedbb1 = boundingBox.copy();
				boundingBox.setBB(axisalignedbb);
				list = worldObj.getCollidingBoundingBoxes(this, boundingBox.addCoord(d6, p_70091_3_, d8));

				for (k = 0; k < list.size(); ++k) {
					p_70091_3_ = ((AxisAlignedBB) list.get(k)).calculateYOffset(boundingBox, p_70091_3_);
				}

				boundingBox.offset(0.0D, p_70091_3_, 0.0D);

				if (!field_70135_K && d7 != p_70091_3_) {
					p_70091_5_ = 0.0D;
					p_70091_3_ = 0.0D;
					p_70091_1_ = 0.0D;
				}

				for (k = 0; k < list.size(); ++k) {
					p_70091_1_ = ((AxisAlignedBB) list.get(k)).calculateXOffset(boundingBox, p_70091_1_);
				}

				boundingBox.offset(p_70091_1_, 0.0D, 0.0D);

				if (!field_70135_K && d6 != p_70091_1_) {
					p_70091_5_ = 0.0D;
					p_70091_3_ = 0.0D;
					p_70091_1_ = 0.0D;
				}

				for (k = 0; k < list.size(); ++k) {
					p_70091_5_ = ((AxisAlignedBB) list.get(k)).calculateZOffset(boundingBox, p_70091_5_);
				}

				boundingBox.offset(0.0D, 0.0D, p_70091_5_);

				if (!field_70135_K && d8 != p_70091_5_) {
					p_70091_5_ = 0.0D;
					p_70091_3_ = 0.0D;
					p_70091_1_ = 0.0D;
				}

				if (!field_70135_K && d7 != p_70091_3_) {
					p_70091_5_ = 0.0D;
					p_70091_3_ = 0.0D;
					p_70091_1_ = 0.0D;
				} else {
					p_70091_3_ = -stepHeight;

					for (k = 0; k < list.size(); ++k) {
						p_70091_3_ = ((AxisAlignedBB) list.get(k)).calculateYOffset(boundingBox, p_70091_3_);
					}

					boundingBox.offset(0.0D, p_70091_3_, 0.0D);
				}

				if (d12 * d12 + d11 * d11 >= p_70091_1_ * p_70091_1_ + p_70091_5_ * p_70091_5_) {
					p_70091_1_ = d12;
					p_70091_3_ = d10;
					p_70091_5_ = d11;
					boundingBox.setBB(axisalignedbb1);
				}
			}

			worldObj.theProfiler.endSection();
			worldObj.theProfiler.startSection("rest");
			posX = (boundingBox.minX + boundingBox.maxX) / 2.0D;
			posY = boundingBox.minY + yOffset - ySize;
			posZ = (boundingBox.minZ + boundingBox.maxZ) / 2.0D;
			isCollidedHorizontally = d6 != p_70091_1_ || d8 != p_70091_5_;
			isCollidedVertically = d7 != p_70091_3_;
			onGround = d7 != p_70091_3_ && d7 < 0.0D;
			isCollided = isCollidedHorizontally || isCollidedVertically;
			updateFallState(p_70091_3_, onGround);

			if (d6 != p_70091_1_) {
				motionX = 0.0D;
			}

			if (d7 != p_70091_3_) {
				motionY = 0.0D;
			}

			if (d8 != p_70091_5_) {
				motionZ = 0.0D;
			}

			d12 = posX - d3;
			d10 = posY - d4;
			d11 = posZ - d5;

			if (canTriggerWalking() && !flag && ridingEntity == null) {
				int j1 = MathHelper.floor_double(posX);
				k = MathHelper.floor_double(posY - 0.20000000298023224D - yOffset);
				int l = MathHelper.floor_double(posZ);
				Block block = worldObj.getBlock(j1, k, l);
				int i1 = worldObj.getBlock(j1, k - 1, l).getRenderType();

				if (i1 == 11 || i1 == 32 || i1 == 21) {
					block = worldObj.getBlock(j1, k - 1, l);
				}

				if (block != Blocks.ladder) {
					d10 = 0.0D;
				}

				distanceWalkedModified = (float) (distanceWalkedModified
						+ MathHelper.sqrt_double(d12 * d12 + d11 * d11) * 0.6D);
				distanceWalkedOnStepModified = (float) (distanceWalkedOnStepModified
						+ MathHelper.sqrt_double(d12 * d12 + d10 * d10 + d11 * d11) * 0.6D);

				if (distanceWalkedOnStepModified > nextStepDistance && block.getMaterial() != Material.air) {
					nextStepDistance = (int) distanceWalkedOnStepModified + 1;

					if (isInWater()) {
						float f = MathHelper.sqrt_double(motionX * motionX * 0.20000000298023224D + motionY * motionY
								+ motionZ * motionZ * 0.20000000298023224D) * 0.35F;

						if (f > 1.0F) {
							f = 1.0F;
						}

						if (!isEntityPlayerMP() || !((EntityPlayerMP) this).isHidden()) {
							playSound(getSwimSound(), f, 1.0F + (rand.nextFloat() - rand.nextFloat()) * 0.4F);
						}
					}

					func_145780_a(j1, k, l, block);
					block.onEntityWalking(worldObj, j1, k, l, this);
				}
			}

			try {
				func_145775_I();
			} catch (Throwable throwable) {
				CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Checking entity block collision");
				CrashReportCategory crashreportcategory = crashreport
						.makeCategory("Entity being checked for collision");
				addEntityCrashInfo(crashreportcategory);
				throw new ReportedException(crashreport);
			}

			boolean flag2 = isWet();

			if (worldObj.func_147470_e(boundingBox.contract(0.001D, 0.001D, 0.001D))) {
				dealFireDamage(1);

				if (!flag2) {
					++fire;

					if (fire == 0) {
						EntityCombustEvent event = new EntityCombustEvent(getBukkitEntity(), 8);
						Bukkit.getPluginManager().callEvent(event);
						if (!event.isCancelled()) {
							setFire(event.getDuration());
						}
					}
				}
			} else if (fire <= 0) {
				fire = -fireResistance;
			}

			if (flag2 && fire > 0) {
				playSound("random.fizz", 0.7F, 1.6F + (rand.nextFloat() - rand.nextFloat()) * 0.4F);
				fire = -fireResistance;
			}

			worldObj.theProfiler.endSection();
		}
	}

	protected String getSwimSound() {
		return "game.neutral.swim";
	}

	protected void func_145775_I() {
		int i = MathHelper.floor_double(boundingBox.minX + 0.001D);
		int j = MathHelper.floor_double(boundingBox.minY + 0.001D);
		int k = MathHelper.floor_double(boundingBox.minZ + 0.001D);
		int l = MathHelper.floor_double(boundingBox.maxX - 0.001D);
		int i1 = MathHelper.floor_double(boundingBox.maxY - 0.001D);
		int j1 = MathHelper.floor_double(boundingBox.maxZ - 0.001D);

		if (worldObj.checkChunksExist(i, j, k, l, i1, j1)) {
			for (int k1 = i; k1 <= l; ++k1) {
				for (int l1 = j; l1 <= i1; ++l1) {
					for (int i2 = k; i2 <= j1; ++i2) {
						Block block = worldObj.getBlock(k1, l1, i2);

						try {
							block.onEntityCollidedWithBlock(worldObj, k1, l1, i2, this);
						} catch (Throwable throwable) {
							CrashReport crashreport = CrashReport.makeCrashReport(throwable,
									"Colliding entity with block");
							CrashReportCategory crashreportcategory = crashreport
									.makeCategory("Block being collided with");
							CrashReportCategory.func_147153_a(crashreportcategory, k1, l1, i2, block,
									worldObj.getBlockMetadata(k1, l1, i2));
							throw new ReportedException(crashreport);
						}
					}
				}
			}
		}
	}

	protected void func_145780_a(int p_145780_1_, int p_145780_2_, int p_145780_3_, Block p_145780_4_) {
		if (isEntityPlayerMP() && ((EntityPlayerMP) this).isHidden())
			return;
		Block.SoundType soundtype = p_145780_4_.stepSound;

		if (worldObj.getBlock(p_145780_1_, p_145780_2_ + 1, p_145780_3_) == Blocks.snow_layer) {
			soundtype = Blocks.snow_layer.stepSound;
			playSound(soundtype.getStepResourcePath(), soundtype.getVolume() * 0.15F, soundtype.getPitch());
		} else if (!p_145780_4_.getMaterial().isLiquid()) {
			playSound(soundtype.getStepResourcePath(), soundtype.getVolume() * 0.15F, soundtype.getPitch());
		}
	}

	public void playSound(String p_85030_1_, float p_85030_2_, float p_85030_3_) {
		worldObj.playSoundAtEntity(this, p_85030_1_, p_85030_2_, p_85030_3_);
	}

	protected boolean canTriggerWalking() {
		return true;
	}

	protected void updateFallState(double p_70064_1_, boolean p_70064_3_) {
		if (p_70064_3_) {
			if (fallDistance > 0.0F) {
				fall(fallDistance);
				fallDistance = 0.0F;
			}
		} else if (p_70064_1_ < 0.0D) {
			fallDistance = (float) (fallDistance - p_70064_1_);
		}
	}

	public AxisAlignedBB getBoundingBox() {
		return null;
	}

	protected void dealFireDamage(int p_70081_1_) {
		if (!isImmuneToFire) {
			attackEntityFrom(DamageSource.inFire, p_70081_1_);
		}
	}

	public final boolean isImmuneToFire() {
		return isImmuneToFire;
	}

	protected void fall(float p_70069_1_) {
		if (riddenByEntity != null) {
			riddenByEntity.fall(p_70069_1_);
		}
	}

	public boolean isWet() {
		return inWater
				|| worldObj.canLightningStrikeAt(MathHelper.floor_double(posX), MathHelper.floor_double(posY),
						MathHelper.floor_double(posZ))
				|| worldObj.canLightningStrikeAt(MathHelper.floor_double(posX), MathHelper.floor_double(posY + height),
						MathHelper.floor_double(posZ));
	}

	public boolean isInWater() {
		return inWater;
	}

	public boolean handleWaterMovement() {
		if (worldObj.handleMaterialAcceleration(
				boundingBox.expand(0.0D, -0.4000000059604645D, 0.0D).contract(0.001D, 0.001D, 0.001D), Material.water,
				this)) {
			if (!inWater && !firstUpdate) {
				float f = MathHelper.sqrt_double(motionX * motionX * 0.20000000298023224D + motionY * motionY
						+ motionZ * motionZ * 0.20000000298023224D) * 0.2F;

				if (f > 1.0F) {
					f = 1.0F;
				}

				playSound(getSplashSound(), f, 1.0F + (rand.nextFloat() - rand.nextFloat()) * 0.4F);
				float f1 = MathHelper.floor_double(boundingBox.minY);
				int i;
				float f2;
				float f3;

				for (i = 0; i < 1.0F + width * 20.0F; ++i) {
					f2 = (rand.nextFloat() * 2.0F - 1.0F) * width;
					f3 = (rand.nextFloat() * 2.0F - 1.0F) * width;
					worldObj.spawnParticle("bubble", posX + f2, f1 + 1.0F, posZ + f3, motionX,
							motionY - rand.nextFloat() * 0.2F, motionZ);
				}

				for (i = 0; i < 1.0F + width * 20.0F; ++i) {
					f2 = (rand.nextFloat() * 2.0F - 1.0F) * width;
					f3 = (rand.nextFloat() * 2.0F - 1.0F) * width;
					worldObj.spawnParticle("splash", posX + f2, f1 + 1.0F, posZ + f3, motionX, motionY, motionZ);
				}
			}

			fallDistance = 0.0F;
			inWater = true;
			fire = 0;
		} else {
			inWater = false;
		}

		return inWater;
	}

	protected String getSplashSound() {
		return "game.neutral.swim.splash";
	}

	public boolean isInsideOfMaterial(Material p_70055_1_) {
		double d0 = posY + getEyeHeight();
		int i = MathHelper.floor_double(posX);
		int j = MathHelper.floor_float(MathHelper.floor_double(d0));
		int k = MathHelper.floor_double(posZ);
		Block block = worldObj.getBlock(i, j, k);

		if (block.getMaterial() == p_70055_1_) {
			double filled = 1.0f; // If it's not a liquid assume it's a solid block
			if (block instanceof IFluidBlock) {
				filled = ((IFluidBlock) block).getFilledPercentage(worldObj, i, j, k);
			}

			if (filled < 0) {
				filled *= -1;
				// filled -= 0.11111111F; //Why this is needed.. not sure...
				return d0 > j + (1 - filled);
			} else
				return d0 < j + filled;
		} else
			return false;
	}

	public float getEyeHeight() {
		return 0.0F;
	}

	public boolean handleLavaMovement() {
		return worldObj.isMaterialInBB(
				boundingBox.expand(-0.10000000149011612D, -0.4000000059604645D, -0.10000000149011612D), Material.lava);
	}

	public void moveFlying(float p_70060_1_, float p_70060_2_, float p_70060_3_) {
		float f3 = p_70060_1_ * p_70060_1_ + p_70060_2_ * p_70060_2_;

		if (f3 >= 1.0E-4F) {
			f3 = MathHelper.sqrt_float(f3);

			if (f3 < 1.0F) {
				f3 = 1.0F;
			}

			f3 = p_70060_3_ / f3;
			p_70060_1_ *= f3;
			p_70060_2_ *= f3;
			float f4 = MathHelper.sin(rotationYaw * (float) Math.PI / 180.0F);
			float f5 = MathHelper.cos(rotationYaw * (float) Math.PI / 180.0F);
			motionX += p_70060_1_ * f5 - p_70060_2_ * f4;
			motionZ += p_70060_2_ * f5 + p_70060_1_ * f4;
		}
	}

	@SideOnly(Side.CLIENT)
	public int getBrightnessForRender(float p_70070_1_) {
		int i = MathHelper.floor_double(posX);
		int j = MathHelper.floor_double(posZ);

		if (worldObj.blockExists(i, 0, j)) {
			double d0 = (boundingBox.maxY - boundingBox.minY) * 0.66D;
			int k = MathHelper.floor_double(posY - yOffset + d0);
			return worldObj.getLightBrightnessForSkyBlocks(i, k, j, 0);
		} else
			return 0;
	}

	public float getBrightness(float p_70013_1_) {
		int i = MathHelper.floor_double(posX);
		int j = MathHelper.floor_double(posZ);

		if (worldObj.blockExists(i, 0, j)) {
			double d0 = (boundingBox.maxY - boundingBox.minY) * 0.66D;
			int k = MathHelper.floor_double(posY - yOffset + d0);
			return worldObj.getLightBrightness(i, k, j);
		} else
			return 0.0F;
	}

	public void setWorld(World p_70029_1_) {
		worldObj = p_70029_1_;
	}

	public void setPositionAndRotation(double p_70080_1_, double p_70080_3_, double p_70080_5_, float p_70080_7_,
			float p_70080_8_) {
		prevPosX = posX = p_70080_1_;
		prevPosY = posY = p_70080_3_;
		prevPosZ = posZ = p_70080_5_;
		prevRotationYaw = rotationYaw = p_70080_7_;
		prevRotationPitch = rotationPitch = p_70080_8_;
		ySize = 0.0F;
		double d3 = prevRotationYaw - p_70080_7_;

		if (d3 < -180.0D) {
			prevRotationYaw += 360.0F;
		}

		if (d3 >= 180.0D) {
			prevRotationYaw -= 360.0F;
		}

		setPosition(posX, posY, posZ);
		setRotation(p_70080_7_, p_70080_8_);
	}

	public void setLocationAndAngles(double p_70012_1_, double p_70012_3_, double p_70012_5_, float p_70012_7_,
			float p_70012_8_) {
		lastTickPosX = prevPosX = posX = p_70012_1_;
		lastTickPosY = prevPosY = posY = p_70012_3_ + yOffset;
		lastTickPosZ = prevPosZ = posZ = p_70012_5_;
		rotationYaw = p_70012_7_;
		rotationPitch = p_70012_8_;
		setPosition(posX, posY, posZ);
	}

	public float getDistanceToEntity(Entity p_70032_1_) {
		float f = (float) (posX - p_70032_1_.posX);
		float f1 = (float) (posY - p_70032_1_.posY);
		float f2 = (float) (posZ - p_70032_1_.posZ);
		return MathHelper.sqrt_float(f * f + f1 * f1 + f2 * f2);
	}

	public double getDistanceSq(double p_70092_1_, double p_70092_3_, double p_70092_5_) {
		double d3 = posX - p_70092_1_;
		double d4 = posY - p_70092_3_;
		double d5 = posZ - p_70092_5_;
		return d3 * d3 + d4 * d4 + d5 * d5;
	}

	public double getDistance(double p_70011_1_, double p_70011_3_, double p_70011_5_) {
		double d3 = posX - p_70011_1_;
		double d4 = posY - p_70011_3_;
		double d5 = posZ - p_70011_5_;
		return MathHelper.sqrt_double(d3 * d3 + d4 * d4 + d5 * d5);
	}

	public double getDistanceSqToEntity(Entity p_70068_1_) {
		double d0 = posX - p_70068_1_.posX;
		double d1 = posY - p_70068_1_.posY;
		double d2 = posZ - p_70068_1_.posZ;
		return d0 * d0 + d1 * d1 + d2 * d2;
	}

	public void onCollideWithPlayer(EntityPlayer p_70100_1_) {
	}

	public void applyEntityCollision(Entity p_70108_1_) {
		if (p_70108_1_.riddenByEntity != this && p_70108_1_.ridingEntity != this) {
			double d0 = p_70108_1_.posX - posX;
			double d1 = p_70108_1_.posZ - posZ;
			double d2 = MathHelper.abs_max(d0, d1);

			if (d2 >= 0.009999999776482582D) {
				d2 = MathHelper.sqrt_double(d2);
				d0 /= d2;
				d1 /= d2;
				double d3 = 1.0D / d2;

				if (d3 > 1.0D) {
					d3 = 1.0D;
				}

				d0 *= d3;
				d1 *= d3;
				d0 *= 0.05000000074505806D;
				d1 *= 0.05000000074505806D;
				d0 *= 1.0F - entityCollisionReduction;
				d1 *= 1.0F - entityCollisionReduction;
				addVelocity(-d0, 0.0D, -d1);
				p_70108_1_.addVelocity(d0, 0.0D, d1);
			}
		}
	}

	public void addVelocity(double p_70024_1_, double p_70024_3_, double p_70024_5_) {
		motionX += p_70024_1_;
		motionY += p_70024_3_;
		motionZ += p_70024_5_;
		isAirBorne = true;
	}

	protected void setBeenAttacked() {
		velocityChanged = true;
	}

	public boolean attackEntityFrom(DamageSource p_70097_1_, float p_70097_2_) {
		if (isEntityInvulnerable())
			return false;
		else {
			setBeenAttacked();
			return false;
		}
	}

	public boolean canBeCollidedWith() {
		return false;
	}

	public boolean canBePushed() {
		return false;
	}

	public void addToPlayerScore(Entity p_70084_1_, int p_70084_2_) {
	}

	@SideOnly(Side.CLIENT)
	public boolean isInRangeToRender3d(double p_145770_1_, double p_145770_3_, double p_145770_5_) {
		double d3 = posX - p_145770_1_;
		double d4 = posY - p_145770_3_;
		double d5 = posZ - p_145770_5_;
		double d6 = d3 * d3 + d4 * d4 + d5 * d5;
		return isInRangeToRenderDist(d6);
	}

	@SideOnly(Side.CLIENT)
	public boolean isInRangeToRenderDist(double p_70112_1_) {
		double d1 = boundingBox.getAverageEdgeLength();
		d1 *= 64.0D * renderDistanceWeight;
		return p_70112_1_ < d1 * d1;
	}

	public boolean writeMountToNBT(NBTTagCompound p_98035_1_) {
		String s = getEntityString();

		if (!isDead && s != null) {
			p_98035_1_.setString("id", s);
			writeToNBT(p_98035_1_);
			return true;
		} else
			return false;
	}

	public boolean writeToNBTOptional(NBTTagCompound p_70039_1_) {
		String s = getEntityString();

		if (!isDead && s != null && riddenByEntity == null) {
			p_70039_1_.setString("id", s);
			writeToNBT(p_70039_1_);
			return true;
		} else
			return false;
	}

	public void writeToNBT(NBTTagCompound p_70109_1_) {
		try {
			if (owner != null) {
				UMHooks.writeObjectOwner(p_70109_1_, owner);
			}
			p_70109_1_.setTag("Pos", newDoubleNBTList(posX, posY + ySize, posZ));
			p_70109_1_.setTag("Motion", newDoubleNBTList(motionX, motionY, motionZ));
			p_70109_1_.setTag("Rotation", newFloatNBTList(rotationYaw, rotationPitch));
			p_70109_1_.setFloat("FallDistance", fallDistance);
			p_70109_1_.setShort("Fire", (short) fire);
			p_70109_1_.setShort("Air", (short) getAir());
			p_70109_1_.setBoolean("OnGround", onGround);
			p_70109_1_.setInteger("Dimension", dimension);
			p_70109_1_.setBoolean("Invulnerable", invulnerable);
			p_70109_1_.setInteger("PortalCooldown", timeUntilPortal);
			p_70109_1_.setLong("UUIDMost", getUniqueID().getMostSignificantBits());
			p_70109_1_.setLong("UUIDLeast", getUniqueID().getLeastSignificantBits());
			if (customEntityData != null) {
				p_70109_1_.setTag("ForgeData", customEntityData);
			}

			for (String identifier : extendedProperties.keySet()) {
				try {
					IExtendedEntityProperties props = extendedProperties.get(identifier);
					props.saveNBTData(p_70109_1_);
				} catch (Throwable t) {
					FMLLog.severe("Failed to save extended properties for %s.  This is a mod issue.", identifier);
					t.printStackTrace();
				}
			}

			writeEntityToNBT(p_70109_1_);

			if (ridingEntity != null) {
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();

				if (ridingEntity.writeMountToNBT(nbttagcompound1)) {
					p_70109_1_.setTag("Riding", nbttagcompound1);
				}
			}
		} catch (Throwable throwable) {
			CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Saving entity NBT");
			CrashReportCategory crashreportcategory = crashreport.makeCategory("Entity being saved");
			addEntityCrashInfo(crashreportcategory);
			throw new ReportedException(crashreport);
		}
	}

	public void readFromNBT(NBTTagCompound p_70020_1_) {
		try {
			if (owner == null) {
				owner = UMHooks.readObjectOwner(p_70020_1_);
			}
			NBTTagList nbttaglist = p_70020_1_.getTagList("Pos", 6);
			NBTTagList nbttaglist1 = p_70020_1_.getTagList("Motion", 6);
			NBTTagList nbttaglist2 = p_70020_1_.getTagList("Rotation", 5);
			motionX = nbttaglist1.func_150309_d(0);
			motionY = nbttaglist1.func_150309_d(1);
			motionZ = nbttaglist1.func_150309_d(2);

			if (Math.abs(motionX) > 10.0D) {
				motionX = 0.0D;
			}

			if (Math.abs(motionY) > 10.0D) {
				motionY = 0.0D;
			}

			if (Math.abs(motionZ) > 10.0D) {
				motionZ = 0.0D;
			}

			prevPosX = lastTickPosX = posX = nbttaglist.func_150309_d(0);
			prevPosY = lastTickPosY = posY = nbttaglist.func_150309_d(1);
			prevPosZ = lastTickPosZ = posZ = nbttaglist.func_150309_d(2);
			prevRotationYaw = rotationYaw = nbttaglist2.func_150308_e(0);
			prevRotationPitch = rotationPitch = nbttaglist2.func_150308_e(1);
			fallDistance = p_70020_1_.getFloat("FallDistance");
			fire = p_70020_1_.getShort("Fire");
			setAir(p_70020_1_.getShort("Air"));
			onGround = p_70020_1_.getBoolean("OnGround");
			dimension = p_70020_1_.getInteger("Dimension");
			invulnerable = p_70020_1_.getBoolean("Invulnerable");
			timeUntilPortal = p_70020_1_.getInteger("PortalCooldown");

			if (p_70020_1_.hasKey("UUIDMost", 4) && p_70020_1_.hasKey("UUIDLeast", 4)) {
				entityUniqueID = new UUID(p_70020_1_.getLong("UUIDMost"), p_70020_1_.getLong("UUIDLeast"));
			}

			setPosition(posX, posY, posZ);
			setRotation(rotationYaw, rotationPitch);
			if (p_70020_1_.hasKey("ForgeData")) {
				customEntityData = p_70020_1_.getCompoundTag("ForgeData");
			}

			for (String identifier : extendedProperties.keySet()) {
				try {
					IExtendedEntityProperties props = extendedProperties.get(identifier);
					props.loadNBTData(p_70020_1_);
				} catch (Throwable t) {
					FMLLog.severe("Failed to load extended properties for %s.  This is a mod issue.", identifier);
					t.printStackTrace();
				}
			}

			// Rawr, legacy code, Vanilla added a UUID, keep this so older maps will convert
			// properly
			if (p_70020_1_.hasKey("PersistentIDMSB") && p_70020_1_.hasKey("PersistentIDLSB")) {
				entityUniqueID = new UUID(p_70020_1_.getLong("PersistentIDMSB"), p_70020_1_.getLong("PersistentIDLSB"));
			}
			readEntityFromNBT(p_70020_1_);

			if (shouldSetPosAfterLoading()) {
				setPosition(posX, posY, posZ);
			}
		} catch (Throwable throwable) {
			CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Loading entity NBT");
			CrashReportCategory crashreportcategory = crashreport.makeCategory("Entity being loaded");
			addEntityCrashInfo(crashreportcategory);
			throw new ReportedException(crashreport);
		}
	}

	protected boolean shouldSetPosAfterLoading() {
		return true;
	}

	protected final String getEntityString() {
		return EntityList.getEntityString(this);
	}

	protected abstract void readEntityFromNBT(NBTTagCompound p_70037_1_);

	protected abstract void writeEntityToNBT(NBTTagCompound p_70014_1_);

	public void onChunkLoad() {
	}

	protected NBTTagList newDoubleNBTList(double... p_70087_1_) {
		NBTTagList nbttaglist = new NBTTagList();
		double[] adouble = p_70087_1_;
		int i = p_70087_1_.length;

		for (int j = 0; j < i; ++j) {
			double d1 = adouble[j];
			nbttaglist.appendTag(new NBTTagDouble(d1));
		}

		return nbttaglist;
	}

	protected NBTTagList newFloatNBTList(float... p_70049_1_) {
		NBTTagList nbttaglist = new NBTTagList();
		float[] afloat = p_70049_1_;
		int i = p_70049_1_.length;

		for (int j = 0; j < i; ++j) {
			float f1 = afloat[j];
			nbttaglist.appendTag(new NBTTagFloat(f1));
		}

		return nbttaglist;
	}

	public EntityItem dropItem(Item p_145779_1_, int p_145779_2_) {
		return func_145778_a(p_145779_1_, p_145779_2_, 0.0F);
	}

	public EntityItem func_145778_a(Item p_145778_1_, int p_145778_2_, float p_145778_3_) {
		return entityDropItem(new ItemStack(p_145778_1_, p_145778_2_, 0), p_145778_3_);
	}

	public EntityItem entityDropItem(ItemStack p_70099_1_, float p_70099_2_) {
		if (p_70099_1_.stackSize != 0 && p_70099_1_.getItem() != null) {
			EntityItem entityitem = new EntityItem(worldObj, posX, posY + p_70099_2_, posZ, p_70099_1_);
			entityitem.delayBeforeCanPickup = 10;
			if (captureDrops) {
				capturedDrops.add(entityitem);
			} else {
				worldObj.spawnEntityInWorld(entityitem);
			}
			return entityitem;
		} else
			return null;
	}

	@SideOnly(Side.CLIENT)
	public float getShadowSize() {
		return height / 2.0F;
	}

	public boolean isEntityAlive() {
		return !isDead;
	}

	public boolean isEntityInsideOpaqueBlock() {
		for (int i = 0; i < 8; ++i) {
			float f = ((i >> 0) % 2 - 0.5F) * width * 0.8F;
			float f1 = ((i >> 1) % 2 - 0.5F) * 0.1F;
			float f2 = ((i >> 2) % 2 - 0.5F) * width * 0.8F;
			int j = MathHelper.floor_double(posX + f);
			int k = MathHelper.floor_double(posY + getEyeHeight() + f1);
			int l = MathHelper.floor_double(posZ + f2);

			if (worldObj.getBlock(j, k, l).isNormalCube())
				return true;
		}

		return false;
	}

	public boolean interactFirst(EntityPlayer p_130002_1_) {
		return false;
	}

	public AxisAlignedBB getCollisionBox(Entity p_70114_1_) {
		return null;
	}

	public void updateRidden() {
		if (ridingEntity.isDead) {
			ridingEntity = null;
		} else {
			motionX = 0.0D;
			motionY = 0.0D;
			motionZ = 0.0D;
			onUpdate();

			if (ridingEntity != null) {
				ridingEntity.updateRiderPosition();
				entityRiderYawDelta += ridingEntity.rotationYaw - ridingEntity.prevRotationYaw;

				for (entityRiderPitchDelta += ridingEntity.rotationPitch
						- ridingEntity.prevRotationPitch; entityRiderYawDelta >= 180.0D; entityRiderYawDelta -= 360.0D) {
				}

				while (entityRiderYawDelta < -180.0D) {
					entityRiderYawDelta += 360.0D;
				}

				while (entityRiderPitchDelta >= 180.0D) {
					entityRiderPitchDelta -= 360.0D;
				}

				while (entityRiderPitchDelta < -180.0D) {
					entityRiderPitchDelta += 360.0D;
				}

				double d0 = entityRiderYawDelta * 0.5D;
				double d1 = entityRiderPitchDelta * 0.5D;
				float f = 10.0F;

				if (d0 > f) {
					d0 = f;
				}

				if (d0 < -f) {
					d0 = -f;
				}

				if (d1 > f) {
					d1 = f;
				}

				if (d1 < -f) {
					d1 = -f;
				}

				entityRiderYawDelta -= d0;
				entityRiderPitchDelta -= d1;
			}
		}
	}

	public void updateRiderPosition() {
		if (riddenByEntity != null) {
			riddenByEntity.setPosition(posX, posY + getMountedYOffset() + riddenByEntity.getYOffset(), posZ);
		}
	}

	public double getYOffset() {
		return yOffset;
	}

	public double getMountedYOffset() {
		return height * 0.75D;
	}

	public void mountEntity(Entity entity) {
		setPassengerOf(entity);
	}

	@SideOnly(Side.CLIENT)
	public void setPositionAndRotation2(double p_70056_1_, double p_70056_3_, double p_70056_5_, float p_70056_7_,
			float p_70056_8_, int p_70056_9_) {
		setPosition(p_70056_1_, p_70056_3_, p_70056_5_);
		setRotation(p_70056_7_, p_70056_8_);
		List list = worldObj.getCollidingBoundingBoxes(this, boundingBox.contract(0.03125D, 0.0D, 0.03125D));

		if (!list.isEmpty()) {
			double d3 = 0.0D;

			for (int j = 0; j < list.size(); ++j) {
				AxisAlignedBB axisalignedbb = (AxisAlignedBB) list.get(j);

				if (axisalignedbb.maxY > d3) {
					d3 = axisalignedbb.maxY;
				}
			}

			p_70056_3_ += d3 - boundingBox.minY;
			setPosition(p_70056_1_, p_70056_3_, p_70056_5_);
		}
	}

	public float getCollisionBorderSize() {
		return 0.1F;
	}

	public Vec3 getLookVec() {
		return null;
	}

	public void setInPortal() {
		if (timeUntilPortal > 0) {
			timeUntilPortal = getPortalCooldown();
		} else {
			double d0 = prevPosX - posX;
			double d1 = prevPosZ - posZ;

			if (!worldObj.isRemote && !inPortal) {
				teleportDirection = Direction.getMovementDirection(d0, d1);
			}

			inPortal = true;
		}
	}

	public int getPortalCooldown() {
		return 300;
	}

	@SideOnly(Side.CLIENT)
	public void setVelocity(double p_70016_1_, double p_70016_3_, double p_70016_5_) {
		motionX = p_70016_1_;
		motionY = p_70016_3_;
		motionZ = p_70016_5_;
	}

	@SideOnly(Side.CLIENT)
	public void handleHealthUpdate(byte p_70103_1_) {
	}

	@SideOnly(Side.CLIENT)
	public void performHurtAnimation() {
	}

	public ItemStack[] getLastActiveItems() {
		return null;
	}

	public void setCurrentItemOrArmor(int p_70062_1_, ItemStack p_70062_2_) {
	}

	public boolean isBurning() {
		boolean flag = worldObj != null && worldObj.isRemote;
		return !isImmuneToFire && (fire > 0 || flag && getFlag(0));
	}

	public boolean isRiding() {
		return ridingEntity != null && ridingEntity.shouldRiderSit();
	}

	public boolean isSneaking() {
		return getFlag(1);
	}

	public void setSneaking(boolean p_70095_1_) {
		setFlag(1, p_70095_1_);
	}

	public boolean isSprinting() {
		return getFlag(3);
	}

	public void setSprinting(boolean p_70031_1_) {
		setFlag(3, p_70031_1_);
	}

	public boolean isInvisible() {
		return getFlag(5);
	}

	@SideOnly(Side.CLIENT)
	public boolean isInvisibleToPlayer(EntityPlayer p_98034_1_) {
		return isInvisible();
	}

	public void setInvisible(boolean p_82142_1_) {
		setFlag(5, p_82142_1_);
	}

	@SideOnly(Side.CLIENT)
	public boolean isEating() {
		return getFlag(4);
	}

	public void setEating(boolean p_70019_1_) {
		setFlag(4, p_70019_1_);
	}

	protected boolean getFlag(int p_70083_1_) {
		return (dataWatcher.getWatchableObjectByte(0) & 1 << p_70083_1_) != 0;
	}

	protected void setFlag(int p_70052_1_, boolean p_70052_2_) {
		byte b0 = dataWatcher.getWatchableObjectByte(0);

		if (p_70052_2_) {
			dataWatcher.updateObject(0, Byte.valueOf((byte) (b0 | 1 << p_70052_1_)));
		} else {
			dataWatcher.updateObject(0, Byte.valueOf((byte) (b0 & ~(1 << p_70052_1_))));
		}
	}

	public int getAir() {
		return dataWatcher.getWatchableObjectShort(1);
	}

	public void setAir(int p_70050_1_) {
		dataWatcher.updateObject(1, Short.valueOf((short) p_70050_1_));
	}

	public void onStruckByLightning(EntityLightningBolt p_70077_1_) {
		dealFireDamage(5);
		++fire;

		if (fire == 0) {
			setFire(8);
		}
	}

	public void onKillEntity(EntityLivingBase p_70074_1_) {
	}

	protected boolean func_145771_j(double p_145771_1_, double p_145771_3_, double p_145771_5_) {
		int i = MathHelper.floor_double(p_145771_1_);
		int j = MathHelper.floor_double(p_145771_3_);
		int k = MathHelper.floor_double(p_145771_5_);
		double d3 = p_145771_1_ - i;
		double d4 = p_145771_3_ - j;
		double d5 = p_145771_5_ - k;
		List list = worldObj.func_147461_a(boundingBox);

		if (list.isEmpty() && !worldObj.func_147469_q(i, j, k))
			return false;
		else {
			boolean flag = !worldObj.func_147469_q(i - 1, j, k);
			boolean flag1 = !worldObj.func_147469_q(i + 1, j, k);
			worldObj.func_147469_q(i, j - 1, k);
			boolean flag3 = !worldObj.func_147469_q(i, j + 1, k);
			boolean flag4 = !worldObj.func_147469_q(i, j, k - 1);
			boolean flag5 = !worldObj.func_147469_q(i, j, k + 1);
			byte b0 = 3;
			double d6 = 9999.0D;

			if (flag && d3 < d6) {
				d6 = d3;
				b0 = 0;
			}

			if (flag1 && 1.0D - d3 < d6) {
				d6 = 1.0D - d3;
				b0 = 1;
			}

			if (flag3 && 1.0D - d4 < d6) {
				d6 = 1.0D - d4;
				b0 = 3;
			}

			if (flag4 && d5 < d6) {
				d6 = d5;
				b0 = 4;
			}

			if (flag5 && 1.0D - d5 < d6) {
				d6 = 1.0D - d5;
				b0 = 5;
			}

			float f = rand.nextFloat() * 0.2F + 0.1F;

			if (b0 == 0) {
				motionX = -f;
			}

			if (b0 == 1) {
				motionX = f;
			}

			if (b0 == 2) {
				motionY = -f;
			}

			if (b0 == 3) {
				motionY = f;
			}

			if (b0 == 4) {
				motionZ = -f;
			}

			if (b0 == 5) {
				motionZ = f;
			}

			return true;
		}
	}

	public void setInWeb() {
		isInWeb = true;
		fallDistance = 0.0F;
	}

	public String getCommandSenderName() {
		String s = EntityList.getEntityString(this);

		if (s == null) {
			s = "generic";
		}

		return StatCollector.translateToLocal("entity." + s + ".name");
	}

	public Entity[] getParts() {
		return null;
	}

	public boolean isEntityEqual(Entity p_70028_1_) {
		return this == p_70028_1_;
	}

	public float getRotationYawHead() {
		return 0.0F;
	}

	@SideOnly(Side.CLIENT)
	public void setRotationYawHead(float p_70034_1_) {
	}

	public boolean canAttackWithItem() {
		return true;
	}

	public boolean hitByEntity(Entity p_85031_1_) {
		return false;
	}

	@Override
	public String toString() {
		return String.format("%s['%s'/%d, l='%s', x=%.2f, y=%.2f, z=%.2f]",
				this.getClass().getSimpleName(), getCommandSenderName(), Integer.valueOf(entityId),
				worldObj == null ? "~NULL~" : worldObj.getWorldInfo().getWorldName(), Double.valueOf(posX),
				Double.valueOf(posY), Double.valueOf(posZ));
	}

	public boolean isEntityInvulnerable() {
		return invulnerable;
	}

	public void copyLocationAndAnglesFrom(Entity p_82149_1_) {
		setLocationAndAngles(p_82149_1_.posX, p_82149_1_.posY, p_82149_1_.posZ, p_82149_1_.rotationYaw,
				p_82149_1_.rotationPitch);
	}

	public void copyDataFrom(Entity p_82141_1_, boolean p_82141_2_) {
		NBTTagCompound nbttagcompound = new NBTTagCompound();
		p_82141_1_.writeToNBT(nbttagcompound);
		readFromNBT(nbttagcompound);
		timeUntilPortal = p_82141_1_.timeUntilPortal;
		teleportDirection = p_82141_1_.teleportDirection;
	}

	public void travelToDimension(int p_71027_1_) {

		if (!worldObj.isRemote && !isDead) {
			worldObj.theProfiler.startSection("changeDimension");
			MinecraftServer minecraftserver = MinecraftServer.getServer();
			// CraftBukkit start - Move logic into new function "teleportToLocation"
			// int j = this.dimension;
			// Cauldron start - Allow Forge hotloading on teleport
			WorldServer exitWorld = minecraftserver.worldServerForDimension(p_71027_1_);
			Location enter = getBukkitEntity().getLocation();
			Location exit = exitWorld != null ? minecraftserver.getConfigurationManager().calculateTarget(enter,
					minecraftserver.worldServerForDimension(p_71027_1_)) : null;
			boolean useTravelAgent = exitWorld != null && !(dimension == 1 && exitWorld.provider.dimensionId == 1); // don't
																													// use
																													// agent
																													// for
																													// custom
																													// worlds
																													// or
																													// return
																													// from
																													// THE_END
			Teleporter teleporter = exit != null ? ((CraftWorld) exit.getWorld()).getHandle().getDefaultTeleporter()
					: null;
			new CraftTravelAgent(((CraftWorld) exit.getWorld()).getHandle());
			TravelAgent agent = teleporter != null && teleporter instanceof TravelAgent ? (TravelAgent) teleporter
					: CraftTravelAgent.DEFAULT; // return arbitrary TA
												// to compensate for
												// implementation
												// dependent plugins
			EntityPortalEvent event = new EntityPortalEvent(getBukkitEntity(), enter, exit, agent);
			event.useTravelAgent(useTravelAgent);
			Bukkit.getPluginManager().callEvent(event);
			if (event.isCancelled() || event.getTo() == null || isDead)
				return;
			exit = event.useTravelAgent() ? event.getPortalTravelAgent().findOrCreate(event.getTo()) : event.getTo();
			teleportTo(exit, true);
		}
	}

	public float func_145772_a(Explosion p_145772_1_, World p_145772_2_, int p_145772_3_, int p_145772_4_,
			int p_145772_5_, Block p_145772_6_) {
		return p_145772_6_.getExplosionResistance(this, p_145772_2_, p_145772_3_, p_145772_4_, p_145772_5_, posX,
				posY + getEyeHeight(), posZ);
	}

	public boolean func_145774_a(Explosion p_145774_1_, World p_145774_2_, int p_145774_3_, int p_145774_4_,
			int p_145774_5_, Block p_145774_6_, float p_145774_7_) {
		return true;
	}

	public int getMaxSafePointTries() {
		return 3;
	}

	public int getTeleportDirection() {
		return teleportDirection;
	}

	public boolean doesEntityNotTriggerPressurePlate() {
		return false;
	}

	public void addEntityCrashInfo(CrashReportCategory p_85029_1_) {
		p_85029_1_.addCrashSectionCallable("Entity Type", new Callable() {
			private static final String __OBFID = "CL_00001534";

			@Override
			public String call() {
				return EntityList.getEntityString(Entity.this) + " (" + Entity.this.getClass().getCanonicalName() + ")";
			}
		});
		p_85029_1_.addCrashSection("Entity ID", Integer.valueOf(entityId));
		p_85029_1_.addCrashSectionCallable("Entity Name", new Callable() {
			private static final String __OBFID = "CL_00001535";

			@Override
			public String call() {
				return Entity.this.getCommandSenderName();
			}
		});
		p_85029_1_.addCrashSection("Entity's Exact location", String.format("%.2f, %.2f, %.2f",
				Double.valueOf(posX), Double.valueOf(posY), Double.valueOf(posZ)));
		p_85029_1_.addCrashSection("Entity's Block location", CrashReportCategory.getLocationInfo(
				MathHelper.floor_double(posX), MathHelper.floor_double(posY), MathHelper.floor_double(posZ)));
		p_85029_1_.addCrashSection("Entity's Momentum", String.format("%.2f, %.2f, %.2f",
				Double.valueOf(motionX), Double.valueOf(motionY), Double.valueOf(motionZ)));
	}

	@SideOnly(Side.CLIENT)
	public boolean canRenderOnFire() {
		return isBurning();
	}

	public UUID getUniqueID() {
		return entityUniqueID;
	}

	public boolean isPushedByWater() {
		return true;
	}

	public IChatComponent func_145748_c_() {
		return new ChatComponentText(getCommandSenderName());
	}

	public void func_145781_i(int p_145781_1_) {
	}

	/*
	 * ================================== Forge Start
	 * =====================================
	 */
	/**
	 * Returns a NBTTagCompound that can be used to store custom data for this
	 * entity. It will be written, and read from disc, so it persists over world
	 * saves.
	 *
	 * @return A NBTTagCompound
	 */
	public NBTTagCompound getEntityData() {
		if (customEntityData == null) {
			customEntityData = new NBTTagCompound();
		}
		return customEntityData;
	}

	/**
	 * Used in model rendering to determine if the entity riding this entity should
	 * be in the 'sitting' position.
	 *
	 * @return false to prevent an entity that is mounted to this entity from
	 *         displaying the 'sitting' animation.
	 */
	public boolean shouldRiderSit() {
		return true;
	}

	/**
	 * Called when a user uses the creative pick block button on this entity.
	 *
	 * @param target
	 *            The full target the player is looking at
	 * @return A ItemStack to add to the player's inventory, Null if nothing should
	 *         be added.
	 */
	public ItemStack getPickedResult(MovingObjectPosition target) {
		if (this instanceof EntityPainting)
			return new ItemStack(Items.painting);
		else if (this instanceof EntityLeashKnot)
			return new ItemStack(Items.lead);
		else if (this instanceof EntityItemFrame) {
			ItemStack held = ((EntityItemFrame) this).getDisplayedItem();
			if (held == null)
				return new ItemStack(Items.item_frame);
			else
				return held.copy();
		} else if (this instanceof EntityMinecart)
			return ((EntityMinecart) this).getCartItem();
		else if (this instanceof EntityBoat)
			return new ItemStack(Items.boat);
		else {
			int id = EntityList.getEntityID(this);
			if (id > 0 && EntityList.entityEggs.containsKey(id))
				return new ItemStack(Items.spawn_egg, 1, id);
		}
		return null;
	}

	public UUID getPersistentID() {
		return entityUniqueID;
	}

	/**
	 * Reset the entity ID to a new value. Not to be used from Mod code
	 */
	public final void resetEntityId() {
		entityId = nextEntityID++;
	}

	public boolean shouldRenderInPass(int pass) {
		return pass == 0;
	}

	/**
	 * Returns true if the entity is of the @link{EnumCreatureType} provided
	 *
	 * @param type
	 *            The EnumCreatureType type this entity is evaluating
	 * @param forSpawnCount
	 *            If this is being invoked to check spawn count caps.
	 * @return If the creature is of the type provided
	 */
	public boolean isCreatureType(EnumCreatureType type, boolean forSpawnCount) {
		return type.getCreatureClass().isAssignableFrom(this.getClass());
	}

	/**
	 * Register the instance of IExtendedProperties into the entity's collection.
	 *
	 * @param identifier
	 *            The identifier which you can use to retrieve these properties for
	 *            the entity.
	 * @param properties
	 *            The instanceof IExtendedProperties to register
	 * @return The identifier that was used to register the extended properties.
	 *         Empty String indicates an error. If your requested key already
	 *         existed, this will return a modified one that is unique.
	 */
	public String registerExtendedProperties(String identifier, IExtendedEntityProperties properties) {
		if (identifier == null) {
			FMLLog.warning(
					"Someone is attempting to register extended properties using a null identifier.  This is not allowed.  Aborting.  This may have caused instability.");
			return "";
		}
		if (properties == null) {
			FMLLog.warning(
					"Someone is attempting to register null extended properties.  This is not allowed.  Aborting.  This may have caused instability.");
			return "";
		}

		String baseIdentifier = identifier;
		int identifierModCount = 1;
		while (extendedProperties.containsKey(identifier)) {
			identifier = String.format("%s%d", baseIdentifier, identifierModCount++);
		}

		if (baseIdentifier != identifier) {
			FMLLog.info(
					"An attempt was made to register exended properties using an existing key.  The duplicate identifier (%s) has been remapped to %s.",
					baseIdentifier, identifier);
		}

		extendedProperties.put(identifier, properties);
		return identifier;
	}

	/**
	 * Gets the extended properties identified by the passed in key
	 *
	 * @param identifier
	 *            The key that identifies the extended properties.
	 * @return The instance of IExtendedProperties that was found, or null.
	 */
	public IExtendedEntityProperties getExtendedProperties(String identifier) {
		return extendedProperties.get(identifier);
	}

	/**
	 * If a rider of this entity can interact with this entity. Should return true
	 * on the ridden entity if so.
	 *
	 * @return if the entity can be interacted with from a rider
	 */
	public boolean canRiderInteract() {
		return false;
	}

	/**
	 * If the rider should be dismounted from the entity when the entity goes under
	 * water
	 *
	 * @param rider
	 *            The entity that is riding
	 * @return if the entity should be dismounted when under water
	 */
	public boolean shouldDismountInWater(Entity rider) {
		return this instanceof EntityLivingBase;
	}
	/*
	 * ================================== Forge End
	 * =====================================
	 */

	public enum EnumEntitySize {
		SIZE_1, SIZE_2, SIZE_3, SIZE_4, SIZE_5, SIZE_6;

		private static final String __OBFID = "CL_00001537";

		public int multiplyBy32AndRound(double p_75630_1_) {
			double d1 = p_75630_1_ - (MathHelper.floor_double(p_75630_1_) + 0.5D);

			switch (Entity.SwitchEnumEntitySize.field_96565_a[ordinal()]) {
			case 1:
				if (d1 < 0.0D) {
					if (d1 < -0.3125D)
						return MathHelper.ceiling_double_int(p_75630_1_ * 32.0D);
				} else if (d1 < 0.3125D)
					return MathHelper.ceiling_double_int(p_75630_1_ * 32.0D);

				return MathHelper.floor_double(p_75630_1_ * 32.0D);
			case 2:
				if (d1 < 0.0D) {
					if (d1 < -0.3125D)
						return MathHelper.floor_double(p_75630_1_ * 32.0D);
				} else if (d1 < 0.3125D)
					return MathHelper.floor_double(p_75630_1_ * 32.0D);

				return MathHelper.ceiling_double_int(p_75630_1_ * 32.0D);
			case 3:
				if (d1 > 0.0D)
					return MathHelper.floor_double(p_75630_1_ * 32.0D);

				return MathHelper.ceiling_double_int(p_75630_1_ * 32.0D);
			case 4:
				if (d1 < 0.0D) {
					if (d1 < -0.1875D)
						return MathHelper.ceiling_double_int(p_75630_1_ * 32.0D);
				} else if (d1 < 0.1875D)
					return MathHelper.ceiling_double_int(p_75630_1_ * 32.0D);

				return MathHelper.floor_double(p_75630_1_ * 32.0D);
			case 5:
				if (d1 < 0.0D) {
					if (d1 < -0.1875D)
						return MathHelper.floor_double(p_75630_1_ * 32.0D);
				} else if (d1 < 0.1875D)
					return MathHelper.floor_double(p_75630_1_ * 32.0D);

				return MathHelper.ceiling_double_int(p_75630_1_ * 32.0D);
			case 6:
			default:
				if (d1 > 0.0D)
					return MathHelper.ceiling_double_int(p_75630_1_ * 32.0D);
				else
					return MathHelper.floor_double(p_75630_1_ * 32.0D);
			}
		}
	}

	static final class SwitchEnumEntitySize {
		static final int[] field_96565_a = new int[Entity.EnumEntitySize.values().length];
		private static final String __OBFID = "CL_00001536";

		static {
			try {
				field_96565_a[Entity.EnumEntitySize.SIZE_1.ordinal()] = 1;
			} catch (NoSuchFieldError var6) {
			}

			try {
				field_96565_a[Entity.EnumEntitySize.SIZE_2.ordinal()] = 2;
			} catch (NoSuchFieldError var5) {
			}

			try {
				field_96565_a[Entity.EnumEntitySize.SIZE_3.ordinal()] = 3;
			} catch (NoSuchFieldError var4) {
			}

			try {
				field_96565_a[Entity.EnumEntitySize.SIZE_4.ordinal()] = 4;
			} catch (NoSuchFieldError var3) {
			}

			try {
				field_96565_a[Entity.EnumEntitySize.SIZE_5.ordinal()] = 5;
			} catch (NoSuchFieldError var2) {
			}

			try {
				field_96565_a[Entity.EnumEntitySize.SIZE_6.ordinal()] = 6;
			} catch (NoSuchFieldError var1) {
			}
		}
	}

	/*
	 * ===================================== ULTRAMINE START
	 * =====================================
	 */

	private EntityType cachedEntityType;
	private GameProfile owner;
	public boolean removeThisTick;

	public final void setObjectOwner(GameProfile owner) {
		if (this.owner == null) {
			this.owner = owner;
		}
	}

	public final GameProfile getObjectOwner() {
		return owner;
	}

	public boolean isEntityLiving() {
		return false;
	}

	public boolean isEntityPlayer() {
		return false;
	}

	public boolean isEntityPlayerMP() {
		return false;
	}

	protected EntityType computeEntityType() {
		return isCreatureType(EnumCreatureType.monster, false) ? EntityType.MONSTER
				: isCreatureType(EnumCreatureType.creature, false) ? EntityType.ANIMAL
						: isCreatureType(EnumCreatureType.ambient, false) ? EntityType.AMBIENT
								: isCreatureType(EnumCreatureType.waterCreature, false) ? EntityType.WATER
										: isCreatureType(EnumCreatureType.monster, true) ? EntityType.MONSTER
												: isCreatureType(EnumCreatureType.creature, true) ? EntityType.ANIMAL
														: isCreatureType(EnumCreatureType.ambient, true)
																? EntityType.AMBIENT
																: isCreatureType(EnumCreatureType.waterCreature, true)
																		? EntityType.WATER
																		: EntityType.OTHER;
	}

	public final EntityType getEntityType() {
		if (cachedEntityType == null)
			return cachedEntityType = computeEntityType();
		return cachedEntityType;
	}

	public final boolean isEntityMonster() {
		return getEntityType() == EntityType.MONSTER;
	}

	public final boolean isEntityAnimal() {
		return getEntityType() == EntityType.ANIMAL;
	}

	public final boolean isEntityAmbient() {
		return getEntityType() == EntityType.AMBIENT;
	}

	public final boolean isEntityWater() {
		return getEntityType() == EntityType.WATER;
	}

	public final boolean isEntityItem() {
		return getEntityType() == EntityType.ITEM;
	}

	public final boolean isEntityXPOrb() {
		return getEntityType() == EntityType.XP_ORB;
	}

	public double getEntityDespawnDistance() {
		return 9216d;// 16384.0d;
	}

	public void updateInactive() {

	}

	public int getFireTicks() {
		return fire;
	}

	public void setFireTicks(int ticks) {
		fire = ticks;
	}

	public CraftEntity getBukkitEntity() {
		if (bukkitEntity == null) {
			bukkitEntity = CraftEntity.getEntity(worldObj.getServer(), this);
		}
		return bukkitEntity;
	}

	public void setBukkitEntity(CraftEntity craftEntity) {
		bukkitEntity = craftEntity;
	}

	public ProjectileSource getProjectileSource() {
		return projectileSource;
	}

	public String getSpawnReason() {
		return spawnReason;
	}

	public void setSpawnReason(String spawnReason) {
		this.spawnReason = spawnReason;
	}

	public void setProjectileSource(ProjectileSource projectileSource) {
		this.projectileSource = projectileSource;
	}

	public void setPassengerOf(Entity entity) {
		// mountEntity(null) doesn't really fly for overloaded methods,
		// so this method is needed
		Entity originalVehicle = ridingEntity;
		Entity originalPassenger = ridingEntity == null ? null : ridingEntity.riddenByEntity;
		PluginManager pluginManager = Bukkit.getPluginManager();
		getBukkitEntity(); // make sure bukkitEntity is initialised
		// CraftBukkit end
		entityRiderPitchDelta = 0.0D;
		entityRiderYawDelta = 0.0D;

		if (entity == null) {
			if (ridingEntity != null) {
				// CraftBukkit start
				if (bukkitEntity instanceof LivingEntity && ridingEntity.getBukkitEntity() instanceof Vehicle) {
					VehicleExitEvent event = new VehicleExitEvent((Vehicle) ridingEntity.getBukkitEntity(),
							(LivingEntity) bukkitEntity);
					pluginManager.callEvent(event);

					if (event.isCancelled() || ridingEntity != originalVehicle)
						return;
				}

				// CraftBukkit end
				pluginManager.callEvent(new org.spigotmc.event.entity.EntityDismountEvent(getBukkitEntity(),
						ridingEntity.getBukkitEntity())); // Spigot
				setLocationAndAngles(ridingEntity.posX, ridingEntity.boundingBox.minY + ridingEntity.height,
						ridingEntity.posZ, rotationYaw, rotationPitch);
				ridingEntity.riddenByEntity = null;
			}

			ridingEntity = null;
		} else {
			// CraftBukkit start
			if (bukkitEntity instanceof LivingEntity && entity.getBukkitEntity() instanceof Vehicle
					&& entity.worldObj.chunkExists((int) entity.posX >> 4, (int) entity.posZ >> 4)) {
				// It's possible to move from one vehicle to another. We need to check if
				// they're already in a vehicle, and fire an exit event if they are.
				VehicleExitEvent exitEvent = null;

				if (ridingEntity != null && ridingEntity.getBukkitEntity() instanceof Vehicle) {
					exitEvent = new VehicleExitEvent((Vehicle) ridingEntity.getBukkitEntity(),
							(LivingEntity) bukkitEntity);
					pluginManager.callEvent(exitEvent);

					if (exitEvent.isCancelled() || ridingEntity != originalVehicle
							|| ridingEntity != null && ridingEntity.riddenByEntity != originalPassenger)
						return;
				}

				VehicleEnterEvent event = new VehicleEnterEvent((Vehicle) entity.getBukkitEntity(), bukkitEntity);
				pluginManager.callEvent(event);

				// If a plugin messes with the vehicle or the vehicle's passenger
				if (event.isCancelled() || ridingEntity != originalVehicle
						|| ridingEntity != null && ridingEntity.riddenByEntity != originalPassenger) {
					// If we only cancelled the enterevent then we need to put the player in a
					// decent position.
					if (exitEvent != null && ridingEntity == originalVehicle && ridingEntity != null
							&& ridingEntity.riddenByEntity == originalPassenger) {
						setLocationAndAngles(ridingEntity.posX, ridingEntity.boundingBox.minY + ridingEntity.height,
								ridingEntity.posZ, rotationYaw, rotationPitch);
						ridingEntity.riddenByEntity = null;
						ridingEntity = null;
					}

					return;
				}
			}

			// CraftBukkit end
			// Spigot Start
			if (entity.worldObj.chunkExists((int) entity.posX >> 4, (int) entity.posZ >> 4)) {
				org.spigotmc.event.entity.EntityMountEvent event = new org.spigotmc.event.entity.EntityMountEvent(
						getBukkitEntity(), entity.getBukkitEntity());
				pluginManager.callEvent(event);

				if (event.isCancelled())
					return;
			}

			// Spigot End

			if (ridingEntity != null) {
				ridingEntity.riddenByEntity = null;
			}

			ridingEntity = entity;
			entity.riddenByEntity = this;
		}
	}

	// public void onStruckByLightning(EntityLightningBolt entity)
	// {
	// // CraftBukkit start
	// final org.bukkit.entity.Entity thisBukkitEntity = this.getBukkitEntity();
	// if(thisBukkitEntity == null) return; // Cauldron - skip mod entities with no
	// wrapper (TODO: create a wrapper)
	// if(entity == null) return; // Cauldron - skip null entities, see #392
	// final org.bukkit.entity.Entity stormBukkitEntity = ((IMixinEntity)
	// entity).getBukkitEntity();
	// if(stormBukkitEntity == null) return; // Cauldron - skip mod entities with no
	// wrapper (TODO: create a wrapper)
	// final PluginManager pluginManager = Bukkit.getPluginManager();
	//
	// if(thisBukkitEntity instanceof Hanging)
	// {
	// HangingBreakByEntityEvent hangingEvent = new
	// HangingBreakByEntityEvent((Hanging) thisBukkitEntity, stormBukkitEntity);
	// PaintingBreakByEntityEvent paintingEvent = null;
	//
	// if(thisBukkitEntity instanceof Painting)
	// {
	// paintingEvent = new PaintingBreakByEntityEvent((Painting) thisBukkitEntity,
	// stormBukkitEntity);
	// }
	//
	// pluginManager.callEvent(hangingEvent);
	//
	// if(paintingEvent != null)
	// {
	// paintingEvent.setCancelled(hangingEvent.isCancelled());
	// pluginManager.callEvent(paintingEvent);
	// }
	//
	// if(hangingEvent.isCancelled() || (paintingEvent != null &&
	// paintingEvent.isCancelled()))
	// {
	// return;
	// }
	// }
	//
	// if(this.isImmuneToFire)
	// {
	// return;
	// }
	// CraftEventFactory.entityDamage = entity;
	// if(!this.attackEntityFrom(DamageSource.inFire, 5.0F))
	// {
	// CraftEventFactory.entityDamage = null;
	// return;
	// }
	//
	// // CraftBukkit end
	// ++this.fire;
	//
	// if(this.fire == 0)
	// {
	// // CraftBukkit start - Call a combust event when lightning strikes
	// EntityCombustByEntityEvent entityCombustEvent = new
	// EntityCombustByEntityEvent(stormBukkitEntity, thisBukkitEntity, 8);
	// pluginManager.callEvent(entityCombustEvent);
	//
	// if(!entityCombustEvent.isCancelled())
	// {
	// this.setFire(entityCombustEvent.getDuration());
	// }
	//
	// // CraftBukkit end
	// }
	// }

	@Nullable
	private org.bukkit.block.Block getMaterialInBB(AxisAlignedBB p_72875_1_,
			net.minecraft.block.material.Material material) {
		int minX = MathHelper.floor_double(p_72875_1_.minX);
		int maxX = MathHelper.floor_double(p_72875_1_.maxX + 1.0D);
		int minY = MathHelper.floor_double(p_72875_1_.minY);
		int maxY = MathHelper.floor_double(p_72875_1_.maxY + 1.0D);
		int minZ = MathHelper.floor_double(p_72875_1_.minZ);
		int maxZ = MathHelper.floor_double(p_72875_1_.maxZ + 1.0D);
		for (int x = minX; x < maxX; ++x) {
			for (int y = minY; y < maxY; ++y) {
				for (int z = minZ; z < maxZ; ++z)
					if (worldObj.getBlock(x, y, z).getMaterial() == material)
						return worldObj.getWorld().getBlockAt(x, y, z);
			}
		}
		return null;
	}

	public void teleportTo(Location exit, boolean portal) {
		WorldServer worldserver = ((CraftWorld) getBukkitEntity().getLocation().getWorld()).getHandle();
		WorldServer worldserver1 = ((CraftWorld) exit.getWorld()).getHandle();
		dimension = worldserver1.provider.dimensionId;
		worldObj.removeEntity(this);
		// Thermos silently remove the entity so it can be magically transported to
		// another world :D
		if (!(this instanceof EntityLivingBase) && addedToChunk && worldObj.chunkExists(chunkCoordX, chunkCoordZ)) {
			worldObj.getChunkFromChunkCoords(chunkCoordX, chunkCoordZ).removeEntity(this);
			worldObj.loadedEntityList.remove(this);
		}
		isDead = false;
		worldObj.theProfiler.startSection("reposition");
		// CraftBukkit start - Ensure chunks are loaded in case TravelAgent is not used
		// which would initially cause chunks to load during find/create
		// minecraftserver.getPlayerList().a(this, j, worldserver, worldserver1);
		boolean before = worldserver1.theChunkProviderServer.loadChunkOnProvideRequest; // Cauldron start - load chunks
																						// on provide request
		worldserver1.theChunkProviderServer.loadChunkOnProvideRequest = true;
		worldserver1.func_73046_m().getConfigurationManager().repositionEntity(this, exit, portal);
		worldserver1.theChunkProviderServer.loadChunkOnProvideRequest = before; // Cauldron end
		// CraftBukkit end
		worldObj.theProfiler.endStartSection("reloading");
		Entity entity = EntityList.createEntityByName(EntityList.getEntityString(this), worldserver1);
		if (entity != null) {
			entity.copyDataFrom(this, true);
			worldserver1.spawnEntityInWorld(entity);
			// CraftBukkit start - Forward the CraftEntity to the new entity
			getBukkitEntity().setHandle(entity);
			entity.setBukkitEntity(getBukkitEntity());
			// CraftBukkit end
		}
		isDead = true;
		if (this instanceof EntityItem) // Thermos kill this stack to avoid dupe glitch?
		{
			ItemStack stack = dataWatcher.getWatchableObjectItemStack(10);
			stack.stackSize = 0;
		}
		worldObj.theProfiler.endSection();
		worldserver.resetUpdateEntityTick();
		worldserver1.resetUpdateEntityTick();
		worldObj.theProfiler.endSection();
	}
}
