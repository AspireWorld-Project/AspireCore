package net.minecraft.world.chunk;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class EmptyChunk extends Chunk {
	private static final String __OBFID = "CL_00000372";

	public EmptyChunk(World p_i1994_1_, int p_i1994_2_, int p_i1994_3_) {
		super(p_i1994_1_, p_i1994_2_, p_i1994_3_);
	}

	@Override
	public boolean isAtLocation(int p_76600_1_, int p_76600_2_) {
		return p_76600_1_ == xPosition && p_76600_2_ == zPosition;
	}

	@Override
	public int getHeightValue(int p_76611_1_, int p_76611_2_) {
		return 0;
	}

	@Override
	public void generateSkylightMap() {
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void generateHeightMap() {
	}

	@Override
	public Block getBlock(int p_150810_1_, int p_150810_2_, int p_150810_3_) {
		return Blocks.air;
	}

	@Override
	public int func_150808_b(int p_150808_1_, int p_150808_2_, int p_150808_3_) {
		return 255;
	}

	@Override
	public boolean func_150807_a(int p_150807_1_, int p_150807_2_, int p_150807_3_, Block p_150807_4_,
			int p_150807_5_) {
		return true;
	}

	@Override
	public int getBlockMetadata(int p_76628_1_, int p_76628_2_, int p_76628_3_) {
		return 0;
	}

	@Override
	public boolean setBlockMetadata(int p_76589_1_, int p_76589_2_, int p_76589_3_, int p_76589_4_) {
		return false;
	}

	@Override
	public int getSavedLightValue(EnumSkyBlock p_76614_1_, int p_76614_2_, int p_76614_3_, int p_76614_4_) {
		return 0;
	}

	@Override
	public void setLightValue(EnumSkyBlock p_76633_1_, int p_76633_2_, int p_76633_3_, int p_76633_4_, int p_76633_5_) {
	}

	@Override
	public int getBlockLightValue(int p_76629_1_, int p_76629_2_, int p_76629_3_, int p_76629_4_) {
		return 0;
	}

	@Override
	public void addEntity(Entity p_76612_1_) {
	}

	@Override
	public void removeEntity(Entity p_76622_1_) {
	}

	@Override
	public void removeEntityAtIndex(Entity p_76608_1_, int p_76608_2_) {
	}

	@Override
	public boolean canBlockSeeTheSky(int p_76619_1_, int p_76619_2_, int p_76619_3_) {
		return false;
	}

	@Override
	public TileEntity func_150806_e(int p_150806_1_, int p_150806_2_, int p_150806_3_) {
		return null;
	}

	@Override
	public void addTileEntity(TileEntity p_150813_1_) {
	}

	@Override
	public void func_150812_a(int p_150812_1_, int p_150812_2_, int p_150812_3_, TileEntity p_150812_4_) {
	}

	@Override
	public void removeTileEntity(int p_150805_1_, int p_150805_2_, int p_150805_3_) {
	}

	@Override
	public void onChunkLoad() {
	}

	@Override
	public void onChunkUnload() {
	}

	@Override
	public void setChunkModified() {
	}

	@Override
	public void getEntitiesWithinAABBForEntity(Entity p_76588_1_, AxisAlignedBB p_76588_2_, List p_76588_3_,
			IEntitySelector p_76588_4_) {
	}

	@Override
	public void getEntitiesOfTypeWithinAAAB(Class p_76618_1_, AxisAlignedBB p_76618_2_, List p_76618_3_,
			IEntitySelector p_76618_4_) {
	}

	@Override
	public boolean needsSaving(boolean p_76601_1_) {
		return false;
	}

	@Override
	public Random getRandomWithSeed(long p_76617_1_) {
		return new Random(worldObj.getSeed() + xPosition * xPosition * 4987142 + xPosition * 5947611
				+ zPosition * zPosition * 4392871L + zPosition * 389711 ^ p_76617_1_);
	}

	@Override
	public boolean isEmpty() {
		return true;
	}

	@Override
	public boolean getAreLevelsEmpty(int p_76606_1_, int p_76606_2_) {
		return true;
	}
}