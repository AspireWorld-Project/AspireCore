package net.minecraft.client.particle;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.World;

@SideOnly(Side.CLIENT)
public class EntitySpellParticleFX extends EntityFX {
	private int baseSpellTextureIndex = 128;
	private static final String __OBFID = "CL_00000926";

	public EntitySpellParticleFX(World p_i1229_1_, double p_i1229_2_, double p_i1229_4_, double p_i1229_6_,
			double p_i1229_8_, double p_i1229_10_, double p_i1229_12_) {
		super(p_i1229_1_, p_i1229_2_, p_i1229_4_, p_i1229_6_, p_i1229_8_, p_i1229_10_, p_i1229_12_);
		motionY *= 0.20000000298023224D;

		if (p_i1229_8_ == 0.0D && p_i1229_12_ == 0.0D) {
			motionX *= 0.10000000149011612D;
			motionZ *= 0.10000000149011612D;
		}

		particleScale *= 0.75F;
		particleMaxAge = (int) (8.0D / (Math.random() * 0.8D + 0.2D));
		noClip = false;
	}

	@Override
	public void renderParticle(Tessellator p_70539_1_, float p_70539_2_, float p_70539_3_, float p_70539_4_,
			float p_70539_5_, float p_70539_6_, float p_70539_7_) {
		float f6 = (particleAge + p_70539_2_) / particleMaxAge * 32.0F;

		if (f6 < 0.0F) {
			f6 = 0.0F;
		}

		if (f6 > 1.0F) {
			f6 = 1.0F;
		}

		super.renderParticle(p_70539_1_, p_70539_2_, p_70539_3_, p_70539_4_, p_70539_5_, p_70539_6_, p_70539_7_);
	}

	@Override
	public void onUpdate() {
		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;

		if (particleAge++ >= particleMaxAge) {
			setDead();
		}

		setParticleTextureIndex(baseSpellTextureIndex + 7 - particleAge * 8 / particleMaxAge);
		motionY += 0.004D;
		moveEntity(motionX, motionY, motionZ);

		if (posY == prevPosY) {
			motionX *= 1.1D;
			motionZ *= 1.1D;
		}

		motionX *= 0.9599999785423279D;
		motionY *= 0.9599999785423279D;
		motionZ *= 0.9599999785423279D;

		if (onGround) {
			motionX *= 0.699999988079071D;
			motionZ *= 0.699999988079071D;
		}
	}

	public void setBaseSpellTextureIndex(int p_70589_1_) {
		baseSpellTextureIndex = p_70589_1_;
	}
}