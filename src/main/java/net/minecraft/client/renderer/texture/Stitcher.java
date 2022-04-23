package net.minecraft.client.renderer.texture;

import com.google.common.collect.Lists;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.StitcherException;
import net.minecraft.util.MathHelper;

import java.util.*;

@SideOnly(Side.CLIENT)
public class Stitcher {
	private final int mipmapLevelStitcher;
	private final Set setStitchHolders = new HashSet(256);
	private final List stitchSlots = new ArrayList(256);
	private int currentWidth;
	private int currentHeight;
	private final int maxWidth;
	private final int maxHeight;
	private final boolean forcePowerOf2;
	private final int maxTileDimension;
	private static final String __OBFID = "CL_00001054";

	public Stitcher(int p_i45095_1_, int p_i45095_2_, boolean p_i45095_3_, int p_i45095_4_, int p_i45095_5_) {
		mipmapLevelStitcher = p_i45095_5_;
		maxWidth = p_i45095_1_;
		maxHeight = p_i45095_2_;
		forcePowerOf2 = p_i45095_3_;
		maxTileDimension = p_i45095_4_;
	}

	public int getCurrentWidth() {
		return currentWidth;
	}

	public int getCurrentHeight() {
		return currentHeight;
	}

	public void addSprite(TextureAtlasSprite p_110934_1_) {
		Stitcher.Holder holder = new Stitcher.Holder(p_110934_1_, mipmapLevelStitcher);

		if (maxTileDimension > 0) {
			holder.setNewDimension(maxTileDimension);
		}

		setStitchHolders.add(holder);
	}

	public void doStitch() {
		Stitcher.Holder[] aholder = (Stitcher.Holder[]) setStitchHolders
				.toArray(new Stitcher.Holder[setStitchHolders.size()]);
		cpw.mods.fml.common.ProgressManager.ProgressBar bar = cpw.mods.fml.common.ProgressManager
				.push("Texture stitching", aholder.length);
		Arrays.sort(aholder);
		Stitcher.Holder[] aholder1 = aholder;
		int i = aholder.length;

		for (int j = 0; j < i; ++j) {
			Stitcher.Holder holder = aholder1[j];
			bar.step(holder.getAtlasSprite().getIconName());

			if (!allocateSlot(holder)) {
				String s = String.format("Unable to fit: %s - size: %dx%d - Maybe try a lowerresolution resourcepack?",
						holder.getAtlasSprite().getIconName(),
						Integer.valueOf(holder.getAtlasSprite().getIconWidth()),
						Integer.valueOf(holder.getAtlasSprite().getIconHeight()));
				throw new StitcherException(holder, s);
			}
		}

		if (forcePowerOf2) {
			currentWidth = MathHelper.roundUpToPowerOfTwo(currentWidth);
			currentHeight = MathHelper.roundUpToPowerOfTwo(currentHeight);
		}
		cpw.mods.fml.common.ProgressManager.pop(bar);
	}

	public List getStichSlots() {
		ArrayList arraylist = Lists.newArrayList();
		Iterator iterator = stitchSlots.iterator();

		while (iterator.hasNext()) {
			Stitcher.Slot slot = (Stitcher.Slot) iterator.next();
			slot.getAllStitchSlots(arraylist);
		}

		ArrayList arraylist1 = Lists.newArrayList();
		Iterator iterator1 = arraylist.iterator();

		while (iterator1.hasNext()) {
			Stitcher.Slot slot1 = (Stitcher.Slot) iterator1.next();
			Stitcher.Holder holder = slot1.getStitchHolder();
			TextureAtlasSprite textureatlassprite = holder.getAtlasSprite();
			textureatlassprite.initSprite(currentWidth, currentHeight, slot1.getOriginX(), slot1.getOriginY(),
					holder.isRotated());
			arraylist1.add(textureatlassprite);
		}

		return arraylist1;
	}

	private static int getMipmapDimension(int p_147969_0_, int p_147969_1_) {
		return (p_147969_0_ >> p_147969_1_) + ((p_147969_0_ & (1 << p_147969_1_) - 1) == 0 ? 0 : 1) << p_147969_1_;
	}

