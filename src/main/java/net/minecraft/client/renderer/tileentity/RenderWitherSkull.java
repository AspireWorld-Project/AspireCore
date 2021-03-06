package net.minecraft.client.renderer.tileentity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelSkeletonHead;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityWitherSkull;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

@SideOnly(Side.CLIENT)
public class RenderWitherSkull extends Render {
	private static final ResourceLocation invulnerableWitherTextures = new ResourceLocation(
			"textures/entity/wither/wither_invulnerable.png");
	private static final ResourceLocation witherTextures = new ResourceLocation("textures/entity/wither/wither.png");
	private final ModelSkeletonHead skeletonHeadModel = new ModelSkeletonHead();
	private static final String __OBFID = "CL_00001035";

	private float func_82400_a(float p_82400_1_, float p_82400_2_, float p_82400_3_) {
		float f3;

		for (f3 = p_82400_2_ - p_82400_1_; f3 < -180.0F; f3 += 360.0F) {
		}

		while (f3 >= 180.0F) {
			f3 -= 360.0F;
		}

		return p_82400_1_ + p_82400_3_ * f3;
	}

	public void doRender(EntityWitherSkull p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_,
			float p_76986_8_, float p_76986_9_) {
		GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_CULL_FACE);
		float f2 = func_82400_a(p_76986_1_.prevRotationYaw, p_76986_1_.rotationYaw, p_76986_9_);
		float f3 = p_76986_1_.prevRotationPitch
				+ (p_76986_1_.rotationPitch - p_76986_1_.prevRotationPitch) * p_76986_9_;
		GL11.glTranslatef((float) p_76986_2_, (float) p_76986_4_, (float) p_76986_6_);
		float f4 = 0.0625F;
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glScalef(-1.0F, -1.0F, 1.0F);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		bindEntityTexture(p_76986_1_);
		skeletonHeadModel.render(p_76986_1_, 0.0F, 0.0F, 0.0F, f2, f3, f4);
		GL11.glPopMatrix();
	}

	protected ResourceLocation getEntityTexture(EntityWitherSkull p_110775_1_) {
		return p_110775_1_.isInvulnerable() ? invulnerableWitherTextures : witherTextures;
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity p_110775_1_) {
		return this.getEntityTexture((EntityWitherSkull) p_110775_1_);
	}

	@Override
	public void doRender(Entity p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_,
			float p_76986_9_) {
		this.doRender((EntityWitherSkull) p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
	}
}