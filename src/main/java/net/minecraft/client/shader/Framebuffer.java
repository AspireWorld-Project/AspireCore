package net.minecraft.client.shader;

import java.nio.ByteBuffer;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureUtil;

@SideOnly(Side.CLIENT)
public class Framebuffer {
	public int framebufferTextureWidth;
	public int framebufferTextureHeight;
	public int framebufferWidth;
	public int framebufferHeight;
	public boolean useDepth;
	public int framebufferObject;
	public int framebufferTexture;
	public int depthBuffer;
	public float[] framebufferColor;
	public int framebufferFilter;
	private static final String __OBFID = "CL_00000959";

	public Framebuffer(int p_i45078_1_, int p_i45078_2_, boolean p_i45078_3_) {
		useDepth = p_i45078_3_;
		framebufferObject = -1;
		framebufferTexture = -1;
		depthBuffer = -1;
		framebufferColor = new float[4];
		framebufferColor[0] = 1.0F;
		framebufferColor[1] = 1.0F;
		framebufferColor[2] = 1.0F;
		framebufferColor[3] = 0.0F;
		createBindFramebuffer(p_i45078_1_, p_i45078_2_);
	}

	public void createBindFramebuffer(int p_147613_1_, int p_147613_2_) {
		if (!OpenGlHelper.isFramebufferEnabled()) {
			framebufferWidth = p_147613_1_;
			framebufferHeight = p_147613_2_;
		} else {
			GL11.glEnable(GL11.GL_DEPTH_TEST);

			if (framebufferObject >= 0) {
				deleteFramebuffer();
			}

			createFramebuffer(p_147613_1_, p_147613_2_);
			checkFramebufferComplete();
			OpenGlHelper.func_153171_g(OpenGlHelper.field_153198_e, 0);
		}
	}

	public void deleteFramebuffer() {
		if (OpenGlHelper.isFramebufferEnabled()) {
			unbindFramebufferTexture();
			unbindFramebuffer();

			if (depthBuffer > -1) {
				OpenGlHelper.func_153184_g(depthBuffer);
				depthBuffer = -1;
			}

			if (framebufferTexture > -1) {
				TextureUtil.deleteTexture(framebufferTexture);
				framebufferTexture = -1;
			}

			if (framebufferObject > -1) {
				OpenGlHelper.func_153171_g(OpenGlHelper.field_153198_e, 0);
				OpenGlHelper.func_153174_h(framebufferObject);
				framebufferObject = -1;
			}
		}
	}

	public void createFramebuffer(int p_147605_1_, int p_147605_2_) {
		framebufferWidth = p_147605_1_;
		framebufferHeight = p_147605_2_;
		framebufferTextureWidth = p_147605_1_;
		framebufferTextureHeight = p_147605_2_;

		if (!OpenGlHelper.isFramebufferEnabled()) {
			framebufferClear();
		} else {
			framebufferObject = OpenGlHelper.func_153165_e();
			framebufferTexture = TextureUtil.glGenTextures();

			if (useDepth) {
				depthBuffer = OpenGlHelper.func_153185_f();
			}

			setFramebufferFilter(9728);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, framebufferTexture);
			GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, framebufferTextureWidth, framebufferTextureHeight,
					0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, (ByteBuffer) null);
			OpenGlHelper.func_153171_g(OpenGlHelper.field_153198_e, framebufferObject);
			OpenGlHelper.func_153188_a(OpenGlHelper.field_153198_e, OpenGlHelper.field_153200_g, 3553,
					framebufferTexture, 0);

			if (useDepth) {
				OpenGlHelper.func_153176_h(OpenGlHelper.field_153199_f, depthBuffer);
				if (net.minecraftforge.client.MinecraftForgeClient.getStencilBits() == 0) {
					OpenGlHelper.func_153186_a(OpenGlHelper.field_153199_f, 33190, framebufferTextureWidth,
							framebufferTextureHeight);
					OpenGlHelper.func_153190_b(OpenGlHelper.field_153198_e, OpenGlHelper.field_153201_h,
							OpenGlHelper.field_153199_f, depthBuffer);
				} else {
					OpenGlHelper.func_153186_a(OpenGlHelper.field_153199_f,
							org.lwjgl.opengl.EXTPackedDepthStencil.GL_DEPTH24_STENCIL8_EXT, framebufferTextureWidth,
							framebufferTextureHeight);
					OpenGlHelper.func_153190_b(OpenGlHelper.field_153198_e,
							org.lwjgl.opengl.EXTFramebufferObject.GL_DEPTH_ATTACHMENT_EXT, OpenGlHelper.field_153199_f,
							depthBuffer);
					OpenGlHelper.func_153190_b(OpenGlHelper.field_153198_e,
							org.lwjgl.opengl.EXTFramebufferObject.GL_STENCIL_ATTACHMENT_EXT,
							OpenGlHelper.field_153199_f, depthBuffer);
				}
			}

