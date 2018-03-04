package test;

import java.io.*;

import com.troy.serialization.util.*;
import com.troyberry.util.StringFormatter;

public class Main {

	public static void main(String[] args)
			throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, InstantiationException, FileNotFoundException, IOException {
		SerializationUtils.init();
		boolean[] bools = new boolean[8];
		bools[0] = true;
		bools[5] = true;
		bools[7] = true;
		byte[] dest = new byte[(bools.length + 7) / 8];
		
		NativeUtils.booleansToBytesCompact(dest, bools, 0, 0, bools.length);
		System.out.println(StringFormatter.toBinaryString(dest));
		
		System.exit(0);
		/*System.out.println();

		FieldSerializer<TestSub> tst = new FieldSerializer<TestSub>(TestSub.class);
		int[] src = new int[1];
		src[0] = 0x12345678;
		byte[] dest = new byte[src.length * Integer.BYTES];

		long start;
		start = System.nanoTime();
		NativeUtils.intsToBytes(dest, src, 0, 0, src.length, true);
		System.out.println((System.nanoTime() - start) + " nanos for native");

		start = System.nanoTime();
		intsToBytes(dest, src, 0, 0, src.length, true);
		System.out.println((System.nanoTime() - start) + " nanos for java");
*/
		/*
		 * File file = new File("./test.dat");
		 * 
		 * NativeFileOutput n = new NativeFileOutput(file); long size = 1000; long address = MiscUtil.getUnsafe().allocateMemory(size); short[] array =
		 * new short[10]; array[0] = 10; array[1] = 10010; NativeUtils.shortArrayToNative(address, array, 0, array.length);
		 * System.out.println(MiscUtil.getUnsafe().getShort(address)); System.out.println(MiscUtil.getUnsafe().getShort(address +
		 * sun.misc.Unsafe.ARRAY_SHORT_INDEX_SCALE)); n.close();
		 */

		/*
		 * System.exit(0);
		 * 
		 * char[] src = new char[] { 'a', 'a', 'a', 'a' };
		 * 
		 * SerializationUtils.init(); ByteArrayOutput out = new ByteArrayOutput();
		 * 
		 * TroyCharsets.FOUR_BIT_ENCODING.encode(src, out, 0, src.length, true);
		 * 
		 * byte[] buf = Arrays.copyOf(out.getBuffer(), out.getBufferPosition());
		 * 
		 * new FileOutputStream(new File("test.dat")).write(buf);
		 * 
		 * System.out.println(StringFormatter.toBinaryString(buf));
		 * 
		 * /* char[] src = new char[] { 'T', 'r', 'o', 'y' , '!'};
		 * 
		 * 
		 * SerializationUtils.init();
		 * 
		 * ByteArrayOutput out = new ByteArrayOutput();
		 * 
		 * TroyCharsets.SIX_BIT_ENCODING.encode(src, out, 0, src.length, true);
		 * 
		 * byte[] buf = Arrays.copyOf(out.getBuffer(), out.getBufferPosition());
		 * 
		 * new FileOutputStream(new File("test.dat")).write(buf);
		 * 
		 * System.out.println(StringFormatter.toBinaryString(buf));
		 */
	}

	private static void intsToBytes(byte[] dest, int[] src, int srcOffset, int destOffset, int elements, boolean swapEndianess) {
		if (swapEndianess) {
			for (int i = 0; i < elements; i++) {
				int srcIndex = i + srcOffset;
				int obj = src[srcIndex];
				dest[destOffset + i * 4 + 0] = (byte) (obj & 0xFF000000);
				dest[destOffset + i * 4 + 1] = (byte) (obj & 0x00FF0000);
				dest[destOffset + i * 4 + 2] = (byte) (obj & 0x0000FF00);
				dest[destOffset + i * 4 + 3] = (byte) (obj & 0x000000FF);
			}
		} else {
			System.arraycopy(src, srcOffset, dest, destOffset, elements);
		}
	}

	/*
	 * String s = "tttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttt"; for(int i = 0; i < 21;
	 * i++) { s += s; } char[] source = s.toCharArray();
	 * 
	 * byte[] dest = new byte[(source.length + 3) / 2 - 1]; long start;
	 * 
	 * start = System.nanoTime(); Output out = new Output(new FileOutputStream(new File("kryo.dat"))); out.writeString(s); out.close();
	 * System.out.println("kryo " + ((System.nanoTime() - start) / 1000000.0));
	 * 
	 * start = System.nanoTime(); TroyCharsets.FOUR_BIT_ENCODING.encode(source, dest, 0, 0, source.length,
	 * SerializationUtils.CHECK_CHARSET_PROBLEMS); FileOutputStream stream = new FileOutputStream(new File("test.dat")); stream.write(dest);
	 * stream.close(); System.out.println("my time " + ((System.nanoTime() - start) / 1000000.0));
	 */

}
