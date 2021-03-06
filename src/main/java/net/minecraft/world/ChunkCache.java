package net.minecraft.world;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.util.ForgeDirection;

public class ChunkCache implements IBlockAccess {
	private final int chunkX;
	private final int chunkZ;
	private final Chunk[][] chunkArray;
	private boolean isEmpty;
	private final World worldObj;
	public ChunkCache(World p_i1964_1_, int p_i1964_2_, int p_i1964_3_, int p_i1964_4_, int p_i1964_5_, int p_i1964_6_,
			int p_i1964_7_, int p_i1964_8_) {
		worldObj = p_i1964_1_;
		chunkX = p_i1964_2_ - p_i1964_8_ >> 4;
		chunkZ = p_i1964_4_ - p_i1964_8_ >> 4;
		int l1 = p_i1964_5_ + p_i1964_8_ >> 4;
		int i2 = p_i1964_7_ + p_i1964_8_ >> 4;
		chunkArray = new Chunk[l1 - chunkX + 1][i2 - chunkZ + 1];
		isEmpty = true;
		int j2;
		int k2;
		Chunk chunk;

		for (j2 = chunkX; j2 <= l1; ++j2) {
			for (k2 = chunkZ; k2 <= i2; ++k2) {
				chunk = p_i1964_1_.getChunkIfExists(j2, k2);

				if (chunk != null) {
					chunkArray[j2 - chunkX][k2 - chunkZ] = chunk;
				}
			}
		}

		for (j2 = p_i1964_2_ >> 4; j2 <= p_i1964_5_ >> 4; ++j2) {
			for (k2 = p_i1964_4_ >> 4; k2 <= p_i1964_7_ >> 4; ++k2) {
				chunk = chunkArray[j2 - chunkX][k2 - chunkZ];

				if (chunk != null && !chunk.getAreLevelsEmpty(p_i1964_3_, p_i1964_6_)) {
					isEmpty = false;
				}
			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean extendedLevelsInChunkCache() {
		return isEmpty;
	}

	@Override
	public Block getBlock(int p_147439_1_, int p_147439_2_, int p_147439_3_) {
		Block block = Blocks.air;

		if (p_147439_2_ >= 0 && p_147439_2_ < 256) {
			int l = (p_147439_1_ >> 4) - chunkX;
			int i1 = (p_147439_3_ >> 4) - chunkZ;

			if (l >= 0 && l < chunkArray.length && i1 >= 0 && i1 < chunkArray[l].length) {
				Chunk chunk = chunkArray[l][i1];

				if (chunk != null) {
					block = chunk.getBlock(p_147439_1_ & 15, p_147439_2_, p_147439_3_ & 15);
				}
			}
		}

		return block;
	}

	@Override
	public TileEntity getTileEntity(int p_147438_1_, int p_147438_2_, int p_147438_3_) {
		int l = (p_147438_1_ >> 4) - chunkX;
		int i1 = (p_147438_3_ >> 4) - chunkZ;
		if (l < 0 || l >= chunkArray.length || i1 < 0 || i1 >= chunkArray[l].length)
			return null;
		if (chunkArray[l][i1] == null)
			return null;
		return chunkArray[l][i1].func_150806_e(p_147438_1_ & 15, p_147438_2_, p_147438_3_ & 15);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getLightBrightnessForSkyBlocks(int p_72802_1_, int p_72802_2_, int p_72802_3_, int p_72802_4_) {
		int i1 = getSkyBlockTypeBrightness(EnumSkyBlock.Sky, p_72802_1_, p_72802_2_, p_72802_3_);
		int j1 = getSkyBlockTypeBrightness(EnumSkyBlock.Block, p_72802_1_, p_72802_2_, p_72802_3_);

		if (j1 < p_72802_4_) {
			j1 = p_72802_4_;
		}

		return i1 << 20 | j1 << 4;
	}

	@Override
	public int getBlockMetadata(int p_72805_1_, int p_72805_2_, int p_72805_3_) {
		if (p_72805_2_ < 0)
			return 0;
		else if (p_72805_2_ >= 256)
			return 0;
		else {
			int l = (p_72805_1_ >> 4) - chunkX;
			int i1 = (p_72805_3_ >> 4) - chunkZ;
			if (l < 0 || l >= chunkArray.length || i1 < 0 || i1 >= chunkArray[l].length)
				return 0;
			if (chunkArray[l][i1] == null)
				return 0;
			return chunkArray[l][i1].getBlockMetadata(p_72805_1_ & 15, p_72805_2_, p_72805_3_ & 15);
		}
	}

	@Override
	public int isBlockProvidingPowerTo(int p_72879_1_, int p_72879_2_, int p_72879_3_, int p_72879_4_) {
		return getBlock(p_72879_1_, p_72879_2_, p_72879_3_).isProvidingStrongPower(this, p_72879_1_, p_72879_2_,
				p_72879_3_, p_72879_4_);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public BiomeGenBase getBiomeGenForCoords(int p_72807_1_, int p_72807_2_) {
		return worldObj.getBiomeGenForCoords(p_72807_1_, p_72807_2_);
	}

	@Override
	public boolean isAirBlock(int p_147437_1_, int p_147437_2_, int p_147437_3_) {
		return getBlock(p_147437_1_, p_147437_2_, p_147437_3_).isAir(this, p_147437_1_, p_147437_2_, p_147437_3_);
	}

	@SideOnly(Side.CLIENT)
	public int getSkyBlockTypeBrightness(EnumSkyBlock p_72810_1_, int p_72810_2_, int p_72810_3_, int p_72810_4_) {
		if (p_72810_3_ < 0) {
			p_72810_3_ = 0;
		}

		if (p_72810_3_ >= 256) {
			p_72810_3_ = 255;
		}

		if (p_72810_3_ >= 0 && p_72810_3_ < 256 && p_72810_2_ >= -30000000 && p_72810_4_ >= -30000000
				&& p_72810_2_ < 30000000 && p_72810_4_ <= 30000000) {
			if (p_72810_1_ == EnumSkyBlock.Sky && worldObj.provider.hasNoSky)
				return 0;
			else {
				int l;
				int i1;

				if (getBlock(p_72810_2_, p_72810_3_, p_72810_4_).getUseNeighborBrightness()) {
					l = getSpecialBlockBrightness(p_72810_1_, p_72810_2_, p_72810_3_ + 1, p_72810_4_);
					i1 = getSpecialBlockBrightness(p_72810_1_, p_72810_2_ + 1, p_72810_3_, p_72810_4_);
					int j1 = getSpecialBlockBrightness(p_72810_1_, p_72810_2_ - 1, p_72810_3_, p_72810_4_);
					int k1 = getSpecialBlockBrightness(p_72810_1_, p_72810_2_, p_72810_3_, p_72810_4_ + 1);
					int l1 = getSpecialBlockBrightness(p_72810_1_, p_72810_2_, p_72810_3_, p_72810_4_ - 1);

					if (i1 > l) {
						l = i1;
					}

					if (j1 > l) {
						l = j1;
					}

					if (k1 > l) {
						l = k1;
					}

					if (l1 > l) {
						l = l1;
					}

					return l;
				} else {
					l = (p_72810_2_ >> 4) - chunkX;
					i1 = (p_72810_4_ >> 4) - chunkZ;
					return chunkArray[l][i1].getSavedLightValue(p_72810_1_, p_72810_2_ & 15, p_72810_3_,
							p_72810_4_ & 15);
				}
			}
		} else
			return p_72810_1_.defaultLightValue;
	}

	@SideOnly(Side.CLIENT)
	public int getSpecialBlockBrightness(EnumSkyBlock p_72812_1_, int p_72812_2_, int p_72812_3_, int p_72812_4_) {
		if (p_72812_3_ < 0) {
			p_72812_3_ = 0;
		}

		if (p_72812_3_ >= 256) {
			p_72812_3_ = 255;
		}

		if (p_72812_3_ >= 0 && p_72812_3_ < 256 && p_72812_2_ >= -30000000 && p_72812_4_ >= -30000000
				&& p_72812_2_ < 30000000 && p_72812_4_ <= 30000000) {
			int l = (p_72812_2_ >> 4) - chunkX;
			int i1 = (p_72812_4_ >> 4) - chunkZ;
			return chunkArray[l][i1].getSavedLightValue(p_72812_1_, p_72812_2_ & 15, p_72812_3_, p_72812_4_ & 15);
		} else
			return p_72812_1_.defaultLightValue;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getHeight() {
		return 256;
	}

	@Override
	public boolean isSideSolid(int x, int y, int z, ForgeDirection side, boolean _default) {
		if (x < -30000000 || z < -30000000 || x >= 30000000 || z >= 30000000)
			return _default;

		return getBlock(x, y, z).isSideSolid(this, x, y, z, side);
	}
}
