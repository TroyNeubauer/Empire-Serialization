package com.troy.empireserialization.util;

import java.lang.reflect.*;
import java.nio.*;

import com.troy.empireserialization.nativelibrary.*;

public class NativeUtils {

	private static final int ERROR_CODE_OFFSET = 0;// The error code is one byte
	private static final int INVALID_CHARACTER_OFFSET = 1;// Invalid character is 2 bytes
	private static final int INVALID_CHARACTER_INDEX_OFFSET = 3;// Represents what index the invalid character was at 4
																// bytes

	public static final int DEFAULT_NATIVE_SIZE = 4096;

	public static final boolean IS_64_BIT = System.getProperty("sun.arch.data.model").equals("64");

	// Small structure used for returning string coding errors from native code
	/*
	 * Composed like so: struct info{ jbyte code; jchar badChar; jint index; };
	 */
	private static final ThreadLocal<ByteBuffer> NATIVE_RETURNS = new ThreadLocal<ByteBuffer>() {
		protected ByteBuffer initialValue() {
			return ByteBuffer.allocateDirect(8).order(ByteOrder.nativeOrder());
		};
	};

	static {
		MiscUtil.init();
		LibraryProvider.loadLibrary();
		NATIVE_RETURNS.get();
		InternalLog.log("Beginning to link native methods in Native Utils by calling them");
		long start = System.nanoTime();
		int count = 0;
		// Call all the static native methods in this class to link them to the native
		// code so calls will be faster later
		for (Method method : NativeUtils.class.getDeclaredMethods()) {
			int mods = method.getModifiers();
			if (Modifier.isStatic(mods) && Modifier.isNative(mods) && Modifier.isPublic(mods)) {
				MiscUtil.callMethod(method, null);
				count++;
			}
		}
		long end = System.nanoTime();
		InternalLog.log("Called " + count + " methods in " + (end - start) / 1000000.0 + " milliseconds");
	}

	public static boolean NATIVES_ENABLED = false;

	// Always use native
	public static final int MIN_NATIVE_THRESHOLD = 0;

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
	// Copies data from a primitive array to a byte array. The byte array must be at
	// least bytes long
	public static native int shortsToBytes(byte[] dest, short[] src, int srcOffset, int destOffset, int elements,
			boolean swapEndianess);

	public static native int intsToBytes(byte[] dest, int[] src, int srcOffset, int destOffset, int elements,
			boolean swapEndianess);

	public static native int longsToBytes(byte[] dest, long[] src, int srcOffset, int destOffset, int elements,
			boolean swapEndianess);

	public static native int floatsToBytes(byte[] dest, float[] src, int srcOffset, int destOffset, int elements,
			boolean swapEndianess);

	public static native int doublesToBytes(byte[] dest, double[] src, int srcOffset, int destOffset, int elements,
			boolean swapEndianess);

	public static native int charsToBytes(byte[] dest, char[] src, int srcOffset, int destOffset, int elements,
			boolean swapEndianess);

	public static native int booleansToBytes(byte[] dest, boolean[] src, int srcOffset, int destOffset, int elements,
			boolean swapEndianess);

	public static native int booleansToBytesCompact(byte[] dest, boolean[] src, int srcOffset, int destOffset,
			int elements);

	// From array to native memory
	public static int bytesToNative(long dest, byte[] src, int offset, int elements) {
		return bytesToNative(dest, src, offset, elements, false);
	}

	private static native int bytesToNative(long dest, byte[] src, int offset, int elements, boolean swapEndianess);

	public static native int shortsToNative(long dest, short[] src, int offset, int elements, boolean swapEndianess);

	public static native int intsToNative(long dest, int[] src, int offset, int elements, boolean swapEndianess);

	public static native int longsToNative(long dest, long[] src, int offset, int elements, boolean swapEndianess);

	public static native int floatsToNative(long dest, float[] src, int offset, int elements, boolean swapEndianess);

	public static native int doublesToNative(long dest, double[] src, int offset, int elements, boolean swapEndianess);

	public static native int charsToNative(long dest, char[] src, int offset, int elements, boolean swapEndianess);

	public static int booleansToNative(long dest, boolean[] src, int offset, int elements) {
		return booleansToNative(dest, src, offset, elements, false);
	}

	private static native int booleansToNative(long dest, boolean[] src, int offset, int elements,
			boolean swapEndianess);

	// From native memory to array
	public static int nativeToBytes(byte[] dest, long src, int offset, int elements) {
		return nativeToBytes(dest, src, offset, elements, false);
	}

	private static native int nativeToBytes(byte[] dest, long src, int offset, int elements, boolean swapEndianess);

	public static native int nativeToShorts(short[] dest, long src, int offset, int elements, boolean swapEndianess);

