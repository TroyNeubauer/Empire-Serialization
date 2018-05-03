package com.troy.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.troy.empireserialization.EmpireOutput;
import com.troy.empireserialization.io.out.ByteArrayOutput;
import com.troy.empireserialization.serializers.FieldSerializer;
import com.troy.empireserialization.util.ReflectionUtils;
import com.troy.empireserialization.util.StringFormatter;

public class Main {
	// C:\Empire Serialization\Java Natives\bin\x64\Release
	public static void main(String[] args) throws Throwable {
		init();

		ArrayList<Object> list = new ArrayList<Object>();

		Object[] objects = new Object[] { 5, "test", "test2", "test3" };
		ReflectionUtils.setData(list, objects);
		System.out.println(list);
		ByteArrayOutput bOut = new ByteArrayOutput();
		EmpireOutput out = new EmpireOutput(bOut);

		out.writeObject(list);
		
		ArrayList<String> list2 = new ArrayList<>();
		list2.add("test1");
		list2.add("test2");
		list2.add("test3");
		list2.add("test4");
		out.writeObject(list2);
		
		out.writeObject(list);

		System.out.println(StringFormatter.toHexString(bOut.getBuffer()));
		out.close();

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

	private static void init() {
		ArrayList<Object> list = new ArrayList<Object>();

		Object[] objects = new Object[] { 5, "test", "test2", "test3" };
		ReflectionUtils.setData(list, objects);
		System.out.println(list);

		FieldSerializer data = new FieldSerializer(list.getClass());
		ByteArrayOutput bOut = new ByteArrayOutput();
		EmpireOutput out = new EmpireOutput(bOut);
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
