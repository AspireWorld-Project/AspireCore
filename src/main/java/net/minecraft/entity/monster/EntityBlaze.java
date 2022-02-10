package net.minecraft.entity.monster;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityBlaze extends EntityMob {
	private float heightOffset = 0.5F;
	private int heightOffsetUpdateTime;
	private int field_70846_g;
	private static final String __OBFID = "CL_00001682";

	public EntityBlaze(World p_i1731_1_) {
		super(p_i1731_1_);
		isImmuneToFire = true;
		experienceValue = 10;
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(6.0D);
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		dataWatcher.addObject(16, new Byte((byte) 0));
	}

	@Override
	protected String getLivingSound() {
		return "mob.blaze.breathe";
	}

	@Override
	protected String getHurtSound() {
		return "mob.blaze.hit";
	}

	@Override
	protected String getDeathSound() {
		return "mob.blaze.death";
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getBrightnessForRender(float p_70070_1_) {
		return 15728880;
	}

	@Override
	public float getBrightness(float p_70013_1_) {
		return 1.0F;
	}

	@Override
	public void onLivingUpdate() {
		if (!worldObj.isRemote) {
			if (isWet()) {
				attackEntityFrom(DamageSource.drown, 1.0F);
			}

			--heightOffsetUpdateTime;

			if (heightOffsetUpdateTime <= 0) {
				heightOffsetUpdateTime = 100;
				heightOffset = 0.5F + (float) rand.nextGaussian() * 3.0F;
			}

			if (getEntityToAttack() != null && getEntityToAttack().posY + getEntityToAttack().getEyeHeight() > posY
					+ getEyeHeight() + heightOffset) {
				motionY += (0.30000001192092896D - motionY) * 0.30000001192092896D;
			}
		}

		if (rand.nextInt(24) == 0) {
			worldObj.playSoundEffect(posX + 0.5D, posY + 0.5D, posZ + 0.5D, "fire.fire", 1.0F + rand.nextFloat(),
					rand.nextFloat() * 0.7F + 0.3F);
		}

		if (!onGround && motionY < 0.0D) {
			motionY *= 0.6D;
		}

		for (int i = 0; i < 2; ++i) {
			worldObj.spawnParticle("largesmoke", posX + (rand.nextDouble() - 0.5D) * width,
					posY + rand.nextDouble() * height, posZ + (rand.nextDouble() - 0.5D) * width, 0.0D, 0.0D, 0.0D);
		}

		super.onLivingUpdate();
	}

	@Override
	protected void attackEntity(Entity p_70785_1_, float p_70785_2_) {
		if (attackTime <= 0 && p_70785_2_ < 2.0F && p_70785_1_.boundingBox.maxY > boundingBox.minY
				&& p_70785_1_.boundingBox.minY < boundingBox.maxY) {
			attackTime = 20;
			attackEntityAsMob(p_70785_1_);
		} else if (p_70785_2_ < 30.0F) {
			double d0 = p_70785_1_.posX - posX;
			double d1 = p_70785_1_.boundingBox.minY + p_70785_1_.height / 2.0F - (posY + height / 2.0F);
			double d2 = p_70785_1_.posZ - posZ;

			if (attackTime == 0) {
				++field_70846_g;

				if (field_70846_g == 1) {
					attackTime = 60;
					func_70844_e(true);
				} else if (field_70846_g <= 4) {
					attackTime = 6;
				} else {
					attackTime = 100;
					field_70846_g = 0;
					func_70844_e(false);
				}

				if (field_70846_g > 1) {
					float f1 = MathHelper.sqrt_float(p_70785_2_) * 0.5F;
					worldObj.playAuxSFXAtEntity((EntityPlayer) null, 1009, (int) posX, (int) posY, (int) posZ, 0);

					for (int i = 0; i < 1; ++i) {
						EntitySmallFireball entitysmallfireball = new EntitySmallFireball(worldObj, this,
								d0 + rand.nextGaussian() * f1, d1, d2 + rand.nextGaussian() * f1);
						entitysmallfireball.posY = posY + height / 2.0F + 0.5D;
						worldObj.spawnEntityInWorld(entitysmallfireball);
					}
				}
			}

			rotationYaw = (float) (Math.atan2(d2, d0) * 180.0D / Math.PI) - 90.0F;
			hasAttacked = true;
		}
	}

	@Override
	protected void fall(float p_70069_1_) {
	}

	@Override
	protected Item getDropItem() {
		return Items.blaze_rod;
	}

	@Override
	public boolean isBurning() {
		return func_70845_n();
	}

	@Override
	protected void dropFewItems(boolean p_70628_1_, int p_70628_2_) {
		if (p_70628_1_) {
			int j = rand.nextInt(2 + p_70628_2_);

			for (int k = 0; k < j; ++k) {
				dropItem(Items.blaze_rod, 1);
			}
		}
	}

	public boolean func_70845_n() {
		return (dataWatcher.getWatchableObjectByte(16) & 1) != 0;
	}

	public void func_70844_e(boolean p_70844_1_) {
		byte b0 = dataWatcher.getWatchableObjectByte(16);

		if (p_70844_1_) {
			b0 = (byte) (b0 | 1);
		} else {
			b0 &= -2;
		}

		dataWatcher.updateObject(16, Byte.valueOf(b0));
	}

	@Override
	protected boolean isValidLightLevel() {
		return true;
	}
}