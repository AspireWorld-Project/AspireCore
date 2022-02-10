package net.minecraft.entity.passive;

import net.minecraft.block.material.Material;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntitySquid extends EntityWaterMob {
	public float squidPitch;
	public float prevSquidPitch;
	public float squidYaw;
	public float prevSquidYaw;
	public float squidRotation;
	public float prevSquidRotation;
	public float tentacleAngle;
	public float lastTentacleAngle;
	private float randomMotionSpeed;
	private float rotationVelocity;
	private float field_70871_bB;
	private float randomMotionVecX;
	private float randomMotionVecY;
	private float randomMotionVecZ;
	private static final String __OBFID = "CL_00001651";

	public EntitySquid(World p_i1693_1_) {
		super(p_i1693_1_);
		setSize(0.95F, 0.95F);
		rotationVelocity = 1.0F / (rand.nextFloat() + 1.0F) * 0.2F;
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(10.0D);
	}

	@Override
	protected String getLivingSound() {
		return null;
	}

	@Override
	protected String getHurtSound() {
		return null;
	}

	@Override
	protected String getDeathSound() {
		return null;
	}

	@Override
	protected float getSoundVolume() {
		return 0.4F;
	}

	@Override
	protected Item getDropItem() {
		return Item.getItemById(0);
	}

	@Override
	protected boolean canTriggerWalking() {
		return false;
	}

	@Override
	protected void dropFewItems(boolean p_70628_1_, int p_70628_2_) {
		int j = rand.nextInt(3 + p_70628_2_) + 1;

		for (int k = 0; k < j; ++k) {
			entityDropItem(new ItemStack(Items.dye, 1, 0), 0.0F);
		}
	}

	@Override
	public boolean isInWater() {
		return worldObj.handleMaterialAcceleration(boundingBox.expand(0.0D, -0.6000000238418579D, 0.0D), Material.water,
				this);
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		prevSquidPitch = squidPitch;
		prevSquidYaw = squidYaw;
		prevSquidRotation = squidRotation;
		lastTentacleAngle = tentacleAngle;
		squidRotation += rotationVelocity;

		if (squidRotation > (float) Math.PI * 2F) {
			squidRotation -= (float) Math.PI * 2F;

			if (rand.nextInt(10) == 0) {
				rotationVelocity = 1.0F / (rand.nextFloat() + 1.0F) * 0.2F;
			}
		}

		if (isInWater()) {
			float f;

			if (squidRotation < (float) Math.PI) {
				f = squidRotation / (float) Math.PI;
				tentacleAngle = MathHelper.sin(f * f * (float) Math.PI) * (float) Math.PI * 0.25F;

				if (f > 0.75D) {
					randomMotionSpeed = 1.0F;
					field_70871_bB = 1.0F;
				} else {
					field_70871_bB *= 0.8F;
				}
			} else {
				tentacleAngle = 0.0F;
				randomMotionSpeed *= 0.9F;
				field_70871_bB *= 0.99F;
			}

			if (!worldObj.isRemote) {
				motionX = randomMotionVecX * randomMotionSpeed;
				motionY = randomMotionVecY * randomMotionSpeed;
				motionZ = randomMotionVecZ * randomMotionSpeed;
			}

			f = MathHelper.sqrt_double(motionX * motionX + motionZ * motionZ);
			renderYawOffset += (-((float) Math.atan2(motionX, motionZ)) * 180.0F / (float) Math.PI - renderYawOffset)
					* 0.1F;
			rotationYaw = renderYawOffset;
			squidYaw += (float) Math.PI * field_70871_bB * 1.5F;
			squidPitch += (-((float) Math.atan2(f, motionY)) * 180.0F / (float) Math.PI - squidPitch) * 0.1F;
		} else {
			tentacleAngle = MathHelper.abs(MathHelper.sin(squidRotation)) * (float) Math.PI * 0.25F;

			if (!worldObj.isRemote) {
				motionX = 0.0D;
				motionY -= 0.08D;
				motionY *= 0.9800000190734863D;
				motionZ = 0.0D;
			}

			squidPitch = (float) (squidPitch + (-90.0F - squidPitch) * 0.02D);
		}
	}

	@Override
	public void moveEntityWithHeading(float p_70612_1_, float p_70612_2_) {
		moveEntity(motionX, motionY, motionZ);
	}

	@Override
	protected void updateEntityActionState() {
		++entityAge;

		if (entityAge > 100) {
			randomMotionVecX = randomMotionVecY = randomMotionVecZ = 0.0F;
		} else if (rand.nextInt(50) == 0 || !inWater
				|| randomMotionVecX == 0.0F && randomMotionVecY == 0.0F && randomMotionVecZ == 0.0F) {
			float f = rand.nextFloat() * (float) Math.PI * 2.0F;
			randomMotionVecX = MathHelper.cos(f) * 0.2F;
			randomMotionVecY = -0.1F + rand.nextFloat() * 0.2F;
			randomMotionVecZ = MathHelper.sin(f) * 0.2F;
		}

		despawnEntity();
	}

	@Override
	public boolean getCanSpawnHere() {
		return posY > 45.0D && posY < 63.0D && super.getCanSpawnHere();
	}
}