package net.minecraft.block.material;

public class MaterialLiquid extends Material {
	private static final String __OBFID = "CL_00000541";

	public MaterialLiquid(MapColor p_i2114_1_) {
		super(p_i2114_1_);
		setReplaceable();
		setNoPushMobility();
	}

	@Override
	public boolean isLiquid() {
		return true;
	}

	@Override
	public boolean blocksMovement() {
		return false;
	}

	@Override
	public boolean isSolid() {
		return false;
	}
}