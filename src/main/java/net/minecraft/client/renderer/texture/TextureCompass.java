package net.minecraft.client.renderer.texture;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;

@SideOnly(Side.CLIENT)
public class TextureCompass extends TextureAtlasSprite {
	public double currentAngle;
	public double angleDelta;
	private static final String __OBFID = "CL_00001071";

	public TextureCompass(String p_i1286_1_) {
		super(p_i1286_1_);
	}

	@Override
	public void updateAnimation() {
		Minecraft minecraft = Minecraft.getMinecraft();

		if (minecraft.theWorld != null && minecraft.thePlayer != null) {
			updateCompass(minecraft.theWorld, minecraft.thePlayer.posX, minecraft.thePlayer.posZ,
					minecraft.thePlayer.rotationYaw, false, false);
		} else {
			updateCompass(null, 0.0D, 0.0D, 0.0D, true, false);
		}
	}

	public void updateCompass(World p_94241_1_, double p_94241_2_, double p_94241_4_, double p_94241_6_,
			boolean p_94241_8_, boolean p_94241_9_) {
		if (!framesTextureData.isEmpty()) {
			double d3 = 0.0D;

			if (p_94241_1_ != null && !p_94241_8_) {
				ChunkCoordinates chunkcoordinates = p_94241_1_.getSpawnPoint();
				double d4 = chunkcoordinates.posX - p_94241_2_;
				double d5 = chunkcoordinates.posZ - p_94241_4_;
				p_94241_6_ %= 360.0D;
				d3 = -((p_94241_6_ - 90.0D) * Math.PI / 180.0D - Math.atan2(d5, d4));

				if (!p_94241_1_.provider.isSurfaceWorld()) {
					d3 = Math.random() * Math.PI * 2.0D;
				}
			}

			if (p_94241_9_) {
				currentAngle = d3;
			} else {
				double d6;

				for (d6 = d3 - currentAngle; d6 < -Math.PI; d6 += Math.PI * 2D) {
				}

				while (d6 >= Math.PI) {
					d6 -= Math.PI * 2D;
				}

				if (d6 < -1.0D) {
					d6 = -1.0D;
				}

				if (d6 > 1.0D) {
					d6 = 1.0D;
				}

				angleDelta += d6 * 0.1D;
				angleDelta *= 0.8D;
				currentAngle += angleDelta;
			}

			int i;

			for (i = (int) ((currentAngle / (Math.PI * 2D) + 1.0D) * framesTextureData.size())
					% framesTextureData.size(); i < 0; i = (i + framesTextureData.size()) % framesTextureData.size()) {
			}

			if (i != frameCounter) {
				frameCounter = i;
				TextureUtil.uploadTextureMipmap((int[][]) framesTextureData.get(frameCounter), width, height, originX,
						originY, false, false);
			}
		}
	}
}