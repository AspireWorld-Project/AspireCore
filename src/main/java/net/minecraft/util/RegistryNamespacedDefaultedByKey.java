package net.minecraft.util;

public class RegistryNamespacedDefaultedByKey extends RegistryNamespaced {
	private final String field_148760_d;
	private Object field_148761_e;
	private static final String __OBFID = "CL_00001196";

	public RegistryNamespacedDefaultedByKey(String p_i45127_1_) {
		field_148760_d = p_i45127_1_;
	}

	@Override
	public void addObject(int p_148756_1_, String p_148756_2_, Object p_148756_3_) {
		if (field_148760_d.equals(p_148756_2_)) {
			field_148761_e = p_148756_3_;
		}

		super.addObject(p_148756_1_, p_148756_2_, p_148756_3_);
	}

	@Override
	public Object getObject(String p_82594_1_) {
		Object object = super.getObject(p_82594_1_);
		return object == null ? field_148761_e : object;
	}

	@Override
	public Object getObjectById(int p_148754_1_) {
		Object object = super.getObjectById(p_148754_1_);
		return object == null ? field_148761_e : object;
	}

	@Override
	public Object getObject(Object p_82594_1_) {
		return this.getObject((String) p_82594_1_);
	}
}