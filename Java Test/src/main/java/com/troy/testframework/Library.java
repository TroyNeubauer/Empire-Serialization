package com.troy.testframework;

import java.io.*;
import java.util.*;

import com.esotericsoftware.kryo.*;
import com.esotericsoftware.kryo.io.Output;
import com.google.gson.*;
import com.troy.empireserialization.*;
import com.troy.empireserialization.io.out.*;
import com.troy.empireserialization.util.*;

public abstract class Library {
	public static List<Library> libs = new ArrayList<Library>();

	static {
		for (Class<?> clazz : Library.class.getDeclaredClasses()) {
			try {
				Library lib = (Library) clazz.newInstance();
				lib.name = clazz.getSimpleName();
				libs.add(lib);
			} catch (InstantiationException | IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}
	}

	public static void doTest(Object obj) {
		for (Library lib : libs) {
			lib.test(new File("./" + lib.name + ".dat"), obj);
		}
	}

	private String name;

	public void test(File file, Object obj) {
		System.out.println("Starting test for " + name);
		int samples = 20;
		long[] times = new long[samples];
		long start, end, size = 0;
		for (int i = 0; i < samples; i++) {
			file.delete();
			System.gc();
			start = System.nanoTime();
			writeObjectToFile(file, obj);
			end = System.nanoTime();
			size = file.length();
			/*
			 * if (lastBytes != null) { if (lastBytes.length != bytes.length) throw new RuntimeException(); } else { try {
			 * FileOutputStream stream = new FileOutputStream(new File("./" + getClass().getSimpleName() + ".dat"));
			 * stream.write(bytes); stream.close(); } catch (IOException e) { e.printStackTrace(); } } lastBytes = bytes;
			 */
			times[i] = end - start;
		}

		long average = Math.round(Arrays.stream(times).average().getAsDouble());
		long min = Arrays.stream(times).min().getAsLong();
		System.out.println("\tTook: " + StringFormatter.addCommas(average) + "ns on average.  Min:" + StringFormatter.addCommas(min) + " size: "
				+ StringFormatter.addCommas(size) + " bytes");
		System.out.println("\t" + StringFormatter.formatBytesLong((long) (size / (average / 1_000_000_000.0))) + " per second");
	}

	public abstract void writeObjectToFile(File file, Object obj);

	public abstract byte[] writeObjectToBytes(Object obj);

	public static class MyLibraryNative extends Library {
		public MyLibraryNative() {
			SerializationUtils.init();
		}

		@Override
		public void writeObjectToFile(File file, Object obj) {
			NativeUtils.NATIVES_ENABLED = true;
			OutputSerializationStream out = new OutputSerializationStream(new NativeFileOutput(file));
			out.writeObject(obj);
			out.close();
		}

		@Override
		public byte[] writeObjectToBytes(Object obj) {
			NativeUtils.NATIVES_ENABLED = true;
			NativeOutput out = new NativeOutput(4096);
			OutputSerializationStream stream = new OutputSerializationStream(out);
			stream.writeObject(obj);
			byte[] bytes = Arrays.copyOf(out.getBuffer(), out.getBufferPosition());

			stream.close();

			return bytes;
		}

	}

	public static class MyLibraryJava extends Library {
		public MyLibraryJava() {
			SerializationUtils.init();
		}

		@Override
		public void writeObjectToFile(File file, Object obj) {
			NativeUtils.NATIVES_ENABLED = false;
			OutputSerializationStream out = new OutputSerializationStream(new NativeFileOutput(file));
			out.writeObject(obj);
			out.close();
		}

		@Override
		public byte[] writeObjectToBytes(Object obj) {
			NativeUtils.NATIVES_ENABLED = false;
			NativeOutput out = new NativeOutput(4096);
			OutputSerializationStream stream = new OutputSerializationStream(out);
			stream.writeObject(obj);
			byte[] bytes = Arrays.copyOf(out.getBuffer(), out.getBufferPosition());

			stream.close();

			return bytes;
		}

	}

	public static class JavaSerialization extends Library {
		public JavaSerialization() {
		}

		@Override
		public void writeObjectToFile(File file, Object obj) {
			FileOutputStream stream = null;
			ObjectOutputStream out = null;
			try {
				stream = new FileOutputStream(file);
				out = new ObjectOutputStream(stream);
				out.writeObject(obj);

			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					out.close();
					stream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}

		@Override
		public byte[] writeObjectToBytes(Object obj) {
			ByteArrayOutputStream str = new ByteArrayOutputStream(4096);
			try {
				ObjectOutputStream out = new ObjectOutputStream(str);
				out.writeObject(obj);
				out.close();

			} catch (IOException e) {
				e.printStackTrace();
			}

			return str.toByteArray();
		}

	}

	public static class KryoLibrary extends Library {
		private Kryo k = new Kryo();

		public KryoLibrary() {

		}

		@Override
		public void writeObjectToFile(File file, Object obj) {
			try {
				Output out = new Output(new FileOutputStream(file));
				k.writeClassAndObject(out, obj);
				out.flush();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}

		@Override
		public byte[] writeObjectToBytes(Object obj) {
			Output out = new Output(4096, Integer.MAX_VALUE);
			k.writeClassAndObject(out, obj);
			return out.toBytes();
		}

	}

	public static class GSON extends Library {
		private Gson gson = new Gson();

		@Override
		public void writeObjectToFile(File file, Object obj) {
			FileWriter writer = null;
			try {
				writer = new FileWriter(file);
				writer.write(gson.toJson(obj));
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		@Override
		public byte[] writeObjectToBytes(Object obj) {
			return gson.toJson(obj).getBytes();
		}

	}

}
