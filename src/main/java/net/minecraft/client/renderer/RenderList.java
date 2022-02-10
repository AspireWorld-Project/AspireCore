package net.minecraft.client.renderer;

import java.nio.IntBuffer;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderList {
	public int renderChunkX;
	public int renderChunkY;
	public int renderChunkZ;
	private double cameraX;
	private double cameraY;
	private double cameraZ;
	private IntBuffer glLists = GLAllocation.createDirectIntBuffer(65536);
	private boolean valid;
	private boolean bufferFlipped;
	private static final String __OBFID = "CL_00000957";

	public void setupRenderList(int p_78422_1_, int p_78422_2_, int p_78422_3_, double p_78422_4_, double p_78422_6_,
			double p_78422_8_) {
		valid = true;
		glLists.clear();
		renderChunkX = p_78422_1_;
		renderChunkY = p_78422_2_;
		renderChunkZ = p_78422_3_;
		cameraX = p_78422_4_;
		cameraY = p_78422_6_;
		cameraZ = p_78422_8_;
	}

	public boolean rendersChunk(int p_78418_1_, int p_78418_2_, int p_78418_3_) {
		return !valid ? false : p_78418_1_ == renderChunkX && p_78418_2_ == renderChunkY && p_78418_3_ == renderChunkZ;
	}

	public void addGLRenderList(int p_78420_1_) {
		glLists.put(p_78420_1_);

		if (glLists.remaining() == 0) {
			callLists();
		}
	}

	public void callLists() {
		if (valid) {
			if (!bufferFlipped) {
				glLists.flip();
				bufferFlipped = true;
			}

			if (glLists.remaining() > 0) {
				GL11.glPushMatrix();
				GL11.glTranslatef((float) (renderChunkX - cameraX), (float) (renderChunkY - cameraY),
						(float) (renderChunkZ - cameraZ));
				GL11.glCallLists(glLists);
				GL11.glPopMatrix();
			}
		}
	}

	public void resetList() {
		valid = false;
		bufferFlipped = false;
	}
}