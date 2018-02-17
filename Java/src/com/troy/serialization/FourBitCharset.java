package com.troy.serialization;

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
	public int decode(byte[] src, char[] dest, int srcOffset, int destOffset, final int chars, boolean checkForErrors) {
		int decodedBytes = 0;
		int decodedChars = 0;
		boolean shift = true;
		while (decodedChars < chars) {
			if (shift) {
				dest[decodedChars++] = (char) (src[decodedBytes] >>> 4);
			} else {
				dest[decodedChars++] = (char) (src[decodedBytes++] & MASK);
			}
			shift = !shift;
		}

		return decodedBytes;
	}

	@Override
	public int encode(final char[] src, final byte[] dest, final int srcOffset, int destOffset, final int chars, boolean checkForErrors) {
		if (NativeUtils.NATIVES_ENABLED) {
			int bytes = nEncode(src, dest, srcOffset, destOffset, chars, NativeUtils.getAddress(), checkForErrors);
			if (bytes == -1) {
				switch (NativeUtils.getErrorCode()) {
				case (SerializationUtils.OUT_OF_MEMORY):
					throw new OutOfMemoryError("Unable to allocate buffer for copying to native memory");
				case (SerializationUtils.UNSUPPORTED_CHARACTER):
					throw new IllegalArgumentException("Invalid character\'" + NativeUtils.getInvalidChar() + "\' at index " + NativeUtils.getInvalidCharIndex() + " for charset " + this.name());
				}
				throw new RuntimeException("Error encoding characters!");
			}
			return bytes;
		} else {
			int bytesWritten = 0;
			int i = 0;
			final int end = (chars / 2) * 2;// round down to next mutiple of two
			while (i < end) {
				int index = srcOffset + i;
				dest[bytesWritten++] = (byte) ((encode(src[index], index, checkForErrors) << 4) | encode(src[index + 1], index + 1, checkForErrors));
				i += 2;
			}
			if (chars % 2 != 0) {
				dest[bytesWritten++] = (byte) (encode(src[end], end, checkForErrors) << 4);
			}
			return bytesWritten;
		}
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
	public int encode(char c, int index, boolean checkForErrors) {
		if (checkForErrors) {
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
		if (checkForErrors)
			if (encoded < 0 || encoded >= DECODING_CACHE.length)
				throw new RuntimeException(name() + " doesnt support the character code " + encoded);
		return DECODING_CACHE[encoded];
	}

	@Override
	public String name() {
		return "4Bit-Encoding";
	}

	@Override
	public void init() {
	}

}
