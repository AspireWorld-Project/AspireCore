package net.minecraft.client.renderer.entity;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;

@SideOnly(Side.CLIENT)
public abstract class RenderLiving extends RendererLivingEntity {
	private static final String __OBFID = "CL_00001015";

	public RenderLiving(ModelBase p_i1262_1_, float p_i1262_2_) {
		super(p_i1262_1_, p_i1262_2_);
	}

	protected boolean func_110813_b(EntityLiving p_110813_1_) {
		return super.func_110813_b(p_110813_1_) && (p_110813_1_.getAlwaysRenderNameTagForRender()
				|| p_110813_1_.hasCustomNameTag() && p_110813_1_ == renderManager.field_147941_i);
	}

	public void doRender(EntityLiving p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_,
			float p_76986_8_, float p_76986_9_) {
		super.doRender(p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
		func_110827_b(p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
	}

	private double func_110828_a(double p_110828_1_, double p_110828_3_, double p_110828_5_) {
		return p_110828_1_ + (p_110828_3_ - p_110828_1_) * p_110828_5_;
	}

	protected void func_110827_b(EntityLiving p_110827_1_, double p_110827_2_, double p_110827_4_, double p_110827_6_,
			float p_110827_8_, float p_110827_9_) {
		Entity entity = p_110827_1_.getLeashedToEntity();

		if (entity != null) {
			p_110827_4_ -= (1.6D - p_110827_1_.height) * 0.5D;
			Tessellator tessellator = Tessellator.instance;
			double d3 = func_110828_a(entity.prevRotationYaw, entity.rotationYaw, p_110827_9_ * 0.5F)
					* 0.01745329238474369D;
			double d4 = func_110828_a(entity.prevRotationPitch, entity.rotationPitch, p_110827_9_ * 0.5F)
					* 0.01745329238474369D;
			double d5 = Math.cos(d3);
			double d6 = Math.sin(d3);
			double d7 = Math.sin(d4);

			if (entity instanceof EntityHanging) {
				d5 = 0.0D;
				d6 = 0.0D;
				d7 = -1.0D;
			}

			double d8 = Math.cos(d4);
			double d9 = func_110828_a(entity.prevPosX, entity.posX, p_110827_9_) - d5 * 0.7D - d6 * 0.5D * d8;
			double d10 = func_110828_a(entity.prevPosY + entity.getEyeHeight() * 0.7D,
					entity.posY + entity.getEyeHeight() * 0.7D, p_110827_9_) - d7 * 0.5D - 0.25D;
			double d11 = func_110828_a(entity.prevPosZ, entity.posZ, p_110827_9_) - d6 * 0.7D + d5 * 0.5D * d8;
			double d12 = func_110828_a(p_110827_1_.prevRenderYawOffset, p_110827_1_.renderYawOffset, p_110827_9_)
					* 0.01745329238474369D + Math.PI / 2D;
			d5 = Math.cos(d12) * p_110827_1_.width * 0.4D;
			d6 = Math.sin(d12) * p_110827_1_.width * 0.4D;
			double d13 = func_110828_a(p_110827_1_.prevPosX, p_110827_1_.posX, p_110827_9_) + d5;
			double d14 = func_110828_a(p_110827_1_.prevPosY, p_110827_1_.posY, p_110827_9_);
			double d15 = func_110828_a(p_110827_1_.prevPosZ, p_110827_1_.posZ, p_110827_9_) + d6;
			p_110827_2_ += d5;
			p_110827_6_ += d6;
			double d16 = (float) (d9 - d13);
			double d17 = (float) (d10 - d14);
			double d18 = (float) (d11 - d15);
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glDisable(GL11.GL_CULL_FACE);
			tessellator.startDrawing(5);
			int i;
			float f2;

			for (i = 0; i <= 24; ++i) {
				if (i % 2 == 0) {
					tessellator.setColorRGBA_F(0.5F, 0.4F, 0.3F, 1.0F);
				} else {
					tessellator.setColorRGBA_F(0.35F, 0.28F, 0.21000001F, 1.0F);
				}

				f2 = i / 24.0F;
				tessellator.addVertex(p_110827_2_ + d16 * f2 + 0.0D,
						p_110827_4_ + d17 * (f2 * f2 + f2) * 0.5D + ((24.0F - i) / 18.0F + 0.125F),
						p_110827_6_ + d18 * f2);
				tessellator.addVertex(p_110827_2_ + d16 * f2 + 0.025D,
						p_110827_4_ + d17 * (f2 * f2 + f2) * 0.5D + ((24.0F - i) / 18.0F + 0.125F) + 0.025D,
						p_110827_6_ + d18 * f2);
			}

			tessellator.draw();
			tessellator.startDrawing(5);

			for (i = 0; i <= 24; ++i) {
				if (i % 2 == 0) {
					tessellator.setColorRGBA_F(0.5F, 0.4F, 0.3F, 1.0F);
				} else {
					tessellator.setColorRGBA_F(0.35F, 0.28F, 0.21000001F, 1.0F);
				}

				f2 = i / 24.0F;
				tessellator.addVertex(p_110827_2_ + d16 * f2 + 0.0D,
						p_110827_4_ + d17 * (f2 * f2 + f2) * 0.5D + ((24.0F - i) / 18.0F + 0.125F) + 0.025D,
						p_110827_6_ + d18 * f2);
				tessellator.addVertex(p_110827_2_ + d16 * f2 + 0.025D,
						p_110827_4_ + d17 * (f2 * f2 + f2) * 0.5D + ((24.0F - i) / 18.0F + 0.125F),
						p_110827_6_ + d18 * f2 + 0.025D);
			}

			tessellator.draw();
			GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glEnable(GL11.GL_CULL_FACE);
		}
	}

	@Override
	protected boolean func_110813_b(EntityLivingBase p_110813_1_) {
		return this.func_110813_b((EntityLiving) p_110813_1_);
	}

	@Override
	public void doRender(EntityLivingBase p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_,
			float p_76986_8_, float p_76986_9_) {
		this.doRender((EntityLiving) p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
	}

	@Override
	public void doRender(Entity p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_,
			float p_76986_9_) {
		this.doRender((EntityLiving) p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
	}
}