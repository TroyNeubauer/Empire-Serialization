package com.troy.empireserialization.util;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;

public class ClassHelper {

	private static final HashMap<Class<?>, Boolean> CACHE = new HashMap<Class<?>, Boolean>();

	/**
	 * Manually assigns weather or not a class has a subclass
	 * 
	 * @param type
	 * @param hasSubclass
	 */
	public static void setHasSubclass(Class<?> type, boolean hasSubclass) {
		if (Modifier.isFinal(type.getModifiers()))
			throw new IllegalArgumentException("The " + type + " is final, therefore it cannot have a subclass");
		CACHE.put(type, Boolean.valueOf(hasSubclass));
	}

	public static <T> boolean hasSubClass(Class<T> type) {
		if (Modifier.isFinal(type.getModifiers()))
			return false;
		Boolean cached = CACHE.get(type);
		if (cached != null)
			return cached.booleanValue();
		boolean hasSubclass = false;
		for (Class<?> temp : findAllSubclasses(type)) {
			if (temp != type && type.isAssignableFrom(temp)) {
				hasSubclass = true;
				break;
			}
		}

		CACHE.put(type, Boolean.valueOf(hasSubclass));
		return hasSubclass;
	}

	public static boolean isPrimitive(Class<?> type) {
		return type.isPrimitive() || type == String.class || type == Integer.class || type == Long.class
				|| type == Float.class || type == Double.class || type == Short.class || type == Byte.class
				|| type == Character.class || type == Boolean.class || List.class.isAssignableFrom(type)
				|| Stack.class.isAssignableFrom(type) || Queue.class.isAssignableFrom(type)
				|| Set.class.isAssignableFrom(type) || Map.class.isAssignableFrom(type);
	}

	public static List<Class<?>> findAllSubclasses(Class<?> parent) {
		if (true)
			throw new RuntimeException();
		List<Class<?>> result = new ArrayList<Class<?>>();
		ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
		provider.addIncludeFilter(new AssignableTypeFilter(parent));

		String name = parent.getName();
		// String binaryPackage = name.substring(0, name.lastIndexOf('.')).replaceAll("\\.", "/");
		Set<BeanDefinition> components = provider.findCandidateComponents(/* binaryPackage */"");
		boolean hasSubclass = false;
		for (BeanDefinition component : components) {
			try {
				Class<?> cls = Class.forName(component.getBeanClassName());
				if (parent != cls) {
					result.add(parent);
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return result;
	}
}
