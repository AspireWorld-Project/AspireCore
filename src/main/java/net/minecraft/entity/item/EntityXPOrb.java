package net.minecraft.entity.item;

import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.event.entity.EntityTargetEvent;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerPickupXpEvent;

public class EntityXPOrb extends Entity {
	public int xpColor;
	public int xpOrbAge;
	public int field_70532_c;
	private int xpOrbHealth = 5;
	public int xpValue;
	private EntityPlayer closestPlayer;
	private int xpTargetColor;
	private static final String __OBFID = "CL_00001544";

	public EntityXPOrb(World p_i1585_1_, double p_i1585_2_, double p_i1585_4_, double p_i1585_6_, int p_i1585_8_) {
		super(p_i1585_1_);
		setSize(0.5F, 0.5F);
		yOffset = height / 2.0F;
		setPosition(p_i1585_2_, p_i1585_4_, p_i1585_6_);
		rotationYaw = (float) (Math.random() * 360.0D);
		motionX = (float) (Math.random() * 0.20000000298023224D - 0.10000000149011612D) * 2.0F;
		motionY = (float) (Math.random() * 0.2D) * 2.0F;
		motionZ = (float) (Math.random() * 0.20000000298023224D - 0.10000000149011612D) * 2.0F;
		xpValue = p_i1585_8_;
	}

	@Override
	protected boolean canTriggerWalking() {
		return false;
	}

	public EntityXPOrb(World p_i1586_1_) {
		super(p_i1586_1_);
		setSize(0.25F, 0.25F);
		yOffset = height / 2.0F;
	}

