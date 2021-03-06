package net.minecraft.client.particle;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

@SideOnly(Side.CLIENT)
public class EntityRainFX extends EntityFX {
	private static final String __OBFID = "CL_00000934";

	public EntityRainFX(World p_i1235_1_, double p_i1235_2_, double p_i1235_4_, double p_i1235_6_) {
		super(p_i1235_1_, p_i1235_2_, p_i1235_4_, p_i1235_6_, 0.0D, 0.0D, 0.0D);
		motionX *= 0.30000001192092896D;
		motionY = (float) Math.random() * 0.2F + 0.1F;
		motionZ *= 0.30000001192092896D;
		particleRed = 1.0F;
		particleGreen = 1.0F;
		particleBlue = 1.0F;
		setParticleTextureIndex(19 + rand.nextInt(4));
		setSize(0.01F, 0.01F);
		particleGravity = 0.06F;
		particleMaxAge = (int) (8.0D / (Math.random() * 0.8D + 0.2D));
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

		if (particleMaxAge-- <= 0) {
			setDead();
		}

		if (onGround) {
			if (Math.random() < 0.5D) {
				setDead();
			}

			motionX *= 0.699999988079071D;
			motionZ *= 0.699999988079071D;
		}

		Material material = worldObj
				.getBlock(MathHelper.floor_double(posX), MathHelper.floor_double(posY), MathHelper.floor_double(posZ))
				.getMaterial();

		if (material.isLiquid() || material.isSolid()) {
			double d0 = MathHelper.floor_double(posY) + 1
					- BlockLiquid.getLiquidHeightPercent(worldObj.getBlockMetadata(MathHelper.floor_double(posX),
							MathHelper.floor_double(posY), MathHelper.floor_double(posZ)));

			if (posY < d0) {
				setDead();
			}
		}
	}
}