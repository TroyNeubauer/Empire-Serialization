package com.troy.empireserialization.util;

/**
 * A class with some useful static methods for formatting {@link String}s
 * 
 * @author Troy Neubauer
 *
 */
public class StringFormatter {

	final static char[] DIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
			'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };

	private final static String[] binarySizes = { "", "kilo", "mega", "giga", "tera", "peta", "exa", "zetta", "yotta" };

	public static String insert(String text, int index, String insertText) {
		return text.substring(0, index) + insertText + text.substring(index, text.length());
	}

	public static String replace(String text, int startIndex, int endIndex, String replaceText) {
		return text.substring(0, startIndex) + replaceText + text.substring(endIndex, text.length());
	}

	public static String addCommas(long number) {
		return addCommas(number, 10);
	}

	public static String addCommas(long number, int base) {
		String string = Long.toString(number, base);
		if (string.length() < 4)
			return string;

		char[] chars = new char[((string.length() - 1) / 3) + string.length()];
		int count = 0;
		int indexInString = string.length() - 1;
		for (int i = chars.length - 1; i >= 0; i--) {
			if (count == 3) {
				chars[i] = ',';
				count = 0;
			} else {
				count++;
				chars[i] = string.charAt(indexInString);
				indexInString--;
			}
		}

		return new String(chars);
	}

	public static String addCommas(int number) {
		return addCommas(number, 10);
	}

	public static String addCommas(int number, int base) {
		String string = Integer.toString(number, base);
		if (string.length() < 4)
			return string;

		char[] chars = new char[((string.length() - 1) / 3) + string.length()];
		int count = 0;
		int indexInString = string.length() - 1;
		for (int i = chars.length - 1; i >= 0; i--) {
			if (count == 3) {
				chars[i] = ',';
				count = 0;
			} else {
				count++;
				chars[i] = string.charAt(indexInString);
				indexInString--;
			}
		}

		return new String(chars);
	}

	public static String toHexString(byte b) {
		return "" + DIGITS[b >> 4 & 0x0F] + DIGITS[b >> 0 & 0x0F];
	}

	public static String toHexString(short b) {
		return "" + DIGITS[b >> 12 & 0x0F] + DIGITS[b >> 8 & 0x0F] + DIGITS[b >> 4 & 0x0F] + DIGITS[b >> 0 & 0x0F];
	}

	public static String toHexString(int b) {
		StringBuilder sb = new StringBuilder(8);
		sb.append(DIGITS[b >>> 28 & 0b1111]);
		sb.append(DIGITS[b >>> 24 & 0b1111]);
		sb.append(DIGITS[b >>> 20 & 0b1111]);
		sb.append(DIGITS[b >>> 16 & 0b1111]);
		sb.append(DIGITS[b >>> 12 & 0b1111]);
		sb.append(DIGITS[b >>>  8 & 0b1111]);
		sb.append(DIGITS[b >>>  4 & 0b1111]);
		sb.append(DIGITS[b >>>  0 & 0b1111]);

		return sb.toString();

	}

	public static String toHexString(long b) {
		return "" + DIGITS[(int) (b >> 60 & 0x0F)] + DIGITS[(int) (b >> 56 & 0x0F)] + DIGITS[(int) (b >> 52 & 0x0F)] + DIGITS[(int) (b >> 48 & 0x0F)]
				+ DIGITS[(int) (b >> 44 & 0x0F)] + DIGITS[(int) (b >> 40 & 0x0F)] + DIGITS[(int) (b >> 36 & 0x0F)] + DIGITS[(int) (b >> 32 & 0x0F)]
				+ DIGITS[(int) (b >> 28 & 0x0F)] + DIGITS[(int) (b >> 24 & 0x0F)] + DIGITS[(int) (b >> 20 & 0x0F)] + DIGITS[(int) (b >> 16 & 0x0F)]
				+ DIGITS[(int) (b >> 12 & 0x0F)] + DIGITS[(int) (b >> 8 & 0x0F)] + DIGITS[(int) (b >> 4 & 0x0F)] + DIGITS[(int) (b >> 0 & 0x0F)];
	}

	public static String toHexString(float b) {
		return toHexString(Float.floatToIntBits(b));
	}

	public static String toHexString(double b) {
		return toHexString(Double.doubleToLongBits(b));
	}

