package net.minecraft.client.particle;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

@SideOnly(Side.CLIENT)
public class EntityCrit2FX extends EntityFX {
	private Entity theEntity;
	private int currentLife;
	private int maximumLife;
	private String particleName;
	private static final String __OBFID = "CL_00000899";

	public EntityCrit2FX(World p_i1199_1_, Entity p_i1199_2_) {
		this(p_i1199_1_, p_i1199_2_, "crit");
	}

	public EntityCrit2FX(World p_i1200_1_, Entity p_i1200_2_, String p_i1200_3_) {
		super(p_i1200_1_, p_i1200_2_.posX, p_i1200_2_.boundingBox.minY + p_i1200_2_.height / 2.0F, p_i1200_2_.posZ,
				p_i1200_2_.motionX, p_i1200_2_.motionY, p_i1200_2_.motionZ);
		theEntity = p_i1200_2_;
		maximumLife = 3;
		particleName = p_i1200_3_;
		onUpdate();
	}

	@Override
	public void renderParticle(Tessellator p_70539_1_, float p_70539_2_, float p_70539_3_, float p_70539_4_,
			float p_70539_5_, float p_70539_6_, float p_70539_7_) {
	}

	@Override
	public void onUpdate() {
		for (int i = 0; i < 16; ++i) {
			double d0 = rand.nextFloat() * 2.0F - 1.0F;
			double d1 = rand.nextFloat() * 2.0F - 1.0F;
			double d2 = rand.nextFloat() * 2.0F - 1.0F;

			if (d0 * d0 + d1 * d1 + d2 * d2 <= 1.0D) {
				double d3 = theEntity.posX + d0 * theEntity.width / 4.0D;
				double d4 = theEntity.boundingBox.minY + theEntity.height / 2.0F + d1 * theEntity.height / 4.0D;
				double d5 = theEntity.posZ + d2 * theEntity.width / 4.0D;
				worldObj.spawnParticle(particleName, d3, d4, d5, d0, d1 + 0.2D, d2);
			}
		}

		++currentLife;

		if (currentLife >= maximumLife) {
			setDead();
		}
	}

	@Override
	public int getFXLayer() {
		return 3;
	}
}