package net.minecraft.client.renderer.entity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public abstract class Render {
	private static final ResourceLocation shadowTextures = new ResourceLocation("textures/misc/shadow.png");
	protected RenderManager renderManager;
	protected RenderBlocks field_147909_c = new RenderBlocks();
	protected float shadowSize;
	protected float shadowOpaque = 1.0F;
	private final boolean staticEntity = false;
	private static final String __OBFID = "CL_00000992";

	public abstract void doRender(Entity p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_,
			float p_76986_8_, float p_76986_9_);

	protected abstract ResourceLocation getEntityTexture(Entity p_110775_1_);

	public boolean isStaticEntity() {
		return staticEntity;
	}

	protected void bindEntityTexture(Entity p_110777_1_) {
		bindTexture(getEntityTexture(p_110777_1_));
	}

	protected void bindTexture(ResourceLocation p_110776_1_) {
		renderManager.renderEngine.bindTexture(p_110776_1_);
	}

	private void renderEntityOnFire(Entity p_76977_1_, double p_76977_2_, double p_76977_4_, double p_76977_6_,
			float p_76977_8_) {
		GL11.glDisable(GL11.GL_LIGHTING);
		IIcon iicon = Blocks.fire.getFireIcon(0);
		IIcon iicon1 = Blocks.fire.getFireIcon(1);
		GL11.glPushMatrix();
		GL11.glTranslatef((float) p_76977_2_, (float) p_76977_4_, (float) p_76977_6_);
		float f1 = p_76977_1_.width * 1.4F;
		GL11.glScalef(f1, f1, f1);
		Tessellator tessellator = Tessellator.instance;
		float f2 = 0.5F;
		float f3 = 0.0F;
		float f4 = p_76977_1_.height / f1;
		float f5 = (float) (p_76977_1_.posY - p_76977_1_.boundingBox.minY);
		GL11.glRotatef(-renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
		GL11.glTranslatef(0.0F, 0.0F, -0.3F + (int) f4 * 0.02F);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		float f6 = 0.0F;
		int i = 0;
		tessellator.startDrawingQuads();

		while (f4 > 0.0F) {
			IIcon iicon2 = i % 2 == 0 ? iicon : iicon1;
			bindTexture(TextureMap.locationBlocksTexture);
			float f7 = iicon2.getMinU();
			float f8 = iicon2.getMinV();
			float f9 = iicon2.getMaxU();
			float f10 = iicon2.getMaxV();

			if (i / 2 % 2 == 0) {
				float f11 = f9;
				f9 = f7;
				f7 = f11;
			}

			tessellator.addVertexWithUV(f2 - f3, 0.0F - f5, f6, f9, f10);
			tessellator.addVertexWithUV(-f2 - f3, 0.0F - f5, f6, f7, f10);
			tessellator.addVertexWithUV(-f2 - f3, 1.4F - f5, f6, f7, f8);
			tessellator.addVertexWithUV(f2 - f3, 1.4F - f5, f6, f9, f8);
			f4 -= 0.45F;
			f5 -= 0.45F;
			f2 *= 0.9F;
			f6 += 0.03F;
			++i;
		}

		tessellator.draw();
		GL11.glPopMatrix();
		GL11.glEnable(GL11.GL_LIGHTING);
	}

	private void renderShadow(Entity p_76975_1_, double p_76975_2_, double p_76975_4_, double p_76975_6_,
			float p_76975_8_, float p_76975_9_) {
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		renderManager.renderEngine.bindTexture(shadowTextures);
		World world = getWorldFromRenderManager();
		GL11.glDepthMask(false);
		float f2 = shadowSize;

		if (p_76975_1_ instanceof EntityLiving) {
			EntityLiving entityliving = (EntityLiving) p_76975_1_;
			f2 *= entityliving.getRenderSizeModifier();

			if (entityliving.isChild()) {
				f2 *= 0.5F;
			}
		}

		double d8 = p_76975_1_.lastTickPosX + (p_76975_1_.posX - p_76975_1_.lastTickPosX) * p_76975_9_;
		double d3 = p_76975_1_.lastTickPosY + (p_76975_1_.posY - p_76975_1_.lastTickPosY) * p_76975_9_
				+ p_76975_1_.getShadowSize();
		double d4 = p_76975_1_.lastTickPosZ + (p_76975_1_.posZ - p_76975_1_.lastTickPosZ) * p_76975_9_;
		int i = MathHelper.floor_double(d8 - f2);
		int j = MathHelper.floor_double(d8 + f2);
		int k = MathHelper.floor_double(d3 - f2);
		int l = MathHelper.floor_double(d3);
		int i1 = MathHelper.floor_double(d4 - f2);
		int j1 = MathHelper.floor_double(d4 + f2);
		double d5 = p_76975_2_ - d8;
		double d6 = p_76975_4_ - d3;
		double d7 = p_76975_6_ - d4;
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();

		for (int k1 = i; k1 <= j; ++k1) {
			for (int l1 = k; l1 <= l; ++l1) {
				for (int i2 = i1; i2 <= j1; ++i2) {
					Block block = world.getBlock(k1, l1 - 1, i2);

					if (block.getMaterial() != Material.air && world.getBlockLightValue(k1, l1, i2) > 3) {
						func_147907_a(block, p_76975_2_, p_76975_4_ + p_76975_1_.getShadowSize(), p_76975_6_, k1, l1,
								i2, p_76975_8_, f2, d5, d6 + p_76975_1_.getShadowSize(), d7);
					}
				}
			}
		}

		tessellator.draw();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDepthMask(true);
	}

	private World getWorldFromRenderManager() {
		return renderManager.worldObj;
	}

	private void func_147907_a(Block p_147907_1_, double p_147907_2_, double p_147907_4_, double p_147907_6_,
			int p_147907_8_, int p_147907_9_, int p_147907_10_, float p_147907_11_, float p_147907_12_,
			double p_147907_13_, double p_147907_15_, double p_147907_17_) {
		Tessellator tessellator = Tessellator.instance;

		if (p_147907_1_.renderAsNormalBlock()) {
			double d6 = (p_147907_11_ - (p_147907_4_ - (p_147907_9_ + p_147907_15_)) / 2.0D) * 0.5D
					* getWorldFromRenderManager().getLightBrightness(p_147907_8_, p_147907_9_, p_147907_10_);

			if (d6 >= 0.0D) {
				if (d6 > 1.0D) {
					d6 = 1.0D;
				}

				tessellator.setColorRGBA_F(1.0F, 1.0F, 1.0F, (float) d6);
				double d7 = p_147907_8_ + p_147907_1_.getBlockBoundsMinX() + p_147907_13_;
				double d8 = p_147907_8_ + p_147907_1_.getBlockBoundsMaxX() + p_147907_13_;
				double d9 = p_147907_9_ + p_147907_1_.getBlockBoundsMinY() + p_147907_15_ + 0.015625D;
				double d10 = p_147907_10_ + p_147907_1_.getBlockBoundsMinZ() + p_147907_17_;
				double d11 = p_147907_10_ + p_147907_1_.getBlockBoundsMaxZ() + p_147907_17_;
				float f2 = (float) ((p_147907_2_ - d7) / 2.0D / p_147907_12_ + 0.5D);
				float f3 = (float) ((p_147907_2_ - d8) / 2.0D / p_147907_12_ + 0.5D);
				float f4 = (float) ((p_147907_6_ - d10) / 2.0D / p_147907_12_ + 0.5D);
				float f5 = (float) ((p_147907_6_ - d11) / 2.0D / p_147907_12_ + 0.5D);
				tessellator.addVertexWithUV(d7, d9, d10, f2, f4);
				tessellator.addVertexWithUV(d7, d9, d11, f2, f5);
				tessellator.addVertexWithUV(d8, d9, d11, f3, f5);
				tessellator.addVertexWithUV(d8, d9, d10, f3, f4);
			}
		}
	}

	public static void renderOffsetAABB(AxisAlignedBB p_76978_0_, double p_76978_1_, double p_76978_3_,
			double p_76978_5_) {
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		Tessellator tessellator = Tessellator.instance;
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		tessellator.startDrawingQuads();
		tessellator.setTranslation(p_76978_1_, p_76978_3_, p_76978_5_);
		tessellator.setNormal(0.0F, 0.0F, -1.0F);
		tessellator.addVertex(p_76978_0_.minX, p_76978_0_.maxY, p_76978_0_.minZ);
		tessellator.addVertex(p_76978_0_.maxX, p_76978_0_.maxY, p_76978_0_.minZ);
		tessellator.addVertex(p_76978_0_.maxX, p_76978_0_.minY, p_76978_0_.minZ);
		tessellator.addVertex(p_76978_0_.minX, p_76978_0_.minY, p_76978_0_.minZ);
		tessellator.setNormal(0.0F, 0.0F, 1.0F);
		tessellator.addVertex(p_76978_0_.minX, p_76978_0_.minY, p_76978_0_.maxZ);
		tessellator.addVertex(p_76978_0_.maxX, p_76978_0_.minY, p_76978_0_.maxZ);
		tessellator.addVertex(p_76978_0_.maxX, p_76978_0_.maxY, p_76978_0_.maxZ);
		tessellator.addVertex(p_76978_0_.minX, p_76978_0_.maxY, p_76978_0_.maxZ);
		tessellator.setNormal(0.0F, -1.0F, 0.0F);
		tessellator.addVertex(p_76978_0_.minX, p_76978_0_.minY, p_76978_0_.minZ);
		tessellator.addVertex(p_76978_0_.maxX, p_76978_0_.minY, p_76978_0_.minZ);
		tessellator.addVertex(p_76978_0_.maxX, p_76978_0_.minY, p_76978_0_.maxZ);
		tessellator.addVertex(p_76978_0_.minX, p_76978_0_.minY, p_76978_0_.maxZ);
		tessellator.setNormal(0.0F, 1.0F, 0.0F);
		tessellator.addVertex(p_76978_0_.minX, p_76978_0_.maxY, p_76978_0_.maxZ);
		tessellator.addVertex(p_76978_0_.maxX, p_76978_0_.maxY, p_76978_0_.maxZ);
		tessellator.addVertex(p_76978_0_.maxX, p_76978_0_.maxY, p_76978_0_.minZ);
		tessellator.addVertex(p_76978_0_.minX, p_76978_0_.maxY, p_76978_0_.minZ);
		tessellator.setNormal(-1.0F, 0.0F, 0.0F);
		tessellator.addVertex(p_76978_0_.minX, p_76978_0_.minY, p_76978_0_.maxZ);
		tessellator.addVertex(p_76978_0_.minX, p_76978_0_.maxY, p_76978_0_.maxZ);
		tessellator.addVertex(p_76978_0_.minX, p_76978_0_.maxY, p_76978_0_.minZ);
		tessellator.addVertex(p_76978_0_.minX, p_76978_0_.minY, p_76978_0_.minZ);
		tessellator.setNormal(1.0F, 0.0F, 0.0F);
		tessellator.addVertex(p_76978_0_.maxX, p_76978_0_.minY, p_76978_0_.minZ);
		tessellator.addVertex(p_76978_0_.maxX, p_76978_0_.maxY, p_76978_0_.minZ);
		tessellator.addVertex(p_76978_0_.maxX, p_76978_0_.maxY, p_76978_0_.maxZ);
		tessellator.addVertex(p_76978_0_.maxX, p_76978_0_.minY, p_76978_0_.maxZ);
		tessellator.setTranslation(0.0D, 0.0D, 0.0D);
		tessellator.draw();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}

	public static void renderAABB(AxisAlignedBB p_76980_0_) {
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		tessellator.addVertex(p_76980_0_.minX, p_76980_0_.maxY, p_76980_0_.minZ);
		tessellator.addVertex(p_76980_0_.maxX, p_76980_0_.maxY, p_76980_0_.minZ);
		tessellator.addVertex(p_76980_0_.maxX, p_76980_0_.minY, p_76980_0_.minZ);
		tessellator.addVertex(p_76980_0_.minX, p_76980_0_.minY, p_76980_0_.minZ);
		tessellator.addVertex(p_76980_0_.minX, p_76980_0_.minY, p_76980_0_.maxZ);
		tessellator.addVertex(p_76980_0_.maxX, p_76980_0_.minY, p_76980_0_.maxZ);
		tessellator.addVertex(p_76980_0_.maxX, p_76980_0_.maxY, p_76980_0_.maxZ);
		tessellator.addVertex(p_76980_0_.minX, p_76980_0_.maxY, p_76980_0_.maxZ);
		tessellator.addVertex(p_76980_0_.minX, p_76980_0_.minY, p_76980_0_.minZ);
		tessellator.addVertex(p_76980_0_.maxX, p_76980_0_.minY, p_76980_0_.minZ);
		tessellator.addVertex(p_76980_0_.maxX, p_76980_0_.minY, p_76980_0_.maxZ);
		tessellator.addVertex(p_76980_0_.minX, p_76980_0_.minY, p_76980_0_.maxZ);
		tessellator.addVertex(p_76980_0_.minX, p_76980_0_.maxY, p_76980_0_.maxZ);
		tessellator.addVertex(p_76980_0_.maxX, p_76980_0_.maxY, p_76980_0_.maxZ);
		tessellator.addVertex(p_76980_0_.maxX, p_76980_0_.maxY, p_76980_0_.minZ);
		tessellator.addVertex(p_76980_0_.minX, p_76980_0_.maxY, p_76980_0_.minZ);
		tessellator.addVertex(p_76980_0_.minX, p_76980_0_.minY, p_76980_0_.maxZ);
		tessellator.addVertex(p_76980_0_.minX, p_76980_0_.maxY, p_76980_0_.maxZ);
		tessellator.addVertex(p_76980_0_.minX, p_76980_0_.maxY, p_76980_0_.minZ);
		tessellator.addVertex(p_76980_0_.minX, p_76980_0_.minY, p_76980_0_.minZ);
		tessellator.addVertex(p_76980_0_.maxX, p_76980_0_.minY, p_76980_0_.minZ);
		tessellator.addVertex(p_76980_0_.maxX, p_76980_0_.maxY, p_76980_0_.minZ);
		tessellator.addVertex(p_76980_0_.maxX, p_76980_0_.maxY, p_76980_0_.maxZ);
		tessellator.addVertex(p_76980_0_.maxX, p_76980_0_.minY, p_76980_0_.maxZ);
		tessellator.draw();
	}

	public void setRenderManager(RenderManager p_76976_1_) {
		renderManager = p_76976_1_;
	}

	public void doRenderShadowAndFire(Entity p_76979_1_, double p_76979_2_, double p_76979_4_, double p_76979_6_,
			float p_76979_8_, float p_76979_9_) {
		if (renderManager.options.fancyGraphics && shadowSize > 0.0F && !p_76979_1_.isInvisible()) {
			double d3 = renderManager.getDistanceToCamera(p_76979_1_.posX, p_76979_1_.posY, p_76979_1_.posZ);
			float f2 = (float) ((1.0D - d3 / 256.0D) * shadowOpaque);

			if (f2 > 0.0F) {
				renderShadow(p_76979_1_, p_76979_2_, p_76979_4_, p_76979_6_, f2, p_76979_9_);
			}
		}

		if (p_76979_1_.canRenderOnFire()) {
			renderEntityOnFire(p_76979_1_, p_76979_2_, p_76979_4_, p_76979_6_, p_76979_9_);
		}
	}

	public FontRenderer getFontRendererFromRenderManager() {
		return renderManager.getFontRenderer();
	}

	public void updateIcons(IIconRegister p_94143_1_) {
	}

	protected void func_147906_a(Entity p_147906_1_, String p_147906_2_, double p_147906_3_, double p_147906_5_,
			double p_147906_7_, int p_147906_9_) {
		double d3 = p_147906_1_.getDistanceSqToEntity(renderManager.livingPlayer);

		if (d3 <= p_147906_9_ * p_147906_9_) {
			FontRenderer fontrenderer = getFontRendererFromRenderManager();
			float f = 1.6F;
			float f1 = 0.016666668F * f;
			GL11.glPushMatrix();
			GL11.glTranslatef((float) p_147906_3_ + 0.0F, (float) p_147906_5_ + p_147906_1_.height + 0.5F,
					(float) p_147906_7_);
			GL11.glNormal3f(0.0F, 1.0F, 0.0F);
			GL11.glRotatef(-renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
			GL11.glScalef(-f1, -f1, f1);
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glDepthMask(false);
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			GL11.glEnable(GL11.GL_BLEND);
			OpenGlHelper.glBlendFunc(770, 771, 1, 0);
			Tessellator tessellator = Tessellator.instance;
			byte b0 = 0;

			if (p_147906_2_.equals("deadmau5")) {
				b0 = -10;
			}

			GL11.glDisable(GL11.GL_TEXTURE_2D);
			tessellator.startDrawingQuads();
			int j = fontrenderer.getStringWidth(p_147906_2_) / 2;
			tessellator.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.25F);
			tessellator.addVertex(-j - 1, -1 + b0, 0.0D);
			tessellator.addVertex(-j - 1, 8 + b0, 0.0D);
			tessellator.addVertex(j + 1, 8 + b0, 0.0D);
			tessellator.addVertex(j + 1, -1 + b0, 0.0D);
			tessellator.draw();
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			fontrenderer.drawString(p_147906_2_, -fontrenderer.getStringWidth(p_147906_2_) / 2, b0, 553648127);
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			GL11.glDepthMask(true);
			fontrenderer.drawString(p_147906_2_, -fontrenderer.getStringWidth(p_147906_2_) / 2, b0, -1);
			GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glPopMatrix();
		}
	}
}