package net.minecraft.client.shader;

import com.google.common.collect.Lists;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.util.JsonException;
import org.lwjgl.opengl.GL11;

import javax.vecmath.Matrix4f;
import java.util.Iterator;
import java.util.List;

@SideOnly(Side.CLIENT)
public class Shader {
	private final ShaderManager manager;
	public final Framebuffer framebufferIn;
	public final Framebuffer framebufferOut;
	private final List listAuxFramebuffers = Lists.newArrayList();
	private final List listAuxNames = Lists.newArrayList();
	private final List listAuxWidths = Lists.newArrayList();
	private final List listAuxHeights = Lists.newArrayList();
	private Matrix4f projectionMatrix;
	private static final String __OBFID = "CL_00001042";

	public Shader(IResourceManager p_i45089_1_, String p_i45089_2_, Framebuffer p_i45089_3_, Framebuffer p_i45089_4_)
			throws JsonException {
		manager = new ShaderManager(p_i45089_1_, p_i45089_2_);
		framebufferIn = p_i45089_3_;
		framebufferOut = p_i45089_4_;
	}

	public void deleteShader() {
		manager.func_147988_a();
	}

	public void addAuxFramebuffer(String p_148041_1_, Object p_148041_2_, int p_148041_3_, int p_148041_4_) {
		listAuxNames.add(listAuxNames.size(), p_148041_1_);
		listAuxFramebuffers.add(listAuxFramebuffers.size(), p_148041_2_);
		listAuxWidths.add(listAuxWidths.size(), Integer.valueOf(p_148041_3_));
		listAuxHeights.add(listAuxHeights.size(), Integer.valueOf(p_148041_4_));
	}

	private void preLoadShader() {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glDisable(GL11.GL_FOG);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_COLOR_MATERIAL);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
	}

	public void setProjectionMatrix(Matrix4f p_148045_1_) {
		projectionMatrix = p_148045_1_;
	}

	public void loadShader(float p_148042_1_) {
		preLoadShader();
		framebufferIn.unbindFramebuffer();
		float f1 = framebufferOut.framebufferTextureWidth;
		float f2 = framebufferOut.framebufferTextureHeight;
		GL11.glViewport(0, 0, (int) f1, (int) f2);
		manager.func_147992_a("DiffuseSampler", framebufferIn);

		for (int i = 0; i < listAuxFramebuffers.size(); ++i) {
			manager.func_147992_a((String) listAuxNames.get(i), listAuxFramebuffers.get(i));
			manager.func_147984_b("AuxSize" + i).func_148087_a(((Integer) listAuxWidths.get(i)).intValue(),
					((Integer) listAuxHeights.get(i)).intValue());
		}

		manager.func_147984_b("ProjMat").func_148088_a(projectionMatrix);
		manager.func_147984_b("InSize").func_148087_a(framebufferIn.framebufferTextureWidth,
				framebufferIn.framebufferTextureHeight);
		manager.func_147984_b("OutSize").func_148087_a(f1, f2);
		manager.func_147984_b("Time").func_148090_a(p_148042_1_);
		Minecraft minecraft = Minecraft.getMinecraft();
		manager.func_147984_b("ScreenSize").func_148087_a(minecraft.displayWidth, minecraft.displayHeight);
		manager.func_147995_c();
		framebufferOut.framebufferClear();
		framebufferOut.bindFramebuffer(false);
		GL11.glDepthMask(false);
		GL11.glColorMask(true, true, true, false);
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		tessellator.setColorOpaque_I(-1);
		tessellator.addVertex(0.0D, f2, 500.0D);
		tessellator.addVertex(f1, f2, 500.0D);
		tessellator.addVertex(f1, 0.0D, 500.0D);
		tessellator.addVertex(0.0D, 0.0D, 500.0D);
		tessellator.draw();
		GL11.glDepthMask(true);
		GL11.glColorMask(true, true, true, true);
		manager.func_147993_b();
		framebufferOut.unbindFramebuffer();
		framebufferIn.unbindFramebufferTexture();
		Iterator iterator = listAuxFramebuffers.iterator();

		while (iterator.hasNext()) {
			Object object = iterator.next();

			if (object instanceof Framebuffer) {
				((Framebuffer) object).unbindFramebufferTexture();
			}
		}
	}

	public ShaderManager getShaderManager() {
		return manager;
	}
}