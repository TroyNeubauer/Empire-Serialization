package com.troy.empireserialization.util;

import java.lang.reflect.Modifier;
import java.util.HashMap;

import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;
import io.github.lukehutch.fastclasspathscanner.matchprocessor.ClassMatchProcessor;

public class ClassHelper {
	private static FastClasspathScanner scanner = new FastClasspathScanner();
	private static HashMap<Class<?>, Boolean> cache = new HashMap<Class<?>, Boolean>();

	static {
		scanner.matchAllClasses(new ClassMatchProcessor() {

			@Override
			public void processMatch(Class<?> classRef) {

			}
		});
	}

	public static <T> boolean hasSubClass(Class<T> type) {
		if (Modifier.isFinal(type.getModifiers()))
			return false;
		Boolean cached = cache.get(type);
		if (cached != null)
			return cached.booleanValue();

		boolean hasSubclass = scanner.scan().getNamesOfSubclassesOf(type).size() > 0;
		cache.put(type, Boolean.valueOf(hasSubclass));
		return hasSubclass;
	}
}
