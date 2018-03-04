package com.troy.serialization.serializers;

import java.util.HashMap;

import com.troy.serialization.util.SerializationUtils;

public class Serializers {

	private static HashMap<Class<?>, Serializer<?>> map = new HashMap<>();

	public static <T> Serializer<T> getSerializer(Class<T> type) {
		if (map.containsKey(type)) {

		} else {
			Serializer<T> result;
			if (SerializationUtils.DYNAMIC_CLASS_LOADING_ENABLED) {
				result = createDynamicSerializer(type);
			} else {
				result = null;
			}
			map.put(type, result);
		}
		return null;
	}
	
	private static <T> Serializer<T> createDynamicSerializer(Class<T> type) {
		//Do stuff with ASM
		return null;
		
	}

}
