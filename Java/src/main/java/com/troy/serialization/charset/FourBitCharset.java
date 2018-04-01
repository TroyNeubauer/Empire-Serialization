package com.troy.serialization.charset;

import com.troy.serialization.exception.UnsupportedCharacterException;
import com.troy.serialization.io.*;
import com.troy.serialization.util.*;
public final class FourBitCharset implements TroyCharset {
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
		int i = srcOffset;
		final int end = (chars / 2) * 2;// round down to next multiple of two
		if (checkForErrors) {
			while (i < end) {
				char c = src[i++];
				if (c >= ENCODING_CACHE.length || ENCODING_CACHE[c] == -1)
					throw new UnsupportedCharacterException(c, i, this);
				char d = src[i++];
				if (d >= ENCODING_CACHE.length || ENCODING_CACHE[d] == -1)
					throw new UnsupportedCharacterException(c, i, this);
				dest.writeByte((byte) ((ENCODING_CACHE[c] << 4) | ENCODING_CACHE[d]));
			}
		} else {
			while (i < end) {
				char c = src[i++];
				char d = src[i++];
				dest.writeByte((byte) ((ENCODING_CACHE[c] << 4) | ENCODING_CACHE[d]));
			}
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

	@Override
	public byte getOpCode() {
		return 0b00;
	}

	@Override
	public char[] getDecodingCache() {
		return DECODING_CACHE;
	}

	@Override
	public int[] getEncodingCache() {
		return ENCODING_CACHE;
	}

}
