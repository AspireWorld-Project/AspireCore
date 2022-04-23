package net.minecraft.block.material;

public class MaterialLogic extends Material {
	public MaterialLogic(MapColor p_i2112_1_) {
		super(p_i2112_1_);
		setAdventureModeExempt();
	}

	@Override
	public boolean isSolid() {
		return false;
	}

	@Override
	public boolean getCanBlockGrass() {
		return false;
	}

	@Override
	public boolean blocksMovement() {
		return false;
	}
}