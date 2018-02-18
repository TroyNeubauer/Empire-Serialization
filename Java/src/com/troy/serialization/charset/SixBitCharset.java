package com.troy.serialization.charset;

import com.troy.serialization.io.*;
import com.troy.serialization.util.*;

public class SixBitCharset implements TroyCharset {
	static {
		NativeUtils.init();
	}
	public static final byte CODE = 0b01;
	public static final int MIN_VALUE = 0, MAX_VALUE = 63;
	private static final int MASK = 0b00111111;
	// maps a four bit code -> java char value
	//format:off
	public static final char[] DECODING_CACHE = new char[] { 
			'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', ')', '!', '@', '#', '$', '%', '^', '&','\"', 
			'(', ' ','\0', '+', '-', '*', '/', '<', '>', '?', ',', '.','\'', ';', ':', '[', ']', '{', '}',
			'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's',
			't', 'u', 'v', 'w', 'x', 'y', 'z' };
	//format:on

	// maps a java char -> four bit code
	private static final int[] ENCODING_CACHE = SerializationUtils.constructEncodingFromDecoding(DECODING_CACHE);

	@Override
	public void decode(Input src, char[] dest, int destOffset, int chars) {
	}

	@Override
	public void encode(char[] src, Output dest, int srcOffset, int chars, boolean checkForErrors) {
		int i = srcOffset;
		final int end = (chars / 4) * 4;// round down to next multiple of two
		while (i < end) {
			char c0 = src[i++];
			char c1 = src[i++];
			char c2 = src[i++];
			char c3 = src[i++];
			//format:off
			byte b0 = (byte) ((ENCODING_CACHE[c0] << 2) & MASK   | (ENCODING_CACHE[c1] >>> 4) & 0b11);
			byte b1 = (byte) ((ENCODING_CACHE[c1] << 4) & 0b1111 | (ENCODING_CACHE[c2] >>> 4) & 0b11);
			byte b2 = (byte) ((ENCODING_CACHE[c2] << 6) & 0b11   | ENCODING_CACHE[c3] & MASK);
			//format:on

			dest.writeByte(b0);
			dest.writeByte(b1);
			dest.writeByte(b2);
		}
		if (chars % 2 != 0) {// Write the remaining byte if there was an odd number
			dest.writeByte((byte) (ENCODING_CACHE[src[end]] << 4));
		}
	}

	@Override
	public float getMinCharactersPerByte() {
		return 8.0f / (6.0f * 2.0f);// Usually 4 chars per 3 bytes but if we have three capital letters its 4 chars in 6 bytes
	}

	@Override
	public int getCharsetCode() {
		return CODE;
	}

	@Override
	public String name() {

		return "6Bit-Encoding";
	}

	@Override
	public void init() {
	}

}