	@Override
	protected void entityInit() {
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getBrightnessForRender(float p_70070_1_) {
		float f1 = 0.5F;

		if (f1 < 0.0F) {
			f1 = 0.0F;
		}

		if (f1 > 1.0F) {
			f1 = 1.0F;
		}

		int i = super.getBrightnessForRender(p_70070_1_);
		int j = i & 255;
		int k = i >> 16 & 255;
		j += (int) (f1 * 15.0F * 16.0F);

		if (j > 240) {
			j = 240;
		}

		return j | k << 16;
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		if (field_70532_c > 0) {
			--field_70532_c;
		}
		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;
		motionY -= 0.029999999329447746D;
		if (worldObj
				.getBlock(MathHelper.floor_double(posX), MathHelper.floor_double(posY), MathHelper.floor_double(posZ))
				.getMaterial() == Material.lava) {
			motionY = 0.20000000298023224D;
			motionX = (rand.nextFloat() - rand.nextFloat()) * 0.2F;
			motionZ = (rand.nextFloat() - rand.nextFloat()) * 0.2F;
			playSound("random.fizz", 0.4F, 2.0F + rand.nextFloat() * 0.4F);
		}
		func_145771_j(posX, (boundingBox.minY + boundingBox.maxY) / 2.0D, posZ);
		double d0 = 8.0D;
		if (xpTargetColor < xpColor - 20 + getEntityId() % 100) {
			if (closestPlayer == null || closestPlayer.getDistanceSqToEntity(this) > d0 * d0) {
				closestPlayer = worldObj.getClosestPlayerToEntity(this, d0);
			}
			xpTargetColor = xpColor;
		}
		if (closestPlayer != null) {
			EntityTargetEvent event = CraftEventFactory.callEntityTargetEvent(this, closestPlayer,
					EntityTargetEvent.TargetReason.CLOSEST_PLAYER);
			Entity target = event.getTarget() == null ? null : ((CraftEntity) event.getTarget()).getHandle();
			if (!event.isCancelled() && target != null) {
				double d1 = (target.posX - posX) / d0;
				double d2 = (target.posY + target.getEyeHeight() - posY) / d0;
				double d3 = (target.posZ - posZ) / d0;
				double d4 = Math.sqrt(d1 * d1 + d2 * d2 + d3 * d3);
				double d5 = 1.0D - d4;
				if (d5 > 0.0D) {
					d5 *= d5;
					motionX += d1 / d4 * d5 * 0.1D;
					motionY += d2 / d4 * d5 * 0.1D;
					motionZ += d3 / d4 * d5 * 0.1D;
				}
			}
			moveEntity(motionX, motionY, motionZ);
			float f = 0.98F;
			if (onGround) {
				f = worldObj.getBlock(MathHelper.floor_double(posX), MathHelper.floor_double(boundingBox.minY) - 1,
						MathHelper.floor_double(posZ)).slipperiness * 0.98F;
			}
			motionX *= f;
			motionY *= 0.9800000190734863D;
			motionZ *= f;
			if (onGround) {
				motionY *= -0.8999999761581421D;
			}
			++xpColor;
			++xpOrbAge;
			if (xpOrbAge >= 6000) {
				setDead();
			}
		}
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
		else {
			setBeenAttacked();
			xpOrbHealth = (int) (xpOrbHealth - p_70097_2_);

			if (xpOrbHealth <= 0) {
				setDead();
			}

			return false;
		}
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound p_70014_1_) {
		p_70014_1_.setShort("Health", (byte) xpOrbHealth);
		p_70014_1_.setShort("Age", (short) xpOrbAge);
		p_70014_1_.setShort("Value", (short) xpValue);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound p_70037_1_) {
		xpOrbHealth = p_70037_1_.getShort("Health") & 255;
		xpOrbAge = p_70037_1_.getShort("Age");
		xpValue = p_70037_1_.getShort("Value");
	}

	@Override
	public void onCollideWithPlayer(EntityPlayer p_70100_1_) {
		if (!worldObj.isRemote) {
			if (field_70532_c == 0 && p_70100_1_.xpCooldown == 0) {
				if (MinecraftForge.EVENT_BUS.post(new PlayerPickupXpEvent(p_70100_1_, this)))
					return;
				p_70100_1_.xpCooldown = 2;
				worldObj.playSoundAtEntity(p_70100_1_, "random.orb", 0.1F,
						0.5F * ((rand.nextFloat() - rand.nextFloat()) * 0.7F + 1.8F));
				p_70100_1_.onItemPickup(this, 1);

				p_70100_1_.addExperience(CraftEventFactory.callPlayerExpChangeEvent(p_70100_1_, xpValue).getAmount());
				setDead();
			}
		}
	}

	public int getXpValue() {
		return xpValue;
	}

	@SideOnly(Side.CLIENT)
	public int getTextureByXP() {
		return xpValue >= 2477 ? 10
				: xpValue >= 1237 ? 9
						: xpValue >= 617 ? 8
								: xpValue >= 307 ? 7
										: xpValue >= 149 ? 6
												: xpValue >= 73 ? 5
														: xpValue >= 37 ? 4
																: xpValue >= 17 ? 3
																		: xpValue >= 7 ? 2 : xpValue >= 3 ? 1 : 0;
	}

	public static int getXPSplit(int p_70527_0_) {
		return p_70527_0_ >= 2477 ? 2477
				: p_70527_0_ >= 1237 ? 1237
						: p_70527_0_ >= 617 ? 617
								: p_70527_0_ >= 307 ? 307
										: p_70527_0_ >= 149 ? 149
												: p_70527_0_ >= 73 ? 73
														: p_70527_0_ >= 37 ? 37
																: p_70527_0_ >= 17 ? 17
																		: p_70527_0_ >= 7 ? 7 : p_70527_0_ >= 3 ? 3 : 1;
	}

	@Override
	public boolean canAttackWithItem() {
		return false;
	}

	/*
	 * ===================================== ULTRAMINE START
	 * =====================================
	 */

	@Override
	public org.ultramine.server.EntityType computeEntityType() {
		return org.ultramine.server.EntityType.XP_ORB;
	}
}