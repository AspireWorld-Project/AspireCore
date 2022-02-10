package net.minecraft.server.management;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class LowerStringMap implements Map {
	private final Map internalMap = new LinkedHashMap();
	private static final String __OBFID = "CL_00001488";

	@Override
	public int size() {
		return internalMap.size();
	}

	@Override
	public boolean isEmpty() {
		return internalMap.isEmpty();
	}

	@Override
	public boolean containsKey(Object p_containsKey_1_) {
		return internalMap.containsKey(p_containsKey_1_.toString().toLowerCase());
	}

	@Override
	public boolean containsValue(Object p_containsValue_1_) {
		return internalMap.containsKey(p_containsValue_1_);
	}

	@Override
	public Object get(Object p_get_1_) {
		return internalMap.get(p_get_1_.toString().toLowerCase());
	}

	public Object put(String p_put_1_, Object p_put_2_) {
		return internalMap.put(p_put_1_.toLowerCase(), p_put_2_);
	}

	@Override
	public Object remove(Object p_remove_1_) {
		return internalMap.remove(p_remove_1_.toString().toLowerCase());
	}

	@Override
	public void putAll(Map p_putAll_1_) {
		Iterator iterator = p_putAll_1_.entrySet().iterator();

		while (iterator.hasNext()) {
			Entry entry = (Entry) iterator.next();
			this.put((String) entry.getKey(), entry.getValue());
		}
	}

	@Override
	public void clear() {
		internalMap.clear();
	}

	@Override
	public Set keySet() {
		return internalMap.keySet();
	}

	@Override
	public Collection values() {
		return internalMap.values();
	}

	@Override
	public Set entrySet() {
		return internalMap.entrySet();
	}

	@Override
	public Object put(Object p_put_1_, Object p_put_2_) {
		return this.put((String) p_put_1_, p_put_2_);
	}
}