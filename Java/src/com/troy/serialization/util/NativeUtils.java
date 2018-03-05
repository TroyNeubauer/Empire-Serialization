package com.troy.serialization.util;

import java.lang.reflect.*;
import java.nio.*;

import com.troy.serialization.nativelibrary.LibraryProvider;

public class NativeUtils {

	private static final int ERROR_CODE_OFFSET = 0;// The error code is one byte
	private static final int INVALID_CHARACTER_OFFSET = 1;// Invalid character is 2 bytes
	private static final int INVALID_CHARACTER_INDEX_OFFSET = 3;// Represents what index the invalid character was at 4 byte int
	public static final boolean IS_64_BIT = System.getProperty("sun.arch.data.model").equals("64");
	
	
	// Small structure used for returning error information from native code
	/*
	 * Composed like so: struct info{ jbyte code; jchar badChar; jint index; };
	 */
	private static final ThreadLocal<ByteBuffer> NATIVE_RETURNS = new ThreadLocal<ByteBuffer>() {
		protected ByteBuffer initialValue() {
			return ByteBuffer.allocateDirect(8).order(ByteOrder.nativeOrder());
		};
	};

	static {
		LibraryProvider.loadLibrary();
		NATIVE_RETURNS.get();
		long start = System.nanoTime();
		int count = 0;
		for (Method method : NativeUtils.class.getDeclaredMethods()) {
			int mods = method.getModifiers();
			if (Modifier.isStatic(mods) && Modifier.isNative(mods)) {
				MiscUtil.callMethod(method, null);
				count++;
			}
		}
		long end = System.nanoTime();
		System.out.println("Called " + count + " methods in " + (end - start) / 1000000.0 + " milliseconds");
	}

	public static final boolean NATIVES_ENABLED = true;

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
	}

	public static native void nothing();

	// Copies data from a primitive array to a byte array. The byte array must be at least bytes long
	public static native int shortsToBytes(byte[] dest, short[] src, int srcOffset, int destOffset, int elements, boolean swapEndianess);

	public static native int intsToBytes(byte[] dest, int[] src, int srcOffset, int destOffset, int elements, boolean swapEndianess);

	public static native int longsToBytes(byte[] dest, long[] src, int srcOffset, int destOffset, int elements, boolean swapEndianess);

	public static native int floatsToBytes(byte[] dest, float[] src, int srcOffset, int destOffset, int elements, boolean swapEndianess);

	public static native int doublesToBytes(byte[] dest, double[] src, int srcOffset, int destOffset, int elements, boolean swapEndianess);

	public static native int charsToBytes(byte[] dest, char[] src, int srcOffset, int destOffset, int elements, boolean swapEndianess);

	public static native int booleansToBytes(byte[] dest, boolean[] src, int srcOffset, int destOffset, int elements, boolean swapEndianess);

	public static native int booleansToBytesCompact(byte[] dest, boolean[] src, int srcOffset, int destOffset, int elements);

	// From array to native memory
	public static native int bytesToNative(long dest, byte[] src, int offset, int elements);

	public static native int shortsToNative(long dest, short[] src, int offset, int elements);

	public static native int intsToNative(long dest, int[] src, int offset, int elements);

	public static native int longsToNative(long dest, long[] src, int offset, int elements);

	public static native int floatsToNative(long dest, float[] src, int offset, int elements);

	public static native int doublesToNative(long dest, double[] src, int offset, int elements);

	public static native int charsToNative(long dest, char[] src, int offset, int elements);

	// From native memory to array
	public static native int nativeToBytes(byte[] dest, long src, int offset, int bytes);

	public static native int nativeToShorts(short[] dest, long src, int offset, int bytes);

	public static native int nativeToInts(int[] dest, long src, int offset, int bytes);

	public static native int nativeToLongs(long[] dest, long src, int offset, int bytes);

	public static native int nativeToFloats(float[] dest, long src, int offset, int bytes);

	public static native int nativeToDoubles(double[] dest, long src, int offset, int bytes);

	public static native int nativeToChars(char[] dest, long src, int offset, int bytes);

	/**
	 * Copies n bytes from the source address to the destination address<br>
	 * Both {@code dest} and {@code src} pointers are <b>NOT</b> checked for validity! If they are null or point to memory that cannot be read, the
	 * Java Virtual Machine will terminate with a memory access violation!!!
	 * 
	 * @param dest A pointer to copy n bytes to
	 * @param src A pointer to copy n bytes from
	 * @param bytes The number of bytes to copy
	 */
	public static native void memcpy(long dest, long src, long bytes);
}
