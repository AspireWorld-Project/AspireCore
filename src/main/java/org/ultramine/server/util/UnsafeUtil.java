package org.ultramine.server.util;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

public class UnsafeUtil {
	private static final Unsafe UNSAFE = createUnsafe();

	public static Unsafe getUnsafe() {
		return UNSAFE;
	}

	private static Unsafe createUnsafe() {
		try {
			Field uf = Unsafe.class.getDeclaredField("theUnsafe");
			uf.setAccessible(true);
			return (Unsafe) uf.get(null);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
