package net.minecraft.tileentity;

import net.minecraft.block.BlockDaylightDetector;

public class TileEntityDaylightDetector extends TileEntity {
	@Override
	public void updateEntity() {
		if (worldObj != null && !worldObj.isRemote && worldObj.getTotalWorldTime() % 20L == 0L) {
			blockType = getBlockType();

			if (blockType instanceof BlockDaylightDetector) {
				((BlockDaylightDetector) blockType).func_149957_e(worldObj, xCoord, yCoord, zCoord);
			}
		}
	}
}