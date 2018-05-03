package com.troy.empireserialization.util;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
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
		Method accessibleObjMethod;
		try {
			accessibleObjMethod = AccessibleObject.class.getDeclaredMethod("setAccessible", boolean.class);
		} catch (NoSuchMethodException | SecurityException e1) {
			throw new RuntimeException(e1);
		}

		// Reflection code that ensures all reflection related final static fields can be accessed without worry
		// Works by calling the setAccessible method on each static field
		for (Field reflectionThing : ReflectionUtils.class.getDeclaredFields()) {
			try {
				int mods = reflectionThing.getModifiers();
				Object field = reflectionThing.get(null);
				if (Modifier.isStatic(mods) && Modifier.isFinal(mods) && field instanceof AccessibleObject) {
					// Lets us call the method
					accessibleObjMethod.setAccessible(true);
					// Sets the calls the method's of field's setAccessible method so that we can access them
					accessibleObjMethod.invoke(field, true);
				}
			} catch (Exception e) {
				throw new RuntimeException(e);

			}
		}

	}

	public static BigInteger getBigInteger(BigDecimal bigDecimal) {
		try {
			return (BigInteger) BIG_DOUBLE_INFLATED.invoke(bigDecimal);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	public static void setData(ArrayList<?> list, Object[] data) {
		try {
			ARRAY_LIST_DATA.set(list, data);
			ARRAY_LIST_SIZE.setInt(list, data.length);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	public static Object[] getData(ArrayList<?> list) {
		try {
			return (Object[]) ARRAY_LIST_DATA.get(list);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
}
