package net.minecraft.client.particle;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

@SideOnly(Side.CLIENT)
public class EntityFX extends Entity {
	protected int particleTextureIndexX;
	protected int particleTextureIndexY;
	protected float particleTextureJitterX;
	protected float particleTextureJitterY;
	protected int particleAge;
	protected int particleMaxAge;
	protected float particleScale;
	protected float particleGravity;
	protected float particleRed;
	protected float particleGreen;
	protected float particleBlue;
	protected float particleAlpha;
	protected IIcon particleIcon;
	public static double interpPosX;
	public static double interpPosY;
	public static double interpPosZ;
	private static final String __OBFID = "CL_00000914";

	protected EntityFX(World p_i1218_1_, double p_i1218_2_, double p_i1218_4_, double p_i1218_6_) {
		super(p_i1218_1_);
		particleAlpha = 1.0F;
		setSize(0.2F, 0.2F);
		yOffset = height / 2.0F;
		setPosition(p_i1218_2_, p_i1218_4_, p_i1218_6_);
		lastTickPosX = p_i1218_2_;
		lastTickPosY = p_i1218_4_;
		lastTickPosZ = p_i1218_6_;
		particleRed = particleGreen = particleBlue = 1.0F;
		particleTextureJitterX = rand.nextFloat() * 3.0F;
		particleTextureJitterY = rand.nextFloat() * 3.0F;
		particleScale = (rand.nextFloat() * 0.5F + 0.5F) * 2.0F;
		particleMaxAge = (int) (4.0F / (rand.nextFloat() * 0.9F + 0.1F));
		particleAge = 0;
	}

	public EntityFX(World p_i1219_1_, double p_i1219_2_, double p_i1219_4_, double p_i1219_6_, double p_i1219_8_,
			double p_i1219_10_, double p_i1219_12_) {
		this(p_i1219_1_, p_i1219_2_, p_i1219_4_, p_i1219_6_);
		motionX = p_i1219_8_ + (float) (Math.random() * 2.0D - 1.0D) * 0.4F;
		motionY = p_i1219_10_ + (float) (Math.random() * 2.0D - 1.0D) * 0.4F;
		motionZ = p_i1219_12_ + (float) (Math.random() * 2.0D - 1.0D) * 0.4F;
		float f = (float) (Math.random() + Math.random() + 1.0D) * 0.15F;
		float f1 = MathHelper.sqrt_double(motionX * motionX + motionY * motionY + motionZ * motionZ);
		motionX = motionX / f1 * f * 0.4000000059604645D;
		motionY = motionY / f1 * f * 0.4000000059604645D + 0.10000000149011612D;
		motionZ = motionZ / f1 * f * 0.4000000059604645D;
	}

	public EntityFX multiplyVelocity(float p_70543_1_) {
		motionX *= p_70543_1_;
		motionY = (motionY - 0.10000000149011612D) * p_70543_1_ + 0.10000000149011612D;
		motionZ *= p_70543_1_;
		return this;
	}

	public EntityFX multipleParticleScaleBy(float p_70541_1_) {
		setSize(0.2F * p_70541_1_, 0.2F * p_70541_1_);
		particleScale *= p_70541_1_;
		return this;
	}

	public void setRBGColorF(float p_70538_1_, float p_70538_2_, float p_70538_3_) {
		particleRed = p_70538_1_;
		particleGreen = p_70538_2_;
		particleBlue = p_70538_3_;
	}

	public void setAlphaF(float p_82338_1_) {
		particleAlpha = p_82338_1_;
	}

	public float getRedColorF() {
		return particleRed;
	}

	public float getGreenColorF() {
		return particleGreen;
	}

	public float getBlueColorF() {
		return particleBlue;
	}

	@Override
	protected boolean canTriggerWalking() {
		return false;
	}

	@Override
	protected void entityInit() {
	}

	@Override
	public void onUpdate() {
		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;

		if (particleAge++ >= particleMaxAge) {
			setDead();
		}

		motionY -= 0.04D * particleGravity;
		moveEntity(motionX, motionY, motionZ);
		motionX *= 0.9800000190734863D;
		motionY *= 0.9800000190734863D;
		motionZ *= 0.9800000190734863D;

		if (onGround) {
			motionX *= 0.699999988079071D;
			motionZ *= 0.699999988079071D;
		}
	}

	public void renderParticle(Tessellator p_70539_1_, float p_70539_2_, float p_70539_3_, float p_70539_4_,
			float p_70539_5_, float p_70539_6_, float p_70539_7_) {
		float f6 = particleTextureIndexX / 16.0F;
		float f7 = f6 + 0.0624375F;
		float f8 = particleTextureIndexY / 16.0F;
		float f9 = f8 + 0.0624375F;
		float f10 = 0.1F * particleScale;

		if (particleIcon != null) {
			f6 = particleIcon.getMinU();
			f7 = particleIcon.getMaxU();
			f8 = particleIcon.getMinV();
			f9 = particleIcon.getMaxV();
		}

		float f11 = (float) (prevPosX + (posX - prevPosX) * p_70539_2_ - interpPosX);
		float f12 = (float) (prevPosY + (posY - prevPosY) * p_70539_2_ - interpPosY);
		float f13 = (float) (prevPosZ + (posZ - prevPosZ) * p_70539_2_ - interpPosZ);
		p_70539_1_.setColorRGBA_F(particleRed, particleGreen, particleBlue, particleAlpha);
		p_70539_1_.addVertexWithUV(f11 - p_70539_3_ * f10 - p_70539_6_ * f10, f12 - p_70539_4_ * f10,
				f13 - p_70539_5_ * f10 - p_70539_7_ * f10, f7, f9);
		p_70539_1_.addVertexWithUV(f11 - p_70539_3_ * f10 + p_70539_6_ * f10, f12 + p_70539_4_ * f10,
				f13 - p_70539_5_ * f10 + p_70539_7_ * f10, f7, f8);
		p_70539_1_.addVertexWithUV(f11 + p_70539_3_ * f10 + p_70539_6_ * f10, f12 + p_70539_4_ * f10,
				f13 + p_70539_5_ * f10 + p_70539_7_ * f10, f6, f8);
		p_70539_1_.addVertexWithUV(f11 + p_70539_3_ * f10 - p_70539_6_ * f10, f12 - p_70539_4_ * f10,
				f13 + p_70539_5_ * f10 - p_70539_7_ * f10, f6, f9);
	}

	public int getFXLayer() {
		return 0;
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound p_70014_1_) {
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound p_70037_1_) {
	}

	public void setParticleIcon(IIcon p_110125_1_) {
		if (getFXLayer() == 1) {
			particleIcon = p_110125_1_;
		} else {
			if (getFXLayer() != 2)
				throw new RuntimeException("Invalid call to Particle.setTex, use coordinate methods");

			particleIcon = p_110125_1_;
		}
	}

	public void setParticleTextureIndex(int p_70536_1_) {
		if (getFXLayer() != 0)
			throw new RuntimeException("Invalid call to Particle.setMiscTex");
		else {
			particleTextureIndexX = p_70536_1_ % 16;
			particleTextureIndexY = p_70536_1_ / 16;
		}
	}

	public void nextTextureIndexX() {
		++particleTextureIndexX;
	}

	@Override
	public boolean canAttackWithItem() {
		return false;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + ", Pos (" + posX + "," + posY + "," + posZ + "), RGBA (" + particleRed
				+ "," + particleGreen + "," + particleBlue + "," + particleAlpha + "), Age " + particleAge;
	}
}