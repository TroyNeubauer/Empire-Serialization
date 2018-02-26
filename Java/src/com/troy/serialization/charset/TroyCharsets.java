package com.troy.serialization.charset;

public class TroyCharsets {
	public static final FourBitCharset FOUR_BIT_ENCODING = new FourBitCharset();
	public static final SixBitCharset SIX_BIT_ENCODING = new SixBitCharset();

	public static void init() {
		FOUR_BIT_ENCODING.init();
		SIX_BIT_ENCODING.init();
	}
}
