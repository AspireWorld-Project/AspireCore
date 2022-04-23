package net.minecraft.client.particle;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class EntityFootStepFX extends EntityFX {
	private static final ResourceLocation field_110126_a = new ResourceLocation("textures/particle/footprint.png");
	private int footstepAge;
	private final int footstepMaxAge;
	private final TextureManager currentFootSteps;
	private static final String __OBFID = "CL_00000908";

	public EntityFootStepFX(TextureManager p_i1210_1_, World p_i1210_2_, double p_i1210_3_, double p_i1210_5_,
			double p_i1210_7_) {
		super(p_i1210_2_, p_i1210_3_, p_i1210_5_, p_i1210_7_, 0.0D, 0.0D, 0.0D);
		currentFootSteps = p_i1210_1_;
		motionX = motionY = motionZ = 0.0D;
		footstepMaxAge = 200;
	}

	@Override
	public void renderParticle(Tessellator p_70539_1_, float p_70539_2_, float p_70539_3_, float p_70539_4_,
			float p_70539_5_, float p_70539_6_, float p_70539_7_) {
		float f6 = (footstepAge + p_70539_2_) / footstepMaxAge;
		f6 *= f6;
		float f7 = 2.0F - f6 * 2.0F;

		if (f7 > 1.0F) {
			f7 = 1.0F;
		}

		f7 *= 0.2F;
		GL11.glDisable(GL11.GL_LIGHTING);
		float f8 = 0.125F;
		float f9 = (float) (posX - interpPosX);
		float f10 = (float) (posY - interpPosY);
		float f11 = (float) (posZ - interpPosZ);
		float f12 = worldObj.getLightBrightness(MathHelper.floor_double(posX), MathHelper.floor_double(posY),
				MathHelper.floor_double(posZ));
		currentFootSteps.bindTexture(field_110126_a);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		p_70539_1_.startDrawingQuads();
		p_70539_1_.setColorRGBA_F(f12, f12, f12, f7);
		p_70539_1_.addVertexWithUV(f9 - f8, f10, f11 + f8, 0.0D, 1.0D);
		p_70539_1_.addVertexWithUV(f9 + f8, f10, f11 + f8, 1.0D, 1.0D);
		p_70539_1_.addVertexWithUV(f9 + f8, f10, f11 - f8, 1.0D, 0.0D);
		p_70539_1_.addVertexWithUV(f9 - f8, f10, f11 - f8, 0.0D, 0.0D);
		p_70539_1_.draw();
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_LIGHTING);
	}

	@Override
	public void onUpdate() {
		++footstepAge;

		if (footstepAge == footstepMaxAge) {
			setDead();
		}
	}

	@Override
	public int getFXLayer() {
		return 3;
	}
}