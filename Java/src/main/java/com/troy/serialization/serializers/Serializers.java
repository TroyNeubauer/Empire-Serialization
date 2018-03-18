package com.troy.serialization.serializers;

import java.util.HashMap;

import com.troy.serialization.util.SerializationUtils;

public class Serializers {

	private final static HashMap<Class<?>, Serializer<?>> map = new HashMap<Class<?>, Serializer<?>>();
	private static Serializer<Object> defaultSerializer = getDefaultSerializer();
	
	public static <T> Serializer<T> getSerializer(Class<T> type) {
		if (map.containsKey(type)) {

		} else {
			Serializer<T> result;
			if (SerializationUtils.DYNAMIC_CLASS_LOADING_ENABLED) {
				result = createSerializer(type);
			} else {
				result = null;
			}
			map.put(type, result);
		}
		return null;
	}

	private static <T> Serializer<T> createSerializer(Class<T> type) {
		if(!SerializationUtils.DYNAMIC_CLASS_LOADING_ENABLED) {//Use the default one if we can't create a dynamic one
			return (Serializer<T>) getDefaultSerializer();
		}//Create a dynamic one
		
		return null;//TODO create dynamic serializer creation system
	}
	
	public static Serializer<Object> getDefaultSerializer() {
		return Serializers.defaultSerializer;
	}
	
	public static void setDefaultSerializer(Serializer<Object> defaultSerializer) {
		Serializers.defaultSerializer = defaultSerializer;
	}
	

}
