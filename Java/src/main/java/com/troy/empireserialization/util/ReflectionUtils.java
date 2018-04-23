package com.troy.empireserialization.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;

public class ReflectionUtils {
	private static final Method BIG_DOUBLE_INFLATED;

	static {
		Method inflated = null;
		try {
			inflated = BigDecimal.class.getDeclaredMethod("inflated");
		} catch (NoSuchMethodException | SecurityException e) {
			throw new Error();
		}

		BIG_DOUBLE_INFLATED = inflated;

	}

	public static BigInteger getBigInteger(BigDecimal bigDecimal) {
		try {
			return (BigInteger) BIG_DOUBLE_INFLATED.invoke(bigDecimal);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}
}
