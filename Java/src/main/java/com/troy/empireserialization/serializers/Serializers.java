package com.troy.empireserialization.serializers;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import com.troy.empireserialization.EmpireOutput;
import com.troy.empireserialization.util.SerializationUtils;

public class Serializers {

	private final static HashMap<Class<?>, ObjectSerializer<?>> map = new HashMap<Class<?>, ObjectSerializer<?>>(100, 0.5f);

	static {
		PrimitiveSerializers.init();
		PrimitiveArraySerializers.init();
		addSerializer(ArraySerializer.SERIALIZER);
		addSerializer(ListSerializer.SERIALIZER);
	}

	/**
	 * Fetches and returns the serializer associated with the desired type, or
	 * creates a new serializer if there is currently no mapping associated with
	 * requested type.
	 * 
	 * @return The serializer associated with the given type
	 */
	public static <T> ObjectSerializer<T> getSerializer(Class<T> type, EmpireOutput out) {
		ObjectSerializer<T> ser = (ObjectSerializer<T>) map.get(type);
		if (ser == null) {
			if (type.isArray() && Object.class.isAssignableFrom(type.getComponentType())) {
				ser = (ObjectSerializer<T>) ArraySerializer.SERIALIZER;
			}
			if (List.class.isAssignableFrom(type)) {
				ser = (ObjectSerializer<T>) ListSerializer.SERIALIZER;
			} else
				ser = createSerializer(type, out);
			map.put(type, ser);
		}
		return ser;
	}

	/**
	 * Registers the given type to be serialized using the desired serializer.
	 * Returns {@code true} if calling this method overrode a previous type -
	 * serializer entry.
	 * 
	 * @param type
	 *            The type to associate with the given serializer
	 * @param serializer
	 *            The serializer to associate with the given type
	 * @return True if calling this method overrode a previous type - serializer
	 *         entry.
	 */
	public static <T> boolean setSerializer(Class<T> type, ObjectSerializer<T> serializer) {
		return map.put(Objects.requireNonNull(type), Objects.requireNonNull(serializer)) != null;
	}

	protected static void addSerializer(ObjectSerializer<?> serializer) {
		map.put(serializer.getType(), serializer);
	}

	private static <T> ObjectSerializer<T> createSerializer(Class<T> type, EmpireOutput out) {
		if (!SerializationUtils.DYNAMIC_CLASS_LOADING_ENABLED) {// Use the default one if we can't create a dynamic one
			return new DefaultObjectSerializer<T>(type, out);
		} // Create a dynamic one

		return null;// TODO create dynamic serializer creation system
	}

	public static void init() {

	}

}
