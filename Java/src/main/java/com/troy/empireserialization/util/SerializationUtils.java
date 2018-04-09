package com.troy.empireserialization.util;

import com.troy.empireserialization.charset.*;
import com.troy.empireserialization.serializers.*;

public class SerializationUtils {

	public static final boolean DEBUG = true;
	public static final boolean CHECK_CHARSET_PROBLEMS = DEBUG;
	public static final int ERROR = -1;
	public static final int OUT_OF_MEMORY = -1, UNSUPPORTED_CHARACTER = -2, INVALID_ARGUMENT = -10;

	public static final boolean DYNAMIC_CLASS_LOADING_ENABLED = false;

	/**
	 * Constructs an array that maps the values of decodingCache to the indices of decodingCache. Used for creating an array that maps custom values
	 * that represent java chars mapped to custom character code values.
	 * 
	 * @param decodingCache The decodingCache to use to create an encoding cache
	 * @return The encoding cache
	 */
	public static int[] constructEncodingFromDecoding(char[] decodingCache) {
		int size = -1;
		for (char c : decodingCache) {
			if (c > size)
				size = c;
		}
		int[] result = new int[size + 1];
		for (int i = 0; i < result.length; i++) {
			result[i] = -1;
		}
		for (int index = 0; index < decodingCache.length; index++) {
			char decodingValue = decodingCache[index];
			result[decodingValue] = index;
		}
		return result;
	}

	public static void init() {
		NativeUtils.init();
		EmpireCharsets.init();
		Serializers.init();
		MiscUtil.init();
	}

}
