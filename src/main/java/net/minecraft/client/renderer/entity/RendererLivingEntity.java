package net.minecraft.client.renderer.entity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.common.MinecraftForge;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.util.Random;

@SideOnly(Side.CLIENT)
public abstract class RendererLivingEntity extends Render {
	private static final Logger logger = LogManager.getLogger();
	private static final ResourceLocation RES_ITEM_GLINT = new ResourceLocation(
			"textures/misc/enchanted_item_glint.png");
	protected ModelBase mainModel;
	protected ModelBase renderPassModel;
	private static final String __OBFID = "CL_00001012";

	public static float NAME_TAG_RANGE = 64.0f;
	public static float NAME_TAG_RANGE_SNEAK = 32.0f;

	public RendererLivingEntity(ModelBase p_i1261_1_, float p_i1261_2_) {
		mainModel = p_i1261_1_;
		shadowSize = p_i1261_2_;
	}

	public void setRenderPassModel(ModelBase p_77042_1_) {
		renderPassModel = p_77042_1_;
	}

	private float interpolateRotation(float p_77034_1_, float p_77034_2_, float p_77034_3_) {
		float f3;

		for (f3 = p_77034_2_ - p_77034_1_; f3 < -180.0F; f3 += 360.0F) {
			;
		}

		while (f3 >= 180.0F) {
			f3 -= 360.0F;
		}

		return p_77034_1_ + p_77034_3_ * f3;
	}

	public void doRender(EntityLivingBase p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_,
			float p_76986_8_, float p_76986_9_) {
		if (MinecraftForge.EVENT_BUS
				.post(new RenderLivingEvent.Pre(p_76986_1_, this, p_76986_2_, p_76986_4_, p_76986_6_)))
			return;
		GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_CULL_FACE);
		mainModel.onGround = renderSwingProgress(p_76986_1_, p_76986_9_);

		if (renderPassModel != null) {
			renderPassModel.onGround = mainModel.onGround;
		}

		mainModel.isRiding = p_76986_1_.isRiding();

		if (renderPassModel != null) {
			renderPassModel.isRiding = mainModel.isRiding;
		}

		mainModel.isChild = p_76986_1_.isChild();

		if (renderPassModel != null) {
			renderPassModel.isChild = mainModel.isChild;
		}

