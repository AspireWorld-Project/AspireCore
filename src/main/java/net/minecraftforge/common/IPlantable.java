package net.minecraftforge.common;

import net.minecraft.block.Block;
import net.minecraft.world.IBlockAccess;

public interface IPlantable {
	EnumPlantType getPlantType(IBlockAccess world, int x, int y, int z);

	Block getPlant(IBlockAccess world, int x, int y, int z);

	int getPlantMetadata(IBlockAccess world, int x, int y, int z);
}