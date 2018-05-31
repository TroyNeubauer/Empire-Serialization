package com.troy.empireserialization.util;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import com.troy.empireserialization.clazz.ClassData;

import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;
import io.github.lukehutch.fastclasspathscanner.scanner.ScanResult;

public class ClassHelper {

	private static final HashMap<Class<?>, Boolean> HAS_SUBCLASS_CACHE = new HashMap<Class<?>, Boolean>();
	private static final HashMap<Class<?>, ClassData<?>> CLASS_DATA = new HashMap<Class<?>, ClassData<?>>();
	private static final Object DATA_LOCK = new Object();

	public static <T> ClassData<T> getClassData(Class<T> type) {
		ClassData<T> data = (ClassData<T>) CLASS_DATA.get(type);
		if (data == null) {
			synchronized (DATA_LOCK) {
				data = new ClassData<T>(type);
				CLASS_DATA.put(type, data);
			}
		}
		return data;
	}

	/**
	 * Manually assigns weather or not a class has a subclass
	 * 
	 * @param type
	 *            The class to set the property for
	 * @param hasSubclass
	 *            Weather or not the specified class has subclass
	 */
	public static void setHasSubclass(Class<?> type, boolean hasSubclass) {
		if (Modifier.isFinal(type.getModifiers()))
			throw new IllegalArgumentException("The " + type + " is final, therefore it cannot have a subclass");
		HAS_SUBCLASS_CACHE.put(type, Boolean.valueOf(hasSubclass));
	}

	/**
	 * Returns {@code true} if the specified class has a subclass {@code false}
	 * otherwise.
	 * <p>
	 * This method conducts a classpath search if a cached result for weather or not
	 * this class has a subclass doesnt exist. This can be extremely expensive. This
	 * can be avoided if the user already knows weather or not a class has a
	 * subclass using {@link ClassHelper#setHasSubclass(Class, boolean)}
	 * 
	 * @param type
	 *            The class to search for
	 * @return
	 */
	public static <T> boolean hasSubClass(Class<T> type) {
		Boolean cached = HAS_SUBCLASS_CACHE.get(type);
		if (cached != null)
			return cached.booleanValue();
		boolean hasSubclass = false;
		hasSubclass = parseList(type, findAllSubclassesFast(type));// Search the current directory
		if (!hasSubclass) // Scan the entire classpath if it wasn't found in the current directory
			hasSubclass = parseList(type, findAllSubclassesSlow(type));

		HAS_SUBCLASS_CACHE.put(type, Boolean.valueOf(hasSubclass));
		return hasSubclass;
	}

	private static boolean parseList(Class<?> type, List<String> list) {
		for (String element : list) {
			try {
				Class<?> temp = Class.forName(element);
				if (temp != type && type.isAssignableFrom(temp)) {
					return true;
				}
			} catch (Exception e) {

			}
		}
		return false;

	}

	public static boolean isDataStructure(Class<?> type) {
		// List because is is basically a fancy array.
		return type.isArray() || List.class.isAssignableFrom(type) || Map.class.isAssignableFrom(type);
	}

	public static boolean isPrimitive(Class<?> type) {
		return type.isPrimitive() || type == String.class || type.isArray() || type == Integer.class || type == Long.class || type == Float.class
				|| type == Double.class || type == Short.class || type == Byte.class || type == Character.class || type == Boolean.class
				|| List.class.isAssignableFrom(type) || Map.class.isAssignableFrom(type);
	}

	private static final Object fastLock = new Object(), slowLock = new Object();
	private static ScanResult fast, slow;
	private static CurrentThreadExecutorService service = new CurrentThreadExecutorService();

	public static List<String> findAllSubclassesFast(Class<?> clazz) {
		if (fast == null) {
			synchronized (fastLock) {
				fast = new FastClasspathScanner(clazz.getPackage().getName()).scan(service, 1);
			}
		}
		return fast.getNamesOfSubclassesOf(clazz.getName());
	}

	public static List<String> findAllSubclassesSlow(Class<?> clazz) {
		if (slow == null) {
			synchronized (slowLock) {
				slow = new FastClasspathScanner().scan();// Scan the entire classpath
			}
		}
		return slow.getNamesOfSubclassesOf(clazz.getName());
	}
}
