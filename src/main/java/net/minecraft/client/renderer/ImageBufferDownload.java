package net.minecraft.client.renderer;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.ImageObserver;

@SideOnly(Side.CLIENT)
public class ImageBufferDownload implements IImageBuffer {
	private int[] imageData;
	private int imageWidth;
	private int imageHeight;
	private static final String __OBFID = "CL_00000956";

	@Override
	public BufferedImage parseUserSkin(BufferedImage p_78432_1_) {
		if (p_78432_1_ == null)
			return null;
		else {
			imageWidth = 64;
			imageHeight = 32;
			BufferedImage bufferedimage1 = new BufferedImage(imageWidth, imageHeight, 2);
			Graphics graphics = bufferedimage1.getGraphics();
			graphics.drawImage(p_78432_1_, 0, 0, null);
			graphics.dispose();
			imageData = ((DataBufferInt) bufferedimage1.getRaster().getDataBuffer()).getData();
			setAreaOpaque(0, 0, 32, 16);
			setAreaTransparent(32, 0, 64, 32);
			setAreaOpaque(0, 16, 64, 32);
			return bufferedimage1;
		}
	}

	@Override
	public void func_152634_a() {
	}

	private void setAreaTransparent(int p_78434_1_, int p_78434_2_, int p_78434_3_, int p_78434_4_) {
		if (!hasTransparency(p_78434_1_, p_78434_2_, p_78434_3_, p_78434_4_)) {
			for (int i1 = p_78434_1_; i1 < p_78434_3_; ++i1) {
				for (int j1 = p_78434_2_; j1 < p_78434_4_; ++j1) {
					imageData[i1 + j1 * imageWidth] &= 16777215;
				}
			}
		}
	}

	private void setAreaOpaque(int p_78433_1_, int p_78433_2_, int p_78433_3_, int p_78433_4_) {
		for (int i1 = p_78433_1_; i1 < p_78433_3_; ++i1) {
			for (int j1 = p_78433_2_; j1 < p_78433_4_; ++j1) {
				imageData[i1 + j1 * imageWidth] |= -16777216;
			}
		}
	}

	private boolean hasTransparency(int p_78435_1_, int p_78435_2_, int p_78435_3_, int p_78435_4_) {
		for (int i1 = p_78435_1_; i1 < p_78435_3_; ++i1) {
			for (int j1 = p_78435_2_; j1 < p_78435_4_; ++j1) {
				int k1 = imageData[i1 + j1 * imageWidth];

				if ((k1 >> 24 & 255) < 128)
					return true;
			}
		}

		return false;
	}
}