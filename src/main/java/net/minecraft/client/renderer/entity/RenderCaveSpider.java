package net.minecraft.client.renderer.entity;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class RenderCaveSpider extends RenderSpider {
	private static final ResourceLocation caveSpiderTextures = new ResourceLocation(
			"textures/entity/spider/cave_spider.png");
	private static final String __OBFID = "CL_00000982";

	public RenderCaveSpider() {
		shadowSize *= 0.7F;
	}

	protected void preRenderCallback(EntityCaveSpider p_77041_1_, float p_77041_2_) {
		GL11.glScalef(0.7F, 0.7F, 0.7F);
	}

	protected ResourceLocation getEntityTexture(EntityCaveSpider p_110775_1_) {
		return caveSpiderTextures;
	}

	@Override
	protected ResourceLocation getEntityTexture(EntitySpider p_110775_1_) {
		return this.getEntityTexture((EntityCaveSpider) p_110775_1_);
	}

	@Override
	protected void preRenderCallback(EntityLivingBase p_77041_1_, float p_77041_2_) {
		this.preRenderCallback((EntityCaveSpider) p_77041_1_, p_77041_2_);
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity p_110775_1_) {
		return this.getEntityTexture((EntityCaveSpider) p_110775_1_);
	}
}