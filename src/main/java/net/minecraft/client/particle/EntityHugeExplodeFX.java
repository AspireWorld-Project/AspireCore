package net.minecraft.client.particle;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.World;

@SideOnly(Side.CLIENT)
public class EntityHugeExplodeFX extends EntityFX {
	private int timeSinceStart;
	private int maximumTime = 8;
	private static final String __OBFID = "CL_00000911";

	public EntityHugeExplodeFX(World p_i1214_1_, double p_i1214_2_, double p_i1214_4_, double p_i1214_6_,
			double p_i1214_8_, double p_i1214_10_, double p_i1214_12_) {
		super(p_i1214_1_, p_i1214_2_, p_i1214_4_, p_i1214_6_, 0.0D, 0.0D, 0.0D);
	}

	@Override
	public void renderParticle(Tessellator p_70539_1_, float p_70539_2_, float p_70539_3_, float p_70539_4_,
			float p_70539_5_, float p_70539_6_, float p_70539_7_) {
	}

	@Override
	public void onUpdate() {
		for (int i = 0; i < 6; ++i) {
			double d0 = posX + (rand.nextDouble() - rand.nextDouble()) * 4.0D;
			double d1 = posY + (rand.nextDouble() - rand.nextDouble()) * 4.0D;
			double d2 = posZ + (rand.nextDouble() - rand.nextDouble()) * 4.0D;
			worldObj.spawnParticle("largeexplode", d0, d1, d2, (float) timeSinceStart / (float) maximumTime, 0.0D,
					0.0D);
		}

		++timeSinceStart;

		if (timeSinceStart == maximumTime) {
			setDead();
		}
	}

	@Override
	public int getFXLayer() {
		return 1;
	}
}