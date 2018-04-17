package com.troy.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.troy.empireserialization.clazz.ClassData;

import java.util.TreeMap;

public class Main {

	public static void main(String[] args) throws Throwable {

		System.out.println(Object.class.isAssignableFrom(String.class));
		
		List<String> list = new ArrayList<String>();
		new ClassData<>(list.getClass());

		/*
		 * File f = new File("./MyLibraryNative.dat"); InputStreamInput in = new InputStreamInput(new
		 * FileInputStream(f)); NativeMemoryBlock block = in.map(f.length()); System.out.println(block);
		 * 
		 * System.exit(0);
		 * 
		 * final Unsafe unsafe = MiscUtil.getUnsafe();
		 */

		// Library.doTest(Constants.BIG_HARRY_POTTER);

		/*
		 * ArrayList<File> files = new ArrayList<File>(); for (File file : new
		 * File("C:\\Users\\Troy Neubauer\\Desktop\\scan").listFiles()) files.add(file); files.add(new
		 * File("D:\\Minecraft\\1.2billion.txt")); wordTest(files.toArray(new File[files.size()]));
		 */

	}

	public static void wordTest(File... files) throws Exception {
		HashMap<Character, Integer> map = new HashMap<Character, Integer>();
		int counter = 0;
		for (File file : files) {
			long start = System.nanoTime();
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
			double seconds = (System.nanoTime() - start) / 1000000000.0;
			reader.close();
			System.out.println("Scanned " + counter + " characters. ");
			System.out.println(map.size() + " different characters");
			System.out.println("Results for file " + file + ":");
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
				System.out.println(key + " (" + ((int) key) + ") count " + value + " = "
						+ ((double) value / counter * 100.0) + "%");
			}
			map.clear();
		}

	}
}
