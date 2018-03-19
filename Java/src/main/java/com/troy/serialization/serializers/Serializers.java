package com.troy.serialization.serializers;

import java.util.HashMap;
import java.util.Objects;

import com.troy.serialization.util.SerializationUtils;

public class Serializers {

	private final static HashMap<Class<?>, Serializer<?>> map = new HashMap<Class<?>, Serializer<?>>();

	/**
	 * Fetches and returns the serializer associated with the desired type, or creates a new serializer if there is currently 
	 * no mapping associated with requested type.
	 * @return The serializer associated with the given type
	 */
	public static <T> Serializer<T> getSerializer(Class<T> type) {
		Serializer<T> ser = (Serializer<T>) map.get(type);
		if (ser == null) {
			ser = createSerializer(type);
			map.put(type, ser);
		}
		return ser;
	}

	/**
	 * Registers the given type to be serialized using the desired serializer. Returns {@code true} if calling this method overrode a previous 
	 * type - serializer entry.
	 * @param type The type to associate with the given serializer
	 * @param serializer The serializer to associate with the given type
	 * @return True if calling this method overrode a previous type - serializer entry.
	 */
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
