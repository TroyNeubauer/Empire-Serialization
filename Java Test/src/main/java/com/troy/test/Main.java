package com.troy.test;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.Map.*;

public class Main {

	public static void main(String[] args) throws Throwable {

		/*
		 * File f = new File("./MyLibraryNative.dat"); InputStreamInput in = new InputStreamInput(new
		 * FileInputStream(f)); NativeMemoryBlock block = in.map(f.length()); System.out.println(block);
		 * 
		 * System.exit(0);
		 * 
		 * final Unsafe unsafe = MiscUtil.getUnsafe();
		 */

		// Library.doTest(Constants.BIG_HARRY_POTTER);
		wordTest(new File("C:\\Users\\Troy Neubauer\\Desktop\\scan").listFiles());

	}

	public static void wordTest(File... files) throws Exception {
		HashMap<Character, Integer> map = new HashMap<Character, Integer>();
		int counter = 0;
		for (File file : files) {
			System.out.println(file);
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;
			while ((line = reader.readLine()) != null) {
				for (char c : line.toCharArray()) {
					Character cc = Character.valueOf(c);
					Integer result = map.get(cc);
					if (result == null) {
						result = Integer.valueOf(1);
					} else {
						result = Integer.valueOf(result.intValue() + 1);
					}
					map.put(cc, result);
					counter++;
				}
			}
			reader.close();
		}
		System.out.println("Scanned " + counter + " characters. ");
		System.out.println(map.size() +" different characters");
		System.out.println("Results:");
		Map<Character, Integer> r = new TreeMap<Character, Integer>(new Comparator<Character>() {

			@Override
			public int compare(Character o1, Character o2) {
				return Integer.compare(map.get(o2).intValue(), map.get(o1).intValue());
			}
		});
		r.putAll(map);
		for (Entry<Character, Integer> entry : r.entrySet()) {
			char key = entry.getKey().charValue();
			int value = entry.getValue().intValue();
			System.out.println(
					key + " (" + ((int) key) + ") count " + value + " = " + ((double) value / counter * 100.0) + "%");
		}

	}

	private static int[] sort(int[] chars) {
		int[] result = new int[chars.length];
		for (int i = 0; i < result.length; i++) {
			result[i] = i;
		}
		int n = result.length;
		for (int i = 1; i < n; ++i) {
			int key = result[i];
			int j = i - 1;

			/*
			 * Move elements of arr[0..i-1], that are greater than key, to one position ahead of their current position
			 */
			while (j >= 0 && chars[j] > key) {
				result[j + 1] = result[j];
				j = j - 1;
			}
			result[j + 1] = key;
		}
		return result;
	}

}
