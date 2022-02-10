package net.minecraft.tileentity;

public class TileEntityDropper extends TileEntityDispenser {
	private static final String __OBFID = "CL_00000353";

	@Override
	public String getInventoryName() {
		return hasCustomInventoryName() ? field_146020_a : "container.dropper";
	}
}