	/**
	 * Returns a String the represents a byte in binary form<br>
	 * The String will always be 8 characters long<br>
	 * IE: <code>
	 * byte: 16 -> 00010000, byte: 100 -> 01100100,<br> byte: 0 -> 00000000
	 * </code>
	 * 
	 * @param b
	 *            The byte to be molded after
	 * @return The formatted String
	 */
	public static String toBinaryString(byte b) {
		String result = "";
		for (int i = Byte.SIZE - 1; i >= 0; i--) {
			result += ((b >> i) & 0b00000001);
		}
		return result;
	}

	/**
	 * Returns a String the represents a long in binary form<br>
	 * The String will always be 64 characters long<br>
	 * IE: <code>
	 * long: 16 -> 0000...preceding 0's 010000, long: 100 -> 0000...preceding 0's 01100100,<br> long: 0 -> all 0's
	 * </code>
	 * 
	 * @param l
	 *            The long to be molded after
	 * @return The formatted String
	 */
	public static String toBinaryString(long l) {
		String result = "";
		for (int i = Long.SIZE - 1; i >= 0; i--) {
			result += ((l >> i) & 0b1);
		}
		return result;
	}

	public static String toBinaryString(int i) {
		String result = "";
		for (int loopCtr = Integer.SIZE - 1; loopCtr >= 0; loopCtr--) {
			result += ((i >> loopCtr) & 0b1);
		}
		return result;
	}

	public static String toBinaryString(short s) {
		String result = "";
		for (int loopCtr = Integer.SIZE - 1; loopCtr >= 0; loopCtr--) {
			result += ((s >> loopCtr) & 0b1);
		}
		return result;
	}

	public static String toBinaryString(char c) {
		String result = "";
		for (int loopCtr = Integer.SIZE - 1; loopCtr >= 0; loopCtr--) {
			result += ((c >> loopCtr) & 0b1);
		}
		return result;
	}

	public static String toBinaryString(byte[] bytes) {
		return ArrayUtil.toBinaryString(bytes);
	}

	/**
	 * Ensures that a particular String is always a certain length by adding whitespace at the end or trimming to a shorter
	 * length<br>
	 * Because of Javadoc formatting issues, assume that '-' means space<br>
	 * IE: <code> cutToSize("Hi,-i'm-here", 20) ->  "Hi,-i'm-here--------"<br>
	 * cutToSize("This-is-very-interesting,-I-am-a-particularly-long-String,-what-a-waste-of-memory...", 10) -> "This-is-ve"<br> 
	 * </code>
	 * 
	 * @param string
	 *            The string to clip or extend
	 * @param characters
	 *            The amount of characters to clip of extend to
	 * @return The formatted String
	 */
	public static String cutToSize(String string, int characters) {
		char[] chars = new char[characters];
		int length = Math.min(string.length(), characters);
		for (int i = 0; i < length; i++) {
			chars[i] = string.charAt(i);
		}
		for (int i = length; i < characters; i++) {
			chars[i] = ' ';
		}
		return new String(chars);
	}

	/**
	 * Returns a String representing the double where all decimals after decimalPlaces are clipped.</br>
	 * Example: <code></br>
	 * System.out.println(clip(123.456789091234, 3));
	 * </code></br>
	 * Yields the output "123.456"
	 * 
	 * @param value
	 *            The double value to be clipped
	 * @param decimalPlaces
	 *            The amount of decimal places to keep
	 * @return The formatted String
	 */
	public static String clip(double value, int decimalPlaces) {
		String x = Double.toString(value);
		return x.substring(0, Math.min(x.lastIndexOf(".") + decimalPlaces + 1, x.length() - x.lastIndexOf(".")));
	}

	/**
	 * Returns a String representing the float where all decimals after decimalPlaces are clipped.</br>
	 * Example: <code></br>
	 * System.out.println(clip(123.456789091234f, 3));
	 * </code></br>
	 * Yields the output "123.456"
	 * 
	 * @param value
	 *            The float value to be clipped
	 * @param decimalPlaces
	 *            The amount of decimal places to keep
	 * @return The formatted String
	 */
	public static String clip(float value, int decimalPlaces) {
		String x = "" + value;
		return x.substring(0, Math.min(x.lastIndexOf(".") + decimalPlaces + 1, x.length() - x.lastIndexOf(".")));
	}

