package com.troy.empireserialization.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;

public class ReflectionUtils {
	private static final Method BIG_DOUBLE_INFLATED;
	private static final Field ARRAY_LIST_DATA, ARRAY_LIST_SIZE;

	static {
		Method inflated = null;
		Field data = null, size = null;
		try {
			inflated = BigDecimal.class.getDeclaredMethod("inflated");
		} catch (NoSuchMethodException | SecurityException e) {
			throw new Error();
		}
		for (Field field : ArrayList.class.getDeclaredFields()) {
			if (field.getType().isArray() && field.getName().equals("elementData")) {
				data = field;
				System.out.println("Array List data: " + data);
			}
			if (field.getName().equals("size") && field.getType().isPrimitive()) {
				size = field;
				System.out.println("ALsize " + size);

			}
		}

		BIG_DOUBLE_INFLATED = inflated;
		ARRAY_LIST_DATA = data;
		ARRAY_LIST_SIZE = size;

	}

	public static BigInteger getBigInteger(BigDecimal bigDecimal) {
		try {
			return (BigInteger) BIG_DOUBLE_INFLATED.invoke(bigDecimal);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	public static void setData(ArrayList list, Object[] data) {
		try {
			ARRAY_LIST_DATA.set(list, data);
			ARRAY_LIST_SIZE.setInt(list, data.length);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