	private boolean allocateSlot(Stitcher.Holder p_94310_1_) {
		for (int i = 0; i < stitchSlots.size(); ++i) {
			if (((Stitcher.Slot) stitchSlots.get(i)).addSlot(p_94310_1_))
				return true;

			p_94310_1_.rotate();

			if (((Stitcher.Slot) stitchSlots.get(i)).addSlot(p_94310_1_))
				return true;

			p_94310_1_.rotate();
		}

		return expandAndAllocateSlot(p_94310_1_);
	}

	private boolean expandAndAllocateSlot(Stitcher.Holder p_94311_1_) {
		int i = Math.min(p_94311_1_.getWidth(), p_94311_1_.getHeight());
		boolean flag = currentWidth == 0 && currentHeight == 0;
		boolean flag1;
		int j;

		if (forcePowerOf2) {
			j = MathHelper.roundUpToPowerOfTwo(currentWidth);
			int k = MathHelper.roundUpToPowerOfTwo(currentHeight);
			int l = MathHelper.roundUpToPowerOfTwo(currentWidth + i);
			int i1 = MathHelper.roundUpToPowerOfTwo(currentHeight + i);
			boolean flag2 = l <= maxWidth;
			boolean flag3 = i1 <= maxHeight;

			if (!flag2 && !flag3)
				return false;

			boolean flag4 = j != l;
			boolean flag5 = k != i1;

			if (flag4 ^ flag5) {
				flag1 = !flag4;
			} else {
				flag1 = flag2 && j <= k;
			}
		} else {
			boolean flag6 = currentWidth + i <= maxWidth;
			boolean flag7 = currentHeight + i <= maxHeight;

			if (!flag6 && !flag7)
				return false;

			flag1 = flag6 && (flag || currentWidth <= currentHeight);
		}

		j = Math.max(p_94311_1_.getWidth(), p_94311_1_.getHeight());

		if (MathHelper.roundUpToPowerOfTwo((flag1 ? currentHeight : currentWidth) + j) > (flag1 ? maxHeight : maxWidth))
			return false;
		else {
			Stitcher.Slot slot;

			if (flag1) {
				if (p_94311_1_.getWidth() > p_94311_1_.getHeight()) {
					p_94311_1_.rotate();
				}

				if (currentHeight == 0) {
					currentHeight = p_94311_1_.getHeight();
				}

				slot = new Stitcher.Slot(currentWidth, 0, p_94311_1_.getWidth(), currentHeight);
				currentWidth += p_94311_1_.getWidth();
			} else {
				slot = new Stitcher.Slot(0, currentHeight, currentWidth, p_94311_1_.getHeight());
				currentHeight += p_94311_1_.getHeight();
			}

			slot.addSlot(p_94311_1_);
			stitchSlots.add(slot);
			return true;
		}
	}

	@SideOnly(Side.CLIENT)
	public static class Holder implements Comparable {
		private final TextureAtlasSprite theTexture;
		private final int width;
		private final int height;
		private final int mipmapLevelHolder;
		private boolean rotated;
		private float scaleFactor = 1.0F;
		private static final String __OBFID = "CL_00001055";

		public Holder(TextureAtlasSprite p_i45094_1_, int p_i45094_2_) {
			theTexture = p_i45094_1_;
			width = p_i45094_1_.getIconWidth();
			height = p_i45094_1_.getIconHeight();
			mipmapLevelHolder = p_i45094_2_;
			rotated = Stitcher.getMipmapDimension(height, p_i45094_2_) > Stitcher.getMipmapDimension(width,
					p_i45094_2_);
		}

		public TextureAtlasSprite getAtlasSprite() {
			return theTexture;
		}

		public int getWidth() {
			return rotated ? Stitcher.getMipmapDimension((int) (height * scaleFactor), mipmapLevelHolder)
					: Stitcher.getMipmapDimension((int) (width * scaleFactor), mipmapLevelHolder);
		}

		public int getHeight() {
			return rotated ? Stitcher.getMipmapDimension((int) (width * scaleFactor), mipmapLevelHolder)
					: Stitcher.getMipmapDimension((int) (height * scaleFactor), mipmapLevelHolder);
		}

