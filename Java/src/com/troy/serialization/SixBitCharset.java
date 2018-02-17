package com.troy.serialization;

public class SixBitCharset implements TroyCharset {
	static {
		NativeUtils.init();
	}
	public static final byte CODE = 0b01;
	public static final int MIN_VALUE = 0, MAX_VALUE = 63;
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
	public int decode(byte[] source, char[] dest, int srcOffset, int destOffset, int chars, boolean checkForErrors) {

		return 0;
	}

	@Override
	public int encode(char[] src, byte[] dest, int srcOffset, int destOffset, int chars, boolean checkForErrors) {

		return 0;
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
	public int encode(char c, int index, boolean checkForErrors) {
		if (SerializationUtils.CHECK_CHARSET_PROBLEMS) {
			if (c >= ENCODING_CACHE.length)
				throw new UnsupportedCharacterException(c, index, this);
			int code = ENCODING_CACHE[c];
			if (code < MIN_VALUE || code > MAX_VALUE) {
				throw new UnsupportedCharacterException(c, this);
			}
			return code;
		}
		return ENCODING_CACHE[c];
	}

	@Override
	public char decode(int encoded, boolean checkForErrors) {

		return DECODING_CACHE[encoded];
	}

	@Override
	public String name() {

		return "6Bit-Encoding";
	}

	@Override
	public void init() {
	}

}
