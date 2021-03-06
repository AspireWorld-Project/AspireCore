package net.minecraft.client.renderer.texture;

import com.google.common.collect.Lists;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.data.AnimationFrame;
import net.minecraft.client.resources.data.AnimationMetadataSection;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.IIcon;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;

@SideOnly(Side.CLIENT)
public class TextureAtlasSprite implements IIcon {
	private final String iconName;
	protected List framesTextureData = Lists.newArrayList();
	private AnimationMetadataSection animationMetadata;
	protected boolean rotated;
	private boolean useAnisotropicFiltering;
	protected int originX;
	protected int originY;
	protected int width;
	protected int height;
	private float minU;
	private float maxU;
	private float minV;
	private float maxV;
	protected int frameCounter;
	protected int tickCounter;
	private static final String __OBFID = "CL_00001062";

	protected TextureAtlasSprite(String p_i1282_1_) {
		iconName = p_i1282_1_;
	}

	public void initSprite(int p_110971_1_, int p_110971_2_, int p_110971_3_, int p_110971_4_, boolean p_110971_5_) {
		originX = p_110971_3_;
		originY = p_110971_4_;
		rotated = p_110971_5_;
		float f = (float) (0.009999999776482582D / p_110971_1_);
		float f1 = (float) (0.009999999776482582D / p_110971_2_);
		minU = p_110971_3_ / (float) p_110971_1_ + f;
		maxU = (p_110971_3_ + width) / (float) p_110971_1_ - f;
		minV = (float) p_110971_4_ / (float) p_110971_2_ + f1;
		maxV = (float) (p_110971_4_ + height) / (float) p_110971_2_ - f1;

		if (useAnisotropicFiltering) {
			float f2 = 8.0F / p_110971_1_;
			float f3 = 8.0F / p_110971_2_;
			minU += f2;
			maxU -= f2;
			minV += f3;
			maxV -= f3;
		}
	}

	public void copyFrom(TextureAtlasSprite p_94217_1_) {
		originX = p_94217_1_.originX;
		originY = p_94217_1_.originY;
		width = p_94217_1_.width;
		height = p_94217_1_.height;
		rotated = p_94217_1_.rotated;
		minU = p_94217_1_.minU;
		maxU = p_94217_1_.maxU;
		minV = p_94217_1_.minV;
		maxV = p_94217_1_.maxV;
	}

	public int getOriginX() {
		return originX;
	}

	public int getOriginY() {
		return originY;
	}

	@Override
	public int getIconWidth() {
		return width;
	}

	@Override
	public int getIconHeight() {
		return height;
	}

	@Override
	public float getMinU() {
		return minU;
	}

	@Override
	public float getMaxU() {
		return maxU;
	}

	@Override
	public float getInterpolatedU(double p_94214_1_) {
		float f = maxU - minU;
		return minU + f * (float) p_94214_1_ / 16.0F;
	}

	@Override
	public float getMinV() {
		return minV;
	}

	@Override
	public float getMaxV() {
		return maxV;
	}

	@Override
	public float getInterpolatedV(double p_94207_1_) {
		float f = maxV - minV;
		return minV + f * ((float) p_94207_1_ / 16.0F);
	}

	@Override
	public String getIconName() {
		return iconName;
	}

	public void updateAnimation() {
		++tickCounter;

		if (tickCounter >= animationMetadata.getFrameTimeSingle(frameCounter)) {
			int i = animationMetadata.getFrameIndex(frameCounter);
			int j = animationMetadata.getFrameCount() == 0 ? framesTextureData.size()
					: animationMetadata.getFrameCount();
			frameCounter = (frameCounter + 1) % j;
			tickCounter = 0;
			int k = animationMetadata.getFrameIndex(frameCounter);

			if (i != k && k >= 0 && k < framesTextureData.size()) {
				TextureUtil.uploadTextureMipmap((int[][]) framesTextureData.get(k), width, height, originX, originY,
						false, false);
			}
		}
	}

	public int[][] getFrameTextureData(int p_147965_1_) {
		return (int[][]) framesTextureData.get(p_147965_1_);
	}

	public int getFrameCount() {
		return framesTextureData.size();
	}

