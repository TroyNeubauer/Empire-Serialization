package com.troy.serialization.charset;

import com.troy.serialization.io.*;

public class TunicodeCharset implements TroyCharset {
	public static final byte CODE = 0b10;
	private static final int INFO_MASK = 0b10000000;
	private static final int DATA_MASK = 0b01111111;

	private static final int HAS_NEXT_BYTE = 0b10000000;
	private static final int DOESNT_HAVE_NEXT_BYTE = 0b00000000;

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
	public void encode(char[] src, Output dest, int srcOffset, int chars, boolean checkForErrors) {
		final int end = srcOffset + chars;
		while (srcOffset < end) {
			char value = src[srcOffset];
			if (value >>> 7 == 0) {
				dest.writeByte((byte) value);
			} else if (value >>> 14 == 0) {
				dest.writeByte((byte) ((value & DATA_MASK) | HAS_NEXT_BYTE));
				dest.writeByte((byte) (value >>> 7));
			} else if (value >>> 21 == 0) {
				dest.writeByte((byte) ((value & DATA_MASK) | HAS_NEXT_BYTE));
				dest.writeByte((byte) (value >>> 7 | 0x80));
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
		return "Tunicode";
	}

	@Override
	public void init() {
	}

}
