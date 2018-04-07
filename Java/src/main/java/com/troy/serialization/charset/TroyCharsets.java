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
	 * @return A set of information containing a charset capable of encoding the specified string and some general info
	 */
	public static StringInfo identifyCharset(String str, int offset, int length) {
		char[] chars = MiscUtil.getCharsFast(str);
		if (NativeUtils.NATIVES_ENABLED) {
			int result = nIdentifyCharset(chars, offset, length);
			int value = result & 0b11;
			int info = (result >>> 2) & 0b11;
			StringInfo r = new StringInfo(info);

			if (value == FourBitCharset.CODE)
				r.charset = FOUR_BIT_CHARSET;
			else if (value == SixBitCharset.CODE)
				r.charset = SIX_BIT_CHARSET;
			else
				r.charset = VLE8_CHARSET;
			return r;
		} else {
			boolean fourOK = true;
			boolean sixOK = true;
			final int end = length + offset;
			int[] fourEncoding = FourBitCharset.ENCODING_CACHE;
			int[] sixEncoding = SixBitCharset.ENCODING_CACHE;
			boolean allASCII = true;
			for (int i = offset; i < end; i++) {
				char c = chars[i];
				if (allASCII && (c >>> 7) != 0)
					allASCII = false;
				if (fourOK && (c > fourEncoding.length || fourEncoding[c] == -1)) {
					fourOK = false;
					// System.out.println("four failed on character: " +c + " ("+((int)c)+")line " + line);
				}
				if (sixOK && (c > sixEncoding.length || sixEncoding[c] == -1)) {
					sixOK = false;
					// System.out.println("six failed on character: " +c + " ("+((int)c)+")line " + line);
				}
				if (!fourOK && !sixOK && !allASCII) {
					break;
				}
			}

			StringInfo r = new StringInfo(allASCII ? StringInfo.ALL_ASCII : StringInfo.OTHER);

			if (fourOK)
				r.charset = FOUR_BIT_CHARSET;
			else if (sixOK)
				r.charset = SIX_BIT_CHARSET;
			else
				r.charset = VLE8_CHARSET;
			return r;
		}
	}

	private static native int nIdentifyCharset(char[] chars, int offset, int length);
}
