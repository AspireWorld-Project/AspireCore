package net.minecraft.client.particle;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.world.World;

@SideOnly(Side.CLIENT)
public class EntityFishWakeFX extends EntityFX {
	private static final String __OBFID = "CL_00000933";

	public EntityFishWakeFX(World p_i45073_1_, double p_i45073_2_, double p_i45073_4_, double p_i45073_6_,
			double p_i45073_8_, double p_i45073_10_, double p_i45073_12_) {
		super(p_i45073_1_, p_i45073_2_, p_i45073_4_, p_i45073_6_, 0.0D, 0.0D, 0.0D);
		motionX *= 0.30000001192092896D;
		motionY = (float) Math.random() * 0.2F + 0.1F;
		motionZ *= 0.30000001192092896D;
		particleRed = 1.0F;
		particleGreen = 1.0F;
		particleBlue = 1.0F;
		setParticleTextureIndex(19);
		setSize(0.01F, 0.01F);
		particleMaxAge = (int) (8.0D / (Math.random() * 0.8D + 0.2D));
		particleGravity = 0.0F;
		motionX = p_i45073_8_;
		motionY = p_i45073_10_;
		motionZ = p_i45073_12_;
	}

	@Override
	public void onUpdate() {
		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;
		motionY -= particleGravity;
		moveEntity(motionX, motionY, motionZ);
		motionX *= 0.9800000190734863D;
		motionY *= 0.9800000190734863D;
		motionZ *= 0.9800000190734863D;
		int i = 60 - particleMaxAge;
		float f = i * 0.001F;
		setSize(f, f);
		setParticleTextureIndex(19 + i % 4);

		if (particleMaxAge-- <= 0) {
			setDead();
		}
	}
}