	public static native int nativeToInts(int[] dest, long src, int offset, int elements, boolean swapEndianess);

	public static native int nativeToLongs(long[] dest, long src, int offset, int elements, boolean swapEndianess);

	public static native int nativeToFloats(float[] dest, long src, int offset, int elements, boolean swapEndianess);

	public static native int nativeToDoubles(double[] dest, long src, int offset, int elements, boolean swapEndianess);

	public static native int nativeToChars(char[] dest, long src, int offset, int elements, boolean swapEndianess);

	// Writes primitive arrays to a C file

	public static native long fopen(String file, String access);

	public static native void fclose(long fd);

	public static native void fflush(long fd);

	public static native int fputc(byte c, long fd);
	
	public static native int fgetc(byte c, long fd);

	// Single primitive

	public static void byteToFWrite(long fd, byte value) {
		byteToFWrite(fd, value, false);
	}

	private static native void byteToFWrite(long fd, byte value, boolean swapEndianess);

	public static native void shortToFWrite(long fd, short value, boolean swapEndianess);

	public static native void intToFWrite(long fd, int value, boolean swapEndianess);

	public static native void longToFWrite(long fd, long value, boolean swapEndianess);

	public static native void floatToFWrite(long fd, float value, boolean swapEndianess);

	public static native void doubleToFWrite(long fd, double value, boolean swapEndianess);

	public static native void charToFWrite(long fd, char value, boolean swapEndianess);

	public static void booleanToFWrite(long fd, boolean value) {
		booleanToFWrite(fd, value, false);
	}

	private static native void booleanToFWrite(long fd, boolean value, boolean swapEndianess);

	// Arrays

	public static int bytesToFWrite(long fd, byte[] srcJ, int srcOffset, int elements) {
		return bytesToFWrite(fd, srcJ, srcOffset, elements, false);
	}

	private static native int bytesToFWrite(long fd, byte[] srcJ, int srcOffset, int elements, boolean swapEndianess);

	public static native int shortsToFWrite(long fd, short[] srcJ, int srcOffset, int elements, boolean swapEndianess);

	public static native int intsToFWrite(long fd, int[] srcJ, int srcOffset, int elements, boolean swapEndianess);

	public static native int longsToFWrite(long fd, long[] srcJ, int srcOffset, int elements, boolean swapEndianess);

	public static native int floatsToFWrite(long fd, float[] srcJ, int srcOffset, int elements, boolean swapEndianess);

	public static native int doublesToFWrite(long fd, double[] srcJ, int srcOffset, int elements,
			boolean swapEndianess);

	public static native int charsToFWrite(long fd, char[] srcJ, int srcOffset, int elements, boolean swapEndianess);

	public static int booleansToFWrite(long fd, boolean[] srcJ, int srcOffset, int elements) {
		return booleansToFWrite(fd, srcJ, srcOffset, elements, false);
	}

	private static native int booleansToFWrite(long fd, boolean[] srcJ, int srcOffset, int elements,
			boolean swapEndianess);

	public static native int nativeToFWrite(long fd, long src, long bytes);

	// For native output

	public static void byteToNative(long address, byte value) {
		byteToNative(address, value, false);
	}

	private static native void byteToNative(long address, byte value, boolean swapEndianness);

	public static native void shortToNative(long address, short value, boolean swapEndianness);

	public static native void intToNative(long address, int value, boolean swapEndianness);

	public static native void longToNative(long address, long value, boolean swapEndianness);

	public static native void floatToNative(long address, float value, boolean swapEndianness);

	public static native void doubleToNative(long address, double value, boolean swapEndianness);

	public static native void charToNative(long address, char value, boolean swapEndianness);

	public static void booleanToNative(long address, boolean value) {
		booleanToNative(address, value, false);
	}

	private static native void booleanToNative(long address, boolean value, boolean swapEndianness);

	// For VLE. Returns the number of bytes written to address
	public static native int shortToVLENative(long address, short value);

	public static native int intToVLENative(long address, int value);

	public static native int longToVLENative(long address, long value);

	/**
	 * Copies n bytes from the source address to the destination address<br>
	 * Both {@code dest} and {@code src} pointers are <b>NOT</b> checked for validity! If they are null or point to
	 * memory that cannot be read, the Java Virtual Machine will terminate with a memory access violation!!!
	 * 
	 * @param dest
	 *            A pointer to copy n bytes to
	 * @param src
	 *            A pointer to copy n bytes from
	 * @param bytes
	 *            The number of bytes to copy
	 */
	public static native void memcpy(long dest, long src, long bytes);

	public static native byte[] ngetBuffer(long address, int capacity);

	public static void throwByteIndexOutOfBounds() {
		throw new RuntimeException("Java byte array cannot hold all of the elements! Switch to a native alternative!");
	}
}
