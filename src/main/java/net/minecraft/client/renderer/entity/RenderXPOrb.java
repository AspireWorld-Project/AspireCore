package net.minecraft.client.renderer.entity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

@SideOnly(Side.CLIENT)
public class RenderXPOrb extends Render {
	private static final ResourceLocation experienceOrbTextures = new ResourceLocation(
			"textures/entity/experience_orb.png");
	private static final String __OBFID = "CL_00000993";

	public RenderXPOrb() {
		shadowSize = 0.15F;
		shadowOpaque = 0.75F;
	}

	public void doRender(EntityXPOrb p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_,
			float p_76986_8_, float p_76986_9_) {
		GL11.glPushMatrix();
		GL11.glTranslatef((float) p_76986_2_, (float) p_76986_4_, (float) p_76986_6_);
		bindEntityTexture(p_76986_1_);
		int i = p_76986_1_.getTextureByXP();
		float f2 = (i % 4 * 16 + 0) / 64.0F;
		float f3 = (i % 4 * 16 + 16) / 64.0F;
		float f4 = (i / 4 * 16 + 0) / 64.0F;
		float f5 = (i / 4 * 16 + 16) / 64.0F;
		float f6 = 1.0F;
		float f7 = 0.5F;
		float f8 = 0.25F;
		int j = p_76986_1_.getBrightnessForRender(p_76986_9_);
		int k = j % 65536;
		int l = j / 65536;
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, k / 1.0F, l / 1.0F);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		float f10 = 255.0F;
		float f11 = (p_76986_1_.xpColor + p_76986_9_) / 2.0F;
		l = (int) ((MathHelper.sin(f11 + 0.0F) + 1.0F) * 0.5F * f10);
		int i1 = (int) f10;
		int j1 = (int) ((MathHelper.sin(f11 + 4.1887903F) + 1.0F) * 0.1F * f10);
		int k1 = l << 16 | i1 << 8 | j1;
		GL11.glRotatef(180.0F - renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(-renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
		float f9 = 0.3F;
		GL11.glScalef(f9, f9, f9);
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		tessellator.setColorRGBA_I(k1, 128);
		tessellator.setNormal(0.0F, 1.0F, 0.0F);
		tessellator.addVertexWithUV(0.0F - f7, 0.0F - f8, 0.0D, f2, f5);
		tessellator.addVertexWithUV(f6 - f7, 0.0F - f8, 0.0D, f3, f5);
		tessellator.addVertexWithUV(f6 - f7, 1.0F - f8, 0.0D, f3, f4);
		tessellator.addVertexWithUV(0.0F - f7, 1.0F - f8, 0.0D, f2, f4);
		tessellator.draw();
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		GL11.glPopMatrix();
	}

	protected ResourceLocation getEntityTexture(EntityXPOrb p_110775_1_) {
		return experienceOrbTextures;
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity p_110775_1_) {
		return this.getEntityTexture((EntityXPOrb) p_110775_1_);
	}

	@Override
	public void doRender(Entity p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_,
			float p_76986_9_) {
		this.doRender((EntityXPOrb) p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
	}
}