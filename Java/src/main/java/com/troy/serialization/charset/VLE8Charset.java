package com.troy.serialization.charset;

import com.troy.serialization.io.*;
import com.troy.serialization.util.*;

public class VLE8Charset implements TroyCharset {

	public static final byte CODE = 0b10;
	private static final int INFO_MASK = 0b10000000;
	private static final int DATA_MASK = 0b01111111;

	private static final int HAS_NEXT_BYTE = 0b10000000;
	private static final int DOESNT_HAVE_NEXT_BYTE = 0b00000000;

	//Not used for encoding since java natively uses unicode only for implementing methods
	private static final char[] DECODING_CACHE;
	private static final int[] ENCODING_CACHE;

	static {
		DECODING_CACHE = new char[Character.MAX_VALUE];
		for (int i = 0; i < DECODING_CACHE.length; i++) {
			DECODING_CACHE[i] = (char) i;
		}
		ENCODING_CACHE = SerializationUtils.constructEncodingFromDecoding(DECODING_CACHE);
	}

	public native int nEncodeImpl(char[] src, long dest, int srcOffset, int chars);

	@Override
	public void decode(Input src, char[] dest, int destOffset, int chars) {
		final int end = destOffset + chars;
		for (int i = destOffset; i < end; i++) {
			int b = src.readByte();
			int result = b & DATA_MASK;
			if ((b & INFO_MASK) == HAS_NEXT_BYTE) {
				b = src.readByte();
				result |= (b & DATA_MASK) << 7;
				if ((b & INFO_MASK) == HAS_NEXT_BYTE) {
					b = src.readByte();
					result |= (b & DATA_MASK) << 14;
				}
			}
			dest[i] = (char) result;
		}
	}

	@Override
	public void encodeImpl(char[] src, Output dest, int srcOffset, int chars) {
		final int end = srcOffset + chars;
		while (srcOffset < end) {
			char value = src[srcOffset++];
			if (value >>> 7 == 0) {
				dest.writeByte((byte) value);
			} else if (value >>> 14 == 0) {
				dest.writeByte((byte) ((value & DATA_MASK) | HAS_NEXT_BYTE));
				dest.writeByte((byte) (value >>> 7));
			} else if (value >>> 21 == 0) {
				dest.writeByte((byte) ((value & DATA_MASK) | HAS_NEXT_BYTE));
				dest.writeByte((byte) (value >>> 7 | HAS_NEXT_BYTE));
				dest.writeByte((byte) (value >>> 14));
			}
		}
	}

	@Override
	public float getMinCharactersPerByte() {
		return 1.0f / 3.0f;
	}

	@Override
	public int getCharsetCode() {
		return CODE;
	}

	@Override
	public String name() {
		return "VLE8";
	}

	@Override
	public byte getOpCode() {
		return CODE;
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