		try {
			float f2 = interpolateRotation(p_76986_1_.prevRenderYawOffset, p_76986_1_.renderYawOffset, p_76986_9_);
			float f3 = interpolateRotation(p_76986_1_.prevRotationYawHead, p_76986_1_.rotationYawHead, p_76986_9_);
			float f4;

			if (p_76986_1_.isRiding() && p_76986_1_.ridingEntity instanceof EntityLivingBase) {
				EntityLivingBase entitylivingbase1 = (EntityLivingBase) p_76986_1_.ridingEntity;
				f2 = interpolateRotation(entitylivingbase1.prevRenderYawOffset, entitylivingbase1.renderYawOffset,
						p_76986_9_);
				f4 = MathHelper.wrapAngleTo180_float(f3 - f2);

				if (f4 < -85.0F) {
					f4 = -85.0F;
				}

				if (f4 >= 85.0F) {
					f4 = 85.0F;
				}

				f2 = f3 - f4;

				if (f4 * f4 > 2500.0F) {
					f2 += f4 * 0.2F;
				}
			}

			float f13 = p_76986_1_.prevRotationPitch
					+ (p_76986_1_.rotationPitch - p_76986_1_.prevRotationPitch) * p_76986_9_;
			renderLivingAt(p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_);
			f4 = handleRotationFloat(p_76986_1_, p_76986_9_);
			rotateCorpse(p_76986_1_, f4, f2, p_76986_9_);
			float f5 = 0.0625F;
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
			GL11.glScalef(-1.0F, -1.0F, 1.0F);
			preRenderCallback(p_76986_1_, p_76986_9_);
			GL11.glTranslatef(0.0F, -24.0F * f5 - 0.0078125F, 0.0F);
			float f6 = p_76986_1_.prevLimbSwingAmount
					+ (p_76986_1_.limbSwingAmount - p_76986_1_.prevLimbSwingAmount) * p_76986_9_;
			float f7 = p_76986_1_.limbSwing - p_76986_1_.limbSwingAmount * (1.0F - p_76986_9_);

			if (p_76986_1_.isChild()) {
				f7 *= 3.0F;
			}

			if (f6 > 1.0F) {
				f6 = 1.0F;
			}

			GL11.glEnable(GL11.GL_ALPHA_TEST);
			mainModel.setLivingAnimations(p_76986_1_, f7, f6, p_76986_9_);
			renderModel(p_76986_1_, f7, f6, f4, f3 - f2, f13, f5);
			int j;
			float f8;
			float f9;
			float f10;

			for (int i = 0; i < 4; ++i) {
				j = shouldRenderPass(p_76986_1_, i, p_76986_9_);

				if (j > 0) {
					renderPassModel.setLivingAnimations(p_76986_1_, f7, f6, p_76986_9_);
					renderPassModel.render(p_76986_1_, f7, f6, f4, f3 - f2, f13, f5);

					if ((j & 240) == 16) {
						func_82408_c(p_76986_1_, i, p_76986_9_);
						renderPassModel.render(p_76986_1_, f7, f6, f4, f3 - f2, f13, f5);
					}

					if ((j & 15) == 15) {
						f8 = p_76986_1_.ticksExisted + p_76986_9_;
						bindTexture(RES_ITEM_GLINT);
						GL11.glEnable(GL11.GL_BLEND);
						f9 = 0.5F;
						GL11.glColor4f(f9, f9, f9, 1.0F);
						GL11.glDepthFunc(GL11.GL_EQUAL);
						GL11.glDepthMask(false);

						for (int k = 0; k < 2; ++k) {
							GL11.glDisable(GL11.GL_LIGHTING);
							f10 = 0.76F;
							GL11.glColor4f(0.5F * f10, 0.25F * f10, 0.8F * f10, 1.0F);
							GL11.glBlendFunc(GL11.GL_SRC_COLOR, GL11.GL_ONE);
							GL11.glMatrixMode(GL11.GL_TEXTURE);
							GL11.glLoadIdentity();
							float f11 = f8 * (0.001F + k * 0.003F) * 20.0F;
							float f12 = 0.33333334F;
							GL11.glScalef(f12, f12, f12);
							GL11.glRotatef(30.0F - k * 60.0F, 0.0F, 0.0F, 1.0F);
							GL11.glTranslatef(0.0F, f11, 0.0F);
							GL11.glMatrixMode(GL11.GL_MODELVIEW);
							renderPassModel.render(p_76986_1_, f7, f6, f4, f3 - f2, f13, f5);
						}

						GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
						GL11.glMatrixMode(GL11.GL_TEXTURE);
						GL11.glDepthMask(true);
						GL11.glLoadIdentity();
						GL11.glMatrixMode(GL11.GL_MODELVIEW);
						GL11.glEnable(GL11.GL_LIGHTING);
						GL11.glDisable(GL11.GL_BLEND);
						GL11.glDepthFunc(GL11.GL_LEQUAL);
					}

					GL11.glDisable(GL11.GL_BLEND);
					GL11.glEnable(GL11.GL_ALPHA_TEST);
				}
			}

			GL11.glDepthMask(true);
			renderEquippedItems(p_76986_1_, p_76986_9_);
			float f14 = p_76986_1_.getBrightness(p_76986_9_);
			j = getColorMultiplier(p_76986_1_, f14, p_76986_9_);
			OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);

