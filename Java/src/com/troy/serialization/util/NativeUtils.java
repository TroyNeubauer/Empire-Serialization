package com.troy.serialization.util;

import java.nio.*;

import com.troyberry.util.MiscUtil;

public class NativeUtils {

	static {
		System.load("C:\\C++\\Current Projects\\bin\\x64\\Release\\Serialization Natives.dll");
	}

	public static final boolean NATIVES_ENABLED = true;

	// Small structure used for returning error information from native code
	/*
	 * Composed like so: struct info{ jbyte code; jchar badChar; jint index; };
	 */
	private static final ThreadLocal<ByteBuffer> NATIVE_RETURNS = new ThreadLocal<ByteBuffer>() {
		protected ByteBuffer initialValue() {
			return ByteBuffer.allocateDirect(8).order(ByteOrder.nativeOrder());
		};
	};
	private static final int ERROR_CODE_OFFSET = 0;// The error code is one byte
	private static final int INVALID_CHARACTER_OFFSET = 1;// Invalid character is 2 bytes
	private static final int INVALID_CHARACTER_INDEX_OFFSET = 3;// Represents what index the invalid character was at 4 byte int

	public static byte getErrorCode() {
		return NATIVE_RETURNS.get().get(ERROR_CODE_OFFSET);
	}

	public static char getInvalidChar() {
		return NATIVE_RETURNS.get().getChar(INVALID_CHARACTER_OFFSET);
	}

	public static int getInvalidCharIndex() {
		return NATIVE_RETURNS.get().getInt(INVALID_CHARACTER_INDEX_OFFSET);
	}

	public static long getAddress() {
		return MiscUtil.address(NATIVE_RETURNS.get());
	}

	public static void clearError() {
		NATIVE_RETURNS.get().put(ERROR_CODE_OFFSET, (byte) 0);
		NATIVE_RETURNS.get().putChar(INVALID_CHARACTER_OFFSET, (char) 0);
		NATIVE_RETURNS.get().putInt(INVALID_CHARACTER_INDEX_OFFSET, -1);
	}

	public static void init() {
		NATIVE_RETURNS.get();// Create copy for this thread
	}

}
