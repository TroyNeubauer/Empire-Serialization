package com.troy.test;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;
import com.google.gson.Gson;
import com.troy.empireserialization.EmpireOutput;
import com.troy.empireserialization.SerializationSettings;
import com.troy.empireserialization.charset.EmpireCharsets;
import com.troy.empireserialization.io.out.ByteArrayOutput;
import com.troy.empireserialization.io.out.NativeFileOutput;
import com.troy.empireserialization.util.StringFormatter;

public class ExcelBenchmarks {

	private static class D {
		private int[] ints = { 1, 2, 3, 4, 5 };
		private String[] strings = { "one", "two", "three" };
		private Object[] objects = { Integer.valueOf(4), "polys", Boolean.valueOf(false) };

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + Arrays.hashCode(ints);
			result = prime * result + Arrays.hashCode(objects);
			result = prime * result + Arrays.hashCode(strings);
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			D other = (D) obj;
			if (!Arrays.equals(ints, other.ints))
				return false;
			if (!Arrays.equals(objects, other.objects))
				return false;
			if (!Arrays.equals(strings, other.strings))
				return false;
			return true;
		}

	}

	static abstract class Test {
		private String name;
		protected long empireTime = -1, kryoTime = -1, gsonTime = -1, empireSize, kryoSize, gsonSize;

		Output kryoOut;
		EmpireOutput empireOut;
		FileWriter gsonOut;

		public Test(String name) throws Exception {
			this.name = name;
		}

		private static int compactDataRow = 30;

		public int doTest(Sheet sheet, int currentRow) throws Exception {
			System.gc();// Try to do gc because out results will be very flawed if we are counting time
						// while a gc happens
			kryoOut = new Output(new FileOutputStream(KRYO_FILE));
			empireOut = new EmpireOutput(new NativeFileOutput(EMPIRE_FILE), new SerializationSettings());
			gsonOut = new FileWriter(GSON_FILE);

			doTestImpl();

			empireOut.close();
			kryoOut.close();
			gsonOut.close();

			empireSize = EMPIRE_FILE.length();
			kryoSize = KRYO_FILE.length();
			gsonSize = GSON_FILE.length();

			Row row = sheet.createRow(currentRow++);
			row.createCell(0, CellType.STRING).setCellValue(name);

			currentRow = addRow(sheet, "Empire", empireTime, empireTime, currentRow, empireSize, empireSize);
			currentRow = addRow(sheet, "Kryo", kryoTime, empireTime, currentRow, kryoSize, empireSize);
			currentRow = addRow(sheet, "Gson", gsonTime, empireTime, currentRow, gsonSize, empireSize);

			compactDataRow++;
			Row comRow = sheet.createRow(compactDataRow++);
			comRow.createCell(0, CellType.STRING).setCellValue(name);
			comRow.createCell(1, CellType.STRING).setCellValue(colums[4]);
			comRow.createCell(2, CellType.STRING).setCellValue(name);
			comRow.createCell(3, CellType.STRING).setCellValue(colums[5]);

			addCompactRow(sheet, "Empire", empireTime, empireTime, empireSize, empireSize);
			addCompactRow(sheet, "Kryo", kryoTime, empireTime, kryoSize, empireSize);
			addCompactRow(sheet, "Gson", gsonTime, empireTime, gsonSize, empireSize);

			return currentRow;
		}

		private void addCompactRow(Sheet sheet, String name, long thisTime, long empireTime, long thisSize, long empireSize) {
			Row row = sheet.createRow(compactDataRow++);
			int col = 0;
			row.createCell(col++, CellType.STRING).setCellValue(name);
			row.createCell(col++, CellType.NUMERIC).setCellValue((double) thisTime / TESTS);
			row.createCell(col++, CellType.STRING).setCellValue(name);
			row.createCell(col++, CellType.NUMERIC).setCellValue((double) thisSize / TESTS);

		}

		private int addRow(Sheet sheet, String name, long thisTime, long empireTime, int currentRow, long thisSize, long empireSize) {
			Row row = sheet.createRow(currentRow++);
			int col = 1;
			row.createCell(col++, CellType.STRING).setCellValue(name);
			row.createCell(col++, CellType.NUMERIC).setCellValue(thisTime);
			row.createCell(col++, CellType.NUMERIC).setCellValue(thisSize);

			row.createCell(col++, CellType.NUMERIC).setCellValue((double) thisTime / TESTS);
			row.createCell(col++, CellType.NUMERIC).setCellValue((double) thisSize / TESTS);

			row.createCell(col++, CellType.NUMERIC).setCellValue((double) thisTime / empireTime);
			row.createCell(col++, CellType.NUMERIC).setCellValue((double) thisSize / empireSize);

			row.createCell(col++, CellType.NUMERIC).setCellValue((double) thisTime / TESTS / 1_000_000_000.0);
			return currentRow;
		}

		public abstract void doTestImpl() throws IOException;

	}

	public static final int TESTS = 10_000;
	public static final String[] colums = { "Test        ", "Library", "Total time (nanoseconds)", "Total file size (bytes)",
			"Average time (nanoseconds/object)", "Average size (bytes/object)", "X slower than Empire", "X larger than Empire",
			"Average time (seconds/object)" };

	public static Object newObject() {
		return new ClassA("name", (int) (Math.random() * 10_000));
	}

	public static final File EMPIRE_FILE = new File("./empire.dat"), KRYO_FILE = new File("kryo.dat"), GSON_FILE = new File("gson.dat");

	public static void main(String[] args) throws Exception {
		ByteArrayOutput o = new ByteArrayOutput();
		char[] chars = "I am fast!".toCharArray();
		EmpireCharsets.SIX_BIT_CHARSET.encode(chars, o, 0, chars.length, 0);
		System.out.println(StringFormatter.toBinaryString(Arrays.copyOf(o.getBuffer(), o.getBufferPosition())));
		System.exit(0);
		Kryo kryo = new Kryo();
		Gson gson = new Gson();

		warmup(kryo, gson);
		List<Test> tests = new ArrayList<Test>();
		tests.add(new Test("Best case") {

			@Override
			public void doTestImpl() throws IOException {
				long start, end;
				kryo.register(ClassA.class, 5);

				for (int i = 0; i < TESTS; i++) {
					Object a = newObject();
					start = System.nanoTime();
					empireOut.writeObject(a);
					end = System.nanoTime();
					empireTime += end - start;

					start = System.nanoTime();
					kryo.writeObject(kryoOut, a);
					end = System.nanoTime();
					kryoTime += end - start;

					start = System.nanoTime();
					gson.toJson(a, gsonOut);
					end = System.nanoTime();
					gsonTime += end - start;
				}
			}
		});

		tests.add(new Test("No String Cache") {

			@Override
			public void doTestImpl() throws IOException {
				long start, end;
				kryo.register(ClassA.class, 5);
				empireOut.getSettings().useStringCache = false;

				for (int i = 0; i < TESTS; i++) {
					Object a = newObject();
					start = System.nanoTime();
					empireOut.writeObject(a);
					end = System.nanoTime();
					empireTime += end - start;

					start = System.nanoTime();
					kryo.writeObject(kryoOut, a);
					end = System.nanoTime();
					kryoTime += end - start;

					start = System.nanoTime();
					gson.toJson(a, gsonOut);
					end = System.nanoTime();
					gsonTime += end - start;
				}
			}
		});

		tests.add(new Test("No String or Object Cache") {

			@Override
			public void doTestImpl() throws IOException {
				long start, end;
				kryo.register(ClassA.class, 5);
				empireOut.getSettings().useStringCache = false;
				empireOut.getSettings().useObjectCache = false;

				for (int i = 0; i < TESTS; i++) {
					Object a = newObject();
					start = System.nanoTime();
					empireOut.writeObject(a);
					end = System.nanoTime();
					empireTime += end - start;

					start = System.nanoTime();
					kryo.writeObject(kryoOut, a);
					end = System.nanoTime();
					kryoTime += end - start;

					start = System.nanoTime();
					gson.toJson(a, gsonOut);
					end = System.nanoTime();
					gsonTime += end - start;
				}
			}
		});

		tests.add(new Test("No Object Cache - Kryo") {

			@Override
			public void doTestImpl() throws IOException {
				long start, end;

				for (int i = 0; i < TESTS; i++) {
					Object a = newObject();
					start = System.nanoTime();
					empireOut.writeObject(a);
					end = System.nanoTime();
					empireTime += end - start;

					start = System.nanoTime();
					kryo.writeClassAndObject(kryoOut, a);
					end = System.nanoTime();
					kryoTime += end - start;

					start = System.nanoTime();
					gson.toJson(a, gsonOut);
					end = System.nanoTime();
					gsonTime += end - start;
				}
			}
		});

		Workbook wb = new XSSFWorkbook();
		Sheet sheet = wb.createSheet("1");

		int currentRow = 0;
		Row title = sheet.createRow(currentRow);
		for (int i = 0; i < colums.length; i++) {
			title.createCell(i, CellType.STRING).setCellValue(colums[i]);
			sheet.setColumnWidth(i, 200 * colums[i].length());
		}
		title.createCell(10, CellType.STRING).setCellValue("Number of tests = " + TESTS);
		currentRow++;
		System.out.println("Starting benchmarks");
		long benchStart = System.nanoTime();
		for (Test test : tests) {
			warmup(kryo, gson);// Try to keep things consistant
			long start = System.nanoTime();
			currentRow = test.doTest(sheet, currentRow) + 1;
			long end = System.nanoTime();
			double seconds = ((double) end - start) / 1_000_000_000.0;
			System.out.println("Done with benchmark \"" + test.name + "\" took " + seconds + " seconds");
		}
		long benchEnd = System.nanoTime();
		double benchSecs = ((double) benchEnd - benchStart) / 1_000_000_000.0;
		System.out.println("Done with benchmarks benchmarks took " + benchSecs + " seconds");

		File result = new File("result.xlsx");
		wb.write(new FileOutputStream(result));
		Desktop.getDesktop().open(result);
	}

	public class A {
		private int x;
		private B b;

		public A(int x, B b) {
			this.x = x;
			this.b = b;
		}
	}

	public class B {
		private int y;
		private A a;

		public B(int y, A a) {
			this.y = y;
			this.a = a;
		}
	}

	private static void warmup(Kryo kryo, Gson gson) throws IOException {
		Output kryoWarmup = new Output(new FileOutputStream(new File("kryoWarmup.dat")));
		EmpireOutput empireWarmup = new EmpireOutput(new File("meWarmup.dat"));
		FileWriter gsonWarmup = new FileWriter(new File("gsonWarmup.dat"));
		for (int i = 0; i < 10_000; i++) {
			Object a = newObject();
			empireWarmup.writeObject(a);
			kryo.writeObject(kryoWarmup, a);
			gson.toJson(a, gsonWarmup);
		}
		empireWarmup.close();
		kryoWarmup.close();
		gsonWarmup.close();
	}
}
