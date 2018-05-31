package com.troy.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.esotericsoftware.kryo.Kryo;
import com.troy.empireserialization.EmpireOutput;
import com.troy.empireserialization.charset.EmpireCharsets;
import com.troy.empireserialization.io.in.NativeFileInput;
import com.troy.empireserialization.io.out.ByteArrayOutput;
import com.troy.empireserialization.io.out.NativeFileOutput;
import com.troy.empireserialization.util.ReflectionUtils;
import com.troy.empireserialization.util.StringFormatter;

public class Main {
	public static void main(String[] args) {
		arrayTest();
		System.exit(0);

	}

	// C:\Empire Serialization\Java Natives\bin\x64\Release
	public static void test() {
		System.out.println("e");
		NativeFileInput i = new NativeFileInput(new File("./out.emp"));
		i.readByte();
		char[] chars = new char[12];
		System.out.println(EmpireCharsets.SIX_BIT_CHARSET.decode(i, chars, 0, chars.length));

		System.out.println(StringFormatter.toHexString(i.readShort()));
		short[] shorts = new short[2];
		i.readShorts(shorts);
		System.out.println(StringFormatter.toHexString(shorts));
		i.close();

		System.exit(0);

		NativeFileOutput out = new NativeFileOutput(new File("./out.emp"));
		EmpireOutput emp = new EmpireOutput(out);
		emp.writeString("Test String!");
		emp.close();

		System.exit(0);
		init();

		Kryo k = new Kryo();
		TestSuper sup = new TestSuper(10, "test String");
		ClassA instance = new ClassA("Class A test!", 5);

		// k.writeClassAndObject(new Output(1000), instance);
		/*
		 * ByteArrayOutput out = new ByteArrayOutput(); EmpireOutput eo = new
		 * EmpireOutput(out); eo.writeTypeComplete(String.class); //
		 * eo.writeObject(instance); eo.writeArray(new String[] { "one", "one", "one",
		 * "one", "one" });
		 * System.out.println(StringFormatter.toBinaryString(out.getBuffer()));
		 * eo.close();
		 */
	}

	private static void arrayTest() {
		Object[] objects = new Object[3];
		objects[0] = new TestSuper(15, "test0");
		objects[1] = new TestSub(-1, "sub", 0xFF00FF);
		ByteArrayOutput oout = new ByteArrayOutput();
		EmpireOutput ooout = new EmpireOutput(oout);
		ooout.writeObject(objects);
		ooout.close();
	}

	private static void arrayListTest() {
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
	}

	private static void init() {
		ArrayList<Object> list = new ArrayList<Object>();

		Object[] objects = new Object[] { 5, "test", "test2", "test3" };
		ReflectionUtils.setData(list, objects);

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
				System.out.println(key + " (" + ((int) key) + ") count " + value + " = " + ((double) value / counter * 100.0) + "%");
			}
			map.clear();
		}

	}
}
