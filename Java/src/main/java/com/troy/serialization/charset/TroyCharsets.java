package com.troy.serialization.charset;

import com.troy.serialization.util.*;

public class TroyCharsets {
	public static final FourBitCharset FOUR_BIT_CHARSET = new FourBitCharset();
	public static final SixBitCharset SIX_BIT_CHARSET = new SixBitCharset();
	public static final VLE8Charset VLE8_CHARSET = new VLE8Charset();

	public static void init() {
	}

	/**
	 * Returns a charset capable of encoding the specified string
	 * 
	 * @param str
	 *            The string to use when identifying an appropriate charset
	 * @return A charset capable of encoding the specified string
	 */
	public static TroyCharset identifyCharset(String str, int offset, int length) {
		char[] chars = MiscUtil.getCharsFast(str);
		if (NativeUtils.NATIVES_ENABLED) {
			int value = nIdentifyCharset(chars, offset, length);

			if(value == FourBitCharset.CODE) return FOUR_BIT_CHARSET;
			if(value == SixBitCharset.CODE) return SIX_BIT_CHARSET;
			else return VLE8_CHARSET;
		} else {
			boolean fourOK = true;
			boolean sixOK = true;
			final int end = length + offset;
			int[] fourEncoding = FourBitCharset.ENCODING_CACHE;
			int[] sixEncoding = SixBitCharset.ENCODING_CACHE;
			
			for (int i = offset; i < end; i++) {
				char c = chars[i];
				if (fourOK && (c > fourEncoding.length || fourEncoding[c] == -1)) {
					fourOK = false;
					// System.out.println("four failed on character: " +c + " ("+((int)c)+")line " + line);
				}
				if (sixOK && (c > sixEncoding.length || sixEncoding[c] == -1)) {
					sixOK = false;
					// System.out.println("six failed on character: " +c + " ("+((int)c)+")line " + line);
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

	private static native int nIdentifyCharset(char[] chars, int offset, int length);
}
