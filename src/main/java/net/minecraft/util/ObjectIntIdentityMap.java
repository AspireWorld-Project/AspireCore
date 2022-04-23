package net.minecraft.util;

import com.google.common.base.Predicates;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;

import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;

public class ObjectIntIdentityMap implements IObjectIntIterable {
	protected IdentityHashMap field_148749_a = new IdentityHashMap(512);
	protected List field_148748_b = Lists.newArrayList();
	private static final String __OBFID = "CL_00001203";

	public void func_148746_a(Object p_148746_1_, int p_148746_2_) {
		field_148749_a.put(p_148746_1_, Integer.valueOf(p_148746_2_));

		while (field_148748_b.size() <= p_148746_2_) {
			field_148748_b.add((Object) null);
		}

		field_148748_b.set(p_148746_2_, p_148746_1_);
	}

	public int func_148747_b(Object p_148747_1_) {
		Integer integer = (Integer) field_148749_a.get(p_148747_1_);
		return integer == null ? -1 : integer.intValue();
	}

	public Object func_148745_a(int p_148745_1_) {
		return p_148745_1_ >= 0 && p_148745_1_ < field_148748_b.size() ? field_148748_b.get(p_148745_1_) : null;
	}

	@Override
	public Iterator iterator() {
		return Iterators.filter(field_148748_b.iterator(), Predicates.notNull());
	}

	public boolean func_148744_b(int p_148744_1_) {
		return func_148745_a(p_148744_1_) != null;
	}
}