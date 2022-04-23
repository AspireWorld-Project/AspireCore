package net.minecraft.client.shader;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TesselatorVertexState {
	private final int[] rawBuffer;
	private final int rawBufferIndex;
	private final int vertexCount;
	private final boolean hasTexture;
	private final boolean hasBrightness;
	private final boolean hasNormals;
	private final boolean hasColor;
	private static final String __OBFID = "CL_00000961";

	public TesselatorVertexState(int[] p_i45079_1_, int p_i45079_2_, int p_i45079_3_, boolean p_i45079_4_,
			boolean p_i45079_5_, boolean p_i45079_6_, boolean p_i45079_7_) {
		rawBuffer = p_i45079_1_;
		rawBufferIndex = p_i45079_2_;
		vertexCount = p_i45079_3_;
		hasTexture = p_i45079_4_;
		hasBrightness = p_i45079_5_;
		hasNormals = p_i45079_6_;
		hasColor = p_i45079_7_;
	}

	public int[] getRawBuffer() {
		return rawBuffer;
	}

	public int getRawBufferIndex() {
		return rawBufferIndex;
	}

	public int getVertexCount() {
		return vertexCount;
	}

	public boolean getHasTexture() {
		return hasTexture;
	}

	public boolean getHasBrightness() {
		return hasBrightness;
	}

	public boolean getHasNormals() {
		return hasNormals;
	}

	public boolean getHasColor() {
		return hasColor;
	}
}