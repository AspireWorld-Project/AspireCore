package net.minecraft.client.renderer.entity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelDragon;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.BossStatus;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.Random;

@SideOnly(Side.CLIENT)
public class RenderDragon extends RenderLiving {
	private static final ResourceLocation enderDragonExplodingTextures = new ResourceLocation(
			"textures/entity/enderdragon/dragon_exploding.png");
	private static final ResourceLocation enderDragonCrystalBeamTextures = new ResourceLocation(
			"textures/entity/endercrystal/endercrystal_beam.png");
	private static final ResourceLocation enderDragonEyesTextures = new ResourceLocation(
			"textures/entity/enderdragon/dragon_eyes.png");
	private static final ResourceLocation enderDragonTextures = new ResourceLocation(
			"textures/entity/enderdragon/dragon.png");
	protected ModelDragon modelDragon;
	private static final String __OBFID = "CL_00000988";

	public RenderDragon() {
		super(new ModelDragon(0.0F), 0.5F);
		modelDragon = (ModelDragon) mainModel;
		setRenderPassModel(mainModel);
	}

	protected void rotateCorpse(EntityDragon p_77043_1_, float p_77043_2_, float p_77043_3_, float p_77043_4_) {
		float f3 = (float) p_77043_1_.getMovementOffsets(7, p_77043_4_)[0];
		float f4 = (float) (p_77043_1_.getMovementOffsets(5, p_77043_4_)[1]
				- p_77043_1_.getMovementOffsets(10, p_77043_4_)[1]);
		GL11.glRotatef(-f3, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(f4 * 10.0F, 1.0F, 0.0F, 0.0F);
		GL11.glTranslatef(0.0F, 0.0F, 1.0F);

		if (p_77043_1_.deathTime > 0) {
			float f5 = (p_77043_1_.deathTime + p_77043_4_ - 1.0F) / 20.0F * 1.6F;
			f5 = MathHelper.sqrt_float(f5);

			if (f5 > 1.0F) {
				f5 = 1.0F;
			}

			GL11.glRotatef(f5 * getDeathMaxRotation(p_77043_1_), 0.0F, 0.0F, 1.0F);
		}
	}

	protected void renderModel(EntityDragon p_77036_1_, float p_77036_2_, float p_77036_3_, float p_77036_4_,
			float p_77036_5_, float p_77036_6_, float p_77036_7_) {
		if (p_77036_1_.deathTicks > 0) {
			float f6 = p_77036_1_.deathTicks / 200.0F;
			GL11.glDepthFunc(GL11.GL_LEQUAL);
			GL11.glEnable(GL11.GL_ALPHA_TEST);
			GL11.glAlphaFunc(GL11.GL_GREATER, f6);
			bindTexture(enderDragonExplodingTextures);
			mainModel.render(p_77036_1_, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, p_77036_7_);
			GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
			GL11.glDepthFunc(GL11.GL_EQUAL);
		}

		bindEntityTexture(p_77036_1_);
		mainModel.render(p_77036_1_, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, p_77036_7_);

		if (p_77036_1_.hurtTime > 0) {
			GL11.glDepthFunc(GL11.GL_EQUAL);
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glColor4f(1.0F, 0.0F, 0.0F, 0.5F);
			mainModel.render(p_77036_1_, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, p_77036_7_);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glDepthFunc(GL11.GL_LEQUAL);
		}
	}

	public void doRender(EntityDragon p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_,
			float p_76986_8_, float p_76986_9_) {
		BossStatus.setBossStatus(p_76986_1_, false);
		super.doRender(p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);

		if (p_76986_1_.healingEnderCrystal != null) {
			float f2 = p_76986_1_.healingEnderCrystal.innerRotation + p_76986_9_;
			float f3 = MathHelper.sin(f2 * 0.2F) / 2.0F + 0.5F;
			f3 = (f3 * f3 + f3) * 0.2F;
			float f4 = (float) (p_76986_1_.healingEnderCrystal.posX - p_76986_1_.posX
					- (p_76986_1_.prevPosX - p_76986_1_.posX) * (1.0F - p_76986_9_));
			float f5 = (float) (f3 + p_76986_1_.healingEnderCrystal.posY - 1.0D - p_76986_1_.posY
					- (p_76986_1_.prevPosY - p_76986_1_.posY) * (1.0F - p_76986_9_));
			float f6 = (float) (p_76986_1_.healingEnderCrystal.posZ - p_76986_1_.posZ
					- (p_76986_1_.prevPosZ - p_76986_1_.posZ) * (1.0F - p_76986_9_));
			float f7 = MathHelper.sqrt_float(f4 * f4 + f6 * f6);
			float f8 = MathHelper.sqrt_float(f4 * f4 + f5 * f5 + f6 * f6);
			GL11.glPushMatrix();
			GL11.glTranslatef((float) p_76986_2_, (float) p_76986_4_ + 2.0F, (float) p_76986_6_);
			GL11.glRotatef((float) -Math.atan2(f6, f4) * 180.0F / (float) Math.PI - 90.0F, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef((float) -Math.atan2(f7, f5) * 180.0F / (float) Math.PI - 90.0F, 1.0F, 0.0F, 0.0F);
			Tessellator tessellator = Tessellator.instance;
			RenderHelper.disableStandardItemLighting();
			GL11.glDisable(GL11.GL_CULL_FACE);
			bindTexture(enderDragonCrystalBeamTextures);
			GL11.glShadeModel(GL11.GL_SMOOTH);
			float f9 = 0.0F - (p_76986_1_.ticksExisted + p_76986_9_) * 0.01F;
			float f10 = MathHelper.sqrt_float(f4 * f4 + f5 * f5 + f6 * f6) / 32.0F
					- (p_76986_1_.ticksExisted + p_76986_9_) * 0.01F;
			tessellator.startDrawing(5);
			byte b0 = 8;

			for (int i = 0; i <= b0; ++i) {
				float f11 = MathHelper.sin(i % b0 * (float) Math.PI * 2.0F / b0) * 0.75F;
				float f12 = MathHelper.cos(i % b0 * (float) Math.PI * 2.0F / b0) * 0.75F;
				float f13 = i % b0 * 1.0F / b0;
				tessellator.setColorOpaque_I(0);
				tessellator.addVertexWithUV(f11 * 0.2F, f12 * 0.2F, 0.0D, f13, f10);
				tessellator.setColorOpaque_I(16777215);
				tessellator.addVertexWithUV(f11, f12, f8, f13, f9);
			}

			tessellator.draw();
			GL11.glEnable(GL11.GL_CULL_FACE);
			GL11.glShadeModel(GL11.GL_FLAT);
			RenderHelper.enableStandardItemLighting();
			GL11.glPopMatrix();
		}
	}

	protected ResourceLocation getEntityTexture(EntityDragon p_110775_1_) {
		return enderDragonTextures;
	}

	protected void renderEquippedItems(EntityDragon p_77029_1_, float p_77029_2_) {
		super.renderEquippedItems(p_77029_1_, p_77029_2_);
		Tessellator tessellator = Tessellator.instance;

		if (p_77029_1_.deathTicks > 0) {
			RenderHelper.disableStandardItemLighting();
			float f1 = (p_77029_1_.deathTicks + p_77029_2_) / 200.0F;
			float f2 = 0.0F;

			if (f1 > 0.8F) {
				f2 = (f1 - 0.8F) / 0.2F;
			}

			Random random = new Random(432L);
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glShadeModel(GL11.GL_SMOOTH);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
			GL11.glDisable(GL11.GL_ALPHA_TEST);
			GL11.glEnable(GL11.GL_CULL_FACE);
			GL11.glDepthMask(false);
			GL11.glPushMatrix();
			GL11.glTranslatef(0.0F, -1.0F, -2.0F);

			for (int i = 0; i < (f1 + f1 * f1) / 2.0F * 60.0F; ++i) {
				GL11.glRotatef(random.nextFloat() * 360.0F, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(random.nextFloat() * 360.0F, 0.0F, 1.0F, 0.0F);
				GL11.glRotatef(random.nextFloat() * 360.0F, 0.0F, 0.0F, 1.0F);
				GL11.glRotatef(random.nextFloat() * 360.0F, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(random.nextFloat() * 360.0F, 0.0F, 1.0F, 0.0F);
				GL11.glRotatef(random.nextFloat() * 360.0F + f1 * 90.0F, 0.0F, 0.0F, 1.0F);
				tessellator.startDrawing(6);
				float f3 = random.nextFloat() * 20.0F + 5.0F + f2 * 10.0F;
				float f4 = random.nextFloat() * 2.0F + 1.0F + f2 * 2.0F;
				tessellator.setColorRGBA_I(16777215, (int) (255.0F * (1.0F - f2)));
				tessellator.addVertex(0.0D, 0.0D, 0.0D);
				tessellator.setColorRGBA_I(16711935, 0);
				tessellator.addVertex(-0.866D * f4, f3, -0.5F * f4);
				tessellator.addVertex(0.866D * f4, f3, -0.5F * f4);
				tessellator.addVertex(0.0D, f3, 1.0F * f4);
				tessellator.addVertex(-0.866D * f4, f3, -0.5F * f4);
				tessellator.draw();
			}

			GL11.glPopMatrix();
			GL11.glDepthMask(true);
			GL11.glDisable(GL11.GL_CULL_FACE);
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glShadeModel(GL11.GL_FLAT);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glEnable(GL11.GL_ALPHA_TEST);
			RenderHelper.enableStandardItemLighting();
		}
	}

	protected int shouldRenderPass(EntityDragon p_77032_1_, int p_77032_2_, float p_77032_3_) {
		if (p_77032_2_ == 1) {
			GL11.glDepthFunc(GL11.GL_LEQUAL);
		}

		if (p_77032_2_ != 0)
			return -1;
		else {
			bindTexture(enderDragonEyesTextures);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glDisable(GL11.GL_ALPHA_TEST);
			GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glDepthFunc(GL11.GL_EQUAL);
			char c0 = 61680;
			int j = c0 % 65536;
			int k = c0 / 65536;
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, j / 1.0F, k / 1.0F);
			GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			return 1;
		}
	}

	@Override
	public void doRender(EntityLiving p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_,
			float p_76986_8_, float p_76986_9_) {
		this.doRender((EntityDragon) p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
	}

	@Override
	protected int shouldRenderPass(EntityLivingBase p_77032_1_, int p_77032_2_, float p_77032_3_) {
		return this.shouldRenderPass((EntityDragon) p_77032_1_, p_77032_2_, p_77032_3_);
	}

	@Override
	protected void renderEquippedItems(EntityLivingBase p_77029_1_, float p_77029_2_) {
		this.renderEquippedItems((EntityDragon) p_77029_1_, p_77029_2_);
	}

	@Override
	protected void rotateCorpse(EntityLivingBase p_77043_1_, float p_77043_2_, float p_77043_3_, float p_77043_4_) {
		this.rotateCorpse((EntityDragon) p_77043_1_, p_77043_2_, p_77043_3_, p_77043_4_);
	}

	@Override
	protected void renderModel(EntityLivingBase p_77036_1_, float p_77036_2_, float p_77036_3_, float p_77036_4_,
			float p_77036_5_, float p_77036_6_, float p_77036_7_) {
		this.renderModel((EntityDragon) p_77036_1_, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_,
				p_77036_7_);
	}

	@Override
	public void doRender(EntityLivingBase p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_,
			float p_76986_8_, float p_76986_9_) {
		this.doRender((EntityDragon) p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity p_110775_1_) {
		return this.getEntityTexture((EntityDragon) p_110775_1_);
	}

	@Override
	public void doRender(Entity p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_,
			float p_76986_9_) {
		this.doRender((EntityDragon) p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
	}
}