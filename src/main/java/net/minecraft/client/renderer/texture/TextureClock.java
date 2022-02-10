package net.minecraft.client.renderer.texture;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;

@SideOnly(Side.CLIENT)
public class TextureClock extends TextureAtlasSprite {
	private double field_94239_h;
	private double field_94240_i;
	private static final String __OBFID = "CL_00001070";

	public TextureClock(String p_i1285_1_) {
		super(p_i1285_1_);
	}

	@Override
	public void updateAnimation() {
		if (!framesTextureData.isEmpty()) {
			Minecraft minecraft = Minecraft.getMinecraft();
			double d0 = 0.0D;

			if (minecraft.theWorld != null && minecraft.thePlayer != null) {
				float f = minecraft.theWorld.getCelestialAngle(1.0F);
				d0 = f;

				if (!minecraft.theWorld.provider.isSurfaceWorld()) {
					d0 = Math.random();
				}
			}

			double d1;

			for (d1 = d0 - field_94239_h; d1 < -0.5D; ++d1) {
				;
			}

			while (d1 >= 0.5D) {
				--d1;
			}

			if (d1 < -1.0D) {
				d1 = -1.0D;
			}

			if (d1 > 1.0D) {
				d1 = 1.0D;
			}

			field_94240_i += d1 * 0.1D;
			field_94240_i *= 0.8D;
			field_94239_h += field_94240_i;
			int i;

			for (i = (int) ((field_94239_h + 1.0D) * framesTextureData.size())
					% framesTextureData.size(); i < 0; i = (i + framesTextureData.size()) % framesTextureData.size()) {
				;
			}

			if (i != frameCounter) {
				frameCounter = i;
				TextureUtil.uploadTextureMipmap((int[][]) framesTextureData.get(frameCounter), width, height, originX,
						originY, false, false);
			}
		}
	}
}