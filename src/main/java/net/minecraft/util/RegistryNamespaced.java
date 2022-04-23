package net.minecraft.util;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import java.util.Iterator;
import java.util.Map;

public class RegistryNamespaced extends RegistrySimple implements IObjectIntIterable {
	protected ObjectIntIdentityMap underlyingIntegerMap = new ObjectIntIdentityMap();
	@SuppressWarnings("rawtypes")
	protected final Map field_148758_b;
	@SuppressWarnings("rawtypes")
	public RegistryNamespaced() {
		field_148758_b = ((BiMap) registryObjects).inverse();
	}

	public void addObject(int p_148756_1_, String p_148756_2_, Object p_148756_3_) {
		underlyingIntegerMap.func_148746_a(p_148756_3_, p_148756_1_);
		putObject(ensureNamespaced(p_148756_2_), p_148756_3_);
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected Map createUnderlyingMap() {
		return HashBiMap.create();
	}

	public Object getObject(String p_82594_1_) {
		return super.getObject(ensureNamespaced(p_82594_1_));
	}

	public String getNameForObject(Object p_148750_1_) {
		return (String) field_148758_b.get(p_148750_1_);
	}

	public boolean containsKey(String p_148741_1_) {
		return super.containsKey(ensureNamespaced(p_148741_1_));
	}

	public int getIDForObject(Object p_148757_1_) {
		return underlyingIntegerMap.func_148747_b(p_148757_1_);
	}

	public Object getObjectById(int p_148754_1_) {
		return underlyingIntegerMap.func_148745_a(p_148754_1_);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Iterator iterator() {
		return underlyingIntegerMap.iterator();
	}

	public boolean containsId(int p_148753_1_) {
		return underlyingIntegerMap.func_148744_b(p_148753_1_);
	}

	protected static String ensureNamespaced(String p_148755_0_) {
		return p_148755_0_.indexOf(58) == -1 ? "minecraft:" + p_148755_0_ : p_148755_0_;
	}

	@Override
	public boolean containsKey(Object p_148741_1_) {
		return this.containsKey((String) p_148741_1_);
	}

	@Override
	public Object getObject(Object p_82594_1_) {
		return this.getObject((String) p_82594_1_);
	}
}