	public void setIconWidth(int p_110966_1_) {
		width = p_110966_1_;
	}

	public void setIconHeight(int p_110969_1_) {
		height = p_110969_1_;
	}

	public void loadSprite(BufferedImage[] p_147964_1_, AnimationMetadataSection p_147964_2_, boolean p_147964_3_) {
		resetSprite();
		useAnisotropicFiltering = p_147964_3_;
		int i = p_147964_1_[0].getWidth();
		int j = p_147964_1_[0].getHeight();
		width = i;
		height = j;

		if (p_147964_3_) {
			width += 16;
			height += 16;
		}

		int[][] aint = new int[p_147964_1_.length][];
		int k;

		for (k = 0; k < p_147964_1_.length; ++k) {
			BufferedImage bufferedimage = p_147964_1_[k];

			if (bufferedimage != null) {
				if (k > 0 && (bufferedimage.getWidth() != i >> k || bufferedimage.getHeight() != j >> k))
					throw new RuntimeException(
							String.format("Unable to load miplevel: %d, image is size: %dx%d, expected %dx%d",
									Integer.valueOf(k), Integer.valueOf(bufferedimage.getWidth()),
									Integer.valueOf(bufferedimage.getHeight()), Integer.valueOf(i >> k),
									Integer.valueOf(j >> k)));

				aint[k] = new int[bufferedimage.getWidth() * bufferedimage.getHeight()];
				bufferedimage.getRGB(0, 0, bufferedimage.getWidth(), bufferedimage.getHeight(), aint[k], 0,
						bufferedimage.getWidth());
			}
		}

		if (p_147964_2_ == null) {
			if (j != i)
				throw new RuntimeException("broken aspect ratio and not an animation");

			fixTransparentPixels(aint);
			framesTextureData.add(prepareAnisotropicFiltering(aint, i, j));
		} else {
			k = j / i;
			int j1 = i;
			int l = i;
			height = width;
			int i1;

			if (p_147964_2_.getFrameCount() > 0) {
				Iterator iterator = p_147964_2_.getFrameIndexSet().iterator();

				while (iterator.hasNext()) {
					i1 = ((Integer) iterator.next()).intValue();

					if (i1 >= k)
						throw new RuntimeException("invalid frameindex " + i1);

					allocateFrameTextureData(i1);
					framesTextureData.set(i1, prepareAnisotropicFiltering(getFrameTextureData(aint, j1, l, i1), j1, l));
				}

				animationMetadata = p_147964_2_;
			} else {
				ArrayList arraylist = Lists.newArrayList();

				for (i1 = 0; i1 < k; ++i1) {
					framesTextureData.add(prepareAnisotropicFiltering(getFrameTextureData(aint, j1, l, i1), j1, l));
					arraylist.add(new AnimationFrame(i1, -1));
				}

				animationMetadata = new AnimationMetadataSection(arraylist, width, height, p_147964_2_.getFrameTime());
			}
		}
	}

	public void generateMipmaps(int p_147963_1_) {
		ArrayList arraylist = Lists.newArrayList();

		for (int j = 0; j < framesTextureData.size(); ++j) {
			final int[][] aint = (int[][]) framesTextureData.get(j);

			if (aint != null) {
				try {
					arraylist.add(TextureUtil.generateMipmapData(p_147963_1_, width, aint));
				} catch (Throwable throwable) {
					CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Generating mipmaps for frame");
					CrashReportCategory crashreportcategory = crashreport.makeCategory("Frame being iterated");
					crashreportcategory.addCrashSection("Frame index", Integer.valueOf(j));
					crashreportcategory.addCrashSectionCallable("Frame sizes", new Callable() {
						private static final String __OBFID = "CL_00001063";

						@Override
						public String call() {
							StringBuilder stringbuilder = new StringBuilder();
							int[][] aint1 = aint;
							int k = aint1.length;

							for (int l = 0; l < k; ++l) {
								int[] aint2 = aint1[l];

								if (stringbuilder.length() > 0) {
									stringbuilder.append(", ");
								}

								stringbuilder.append(aint2 == null ? "null" : Integer.valueOf(aint2.length));
							}

							return stringbuilder.toString();
						}
					});
					throw new ReportedException(crashreport);
				}
			}
		}

		setFramesTextureData(arraylist);
	}

