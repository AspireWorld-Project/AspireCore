package net.minecraft.client.renderer.entity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderWolf extends RenderLiving {
	private static final ResourceLocation wolfTextures = new ResourceLocation("textures/entity/wolf/wolf.png");
	private static final ResourceLocation tamedWolfTextures = new ResourceLocation(
			"textures/entity/wolf/wolf_tame.png");
	private static final ResourceLocation anrgyWolfTextures = new ResourceLocation(
			"textures/entity/wolf/wolf_angry.png");
	private static final ResourceLocation wolfCollarTextures = new ResourceLocation(
			"textures/entity/wolf/wolf_collar.png");
	private static final String __OBFID = "CL_00001036";

	public RenderWolf(ModelBase p_i1269_1_, ModelBase p_i1269_2_, float p_i1269_3_) {
		super(p_i1269_1_, p_i1269_3_);
		setRenderPassModel(p_i1269_2_);
	}

	protected float handleRotationFloat(EntityWolf p_77044_1_, float p_77044_2_) {
		return p_77044_1_.getTailRotation();
	}

	protected int shouldRenderPass(EntityWolf p_77032_1_, int p_77032_2_, float p_77032_3_) {
		if (p_77032_2_ == 0 && p_77032_1_.getWolfShaking()) {
			float f1 = p_77032_1_.getBrightness(p_77032_3_) * p_77032_1_.getShadingWhileShaking(p_77032_3_);
			bindTexture(wolfTextures);
			GL11.glColor3f(f1, f1, f1);
			return 1;
		} else if (p_77032_2_ == 1 && p_77032_1_.isTamed()) {
			bindTexture(wolfCollarTextures);
			int j = p_77032_1_.getCollarColor();
			GL11.glColor3f(EntitySheep.fleeceColorTable[j][0], EntitySheep.fleeceColorTable[j][1],
					EntitySheep.fleeceColorTable[j][2]);
			return 1;
		} else
			return -1;
	}

	protected ResourceLocation getEntityTexture(EntityWolf p_110775_1_) {
		return p_110775_1_.isTamed() ? tamedWolfTextures : p_110775_1_.isAngry() ? anrgyWolfTextures : wolfTextures;
	}

	@Override
	protected int shouldRenderPass(EntityLivingBase p_77032_1_, int p_77032_2_, float p_77032_3_) {
		return this.shouldRenderPass((EntityWolf) p_77032_1_, p_77032_2_, p_77032_3_);
	}

	@Override
	protected float handleRotationFloat(EntityLivingBase p_77044_1_, float p_77044_2_) {
		return this.handleRotationFloat((EntityWolf) p_77044_1_, p_77044_2_);
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity p_110775_1_) {
		return this.getEntityTexture((EntityWolf) p_110775_1_);
	}
}