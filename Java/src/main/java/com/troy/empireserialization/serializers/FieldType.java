package com.troy.empireserialization.serializers;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import com.troy.empireserialization.EmpireSerializationSettings;
import com.troy.empireserialization.util.ClassHelper;

public enum FieldType {
	WILDCARD(0b00), PRIMITIVE(0b01), USER_DEFINED(0b10);
	public static final int MASK = 0b11;

	int code;

	FieldType(int code) {
		this.code = code;
	}

	public static FieldType identifyFieldType(Field field) {
		return identifyFieldType(field, EmpireSerializationSettings.defaultSettings);
	}

	public static int sizeof(Class<?> clazz) {
		if (clazz.isPrimitive()) {
			if (clazz == byte.class) {
				return 1;
			}
			if (clazz == short.class) {
				return 2;
			}
			if (clazz == int.class) {
				return 4;
			}
			if (clazz == long.class) {
				return 8;
			}
			if (clazz == float.class) {
				return 4;
			}
			if (clazz == double.class) {
				return 8;
			}
			if (clazz == char.class) {
				return 2;
			} else {
				throw new Error();
			}
		} else {
			return sun.misc.Unsafe.ADDRESS_SIZE;
		}
	}

	public static FieldType identifyFieldType(Field field, EmpireSerializationSettings settings) {
		Class<?> type = field.getDeclaringClass();
		if (type.isPrimitive())
			return FieldType.PRIMITIVE;
		if (Modifier.isFinal(type.getModifiers()))
			return FieldType.USER_DEFINED;
		if (type == Object.class)
			return FieldType.WILDCARD;
		if (settings.inferFinalClasses) {
			if (ClassHelper.hasSubClass(type))
				return FieldType.WILDCARD;
			else
				return FieldType.USER_DEFINED;
		}
		return FieldType.WILDCARD;
	}
}
