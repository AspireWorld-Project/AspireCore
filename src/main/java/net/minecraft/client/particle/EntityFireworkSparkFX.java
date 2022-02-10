package net.minecraft.client.particle;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

@SideOnly(Side.CLIENT)
public class EntityFireworkSparkFX extends EntityFX {
	private int baseTextureIndex = 160;
	private boolean field_92054_ax;
	private boolean field_92048_ay;
	private final EffectRenderer field_92047_az;
	private float fadeColourRed;
	private float fadeColourGreen;
	private float fadeColourBlue;
	private boolean hasFadeColour;
	private static final String __OBFID = "CL_00000905";

	public EntityFireworkSparkFX(World p_i1207_1_, double p_i1207_2_, double p_i1207_4_, double p_i1207_6_,
			double p_i1207_8_, double p_i1207_10_, double p_i1207_12_, EffectRenderer p_i1207_14_) {
		super(p_i1207_1_, p_i1207_2_, p_i1207_4_, p_i1207_6_);
		motionX = p_i1207_8_;
		motionY = p_i1207_10_;
		motionZ = p_i1207_12_;
		field_92047_az = p_i1207_14_;
		particleScale *= 0.75F;
		particleMaxAge = 48 + rand.nextInt(12);
		noClip = false;
	}

	public void setTrail(boolean p_92045_1_) {
		field_92054_ax = p_92045_1_;
	}

	public void setTwinkle(boolean p_92043_1_) {
		field_92048_ay = p_92043_1_;
	}

	public void setColour(int p_92044_1_) {
		float f = ((p_92044_1_ & 16711680) >> 16) / 255.0F;
		float f1 = ((p_92044_1_ & 65280) >> 8) / 255.0F;
		float f2 = ((p_92044_1_ & 255) >> 0) / 255.0F;
		float f3 = 1.0F;
		setRBGColorF(f * f3, f1 * f3, f2 * f3);
	}

	public void setFadeColour(int p_92046_1_) {
		fadeColourRed = ((p_92046_1_ & 16711680) >> 16) / 255.0F;
		fadeColourGreen = ((p_92046_1_ & 65280) >> 8) / 255.0F;
		fadeColourBlue = ((p_92046_1_ & 255) >> 0) / 255.0F;
		hasFadeColour = true;
	}

	@Override
	public AxisAlignedBB getBoundingBox() {
		return null;
	}

	@Override
	public boolean canBePushed() {
		return false;
	}

	@Override
	public void renderParticle(Tessellator p_70539_1_, float p_70539_2_, float p_70539_3_, float p_70539_4_,
			float p_70539_5_, float p_70539_6_, float p_70539_7_) {
		if (!field_92048_ay || particleAge < particleMaxAge / 3 || (particleAge + particleMaxAge) / 3 % 2 == 0) {
			super.renderParticle(p_70539_1_, p_70539_2_, p_70539_3_, p_70539_4_, p_70539_5_, p_70539_6_, p_70539_7_);
		}
	}

	@Override
	public void onUpdate() {
		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;

		if (particleAge++ >= particleMaxAge) {
			setDead();
		}

		if (particleAge > particleMaxAge / 2) {
			setAlphaF(1.0F - ((float) particleAge - (float) (particleMaxAge / 2)) / particleMaxAge);

			if (hasFadeColour) {
				particleRed += (fadeColourRed - particleRed) * 0.2F;
				particleGreen += (fadeColourGreen - particleGreen) * 0.2F;
				particleBlue += (fadeColourBlue - particleBlue) * 0.2F;
			}
		}

		setParticleTextureIndex(baseTextureIndex + 7 - particleAge * 8 / particleMaxAge);
		motionY -= 0.004D;
		moveEntity(motionX, motionY, motionZ);
		motionX *= 0.9100000262260437D;
		motionY *= 0.9100000262260437D;
		motionZ *= 0.9100000262260437D;

		if (onGround) {
			motionX *= 0.699999988079071D;
			motionZ *= 0.699999988079071D;
		}

		if (field_92054_ax && particleAge < particleMaxAge / 2 && (particleAge + particleMaxAge) % 2 == 0) {
			EntityFireworkSparkFX entityfireworksparkfx = new EntityFireworkSparkFX(worldObj, posX, posY, posZ, 0.0D,
					0.0D, 0.0D, field_92047_az);
			entityfireworksparkfx.setRBGColorF(particleRed, particleGreen, particleBlue);
			entityfireworksparkfx.particleAge = entityfireworksparkfx.particleMaxAge / 2;

			if (hasFadeColour) {
				entityfireworksparkfx.hasFadeColour = true;
				entityfireworksparkfx.fadeColourRed = fadeColourRed;
				entityfireworksparkfx.fadeColourGreen = fadeColourGreen;
				entityfireworksparkfx.fadeColourBlue = fadeColourBlue;
			}

			entityfireworksparkfx.field_92048_ay = field_92048_ay;
			field_92047_az.addEffect(entityfireworksparkfx);
		}
	}

	@Override
	public int getBrightnessForRender(float p_70070_1_) {
		return 15728880;
	}

	@Override
	public float getBrightness(float p_70013_1_) {
		return 1.0F;
	}
}