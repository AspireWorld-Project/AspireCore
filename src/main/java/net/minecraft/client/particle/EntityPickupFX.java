package net.minecraft.client.particle;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class EntityPickupFX extends EntityFX {
	private Entity entityToPickUp;
	private Entity entityPickingUp;
	private int age;
	private int maxAge;
	private float yOffs;
	private static final String __OBFID = "CL_00000930";

	public EntityPickupFX(World p_i1233_1_, Entity p_i1233_2_, Entity p_i1233_3_, float p_i1233_4_) {
		super(p_i1233_1_, p_i1233_2_.posX, p_i1233_2_.posY, p_i1233_2_.posZ, p_i1233_2_.motionX, p_i1233_2_.motionY,
				p_i1233_2_.motionZ);
		entityToPickUp = p_i1233_2_;
		entityPickingUp = p_i1233_3_;
		maxAge = 3;
		yOffs = p_i1233_4_;
	}

	@Override
	public void renderParticle(Tessellator p_70539_1_, float p_70539_2_, float p_70539_3_, float p_70539_4_,
			float p_70539_5_, float p_70539_6_, float p_70539_7_) {
		float f6 = (age + p_70539_2_) / maxAge;
		f6 *= f6;
		double d0 = entityToPickUp.posX;
		double d1 = entityToPickUp.posY;
		double d2 = entityToPickUp.posZ;
		double d3 = entityPickingUp.lastTickPosX + (entityPickingUp.posX - entityPickingUp.lastTickPosX) * p_70539_2_;
		double d4 = entityPickingUp.lastTickPosY + (entityPickingUp.posY - entityPickingUp.lastTickPosY) * p_70539_2_
				+ yOffs;
		double d5 = entityPickingUp.lastTickPosZ + (entityPickingUp.posZ - entityPickingUp.lastTickPosZ) * p_70539_2_;
		double d6 = d0 + (d3 - d0) * f6;
		double d7 = d1 + (d4 - d1) * f6;
		double d8 = d2 + (d5 - d2) * f6;
		int i = getBrightnessForRender(p_70539_2_);
		int j = i % 65536;
		int k = i / 65536;
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, j / 1.0F, k / 1.0F);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		d6 -= interpPosX;
		d7 -= interpPosY;
		d8 -= interpPosZ;
		RenderManager.instance.renderEntityWithPosYaw(entityToPickUp, (float) d6, (float) d7, (float) d8,
				entityToPickUp.rotationYaw, p_70539_2_);
	}

	@Override
	public void onUpdate() {
		++age;

		if (age == maxAge) {
			setDead();
		}
	}

	@Override
	public int getFXLayer() {
		return 3;
	}
}