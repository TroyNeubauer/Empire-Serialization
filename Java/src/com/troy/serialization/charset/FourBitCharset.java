package com.troy.serialization.charset;

import com.troy.serialization.*;
import com.troy.serialization.exception.UnsupportedCharacterException;
import com.troy.serialization.io.*;
import com.troy.serialization.util.*;

final class FourBitCharset implements TroyCharset {
	static {
		NativeUtils.init();
	}
	public static final byte CODE = 0b00;
	public static final int MASK = 0b00001111;
	public static final int MIN_VALUE = 0, MAX_VALUE = 15;
	// maps a four bit code -> java char value
	public static final char[] DECODING_CACHE = new char[] { 'e', 't', 'a', 'o', 'i', 'n', 's', 'h', 'r', 'd', 'l', 'c', 'u', 'm', 'w', 'f' };
	// maps a java char -> four bit code
	private static final int[] ENCODING_CACHE = SerializationUtils.constructEncodingFromDecoding(DECODING_CACHE);

	@Override
	public void decode(Input src, char[] dest, int destOffset, final int chars) {
		boolean shift = true;
		final int end = destOffset + chars;
		byte b = 0;
		while (destOffset < end) {
			if (shift) {
				b = src.readByte();
				dest[destOffset++] = DECODING_CACHE[b >>> 4];
			} else {
				dest[destOffset++] = DECODING_CACHE[b & MASK];
			}
			shift = !shift;
		}
	}

	@Override
	public void encode(final char[] src, Output dest, final int srcOffset, final int chars, boolean checkForErrors) {
		/*
		 * if (NativeUtils.NATIVES_ENABLED) { int bytes = nEncode(src, dest, srcOffset, destOffset, chars, NativeUtils.getAddress(), checkForErrors); if
		 * (bytes == -1) { switch (NativeUtils.getErrorCode()) { case (SerializationUtils.OUT_OF_MEMORY): throw new
		 * OutOfMemoryError("Unable to allocate buffer for copying to native memory"); case (SerializationUtils.UNSUPPORTED_CHARACTER): throw new
		 * IllegalArgumentException("Invalid character\'" + NativeUtils.getInvalidChar() + "\' at index " + NativeUtils.getInvalidCharIndex() +
		 * " for charset " + this.name()); } throw new RuntimeException("Error encoding characters!"); } return bytes; } else {
		 */
		if (checkForErrors) {// Scan for unsupported characters
			for (int i = srcOffset; i < chars + srcOffset; i++) {
				char c = src[i];
				if (c >= ENCODING_CACHE.length)
					throw new UnsupportedCharacterException(c, i, this);

				if (ENCODING_CACHE[c] == -1)
					throw new UnsupportedCharacterException(c, i, this);
			}
		}
		int i = srcOffset;
		final int end = (chars / 2) * 2;// round down to next multiple of two
		while (i < end) {
			dest.writeByte((byte) ((ENCODING_CACHE[src[i++]] << 4) | ENCODING_CACHE[src[i++]]));
		}
		if (chars % 2 != 0) {// Write the remaining byte if there was an odd number
			dest.writeByte((byte) (ENCODING_CACHE[src[end]] << 4));
		}
		// }
	}

	public native int nEncode(char[] src, byte[] dest, int srcOffset, int destOffset, int chars, long address, boolean checkForErrors);

	@Override
	public float getMinCharactersPerByte() {
		return 2.0f;
	}

	@Override
	public int getCharsetCode() {
		return CODE;
	}

	@Override
	public String name() {
		return "4Bit-Encoding";
	}

	@Override
	public void init() {
	}

}
