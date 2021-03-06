package net.minecraft.client.renderer.entity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelMagmaCube;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderMagmaCube extends RenderLiving {
	private static final ResourceLocation magmaCubeTextures = new ResourceLocation(
			"textures/entity/slime/magmacube.png");
	private static final String __OBFID = "CL_00001009";

	public RenderMagmaCube() {
		super(new ModelMagmaCube(), 0.25F);
	}

	protected ResourceLocation getEntityTexture(EntityMagmaCube p_110775_1_) {
		return magmaCubeTextures;
	}

	protected void preRenderCallback(EntityMagmaCube p_77041_1_, float p_77041_2_) {
		int i = p_77041_1_.getSlimeSize();
		float f1 = (p_77041_1_.prevSquishFactor + (p_77041_1_.squishFactor - p_77041_1_.prevSquishFactor) * p_77041_2_)
				/ (i * 0.5F + 1.0F);
		float f2 = 1.0F / (f1 + 1.0F);
		float f3 = i;
		GL11.glScalef(f2 * f3, 1.0F / f2 * f3, f2 * f3);
	}

	@Override
	protected void preRenderCallback(EntityLivingBase p_77041_1_, float p_77041_2_) {
		this.preRenderCallback((EntityMagmaCube) p_77041_1_, p_77041_2_);
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity p_110775_1_) {
		return this.getEntityTexture((EntityMagmaCube) p_110775_1_);
	}
}