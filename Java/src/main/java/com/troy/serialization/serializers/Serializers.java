package com.troy.serialization.serializers;

import java.util.HashMap;
import java.util.Objects;

import com.troy.serialization.util.SerializationUtils;

public class Serializers {

	private final static HashMap<Class<?>, Serializer<?>> map = new HashMap<Class<?>, Serializer<?>>();

	public static <T> Serializer<T> getSerializer(Class<T> type) {
		Serializer<T> ser = (Serializer<T>) map.get(type);
		if (ser == null) {
			ser = createSerializer(type);
			map.put(type, ser);
		}
		return ser;
	}

	public static <T> boolean setSerializer(Class<T> type, Serializer<T> serializer) {
		return map.put(Objects.requireNonNull(type), Objects.requireNonNull(serializer)) != null;
	}

	private static <T> Serializer<T> createSerializer(Class<T> type) {
		if (!SerializationUtils.DYNAMIC_CLASS_LOADING_ENABLED) {// Use the default one if we can't create a dynamic one
			return new FieldSerializer<T>(type);
		} // Create a dynamic one

		return null;// TODO create dynamic serializer creation system
	}

}
