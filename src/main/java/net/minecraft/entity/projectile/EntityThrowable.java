package net.minecraft.entity.projectile;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import org.bukkit.craftbukkit.event.CraftEventFactory;

import java.util.List;

public abstract class EntityThrowable extends Entity implements IProjectile {
	private int field_145788_c = -1;
	private int field_145786_d = -1;
	private int field_145787_e = -1;
	private Block field_145785_f;
	protected boolean inGround;
	public int throwableShake;
	private EntityLivingBase thrower;
	private String throwerName;
	private int ticksInGround;
	private int ticksInAir;
	private static final String __OBFID = "CL_00001723";

	public EntityThrowable(World p_i1776_1_) {
		super(p_i1776_1_);
		setSize(0.25F, 0.25F);
	}

	@Override
	protected void entityInit() {
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean isInRangeToRenderDist(double p_70112_1_) {
		double d1 = boundingBox.getAverageEdgeLength() * 4.0D;
		d1 *= 64.0D;
		return p_70112_1_ < d1 * d1;
	}

	public EntityThrowable(World p_i1777_1_, EntityLivingBase p_i1777_2_) {
		super(p_i1777_1_);
		thrower = p_i1777_2_;
		setSize(0.25F, 0.25F);
		setLocationAndAngles(p_i1777_2_.posX, p_i1777_2_.posY + p_i1777_2_.getEyeHeight(), p_i1777_2_.posZ,
				p_i1777_2_.rotationYaw, p_i1777_2_.rotationPitch);
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
		motionY = -MathHelper.sin((rotationPitch + func_70183_g()) / 180.0F * (float) Math.PI) * f;
		setThrowableHeading(motionX, motionY, motionZ, func_70182_d(), 1.0F);
	}

	public EntityThrowable(World p_i1778_1_, double p_i1778_2_, double p_i1778_4_, double p_i1778_6_) {
		super(p_i1778_1_);
		ticksInGround = 0;
		setSize(0.25F, 0.25F);
		setPosition(p_i1778_2_, p_i1778_4_, p_i1778_6_);
		yOffset = 0.0F;
	}

	protected float func_70182_d() {
		return 1.5F;
	}

	protected float func_70183_g() {
		return 0.0F;
	}

	@Override
	public void setThrowableHeading(double p_70186_1_, double p_70186_3_, double p_70186_5_, float p_70186_7_,
			float p_70186_8_) {
		float f2 = MathHelper.sqrt_double(p_70186_1_ * p_70186_1_ + p_70186_3_ * p_70186_3_ + p_70186_5_ * p_70186_5_);
		p_70186_1_ /= f2;
		p_70186_3_ /= f2;
		p_70186_5_ /= f2;
		p_70186_1_ += rand.nextGaussian() * 0.007499999832361937D * p_70186_8_;
		p_70186_3_ += rand.nextGaussian() * 0.007499999832361937D * p_70186_8_;
		p_70186_5_ += rand.nextGaussian() * 0.007499999832361937D * p_70186_8_;
		p_70186_1_ *= p_70186_7_;
		p_70186_3_ *= p_70186_7_;
		p_70186_5_ *= p_70186_7_;
		motionX = p_70186_1_;
		motionY = p_70186_3_;
		motionZ = p_70186_5_;
		float f3 = MathHelper.sqrt_double(p_70186_1_ * p_70186_1_ + p_70186_5_ * p_70186_5_);
		prevRotationYaw = rotationYaw = (float) (Math.atan2(p_70186_1_, p_70186_5_) * 180.0D / Math.PI);
		prevRotationPitch = rotationPitch = (float) (Math.atan2(p_70186_3_, f3) * 180.0D / Math.PI);
		ticksInGround = 0;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void setVelocity(double p_70016_1_, double p_70016_3_, double p_70016_5_) {
		motionX = p_70016_1_;
		motionY = p_70016_3_;
		motionZ = p_70016_5_;

		if (prevRotationPitch == 0.0F && prevRotationYaw == 0.0F) {
			float f = MathHelper.sqrt_double(p_70016_1_ * p_70016_1_ + p_70016_5_ * p_70016_5_);
			prevRotationYaw = rotationYaw = (float) (Math.atan2(p_70016_1_, p_70016_5_) * 180.0D / Math.PI);
			prevRotationPitch = rotationPitch = (float) (Math.atan2(p_70016_3_, f) * 180.0D / Math.PI);
		}
	}

	public EntityLivingBase getMixinThrower() {
		return thrower;
	}

	public void setThrower(EntityLivingBase thrower) {
		this.thrower = thrower;
	}

	public String getThrowerName() {
		return throwerName;
	}

	public void setThrowerName(String throwerName) {
		this.throwerName = throwerName;
	}

	@Override
	public void onUpdate() {
		lastTickPosX = posX;
		lastTickPosY = posY;
		lastTickPosZ = posZ;
		super.onUpdate();

		if (throwableShake > 0) {
			--throwableShake;
		}

		if (inGround) {
			if (worldObj.getBlock(field_145788_c, field_145786_d, field_145787_e) == field_145785_f) {
				++ticksInGround;

				if (ticksInGround == 1200) {
					setDead();
				}

				return;
			}

			inGround = false;
			motionX *= rand.nextFloat() * 0.2F;
			motionY *= rand.nextFloat() * 0.2F;
			motionZ *= rand.nextFloat() * 0.2F;
			ticksInGround = 0;
			ticksInAir = 0;
		} else {
			++ticksInAir;
		}

		Vec3 vec3 = Vec3.createVectorHelper(posX, posY, posZ);
		Vec3 vec31 = Vec3.createVectorHelper(posX + motionX, posY + motionY, posZ + motionZ);
		MovingObjectPosition movingobjectposition = worldObj.rayTraceBlocks(vec3, vec31);
		vec3 = Vec3.createVectorHelper(posX, posY, posZ);
		vec31 = Vec3.createVectorHelper(posX + motionX, posY + motionY, posZ + motionZ);

		if (movingobjectposition != null) {
			vec31 = Vec3.createVectorHelper(movingobjectposition.hitVec.xCoord, movingobjectposition.hitVec.yCoord,
					movingobjectposition.hitVec.zCoord);
		}

		if (!worldObj.isRemote) {
			Entity entity = null;
			List list = worldObj.getEntitiesWithinAABBExcludingEntity(this,
					boundingBox.addCoord(motionX, motionY, motionZ).expand(1.0D, 1.0D, 1.0D));
			double d0 = 0.0D;
			EntityLivingBase entitylivingbase = getMixinThrower();

			for (int j = 0; j < list.size(); ++j) {
				Entity entity1 = (Entity) list.get(j);

				if (entity1.canBeCollidedWith() && (entity1 != entitylivingbase || ticksInAir >= 5)) {
					float f = 0.3F;
					AxisAlignedBB axisalignedbb = entity1.boundingBox.expand(f, f, f);
					MovingObjectPosition movingobjectposition1 = axisalignedbb.calculateIntercept(vec3, vec31);

					if (movingobjectposition1 != null) {
						double d1 = vec3.distanceTo(movingobjectposition1.hitVec);

						if (d1 < d0 || d0 == 0.0D) {
							entity = entity1;
							d0 = d1;
						}
					}
				}
			}

			if (entity != null) {
				movingobjectposition = new MovingObjectPosition(entity);
			}
		}

		if (movingobjectposition != null) {
			if (movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK
					&& worldObj.getBlock(movingobjectposition.blockX, movingobjectposition.blockY,
							movingobjectposition.blockZ) == Blocks.portal) {
				setInPortal();
			} else {
				onImpact(movingobjectposition);
				if (isDead) {
					CraftEventFactory.callProjectileHitEvent(this);
				}
			}
		}

		posX += motionX;
		posY += motionY;
		posZ += motionZ;
		float f1 = MathHelper.sqrt_double(motionX * motionX + motionZ * motionZ);
		rotationYaw = (float) (Math.atan2(motionX, motionZ) * 180.0D / Math.PI);

		for (rotationPitch = (float) (Math.atan2(motionY, f1) * 180.0D / Math.PI); rotationPitch
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
		float f2 = 0.99F;
		float f3 = getGravityVelocity();

		if (isInWater()) {
			for (int i = 0; i < 4; ++i) {
				float f4 = 0.25F;
				worldObj.spawnParticle("bubble", posX - motionX * f4, posY - motionY * f4, posZ - motionZ * f4, motionX,
						motionY, motionZ);
			}

			f2 = 0.8F;
		}

		motionX *= f2;
		motionY *= f2;
		motionZ *= f2;
		motionY -= f3;
		setPosition(posX, posY, posZ);
	}

	protected float getGravityVelocity() {
		return 0.03F;
	}

	protected abstract void onImpact(MovingObjectPosition p_70184_1_);

	@Override
	public void writeEntityToNBT(NBTTagCompound p_70014_1_) {
		p_70014_1_.setShort("xTile", (short) field_145788_c);
		p_70014_1_.setShort("yTile", (short) field_145786_d);
		p_70014_1_.setShort("zTile", (short) field_145787_e);
		p_70014_1_.setByte("inTile", (byte) Block.getIdFromBlock(field_145785_f));
		p_70014_1_.setByte("shake", (byte) throwableShake);
		p_70014_1_.setByte("inGround", (byte) (inGround ? 1 : 0));

		if ((throwerName == null || throwerName.length() == 0) && thrower != null && thrower instanceof EntityPlayer) {
			throwerName = thrower.getCommandSenderName();
		}

		p_70014_1_.setString("ownerName", throwerName == null ? "" : throwerName);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound p_70037_1_) {
		field_145788_c = p_70037_1_.getShort("xTile");
		field_145786_d = p_70037_1_.getShort("yTile");
		field_145787_e = p_70037_1_.getShort("zTile");
		field_145785_f = Block.getBlockById(p_70037_1_.getByte("inTile") & 255);
		throwableShake = p_70037_1_.getByte("shake") & 255;
		inGround = p_70037_1_.getByte("inGround") == 1;
		throwerName = p_70037_1_.getString("ownerName");

		if (throwerName != null && throwerName.length() == 0) {
			throwerName = null;
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public float getShadowSize() {
		return 0.0F;
	}

	public EntityLivingBase getThrower() {
		if (thrower == null && throwerName != null && throwerName.length() > 0) {
			thrower = worldObj.getPlayerEntityByName(throwerName);
		}

		return thrower;
	}
}