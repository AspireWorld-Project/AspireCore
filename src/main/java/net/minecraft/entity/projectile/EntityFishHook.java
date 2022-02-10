package net.minecraft.entity.projectile;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.entity.Fish;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerFishEvent;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemFishFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.WeightedRandomFishable;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class EntityFishHook extends Entity {
	public static final List field_146039_d = Arrays.asList(new WeightedRandomFishable[] {
			new WeightedRandomFishable(new ItemStack(Items.leather_boots), 10).func_150709_a(0.9F),
			new WeightedRandomFishable(new ItemStack(Items.leather), 10),
			new WeightedRandomFishable(new ItemStack(Items.bone), 10),
			new WeightedRandomFishable(new ItemStack(Items.potionitem), 10),
			new WeightedRandomFishable(new ItemStack(Items.string), 5),
			new WeightedRandomFishable(new ItemStack(Items.fishing_rod), 2).func_150709_a(0.9F),
			new WeightedRandomFishable(new ItemStack(Items.bowl), 10),
			new WeightedRandomFishable(new ItemStack(Items.stick), 5),
			new WeightedRandomFishable(new ItemStack(Items.dye, 10, 0), 1),
			new WeightedRandomFishable(new ItemStack(Blocks.tripwire_hook), 10),
			new WeightedRandomFishable(new ItemStack(Items.rotten_flesh), 10) });
	public static final List field_146041_e = Arrays
			.asList(new WeightedRandomFishable[] { new WeightedRandomFishable(new ItemStack(Blocks.waterlily), 1),
					new WeightedRandomFishable(new ItemStack(Items.name_tag), 1),
					new WeightedRandomFishable(new ItemStack(Items.saddle), 1),
					new WeightedRandomFishable(new ItemStack(Items.bow), 1).func_150709_a(0.25F).func_150707_a(),
					new WeightedRandomFishable(new ItemStack(Items.fishing_rod), 1).func_150709_a(0.25F)
							.func_150707_a(),
					new WeightedRandomFishable(new ItemStack(Items.book), 1).func_150707_a() });
	public static final List field_146036_f = Arrays.asList(new WeightedRandomFishable[] {
			new WeightedRandomFishable(new ItemStack(Items.fish, 1, ItemFishFood.FishType.COD.func_150976_a()), 60),
			new WeightedRandomFishable(new ItemStack(Items.fish, 1, ItemFishFood.FishType.SALMON.func_150976_a()), 25),
			new WeightedRandomFishable(new ItemStack(Items.fish, 1, ItemFishFood.FishType.CLOWNFISH.func_150976_a()),
					2),
			new WeightedRandomFishable(new ItemStack(Items.fish, 1, ItemFishFood.FishType.PUFFERFISH.func_150976_a()),
					13) });
	private int field_146037_g;
	private int field_146048_h;
	private int field_146050_i;
	private Block field_146046_j;
	private boolean field_146051_au;
	public int field_146044_a;
	public EntityPlayer field_146042_b;
	private int field_146049_av;
	private int field_146047_aw;
	private int field_146045_ax;
	private int field_146040_ay;
	private int field_146038_az;
	private float field_146054_aA;
	public Entity field_146043_c;
	private int field_146055_aB;
	private double field_146056_aC;
	private double field_146057_aD;
	private double field_146058_aE;
	private double field_146059_aF;
	private double field_146060_aG;
	@SideOnly(Side.CLIENT)
	private double field_146061_aH;
	@SideOnly(Side.CLIENT)
	private double field_146052_aI;
	@SideOnly(Side.CLIENT)
	private double field_146053_aJ;
	private static final String __OBFID = "CL_00001663";

	public EntityFishHook(World p_i1764_1_) {
		super(p_i1764_1_);
		field_146037_g = -1;
		field_146048_h = -1;
		field_146050_i = -1;
		setSize(0.25F, 0.25F);
		ignoreFrustumCheck = true;
	}

	@SideOnly(Side.CLIENT)
	public EntityFishHook(World p_i1765_1_, double p_i1765_2_, double p_i1765_4_, double p_i1765_6_,
			EntityPlayer p_i1765_8_) {
		this(p_i1765_1_);
		setPosition(p_i1765_2_, p_i1765_4_, p_i1765_6_);
		ignoreFrustumCheck = true;
		field_146042_b = p_i1765_8_;
		p_i1765_8_.fishEntity = this;
	}

	public EntityFishHook(World p_i1766_1_, EntityPlayer p_i1766_2_) {
		super(p_i1766_1_);
		field_146037_g = -1;
		field_146048_h = -1;
		field_146050_i = -1;
		ignoreFrustumCheck = true;
		field_146042_b = p_i1766_2_;
		field_146042_b.fishEntity = this;
		setSize(0.25F, 0.25F);
		setLocationAndAngles(p_i1766_2_.posX, p_i1766_2_.posY + 1.62D - p_i1766_2_.yOffset, p_i1766_2_.posZ,
				p_i1766_2_.rotationYaw, p_i1766_2_.rotationPitch);
		posX -= MathHelper.cos(rotationYaw / 180.0F * (float) Math.PI) * 0.16F;
		posY -= 0.10000000149011612D;
		posZ -= MathHelper.sin(rotationYaw / 180.0F * (float) Math.PI) * 0.16F;
		setPosition(posX, posY, posZ);
		yOffset = 0.0F;
		float f = 0.4F;
		motionX = -MathHelper.sin(rotationYaw / 180.0F * (float) Math.PI)
				* MathHelper.cos(rotationPitch / 180.0F * (float) Math.PI) * f;
		motionZ = MathHelper.cos(rotationYaw / 180.0F * (float) Math.PI)
				* MathHelper.cos(rotationPitch / 180.0F * (float) Math.PI) * f;
		motionY = -MathHelper.sin(rotationPitch / 180.0F * (float) Math.PI) * f;
		func_146035_c(motionX, motionY, motionZ, 1.5F, 1.0F);
	}

	@Override
	protected void entityInit() {
	}

	public void func_146035_c(double p_146035_1_, double p_146035_3_, double p_146035_5_, float p_146035_7_,
			float p_146035_8_) {
		float f2 = MathHelper
				.sqrt_double(p_146035_1_ * p_146035_1_ + p_146035_3_ * p_146035_3_ + p_146035_5_ * p_146035_5_);
		p_146035_1_ /= f2;
		p_146035_3_ /= f2;
		p_146035_5_ /= f2;
		p_146035_1_ += rand.nextGaussian() * 0.007499999832361937D * p_146035_8_;
		p_146035_3_ += rand.nextGaussian() * 0.007499999832361937D * p_146035_8_;
		p_146035_5_ += rand.nextGaussian() * 0.007499999832361937D * p_146035_8_;
		p_146035_1_ *= p_146035_7_;
		p_146035_3_ *= p_146035_7_;
		p_146035_5_ *= p_146035_7_;
		motionX = p_146035_1_;
		motionY = p_146035_3_;
		motionZ = p_146035_5_;
		float f3 = MathHelper.sqrt_double(p_146035_1_ * p_146035_1_ + p_146035_5_ * p_146035_5_);
		prevRotationYaw = rotationYaw = (float) (Math.atan2(p_146035_1_, p_146035_5_) * 180.0D / Math.PI);
		prevRotationPitch = rotationPitch = (float) (Math.atan2(p_146035_3_, f3) * 180.0D / Math.PI);
		field_146049_av = 0;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean isInRangeToRenderDist(double p_70112_1_) {
		double d1 = boundingBox.getAverageEdgeLength() * 4.0D;
		d1 *= 64.0D;
		return p_70112_1_ < d1 * d1;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void setPositionAndRotation2(double p_70056_1_, double p_70056_3_, double p_70056_5_, float p_70056_7_,
			float p_70056_8_, int p_70056_9_) {
		field_146056_aC = p_70056_1_;
		field_146057_aD = p_70056_3_;
		field_146058_aE = p_70056_5_;
		field_146059_aF = p_70056_7_;
		field_146060_aG = p_70056_8_;
		field_146055_aB = p_70056_9_;
		motionX = field_146061_aH;
		motionY = field_146052_aI;
		motionZ = field_146053_aJ;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void setVelocity(double p_70016_1_, double p_70016_3_, double p_70016_5_) {
		field_146061_aH = motionX = p_70016_1_;
		field_146052_aI = motionY = p_70016_3_;
		field_146053_aJ = motionZ = p_70016_5_;
	}

	@Override
	public void onUpdate() {
		super.onUpdate();

		if (field_146055_aB > 0) {
			double d7 = posX + (field_146056_aC - posX) / field_146055_aB;
			double d8 = posY + (field_146057_aD - posY) / field_146055_aB;
			double d9 = posZ + (field_146058_aE - posZ) / field_146055_aB;
			double d1 = MathHelper.wrapAngleTo180_double(field_146059_aF - rotationYaw);
			rotationYaw = (float) (rotationYaw + d1 / field_146055_aB);
			rotationPitch = (float) (rotationPitch + (field_146060_aG - rotationPitch) / field_146055_aB);
			--field_146055_aB;
			setPosition(d7, d8, d9);
			setRotation(rotationYaw, rotationPitch);
		} else {
			if (!worldObj.isRemote) {
				ItemStack itemstack = field_146042_b.getCurrentEquippedItem();

				if (field_146042_b.isDead || !field_146042_b.isEntityAlive() || itemstack == null
						|| itemstack.getItem() != Items.fishing_rod
						|| getDistanceSqToEntity(field_146042_b) > 1024.0D) {
					setDead();
					field_146042_b.fishEntity = null;
					return;
				}

				if (field_146043_c != null) {
					if (!field_146043_c.isDead) {
						posX = field_146043_c.posX;
						posY = field_146043_c.boundingBox.minY + field_146043_c.height * 0.8D;
						posZ = field_146043_c.posZ;
						return;
					}

					field_146043_c = null;
				}
			}

			if (field_146044_a > 0) {
				--field_146044_a;
			}

			if (field_146051_au) {
				if (worldObj.getBlock(field_146037_g, field_146048_h, field_146050_i) == field_146046_j) {
					++field_146049_av;

					if (field_146049_av == 1200) {
						setDead();
					}

					return;
				}

				field_146051_au = false;
				motionX *= rand.nextFloat() * 0.2F;
				motionY *= rand.nextFloat() * 0.2F;
				motionZ *= rand.nextFloat() * 0.2F;
				field_146049_av = 0;
				field_146047_aw = 0;
			} else {
				++field_146047_aw;
			}

			Vec3 vec31 = Vec3.createVectorHelper(posX, posY, posZ);
			Vec3 vec3 = Vec3.createVectorHelper(posX + motionX, posY + motionY, posZ + motionZ);
			MovingObjectPosition movingobjectposition = worldObj.rayTraceBlocks(vec31, vec3);
			vec31 = Vec3.createVectorHelper(posX, posY, posZ);
			vec3 = Vec3.createVectorHelper(posX + motionX, posY + motionY, posZ + motionZ);

			if (movingobjectposition != null) {
				vec3 = Vec3.createVectorHelper(movingobjectposition.hitVec.xCoord, movingobjectposition.hitVec.yCoord,
						movingobjectposition.hitVec.zCoord);
			}

			Entity entity = null;
			List list = worldObj.getEntitiesWithinAABBExcludingEntity(this,
					boundingBox.addCoord(motionX, motionY, motionZ).expand(1.0D, 1.0D, 1.0D));
			double d0 = 0.0D;
			double d2;

			for (int i = 0; i < list.size(); ++i) {
				Entity entity1 = (Entity) list.get(i);

				if (entity1.canBeCollidedWith() && (entity1 != field_146042_b || field_146047_aw >= 5)) {
					float f = 0.3F;
					AxisAlignedBB axisalignedbb = entity1.boundingBox.expand(f, f, f);
					MovingObjectPosition movingobjectposition1 = axisalignedbb.calculateIntercept(vec31, vec3);

					if (movingobjectposition1 != null) {
						d2 = vec31.distanceTo(movingobjectposition1.hitVec);

						if (d2 < d0 || d0 == 0.0D) {
							entity = entity1;
							d0 = d2;
						}
					}
				}
			}

			if (entity != null) {
				movingobjectposition = new MovingObjectPosition(entity);
			}

			if (movingobjectposition != null) {
				if (!isProjectileHitEventFired) {
					CraftEventFactory.callProjectileHitEvent(this);
					isProjectileHitEventFired = true;
				}
				if (movingobjectposition.entityHit != null) {
					if (movingobjectposition.entityHit
							.attackEntityFrom(DamageSource.causeThrownDamage(this, field_146042_b), 0.0F)) {
						field_146043_c = movingobjectposition.entityHit;
					}
				} else {
					field_146051_au = true;
				}
			}

			if (!field_146051_au) {
				moveEntity(motionX, motionY, motionZ);
				float f5 = MathHelper.sqrt_double(motionX * motionX + motionZ * motionZ);
				rotationYaw = (float) (Math.atan2(motionX, motionZ) * 180.0D / Math.PI);

				for (rotationPitch = (float) (Math.atan2(motionY, f5) * 180.0D / Math.PI); rotationPitch
						- prevRotationPitch < -180.0F; prevRotationPitch -= 360.0F) {
					;
				}

				while (rotationPitch - prevRotationPitch >= 180.0F) {
					prevRotationPitch += 360.0F;
				}

				while (rotationYaw - prevRotationYaw < -180.0F) {
					prevRotationYaw -= 360.0F;
				}

				while (rotationYaw - prevRotationYaw >= 180.0F) {
					prevRotationYaw += 360.0F;
				}

				rotationPitch = prevRotationPitch + (rotationPitch - prevRotationPitch) * 0.2F;
				rotationYaw = prevRotationYaw + (rotationYaw - prevRotationYaw) * 0.2F;
				float f6 = 0.92F;

				if (onGround || isCollidedHorizontally) {
					f6 = 0.5F;
				}

				byte b0 = 5;
				double d10 = 0.0D;

				for (int j = 0; j < b0; ++j) {
					double d3 = boundingBox.minY + (boundingBox.maxY - boundingBox.minY) * (j + 0) / b0 - 0.125D
							+ 0.125D;
					double d4 = boundingBox.minY + (boundingBox.maxY - boundingBox.minY) * (j + 1) / b0 - 0.125D
							+ 0.125D;
					AxisAlignedBB axisalignedbb1 = AxisAlignedBB.getBoundingBox(boundingBox.minX, d3, boundingBox.minZ,
							boundingBox.maxX, d4, boundingBox.maxZ);

					if (worldObj.isAABBInMaterial(axisalignedbb1, Material.water)) {
						d10 += 1.0D / b0;
					}
				}

				if (!worldObj.isRemote && d10 > 0.0D) {
					WorldServer worldserver = (WorldServer) worldObj;
					int k = 1;

					if (rand.nextFloat() < 0.25F && worldObj.canLightningStrikeAt(MathHelper.floor_double(posX),
							MathHelper.floor_double(posY) + 1, MathHelper.floor_double(posZ))) {
						k = 2;
					}

					if (rand.nextFloat() < 0.5F && !worldObj.canBlockSeeTheSky(MathHelper.floor_double(posX),
							MathHelper.floor_double(posY) + 1, MathHelper.floor_double(posZ))) {
						--k;
					}

					if (field_146045_ax > 0) {
						--field_146045_ax;

						if (field_146045_ax <= 0) {
							field_146040_ay = 0;
							field_146038_az = 0;
						}
					} else {
						float f1;
						float f2;
						double d5;
						double d6;
						float f7;
						double d11;

						if (field_146038_az > 0) {
							field_146038_az -= k;

							if (field_146038_az <= 0) {
								motionY -= 0.20000000298023224D;
								playSound("random.splash", 0.25F, 1.0F + (rand.nextFloat() - rand.nextFloat()) * 0.4F);
								f1 = MathHelper.floor_double(boundingBox.minY);
								worldserver.func_147487_a("bubble", posX, f1 + 1.0F, posZ, (int) (1.0F + width * 20.0F),
										width, 0.0D, width, 0.20000000298023224D);
								worldserver.func_147487_a("wake", posX, f1 + 1.0F, posZ, (int) (1.0F + width * 20.0F),
										width, 0.0D, width, 0.20000000298023224D);
								field_146045_ax = MathHelper.getRandomIntegerInRange(rand, 10, 30);
							} else {
								field_146054_aA = (float) (field_146054_aA + rand.nextGaussian() * 4.0D);
								f1 = field_146054_aA * 0.017453292F;
								f7 = MathHelper.sin(f1);
								f2 = MathHelper.cos(f1);
								d11 = posX + f7 * field_146038_az * 0.1F;
								d5 = MathHelper.floor_double(boundingBox.minY) + 1.0F;
								d6 = posZ + f2 * field_146038_az * 0.1F;

								if (rand.nextFloat() < 0.15F) {
									worldserver.func_147487_a("bubble", d11, d5 - 0.10000000149011612D, d6, 1, f7, 0.1D,
											f2, 0.0D);
								}

								float f3 = f7 * 0.04F;
								float f4 = f2 * 0.04F;
								worldserver.func_147487_a("wake", d11, d5, d6, 0, f4, 0.01D, -f3, 1.0D);
								worldserver.func_147487_a("wake", d11, d5, d6, 0, -f4, 0.01D, f3, 1.0D);
							}
						} else if (field_146040_ay > 0) {
							field_146040_ay -= k;
							f1 = 0.15F;

							if (field_146040_ay < 20) {
								f1 = (float) (f1 + (20 - field_146040_ay) * 0.05D);
							} else if (field_146040_ay < 40) {
								f1 = (float) (f1 + (40 - field_146040_ay) * 0.02D);
							} else if (field_146040_ay < 60) {
								f1 = (float) (f1 + (60 - field_146040_ay) * 0.01D);
							}

							if (rand.nextFloat() < f1) {
								f7 = MathHelper.randomFloatClamp(rand, 0.0F, 360.0F) * 0.017453292F;
								f2 = MathHelper.randomFloatClamp(rand, 25.0F, 60.0F);
								d11 = posX + MathHelper.sin(f7) * f2 * 0.1F;
								d5 = MathHelper.floor_double(boundingBox.minY) + 1.0F;
								d6 = posZ + MathHelper.cos(f7) * f2 * 0.1F;
								worldserver.func_147487_a("splash", d11, d5, d6, 2 + rand.nextInt(2),
										0.10000000149011612D, 0.0D, 0.10000000149011612D, 0.0D);
							}

							if (field_146040_ay <= 0) {
								field_146054_aA = MathHelper.randomFloatClamp(rand, 0.0F, 360.0F);
								field_146038_az = MathHelper.getRandomIntegerInRange(rand, 20, 80);
							}
						} else {
							field_146040_ay = MathHelper.getRandomIntegerInRange(rand, 100, 900);
							field_146040_ay -= EnchantmentHelper.func_151387_h(field_146042_b) * 20 * 5;
						}
					}

					if (field_146045_ax > 0) {
						motionY -= rand.nextFloat() * rand.nextFloat() * rand.nextFloat() * 0.2D;
					}
				}

				d2 = d10 * 2.0D - 1.0D;
				motionY += 0.03999999910593033D * d2;

				if (d10 > 0.0D) {
					f6 = (float) (f6 * 0.9D);
					motionY *= 0.8D;
				}

				motionX *= f6;
				motionY *= f6;
				motionZ *= f6;
				setPosition(posX, posY, posZ);
			}
		}
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound p_70014_1_) {
		p_70014_1_.setShort("xTile", (short) field_146037_g);
		p_70014_1_.setShort("yTile", (short) field_146048_h);
		p_70014_1_.setShort("zTile", (short) field_146050_i);
		p_70014_1_.setByte("inTile", (byte) Block.getIdFromBlock(field_146046_j));
		p_70014_1_.setByte("shake", (byte) field_146044_a);
		p_70014_1_.setByte("inGround", (byte) (field_146051_au ? 1 : 0));
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound p_70037_1_) {
		field_146037_g = p_70037_1_.getShort("xTile");
		field_146048_h = p_70037_1_.getShort("yTile");
		field_146050_i = p_70037_1_.getShort("zTile");
		field_146046_j = Block.getBlockById(p_70037_1_.getByte("inTile") & 255);
		field_146044_a = p_70037_1_.getByte("shake") & 255;
		field_146051_au = p_70037_1_.getByte("inGround") == 1;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public float getShadowSize() {
		return 0.0F;
	}

	private PlayerFishEvent playerFishEvent;
	private boolean isProjectileHitEventFired = false;

	public int func_146034_e() {
		if (worldObj.isRemote)
			return 0;
		else {
			byte b0 = 0;

			if (field_146043_c != null) {
				PlayerFishEvent playerFishEvent = new PlayerFishEvent((Player) field_146042_b.getBukkitEntity(),
						field_146043_c.getBukkitEntity(), (Fish) this.getBukkitEntity(),
						PlayerFishEvent.State.CAUGHT_ENTITY);
				Bukkit.getPluginManager().callEvent(playerFishEvent);
				if (playerFishEvent.isCancelled())
					return 0;
				double d0 = field_146042_b.posX - posX;
				double d2 = field_146042_b.posY - posY;
				double d4 = field_146042_b.posZ - posZ;

				double d6 = MathHelper.sqrt_double(d0 * d0 + d2 * d2 + d4 * d4);
				double d8 = 0.1D;
				field_146043_c.motionX += d0 * d8;
				field_146043_c.motionY += d2 * d8 + MathHelper.sqrt_double(d6) * 0.08D;
				field_146043_c.motionZ += d4 * d8;
				b0 = 3;
			} else if (field_146045_ax > 0) {
				EntityItem entityitem = new EntityItem(worldObj, posX, posY, posZ, func_146033_f());

				playerFishEvent = new PlayerFishEvent((Player) field_146042_b.getBukkitEntity(),
						entityitem.getBukkitEntity(), (Fish) this.getBukkitEntity(), PlayerFishEvent.State.CAUGHT_FISH);
				playerFishEvent.setExpToDrop(rand.nextInt(6) + 1);
				Bukkit.getPluginManager().callEvent(playerFishEvent);
				if (playerFishEvent.isCancelled())
					return 0;
				double d1 = field_146042_b.posX - posX;
				double d3 = field_146042_b.posY - posY;
				double d5 = field_146042_b.posZ - posZ;
				double d7 = MathHelper.sqrt_double(d1 * d1 + d3 * d3 + d5 * d5);
				double d9 = 0.1D;
				entityitem.motionX = d1 * d9;
				entityitem.motionY = d3 * d9 + MathHelper.sqrt_double(d7) * 0.08D;
				entityitem.motionZ = d5 * d9;
				worldObj.spawnEntityInWorld(entityitem);
				EntityXPOrb orb = new EntityXPOrb(field_146042_b.worldObj, field_146042_b.posX,
						field_146042_b.posY + 0.5D, field_146042_b.posZ + 0.5D, rand.nextInt(6) + 1);
				orb.xpValue = playerFishEvent.getExpToDrop();
				field_146042_b.worldObj.spawnEntityInWorld(orb);
				b0 = 1;
			}

			if (field_146051_au) {
				PlayerFishEvent playerFishEvent = new PlayerFishEvent((Player) field_146042_b.getBukkitEntity(), null,
						(Fish) this.getBukkitEntity(), PlayerFishEvent.State.IN_GROUND);
				Bukkit.getPluginManager().callEvent(playerFishEvent);
				if (playerFishEvent.isCancelled())
					return 0;
				b0 = 2;
			}

			setDead();

			if (b0 == 0) {
				PlayerFishEvent playerFishEvent = new PlayerFishEvent((Player) field_146042_b.getBukkitEntity(), null,
						(Fish) this.getBukkitEntity(), PlayerFishEvent.State.FAILED_ATTEMPT);
				Bukkit.getPluginManager().callEvent(playerFishEvent);
				if (playerFishEvent.isCancelled())
					return 0;
			}

			field_146042_b.fishEntity = null;
			return b0;
		}
	}

	private ItemStack func_146033_f() {
		float f = worldObj.rand.nextFloat();
		int i = EnchantmentHelper.func_151386_g(field_146042_b);
		int j = EnchantmentHelper.func_151387_h(field_146042_b);
		if (true) {
			field_146042_b.addStat(net.minecraftforge.common.FishingHooks.getFishableCategory(f, i, j).stat, 1);
			return net.minecraftforge.common.FishingHooks.getRandomFishable(rand, f, i, j);
		}

		float f1 = 0.1F - i * 0.025F - j * 0.01F;
		float f2 = 0.05F + i * 0.01F - j * 0.01F;
		f1 = MathHelper.clamp_float(f1, 0.0F, 1.0F);
		f2 = MathHelper.clamp_float(f2, 0.0F, 1.0F);

		if (f < f1) {
			field_146042_b.addStat(StatList.field_151183_A, 1);
			return ((WeightedRandomFishable) WeightedRandom.getRandomItem(rand, field_146039_d)).func_150708_a(rand);
		} else {
			f -= f1;

			if (f < f2) {
				field_146042_b.addStat(StatList.field_151184_B, 1);
				return ((WeightedRandomFishable) WeightedRandom.getRandomItem(rand, field_146041_e))
						.func_150708_a(rand);
			} else {
				float f3 = f - f2;
				field_146042_b.addStat(StatList.fishCaughtStat, 1);
				return ((WeightedRandomFishable) WeightedRandom.getRandomItem(rand, field_146036_f))
						.func_150708_a(rand);
			}
		}
	}

	@Override
	public void setDead() {
		super.setDead();

		if (field_146042_b != null) {
			field_146042_b.fishEntity = null;
		}
	}
}