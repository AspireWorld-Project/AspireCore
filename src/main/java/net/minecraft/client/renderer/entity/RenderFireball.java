package net.minecraft.client.renderer.entity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.init.Items;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

@SideOnly(Side.CLIENT)
public class RenderFireball extends Render {
	private final float field_77002_a;
	private static final String __OBFID = "CL_00000995";

	public RenderFireball(float p_i1254_1_) {
		field_77002_a = p_i1254_1_;
	}

	public void doRender(EntityFireball p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_,
			float p_76986_8_, float p_76986_9_) {
		GL11.glPushMatrix();
		bindEntityTexture(p_76986_1_);
		GL11.glTranslatef((float) p_76986_2_, (float) p_76986_4_, (float) p_76986_6_);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		float f2 = field_77002_a;
		GL11.glScalef(f2 / 1.0F, f2 / 1.0F, f2 / 1.0F);
		IIcon iicon = Items.fire_charge.getIconFromDamage(0);
		Tessellator tessellator = Tessellator.instance;
		float f3 = iicon.getMinU();
		float f4 = iicon.getMaxU();
		float f5 = iicon.getMinV();
		float f6 = iicon.getMaxV();
		float f7 = 1.0F;
		float f8 = 0.5F;
		float f9 = 0.25F;
		GL11.glRotatef(180.0F - renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(-renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, 1.0F, 0.0F);
		tessellator.addVertexWithUV(0.0F - f8, 0.0F - f9, 0.0D, f3, f6);
		tessellator.addVertexWithUV(f7 - f8, 0.0F - f9, 0.0D, f4, f6);
		tessellator.addVertexWithUV(f7 - f8, 1.0F - f9, 0.0D, f4, f5);
		tessellator.addVertexWithUV(0.0F - f8, 1.0F - f9, 0.0D, f3, f5);
		tessellator.draw();
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		GL11.glPopMatrix();
	}

	protected ResourceLocation getEntityTexture(EntityFireball p_110775_1_) {
		return TextureMap.locationItemsTexture;
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity p_110775_1_) {
		return this.getEntityTexture((EntityFireball) p_110775_1_);
	}

	@Override
	public void doRender(Entity p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_,
			float p_76986_9_) {
		this.doRender((EntityFireball) p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
	}
}