package com.troy.serialization.charset;

import com.troy.serialization.*;

public class TroyCharsets {
	public static final TroyCharset FOUR_BIT_ENCODING = new FourBitCharset(), SIX_BIT_ENCODING = new SixBitCharset();

	public static void init() {
		FOUR_BIT_ENCODING.init();
		SIX_BIT_ENCODING.init();
	}
}
