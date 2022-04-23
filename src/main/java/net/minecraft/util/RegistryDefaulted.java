package net.minecraft.util;

public class RegistryDefaulted extends RegistrySimple {
	private final Object defaultObject;
	public RegistryDefaulted(Object p_i1366_1_) {
		defaultObject = p_i1366_1_;
	}

	@Override
	public Object getObject(Object p_82594_1_) {
		Object object1 = super.getObject(p_82594_1_);
		return object1 == null ? defaultObject : object1;
	}
}