			if ((j >> 24 & 255) > 0 || p_76986_1_.hurtTime > 0 || p_76986_1_.deathTime > 0) {
				GL11.glDisable(GL11.GL_TEXTURE_2D);
				GL11.glDisable(GL11.GL_ALPHA_TEST);
				GL11.glEnable(GL11.GL_BLEND);
				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
				GL11.glDepthFunc(GL11.GL_EQUAL);

				if (p_76986_1_.hurtTime > 0 || p_76986_1_.deathTime > 0) {
					GL11.glColor4f(f14, 0.0F, 0.0F, 0.4F);
					mainModel.render(p_76986_1_, f7, f6, f4, f3 - f2, f13, f5);

					for (int l = 0; l < 4; ++l) {
						if (inheritRenderPass(p_76986_1_, l, p_76986_9_) >= 0) {
							GL11.glColor4f(f14, 0.0F, 0.0F, 0.4F);
							renderPassModel.render(p_76986_1_, f7, f6, f4, f3 - f2, f13, f5);
						}
					}
				}

				if ((j >> 24 & 255) > 0) {
					f8 = (j >> 16 & 255) / 255.0F;
					f9 = (j >> 8 & 255) / 255.0F;
					float f15 = (j & 255) / 255.0F;
					f10 = (j >> 24 & 255) / 255.0F;
					GL11.glColor4f(f8, f9, f15, f10);
					mainModel.render(p_76986_1_, f7, f6, f4, f3 - f2, f13, f5);

					for (int i1 = 0; i1 < 4; ++i1) {
						if (inheritRenderPass(p_76986_1_, i1, p_76986_9_) >= 0) {
							GL11.glColor4f(f8, f9, f15, f10);
							renderPassModel.render(p_76986_1_, f7, f6, f4, f3 - f2, f13, f5);
						}
					}
				}

				GL11.glDepthFunc(GL11.GL_LEQUAL);
				GL11.glDisable(GL11.GL_BLEND);
				GL11.glEnable(GL11.GL_ALPHA_TEST);
				GL11.glEnable(GL11.GL_TEXTURE_2D);
			}

			GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		} catch (Exception exception) {
			logger.error("Couldn\'t render entity", exception);
		}

		OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glPopMatrix();
		passSpecialRender(p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_);
		MinecraftForge.EVENT_BUS.post(new RenderLivingEvent.Post(p_76986_1_, this, p_76986_2_, p_76986_4_, p_76986_6_));
	}

	protected void renderModel(EntityLivingBase p_77036_1_, float p_77036_2_, float p_77036_3_, float p_77036_4_,
			float p_77036_5_, float p_77036_6_, float p_77036_7_) {
		bindEntityTexture(p_77036_1_);

		if (!p_77036_1_.isInvisible()) {
			mainModel.render(p_77036_1_, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, p_77036_7_);
		} else if (!p_77036_1_.isInvisibleToPlayer(Minecraft.getMinecraft().thePlayer)) {
			GL11.glPushMatrix();
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.15F);
			GL11.glDepthMask(false);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glAlphaFunc(GL11.GL_GREATER, 0.003921569F);
			mainModel.render(p_77036_1_, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, p_77036_7_);
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
			GL11.glPopMatrix();
			GL11.glDepthMask(true);
		} else {
			mainModel.setRotationAngles(p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, p_77036_7_,
					p_77036_1_);
		}
	}

	protected void renderLivingAt(EntityLivingBase p_77039_1_, double p_77039_2_, double p_77039_4_,
			double p_77039_6_) {
		GL11.glTranslatef((float) p_77039_2_, (float) p_77039_4_, (float) p_77039_6_);
	}

	protected void rotateCorpse(EntityLivingBase p_77043_1_, float p_77043_2_, float p_77043_3_, float p_77043_4_) {
		GL11.glRotatef(180.0F - p_77043_3_, 0.0F, 1.0F, 0.0F);

		if (p_77043_1_.deathTime > 0) {
			float f3 = (p_77043_1_.deathTime + p_77043_4_ - 1.0F) / 20.0F * 1.6F;
			f3 = MathHelper.sqrt_float(f3);

			if (f3 > 1.0F) {
				f3 = 1.0F;
			}

			GL11.glRotatef(f3 * getDeathMaxRotation(p_77043_1_), 0.0F, 0.0F, 1.0F);
		} else {
			String s = EnumChatFormatting.getTextWithoutFormattingCodes(p_77043_1_.getCommandSenderName());

			if ((s.equals("Dinnerbone") || s.equals("Grumm"))
					&& (!(p_77043_1_ instanceof EntityPlayer) || !((EntityPlayer) p_77043_1_).getHideCape())) {
				GL11.glTranslatef(0.0F, p_77043_1_.height + 0.1F, 0.0F);
				GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
			}
		}
	}

	protected float renderSwingProgress(EntityLivingBase p_77040_1_, float p_77040_2_) {
		return p_77040_1_.getSwingProgress(p_77040_2_);
	}

	protected float handleRotationFloat(EntityLivingBase p_77044_1_, float p_77044_2_) {
		return p_77044_1_.ticksExisted + p_77044_2_;
	}

	protected void renderEquippedItems(EntityLivingBase p_77029_1_, float p_77029_2_) {
	}

	protected void renderArrowsStuckInEntity(EntityLivingBase p_85093_1_, float p_85093_2_) {
		int i = p_85093_1_.getArrowCountInEntity();

		if (i > 0) {
			EntityArrow entityarrow = new EntityArrow(p_85093_1_.worldObj, p_85093_1_.posX, p_85093_1_.posY,
					p_85093_1_.posZ);
			Random random = new Random(p_85093_1_.getEntityId());
			RenderHelper.disableStandardItemLighting();

			for (int j = 0; j < i; ++j) {
				GL11.glPushMatrix();
				ModelRenderer modelrenderer = mainModel.getRandomModelBox(random);
				ModelBox modelbox = (ModelBox) modelrenderer.cubeList
						.get(random.nextInt(modelrenderer.cubeList.size()));
				modelrenderer.postRender(0.0625F);
				float f1 = random.nextFloat();
				float f2 = random.nextFloat();
				float f3 = random.nextFloat();
				float f4 = (modelbox.posX1 + (modelbox.posX2 - modelbox.posX1) * f1) / 16.0F;
				float f5 = (modelbox.posY1 + (modelbox.posY2 - modelbox.posY1) * f2) / 16.0F;
				float f6 = (modelbox.posZ1 + (modelbox.posZ2 - modelbox.posZ1) * f3) / 16.0F;
				GL11.glTranslatef(f4, f5, f6);
				f1 = f1 * 2.0F - 1.0F;
				f2 = f2 * 2.0F - 1.0F;
				f3 = f3 * 2.0F - 1.0F;
				f1 *= -1.0F;
				f2 *= -1.0F;
				f3 *= -1.0F;
				float f7 = MathHelper.sqrt_float(f1 * f1 + f3 * f3);
				entityarrow.prevRotationYaw = entityarrow.rotationYaw = (float) (Math.atan2(f1, f3) * 180.0D / Math.PI);
				entityarrow.prevRotationPitch = entityarrow.rotationPitch = (float) (Math.atan2(f2, f7) * 180.0D
						/ Math.PI);
				double d0 = 0.0D;
				double d1 = 0.0D;
				double d2 = 0.0D;
				float f8 = 0.0F;
				renderManager.renderEntityWithPosYaw(entityarrow, d0, d1, d2, f8, p_85093_2_);
				GL11.glPopMatrix();
			}

			RenderHelper.enableStandardItemLighting();
		}
	}

	protected int inheritRenderPass(EntityLivingBase p_77035_1_, int p_77035_2_, float p_77035_3_) {
		return shouldRenderPass(p_77035_1_, p_77035_2_, p_77035_3_);
	}

	protected int shouldRenderPass(EntityLivingBase p_77032_1_, int p_77032_2_, float p_77032_3_) {
		return -1;
	}

	protected void func_82408_c(EntityLivingBase p_82408_1_, int p_82408_2_, float p_82408_3_) {
	}

	protected float getDeathMaxRotation(EntityLivingBase p_77037_1_) {
		return 90.0F;
	}

	protected int getColorMultiplier(EntityLivingBase p_77030_1_, float p_77030_2_, float p_77030_3_) {
		return 0;
	}

	protected void preRenderCallback(EntityLivingBase p_77041_1_, float p_77041_2_) {
	}

	protected void passSpecialRender(EntityLivingBase p_77033_1_, double p_77033_2_, double p_77033_4_,
			double p_77033_6_) {
		if (MinecraftForge.EVENT_BUS
				.post(new RenderLivingEvent.Specials.Pre(p_77033_1_, this, p_77033_2_, p_77033_4_, p_77033_6_)))
			return;
		GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);

		if (func_110813_b(p_77033_1_)) {
			float f = 1.6F;
			float f1 = 0.016666668F * f;
			double d3 = p_77033_1_.getDistanceSqToEntity(renderManager.livingPlayer);
			float f2 = p_77033_1_.isSneaking() ? NAME_TAG_RANGE_SNEAK : NAME_TAG_RANGE;

			if (d3 < f2 * f2) {
				String s = p_77033_1_.func_145748_c_().getFormattedText();

				if (p_77033_1_.isSneaking()) {
					FontRenderer fontrenderer = getFontRendererFromRenderManager();
					GL11.glPushMatrix();
					GL11.glTranslatef((float) p_77033_2_ + 0.0F, (float) p_77033_4_ + p_77033_1_.height + 0.5F,
							(float) p_77033_6_);
					GL11.glNormal3f(0.0F, 1.0F, 0.0F);
					GL11.glRotatef(-renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
					GL11.glRotatef(renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
					GL11.glScalef(-f1, -f1, f1);
					GL11.glDisable(GL11.GL_LIGHTING);
					GL11.glTranslatef(0.0F, 0.25F / f1, 0.0F);
					GL11.glDepthMask(false);
					GL11.glEnable(GL11.GL_BLEND);
					OpenGlHelper.glBlendFunc(770, 771, 1, 0);
					Tessellator tessellator = Tessellator.instance;
					GL11.glDisable(GL11.GL_TEXTURE_2D);
					tessellator.startDrawingQuads();
					int i = fontrenderer.getStringWidth(s) / 2;
					tessellator.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.25F);
					tessellator.addVertex(-i - 1, -1.0D, 0.0D);
					tessellator.addVertex(-i - 1, 8.0D, 0.0D);
					tessellator.addVertex(i + 1, 8.0D, 0.0D);
					tessellator.addVertex(i + 1, -1.0D, 0.0D);
					tessellator.draw();
					GL11.glEnable(GL11.GL_TEXTURE_2D);
					GL11.glDepthMask(true);
					fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2, 0, 553648127);
					GL11.glEnable(GL11.GL_LIGHTING);
					GL11.glDisable(GL11.GL_BLEND);
					GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
					GL11.glPopMatrix();
				} else {
					func_96449_a(p_77033_1_, p_77033_2_, p_77033_4_, p_77033_6_, s, f1, d3);
				}
			}
		}
		MinecraftForge.EVENT_BUS
				.post(new RenderLivingEvent.Specials.Post(p_77033_1_, this, p_77033_2_, p_77033_4_, p_77033_6_));
	}

	protected boolean func_110813_b(EntityLivingBase p_110813_1_) {
		return Minecraft.isGuiEnabled() && p_110813_1_ != renderManager.livingPlayer
				&& !p_110813_1_.isInvisibleToPlayer(Minecraft.getMinecraft().thePlayer)
				&& p_110813_1_.riddenByEntity == null;
	}

	protected void func_96449_a(EntityLivingBase p_96449_1_, double p_96449_2_, double p_96449_4_, double p_96449_6_,
			String p_96449_8_, float p_96449_9_, double p_96449_10_) {
		if (p_96449_1_.isPlayerSleeping()) {
			func_147906_a(p_96449_1_, p_96449_8_, p_96449_2_, p_96449_4_ - 1.5D, p_96449_6_, 64);
		} else {
			func_147906_a(p_96449_1_, p_96449_8_, p_96449_2_, p_96449_4_, p_96449_6_, 64);
		}
	}

	@Override
	public void doRender(Entity p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_,
			float p_76986_9_) {
		this.doRender((EntityLivingBase) p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
	}
}