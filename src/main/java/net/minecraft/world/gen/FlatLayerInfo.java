package net.minecraft.world.gen;

import net.minecraft.block.Block;

public class FlatLayerInfo {
	private final Block field_151537_a;
	private int layerCount;
	private int layerFillBlockMeta;
	private int layerMinimumY;
	public FlatLayerInfo(int p_i45467_1_, Block p_i45467_2_) {
		layerCount = 1;
		layerCount = p_i45467_1_;
		field_151537_a = p_i45467_2_;
	}

	public FlatLayerInfo(int p_i45468_1_, Block p_i45468_2_, int p_i45468_3_) {
		this(p_i45468_1_, p_i45468_2_);
		layerFillBlockMeta = p_i45468_3_;
	}

	public int getLayerCount() {
		return layerCount;
	}

	public Block func_151536_b() {
		return field_151537_a;
	}

	public int getFillBlockMeta() {
		return layerFillBlockMeta;
	}

	public int getMinY() {
		return layerMinimumY;
	}

	public void setMinY(int p_82660_1_) {
		layerMinimumY = p_82660_1_;
	}

	@Override
	public String toString() {
		String s = Integer.toString(Block.getIdFromBlock(field_151537_a));

		if (layerCount > 1) {
			s = layerCount + "x" + s;
		}

		if (layerFillBlockMeta > 0) {
			s = s + ":" + layerFillBlockMeta;
		}

		return s;
	}
}