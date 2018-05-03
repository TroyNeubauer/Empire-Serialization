package com.troy.empireserialization.util;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Set;

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

		ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
		provider.addIncludeFilter(new AssignableTypeFilter(type));

		String name = type.getName();
		String binaryPackage = name.substring(0, name.lastIndexOf('.')).replaceAll(".", "/");
		Set<BeanDefinition> components = provider.findCandidateComponents(binaryPackage);
		boolean hasSubclass = false;
		for (BeanDefinition component : components) {
			try {
				Class<?> cls = Class.forName(component.getBeanClassName());
				hasSubclass = true;
				break;
			} catch (Exception e) {

			}
		}

		CACHE.put(type, Boolean.valueOf(hasSubclass));
		return hasSubclass;
	}
}