	private void fixTransparentPixels(int[][] p_147961_1_) {
		int[] aint1 = p_147961_1_[0];
		int i = 0;
		int j = 0;
		int k = 0;
		int l = 0;
		int i1;

		for (i1 = 0; i1 < aint1.length; ++i1) {
			if ((aint1[i1] & -16777216) != 0) {
				j += aint1[i1] >> 16 & 255;
				k += aint1[i1] >> 8 & 255;
				l += aint1[i1] >> 0 & 255;
				++i;
			}
		}

		if (i != 0) {
			j /= i;
			k /= i;
			l /= i;

			for (i1 = 0; i1 < aint1.length; ++i1) {
				if ((aint1[i1] & -16777216) == 0) {
					aint1[i1] = j << 16 | k << 8 | l;
				}
			}
		}
	}

	private int[][] prepareAnisotropicFiltering(int[][] p_147960_1_, int p_147960_2_, int p_147960_3_) {
		if (!useAnisotropicFiltering)
			return p_147960_1_;
		else {
			int[][] aint1 = new int[p_147960_1_.length][];

			for (int k = 0; k < p_147960_1_.length; ++k) {
				int[] aint2 = p_147960_1_[k];

				if (aint2 != null) {
					int[] aint3 = new int[(p_147960_2_ + 16 >> k) * (p_147960_3_ + 16 >> k)];
					System.arraycopy(aint2, 0, aint3, 0, aint2.length);
					aint1[k] = TextureUtil.prepareAnisotropicData(aint3, p_147960_2_ >> k, p_147960_3_ >> k, 8 >> k);
				}
			}

			return aint1;
		}
	}

	private void allocateFrameTextureData(int p_130099_1_) {
		if (framesTextureData.size() <= p_130099_1_) {
			for (int j = framesTextureData.size(); j <= p_130099_1_; ++j) {
				framesTextureData.add(null);
			}
		}
	}

	private static int[][] getFrameTextureData(int[][] p_147962_0_, int p_147962_1_, int p_147962_2_, int p_147962_3_) {
		int[][] aint1 = new int[p_147962_0_.length][];

		for (int l = 0; l < p_147962_0_.length; ++l) {
			int[] aint2 = p_147962_0_[l];

			if (aint2 != null) {
				aint1[l] = new int[(p_147962_1_ >> l) * (p_147962_2_ >> l)];
				System.arraycopy(aint2, p_147962_3_ * aint1[l].length, aint1[l], 0, aint1[l].length);
			}
		}

		return aint1;
	}

	public void clearFramesTextureData() {
		framesTextureData.clear();
	}

	public boolean hasAnimationMetadata() {
		return animationMetadata != null;
	}

	public void setFramesTextureData(List p_110968_1_) {
		framesTextureData = p_110968_1_;
	}

	private void resetSprite() {
		animationMetadata = null;
		setFramesTextureData(Lists.newArrayList());
		frameCounter = 0;
		tickCounter = 0;
	}

	@Override
	public String toString() {
		return "TextureAtlasSprite{name='" + iconName + '\'' + ", frameCount=" + framesTextureData.size()
				+ ", rotated=" + rotated + ", x=" + originX + ", y=" + originY + ", height=" + height + ", width="
				+ width + ", u0=" + minU + ", u1=" + maxU + ", v0=" + minV + ", v1=" + maxV + '}';
	}

	/**
	 * The result of this function determines is the below 'load' function is
	 * called, and the default vanilla loading code is bypassed completely.
	 *
	 * @param manager
	 * @param location
	 * @return True to use your own custom load code and bypass vanilla loading.
	 */
	public boolean hasCustomLoader(IResourceManager manager, ResourceLocation location) {
		return false;
	}

	/**
	 * Load the specified resource as this sprite's data. Returning false from this
	 * function will prevent this icon from being stitched onto the master texture.
	 *
	 * @param manager
	 *            Main resource manager
	 * @param location
	 *            File resource location
	 * @return False to prevent this Icon from being stitched
	 */
	public boolean load(IResourceManager manager, ResourceLocation location) {
		return true;
	}
}