			framebufferClear();
			unbindFramebufferTexture();
		}
	}

	public void setFramebufferFilter(int p_147607_1_) {
		if (OpenGlHelper.isFramebufferEnabled()) {
			framebufferFilter = p_147607_1_;
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, framebufferTexture);
			GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, p_147607_1_);
			GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, p_147607_1_);
			GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, 10496.0F);
			GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, 10496.0F);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		}
	}

	public void checkFramebufferComplete() {
		int i = OpenGlHelper.func_153167_i(OpenGlHelper.field_153198_e);

		if (i != OpenGlHelper.field_153202_i) {
			if (i == OpenGlHelper.field_153203_j)
				throw new RuntimeException("GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT");
			else if (i == OpenGlHelper.field_153204_k)
				throw new RuntimeException("GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT");
			else if (i == OpenGlHelper.field_153205_l)
				throw new RuntimeException("GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER");
			else if (i == OpenGlHelper.field_153206_m)
				throw new RuntimeException("GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER");
			else
				throw new RuntimeException("glCheckFramebufferStatus returned unknown status:" + i);
		}
	}

	public void bindFramebufferTexture() {
		if (OpenGlHelper.isFramebufferEnabled()) {
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, framebufferTexture);
		}
	}

	public void unbindFramebufferTexture() {
		if (OpenGlHelper.isFramebufferEnabled()) {
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		}
	}

	public void bindFramebuffer(boolean p_147610_1_) {
		if (OpenGlHelper.isFramebufferEnabled()) {
			OpenGlHelper.func_153171_g(OpenGlHelper.field_153198_e, framebufferObject);

			if (p_147610_1_) {
				GL11.glViewport(0, 0, framebufferWidth, framebufferHeight);
			}
		}
	}

	public void unbindFramebuffer() {
		if (OpenGlHelper.isFramebufferEnabled()) {
			OpenGlHelper.func_153171_g(OpenGlHelper.field_153198_e, 0);
		}
	}

	public void setFramebufferColor(float p_147604_1_, float p_147604_2_, float p_147604_3_, float p_147604_4_) {
		framebufferColor[0] = p_147604_1_;
		framebufferColor[1] = p_147604_2_;
		framebufferColor[2] = p_147604_3_;
		framebufferColor[3] = p_147604_4_;
	}

	public void framebufferRender(int p_147615_1_, int p_147615_2_) {
		if (OpenGlHelper.isFramebufferEnabled()) {
			GL11.glColorMask(true, true, true, false);
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			GL11.glDepthMask(false);
			GL11.glMatrixMode(GL11.GL_PROJECTION);
			GL11.glLoadIdentity();
			GL11.glOrtho(0.0D, p_147615_1_, p_147615_2_, 0.0D, 1000.0D, 3000.0D);
			GL11.glMatrixMode(GL11.GL_MODELVIEW);
			GL11.glLoadIdentity();
			GL11.glTranslatef(0.0F, 0.0F, -2000.0F);
			GL11.glViewport(0, 0, p_147615_1_, p_147615_2_);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glDisable(GL11.GL_ALPHA_TEST);
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glEnable(GL11.GL_COLOR_MATERIAL);
			bindFramebufferTexture();
			float f = p_147615_1_;
			float f1 = p_147615_2_;
			float f2 = (float) framebufferWidth / (float) framebufferTextureWidth;
			float f3 = (float) framebufferHeight / (float) framebufferTextureHeight;
			Tessellator tessellator = Tessellator.instance;
			tessellator.startDrawingQuads();
			tessellator.setColorOpaque_I(-1);
			tessellator.addVertexWithUV(0.0D, f1, 0.0D, 0.0D, 0.0D);
			tessellator.addVertexWithUV(f, f1, 0.0D, f2, 0.0D);
			tessellator.addVertexWithUV(f, 0.0D, 0.0D, f2, f3);
			tessellator.addVertexWithUV(0.0D, 0.0D, 0.0D, 0.0D, f3);
			tessellator.draw();
			unbindFramebufferTexture();
			GL11.glDepthMask(true);
			GL11.glColorMask(true, true, true, true);
		}
	}

	public void framebufferClear() {
		bindFramebuffer(true);
		GL11.glClearColor(framebufferColor[0], framebufferColor[1], framebufferColor[2], framebufferColor[3]);
		int i = 16384;

		if (useDepth) {
			GL11.glClearDepth(1.0D);
			i |= 256;
		}

		GL11.glClear(i);
		unbindFramebuffer();
	}
}