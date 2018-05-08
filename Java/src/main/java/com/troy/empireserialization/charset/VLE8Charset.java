package com.troy.empireserialization.charset;

import com.troy.empireserialization.io.in.*;
import com.troy.empireserialization.io.out.*;
import com.troy.empireserialization.util.*;

public class VLE8Charset implements EmpireCharset {

	public static final byte CODE = 0b10;
	private static final int INFO_MASK = 0b10000000;
	private static final int DATA_MASK = 0b01111111;

	private static final int HAS_NEXT_BYTE = 0b10000000;
	private static final int DOESNT_HAVE_NEXT_BYTE = 0b00000000;

	// Not used for encoding since java natively uses unicode only for implementing
	// methods
	private static final char[] DECODING_CACHE;
	private static final int[] ENCODING_CACHE;

	static {
		DECODING_CACHE = new char[Character.MAX_VALUE];
		for (int i = 0; i < DECODING_CACHE.length; i++) {
			DECODING_CACHE[i] = (char) i;
		}
		ENCODING_CACHE = SerializationUtils.constructEncodingFromDecoding(DECODING_CACHE);
	}

	public native int nEncodeImpl(char[] src, long dest, int srcOffset, int chars, int info);

	@Override
	public long decode(Input src, char[] dest, int destOffset, int chars) {
		long count = 0;
		final int end = destOffset + chars;
		for (int i = destOffset; i < end; i++) {
			int b = src.readByte();
			count++;
			int result = b & DATA_MASK;
			if ((b & INFO_MASK) == HAS_NEXT_BYTE) {
				b = src.readByte();
				count++;
				result |= (b & DATA_MASK) << 7;
				if ((b & INFO_MASK) == HAS_NEXT_BYTE) {
					b = src.readByte();
					count++;
					result |= (b & DATA_MASK) << 14;
				}
			}
			dest[i] = (char) result;
		}
		return count;
	}

	@Override
	public long encodeImpl(char[] src, Output dest, int srcOffset, int chars, int info) {

		final int end = srcOffset + chars;
		if (info == StringInfo.ALL_ASCII) {
			while (srcOffset < end)
				dest.writeByte(src[srcOffset++]);
			return chars;
		} else {
			long count = 0;
			while (srcOffset < end) {
				char value = src[srcOffset++];
				if (value >>> 7 == 0) {
					dest.writeByte((byte) value);
					count++;
				} else if (value >>> 14 == 0) {
					dest.writeByte((byte) ((value & DATA_MASK) | HAS_NEXT_BYTE));
					dest.writeByte((byte) (value >>> 7));
					count++;
				} else if (value >>> 21 == 0) {
					dest.writeByte((byte) ((value & DATA_MASK) | HAS_NEXT_BYTE));
					dest.writeByte((byte) (value >>> 7 | HAS_NEXT_BYTE));
					dest.writeByte((byte) (value >>> 14));
					count++;
				}
			}
			return count;
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
