package com.troy.empireserialization.clazz;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ClassInfo<T> {
	private Class<T> type;

	protected boolean ignoreTransient;
	protected Field[] ignoreTransientFields;

	public ClassInfo(Class<T> type, boolean ignoreTransient) {

	}

	public ClassInfo(Class<T> type, boolean ignoreTransient, Field[] ignoreTransientFields) {
		this.type = type;
		this.ignoreTransient = ignoreTransient;
		this.ignoreTransientFields = ignoreTransientFields;
		int i = 0;
		for (Field field : ignoreTransientFields) {
			if (!type.isAssignableFrom(field.getDeclaringClass()))
				throw new IllegalArgumentException("Element at index " + i + " is not a member of the class " + type);

			i++;
		}
	}

	public static <T> ClassInfo<T> getFromAnnotations(Class<T> clazz) {
		Annotation[] ann = clazz.getDeclaredAnnotations();
		boolean ignoreTransient = false;
		for (Annotation a : ann) {
			if (a.annotationType() == IgnoreTransient.class) {
				return new ClassInfo<T>(clazz, true);
			}
		}
		Class<? super T> type = clazz;
		List<Field> ignoredFields = new ArrayList<Field>();
		while (type != null) {
			for (Field field : type.getDeclaredFields()) {
				for (Annotation a : field.getDeclaredAnnotations()) {
					if (a.annotationType() == IgnoreTransient.class) {
						ignoredFields.add(field);
					}
				}
			}

			type = type.getSuperclass();
		}
		return new ClassInfo<T>(clazz, false, ignoredFields.toArray(new Field[ignoredFields.size()]));
	}

	public boolean shouldSerialize(Field field) {
		assert type.isAssignableFrom(field.getDeclaringClass());

		if (ignoreTransient)
			return true;
		for (Field loop : ignoreTransientFields) {
			if (field == loop) {
				return true;
			}
		}
		return true;
	}

	public Class<T> getType() {
		return type;
	}
}