		public void rotate() {
			rotated = !rotated;
		}

		public boolean isRotated() {
			return rotated;
		}

		public void setNewDimension(int p_94196_1_) {
			if (width > p_94196_1_ && height > p_94196_1_) {
				scaleFactor = (float) p_94196_1_ / (float) Math.min(width, height);
			}
		}

		@Override
		public String toString() {
			return "Holder{width=" + width + ", height=" + height + '}';
		}

		public int compareTo(Stitcher.Holder p_compareTo_1_) {
			int i;

			if (getHeight() == p_compareTo_1_.getHeight()) {
				if (getWidth() == p_compareTo_1_.getWidth()) {
					if (theTexture.getIconName() == null)
						return p_compareTo_1_.theTexture.getIconName() == null ? 0 : -1;

					return theTexture.getIconName().compareTo(p_compareTo_1_.theTexture.getIconName());
				}

				i = getWidth() < p_compareTo_1_.getWidth() ? 1 : -1;
			} else {
				i = getHeight() < p_compareTo_1_.getHeight() ? 1 : -1;
			}

			return i;
		}

		@Override
		public int compareTo(Object p_compareTo_1_) {
			return this.compareTo((Stitcher.Holder) p_compareTo_1_);
		}
	}

	@SideOnly(Side.CLIENT)
	public static class Slot {
		private final int originX;
		private final int originY;
		private final int width;
		private final int height;
		private List subSlots;
		private Stitcher.Holder holder;
		private static final String __OBFID = "CL_00001056";

		public Slot(int p_i1277_1_, int p_i1277_2_, int p_i1277_3_, int p_i1277_4_) {
			originX = p_i1277_1_;
			originY = p_i1277_2_;
			width = p_i1277_3_;
			height = p_i1277_4_;
		}

		public Stitcher.Holder getStitchHolder() {
			return holder;
		}

		public int getOriginX() {
			return originX;
		}

		public int getOriginY() {
			return originY;
		}

		public boolean addSlot(Stitcher.Holder p_94182_1_) {
			if (holder != null)
				return false;
			else {
				int i = p_94182_1_.getWidth();
				int j = p_94182_1_.getHeight();

				if (i <= width && j <= height) {
					if (i == width && j == height) {
						holder = p_94182_1_;
						return true;
					} else {
						if (subSlots == null) {
							subSlots = new ArrayList(1);
							subSlots.add(new Stitcher.Slot(originX, originY, i, j));
							int k = width - i;
							int l = height - j;

							if (l > 0 && k > 0) {
								int i1 = Math.max(height, k);
								int j1 = Math.max(width, l);

								if (i1 >= j1) {
									subSlots.add(new Stitcher.Slot(originX, originY + j, i, l));
									subSlots.add(new Stitcher.Slot(originX + i, originY, k, height));
								} else {
									subSlots.add(new Stitcher.Slot(originX + i, originY, k, j));
									subSlots.add(new Stitcher.Slot(originX, originY + j, width, l));
								}
							} else if (k == 0) {
								subSlots.add(new Stitcher.Slot(originX, originY + j, i, l));
							} else if (l == 0) {
								subSlots.add(new Stitcher.Slot(originX + i, originY, k, j));
							}
						}

						Iterator iterator = subSlots.iterator();
						Stitcher.Slot slot;

						do {
							if (!iterator.hasNext())
								return false;

							slot = (Stitcher.Slot) iterator.next();
						} while (!slot.addSlot(p_94182_1_));

						return true;
					}
				} else
					return false;
			}
		}

		public void getAllStitchSlots(List p_94184_1_) {
			if (holder != null) {
				p_94184_1_.add(this);
			} else if (subSlots != null) {
				Iterator iterator = subSlots.iterator();

				while (iterator.hasNext()) {
					Stitcher.Slot slot = (Stitcher.Slot) iterator.next();
					slot.getAllStitchSlots(p_94184_1_);
				}
			}
		}

		@Override
		public String toString() {
			return "Slot{originX=" + originX + ", originY=" + originY + ", width=" + width + ", height=" + height
					+ ", texture=" + holder + ", subSlots=" + subSlots + '}';
		}
	}
}