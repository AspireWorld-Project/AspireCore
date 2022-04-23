package net.minecraft.tileentity;

public class TileEntityDropper extends TileEntityDispenser {
	@Override
	public String getInventoryName() {
		return hasCustomInventoryName() ? field_146020_a : "container.dropper";
	}
}