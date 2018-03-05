package com.troy.serialization.util;

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.List;

public class ArrayUtil {

	public static <T> T[] concat(T[] a, T[] b) {
		int aLen = a.length;
		int bLen = b.length;

		@SuppressWarnings("unchecked")
		T[] c = (T[]) Array.newInstance(a.getClass().getComponentType(), aLen + bLen);
		System.arraycopy(a, 0, c, 0, aLen);
		System.arraycopy(b, 0, c, aLen, bLen);

		return c;
	}

	public static byte[] concat(byte[] a, byte[] b) {
		int aLen = a.length;
		int bLen = b.length;

		byte[] c = new byte[aLen + bLen];
		System.arraycopy(a, 0, c, 0, aLen);
		System.arraycopy(b, 0, c, aLen, bLen);

		return c;
	}

	public static short[] concat(short[] a, short[] b) {
		int aLen = a.length;
		int bLen = b.length;

		short[] c = new short[aLen + bLen];
		System.arraycopy(a, 0, c, 0, aLen);
		System.arraycopy(b, 0, c, aLen, bLen);

		return c;
	}

	public static char[] concat(char[] a, char[] b) {
		int aLen = a.length;
		int bLen = b.length;

		char[] c = new char[aLen + bLen];
		System.arraycopy(a, 0, c, 0, aLen);
		System.arraycopy(b, 0, c, aLen, bLen);

		return c;
	}

	public static int[] concat(int[] a, int[] b) {
		int aLen = a.length;
		int bLen = b.length;

		int[] c = new int[aLen + bLen];
		System.arraycopy(a, 0, c, 0, aLen);
		System.arraycopy(b, 0, c, aLen, bLen);

		return c;
	}

	public static long[] concat(long[] a, long[] b) {
		int aLen = a.length;
		int bLen = b.length;

		long[] c = new long[aLen + bLen];
		System.arraycopy(a, 0, c, 0, aLen);
		System.arraycopy(b, 0, c, aLen, bLen);

		return c;
	}

	public static float[] concat(float[] a, float[] b) {
		int aLen = a.length;
		int bLen = b.length;

		float[] c = new float[aLen + bLen];
		System.arraycopy(a, 0, c, 0, aLen);
		System.arraycopy(b, 0, c, aLen, bLen);

		return c;
	}

	public static double[] concat(double[] a, double[] b) {
		int aLen = a.length;
		int bLen = b.length;

		double[] c = new double[aLen + bLen];
		System.arraycopy(a, 0, c, 0, aLen);
		System.arraycopy(b, 0, c, aLen, bLen);

		return c;
	}

	public static byte[] subArray(byte[] array, int start, int end) {
		if (start < 0 || end < 0) {
			throw new IllegalArgumentException("Invalid start or end! Start: " + start + "  End: " + end);
		}
		int newLength = end - start;
		if (newLength < 0)
			throw new IllegalArgumentException(
					"New array length is < 0!  Start: " + start + "  End: " + end + "  New Length: " + newLength);
		byte[] newArray = new byte[newLength];
		System.arraycopy(array, start, newArray, 0, newLength);
		return newArray;
	}

	public static byte[] trimZeros(byte[] data) {
		if ((data == null) || isEmpty(data))
			return new byte[0];

		int beginIndex = 0, endIndex = data.length - 1;
		for (int i = 0; i < data.length; i++) {
			if (data[i] == 0x0)
				beginIndex = i + 1;
			else
				break;
		}
		for (int i = data.length - 1; i >= 0; i--) {
			if (data[i] == 0x0)
				endIndex = i - 1;
			else
				break;
		}

		byte[] result = new byte[endIndex - beginIndex + 1];
		int index = 0;
		for (int i = beginIndex; i <= endIndex; i++, index++)
			result[index] = data[i];

		return result;

	}

	public static byte[] toByteArray(List<Byte> data) {
		byte[] finalArray = new byte[data.size()];
		Iterator<Byte> i = data.iterator();
		int count = 0;
		while (i.hasNext()) {
			finalArray[count] = (byte) i.next();
			count++;
		}
		return finalArray;
	}

	public static boolean isEmpty(byte[] data) {
		if (data == null)
			return true;
		for (int i = 0; i < data.length; i++)
			if (data[i] != 0x0)
				return false;

		return true;
	}

	public static String toBinaryString(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (int i = 0; i < bytes.length; i++) {
			sb.append(StringFormatter.toBinaryString(bytes[i]));
			if (i != bytes.length - 1)
				sb.append(", ");
		}
		sb.append("]");
		return sb.toString();
	}

	public static String toHexString(byte[] a) {
		if (a == null)
			return "null";
		int iMax = a.length - 1;
		if (iMax == -1)
			return "[]";

		StringBuilder b = new StringBuilder();
		b.append('[');
		for (int i = 0;; i++) {
			b.append("0x" + StringFormatter.toHexString(a[i]));
			if (i == iMax)
				return b.append(']').toString();
			b.append(", ");
		}
	}

	public static String toHexString(short[] a) {
		if (a == null)
			return "null";
		int iMax = a.length - 1;
		if (iMax == -1)
			return "[]";

		StringBuilder b = new StringBuilder();
		b.append('[');
		for (int i = 0;; i++) {
			b.append("0x" + StringFormatter.toHexString(a[i]));
			if (i == iMax)
				return b.append(']').toString();
			b.append(", ");
		}
	}

	public static String toHexString(char[] a) {
		if (a == null)
			return "null";
		int iMax = a.length - 1;
		if (iMax == -1)
			return "[]";

		StringBuilder b = new StringBuilder();
		b.append('[');
		for (int i = 0;; i++) {
			b.append("0x" + StringFormatter.toHexString(a[i]));
			if (i == iMax)
				return b.append(']').toString();
			b.append(", ");
		}
	}

	public static String toHexString(int[] a) {
		if (a == null)
			return "null";
		int iMax = a.length - 1;
		if (iMax == -1)
			return "[]";

		StringBuilder b = new StringBuilder();
		b.append('[');
		for (int i = 0;; i++) {
			b.append("0x" + StringFormatter.toHexString(a[i]));
			if (i == iMax)
				return b.append(']').toString();
			b.append(", ");
		}
	}

	public static String toHexString(long[] a) {
		if (a == null)
			return "null";
		int iMax = a.length - 1;
		if (iMax == -1)
			return "[]";

		StringBuilder b = new StringBuilder();
		b.append('[');
		for (int i = 0;; i++) {
			b.append("0x" + StringFormatter.toHexString(a[i]));
			if (i == iMax)
				return b.append(']').toString();
			b.append(", ");
		}
	}

}
