package net.minecraft.client.renderer.entity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderSlime extends RenderLiving {
	private static final ResourceLocation slimeTextures = new ResourceLocation("textures/entity/slime/slime.png");
	private final ModelBase scaleAmount;
	private static final String __OBFID = "CL_00001024";

	public RenderSlime(ModelBase p_i1267_1_, ModelBase p_i1267_2_, float p_i1267_3_) {
		super(p_i1267_1_, p_i1267_3_);
		scaleAmount = p_i1267_2_;
	}

	protected int shouldRenderPass(EntitySlime p_77032_1_, int p_77032_2_, float p_77032_3_) {
		if (p_77032_1_.isInvisible())
			return 0;
		else if (p_77032_2_ == 0) {
			setRenderPassModel(scaleAmount);
			GL11.glEnable(GL11.GL_NORMALIZE);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			return 1;
		} else {
			if (p_77032_2_ == 1) {
				GL11.glDisable(GL11.GL_BLEND);
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			}

			return -1;
		}
	}

	protected void preRenderCallback(EntitySlime p_77041_1_, float p_77041_2_) {
		float f1 = p_77041_1_.getSlimeSize();
		float f2 = (p_77041_1_.prevSquishFactor + (p_77041_1_.squishFactor - p_77041_1_.prevSquishFactor) * p_77041_2_)
				/ (f1 * 0.5F + 1.0F);
		float f3 = 1.0F / (f2 + 1.0F);
		GL11.glScalef(f3 * f1, 1.0F / f3 * f1, f3 * f1);
	}

	protected ResourceLocation getEntityTexture(EntitySlime p_110775_1_) {
		return slimeTextures;
	}

	@Override
	protected void preRenderCallback(EntityLivingBase p_77041_1_, float p_77041_2_) {
		this.preRenderCallback((EntitySlime) p_77041_1_, p_77041_2_);
	}

	@Override
	protected int shouldRenderPass(EntityLivingBase p_77032_1_, int p_77032_2_, float p_77032_3_) {
		return this.shouldRenderPass((EntitySlime) p_77032_1_, p_77032_2_, p_77032_3_);
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity p_110775_1_) {
		return this.getEntityTexture((EntitySlime) p_110775_1_);
	}
}