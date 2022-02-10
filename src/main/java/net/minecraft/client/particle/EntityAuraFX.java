package net.minecraft.client.particle;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.world.World;

@SideOnly(Side.CLIENT)
public class EntityAuraFX extends EntityFX {
	private static final String __OBFID = "CL_00000929";

	public EntityAuraFX(World p_i1232_1_, double p_i1232_2_, double p_i1232_4_, double p_i1232_6_, double p_i1232_8_,
			double p_i1232_10_, double p_i1232_12_) {
		super(p_i1232_1_, p_i1232_2_, p_i1232_4_, p_i1232_6_, p_i1232_8_, p_i1232_10_, p_i1232_12_);
		float f = rand.nextFloat() * 0.1F + 0.2F;
		particleRed = f;
		particleGreen = f;
		particleBlue = f;
		setParticleTextureIndex(0);
		setSize(0.02F, 0.02F);
		particleScale *= rand.nextFloat() * 0.6F + 0.5F;
		motionX *= 0.019999999552965164D;
		motionY *= 0.019999999552965164D;
		motionZ *= 0.019999999552965164D;
		particleMaxAge = (int) (20.0D / (Math.random() * 0.8D + 0.2D));
		noClip = true;
	}

	@Override
	public void onUpdate() {
		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;
		moveEntity(motionX, motionY, motionZ);
		motionX *= 0.99D;
		motionY *= 0.99D;
		motionZ *= 0.99D;

		if (particleMaxAge-- <= 0) {
			setDead();
		}
	}
}