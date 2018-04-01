package com.troy.serialization.charset;

public class TroyCharsets {
	public static final FourBitCharset FOUR_BIT_CHARSET = new FourBitCharset();
	public static final SixBitCharset SIX_BIT_CHARSET = new SixBitCharset();
	public static final VLE8Charset VLE8_CHARSET = new VLE8Charset();

	public static void init() {
		FOUR_BIT_CHARSET.init();
		SIX_BIT_CHARSET.init();
	}

	/**
	 * Returns a charset capable of encoding the specified string
	 * @param str The string to use when identifying an appropriate charset
	 * @return A charset capable of encoding the specified string
	 */
	public static TroyCharset identifyCharset(String str) {
		boolean fourOK = true;
		boolean sixOK = true;
		int len = str.length();
		int[] fourEncoding = FOUR_BIT_CHARSET.getEncodingCache();
		int[] sixEncoding = SIX_BIT_CHARSET.getEncodingCache();
		for (int i = 0; i < len; i++) {
			char c = str.charAt(i);
			if (fourOK && fourEncoding[c] == -1) {
				fourOK = false;
			}
			if (sixOK && sixEncoding[c] == -1) {
				sixOK = false;
			}
			if (!fourOK && !sixOK) {
				break;
			}
		}

		if (fourOK)
			return FOUR_BIT_CHARSET;
		if (sixOK)
			return SIX_BIT_CHARSET;

		return VLE8_CHARSET;
	}
}
