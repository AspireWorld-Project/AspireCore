package net.minecraft.client.renderer.texture;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class AbstractTexture implements ITextureObject {
	protected int glTextureId = -1;
	private static final String __OBFID = "CL_00001047";

	@Override
	public int getGlTextureId() {
		if (glTextureId == -1) {
			glTextureId = TextureUtil.glGenTextures();
		}

		return glTextureId;
	}

	public void deleteGlTexture() {
		if (glTextureId != -1) {
			TextureUtil.deleteTexture(glTextureId);
			glTextureId = -1;
		}
	}
}