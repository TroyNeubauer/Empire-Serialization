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
		int line = 1;
		for (int i = 0; i < len; i++) {
			char c = str.charAt(i);
			if (fourOK && (c > fourEncoding.length || fourEncoding[c] == -1)) {
				fourOK = false;
				//System.out.println("four failed on character: " +c + " ("+((int)c)+")line " + line);
			}
			if (sixOK && (c > sixEncoding.length || sixEncoding[c] == -1)) {
				sixOK = false;
				//System.out.println("six failed on character: " +c + " ("+((int)c)+")line " + line);
			}
			if (!fourOK && !sixOK) {
				break;
			}
			if(c == '\n') line++;
		}

		if (fourOK)
			return FOUR_BIT_CHARSET;
		if (sixOK)
			return SIX_BIT_CHARSET;

		return VLE8_CHARSET;
	}
}
