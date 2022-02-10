package net.minecraft.entity.ai.attributes;

public abstract class BaseAttribute implements IAttribute {
	private final String unlocalizedName;
	private final double defaultValue;
	private boolean shouldWatch;
	private static final String __OBFID = "CL_00001565";

	protected BaseAttribute(String p_i1607_1_, double p_i1607_2_) {
		unlocalizedName = p_i1607_1_;
		defaultValue = p_i1607_2_;

		if (p_i1607_1_ == null)
			throw new IllegalArgumentException("Name cannot be null!");
	}

	@Override
	public String getAttributeUnlocalizedName() {
		return unlocalizedName;
	}

	@Override
	public double getDefaultValue() {
		return defaultValue;
	}

	@Override
	public boolean getShouldWatch() {
		return shouldWatch;
	}

	public BaseAttribute setShouldWatch(boolean p_111112_1_) {
		shouldWatch = p_111112_1_;
		return this;
	}

	@Override
	public int hashCode() {
		return unlocalizedName.hashCode();
	}
}