	/**
	 * Returns a string representing the amount of bytes in "Standard data form" base 1024</br>
	 * Examples:<code> 33 -> "33B", 15360 -> "15K", 583008256 -> "556MB", 3221225472 -> "3TB" etc.</code></br>
	 * This method assumes 1024 bytes to one kilobyte, 1024^2 to one megabyte etc.
	 * 
	 * @param bytes
	 *            The value in bytes to be formatted
	 * @return The formatted string
	 */
	public static String formatBytesShort(long bytes) {
		int unit = 1024;
		if (bytes < unit)
			return bytes + " B";
		int exp = (int) (Math.log(bytes) / Math.log(unit));
		String pre = ("KMGTPEZY").charAt(exp - 1) + "";
		return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);

	}

	/**
	 * Returns a string representing the amount of bytes in "Standard data form" base 1024</br>
	 * Examples:<code> 33 -> "33B", 15360 -> "15K", 583008256 -> "556MB", 3221225472 -> "3TB" etc.</code></br>
	 * This method assumes 1024 bytes to one kilobyte, 1024^2 to one megabyte etc.
	 * 
	 * @param bytes
	 *            The value in bytes to be formatted
	 * @return The formatted string
	 */
	public static String formatBytesLong(long bytes) {
		int unit = 1024;
		if (bytes < unit)
			return bytes + " bytes";
		int exp = (int) (Math.log(bytes) / Math.log(unit));
		String pre = binarySizes[exp];
		return String.format("%.1f %sbytes", bytes / Math.pow(unit, exp), pre);

	}

	/**
	 * Ensures that the string is all lower case except for the first letter
	 * 
	 * @param string
	 *            The string to capitalize
	 * @return The formatted string
	 */
	public static String capitalizeFirstLetter(String string) {
		int length = string.length();
		if (length == 0)
			return string;

		char[] chars = new char[length];
		chars[0] = Character.toUpperCase(string.charAt(0));
		for (int i = 1; i < length; i++) {
			chars[i] = Character.toLowerCase(string.charAt(i));
		}

		return new String(chars);
	}

	/**
	 * Faster then {@link String#replaceAll(String, String)} and can ignore case
	 * 
	 * @param findtxt
	 *            The text to look for
	 * @param replacetxt
	 *            The text to replace findtxt with
	 * @param str
	 *            The string to search in
	 * @param isCaseInsensitive
	 *            Weather or not expressions in a different case then findtxt should be replaced
	 * @return The resulting string
	 */
	public String replaceAll(String findtxt, String replacetxt, String str, boolean isCaseInsensitive) {
		if (str == null)
			return null;

		if (findtxt == null || findtxt.length() == 0)
			return str;

		if (findtxt.length() > str.length())
			return str;

		int counter = 0;
		String thesubstr = "";
		while ((counter < str.length()) && (str.substring(counter).length() >= findtxt.length())) {
			thesubstr = str.substring(counter, counter + findtxt.length());
			if (isCaseInsensitive) {
				if (thesubstr.equalsIgnoreCase(findtxt)) {
					str = str.substring(0, counter) + replacetxt + str.substring(counter + findtxt.length());
					counter += replacetxt.length();
				} else {
					counter++; // No match so move on to the next character from
								// which to check for a findtxt string match.
				}
			} else {
				if (thesubstr.equals(findtxt)) {
					str = str.substring(0, counter) + replacetxt + str.substring(counter + findtxt.length());
					counter += replacetxt.length();
				} else {
					counter++;
				}
			}
		}
		return str;
	}

	public static String toHexString(byte[] a) {
		return ArrayUtil.toHexString(a);
	}

	public static String toHexString(short[] a) {
		return ArrayUtil.toHexString(a);
	}

	public static String toHexString(char[] a) {
		return ArrayUtil.toHexString(a);
	}

	public static String toHexString(int[] a) {
		return ArrayUtil.toHexString(a);
	}

	public static String toHexString(long[] a) {
		return ArrayUtil.toHexString(a);
	}

	// No instances of this class allowed
	private StringFormatter() {
	}

	/**
	 * Appends an s to the end of {@code string} if the value is not one, otherwise the origional string is returned
	 * 
	 * @param string
	 *            The string to add an s to
	 * @param value
	 *            the value to check
	 * @return {@code string + "s"} if value != 1 else {@code string}
	 */
	public static String plural(String string, long value) {
		return value == 1 ? string : string.concat("s");
	}
}