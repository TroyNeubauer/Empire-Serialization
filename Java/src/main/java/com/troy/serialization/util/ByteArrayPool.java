package com.troy.serialization.util;

import java.util.*;

public class ByteArrayPool {

	private static final List<byte[]> avilable = new ArrayList<byte[]>();

	static {
		for (int i = 6; i < 16; i++) {
			int size = (int) Math.pow(2, i);
			for (int j = 0; j < 3; j++) {
				avilable.add(new byte[size]);
			}
		}
	}

	public static byte[] aquire(int minLength) {
		// Binary search
		int low = 0;
		int high = avilable.size() - 1;

		while (low <= high) {
			int mid = (low + high) >>> 1;
			byte[] midVal = avilable.get(mid);
			if (midVal.length < minLength)
				low = mid + 1;
			else {
				avilable.remove(mid);
				sort();
				return midVal;
			}
		}
		byte[] bytes = new byte[minLength];
		avilable.add(bytes);
		sort();
		return bytes;
	}
	
	public static void restore(byte[] bytes) {
		avilable.add(bytes);
		sort();
	}

	private static void sort() {
		avilable.sort(comp);
	}
	
	private static final Comparator<byte[]> comp = new Comparator<byte[]>() {
		@Override
		public int compare(byte[] o1, byte[] o2) {
			return Integer.compare(o1.length, o2.length);
		}
	};

}
