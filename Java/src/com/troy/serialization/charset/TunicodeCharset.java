package com.troy.serialization.charset;

import com.troy.serialization.io.*;

public class TunicodeCharset implements TroyCharset {
	public static final byte CODE = 0b10;
	private static final int INFO_MASK = 0b10000000;

	@Override
	public int decode(Input src, char[] dest, int destOffset, int chars, boolean checkForErrors) {
		int charsRead = 0;

		return charsRead;
	}

	@Override
	public int encode(char[] src, Output dest, int srcOffset, int chars, boolean checkForErrors) {
		final int end = srcOffset + chars;
		/*int i = destOffset;
		while (srcOffset < end) {
			char c = src[srcOffset++];
			if ((c & INFO_MASK) == 0) {
				dest[i++] = (byte) c;
			} else {
				if(Character.isHighSurrogate(c)) {//Encode the next two characters
					char low = src[srcOffset++];
					assert Character.isLowSurrogate(low);
					
					int codePoint = c << Character.SIZE | low;
				} else {//Normal 16 bit char
					
				}
			}
		}
		return i - destOffset;*/
		return 0;
	}

	@Override
	public float getMinCharactersPerByte() {
		return 1;
	}

	@Override
	public int getCharsetCode() {
		return CODE;
	}

	@Override
	public String name() {
		return "UTF-8";
	}

	@Override
	public void init() {
	}

}
