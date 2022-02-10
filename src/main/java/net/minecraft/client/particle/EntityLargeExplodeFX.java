package net.minecraft.client.particle;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

@SideOnly(Side.CLIENT)
public class EntityLargeExplodeFX extends EntityFX {
	private static final ResourceLocation field_110127_a = new ResourceLocation("textures/entity/explosion.png");
	private int field_70581_a;
	private int field_70584_aq;
	private TextureManager theRenderEngine;
	private float field_70582_as;
	private static final String __OBFID = "CL_00000910";

	public EntityLargeExplodeFX(TextureManager p_i1213_1_, World p_i1213_2_, double p_i1213_3_, double p_i1213_5_,
			double p_i1213_7_, double p_i1213_9_, double p_i1213_11_, double p_i1213_13_) {
		super(p_i1213_2_, p_i1213_3_, p_i1213_5_, p_i1213_7_, 0.0D, 0.0D, 0.0D);
		theRenderEngine = p_i1213_1_;
		field_70584_aq = 6 + rand.nextInt(4);
		particleRed = particleGreen = particleBlue = rand.nextFloat() * 0.6F + 0.4F;
		field_70582_as = 1.0F - (float) p_i1213_9_ * 0.5F;
	}

	@Override
	public void renderParticle(Tessellator p_70539_1_, float p_70539_2_, float p_70539_3_, float p_70539_4_,
			float p_70539_5_, float p_70539_6_, float p_70539_7_) {
		int i = (int) ((field_70581_a + p_70539_2_) * 15.0F / field_70584_aq);

		if (i <= 15) {
			theRenderEngine.bindTexture(field_110127_a);
			float f6 = i % 4 / 4.0F;
			float f7 = f6 + 0.24975F;
			float f8 = i / 4 / 4.0F;
			float f9 = f8 + 0.24975F;
			float f10 = 2.0F * field_70582_as;
			float f11 = (float) (prevPosX + (posX - prevPosX) * p_70539_2_ - interpPosX);
			float f12 = (float) (prevPosY + (posY - prevPosY) * p_70539_2_ - interpPosY);
			float f13 = (float) (prevPosZ + (posZ - prevPosZ) * p_70539_2_ - interpPosZ);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glDisable(GL11.GL_LIGHTING);
			RenderHelper.disableStandardItemLighting();
			p_70539_1_.startDrawingQuads();
			p_70539_1_.setColorRGBA_F(particleRed, particleGreen, particleBlue, 1.0F);
			p_70539_1_.setNormal(0.0F, 1.0F, 0.0F);
			p_70539_1_.setBrightness(240);
			p_70539_1_.addVertexWithUV(f11 - p_70539_3_ * f10 - p_70539_6_ * f10, f12 - p_70539_4_ * f10,
					f13 - p_70539_5_ * f10 - p_70539_7_ * f10, f7, f9);
			p_70539_1_.addVertexWithUV(f11 - p_70539_3_ * f10 + p_70539_6_ * f10, f12 + p_70539_4_ * f10,
					f13 - p_70539_5_ * f10 + p_70539_7_ * f10, f7, f8);
			p_70539_1_.addVertexWithUV(f11 + p_70539_3_ * f10 + p_70539_6_ * f10, f12 + p_70539_4_ * f10,
					f13 + p_70539_5_ * f10 + p_70539_7_ * f10, f6, f8);
			p_70539_1_.addVertexWithUV(f11 + p_70539_3_ * f10 - p_70539_6_ * f10, f12 - p_70539_4_ * f10,
					f13 + p_70539_5_ * f10 - p_70539_7_ * f10, f6, f9);
			p_70539_1_.draw();
			GL11.glPolygonOffset(0.0F, 0.0F);
			GL11.glEnable(GL11.GL_LIGHTING);
		}
	}

	@Override
	public int getBrightnessForRender(float p_70070_1_) {
		return 61680;
	}

	@Override
	public void onUpdate() {
		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;
		++field_70581_a;

		if (field_70581_a == field_70584_aq) {
			setDead();
		}
	}

	@Override
	public int getFXLayer() {
		return 3;
	}
}