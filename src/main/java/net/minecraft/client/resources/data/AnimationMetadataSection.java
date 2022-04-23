package net.minecraft.client.resources.data;

import com.google.common.collect.Sets;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@SideOnly(Side.CLIENT)
public class AnimationMetadataSection implements IMetadataSection {
	private final List animationFrames;
	private final int frameWidth;
	private final int frameHeight;
	private final int frameTime;
	private static final String __OBFID = "CL_00001106";

	public AnimationMetadataSection(List p_i1309_1_, int p_i1309_2_, int p_i1309_3_, int p_i1309_4_) {
		animationFrames = p_i1309_1_;
		frameWidth = p_i1309_2_;
		frameHeight = p_i1309_3_;
		frameTime = p_i1309_4_;
	}

	public int getFrameHeight() {
		return frameHeight;
	}

	public int getFrameWidth() {
		return frameWidth;
	}

	public int getFrameCount() {
		return animationFrames.size();
	}

	public int getFrameTime() {
		return frameTime;
	}

	private AnimationFrame getAnimationFrame(int p_130072_1_) {
		return (AnimationFrame) animationFrames.get(p_130072_1_);
	}

	public int getFrameTimeSingle(int p_110472_1_) {
		AnimationFrame animationframe = getAnimationFrame(p_110472_1_);
		return animationframe.hasNoTime() ? frameTime : animationframe.getFrameTime();
	}

	public boolean frameHasTime(int p_110470_1_) {
		return !((AnimationFrame) animationFrames.get(p_110470_1_)).hasNoTime();
	}

	public int getFrameIndex(int p_110468_1_) {
		return ((AnimationFrame) animationFrames.get(p_110468_1_)).getFrameIndex();
	}

	public Set getFrameIndexSet() {
		HashSet hashset = Sets.newHashSet();
		Iterator iterator = animationFrames.iterator();

		while (iterator.hasNext()) {
			AnimationFrame animationframe = (AnimationFrame) iterator.next();
			hashset.add(Integer.valueOf(animationframe.getFrameIndex()));
		}

		return hashset;
	}
}